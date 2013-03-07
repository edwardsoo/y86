package ui.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;
import java.lang.reflect.InvocationTargetException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Component;
import java.awt.Window;
import java.awt.Font;
import java.awt.EventQueue;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Shape;
import java.awt.Polygon;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Point2D;
import java.awt.font.GlyphVector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import isa.Memory;


/**
 * Animation engine.
 */

class Animator {
  
  private final static boolean AGGRESSIVE_GC_TO_SMOOTH_ANIMATION  = true;
  private final static Color   READ_TARGET_COLOR                  = new Color (120,120,240,125);
  private final static Color   WRITE_TARGET_COLOR                 = new Color (240,120,120,125);
  private final static Color   CPU_COLOR                          = new Color (40,40,40,210);
  private final static Color   CPU_LABEL_COLOR                    = new Color (255,255,255,200);
  private final static Point   CPU_BLOCK_START                    = new Point (4,-1);
  private final static int     CPU_BLOCK_WIDTH                    = 116;
  private final static int     CPU_BLOCK_HEIGHT                   = 21;
  private final static int     CPU_BLOCK_SPACER                   = 4;
  private final static int     CPU_BLOCK_ARCWIDTH                 = 12;
  private final static int     CPU_BLOCK_ARCHEIGHT                = 12;
  private final static Color   ADDRESS_BLOCK_COLOR                = new Color (100,240,100,75);
  private final static Color   CONTROL_FLOW_COLOR                 = new Color (100,240,100,245);
  private final static int     CONTROL_FLOW_FROM_OFFSET           = 6;
  private final static int     CONTROL_FLOW_TO_OFFSET             = 7;
  private final static int     CONTROL_FLOW_LINE_WIDTH            = 7;  // should be odd
  private final static int     CONTROL_FLOW_LINE_STAGGER          = 11;
  private final static int     ARROW_WIDTH                        = 9;
  private final static int     ARROW_HEIGHT                       = 21; // should be odd
  private final static int     SCENE_PAUSE                        = 16;
  
  private JFrame                             frame;
  private Memory                             memory;
  private Component                          cpu;
  private Point                              cpuPos               = new Point (CPU_BLOCK_START);
  private int                                duration;
  private Font                               font;
  private JComponent[]                       paintFirst;
  private BlockList                          cpuBlocks            = new BlockList();
  private boolean                            isEnabled            = false;
  private boolean                            isStopped            = false;
  private boolean                            isPaused             = false;
  private Scene                              activeScene          = null;
  private Overlay                            clearScene           = new Overlay();
  /** Map row to view for handling multiple context Memory/Register animations */
  private HashMap <Integer, View>            rowRenderedView      = new HashMap <Integer, View> ();
  /** Control flow queue for each context */
  private HashMap <JComponent, Position>     cfPosition           = new HashMap <JComponent, Position> ();
  private HashMap <JComponent, Position>     cfStartPosition      = new HashMap <JComponent, Position> ();
  /** List pending control-flow targets for each context */

  
  /**
   * Constructor
   */
  
  Animator (JFrame aFrame, Memory aMemory, Component aCpu, int aDuration, Font aFont, JComponent ... aPaintFirst) {
    frame=aFrame; memory=aMemory; cpu=aCpu; duration=aDuration; font=aFont; paintFirst=aPaintFirst;
  }
  
  /**
   * Argument Type Enums
   */
  
  static enum Target {REGISTER,MEMORY,INSTRUCTIONS};
  static enum Type {
    CPU_TO_TARGET, TARGET_TO_CPU, CONTROL_FLOW;
    static Type valueOf (View.AccessListener.Type t) {
      switch (t) {
        case READ:
          return TARGET_TO_CPU;
        case WRITE:
          return CPU_TO_TARGET;
        case CURSOR_SET:
          return CONTROL_FLOW;
        default:
          return null;
      }
    }
  } 
  
  /**
   * Position as a View, Row pair
   */
  private class Position {
    final public View    view;
    final public Integer row;
    public Position (View aView, Integer aRow) {
      view=aView; row=aRow;
    }
  }
  
  /**
   * Synchronized, searchable list of Blocks. 
   */
  
  private class BlockList extends ArrayList <Block> {
    
    synchronized Block findLast (int value) {
      for (int i=size()-1; i>=0; i--) {
        Block block = get(i);
        if (block.value==value)
          return block;
      }
      return null;
    }
    
    @Override synchronized public void clear () {
      for (Block b : this)
        b.clear();
      super.clear();
    }
    
    @Override synchronized public boolean add (Block b) {
      return super.add (b);
    }
    
    @Override synchronized public boolean contains (Object o) {
      return super.contains (o);
    }
    
    @Override synchronized public boolean isEmpty() {
      return super.isEmpty();
    }
  }
  
  /**
   * Animation stage timing.
   */
  
  private enum Stage {
    FADE_IN               (0.2), 
    ACTION                (0.5), 
    HOLD                  (0.1), 
    FADE_OUT              (0.3),
    CONTROL_FLOW_LINE     (0.4);
    
    final double durationWeight;
    
    Stage (double aDurationWeight) {
      durationWeight = aDurationWeight;
    }
  }
  
  /**
   * Determine number of animation steps for specified stage.
   */
  
  private int stageSteps (Stage s) {
    return (int) (duration * s.durationWeight + 0.5) / SCENE_PAUSE;
  }  
  
  /**
   * Convert rectangle to animation coordinates.
   */
  
  private Rectangle convertRectangle (Component source, Rectangle rectangle) {
    return SwingUtilities.convertRectangle (source, rectangle, frame.getGlassPane());
  }
  
  /**
   * Conver point to animation coordinates.
   */
  
  private Point convertPoint (Component source, Point point) {
    return SwingUtilities.convertPoint (source, point, frame.getGlassPane());
  }
  
  /**
   * XXX
   */
  
  private static Point clipPointToRect (Rectangle rect, Point point) {
    int x, y;
    x = Math.min (rect.x+rect.width-1,  Math.max (rect.x, point.x));
    y = Math.min (rect.y+rect.height-1, Math.max (rect.y, point.y));
    return new Point (x, y);
  }
  
  /**
   * Top-level animation scene interface.
   */
  
  private interface Scene {
    void render ();
    void stop   ();
    void pause  ();
    void resume ();
  }
  
  /**
   * Runable version of a scene.  Scene genration runs on the EDT, but
   * scene rendering runs on the current thread.  The rendering itself is
   * performed using an EDT timer so the actual rendering work is on the EDT.
   *
   * Call to render() blocks until rendering completes.
   */
  
  private abstract class SceneWorker implements Runnable, Scene {
    private Clip clip=null;
    @Override public void render() {
      if (EventQueue.isDispatchThread())
        run();
      else
        try {
          EventQueue.invokeAndWait (this);
        } catch (InterruptedException e) {
          throw new AssertionError (e);
        } catch (InvocationTargetException e) {
          if (e.getTargetException() instanceof RuntimeException)
            throw (RuntimeException) e.getTargetException();
          else
            throw new AssertionError (e.getTargetException());
        }
      if (clip!=null) {
        if (isPaused && !isStopped)
          clip.pause();
        clip.render(); 
      }
    }
    void setClip (Clip aClip) {
      clip = aClip;
    }
    @Override public void stop () {
      if (clip!=null)
        clip.stop();
    }
    @Override public void pause () {
      if (clip!=null)
        clip.pause ();
    }
    @Override public void resume () {
      if (clip!=null)
        clip.resume();
    }
  }
  
