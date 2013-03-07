package ui.gui;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Vector;
import java.util.Observer;
import java.util.Observable;
import java.util.Hashtable;
import java.util.ArrayList;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.awt.FileDialog;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout; 
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JSplitPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.BorderUIResource;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.TransferHandler;
import javax.swing.BorderFactory;
import javax.swing.Scrollable;
import javax.imageio.ImageIO;
import machine.RegisterSet;
import isa.Memory;
import isa.Region;
import isa.InstructionRegion;
import isa.InstructionModel;
import isa.DataRegion;
import util.CompoundModel;
import util.PickModel;
import util.TableCellIndex;
import util.DataModel;
import util.AbstractDataModel;
import util.DataModelEvent;
import util.HalfByteNumber;
import util.SixByteNumber;
import util.MapModel;
import util.AbstractDataModel;
import ui.AbstractUI;
import ui.Machine;


public class UI extends AbstractUI {
  
  // Local Look and Feel Properties
  private final static Color                READ_HL             = new Color (120,120,240);
  private final static Color                WRITE_HL            = new Color (240,120,120);
  private final static Color                CURSOR_HL           = new Color (100,240,100);
  private final ViewFormat.HighlightControl MEMORY_HIGHLIGHT    = new ViewFormat.HighlightControl (new ViewFormat.BorderHighlight (READ_HL), new ViewFormat.BorderHighlight (WRITE_HL), new ViewFormat.BorderHighlight (CURSOR_HL), null);
  private final ViewFormat.HighlightControl CODE_HIGHLIGHT      = new ViewFormat.HighlightControl (new ViewFormat.BorderHighlight (READ_HL), new ViewFormat.BorderHighlight (WRITE_HL), new ViewFormat.BorderHighlight (CURSOR_HL), null);
  private final String                      monoSpaceFont       = pickMonoSpaceFont ();
  private final Font                        addressFont         = new Font  (monoSpaceFont, Font.PLAIN, 10);
  private final Font                        cellFont            = new Font  (monoSpaceFont, Font.PLAIN, 10);
  private final Font                        memAddressFont      = new Font  (monoSpaceFont, Font.PLAIN, 9);
  private final Font                        memCellFont         = new Font  (monoSpaceFont, Font.PLAIN, 9);
  private final Font                        macFont             = new Font  (monoSpaceFont, Font.PLAIN, 8);
  private final Font                        labelFont           = new Font  (monoSpaceFont, Font.PLAIN, 10);
  private final Font                        animationFont       = new Font  (monoSpaceFont, Font.BOLD,  12);
  private final Font                        titleFont           = new Font  ("Default",     Font.BOLD,  14);
  private final Font                        subTitleFont        = new Font  ("Default",     Font.BOLD,  12);
  private final Font                        aboutFont           = new Font  ("Default",     Font.PLAIN, 10);
  private final Font                        nameFont            = new Font  ("Default",     Font.PLAIN, 10);
  private final Font                        commentFont         = new Font  ("Arial",       Font.PLAIN, 10);
  private final Font                        statusMessageFont   = new Font  ("Default",     Font.PLAIN, 11);
  private final Font                        curInsFont          = new Font  ("Default",     Font.PLAIN, 20);
  private final Font                        curInsDscFont       = new Font  ("Default",     Font.PLAIN, 11);
  private final Font                        curInsTwoColFont    = new Font  ("Default",     Font.PLAIN, 13);
  private final Font                        curInsDscTwoColFont = new Font  ("Default",     Font.PLAIN, 9);
  private final Color                       strutColor          = new Color (200,200,200);
  private final Color                       toolBarColor        = new Color (180,180,180);
  private final Color                       toolBarBorder       = new Color (64,64,64);
  private final Color                       statusBarBorder     = new Color (128,128,128);
  private final Color                       macColor            = new Color (100,100,100);
  private final Color                       labelColor          = new Color (130,0,130);
  private final Color                       codeColor           = new Color (0,0,180);
  private final Color                       commentColor        = new Color (0,100,0);
  private final Color                       addressColor        = new Color (50,50,50);
  private final Color                       nameColor           = new Color (50,50,50);
  private final Color                       breakpointColor     = new Color (240,0,0);
  private final int                         PAUSE_MS            = 1400;
  private final int                         PAUSE_MIN_MS        = 200;
  private final int                         PAUSE_MAX_MS        = 5000;
  private final int                         PAUSE_INC_MS        = 200;
  public static final boolean               IS_MAC_OS_X         = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
  public static final boolean               IS_MS_WINDOWS       = (System.getProperty("os.name").toLowerCase().startsWith("windows"));
  private final int                         platformAgnosticMetaMask = IS_MAC_OS_X? ActionEvent.META_MASK : ActionEvent.CTRL_MASK;
  private final static String               ICON_RESOURCE_NAME  = "/images/icon.png";
  
  // Global Look and Feel properties
  final static Font                   TITLE_FONT         = new Font ("Default", Font.BOLD, 12);
  final static Color                  BACKGROUND_COLOR   = new Color (250,250,250);
  final static Color                  CELL_BORDER_COLOR  = new Color (200,200,200);
  final static Color                  PANE_BORDER_COLOR  = new Color (160,160,160);
  final static Color                  SELECTION_COLOR    = new Color (255,210,0);
  final static Color                  ERROR_BORDER_COLOR = new Color (255,0,0);
  
  // Options
  protected boolean                   isMacShown;
  protected boolean                   isTwoProcStateCols;
  protected boolean                   isSmallInsMemDpy;
  protected boolean                   isRegFileInOwnCol;
  protected boolean                   isSmallCurInsDpy;
  protected boolean                   isDataAddrShown;
  protected boolean                   isAnimationAvailable;
  
  // Application object
  protected Application               application;
  
  static {
    System.setProperty ("apple.laf.useScreenMenuBar", "true");
  }
  
  private static String pickMonoSpaceFont () {
    String[] fontsToTry = new String[] { "Menlo", "Consolas", "Monaco", "Courier New" };
    for (String fontFamily : fontsToTry)
      if (new Font (fontFamily, Font.PLAIN, 10).getFamily ().equals (fontFamily)) 
	return fontFamily;
    return "";
  }
  
  public UI (ArrayList <String> args) throws ArgException {
    super (args);
    if (args.size()!=0) 
      throw new ArgException ("Invalid command-line syntax.\n");
    isMacShown           = machine.options.contains ("[showMac]");
    isTwoProcStateCols   = machine.options.contains ("[twoProcStateCols]");
    isSmallInsMemDpy     = machine.options.contains ("[smallInsMemDpy]");
    isRegFileInOwnCol    = machine.options.contains ("[regFileInOwnCol]");
    isSmallCurInsDpy     = machine.options.contains ("[smallCurInsDpy]");
    isDataAddrShown      = machine.options.contains ("[showDataAddr]");
    isAnimationAvailable = machine.options.contains ("[animation]");
  }
  
  @Override
  public void run () {
    application = new Application ();
  }
  
  interface StateChangedListener {
    void stateChanged (Object o);
  }
  
  interface IsRunningListener {
    void isRunningChanged ();
  }
  
  /**
   * Application conists of this single frame plus addition memory frames 
   * created and displosed of on demand.
   */
  
  class Application extends JFrame implements Observer {
    AboutBox                   aboutBox;
    ImageIcon                  icon=null;
    MainPane                   mainPane;
    StatusBar                  statusBar;
    Vector <IsRunningListener> isRunningListeners      = new Vector <IsRunningListener> ();
    MasterSelectionListner     masterSelectionListener = new MasterSelectionListner();
    ApplicationUndoManager     undoManager             = new ApplicationUndoManager();
    boolean                    isRunning               = false;
    int                        screenWidth             = Toolkit.getDefaultToolkit().getScreenSize().width;
    int                        screenHeight            = Toolkit.getDefaultToolkit().getScreenSize().height;
    Animator                   animator                = null;
    
    Application () {
      super (applicationFullName);      
      URL iconURL = getClass().getResource (ICON_RESOURCE_NAME);
      if (iconURL!=null) {
        icon = new ImageIcon (iconURL);
        setIconImage (icon.getImage());
      }
      machine.addObserver (this);      
      machine.memory.addUndoableEditListener (undoManager);
      setDefaultCloseOperation (WindowConstants.DO_NOTHING_ON_CLOSE);
      addWindowListener (new WindowAdapter () {
        public void windowClosing (WindowEvent e) {
          if (quit ())
            System.exit (0);
        }
      });
      setJMenuBar (new MenuBar ());
      Container cp = getContentPane ();
      cp.setLayout (new BorderLayout ());
      statusBar = new StatusBar ();
      mainPane  = new MainPane ();
      cp.add (new ToolBar (), BorderLayout.NORTH);
      cp.add (mainPane,       BorderLayout.CENTER);
      cp.add (statusBar,      BorderLayout.SOUTH);
      doPlatformSpecificInitialization ();
      aboutBox = new AboutBox ();
      pack       ();
      setVisible (true);
    }
    
    void adjustHighlights (boolean clear) {
      mainPane.adjustHighlights (clear);
    }
    
    void setRunning (boolean anIsRunning) {
      isRunning = anIsRunning;
      fireIsRunningChanged ();
    }
    
    void doPlatformSpecificInitialization () {
      if (IS_MAC_OS_X) 
	try {
	  OSXAdapter.setAboutHandler (this, getClass ().getDeclaredMethod ("showAbout", (Class[]) null));
	  OSXAdapter.setQuitHandler  (this, getClass ().getDeclaredMethod ("quit",      (Class[]) null));
          if (icon!=null) {
            Class <?> appClass         = Class.forName ("com.apple.eawt.Application");
            Method    getApplication   = appClass.getDeclaredMethod ("getApplication");
            Method    setDockIconImage = appClass.getDeclaredMethod ("setDockIconImage", Image.class);
            Object    app              = getApplication.invoke (null);
            setDockIconImage.invoke (app, icon.getImage());
          }
	} catch (Exception e) {
	  throw new AssertionError(e);
	}
    }
    
    boolean quit () {
      if (machine.memory.hasUnsavedChanges ()) {
	int option = JOptionPane.
	  showOptionDialog (this, 
			    "You have unsaved changes.  Do you really want to quit and lose them?",
			    "Quit with UnsavedChanges", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
			    new String[] { "Discard Changes", "Cancel" }, "Cancel");
	return (option == 0);
      } else
	return true;
    }
    
    void showAbout () {
      aboutBox.setLocation ((int) getLocation ().getX () + 22, (int) getLocation ().getY () + 22);
      aboutBox.setVisible (true);
    }
    
    void loadFile (String fileName) {
      try {
        animator = new Animator (this, machine.memory, mainPane.cpuPane.processorStatePane, 
                                 PAUSE_MS, animationFont,
                                 mainPane.cpuPane.registerViewPane, mainPane.allMemoryPane.instructionMemoryPane,
                                 statusBar, mainPane.cpuPane.processorStatePane);
        machine.memory.loadFile (fileName);
        updateMemoryView        ();
        statusBar.showMessage   ("File loaded into memory.");        
      } catch (Memory.FileTypeException fte) {
        statusBar.showMessage ("Unable to load file; invalid file type.");
      } catch (Memory.InputFileSyntaxException ifse) {
        statusBar.showMessage (ifse.toString ());
      } catch (Exception ex) {
        throw new AssertionError (ex);
      }
    }
    
    /**
     * Application's "about" box.
     */
    class AboutBox extends JDialog {
      final ImageIcon scaledIcon    = icon!=null? new ImageIcon (icon.getImage().getScaledInstance (64, -1, 0)): null;
      final JLabel    image         = icon!=null? new JLabel (scaledIcon): null;
      final JLabel    title         = new JLabel (APPLICATION_NAME);
      final JLabel    version       = new JLabel (APPLICATION_VERSION);
      final JLabel    jdk           = new JLabel ("JDK " + System.getProperty ("java.version"));
      final JLabel    copyright[]   = new JLabel [APPLICATION_COPYRIGHT.length];
      
