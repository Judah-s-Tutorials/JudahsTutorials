package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * First part of an example showing how to designate a column
 * as a specific data type.
 * In this part,
 * column 2 should be type integer
 * but does not behave that way.
 * It is not right-justfied, 
 * and you can give it non-integer values.
 * 
 * @author Jack Straub
 * 
 * @see TableModelDemo2B
 */
public class TableModelDemo2A
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        String[]    headers = { "State", "Capital", "Population" };
        Object[][]  data    = 
            State.getDataSet( "state", "capital", "population" );
        TableModel  model   = new DefaultTableModel( data, headers );
        SwingUtilities.invokeLater( () -> makeGUI( model ) );
    }
    
    /**
     * Creates a JTable incorporating the given model.
     * 
     * @param model the given model
     */
    private static void makeGUI( TableModel model )
    {
        JFrame      frame       = new JFrame( "Table Model Demo 2B" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JTable      table       = new JTable( model );
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
