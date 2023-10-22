package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * This is a simple application
 * to demonstrate how radio buttons work.
 * Three JRadioButtons are instantiated
 * and added to a <em>ButtonGroup<em>.
 * When one button in the group
 * is selected,
 * the previously selected button
 * (if any) 
 * is deselected
 * (it "pops up").
 * When a button is selected
 * its ActionListeners are invoked.
 * 
 * @author Jack Straub
 */
public class RadioButtonDemo1
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new RadioButtonDemo1().build() );
    }
    
    /**
     * Create and deploy the application GUI.
     */
    private void build()
    {
        JFrame      frame       = new JFrame( "Radio Button Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );

        JPanel      centerPanel = new JPanel();
        Border      border      = 
            BorderFactory.createEmptyBorder( 20, 20, 20, 20 );
        centerPanel.setBorder( border );
        BoxLayout   layout      = 
            new BoxLayout( centerPanel, BoxLayout.Y_AXIS );
        centerPanel.setLayout( layout );
        contentPane.add( centerPanel, BorderLayout.CENTER );
        
        ActivityLog log     = new ActivityLog( frame );
        ButtonGroup group   = new ButtonGroup();
        Stream.of( "Pick me!!", "No, me!!", "Me me me me me!!!!!" )
            .forEach( s -> {
                JRadioButton    button  = new JRadioButton( s );
                group.add( button );
                centerPanel.add( button );
                button.addActionListener( e -> 
                    log.append( "Selected: " + button.getText() ));
            });

        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
}
