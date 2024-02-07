package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class JTableDemo1A
{
    private final String[]      headers = 
    { "First", "Last", "ID" };
    private final Object[][]    data    =
    {
        { "Jill", "Biden", 131157 },
        { "Melania", "Trump", 171129 },
        { "Michelle", "Obama", 200231 },
        { "Laura", "Bush", 100719 },
        { "Hillary", "Clinton", 131157 },
    };
    private final JTable        table   = new JTable( data, headers );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( JTableDemo1A::new );
    }
    
    private JTableDemo1A()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     print       = new JButton( "Print" );
        JButton     exit        = new JButton( "Exit" );
        print.addActionListener( this::printAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( print );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void printAction( ActionEvent evt )
    {
        int     rowCount    = table.getRowCount();
        int     colCount    = table.getColumnCount();
        for ( int row = 0 ; row < rowCount ; ++row )
        {
            for ( int col = 0 ; col < colCount ; ++col )
            {
                Object  value   = table.getValueAt( row, col );
                System.out.print( value + ", " );
            }
            System.out.println();
        }
    }
}
