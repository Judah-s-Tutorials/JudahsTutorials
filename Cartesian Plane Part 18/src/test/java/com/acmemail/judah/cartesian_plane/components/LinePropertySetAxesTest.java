package com.acmemail.judah.cartesian_plane.components;

import com.acmemail.judah.cartesian_plane.CPConstants;

public class LinePropertySetAxesTest extends LinePropertySetTest
{
    public LinePropertySetAxesTest()
    {
        super( 
            "", // axes don't have a draw property
            CPConstants.AXIS_WEIGHT_PN,
            "", // axes don't have a length property
            "", // axes don't have a spacing property
            CPConstants.AXIS_COLOR_PN,
            () -> new LinePropertySetAxes()
        );
    }
}
