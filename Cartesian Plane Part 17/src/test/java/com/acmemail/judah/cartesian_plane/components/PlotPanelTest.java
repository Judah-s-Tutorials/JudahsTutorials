package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.test_utils.PlotPanelTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class PlotPanelTest
{
    private static final char       PII         = '\u03c0';
    private static PlotPanelTestGUI testGUI;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> testGUI = new PlotPanelTestGUI() );
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception
    {
    }

    @BeforeEach
    public void setUp() throws Exception
    {
        testGUI.newEquation();
    }

    @ParameterizedTest
    @ValueSource( strings= {"x=", "y=", "t=", "r="} )
    public void testEnter( String fieldID )
    {
        testSimple( fieldID, KeyEvent.VK_ENTER );
    }

    @ParameterizedTest
    @ValueSource( strings= {"x=", "y="} )
    public void testTab( String fieldID )
    {
        testSimple( fieldID, KeyEvent.VK_TAB );
    }

    @ParameterizedTest
    @ValueSource( strings= {"x=", "y=", "t=", "r="} )
    public void testPi( String fieldID )
    {
        String  before      = "a + 2pi - 5";
        String  expResult   = calculateExpPIResult( before );
        testGUI.click( fieldID );
        testGUI.clearText( fieldID );
        testGUI.paste( before );
        assertTrue( testGUI.isChangedText( fieldID ) );
        assertTrue( testGUI.isValidText( fieldID ) );
        
        assertTrue( testGUI.positionAtPI() > 0 );
        testGUI.typeCtrlP();
        assertTrue( testGUI.isChangedText( fieldID ) );
        assertTrue( testGUI.isValidText( fieldID ) );

        String  actResult   = testGUI.getText( fieldID );
        testGUI.type( KeyEvent.VK_ENTER );
        assertFalse( testGUI.isChangedText( fieldID ) );
        assertTrue( testGUI.isValidText( fieldID ) );
        assertEquals( expResult, actResult );
    }
    
    @Test
    public void testPlotButton()
    {
        testGUI.click( "y=" );
        testGUI.clearText( "y=");
        testGUI.type( KeyEvent.VK_X );
        testGUI.type( KeyEvent.VK_ENTER );
        testGUI.clickPlotButton();
        System.out.println( testGUI.getPlotPoint() );
        
        testGUI.setPlotType( Command.XYPLOT );
        testGUI.click( "y=");
        testGUI.clearText( "y=");
        testGUI.type( KeyEvent.VK_2 );
        testGUI.type( KeyEvent.VK_ENTER );
        testGUI.click( "x=");
        testGUI.clearText( "x=");
        testGUI.type( KeyEvent.VK_2 );
        testGUI.type( KeyEvent.VK_ENTER );
        testGUI.clickPlotButton();
        System.out.println( testGUI.getPlotPoint() );

        Utils.pause( 1000 );
    }

    private void testSimple( String fieldID, int keyCode )
    {
        String  orig    = testGUI.getExpression( fieldID );
        
        testGUI.click( fieldID );
        assertFalse( testGUI.isChangedText( fieldID ) );
        assertTrue( testGUI.isValidText( fieldID ) );
        
        testGUI.clearText( fieldID );
        assertFalse( testGUI.isChangedText( fieldID ) );
        assertTrue( testGUI.isValidText( fieldID ) );
        assertFalse( testGUI.isCommitted() );
        assertNotEquals( orig, testGUI.getText() );
        
        testGUI.type( KeyEvent.VK_A );
        assertTrue( testGUI.isChangedText( fieldID ) );
        assertTrue( testGUI.isValidText( fieldID ) );
        assertFalse( testGUI.isCommitted() );
        assertNotEquals( orig, testGUI.getText() );
        
        testGUI.type( KeyEvent.VK_ADD );
        assertTrue( testGUI.isChangedText( fieldID ) );
        assertFalse( testGUI.isValidText( fieldID ) );
        assertFalse( testGUI.isCommitted() );
        assertNotEquals( orig, testGUI.getText() );
        
        testGUI.type( KeyEvent.VK_B );
        assertTrue( testGUI.isChangedText( fieldID ) );
        assertTrue( testGUI.isValidText( fieldID ) );
        assertFalse( testGUI.isCommitted() );
        assertNotEquals( orig, testGUI.getText() );
        
        testGUI.type( keyCode );
        assertFalse( testGUI.isChangedText( fieldID ) );
        assertTrue( testGUI.isValidText( fieldID ) );
        assertTrue( testGUI.isCommitted() );
        String  currExpr    = testGUI.getExpression( fieldID );
        assertEquals( currExpr, testGUI.getValue( fieldID ) );

        Utils.pause( 250 );
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
    private String calculateExpPIResult( String input )
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
}
