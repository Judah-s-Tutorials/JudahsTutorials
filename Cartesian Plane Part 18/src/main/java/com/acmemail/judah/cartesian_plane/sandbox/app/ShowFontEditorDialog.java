package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.FontEditorDialog;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.Profile;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * Application to display the {@link FontEditorDialog}.
 * The main application has
 * an Edit button and a Log button.
 * The FontDialog is instantiated
 * with a Profile that belongs
 * to the application class.
 * To see the {@link FontEditorDialog}
 * press the edit button;
 * to see the current font properties
 * maintained in the Profile
 * press the log button.
 * 
 * @author Jack Straub
 * 
 * @see FontEditor
 */
public class ShowFontEditorDialog
{
    /** For formatting the current time. */
    private static final DateTimeFormatter  timeFormatter   = 
        DateTimeFormatter.ofPattern( "HH:mm:ss" );
    /**
     * Main window properties from the encapsulated Profile.
     */
    private final GraphPropertySet  propertySet;
    /** The FontEditorDialog to be displayed. */
    private final FontEditorDialog  dialog;
    /** Window for logging events and font properties. */
    private final ActivityLog       log;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( ShowFontEditorDialog::new );
    }

    /**
     * Constructor.
     * Full configures the GUI.
     * Makes the application frame
     * and the activity log visible;
     * does not make the editor visible.
     */
    public ShowFontEditorDialog()
    {
        JFrame  frame       = new JFrame( "Show Font Editor Dialog " );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  contentPane = new JPanel( new GridLayout( 1, 3, 3, 3 ) );
        Border  border      =
            BorderFactory.createEmptyBorder( 25, 25,  25, 25 );
        contentPane.setBorder( border );
        frame.setContentPane( contentPane );
        
        JButton editButton  = new JButton( "Edit" );
        JButton logButton   = new JButton( "Log" );
        JButton exitButton  = new JButton( "Exit" );
        editButton.addActionListener( this::showDialog );
        logButton.addActionListener( this::showLog );
        contentPane.add( editButton );
        contentPane.add( logButton );
        contentPane.add( exitButton );
        
        exitButton.addActionListener( e -> System.exit( 0 ) );
        Point       frameLocation   = new Point( 100, 100 );
        frame.pack();
        frame.setLocation( frameLocation );
        Dimension   frameSize       = frame.getPreferredSize();
        frame.setVisible( true );
        
        log = new ActivityLog();
        int         logLocationX    = frameLocation.x;
        int         logLocationY    = 
            frameLocation.y + frameSize.height + 10;
        log.setLocation( logLocationX, logLocationY );
        log.setVisible( true );
        
        Profile     profile     = new Profile();
        propertySet = profile.getMainWindow();
        dialog = new FontEditorDialog( frame, propertySet );
        int         editorLocationX =
            frameLocation.x + frameSize.width + 10;
        int         editorLocationY = frameLocation.y;
        dialog.setLocation( editorLocationX, editorLocationY );
    }
    
    /**
     * Shows the dialog and logs the status
     * after the dialog is closed.
     * This method is invoked
     * when the Edit button is selected.
     * 
     * @param evt   object describing this ActionEvent
     */
    private void showDialog( ActionEvent evt )
    {
        int     result      = dialog.showDialog();
        String  strResult   = null;
        if ( result == JOptionPane.OK_OPTION )
            strResult = "OK";
        else if ( result == JOptionPane.CANCEL_OPTION )
            strResult = "Cancel";
        else
            strResult = "Unknown";
        
        logTime();
        log.append( "Result: " + strResult);
    }
    
    /**
     * Logs the font properties.
     * This method is invoked
     * when the Log button is selected.
     * 
     * @param evt   object describing this ActionEvent
     */
    private void showLog( ActionEvent evt )
    {
        Color   fontBGColor     = propertySet.getBGColor();
        String  strBGColor      = getColorVal( fontBGColor );
        Color   fontFGColor     = propertySet.getFGColor();
        String  strFGColor      = getColorVal( fontFGColor );

        logTime();
        log.append( "Font name: " + propertySet.getFontName() );
        log.append( "Font size: " + propertySet.getFontSize() );
        log.append( "Font style: " + propertySet.getFontStyle() );
        log.append( "Font FG color: " + strFGColor );
        log.append( "Font BG color: " + strBGColor );
    }
    
    /**
     * Logs the current time.
     */
    private void logTime()
    {
        LocalTime   timeNow = LocalTime.now();
        String      strNow  = timeNow.format( timeFormatter );
        log.append( "***** " + strNow + " *****", "" );
    }
    
    /**
     * Converts the given color to a hex string.
     * 
     * @param color the given color
     * 
     * @return  the hex string representation of the given color
     */
    private static String getColorVal( Color color )
    {
        int     iRGB    = color.getRGB() & 0xFFFFFF;
        String  strRGB  = String.format( "0x%06x", iRGB  );
        return strRGB;
    }
}
