package com.acmemail.judah.cartesian_plane.test_sutes;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.acmemail.judah.cartesian_plane.components.CPMenuBarDMTest;
import com.acmemail.judah.cartesian_plane.components.CPMenuBarNoCPFrameTest;
import com.acmemail.judah.cartesian_plane.components.CPMenuBarTest;

@Suite
@SuiteDisplayName( "CPMenuBar Test Suite" )
@SelectClasses({ 
    CPMenuBarTest.class, 
    CPMenuBarDMTest.class, 
    CPMenuBarNoCPFrameTest.class
})
public class CPMenuBarTestSuite
{
}
