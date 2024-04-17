package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;

import javax.swing.JFormattedTextField;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.ParameterPanelTestGUI;

class ParameterPanelTest
{
    private static final char               PII     = '\u03c0';
    private static ParameterPanelTestGUI    testGUI;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> 
            testGUI = new ParameterPanelTestGUI()
        );
    }

    @BeforeEach
    public void setUp() throws Exception
    {
        testGUI.newEquation();
    }
    
    @ParameterizedTest
    @ValueSource( 
        strings= {"Radius", "Theta", "Param", "Start", "End", "Step"}
    )
    public void testStringFields( String ident )
    {
        testGUI.newEquation();
        testStringField( ident,KeyEvent.VK_ENTER );
        testGUI.newEquation();
        testStringField( ident,KeyEvent.VK_TAB );
    }
    
    @Test
    public void testEnabled()
    {
        assertTrue( testGUI.isEnabled() );
        testGUI.closeEquation();
        assertFalse( testGUI.isNotEnabled() );
    }
    
    @ParameterizedTest
    @ValueSource( ints= {KeyEvent.VK_ENTER,KeyEvent.VK_TAB} )
    public void testPrecision( int commitKey )
    {
        testGUI.newEquation();
        String  ident       = "Prec";
        assertFalse( testGUI.isDMModified() );
        assertTrue( testPrecision() );        
        
        testGUI.clearText( ident );
        assertEquals( "", testGUI.getText( ident ) );
        assertFalse( testPrecision() );
        
        testGUI.click( ident );
        testGUI.type( KeyEvent.VK_A );
        assertFalse( testGUI.isDMModified() );
        assertFalse( testGUI.isValidTextColor( ident ) );
        assertTrue( testGUI.isChangedTextFont( ident ) );
        assertFalse( testPrecision() );
        
        JFormattedTextField focused = testGUI.getFocusedField();
        testGUI.type( KeyEvent.VK_TAB );
        assertEquals( focused, testGUI.getFocusedField() );
        assertFalse( testPrecision() );
        
        testGUI.clearText( ident );
        testGUI.type( KeyEvent.VK_1 );
        assertFalse( testGUI.isDMModified() );
        assertTrue( testGUI.isValidTextColor( ident ) );
        assertTrue( testGUI.isChangedTextFont( ident ) );
        assertFalse( testPrecision() );
    
        testGUI.type( commitKey );
        assertTrue( testGUI.isDMModified() );
        assertTrue( testGUI.isValidTextColor( ident ) );
        assertFalse( testGUI.isChangedTextFont( ident ) );
        assertTrue( testPrecision() );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"Start", "End", "Step"} )
    public void testHasPI( String ident )
    {
        testGUI.newEquation();
        String  before      = "2pi/4";
        String  expAfter    = "2" + PII + "/4";
        testGUI.clearText( ident );
        testGUI.click( ident );
        testGUI.paste( before );
        testGUI.positionAtPI();
        testGUI.typeCtrlP();
        assertEquals( expAfter, testGUI.getText( ident ) );
        assertFalse( testGUI.isCommitted( ident ) );
        assertFalse( testGUI.isDMModified() );
        assertTrue( testGUI.isValidTextColor( ident ) );
        assertTrue( testGUI.isChangedTextFont( ident ) );

        testGUI.type( KeyEvent.VK_ENTER );
        assertTrue( testGUI.isCommitted( ident ) );
        assertTrue( testGUI.isDMModified() );
        assertTrue( testGUI.isValidTextColor( ident ) );
        assertFalse( testGUI.isChangedTextFont( ident ) );
        assertEquals( expAfter, testGUI.getEqProperty( ident ) );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"Radius", "Theta", "Param", } )
    public void testNotHasPI( String ident )
    {
        testGUI.newEquation();
        String  before      = "pi";
        String  expAfter    = before;
        testGUI.clearText( ident );
        testGUI.click( ident );
        testGUI.paste( before );
        testGUI.positionAtPI();
        testGUI.typeCtrlP();
        assertEquals( expAfter, testGUI.getText( ident ) );
        assertFalse( testGUI.isCommitted( ident ) );
        assertFalse( testGUI.isDMModified() );
        assertTrue( testGUI.isValidTextColor( ident ) );
        assertTrue( testGUI.isChangedTextFont( ident ) );
    }
    
    private void testStringField( String ident, int commitKey )
    {
        String  text    = testGUI.getText( ident );
        String  field   = testGUI.getEqProperty( ident );
        Object  value   = testGUI.getValue( ident );
        assertEquals( text, field );
        assertEquals( text, value );
        assertFalse( testGUI.isDMModified() );
        
        testGUI.clearText( ident );
        assertEquals( "", testGUI.getText( ident ) );
        assertEquals( value, testGUI.getValue( ident ) );
        assertEquals( field, testGUI.getEqProperty( ident ) );
        
        testGUI.click( ident );
        testGUI.type( KeyEvent.VK_SLASH );
        assertFalse( testGUI.isCommitted( ident ) );
        assertFalse( testGUI.isDMModified() );
        assertFalse( testGUI.isValidTextColor( ident ) );
        assertTrue( testGUI.isChangedTextFont( ident ) );
        assertEquals( field, testGUI.getEqProperty( ident ) );
        
        JFormattedTextField focused = testGUI.getFocusedField();
        testGUI.type( KeyEvent.VK_TAB );
        assertEquals( focused, testGUI.getFocusedField() );
        assertFalse( testGUI.isCommitted( ident ) );
        
        testGUI.clearText( ident );
        testGUI.type( KeyEvent.VK_X );
        assertFalse( testGUI.isCommitted( ident ) );
        assertFalse( testGUI.isDMModified() );
        assertTrue( testGUI.isValidTextColor( ident ) );
        assertTrue( testGUI.isChangedTextFont( ident ) );
        assertEquals( field, testGUI.getEqProperty( ident ) );

        testGUI.type( commitKey );
        assertTrue( testGUI.isCommitted( ident ) );
        assertTrue( testGUI.isDMModified() );
        assertTrue( testGUI.isValidTextColor( ident ) );
        assertFalse( testGUI.isChangedTextFont( ident ) );
        assertEquals( "x", testGUI.getEqProperty( ident ) );
    }
    
    /**
     * Gets the value of the precision property
     * from the PropertyManager.
     * 
     * @return  
     *      the value of the precision property
     *      as maintained by the PropertyManager
     */
    private int getPrecValue()
    {
        Object  value   = testGUI.getValue( "Prec" );
        assertNotNull( value );
        assertTrue( value instanceof Integer );
        int intValue    = (int)value;
        return intValue;
    }
    
    /**
     * Verifies that precision property
     * in the GUI and equation match.
     * "Value" in the GUI 
     * means the "value" of the "Prec" text field.
     * Returns true if precision is committed.
     * 
     * @return  true if the precision property is committed
     */
    private boolean testPrecision()
    {
        // The precision property according to the PropertyManager.
        int     pmPrecision     = 
            PropertyManager.INSTANCE 
                .asInt( CPConstants.VP_DPRECISION_PN );
        // The precision property according to the GUI.
        int     guiPrecision    = getPrecValue();
        // The precision property according to the equation.
        String  strPrecision    = testGUI.getEqProperty( "Prec" );
        int     eqPrecision     = Integer.parseInt( strPrecision );
        
        // Property manager, GUI and equation values must
        // always match;
        assertEquals( pmPrecision, guiPrecision );
        assertEquals( pmPrecision, eqPrecision );
        
        boolean committed   = testGUI.isCommitted( "Prec" );
        return committed;
    }
}
