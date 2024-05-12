package com.acmemail.judah.cartesian_plane.components.test_suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.acmemail.judah.cartesian_plane.components.LinePropertiesPanelFuncTest;
import com.acmemail.judah.cartesian_plane.components.LinePropertiesPanelVisualTest;


@Suite
@SuiteDisplayName( "LinePropertiesPanel Test Suite" )
@SelectClasses({ 
    LinePropertiesPanelVisualTest.class,
    LinePropertiesPanelFuncTest.class
})
class LinePropertiesPanelTestSuite
{
}