  /**
   * Base class of all animation clips.
   */
  
  private abstract class Clip implements Scene {
    private Timer   timer = null;
    private boolean isMoreToRender;
    private boolean isStopped = false;;
    private boolean isPaused  = false;
    
    /**
     * Render the clip.
     */
    
    @Override public void render () {
      if (EventQueue.isDispatchThread()) {
        while (renderNextFrame())
          try {Thread.sleep (SCENE_PAUSE);} catch (InterruptedException e) {}
      } else {
        synchronized (this) {
          try {
            if (!isStopped) {
              isMoreToRender = true;
              do {
                if (AGGRESSIVE_GC_TO_SMOOTH_ANIMATION)
                  System.gc();
                timer = new Timer (SCENE_PAUSE, new ActionListener () {
                  @Override public void actionPerformed (ActionEvent e) {
                    if (isPaused) 
                      ((Timer) e.getSource()).stop();
                    else {
                      isMoreToRender = renderNextFrame();
                      if (!isMoreToRender) {
                        ((Timer) e.getSource()).stop();
                        synchronized (Clip.this) {
                          Clip.this.notifyAll();
                        }
                      }
                    }
                  }
                });
                timer.start();
                try {wait();} catch (InterruptedException e) {}
              } while (isMoreToRender && !isStopped);
            }
          } finally {
            timer=null;
            notifyAll();
          }
        }
      }
    }
    
    /**
     * Stop rendering immediately and clean up.
     */
    
    @Override public void stop () {
      synchronized (this) {
        if (timer!=null) 
          timer.stop();
        isStopped = true;
        isPaused  = false;
        notifyAll();
        while (timer!=null)
          try {wait();} catch (InterruptedException e) {}
      }
    }
    
    /**
     * Pause rendering process. 
     */
    
    @Override public void pause () {
      synchronized (this) {
        isPaused = true;
      }
    }
    
    /**
     * Resume paused rendering.
     */
    
    @Override public void resume () {
      synchronized (this) {
        isPaused = false;
        notifyAll();
      }
    }
    
    /**
     * Called when rendering finishes.  Subclasses can override to preform custom cleanup.
     */
    
    public void clear () {}
    
    /**
     * Return an optional clip to animate the clearing of the scene.  Subclasses can override.
     */
    
    public Clip getClearClip () {return null;}
    
    /**
     * Render the next frame of the clip.  Implemented by subclasses.
     * @return true iff there are more frames to render.
     */
    
    abstract boolean renderNextFrame ();
  }
  
  /**
   * Collection of clips.  Rendering order is determined by subclasses.
   */
  
  private abstract class Composite extends Clip {
    ArrayList <Clip> clips = new ArrayList <Clip> ();
    synchronized void add (Clip aClip) {
      clips.add (aClip);
    }
    synchronized void remove (Clip aClip) {
      clips.remove (aClip);
    }
    synchronized int size () {
      return clips.size();
    }
    synchronized boolean isEmpty() {
      return clips.isEmpty();
    }
    @Override synchronized public void clear () {
      super.clear ();
      for (Clip clip : clips)
        clip.clear ();
      clips.clear ();
    }
    @Override synchronized public void stop () {
      super.stop ();
      for (Clip clip : clips)
        clip.stop();
    }
    @Override synchronized public void pause () {
      super.pause ();
      for (Clip clip : clips)
        clip.pause ();
    }
    @Override synchronized public void resume () {
      super.resume ();
      for (Clip clip : clips)
        clip.resume();
    }
    @Override synchronized public Clip getClearClip () {
      Overlay clearClips = new Overlay();
      if (super.getClearClip()!=null)
        clearClips.add (super.getClearClip());
      for (Clip clip : clips) {
        Clip clearClip = clip.getClearClip ();
        if (clearClip!=null)
          clearClips.add (clearClip);
      }
      return clearClips.size()>0? clearClips: null;
    }
  }
  
  /**
   * Collection of clips rendered in sequence.
   */
  
  private class Sequence extends Composite {
    int nextClip=0;
    @Override synchronized boolean renderNextFrame () {
      if (nextClip<clips.size())
        if (!clips.get(nextClip).renderNextFrame())
          nextClip++;
      return nextClip<clips.size();
    }
  }
  
  /** 
   * Collection of clips rendered in parallel.
   */
  
  private class Overlay extends Composite {
    @Override synchronized boolean renderNextFrame () {
      boolean isMoreToRender = false;
      for (Clip clip : clips)
        isMoreToRender |= clip.renderNextFrame ();
      return isMoreToRender;
    }
  }
  
  /**
   * A rectangle, color, label, labelColor and possible visible component, grouped for convenience.
   */
  
  private class Block {
    private final RoundRectangle2D.Double rect;
    private final Color                   color;
    private final String                  label;
    private final Color                   labelColor;
    private final int                     value;
    private AComponent                    visible=null;
    
    Block (RoundRectangle2D.Double aRect, Color aColor, String aLabel, Color aLabelColor, int aValue) {
      rect=aRect; color=aColor; label=aLabel; labelColor=aLabelColor; value=aValue;
    }
    
    Block (Rectangle aRect, Color aColor, String aLabel, Color aLabelColor, int aValue) {
      this (new RoundRectangle2D.Double (aRect.x, aRect.y, aRect.width, aRect.height,
                                         CPU_BLOCK_ARCWIDTH, CPU_BLOCK_ARCHEIGHT),
            aColor, aLabel, aLabelColor, aValue);
    }
    
    Block (Rectangle aRect, Color aColor) {
      this (new RoundRectangle2D.Double (aRect.x, aRect.y, aRect.width, aRect.height, CPU_BLOCK_ARCWIDTH, CPU_BLOCK_ARCHEIGHT),
            aColor, null, new Color (0,0,0,0), 0);
    }
    
    Block (Block aBlock, Color aColor, Color aLabelColor) {
      this (aBlock.rect,
            aColor!=null? aColor: aBlock.color,
            aBlock.label,
            aLabelColor!=null? aLabelColor: aBlock.labelColor,
            aBlock.value);
      visible=aBlock.visible;
    }
    
    void clear () {
      if (visible!=null) {
        visible.clear (rect.getBounds2D());
        visible=null;
      }
    }
  }
  
  /**
   * Animate the fading in of a block.
   */
  
  private class FadeIn extends Transmute {
    FadeIn (Block block, int steps) {
      super (new Block (block,  
                        new Color (block.color.getRed(),      block.color.getGreen(),      block.color.getBlue(),      0),
                        new Color (block.labelColor.getRed(), block.labelColor.getGreen(), block.labelColor.getBlue(), 0)),
             block,
             steps);
    }
  }
  
  /**
   * Animate the fading out of a block.
   */
  
