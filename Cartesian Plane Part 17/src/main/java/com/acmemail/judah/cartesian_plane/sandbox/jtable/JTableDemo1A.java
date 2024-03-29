package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * This application is a minor revision of JTableDemo1
 * that shows how to traverse the data in a table.
 * It adds a button to the bottom of the application's frame
 * which, when pushed, traverses the table
 * printing the value of every cell.
 * 
 * @author Jack Straub
 * 
 * @see #printAction(JTable)
 * @see JTableDemo1 
 */
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
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private JTableDemo1A()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JTable      table       = new JTable( data, headers );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     print       = new JButton( "Print" );
        JButton     exit        = new JButton( "Exit" );
        print.addActionListener( e -> printAction( table ) );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( print );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Traverses a given table,
     * printing the values of all the table's cells.
     * 
     * @param table the given table
     */
    private void printAction( JTable table )
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
