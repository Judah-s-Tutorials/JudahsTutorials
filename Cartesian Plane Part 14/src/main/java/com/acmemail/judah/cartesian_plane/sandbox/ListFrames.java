package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
    public static void main(String[] args)
    {
        try
        {
            showBackground();
            root.start();
            newThread( () -> showDialog( 
                "Moses", 
                "Let my people go", 
                100, 
                100 
            ));
            newThread( () -> showDialog( 
                "Lincoln", 
                "Fourscore and seven...", 
                150, 
                170 
            ));
            newThread( () -> showDialog( 
                "Roosevelt", 
                "No one can make you feel inferior ...",
                200,
                250 
            ));
            newThread( () -> showDialog( 
                "Ginsburg", 
                "Fight for the things that you care about, ...",
                250,
                325 
            ));
            newThread( () -> showDialog( 
                "Sylvester", 
                "Sufferin' succotash!",
                300,
                400 
            ));
            newThread( () -> printTopWindows() );
            JOptionPane.showMessageDialog( null, "Push me to exit" );
        }
        catch ( InterruptedException exc )
        {
            exc.printStackTrace();
        }
        
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
    
    private static void newThread( Runnable runnable )
        throws InterruptedException
    {
            new Thread( runnable ).start();
            Thread.sleep( 100 );
    }
    
    private static void showBackground()
        throws InterruptedException
    {
        JFrame  frame   = new JFrame( "Background" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  panel   = new JPanel();
        panel.setBackground( Color.BLACK );
        panel.setPreferredSize( new Dimension( 800, 700 ) );
        frame.setContentPane( panel );
        frame.pack();
        frame.setVisible( true );
        Thread.sleep( 100 );
    }
    
    private static void printTopWindows()
    {
        try
        {
            Thread.sleep( 500 );
            Arrays.stream( Frame.getFrames() )
                .filter( f -> f instanceof JFrame )
                .map( f -> (JFrame)f )
                .forEach( jf -> {
                    System.out.println( jf.getClass().getName() );
                    System.out.println( jf.getTitle() );
                    System.out.println( jf.isVisible() );
                    System.out.println();
                });
             
            Arrays.stream( Window.getWindows() )
                .filter( w -> w instanceof JDialog )
                .map( w -> (JDialog)w )
                .forEach( w -> {
                System.out.println( w.getClass().getName() );
                System.out.println( w.getTitle() );
                System.out.println( w.isVisible() );
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
