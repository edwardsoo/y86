package ui.gui;

import java.lang.Math;
import java.util.List;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;
import java.util.ArrayList;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.undo.UndoableEdit;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import util.DataModel;
import util.DataModelEvent;
import util.TableCellIndex;

class View extends JTable implements ViewModel.Listener, UndoableEditListener {
  private MessageBoard                                   messageBoard;
  private List <ViewFormat>                              columnFormats;
  private Map <TableCellIndex, ViewFormat.HighlightCell> highlights;
  
  View (ViewModel aModel, MessageBoard aMessageBoard, List<ViewFormat> aColumnFormats) {
    super (aModel);
    getTableHeader ().setReorderingAllowed (false);
    messageBoard  = aMessageBoard;
    columnFormats = aColumnFormats;
    highlights    = new HashMap<TableCellIndex,ViewFormat.HighlightCell> ();
    setupColumns ();
    ((DefaultTableCellRenderer) getTableHeader ().getDefaultRenderer ()).setHorizontalAlignment (SwingConstants.CENTER);
    aModel.addListener (this);
    setColumnSelectionAllowed (true);
    addFocusListener (new FocusAdapter () {
      public void focusLost (FocusEvent e) {
	if (! contains (getComponents (), e.getOppositeComponent ())) {
	  boolean okayToClear;
	  if (getCellEditor () != null) 
	    okayToClear = getCellEditor ().stopCellEditing ();
	  else
	    okayToClear = true;
	  if (okayToClear)
	    clearSelection ();
        }
      }
    });
  }
		      
  private static boolean contains (Component[] c, Component t) {
    for (int i=0; i<c.length; i++)
      if (c[i].equals (t))
	return true;
      else if (c[i] instanceof Container)
	if (contains (((Container) c[i]).getComponents (), t))
	  return true;
    return false;
  }
  
  private void setupColumns () {
    TableModel       model       = getModel ();
    TableColumnModel columnModel = getColumnModel ();
    for (int i=0; i<model.getColumnCount (); i++) {
      TableColumn column       = columnModel.getColumn (i);
      ViewFormat  columnFormat = columnFormats.get (i);
      column.setPreferredWidth (columnFormat.getWidth ());
      if (!columnFormat.isWidthVariable()) {
        column.setMinWidth (columnFormat.getWidth ());
        column.setMaxWidth (columnFormat.getWidth ());
      }
      column.setCellRenderer (new CellRenderer ());
      if (columnFormat.isEditable ()) {
	JComponent prototype = columnFormat.getEditorPrototype ();
	if (prototype instanceof JTextField) {
	  JTextField prototypeTF = (JTextField) prototype;
	  column.setCellEditor (new CellEditor (prototypeTF, columnFormat));
	  prototypeTF.getDocument ().addUndoableEditListener (this);
	} else
	  column.setCellEditor (new CellEditor ((JCheckBox) prototype, columnFormat));
      }
    }
  }
  
  private void syncColumnWidths () {
    TableColumnModel columnModel = getColumnModel ();
    for (int i=0; i<getModel ().getColumnCount (); i++) {
      TableColumn column       = columnModel.getColumn (i);
      ViewFormat  columnFormat = columnFormats.get (i);
      if (!columnFormat.isWidthVariable()) {
        column.setMinWidth (columnFormat.getWidth ()); 
        column.setMaxWidth (columnFormat.getWidth ());
      }
      column.setPreferredWidth (columnFormat.getWidth ());
    }
  }
  
