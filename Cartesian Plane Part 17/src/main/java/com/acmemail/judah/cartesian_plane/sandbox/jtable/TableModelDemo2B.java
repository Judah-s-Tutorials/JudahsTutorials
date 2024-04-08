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
 * Second part of an example showing how to designate a column
 * as a specific data type.
 * In this part,
 * column 2 is designated type integer.
 * It is right-justfied, 
 * and attempting to give it a non-integer value
 * results in an error.
 * 
 * @author Jack Straub
 * 
 * @see TableModelDemo2A
 * @see TableModelDemo2C
 */
public class TableModelDemo2B
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
        TableModel  model   = new LocalTableModel( data, headers );
        SwingUtilities.invokeLater( () -> makeGUI( model ) );
    }
    
    /**
     * Creates and shows the GUI,
     * using a JTable that incorporates the given table model.
     * 
     * @param model the given data model
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

    /**
     * Model to express column 2 as integer data.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private static class LocalTableModel extends DefaultTableModel
    {
        /* Constructor.
         * 
         * @param data      data for table model
         * @param headers   headers for table model
         */
        public LocalTableModel( Object[][] data, Object[] headers )
        {
            super( data, headers );
        }
        
        /**
         * Force column 2 to be type integer.
         * All other columns default.
         */
        @Override
        public Class<?> getColumnClass( int col ) 
        {
            Class<?>    clazz   = 
                col == 2 ? Integer.class : super.getColumnClass( col );
            return clazz;
        }
    }}
