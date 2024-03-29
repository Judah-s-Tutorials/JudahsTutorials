package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * This is an application that shows how
 * to implement a simple JTable.
 * The JTable is instantiated
 * using data and header arrays,
 * and positioned in a JScrollPane.
 * 
 * @author Jack Straub
 */
public class JTableDemo1
{
    /** Array of headers (column names). */
    private final String[]      headers = 
    { "First", "Last", "ID" };
    /** Data array. */
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
        SwingUtilities.invokeLater( JTableDemo1::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private JTableDemo1()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JTable      table       = new JTable( data, headers );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     exit        = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
}
