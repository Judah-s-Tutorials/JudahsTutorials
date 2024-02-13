package com.acmemail.judah.cartesian_plane.components;

import com.acmemail.judah.cartesian_plane.CPConstants;

class LinePropertySetGridLinesTest extends LinePropertySetTest
{
    public LinePropertySetGridLinesTest()
    {
        super( 
            CPConstants.GRID_LINE_DRAW_PN,
            CPConstants.GRID_LINE_WEIGHT_PN,
            "", // grid lines don't have a length property
            CPConstants.GRID_LINE_LPU_PN,
            CPConstants.GRID_LINE_COLOR_PN,
            () -> new LinePropertySetGridLines()
        );
    }
}
