package ui.gui;

import java.util.List;
import java.util.Vector;
import java.util.Map;
import java.util.EnumMap;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import isa.AbstractAssembler;
import util.DataModel;
import util.DataModelEvent;

abstract class ViewFormat {
  private HighlightControl highlightControl = null;
  private List<Format>     formats;
  
  ViewFormat (List<Format> aFormats) {
    formats = aFormats;
  }
  
  protected void setHighlightControl (HighlightControl aHighlightControl, JComponent referenceComponent) {
    highlightControl = aHighlightControl;
    if (highlightControl != null)
      highlightControl.setReferenceComponent (referenceComponent);
  }
  
  protected String valueToText (Object value, boolean isSelected) {
    if (value != null) {
      for (Format f : formats)
	if (f.matches (value))
	  return f.valueToText (value, isSelected);
      try {
	if (isSelected)
	  try {
	    value = value.getClass ().getDeclaredMethod ("toSelectedString").invoke (value);	  
	  } catch (Exception e) {
	    isSelected = false;
	  }
	if (!isSelected)
	  value = value.getClass ().getDeclaredMethod ("toString").invoke (value);
	for (Format f : formats)
	  if (f.matches (value))
	    return f.valueToText (value, isSelected);
      } catch (Exception e) {}
      throw new ClassCastException (value.getClass ().getName ());
    } else
      return "";
  }
  
  protected Object textToValue (String text, Object value) {
    try {
      return formats.get (0).textToValue (text, value);
    } catch (Throwable t) {
      throw new RuntimeException (t);
    }
  }
  
  protected String checkValidity (String text, Object value) {
    return formats.get (0).checkValidity (text, value);
  }
  
  int getWidth () {
    return getRendererPrototype ().getMinimumSize ().width; 
  }
  
  boolean isWidthVariable () {
    return false;
  }
  
  void clearRendererPrototypeHighlight () {
    if (highlightControl != null) 
      highlightControl.clear (getRendererPrototype ());
  }
  
  void setRendererPrototypeHighlight (HighlightCell highlightCell) {
    if (highlightControl != null) 
      highlightControl.set (highlightCell, getRendererPrototype ());
  }
  
  void clearEditorPrototypeHighlight () {
    if (highlightControl != null) 
      highlightControl.clear (getEditorPrototype ());
  }
  
  void setEditorPrototypeHighlight (HighlightCell highlightCell) {
    if (highlightControl != null) 
      highlightControl.set (highlightCell, getEditorPrototype ());
  }
  
  void adjustFontSize (int increment) {
    ;
  }
  
  void setWidth (int width) {
    ;
  }
  
  boolean isEditable () {
    return false;
  }
  
  JComponent getEditorPrototype () {
    throw new AssertionError ("Format is not editable.");
  }
  
  void setEditorPrototypeValue (Object value) {
    throw new AssertionError ("Format is not editable.");
  }
				
  Object getEditorPrototypeValue () {
    throw new AssertionError ("Format is not editable.");
  }
  
  String checkRendererPrototypeValidity () {
    return null;
  }
  
  void setRendererPrototypeError (boolean isError) {
    ;
  }
  
  String checkEditorPrototypeValidity () {
    throw new AssertionError ("Format is not editable.");
  }
  
  void setEditorPrototypeError (boolean isError) {
    throw new AssertionError ("Format is not editable.");
  }  
  
  abstract void setRendererPrototypeValue (Object value, boolean isSelected);
  abstract JComponent getRendererPrototype      ();
  
  public static class FormatException extends RuntimeException {
    FormatException (String msg) {
      super (msg);
    }
    @Override
    public String toString () {
      return getMessage ();
    }
  }
  
  static class Format {
    protected Class      formatClass;
    protected String     formatString;

    Format (Class aClass, String aFormat) {
      formatClass  = aClass;
      formatString = aFormat;
    }
    boolean matches (Object value) {
      return formatClass.isInstance (value);
    }
    String valueToText (Object value, boolean isSelected) {
      return String.format (formatString, value);
    }
    Object textToValue (String text, Object value) throws Throwable {
      if (value.getClass () == String.class)
	return text;
      else try {
	value.getClass ().getDeclaredMethod ("setValue", String.class).invoke (value, text);
	return value;
      } catch (InvocationTargetException it) {
	throw (RuntimeException) it.getTargetException ();
      } catch (Exception e) {
	try {
	  return value.getClass ().getDeclaredMethod ("toValue", String.class).invoke (null, text);
	} catch (InvocationTargetException it) {
	  throw (RuntimeException) it.getTargetException ();
	} catch (Exception ee) {
	  throw new FormatException ("Invalid format.");
	}
      }
    }
    String checkValidity (String text, Object value) {
      try {
	textToValue (text, value);
	return null;
      } catch (Throwable e) {
	return e.toString ();
      }      
    }
  }
  
  static class NumberFormat extends Format {
    protected int formatRadix;
    NumberFormat (Class aClass, String aFormat, int aRadix) {
      super (aClass, aFormat);
      formatRadix = aRadix;
    }
    @Override
    String valueToText (Object value, boolean isSelected) {
      try {
	value = value.getClass().getDeclaredMethod ("toNumber").invoke (value);
      } catch (Exception e) {}
      return String.format (formatString, value);
    }
    @Override
    Object textToValue (String text, Object value) {
      try {
 	Class <?> valueClass = value.getClass ();
	if (formatRadix != 10) {
	  long num = Long.valueOf (text, formatRadix);
	  if (num > valueClass.getField ("MAX_VALUE").getLong (null))
	    num -= valueClass.getField ("MAX_VALUE").getLong (null) * 2 + 2;
	  if (valueClass==Byte.class)
	    return new Byte ((byte) num);
	  else if (valueClass==Short.class)
	    return new Short ((short) num);
	  else if (valueClass==Integer.class)
	    return new Integer ((int) num);
	  else
	    return new Long (num);
	} else
	  try {
	    value.getClass ().getDeclaredMethod ("setValue", String.class, int.class).invoke (value, text, formatRadix);
	    return value;
	  } catch (Exception e) {
            return valueClass.getDeclaredMethod ("valueOf", String.class, int.class).invoke (null, text, formatRadix);
 	  }
      } catch (Exception e) {
	throw new FormatException ("Invalid format.");
      }
    }
  }
  
