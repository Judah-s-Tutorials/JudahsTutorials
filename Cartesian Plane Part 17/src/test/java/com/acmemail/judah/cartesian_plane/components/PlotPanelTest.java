package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.test_utils.PlotPanelTestGUI;

public class PlotPanelTest
{
    private static final PropertyManager    pmgr    =
        PropertyManager.INSTANCE;
    private static final char               PII     = '\u03c0';
    private static PlotPanelTestGUI         testGUI;
    
    private Equation    currEquation;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> testGUI = new PlotPanelTestGUI() );
    }

    @BeforeEach
    public void setUp() throws Exception
    {
        currEquation = testGUI.newEquation();
        assertFalse( testGUI.isDMModified() );
    }

    @ParameterizedTest
    @ValueSource( strings= {"x=", "y=", "t=", "r="} )
    public void testEnter( String fieldID )
    {
        testSimple( fieldID, KeyEvent.VK_ENTER );
    }

    @ParameterizedTest
    @ValueSource( strings= {"x=", "y=", "t=", "r="} )
    public void testTab( String fieldID )
    {
        testSimple( fieldID, KeyEvent.VK_TAB );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"x=", "y=", "t=", "r="} )
    public void testInvalidEnter( String fieldID )
    {
        // Start by entering invalid text
        testGUI.click( fieldID );
        JComponent  comp = testGUI.getFocusedField();
        testGUI.clearText( fieldID );
        testGUI.paste( "ap" );
        assertTrue( testGUI.isChangedTextFont( fieldID ) );
        assertFalse( testGUI.isValidTextColor( fieldID ) );
        assertFalse( testGUI.isCommitted( fieldID ) );
        assertFalse( testGUI.isDMModified() );
        
        // Type ENTER, make sure invalid text not committed
        testGUI.type( KeyEvent.VK_ENTER );
        assertTrue( testGUI.isChangedTextFont( fieldID ) );
        assertFalse( testGUI.isValidTextColor( fieldID ) );
        assertFalse( testGUI.isCommitted( fieldID ) );
        assertFalse( testGUI.isDMModified() );
        
        // Type TAB, make sure focus does not move
        testGUI.type( KeyEvent.VK_TAB );
        assertTrue( testGUI.isChangedTextFont( fieldID ) );
        assertFalse( testGUI.isValidTextColor( fieldID ) );
        assertFalse( testGUI.isCommitted( fieldID ) );
        assertTrue( comp.isFocusOwner() );
        assertFalse( testGUI.isDMModified() );

        // Complete text entry so equation is valid.
        testGUI.type( KeyEvent.VK_I );
        assertTrue( testGUI.isChangedTextFont( fieldID ) );
        assertTrue( testGUI.isValidTextColor( fieldID ) );
        assertFalse( testGUI.isCommitted( fieldID ) );
        assertFalse( testGUI.isDMModified() );
        
        // Verify that enter commits the text
        testGUI.type( KeyEvent.VK_ENTER );
        assertFalse( testGUI.isChangedTextFont( fieldID ) );
        assertTrue( testGUI.isDMModified() );
        assertTrue( testGUI.isValidTextColor( fieldID ) );
        assertTrue( testGUI.isCommitted( fieldID ) );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"x=", "y=", "t=", "r="} )
    public void testInvalidTab( String fieldID )
    {
        // Shorter version of testInvalid; make sure tab
        // works when its supposed to.       
        JComponent  comp        = null;
        String      invalidText = "ap";
        int         finalChar   = KeyEvent.VK_I;
        
        // Start by entering invalid text
        testGUI.click( fieldID );
        comp = testGUI.getFocusedField();
        testGUI.clearText( fieldID );
        testGUI.paste( invalidText );
        testGUI.type( KeyEvent.VK_TAB );
        assertTrue( comp.isFocusOwner() );
        
        testGUI.type( finalChar );
        testGUI.type( KeyEvent.VK_TAB );
        assertTrue( testGUI.isCommitted( fieldID ) );
        assertFalse( comp.isFocusOwner() );
        
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
        assertTrue( testGUI.isChangedTextFont( fieldID ) );
        assertTrue( testGUI.isValidTextColor( fieldID ) );
        
        assertTrue( testGUI.positionAtPI() > 0 );
        testGUI.typeCtrlP();
        assertTrue( testGUI.isChangedTextFont( fieldID ) );
        assertTrue( testGUI.isValidTextColor( fieldID ) );

        String  actResult   = testGUI.getText( fieldID );
        testGUI.type( KeyEvent.VK_ENTER );
        assertFalse( testGUI.isChangedTextFont( fieldID ) );
        assertTrue( testGUI.isValidTextColor( fieldID ) );
        assertEquals( expResult, actResult );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"YPLOT", "XYPLOT", "TPLOT", "RPLOT" } )
    public void testComboBox( String strCmd )
    {
        pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
        Command cmd = Command.valueOf( strCmd );
        assertNotNull( cmd );
        testGUI.setPlotType( cmd );
        assertEquals( strCmd, currEquation.getPlot() );
        assertTrue( testGUI.isDMModified() );
    }

    @Test
    public void testPlotY()
    {
        String  toPlot      = "2x";
        Point2D expResult   = new Point2D.Double( 1, 2 );
        currEquation.setRangeStart( "1" );
        currEquation.setRangeEnd( "2" );
        
        testGUI.enterText( "y=", toPlot );
        assertTrue( testGUI.isDMModified() );
        assertTrue( testGUI.isCommitted( "y=" ) );

        testGUI.setPlotType( Command.YPLOT );
        testGUI.clickPlotButton();
        Point2D actResult   = testGUI.getPlotPoint();
        assertEquals( expResult, actResult );
        assertTrue( testGUI.isDMModified() );
    }
    
    @Test
    public void testPlotXY()
    {
        String  toPlotX     = "cos(t)";
        String  toPlotY     = "sin(t)";
        Point2D expResult   = new Point2D.Double( 0, 1 );
        currEquation.setRangeStart( "pi/2" );
        currEquation.setRangeEnd( "pi/2" );
        
        testGUI.enterText( "y=", toPlotY );
        testGUI.enterText( "x=", toPlotX );
        assertTrue( testGUI.isDMModified() );
        assertTrue( testGUI.isCommitted( "y=" ) );
        assertTrue( testGUI.isCommitted( "x=" ) );
        
        testGUI.setPlotType( Command.XYPLOT );
        testGUI.clickPlotButton();
        Point2D actResult   = testGUI.getPlotPoint();
        assertEquals( expResult, actResult );
    }
    
    @Test
    public void testPlotR()
    {
        String  toPlotR     = "1.5";
        Point2D expResult   = new Point2D.Double( -1.5, 0 );
        currEquation.setRangeStart( "pi" );
        currEquation.setRangeEnd( "pi" );
        
        testGUI.enterText( "r=", toPlotR );
        assertTrue( testGUI.isDMModified() );
        assertTrue( testGUI.isCommitted( "r=" ) );
        testGUI.setPlotType( Command.RPLOT );
        testGUI.clickPlotButton();
        Point2D actResult   = testGUI.getPlotPoint();
        assertEquals( expResult, actResult );
    }
    
    @Test
    public void testPlotT()
    {
        String  toPlotT     = "pi/r";
        Point2D expResult   = new Point2D.Double( 0, 2 );
        currEquation.setRangeStart( "2" );
        currEquation.setRangeEnd( "2" );
        
        testGUI.enterText( "t=", toPlotT );
        assertTrue( testGUI.isDMModified() );
        assertTrue( testGUI.isCommitted( "t=" ) );
        testGUI.setPlotType( Command.TPLOT );
        testGUI.clickPlotButton();
        Point2D actResult   = testGUI.getPlotPoint();
        assertEquals( expResult, actResult );
    }

    private void testSimple( String fieldID, int keyCode )
    {
        String  orig    = testGUI.getExpression( fieldID );
        
        testGUI.click( fieldID );
        assertFalse( testGUI.isChangedTextFont( fieldID ) );
        assertTrue( testGUI.isValidTextColor( fieldID ) );
        assertFalse( testGUI.isDMModified() );
        
        testGUI.clearText( fieldID );
        assertFalse( testGUI.isChangedTextFont( fieldID ) );
        assertTrue( testGUI.isValidTextColor( fieldID ) );
        assertFalse( testGUI.isCommitted( fieldID ) );
        assertFalse( testGUI.isDMModified() );
        
        testGUI.type( KeyEvent.VK_A );
        assertTrue( testGUI.isChangedTextFont( fieldID ) );
        assertTrue( testGUI.isValidTextColor( fieldID ) );
        assertFalse( testGUI.isCommitted( fieldID ) );
        assertFalse( testGUI.isDMModified() );
        
        testGUI.type( KeyEvent.VK_ADD );
        assertTrue( testGUI.isChangedTextFont( fieldID ) );
        assertFalse( testGUI.isValidTextColor( fieldID ) );
        assertFalse( testGUI.isCommitted( fieldID ) );
        assertFalse( testGUI.isDMModified() );
        
        testGUI.type( KeyEvent.VK_B );
        assertTrue( testGUI.isChangedTextFont( fieldID ) );
        assertTrue( testGUI.isValidTextColor( fieldID ) );
        assertFalse( testGUI.isCommitted( fieldID ) );
        assertFalse( testGUI.isDMModified() );
        
        assertEquals( orig, testGUI.getExpression( fieldID ) );
        testGUI.type( keyCode );
        assertFalse( testGUI.isChangedTextFont( fieldID ) );
        assertTrue( testGUI.isValidTextColor( fieldID ) );
        assertTrue( testGUI.isCommitted( fieldID ) );
        assertTrue( testGUI.isDMModified() );

        String  currExpr    = testGUI.getExpression( fieldID );
        assertEquals( currExpr, testGUI.getValue( fieldID ) );
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
