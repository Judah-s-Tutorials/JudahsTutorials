package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.test_utils.CPMenuBarTestDialog;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class CPMenuBarTest
{

    @AfterEach
    void tearDown() throws Exception
    {
    }

    @Test
    void test()
    {
        CPMenuBarTestDialog.getTestDialog();
        Utils.pause( 3000 );
    }

}
