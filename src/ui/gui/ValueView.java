package ui.gui;

import java.util.Vector;
import java.util.Arrays;
import java.util.Map;
import java.util.Observer;
import java.util.Observable;
import util.AbstractDataModel;
import util.TableCellIndex;
import util.DataModel;
import util.DataModelEvent;
import util.MapModel;

class ValueView extends AbstractDataModel implements Observer {
  DataModel       baseModel;
  int             baseColumnIndex;
  MapModel        mapModel;
  String          stringName;
  Vector <Object> lastRequestedValue;
  
  ValueView (DataModel aBaseModel, int aBaseColumnIndex, MapModel aMapModel, String aStringName) {
    baseModel       = aBaseModel;
    baseColumnIndex = aBaseColumnIndex;
    mapModel        = aMapModel;
    stringName      = aStringName;
    lastRequestedValue = new Vector<Object> ();
    lastRequestedValue.setSize (getRowCount ());
    baseModel.addObserver (this);
    mapModel.addObserver  (this);
  }
  
  public class Value {
    Integer value;
    Value (Integer aValue) {
      value = aValue;
    }
    public String toString () {
      return value.toString ();
    }
    public Integer toNumber () {
      return value;
    }
    public void setValue (String text) {
      setValue (text, 10);
    }
    public void setValue (String text, int radix) {
      if (text.matches ("\\s*-?[0-9]+\\s*"))
	value = Integer.valueOf (text, radix);
      else if (text.matches ("\\s*-?0x[0-9,a-f,A-F]+\\s*"))
	value = Integer.valueOf (text.split ("0x",2)[1], 16);
      else {
	Integer v = (Integer) mapModel.reverseGet (text);
	if (v != null)
	  value = v;
	else
	  throw new NumberFormatException ();
      }
    }
  }
  
  public void update (Observable o, Object arg) {
    if (o == baseModel) {
      DataModelEvent event = (DataModelEvent) arg;
      tellObservers (new DataModelEvent (event.getType (), Arrays.asList (new TableCellIndex (event.getRowIndex(), 0), new TableCellIndex (event.getRowIndex(), 1))));
    } else if (o == mapModel) {
      for (int row=0; row<getRowCount(); row++) {
	Object key       = baseModel.getValueAt (row, baseColumnIndex);
	Object value     = mapModel.get (key);
	Object lastValue = lastRequestedValue.get (row);
	if (((lastValue == null || value == null) &&  lastValue != value) || (value != null && lastValue != null && ! value.equals (lastValue))) {
	  lastRequestedValue.set (row, value);
	  tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, row, 1));
	}
      }
    } 
  }
  
  public Class getColumnClass (int columnIndex) {
    if (columnIndex==0)
      return Integer.class;
    else if (columnIndex==1)
      return String.class;
    else
      throw new AssertionError ();
  }
  
  public int getColumnCount () {
    return 2;
  }
  
  public String getColumnName  (int columnIndex) {
    if (columnIndex==0)
      return "As Int";
    else if (columnIndex==1)
      return "As ".concat (stringName);
    else
      throw new AssertionError ();
  }
  
  public int getRowCount () {
    return baseModel.getRowCount ();
  }
  
  public Object getValueAt (int rowIndex, int columnIndex) {
    if (columnIndex==0)
      return new Value ((Integer) baseModel.getValueAt (rowIndex, baseColumnIndex));
    else if (columnIndex==1) {
      Object value = mapModel.get (baseModel.getValueAt (rowIndex, baseColumnIndex));
      return value!=null? value : "";
    } else 
      throw new AssertionError ();
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex) {
    return columnIndex==0;
  }
  
  public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
    assert columnIndex==0;
    baseModel.setValueAtByUser (((Value) aValue).toNumber (), rowIndex, baseColumnIndex);
  }
}