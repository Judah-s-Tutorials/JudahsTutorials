package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.State;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * This application demonstrates two ways
 * in which to implement automated editing
 * of a JTable cell 
 * for the purpose of testing.
 * See the {@linkplain #doEdit(ActionEvent)} method
 * to see how to automate editing
 * from beginning ("table.editCellAt(row,col)")
 * to commit ("editor.stopCellEditing()").
 * <p>
 * The ({@linkplain #doubleClick(ActionEvent)} method
 * shows how to find the screen location
 * of the editor component
 * for a given cell
 * and put it into editing mode
 * by double-clicking it.
 * This method does not modify the cell value
 * or execute a commit.
 * In this application
 * type any text to modify the given value
 * and then press Enter to commit it.
 * In a fully automated unit test
 * you would use Robot
 * to change the text
 * and then press "Enter."
 * Commits are automatically logged.
 * <p>
 * Any operation should be begun
 * by setting the row and column to address.
 * To auto-edit push the "Edit" button.
 * To double-click push the "Double Click" button.
 * To print the currently selected row
 * (that is, the row with the index
 * selected in the row editor JSpinner)
 * press the "Print" button.
 * The "Print" function can be activated
 * by typing control-P.
 * 
 * @author Jack Straub
 */
/**
 * @author Jack Straub
 */
public class AutoEditCellDemo
{
    /** The key code for P on the keyboard. */
    private static final int    keyCodeP    = KeyEvent.VK_P;
    /** The value entered when the "edit" button is pushed. */
    private static final String newValue    = "***edited-value***";
    
    private final Object[]      headers         = 
    { "State", "Abbrev", "Population" };
    private final Object[][]    data            =
        State.getDataSet( "state", "abbreviation", "population" );
    
    /** Robot used for double-clicking a cell. */
    private final Robot                 robot       = getRobot();
    /** Demo JTable. */
    private final JTable                table;
    /** Activity dialog for displaying feedback. */
    private final ActivityLog           log;
    
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

    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( AutoEditCellDemo::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private AutoEditCellDemo()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        table = new JTable( data, headers );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        contentPane.add( getBottomPanel(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        
        log = new ActivityLog();
        Dimension   frameDim    = frame.getPreferredSize();
        Point       frameLoc    = new Point( 200, 100 );
        int         logXco      = frameLoc.x + frameDim.width + 10;
        int         logYco      = frameLoc.y;
        frame.setLocation( frameLoc );
        log.setLocation( logXco, logYco );
        frame.pack();
        frame.setVisible( true );
        
        addKeyListener( table );
        table.getModel().addTableModelListener( this::tableChanged );
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
        JPanel      panel       = new JPanel();
        JButton     editButton  = new JButton( "Edit" );
        JButton     doubleClick = new JButton( "Double Click" );
        JButton     printButton = new JButton( "Print" );
        JButton     exit        = new JButton( "Exit" );
        
        editButton.addActionListener( this::doEdit );
        printButton.addActionListener( this::doPrint );
        doubleClick.addActionListener( this::doubleClick );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        panel.add( editButton );
        panel.add( doubleClick );
        panel.add( printButton );
        panel.add( exit );
        
        return panel;
    }
    
    /**
     * Performs automated editing 
     * of the table cell
     * indicated by the row- and column-editors.
     * Invoked when the "Edit" button is pushed.
     * <p>
     * Get the component used for editing a cell,
     * which should be a JTextField,
     * and modify its text.
     * After setting the modified value
     * it has to be committed.
     * 
     * @param evt   object describing the ActionEvent; not used
     */
    private void doEdit( ActionEvent evt  )
    {
        // To get the cell's editor component we need to know the target
        // row and column of the cell, and the value it contains.
        int             row         = rowModel.getNumber().intValue();
        int             col         = colModel.getNumber().intValue();
        Object          val         = table.getValueAt( row, col );
        
        // Before getting getting the cell editor component we need to
        // initiate editing of the cell. Then we get the component from
        // the cell's editor.
        table.editCellAt( row, col );
        TableCellEditor editor      = table.getCellEditor();
        Component       comp        = 
            editor.getTableCellEditorComponent(table, val, true, row, col);
        
        // Sanity check. The only way this will fail is if we
        // coded something incorrectly.
        if ( !(comp instanceof JTextField) )
            throw new Error( "component error" );

        // Put the edited value in the text field.
        JTextField      textField   = (JTextField)comp;
        textField.setText( newValue );
        
        // Commit the edited value.
        editor.stopCellEditing();
        appendLog( "Auto edit" );
    }
    
    /**
     * Find the cell at the location
     * specified by the row- and column- editors
     * and double-click it.
     * Invoked when the "Double Click" button is pushed.
     * 
     * @param evt   object passed to ActionListener; not used
     */
    private void doubleClick( ActionEvent evt )
    {
        int         row         = rowModel.getNumber().intValue();
        int         col         = colModel.getNumber().intValue();

        JTextField  textField   = getEditComponent( row, col );
        Point       location    = textField.getLocationOnScreen();
        robot.mouseMove( location.x, location.y );
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
        appendLog( "Double click" );
    }
    
    /**
     * Prints all columns of the row
     * selected in the row-editor.
     * Invoked when the operator
     * pushes the "Print" button
     * or when the operator types control-P.
     * 
     * @param evt   object passed to ActionListener; not used
     */
    private void doPrint( ActionEvent evt )
    {
        int             row     =  rowModel.getNumber().intValue();
        int             lastCol = table.getColumnCount();
        StringBuilder   bldr    = new StringBuilder()
            .append( "Print row " ).append( row ).append( "=" );
        IntStream.range( 0, lastCol )
            .mapToObj( i -> table.getValueAt( row, i ) )
            .map( bldr::append )
            .forEach( o -> bldr.append( " " ) );
        log.append( bldr.toString() );
    }
    
    /**
     * Detects when the value of a cell
     * has been committed.
     * 
     * @param evt   object passed to TableModelListener
     */
    private void tableChanged( TableModelEvent evt )
    {
        if ( evt.getType() == TableModelEvent.UPDATE )
        {
            int     row     = evt.getFirstRow();
            int     col     = evt.getColumn();
            Object  val     = table.getValueAt( row, col );
            StringBuilder   bldr    = new StringBuilder()
                .append( "Commit: " )
                .append( "row=" ).append( row )
                .append( ",col=").append( col )
                .append( " " ).append( val );
            log.append( bldr.toString() );
        }
    }
    
    /**
     * Writes a message to the application log,
     * including the given description,
     * the row and column indicated
     * by the row- and column-selectors,
     * and the data contained in the data model
     * at the given cell location.
     * 
     * @param descrip   the given description
     */
    private void appendLog( String descrip )
    {
        StringBuilder   bldr    = new StringBuilder()
            .append( descrip ).append( ": " )
            .append( "row=" ).append( rowModel.getNumber() )
            .append( ",col=" ).append( colModel.getNumber() );
        log.append( bldr.toString() );
    }
    
    /**
     * Adds a key listener to
     * the given component,
     * and the text field 
     * used to edit cell data.
     * 
     * @param container the given component
     */
    private void addKeyListener( JComponent container )
    {
        KeyAdapter  listener    = new KeyAdapter() {
            @Override
            public void keyReleased( KeyEvent evt )
            {
                int     keyCode     = evt.getKeyCode();
                boolean isCtrl      = evt.isControlDown();
                
                if ( isCtrl && keyCode == keyCodeP )
                    doPrint( null );
            }
        };
        JTextField  textField   = getEditComponent( 0, 0 );
        textField.addKeyListener( listener );
        container.addKeyListener( listener );
    }
    
    /**
     * Obtains the component
     * used to edit a cell
     * and the given row- and column-coordinates.
     * Note that as part of obtaining the component
     * editing of the cell must be initiated.
     * 
     * @param row   given row-coordinate
     * @param col   given column-coordinate
     * 
     * @return  component used to edit the cell at the given coordinates
     */
    private JTextField getEditComponent( int row, int col )
    {
        // In addition to knowing the row and column of the target cell
        // we have to know the value it contains.
        Object  val     = table.getValueAt( row, col );
        
        // To get the editor component we have to initiate editing
        // of the cell, and obtain its cell editor.
        table.editCellAt( row, col );
        TableCellEditor editor  = table.getCellEditor();
        Component       comp    = 
            editor.getTableCellEditorComponent(table, val, true, row, col);
        
        // Sanity check. This will only fail if we coded something wrong.
        if ( !(comp instanceof JTextField) )
            throw new Error( "component error" );
        return (JTextField)comp;
    }
    
    /**
     * Gets a robot to use 
     * to simulate key and mouse events.
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
}
