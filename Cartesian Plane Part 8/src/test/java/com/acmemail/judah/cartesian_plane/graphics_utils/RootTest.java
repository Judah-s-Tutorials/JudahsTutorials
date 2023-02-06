package com.acmemail.judah.cartesian_plane.graphics_utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

import util.TestUtils;

class RootTest
{
    @Test
    void test()
    {
        Root root   = new Root( new JPanel() );
        assertFalse( root.isStarted() );
        root.start();
        TestUtils.pause( 2000 );
        assertTrue( root.isStarted() );
    }
}
