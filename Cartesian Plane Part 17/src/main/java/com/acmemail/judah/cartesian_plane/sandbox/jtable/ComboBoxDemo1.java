package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * This is an application that shows how
 * to change the cell editor for a column
 * to a JComboBox.
 * Specifically, the editor for column 1
 * is set to a combo box.
 * Note that you can't see the combo box
 * until you edit a cell 
 * (click in it, for example).
 * 
 * @author Jack Straub
 */
public class ComboBoxDemo1
{
    /** Array of headers (column names). */
    private final String[]      headers = 
    { "Name", "Teacher", "Present" };
    /** Data array. */
    private final Object[][]    data    =
    {
        { "Alex", "Archimedes, Burton", true },
        { "Ashley", "Lamarr, Hedy", true },
        { "Jesse", "Tesla, Nikola", true },
        { "Joyce", "Gilbreth, Lillian", false },
        { "Leslie", "Watt, James", true },
        { "Riley", "Roebling, Emily", true },
        { "Robin", "Jemison, Mae", true },
        { "Ryan", "Gutenberg, Johannes", false },
    };
    /** Array for initialization of JComboBox */
    private final String[] teachers =
    {
        "Archimedes, Burton",
        "Gilbreth, Lillian",
        "Gutenberg, Johannes",
        "Jemison, Mae",
        "Lamarr, Hedy",
        "Roebling, Emily",
        "Tesla, Nikola",
        "Watt, James",
    };
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( ComboBoxDemo1::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private ComboBoxDemo1()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        TableModel  model       = new LocalTableModel( data, headers );
        JTable      table       = new JTable( model );
        installComboBox( table );
        
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
     * Add the JComboxEditor to column #1
     * of the GUI's JTable.
     * 
     * @param table the GUI's JTable
     */
    private void installComboBox( JTable table )
    {
        JComboBox<String>   comboBox    = new JComboBox<>( teachers );
        TableCellEditor     comboEditor = 
            new DefaultCellEditor( comboBox );
        TableColumnModel    columnModel = table.getColumnModel();
        TableColumn         teacherCol  = columnModel.getColumn( 1 );
        teacherCol.setCellEditor( comboEditor );
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
                col == 2 ? Boolean.class : super.getColumnClass( col );
            return clazz;
        }
    }
}
