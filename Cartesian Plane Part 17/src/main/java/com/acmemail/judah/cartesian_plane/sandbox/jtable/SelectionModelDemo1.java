package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import static javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
import static javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

/**
 * This application demonstrates how to 
 * dynamically delete rows from a table 
 * after the table has been created and deployed.
 * 
 * @author Jack Straub
 */
public class SelectionModelDemo1
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
        SwingUtilities.invokeLater( SelectionModelDemo1::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private SelectionModelDemo1()
    {
        JFrame      frame       = new JFrame( "JTable Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JScrollPane scrollPane  = new JScrollPane( table );
        table.setColumnSelectionAllowed( true );
        table.setRowSelectionAllowed( true );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        contentPane.add( getOptionsPanel(), BorderLayout.EAST );
        
        JPanel      buttonPanel = new JPanel();
        JButton     print       = new JButton( "Print" );
        JButton     exit        = new JButton( "Exit" );
        print.addActionListener( this::printAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( print );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getOptionsPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        JCheckBox   rowSel      = new JCheckBox( "Allow Row Selection" );
        JCheckBox   colSel      = new JCheckBox( "Allow Col Selection" );
        rowSel.addActionListener( this::rowSelAction );
        colSel.addActionListener( this::colSelAction );
        
        panel.add( rowSel );
        panel.add( colSel );
        panel.add( getSelectionModePanel() );
        
        return panel;
    }
    
    private JPanel getSelectionModePanel()
    {
        class Pair
        {
            String label; int mode;
            Pair( String label, int mode )
            { this.label = label; this.mode = mode; }
        }
        Pair[] allPairs = 
        {
            new Pair( "Single", SINGLE_SELECTION ),
            new Pair( "Single Interval", SINGLE_INTERVAL_SELECTION ),
            new Pair( "Multiple Interval", MULTIPLE_INTERVAL_SELECTION ),
        };
        
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        Border      lineBorder  = 
            BorderFactory.createLineBorder( Color.BLACK );
        Border      border      =
            BorderFactory.createTitledBorder( 
                lineBorder, 
                "Selection Mode"
            );
        panel.setBorder( border );
        
        ButtonGroup     group   = new ButtonGroup();
        Stream.of( allPairs )
            .map( p -> {
                JRadioButton    button  = new JRadioButton( p.label );
                button.addActionListener( e -> modeAction( p.mode ) );
                return button;
            })
            .peek( panel::add )
            .forEach( group::add );
        return panel;
    }
    
    private void rowSelAction( ActionEvent evt )
    {
        
    }
    
    private void colSelAction( ActionEvent evt )
    {
        
    }
    
    private void modeAction( int mode )
    {
        
    }
    
    /**
     * Traverses a given table,
     * printing the all selected rows.
     * 
     * @param evt   object that accompanies an action event; not used
     */
    private void printAction( ActionEvent evt )
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
            else
                clazz = super.getColumnClass( col );
            return clazz;
        }
    }
}
