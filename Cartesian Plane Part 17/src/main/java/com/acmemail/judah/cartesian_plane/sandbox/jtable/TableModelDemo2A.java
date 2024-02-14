package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * This application is part one
 * of a demonstration of how to use column types.
 * By default column types are assumed to be
 * type Object.
 * A cell is displayed and edited 
 * as though it were a string.
 * In this demo the third column (column index number 2)
 * contains population data.
 * This is data that we would normally 
 * displayed right-justifed and edited as an integer,
 * but because its column type is Object
 * it is left justified,
 * and the cell editor allows you
 * to enter non-numeric data.
 * The solution to this
 * is applied in application {@linkplain TableModelDemo2B}.
 * 
 * @author Jack Straub
 * 
 * @see TableModelDemo2B
 */
public class TableModelDemo2A
{
    /** Table header array */
    private final String[]      headers = 
        { "State", "Capital", "Population" };
    /** Table data array. */
    private final Object[][]    data    = 
        State.getDataSet( "state", "capital", "population" );
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new TableModelDemo2A() );
    }
    
    /**
     * Constructor.
     * Configures and displays the application frame.
     * Must be executed on the EDT.
     */
    public TableModelDemo2A()
    {
        JFrame      frame       = new JFrame( "Tabel Model Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        DefaultTableModel   model       = 
            new DefaultTableModel( data, headers );
        JTable              table       = new JTable( model );
        JPanel              contentPane = new JPanel( new BorderLayout() );
        JScrollPane         scrollPane  = new JScrollPane( table );
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
