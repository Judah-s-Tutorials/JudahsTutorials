package com.acmemail.judah.cartesian_plane.test_suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxesTest;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLinesTest;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetMiscTest;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajorTest;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinorTest;

@Suite
@SuiteDisplayName( "LinePropertySet Test Suite" )
@SelectClasses({ 
    LinePropertySetAxesTest.class,
    LinePropertySetGridLinesTest.class,
    LinePropertySetTicMajorTest.class,
    LinePropertySetTicMinorTest.class,
    LinePropertySetMiscTest.class
})
class GraphPropertySetTestSuite
{
}
