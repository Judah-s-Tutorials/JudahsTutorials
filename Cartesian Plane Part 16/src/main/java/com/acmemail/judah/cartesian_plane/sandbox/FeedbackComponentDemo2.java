package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.ColorEditor;
import com.acmemail.judah.cartesian_plane.components.LengthFeedback;
import com.acmemail.judah.cartesian_plane.components.SpacingFeedback;
import com.acmemail.judah.cartesian_plane.components.StrokeFeedback;

/**
 * Application collect
 * all our feedback components together:
 * LengthFeedback, SpacingFeedbac, StrokeFeedback
 * and ColorEditor.
 * 
 * @author Jack Straub
 */
public class FeedbackComponentDemo2
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
    
    /** Model encapsulated by the stroke spinner. */
    private final SpinnerNumberModel    strokeModel     = 
        new SpinnerNumberModel( 3, 0, 1000, 1 );
    /** Spinner that controls the stroke property. */
    private final JSpinner              strokeSpinner   = 
        new JSpinner( strokeModel );
    
    /** Components for managing color. */
    private final ColorEditor           colorEditor     =
        new ColorEditor();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new FeedbackComponentDemo2() );
    }
    
    /**
     * Create and show the GUI.
     */
    public FeedbackComponentDemo2()
    {
        JFrame          frame           = new JFrame( "FeedbackComponent Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel          contentPane     = 
            new JPanel( new GridLayout( 4, 3, 3, 3 ) );
        
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

        // Configure and layout the stroke-related components
        JLabel          strokeLabel     = 
            new JLabel( "Stroke", SwingConstants.RIGHT );
        StrokeFeedback  strokeFB        = new StrokeFeedback( () -> 
            strokeModel.getNumber().doubleValue() );
        strokeSpinner.addChangeListener( e -> strokeFB.repaint() );
        contentPane.add( strokeLabel );
        contentPane.add( strokeSpinner );
        contentPane.add( strokeFB );
        
        // Configure ColorEditor components.
        contentPane.add( colorEditor.getColorButton() );
        contentPane.add( colorEditor.getTextEditor() );
        contentPane.add( colorEditor.getFeedback() );
        
        Border  emptyBorder = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        contentPane.setBorder( emptyBorder );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
}
