package ui.gui;

import java.util.List;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JCheckBox;
import javax.swing.plaf.metal.MetalCheckBoxIcon;
import util.DataModel;

class ViewCheckBox extends ViewFormat {
  private JCheckBox rendererPrototype;
  private JCheckBox editorPrototype;
  private Color     checkColor;
  
  ViewCheckBox (Color aCheckColor) {
    super (null);
    checkColor        = aCheckColor;
    rendererPrototype = new JCheckBox (new CheckBoxIcon ());
    rendererPrototype.setBackground (UI.BACKGROUND_COLOR);
    editorPrototype   = new JCheckBox (new CheckBoxIcon ());
  }
  
  class CheckBoxIcon extends MetalCheckBoxIcon {
    protected int getControlSize () {
      return 9;
    }
    protected void drawCheck (Component c, Graphics g, int x, int y) {
      int controlSize = getControlSize ();
      g.setColor (checkColor);
      g.fillRect(x+1, y+1, controlSize-2, controlSize-2);
    }
  }
  
  @Override
  int getWidth () {
    return Math.max (super.getWidth (), getEditorPrototype ().getPreferredSize().width + 2);
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
    rendererPrototype.setSelected ((Boolean) value);
  }
  
  @Override
  void setEditorPrototypeValue (Object value) {
    editorPrototype.setSelected ((Boolean) value);
  }
  
  @Override 
  Object getEditorPrototypeValue () {
    return editorPrototype.isSelected ();
  }
  
  @Override 
  String checkEditorPrototypeValidity () {
    return null;
  }
  
  @Override
  void setEditorPrototypeError (boolean isError) {
    ;
  }
}