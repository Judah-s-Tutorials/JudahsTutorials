package com.acmemail.judah.cartesian_plane.test_sutes;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.acmemail.judah.cartesian_plane.components.GraphPropertiesPanelFuncTest;
import com.acmemail.judah.cartesian_plane.components.GraphPropertiesPanelVisualTest;


@Suite
@SuiteDisplayName( "GraphPropertiesPanel Test Suite" )
@SelectClasses({ 
    GraphPropertiesPanelVisualTest.class,
    GraphPropertiesPanelFuncTest.class 
})
class GraphPropertiesPanelTestSuite
{
}
