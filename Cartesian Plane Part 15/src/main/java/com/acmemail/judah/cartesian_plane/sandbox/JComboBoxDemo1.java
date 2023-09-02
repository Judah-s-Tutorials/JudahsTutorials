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
public class JComboBoxDemo1
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
    /** Feedback window; updated every time an ActionEvent fires. */
    private JLabel      actionFB;
    /** Feedback window; updated every time an Item fires. */
    private JLabel      selectedFB;
    /** Feedback window; updated every time an Item fires. */
    private JLabel      deselectedFB;
    /** Activity log. */
    private JTextArea   activityLog;

    
    /**
     * Application entry point.
     * 
     * @param args command line arguments; not used
     */
    public static void main(String[] args)
    {
        new JComboBoxDemo1().makeGUI();
    }

    /**
     * Builds the GUI on the EDT.
     */
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
    
    /**
     * Composes a JPanel
     * containing the feed back windows.
     * 
     * @return  the composed JPanel
     */
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
        comboBox.addActionListener( e -> {
            String  selectedItem = getSelectedItem();
            actionFB.setText( selectedItem );
            appendToLog( "ACTION: " + selectedItem );
        });
        comboBox.addActionListener( e -> actionFB.setText( (String)comboBox.getSelectedItem() ));
        
        JPanel  panel   = new JPanel( new GridLayout( 3, 2, 10, 0 ) );
        panel.add( selected );
        panel.add( selectedFB );
        panel.add( deselected );
        panel.add( deselectedFB );
        panel.add( action );
        panel.add( actionFB );
        
        return panel;
    }
    
    /**
     * Composes a JPanel
     * to be displayed
     * on the left side
     * of the parent window.
     * The panel comprises
     * the combo box and the feedback panel.
     * 
     * @return  the composed JPanel
     */
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
            appendToLog( "COMMITTED: " + getSelectedItem() )
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
        activityLog = new JTextArea( 20, 30 );
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
    
    /*
     * Listens for ItemEvents.
     * Determines whether the event
     * encapsulates a selection of deselection,
     * and updates the corresponding 
     * feedback window,
     * and the activity log.
     * 
     * @param evt   the event to be processed
     */
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

    /**
     * Gets the currently selected item
     * from the combo box
     * and returns it
     * as a String.
     * 
     * @return  the currently selected item from the combo box
     */
    private String getSelectedItem()
    {
        return (String)comboBox.getSelectedItem();
    }
}
