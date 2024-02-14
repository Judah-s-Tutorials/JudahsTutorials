package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * This application is part two
 * of a demonstration of how to use column types.
 * It uses a static nested class
 * that extends DefaultTableModel,
 * and overrides getColumnClass.
 * The method returns the default type
 * for all columns except column 2,
 * for which it returns Integer.class.
 * This causes the data in column 2 cell
 * to be displayed and edited an integer.
 * <p>
 * The bottom of the application frame shows a Test button.
 * When pushed,
 * all the data in column 2
 * will be printed to stdout.
 * 
 * @author Jack Straub
 * 
 * @see TableModelDemo2A
 */
public class TableModelDemo2B
{
    /** Header array for the GUI's JTable. */
    private final String[]      headers = 
        { "State", "Capital", "Population" };
    /** Data array for the GUI's JTable. */
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
        SwingUtilities.invokeLater( () -> new TableModelDemo2B() );
    }
    
    /**
     * Constructor.
     * Configures and displays the application frame.
     * Must be executed on the EDT.
     */
    public TableModelDemo2B()
    {
        JFrame      frame       = new JFrame( "Tabel Model Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        LocalTableModel     model       = 
            new LocalTableModel( data, headers );
        JTable              table       = new JTable( model );
        JPanel              contentPane = new JPanel( new BorderLayout() );
        JScrollPane         scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     exit        = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        JButton test            = new JButton( "Test" );
        test.addActionListener( e -> testAction( model) );
        buttonPanel.add( test );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
                
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Prints out the value of column three
     * for the first 5 rows of the given data model.
     * 
     * @param model the given data model
     */
    private void testAction( DefaultTableModel model )
    {
        IntStream.range( 0, 5 ).forEach( i -> 
            System.out.println( model.getValueAt( i, 2) ) );
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
                col == 2 ? Integer.class : super.getColumnClass( col );
            return clazz;
        }
    }
}
