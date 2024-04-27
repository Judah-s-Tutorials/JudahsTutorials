package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

/**
 * Manages JFormattedTextFields
 * and their connections to an Equation.
 * 
 * @author Jack Straub
 */
public class FTextFieldTestMgr
{
    /** Unicode value for the Greek letter pi. */
    private static final char               PII     = '\u03c0';

    /** Convenient declaration of PropertyManager singleton */
    private static final PropertyManager            pmgr            =
        PropertyManager.INSTANCE;
    /** 
     * Map of field IDs to JFormattedTextFields. For example,
     * textFieldMap.get( "y=" ) would return the text field
     * for editing the yExpression, and textFieldMap.get( "Start" )
     * would return the text field for editing the startExpression.
     */
    private final Map<String,JFormattedTextField>   textFieldMap    = 
        new HashMap<>();
    
    /**
     * Map of field IDs to suppliers of values from the
     * currently open equation. For example, exprMap.get( "y=" )
     * will return a reference to equation.getYExpression()
     * and exprMap.get( "Start" )
     * will return a reference to equation.getRangeStartExpr();
     */
    private final Map<String,Supplier<String>>  supplierMap = 
        new HashMap<>();
    
    /** For simulating key strokes and mouse button pushes. */
    private final RobotAssistant    robotAsst;
    /** Robot object from RobotAssistant. */
    private final Robot             robot;

    /** Currently loaded equation; may be null. */
    private Equation    equation    = null;
    
    /** Temporary object for limited use by lambdas. */
    private Object  adHocObject1;
    
    /**
     * Performs complete initialization of this object.
     */
    public FTextFieldTestMgr()
    {
        robotAsst = makeRobot();
        robot = robotAsst.getRobot();
        robot.setAutoDelay( 100 );
    }
    
    /**
     * Adds a fieldID/textfield pair to the text field map.
     * 
     * @param fieldID       ID of text field to add
     * @param textField     test field to add
     */
    public void 
    putTextField( String fieldID, JFormattedTextField textField )
    {
        textFieldMap.put( fieldID, textField );
    }
    
    /**
     * Adds a fieldID/Supplier to the supplier map.
     * 
     * @param fieldID   field ID
     * @param supplier  the Supplier
     */
    public void putSupplier( String fieldID, Supplier<String> supplier )
    {
        supplierMap.put( fieldID, supplier );
    }
    
    /**
     * Obtains the text field indicated
     * by the given string identifier
     * and clicks on it using the {@linkplain #click(JComponent)} method.
     * The string identifier must be one of those
     * present in the textFieldMap.
     * 
     * @param fieldID   the given string identifier
     */
    public void click( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        assertNotNull( textField );
        click( textField );
    }
    
    /**
     * Clicks the mouse on the given component.
     * 
     * @param comp  the given component
     */
    public void click( JComponent comp )
    {
        Point       pos     = comp.getLocationOnScreen();
        Dimension   size    = comp.getSize();
        pos.x += size.width / 2;
        pos.y += size.height / 2;
        robot.mouseMove( pos.x, pos.y );
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
    }

    /**
     * Uses a Robot to execute a press and release
     * for the given key code.
     * 
     * @param keyCode   the given key code
     */
    public void type( int keyCode )
    {
        robot.keyPress( keyCode );
        robot.keyRelease( keyCode );
    }
    
    /**
     * Types the sequence of key codes for control-P.
     */
    public void typeCtrlP()
    {
        type( KeyEvent.VK_CONTROL, KeyEvent.VK_P );
    }
    
    /**
     * In the text field with the keyboard focus,
     * find the first occurrence of the characters "pi"
     * (not case-sensitive)
     * and position the caret immediately after.
     * Returns the position of the caret,
     * or -1 if "pi" is not found.
     */
    public int positionAtPI()
    {
        JFormattedTextField textField   = getFocusedTextField();
        assertNotNull( textField );
        String  text        = getText( textField ).toLowerCase();
        int     caretPos    = text.indexOf( "pi" );
        if ( caretPos >= 0 )
            GUIUtils.schedEDTAndWait( () -> {
                textField.setCaretPosition( caretPos + 2 );
            });
        return caretPos;
    }

    /**
     * Returns true if all the text fields in the textFieldMap
     * are enabled.
     */
    public boolean isEnabled()
    {
        // notEnabled contains the first text field
        // that is not enabled.
        Optional<?> notEnabled  = textFieldMap
            .values()
            .stream()
            .filter( tf -> !tf.isEnabled() )
            .findFirst();
        
        // If there is at least one text field that is
        // not enabled, return false.
        boolean result  = notEnabled.isEmpty();
        return result;
    }