      {
        if (image!=null)
          image.setAlignmentX   (0.5f);
        title.setAlignmentX   (0.5f);
        version.setAlignmentX (0.5f);
        jdk.setAlignmentX     (0.5f);
        Font font = title.getFont().deriveFont (10.0f);
        title.setFont   (font.deriveFont (14.0f).deriveFont (Font.BOLD));
        version.setFont (font);
        jdk.setFont     (font);
        for (int i=0; i<APPLICATION_COPYRIGHT.length; i++) {
          copyright[i] = new JLabel (APPLICATION_COPYRIGHT[i]);
          copyright[i].setFont       (font);
          copyright[i].setAlignmentX (0.5f);
        }
        setLayout (new BoxLayout (getContentPane(), BoxLayout.PAGE_AXIS));
      }
      
      AboutBox () {
	super (Application.this, "About ".concat (APPLICATION_NAME));
        if (image!=null)
          add (image);
        add (title);   add (Box.createRigidArea (new Dimension (0,10)));
        add (version); add (Box.createRigidArea (new Dimension (0,10)));
        add (jdk);     add (Box.createRigidArea (new Dimension (0,10)));
        for (JLabel cp : copyright)
          add (cp);
        add (Box.createRigidArea (new Dimension (0,12)));
        pack ();
        setSize (new Dimension (282, getSize().height));
	setResizable (false);
      }
    }
        
    /**
     * Some actions must be shared by both MenuBar and ToolBar for them both
     * to show their state consistently.  These actions are defined here.
     */
    private final RunSlowerAction       runSlower       = new RunSlowerAction();
    private final RunFasterAction       runFaster       = new RunFasterAction();
    private final AnimationOnAction     animationOn     = new AnimationOnAction();
    private final AnimationPauseAction  animationPause  = new AnimationPauseAction();
    private final RunAction             run             = new RunAction ();
    {
      animationOn.addPropertyChangeListener     (animationPause);
      animationOn.addPropertyChangeListener     (runSlower);
      animationOn.addPropertyChangeListener     (runFaster);
      run.addPropertyChangeListener             (animationPause);
    }
    
    /**
     * Application's MenuBar.
     */
    class MenuBar extends JMenuBar {
      MenuBar () {
	JMenu menu = new JMenu ("File");
	add (menu);
	menu.add (new JMenuItem (new OpenAction ()));
	menu.addSeparator ();
	menu.add (new JMenuItem (new SaveAction ()));
	menu.add (new JMenuItem (new SaveAsAction ()));
	menu.addSeparator ();
	menu.add (new JMenuItem (new RestoreDataFromCheckpointAction ()));
	menu.add (new JMenuItem (new CheckpointDataAction ()));
        if (!IS_MAC_OS_X) {
          menu.addSeparator ();
          menu.add (new JMenuItem (new QuitAction()));
        }
	menu = new JMenu ("Edit");
	add (menu);
	menu.add (new JMenuItem (new UndoAction ()));
	menu.add (new JMenuItem (new RedoAction ()));
	menu.addSeparator ();
	menu.add (new JMenuItem (new InsertAboveAction ()));
	menu.add (new JMenuItem (new InsertBelowAction ()));
	menu.add (new JMenuItem (new DeleteAction ()));
	menu = new JMenu ("View");
	add (menu);
	menu.add (new JMenuItem (new IncreaseFontSizeAction ()));
	JMenuItem x = new JMenuItem (new IncreaseFontSizeAction0 ());        
	JMenuItem y = new JMenuItem (new IncreaseFontSizeAction1 ());
	x.setVisible (true);
	y.setVisible (true);
	menu.add (x);
	menu.add (y);
	menu.add (new JMenuItem (new DecreaseFontSizeAction ()));
	menu = new JMenu ("Run");
	add (menu);        
        menu.add (new JMenuItem (run));
        menu.add (new JMenuItem (new StepAction()));
        menu.add (new JMenuItem (new RunSlowlyAction()));
        menu.addSeparator ();
        menu.add (new JMenuItem (new HaltAction()));
        if (isAnimationAvailable) {
          menu.addSeparator ();
          menu.add (new JMenuItem (animationOn));
          menu.add (new JMenuItem (animationPause));
        }
        menu.addSeparator ();
        menu.add (new JMenuItem (runSlower));
        menu.add (new JMenuItem (runFaster));
	menu.addSeparator ();
	menu.add (new JMenuItem (new ClearHighlightsAction ()));
	menu = new JMenu ("Debug");
	menu.add (new JMenuItem (new ClearAllBreakpointsAction ()));
	add (menu);
        if (!IS_MAC_OS_X) {
          menu = new JMenu ("Help");
          menu.add (new JMenuItem (new AboutAction()));
          add (menu);
        }
      }
    }
    
    /**
     * Application's ToolBar.
     */
    class ToolBar extends JToolBar {
      ToolBar () {
	setBackground (toolBarColor);
	setBorder (new CompoundBorder (new MatteBorder (0,0,1,0, toolBarBorder), getBorder ()));
	add (new OpenAction ());
	add (new SaveAction ());
	add (new SaveAsAction ());
	add (new RestoreDataFromCheckpointAction ());
	add (new CheckpointDataAction ());
	addSeparator ();
	add (run);
	add (new StepAction ());
	add (new RunSlowlyAction ());
        addSeparator ();
        add (new HaltAction ());
        if (isAnimationAvailable) {
          addSeparator ();
          add (animationOn);
          add (animationPause);
        }
	addSeparator ();
	add (runSlower);
	add (runFaster);
      }
    }
    
    /**
     * Application's StatusBar.
     */
    class StatusBar extends JPanel implements MessageBoard {
      JLabel label;
      StatusBar () {
	super (new GridLayout (1,1));
	setBorder (new CompoundBorder (new MatteBorder (1,0,0,0, statusBarBorder), new EmptyBorder (2,4,2,4)));
	setBackground (BACKGROUND_COLOR);
	label = new JLabel ();
	label.setFont (statusMessageFont);
	label.setHorizontalAlignment (SwingConstants.LEFT);
	label.setText (" ");
	add (label);
      }
      public void showMessage (String message) {
	label.setText (message!=null && !message.trim().equals("")? message : " ");
      }
    }
    
    /**
     * Drag and Drop Support
     */
    {
      setTransferHandler (new javax.swing.TransferHandler() {
        @Override public boolean canImport (javax.swing.TransferHandler.TransferSupport support) {
          return true;
        }
        @Override public boolean importData (javax.swing.TransferHandler.TransferSupport support) {
          Transferable tf = support.getTransferable();
          for (DataFlavor df : tf.getTransferDataFlavors()) 
            try {
              Object data = tf.getTransferData (df);
              if (data instanceof String) {
                if (!isRunning && quit()) 
                  loadFile (URLDecoder.decode ((new URL ((String) data)).getFile(), "UTF-8"));
                break;
              }
            } catch (UnsupportedFlavorException e) {
            } catch (IOException e) {}
          return true;          
        }
      });
    }
    
    /**
     * Frame maximizes to its preferred size and current position.
     */
    
    void setMaximizedBounds () {
      setMaximizedBounds (new Rectangle (Application.this.getX(),
                                         Application.this.getY(),
                                         Math.min (getMaximizedWidth(),  screenWidth),
                                         Math.min (getMaximizedHeight(), screenHeight)));
    }
    
    /**
     * Determined maximized width.  Tricky, because we want maximized to be two-column, if possible, but
     * if the frame is currently displaying one column, then getPreferredSize().width will not
     * be the preferred width.
     * @see AllMemoryPane
     */
    
    int getMaximizedWidth() {
      return mainPane.getMaximizedWidth();
    }
    
    /**
     * Determine maximized height.  Tricky, like getMaximizedWidth().
     */
    
    int getMaximizedHeight() {
      int oh = Application.this.getPreferredSize().height - mainPane.getPreferredSize().height;
      return mainPane.getMaximizedHeight() + oh;
    }
    
    /**
     * Add componentMove listener to ensure that maximized bounds reflects current position.
     */
    
    {
      addComponentListener (new ComponentAdapter () {
        @Override public void componentMoved (ComponentEvent e) {
          Application.this.setMaximizedBounds ();
        }
      });
    }
    
    /**
     * Fire the isRunningChanged event.
     */
    void fireIsRunningChanged () {
      for (IsRunningListener l : isRunningListeners)
	l.isRunningChanged ();
    }
    
    /**
     * Deterine which memory pane is currently selected.
     */
    
    ViewPane getSelectedPane () {
      return mainPane.allMemoryPane.getSelectedPane ();
    }
    
    /**
     * Tell table to stop editing.
     */
    
    boolean stopEditing () {
      ViewPane sp = getSelectedPane ();
      if (sp != null && sp.view.getCellEditor () != null)
      	return sp.view.getCellEditor ().stopCellEditing ();
      else
	return true;
    }
    
    /**
     * Manage selection-listener list of all views.
     */
    
    class MasterSelectionListner extends Vector <View.SelectionListener> implements View.SelectionListener {
      public void selectionMayHaveChanged (boolean isKnownToBeSelected) {
	for (View.SelectionListener l : this)
	  l.selectionMayHaveChanged (isKnownToBeSelected);
      }
    }
    
    /**
     * Enforce minimum size constraint.
     */
    
    {
      addComponentListener (new ComponentAdapter() {
        @Override public void componentResized (ComponentEvent ce) {
          setSize (Math.max (getMinimumSize().width, getWidth()), 
                   Math.max (getMinimumSize().height, getHeight()));
        }
      });
    }
    
    /////////////////////////
    // ACTIONS
  
    /**
     * Open file dialog action.
     */
    class OpenAction extends AbstractAction implements IsRunningListener {
      FileDialog aFileDialog;
      public OpenAction () {
	putValue (NAME,              "Open...");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_O);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_O, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Open an assembly- or machine-lanuage file.");
	isRunningListeners.add (this);
        
        aFileDialog = new FileDialog (Application.this, "Open", FileDialog.LOAD);
        if (IS_MS_WINDOWS)
          aFileDialog.setFile ("*.s; *.machine");
        aFileDialog.setFilenameFilter (new FilenameFilter () {
          @Override public boolean accept (File dir, String name) {
            return name.matches (".*\\.(s|machine)");
          }
        });
      }
      public void actionPerformed (ActionEvent e) {
 	if (quit ()) {
	  aFileDialog.setVisible (true);
	  if (aFileDialog.getFile () != null) {
	    String pathname = aFileDialog.getDirectory ().concat (aFileDialog.getFile ());
            loadFile (pathname);
	  }
	}
      }
      public void isRunningChanged () {
	setEnabled (! isRunning);
      }
    }
    
