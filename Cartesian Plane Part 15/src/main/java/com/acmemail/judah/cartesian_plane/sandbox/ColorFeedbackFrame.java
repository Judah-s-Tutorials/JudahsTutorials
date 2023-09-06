package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class 
 * is for use
 * by other sandbox applications.
 * It encapsulates a frame 
 * with an empty window
 * (the "feedback window")
 * and buttons labeled
 * "execute" and "exit".
 * Pushing the exit button
 * will terminate the application.
 * The behavior of the execute button
 * is controlled by an ActionListener
 * provided by the user.
 * The user can change
 * the color of the feedback window
 * by calling the {@link #setColor(Color)} method.
 * 
 * @author Jack Straub
 */
public class ColorFeedbackFrame
{
    /** 
     * The feedback window.
     * Its properties are modified
     * in the {@link #setColor(Color)} method.
     */
    private JPanel  feedbackWindow;
    
    /**
     * Creates a frame with an empty window
     * (the "feedback window")
     * and buttons labeled
     * "execute" and "exit".
     * Pushing the exit button
     * will terminate the application.
     * The behavior of the execute button
     * is controlled by an ActionListener
     * given by the caller.
     * 
     * @param listener  the given ActionListener
     * 
     * @see #setColor(Color)
     */
    public void makeGUI( ActionListener listener )
    {
        JFrame  frame   = new JFrame( "Feedback Window" ); 
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JButton exit    = new JButton( "Exit" );
        JButton execute = new JButton( "Execute" );
        JPanel  buttons = new JPanel();
        buttons.add( execute );
        buttons.add( exit );
        execute.addActionListener( listener );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        feedbackWindow = new JPanel();
        feedbackWindow.setPreferredSize( new Dimension( 150, 150 ) );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( feedbackWindow, BorderLayout.CENTER );
        contentPane.add( buttons, BorderLayout.SOUTH );
        
        frame.setLocation( 200, 200 );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Changes the background
     * of the feedback window
     * to the given color.
     * 
     * @param color the given color
     */
    public void setColor( Color color )
    {
        feedbackWindow.setBackground( color );
    }
}
