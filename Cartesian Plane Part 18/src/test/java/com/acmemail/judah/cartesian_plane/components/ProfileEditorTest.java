package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileEditorTestGUI;

class ProfileEditorTest
{
    private static Profile                 profile     = new Profile();
    private static ProfileEditorTestGUI    testGUI;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        testGUI = ProfileEditorTestGUI.getTestGUI( profile );
    }

    @BeforeEach
    void setUp() throws Exception
    {
    }

    @Test
    void testProfileEditor()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetFeedBack()
    {
        fail("Not yet implemented");
    }

    @Test
    void testApply()
    {
        fail("Not yet implemented");
    }

    @Test
    void testReset()
    {
        fail("Not yet implemented");
    }

}
