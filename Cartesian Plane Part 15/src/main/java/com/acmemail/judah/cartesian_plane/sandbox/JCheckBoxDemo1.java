package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;

public class JCheckBoxDemo1
{
    private static final String cbStateChangedStyle =
        "\"color: green; font-style: italic;\"";
    private static final String cbActionStyle       =
        "\"color: olive; font-style: bold;\"";
    private static final String tbStateChangedStyle =
        "\"color: red; font-style: italic;\"";
    private static final String tbActionStyle       =
        "\"color: fuchsia; font-style: bold;\"";
    private static final String strSeparator        =
        "----------------------------------------";
    
    private ActivityLog     log;
    private JCheckBox       checkBox;
    private JToggleButton   toggleButton;
    
    public static void main(String[] args)
    {
        JCheckBoxDemo1  demo    = new JCheckBoxDemo1();
        SwingUtilities.invokeLater( () -> demo.build() );
    }

    private void build()
    {
        log = new ActivityLog();
        toggleButton = new JToggleButton( "Toggle Button" );
        checkBox = new JCheckBox( "Check Box" );
        
        JFrame      frame       = new JFrame( "Check Box Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel          contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getTogglePanel(), BorderLayout.CENTER );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getTogglePanel()
    {
        JPanel      panel   = new JPanel( new GridLayout( 1, 2, 4, 0 ) );
        Border      border  = BorderFactory.createEmptyBorder( 5, 70, 0, 5 );
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.add( toggleButton );
        panel.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
        panel.setBorder( border );
        panel.setLayout( layout );
        panel.add( checkBox );
        
        toggleButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        checkBox.setAlignmentX( Component.CENTER_ALIGNMENT );
        
        toggleButton.addChangeListener( this::stateChanged );
        toggleButton.addActionListener( this::actionPerformed );
        checkBox.addChangeListener( this::stateChanged );
        checkBox.addActionListener( this::actionPerformed );
        
        return panel;
    }
    
    private JPanel getButtonPanel()
    {
        JPanel  panel       = new JPanel( new GridLayout( 3, 3, 3, 3 ) );
        
        Border  emptyBorder     = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        Border  bevelBorder     = 
            BorderFactory.createRaisedBevelBorder();
        Border  compoundBorder  = 
            BorderFactory.createCompoundBorder( emptyBorder, bevelBorder );
        Border  border          = 
            BorderFactory.createCompoundBorder( compoundBorder, emptyBorder );
        panel.setBorder( border );
        
        JLabel  tbLabel     = new JLabel( "Toggle Button" );
        JButton tbToggle    = new JButton( "Toggle Value" );
        JButton tbDoClick   = new JButton( "Do Click" );
        panel.add( tbLabel );
        panel.add( tbToggle );
        panel.add( tbDoClick );
        
        JLabel  cbLabel     = new JLabel( "Check Box" );
        JButton cbToggle    = new JButton( "Toggle Value" );
        JButton cbDoClick   = new JButton( "Do Click" );
        panel.add( cbLabel );
        panel.add( cbToggle );
        panel.add( cbDoClick );
        
        JButton separator   = new JButton( "Separator" );
        panel.add( new JLabel( "" ) );
        panel.add( new JButton( "" ) );
        panel.add( separator );
        
        tbToggle.addActionListener( e -> 
            toggleButton.setSelected( !toggleButton.isSelected() )
        );
        tbDoClick.addActionListener( e -> toggleButton.doClick() );
        cbToggle.addActionListener( e -> 
            checkBox.setSelected( !checkBox.isSelected() )
        );
        cbDoClick.addActionListener( e -> checkBox.doClick() );
        separator.addActionListener( e -> log.append( strSeparator ) );
        
        return panel;
    }
    
    private void stateChanged( ChangeEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof JToggleButton )
        {
            JToggleButton   toggle  = (JToggleButton)source;
            StringBuilder   bldr    = new StringBuilder( "STATE CHANGE " );
            getToggleState( toggle, bldr );
            String          style   =
                toggle instanceof JCheckBox ? 
                    cbStateChangedStyle : tbStateChangedStyle;
            log.append( bldr.toString(), style );
        }
    }
    
    private void actionPerformed( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof JToggleButton )
        {
            JToggleButton   toggle  = (JToggleButton)source;
            StringBuilder   bldr    = new StringBuilder( "ACTION " );
            getToggleState( toggle, bldr );
            String          style   =
                toggle instanceof JCheckBox ? 
                    cbActionStyle : tbActionStyle;
            log.append( bldr.toString(), style );
        }
    }
    
    private void getToggleState( JToggleButton toggle, StringBuilder bldr )
    {
        ButtonModel     model   = toggle.getModel();
        String          text    = toggle.getText();
        String          status  =
            toggle.isSelected() ? " selected" : " deselected";
        bldr.append( text ).append( status )
            .append( " (" )
                .append( "armed: ").append( model.isArmed() )
                .append( ", pressed: " ).append( model.isPressed() )
            .append( ")" );
    }
}
