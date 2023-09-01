package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JComboBoxDemo1
{
    private static final String     endl        = System.lineSeparator();
    private static final String[]   items       =
    { 
        "Montgomery",     "Juneau",       "Phoenix",    "Little Rock", 
        "Sacramento",     "Denver",       "Hartford",   "Dover",
        "Tallahassee",    "Atlanta",      "Honolulu",   "Boise",
        "Springfield",    "Indianapolis", "Des Moines", "Topeka",
        "Frankfort",      "Baton Rouge ", "Augusta",    "Annapolis",
        "Boston",         "Lansing",      "St. Paul",   "Jackson",
        "Jefferson City", "Helena",       "Lincoln",    "Carson City",
        "Concord",        "Trenton",      "Santa Fe",   "Albany",
        "Raleigh",        "Bismark",      "Columbus",   "Oklahoma City",
        "Salem",          "Harrisburg",   "Providence", "Columbia",
        "Pierre",         "Nashville",    "Austin",     "Salt Lake City",
    };
    
    private JComboBox<String> comboBox;
    private JLabel      actionFB;
    private JLabel      selectedFB;
    private JLabel      deselectedFB;
    private JTextArea   activityLog;

    
    public static void main(String[] args)
    {
        new JComboBoxDemo1().makeGUI();
    }

    public void makeGUI()
    {
        comboBox  = new JComboBox<>( items );
        activityLog = new JTextArea( 20, 30 );
        JFrame      frame       = new JFrame( "ComboBox Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      mainPanel   = new JPanel( new BorderLayout() );
        mainPanel.add( getWestPanel(), BorderLayout.WEST );
        mainPanel.add( getActivityLog(), BorderLayout.CENTER );
        mainPanel.add( getButtonPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( mainPanel );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getFeedbackPanel()
    {
        JLabel  action      = new JLabel( "Action: " );
        JLabel  selected    = new JLabel( "Selected: " );
        JLabel  deselected  = new JLabel( "Deselect: " );
        action.setForeground( Color.WHITE );
        action.setOpaque( true );
        action.setBackground( Color.BLACK );
        selected.setForeground( Color.WHITE );
        selected.setOpaque( true );
        selected.setBackground( Color.BLACK );
        deselected.setForeground( Color.WHITE );
        deselected.setOpaque( true );
        deselected.setBackground( Color.BLACK );

        actionFB = new JLabel();
        selectedFB = new JLabel();
        deselectedFB = new JLabel();
        actionFB.setOpaque( true );
        actionFB.setBackground( Color.WHITE );
        actionFB.setForeground( Color.BLACK );
        
        comboBox.addItemListener( this::logStateChange );
        comboBox.addActionListener( e ->
            actionFB.setText( getSelectedItem() )
        );
        
        JPanel  panel   = new JPanel( new GridLayout( 3, 2, 10, 0 ) );
        panel.add( selected );
        panel.add( selectedFB );
        panel.add( deselected );
        panel.add( deselectedFB );
        panel.add( action );
        panel.add( actionFB );
        
        return panel;
    }
    
    private JPanel getWestPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        panel.add( comboBox );
        panel.add( Box.createRigidArea( new Dimension( 0, 150 ) ) );
        panel.add( getFeedbackPanel() );
        
        // If I put the above panel in a BorderLayout region, all the
        // components will be stretched out to fit the available area.
        // prevent the above components from stretching, put their panel
        // inside another (final) panel, then add the final panel to the
        // container with the BorderLayout. The final panel will still
        // stretch, but it won't stretch the panel inside it.
        JPanel  finalPanel  = new JPanel();
        finalPanel.add( panel );
        
        return finalPanel;
    }
    
    private JPanel getButtonPanel()
    {
        JButton select  = new JButton( "Commit" );
        JButton exit    = new JButton( "Exit" );
        select.addActionListener( e ->
            appendToLog( "committed: " + getSelectedItem() )
        );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        JPanel  panel   = new JPanel();
        panel.add( select );
        panel.add( exit );
        return panel;
    }
    
    private JComponent getActivityLog()
    {
        activityLog = new JTextArea( 20, 30 );
        JScrollPane pane    = new JScrollPane();
        pane.setViewportView( activityLog );
        activityLog.setEditable( false );
        return pane;
    }
    
    private void appendToLog( String text )
    {
        activityLog.append( text + endl );
        int     len     = activityLog.getDocument().getLength();
        activityLog.setCaretPosition( len );
    }
    
    private void logStateChange( ItemEvent evt )
    {
        String  state   = null;
        Object  item    = evt.getItem();
        if ( evt.getStateChange() == ItemEvent.SELECTED )
        {
            state = "SELECTED: ";
            selectedFB.setText( item.toString() );
        }
        else
        {
            state = "DESELECTED: ";
            deselectedFB.setText( item.toString() );
        }

        String  msg     = state + item;
        appendToLog( msg );
    }

    private String getSelectedItem()
    {
        return (String)comboBox.getSelectedItem();
    }
}
