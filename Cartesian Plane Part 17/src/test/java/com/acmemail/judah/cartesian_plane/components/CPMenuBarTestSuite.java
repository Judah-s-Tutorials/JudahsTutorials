package com.acmemail.judah.cartesian_plane.components;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

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
