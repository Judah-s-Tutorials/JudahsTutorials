package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.time.LocalDate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * This application shows how
 * to create a JSpinner
 * that traverses 
 * a sequence of integers.
 * 
 * @author Jack Straub
 */
public class JSpinnerDemo2
{
    /** Days of the week, to be displayed in spinner. */
    private static final    String[]    daysOfWeek  =
    { "Sunday",   "Monday", "Tuesday", "Wednesday", 
      "Thursday", "Friday", "Saturday"
    };
    
    /** The spinner being demonstrated. */
    private JSpinner    spinner;
    /** Feedback window for displaying selected value. */
    private JTextField  selected;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments, not used
     */
    public static void main(String[] args)
    {
        JSpinnerDemo2   demo    = new JSpinnerDemo2();
        SwingUtilities.invokeLater( () -> demo.buildGUI() );
    }

    /**
     * Create and show the GUI.
     */
    private void buildGUI()
    {
        JFrame  frame   = new JFrame( "JSpinner Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      mainPanel   = new JPanel();
        BoxLayout   mainLayout  = 
            new BoxLayout( mainPanel, BoxLayout.Y_AXIS );
        mainPanel.setLayout( mainLayout );
        
        Dimension   dim         = new Dimension( 0, 5 );
        mainPanel.add( getSpinnerPanel() );
        mainPanel.add( Box.createRigidArea( dim ) );
        mainPanel.add( getFeedbackPanel() );
        mainPanel.add( Box.createRigidArea( dim ) );
        mainPanel.add( getButtonPanel() );
        
        frame.setContentPane( mainPanel );
        frame.setLocation( 400, 400 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Gets a panel
     * containing the JSpinner
     * and a label.
     * 
     * @return  a panel containing the labeled JSpinner
     */
    private JPanel getSpinnerPanel()
    {
        SpinnerListModel    dayModel    =
            new SpinnerListModel( daysOfWeek );
        spinner = new JSpinner( dayModel );
        
        JLabel      label   = new JLabel( "Choose best day" );
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        
        Dimension   spacer  = new Dimension( 5, 0 );
        panel.add( label );
        panel.add( Box.createRigidArea( spacer ) );
        panel.add( spinner );
        
        return panel;
    }
    
    /**
     * Gets a panel
     * containing the feedback window
     * and a label.
     * 
     * @return  a panel containing the labeled feedback window
     */
    private JPanel getFeedbackPanel()
    {
        selected = new JTextField( 14 );
        selected.setEditable( false );
        selected.setHorizontalAlignment( SwingConstants.CENTER );
        
        JLabel  label   = new JLabel( "Scheduled for" );
        JPanel  panel   = new JPanel();
        panel.add( label );
        panel.add( selected );
        return panel;
    }
    
    /**
     * Gets a panel
     * containing the select and exit buttons.
     * 
     * @return  a panel containing the button controls
     */
    private JPanel getButtonPanel()
    {
        JButton select  = new JButton( "Select" );
        JButton exit    = new JButton( "Exit" );
        select.addActionListener( this::selectCallback );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        JPanel  panel   = new JPanel();
        panel.add( select );
        panel.add( exit );
        return panel;
    }
    
    /**
     * Invoked when the select button
     * is pushed.
     * Copies the current value of the spinner
     * into the feedback window.
     * 
     * @param evt   event that precipitated the invocation
     */
    private void selectCallback( ActionEvent evt )
    {
        try
        {
            spinner.commitEdit();
            Object  day = spinner.getValue();
            selected.setText( day.toString() );
        }
        catch ( ParseException exc )
        {
            selected.setText( "#ERROR" );
        }
    }
}
