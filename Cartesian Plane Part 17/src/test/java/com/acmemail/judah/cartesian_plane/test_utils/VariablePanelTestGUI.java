package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.VariablePanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

public class VariablePanelTestGUI
{
    /** Reduces typing when accessing PropertyManager singleton. */
    private static final PropertyManager    pMgr    =
        PropertyManager.INSTANCE;

    /** Text on the "+" button. */
    private static final String plusLabel   = "\u2795";
    /** Text on the "-" button. */
    private static final String minusLabel  = "\u2796";

    /** The ParameterPanel under test. */
    private final VariablePanel varPanel;
    
    private final RobotAssistant    robotAsst   = getRobot();
    private final Robot             robot       = robotAsst.getRobot();
    private final JButton           addButton;
    private final JButton           delButton;
    private final JTable            table;
    private final DefaultTableModel tableModel;
    private Equation                equation;
    
    /** Temporary object for limited use by lambdas. */
    private Object              adHocObject1;
    /** Temporary object for limited use by lambdas. */
    private Optional<Double>    adHocOptionalDouble1;

    /**
     * Constructor.
     * Fully configures and displays GUI.
     * 
     */
    public VariablePanelTestGUI()
    {
        JFrame  frame       = new JFrame( "Parameter Panel Test Frame" );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        varPanel = new VariablePanel();
        contentPane.add( varPanel );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
        addButton = getButton( plusLabel );
        delButton = getButton( minusLabel );
        table = getTable();
        TableModel  temp    = table.getModel();
        assertTrue( temp instanceof DefaultTableModel );
        tableModel = (DefaultTableModel)temp;
    }
    
