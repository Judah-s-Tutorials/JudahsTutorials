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

public class LengthFeedbackDemo1
{
    private final SpinnerNumberModel    numberModel = 
        new SpinnerNumberModel( 10, 0, 1000, 1 );
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
        SwingUtilities.invokeLater( () -> new LengthFeedbackDemo1() );
    }
    
    public LengthFeedbackDemo1()
    {
        JFrame          frame       = new JFrame( "LengthFeedback Demo1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel          contentPane = 
            new JPanel( new GridLayout( 1, 3, 3, 3 ) );
        JLabel          label       = 
            new JLabel( "Length", SwingConstants.RIGHT );
        LengthFeedback  lengthFB    = 
            new LengthFeedback( () -> numberModel.getNumber().doubleValue() );
        spinner.addChangeListener( e -> lengthFB.repaint() );
        
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
