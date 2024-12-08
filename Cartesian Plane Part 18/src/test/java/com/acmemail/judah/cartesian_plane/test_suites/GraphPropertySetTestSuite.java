package com.acmemail.judah.cartesian_plane.test_suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySetBMTest;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetLMTest;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMWTest;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetRMTest;

@Suite
@SuiteDisplayName( "LinePropertySet Test Suite" )
@SelectClasses({ 
    GraphPropertySetBMTest.class,
    GraphPropertySetLMTest.class,
    GraphPropertySetMWTest.class,
    GraphPropertySetRMTest.class
})
class GraphPropertySetTestSuite
{
}
