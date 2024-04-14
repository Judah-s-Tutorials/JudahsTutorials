package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * This application shows how to fix
 * the issue demonstrated in {@linkplain RenderingDemo2A}.
 * In the previous application
 * replacing the data in a table
 * caused the table columns to lose formatting.
 * In this application the problem is fixed
 * by reconfiguring the columns
 * after replacing the data.
 * 
 * @author Jack Straub
 * 
 * @see #updateAction(ActionEvent)
 */
public class RenderingDemo2B
{
    /** Table headers. */
    private final Object[]      headers = { "Col 1", "Col 2" };
    /** Table data. */
    private final Object[][]    data    = { 
        { "a", 1000 },{ "b", 2000 },{ "c", 3000 }
    };
    
    /** Table model. */
    private final DefaultTableModel model   = 
        new LocalTableModel( data, headers );
    /** Demo table. */
    private final JTable            table   = new JTable( model );
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( RenderingDemo2B::new );
    }
    
    /**
     * Constructor.
     * Configures and displays the GUI.
     */
    private RenderingDemo2B()
    {
        TableColumnModel    colModel    = table.getColumnModel();
        TableColumn         column1     = colModel.getColumn( 1 );
        column1.setCellRenderer( new ValueRenderer() );

        JFrame      frame       = new JFrame( "SetDataVectorTest" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( table, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     update      = new JButton( "Update" );
        JButton     exit        = new JButton( "Exit" );
        update.addActionListener( this::updateAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( update );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Replaces the data (and header)
     * in the demo table.
     * Invoked when the Update button is pushed.
     * 
     * @param evt   object accompanying action event; not used
     */
    @SuppressWarnings("rawtypes")
    private void updateAction( ActionEvent evt )
    {
        Vector<Vector>  data        = model.getDataVector();
        List<Object>    cHeaders    = Arrays.asList( headers );
        Vector<Object>  vHeaders    = new Vector<>( cHeaders );
        model.setDataVector( data, vHeaders );
        TableColumnModel    colModel    = table.getColumnModel();
        TableColumn         column1     = colModel.getColumn( 1 );
        column1.setCellRenderer( new ValueRenderer() );
    }
    
    /**
     * Subclass of DefaultTableModel used with the demo JTable.
     * Overrides getColumnClass in order to
     * designate column 1 as type integer.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private static class LocalTableModel extends DefaultTableModel
    {
        /**
         * Constructor.
         * Pass the given data and header arrays
         * to the superclass.
         * 
         * @param data      the given data array
         * @param headers   the given header array
         */
        public LocalTableModel( Object[][] data, Object[] headers )
        {
            super( data, headers );
        }
        
        /**
         * Determines the type of a given column in the demo table.
         * Column 1 is designated type integer,
         * all other columns default.
         * 
         * @param col   the given column
         * 
         * @return the type of the given column
         */
        @Override
        public Class<?> getColumnClass( int col ) 
        {
            Class<?>    clazz   = null;
            if ( col == 1 )
                clazz = Integer.class;
            else
                clazz = super.getColumnClass( col );
            return clazz;
        }
    }
    
    /**
     * Subclass of DefaultTableCellRenderer
     * that formats an integer value
     * with group separators.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private class ValueRenderer extends DefaultTableCellRenderer
    {
        /**
         * Formats the given value
         * as a string with group separators.
         * The given value is assumed to be
         * an integer type.
         * 
         * @param value the integer value to format
         */
        @Override
        public void setValue( Object value )
        {
            if ( value != null )
            {
                String  format   = "%,d";
                String  fmtValue = String.format( format, value );
                setText( fmtValue );
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            else
                setText( "" );
        }
    }
}