    /**
     * Instantiates and loads a new Equation.
     */
    public Equation newEquation()
    {
        equation = new Exp4jEquation();
        varPanel.load( equation );
        equation.setVar( "b", 111 );
        pMgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, true);
        setDMModified( false );
        return equation;
    }
    
    /**
     * Activates the GUI's add (+) button.
     */
    public void pushAddButton()
    {
        GUIUtils.schedEDTAndWait( () -> addButton.doClick() );
    }
    
    /**
     * Activates the GUI's delete (-) button.
     */
    public void pushDeleteButton()
    {
        GUIUtils.schedEDTAndWait( () -> delButton.doClick() );
    }
    
    /**
     * Selects within the GUI's JTable the rows
     * corresponding to the given indices.
     * 
     * @param items the given indices
     */
    public void select( Integer... items )
    {
        GUIUtils.schedEDTAndWait( () -> {
            ListSelectionModel  listModel   = table.getSelectionModel();
            Stream.of( items )
                .forEach( n -> listModel.addSelectionInterval( n, n ) );
        });
    }
    
    /**
     * Clears all selected rows in the GUI's JTable.
     * 
     * @param items the given indices
     */
    public void clearSelection()
    {
        GUIUtils.schedEDTAndWait( () -> {
            ListSelectionModel  listModel   = table.getSelectionModel();
            listModel.clearSelection();
        });
    }
    
    /**
     * Uses Robot to paste the given string, 
     * presumably into a text field.
     * 
     * @param str   the given string
     */
    public void paste( String str )
    {
        robotAsst.paste( str );
    }
    
    /**
     * Uses Robot to type the given key code, 
     * presumably into a text field.
     * 
     * @param keyCode   the key code
     */
    public void type( int keyCode )
    {
        robot.keyPress( keyCode );
        robot.keyRelease( keyCode );
    }
    
    public void 
    doAddProcessing( String text, int keyCode, boolean expectError )
    {
        Thread  thread  = new Thread( () -> pushAddButton() );
        thread.start();
        Utils.pause( 250 );
        robotAsst.paste( text );
        Utils.pause( 2000 );
        type( keyCode );
        if ( expectError )
        {
            String  msg = "Failed to find message dialog.";
            Utils.pause( 2000 );
            ComponentFinder finder  = 
                new ComponentFinder( true, false, true );
            Window  window  = finder.findWindow( c -> true );
            assertNotNull( window, msg );
            assertTrue( window instanceof JDialog, msg );
            
            Predicate<JComponent>   pred    = 
                ComponentFinder.getButtonPredicate( "OK" );
            JComponent  comp    = ComponentFinder.find( window, pred );
            assertNotNull( comp );
            assertTrue( comp instanceof JButton );
            ((JButton)comp).doClick();
        }
        Utils.join( thread );
    }
    
    /**
     * Traverse the table's data model
     * for a row containing a given name
     * and return its value
     * in an Optional.
     * If not found,
     * an empty Optional is returned.
     *  
     * @param name  the given name
     * 
     * @return  
     *      Optional containing the value corresponding to name,
     *      or empty Optional if name not found.
     */
    public Optional<Double> getTableValue( String name )
    {
        int                 bound   = table.getRowCount();
        Optional<Double>    value   =
        IntStream.range( 0, bound )
            .mapToObj( NameValuePair::new )
            .filter( p -> name.equals( p.name ) )
            .map( p -> p.value )
            .findFirst();
        
        return value;
    }
    
    public int getRowOf( String name )
    {
        int     bound   = table.getRowCount();
        int     row     =
        IntStream.range( 0, bound )
            .filter( i -> name.equals( table.getValueAt( i, 0 ) ) )
            .findFirst().orElse( -1 );
        
        return row;
    }
    
    /**
     * Gets from the currently open equation
     * the value associated with a given variable name
     * and return it in an Optional.
     * If not found,
     * an empty Optional is returned.
     *  
     * @param name  the given name
     * 
     * @return  
     *      Optional containing the value corresponding to name,
     *      or empty Optional if name not found.
     */
    public Optional<Double> getEquationValue( String name )
    {
        return equation.getVar( name );
    }
    
    public boolean isCommitted( String name )
    {
        Optional<Double>    tableValue      = getTableValue( name );
        assertTrue( tableValue.isPresent() );
        Optional<Double>    equationValue   = getTableValue( name );
        assertTrue( equationValue.isPresent() );
        boolean             result          = 
            tableValue.equals( equationValue );
        return result;
    }
    
    /**
     * Edits the value column of the given row.
     * The user specifies the value
     * to type into the given cell
     * and the key to terminate the edit
     * (should be VK_ENTER or VK_TAB).
     * 
     * @param row       the given row
     * @param value     the new value
     * @param keyCode   key to terminate edit
     */
    public void editValue( int row, String value, int keyCode )
    {
        JTextField  textField   = getEditComponent( row, 1 );
        Point       location    = getLocationOnScreen( textField );
        clearText( textField );
        
        robot.mouseMove( location.x, location.y );
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
//        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
//        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
        
        robotAsst.paste( value );
        GUIUtils.schedEDTAndWait( () -> textField.postActionEvent() );
        GUIUtils.schedEDTAndWait( () -> { return; } );
//        robot.keyPress( keyCode );
//        robot.keyRelease( keyCode );
        Utils.pause( 250 );
    }
    
    /**
     * Clears the text field with the given string identifier.
     * 
     * @param fieldID   the given string identifier
     */
    public void clearText( JTextField textField )
    {
        GUIUtils.schedEDTAndWait( () -> textField.setText( "" ) );
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
        GUIUtils.schedEDTAndWait( () -> 
            adHocObject1 = getEditComponentEDT(row, col)
        );
        return (JTextField)adHocObject1;
    }
    
    /**
     * Does the same job as {@linkplain #getEditComponent}
     * but assumes that it is executing on the EDT.
     * 
     * @param row   given row-coordinate
     * @param col   given column-coordinate
     * 
     * @return  component used to edit the cell at the given coordinates
     */
    private JTextField getEditComponentEDT( int row, int col )
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
     * Gets the screen location of the given component;
     * executed on the EDT.
     * 
     * @param comp  the given component
     * 
     * @return  the screen location of the given component
     */
    private Point getLocationOnScreen( Component comp )
    {
        GUIUtils.schedEDTAndWait( 
            () -> adHocObject1 = comp.getLocationOnScreen()
        );
        return (Point)adHocObject1;
    }

    /**
     * Sets the DM_MODIFIED_PN property.
     * 
     * @param val   new value of property
     */
    private void setDMModified( boolean val )
    {
        pMgr.setProperty( CPConstants.DM_MODIFIED_PN, val );
    }
    
    private JTable getTable()
    {
        Predicate<JComponent>   pred    = c -> (c instanceof JTable);
        JComponent              comp    = 
            ComponentFinder.find( varPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JTable );
        return (JTable)comp;
    }

    /**
     * Gets the JButton with the given text.
     * 
     * @param text  the given text
     * 
     * @return the JButton with the given text
     */
    private JButton getButton( String text )
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( text );
        JComponent              comp    = 
            ComponentFinder.find( varPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
    }
    
    private RobotAssistant getRobot()
    {
        RobotAssistant  robot   = null;
        try
        {
            robot = new RobotAssistant();
            robot.setAutoDelay( 250 );
        }
        catch ( AWTException exc )
        {
            fail( "Unable to instantiate RobotAssistant" );
        }
        return robot;
    }
    
    private class NameValuePair
    {
        public final String name;
        public final Double value;
        
        public NameValuePair( int index )
        {
            assert( index < tableModel.getRowCount() );
            Object  oName   = tableModel.getValueAt( index, 0 );
            Object  oValue  = tableModel.getValueAt( index, 1 );
            assertTrue( oName instanceof String );
            assertTrue( oValue instanceof  Double );
            name = (String)oName;
            value = (Double)oValue;
        }
    }
}