package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.test_utils.VariablePanelTestGUI;

class VariablePanelTest
{
    private static final PropertyManager    pMgr            = 
        PropertyManager.INSTANCE;
    private static final int                defaultPrec     =
        pMgr.asInt( CPConstants.VP_DPRECISION_PN );
    private static final Map<String,Double> defaultPairs    = 
        new HashMap<>();
    private static VariablePanelTestGUI testGUI;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception
    {
        "abcdefgh".chars()
            .forEach( c -> 
                defaultPairs.put( "" + (char)c, (double)(c - 'a') )
            );
        GUIUtils.schedEDTAndWait( () -> 
            testGUI = new VariablePanelTestGUI()
        );
    }

    @BeforeEach
    public void setUp() throws Exception
    {
        Equation            equation    = 
            new Exp4jEquation( defaultPairs, "0" );
        testGUI.loadEquation( equation );
        testGUI.changeDPrecision( defaultPrec );
    }
    
    @Test
    public void testInitialState()
    {
        assertEquals( defaultPairs, testGUI.getTableVars() );
        assertEquals( defaultPairs, testGUI.getEquationVars() );
        assertTrue( testGUI.isEnabled() );
        assertFalse( testGUI.isDelEnabled() );
        validatePrecision();
    }
    
    @Test
    public void testToString()
    {
        String  result  = testGUI.panelToString();
        defaultPairs.keySet().stream()
            .map( k -> k + "," )
            .forEach( s -> assertTrue( result.contains( s ), s ) );
    }

    @Test
    public void testNoEquation()
    {
        testGUI.loadEquation( null );
        assertFalse( testGUI.isEnabled() );
        assertFalse( testGUI.isDelEnabled() );
    }
    
    @Test
    public void testGetDPrecision()
    {
        assertEquals( defaultPrec, testGUI.getDPrecision() );
    }
    
    @ParameterizedTest
    @ValueSource( ints = {KeyEvent.VK_ENTER, KeyEvent.VK_TAB} )
    public void testEditValue( int keyCode )
    {
        double              newValue    = 10;
        String              targetVar   = "a";
        Map<String,Double>  expMap      = new HashMap<>( defaultPairs );
        
        int                 row         = testGUI.getRowOf( targetVar );
        expMap.put( targetVar, newValue );
        testGUI.editValue( row, "" + newValue, keyCode );
        assertEquals( expMap, testGUI.getTableVars() );
        assertEquals( expMap, testGUI.getEquationVars() );
    }
    
    @Test
    public void testEditInvalidValue()
    {
        String  targetVar   = "a";
        
        int     row         = testGUI.getRowOf( targetVar );
        testGUI.editValue( row, "#", KeyEvent.VK_ENTER );
        assertEquals( defaultPairs, testGUI.getTableVars() );
        assertEquals( defaultPairs, testGUI.getEquationVars() );
    }
    
    @Test
    public void testDelete()
    {
        String[]    toDelete    = { "a", "e" };
        assertFalse( testGUI.isDelEnabled() );
        testGUI.selectRows( toDelete );
        assertTrue( testGUI.isDelEnabled() );
        testGUI.pushDeleteButton();
        
        Map<String,Double>  expMap  = new HashMap<>( defaultPairs );
        Stream.of( toDelete ).forEach( expMap::remove );
        assertEquals( expMap, testGUI.getTableVars() );
        assertEquals( expMap, testGUI.getEquationVars() );
        validatePrecision();
    }
    
    @Test
    public void testAddNameEnter()
    {
        String  newName = "bb";
        testGUI.doAddProcessing( newName, KeyEvent.VK_ENTER, false );
        
        Map<String,Double>  expMap  = new HashMap<>( defaultPairs );
        expMap.put( newName, 0d );
        assertEquals( expMap, testGUI.getTableVars() );
        assertEquals( expMap, testGUI.getEquationVars() );
        validatePrecision();
    }
    
    @Test
    public void testAddCancel()
    {
        testGUI.doAddProcessing( "aa", KeyEvent.VK_ESCAPE, false );
        assertEquals( defaultPairs, testGUI.getTableVars() );
        assertEquals( defaultPairs, testGUI.getEquationVars() );
        validatePrecision();
    }
    