  private class FadeOut extends Transmute {
    FadeOut (Block block, int steps) {
      super (block,
             new Block (block,  
                        new Color (block.color.getRed(),      block.color.getGreen(),      block.color.getBlue(),      0),
                        new Color (block.labelColor.getRed(), block.labelColor.getGreen(), block.labelColor.getBlue(), 0)),
             steps);
    }
  }
  
  /**
   * Animate the changing of a block to another block (i.e., moving, changing color etc.).
   */
  
  private class Transmute extends ALabel {
    Transmute (Block from, Block to, int steps) {
      super (to.visible!=null? to.visible: new AComponent(),
             new RoundRectangleGradient (from.rect, to.rect, steps),
             new ColorGradient          (from.color, to.color, steps),
             new StringGradient         (from.label, to.label, steps),
             new ColorGradient          (from.labelColor, to.labelColor, steps),
             font);
      to.visible = getComponent();
    }
  }
  
  /**
   * A clip to clear a block.  Allows clearing to be sequenced with other clip rendering.
   */
  
  private class Clear extends Clip {
    Block block;
    Clear (Block aBlock) { 
      block=aBlock;
    }
    @Override boolean renderNextFrame () {
      block.clear ();
      return false;
    }
  }
  
  /**
   * Animate drawing an arrow.
   */
  
  private class Arrow extends DrawingClip {
    Arrow (Polygon from, Polygon to, Color color, int steps, int clearSteps, Shape drawingArea) {
      super (new PolygonGradient (from, to, steps),
             new ColorGradient   (color),
             new ColorGradient   (color, new Color (color.getRed(), color.getGreen(), color.getBlue(), 0), clearSteps),
             drawingArea);             
    }
    Arrow (Polygon from, Polygon to, Color color, int steps, int clearSteps) {
      this (from, to ,color, steps, clearSteps, null);
    }
  }
  
  /**
   * Animate drawing a line.
   */
  
  private class Line extends DrawingClip {
    Line (Point from, Point to, int width, Color color, int steps, int clearSteps, Shape drawingArea) {
      super (RectangleGradient.createStretch (new Point2D.Double (from.x, from.y),
                                              new Point2D.Double (to.x, to.y),
                                              width, steps),
             new ColorGradient (color), 
             new ColorGradient (color, new Color (color.getRed(), color.getGreen(), color.getBlue(), 0), clearSteps),
             drawingArea);
    }
    Line (Point from, Point to, int width, Color color, int steps, int clearSteps) {
      this (from, to, width, color, steps, clearSteps, null);
    }
  }
  
  /**
   * Animate drawing an arc.
   */
  
  private class Arc extends DrawingClip {
    Arc (Rectangle startRect, int startAngle, int arcAngle, Color color, int steps, int clearSteps, Shape drawingArea) {
      super (new ArcGradient ((double)startRect.x, (double)startRect.y, (double)startRect.width, (double)startRect.height, (double)startAngle, (double)arcAngle, Arc2D.PIE, steps), 
             new ColorGradient (color), 
             new ColorGradient (color, new Color (color.getRed(), color.getGreen(), color.getBlue(), 0), clearSteps),
             drawingArea);
    }
    Arc (Rectangle startRect, int startAngle, int arcAngle, Color color, int steps, int clearSteps) {
      this (startRect, startAngle, arcAngle, color, steps, clearSteps, null);
    }
  }
  
  /**
   * Animate drawing a line that connects two vertically aligned points that is comprised of 3 sections: 
   * horizontal to the left a stagger distance, vertical, then horizontal to the right the stagger distance.
   */
  
  private class StaggeredPath extends Sequence {
    StaggeredPath (Point from, Point to, int width, int stagger, boolean isArrow, Color color, int steps, int clearSteps, Shape drawingArea) {
      Point c0 = new Point (to.x-stagger, from.y);
      Point c1 = new Point (c0.x-width, c0.y + (to.y>from.y? width: 0));
      Point c2 = new Point (c1.x, to.y + (to.y<from.y? width: 0));
      Point c3 = new Point (c2.x+width, to.y);
      Rectangle a0 = new Rectangle (c1.x, c1.y-width, width*2, width*2);
      Rectangle a1 = new Rectangle (c2.x, c2.y-width, width*2, width*2);
      int dist   = Math.abs (to.y-from.y);
      int length = stagger*2 + width*2 + dist;
      add (new Line (from, c0, width, color, Math.max (1,steps*stagger/length), clearSteps, drawingArea));
      add (new Arc  (a0, to.y<from.y? 270: 90, to.y<from.y? -90: 90, color, Math.max (1,steps*width/length), clearSteps, drawingArea));
      add (new Line (c1, c2, width, color, Math.max (1,steps*dist/length), clearSteps, drawingArea));
      add (new Arc (a1, 180, to.y<from.y? -90: 90, color, Math.max (1,steps*width/length), clearSteps, drawingArea));
      if (isArrow) {
        Point c4 = new Point (to.x-ARROW_WIDTH, to.y);
        add (new Line (c3, c4, width, color, Math.max (1,steps*(stagger-ARROW_WIDTH)/length), clearSteps, drawingArea));
        int h0 = width/2;
        int h1 = (ARROW_HEIGHT - width)/2;
        int y0 = c4.y - h1;
        int y1 = to.y + h0;
        int y2 = c4.y + width + h1;
        Polygon p0 = new Polygon (new int[] {c4.x, c4.x, c4.x, c4.x}, new int[] {y0, y0, y2, y2}, 4);
        Polygon p1 = new Polygon (new int[] {c4.x, to.x, to.x, c4.x}, new int[] {y0, y1, y1, y2}, 4);
        add (new Arrow (p0, p1, color, Math.max (1, steps*ARROW_WIDTH/length), clearSteps, drawingArea));
      } else
        add (new Line (c3, to, width, color, Math.max (1,steps*stagger/length), clearSteps, drawingArea));
    }
  }
  
  /**
   * Extend AShape to include a clear clip and to encapsulate the associated AComponent.
   */
  
  private class DrawingClip extends AShape {
    ShapeGradient clearShape;
    ColorGradient clearColor;
    
    DrawingClip (ShapeGradient aShape, ColorGradient aColor, ColorGradient aClearColor, Shape drawingArea) {
      super (new AComponent(-1, drawingArea), aShape, aColor);
      clearShape = shape.createFinal ();
      clearColor = aClearColor;      
    }
    
    DrawingClip (ShapeGradient aShape, ColorGradient aColor, ColorGradient aClearColor) {
      this (aShape, aColor, aClearColor, null);
    }
    
    @Override public Clip getClearClip () {
      return new AShape (getComponent(), clearShape, clearColor) {
        @Override public void clear () {
          super.clear();
          DrawingClip.this.clear();
        }
        @Override public void setStep (int step) {
          super.setStep (step);
          if (step==clearColor.getSteps())
            clear();
        }
      };
    }
    
    @Override public void clear() {
      AComponent component = getComponent();
      super.clear();
      if (component!=null)
        component.clear();
    }
  }
  
  /**
   * Progressive gradient between two colors through a set of steps.
   */
  
  private static class ColorGradient {
    private final Color  from;
    private final double deltaRed, deltaGreen, deltaBlue, deltaAlpha;
    private final int    steps;
    private double       red, green, blue, alpha;
    private Color        color;
    private int          curStep=0;
    
