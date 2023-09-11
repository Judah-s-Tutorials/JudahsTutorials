package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;

/**
 * Simple application
 * to examine button events.
 * Two JButton displayed
 * and configured with event handlers.
 * All state changes and actions
 * reported by 
 * the encapsulated ButtonModel
 * are recorded in an activity log.
 * The operator
 * can introduce state changes
 * and actions
 * by mouse manipulation.
 * There are controls
 * to enable/disable or activate the button; 
 * use these controls to observe
 * the events precipitated
 * by programmatically
 * disabling or activating a button.
 * 
 * @author Jack Straub
 */
public class ButtonDemo1
{
    /** CSS properties for reporting a state event. */
    private static final String stateChangedStyle   =
        "\"color: green; font-style: italic;\"";
    /** CSS properties for reporting an action event. */
    private static final String actionStyle         =
        "\"color: olive; font-style: bold;\"";
    /** CSS properties for reporting a JCheckBox item event. */
    private static final String itemStyle           =
        "\"color: #90EE90; font-style: plain;\"";

    /** Separator to introduce into log when convenient to do so. */
    private static final String strSeparator        =
        "----------------------------------------";
    
    /** Activity log. */
    private ActivityLog     log;
    /** Check box being demonstrated. */
    private JButton         button;
    private JCheckBox       showSelected;
    private JCheckBox       showEnabled;
    
    private JFrame frame;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments, not used
     */
    public static void main(String[] args)
    {
        ButtonDemo1  demo    = new ButtonDemo1();
        SwingUtilities.invokeLater( () -> demo.build() );
    }

    /**
     * Create and deploy GUI.
     */
    private void build()
    {
//        JFrame      frame       = new JFrame( "Button Demo 1" );
        frame       = new JFrame( "Button Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        log = new ActivityLog( frame );
        
        JPanel          contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getDemoPanel(), BorderLayout.CENTER );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getDemoPanel()
    {
        button = new JButton( "Push Button" );
        button.addActionListener( this::actionPerformed );
        button.addChangeListener( this::stateChanged );
        button.addItemListener( this::itemStateChanged );
        
        JPanel  panel   = new JPanel();
        panel.add( button );
        return panel;
    }
    
    /**
     * Create a panel
     * containing the buttons
     * for controlling the demonstration.
     * One control
     * enables/disables the button,
     * and the other simulates a button push
     * by invoking AbstractButton.doClick.
     * 
     * @return the created panel
     */
    private JPanel getButtonPanel()
    {
        JPanel  panel       = new JPanel( new GridLayout( 3, 2, 3, 0 ) );
        
        Border  emptyBorder     = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        Border  bevelBorder     = 
            BorderFactory.createRaisedBevelBorder();
        Border  compoundBorder  = 
            BorderFactory.createCompoundBorder( emptyBorder, bevelBorder );
        Border  border          = 
            BorderFactory.createCompoundBorder( compoundBorder, emptyBorder );
        panel.setBorder( border );
        
        JCheckBox   enabled     = new JCheckBox( "Enable Push Button", true );
        JButton     doClick     = new JButton( "Do Click" );
        JButton     separator   = new JButton( "Separator" );
        showSelected = new JCheckBox( "Show Selected", true );
        showEnabled = new JCheckBox( "Show Enabled", true );
        panel.add( enabled );
        panel.add( doClick );
        panel.add( showEnabled );
        panel.add( showSelected );
        panel.add( new JLabel( "" ) );
        panel.add( separator );
        
        enabled.addActionListener( e -> 
            button.setEnabled( enabled.isSelected() )
        );
        doClick.addActionListener( e -> button.doClick() );
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
        if ( source instanceof JButton )
        {
            JButton         button  = (JButton)source;
            StringBuilder   bldr    = new StringBuilder( "STATE CHANGE " );
            getButtonState( button, bldr );
            String          style   = stateChangedStyle;
            log.append( bldr.toString(), style );
            System.out.println( evt );
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
        if ( source instanceof JButton )
        {
            JButton         button  = (JButton)source;
            StringBuilder   bldr    = new StringBuilder( "ITEM STATE CHANGE " );
            getButtonState( button, bldr );
            log.append( bldr.toString(), itemStyle );
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
        if ( source instanceof JButton )
        {
            JButton         button  = (JButton)source;
            StringBuilder   bldr    = new StringBuilder( "ACTION " );
            getButtonState( button, bldr );
            String          style   = actionStyle;
            log.append( bldr.toString(), style );
        }
    }
    
    /**
     * Creates a log message
     * describing the state
     * of a given button.
     * The message is appended
     * to the given StringBuilder.
     * The log message includes:
     * <ul>
     * <li>The button label;</li>
     * <li>The button state (selected or deselected);</li>
     * <li>The button's armed state; and</li>
     * <li>The button's pressed state.</li>
     * <li>The button's enabled state.</li>
     * </ul>
     * 
     * @param button    the given button
     * @param bldr      the given StringBuilder
     */
    private void 
    getButtonState( JButton button, StringBuilder bldr )
    {
        ButtonModel     model   = button.getModel();
        String          text    = button.getText();
        String          status  = button.isSelected() ? " selected" : " deselected";
        bldr.append( text );
        if ( showSelected.isSelected() )
            bldr.append( status );
        bldr.append( " (" )
            .append( "armed: ").append( model.isArmed() )
            .append( ", pressed: " ).append( model.isPressed() )
            .append( ", rollover: " ).append( model.isRollover() )
            .append( ", has focus: " ).append( button.isFocusOwner() );
        if ( showEnabled.isSelected() )
            bldr.append( ", enabled: " ).append( model.isEnabled() );
        bldr.append( ")" );
    }
}
