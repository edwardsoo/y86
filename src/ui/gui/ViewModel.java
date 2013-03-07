package ui.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.EnumMap;
import java.util.Observer;
import java.util.Observable;
import javax.swing.table.AbstractTableModel;
import util.DataModel;
import util.DataModelEvent;
import util.TableCellIndex;

/**
 * TableModel built around a subrange of a DataModel, optionally grouping rows of the 
 * DataModel into a single row of the TableModel and supplying new names for the revised
 * columns.
 */

class ViewModel extends AbstractTableModel implements Observer {
  private DataModel                   dataModel;
  private int                         startingDataModelRowIndex;
  private int                         length;
  private List<Integer>               labelColumns;
  private List<Integer>               dataColumns;
  private int                         dataModelRowsPerTableRow;
  private List<String>                columnNames;
  private List<Listener>              listeners = new Vector<Listener> ();
  
  ViewModel (DataModel aDataModel, int aStartingDataModelRowIndex, int aLength, int aDataModelRowsPerTableRow, List<Integer> aLabelColumns, List<Integer> aDataColumns, List<String> aColumnNames) {
    dataModel                 = aDataModel;
    startingDataModelRowIndex = aStartingDataModelRowIndex;
    length                    = aLength;
    labelColumns              = aLabelColumns!=null? aLabelColumns : new Vector<Integer> ();
    dataColumns               = aDataColumns!=null? aDataColumns : new Vector<Integer> ();
    dataModelRowsPerTableRow  = aDataModelRowsPerTableRow;
    columnNames               = aColumnNames;
    dataModel.addObserver (this);
  }
  
  public int getStartingDataModelRowIndex () {
    return startingDataModelRowIndex;
  }
  
  public int getDataModelLength () {
    return length * dataModelRowsPerTableRow;
  }
  
  public void setLength (int len) {
    int oldLength = length;
    length = len;
    if (length > oldLength)
      fireTableRowsInserted (oldLength, length-1);
    else if (length < oldLength)
      fireTableRowsDeleted (length, oldLength-1);
  }
  
  public int length () {
    return length;
  }
  
  public boolean isLabelColumn (int col) {
    return col < labelColumns.size ();
  }
  
  public boolean insertRow (int row) {
    assert row >= 0 && row <= length*dataModelRowsPerTableRow;
    boolean inserted = false;
    for (int i=0; i<dataModelRowsPerTableRow; i++) {
      boolean ins = dataModel.insertRow (dataModelIndexFromTableIndex (row,0).rowIndex+i);
      assert ! inserted || ins;
      inserted = ins;
    }
    return inserted;
  }
  
  public boolean deleteRow (int row) {
    assert row >=0 && row < length*dataModelRowsPerTableRow;
    boolean deleted = false;
    for (int i=0; i<dataModelRowsPerTableRow; i++) {
      boolean del = dataModel.deleteRow (dataModelIndexFromTableIndex (row,0).rowIndex+i);
      assert ! deleted || del;
      deleted = del;
    }
    return deleted;
  }

  public TableCellIndex prevDataCell (TableCellIndex cell) {
    int rowIndex = cell.rowIndex;
    int colIndex = cell.columnIndex - 1;
    if (colIndex == labelColumns.size () - 1) {
      rowIndex -= 1;
      colIndex  = labelColumns.size () + dataColumns.size () * dataModelRowsPerTableRow - 1;
    }
    return (rowIndex == -1)? null : new TableCellIndex (rowIndex, colIndex);
  }
  
  public TableCellIndex nextDataCell (TableCellIndex cell) {
    int rowIndex = cell.rowIndex;
    int colIndex = cell.columnIndex + 1;
    if (colIndex == labelColumns.size () + dataColumns.size () * dataModelRowsPerTableRow) {
      rowIndex += 1;
      colIndex  = labelColumns.size ();
    }
    return (rowIndex >= length)? null : new TableCellIndex (rowIndex, colIndex);
  }
  
  private TableCellIndex tableIndexFromDataModelIndex (int modelRow, int modelCol) {
    int normalizedRow = modelRow - startingDataModelRowIndex;
    int tableRow      = normalizedRow / dataModelRowsPerTableRow;
    int tableCol;
    if (labelColumns.contains (modelCol))
      tableCol = labelColumns.indexOf (modelCol);
    else {
      assert dataColumns.contains (modelCol);
      tableCol = labelColumns.size() + dataColumns.indexOf (modelCol) + (normalizedRow % dataModelRowsPerTableRow) * dataColumns.size ();
    }
    return new TableCellIndex (tableRow, tableCol);
  }
  
  private TableCellIndex dataModelIndexFromTableIndex (int tableRow, int tableCol) {
    int modelRow = tableRow * dataModelRowsPerTableRow + startingDataModelRowIndex;
    int modelCol;
    if (tableCol < labelColumns.size ())
      modelCol           =  labelColumns.get (tableCol);
    else {
      int normalizedCol  =  tableCol - labelColumns.size ();
      modelRow           += normalizedCol / dataColumns.size ();
      modelCol           =  dataColumns.get (normalizedCol % dataColumns.size ());
    }
    return new TableCellIndex (modelRow, modelCol);
  }
  