    ColorGradient (Color aFrom, Color to, int aSteps) {
      assert aSteps>0;
      steps      = aSteps;
      from       = aFrom;
      red        = from.getRed();
      green      = from.getGreen();
      blue       = from.getBlue();
      alpha      = from.getAlpha();
      deltaRed   = (((double) to.getRed())   - red)   / steps;
      deltaGreen = (((double) to.getGreen()) - green) / steps;
      deltaBlue  = (((double) to.getBlue())  - blue)  / steps;
      deltaAlpha = (((double) to.getAlpha()) - alpha) / steps;
    }
    
    ColorGradient (Color aColor) {
      this (aColor, aColor, 1);
    }
    
    void setStep (int step) {
      if (step>steps)
        step = steps;
      if (step==curStep+1) {
        red   += deltaRed;
        green += deltaGreen;
        blue  += deltaBlue;
        alpha += deltaAlpha;
      } else if (step!=curStep) {
        red   = from.getRed()   + deltaRed   * step;
        green = from.getGreen() + deltaGreen * step;
        blue  = from.getBlue()  + deltaBlue  * step;
        alpha = from.getAlpha() + deltaAlpha * step;
      }
      curStep=step;
      color = new Color ((int) (red+0.5), (int) (green+0.5), (int) (blue+0.5), (int) (alpha+0.5));
    }
    
    int getSteps () {
      return steps;
    }
    
    Color colorValue () {
      return color;
    }
  }
  
  /**
   * Progressive string between two values through a set of steps.
   */
  
  private static class StringGradient {
    private final String from;
    private final String to;
    private final int    steps;
    private int          curStep=0;
    
    StringGradient (String aFrom, String aTo, int aSteps) {
      from=aFrom; to=aTo; steps=aSteps;
    }
    
    void setStep (int step) {
      if (step>steps)
        step=steps;
      curStep=step;
    }
    
    int getSteps () {
      return steps;
    }
    
    String stringValue () {
      if (curStep < steps>>2)
        return from!=null? from: (to!=null? to: "");
      else
        return to!=null? to: (from!=null? from: "");
    }
    
    boolean isValueChanged () {
      return curStep==steps>>2;
    }
  }
  
  /**
   * Progressive shape through a set of steps.
   */
  
  private interface ShapeGradient extends Shape {
    List <Rectangle2D> getBoundsList ();
    double             getX          ();
    double             getY          ();
    double             getWidth      ();
    double             getHeight     ();
    void               setStep       (int step);
    int                getSteps      ();
    ShapeGradient      createFinal   ();
    Rectangle2D.Double getBounds2D   (Rectangle2D.Double rect);
  }
  
  /**
   * Arc2D that grows from nothing to specitifed extent.
   */
  
  private static class ArcGradient extends Arc2D.Double implements ShapeGradient {
    private double                        extentDelta;
    private final int                     type;
    private final int                     steps;
    private int                           curStep=0;
    private final Rectangle2D             boundsDelta;
    private final ArrayList <Rectangle2D> boundsDeltaList = new ArrayList <Rectangle2D> ();
    
    ArcGradient (double x, double y, double w, double h, double start, double extent, int aType, int aSteps) {
      super (x, y, w, h, start, extent, aType);
      assert aSteps>0;
      type        = aType;
      steps       = aSteps;
      extentDelta = extent / steps;
      boundsDelta = getBounds2D();
      boundsDeltaList.add (boundsDelta);
      setAngleExtent (0);
    }
    
    ArcGradient (double x, double y, double w, double h, double start, double extent, int type) {
      this (x, y, w, h, start, extent, type, 1);
    }
    
    @Override public int getSteps () {
      return steps;
    }
    
    @Override public void setStep (int step) {
      if (step>steps)
        step = steps;
      if (step==curStep+1) 
        setAngleExtent (getAngleExtent() + extentDelta);
      else if (step!=curStep)
        setAngleExtent (extentDelta * step);
      curStep = step;
    }
    
    @Override public List <Rectangle2D> getBoundsList () {
      return boundsDeltaList;
    }
    
    @Override public ShapeGradient createFinal () {
      return new ArcGradient (x, y, width, height, start, extentDelta*steps, type);
    }
    
    @Override public Rectangle2D.Double getBounds2D (Rectangle2D.Double rect) {
      rect.x      = x;
      rect.y      = y;
      rect.width  = width;
      rect.height = height;
      return rect;
    }
  }
  
  /**
   * Polygon that changes from one bounds to another.
   */
  
  private static class PolygonGradient extends Polygon implements ShapeGradient {
    private final Polygon            from;
    private final Polygon            to;
    private final Point2D.Double     delta[];
    private final int                steps;
    private final Rectangle2D.Double bounds = new Rectangle2D.Double ();
    private final List <Rectangle2D> boundsList = new ArrayList <Rectangle2D> ();
    {
      boundsList.add (bounds);
    }
    
    PolygonGradient (Polygon aFrom, Polygon aTo, int aSteps) {
      super (aFrom.xpoints, aFrom.ypoints, aFrom.npoints);
      from=aFrom; to=aTo; steps=aSteps;
      assert from.npoints==to.npoints;
      delta = new Point2D.Double[npoints];
      for (int i=0; i<npoints; i++) 
        delta[i] = new Point2D.Double ((((double) to.xpoints[i]) - ((double) from.xpoints[i]))/steps,
                                       (((double) to.ypoints[i]) - ((double) from.ypoints[i]))/steps);
      resetBounds();
    }
    
    PolygonGradient (Polygon p) {
      this (p, p, 1);
    }
    
    private void resetBounds () {
      Rectangle2D b = getBounds2D();
      bounds.setRect (b.getX(), b.getY(), b.getWidth(), b.getHeight());
    }
    
    @Override public int getSteps () {
      return steps;
    }
    
    @Override public void setStep (int step) {
      if (step>steps)
        step = steps;
      reset ();
      for (int i=0; i<from.npoints; i++) 
        addPoint ((int) Math.round (((double) from.xpoints[i]) + delta[i].x * step),
                  (int) Math.round (((double) from.ypoints[i]) + delta[i].y * step));
      resetBounds();
    }
    
    @Override public List <Rectangle2D> getBoundsList () {
      return boundsList;
    }
    
    @Override public ShapeGradient createFinal () {
      return new PolygonGradient (to);
    }
    
    @Override public Rectangle2D.Double getBounds2D (Rectangle2D.Double rect) {
      return bounds;
    }
    
    @Override public double getX() {
      return bounds.x;
    }
    
    @Override public double getY() {
      return bounds.y;
    }
    
    @Override public double getWidth() {
      return bounds.width;
    }
    
    @Override public double getHeight() {
      return bounds.height;
    }
  }
  
  /**
   * Rectangle2D changes from one bounds to another.
   */
  
  private static class RectangleGradient extends Rectangle2D.Double implements ShapeGradient {
    private final Rectangle2D.Double      from;
    private final Rectangle2D.Double      to;
    private final Rectangle2D.Double      delta;
    private final int                     steps;
    private int                           curStep=0;
    private final Rectangle2D             unionBounds = new Rectangle2D.Double();
    private final Rectangle2D             prevBounds;
    private final Rectangle2D             curBounds;
    private final ArrayList <Rectangle2D> boundsDeltaList1 = new ArrayList <Rectangle2D> ();
    private final ArrayList <Rectangle2D> boundsDeltaList2 = new ArrayList <Rectangle2D> ();
    
