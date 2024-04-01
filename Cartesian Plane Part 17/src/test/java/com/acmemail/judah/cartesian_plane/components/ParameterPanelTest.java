package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.test_utils.ParameterPanelTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.PlotPanelTestGUI;

class ParameterPanelTest
{
    private static final PropertyManager    pmgr    =
        PropertyManager.INSTANCE;
    private static final char               PII     = '\u03c0';
    private static ParameterPanelTestGUI    testGUI;
    
    private Equation    currEquation;

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

    @Test
    void testParameterPanel()
    {
        new Scanner( System.in ).nextLine();
    }

    @Test
    void testLoad()
    {
        fail("Not yet implemented");
    }

}
