package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.LengthFeedback;

public class FeedbackComponentTA1
{
    private final JDialog   componentDialog = new JDialog();
    private final Border    dialogBorder    =
        BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
    private final Dimension defSize = new Dimension( 100, 25 );
    private final double[]  propValues      = 
        { 2, 8, 32, 64, 2, 8, 32 };
    
    private LengthFeedback  feedback;
    private int             nextVal     = 1;
    private double          currVal     = propValues[0];
    private double          currWeight  = -1;
    
    private final JTextField    weightField     = new JTextField( 5 );
    private final JLabel        propValueLabel  = new JLabel();
    private final JLabel        weightLabel     = new JLabel();
    private final JLabel        lastSavedLabel  = new JLabel();
    
    private double  next    = 2;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> 
            new FeedbackComponentTA1()
        );
    }
    
    public FeedbackComponentTA1()
    {
        JFrame  frame   = new JFrame( "Feedback Component Test Assistant" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        feedback = new LengthFeedback( () -> currVal );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getDisplayPanel(), BorderLayout.CENTER );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        updateDisplay();
        
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocation( 100, 100 );;
        frame.setVisible( true );
        showDialog( "Length Feedback 4", feedback );
        
        File    file    = Utils.getTestDataDir( "Feedback/LengthData" );
        if ( file == null )
            System.out.println( "fail" );
        else
            System.out.println( file.getAbsolutePath() );
    }
    
    private JPanel getButtonPanel()
    {
        JButton     next        = new JButton( "Next" );
        JButton     save        = new JButton( "Save" );
        JButton     weight      = new JButton( "Weight" );
        JButton     exit        = new JButton( "Exit" );
        
        next.addActionListener( this::nextActionPerformed );
        save.addActionListener( this::saveActionPerformed );
        weightField.addActionListener( this::weightActionPerformed );
        weight.addActionListener( this:: weightActionPerformed );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.add( next );
        panel.add( save );
        panel.add( weightField );
        panel.add( weight );
        panel.add( exit );
        
        return panel;
    }
    
    private JPanel getDisplayPanel()
    {
        int     align   = SwingConstants.RIGHT;
        Border  border  = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel  panel       = new JPanel( new GridLayout( 3, 2, 3, 3 ) );
        panel.setBorder( border );
        
        panel.add( new JLabel( "Curr prop:", align ) );
        panel.add( propValueLabel );
        
        panel.add( new JLabel( "Curr weight:", align ) );
        panel.add( weightLabel );
        
        panel.add( new JLabel( "Last Saved:", align ) );
        panel.add( lastSavedLabel );
        
        return panel;
    }
    
    private void updateDisplay()
    {
        String  fmt         = "%4.1f";
        String  strValue    = String.format( fmt,  currVal );
        String  strWeight   = String.format( fmt, currWeight );
        propValueLabel.setText( strValue );
        weightLabel.setText( strWeight );
    }
    
    private void nextActionPerformed( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof JComponent )
        {
            currVal = propValues[nextVal];
            if ( ++nextVal == propValues.length )
                ((JComponent)source).setEnabled( false );
            updateDisplay();
            feedback.repaint();
        }
    }
    
    private void weightActionPerformed( ActionEvent evt )
    {
        try
        {
            String  text    = weightField.getText();
            double  weight  = Double.parseDouble( text );
            if ( weight <= 0 )
                throw new NumberFormatException();
            currWeight = weight;
            updateDisplay();
            feedback.setWeight( (float)currWeight );
            feedback.repaint();
        }
        catch ( NumberFormatException exc )
        {
            weightField.setText( "error" );
        }
    }
    
    private void saveActionPerformed( ActionEvent evt )
    {
        
    }
    
    private void showDialog( String title, JComponent component )
    {
        componentDialog.setVisible( false );
        component.setPreferredSize( defSize );
        componentDialog.setTitle( title );
        JPanel  contentPane = new JPanel();
        contentPane.setBorder( dialogBorder );
        contentPane.add( component );
        componentDialog.setContentPane( contentPane );
        
        componentDialog.setLocation( 300, 300 );
        componentDialog.pack();
        componentDialog.setVisible( true );
        
        System.out.println( component.getSize() );
    }
}
