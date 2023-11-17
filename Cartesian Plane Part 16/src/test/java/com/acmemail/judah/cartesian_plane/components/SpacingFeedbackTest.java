package com.acmemail.judah.cartesian_plane.components;

import org.junit.jupiter.api.BeforeAll;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.fb_comp.FBCompSpacingTA;

class SpacingFeedbackTest extends FeedbackTest
{
    @BeforeAll
    public static void beforeAll()
    {
        GUIUtils.schedEDTAndWait( () -> 
            FeedbackTest.initAll( 
                FBCompSpacingTA.SUBDIR, 
                s -> new SpacingFeedback( s ) )
        );
    }
}
