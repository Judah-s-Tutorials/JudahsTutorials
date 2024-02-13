package com.acmemail.judah.cartesian_plane.components;

import com.acmemail.judah.cartesian_plane.CPConstants;

/**
 * This class
 * supplies the LinePropertySet superclass
 * with the configuration
 * necessary to manage
 * major tic properties.
 * 
 * @author Jack Straub
 * 
 * @see LinePropertySet
 * @see CPConstants
 * @see com.acmemail.judah.cartesian_plane.PropertyManager
 */
public class LinePropertySetTicMajor extends LinePropertySet
{
    public LinePropertySetTicMajor()
    {
        super( 
            CPConstants.TIC_MAJOR_DRAW_PN,
            CPConstants.TIC_MAJOR_WEIGHT_PN,
            CPConstants.TIC_MAJOR_LEN_PN,
            CPConstants.TIC_MAJOR_MPU_PN,
            CPConstants.TIC_MAJOR_COLOR_PN
        );
    }
}
