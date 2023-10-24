package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
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
public class RadioButtonDemo1a
{
    private ActivityLog log;
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new RadioButtonDemo1a().build() );
    }
    
    /**
     * Create and deploy the application GUI.
     */
    private void build()
    {
        JFrame      frame       = new JFrame( "Radio Button Demo 1a" );
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
        
        log = new ActivityLog( frame );
        JRadioButton    button1 = new JRadioButton( "Pick me!!" );
        JRadioButton    button2 = new JRadioButton( "No, me!!" );
        JRadioButton    button3 = new JRadioButton( "Me me me me me!!!!!" );
        button1.setSelected( true );
        ButtonGroup group   = new ButtonGroup();
        Stream.of( button1, button2, button3 )
            .peek( group::add )
            .peek( centerPanel::add )
            .peek( b -> b.addActionListener( this::actionPerformed ) )
            .forEach( b -> b.addItemListener( this::itemStateChanged ) );

        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Listener for action events.
     * 
     * @param evt   dispatched action event
     */
    private void actionPerformed( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof AbstractButton )
        {
            AbstractButton  button      = (AbstractButton)source;
            String          text        = button.getText();
            String          feedback    =
                "<span style='color: NAVY;'>" 
                + "Action Listener: "
                + text
                + " selected"
                + "</span>";
            log.append( feedback );
        }
    }
    
    /**
     * Listener for item events.
     * 
     * @param evt   dispatched item event
     */
    private void itemStateChanged( ItemEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof AbstractButton )
        {
            AbstractButton  button      = (AbstractButton)source;
            String          text        = button.getText();
            String          state       =
                button.isSelected() ? " selected" : " deselected";
            String          feedback    =
                "<span style='color: ORANGE;'>" 
                + "Item Listener: "
                + text
                + state
                + "</span>";
            log.append( feedback );
        }
    }
}
