package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * Application to show how to use JTable.getCellRect
 * to locate and click on a cell.
 * The GUI is intentionally configured 
 * so that the width and height of the table
 * will not fit in the scroll pane's
 * viewport view
 * (i.e. there will be horizontal and vertical scroll bars).
 * Use the row- and column-selectors
 * to choose a cell to click on
 * then push the click button.
 * If necessary the table will be scrolled
 * to make the selected cell visible,
 * then it will be clicked.
 * 
 * @author Jack Straub
 * 
 * @see #clickAction(ActionEvent)
 */
public class GetCellRectDemo1
{
    /** Table column headers. */
    private final Object[]      headers         = 
    { "State", "Abbrev", "Population", "Latitude", "Longitude" };
    /** Table data. */
    private final Object[][]    data            =
        State.getDataSet( 
            "state", 
            "abbreviation", 
            "population",
            "latitude",
            "longitude"
        );
    
    /** Table model; used to set column types. */
    private final DefaultTableModel model   = 
        new LocalTableModel( data, headers );
    /** Demo table. */
    private final JTable            table       = new JTable( model );
    /** Scroll pane to contain table. */
    private final JScrollPane       scrollPane  = new JScrollPane( table );
    
    /** Spinner model for row selection */
    private final SpinnerNumberModel    rowModel    =
        new SpinnerNumberModel( 0, 0, data.length - 1, 1 );
    /** Row selector. */
    private final JSpinner              rowEditor   = 
        new JSpinner( rowModel );

    /** Spinner model for column selection */
    private final SpinnerNumberModel    colModel    =
        new SpinnerNumberModel( 0, 0, headers.length - 1, 1 );
    /** Column selector. */
    private final JSpinner              colEditor   = 
        new JSpinner( colModel );
    
    /** Robot to move and click the mouse. */
    private final Robot             robot = getRobot();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( GetCellRectDemo1::new );
    }
    
    /**
     * Constructor.
     * Configures and displays the GUI.
     */
    private GetCellRectDemo1()
    {
        JFrame      frame       = new JFrame( "GetCellRectDemo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        contentPane.add( getBottomPanel(), BorderLayout.SOUTH );
        
        // Adjust the width of the scroll pane to ensure
        // that you have to scroll horizontally to see
        // the last cell;
        Dimension   dim     = scrollPane.getPreferredSize();
        table.setMinimumSize( dim );
        dim.width /= 2;
        scrollPane.setPreferredSize( dim );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Gets the bottom panel to display in the GUI's content pane.
     * The bottom panel consists of one panel
     * to configure the row- and column-editors
     * and a second panel to configure the control buttons.
     * 
     * @return  the bottom panel to display in the GUI's content pane
     */
    private JPanel getBottomPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.add( getEditorPanel() );
        panel.add( getButtonPanel() );
        return panel;
    }
    
    /**
     * Gets the panel that contains the row- and column-editors.
     * 
     * @return  the panel that contains the row- and column-editors
     */
    private JPanel getEditorPanel()
    {
        JPanel  panel   = new JPanel();
        panel.add( new JLabel( "Row" ) );
        panel.add( rowEditor );
        panel.add( new JLabel( "Column" ) );
        panel.add( colEditor );
        return panel;
    }
    
    /**
     * Gets the panel that contains the control buttons.
     * 
     * @return  the panel that contains the control buttons
     */
    private JPanel getButtonPanel()
    {
        JPanel      buttonPanel = new JPanel();
        JButton     click       = new JButton( "Click" );
        JButton     exit        = new JButton( "Exit" );
        click.addActionListener( this::clickAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( click );
        buttonPanel.add( exit );
                
        return buttonPanel;
    }

    /**
     * Moves the mouse to the selected cell
     * and clicks it.
     * Invoked with the "Click" button is pushed.
     * 
     * @param evt   object accompanying action event; not used
     */
    /**
     * @param evt
     */
    private void clickAction( ActionEvent evt )
    {
        // Get the rectangle, relative to the position of the table,
        // that encloses the selected cell, and make sure it is visible.
        int         row         = rowModel.getNumber().intValue();
        int         col         = colModel.getNumber().intValue();
        Rectangle   rect        = table.getCellRect( row, col, true );
        table.scrollRectToVisible( rect );
        
        // Find the location of the table on the screen, and us it
        // to translated the rectangle to screen coordinates.
        Point       tableLoc    = table.getLocationOnScreen();
        int         xco         = tableLoc.x + rect.x + rect.width / 2;
        int         yco         = tableLoc.y + rect.y + rect.height / 2;
        
        // Move the mouse to the screen coordinates and click it.
        robot.mouseMove( xco, yco );
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
    }
    
    /**
     * Gets a robot to use to mouse events.
     * 
     * @return  a robot for simulating key and mouse events
     */
    private static Robot getRobot()
    {
        Robot   robot   = null;
        try
        {
            robot = new Robot();
        }
        catch ( AWTException exc )
        {
            System.exit( 1 );
        }
        return robot;
    }
    
    /**
     * Used to configure the data model
     * for the demo table.
     * Mainly needed to set column types.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private static class LocalTableModel extends DefaultTableModel
    {
        /**
         * Initializes the table model.
         * 
         * @param data      data encapsulated by the table model
         * @param headers   headers encapsulated by the table model
         */
        public LocalTableModel( Object[][] data, Object[] headers )
        {
            super( data, headers );
        }
        
        /**
         * Determines the type of a column in the table.
         * Column 2 is type integer, 
         * 3 and 4 are type decimal.
         */
        @Override
        public Class<?> getColumnClass( int col ) 
        {
            Class<?>    clazz   = null;
            if ( col == 2 )
                clazz = Integer.class;
            else if ( col == 3 || col == 4 )
                clazz = Double.class;
            else
                clazz = super.getColumnClass( col );
            return clazz;
        }
    }
}
