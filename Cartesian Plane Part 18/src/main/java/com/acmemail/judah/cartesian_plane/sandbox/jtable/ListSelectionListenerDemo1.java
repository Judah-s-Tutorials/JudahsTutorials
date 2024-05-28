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
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * This application demonstrates 
 * how to write a ListSelectionListener
 * for a JTable-based facility.
 * Each change to the JTable's selection state is logged.
 * Also, the the application's Delete button
 * is enabled or disabled
 * depending on whether or not a row has been selected
 * (there's nothing to delete
 * if no row is selected).
 * 
 * @author Jack Straub
 */
public class ListSelectionListenerDemo1
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
     *  "Delete-row" button; declared as an instance variable
     *  for the convenience of the 
     *  {@linkplain #valueChanged(ListSelectionEvent)} method.
     */
    private final JButton           delete  = new JButton( "Delete" );
    
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
        SwingUtilities.invokeLater( ListSelectionListenerDemo1::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private ListSelectionListenerDemo1()
    {
        table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        ListSelectionModel  selectionModel  = table.getSelectionModel();
        selectionModel.addListSelectionListener( this::valueChanged );
        
        JFrame      frame       = new JFrame( "JTable Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     insert      = new JButton( "Insert" );
        JButton     exit        = new JButton( "Exit" );
        JButton     clear       = new JButton( "Clear Selection" );
        insert.addActionListener( this::insertAction );
        delete.addActionListener( this::deleteAction );
        delete.setEnabled( false );
        clear.addActionListener( e -> table.clearSelection() );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( insert );
        buttonPanel.add( delete );
        buttonPanel.add( clear );
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
     * all changes to the JTable's selection state.
     * 
     * @param evt   event object describing changes to the selection state
     */
    private void valueChanged( ListSelectionEvent evt )
    {
        int     firstIndex  = evt.getFirstIndex();
        int     lastIndex   = evt.getLastIndex();
        boolean isAdjusting = evt.getValueIsAdjusting();
        
        StringBuilder   bldr    = new StringBuilder()
            .append( "Selection event: " )
            .append( "First index=" ).append( firstIndex ).append( "," )
            .append( "Last index=" ).append( lastIndex ).append( "," )
            .append( "Adjusting=" ).append( isAdjusting );
        log.append( bldr.toString() );
        
        bldr.setLength( 0 );
        bldr.append( "<span style=background:yellow;>" )
            .append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" )
            .append( "table.getSelectedRowCount(): ")
            .append( table.getSelectedRowCount() )
            .append( "</span>" );
        log.append( bldr.toString() );
        
        if ( !isAdjusting )
        {
            int     selectedRow     = table.getSelectedRow();
            boolean hasSelection    = selectedRow >= 0;
            delete.setEnabled( hasSelection );
            log.append( "*********************" );
        }
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
