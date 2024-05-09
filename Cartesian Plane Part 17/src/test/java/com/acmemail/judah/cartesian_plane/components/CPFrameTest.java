package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.test_utils.CPFrameTestGUI;

class CPFrameTest
{
    private static final String sampleName          = "Sample Name";
    private static final double sampleVarDValue     = 200;
    private static final String sampleVarValue      = 
        String.valueOf( sampleVarDValue );
    private static final String sampleStartValue    = "1000";
    private static final String sampleXExpr         = "13";
    
    private static CPFrameTestGUI   testGUI;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> testGUI = new CPFrameTestGUI() );
    }

    @BeforeEach
    void setUp() throws Exception
    {
        String      testVarName     = CPFrameTestGUI.DEFAULT_VAR_NAME;
        Equation    equation    = new Exp4jEquation();
        equation.setName( sampleName );
        equation.setVar( testVarName, sampleVarDValue );
        equation.setRangeStart( sampleStartValue );
        equation.setXExpression( sampleXExpr );
        testGUI.newEquation( equation );
    }

    @Test
    public void testEnabled()
    {
        assertTrue( testGUI.isEnabled() );
        testGUI.newEquation( null );
        assertTrue( testGUI.isDisabled() );
    }

    @Test
    public void testInitValues()
    {
        assertEquals( sampleName, testGUI.getText( NamePanel.class ) );
        assertEquals( sampleVarValue, testGUI.getValue() );
        assertEquals( 
            sampleStartValue, 
            testGUI.getText( ParameterPanel.class ) 
        );
        assertEquals( sampleXExpr, testGUI.getText( PlotPanel.class ) );
    }
    
    @Test
    public void testGetEquation()
    {
        Equation    equation    = new Exp4jEquation();
        testGUI.newEquation( equation );
        assertEquals( equation, testGUI.getEquation() );
    }
    
    @Test
    public void testGetCartesianPlane()
    {
        CartesianPlane  plane   = testGUI.getCartesianPlane();
        assertNotNull( plane );
    }
}
