package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * Second part of an example showing how to designate a column
 * as a specific data type.
 * In this part,
 * column 2 is designated type integer.
 * It is right-justfied, 
 * and attempting to give it a non-integer value
 * results in an error.
 * 
 * @author Jack Straub
 * 
 * @see TableModelDemo2A
 * @see TableModelDemo2C
 */
public class TableModelDemo2B
{    
    /** Activity dialog for displaying feedback. */
    private static ActivityLog  log;
    /** Table model for demo. */
    private static TableModel   model;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        String[]    headers = { "State", "Capital", "Population" };
        Object[][]  data    = 
            State.getDataSet( "state", "capital", "population" );
        model = new LocalTableModel( data, headers );
        SwingUtilities.invokeLater( () -> makeGUI( model ) );
    }
    
    /**
     * Creates and shows the GUI,
     * using a JTable that incorporates the given table model.
     * 
     * @param model the given data model
     */
    private static void makeGUI( TableModel model )
    {
        JFrame      frame       = new JFrame( "Table Model Demo 2B" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JTable      table       = new JTable( model );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     print       = new JButton( "Print" );
        JButton     exit        = new JButton( "Exit" );
        print.addActionListener( TableModelDemo2B::printAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( print );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
        
        log = new ActivityLog();
        Dimension   frameSize   = frame.getPreferredSize();
        int         logXco      = 200 + frameSize.width + 10;
        log.setLocation( logXco, 200 );
    }
    
    /**
     * Logs the first five rows of data from the JTable.
     * Activated when the Print button in the GUI is pressed.
     * 
     * @param evt   object accompanying an action event; not used
     */
    private static void printAction( ActionEvent evt )
    {
        StringBuilder   bldr    = new StringBuilder();
        IntStream.range( 0, 5 )
            .peek( i -> bldr.setLength( 0 ) )
            .peek( i -> bldr.append( i ).append( ": " ) )
            .peek( i -> bldr.append( model.getValueAt( i, 2 ) ) )
            .forEach( i -> log.append( bldr.toString() ) );
        log.append( "*************" );
    }

    /**
     * Model to express column 2 as integer data.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private static class LocalTableModel extends DefaultTableModel
    {
        /* Constructor.
         * 
         * @param data      data for table model
         * @param headers   headers for table model
         */
        public LocalTableModel( Object[][] data, Object[] headers )
        {
            super( data, headers );
        }
        
        /**
         * Force column 2 to be type integer.
         * All other columns default.
         */
        @Override
        public Class<?> getColumnClass( int col ) 
        {
            Class<?>    clazz   = 
                col == 2 ? Integer.class : super.getColumnClass( col );
            return clazz;
        }
    }}
