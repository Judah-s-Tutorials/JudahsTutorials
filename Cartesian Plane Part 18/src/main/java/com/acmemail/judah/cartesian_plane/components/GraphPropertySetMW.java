package com.acmemail.judah.cartesian_plane.components;

import com.acmemail.judah.cartesian_plane.CPConstants;

/**
 * Configures the superclass
 * as a container for values
 * reflecting the graph properties
 * of the Cartesian plane main window.
 * 
 * @author Jack Straub
 */
public class GraphPropertySetMW extends GraphPropertySet
{    
    /**
     * Constructor.
     * Configures the super class
     * with the specific property names
     * for the Cartesian plane main window.
     */
    public GraphPropertySetMW()
    {
        super(
            CPConstants.MW_WIDTH_PN,
            CPConstants.MW_BG_COLOR_PN,
            CPConstants.MW_FONT_COLOR_PN,
            CPConstants.MW_FONT_NAME_PN,
            CPConstants.MW_FONT_SIZE_PN,
            CPConstants.MW_FONT_STYLE_PN,
            CPConstants.MW_FONT_DRAW_PN
        );
    }
}