    RectangleGradient (Rectangle2D.Double aFrom, Rectangle2D.Double aTo, int aSteps) {
      super (aFrom.x, aFrom.y, aFrom.width, aFrom.height);
      assert aSteps>0;
      steps = aSteps;
      from  = aFrom;
      to    = aTo;
      delta = new Rectangle2D.Double ((to.x      - from.x)      / steps,
                                      (to.y      - from.y)      / steps,
                                      (to.width  - from.width)  / steps,
                                      (to.height - from.height) / steps);
      prevBounds = getBounds2D();
      curBounds  = getBounds2D();
      boundsDeltaList1.add (unionBounds);
      boundsDeltaList2.add (prevBounds);
      boundsDeltaList2.add (curBounds);
    }
    
    RectangleGradient (Rectangle2D.Double to) {
      this (to, to, 1);
    }
    
    static RectangleGradient createStretch (Point2D.Double from, Point2D.Double to, int width, int steps) {
      double deltaX = to.x - from.x;
      double deltaY = to.y - from.y;
      double stWidth, enWidth, stHeight, enHeight;
      
      assert deltaX==0 || deltaY==0;
      
      if (deltaX==0) {
        // Verticle
        if (deltaY > 0) {
          // Stretching Down
          to.x = from.x;
          to.y = from.y;
        }
        stWidth  = width;
        enWidth  = width;
        stHeight = 0;
        enHeight = Math.abs (deltaY);
      } else {
        // Horizontal
        if (deltaX > 0) {
          // Stretching Right
          to.x = from.x;
          to.y = from.y;
        }
        stWidth  = 0;
        enWidth  = Math.abs (deltaX);
        stHeight = width;
        enHeight = width;
      }
      
      Rectangle2D.Double st = new Rectangle2D.Double (from.x, from.y, stWidth, stHeight);
      Rectangle2D.Double en = new Rectangle2D.Double (to.x,   to.y,   enWidth, enHeight);
      return new RectangleGradient (st, en, steps);
    }
    
    @Override public int getSteps () {
      return steps;
    }
    
    @Override public void setStep (int step) {
      if (step>steps)
        step = steps;
      if (false && step==curStep+1)
        setRect (x      + delta.x, 
                 y      + delta.y, 
                 width  + delta.width, 
                 height + delta.height);
      else if (step==steps)
        // apparently rounding in incremental case can lead to off-by-one in ending bounds XXX
        setRect (to.x,
                 to.y,
                 to.width,
                 to.height);
      else if (step!=curStep) 
        setRect (from.x      + delta.x      * step,
                 from.y      + delta.y      * step,
                 from.width  + delta.width  * step,
                 from.height + delta.height * step);
      curStep = step;
      prevBounds.setRect (curBounds);
      curBounds.setRect  (x-1 , y-1, width+2, height+2);
    }
    
    @Override public List <Rectangle2D> getBoundsList () {
      if (curBounds.intersects (prevBounds)) {
        unionBounds.setRect (prevBounds);
        unionBounds.add     (curBounds);
        return boundsDeltaList1;
      } else
        return boundsDeltaList2;
    }
    
    @Override public ShapeGradient createFinal () {
      return new RectangleGradient (to);
    }
    
    @Override public Rectangle2D.Double getBounds2D (Rectangle2D.Double rect) {
      rect.x      = x;
      rect.y      = y;
      rect.width  = width;
      rect.height = height;
      return rect;
    }
  }
  
  /**
   * RoundRectangle2D changes from one bounds to another.
   */
  
  private static class RoundRectangleGradient extends RoundRectangle2D.Double implements ShapeGradient {
    private final RoundRectangle2D.Double from;
    private final RoundRectangle2D.Double to;
    private final RoundRectangle2D.Double delta;
    private final int                     steps;
    private int                           curStep=0;
    private final Rectangle2D             unionBounds = new Rectangle2D.Double();
    private final Rectangle2D             prevBounds;
    private final Rectangle2D             curBounds;
    private final ArrayList <Rectangle2D> boundsDeltaList1 = new ArrayList <Rectangle2D> ();
    private final ArrayList <Rectangle2D> boundsDeltaList2 = new ArrayList <Rectangle2D> ();
    
    RoundRectangleGradient (RoundRectangle2D.Double aFrom, RoundRectangle2D.Double aTo, int aSteps) {
      super (aFrom.x, aFrom.y, aFrom.width, aFrom.height, aFrom.arcwidth, aFrom.archeight);
      assert aSteps>0;
      steps = aSteps;
      from  = aFrom;
      to    = aTo;
      delta = new RoundRectangle2D.Double ((to.x         - from.x)        / steps,
                                           (to.y         - from.y)        / steps,
                                           (to.width     - from.width)    / steps,
                                           (to.height    - from.height)   / steps,
                                           (to.arcwidth  - from.arcwidth) / steps,
                                           (to.archeight - from.archeight) / steps);
      prevBounds = getBounds2D();
      curBounds  = getBounds2D();
      boundsDeltaList1.add (unionBounds);
      boundsDeltaList2.add (prevBounds);
      boundsDeltaList2.add (curBounds);
    }
    
    RoundRectangleGradient (RoundRectangle2D.Double to) {
      this (to, to, 1);
    }
    
    @Override public int getSteps () {
      return steps;
    }
    
    @Override public void setStep (int step) {
      if (step>steps)
        step = steps;
      if (false && step==curStep+1)
        setRoundRect (x         + delta.x, 
                      y         + delta.y, 
                      width     + delta.width, 
                      height    + delta.height,
                      arcwidth  + delta.arcwidth,
                      archeight + delta.archeight);
      else if (step==steps)
        // it appears that rounding in incremental case can lead to off-by-one error in ending bounds XXX
        setRoundRect (to.x,
                      to.y,
                      to.width,
                      to.height,
                      to.arcwidth,
                      to.archeight);
      else if (step!=curStep) 
        setRoundRect (from.x         + delta.x      * step,
                      from.y         + delta.y      * step,
                      from.width     + delta.width  * step,
                      from.height    + delta.height * step,
                      from.arcwidth  + delta.arcwidth * step,
                      from.archeight + delta.archeight * step);
      curStep = step;
      prevBounds.setRect (curBounds);
      curBounds.setRect  (x-1 , y-1, width+2, height+2);
    }
    
    @Override public List <Rectangle2D> getBoundsList () {
      if (curBounds.intersects (prevBounds)) {
        unionBounds.setRect (prevBounds);
        unionBounds.add     (curBounds);
        return boundsDeltaList1;
      } else
        return boundsDeltaList2;
    }
    
    @Override public ShapeGradient createFinal () {
      return new RoundRectangleGradient (to);
    }
    
    @Override public Rectangle2D.Double getBounds2D (Rectangle2D.Double rect) {
      rect.x      = x;
      rect.y      = y;
      rect.width  = width;
      rect.height = height;
      return rect;
    }
  }
  
  /**
   * A class that can paint.
   */
  