  static class TwoIntegerFormat extends NumberFormat {
    TwoIntegerFormat (Class aClass, String aFormat, int aRadix) {
      super (aClass, aFormat, aRadix);
    }
    @Override
    String valueToText (Object value, boolean isSelected) {
      try {
	long number = ((Number) value).longValue ();
	return String.format (formatString, number >>> 32, (number << 32) >>> 32);	
      } catch (Exception e) {
	throw new FormatException ("Invalid format.");
      }
    }
  }
  
  static class HighlightControl {
    Map<DataModelEvent.Type,Highlight> highlights;
    HighlightControl (Highlight aRead, Highlight aWrite, Highlight aCursorSet, Highlight aCursorClear) {
      highlights = new EnumMap<DataModelEvent.Type,Highlight> (DataModelEvent.Type.class);
      highlights.put (DataModelEvent.Type.READ,         aRead);
      highlights.put (DataModelEvent.Type.WRITE,        aWrite);
      highlights.put (DataModelEvent.Type.CURSOR_SET,   aCursorSet);
      highlights.put (DataModelEvent.Type.CURSOR_CLEAR, aCursorClear);
    }
    static boolean isHighlightType (DataModelEvent.Type type) {
      return type == DataModelEvent.Type.READ || type == DataModelEvent.Type.WRITE || type == DataModelEvent.Type.CURSOR_SET || type == DataModelEvent.Type.CURSOR_CLEAR;
    }
    void setReferenceComponent (JComponent component) {
      for (DataModelEvent.Type type : DataModelEvent.Type.values ()) {
	Highlight highlight = highlights.get (type);
	if (highlight != null)
	  highlight.setReferenceComponent (component);
      }
    }
    void clear (JComponent component) {
      Highlight highlight = highlights.get (DataModelEvent.Type.READ);
      if (highlight != null)
	highlight.clear (component);
    }
    void set (HighlightCell cell, JComponent component) {
      Highlight highlight = highlights.get (cell.type);
      if (highlight != null)
	highlight.set (cell.isStart, cell.isEnd, cell.fadeFactor, component);
    }
  }
  
  static class HighlightCell {
    static final int    FADE_STEPS = 5;
    DataModelEvent.Type type;
    boolean             isStart;
    boolean             isEnd;
    double              fadeFactor = 1.0;
    HighlightCell (DataModelEvent.Type aType, boolean anIsStart, boolean anIsEnd) {
      type    = aType;
      isStart = anIsStart;
      isEnd   = anIsEnd;
    }
    boolean fade () {
      if (type != DataModelEvent.Type.CURSOR_SET && type != DataModelEvent.Type.CURSOR_CLEAR) {
	fadeFactor -= (1.0 / FADE_STEPS) * (fadeFactor==1.0? 3.1 : 1.0);
	return fadeFactor <= 0.0;
      } else 
	return false;
    }
  }
  
  interface Highlight {
    void setReferenceComponent (JComponent component);
    void clear (JComponent component);
    void set   (boolean isStart, boolean isEnd, double fadeFactor, JComponent component);
  }

  static class BorderHighlight implements Highlight {
    final int      width = 2;
    private Border clearBorder;
    private Color  color, background, clearBackground;
    
    BorderHighlight (Color aColor) {
      color           = aColor;
      background      = new Color (color.getRed(), color.getGreen(), color.getBlue(), 60);
      clearBorder     = null;
      clearBackground = null;
    }
    
    public void setReferenceComponent (JComponent component) {
      clearBorder     = component.getBorder ();
      clearBackground = component.getBackground ();
    }
    
    public void clear (JComponent component) {
      component.setBorder     (clearBorder);
      component.setBackground (clearBackground);
    }
    
    public void set (boolean isStart, boolean isEnd, double fadeFactor, JComponent component) {
      int l = isStart? width : 0;
      int r = isEnd? width : 0;
      Color bg = new Color (background.getRed (), background.getGreen (), background.getBlue (), (int) (background.getAlpha () * fadeFactor));
      Color bd = new Color (color.getRed(),       color.getGreen (),      color.getBlue(),       (int) (color.getAlpha () * fadeFactor));
      Color ln = new Color ((int) (200*(1.0-fadeFactor)+background.getRed ()*fadeFactor), 
			    (int) (200*(1.0-fadeFactor)+background.getGreen ()*fadeFactor), 
			    (int) (200*(1.0-fadeFactor)+background.getBlue ()*fadeFactor));
      component.setBackground (bg);  
      Insets insets = component.getBorder ().getBorderInsets (component);
      component.setBorder (new CompoundBorder (new CompoundBorder (new MatteBorder (1,l>0?1:0,1,r>0?1:0,ln), new MatteBorder (width-1,l>0?l-1:0,width-1,r>0?r-1:0,bd)), new EmptyBorder (insets.top-width,3-l,insets.bottom-width,3-r)));
     }    
  }
}