    /**
     * Returns true if all the text fields in the textFieldMap
     * are NOT enabled.
     */
    public boolean isNotEnabled()
    {
        // enabled contains the first text field
        // that is enabled.
        Optional<?> enabled = textFieldMap
            .values()
            .stream()
            .filter( tf -> tf.isEnabled() )
            .findFirst();
        
        // If there is at least one text field that is
        // enabled, return false.
        boolean result  = !enabled.isPresent();
        return result;
    }
    
    /**
     * Instantiates and loads a new Equation.
     */
    public Equation newEquation()
    {
        equation = new Exp4jEquation();
        equation.setRangeStart( "1" );
        equation.setRangeEnd( "2" );
        pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
        pmgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        pmgr.setProperty( CPConstants.DM_OPEN_FILE_PN, false );
        return equation;
    }
    
    /**
     * Simulates closing the currently open equation.
     * Sets DM_MODIFIED_PN, DM_OPEN_EQUATION_PN,
     * and DM_OPEN_FILE_PN to false.
     */
    public void closeEquation()
    {
        pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
        pmgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        pmgr.setProperty( CPConstants.DM_OPEN_FILE_PN, false );
    }
    
    /**
     * Enters the given text into a text field.
     * The text field is focused and cleared,
     * the text is pasted into the text field
     * and then ENTER is typed in the text field.
     * <p>
     * Precondition: the text argument is valid.
     * <p>
     * Postcondition: the expression argument is committed.
     * 
     * @param fieldID     ID of the text field to paste in
     * @param text        the given text
     */
    public void enterText( String fieldID, String text )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        click( textField );
        clearText( fieldID );
        paste( text );
        type( KeyEvent.VK_ENTER );
    }
    
    /**
     * Pastes the given string into component with focus.
     * The component should be a text field.
     * The text field is not cleared;
     * no assumptions are made about
     * the state of the text field.
     * 
     * @param str   the given string
     */
    public void paste( String str )
    {
        robotAsst.paste( str );
    }
    
    /**
     * Returns true if the text field
     * indicated by the given string identifier
     * has an italic font.
     * 
     * @param fieldID   the given string identifier
     * 
     * @return  
     *      true if the indicated text field
     *      has an italic font
     */
    public boolean isChangedTextFont( String fieldID )
    {
        JTextField  textField   = textFieldMap.get( fieldID );
        Font        font        = getFont( textField );
        boolean     result      = font.getStyle() == Font.ITALIC;
        return result;
    }
    
    /**
     * Returns true if the text field
     * indicated by the given string identifier
     * uses a "valid" text color.
     * <p>
     * Precondition: invalid text is displayed in red
     * 
     * @param fieldID   the given string identifier
     * 
     * @return  
     *      true if the indicated text field
     *      has uses a "valid" color
     */
    public boolean isValidTextColor( String fieldID )
    {
        JTextField  textField   = textFieldMap.get( fieldID );
        Color       foreground  = getColor( textField );
        boolean     result      = !foreground.equals( Color.RED );
        return result;
    }
    
    /**
     * Clears the text field with the given string identifier.
     * 
     * @param fieldID   the given string identifier
     */
    public void clearText( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        GUIUtils.schedEDTAndWait( () -> textField.setText( "" ) );
    }
    
    /**
     * Returns the text of the text field
     * with the given string identifier.
     * 
     * @param fieldID   the given string identifier
     * 
     * @return  the text of the indicated text field
     */
    public String getText( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
            assertNotNull( textField );
        String  text    = getText( textField );
        return text;
    }
    
    /**
     * Gets the property
     * from the currently open equation
     * indicated by the given field identifier.
     * For example,
     * if the given identifier is "y="
     * the value given by 
     * equation.getYExpression() will be returned.
     * 
     * @param fieldID   the given field identifier
     * 
     * @return  
     *      the property from the currently open equation
     *      indicated by the given field identifier
     */
    public String getEqProperty( String fieldID )
    {
        Supplier<String>    getter = supplierMap.get( fieldID );
        assertNotNull( getter );
        String              prop    = getter.get();
        
        return prop;
    }
    
    /**
     * Returns the text field that holds the keyboard focus.
     * If no text field has the focus
     * null is returned.
     * 
     * @return  the text field with the focus, or null if none
     */
    public JFormattedTextField getFocusedField()
    {
        JFormattedTextField textField   = getFocusedTextField();
        return textField;
    }
    
    /**
     * Get the value property
     * from the text field with the given field ID.
     * 
     * @param fieldID   the given field ID
     * 
     * @return  
     *      the value property
     *      from the text field with the given field ID
     */
    public Object getValue( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        Object              value       = 
            getProperty( () -> textField.getValue() );
        return value;
    }
    
    /**
     * Obtains the currently open equation.
     * 
     * @return  the currently open equation
     */
    public Equation getEquation()
    {
        return equation;
    }
    
    /**
     * Returns true if the text of the text field
     * with the given string identifier
     * has been committed.
     * 
     * @param fieldID   the given string identifier
     * 
     * @return  
     *      true if the text of the indicated text field
     *      has been committed
     */
    public boolean isCommitted( String fieldID )
    {
        String      text        = getText( fieldID );
        Object      value       = getValue( fieldID );
        String      strValue    = value.toString();
        boolean     committed   = text.equals( value.toString() );
        // if committed, make sure equation matches text field value
        if ( committed )
        {
            Supplier<String>    getter  = supplierMap.get( fieldID );
            assertNotNull( getter );
            assertEquals( strValue, getter.get() );
        }
        return committed;
    }

    /**
     * Calculate the expected result
     * of replacing "pi" with the Greek letter pi
     * in a given string.
     * If the given string contains more than one instance of "pi"
     * only the first is considered.
     * The search for "pi"
     * is case-insensitive.
     * If the search for "pi" fails
     * the entire string is returned.
     * 
     * @param input the given string
     * 
     * @return  
     *      the result of replacing "pi" with the greek letter pi
     *      in the given string
     */
    public String calculateExpPIResult( String input )
    {
        String  expRes  = input;
        int     piPos   = input.toLowerCase().indexOf( "pi" );
        if ( piPos >= 0 )
        {
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( input.substring( 0, piPos ) )
                .append( PII )
                .append( input.substring( piPos + 2 ) );
            expRes = bldr.toString();
        }
        return expRes;
    }

    /**
     * Returns the value of the DM_MODIFIED_PN property.
     * 
     * @return  the value of the DM_MODIFIED_PN property
     */
    public boolean isDMModified()
    {
        boolean modified    = pmgr.asBoolean( CPConstants.DM_MODIFIED_PN );
        return modified;
    }
    
    /**
     * Updates the state of the 
     * DM_MODIFIED_PN property.
     * 
     * @param state new state of DM_MODIFIED_PN property
     */
    public void setDMProperties( boolean state )
    {
        pmgr.setProperty( CPConstants.DM_MODIFIED_PN, state );
        pmgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, state );
    }

    /**
     * Types a series of key codes.
     * The keys will be pressed in the given sequence
     * then released in reverse sequence.
     * The expectation is that the initial key codes
     * are modifiers (shift, control, alt, etc.)
     * and the last key code is for a non-modifier.
     * For example, "type(VK_CONTROL, VK_ALT, VK_C)"
     * would be the equivalent of typing control-alt-C.
     * 
     * @param keyCodes  the series of key codes to type
     */
    private void type( int... keyCodes )
    {
        Deque<Integer>  stack   = new ArrayDeque<>();
        Arrays.stream( keyCodes )
            .peek( i -> stack.push( i ) )
            .forEach( robot::keyPress);
        while ( !stack.isEmpty() )
            robot.keyRelease( stack.pop() );
    }
    
    /**
     * Gets the text field
     * that currently holds the keyboard focus.
     * If no text field has the focus
     * null is returned.
     * 
     * @return  the text field with the focus, or null if none
     */
    private JFormattedTextField getFocusedTextField()
    {
        GUIUtils.schedEDTAndWait( () -> {
            adHocObject1 = textFieldMap.values().stream()
                .filter( t -> t.isFocusOwner() )
                .findFirst().orElse( null );
        });
        return (JFormattedTextField)adHocObject1;
    }
    
    /**
     * Gets the text color of the given text field.
     * 
     * @param textField the given text field
     * 
     * @return  the text color of the given text field
     */
    private Color getColor( JTextField textField )
    {
        Object  obj = getProperty( () -> textField.getForeground() );
        assertTrue( obj instanceof Color );
        return (Color)obj;
    }
    
    /**
     * Gets the text of the given text field.
     * 
     * @param textField the given text field
     * 
     * @return  the text of the given text field
     */
    private String getText( JTextField textField )
    {
        Object  obj = getProperty( () -> textField.getText() );
        assertTrue( obj instanceof String );
        return (String)obj;
    }
    
    /**
     * Gets the font of the given text field.
     * 
     * @param textField the given text field
     * 
     * @return  the font of the given text field
     */
    private Font getFont( JTextField textField )
    {
        Object  obj = getProperty( () -> textField.getFont() );
        assertTrue( obj instanceof Font );
        return (Font)obj;
    }
    
    /**
     * Executes the given Supplier
     * in the context of the EDT.
     * Returns the object obtained.
     * 
     * @param supplier  the given Supplier
     * 
     * @return  the object obtained
     */
    private Object getProperty( Supplier<Object> supplier )
    {
        GUIUtils.schedEDTAndWait( () -> adHocObject1 = supplier.get() );
        return adHocObject1;
    }
    
    /**
     * Instantiates a RobotAssictant.
     * 
     * @return the instantiate RobotAssistant.
     */
    private RobotAssistant makeRobot()
    {
        RobotAssistant  robot   = null;
        try
        {
            robot = new RobotAssistant();
        }
        catch ( AWTException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        return robot;
    }
}