  private interface APainter {
    void paintComponent (Graphics2D g);
  }
  
  /**
   * Element added to root layered pane for painting animations.
   */
  
  private class AComponent {
    private JComponent      canvas;
    private APainter        painter=null;
    private final Rectangle clipRect;
    private final Rectangle clip = new Rectangle();
    
    AComponent (final int layerOffset, Shape drawingArea) {
      clipRect = drawingArea!=null? drawingArea.getBounds() : null;
      canvas = new JComponent () {
        {
          frame.getLayeredPane().add (this, new Integer(JLayeredPane.MODAL_LAYER + layerOffset));
        }
        @Override public void paintComponent (Graphics g) {
          if (painter!=null) {
            Graphics2D g2d = (Graphics2D) g;
            if (clipRect!=null) {
              clip.setBounds (clipRect.x-canvas.getX(), clipRect.y-canvas.getY(), clipRect.width, clipRect.height);
              g2d.clip (clip);
            }
            painter.paintComponent (g2d);
          }
        }
      };      
    }
    
    AComponent (final int layerOffset) {
      this (layerOffset, null);
    }
    
    AComponent () {
      this (0, null);
    }
    
    AComponent (Shape drawingArea) {
      this (0, drawingArea);
    }
    
    void setPainter (APainter aPainter) {
      painter=aPainter;
    }
    
    void paintImmediately (Rectangle r) {
      if (canvas!=null) {
        canvas.setBounds        (r);
        canvas.paintImmediately (r.x-canvas.getX(), r.y-canvas.getY(), r.width, r.height);
      }
    }

    void paintImmediately (List <Rectangle2D> bounds) {
      int tlx = Integer.MAX_VALUE;
      int tly = Integer.MAX_VALUE;
      int brx = Integer.MIN_VALUE;
      int bry = Integer.MIN_VALUE;
      for (Rectangle2D r : bounds) {
        int rtlx = (int) Math.round (r.getX());
        int rtly = (int) Math.round (r.getY());
        int rbrx = rtlx + (int) Math.round (r.getWidth());
        int rbry = rtly + (int) Math.round (r.getHeight());
        tlx = Math.min (tlx, rtlx);
        tly = Math.min (tly, rtly);
        brx = Math.max (brx, rbrx);
        bry = Math.max (bry, rbry);
      }
      canvas.setBounds (tlx, tly, brx-tlx, bry-tly);
      for (Rectangle2D r : bounds)
        canvas.paintImmediately ((int) Math.round (r.getX())-canvas.getX(),
                                 (int) Math.round (r.getY())-canvas.getY(),
                                 (int) Math.round (r.getWidth()),
                                 (int) Math.round (r.getHeight()));      
    }
    
    int getX () {
      return canvas.getX();
    }
    
    int getY () {
      return canvas.getY();
    }
    
    void clear (Rectangle2D bounds) {
      clear();
      frame.getLayeredPane().paintImmediately ((int) Math.round (bounds.getX()),
                                               (int) Math.round (bounds.getY()),
                                               (int) Math.round (bounds.getWidth()),
                                               (int) Math.round (bounds.getHeight()));
    }
    
    void clear () {
      if (canvas!=null) {
        frame.getLayeredPane().remove (canvas);
        canvas=null;
      }
    }
  }
  
  /**
   * Animation of shape bounds and color changes.
   */
  
  private class AShape extends Clip implements APainter {
    private       AComponent         component;
    final         ShapeGradient      shape;
    private final ColorGradient      color;
    private final int                steps;
    private       int                curStep = 0;
    private final Rectangle2D.Double bounds  = new Rectangle2D.Double ();
    
    AShape (AComponent aComponent, ShapeGradient aShape, ColorGradient aColor, int minSteps) {
      component=aComponent; shape=aShape; color=aColor; 
      steps = Math.max (minSteps, Math.max (shape.getSteps(), color.getSteps()));
    }
    
    AShape (AComponent aComponent, ShapeGradient aShape, ColorGradient aColor) {
      this (aComponent, aShape, aColor, 0);
    }
    
    void setStep (int step) {
      shape.setStep (curStep);
      color.setStep (curStep);
      if (component!=null) {
        if (step==0)
          component.setPainter (this);
        component.paintImmediately (shape.getBoundsList());
      }
    }
    
    AComponent getComponent () {
      return component;
    }
    
    @Override public void clear () {
      super.clear();
      if (component!=null) {
        component.setPainter       (null);
        component.paintImmediately (shape.getBounds());
        component=null;
      }
    }
    
    @Override public void stop () {
      super.stop();
      clear();
    }
    
    @Override public void paintComponent (Graphics2D g) {
      g.translate (-component.getX(), -component.getY());
      g.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g.setColor (color.colorValue());
      g.clip     (shape.getBounds2D (bounds));
      g.fill     (shape);
    }
    
    @Override boolean renderNextFrame () {
      if (curStep<=steps) {
        setStep (curStep);
        curStep++;      
      }
      return curStep<=steps;
    }
  }
  
  /**
   * Animation of labeled shape bounds, color and label color changes.
   */
  
  private class ALabel extends AShape {
    private final StringGradient label;
    private final Font           font;
    private final ColorGradient  labelColor;
    private       GlyphVector    glyphVector=null;
    private       Rectangle2D    glyphBounds=null;
    
    ALabel (AComponent aComponent, ShapeGradient aShape, ColorGradient aColor, StringGradient aLabel, ColorGradient aLabelColor, Font aFont) {
      super (aComponent, aShape, aColor, aLabelColor.getSteps()); label=aLabel; labelColor=aLabelColor; font=aFont;
    }
    
    @Override void setStep (int step) {
      label.setStep      (step);
      labelColor.setStep (step);
      super.setStep      (step);
    }
    
    @Override public void paintComponent (Graphics2D g) {
      super.paintComponent (g);
      g.setColor           (labelColor.colorValue());
      g.setFont            (font);
      if (glyphVector==null || label.isValueChanged()) {
        glyphVector = font.createGlyphVector (g.getFontRenderContext(), label.stringValue());
        glyphBounds = glyphVector.getVisualBounds();
      }
      g.drawGlyphVector (glyphVector, 
                         (int) Math.round (shape.getX() + shape.getWidth()/2  - (glyphBounds.getWidth())/2 - glyphBounds.getX()), 
                         (int) Math.round (shape.getY() + shape.getHeight()/2 - (glyphBounds.getHeight())/2 - glyphBounds.getY()));
    }
  }
  
  /**
   * Animate the flow of data between target location and cpu location.
   */
  
  private class DataFlowScene extends Sequence {
    Block tvb=null;
    Block tab=null;
    Block cvb=null;
    Block cab=null;
    
