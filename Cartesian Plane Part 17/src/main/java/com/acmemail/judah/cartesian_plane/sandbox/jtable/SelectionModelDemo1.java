package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import static javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
import static javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import com.acmemail.judah.cartesian_plane.components.PButtonGroup;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

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
    
    private final ActivityLog       log;

    
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
        log = new ActivityLog( frame );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JScrollPane scrollPane  = new JScrollPane( table );
        table.setColumnSelectionAllowed( false );
        table.setRowSelectionAllowed( true );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        contentPane.add( getOptionsPanel(), BorderLayout.EAST );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        Dimension   frameSize   = frame.getPreferredSize();
        int         logWidth    = (int)(frameSize.width * .6) ;
        int         logHeight   = frameSize.height;
        Dimension   logSize     = new Dimension( logWidth, logHeight );
        log.setPreferredSize( logSize );
        log.setLocation( 100 + frame.getPreferredSize().width + 10, 100 );
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
        rowSel.setSelected( table.getRowSelectionAllowed() );
        colSel.setSelected( table.getColumnSelectionAllowed() );
        
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
        
        PButtonGroup<Integer>   group   = new PButtonGroup<>();
        Stream.of( allPairs )
            .map( p -> {
                PRadioButton<Integer>   button  = 
                    new PRadioButton<>( p.mode, p.label );
                button.addActionListener( this::modeAction );
                return button;
            })
            .peek( panel::add )
            .forEach( group::add );
        
        group.selectButton( allPairs[0].mode );
        return panel;
    }
    
    private JPanel getButtonPanel()
    {
        JPanel      panel       = new JPanel();
        JButton     showSelRows = new JButton( "Show Selected Rows" );
        JButton     showSelCols = new JButton( "Show Selected Cols" );
        JButton     exit    = new JButton( "Exit" );
        showSelRows.addActionListener( this::showSelRowsAction);
        showSelCols.addActionListener( this::showSelColsAction);
        exit.addActionListener( e -> System.exit( 0 ) );
        panel.add( showSelRows );
        panel.add( showSelCols );
        panel.add( exit );        
        
        return panel;
    }
    
    private void rowSelAction( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof JCheckBox )
        {
            JCheckBox   rowSelection    = (JCheckBox)source;
            table.clearSelection();
            table.setRowSelectionAllowed( rowSelection.isSelected() );
        }
    }
    
    private void colSelAction( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof JCheckBox )
        {
            JCheckBox   colSelection    = (JCheckBox)source;
            table.clearSelection();
            table.setColumnSelectionAllowed( colSelection.isSelected() );
        }
    }
    
    private void modeAction( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof PRadioButton<?> )
        {
            PRadioButton<?> radioButton = (PRadioButton<?>)source;
            Object          prop        = radioButton.get();
            if ( prop instanceof Integer )
                table.setSelectionMode( (Integer)prop );
        }
    }
    
    private void showSelRowsAction( ActionEvent evt )
    {
        String  line;
        
        line = "<strong>Row selection allowed: ";
        line += table.getRowSelectionAllowed() + "</strong>";
        log.append( line );
        
        line = "<strong>Selected row count: ";
        line += table.getSelectedRowCount() + "</strong>";
        log.append( line );
        
        line = "<strong>Selected row: ";
        line += table.getSelectedRow() + "</strong>";
        log.append( line );
        
        line = "<strong>Selection mode: ";
        switch ( table.getSelectionModel().getSelectionMode() )
        {
        case SINGLE_SELECTION:
            line += "Single Selection";
            break;
        case SINGLE_INTERVAL_SELECTION:
            line += "Single Interval Selection";
            break;
        case MULTIPLE_INTERVAL_SELECTION:
            line += "Multiple Interval Selection";
            break;
        }
        line += "</strong>";
        log.append( line );
        
        line = "<strong>Selected rows:</strong>";
        log.append( line );

        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "<span style='color: #ff0000;'>" );
        int[]   rows    = table.getSelectedRows();
        Arrays.stream( rows )
            .forEach( r -> 
                bldr.append( "&nbsp;&nbsp;&nbsp;&nbsp;")
                    .append( r ).append( ": " )
                    .append( table.getValueAt( r, 0 ) ).append( ", " )
                    .append( table.getValueAt( r, 1 ) ).append( ", " )
                    .append( table.getValueAt( r, 2 ) ).append( "<br>" )
            );
        bldr.append( "</span>" );
        log.append( bldr.toString() );
    }
    
    private void showSelColsAction( ActionEvent evt )
    {
        String  line;
        
        line = "<strong>Column selection allowed: ";
        line += table.getColumnSelectionAllowed() + "</strong>";
        log.append( line );
        
        line = "<strong>Selected column count: ";
        line += table.getSelectedColumnCount() + "</strong>";
        log.append( line );
        
        line = "<strong>Selected column: ";
        line += table.getSelectedColumn() + "</strong>";
        log.append( line );
        
        line = "<strong>Selection mode: ";
        switch ( table.getSelectionModel().getSelectionMode() )
        {
        case SINGLE_SELECTION:
            line += "Single Selection";
            break;
        case SINGLE_INTERVAL_SELECTION:
            line += "Single Interval Selection";
            break;
        case MULTIPLE_INTERVAL_SELECTION:
            line += "Multiple Interval Selection";
            break;
        }
        line += "</strong>";
        log.append( line );
        
        line = "<strong>Selected columns:</strong>";
        log.append( line );

        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "<span style='color: #ff0000;'>" );
        int[]   cols    = table.getSelectedColumns();
        Arrays.stream( cols )
            .forEach( r -> 
                bldr.append( "&nbsp;&nbsp;&nbsp;&nbsp;")
                    .append( r ).append( ": " )
                    .append( table.getColumnName( r ) )
                    .append( "<br>" )
            );
        bldr.append( "</span>" );
        log.append( bldr.toString() );
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
