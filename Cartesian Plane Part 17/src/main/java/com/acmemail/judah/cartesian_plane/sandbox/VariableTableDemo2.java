package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.VariablePanel;
import com.acmemail.judah.cartesian_plane.components.VariableTable;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

public class VariableTableDemo2
{
    private static final String inFile  = 
        "data/VariableTableData1.csv";
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> buildGUI() );
    }
    
    private static void buildGUI()
    {
        VariablePanel   varPanel    = new VariablePanel();
        Equation        equation    = new Exp4jEquation();
        IntStream.range( 0, 15 )
            .forEach( i -> 
                equation.setVar("" + (char)('a' + i), i * 1.1 )
            );
        varPanel.load( equation );

        JPanel          cPane   = new JPanel( new BorderLayout() );
        cPane.add( varPanel, BorderLayout.CENTER );
        
        JButton         exit    = new JButton( "Exit" );
        JPanel          buttons = new JPanel();
        exit.addActionListener( e -> System.exit( 0 ) );
        buttons.add( exit );
        cPane.add( buttons, BorderLayout.SOUTH );

        String          title   = "Variable Table Demo 2";
        JFrame          frame   = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( cPane );
        frame.pack();
        frame.setLocation( 100, 100 );
        frame.setVisible( true );
    }
}
