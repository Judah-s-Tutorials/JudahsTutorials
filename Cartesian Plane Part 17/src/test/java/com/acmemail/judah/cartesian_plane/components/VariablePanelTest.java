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

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;
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
    static void setUpBeforeClass() throws Exception
    {
        "abcdefgh".chars()
            .forEach( c -> 
                defaultPairs.put( "" + (char)c, (double)(c - 'a') )
            );
        GUIUtils.schedEDTAndWait( () -> 
            testGUI = new VariablePanelTestGUI()
        );
        
        pMgr.addPropertyChangeListener( CPConstants.VP_DPRECISION_PN,
            e -> {
                Object  newValue    = e.getNewValue();
                String  strValue    = newValue.toString();
                if ( Integer.parseInt( strValue ) < 4 )
                    System.out.println( "*** " + e.getNewValue() );
            });
    }

    @BeforeEach
    void setUp() throws Exception
    {
        Equation            equation    = new Exp4jEquation();
        Map<String,Double>  oldMap      = equation.getVars();
        oldMap.keySet().forEach( equation::removeVar);
        
        defaultPairs.entrySet()
            .forEach( s -> equation.setVar( s.getKey(), s.getValue() ) );
        testGUI.loadEquation( equation );
        pMgr.setProperty( CPConstants.VP_DPRECISION_PN, defaultPrec );
    }
    

    @Test
    public void initialStateTest()
    {
        assertEquals( defaultPairs, testGUI.getTableVars() );
        assertEquals( defaultPairs, testGUI.getEquationVars() );
        assertTrue( testGUI.isEnabled() );
        assertFalse( testGUI.isDelEnabled() );
        validatePrecision();
    }
    
    @Test
    public void deleteTest()
    {
        String[]    toDelete    = { "a", "e" };
        testGUI.selectRows( toDelete );
        assertTrue( testGUI.isDelEnabled() );
        testGUI.pushDeleteButton();
        
        Map<String,Double>  expMap  = new HashMap<>( defaultPairs );
        Stream.of( toDelete ).forEach( expMap::remove );
        assertEquals( expMap, testGUI.getTableVars() );
        assertEquals( expMap, testGUI.getEquationVars() );
//        validatePrecision();
    }
    
    @Test
    public void addCancelTest()
    {
        testGUI.doAddProcessing( "aa", KeyEvent.VK_ESCAPE, false );
        assertEquals( defaultPairs, testGUI.getTableVars() );
        assertEquals( defaultPairs, testGUI.getEquationVars() );
        validatePrecision();
    }
    
    @Test
    public void addNameValueEnterTest()
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
    public void addNameEnterTest()
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
    public void noEquationTest()
    {
        testGUI.loadEquation( null );
        assertFalse( testGUI.isEnabled() );
        assertFalse( testGUI.isDelEnabled() );
    }
    
    @Test
    public void addInvalidName()
    {
        testGUI.doAddProcessing( "#a", KeyEvent.VK_ENTER, true );
        assertEquals( defaultPairs, testGUI.getTableVars() );
        assertEquals( defaultPairs, testGUI.getEquationVars() );
    }
    
    @Test
    public void addDuplicateName()
    {
        testGUI.doAddProcessing( "a", KeyEvent.VK_ENTER, true );
        assertEquals( defaultPairs, testGUI.getTableVars() );
        assertEquals( defaultPairs, testGUI.getEquationVars() );
    }
    
    @Test
    public void addInvalidValue()
    {
        testGUI.doAddProcessing( "z,1a", KeyEvent.VK_ENTER, true );
        assertEquals( defaultPairs, testGUI.getTableVars() );
        assertEquals( defaultPairs, testGUI.getEquationVars() );
        validatePrecision();
    }
    
    @Test
    public void testChangePrecision()
    {
        int newPrecision    = defaultPrec + 1;
        validatePrecision();
        pMgr.setProperty( CPConstants.VP_DPRECISION_PN, newPrecision );
        // sanity check
        int testPrecision   = pMgr.asInt( CPConstants.VP_DPRECISION_PN );
        assertEquals( newPrecision, testPrecision );
        validatePrecision();
    }

    private void validatePrecision()
    {
        int     expDec  = pMgr.asInt( CPConstants.VP_DPRECISION_PN );
        String  text    = testGUI.getLabelText( 0, 1 );
        int     decPos  = text.indexOf( "." );
        System.out.println( text );
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
