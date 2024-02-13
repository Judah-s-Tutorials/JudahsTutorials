package com.acmemail.judah.cartesian_plane.components;

import com.acmemail.judah.cartesian_plane.CPConstants;

/**
 * This class
 * supplies the LinePropertySet superclass
 * with the configuration
 * necessary to manage
 * grid line properties.
 * 
 * @author Jack Straub
 * 
 * @see LinePropertySet
 * @see CPConstants
 * @see com.acmemail.judah.cartesian_plane.PropertyManager
 */
public class LinePropertySetGridLines extends LinePropertySet
{
    public LinePropertySetGridLines()
    {
        super( 
            CPConstants.GRID_LINE_DRAW_PN,
            CPConstants.GRID_LINE_WEIGHT_PN,
            "", // grid lines don't have a length
            CPConstants.GRID_LINE_LPU_PN,
            CPConstants.GRID_LINE_COLOR_PN
        );
    }
}
