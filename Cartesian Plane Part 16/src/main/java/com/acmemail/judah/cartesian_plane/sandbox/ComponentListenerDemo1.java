package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * Simple application
 * to demonstrate
 * how to use a ComponentAdapter
 * to implement a ConmponentListener
 * to detect changes
 * in a Component's visibility.
 * The ComponentListener
 * is implemented 
 * via a nested class.
 * See also 
 * {@linkplain ComponentListenerDemo2}
 * which substitutes
 * an anonymous class
 * for the nested class.
 * 
 * @author Jack Straub
 */
public class ComponentListenerDemo1
{
    private static JDialog  dialog;
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> build() );
    }

    /**
     * Create and show GUI.
     */
    private static void build()
    {
        JFrame  frame   = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        makeDialog();
        dialog.addComponentListener( new VListener() );
        JButton button  = new JButton( "Show" );
        button.addActionListener( e -> dialog.setVisible( true ) );
        frame.getContentPane().add( button );
        
        frame.pack();
        frame.setLocation( 300, 300 );
        frame.setVisible( true );
    }
    
    /**
     * Create (without showing) the demo dialog.
     */
    private static void makeDialog()
    {
        String  labelText   = 
            "<html><p style="
            + "'font-size: 300%;'>" 
            + "THIS IS A DIALOG"
            + "</p></html>";
        JLabel  label       = new JLabel( labelText );
        dialog = new JDialog();
        dialog.setTitle( "ComponentListener Demo 1" );
        dialog.add( label );
        dialog.pack();
    }
    
    /**
     * Component listener
     * that detects when a Component
     * has changed visibility.
     * 
     * @author Jack Straub
     */
    private static class VListener extends ComponentAdapter
    {
        /**
         * Invoked when component loses visibility.
         */
        @Override
        public void componentHidden( ComponentEvent evt )
        {
            String  message =
                "Hidden: " + dialog.getTitle() + ", " + dialog.isVisible();
            System.out.println( message );
        }

        /**
         * Invoked when component becomes visible.
         */
        @Override
        public void componentShown( ComponentEvent evt )
        {
            String  message =
                "Shown: " + dialog.getTitle() + ", " + dialog.isVisible();
            System.out.println( message );
        }
    }
}
