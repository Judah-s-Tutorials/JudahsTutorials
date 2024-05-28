package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.PIListener;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * Last in a series of demos
 * to use a KeyListener
 * to detect keystrokes
 * in a text field.
 * The ultimate goal
 * is to detect "pi^P"
 * and then substitute &Pi;.
 * 
 * @author Jack Straub
 * 
 * @see PIListenerDemo2
 * @see PIListenerDemo
 */
public class PIListenerDemo
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new PIListenerDemo().buildGUI() );
    }
    
    /**
     * Creates the GUI and makes it visible.
     */
    private void buildGUI()
    {
        JFrame      frame   = new JFrame( "PI Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      pane    = new JPanel( new BorderLayout() );
        JTextField  field   = new JTextField( 10 );
        field.addKeyListener( new PIListener() );
        pane.add( field, BorderLayout.NORTH );
        
        JPanel      buttons = new JPanel();
        JButton     exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 1 ) );
        buttons.add( exit );
        pane.add( buttons, BorderLayout.SOUTH );
        
        JButton     print   = new JButton( "Print" );
        print.addActionListener( e -> {
            String  text    = field.getText();
            text += " " + text.length();
            System.out.println( text );
        });
        buttons.add( print );
        pane.add( buttons, BorderLayout.SOUTH );

        frame.setContentPane( pane );
        frame.pack();
        GUIUtils.center( frame );
        frame.setVisible( true );
    }
}
