package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.VariableTable;

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
        VariableTable   table   = null;
        try
        {
            table = new VariableTable();
            table.load( inFile );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        JPanel          cPane   = new JPanel( new BorderLayout() );
        cPane.add( table.getPanel(), BorderLayout.CENTER );
        
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
