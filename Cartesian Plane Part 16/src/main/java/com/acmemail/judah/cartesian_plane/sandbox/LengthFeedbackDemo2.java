package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Dimension;

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

/**
 * This is a revision
 * of {@linkplain LengthFeedbackDemo1}
 * that addresses the issue
 * of making sure
 * the LengthFeedback component
 * has non-0 dimensions.
 * The previous application
 * used a GridLayout( 1, 3 )
 * to layout 3 components,
 * which gave the LengthFeedback component
 * the same dimensions
 * as the other two components.
 * In this application
 * we lay out the same components
 * using a FlowLayout,
 * which doesn't resize its children.
 * We have to give the LengthFeedback component
 * explicit dimensions,
 * otherwise they will be 0.
 * We do this
 * by giving the LengthComponent
 * a width of 100,
 * and a height
 * that is the same
 * as the label
 * that precedes it.
 * 
 * @author Jack Straub
 */
public class LengthFeedbackDemo2
{
    /** Model encapsulated by the spinner. */
    private final SpinnerNumberModel    numberModel = 
        new SpinnerNumberModel( 10, 0, 1000, 1 );
    /** Model encapsulated by the spinner. */
    private final JSpinner              spinner     = 
        new JSpinner( numberModel );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new LengthFeedbackDemo2() );
    }
    
    /**
     * Create and show the GUI.
     */
    public LengthFeedbackDemo2()
    {
        JFrame          frame       = new JFrame( "LengthFeedback Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel          contentPane = new JPanel();
        JLabel          label       = 
            new JLabel( "Length", SwingConstants.RIGHT );
        LengthFeedback  lengthFB    = 
            new LengthFeedback( () -> numberModel.getNumber().doubleValue() );
        spinner.addChangeListener( e -> lengthFB.repaint() );
        
        // Give the LengthFeedback component an explicit width of 100,
        // and the same height as the preceding label.
        Dimension   prefSize    = label.getPreferredSize();
        prefSize.width = 100;
        lengthFB.setPreferredSize( prefSize );
        
        Border  emptyBorder = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        contentPane.setBorder( emptyBorder );
        contentPane.add( label );
        contentPane.add( spinner );
        contentPane.add( lengthFB );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
}
