package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * This is a revision of JTableDemo4.
 * Allows insertion and deletion of rows.
 * JTableDemo4 based its logic on 
 * which rows contained a selected check box.
 * This application eliminates the column of check boxes,
 * and instead uses logic based on which rows are selected
 * as determined by a JTable's ListSelectionModel.
 * 
 * @author Jack Straub
 */
public class SelectionModelDemo2X
{
    private static final String prompt          = 
        "Enter name, abbreviation and population, separated by commas.";
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
        SwingUtilities.invokeLater( SelectionModelDemo2X::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private SelectionModelDemo2X()
    {
        TableColumnModel    colModel    = table.getColumnModel();
        TableColumn         column2     = colModel.getColumn( 2 );
        column2.setCellRenderer( new ValueRenderer() );

        JFrame      frame       = new JFrame( "JTable Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     print       = new JButton( "Print" );
        JButton     add         = new JButton( "Add" );
        JButton     insert      = new JButton( "Insert" );
        JButton     delete      = new JButton( "Delete" );
        JButton     exit        = new JButton( "Exit" );
        print.addActionListener( this::printAction );
        add.addActionListener( this::addAction );
        insert.addActionListener( this::insertAction );
        delete.addActionListener( this::deleteAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( add );
        buttonPanel.add( insert );
        buttonPanel.add( delete );
        buttonPanel.add( print );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }

    /**
     * Begins the "add" process.
     * A new row will be added to the bottom
     * of the GUI's JTable.
     * 
     * @param evt   object that accompanies an action event; not used
     */
    private void addAction( ActionEvent evt )
    {
        insertRow( model.getRowCount() );
    }
    
    /**
     * Begins the "insert" process.
     * The goal is to insert a new row
     * before the selected item 
     * in the GUI's JTable.
     * If multiple items are selected
     * the first one found will be used;
     * there is no guarantee as to which selected item
     * is the first found.
     * If no item is selected
     * the new row is added at position 0.
     * 
     * @param evt   object that accompanies an action event; not used
     */
    private void insertAction( ActionEvent evt )
    {
        int position    = table.getSelectedRow();
        if ( position < 0 )
            position = 0;
        insertRow( position );
    }
    
    @SuppressWarnings("rawtypes")
    private void deleteAction( ActionEvent evt )
    {
        int[]               selected    = table.getSelectedRows();
        int                 currInx     = 0;
        Vector<Vector>      data        = model.getDataVector();
        Iterator<Vector>    iter        = data.iterator();
        while ( iter.hasNext() )
        {
            iter.next();
            if ( Arrays.binarySearch( selected, currInx++ ) >= 0 )
                iter.remove();
        }
        List<Object>    cHeaders    = Arrays.asList( headers );
        Vector<Object>  vHeaders    = new Vector<>( cHeaders );
        model.setDataVector( data, vHeaders );
    }
    
    /**
     * Inserts a row into the GUI's JTable
     * at the given position.
     * 
     * @param position  the given position
     */
    private void insertRow( int position )
    {
        Object[]    row = getNewRow();
        if ( row != null )
            model.insertRow( position, row );
    }
    
    /**
     * Asks the operator to enter three values
     * for the columns displayed in the GUI's JTable:
     * <pre>    name, abbreviation, population</pre>
     * The three fields must be separated by commas.
     * The three fields are parsed
     * and collected into an object array,
     * and the array is returned.
     * If the operator cancels the operation
     * null is returned.
     * If a data entry error is detected
     * null is returned.

     * @return  
     *      a row suitable to be added to the GUI's JTable,
     *      or null if the operation is aborted
     *      
     * @see #parseInput(String)
     */
    private Object[] getNewRow()
    {
        String      input   = JOptionPane.showInputDialog( prompt );
        Object[]    row     = null;
        if ( input != null )
            row = parseInput( input );
        return row;
    }
    
    /**
     * Parse a given input string
     * into "state", "abbreviation" and "population".
     * The data are returned in an object array.
     * If an input error is detected,
     * an error dialog is posted
     * and null is returned.
     * 
     * @param input the given input string
     * 
     * @return  
     *      an object array containing the parsed data,
     *      or null if an error is detected
     *      
     * @see #getNewRow()
     */
    private Object[] parseInput( String input )
    {
        Object[]        row     = null;
        StringTokenizer tizer   = new StringTokenizer( input, "," );
        try
        {
            if ( tizer.countTokens() != 3 )
            {
                String  msg = "Incorrect number of fields entered";
                throw new NumberFormatException( msg );
            }
            String  name    = tizer.nextToken().trim();
            String  abbr    = tizer.nextToken().trim();
            String  sPop    = tizer.nextToken().trim();
            Integer iPop    = Integer.valueOf( sPop );
            row = new Object[]{ name, abbr, iPop };
        }
        catch ( NumberFormatException exc )
        {
            JOptionPane.showMessageDialog(
                null, 
                exc.getMessage(), 
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        return row;
    }
    
    /**
     * Traverses a given table,
     * printing the all selected rows.
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
    private class ValueRenderer extends DefaultTableCellRenderer
    {
        /**
         * Converts an Object to a String
         * for display in a JTable cell.
         * The object is assumed to be type double.
         * It is formatted with a given number of decimals,
         * and comma separators.
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