  public void modelTouched (DataModelEvent event) {
    ViewModel             model = (ViewModel) getModel ();
    List <TableCellIndex> cells = event.getCells ();
    synchronized (highlights) {
      for (TableCellIndex cell : cells) {
	boolean isStart = !cells.contains (model.prevDataCell (cell));
	boolean isEnd   = !cells.contains (model.nextDataCell (cell));
	DataModelEvent.Type type = event.getType ();
	if (ViewFormat.HighlightControl.isHighlightType (type) && 
	    (type != DataModelEvent.Type.CURSOR_CLEAR || highlights.get (cell) == null || highlights.get (cell).type == DataModelEvent.Type.CURSOR_SET)) {
	  highlights.put (cell, new ViewFormat.HighlightCell (event.getType(), isStart, isEnd));
        }
      }
    }
    int columnCount=0;
    for (int i=0; i<cells.size(); i++) {
      TableCellIndex cell     = cells.get (i);
      TableCellIndex nextCell = i+1<cells.size()? cells.get (i+1): null;
      if (nextCell!=null && cell.rowIndex==nextCell.rowIndex && cell.columnIndex==nextCell.columnIndex-1)
        columnCount++;
      else {
        fireAccess (event.getType(), cell.rowIndex, cell.columnIndex-columnCount, columnCount+1);
        columnCount=0;
      }
    }
  }
  
  public interface AccessListener {
    public enum Type {
      READ, WRITE, CURSOR_SET;
      static Type valueOf (DataModelEvent.Type t) {
        switch (t) {
          case READ:
            return READ;
          case WRITE:
            return WRITE;
          case CURSOR_SET:
            return CURSOR_SET;
          default:
            return null;
        }
     }
    }
    public void access (Type type, int rowIndex, int columnIndex, int columnCount);
  }
  
  private ArrayList <AccessListener> accessListeners = new ArrayList <AccessListener> ();
  
  public void addAccessListener (AccessListener l) {
    accessListeners.add (l);
  }
  
  private void fireAccess (DataModelEvent.Type type, int rowIndex, int columnIndex, int count) {
    for (AccessListener l : accessListeners)
      if (AccessListener.Type.valueOf (type) != null)
        l.access (AccessListener.Type.valueOf (type), rowIndex, columnIndex, count);
  }
  
  public void changingModel (boolean cancelEditing, List <TableCellIndex> cells) {
    if (cancelEditing) {
      TableCellEditor cellEditor = getCellEditor ();
      if (cellEditor != null)
	cellEditor.cancelCellEditing ();
    }
  }
  
  public void adjustHighlights (boolean clear) {
    Vector<TableCellIndex> changesToFire = new Vector<TableCellIndex> ();
    Vector<TableCellIndex> cellsToRemove = new Vector<TableCellIndex> ();
    synchronized (highlights) {
      for (Map.Entry<TableCellIndex,ViewFormat.HighlightCell> mapEntry : highlights.entrySet ()) {
	changesToFire.add (mapEntry.getKey ());
	if (! clear)
	  if (mapEntry.getValue ().fade ())
	    cellsToRemove.add (mapEntry.getKey ());
      }
      if (clear)
	highlights.clear ();
      else 
	for (TableCellIndex cell : cellsToRemove)
	  highlights.remove (cell);
    }
    for (TableCellIndex cell : changesToFire)
      ((AbstractTableModel) getModel()).fireTableCellUpdated (cell.rowIndex, cell.columnIndex);
  }
  
  public void adjustFontSize (int increment) {
    for (ViewFormat columnFormat : columnFormats) 
      columnFormat.adjustFontSize (increment);
    syncColumnWidths ();
    setRowHeight (getRowHeight () + increment);
    tableChanged (new TableModelEvent (getModel ()));
  }
  
  public void setColumnWidth (int columnIndex, int width) {
    columnFormats.get (columnIndex).setWidth (width);
    syncColumnWidths ();
    tableChanged (new TableModelEvent (getModel ()));
    TableColumnModel columnModel = getColumnModel ();
  }
  
  public ViewFormat getColumnFormat (int columnIndex) {
    return columnFormats.get (columnIndex);
  }
  
  public boolean insertAboveSelection () {
    int row = getSelectedRow ();
    int col = getSelectedColumn ();
    if (row == -1 ) {
      row = getEditingRow ();
      col = getEditingColumn ();
    }
    if (row < 0 || col < 0)
      return false;
    boolean inserted = ((ViewModel) getModel ()).insertRow (row);
    return inserted;
  }
  
