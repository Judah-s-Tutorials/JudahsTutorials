package com.acmemail.judah.sandbox.sandbox;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

public class NumberSpinnerTester
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new NumberSpinnerTester().buildGUI() );
    }
    
    private void buildGUI()
    {
        SpinnerNumberModel  model       = new SpinnerNumberModel( 5, 1, 10, 1 );
        JSpinner            spinner     = new JSpinner( model );
        JButton             button      = new JButton( "Test" );
        button.addActionListener( e -> model.setValue( 100 ) );
        
        JFrame  frame   = new JFrame( "Number Spinner Test" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  panel   = new JPanel();
        panel.add( spinner );
        panel.add( button );
        frame.setContentPane( panel );
        frame.pack();
        frame.setVisible( true );
    }
}
