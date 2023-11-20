package com.acmemail.judah.cartesian_plane.components;

import com.acmemail.judah.cartesian_plane.CPConstants;

class LinePropertySetTicMinorTest extends LinePropertySetTest
{
    public LinePropertySetTicMinorTest()
    {
        super( 
            CPConstants.TIC_MINOR_DRAW_PN,
            CPConstants.TIC_MINOR_WEIGHT_PN,
            CPConstants.TIC_MINOR_LEN_PN,
            CPConstants.TIC_MINOR_MPU_PN,
            CPConstants.TIC_MINOR_COLOR_PN,
            () -> new LinePropertySetTicMinor()
        );
    }
}
