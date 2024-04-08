package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * This application demonstrates 
 * how to write a TableModelListener
 * for a JTable-based facility.
 * The user can modify existing rows of the JTable,
 * and insert and delete rows.
 * Each change to the JTable's data model
 * is logged.
 * 
 * @author Jack Straub
 */
public class TableModelListenerDemo2
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
    
    /** Activity dialog for displaying feedback. */
    private final ActivityLog       log     = new ActivityLog();

    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( TableModelListenerDemo2::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private TableModelListenerDemo2()
    {
        table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        model.addTableModelListener( this::tableChanged );
        
        JFrame      frame       = new JFrame( "JTable Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     insert      = new JButton( "Insert" );
        JButton     delete      = new JButton( "Delete" );
        JButton     exit        = new JButton( "Exit" );
        insert.addActionListener( this::insertAction );
        delete.addActionListener( this::deleteAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( insert );
        buttonPanel.add( delete );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        
        Dimension   frameSize   = frame.getPreferredSize();
        int         logXco      = 100 + frameSize.width + 10;
        log.setLocation( logXco, 100 );
        
        Dimension   logSize     = log.getPreferredSize();
        logSize.width = (int)(logSize.width * 1.25);
        log.setPreferredSize( logSize );
        
        frame.setVisible( true );
    }
    
    /**
     * Listens for and logs
     * all changes to the JTable's data model.
     * 
     * @param evt   event object describing changes to the data model
     */
    private void tableChanged( TableModelEvent evt )
    {
        Object  deleted     = null;
        int     type        = evt.getType();
        String  strType     = "";
        int     firstRow    = evt.getFirstRow();
        int     lastRow     = evt.getLastRow();
        int     col         = evt.getColumn();
        Object  val         = "[n/a]";
        if ( firstRow >= 0 && col >= 0 )
            val = model.getValueAt( firstRow, col );
        switch ( type )
        {
        case TableModelEvent.DELETE:
            strType = "DELETE";
            deleted = model.getValueAt( firstRow, 0 );
            break;
        case TableModelEvent.INSERT:
            strType = "INSERT";
            break;
        case TableModelEvent.UPDATE:
            strType = "UPDATE";
            break;
        default:
            strType = "ERROR";
        }

        StringBuilder   bldr    = new StringBuilder();
        bldr.append( strType ).append( ": " )
            .append("first row=").append( firstRow ).append( "," )
            .append("last row=").append( lastRow ).append( "," )
            .append("col=").append( col ).append( "," )
            .append( "value=").append( val );
        log.append( bldr.toString() );
    }

    /**
     * Begins the "insert" process.
     * The goal is to insert a new row
     * before the selected item 
     * in the GUI's JTable.
     * If no item is selected
     * the new row is added at the end.
     * 
     * @param evt   object that accompanies an action event; not used
     */
    private void insertAction( ActionEvent evt )
    {
        int position    = table.getSelectedRow();
        if ( position < 0 )
            position = table.getRowCount();
        insertRow( position );
    }
    
    private void deleteAction( ActionEvent evt )
    {
        int position    = table.getSelectedRow();
        if ( position >= 0 )
        {
            model.removeRow( position );
        }
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
