package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.JPanel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        testAbled( true );
        testGUI.newEquation( null );
        testAbled( false );
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
    
    /**
     * Verify that all panels
     * are either enabled or disabled
     * as indicate by the given value.
     * 
     * @param exp   the given value
     */
    private void testAbled( boolean exp )
    {
        assertEquals( exp, testGUI.isEnabled( NamePanel.class ) );
        assertEquals( exp, testGUI.isEnabled( VariablePanel.class ) );
        assertEquals( exp, testGUI.isEnabled( ParameterPanel.class ) );
        assertEquals( exp, testGUI.isEnabled( PlotPanel.class ) );
    }
}
