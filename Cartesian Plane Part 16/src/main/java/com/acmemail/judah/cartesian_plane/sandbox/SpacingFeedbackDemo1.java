package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.LengthFeedback;
import com.acmemail.judah.cartesian_plane.components.SpacingFeedback;

/**
 * Application to demonstrate
 * the use of the <em>StrokeFeedback</em> component.
 * Revises {@linkplain SpacingFeedbackDemo1}
 * by adding to the content pane
 * a third row of three components:
 * a label, StrokeFeedback component
 * and a spinner that controls the value
 * of the stroke feedback component.
 * 
 * @author Jack Straub
 */
public class SpacingFeedbackDemo1
{
    /** Model encapsulated by the length spinner. */
    private final SpinnerNumberModel    lengthModel     = 
        new SpinnerNumberModel( 10, 0, 1000, 1 );
    /** Spinner that controls the length property. */
    private final JSpinner              lengthSpinner   = 
        new JSpinner( lengthModel );
    
    /** Model encapsulated by the spacing spinner. */
    private final SpinnerNumberModel    spacingModel    = 
        new SpinnerNumberModel( 10, 0, 1000, 1 );
    /** Spinner that controls the spacing property. */
    private final JSpinner              spacingSpinner  = 
        new JSpinner( spacingModel );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new SpacingFeedbackDemo1() );
    }
    
    /**
     * Create and show the GUI.
     */
    public SpacingFeedbackDemo1()
    {
        JFrame          frame           = new JFrame( "SpacingFeedback Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel          contentPane     = 
            new JPanel( new GridLayout( 2, 3, 3, 3 ) );
        
        // Configure and layout the length-related components
        JLabel          lengthLabel     = 
            new JLabel( "Length", SwingConstants.RIGHT );
        LengthFeedback  lengthFB    = new LengthFeedback( () -> 
            lengthModel.getNumber().doubleValue()
        );
        lengthSpinner.addChangeListener( e -> lengthFB.repaint() );
        contentPane.add( lengthLabel );
        contentPane.add( lengthSpinner );
        contentPane.add( lengthFB );

        // Configure and layout the spacing-related components
        JLabel          spacingLabel    = 
            new JLabel( "Spacing", SwingConstants.RIGHT );
        SpacingFeedback spacingFB       = new SpacingFeedback( () -> 
            spacingModel.getNumber().doubleValue() );
        spacingSpinner.addChangeListener( e -> spacingFB.repaint() );
        contentPane.add( spacingLabel );
        contentPane.add( spacingSpinner );
        contentPane.add( spacingFB );
        
        Border  emptyBorder = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        contentPane.setBorder( emptyBorder );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
}
