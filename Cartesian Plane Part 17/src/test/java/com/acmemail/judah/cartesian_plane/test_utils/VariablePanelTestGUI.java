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
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.VariablePanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;

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
     * Loads the given equation
     * into the VariablePanel.
     * The given equation may be null.
     * If equation is null
     * DM_OPEN_EQUATION_PN is set to false,
     * otherwise it is set to true.
     * DM_MODIFIED_PN is set to false.
     */
    public void loadEquation( Equation equation )
    {
        this.equation = equation;
        boolean isOpen  = equation != null;
        varPanel.load( equation );
        pMgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, isOpen );
        pMgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
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
    
    /**
     * Pushes the "Add" button
     * and pastes the given text
     * into the resultant input dialog.
     * The input operation is completed
     * by typing the given key code
     * (should be VK_ENTER or VK_ESCAPE).
     * @param text
     * @param keyCode
     * @param expectError
     */
    public void 
    doAddProcessing( String text, int keyCode, boolean expectError )
    {
        Thread  thread  = new Thread( () -> pushAddButton() );
        thread.start();
        Utils.pause( 250 );
        robotAsst.paste( text );
        type( keyCode );
        if ( expectError )
        {
            String  msg = "Failed to find message dialog.";
            Utils.pause( 500 );
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
    
    /**
     * Get the number of the row in the JTable
     * that contains the given name.
     * Returns -1 if not found.
     * 
     * @param name  the given name
     * 
     * @return
     *      the number of the row in the JTable
     *      that contains the given name; -1 if not found
     */
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
    
    /**
     * Moves the mouse to the given component
     * and clicks on it.
     * 
     * @param comp  the given component
     */
    public void clickOn( JComponent comp )
    {
        Point   location    = getLocationOnScreen( comp );
        robot.mouseMove( location.x, location.y );
        click();
    }
    
    /**
     * Moves the mouse to the given row and column of the JTable
     * and click on it.
     * 
     * @param row   the given row
     * @param col   the given column
     */
    public void clickOn( int row, int col )
    {
        Component   comp        = getCellRendererComponent( row, col );
        Point       location    = getLocationOnScreen( comp );
        robot.mouseMove( location.x, location.y );
        click();
    }
    
    /**
     * Uses robot to click the mouse.
     */
    public void click()
    {
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
    }
    
    /**
     * Returns true if the GUI's JTable and "Add" button are enabled.
     * 
     * @return  true if the JTable and JButtons are enabled
     */
    public boolean isEnabled()
    {
        boolean result  = table.isEnabled() && addButton.isEnabled() ;
        return result;
    }
    
    /**
     * Gets a name to value map of all the name/value pairs
     * in the JTable.
     * 
     * @return  
     *      a name to value map 
     *      of all the name/value pairs in the JTable
     */
    public Map<String,Double> getTableVars()
    {
        Map<String,Double>  map = new HashMap<>();
        IntStream.range( 0, table.getRowCount() )
            .forEach( i -> {
                String  name    = (String)table.getValueAt( i, 0 );
                Double  val     = (Double)table.getValueAt( i, 1 );
                map.put( name, val );
            });
        return map;
    }   
    
    /**
     * Gets a name to value map of all the name/value pairs
     * in the currently open equation. 
     * Returns an empty map if no equation is currently open.
     * 
     * @return  
     *      a name to value map 
     *      of all the name/value pairs in the JTable
     */
    public Map<String,Double> getEquationVars()
    {
        Map<String,Double>  map = 
            equation == null ? null : equation.getVars();
        
        return map;
    }
    
    /**
     * Get the text from the label from the cell
     * at the given row and column
     * of the JTable.
     * 
     * @param row   given row
     * @param col   given column
     * 
     * @return  from the label at the given row and column
     */
    public String getLabelText( int row, int col )
    {
        Component   comp    = getCellRendererComponent( row, col );
        assertTrue( comp instanceof JLabel );
        String              text        = ((JLabel)comp).getText();
        return text;
    }
    
    /**
     * Returns true if the GUI's "Delete" button is enabled.
     * 
     * @return  true if the GUI's "Delete" button is enabled
     */
    public boolean isDelEnabled()
    {
        boolean result  = delButton.isEnabled() ;
        return result;
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
     * Edits the value column of the row
     * that contains the given variable name.
     * The user specifies the value
     * to type into the target cell
     * and the key to terminate the edit
     * (should be VK_ENTER or VK_TAB).
     * 
     * @param name      the given name
     * @param value     the new value
     * @param keyCode   key to terminate edit
     */
    public void editValue( String name, String value, int keyCode )
    {
        int row = getRowOf( name );
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
        clickOn( textField );
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
        
        robotAsst.paste( value );
        GUIUtils.schedEDTAndWait( () -> textField.postActionEvent() );
        GUIUtils.schedEDTAndWait( () -> { return; } );
        Utils.pause( 250 );
    }
    
    /**
     * Selects the given rows.
     * 
     * @param rows  the given rows
     */
    public void selectRows( Integer... rows )
    {
        GUIUtils.schedEDTAndWait( () -> {
            table.clearSelection();
            adHocObject1 = table.getSelectionModel();
        });
        assertTrue( adHocObject1 instanceof ListSelectionModel );
        
        ListSelectionModel  model   = (ListSelectionModel)adHocObject1;
        Stream.of( rows )
            .forEach( i -> model.addSelectionInterval( i, i ) );
    }
    
    /**
     * Selects the rows containing the given names.
     * 
     * @param rows  the given names
     */
    public void selectRows( String... names )
    {
        Integer[]   rows    = Stream.of( names )
            .map( this::getRowOf )
            .toArray( Integer[]::new );
        selectRows( rows );
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
    
    private Component getCellRendererComponent( int row, int col )
    {
        Object              val         = table.getValueAt( row, col );
        TableCellRenderer   renderer    = table.getCellRenderer( row, col );
        Component           comp        = 
            renderer.getTableCellRendererComponent(
                table, val, false, false, row, col 
            );
        assertNotNull( comp );
        return comp;
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
    
    /**
     * Encapsulates a name/value pair.
     * Presumably, "name" is the name of a variable
     * and "value" is its value.
     * 
     * @author Jack Straub
     */
    public class NameValuePair
    {
        /** Encapsulated name. */
        public final String name;
        /** Encapsulated value. */
        public final Double value;
        
        /**
         * Constructor.
         * 
         * @param name      the name of the encapsulated pair
         * @param value     the value of the encapsulated pair
         */
        public NameValuePair( String name, double value )
        {
            this.name = name;
            this.value = value;
        }
        
        /**
         * Constructor.
         * Sets the name/value pair
         * to the values contained in the JTable row
         * with the given index.
         * 
         * @param index the given index
         */
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