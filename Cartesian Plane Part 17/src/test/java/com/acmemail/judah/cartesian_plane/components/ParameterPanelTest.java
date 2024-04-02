package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;

import javax.swing.JFormattedTextField;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.ParameterPanelTestGUI;

class ParameterPanelTest
{
    private static final char               PII     = '\u03c0';
    private static ParameterPanelTestGUI    testGUI;

    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> 
            testGUI = new ParameterPanelTestGUI()
        );
    }

    @BeforeEach
    void setUp() throws Exception
    {
        testGUI.newEquation();
    }
    
    @ParameterizedTest
    @ValueSource( 
        strings= {"Radius", "Theta", "Param", "Start", "End", "Step"}
    )
    public void testStringFields( String ident )
    {
        testStringField( ident,KeyEvent.VK_ENTER );
        testGUI.newEquation();
        testStringField( ident,KeyEvent.VK_TAB );
    }
    
    @ParameterizedTest
    @ValueSource( ints= {KeyEvent.VK_ENTER,KeyEvent.VK_TAB} )
    public void testPrecistion( int commitKey )
    {
        String  ident       = "Prec";
        String  text        = testGUI.getText( ident );
        String  field       = testGUI.getExpression( ident );
        Object  objValue    = testGUI.getValue( ident );
        String  strValue    = getIntValue( objValue );
        assertEquals( text, field );
        assertEquals( text, strValue );
        assertFalse( testGUI.isDMModified() );
        
        testGUI.clearText( ident );
        assertEquals( "", testGUI.getText( ident ) );
        assertEquals( objValue, testGUI.getValue( ident ) );
        assertEquals( field, testGUI.getExpression( ident ) );
        
        testGUI.click( ident );
        testGUI.type( KeyEvent.VK_A );
        assertFalse( testGUI.isCommitted( ident ) );
        assertFalse( testGUI.isDMModified() );
        assertFalse( testGUI.isValidTextColor( ident ) );
        assertTrue( testGUI.isChangedTextFont( ident ) );
        assertEquals( field, testGUI.getExpression( ident ) );
        
        JFormattedTextField focused = testGUI.getFocusedField();
        testGUI.type( KeyEvent.VK_TAB );
        assertEquals( focused, testGUI.getFocusedField() );
        assertFalse( testGUI.isCommitted( ident ) );
        
        testGUI.clearText( ident );
        testGUI.type( KeyEvent.VK_1 );
        assertFalse( testGUI.isCommitted( ident ) );
        assertFalse( testGUI.isDMModified() );
        assertTrue( testGUI.isValidTextColor( ident ) );
        assertTrue( testGUI.isChangedTextFont( ident ) );
        assertEquals( field, testGUI.getExpression( ident ) );
    
        testGUI.type( commitKey );
        assertTrue( testGUI.isCommitted( ident ) );
        assertTrue( testGUI.isDMModified() );
        assertTrue( testGUI.isValidTextColor( ident ) );
        assertFalse( testGUI.isChangedTextFont( ident ) );
        assertEquals( "1", testGUI.getExpression( ident ) );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"Start", "End", "Step"} )
    public void testHasPI( String ident )
    {
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
        assertEquals( expAfter, testGUI.getExpression( ident ) );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"Radius", "Theta", "Param", } )
    public void testNotHasPI( String ident )
    {
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
    
    private String getIntValue( Object value )
    {
        assertNotNull( value );
        assertTrue( value instanceof Integer );
        String  strValue    = String.valueOf( value );
        return strValue;
    }
    
    private void testStringField( String ident, int commitKey )
    {
        String  text    = testGUI.getText( ident );
        String  field   = testGUI.getExpression( ident );
        Object  value   = testGUI.getValue( ident );
        assertEquals( text, field );
        assertEquals( text, value );
        assertFalse( testGUI.isDMModified() );
        
        testGUI.clearText( ident );
        assertEquals( "", testGUI.getText( ident ) );
        assertEquals( value, testGUI.getValue( ident ) );
        assertEquals( field, testGUI.getExpression( ident ) );
        
        testGUI.click( ident );
        testGUI.type( KeyEvent.VK_SLASH );
        assertFalse( testGUI.isCommitted( ident ) );
        assertFalse( testGUI.isDMModified() );
        assertFalse( testGUI.isValidTextColor( ident ) );
        assertTrue( testGUI.isChangedTextFont( ident ) );
        assertEquals( field, testGUI.getExpression( ident ) );
        
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
        assertEquals( field, testGUI.getExpression( ident ) );

        testGUI.type( commitKey );
        assertTrue( testGUI.isCommitted( ident ) );
        assertTrue( testGUI.isDMModified() );
        assertTrue( testGUI.isValidTextColor( ident ) );
        assertFalse( testGUI.isChangedTextFont( ident ) );
        assertEquals( "x", testGUI.getExpression( ident ) );
    }
}
