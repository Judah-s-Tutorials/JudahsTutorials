package com.acmemail.judah.cartesian_plane.sandbox.experimental.line_editor_rev2;

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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

public class ShowGraphManagerDialog
{
    private static final String endl    = System.lineSeparator();
    
    private static final DateTimeFormatter  timeFormatter   = 
        DateTimeFormatter.ofPattern( "HH:mm:ss" );
    
    private final PropertyManager       pMgr    = PropertyManager.INSTANCE;
    private final GraphManagerDialog    dialog;
    private final ActivityLog           log;
    private final GraphManager          graphManager;
    
    private final JCheckBox gridCheckBox        =
        new JCheckBox( "Grid" );
    private final JCheckBox axisCheckBox        = 
        new JCheckBox( "Axis" );
    private final JCheckBox gridLineCheckBox    = 
        new JCheckBox( "Grid Lines" );
    private final JCheckBox majorTicCheckBox    = 
        new JCheckBox( "Major Tics" );
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
        SwingUtilities.invokeLater(ShowGraphManagerDialog::new  );
    }
    
    public ShowGraphManagerDialog()
    {
        JFrame      frame   = new JFrame( "Show Font Editor" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        dialog  = new GraphManagerDialog( frame );
        log = new ActivityLog( "Activity Log" );
        graphManager = dialog.getGraphManager();

        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getControlPanel(), BorderLayout.CENTER );
        contentPane.add( getBottomPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.pack();
        
        positionGUI( frame );
        frame.setVisible( true );
        log.setVisible( true );
        
        addKeyListener();
    }
    
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
    
    private void addKeyListener()
    {
        KeyboardFocusManager    focusMgr    =
            KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusMgr.addKeyEventDispatcher( new KeyMonitor() );
    }
    
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
    
    private JPanel getBottomPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK );
        panel.setLayout( layout );
        panel.setBorder( border );
        
        JLabel  label       = new JLabel( "Tracking:" );
        panel.add( label );
        panel.add( getCheckBoxPanel() );
        return panel;
    }
    
    private JPanel getCheckBoxPanel()
    {
        gridCheckBox.setToolTipText( "ctrl-alt-g" );
        axisCheckBox.setToolTipText( "ctrl-alt-a" );
        gridLineCheckBox.setToolTipText( "ctrl-alt-r" );
        majorTicCheckBox.setToolTipText( "ctrl-alt-m" );
        minorTicCheckBox.setToolTipText( "ctrl-alt-i" );

        JPanel      panel   = new JPanel( new GridLayout( 2, 3 ) );
        panel.add( gridCheckBox );
        panel.add( axisCheckBox );
        panel.add( gridLineCheckBox );
        panel.add( majorTicCheckBox );
        panel.add( minorTicCheckBox );
        // Fill the unused cell in row 1, column 2
        panel.add( new JLabel( "" ) );
        return panel;
    }
    
    private void log( ActionEvent evt )
    {
        logTime();
        
        if ( gridCheckBox.isSelected() )
            logGrid( graphManager.getMainWindow() );
        if ( axisCheckBox.isSelected() )
            logPropSet( 
                "Axes",
                graphManager.getAxis(),
                new LinePropertySetAxes(),
                "blue" 
            );
        if ( gridLineCheckBox.isSelected() )
            logPropSet( 
                "Grid Lines",
                graphManager.getGridLine(),
                new LinePropertySetGridLines(),
                "#ff00ff" 
            );
        if ( majorTicCheckBox.isSelected() )
            logPropSet( 
                "Major Tics",
                graphManager.getTicMajor(),
                new LinePropertySetTicMajor(),
                "green" 
            );
        if ( minorTicCheckBox.isSelected() )
            logPropSet( 
                "Minor Tics",
                graphManager.getTicMinor(),
                new LinePropertySetTicMinor(),
                "#ffa500" 
            );

        log.append( "***************" );
        log.append( "***************" );
    }
    
    private void logGrid( GraphPropertySet editSet )
    {
        GraphPropertySet    applSet = new GraphPropertySetMW();
        String              leftStr = null;
        String              fmtStr  = null;
        String              prefix  = 
            "<pre style=\"color: red;\">";
        String  suffix  = "</pre>";
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( prefix )
            .append("<strong>" )
            .append( "*** Grid properties ***" )
            .append( endl );
        fmtStr = String.format( "%-23s%s", "Edited", "Applied" );
        bldr.append( fmtStr )
            .append("</strong>" )
            .append( endl );
            
        float   fEditVal    = graphManager.getGridUnit();
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
    
    private void logPropSet( 
        String          title,
        LinePropertySet editSet,
        LinePropertySet applSet,
        String          color
    )
    {
        String              leftStr = null;
        String              fmtStr  = null;
        String              prefix  = 
            "<pre style=\"color: " + color + ";\">";
        String  suffix  = "</pre>";
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( prefix )
            .append("<strong>" )
            .append( "*** " ).append( title ).append( " ***" )
            .append( endl );
        fmtStr = String.format( "%-23s%s", "Edited", "Applied" );
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
    
            bldr.append( suffix );
            log.append( bldr.toString(), null );
        }
    } 
    
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
    
    private void logTime()
    {
        LocalTime   timeNow = LocalTime.now();
        String      strNow  = timeNow.format( timeFormatter );
        log.append( "***** " + strNow + " *****", "" );
    }
    
    private String getColorStr( Color color )
    {
        int     iRGB    = color.getRGB() & 0xFFFFFF;
        String  sRGB    = String.format( "0x%06x", iRGB );
        return sRGB;
    }
    
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
    
    private class KeyMonitor implements KeyEventDispatcher
    {
        private static final int    ctrlAltDown =
            KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;
        
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
