package com.acmemail.judah.cartesian_plane.components;

import com.acmemail.judah.cartesian_plane.CPConstants;

public class LinePropertySetTicMajorTest extends LinePropertySetTest
{
    public LinePropertySetTicMajorTest()
    {
        super( 
            CPConstants.TIC_MAJOR_DRAW_PN,
            CPConstants.TIC_MAJOR_WEIGHT_PN,
            CPConstants.TIC_MAJOR_LEN_PN,
            CPConstants.TIC_MAJOR_MPU_PN,
            CPConstants.TIC_MAJOR_COLOR_PN,
            () -> new LinePropertySetTicMajor()
        );
    }
}
