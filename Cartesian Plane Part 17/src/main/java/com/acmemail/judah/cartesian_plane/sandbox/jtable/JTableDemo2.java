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

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

/**
 * This application shows how to set the type of a column
 * in order to take advantage of the type-specific
 * editing and formatting capabilities of a JTable.
 * Specifically, the population column
 * (the third column, or column index number 2)
 * is set to integer.
 * This cause the data in the column
 * to be right-justified, 
 * and to prevent the operator
 * from editing a cell in the column
 * to contain a non-integer value.
 * <p>
 * The column type is set 
 * by declaring a nested class that extends DefaultTableModel
 * (see {@linkplain LocalTableModel})
 * and overriding the getColumnClass method.
 * Note that the nested class
 * is responsible for setting the table's
 * header and data array;
 * the JTable is then instantiated 
 * using the constructor
 * that requires a TableModel as an argument.
 * 
 * @author Jack Straub
 */
public class JTableDemo2
{
    /** Array of table headers (column names). */
    private static String[]     headers     =
    { "State", "Abbreviation", "Population" };
    /** Array of table data. */
    private static Object[][]   data        =
        State.getDataSet( "state", "abbreviation", "population" );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        JTableDemo2 demo    = new JTableDemo2();
        SwingUtilities.invokeLater( () -> demo.buildGUI() );
    }
    
    /**
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private void buildGUI()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        TableModel  model       = new LocalTableModel( data, headers );
        JTable      table       = new JTable( model );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Supplies the JPanel that displays
     * the frame's Exit button.
     * 
     * @return  the JPanel that displays the frame's Exit button
     */
    private JPanel getButtonPanel()
    {
        JPanel      buttonPanel = new JPanel();
        JButton     exit        = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( exit );
        return buttonPanel;
    }
    
    /**
     * Subclasses DefaultTableModel for the purpose
     * of overriding getColumnClass.
     * The overridden method sets the type 
     * of the third column (column #2)
     * to Integer.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private static class LocalTableModel extends DefaultTableModel
    {
        /**
         * Constructor.
         * Sets the TableModel's
         * header an data rows.
         * 
         * @param data      
         *      the table's data; equivalent to the data argument
         *      passed to the DefaultTableModel constructor
         *      DefaultTableModel(Object[][] data, Object[] headers)
         * @param headers
         *      the table's headers; equivalent to the headers argument
         *      passed to the DefaultTableModel constructor
         *      DefaultTableModel(Object[][] data, Object[] headers)
         */
        public LocalTableModel( Object[][] data, Object[] headers)
        {
            super( data, headers );
        }
        
        /**
         * Establishes the type of data in a column
         * based on the given column index.
         * If the column index is two
         * the type is explicitly set to Integer,
         * otherwise the type returned by this method
         * in the superclass is used.
         * 
         *  @param  col the given column index.
         */
        @Override
        public Class<?> getColumnClass( int col )
        {
            Class<?>    clazz   = col == 2 ?
                Integer.class : super.getColumnClass( col );
            return clazz;
        }
    }
}
