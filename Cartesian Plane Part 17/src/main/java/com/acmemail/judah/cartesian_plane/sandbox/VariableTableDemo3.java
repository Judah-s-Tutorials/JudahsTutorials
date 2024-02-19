package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VariableTableDemo3
{
    private static final String lineSep = System.lineSeparator();
    private static final String input   =
        "x,1" + lineSep +
        "y,2" + lineSep +
        "z,3" + lineSep;
    
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
        StringReader    reader  = new StringReader( input );
        VariableTableB  table   = null;
        try
        {
            table = new VariableTableB( reader );
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
