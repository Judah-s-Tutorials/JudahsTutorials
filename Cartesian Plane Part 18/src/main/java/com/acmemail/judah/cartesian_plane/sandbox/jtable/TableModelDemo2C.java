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
 * This application is a minor modification
 * of TableModelDemo2B which designates column 2 
 * as type integer.
 * In this example
 * construction of the DefaultTableModel subclass,
 * which determines that column 2 is type integer,
 * is encapsulated in an anonymous class.
 * 
 * @author Jack Straub
 * 
 * @see TableModelDemo2B
 */
public class TableModelDemo2C
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> makeGUI() );
    }
    
    /**
     * Creates and shows the GUI,
     * using a JTable that incorporates the given table model.
     * 
     * @param model the given data model
     */
    private static void makeGUI()
    {
        String[]    headers = { "State", "Capital", "Population" };
        Object[][]  data    = 
            State.getDataSet( "state", "capital", "population" );
        @SuppressWarnings("serial")
        TableModel  model       =   
            new DefaultTableModel( data, headers ) {
                public Class<?> getColumnClass( int col ) 
                {
                    Class<?>    clazz   = col == 2 ? 
                        Integer.class : super.getColumnClass( col );
                    return clazz;
                }
            };

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
