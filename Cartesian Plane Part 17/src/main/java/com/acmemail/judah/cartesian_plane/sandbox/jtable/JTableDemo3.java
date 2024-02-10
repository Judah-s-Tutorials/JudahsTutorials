package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.ComponentException;
import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

/**
 * This application demonstrates how to 
 * dynamically add a row to a table 
 * after the table has been created.
 * 
 * @author Jack Straub
 */
public class JTableDemo3
{
    private static final String prompt  = 
        "Enter name, abbreviation and population, separated by commas.";
    private final String[]      headers = 
    { "State", "Abbrev", "Population" };
    private final Object[][]    data    =
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
        SwingUtilities.invokeLater( JTableDemo3::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private JTableDemo3()
    {
        JFrame      frame       = new JFrame( "JTable Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        addSelectColumn( table );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     print       = new JButton( "Print" );
        JButton     add         = new JButton( "Add" );
        JButton     insert      = new JButton( "Insert" );
        JButton     exit        = new JButton( "Exit" );
        print.addActionListener( e -> printAction( table ) );
        add.addActionListener( e -> addAction( table ) );
        insert.addActionListener( e -> insertAction( table ) );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( add );
        buttonPanel.add( insert );
        buttonPanel.add( print );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }

    private void addAction(JTable table)
    {
        insertRow( model.getRowCount() );
    }
    
    private void insertAction( JTable table )
    {
        int position    =
            IntStream.range( 0, model.getRowCount() )
                .peek( System.out::println )
                .peek( r -> System.out.println( model.getValueAt( r, 3 ) ))
                .filter( r -> (Boolean)( model.getValueAt( r, 3 ) ) )
                .findFirst().orElse( 0 );
        insertRow( position );
    }
    
    private void insertRow( int position )
    {
        Object[]    row = getNewRow();
        if ( row != null )
            model.insertRow( position, row );
    }
    
    private Object[] getNewRow()
    {
        String      input   = JOptionPane.showInputDialog( prompt );
        Object[]    row     = null;
        if ( input != null )
            row = parseInput( input );
        return row;
    }
    
    private Object[] parseInput( String input )
    {
        Object[]        row     = null;
        StringTokenizer tizer   = new StringTokenizer( input, "," );
        try
        {
            if ( tizer.countTokens() != 3 )
            {
                String  msg = "Incorrect number of fields entered";
                throw new ComponentException( msg );
            }
            String  name    = tizer.nextToken().trim();
            String  abbr    = tizer.nextToken().trim();
            String  sPop    = tizer.nextToken().trim();
            Integer iPop    = Integer.valueOf( sPop );
            row = new Object[]{ name, abbr, iPop, false };
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
     * Adds a fourth column to a given JTable's model.
     * The header of the column is select,
     * and the value of all cells is (Boolean)false.
     * 
     * @param table the given JTable
     */
    private void addSelectColumn( JTable table )
    {
        int         rowCount    = model.getRowCount();
        Object[]    colData     = new Object[rowCount];
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
