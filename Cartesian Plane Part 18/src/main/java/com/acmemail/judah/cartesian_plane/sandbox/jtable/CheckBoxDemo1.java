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
 * This is an application that shows how
 * to implement a simple JTable.
 * The JTable is instantiated
 * using data and header arrays,
 * an positioned in a JScrollPane.
 * 
 * @author Jack Straub
 */
public class CheckBoxDemo1
{
    /** Array of headers (column names). */
    private final String[]      headers = 
    { "Name", "Present" };
    /** Data array. */
    private final Object[][]    data    =
    {
        { "Alex", true },
        { "Ashley", true },
        { "Jesse", true },
        { "Joyce", false },
        { "Leslie", true },
        { "Riley", true },
        { "Robin", true },
        { "Ryan", false },
    };
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( CheckBoxDemo1::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private CheckBoxDemo1()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        TableModel  model       = new LocalTableModel( data, headers );
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
     * Subclass of DefaultTableModel
     * that is used to establish the type
     * of column 2 in the GUI's JTable.
     * 
     * @author Jack Straub
     * 
     * @see #getColumnClass(int)
     */
    @SuppressWarnings("serial")
    private static class LocalTableModel extends DefaultTableModel
    {
        public LocalTableModel( Object[][] data, Object[] headers )
        {
            super( data, headers );
        }
        
        /**
         * Establishes the type of a column
         * in the GUI's table.
         * If the given column is 2
         * returns Integer.class,
         * otherwise returns the default
         * obtained from the superclass.
         * 
         * @param the given column number
         */
        @Override
        public Class<?> getColumnClass( int col ) 
        {
            Class<?>    clazz   = 
                col == 1 ? Boolean.class : super.getColumnClass( col );
            return clazz;
        }
    }
}
