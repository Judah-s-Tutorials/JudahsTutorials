package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.LengthFeedback;

public class FeedbackComponentTestAssistant
{
    private final JDialog   componentDialog = new JDialog();
    private final Border    dialogBorder    =
        BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
    private final Dimension defSize = new Dimension( 100, 25 );
    
    private LengthFeedback    feedback;
    
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
            new FeedbackComponentTestAssistant()
        );
    }
    
    public FeedbackComponentTestAssistant()
    {
        JFrame  frame   = new JFrame( "Feedback Component Test Assistant" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        LengthFeedback  feedback    =
            new LengthFeedback( () -> next += 2 );
        JButton next    = new JButton( "Next" );
        next.addActionListener( e -> feedback.repaint() );
        JPanel  contentPane = new JPanel();
        contentPane.add( next );
        frame.setContentPane(contentPane);;
        frame.pack();
        frame.setLocation( 100, 100 );;
        frame.setVisible( true );
        showDialog( "Length Feedback 4", feedback );
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
    }
}
