package ui.gui;

import java.util.List;
import java.util.Arrays;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.View;
import javax.swing.text.FieldView;
import javax.swing.text.Element;
import javax.swing.plaf.metal.MetalTextFieldUI;
import util.DataModel;

class ViewTextField extends ViewFormat {
  private AlphaTextField rendererPrototype;
  private AlphaTextField editorPrototype;
  private Object         rendererValue;
  private Object         editorValue;
  private boolean        isWidthVariable;
  
  ViewTextField (int columns, int horizontalAlignment, Font font, Color color, HighlightControl highlightControl, List<Format> formats) {
    super (formats);
    isWidthVariable = columns < 0;
    columns         = Math.abs (columns);
    rendererPrototype = newPrototype (columns, horizontalAlignment, font, color);
    editorPrototype   = newPrototype (columns, horizontalAlignment, font, color);
    setHighlightControl (highlightControl, rendererPrototype);
  }
  
  ViewTextField (int columns, int horizontalAlignment, Font font, Color color, HighlightControl highlightControl, Format format) {
    this (columns, horizontalAlignment, font, color, highlightControl, Arrays.asList (format));
  }
  
  class AlphaTextField extends JTextField {
    public AlphaTextField () {
      super ();
      setOpaque (false);
    }
    protected void paintComponent (java.awt.Graphics g) {
      g.setColor (getBackground ());
      g.fillRect (0, 0, getWidth (), getHeight ());
      super.paintComponent (g);
    }
  }
  
  AlphaTextField newPrototype (int columns, int horizontalAlignment, Font font, Color color) {
    AlphaTextField prototype;
    prototype = new AlphaTextField ();
    prototype.setColumns (columns);
    prototype.setHorizontalAlignment (horizontalAlignment);
    if (font!=null)
      prototype.setFont (font);
    if (color!=null)
      prototype.setForeground (color);
    Insets insets = prototype.getBorder ().getBorderInsets (prototype);
    if (insets.top >= 2 && insets.bottom >= 2) {
      insets.top -= 1;
      insets.bottom -= 1;
    }
    prototype.setBorder (new CompoundBorder (new LineBorder (UI.CELL_BORDER_COLOR,1), new EmptyBorder (insets.top-1,2,insets.bottom-1,2)));
    prototype.setBackground (UI.BACKGROUND_COLOR);
    // XXX Hack to fix alignment of monospace fields on Windows 7
    if (font.getFamily().equals ("Consolas")) {
      if (prototype.getUI() instanceof MetalTextFieldUI) {
        prototype.setUI (new MetalTextFieldUI() {
          @Override public View create (Element element) {
            return new FieldView (element) {
              @Override protected Shape adjustAllocation (Shape shape) {
                if (shape!=null) {
                  Rectangle bounds = shape.getBounds();
                  bounds = (Rectangle) super.adjustAllocation (bounds);
                  bounds.y += 1;
                  bounds.height -= 1;
                  return bounds;
                } else
                  return null;
              }
            };
          }
        });
      }
    }
    return prototype;
  }
  
  @Override
  void adjustFontSize (int increment) {
    adjustFontSize (rendererPrototype, increment);
    adjustFontSize (editorPrototype, increment);
  }
  
  private void adjustFontSize (JTextField textField, int increment) {
    Font font = textField.getFont ();
    textField.setFont (font.deriveFont (Float.valueOf (font.getSize()+increment)));
  }
  
  @Override
  int getWidth () {
    return getEditorPrototype ().getPreferredSize().width + 2;
  }
  
  @Override
  boolean isWidthVariable () {
    return isWidthVariable;
  }
  
  @Override
  void setWidth (int width) {
    rendererPrototype.setColumns     (width);
    rendererPrototype.setMinimumSize (rendererPrototype.getPreferredSize ());
    editorPrototype.setColumns       (width);
    editorPrototype.setMinimumSize   (editorPrototype.getPreferredSize ());
  }
  
  @Override
  boolean isEditable () {
    return true;
  }
  
  @Override
  JComponent getRendererPrototype () {
    return rendererPrototype;
  }
  
  @Override
  JComponent getEditorPrototype () {
    return editorPrototype;
  }
  
  @Override
  void setRendererPrototypeValue (Object value, boolean isSelected) {
    String text = valueToText (value, isSelected);
    rendererPrototype.setText (text);
    if (text.length () > rendererPrototype.getColumns ())
      rendererPrototype.setToolTipText (text);
    else
      rendererPrototype.setToolTipText (null);
    rendererValue = value;
  }
  
  @Override
  void setEditorPrototypeValue (Object value) {
    editorPrototype.setText (valueToText (value, true));
    editorValue = value;
  }
  
  @Override 
  Object getEditorPrototypeValue () {
    return textToValue (editorPrototype.getText (), editorValue);
  }
  
  @Override 
  String checkRendererPrototypeValidity () {
    return checkValidity (rendererPrototype.getText (), rendererValue);
  }
  
  @Override
  void setRendererPrototypeError (boolean isError) {
    Insets insets = rendererPrototype.getBorder ().getBorderInsets (rendererPrototype);
    if (isError) {
      rendererPrototype.setBorder (new CompoundBorder (new LineBorder (UI.ERROR_BORDER_COLOR,1), new EmptyBorder (insets.top-1,2,insets.bottom-1,2)));      
    } else {
      rendererPrototype.setBorder (new CompoundBorder (new LineBorder (UI.CELL_BORDER_COLOR,1), new EmptyBorder (insets.top-1,2,insets.bottom-1,2)));      
    }
  }
  
  @Override 
  String checkEditorPrototypeValidity () {
    return checkValidity (editorPrototype.getText (), editorValue);
  }
  
  @Override
  void setEditorPrototypeError (boolean isError) {
    Insets insets = editorPrototype.getBorder ().getBorderInsets (editorPrototype);
    if (isError) {
      editorPrototype.setBorder (new CompoundBorder (new LineBorder (UI.ERROR_BORDER_COLOR,1), new EmptyBorder (insets.top-1,2,insets.bottom-1,2)));      
    } else {
      editorPrototype.setBorder (new CompoundBorder (new LineBorder (UI.CELL_BORDER_COLOR,1), new EmptyBorder (insets.top-1,2,insets.bottom-1,2)));      
    }
  }
}