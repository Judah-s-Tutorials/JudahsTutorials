package com.acmemail.judah.cartesian_plane.components;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;
import com.acmemail.judah.cartesian_plane.test_utils.VariablePanelTestGUI;

class VariablePanelTest
{
    private static VariablePanelTestGUI testGUI;

    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> 
            testGUI = new VariablePanelTestGUI()
        );
    }

    @BeforeEach
    void setUp() throws Exception
    {
        testGUI.newEquation();
    }
    

    @Test
    void test()
    {
        int row = testGUI.getRowOf( "b" );
        testGUI.editValue( row, "222", KeyEvent.VK_ENTER );
        System.out.println( testGUI.getEquationValue( "b" ) );
        Utils.pause( 3000 );
    }

}
