package com.acmemail.judah.cartesian_plane.components;

import org.junit.jupiter.api.BeforeAll;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

class LengthFeedbackTest extends FeedbackTest
{
    @BeforeAll
    public static void beforeClass()
    {
        FeedbackTest.getFiles( "Length" );
    }
    
    public LengthFeedbackTest()
    {
//        super( s -> new LengthFeedback( s ) );
        GUIUtils.schedEDTAndWait( () -> 
            FeedbackTest.makeGUI( "Length", s -> new LengthFeedback( s ) ) );
    }
}
