package com.acmemail.judah.cartesian_plane.sandbox;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.swing.JFrame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import util.TestUtils;

/**
 * This class illustrates the use of
 * <em>before-each</em> 
 * and <em>after-each</em> unit test methods.
 * 
 * @author Jack Straub
 *
 */
class AfterEachDemo
{
    private CartesianPlane  defCartesianPlane;
    private Root            defRoot;
    
    @BeforeEach
    void setUp() throws Exception
    {
        defCartesianPlane = new CartesianPlane();
        defRoot = new Root( defCartesianPlane );
        defRoot.start();
    }
    
    // ...

    @AfterEach
    void test()
    {
        JFrame  rootFrame   = TestUtils.getRootFrame();
        assertNotNull( rootFrame );
        rootFrame.dispose();
    }
}