  public boolean insertBelowSelection () {
    int row = getSelectedRow ();
    int col = getSelectedColumn ();
    if (row == -1) {
      row = getEditingRow ();
      col = getEditingColumn ();
    }
    if (row < 0 || col < 0)
      return false;
    boolean inserted = ((ViewModel) getModel ()).insertRow (row+1);
    changeSelection (row+1, col, false, false);
    return inserted;
  }
  
  public boolean deleteSelection () {
    int row = getSelectedRow ();
    int col = getSelectedColumn ();
    if (row == -1 ) {
      row = getEditingRow ();
      col = getEditingColumn ();
    }
    if (row < 0 || col < 0)
      return false;
    ViewModel model = ((ViewModel) getModel ());
    boolean deleted;
    if (model.getRowCount () > 1) {
      deleted = model.deleteRow (row);
      if (row == model.getRowCount ())
	row -= 1;
      changeSelection (row, col, false, false);
    } else
      deleted = false;
    return deleted;
  }
  
  private void setBorderSelected (JComponent component) {
    Insets insets   = component.getBorder ().getBorderInsets (component);
    int insetTop    = insets.top-2;
    int insetBottom = insets.bottom-2;
    if (insetTop >= 0 && insetBottom >= 0)
      component.setBorder (new CompoundBorder (new CompoundBorder (new LineBorder (UI.SELECTION_COLOR, 1), 
								   new LineBorder (UI.SELECTION_COLOR.brighter (), 1)), 
					       new EmptyBorder (insetTop,1,insetBottom,1)));
    else
      component.setBorder (new CompoundBorder (new LineBorder (UI.SELECTION_COLOR, 1), 
					       new EmptyBorder (insetTop+1,2,insetBottom+1,2)));	      
  }
  
