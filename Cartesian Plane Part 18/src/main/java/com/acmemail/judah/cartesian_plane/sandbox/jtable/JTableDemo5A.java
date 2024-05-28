package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.acmemail.judah.cartesian_plane.components.NameValidator;

/**
 * This application demonstrates how to 
 * perform validation on a JTable cell.
 * Validation logic is applied
 * to the first column of the table,
 * which must contain a valid identifier.
 * 
 * @author Jack Straub
 * 
 * @see JTableDemo5B
 */
public class JTableDemo5A
{
    private final String[]      headers = { "Name", "Value" };
    private final Object[][]    data    =
    {
        { "x", 25 },
        { "y", 30 },
        { "base", 22.5 },
        { "alamo", -5.5 },
        { "socrates", .325 },
    };
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( JTableDemo5A::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private JTableDemo5A()
    {
        DefaultTableModel   model   = new LocalTableModel( data, headers );
        JTable              table   = new JTable( model );
        TableColumn         column  = table.getColumnModel().getColumn( 0 );
        column.setCellEditor( new NameEditor() );
        
        JFrame      frame       = new JFrame( "JTable Validation Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     exit        = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
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
    
    /**
     * This class encapsulates a cell editor
     * that performs validation 
     * for identifiers.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private static class NameEditor extends DefaultCellEditor
    {
        /**
         * Constructor.
         * Establishes the component
         * for editing an identifier
         * in a JTable.
         */
        public NameEditor()
        {
            super( new JFormattedTextField() );
        }
        
        /**
         * Determines whether editing in a table cell should cease.
         * Editing can cease if the component used for editing
         * contains a valid value.
         * If said component contains a valid value
         * true is returned.
         * 
         * @return true if the editing component contains a valid value.
         */
        @Override
        public boolean stopCellEditing()
        {
            Object  oValue  = getCellEditorValue();
            boolean status  = false;
            if ( NameValidator.isIdentifier( (String)oValue ) )
                status = super.stopCellEditing();
            return status;
        }
    }
}
