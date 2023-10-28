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
 * via an anonymous class.
 * This is a revision of
 * {@linkplain ComponentListenerDemo1}
 * which used a nested class
 * to implement the ComponentListener.
 * 
 * @author Jack Straub
 */
public class ComponentListenerDemo2
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
        
        dialog.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentHidden( ComponentEvent evt )
            {
                String  message =
                    "Hidden: " + dialog.getTitle() + ", " + dialog.isVisible();
                System.out.println( message );
            }
            @Override
            public void componentShown( ComponentEvent evt )
            {
                String  message =
                    "Shown: " + dialog.getTitle() + ", " + dialog.isVisible();
                System.out.println( message );
            }
        });
        
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
        dialog.setTitle( "ComponentListener Demo 2" );
        dialog.add( label );
        dialog.pack();
    }
}
