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
public class LinePropertySetAxes extends LinePropertySet
{
    public LinePropertySetAxes()
    {
        super( 
            "", // axes don't have a draw property
            CPConstants.AXIS_WEIGHT_PN,
            "", // axes don't have a length
            "", // axes don't have a spacing property
            CPConstants.AXIS_COLOR_PN
        );
    }
}
