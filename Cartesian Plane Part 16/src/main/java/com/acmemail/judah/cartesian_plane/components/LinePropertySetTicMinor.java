package com.acmemail.judah.cartesian_plane.components;

import com.acmemail.judah.cartesian_plane.CPConstants;

/**
 * This class
 * supplies the LinePropertySet superclass
 * with the configuration
 * necessary to manage
 * minor tic properties.
 * 
 * @author Jack Straub
 * 
 * @see LinePropertySet
 * @see CPConstants
 * @see com.acmemail.judah.cartesian_plane.PropertyManager
 */
public class LinePropertySetTicMinor extends LinePropertySet
{
    public LinePropertySetTicMinor()
    {
        super( 
            CPConstants.TIC_MINOR_DRAW_PN,
            CPConstants.TIC_MINOR_WEIGHT_PN,
            CPConstants.TIC_MINOR_LEN_PN,
            CPConstants.TIC_MINOR_MPU_PN,
            CPConstants.TIC_MINOR_COLOR_PN
        );
    }
}