    DataFlowScene (Target target, Type type, int address, Rectangle aRect, int value, Rectangle vRect, String label) {
      
      // Create target value block
      Color tc;
      switch (type) {
        case TARGET_TO_CPU:
          tc = READ_TARGET_COLOR;
          break;
        case CPU_TO_TARGET:
          tc = WRITE_TARGET_COLOR;
          break;
        default:
          throw new AssertionError();
      }
      tvb = new Block (vRect, tc);
      
      // Create target address block for memory access
      switch (target) {
        case MEMORY:
          tab = new Block (aRect, ADDRESS_BLOCK_COLOR);
          break;
        default:
          tab = null;
      }
      
      // Locate or create CPU address block
      switch (target) {
        case MEMORY:
          cab = cpuBlocks.findLast (address);
          if (cab==null) {
            cab = newCpuBlock (String.format ("0x%x", address), address);
          }
          break;
        default:
          cab = null;
      }
      
      // Locate or create cpu value block (matching our value to block value, this is a heuristic)
      switch (type) {
        case CPU_TO_TARGET:
          cvb = cpuBlocks.findLast (value);
          if (cvb==null) 
            cvb = newCpuBlock (String.format ("0x%x", value), value);
          break;
        case TARGET_TO_CPU:
          cvb = newCpuBlock (label, value);
          break;
        default:
          throw new AssertionError();
      }
      
      // compute scene clips
      switch (type) {
        case TARGET_TO_CPU:
          createMoveClip (type, tvb, cab, cvb, tab);
          break;
        case CPU_TO_TARGET:
          createMoveClip (type, cvb, cab, tvb, tab);
          break;
      }
    }
    
    private Block newCpuBlock (String label, int value) {
      Point cp = convertPoint (cpu, cpuPos);
      RoundRectangle2D.Double rect = new RoundRectangle2D.Double (cp.x, cp.y, 
                                                                  CPU_BLOCK_WIDTH, CPU_BLOCK_HEIGHT,
                                                                  CPU_BLOCK_ARCWIDTH, CPU_BLOCK_ARCHEIGHT);
      cpuPos.x += CPU_BLOCK_WIDTH + CPU_BLOCK_SPACER;
      Block block = new Block (rect, CPU_COLOR, label, CPU_LABEL_COLOR, value);
      cpuBlocks.add (block);
      return block;
    }
    
    private void createMoveClip (Type type, Block fv, Block fa, Block tv, Block ta) {
      
      // Fade in the CPU blocks that aren't already there
      Overlay fadeIn = new Overlay ();
      if (type==Type.CPU_TO_TARGET && fv.visible==null)
        fadeIn.add (new FadeIn (fv, stageSteps (Stage.FADE_IN)));
      if (fa!=null && fa.visible==null) 
        fadeIn.add (new FadeIn (fa, stageSteps (Stage.FADE_IN)));
      if (fadeIn.size()>0)
        add (fadeIn);
      
      // Move value and address blocks
      Clip moveV = new Transmute (fv, tv, stageSteps (Stage.ACTION));
      if (fa==null && ta==null) 
        add (moveV);
      else {
        Clip moveA = new Transmute (fa!=null? fa: fv, ta!=null? ta: tv, stageSteps (Stage.ACTION));
        switch (type) {
          case TARGET_TO_CPU:
            Sequence s = new Sequence ();
            s.add (moveA);
            s.add (moveV);
            add (s);
            break;
          case CPU_TO_TARGET:
            Overlay o = new Overlay ();
            o.add (moveA);
            o.add (moveV);
            add (o);
            break;
        }
      }
      
      // Fade target blocks
      Overlay fadeOut = new Overlay ();
      if (!cpuBlocks.contains (tv)) {
        Sequence s = new Sequence ();
        s.add (new FadeOut (tv, stageSteps (Stage.FADE_OUT)));
        s.add (new Clear (tv));
        fadeOut.add (s);
      }
      if (ta!=null && !cpuBlocks.contains (ta)) {
        Sequence s = new Sequence ();
        s.add (new FadeOut (ta, stageSteps (Stage.FADE_OUT)));
        s.add (new Clear (ta));
        fadeOut.add (s);
      }
      add (fadeOut);
    }
    
    @Override public void render () {
      for (JComponent c : paintFirst) {
        Container parent = c.getParent();
        if (parent instanceof JComponent) {
          JComponent jParent = (JComponent) parent;
          jParent.paintImmediately (c.getBounds());
        }
      }
      super.render ();
    }
    
    @Override public void stop () {
      super.stop();
      tvb.clear();
      if (tab!=null)
        tab.clear();
    }
  }
  
  /**
   * Animate control flow from one PC to another.
   * 
   * This sequence is complicated by the fact that there may already be a control-flow
   * scene displayed if the underlying operation is a jump.  In this case, new scene
   * will first animate the clearing of the previous scene and then change the from point
   * to the from point from the previous scene.
   */
  
  private class ControlFlowScene extends Sequence {
    final static int COLUMN = 1;
    ControlFlowScene (JComponent context, Position from, Position to) {
      Rectangle fromRec   = from.view.getCellRect (from.row, COLUMN, false);
      Rectangle toRec     = to.view.getCellRect   (to.row,   COLUMN, false);
      Point     fromPoint = convertPoint (from.view, 
                                          new Point (fromRec.x+CONTROL_FLOW_FROM_OFFSET, 
                                                     fromRec.y+7-CONTROL_FLOW_LINE_WIDTH/2));
      Point     toPoint   = convertPoint (to.view,   
                                          new Point (toRec.x+CONTROL_FLOW_FROM_OFFSET,
                                                     toRec.y+7-CONTROL_FLOW_LINE_WIDTH/2));
      Rectangle drawArea  = convertRectangle (context, context.getVisibleRect());
      if (Math.abs (fromPoint.y-toPoint.y) >= CONTROL_FLOW_LINE_WIDTH) {
        StaggeredPath path = new StaggeredPath (fromPoint, toPoint,
                                                CONTROL_FLOW_LINE_WIDTH, CONTROL_FLOW_LINE_STAGGER,
                                                true, CONTROL_FLOW_COLOR,
                                                stageSteps (Stage.CONTROL_FLOW_LINE),
                                                stageSteps (Stage.FADE_OUT),
                                                drawArea);
        add (path);
        clearScene.add (path.getClearClip());
      }
    }    
  }
    
  /**
   * Render Control Flow Scene
   */
  
  private class ControlFlowWorker extends SceneWorker {
    @Override public void run() {
      Overlay ol = new Overlay() {
        @Override public void render () {
          for (JComponent c : paintFirst) {
            Container parent = c.getParent();
            if (parent instanceof JComponent) {
              JComponent jParent = (JComponent) parent;
              jParent.paintImmediately (c.getBounds());
            }
          }
          super.render ();
        }
      };
      for (JComponent context : cfStartPosition.keySet()) {
        Position from = cfStartPosition.get (context);
        Position to   = cfPosition.get      (context);
        if (from!=null && to!=null)
          ol.add (new ControlFlowScene (context, from, to));
      }
      setClip (ol);
    }
  }
  
  /**
   * Render scene using SceneWorker.
   */
  
  private class DataFlowWorker extends SceneWorker {
    private final Target  target;
    private final Type    type;
    private final View    view;
    private final int     labelCol, row, col, count;
    
    DataFlowWorker (Target aTarget, Type aType, View aView, int aLabelCol, int aRow, int aCol, int aCount) {
      target=aTarget; type=aType; view=aView; labelCol=aLabelCol; row=aRow; col=aCol; count=aCount;
    }
    
