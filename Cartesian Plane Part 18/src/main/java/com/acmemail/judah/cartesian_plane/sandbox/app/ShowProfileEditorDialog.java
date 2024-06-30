package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.components.Profile;
import com.acmemail.judah.cartesian_plane.sandbox.profile.ProfileEditorDialog;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * This application displays a CartesionPlaneWithOCR
 * and demonstrates its use. 
 * The main application frame
 * contains three pushbuttons
 * and a Tracking panel
 * with a set of checkboxes.
 * There is one checkbox
 * for each category of properties
 * (Main Window, Grid Lines, Major Tics, etc.).
 * When you press the Log button
 * the values of all the properties
 * in the checked categories are displayed;
 * there is one column 
 * to show the property values
 * as currently edited,
 * and another column to show the values
 * that have been applied
 * to the PropertyManager.
 * To post the editor dialog,
 * push the Edit button.
 * <p>
 * The editor is displayed in a modal dialog.
 * To select a checkbox, the Log button, or the Exit button,
 * you can use a keyboard shortcut
 * incorporating the control/alt modifiers.
 * To see the specific shortcuts,
 * hover over one of the target button.
 * </p>
 * 
 * @author Jack Straub
 */
public class ShowProfileEditorDialog
{
    /** Platform-specific line ending. */
    private static final String endl    = System.lineSeparator();
    /** Simple name of the LinePropertySetAxes class. */
    private static final String axesSet         =
        LinePropertySetAxes.class.getSimpleName();
    /** Simple name of the LinePropertySetTicMajor class. */
    private static final String ticMajorSet     =
        LinePropertySetTicMajor.class.getSimpleName();
    /** Simple name of the LinePropertySetTicMinor class. */
    private static final String ticMinorSet     =
        LinePropertySetTicMinor.class.getSimpleName();
    /** Simple name of the LinePropertySetTicMinor class. */
    private static final String gridLinesSet    =
        LinePropertySetGridLines.class.getSimpleName();
    
    /** Used to display the time in the application log window. */
    private static final DateTimeFormatter  timeFormatter   = 
        DateTimeFormatter.ofPattern( "HH:mm:ss" );
    
    /** Convenient PropertyManager singleton reference. */
    private final PropertyManager       pMgr    = PropertyManager.INSTANCE;
    /** The editor dialog to display. */
    private final ProfileEditorDialog   dialog;
    /** Window to display log. */
    private final ActivityLog           log;
    /** Profile to be edited. */
    private final Profile               profile = new Profile();
    
