package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * This application demonstrates how to 
 * dynamically add a row to a table 
 * after the table has been created.
 * 
 * @author Jack Straub
 */
public class JTableDemo2
{
    private final String[]      headers = 
    { "State", "Abbrev", "Population" };
    private final Object[][]    data    =
    State.getDataSet( "state", "abbreviation", "population" );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( JTableDemo2::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private JTableDemo2()
    {
        JFrame      frame       = new JFrame( "JTable Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        TableModel  model       = new LocalTableModel( data, headers );
        JTable      table       = new JTable( model );
        addSelectColumn( table );
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
     * Adds a fourth column to a given JTable's model.
     * The header of the column is select,
     * and the value of all cells is (Boolean)false.
     * 
     * @param table the given JTable
     */
    private void addSelectColumn( JTable table )
    {
        TableModel          temp        = table.getModel();
        if ( !(temp instanceof DefaultTableModel) )
            throw new ComponentException( "wrong table model type" );
        DefaultTableModel   model       = (DefaultTableModel)temp;
        int                 rowCount    = model.getRowCount();
        Object[]            colData     = new Object[rowCount];
        Arrays.fill( colData, false );
        model.addColumn( "Select", colData );
    }
    
    /**
     * Traverses a given table,
     * printing the all selected rows.
     * 
     * @param table the given table
     */
    private void printAction( JTable table )
    {
        int     rowCount    = table.getRowCount();
        for ( int row = 0 ; row < rowCount ; ++row )
        {
            Object  value   = table.getValueAt( row, 3 );
            if ( value instanceof Boolean && (Boolean)value )
            {
                StringBuilder   bldr    = new StringBuilder();
                bldr.append( table.getValueAt( row, 0 ) ).append( ", " )
                    .append( table.getValueAt( row, 1 ) ).append( ", " )
                    .append( table.getValueAt( row, 2 ) );
                System.out.println( bldr );
            }
        }
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
         * in the GUI's JTable.
         * 
         * @param the given column number
         */
        @Override
        public Class<?> getColumnClass( int col ) 
        {
            Class<?>    clazz   = null;
            if ( col == 2 )
                clazz = Integer.class;
            else if ( col == 3 )
                clazz = Boolean.class;
            else
                clazz = super.getColumnClass( col );
            return clazz;
        }
    }
}
