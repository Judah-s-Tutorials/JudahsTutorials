package com.acmemail.judah.cartesian_plane.test_suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.acmemail.judah.cartesian_plane.components.ProfileEditorDialogTest;
import com.acmemail.judah.cartesian_plane.components.ProfileEditorFeedbackTest;
import com.acmemail.judah.cartesian_plane.components.ProfileEditorTest;

@Suite
@SuiteDisplayName( "LinePropertySet Test Suite" )
@SelectClasses({ 
    ProfileEditorTest.class,
    ProfileEditorFeedbackTest.class,
    ProfileEditorDialogTest.class
})
class ProfilEditorTestSuite
{
}