    /** This checkbox enables tracking of the main window properties. */
    private final JCheckBox gridCheckBox        =
        new JCheckBox( "Grid" );
    /** This checkbox enables tracking of the axis properties. */
    private final JCheckBox axisCheckBox        = 
        new JCheckBox( "Axis" );
    /** This checkbox enables tracking of the grid line properties. */
    private final JCheckBox gridLineCheckBox    = 
        new JCheckBox( "Grid Lines" );
    /** This checkbox enables tracking of the major tic properties. */
    private final JCheckBox majorTicCheckBox    = 
        new JCheckBox( "Major Tics" );
    /** This checkbox enables tracking of the minor tic properties. */
    private final JCheckBox minorTicCheckBox    = 
        new JCheckBox( "Minor Tics" );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( ShowProfileEditorDialog::new  );
    }
    
    /**
     * Constructor.
     * Configures all aspects of the application GUI.
     * Makes the main application window visible.
     */
    public ShowProfileEditorDialog()
    {
        JFrame      frame   = new JFrame( "Show Font Editor" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        dialog  = new ProfileEditorDialog( frame, profile );
        log = new ActivityLog( "Activity Log" );

        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getControlPanel(), BorderLayout.CENTER );
        contentPane.add( getCheckBoxPanel(), BorderLayout.SOUTH );
        
        Border  inner   =
            BorderFactory.createBevelBorder( BevelBorder.RAISED );
        Border  outer   =
            BorderFactory.createEmptyBorder( 8, 8, 8, 8 );
        Border  border  = 
            BorderFactory.createCompoundBorder( outer, inner );
        contentPane.setBorder( border );
        
        frame.setContentPane( contentPane );
        frame.pack();
        
        positionGUI( frame );
        frame.setVisible( true );
        log.setVisible( true );
        
        // Activate the keyboard shortcuts.
        addKeyListener();
    }
    
    /**
     * Positions the application frame
     * and the log window.
     * 
     * @param frame the application frame
     */
    private void positionGUI( JFrame frame )
    {
        // Position frame near the screen upper-left corner
        int         frameXco    = 10;
        int         frameYco    = 10;
        Dimension   frameDim    = frame.getPreferredSize();
        frame.setLocation( frameXco, frameYco );
        
        // Position the log dialog immediately below the
        // application frame.
        Dimension   screenSize  = 
            Toolkit.getDefaultToolkit().getScreenSize();
        int         logXco      = frameXco;
        int         logYco      = frameYco + frameDim.height;
        log.setLocation( logXco, logYco );
        
        // Get remaining screen height after accounting for
        // frame yco and frame height
        int         availHeight = 
            screenSize.height - frameDim.height - 2 * frameYco;
        // Make log height 80% of remaining height.
        int         logHeight   = (int)(availHeight * .8 + .5);
        Dimension   logDim      = log.getPreferredSize();
        logDim.height = logHeight;
        log.setLocation( logXco, logYco );
        log.setPreferredSize( logDim );
        
        // Figure out which of frame or log dialog is wider.
        int         maxWidth    = frameDim.width > logDim.width ?
            frameDim.width : logDim.width;
        
        // Position the graph dialog immediately to the right of
        // frame/log.
        int         dialogXco   = frameXco + maxWidth;
        int         dialogYco   = frameYco;
        dialog.setLocation( dialogXco, dialogYco );
    }
    
    /**
     * Activates the keyboard shortcuts.
     * This requires adding a KeyEventDispatcher
     * to the KeyboardFocusManager.
     */
    private void addKeyListener()
    {
        KeyboardFocusManager    focusMgr    =
            KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusMgr.addKeyEventDispatcher( new KeyMonitor() );
    }
    
    /**
     * Gets the panel containing the pushbuttons.
     * 
     * @return  the panel containing the pushbuttons
     */
    private JPanel getControlPanel()
    {
        JPanel  panel   = new JPanel();
        Border      border  = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );

        JButton     edit    = new JButton( "Edit" );
        edit.addActionListener( this::showDialog );
        edit.setToolTipText( "ctrl-alt-e" );
        
        JButton     print   = new JButton( "Show Log" );
        print.addActionListener( this::log );
        print.setToolTipText( "ctrl-alt-l" );
        
        JButton     exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        exit.setToolTipText( "ctrl-alt-x" );
        
        panel.setBorder( border );
        panel.add( edit );
        panel.add( print );
        panel.add( exit );
        
        return panel;
    }
    
    /**
     * Creates a panel containing the category checkboxes.
     * 
     * @return  a panel containing the category checkboxes
     */
    private JPanel getCheckBoxPanel()
    {
        gridCheckBox.setToolTipText( "ctrl-alt-g" );
        axisCheckBox.setToolTipText( "ctrl-alt-a" );
        gridLineCheckBox.setToolTipText( "ctrl-alt-r" );
        majorTicCheckBox.setToolTipText( "ctrl-alt-m" );
        minorTicCheckBox.setToolTipText( "ctrl-alt-i" );

        JPanel      panel   = new JPanel( new GridLayout( 2, 3 ) );
        Border      line    =
            BorderFactory.createLineBorder( Color.BLACK );
        Border      outer   =
            BorderFactory.createEmptyBorder( 4, 4, 4, 4 );
        Border      inner   =
            BorderFactory.createTitledBorder( line, "Tracking" );
        Border      border  = 
            BorderFactory.createCompoundBorder( outer, inner );
        panel.setBorder( border );

        panel.add( gridCheckBox );
        panel.add( axisCheckBox );
        panel.add( gridLineCheckBox );
        panel.add( majorTicCheckBox );
        panel.add( minorTicCheckBox );
        // Fill the unused cell in row 1, column 2
        panel.add( new JLabel( "" ) );
        return panel;
    }
    
    /**
     * Logs the properties of a selected categories.
     *  
     * @param evt   
     *      event object associated with ActionListeners;
     *      not used.
     */
    private void log( ActionEvent evt )
    {
        logTime();
        
        if ( gridCheckBox.isSelected() )
            logGraph( profile.getMainWindow() );
        if ( axisCheckBox.isSelected() )
            logPropSet( 
                profile.getLinePropertySet( axesSet ),
                new LinePropertySetAxes(),
                "blue" 
            );
        if ( gridLineCheckBox.isSelected() )
            logPropSet( 
                profile.getLinePropertySet( gridLinesSet ),
                new LinePropertySetGridLines(),
                "#ff00ff" 
            );
        if ( majorTicCheckBox.isSelected() )
            logPropSet( 
                profile.getLinePropertySet( ticMajorSet ),
                new LinePropertySetTicMajor(),
                "green" 
            );
        if ( minorTicCheckBox.isSelected() )
            logPropSet( 
                profile.getLinePropertySet( ticMinorSet ),
                new LinePropertySetTicMinor(),
                "#ffa500" 
            );

        log.append( "***************" );
        log.append( "***************" );
    }
    
    /**
     * Logs the properties
     * from the given GraphPropertySet.
     * 
     * @param editSet   the given GraphPropertySet
     */
    private void logGraph( GraphPropertySet editSet )
    {
        String              title   = editSet.getClass().getSimpleName();
        GraphPropertySet    applSet = new GraphPropertySetMW();
        String              leftStr = null;
        String              fmtStr  = null;
        String              prefix  = 
            "<pre style=\"color: red;\">";
        String  suffix  = "</pre>";
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( prefix )
            .append("<strong>" )
            .append( "*** " + title +  " ***" )
            .append( endl );
        fmtStr = String.format( "%23s%s", "Edited     ", "Applied" );
        bldr.append( fmtStr )
            .append("</strong>" )
            .append( endl );
            
        float   fEditVal    = profile.getGridUnit();
        float   fApplVal    = pMgr.asFloat( CPConstants.GRID_UNIT_PN );
        leftStr = String.format( "%-12s%3.1f", "Grid unit:", fEditVal );
        fmtStr = String.format( "%-23s%3.1f", leftStr, fApplVal );
        bldr.append( fmtStr ).append( endl );
        
        String  editStr = getColorStr( editSet.getBGColor() );
        String  applStr = getColorStr( applSet.getBGColor() );
        leftStr = String.format( "%-12s%s", "BG color: ", editStr );
        fmtStr = String.format( "%-23s%s", leftStr, applStr );
        bldr.append( fmtStr ).append( endl );
        
        editStr = editSet.getFontName();
        applStr = applSet.getFontName();
        leftStr = String.format( "%-12s%s", "Font name:", editStr );
        fmtStr = String.format( "%-23s%s", leftStr, applStr );
        bldr.append( fmtStr ).append( endl );
        
        fEditVal = editSet.getFontSize();
        fApplVal = applSet.getFontSize();
        leftStr = String.format( "%-12s%3.1f", "Font size:", fEditVal );
        fmtStr = String.format( "%-23s%3.1f", leftStr, fApplVal );
        bldr.append( fmtStr ).append( endl );
        
        editStr = "" + editSet.isBold();
        applStr = "" + applSet.isBold();
        leftStr = String.format( "%-12s%s", "Is bold:  ", editStr );
        fmtStr = String.format( "%-23s%s", leftStr, applStr );
        bldr.append( fmtStr ).append( endl );
        
        editStr = "" + editSet.isItalic();
        applStr = "" + applSet.isItalic();
        leftStr = String.format( "%-12s%s", "Is italic:", editStr );
        fmtStr = String.format( "%-23s%s", leftStr, applStr );
        bldr.append( fmtStr ).append( endl );
        
        editStr = getColorStr( editSet.getFGColor() );
        applStr = getColorStr( applSet.getFGColor() );
        leftStr = String.format( "%-12s%s", "Text color:", editStr );
        fmtStr = String.format( "%-23s%s", leftStr, applStr );
        bldr.append( fmtStr ).append( endl );
        
        bldr.append( suffix );
        log.append( bldr.toString(), null );
    }
    
    /**
     * Logs the properties
     * from the given LinePropertySet.
     * 
     * @param editSet   the given LinePropertySet
     * @param applSet   the LinePropertySet from the PropertyManager
     * @param color     the color for displaying properties
     */
    private void logPropSet( 
        LinePropertySet editSet,
        LinePropertySet applSet,
        String          color
    )
    {
        String          title   = editSet.getClass().getSimpleName();
        String          leftStr = null;
        String          fmtStr  = null;
        String          prefix  = 
            "<pre style=\"color: " + color + ";\">";
        String  suffix  = "</pre>";
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( prefix )
            .append("<strong>" )
            .append( "*** " ).append( title ).append( " ***" )
            .append( endl );
        fmtStr = String.format( "%23s%s", "Edited     ", "Applied" );
        bldr.append( fmtStr )
            .append("</strong>" )
            .append( endl );
            
        float   fEditVal    = 0;
        float   fApplVal    = 0;
        if ( editSet.hasSpacing() )
        {
            fEditVal    = editSet.getSpacing();
            fApplVal    = applSet.getSpacing();
            leftStr = String.format( "%-12s%3.1f", "Lines/unit:", fEditVal );
            fmtStr = String.format( "%-23s%3.1f", leftStr, fApplVal );
            bldr.append( fmtStr ).append( endl );
        }
        
        if ( editSet.hasLength() )
        {
            fEditVal    = editSet.getLength();
            fApplVal    = applSet.getLength();
            leftStr = String.format( "%-12s%3.1f", "Length:", fEditVal );
            fmtStr = String.format( "%-23s%3.1f", leftStr, fApplVal );
            bldr.append( fmtStr ).append( endl );
        }
        
        if ( editSet.hasStroke() )
        {
            fEditVal    = editSet.getStroke();
            fApplVal    = applSet.getStroke();
            leftStr = String.format( "%-12s%3.1f", "Weight:", fEditVal );
            fmtStr = String.format( "%-23s%3.1f", leftStr, fApplVal );
            bldr.append( fmtStr ).append( endl );
        }
        
        String  editStr = null;
        String  applStr = null;
        if ( editSet.hasDraw() )
        {
            editStr = "" + editSet.getDraw();
            applStr = "" + applSet.getDraw();
            leftStr = String.format( "%-12s%s", "Draw:  ", editStr );
            fmtStr = String.format( "%-23s%s", leftStr, applStr );
            bldr.append( fmtStr ).append( endl );
        }
        
        if ( editSet.hasColor() )
        {
            editStr = getColorStr( editSet.getColor() );
            applStr = getColorStr( applSet.getColor() );
            leftStr = String.format( "%-12s%s", "Color: ", editStr );
            fmtStr = String.format( "%-23s%s", leftStr, applStr );
            bldr.append( fmtStr ).append( endl );
        }
        bldr.append( suffix );
        log.append( bldr.toString(), null );
    } 
    
    /**
     * Show the edit dialog.
     * This method is activated
     * when the GUI's Edit button is selected.
     * 
     * @param evt   object accompanying action event; not used
     */
    private void showDialog( ActionEvent evt )
    {
        if ( !dialog.isVisible() )
        {
            int     iResult = dialog.showDialog();
            String  sResult = null;
            if ( iResult == JOptionPane.OK_OPTION )
                sResult = "OK";
            else if ( iResult == JOptionPane.CANCEL_OPTION )
                sResult = "Cancel";
            else
                sResult = "???????";
            
            logTime();
            log.append( "result: " + sResult );
            log.append( "*****************************" );
        }
    }
    
    /**
     * Writes the current time to the application log.
     */
    private void logTime()
    {
        LocalTime   timeNow = LocalTime.now();
        String      strNow  = timeNow.format( timeFormatter );
        log.append( "***** " + strNow + " *****", "" );
    }
    
    /**
     * Given a color,
     * returns a string of the of the form "0xRRGGBB,"
     * where "RRGGBB" represents the 
     * red, green, and blue integer values for the color.
     * 
     * @param color the given color
     * 
     * @return  an RGB string derived from the given color
     */
    private String getColorStr( Color color )
    {
        int     iRGB    = color.getRGB() & 0xFFFFFF;
        String  sRGB    = String.format( "0x%06x", iRGB );
        return sRGB;
    }
    
    /**
     * Given a key code,
     * activate the application button
     * associated with the key code.
     * 
     * @param keyCode   the given key code
     */
    private void processCtrlAltKeyEvent( int keyCode )
    {
        switch ( keyCode )
        {
        case KeyEvent.VK_G:
            gridCheckBox.doClick();
            break;
        case KeyEvent.VK_A:
            axisCheckBox.doClick();
            break;
        case KeyEvent.VK_R:
            gridLineCheckBox.doClick();
            break;
        case KeyEvent.VK_M:
            majorTicCheckBox.doClick();
            break;
        case KeyEvent.VK_I:
            minorTicCheckBox.doClick();
            break;
        case KeyEvent.VK_L:
            log( null );
            break;
        case KeyEvent.VK_E:
            showDialog( null );
            break;
        case KeyEvent.VK_X:
            System.exit( 0 );
            break;
        }
    }
    
    /**
     * This class encapsulates an object
     * that can be used 
     * to listen for KeyEventDispatcher events.
     * 
     * @author Jack Straub
     */
    private class KeyMonitor implements KeyEventDispatcher
    {
        /** 
         * Integer that corresponds to 
         * a KeyEvent modifier mask
         * in which the control and alt keys
         * are held down.
         */
        private static final int    ctrlAltDown =
            KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;
        
        /**
         * This method is activated
         * when the KeyboardFocusManager
         * dispatches a KeyEvent.
         * 
         * @param   evt object that describes the associated key event
         */
        @Override
        public boolean dispatchKeyEvent( KeyEvent evt )
        {
            int     mods    = evt.getModifiersEx();
            int     ident   = evt.getID();
            if ( ident == KeyEvent.KEY_PRESSED && mods == ctrlAltDown )
            {
                int keyCode = evt.getKeyCode();
                processCtrlAltKeyEvent( keyCode );
            }
            return false;
        }
    }
}
