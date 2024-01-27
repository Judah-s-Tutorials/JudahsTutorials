package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import com.acmemail.judah.cartesian_plane.components.MessagePane;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.sandbox.utils.SandboxUtils;

/**
 * Demonstrates how to position mouse
 * over the text of a hyperlink
 * then select the hyperlink.
 * 
 * @author Jack Straub
 */
public class ModelToView2DDemo
{
    /** 
     * Address, 
     * within the test/resources directory,
     * that contains the hyperlink 
     * we wish to select.
     */
    private static final String linkResource    = 
        "SandboxDocs/ModelToView2DDemo/TestLinkResource.html";
    
    /** JEditorPane derived from MessagePane object. */
    private static JEditorPane  ePane;
    /** JDialog derived from MessagePane object. */
    private static JDialog      dialog;
    /** Robot for performing mouse actions. */
    private static Robot        robot   = getRobot();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
     */
    public static void main(String[] args) throws IOException
    {
        GUIUtils.schedEDTAndWait( () -> makeGUI() );
        SwingUtilities.invokeLater( () -> showDialog() );
        SandboxUtils.pause( 2000 );
        System.out.println( ePane.getText() );
        
        GUIUtils.schedEDTAndWait( () -> positionMouse( 5 ) );
        SandboxUtils.pause( 2000 );
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
    }
    
    /**
     * Obtains a JDialog containing a JEditorPane.
     */
    private static void makeGUI()
    {
        MessagePane mPane   = MessagePane.ofResource( linkResource );
        ePane   = mPane.getEditorPane();
        dialog = mPane.getDialog( null, "ModelToView2DDemo" );
        dialog.setLocation( 100, 200 );
    }
    
    /**
     * Starts a new thread to make the test dialog visible.
     * Note that the dialog is modal.
     * When the dialog is closed
     * the application is terminated.
     * This method is intended
     * to be called
     * from the EDT.
     */
    private static void showDialog()
    {
        Thread  thread  = new Thread( () -> {
            dialog.setVisible( true );
            System.exit( 0 );
        });
        thread.start();
    }
    
    /**
     * Convenience method to obtain the screen coordinates
     * of a given text position
     * within the JEditorPane contained
     * in the test dialog.
     * This method is intended
     * to be called from the EDT.
     * If an exception is thrown
     * the application is terminated.
     * 
     * @param textPosition  given text position
     * 
     * @return  the screen coordinates of the given text position
     */
    private static void positionMouse( int textPosition )
    {
        try
        {
            Rectangle2D rect        = ePane.modelToView2D( textPosition );
            Point       screenPos   = ePane.getLocationOnScreen();
            int         xco         = (int)rect.getX() + screenPos.x;
            int         yco         = (int)rect.getY() + screenPos.y;
            robot.mouseMove( xco, yco );
        }
        catch ( BadLocationException exc )
        {
            exc.printStackTrace();
        }
    }
    
    /**
     * Convenience routine to instantiate a Robot
     * and perform exception handling.
     * If instantiating the Robot
     * throws an exception
     * the application is terminated.
     * 
     * @return  the instantiated robot
     */
    private static Robot getRobot()
    {
        Robot   robot   = null;
        try
        {
            robot = new Robot();
        }
        catch ( AWTException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return robot;
    }
}
