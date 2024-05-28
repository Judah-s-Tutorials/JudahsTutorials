package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * Demonstrates how to select items in a table
 * via its ListSeletionModel.
 * Run the application and push the "Select" button.
 * in the resulting dialog,
 * enter row indices to select
 * separated by commas.
 * Assuming the indices are valid,
 * those rows in the table will be selected.
 * 
 * @author Jack Straub
 */
public class SelectionModelDemo3
{
    private final Object[]      headers         = 
    { "State", "Abbrev", "Population" };
    private final Object[][]    data            =
    State.getDataSet( "state", "abbreviation", "population" );
    
    /** JTable's data model. */
    private final DefaultTableModel model   = 
        new LocalTableModel( data, headers );
    /** GUI's JTable. */
    private final JTable            table   = new JTable( model );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( SelectionModelDemo3::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private SelectionModelDemo3()
    {
        JFrame      frame       = new JFrame( "JTable Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     print       = new JButton( "Print" );
        JButton     select      = new JButton( "Select" );
        JButton     clear       = new JButton( "Clear Selection" );
        JButton     exit        = new JButton( "Exit" );
        print.addActionListener( this::printAction );
        select.addActionListener( this::selectAction );
        clear.addActionListener( e -> 
            table.getSelectionModel().clearSelection()
        );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( select );
        buttonPanel.add( clear );
        buttonPanel.add( print );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }

    /**
     * Activated when the GUI's Select button is clicked.
     * Obtains a list of indices
     * and selects the corresponding rows
     * in the table.
     * 
     * @param evt   object that accompanies an action event; not used
     */
    private void selectAction( ActionEvent evt )
    {
        ListSelectionModel  selModel    = table.getSelectionModel();
        Stream.of( getSelectionArray() )
            .forEach( n -> selModel.addSelectionInterval( n, n ) );
    }
    
    /**
     * Asks the operator to enter
     * a list of row indices separated by commas
     * and returns an array corresponding
     * to the operator's input.
     * If the operator cancels the operation
     * or enters an invalid integer
     * an empty array is returned.
     *  
     * @return  array of integers selected by the operator
     */
    private Integer[] getSelectionArray()
    {
        String          prompt  = "Enter indices separated by commas.";
        String          input   = JOptionPane.showInputDialog( prompt );
        if ( input == null )
            input = "";
        StringTokenizer tizer   = new StringTokenizer( input, ", " );
        int             last    = table.getRowCount();
        int             count   = tizer.countTokens();
        Integer[]       result;
        try
        {
            result = IntStream.range( 0, count )
                .mapToObj( i -> tizer.nextToken() )
                .map( Integer::parseInt )
                .filter( i -> i < last )
                .toArray( Integer[]::new );
        }
        catch ( NumberFormatException exc )
        {
            JOptionPane.showMessageDialog( null, "Invalid integer" );
            result = new Integer[0];
        }
        return result;
    }
    
    /**
     * Traverses a given table,
     * printing all the selected rows.
     * 
     * @param evt   object that accompanies an action event; not used
     */
    private void printAction( ActionEvent evt )
    {
        int[]   selected    = table.getSelectedRows();
        IntStream.of( selected ).forEach( row -> {
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( table.getValueAt( row, 0 ) ).append( ", " )
                .append( table.getValueAt( row, 1 ) ).append( ", " )
                .append( table.getValueAt( row, 2 ) );
            System.out.println( bldr );
        });
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
