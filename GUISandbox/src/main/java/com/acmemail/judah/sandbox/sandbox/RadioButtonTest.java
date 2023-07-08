package com.acmemail.judah.sandbox.sandbox;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

public class RadioButtonTest
{

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> build() );
    }
    
    private static void build()
    {
        JFrame  frame   = new JFrame( "JRadioButtonb Event Test" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( new ButtonPanel() );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }

    @SuppressWarnings("serial")
    private static class ButtonPanel extends JPanel
        implements ItemListener
    {
        private final ButtonGroup   group   = new ButtonGroup();
        public ButtonPanel()
        {
            BoxLayout layout    = new BoxLayout( this, BoxLayout.Y_AXIS );
            setLayout( layout );
            Arrays.stream( new String[] { "Alpha", "Baker", "Charlie" } )
                .map( JRadioButton::new )
                .peek( group::add )
                .peek( rb -> rb.addItemListener( this ) )
                .forEach( this::add );
        }
        
        public void itemStateChanged( ItemEvent evt )
        {
            Object  source      = evt.getSource();
            int     state       = evt.getStateChange();
            String  strState    = "???";
            if ( state == ItemEvent.DESELECTED )
                strState = "DESELECTED";
            else if ( state == ItemEvent.SELECTED )
                strState = "SELECTED";
            else
                strState = "???";
                
            if ( !(source instanceof JRadioButton) )
                throw new RuntimeException( "not radio button" );
            JRadioButton    butt        = (JRadioButton)source;
            String          feedback    =
                butt.getText() + ": " + butt.isSelected() + ", " + strState;
            System.out.println( feedback );
            System.out.println( "    " + group.getSelection().getClass().getName() );
        }
    }
}
