package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.LengthFeedback;
import com.acmemail.judah.cartesian_plane.components.PButtonGroup;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.components.SpacingFeedback;
import com.acmemail.judah.cartesian_plane.components.StrokeFeedback;

/**
 * Application to demonstrate
 * the use of the <em>SpacingFeedback</em> component.
 * Revises {@linkplain LineFeedbackDemo1}
 * by adding to the content pane
 * a second row of three components:
 * a label, SpacingFeedback component
 * and a spinner that controls the value
 * of the spacing feedback component.
 * 
 * @author Jack Straub
 */
public class FeedbackComponentDemo1
{
    /** Model encapsulated by the length spinner. */
    private final SpinnerNumberModel    lengthModel     = 
        new SpinnerNumberModel( 10, 0, 1000, 1 );
    /** Spinner that controls the length property. */
    private final JSpinner              lengthControl   = 
        new JSpinner( lengthModel );
    
    /** Text component that controls spacing property. */
    private final JTextField            spacingControl  = 
        new JTextField( "5", 10 );
    
    /** Button group that controls the stroke property. */
    private final PButtonGroup<Double>  strokeControl   = 
        new PButtonGroup<>();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new FeedbackComponentDemo1() );
    }
    
    /**
     * Create and show the GUI.
     */
    public FeedbackComponentDemo1()
    {
        JFrame          frame           = new JFrame( "FeedbackComponent Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel          contentPane     = 
            new JPanel( new GridLayout( 3, 3, 3, 3 ) );
        
        // Configure and layout the length-related components
        JLabel          lengthLabel     = 
            new JLabel( "Length", SwingConstants.RIGHT );
        LengthFeedback  lengthFB    = new LengthFeedback( () -> 
            lengthModel.getNumber().doubleValue()
        );
        lengthControl.addChangeListener( e -> lengthFB.repaint() );
        contentPane.add( lengthLabel );
        contentPane.add( lengthControl );
        contentPane.add( lengthFB );

        // Configure and layout the spacing-related components
        JLabel          spacingLabel    = 
            new JLabel( "Spacing", SwingConstants.RIGHT );
        SpacingFeedback spacingFB       = new SpacingFeedback( () -> 
            parseTextControl()
        );
        spacingControl.addActionListener( e -> spacingFB.repaint() );
        contentPane.add( spacingLabel );
        contentPane.add( spacingControl );
        contentPane.add( spacingFB );

        // Configure and layout the stroke-related components
        JPanel          strokePanel     = getStrokePanel();
        JLabel          strokeLabel     = 
            new JLabel( "Stroke", SwingConstants.RIGHT );
        StrokeFeedback  strokeFB        = new StrokeFeedback( () -> 
            strokeControl.getSelectedProperty()
        );
        strokeControl.getButtons().stream()
            .forEach( b -> b.addActionListener( e -> strokeFB.repaint() ) );
        strokeControl.selectIndex( 0 );
        contentPane.add( strokeLabel );
        contentPane.add( strokePanel );
        contentPane.add( strokeFB );
        
        Border  emptyBorder = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        contentPane.setBorder( emptyBorder );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getStrokePanel()
    {
        PRadioButton<Double>   small    = new PRadioButton<>( 1. );
        PRadioButton<Double>   medium   = new PRadioButton<>( 5. );
        PRadioButton<Double>    large   = new PRadioButton<>( 10. );
        strokeControl.add( small );
        strokeControl.add( medium );
        strokeControl.add( large );
        
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK );
        panel.setBorder( border );
        panel.setLayout( layout );
        panel.add( new JLabel( "Sm", SwingConstants.RIGHT ) );
        panel.add( small );
        panel.add( new JLabel( "Med", SwingConstants.RIGHT ) );
        panel.add( medium );
        panel.add( new JLabel( "Lg", SwingConstants.RIGHT ) );
        panel.add( large );
        
        return panel;
    }
    
    private double parseTextControl()
    {
        double  value   = 1;
        try
        {
            String  text    = spacingControl.getText();
            value = Double.parseDouble( text );
        }
        catch ( NumberFormatException exc )
        {
            spacingControl.setText( "##Error##");
        }
        
        return value;
    }
}
