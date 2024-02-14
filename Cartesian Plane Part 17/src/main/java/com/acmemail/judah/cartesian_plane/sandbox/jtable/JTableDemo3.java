package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.StringTokenizer;

import javax.swing.DefaultCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.JTextComponent;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * This application demonstrates how to 
 * dynamically add a row to a table 
 * after the table has been created and deployed.
 * 
 * @author Jack Straub
 */
public class JTableDemo3
{
    private static final String prompt  =
        "Enter an identifier and value "
        + "separated by commas and/or spaces";
    private final String[]      headers = { "Name", "Value" };
    private final Object[][]    data    =
    {
        { "x", 25 },
        { "y", 30 },
        { "base", 22 }
    };
    
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
        JScrollPane scrollPane  = new JScrollPane( table );
//        table.getColumnModel().getColumn(0).setCellEditor( new NameEditor() );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     print       = new JButton( "Print" );
        JButton     add         = new JButton( "Add" );
        JButton     exit        = new JButton( "Exit" );
        print.addActionListener( this::printAction );
        add.addActionListener( this::addAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( add );
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
     * Asks the operator to enter two values
     * for the columns displayed in the GUI's JTable:
     * <pre>    name value</pre>
     * The fields must be separated by commas at least
     * one comma or space.
     * The two fields are parsed
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
     * into "name" and "value".
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
        StringTokenizer tizer   = new StringTokenizer( input, ", " );
        try
        {
            if ( tizer.countTokens() != 2 )
            {
                String  msg = "Incorrect number of fields entered";
                throw new NumberFormatException( msg );
            }
            String  name    = tizer.nextToken().trim();
            String  sPop    = tizer.nextToken().trim();
            Integer iPop    = Integer.valueOf( sPop );
            row = new Object[]{ name, iPop };
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
     * printing the all rows.
     * 
     * @param evt   object that accompanies an action event; not used
     */
    private void printAction( ActionEvent evt )
    {
        int     rowCount    = table.getRowCount();
        for ( int row = 0 ; row < rowCount ; ++row )
        {
            Object  name    = table.getValueAt( row, 0 );
            Object  value   = table.getValueAt( row, 1 );
            System.out.println( name + " = " + value );
        }
    }
    
    /**
     * Subclass of DefaultTableModel
     * that is used to establish the type
     * of column 1 in the GUI's JTable.
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
            if ( col == 1 )
                clazz = Double.class;
            else
                clazz = super.getColumnClass( col );
            return clazz;
        }
    }
    
    @SuppressWarnings("serial")
    private static class NameEditor extends DefaultCellEditor
    {
        private final JFormattedTextField textField;
        public NameEditor() throws ParseException
        {
            super( new JFormattedTextField() );// new IdentFormatter()) );
            textField = (JFormattedTextField)getComponent();
            textField.setInputVerifier( new NameVerifier() );
        }
    }
    @SuppressWarnings("serial")
    private static class IdentFormatter extends DefaultFormatter
    {
        @Override
        public String stringToValue( String input )
            throws ParseException
        {
            boolean status  = input == null ?
                true : NameValidator.isIdentifier( input.toString() );
            if ( !status )
            {
                String  msg = 
                    "\"" + input + "\" is not a valid identifier";
                throw new ParseException( msg, 0 );
            }
            
            return input;
        }
    }    
    private static class NameVerifier extends InputVerifier
    {
        @Override
        public boolean verify( JComponent comp )
        {
            if ( !(comp instanceof JTextComponent) )
                throw new ComponentException( "Invalid component" );
            JTextComponent  jtComp  = (JTextComponent)comp;
            String          input   = jtComp.getText();
            boolean         status  = NameValidator.isIdentifier( input );
            
            if ( !status )
                jtComp.setForeground( Color.RED );
            else
                jtComp.setForeground( Color.BLACK );
            
            return status;
        }
    }
}
