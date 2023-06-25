package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * Encapsulates the GUI
 * used to display the execution log
 * for this demonstration.
 * Each log entry
 * is formatted as a row
 * in a JTable.
 * 
 * @author Jack Straub
 */
public class LogDisplay
{
    /** Used to format the time of day in millisecond precision. */
    private static final DateTimeFormatter  timeFMT     = 
        DateTimeFormatter.ofPattern( "HH:mm:ss.SSS" );
    /** 
     * List of column names for displaying the fields of an
     * execution log entry.
     */
    private static final String[]           columns     =
    {
        "Time", "Class", "Method", "Param", "Thread"
    };
    
    /** Model used in the encapsulated JTable. */
    private final DataModel     tableModel  = new DataModel( columns, 0 );
    /** The encapsulated JTable. */
    private final JTable        jTable      = new JTable( tableModel );
    /** The frame that contains the GUI. */
    private final JFrame        frame       = new JFrame( "Thread Log" );
    /** Used to scroll the encapsulated JTable. */
    private final JScrollPane   scroller    = new JScrollPane( jTable );
    /** 
     * Controls the initial location at which to display the 
     * encapsulated frame.
     */
    private final Point         startPos    = new Point( 100, 100 );
    
    /**
     * Constructor.
     * Connects this GUI to the execution log
     * via an action listener.
     */
    public LogDisplay()
    {
        ExecLog.addActionListener( this::actionPerformed );
    }
    
    /**
     * Constructs the encapsulated GUI.
     */
    public void build()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        
        contentPane.add( scroller, BorderLayout.CENTER );
        frame.setContentPane( contentPane );
        
        frame.setLocation( startPos );
        frame.pack();
        frame.setVisible( true );       
    }
    
    /**
     * Gets the current position
     * of the encapsulated frame.
     * 
     * @return  the position of the encapsulated frame
     */
    public Point getPos()
    {
        return frame.getLocation();
    }
    
    /**
     * Processes notifications 
     * of additions
     * to the execution log.
     * 
     * @param evt   object encapsulating the associated event
     * 
     * @throws  RuntimeException 
     *          if the source of the event
     *          is not an ExecLog object. 
     */
    public void actionPerformed( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( !(source instanceof ExecLog ) )
        {
            String  msg = 
                "Invalid source; expected: ExecLog, actual: "
                + source.getClass().getName();
            throw new RuntimeException( msg );
        }
        appendRow( (ExecLog)source );
    }
    
    /**
     * Adds a row to the display,
     * derived from a given log record.
     * 
     * @param data  the given log record
     */
    private void appendRow( ExecLog data )
    {
        String      timeStr = timeFMT.format( data.getTime() );
        String[]    row     = new String[]
        {
            timeStr,
            data.getClassName(),
            data.getMethodName(),
            data.getParamName(),
            data.getThreadName()
        };
        tableModel.insertRow( tableModel.getRowCount(), row );
        
        JScrollBar  vertBar = scroller.getVerticalScrollBar();
        SwingUtilities.invokeLater( () -> frame.repaint() );
        SwingUtilities.invokeLater( () -> 
            vertBar.setValue( vertBar.getMaximum() )
        );
    }
    
    /**
     * Encapsulates the TableModel
     * used to display 
     * the encapsulated JTable.
     * Its only purpose
     * is to declare every field
     * to be non-editable.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private class DataModel extends DefaultTableModel
    {
        /**
         * Constructor.
         * Establishes the column headings
         * and the current number rows
         * for the encapsulated JTable.
         * 
         * @param columns   the column headings for this table
         * @param numRows   the current number of rows in this table
         */
        public DataModel( Object[] columns, int numRows )
        {
            super( columns, numRows );
        }
        
        @Override
        public boolean isCellEditable(int row, int col)
        {
            // No cell in this GUI is editable.
            return false;
        }
    }
}