  public void update (Observable o, Object arg) {
     DataModelEvent event = (DataModelEvent) arg;
    if (event.getType () == DataModelEvent.Type.CHANGING) {
      Vector <TableCellIndex> tableCells = new Vector <TableCellIndex> ();
      for (TableCellIndex modelCell : event.getCells ())
	if (dataColumns.contains (modelCell.columnIndex))
	  tableCells.add (tableIndexFromDataModelIndex (modelCell.rowIndex, modelCell.columnIndex));
      fireChangingModel (true, tableCells);
    } else if (event.getType () == DataModelEvent.Type.ROWS_INSERTED) {
      length += event.getCells ().get (0).columnIndex - event.getCells ().get (0).rowIndex + 1;
      fireTableRowsInserted (event.getCells ().get (0).rowIndex, event.getCells ().get (0).columnIndex);
    } else if (event.getType () == DataModelEvent.Type.ROWS_DELETED) {
      length -= event.getCells ().get (0).columnIndex - event.getCells ().get (0).rowIndex + 1;
      fireTableRowsDeleted (event.getCells ().get (0).rowIndex, event.getCells ().get (0).columnIndex);
    } else {
      List<TableCellIndex> tableCells = new Vector<TableCellIndex> ();
      for (TableCellIndex modelCell : event.getCells ()) {
	if (modelCell.rowIndex >= startingDataModelRowIndex && modelCell.rowIndex < startingDataModelRowIndex + length * dataModelRowsPerTableRow &&
	    (labelColumns.contains (modelCell.columnIndex) || dataColumns.contains (modelCell.columnIndex)))
	  tableCells.add (tableIndexFromDataModelIndex (modelCell.rowIndex, modelCell.columnIndex));
      }
      event = new DataModelEvent (event.getType (), tableCells);
      fireModelTouched (event);
      if (event.getType () == DataModelEvent.Type.WRITE || event.getType () == DataModelEvent.Type.WRITE_BY_USER || !listeners.isEmpty ()) {
	for (TableCellIndex tableCell : tableCells) {
	  assert tableCell.rowIndex >= 0 && tableCell.rowIndex < length;
	  assert tableCell.columnIndex >= 0 && tableCell.columnIndex < getColumnCount ();
	  fireTableCellUpdated (tableCell.rowIndex, tableCell.columnIndex);
	}
      }
    }
  }
  
  public DataModel getDataModel () {
    return dataModel;
  }
  
  public String getColumnName (int columnIndex) {
    if (columnNames != null && columnNames.get (columnIndex) != null)
      return columnNames.get (columnIndex);
    else
      return dataModel.getColumnName (dataModelIndexFromTableIndex (0, columnIndex).columnIndex);
  }
  
  public Class getColumnClass (int columnIndex) {
    return dataModel.getColumnClass (dataModelIndexFromTableIndex (0, columnIndex).columnIndex);
  }
  
  public int getRowCount () {
    return length;
  }
  
  public int getColumnCount () {
    return labelColumns.size () + dataColumns.size () * dataModelRowsPerTableRow;
  }
  
  public Object getValueAt (int rowIndex, int columnIndex) {
    TableCellIndex dataModelCell = dataModelIndexFromTableIndex (rowIndex, columnIndex);
    if (dataModelCell.rowIndex < dataModel.getRowCount ())
      return dataModel.getValueAt (dataModelCell.rowIndex, dataModelCell.columnIndex);
    else
      return null;
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex) {
    TableCellIndex dataModelCell = dataModelIndexFromTableIndex (rowIndex, columnIndex);
    if (dataModelCell.rowIndex < dataModel.getRowCount ())
      return dataModel.isCellEditable (dataModelCell.rowIndex, dataModelCell.columnIndex);
    else
      return false;
  }
  
  public void setValueAt (Object value, int rowIndex, int columnIndex) {
    TableCellIndex dataModelCell = dataModelIndexFromTableIndex (rowIndex, columnIndex);
    fireChangingModel (false, Arrays.asList (new TableCellIndex (rowIndex, columnIndex)));
    dataModel.setValueAtByUser (value, dataModelCell.rowIndex, dataModelCell.columnIndex);
  }
  
  static interface Listener {
    public void changingModel (boolean cancelEditing, List <TableCellIndex> cells);
    public void modelTouched (DataModelEvent event);
  }
  
  public void addListener (Listener l) {
    listeners.add (l);
  }
  
  public void fireModelTouched (DataModelEvent event) {
    for (Listener l : listeners)
      l.modelTouched (event);
  }
  
  public void fireChangingModel (boolean cancelEditing, List <TableCellIndex> cells) {
    for (Listener l : listeners)
      l.changingModel (cancelEditing, cells);
  }
}