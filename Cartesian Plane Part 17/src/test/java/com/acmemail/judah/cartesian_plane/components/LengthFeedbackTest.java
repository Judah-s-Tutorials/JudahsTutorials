package com.acmemail.judah.cartesian_plane.components;

import org.junit.jupiter.api.BeforeAll;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.fb_comp.FBCompLengthTA;

public class LengthFeedbackTest extends FeedbackTest
{
    @BeforeAll
    public static void beforeAll()
    {
        GUIUtils.schedEDTAndWait( () -> 
            FeedbackTest.initAll( 
                FBCompLengthTA.SUBDIR, 
                s -> new LengthFeedback( s ) )
        );
    }
}