    /**
     * Save file action.
     */
    class SaveAction extends AbstractAction implements Memory.StateChangedListener, IsRunningListener {
      public SaveAction () {
	putValue (NAME,              "Save");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_S);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_S, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Save assembly code to input file.");
	setEnabled (false);
	machine.memory.addStateChangedListener (this);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	try {
	  machine.memory.saveToFile (null);
	} catch (FileNotFoundException fnfe) {
	  throw new AssertionError ();
	}
	statusBar.showMessage ("Saved to assembly file.");
      }
      public void isRunningChanged () {
	setEnabled (! isRunning  && machine.memory.hasLoadedFile () && machine.memory.hasUnsavedChanges ());
      }
      public void memoryStateChanged () {
	setEnabled (! isRunning && machine.memory.hasLoadedFile () && machine.memory.hasUnsavedChanges ());
        getRootPane().putClientProperty ("Window.documentModified", 
                                         machine.memory.hasUnsavedChanges());
      }
    }
    
    /**
     * SaveAs file dialog action.
     */
    class SaveAsAction extends AbstractAction implements IsRunningListener {
      public SaveAsAction () {
	putValue (NAME,              "Save As...");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_A);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_S, platformAgnosticMetaMask| ActionEvent.SHIFT_MASK));
	putValue (SHORT_DESCRIPTION, "Save assembly code to specified file.");
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	FileDialog fd = new FileDialog (Application.this, "Save As", FileDialog.SAVE);
	fd.setFilenameFilter (
	  new java.io.FilenameFilter() {
	    public boolean accept (java.io.File dir, String name) {
	      return name.matches (".*\\.(s|machine)");
	    }
	  });
	fd.setVisible (true);
	if (fd.getFile () != null) {
	  try {
	    machine.memory.saveToFile (fd.getDirectory ().concat (fd.getFile ()));
	  } catch (FileNotFoundException fnfe) {
	    statusBar.showMessage ("File not found.");
	  }
	  statusBar.showMessage ("Saved to assembly file.");
	} 
      }
      public void isRunningChanged () {
	setEnabled (! isRunning);
      }
    }
    
    /**
     * Quit
     */
    class QuitAction extends AbstractAction {
      public QuitAction () {
        putValue (NAME, IS_MS_WINDOWS? "Exit": "Quit");
        putValue (MNEMONIC_KEY, IS_MS_WINDOWS? KeyEvent.VK_X: KeyEvent.VK_Q);
        putValue (SHORT_DESCRIPTION, (IS_MS_WINDOWS? "Exit": "Quit").concat (" application."));
        if (!IS_MS_WINDOWS)
          putValue (ACCELERATOR_KEY, KeyStroke.getKeyStroke (KeyEvent.VK_Q, platformAgnosticMetaMask));
      }
      @Override public void actionPerformed (ActionEvent e) {
        if (quit())
          System.exit (0);
      }
    }
    
    /**
     * About 
     */
    class AboutAction extends AbstractAction {
      public AboutAction () {
        putValue (NAME, "About ".concat (APPLICATION_NAME));
        putValue (MNEMONIC_KEY, KeyEvent.VK_A);
        putValue (SHORT_DESCRIPTION, "About this application.");
      }
      @Override public void actionPerformed (ActionEvent w) {
        showAbout ();
      }
    }
    
    /**
     * Checkpoint Data
     */
    class CheckpointDataAction extends AbstractAction implements Memory.StateChangedListener, IsRunningListener {
      public CheckpointDataAction () {
	putValue (NAME,              "Checkpoint Data");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_C);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_C, platformAgnosticMetaMask | ActionEvent.SHIFT_MASK));
	putValue (SHORT_DESCRIPTION, "Checkpoint data regions.");
	isRunningListeners.add (this);
	setEnabled (false);
	machine.memory.addStateChangedListener (this);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	machine.memory.checkpointData (true);
	statusBar.showMessage ("Checkpointed.");
      }
      public void isRunningChanged () {
	setEnabled (! isRunning  && machine.memory.hasLoadedFile ());
      }
      public void memoryStateChanged () {
	setEnabled (! isRunning && machine.memory.hasLoadedFile ());
      }
    }
    
    /**
     * Restore Data from Checkpoint
     */
    class RestoreDataFromCheckpointAction extends AbstractAction implements Memory.StateChangedListener, IsRunningListener {
      public RestoreDataFromCheckpointAction () {
	putValue (NAME,              "Reset Data");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_R);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_C, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Restore all data regions from their most recent checkpoint.");
	isRunningListeners.add (this);
	setEnabled (false);
	machine.memory.addStateChangedListener (this);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	if (stopEditing ()) {
	  machine.memory.restoreDataFromCheckpoint ();
	  statusBar.showMessage ("Data restored from most recent checkpoint.");
	}
      }
      public void isRunningChanged () {
	setEnabled (! isRunning  && machine.memory.hasLoadedFile ());
      }
      public void memoryStateChanged () {
	setEnabled (! isRunning && machine.memory.hasLoadedFile ());
      }
    }
    
    /**
     * Undo action.
     */
    class UndoAction extends AbstractAction implements StateChangedListener {
      public UndoAction () {
	putValue (NAME,              "Undo");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_U);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_Z, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Undo last change.");
	setEnabled (undoManager.canUndo ());
	undoManager.addStateChangedListener (this);
      }
      public void actionPerformed (ActionEvent e) {
	  undoManager.undo ();
      }
      public void stateChanged (Object o) {
	assert o == undoManager;
	if (undoManager.canUndo ()) {
	  putValue (NAME, undoManager.getUndoPresentationName ());
	  setEnabled (true);
	} else {
	  putValue (NAME, "Undo");
	  setEnabled (false);
	}
	setEnabled (undoManager.canUndo ());
      }
    }
    
    /**
     * Redo action.
     */
    class RedoAction extends AbstractAction implements StateChangedListener {
      public RedoAction () {
	putValue (NAME,              "Redo");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_R);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_Z, platformAgnosticMetaMask | ActionEvent.SHIFT_MASK));
	putValue (SHORT_DESCRIPTION, "Redo change.");
	setEnabled (undoManager.canRedo ());
	undoManager.addStateChangedListener (this);
      }
      public void actionPerformed (ActionEvent e) {
	undoManager.redo ();
      }
      public void stateChanged (Object o) {
	assert o == undoManager;
	if (undoManager.canRedo ()) {
	  putValue (NAME, undoManager.getRedoPresentationName ());
	  setEnabled (true);
	} else {
	  putValue (NAME, "Redo");
	  setEnabled (false);
	}
      }
    }
    
    /**
     * Increase font size action.
     */
    class IncreaseFontSizeAction extends AbstractAction implements IsRunningListener {
      public IncreaseFontSizeAction () {
	putValue (NAME, "Make Text Bigger");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_B);
	putValue (ACCELERATOR_KEY, KeyStroke.getKeyStroke (KeyEvent.VK_PLUS, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Increase font size of all text.");
	isRunningListeners.add (this);
	setEnabled (true);
      }
      public void actionPerformed (ActionEvent e) {
	mainPane.adjustFontSize (1);
      }
      public void isRunningChanged () {
	setEnabled (! isRunning);
      }
    }
    
    /**
     * Increase font size action.
     */
    class IncreaseFontSizeAction0 extends IncreaseFontSizeAction {
      public IncreaseFontSizeAction0 () {
	super ();
	putValue (NAME, "Make Text Bigger");
	putValue (ACCELERATOR_KEY, KeyStroke.getKeyStroke (KeyEvent.VK_EQUALS, platformAgnosticMetaMask));
      }
    }
    
    /**
     * Increase font size action.
     */
    class IncreaseFontSizeAction1 extends IncreaseFontSizeAction {
      public IncreaseFontSizeAction1 () {
	super ();
	putValue (NAME, "Make Text Bigger");
	putValue (ACCELERATOR_KEY, KeyStroke.getKeyStroke (KeyEvent.VK_EQUALS, platformAgnosticMetaMask | ActionEvent.SHIFT_MASK));
      }
    }
    
    /**
     * Decrease font size action.
     */
    class DecreaseFontSizeAction extends AbstractAction implements IsRunningListener {
      public DecreaseFontSizeAction () {
	putValue (NAME, "Make Text Smaller");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_S);
	putValue (ACCELERATOR_KEY, KeyStroke.getKeyStroke (KeyEvent.VK_MINUS, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Decrease font size of all text.");
	isRunningListeners.add (this);
	setEnabled (true);
      }
      public void actionPerformed (ActionEvent e) {
	mainPane.adjustFontSize (-1);
      }
      public void isRunningChanged () {
	setEnabled (! isRunning);
      }
    }
    
    /**
     * Run action.
     */
    
    class RunAction extends AbstractAction implements Memory.StateChangedListener, IsRunningListener {
      public RunAction () {
	putValue (NAME, "Run");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_R);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_R, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Start the CPU running from current PC.");
        setEnabled (false);
        machine.memory.addStateChangedListener (this);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	new Thread () {
	  @Override
	  public void run () {
	    statusBar.showMessage ("Running ...");
            boolean wasAnimating = animator.isEnabled();
            animator.setEnabled (false);
	    setRunning (true);
            statusBar.showMessage (machine.run (false, 0)); 
 	    setRunning (false);
            animator.setEnabled (wasAnimating);
	  }
	}.start ();
      }
      public void isRunningChanged () {
	setEnabled (! isRunning && machine.memory.hasLoadedFile ());
      }
      public void memoryStateChanged () {
	setEnabled (! isRunning && machine.memory.hasLoadedFile ());
      }
    }
    
    /**
     * Run slowly action.
     */
    class RunSlowlyAction extends AbstractAction implements Memory.StateChangedListener, IsRunningListener {
      public RunSlowlyAction () {
	putValue (NAME, "Run Slowly");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_U);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_R, platformAgnosticMetaMask | ActionEvent.SHIFT_MASK));
	putValue (SHORT_DESCRIPTION, "Animate running the CPU from current PC.");
        setEnabled (false);
        machine.memory.addStateChangedListener (this);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	new Thread () {
	  @Override public void run () {
            if (animator.isEnabled()) {
              animator.resume();
              machine.setPauseMilliseconds (0);
            }
            else if (machine.getPauseMilliseconds() < PAUSE_MIN_MS)
              machine.setPauseMilliseconds (PAUSE_MS);
	    statusBar.showMessage (String.format ("Running with %s ...",
                                                  animator.isEnabled()? 
                                                  String.format ("%3.1fs long animation", animator.getDuration()/1000.0):
                                                  String.format ("%3.1fs pause", machine.getPauseMilliseconds()/1000.0)));
 	    setRunning (true);
            statusBar.showMessage (machine.run (false, machine.getPauseMilliseconds())); 
            animator.clear();
	    setRunning (false);
	  }
	}.start ();
      }
      public void isRunningChanged () {
	setEnabled (! isRunning && machine.memory.hasLoadedFile ());
      }
      public void memoryStateChanged () {
	setEnabled (! isRunning && machine.memory.hasLoadedFile ());
      }
    }
    
    /**
     * Run faster.
     */
    class RunFasterAction extends AbstractAction implements IsRunningListener, PropertyChangeListener {
      public RunFasterAction () {
	putValue (NAME, "Faster");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_F);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_OPEN_BRACKET, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Run faster.");   
	setEnabled (false);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
        if (animator.isEnabled()) {
          animator.setDuration (Math.max (PAUSE_MIN_MS, animator.getDuration() - PAUSE_INC_MS));
          if (isRunning)
            statusBar.showMessage (String.format ("Running with %3.1fs long animation ...", animator.getDuration()/1000.0));
          else
            statusBar.showMessage (String.format ("Animation is %3.1fs.", animator.getDuration()/1000.0));
        } else {
          machine.setPauseMilliseconds (Math.max (0, machine.getPauseMilliseconds() - PAUSE_INC_MS));
          statusBar.showMessage (String.format ("Running with %3.1fs pause ...", machine.getPauseMilliseconds()/1000.0));
        }
	fireIsRunningChanged();
      }
      public void computeEnabled () {
        boolean isEnabled;
        if (animator.isEnabled()) {
          isEnabled = animator.getDuration() > PAUSE_MIN_MS;
        } else {
          isEnabled = isRunning && machine.getPauseMilliseconds() > 0;
        }
        setEnabled (isEnabled);
       }
      public void isRunningChanged () {
	computeEnabled();
      }
      @Override public void propertyChange (PropertyChangeEvent e) {
        computeEnabled();
      }
    }
    
    /**
     * Run slower.
     */
    class RunSlowerAction extends AbstractAction implements IsRunningListener, PropertyChangeListener {
      public RunSlowerAction () {
	putValue (NAME, "Slower");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_S);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_CLOSE_BRACKET, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Run slower.");    
	setEnabled (false);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
        if (animator.isEnabled()) {
          animator.setDuration (Math.max (PAUSE_MIN_MS, animator.getDuration() + PAUSE_INC_MS));
          if (isRunning)
            statusBar.showMessage (String.format ("Running with %3.1fs long animation ...", animator.getDuration()/1000.0));
          else
            statusBar.showMessage (String.format ("Animation is %3.1fs.", animator.getDuration()/1000.0));
        } else {
          machine.setPauseMilliseconds (Math.max (0, machine.getPauseMilliseconds() + PAUSE_INC_MS));
          statusBar.showMessage (String.format ("Running with %3.1fs pause ...", machine.getPauseMilliseconds()/1000.0));
        }
	fireIsRunningChanged ();
      }
      public void computeEnabled() {
        boolean isEnabled;
        if (animator.isEnabled()) {
          isEnabled = animator.getDuration() < PAUSE_MAX_MS;
        } else {
          isEnabled = isRunning && machine.getPauseMilliseconds() < PAUSE_MAX_MS;
        }
        setEnabled (isEnabled);
      }
      @Override public void isRunningChanged () {
	computeEnabled ();
      }
      @Override public void propertyChange (PropertyChangeEvent e) {
        computeEnabled();
      }
    }
    
    /**
     * Animation Enable/Disable Action.
     */
    
    class AnimationOnAction extends AbstractAction implements Memory.StateChangedListener, PropertyChangeListener {
      final static String ON_NAME         = "Show Animation";
      final static String OFF_NAME        = "Hide Animation";
      final static int    ON_MNEMONIC     = KeyEvent.VK_A;
      final static int    OFF_MNEMONIC    = KeyEvent.VK_H;
      final static String ON_DESCRIPTION  = "Turn instruction animation on.";
      final static String OFF_DESCRIPTION = "Turn instruction animation off.";
      public AnimationOnAction () {
	putValue (NAME,              ON_NAME);
	putValue (MNEMONIC_KEY,      ON_MNEMONIC);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_D, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, ON_DESCRIPTION);    
	setEnabled (false);
        machine.memory.addStateChangedListener (this);
      }
      public void actionPerformed (ActionEvent e) {
        if (animator.isEnabled()) {
          animator.setEnabled (false);
          machine.setPauseMilliseconds (animator.getDuration());
          if (isRunning)
            statusBar.showMessage (String.format ("Running with %3.1fs duration.", 
                                                  machine.getPauseMilliseconds()/1000.0));
          else
            statusBar.showMessage (String.format ("Animation disabled."));
        } else {
          animator.setDuration         (machine.getPauseMilliseconds());
          if (animator.getDuration() < PAUSE_MIN_MS)
            animator.setDuration (PAUSE_MS);
          animator.setEnabled          (true);
          machine.setPauseMilliseconds (0);
          if (isRunning)
            statusBar.showMessage (String.format ("Running with %3.1fs long animation ...", animator.getDuration()/1000.0));
          else
            statusBar.showMessage (String.format ("Animation enabled with %3.1fs duration ...", animator.getDuration()/1000.0));
        }
        computeState();
      }
      public void computeState () {
        setEnabled (machine.memory.hasLoadedFile());
        if (animator.isEnabled()) {
          putValue (NAME,              OFF_NAME);
          putValue (MNEMONIC_KEY,      OFF_MNEMONIC);
          putValue (SHORT_DESCRIPTION, OFF_DESCRIPTION);
        } else {
          putValue (NAME,              ON_NAME);
          putValue (MNEMONIC_KEY,      ON_MNEMONIC);
          putValue (SHORT_DESCRIPTION, ON_DESCRIPTION);
        }
      }
      public void memoryStateChanged () {
        computeState();
      }
      @Override public void propertyChange (PropertyChangeEvent e) {
        computeState();
      }
    }
    
    /**
     * Animation Pause/Resume Action.
     */

    class AnimationPauseAction extends AbstractAction implements IsRunningListener, PropertyChangeListener {
      final static String PAUSE_NAME         = "Pause";
      final static String RESUME_NAME        = "Resume";
      final static int    PAUSE_MNEMONIC     = KeyEvent.VK_P;
      final static int    RESUME_MNEMONIC    = KeyEvent.VK_R;
      final static String PAUSE_DESCRIPTION  = "Pause instruction animation.";
      final static String RESUME_DESCRIPTION = "Resume instruction animation.";
      public AnimationPauseAction () {
	putValue (NAME,              PAUSE_NAME);
	putValue (MNEMONIC_KEY,      PAUSE_MNEMONIC);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_P, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, PAUSE_DESCRIPTION);    
	setEnabled (false);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
        if (animator.isPaused()) {
          animator.resume();
          statusBar.showMessage (String.format ("Running with %3.1fs long animation ...", animator.getDuration()/1000.0));          
        } else {
          animator.pause();
          statusBar.showMessage ("Animation paused.");
        }
        computeState();
      }
      public void computeState () {
        setEnabled (animator.isEnabled() && isRunning);
        if (animator.isEnabled() && isRunning && animator.isPaused()) {
          putValue (NAME,              RESUME_NAME);
          putValue (MNEMONIC_KEY,      RESUME_MNEMONIC);
          putValue (SHORT_DESCRIPTION, RESUME_DESCRIPTION);          
        } else {
          putValue (NAME,              PAUSE_NAME);
          putValue (MNEMONIC_KEY,      PAUSE_MNEMONIC);
          putValue (SHORT_DESCRIPTION, PAUSE_DESCRIPTION);
        }
      }
      @Override public void isRunningChanged () {
	computeState ();
      }
      @Override public void propertyChange (PropertyChangeEvent e) {
        computeState();
      }
    }
    
    /**
     * Halt action.
     */
    class HaltAction extends AbstractAction implements IsRunningListener {
      public HaltAction () {
	putValue (NAME, "Halt");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_H);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_PERIOD, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Halt CPU execution.");   
	setEnabled (false);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	machine.stop  ();
        animator.stop ();
      }
      public void isRunningChanged () {
	setEnabled (isRunning);
      }
    }
    
    /**
     * Step action.
     */
    
    class StepAction extends AbstractAction implements Memory.StateChangedListener, IsRunningListener {
      public StepAction () {
	putValue (NAME, "Step");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_S);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_S, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Execute one instruction and stop.");
        setEnabled (false);
        machine.memory.addStateChangedListener (this);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	statusBar.showMessage ("");
	setRunning (true);
        animator.resume();
        statusBar.showMessage (machine.run (true, 0));
        animator.clear();
	setRunning (false);
      }
      public void isRunningChanged () {
	setEnabled (!isRunning && machine.memory.hasLoadedFile ());
      }
      public void memoryStateChanged () {
	setEnabled (!isRunning && machine.memory.hasLoadedFile ());
      }
    }
    
    /** 
     * Goto PC action.
     */
    class GotoAction extends AbstractAction implements IsRunningListener {
      public GotoAction () {
	putValue (NAME, "Goto");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_G);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_G, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Set PC to specified address."); 
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	;
      }
      public void isRunningChanged () {
	setEnabled (! isRunning);
      }
    }
    
    /**
     * Set breakpoint action.
     */
    class SetBreakpointAction extends AbstractAction {
      public SetBreakpointAction () {
	putValue (NAME, "Set Breakpoint");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_B);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_B, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Set breakpoint."); 
      }
      public void actionPerformed (ActionEvent e) {
	;
      }
    }
    
    /**
     * Clear breakpoint action.
     */
    class ClearBreakpointAction extends AbstractAction {
      public ClearBreakpointAction () {
	putValue (NAME, "Clear Breakpoint");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_C);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_C, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Clear breakpoint."); 
      }
      public void actionPerformed (ActionEvent e) {
	;
      }
    }
    
    /**
     * Clear all breakpoints action.
     */
    class ClearAllBreakpointsAction extends AbstractAction {
      public ClearAllBreakpointsAction () {
	putValue (NAME, "Clear Breaks");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_L);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_C, platformAgnosticMetaMask | ActionEvent.SHIFT_MASK));
	putValue (SHORT_DESCRIPTION, "Clear all breakpoints."); 
      }
      public void actionPerformed (ActionEvent e) {
	machine.clearAllDebugPoints (Machine.DebugType.BREAK);
	statusBar.showMessage ("All breakpoints cleared.");
      }
    }
    
    /**
     * Clear highlights
     */
    class ClearHighlightsAction extends AbstractAction {
      public ClearHighlightsAction () {
	putValue (NAME, "Erase Highlights");
	putValue (MNEMONIC_KEY,      KeyEvent.VK_H);
	putValue (ACCELERATOR_KEY,   KeyStroke.getKeyStroke (KeyEvent.VK_H, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Erase highlights."); 
      }
      public void actionPerformed (ActionEvent e) {
	adjustHighlights (true);
      }
    }
    
    /**
     * Insert Row in Instruction or Data view at currently selected row
     */
    class InsertAboveAction extends AbstractAction implements IsRunningListener, View.SelectionListener {
      public InsertAboveAction () {
	putValue (NAME, "Insert Above");
	putValue (MNEMONIC_KEY, KeyEvent.VK_B);
	putValue (ACCELERATOR_KEY, KeyStroke.getKeyStroke (KeyEvent.VK_ENTER, platformAgnosticMetaMask | ActionEvent.ALT_MASK));
	putValue (SHORT_DESCRIPTION, "Insert row above selected row.");
	setEnabled (false);
	masterSelectionListener.add (this);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	ViewPane pane = getSelectedPane ();
	if (pane != null) {
	  if (stopEditing ()) {
	    boolean inserted = pane.view.insertAboveSelection ();
	    if (inserted) {
	      statusBar.showMessage ("Row inserted.");
	    } else 
	      statusBar.showMessage ("Unable to insert new row.");
	  } 
	} else
	  statusBar.showMessage ("No row selected for Insert.");	  	
      }
      private void setEnabled () {
	setEnabled (! isRunning && getSelectedPane () != null);
      }
      public void isRunningChanged () {
	setEnabled ();
      }
      public void selectionMayHaveChanged (boolean isKnownToBeSelected) {
	if (isKnownToBeSelected)
	  setEnabled (! isRunning);
	else
	  setEnabled ();
      }
    }
    
    /**
     * Insert Row in Instruction or Data view below currently selected row
     */
    class InsertBelowAction extends AbstractAction implements IsRunningListener, View.SelectionListener {
      public InsertBelowAction () {
	putValue (NAME, "Insert Below");
	putValue (MNEMONIC_KEY, KeyEvent.VK_B);
	putValue (ACCELERATOR_KEY, KeyStroke.getKeyStroke (KeyEvent.VK_ENTER, platformAgnosticMetaMask));
	putValue (SHORT_DESCRIPTION, "Insert row below selected row.");
	setEnabled (false);
	masterSelectionListener.add (this);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	ViewPane pane = getSelectedPane ();
	if (pane != null) {
	  if (stopEditing ()) {
	    boolean inserted = pane.view.insertBelowSelection ();
	    if (inserted) {
	      statusBar.showMessage ("Row inserted.");
	    } else 
	      statusBar.showMessage ("Unable to insert new row.");
	  }
	} else
	  statusBar.showMessage ("No row selected for Insert Before.");
      }
      private void setEnabled () {
	setEnabled (! isRunning && getSelectedPane () != null);
      }
      public void isRunningChanged () {
	setEnabled ();
      }
      public void selectionMayHaveChanged (boolean isKnownToBeSelected) {
	if (isKnownToBeSelected)
	  setEnabled (! isRunning);
	else
	  setEnabled ();
      }
    }

    /**
     * Delete Row in Instruction or Data view
     */
    class DeleteAction extends AbstractAction implements IsRunningListener, View.SelectionListener {
      public DeleteAction () {
	putValue (NAME, "Delete");
	putValue (MNEMONIC_KEY, KeyEvent.VK_D);
        if (IS_MS_WINDOWS)
          putValue (ACCELERATOR_KEY, KeyStroke.getKeyStroke (KeyEvent.VK_BACK_SPACE, ActionEvent.ALT_MASK));
        else
          putValue (ACCELERATOR_KEY, KeyStroke.getKeyStroke (KeyEvent.VK_BACK_SPACE, platformAgnosticMetaMask));          
	putValue (SHORT_DESCRIPTION, "Delete row.");
	setEnabled (false);
	masterSelectionListener.add (this);
	isRunningListeners.add (this);
      }
      public void actionPerformed (ActionEvent e) {
	ViewPane pane = getSelectedPane ();
	if (pane != null) {
	  if (stopEditing ()) {
	    boolean deleted = pane.view.deleteSelection ();
	    if (! deleted) 
	      statusBar.showMessage ("Unable to delete row.");
	  }
	} else 
	  statusBar.showMessage ("No row selected for Delete.");
      }
      private void setEnabled () {
	setEnabled (! isRunning && getSelectedPane () != null);
      }
      public void isRunningChanged () {
	setEnabled ();
      }
      public void selectionMayHaveChanged (boolean isKnownToBeSelected) {
	if (isKnownToBeSelected)
	  setEnabled (! isRunning);
	else
	  setEnabled ();
      }
    }
    
    //////////////////
    // Action Support
    
    /**
     * Checkbox column data model for setting instruction or data breakpoints
     */
    class BreakpointControlModel extends AbstractDataModel implements Observer {
      Region             region;
      Machine.DebugPoint debugPoint;
      BreakpointControlModel (Region aRegion) {
	region     = aRegion;
	debugPoint = region.getType () == Region.Type.INSTRUCTIONS? Machine.DebugPoint.INSTRUCTION : Machine.DebugPoint.MEMORY_READ;
	machine.addDebugPointObserver (this);
      }
      /**
       * Insert row into breakpoint control model.
       * Row must already have been inserted into region and breakpoints adjusted.
       */
      @Override
      public boolean insertRow (int row) {
	for (int r = row+1; r<region.length (); r++) 
	  if (machine.isDebugPointEnabled (Machine.DebugType.BREAK, debugPoint, region.getCellForRowIndex (r).getAddress ()))
	    tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, Arrays.asList (new TableCellIndex (r-1,0), new TableCellIndex (r,0))));	    
	return true;
      }
      /**
       * Delete row from breakpoint control model. 
       * Row must already have been delete from region and breakpoints adjusted.
       */
      @Override
      public boolean deleteRow (int row) {
	for (int r = row; r<region.length () - 1; r++)
	  if (machine.isDebugPointEnabled (Machine.DebugType.BREAK, debugPoint, region.getCellForRowIndex (r).getAddress ())) 
	    tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, Arrays.asList (new TableCellIndex (r,0), new TableCellIndex (r+1,0))));
	return true;
      }
      @Override
      public boolean canInsertRow (int row) {
	return true;
      }
      @Override
      public boolean canDeleteRow (int row) {
	return true;
      }
      public void update (Observable o, Object arg) {
	Vector<TableCellIndex> updatedCells = new Vector<TableCellIndex> ();
	DataModelEvent event = (DataModelEvent) arg;
	if (event.getType () == DataModelEvent.Type.WRITE || event.getType () == DataModelEvent.Type.WRITE_BY_USER)
	  for (TableCellIndex cell : event.getCells ()) 
	    updatedCells.add (new TableCellIndex (region.getRowIndexForAddress (cell.rowIndex), 0));
	tellObservers (new DataModelEvent (event.getType (), updatedCells));
      }
      public Class   getColumnClass (int columnIndex) {
	return Boolean.class;
      }
      public int     getColumnCount () {
	return 1;
      }
      public String  getColumnName  (int columnIndex) {
	return "B";
      }
      public int     getRowCount    () {
	return region.getRowCount ();
      }
      public Object  getValueAt     (int rowIndex, int columnIndex) {
	return machine.isDebugPointEnabled (Machine.DebugType.BREAK, debugPoint, region.getCellForRowIndex (rowIndex).getAddress ());
      }
      public boolean isCellEditable (int rowIndex, int columnIndex) {
	return true;
      }
      public void    setValueAt     (Object aValue, int rowIndex, int columnIndex) {
	machine.setDebugPoint (Machine.DebugType.BREAK, debugPoint, region.getCellForRowIndex (rowIndex).getAddress(), (Boolean) aValue);
      }
    }
    
    /**
     * Undo Manager.
     */
    
    class ApplicationUndoManager extends UndoManager {
      private Vector<StateChangedListener> stateChangedListeners = new Vector<StateChangedListener> ();
      private void fireStateChanged () {
	for (StateChangedListener l : stateChangedListeners)
	  l.stateChanged (this);
      }
      void addStateChangedListener (StateChangedListener l) {
	stateChangedListeners.add (l);
      }
      @Override
      public boolean addEdit (UndoableEdit e) {
	boolean r = super.addEdit (e);
	if (r) 
	  fireStateChanged ();
	return r;
      }
      @Override
      public void undo () {
	super.undo ();
	fireStateChanged ();
      }
      @Override
      public void redo () {
	super.redo ();
	fireStateChanged ();
      }
    }
    
    /////////////////
    // PANES
    
    /**
     * MainPane fills the entire frame.
     */
    
    class MainPane extends JPanel {
      CpuPane              cpuPane;
      AllMemoryPane        allMemoryPane;
      int                  fontSizeAdjustment = 0;
      MainPane () {
	setLayout (new BoxLayout (this, BoxLayout.LINE_AXIS));
	cpuPane       = new CpuPane ();
	allMemoryPane = new AllMemoryPane ();
	cpuPane.setAlignmentY (0);
	allMemoryPane.setAlignmentY (0);
	add (cpuPane);
 	add (allMemoryPane);
      }
      void adjustHighlights (boolean clear) {
	cpuPane.adjustHighlights (clear);
	allMemoryPane.adjustHighlights (clear);
      }
      void adjustFontSize (int increment) {
        if (fontSizeAdjustment>-7 || increment>0) {
          fontSizeAdjustment += increment;
          cpuPane.adjustFontSize (increment);
          allMemoryPane.adjustFontSize (increment);
        }
      }
      int getMaximizedWidth() {
        return cpuPane.getMaximizedWidth() + allMemoryPane.getMaximizedWidth();
      }
      int getMaximizedHeight() {
        return Math.max (cpuPane.getMaximizedHeight(), allMemoryPane.getMaximizedHeight());
      }
    } 

    /**
     * CPU frame contains all processor state.
     */
    
    class CpuPane extends JPanel {
      RegisterPane           registerPane;
      RegisterViewPane       registerViewPane;
      ProcessorStatePane     processorStatePane;
      JPanel                 strut = null;
      
      CpuPane () {
	setLayout (new BoxLayout (this, BoxLayout.LINE_AXIS));
	JPanel col0 = new JPanel ();
	JPanel col1 = null;
	col0.setLayout (new BoxLayout (col0, BoxLayout.PAGE_AXIS));
	col0.setBorder (new MatteBorder (4,0,2,0,Application.this.getBackground ()));
	col0.setAlignmentY (0);
 	registerPane           = new RegisterPane      ();
	registerViewPane       = new RegisterViewPane  ();
	processorStatePane     = new ProcessorStatePane ();
	JPanel p = new JPanel ();
	p.setLayout (new BoxLayout (p, BoxLayout.LINE_AXIS));
	p.setAlignmentX (0);
	registerPane.setAlignmentY     (0);
	registerViewPane.setAlignmentY (0);
	JPanel regGrp  = new JPanel ();
	regGrp.setLayout (new BoxLayout (regGrp, BoxLayout.LINE_AXIS));
	Dimension sz = new Dimension (0,0);
	regGrp.add (new Box.Filler (sz,sz,sz));
	regGrp.add (registerPane);
	regGrp.add (registerViewPane);
	regGrp.add (new Box.Filler (sz,sz,sz));
        p.add (regGrp);
	if (isRegFileInOwnCol) {
	  col1 = new JPanel ();
	  col1.setLayout (new BoxLayout (col1, BoxLayout.PAGE_AXIS));
	  col1.setBorder (new CompoundBorder (new CompoundBorder (new CompoundBorder (new MatteBorder (0,1,0,0,strutColor.darker ()),	
										      new MatteBorder (0,3,0,0,strutColor)),
								  new MatteBorder (0,1,0,0,strutColor.darker ())),
					      new MatteBorder (4,0,2,0,Application.this.getBackground ())));
	  col1.add (p);
	  col1.add (Box.createVerticalGlue ());
	  col1.setAlignmentY (0);
	} else {
	  col0.add (p);
	  strut = new JPanel ();
          strut.setPreferredSize (new Dimension (getPreferredSize().width, 9));
	  strut.setBorder (new CompoundBorder (new MatteBorder (2,0,2,0, Application.this.getBackground ()),
					   new CompoundBorder (new MatteBorder (1,0,1,0,strutColor.darker ()),
							       new MatteBorder (2,0,2,0,strutColor))));
	  col0.add (strut);
	  strut.setAlignmentX (0);
	}
	p = new JPanel ();
	p.setLayout (new BoxLayout (p, BoxLayout.PAGE_AXIS));
	p.setAlignmentX (0);
        p.setAlignmentY (1);
	p.add (processorStatePane);
	col0.add (p);
	add (col0);
	if (col1 != null)
	  add (col1);
        setMaximumSize (new Dimension (getPreferredSize().width, getMaximumSize().height));
      }
      
      void adjustHighlights (boolean clear) {
	registerPane.adjustHighlights (clear);
	registerViewPane.adjustHighlights (clear);
	processorStatePane.adjustHighlights (clear);
      }
      
      void adjustFontSize (int increment) {
	registerPane.adjustFontSize       (increment);
	registerViewPane.adjustFontSize   (increment);
	processorStatePane.adjustFontSize (increment);
        setMaximumSize (new Dimension (getPreferredSize().width, getMaximumSize().height));
      }
      
      int getMaximizedWidth () {
        return getPreferredSize().width;
      }
      
      int getMaximizedHeight () {
        return getPreferredSize().height;
      }
    }
    
    /**
     * Scroll pane that adjust its with to fit vertical scroll bar when it appears, as needed.
     */
    
    class ScrollPane <T extends Component> extends JScrollPane {
      final T                   content;
       private final JScrollBar vsb = getVerticalScrollBar();
      ScrollPane (T aContent) {
        super (aContent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        content=aContent;
        setBorder (null);
        setMinimumSize   (new Dimension (content.getMinimumSize().width, getMinimumSize().height));
        setPreferredSize (new Dimension (content.getPreferredSize().width, getPreferredSize().height)); 
        setMaximumSize   (new Dimension (getMaximumSize().width, getPreferredSize().height));
        vsb.addComponentListener (new ComponentAdapter() {
          @Override public void componentHidden (ComponentEvent e) {
            setMinimumSize   (null);
            setMinimumSize   (new Dimension (content.getMinimumSize().width, getMinimumSize().height));
            setPreferredSize (null);
            setPreferredSize (new Dimension (content.getPreferredSize().width, getPreferredSize().height)); 
            setMaximumSize   (new Dimension (getMaximumSize().width, getPreferredSize().height));
            ((JComponent) getParent()).revalidate();
         }
          @Override public void componentShown (ComponentEvent e) {
            setMinimumSize   (null);
            setMinimumSize   (new Dimension (content.getMinimumSize().width+vsb.getWidth(), getMinimumSize().height));
            setPreferredSize (null);
            setPreferredSize (new Dimension (content.getPreferredSize().width+vsb.getWidth(), getPreferredSize().height)); 
            setMaximumSize   (new Dimension (getMaximumSize().width, getPreferredSize().height));
            ((JComponent) getParent()).revalidate();
          }
        });
      }
    }
    
    /**
     * Scrolled view of ProcessorStateContent.
     */
    
    class ProcessorStatePane extends ScrollPane <ProcessorStatePaneContent> {
      ProcessorStatePane () {
        super (new ProcessorStatePaneContent());
      }
      void adjustHighlights (boolean clear) {
        content.adjustHighlights (clear);
      }
      void adjustFontSize (int increment) {
        content.adjustFontSize (increment);
        setPreferredSize(null);
        setMaximumSize (new Dimension (getMaximumSize().width, getPreferredSize().height));
      }
    }
    
    /**
     * Processor registers (other than general-purpose register file.
     */
    class ProcessorStatePaneContent extends JPanel implements Scrollable {
      Vector <ViewPane> panes         = new Vector <ViewPane> ();
      Vector <JPanel>   struts        = new Vector <JPanel>   ();
      ProcessorStatePaneContent () {
	setLayout (new BoxLayout (this, BoxLayout.PAGE_AXIS));
	for (RegisterSet regSet : machine.processorState) {
 	  JPanel p = new JPanel ();
	  p.setLayout (new BoxLayout (p, BoxLayout.LINE_AXIS));
	  p.setAlignmentY (0);
	  p.setAlignmentX (0);
	  if (isTwoProcStateCols) {
	    JLabel l = new JLabel (regSet.getName ());
	    l.setHorizontalAlignment (SwingConstants.LEFT);
	    l.setFont (subTitleFont);
	    l.setAlignmentY (0);
            l.setPreferredSize (new Dimension (13, l.getPreferredSize().height));
	    JPanel tp = new JPanel (new GridLayout ());
	    tp.add (l);
	    tp.setBorder (new EmptyBorder (0,2,0,0));
	    tp.setMinimumSize (new Dimension (15,tp.getPreferredSize ().height));
	    tp.setMaximumSize (new Dimension (15,tp.getPreferredSize ().height));
	    tp.setAlignmentY (0);
	    tp.setAlignmentX (0);
	    p.add (tp);
	  }
	  JPanel dp = new JPanel ();
	  dp.setLayout (new BoxLayout (dp, BoxLayout.PAGE_AXIS));
	  dp.setAlignmentY (0);
	  dp.setAlignmentX (0);
          DataModel curInsAddr = regSet.getRegister (machine.curInsAddrRegName);
	  ViewPane pane;
	  if (isTwoProcStateCols) 
	    pane = new ViewPane ("", 
				 new ViewModel (regSet, 0, (regSet.getRowCount ()+1)/2, 2, null, Arrays.asList (0,1), Arrays.asList ("","","","")), statusBar,
				 Arrays.asList  (new ViewLabel     (-30, SwingConstants.RIGHT, nameFont, nameColor, null,  new ViewFormat.Format (String.class, "%s:")),
						 new ViewTextField (8,  SwingConstants.LEFT,   cellFont, null,      null, 
								    Arrays.asList ((ViewFormat.Format) 
										   new ViewFormat.NumberFormat     (Integer.class,        "%08x", 16),
										   new ViewFormat.NumberFormat     (Long.class,           "%d",   10),
										   new ViewFormat.NumberFormat     (HalfByteNumber.class, "%01x", 16),
										   new ViewFormat.NumberFormat     (Byte.class,           "%02x", 16),
										   new ViewFormat.NumberFormat     (Short.class,          "%03x", 16),
										   new ViewFormat.TwoIntegerFormat (SixByteNumber.class,  "%04x %08x", 16))),
						 new ViewLabel     (-30, SwingConstants.RIGHT, nameFont, nameColor, null,  new ViewFormat.Format (String.class, "%s:")),
						 new ViewTextField (8,  SwingConstants.LEFT,   cellFont, null,      null, 
								    Arrays.asList ((ViewFormat.Format) 
										   new ViewFormat.NumberFormat     (Integer.class,        "%08x", 16),
										   new ViewFormat.NumberFormat     (Long.class,           "%d",   10),
										   new ViewFormat.NumberFormat     (HalfByteNumber.class, "%01x", 16),
										   new ViewFormat.NumberFormat     (Byte.class,           "%02x", 16),
										   new ViewFormat.NumberFormat     (Short.class,          "%03x", 16),
										   new ViewFormat.TwoIntegerFormat (SixByteNumber.class,  "%04x %08x", 16)))));
	  else
	    pane = new ViewPane (curInsAddr!=null? "" : regSet.getName (), 
				 new ViewModel (regSet, 0, regSet.getRowCount (), 1, null, Arrays.asList (0,1), null), statusBar,
				 Arrays.asList  (new ViewLabel     (-99, SwingConstants.RIGHT, nameFont, nameColor, null,  new ViewFormat.Format (String.class, "%s:")),
						 new ViewTextField (13, SwingConstants.LEFT,   cellFont, null,      null,
								    Arrays.asList ((ViewFormat.Format) 
										   new ViewFormat.NumberFormat     (Integer.class,        "%08x", 16),
										   new ViewFormat.NumberFormat     (Long.class,           "%d",   10),
										   new ViewFormat.NumberFormat     (HalfByteNumber.class, "%01x", 16),
										   new ViewFormat.NumberFormat     (Byte.class,           "%02x", 16),
										   new ViewFormat.NumberFormat     (Short.class,          "%03x", 16),
										   new ViewFormat.TwoIntegerFormat (SixByteNumber.class,  "%04x %08x", 16)))));
	  int width = pane.getPreferredSize ().width;
	  if (curInsAddr!=null) {
 	    ViewPane ip = new CurrentInstructionPane ("", width-3, curInsAddr);
	    dp.add (ip);
	    panes.add (ip);
	    if (!isSmallCurInsDpy) { 
	      ip = new CurInsDescriptionPane  (width-3, curInsAddr);
	      dp.add (ip);
	      panes.add (ip);
	    }
	  }
	  panes.add (pane);
	  dp.add (pane);
	  p.add (dp);
	  add (p);
	  if (isTwoProcStateCols && machine.processorState.lastIndexOf (regSet) != machine.processorState.size ()-1) {
	    JPanel s = new JPanel ();
	    s.setBorder (new CompoundBorder (new MatteBorder (1,0,1,0, Application.this.getBackground ()),
					     new CompoundBorder (new MatteBorder (1,0,0,0,strutColor.darker ()),
								 new MatteBorder (0,0,0,0,strutColor))));
            s.setPreferredSize (new Dimension (s.getPreferredSize().width, 3));
	    add (s);
	    s.setAlignmentX (0);
	    struts.add (s);
	  }
	}
      }
      void adjustHighlights (boolean clear) {
	for (ViewPane pane : panes)
	  pane.adjustHighlights (clear);
      }
      void adjustFontSize (int increment) {
	for (ViewPane pane: panes)
	  pane.adjustFontSize (increment);
      }
      @Override public Dimension getPreferredScrollableViewportSize () {
        return getPreferredSize();
      }
      @Override public int getScrollableBlockIncrement (Rectangle r, int o, int d) {
        return panes.get(0).view.getScrollableBlockIncrement (r, o, d);
      }
      @Override public boolean getScrollableTracksViewportHeight () {
        return false; 
      }
      @Override public boolean getScrollableTracksViewportWidth () {
        return true;
      }
      @Override public int getScrollableUnitIncrement (Rectangle r, int o, int d) {
        return panes.get(0).view.getScrollableUnitIncrement (r, o, d);
      }
    }
    
    /**
     * General Purpose Register File.
     */
    
    class RegisterPane extends ViewPane {
      RegisterPane () {
	super ("Register File", 
	       new ViewModel (machine.registerFile, 0, machine.registerFile.getRowCount (), 1, Arrays.asList (0), Arrays.asList (1), null), statusBar,
	       Arrays.asList (new ViewLabel     (-28, SwingConstants.RIGHT,  addressFont, addressColor, MEMORY_HIGHLIGHT, new ViewFormat.Format       (String.class, "%s:")),
			      new ViewTextField (8,  SwingConstants.CENTER, cellFont,    null,         MEMORY_HIGHLIGHT, new ViewFormat.NumberFormat (Integer.class, "%08x", 16))));
      
      
      view.addAccessListener (new View.AccessListener () {
        @Override public void access (View.AccessListener.Type alType, int row, int col, int count) {
          if (col==1) 
            animator.renderDataFlow (Animator.Target.REGISTER, Animator.Type.valueOf (alType), view, 0, row, col, count);
        }});
      }
    }
    
    /**
     * Base-10 and reference-label view of register file content.
     */
    
    class RegisterViewPane extends ViewPane {
      RegisterViewPane () {
	super ("Reg Views",  
	       new ViewModel (new ValueView (machine.registerFile, 1, (MapModel) machine.memory.getLabelMap (), "Ref"), 
			      0, machine.registerFile.getRowCount (), 1, null, Arrays.asList (0,1), null), statusBar,
	       Arrays.asList ((ViewFormat)
			      new ViewTextField (-6,  SwingConstants.RIGHT, labelFont,    Color.BLACK,  CODE_HIGHLIGHT, new ViewFormat.NumberFormat (ValueView.Value.class, "%d", 10)),
			      new ViewTextField (-8,  SwingConstants.RIGHT, labelFont,    codeColor,    CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,          "%s"))));
      }
    }
    
    /**
     * The memory half of the application frame.
     */
    
    class AllMemoryPane extends JPanel {
      MemoryRegionSplitPane     instructionMemoryPane;
      MemoryRegionSplitPane     dataMemoryPane;
      Vector <MemoryRegionPane> regionPanes = new Vector<MemoryRegionPane> ();
      int                       numCols;
      AllMemoryPane () {
	UIDefaults uidefs = UIManager.getLookAndFeelDefaults ();	
	uidefs.put ("SplitPane.background", new ColorUIResource (strutColor));
	uidefs.put ("SplitPaneDivider.border", new BorderUIResource (new CompoundBorder (new MatteBorder (1,0,1,0, strutColor.darker ()),
											 new MatteBorder (0,0,0,0, strutColor))));
	setLayout (new BoxLayout (this, BoxLayout.LINE_AXIS));
	instructionMemoryPane = new MemoryRegionSplitPane ();
	instructionMemoryPane.setBorder (new CompoundBorder (new CompoundBorder (new CompoundBorder (new MatteBorder (0,1,0,0,strutColor.darker ()),	
												     new MatteBorder (0,3,0,0,strutColor)),
										 new MatteBorder (0,1,0,0,strutColor.darker ())),
							     new MatteBorder (4,0,2,0,Application.this.getBackground ())));
	dataMemoryPane      = new MemoryRegionSplitPane ();
	dataMemoryPane.setBorder (new CompoundBorder (new CompoundBorder (new CompoundBorder (new MatteBorder (0,1,0,0,strutColor.darker ()),	
											      new MatteBorder (0,3,0,0,strutColor)),
									  new MatteBorder (0,1,0,0,strutColor.darker ())),
						      new MatteBorder (4,0,2,0,Application.this.getBackground ())));
	numCols = 2;
	
	instructionMemoryPane.setAlignmentY (0);
	dataMemoryPane.setAlignmentY (0);
	add (instructionMemoryPane);
        add (dataMemoryPane);	
      }
      
      {
        addComponentListener (new ComponentAdapter () {
          @Override public void componentResized (ComponentEvent ce) {
            if (machine.memory.hasLoadedFile()) 
              setNumColumns (Application.this.getWidth()>=Application.this.getMaximizedWidth()? 2: 1);
          }
        });
      }
      
      int getMaximizedWidth () {
        int iw=0;
        int dw=0;
        for (MemoryRegionPane rp : regionPanes) {
          if (rp.region.getType() == Region.Type.DATA)
            dw = rp.getPreferredSize().width;
          else if (rp.region.getType () == Region.Type.INSTRUCTIONS)
            iw = rp.getPreferredSize().width;
          if (dw!=0 && iw!=0)
            break;
        }
        int oh = instructionMemoryPane.getPreferredSize().width - iw;
        if (oh<=10 || oh>=30)
          oh = 20; // XXX race with font-size change can make this wrong.  Its 20 in practice right now.
        return iw + oh + dw + (dw>0? oh: 0);
      }
      
      int getMaximizedHeight () {
        int ih=0;
        int dh=0;
        for (MemoryRegionPane rp : regionPanes) {
          if (rp.region.getType() == Region.Type.DATA)
            dh += rp.getPreferredSize().height;
          else if (rp.region.getType () == Region.Type.INSTRUCTIONS)
            ih += rp.getPreferredSize().height;
        }
        int ch = instructionMemoryPane==dataMemoryPane? dh+ih: Math.max (dh, ih);
        int oh = getPreferredSize().height - ch;
        return Math.max (dh, ih) + oh;
      }
      
      void setNumColumns (int aNum) {
	if (aNum != numCols) {
	  numCols = aNum;
	  instructionMemoryPane.removeAll();
          dataMemoryPane.removeAll();
	  if (numCols == 2) {
	    dataMemoryPane = new MemoryRegionSplitPane ();
	    dataMemoryPane.setBorder (new CompoundBorder (new CompoundBorder (new CompoundBorder (new MatteBorder (0,1,0,0,strutColor.darker ()),	
												  new MatteBorder (0,3,0,0,strutColor)),
									      new MatteBorder (0,1,0,0,strutColor.darker ())),
							  new MatteBorder (4,0,2,0,Application.this.getBackground ())));
	    dataMemoryPane.setAlignmentY (0);
	    add (dataMemoryPane);
	  } else {
	    remove (dataMemoryPane);
	    dataMemoryPane = instructionMemoryPane;
	  }
          for (int i=0; i<regionPanes.size (); i+=2) {
            MemoryRegionPane regionPane0 = regionPanes.get (i);
            MemoryRegionPane regionPane1 = regionPanes.get (i+1);
            if (regionPane0.region.getType () == Region.Type.INSTRUCTIONS)
              instructionMemoryPane.add (regionPane0, regionPane1);
            else
              dataMemoryPane.add (regionPane0, regionPane1);
          }
          revalidate();
        }
      }
      
      {
        addComponentListener (new ComponentAdapter () {
          @Override public void componentResized (ComponentEvent ce) {
            setMaximizedBounds ();
          }
        });
      }

      void clear () {
	regionPanes.clear ();
	instructionMemoryPane.removeAll ();
	dataMemoryPane.removeAll ();
      }
      
      void add (MemoryRegionPane regionPane0, MemoryRegionPane regionPane1) {
 	if (mainPane.fontSizeAdjustment != 0) {
	  regionPane0.adjustFontSize (mainPane.fontSizeAdjustment); 
	  regionPane1.adjustFontSize (mainPane.fontSizeAdjustment);
	}
	regionPanes.add (regionPane0);
	regionPanes.add (regionPane1);
	if (regionPane0.region.getType () == Region.Type.INSTRUCTIONS)
	  instructionMemoryPane.add (regionPane0, regionPane1);
	else
	  dataMemoryPane.add (regionPane0, regionPane1);
      }
      
      void adjustHighlights (boolean clear) {
        instructionMemoryPane.adjustHighlights (clear);
        if (dataMemoryPane!=instructionMemoryPane)
          dataMemoryPane.adjustHighlights (clear);
      }
      
      void adjustFontSize (int increment) {
        instructionMemoryPane.adjustFontSize (increment);
        if (dataMemoryPane!=instructionMemoryPane)
          dataMemoryPane.adjustFontSize (increment);
      }
      
      ViewPane getSelectedPane () {
	for (MemoryRegionPane region : regionPanes)
	  if ((region.isaPane.view.getSelectedRow () != -1 && region.isaPane.view.isCellEditable (region.isaPane.view.getSelectedRow (), region.isaPane.view.getSelectedColumn ())) || 
	      region.isaPane.view.getEditorComponent () != null)
	    return region.isaPane;
	return null;
      }
    }
    
    /**
     * Split pane that contains duplicate copies of memory display.
     */
    
    class MemoryRegionSplitPane extends JSplitPane {
      MemoryRegionSplitPane () {
	super (JSplitPane.VERTICAL_SPLIT, true, new MemoryRegionListPane (), new MemoryRegionListPane ());
	setResizeWeight       (1.0);
        setDividerLocation    (screenHeight+1);
	setOneTouchExpandable (true);
	setVisible            (false);
      }
      public void add (MemoryRegionPane regionPane0, MemoryRegionPane regionPane1) {
	((MemoryRegionListPane) getTopComponent    ()).add (regionPane0);
	((MemoryRegionListPane) getBottomComponent ()).add (regionPane1);
	setVisible         (true);
        setPreferredSize (new Dimension (getPreferredSize().width, regionPane0.getPreferredSize().height));
      }
      public void removeAll () {
	setVisible (false);
	((MemoryRegionListPane) getTopComponent    ()).removeAll ();
	((MemoryRegionListPane) getBottomComponent ()).removeAll ();
      }
      public void adjustFontSize (int increment) {
        ((MemoryRegionListPane) getTopComponent()).adjustFontSize (increment);
        ((MemoryRegionListPane) getBottomComponent()).adjustFontSize (increment);
        setPreferredSize (null);
        setPreferredSize (new Dimension (getPreferredSize().width, getTopComponent().getPreferredSize().height));
      }
      public void adjustHighlights (boolean clear) {
        ((MemoryRegionListPane) getTopComponent()).adjustHighlights (clear);
        ((MemoryRegionListPane) getBottomComponent()).adjustHighlights (clear);
      }
    }
    
    /**
     * Vertically Scrollable JPanel, based on view.
     */
    
    class VSPanel extends JPanel implements Scrollable {
      View view;
      VSPanel (View aView) {
        view = aView;
      }
      @Override public Dimension getPreferredScrollableViewportSize () {
        return getPreferredSize();
      }
      @Override public int getScrollableBlockIncrement (Rectangle r, int o, int d) {
        return view.getScrollableBlockIncrement (r, o, d);
      }
      @Override public boolean getScrollableTracksViewportHeight () {
        return false;
      }
      @Override public boolean getScrollableTracksViewportWidth () {
        return true;
      }
      @Override public int getScrollableUnitIncrement (Rectangle r, int o, int d) {
        return view.getScrollableUnitIncrement (r, o, d);
      }
    }
    
    /**
     * Scroll pane that contains a list of memory regions.
     */
    
    class MemoryRegionListPane extends JScrollPane {
      ArrayList <MemoryRegionPane> regionPanes = new ArrayList <MemoryRegionPane> ();
      MemoryRegionListPane () {
	setHorizontalScrollBarPolicy (JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	setVerticalScrollBarPolicy   (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	setBorder (null);
      }
      public void add (MemoryRegionPane regionPane) {
	if (regionPanes.isEmpty()) {
	  JPanel p = new VSPanel (regionPane.isaPane.view);
	  p.setLayout (new BoxLayout (p, BoxLayout.PAGE_AXIS));
	  p.add (regionPane);
	  setViewportView (p);
          getViewport().setBackground (Application.this.getBackground());
	  setVisible (true);
	} else
	  ((JPanel) getViewport ().getView ()).add (regionPane);
        regionPanes.add (regionPane);
        setMinimumSize (new Dimension (regionPane.getMinimumSize().width+getVerticalScrollBar().getWidth(),
                                       getMinimumSize().height));
      }
      public void removeAll () {
	setViewportView (null);
	regionPanes.clear();
	setVisible (false);
      }
      public void adjustFontSize (int increment) {
        for (MemoryRegionPane regionPane : regionPanes)
          regionPane.adjustFontSize (increment);
      }
      public void adjustHighlights (boolean clear) {
        for (MemoryRegionPane regionPane : regionPanes)
          regionPane.adjustHighlights (clear);
        if (!regionPanes.isEmpty()) {
          setMinimumSize (null);
          setMinimumSize (new Dimension (regionPanes.get(0).getMinimumSize().width+getVerticalScrollBar().getWidth(),
                                         getMinimumSize().height));
        }
      }
    }
    
    /**
     * Current instruction.
     */
    
    class CurrentInstructionPane extends ViewPane {
      CurrentInstructionPane (String name, int width, DataModel curInsAddr) {
	super (name, 
	       new ViewModel (new PickModel (new InstructionModel (machine.memory), Arrays.asList (curInsAddr), Arrays.asList (0), Arrays.asList (1)),
			      0, 1, 1, null, Arrays.asList (1), Arrays.asList (isSmallCurInsDpy? "" : "Current Instruction")), statusBar,
	       Arrays.asList ((ViewFormat) 
			      new ViewLabel    (-width, SwingConstants.CENTER,  isTwoProcStateCols? curInsTwoColFont : curInsFont, codeColor, null, new ViewFormat.Format (String.class, "%s"))));
	view.setRowHeight (view.getRowHeight () + (isTwoProcStateCols? 0: 10));
	adjustSize ();
      }
      public void setWidth (int width) {
	view.setColumnWidth (0, width); 
	adjustSize ();
      }
    }
    
    /**
     * Discription of current instruction.
     */
    
    class CurInsDescriptionPane extends ViewPane {
      CurInsDescriptionPane (int width, DataModel curInsAddr) {
	super ("", 
	       new ViewModel (new PickModel (new InstructionModel (machine.memory), Arrays.asList (curInsAddr), Arrays.asList (0), Arrays.asList (1)),
			      0, 1, 1, null, Arrays.asList (2), Arrays.asList ("")), statusBar,
	       Arrays.asList ((ViewFormat) 
			      new ViewLabel    (-width, SwingConstants.CENTER, isTwoProcStateCols? curInsDscTwoColFont : curInsDscFont, macColor, null, new ViewFormat.Format (String.class, "%s"))));
	view.setRowHeight (view.getRowHeight () - (isTwoProcStateCols? 6: 3));
	adjustSize ();
      }
      public void setWidth (int width) {
	view.setColumnWidth(0, width); 
	adjustSize ();
      }
    }
    
    /**
     * Memory.
     */
    
    class MemoryPane extends ViewPane {
      MemoryPane (int address, int length, boolean isInsMem) {
	super (String.format ("Memory - %x", address, length),  
	       new ViewModel (machine.memory, address, (length+3)/4, 4, Arrays.asList (0), Arrays.asList (1), Arrays.asList ("Addr","0","1","2","3")), statusBar,
	       isSmallInsMemDpy && isInsMem?
	       Arrays.asList (new ViewLabel     (24, SwingConstants.RIGHT,  memAddressFont, addressColor, MEMORY_HIGHLIGHT, new ViewFormat.NumberFormat (Integer.class, "0x%x:", 16)),
			      new ViewTextField (2,  SwingConstants.CENTER, memCellFont,    null,         MEMORY_HIGHLIGHT, new ViewFormat.NumberFormat (Byte.class,    "%02x", 16)),
			      new ViewTextField (2,  SwingConstants.CENTER, memCellFont,    null,         MEMORY_HIGHLIGHT, new ViewFormat.NumberFormat (Byte.class,    "%02x", 16)),
			      new ViewTextField (2,  SwingConstants.CENTER, memCellFont,    null,         MEMORY_HIGHLIGHT, new ViewFormat.NumberFormat (Byte.class,    "%02x", 16)),
			      new ViewTextField (2,  SwingConstants.CENTER, memCellFont,    null,         MEMORY_HIGHLIGHT, new ViewFormat.NumberFormat (Byte.class,    "%02x", 16))) :
	       Arrays.asList (new ViewLabel     (44, SwingConstants.RIGHT,  addressFont,    addressColor, MEMORY_HIGHLIGHT, new ViewFormat.NumberFormat (Integer.class, "0x%x:", 16)),
			      new ViewTextField (2,  SwingConstants.CENTER, cellFont,       null,         MEMORY_HIGHLIGHT, new ViewFormat.NumberFormat (Byte.class,    "%02x", 16)),
			      new ViewTextField (2,  SwingConstants.CENTER, cellFont,       null,         MEMORY_HIGHLIGHT, new ViewFormat.NumberFormat (Byte.class,    "%02x", 16)),
			      new ViewTextField (2,  SwingConstants.CENTER, cellFont,       null,         MEMORY_HIGHLIGHT, new ViewFormat.NumberFormat (Byte.class,    "%02x", 16)),
			      new ViewTextField (2,  SwingConstants.CENTER, cellFont,       null,         MEMORY_HIGHLIGHT, new ViewFormat.NumberFormat (Byte.class,    "%02x", 16))));
        if (isInsMem) {
          view.addMouseListener (new MouseAdapter () {
            @Override
            public void mousePressed (MouseEvent e) {
              if (e.getClickCount () == 2 && view.getSelectedColumn () == 0) {
                mainPane.adjustHighlights (true);
                machine.gotoPC (machine.memory.getAlignedInstructionAddress ((Integer) view.getValueAt (view.getSelectedRow (), 0)));
              }
            }});
        } else {
          view.addAccessListener (new View.AccessListener () {
            @Override public void access (View.AccessListener.Type alType, int row, int col, int count) {
              if (col==1 && Animator.Type.valueOf (alType)!=null) {
                animator.renderDataFlow (Animator.Target.MEMORY, Animator.Type.valueOf (alType), view, 0, row, col, count);
              }
            }});
        }
      }
      public void setLength (int len) {
	((ViewModel) view.getModel ()).setLength ((len+3)/4);
      }
    }
    
    /**
     * Instruction view of memory.
     */
    
    class InstructionsPane extends ViewPane implements Region.ByteLengthChangedListener {
      JComponent context = null;
      MemoryPane memoryPane;
      Region     region;
      InstructionsPane (InstructionRegion aRegion, MemoryPane aMemoryPane) {
	super (String.format ("Instructions - %x", aRegion.getAddress (), aRegion.length ()), 
	       new ViewModel (new CompoundModel (aRegion, new BreakpointControlModel (aRegion)), 
			      0, aRegion.length (), 1, Arrays.asList (5,0), isMacShown? Arrays.asList (1,2,3,4) : Arrays.asList (2,3,4),  null), statusBar,
	       isMacShown?
	       Arrays.asList (new ViewCheckBox  (breakpointColor),
	 		      new ViewLabel     (44, SwingConstants.RIGHT, addressFont, addressColor, CODE_HIGHLIGHT, new ViewFormat.NumberFormat (Integer.class, "0x%x:", 16)),
			      new ViewTextField (13, SwingConstants.LEFT,  macFont,     macColor,     CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,  "%s")),
			      new ViewTextField (8,  SwingConstants.RIGHT, labelFont,   labelColor,   CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,  "%s")),
			      new ViewTextField (20, SwingConstants.LEFT,  cellFont,    codeColor,    CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,  "%s")),
			      new ViewTextField (-20,SwingConstants.LEFT,  commentFont, commentColor, CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,  "%s"))) 
               :
	       Arrays.asList (new ViewCheckBox  (breakpointColor),
			      new ViewLabel     (44, SwingConstants.RIGHT, addressFont, addressColor, CODE_HIGHLIGHT, new ViewFormat.NumberFormat (Integer.class, "0x%x:", 16)),
			      new ViewTextField (8,  SwingConstants.RIGHT, labelFont,   labelColor,   CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,  "%s")),
			      new ViewTextField (26, SwingConstants.LEFT,  cellFont,    codeColor,    CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,  "%s")),
			      new ViewTextField (-20,SwingConstants.LEFT,  commentFont, commentColor, CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,  "%s"))));	       
	memoryPane = aMemoryPane;
	region     = aRegion;
	region.addByteLengthChangedListener (this);
	view.addUndoableEditListener (undoManager);
	view.addMouseListener (new MouseAdapter () {
	  public void mousePressed (MouseEvent e) {
	    if (e.getClickCount() == 2  && view.getSelectedColumn () == 1) {
	      mainPane.adjustHighlights (true);
	      machine.gotoPC ((Integer) view.getValueAt (view.getSelectedRow (), 1));
	    }
	  }});
        view.addAccessListener (new View.AccessListener () {
          @Override public void access (View.AccessListener.Type alType, int row, int col, int count) {
            if (col==1 && Animator.Type.valueOf (alType) == Animator.Type.CONTROL_FLOW) {
              if (context==null) {
                MemoryRegionSplitPane            splitPane       = mainPane.allMemoryPane.instructionMemoryPane;
                ArrayList <MemoryRegionListPane> regionListPanes = new ArrayList <MemoryRegionListPane> ();
                regionListPanes.add ((MemoryRegionListPane) splitPane.getTopComponent());
                regionListPanes.add ((MemoryRegionListPane) splitPane.getBottomComponent());
                for (MemoryRegionListPane regionListPane : regionListPanes) 
                  for (MemoryRegionPane regionPane : regionListPane.regionPanes)
                    if (regionPane.isaPane==InstructionsPane.this)
                      context = regionListPane;
              }
              assert context!=null;
              if (isRunning)
                animator.setControlFlowStart (context);
              animator.recordControlFlow (context, view, row);
            }
          }});
      }
      public void byteLengthChanged () {
	memoryPane.setLength (region.byteLength ());
      }
      public void setCommentColumns (int columns) {
	view.setColumnWidth (isMacShown? 5 : 4, columns);
	adjustSize ();
      }
      JComponent getCommentPrototype () {
	return view.getColumnFormat (isMacShown? 5 : 4).getRendererPrototype ();
      }
    }
    
    /**
     * Data view of memory.
     */
    
    class DataPane extends ViewPane implements Region.ByteLengthChangedListener {
      MemoryPane memoryPane;
      Region     region;
      DataPane (DataRegion aRegion, MemoryPane aMemoryPane) {
	super (String.format ("Data - %x", aRegion.getAddress (), aRegion.length ()),  
	       new ViewModel (aRegion, 0, aRegion.length (), 1, isDataAddrShown? Arrays.asList(0): null, Arrays.asList (1,2,3,4), null), statusBar,
               isDataAddrShown?
	       Arrays.asList ((ViewFormat)
			      new ViewLabel     (44, SwingConstants.RIGHT, addressFont, addressColor, CODE_HIGHLIGHT, new ViewFormat.NumberFormat (Integer.class, "0x%x:", 16)),
			      new ViewTextField (6,  SwingConstants.RIGHT, labelFont,   Color.BLACK,  CODE_HIGHLIGHT, new ViewFormat.NumberFormat (DataRegion.Value.class, "%d", 10)),
			      new ViewTextField (8,  SwingConstants.RIGHT, labelFont,   codeColor,    CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,           "%s")),
			      new ViewTextField (8,  SwingConstants.RIGHT, labelFont,   labelColor,   CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,           "%s")),
			      new ViewTextField (-10,
						     SwingConstants.LEFT,  commentFont, commentColor, CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,           "%s")))
              :
              Arrays.asList ((ViewFormat)
                             new ViewTextField (6,  SwingConstants.RIGHT, labelFont,   Color.BLACK,  CODE_HIGHLIGHT, new ViewFormat.NumberFormat (DataRegion.Value.class, "%d", 10)),
                             new ViewTextField (8,  SwingConstants.RIGHT, labelFont,   codeColor,    CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,           "%s")),
                             new ViewTextField (8,  SwingConstants.RIGHT, labelFont,   labelColor,   CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,           "%s")),
                             new ViewTextField (-10,
                                                SwingConstants.LEFT,  commentFont, commentColor, CODE_HIGHLIGHT, new ViewFormat.Format       (String.class,           "%s"))));
	memoryPane = aMemoryPane;
	region     = aRegion;
	region.addByteLengthChangedListener (this);
      }
      public void byteLengthChanged () {
	memoryPane.setLength (region.byteLength ());
      }
      public void setCommentColumns (int columns) {
	view.setColumnWidth (isDataAddrShown? 4: 3, columns);
	adjustSize ();
      }
      JComponent getCommentPrototype () {
	return view.getColumnFormat (isDataAddrShown? 4: 3).getRendererPrototype ();
      }
    }
    
    /**
     * Memory region combines base-memory view with instruction- or data-view.
     */
    
    class MemoryRegionPane extends JPanel {
      Region     region;
      MemoryPane pane;
      ViewPane   isaPane;
      MemoryRegionPane (Region aRegion) {
	region = aRegion;
	setLayout (new BoxLayout (this, BoxLayout.LINE_AXIS));
	pane = new MemoryPane (region.getAddress (), region.byteLength (), region.getType () == Region.Type.INSTRUCTIONS);
	pane.setAlignmentY (0);
	add (pane);
	Dimension sz = new Dimension (1,0);
	add (new Box.Filler (sz, sz, sz));
	if (region.getType () == Region.Type.INSTRUCTIONS)
	  isaPane = new InstructionsPane ((InstructionRegion) region, pane);
	else if (region.getType () == Region.Type.DATA)
	  isaPane = new DataPane ((DataRegion) region, pane);
	else 
	  throw new AssertionError (region.getType ());
	isaPane.view.setRowSelectionAllowed (true);
	isaPane.setAlignmentY (0);
	add(isaPane);
	setAlignmentX (0);
	isaPane.view.addSelectionListener (masterSelectionListener);
      }
      void adjustHighlights (boolean clear) {
	pane.adjustHighlights (clear);
	isaPane.adjustHighlights (clear);
      }
      void adjustFontSize (int increment) {
	pane.adjustFontSize (increment);
	isaPane.adjustFontSize (increment);
      }
      void setCommentColumns (int columns) {
	if (region.getType () == Region.Type.INSTRUCTIONS)
	  ((InstructionsPane) isaPane).setCommentColumns (columns);
	else
	  ((DataPane) isaPane).setCommentColumns (columns);
      }
      JComponent getCommentPrototype () {
	if (region.getType () == Region.Type.INSTRUCTIONS)
	  return ((InstructionsPane) isaPane).getCommentPrototype ();
	else
	  return ((DataPane) isaPane).getCommentPrototype ();
      }
    }
    
    /**
     * Update memory from loaded file.
     */
    
    void updateMemoryView () {
      if (machine.memory.hasLoadedFile ()) {
	setTitle (applicationFullName.concat (" - ").concat (machine.memory.getLoadedFilename ()));
        getRootPane().putClientProperty ("Window.documentFile", new File (machine.memory.getLoadedPathname()));
	boolean haveSetStartingPC = false;
	mainPane.allMemoryPane.clear ();
	for (Region r : machine.memory.getRegions ()) {
	  MemoryRegionPane p0 = new MemoryRegionPane (r);
	  MemoryRegionPane p1 = new MemoryRegionPane (r);
	  mainPane.allMemoryPane.add (p0, p1);
	  if (r.getType () == Region.Type.INSTRUCTIONS) {
	    if (! haveSetStartingPC) {
	      machine.gotoPC (r.getAddress());
	      haveSetStartingPC = true;
	    }
	  }
	}
        pack();
        setMaximizedBounds ();
      }
    }
    
    /**
     * Pop-up slider to adjust memory speed.  (Not currently used in UI.)
     */
    class RunningSpeedSlider extends JFrame {
      RunningSpeedSlider () {
	Container cp = getContentPane ();
	cp.setLayout (new BorderLayout ());
	JLabel tl = new JLabel ("Running Speed", SwingConstants.CENTER);
	tl.setFont (UI.TITLE_FONT);
	cp.add (tl, BorderLayout.NORTH);
	JSlider slider = new JSlider (1, 5000, 1500);
	Hashtable<Integer,JComponent> labels = new Hashtable<Integer,JComponent> ();
	labels.put (1,    new JLabel ("0s"));
	for (int i=1000; i<=5000; i+=1000)
	  labels.put (i, new JLabel (String.format ("%ds",i/1000)));
	slider.setMajorTickSpacing (1000);
	slider.setLabelTable (labels);
	slider.setPaintLabels (true);
	slider.setPaintTicks (true);
	cp.add (slider, BorderLayout.CENTER);
	setVisible (true);
      }
    }
    
    /**
     * Handle update events from observed objects.
     */
    
    public void update (Observable o, Object arg) {
      if (o instanceof Machine) {
        Machine.Event e = (Machine.Event) arg;
        switch (e.type) {
          case INSTRUCTION_PROLOG:
            animator.clear();
            adjustHighlights (false);
            break;
          case TRACE_POINT:
            Machine.DebugEvent de = (Machine.DebugEvent) e;
            switch (de.debugType) {
              case BREAK:
                // only instruction breakpoints implemented in gui
                assert de.point == Machine.DebugPoint.INSTRUCTION;
                statusBar.showMessage (String.format ("Breakpoint at instruction 0x%x", de.value));
                break;
              case TRACE:
                // not implemented in gui
                break;
            }
            break;
        }
      }
    }
  }
}