  public class CellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
      ViewFormat columnFormat = columnFormats.get (columnIndex);
      JComponent component    = columnFormat.getRendererPrototype ();
      columnFormat.setRendererPrototypeValue (value, isSelected);
      if (!((ViewModel) getModel ()).isLabelColumn (columnIndex)) {
	synchronized (highlights) {
	  TableCellIndex cell = new TableCellIndex (rowIndex, columnIndex);
	  ViewFormat.HighlightCell hc = highlights.get (cell);
	  columnFormat.clearRendererPrototypeHighlight ();
	  if (hc != null)
	    columnFormat.setRendererPrototypeHighlight (hc);
	}
	if (isCellEditable (rowIndex, columnIndex) && columnFormat.checkRendererPrototypeValidity () != null)
	  columnFormat.setRendererPrototypeError (true);
      }
      fireSelectionMayHaveChanged (isSelected);
      return component;
    }
  }
  
  public class CellEditor extends DefaultCellEditor {
    ViewFormat format;
    
    public CellEditor (JTextField prototype, ViewFormat aFormat) {
      super (prototype);
      format = aFormat;
    }
    
    public CellEditor (JCheckBox prototype, ViewFormat aFormat) {
      super (prototype);
      format = aFormat;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int columnIndex) {
      if (!((ViewModel) getModel ()).isLabelColumn (columnIndex))
	synchronized (highlights) {
	  TableCellIndex cell = new TableCellIndex (rowIndex, columnIndex);
	  ViewFormat.HighlightCell hc = highlights.get (cell);
	  format.clearEditorPrototypeHighlight ();
	  if (hc != null)
	    format.setEditorPrototypeHighlight (hc);
	}
      JComponent component = format.getEditorPrototype ();
      setBorderSelected (component);
      fireSelectionMayHaveChanged (true);
      format.setEditorPrototypeValue (value);
      return component;
    }
    
    @Override
    public Object getCellEditorValue () {
      return format.getEditorPrototypeValue ();
    }
    
    @Override
    public boolean stopCellEditing () {
      String errorMessage = format.checkEditorPrototypeValidity ();
      if (errorMessage != null) {
	format.setEditorPrototypeError (true);
	messageBoard.showMessage (errorMessage);
	return false;
      } else {
	format.setEditorPrototypeError (false);
	messageBoard.showMessage ("");
	return super.stopCellEditing ();
      }
    }
    
    @Override
    public void cancelCellEditing () {
      messageBoard.showMessage ("");
      super.cancelCellEditing ();
    }
  }
  
  @Override public void tableChanged (TableModelEvent e) {
    super.tableChanged (e);
    if (e.getType () == TableModelEvent.INSERT || e.getType () == TableModelEvent.DELETE) {
      fireSizeChanged ();
      changeSelection (getSelectedRow (), getSelectedColumn (), false, false);
    }
  }
  
  public interface SizeChangedListener {
    void sizeChanged ();
  }
  
  Vector <SizeChangedListener> sizeChangedListeners = new Vector <SizeChangedListener> ();
  
  public void addSizeChangedListener (SizeChangedListener l) {
    sizeChangedListeners.add (l);
  }
  
  private void fireSizeChanged () {
    for (SizeChangedListener l : sizeChangedListeners)
      l.sizeChanged ();
  }
  
  @Override public boolean getScrollableTracksViewportHeight () {
    return true;
  }
  
  /**
   * Selection listeners receive upcall each time the model's selelection status
   * may have changed due to cell being selected, edited or de-selected.  Receiver
   * of event must check status to determine if change really occured and what the
   * current status is.
   */
  
  public interface SelectionListener {
    void selectionMayHaveChanged (boolean isKnownToBeSelected);
  }
  
  private Vector<SelectionListener> selectionListeners = new Vector<SelectionListener> ();
  
  public void addSelectionListener (SelectionListener l) {
    selectionListeners.add (l);
  }
  
  private void fireSelectionMayHaveChanged (boolean isKnownToBeSelected) {
    for (SelectionListener l : selectionListeners) 
      l.selectionMayHaveChanged (isKnownToBeSelected);
  }
  
  /////////////////////
  // Undo Support 
  
  Vector <UndoableEditListener> undoableEditListeners = new Vector <UndoableEditListener> ();
  
  public void addUndoableEditListener (UndoableEditListener l) {
    undoableEditListeners.add (l);
  }
  
  public void removeUndoableEditListeners (UndoableEditListener l) {
    undoableEditListeners.remove (l);
  }
  
  public void undoableEditHappened (UndoableEditEvent e) {
    ViewEdit ve = new ViewEdit (e.getEdit ());
    if (ve.canUndo ())
      for (UndoableEditListener l : undoableEditListeners)
	l.undoableEditHappened (new UndoableEditEvent (e.getSource (), ve));
  }
  
  class ViewEdit implements UndoableEdit {
    UndoableEdit baseEdit;
    int          rowIndex;
    int          columnIndex;
    ViewEdit (UndoableEdit anEdit) {
      baseEdit    = anEdit;
      rowIndex    = getEditingRow ();
      columnIndex = getEditingColumn ();
    }
    public boolean addEdit (UndoableEdit anEdit) {
      return baseEdit.addEdit (anEdit);
    }
    public boolean canRedo () {
      return rowIndex >= 0 && columnIndex >= 0 && baseEdit.canRedo ();
    }
    public boolean canUndo () {
      return rowIndex >= 0 && columnIndex >= 0 && baseEdit.canUndo ();
    }
    public void die () {
      baseEdit.die ();
    }
    public String getPresentationName () {
      return baseEdit.getPresentationName ();
    }
    public String getRedoPresentationName () {
      return baseEdit.getRedoPresentationName ();
    }
    public String getUndoPresentationName () {
      return baseEdit.getUndoPresentationName ();
    }
    public boolean isSignificant () {
      return baseEdit.isSignificant ();
    }
    public void redo () {
      boolean proceed;
      if (rowIndex == getEditingRow() && columnIndex == getEditingColumn ())
	proceed = true;
      else 
	proceed = editCellAt (rowIndex, columnIndex);
      if (proceed)
	baseEdit.redo ();
    }
    public boolean replaceEdit (UndoableEdit anEdit) {
      return baseEdit.replaceEdit (anEdit);
    }
    public void undo () {
      boolean proceed;
      if (rowIndex == getEditingRow() && columnIndex == getEditingColumn ())
	proceed = true;
      else 
	proceed = editCellAt (rowIndex, columnIndex);
      if (proceed)
	baseEdit.undo ();
    }
  }
    
}