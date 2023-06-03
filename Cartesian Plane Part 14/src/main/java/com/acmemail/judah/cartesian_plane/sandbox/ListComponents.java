package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
public class ListComponents
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
        JFileChooser    chooser = new JFileChooser();
        Thread          thread  =
            new Thread( () -> chooser.showOpenDialog( null ) );
        thread.start();
        processTopWindows();
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
    
    private static void processTopWindows()
    {
        try
        {
            Thread.sleep( 500 );
        }
        catch ( InterruptedException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
            
        Predicate<Window>   isDialog        = w -> w instanceof JDialog;
        Predicate<Window>   isFrame         = w -> w instanceof JFrame;
        Predicate<Window>   isDialogOrFrame = isDialog.or( isFrame );
        Arrays.stream( Window.getWindows() )
            .filter( isDialogOrFrame )
            .peek( ListComponents::printFrame )
            .map( ListComponents::getContentPane )
            .forEach( c -> dumpContainer( c, 1 ) );
    }
    
    private static void printFrame( Window window )
    {
        System.out.print( "class name: " );
        System.out.println( window.getClass().getName() );
        System.out.print( "window name: " );
        System.out.println( window.getName() );
        System.out.print( "title: " );
        if ( window instanceof JFrame )
            System.out.println( ((JFrame)window).getTitle() );
        else if ( window instanceof JDialog )
            System.out.println( ((JDialog)window).getTitle() );
        else 
            System.out.println( "(none)" );
    }
    
    private static Container getContentPane( Window window )
    {
        Container   container   = null;
        if ( window instanceof JDialog )
            container = ((JDialog)window).getContentPane();
        else if ( window instanceof JFrame )
            container = ((JFrame)window).getContentPane();
        else
        {
            String  name    = window.getClass().getName();
            String  message = "Not JDialog or JFrame: " + name;
            throw new ComponentException( message );
        }
        return container;
    }
    
    private static void dumpContainer( Container container, int indentNum )
    {
        String          spaces  = "    ";
        StringBuilder   bldr    = new StringBuilder();
        IntStream.range( 0, indentNum ).forEach( i -> bldr.append( spaces ) );
        String          indent  = bldr.toString();
        
        Component[]     children    = container.getComponents();
        String          text        = null;
        if ( container instanceof JLabel )
            text = ((JLabel)container).getText();
        else if ( container instanceof JButton )
            text = ((JButton)container).getText();
        else
            ;
        System.out.println( indent + container.getClass().getName() );
        System.out.println( indent + "name: " + container.getName() );
        System.out.println( indent + "text: " + text );
        System.out.println( indent + "num children: " + children.length );
        System.out.println( indent + "====================" );
        for ( Component child : children )
        {
            if ( child instanceof Container )
                dumpContainer( (Container)child, indentNum + 1 );
            else
                dumpComponent( child, indent + spaces );
        }
    }
        
    private static void dumpComponent( Component comp, String indent )
    {
        System.out.println( indent + comp.getClass().getName() );
        System.out.println( indent + "name: " + comp.getName() );
        String  text    = null;
        if ( comp instanceof JLabel )
            text = ((JLabel)comp).getText();
        else if ( comp instanceof JButton )
            text = ((JButton)comp).getText();
        else
            ;
        System.out.println( indent + "text: " + text );
    }
}
