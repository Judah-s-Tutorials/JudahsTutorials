package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * Application to read operator input
 * from the console
 * and produce a plot.
 * 
 * @author Jack Straub
 */
public class ListFrames
{
    private static final CartesianPlane  plane   = new CartesianPlane();
    private static final Root            root    = new Root( plane );
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args) throws InterruptedException
    {
        showBackground();
        root.start();
        showDialog( "Moses", "Let my people go", 100, 100 );
        showDialog( "Lincoln", "Fourscore and seven...", 150, 170 );
        showDialog( "Roosevelt", "Do one thing every day...", 200, 250 );
        showDialog( "Ginsburg", "Fight for the things...", 250, 325 );
        showDialog( "Sylvester", "Sufferin' succotash!", 300, 400 );
        
        String          message = "Push me to exit.";
        Thread          thread  = new Thread( () -> 
            JOptionPane.showMessageDialog( null, message )
        );
        thread.start();
        printTopWindows();
        thread.join();
        
        System.exit( 0 );
    }
    
    private static void showDialog( 
        String title, 
        String message,
        int xco,
        int yco
    )
    {
        JPanel  pane    = new JPanel();
        Border  border  = BorderFactory.createEmptyBorder( 15, 15, 15, 15 );
        pane.setBorder( border );
        pane.add( new JLabel( message ) );
        JDialog dialog  = new JDialog( (Frame)null, false );
        dialog.setTitle( title );
        dialog.setContentPane( pane );
        dialog.setLocation( xco, yco );
        dialog.pack();
        dialog.setVisible( true );
    }
    
    private static void showBackground()
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> 
            {
                JFrame  frame   = new JFrame( "Background" );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                JPanel  panel   = new JPanel();
                panel.setBackground( Color.BLACK );
                panel.setPreferredSize( new Dimension( 800, 700 ) );
                frame.setContentPane( panel );
                frame.pack();
                frame.setVisible( true );
            });
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private static void printTopWindows()
    {
        Window[]    windows = Window.getWindows();
        try
        {
            Thread.sleep( 500 );
            Arrays.stream( windows )
                .filter( w -> w instanceof JFrame )
                .map( w -> (JFrame)w )
                .forEach( jf -> {
                    System.out.println( jf.getClass().getName() );
                    System.out.println( jf.getTitle() );
                    System.out.println( jf.isVisible() );
                    System.out.println();
                });
             
            Arrays.stream( windows )
                .filter( w -> w instanceof JDialog )
                .map( w -> (JDialog)w )
                .forEach( jd -> {
                System.out.println( jd.getClass().getName() );
                System.out.println( jd.getTitle() );
                System.out.println( jd.isVisible() );
                System.out.println();
            });
        }
        catch ( InterruptedException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
}
