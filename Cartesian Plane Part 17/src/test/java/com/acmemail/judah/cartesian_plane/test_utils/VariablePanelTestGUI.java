package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.VariablePanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
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
    private Equation                equation;
    
    /** Temporary object for limited use by lambdas. */
    private Object              adHocObject1;
    /** Temporary object for limited use by lambdas. */
    private int                 adHocInt1;
    /** Temporary object for limited use by lambdas. */
    private String              adHocString1;

    /**
     * Constructor.
     * Fully configures the GUI and displays it.
     */
    public VariablePanelTestGUI()
    {
        JFrame  frame       = new JFrame( "Parameter Panel Test Frame" );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        varPanel = new VariablePanel();
        contentPane.add( varPanel );
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
        
        addButton = getButton( plusLabel );
        delButton = getButton( minusLabel );
        table = getTable();
    }
    
    /**
     * Exercises getDPrecision in the VariablePanel object.
     */
    public int getDPrecision()
    {
        GUIUtils.schedEDTAndWait( 
            () -> adHocInt1 = varPanel.getDPrecision() 
        );
        return adHocInt1;
    }
    
    /**
     * Change the decimal precision
     * by modifying the PropertyManater property.
     * 
     * @param prec  precision to change to
     */
    public void changeDPrecision( int prec )
    {
        GUIUtils.schedEDTAndWait( () ->
            pMgr.setProperty( CPConstants.VP_DPRECISION_PN, prec )
        );
    }
    
    /**
     * Gets the value returned by
     * the toString method
     * of the VariablePanel.
     * 
     * @return  
     *      the value returned by the toString method 
     *      of the VariablePanel object
     */
    public String panelToString()
    {
        GUIUtils.schedEDTAndWait( () -> 
            adHocString1 = varPanel.toString() 
        );
            
        return adHocString1;
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
        GUIUtils.schedEDTAndWait( () -> {
            this.equation = equation;
            boolean isOpen  = equation != null;
            varPanel.load( equation );
            pMgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, isOpen );
            pMgr.setProperty( CPConstants.DM_MODIFIED_PN, false );        
        });
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
     * The pasted string may or may not be valid.
     * 
     * @param text          the given text
     * @param keyCode       the given key code
     * @param expectError   
     *      true if the operation is expected
     *      to generate an error
     *      (i.e. the given text is invalid).
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
        // Move the mouse to the screen coordinates and click it.
        positionMouse( row, col );
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
        GUIUtils.schedEDTAndWait( () -> 
            adHocObject1 = getCellRendererComponentEDT( row, col )
        );
        JLabel  comp    = (JLabel)adHocObject1;
        String  text    = comp.getText();
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
        type( keyCode );
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
            assertTrue( adHocObject1 instanceof ListSelectionModel );
            
            ListSelectionModel  model   = (ListSelectionModel)adHocObject1;
            Stream.of( rows )
                .forEach( i -> model.addSelectionInterval( i, i ) );
        });
    }
    
    /**
     * Selects the rows containing the given names.
     * 
     * @param rows  the given names
     */
    public void selectRows( String... names )
    {
        GUIUtils.schedEDTAndWait( () ->
            adHocObject1 = Stream.of( names )
                .map( this::getRowOf )
                .toArray( Integer[]::new )
        );
        selectRows( (Integer[])adHocObject1 );
    }
    
    /**
     * Clears the given text field.
     * 
     * @param textField   the given text field
     */
    public void clearText( JTextField textField )
    {
        GUIUtils.schedEDTAndWait( () -> textField.setText( "" ) );
    }
    
    /**
     * Positions the mouse over the cell with the given coordinates.
     * 
     * @param row   given row coordinate
     * @param col   given column coordinate
     */
    public void positionMouse( int row, int col )
    {
        // Get the rectangle, relative to the position of the table,
        // that encloses the selected cell, and make sure it is visible.
        Rectangle   rect        = table.getCellRect( row, col, true );
        table.scrollRectToVisible( rect );
        
        // Find the location of the table on the screen, and use it
        // to translate the rectangle to screen coordinates.
        Point       tableLoc    = table.getLocationOnScreen();
        int         xco         = tableLoc.x + rect.x + rect.width / 2;
        int         yco         = tableLoc.y + rect.y + rect.height / 2;
        
        // Move the mouse to the screen coordinates
        robot.mouseMove( xco, yco );
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
            throw new ComponentException( "Invalid editor component." );
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
     * Gets the renderer component for a given cell.
     * This method must be called from the EDT.
     * 
     * @param row   the row of the given cell
     * @param col   the column of the given cell
     * 
     * @return  the renderer component for the given cell
     */
    private JLabel getCellRendererComponentEDT( int row, int col )
    {
        Object              val         = table.getValueAt( row, col );
        TableCellRenderer   renderer    = table.getCellRenderer( row, col );
        Component           comp        = 
            renderer.getTableCellRendererComponent(
                table, val, false, false, row, col 
            );
        assertNotNull( comp );
        assertTrue( comp instanceof JLabel );
        return (JLabel)comp;
    }
    
    /**
     * Gets the JTable component from the VariablePanel.
     * 
     * @return  the JTable component from the VariablePanel
     */
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
    
    /**
     * Instantiates a RobotAssistant.
     * The main advantage of using this method
     * is to encapsulate the try/catch logic.
     * 
     * @return  a new RobotAssistant object
     */
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
}