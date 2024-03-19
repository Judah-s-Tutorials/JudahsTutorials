package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.PlotPanelTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class PlotPanelTest
{
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
    void setUp() throws Exception
    {
        testGUI.newEquation();
    }

    @Test
    void test()
    {
        testGUI.type( "x=", 'A' );
        IntStream.range( 'B', 'E' ).forEach( testGUI::type );
        Utils.pause( 2000 );
    }

}
