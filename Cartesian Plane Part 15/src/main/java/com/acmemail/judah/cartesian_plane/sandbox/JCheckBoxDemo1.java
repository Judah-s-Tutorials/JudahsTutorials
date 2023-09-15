package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

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

/**
 * Simple application
 * to examine the JToggleButton
 * and JCheckBox components.
 * Two buttons are displayed
 * (one JToggleButton and one JCheckBox).
 * All state changes and actions
 * are recorded in an activity log.
 * The operator
 * can introduce state changes
 * and actions
 * by mouse manipulation.
 * There are controls
 * to select or activate
 * each button; 
 * use these controls to observe
 * the events precipitated
 * by programmatically
 * selecting or activating a button.
 * 
 * @author Jack Straub
 */
public class JCheckBoxDemo1
{
    /** CSS properties for reporting a JCheckBox state event. */
    private static final String cbStateChangedStyle =
        "\"color: green; font-style: italic;\"";
    /** CSS properties for reporting a JCheckBox action event. */
    private static final String cbActionStyle       =
        "\"color: olive; font-style: bold;\"";
    /** CSS properties for reporting a JCheckBox item event. */
    private static final String cbItemStyle         =
        "\"color: #90EE90; font-style: plain;\"";

    /** CSS properties for reporting a JToggleButton state change event. */
    private static final String tbStateChangedStyle =
        "\"color: red; font-style: italic;\"";
    /** CSS properties for reporting a JToggleButton action event. */
    private static final String tbActionStyle       =
        "\"color: fuchsia; font-style: bold;\"";
    /** CSS properties for reporting a JToggleButton item event. */
    private static final String tbItemStyle         =
        "\"color: #800000; font-style: plain;\"";

    /** Separator to introduce into log when convenient to do so. */
    private static final String strSeparator        =
        "----------------------------------------";
    
    /** Activity log. */
    private ActivityLog     log;
    /** Check box being demonstrated. */
    private JCheckBox       checkBox;
    /** Toggle button being demonstrated. */
    private JToggleButton   toggleButton;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments, not used
     */
    public static void main(String[] args)
    {
        JCheckBoxDemo1  demo    = new JCheckBoxDemo1();
        SwingUtilities.invokeLater( () -> demo.build() );
    }

    /**
     * Create and deploy GUI.
     */
    private void build()
    {
        JFrame      frame       = new JFrame( "Check Box Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        log = new ActivityLog( frame );
        toggleButton = new JToggleButton( "Toggle Button" );
        checkBox = new JCheckBox( "Check Box" );
        
        JPanel          contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getTogglePanel(), BorderLayout.CENTER );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Create a JPanel
     * containing the toggle button
     * and check box
     * to demonstrate.
     * The buttons are configured
     * with ChangListeners and ActionListeners.
     * 
     * @return  the created panel
     */
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
        toggleButton.addItemListener( this::itemStateChanged );
        checkBox.addChangeListener( this::stateChanged );
        checkBox.addActionListener( this::actionPerformed );
        checkBox.addItemListener( this::itemStateChanged );
        
        return panel;
    }
    
    /**
     * Create a panel
     * containing the buttons
     * for controlling the demonstration.
     * There are two controls
     * for each button.
     * One toggles the value
     * of a button
     * using JToggleButton.setSelected.
     * The other simulates a button push
     * by invoking AbstractButton.doClick.
     * 
     * @return the created panel
     */
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
    
    /**
     * The stateChanged method
     * for StateChangeListeners.
     * Records the event
     * and the state of the button
     * associated with the event
     * in the application log.
     * 
     * @param evt   the reported event
     * 
     * @see ActivityLog#append(String, String)
     */
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
    
    /**
     * The itemStateChanged method
     * for ItemListeners.
     * Records the event
     * and the state of the button
     * associated with the event
     * in the application log.
     * 
     * @param evt   the reported event
     * 
     * @see ActivityLog#append(String, String)
     */
    private void itemStateChanged( ItemEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof JToggleButton )
        {
            JToggleButton   toggle  = (JToggleButton)source;
            StringBuilder   bldr    = new StringBuilder( "ITEM STATE CHANGE " );
            getToggleState( toggle, bldr );
            String          style   =
                toggle instanceof JCheckBox ? 
                    cbItemStyle : tbItemStyle;
            log.append( bldr.toString(), style );
        }
    }
    
    /**
     * The actionPerformed method
     * for ActionListeners.
     * Records the event
     * and the state of the button
     * associated with the event
     * in the application log.
     * 
     * @param evt   the reported event
     * 
     * @see ActivityLog#append(String, String)
     */
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
    
    /**
     * Creates a log message
     * describing the state
     * of a given toggle button.
     * The message is appended
     * to the given StringBuilder.
     * The log message includes:
     * <ul>
     * <li>The button label;</li>
     * <li>The button state (selected or deselected);</li>
     * <li>The button's armed state; and</li>
     * <li>The button's pressed state.</li>
     * </ul>
     * 
     * @param button    the given toggle button
     * @param bldr      the given StringBuilder
     */
    private void 
    getToggleState( JToggleButton button, StringBuilder bldr )
    {
        ButtonModel     model   = button.getModel();
        String          text    = button.getText();
        String          status  = button.isSelected() ? " selected" : " deselected";
        bldr.append( text )
            .append( status ).append( " (" )
            .append( "armed: ").append( model.isArmed() )
            .append( ", pressed: " ).append( model.isPressed() )
            .append( ", rollover: " ).append( model.isRollover() )
            .append( ")" );
    }
}