    @Override public void run () {
      Rectangle visibleRect = view.getVisibleRect();
      
      // make sure target is visible
      if (visibleRect.contains (view.getCellRect (row, col, false))) {
        
        switch (target) {
          case REGISTER:
          case MEMORY:
            
            // memory is a split pane and we only want to animate one of the splits (if both are visible)
            boolean skip = false;
            if (target==Target.MEMORY) {
              View renderedView = rowRenderedView.get (row);
              if (renderedView==null)
                rowRenderedView.put (row, view);
              else if (renderedView!=null)
                skip = true;
            }
            if (!skip) {
              
              int       address, value;
              String    name;
              
              // create target rectangle
              Rectangle vRect = view.getCellRect (row, col, false);
              Rectangle aRect = null;
              for (int i=1; i<count; i++) 
                vRect = vRect.union (view.getCellRect (row, col+i, true));
              vRect = convertRectangle (view, vRect);
              
              // determine name, address and value and locate or create cpu block
              switch (target) {
                case REGISTER:
                  address = row;
                  value   = (Integer) view.getModel().getValueAt (row, col);
                  name    = String.format ("%s: 0x%x", (String)  view.getModel().getValueAt (row, labelCol), value);
                  break;
                case MEMORY:
                  address = (Integer) view.getModel().getValueAt (row, labelCol);
                  value   = 0;
                  for (int i=0; i<count; i++)
                    value = (value << 8) | ((Byte) view.getModel().getValueAt (row, col+i)).byteValue();
                  value = memory.normalizeEndianness (value, count);
                  name  = String.format ("mem: 0x%x", value);
                  aRect = convertRectangle (view, view.getCellRect (row, labelCol, false));
                  break;
                default:
                  throw new AssertionError();
              }
              
              // create and render scene
              setClip (new DataFlowScene (target, type, address, aRect, value, vRect, name)); 
            }
            
            break;
        }
      }
    }
  }
  
  /**
   * Perform clear public method using SceneWorker.
   */
  
  private class ClearWorker extends SceneWorker {
    @Override public void run () {
      try {Thread.sleep (stageSteps (Stage.HOLD) * SCENE_PAUSE);} catch (InterruptedException e) {}
      Overlay o = new Overlay ();
      setClip (o);
      synchronized (cpuBlocks) {
        for (Block b : cpuBlocks) {
          Sequence s = new Sequence ();
          s.add (new FadeOut (b, stageSteps (Stage.FADE_OUT)));
          s.add (new Clear (b));
          o.add (s);
        }
      }
      o.add (clearScene);
    }
    
    @Override public void render () {
      super.render ();
      clearScene.clear();
      cpuBlocks.clear();
      cpuPos = new Point (CPU_BLOCK_START);
      rowRenderedView.clear();
      cfStartPosition.clear();
    }
    
    @Override public void stop () {
      resume();
      super.stop();
    }
  }
  
  /**
   * Render a control flow scence with multiple contexts.  
   *
   * Expect multiple identical scenes to play out in multiple contexts.  Render issues on context in 
   * consistent order.  Scenes renedered in parallel each time last context is rendered.  This should 
   * probably be the new render method, but for now (every?) its just for Instructions.
   * 
   * @param context  object that names scene context.
   */
  
  void setControlFlowStart (JComponent context) {
    if (isEnabled && !isStopped && duration>0 && activeScene==null && !cfStartPosition.containsKey (context))
      cfStartPosition.put (context, cfPosition.get (context));
  }
  
  /**
   * 
   */
  
  void renderControlFlow() {
    synchronized (this) {
      assert (activeScene==null);
      try {
        if (!cfStartPosition.isEmpty()) {
          activeScene = new ControlFlowWorker();
          activeScene.render();
          cfStartPosition.clear();
        }  
      } finally {
        activeScene = null;
      }
    }
  }
  
  /**
   * Record a control flow value without rendering.
   *
   * Expect multiple identical scenes to play out in multiple contexts.  Render issues on context in 
   * consistent order.  Scenes renedered in parallel each time last context is rendered.  This should 
   * probably be the new render method, but for now (every?) its just for Instructions.
   * 
   * @param context  object that names scene context.
   * @param view     view compoment of rendering target.
   * @param row      target row in view's dataModel.
   */
  
  void recordControlFlow (JComponent context, View view, int row) {
    cfPosition.put (context, new Position (view, row));    
  }
  
  /**
   * Render a scene.
   * @param target   rendering target type.
   * @param type     rendering type.
   * @param view     view compoment of rendering target.
   * @param labelCol column of view's dataModel that stores target label.
   * @param row      target row in view's dataModel.
   * @param col      target col in view's dataModel.
   * @param count    number of consecutive columns, starting at col, inclued in the target.
   */
  
  void renderDataFlow (Target target, Type type, View view, int labelCol, int row, int col, int count) {
    assert target!=Target.INSTRUCTIONS;
    if (isEnabled && !isStopped && duration>0 && activeScene==null) {
      synchronized (this) {
        assert activeScene==null;
        try {
          activeScene = new DataFlowWorker (target, type, view, labelCol, row, col, count);
          activeScene.render();
        } finally {
          activeScene=null;
        }
      }
    }
 }
  
  /**
   * Clear animation state.
   */
  
  synchronized void clear () {
    renderControlFlow();
    if (!cpuBlocks.isEmpty() || !clearScene.isEmpty()) { 
      assert activeScene==null;
      try {
        activeScene = new ClearWorker ();
        activeScene.render ();
        
      } finally {
        activeScene = null;
      }
    }
  }
  
  /**
   * Set the estimated duration of animation.  This will be the time taken by each scene of the
   * animation and so animations with more scenes taken longer than those with fewer.
   * @param aDuration new duration in milliseconds.
   */
  
  void setDuration (int aDuration) {
    duration = aDuration;
  }
  
  /**
   * Get current animation duration.
   */
  
  int getDuration () {
    return duration;
  }
  
  /**
   * Set animation to be enabled or disabled.  Disabling also stops the current animation immediately.
   */
  
  void setEnabled (boolean anIsEnabled) {
    isEnabled=anIsEnabled;
    if (isEnabled) {
      isStopped = false;
      isPaused  = false;
    } else
      stop();
  }
  
  /**
   * Determine whether animation is currently enabled.
   */
  
  boolean isEnabled () {
    return isEnabled;
  }
  
  /**
   * Stop active animation immediately.  
   *
   * Implementation Note: This call can be concurrent with the renderer and so
   * all data stuctures touched by stop and rendering must be properly synchronized.
   */
  
  void stop () {
    isStopped = true;
    if (activeScene!=null)
      activeScene.stop();
    cpuBlocks.clear();
    clearScene.clear();
    rowRenderedView.clear();
    cfStartPosition.clear();
  }
  
  /**
   * Pause active animation.
   *
   * Implementation Note: Concurrent with rendering.
   */
  
  void pause () {
    isPaused=true;
    if (activeScene!=null)
      activeScene.pause();
  }
  
  /**
   * Resume pased animation.
   *
   * Impementation Note: Concurrent with rendering.
   */
  
  void resume () {
    isPaused  = false;
    isStopped = false;
    if (activeScene!=null)
      activeScene.resume();
  }
  
  /**
   * Determined whether animation is currently paused.
   */
  
  boolean isPaused () {
    return isPaused;
  }
}

