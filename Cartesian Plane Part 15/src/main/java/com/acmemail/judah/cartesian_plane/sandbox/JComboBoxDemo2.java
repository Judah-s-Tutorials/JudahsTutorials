package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This is a simple application
 * to demonstrate how
 * a non-editable JComBox works.
 * In addition to a JComboBox,
 * the demo has three feedback windows
 * and an activity log.
 * Each time an item is selected
 * or deselected
 * the activity log
 * and the selected/deselected feedback windows
 * are updated. 
 * This is accomplished 
 * by putting an ItemListener
 * on the JComboBox.
 * In addition,
 * Each time an item is selected
 * the activity log
 * and the action feedback windows
 * are updated.
 * 
 * @author Jack Straub
 */
public class JComboBoxDemo2
{
    /** Line separator; declared here for convenience. */
    private static final String     endl        = System.lineSeparator();
    /** Items to go in drop-down list. */
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
    
    /** Combo box being demonstrated. */
    private JComboBox<String> comboBox;
    /** Activity log. */
    private JTextArea   activityLog;

    
    /**
     * Application entry point.
     * 
     * @param args command line arguments; not used
     */
    public static void main(String[] args)
    {
        new JComboBoxDemo2().makeGUI();
    }

    /**
     * Builds the GUI on the EDT.
     */
    public void makeGUI()
    {
        comboBox  = new JComboBox<>( items );
        comboBox.setEditable( true );
        JFrame      frame       = new JFrame( "ComboBox Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      mainPanel   = new JPanel( new BorderLayout() );
        mainPanel.add( getMainPanel(), BorderLayout.NORTH );
        mainPanel.add( getButtonPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( mainPanel );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Composes a JPanel
     * containing combo box
     * and the activity log.
     * 
     * @return  the composed JPanel
     */
    private JPanel getMainPanel()
    {
        comboBox.addItemListener( e -> {
            String  state   = 
                e.getStateChange() == ItemEvent.SELECTED ?
                    "SELECTED: " : "DESELECTED: ";
            appendToLog( state + e.getItem() );
        });

        comboBox.addActionListener( e -> 
            appendToLog( "ACTION: " + comboBox.getSelectedItem() )
        );
        
        JPanel      panel   = new JPanel();
        panel.add( comboBox );
        panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        panel.add( getActivityLog() );
        
        return panel;
    }
    
    /**
     * Composes a JPanel 
     * containing the 
     * commit and exit buttons.
     * 
     * @return  the composed JPanel
     */
    private JPanel getButtonPanel()
    {
        JButton commit  = new JButton( "Commit" );
        JButton exit    = new JButton( "Exit" );
        commit.addActionListener( e ->
            appendToLog( "COMMITTED: " + comboBox.getSelectedItem() )
        );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        JPanel  panel   = new JPanel();
        panel.add( commit );
        panel.add( exit );
        return panel;
    }
    
    /**
     * Instantiates and configures
     * the activity log.
     * 
     * @return  the activity log
     */
    private JComponent getActivityLog()
    {
        activityLog = new JTextArea( 10, 20 );
        JScrollPane pane    = new JScrollPane();
        pane.setViewportView( activityLog );
        activityLog.setEditable( false );
        return pane;
    }
    
    /**
     * Appends the given text
     * to the activity log
     * and follows it
     * with a line separator.
     * 
     * @param text  the given text
     */
    private void appendToLog( String text )
    {
        activityLog.append( text + endl );
        int     len     = activityLog.getDocument().getLength();
        activityLog.setCaretPosition( len );
    }
}