    @Test
    public void testAddNameValueEnter()
    {
        String  newName = "aa";
        String  newVal  = "999";
        String  newText = newName + "," + newVal;
        testGUI.doAddProcessing( newText, KeyEvent.VK_ENTER, false );
        
        Map<String,Double>  expMap  = new HashMap<>( defaultPairs );
        expMap.put( newName, Double.parseDouble( newVal ) );
        assertEquals( expMap, testGUI.getTableVars() );
        assertEquals( expMap, testGUI.getEquationVars() );
        validatePrecision();
    }
    
    @Test
    public void testAddNoSelection()
    {
        // Add with no row selected. New name/value pair should
        // go to end of table.
        String  newName = "bb";
        testGUI.doAddProcessing( newName, KeyEvent.VK_ENTER, false );
        
        Map<String,Double>  expMap  = new HashMap<>( defaultPairs );
        expMap.put( newName, 0d );
        assertEquals( expMap, testGUI.getTableVars() );
        assertEquals( expMap, testGUI.getEquationVars() );
        validatePrecision();
        
        int     newPos  = testGUI.getRowOf( newName );
        assertFalse( newPos < 0 );
        assertEquals( expMap.size() - 1, newPos );
    }
    
    @ParameterizedTest
    @ValueSource( ints= {0,1,2} )
    public void testAddWithSelection( int rowNum )
    {
        // Add new variable above selected row
        String  newName = "bb";
        testGUI.selectRows( rowNum );
        testGUI.doAddProcessing( newName, KeyEvent.VK_ENTER, false );
        int     newRow  = testGUI.getRowOf( newName );
        assertEquals( rowNum, newRow ); 
        
        Map<String,Double>  expMap  = new HashMap<>( defaultPairs );
        expMap.put( newName, 0d );
        assertEquals( expMap, testGUI.getTableVars() );
        assertEquals( expMap, testGUI.getEquationVars() );
        validatePrecision();
    }
    
    @Test
    public void testAddInvalidName()
    {
        testGUI.doAddProcessing( "#a", KeyEvent.VK_ENTER, true );
        assertEquals( defaultPairs, testGUI.getTableVars() );
        assertEquals( defaultPairs, testGUI.getEquationVars() );
    }
    
    @Test
    public void testAddDuplicateName()
    {
        testGUI.doAddProcessing( "a", KeyEvent.VK_ENTER, true );
        assertEquals( defaultPairs, testGUI.getTableVars() );
        assertEquals( defaultPairs, testGUI.getEquationVars() );
    }
    
    @Test
    public void testAddInvalidValue()
    {
        testGUI.doAddProcessing( "z,1a", KeyEvent.VK_ENTER, true );
        assertEquals( defaultPairs, testGUI.getTableVars() );
        assertEquals( defaultPairs, testGUI.getEquationVars() );
        validatePrecision();
    }
    
    @Test
    public void testChangePrecisionNonZero()
    {
        int newPrecision    = defaultPrec + 1;
        validatePrecision();
        pMgr.setProperty( CPConstants.VP_DPRECISION_PN, newPrecision );
        // sanity check
        int testPrecision   = pMgr.asInt( CPConstants.VP_DPRECISION_PN );
        assertEquals( newPrecision, testPrecision );
        validatePrecision();
    }
    
    @Test
    public void testChangePrecisionZero()
    {
        int newPrecision = 0;
        pMgr.setProperty( CPConstants.VP_DPRECISION_PN, newPrecision );
        // sanity check
        int testPrecision = pMgr.asInt( CPConstants.VP_DPRECISION_PN );
        assertEquals( newPrecision, testPrecision );
        validatePrecision();
    }
    
    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testIsCellEditable( int col )
    {
        // This is just to get test coverage of the table
        // model used by VariablePanel
        testGUI.clickOn( 0, col );
    }

    private void validatePrecision()
    {
        int     expDec  = pMgr.asInt( CPConstants.VP_DPRECISION_PN );
        String  text    = testGUI.getLabelText( 0, 1 );
        int     decPos  = text.indexOf( "." );
        if ( expDec <= 0 )
            assertEquals( decPos, -1 );
        else
        {
            int len     = text.length();
            int actDec  = len - (decPos + 1);
            assertEquals( expDec, actDec );
        }
    }
}
