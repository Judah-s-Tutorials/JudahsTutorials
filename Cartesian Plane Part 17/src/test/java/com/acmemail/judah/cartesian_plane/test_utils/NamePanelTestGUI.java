package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.NamePanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;

/**
 * Creates and manages a GUI containing a PlotPanel.
 * Provides utilities for the test program
 * to access and manipulate the PlotPanel
 * and the Equation it contains.
 * 
 * @author Jack Straub
 */
public class NamePanelTestGUI
{    
    /** Reduces typing when accessing PropertyManager singleton. */
    private static final PropertyManager    pMgr    =
        PropertyManager.INSTANCE;

    /** NamePanel under test. */
    private final NamePanel             namePanel;
    /** The text field from the NamePanel. */
    private final JFormattedTextField   textField;
    /** RobotAssistant for simulating operator actions. */
    private final RobotAssistant        robotAsst   = getRobot();
    /** Robot for simulating operator actions. */
    private final Robot                 robot       = robotAsst.getRobot();
    
    /** Currently open equation, may be null. */
    private Equation        equation;
    
    /** Temporary object for limited use by lambdas. */
    private Object              adHocObject1;

    /**
     * Constructor.
     * Assembles and displays the GUI.
     * Performs complete initialization of this object.
     */
    public NamePanelTestGUI()
    {
        JFrame  frame       = new JFrame( "Plot Panel Test Dialog" );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        namePanel = new NamePanel();
        contentPane.add( namePanel, BorderLayout.CENTER );
        
        // If we want to test that tabbing from the text field
        // causes a commit we have to have somewhere to tab to.
        // That's what this extra text field is for.
        contentPane.add( new JTextField( 10 ), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
        textField = getTextField();
    }
    
    /**
     * Simulates loading a new equation.
     * To close the currently open equation
     * pass null.
     */
    public void newEquation( Equation equation )
    {
        this.equation = equation;
        namePanel.load( equation );
        
        boolean open    = equation != null;
        pMgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
        pMgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, open );
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
     * Returns true if NamePanel's text field is enabled.
     * 
     * @return  true if NamePanel's text field is enabled
     */
    public boolean isEnabled()
    {
        Object  oResult = getProperty( () -> textField.isEnabled() );
        assertTrue( oResult instanceof Boolean);
        boolean result  = (boolean)oResult;
        return result;
    }
    
    /**
     * Returns the textField's text.
     * 
     * @return  the textField's text
     */
    public String getText()
    {
        Object  oResult = getProperty( () -> textField.getText() );
        assertTrue( oResult instanceof String);
        String  result  = (String)oResult;
        return result;
    }
    
    /**
     * Returns the textField's text.
     * 
     * @return  the textField's text
     */
    public String getValue()
    {
        Object  oResult = getProperty( () -> textField.getValue() );
        assertTrue( oResult instanceof String);
        String  result  = (String)oResult;
        return result;
    }
    
    /**
     * Returns the value of the name property
     * from the currently open equation.
     * Returns null if there is no open equation.
     * 
     * @return  the name property from the currently open equation
     */
    public String getEqValue()
    {
        String  name    = "";
        if ( equation != null )
            name = equation.getName();
        return name;
    }
    
    /**
     * Returns true if the text field's font
     * indicates that its value has not been committed.
     * 
     * @return  
     *      true if the text field's font
     *      indicates that its value has not been committed
     */
    public boolean isChangedFont()
    {
        Object  oResult = getProperty( () -> textField.getFont() );
        assertTrue( oResult instanceof Font );
        Font    font    = (Font)oResult;
        boolean result  = font.getStyle() == Font.ITALIC;
        return result;
    }
    
    /**
     * Returns the value of the DM_MODIFIED_PN property.
     * 
     * @return  the value of the DM_MODIFIED_PN property
     */
    public boolean isModified()
    {
        boolean result  = pMgr.asBoolean( CPConstants.DM_MODIFIED_PN );
        return result;
    }
    
    /**
     * Returns true if the text field's value
     * has been committed.
     * 
     * @return 
     *      true if true if the text field's value 
     *      has been committed
     */
    public boolean isCommitted()
    {
        String  text    = getText();
        String  value   = getValue();
        boolean result  = text.equals( value );
        return result;
    }
    
    /**
     * Pastes the given text into the text field.
     * 
     * @param text  the given text
     */
    public void paste( String text )
    {
        textField.setText( "" );
        clickTextField();
        robotAsst.paste( text );
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
     * Moves the mouse to the text field
     * and clicks on it.
     */
    public void clickTextField()
    {
        Object  obj         = 
            getProperty( () -> textField.getLocationOnScreen() );
        assertTrue( obj instanceof Point );
        Point   location    = (Point)obj;
        robot.mouseMove( location.x, location.y );
        click();
    }
    
    /**
     * Gets the value provided by the given supplier.
     * The operation is executed on the EDT.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the value provided by the given supplier
     */
    private Object getProperty( Supplier<Object> supplier )
    {
        GUIUtils.schedEDTAndWait( () -> adHocObject1 = supplier.get() );
        return adHocObject1;
    }
    
    /**
     * Gets the JFormattedTextField contained in the namePanel.
     * 
     * @return  the JFormattedTextField from the namePanel
     */
    private JFormattedTextField getTextField()
    {
        Predicate<JComponent>   pred    = 
            c -> (c instanceof JFormattedTextField);
        JComponent          comp    = 
            ComponentFinder.find( namePanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JFormattedTextField );
        return (JFormattedTextField)comp;
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
