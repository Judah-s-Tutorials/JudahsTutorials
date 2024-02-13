package com.acmemail.judah.cartesian_plane.components;

import com.acmemail.judah.cartesian_plane.CPConstants;

/**
 * Configures the superclass
 * as a container for values
 * reflecting the graph properties
 * of the Cartesian plane left margin.
 * 
 * @author Jack Straub
 */
public class GraphPropertySetBM extends GraphPropertySet
{    
    /**
     * Constructor.
     * Configures the super class
     * with the specific property names
     * for the Cartesian plane main window.
     */
    public GraphPropertySetBM()
    {
        super(
            CPConstants.MARGIN_BOTTOM_WIDTH_PN,
            CPConstants.MARGIN_BOTTOM_BG_COLOR_PN,
            CPConstants.MARGIN_BOTTOM_FONT_COLOR_PN,
            CPConstants.MARGIN_BOTTOM_FONT_NAME_PN,
            CPConstants.MARGIN_BOTTOM_FONT_SIZE_PN,
            CPConstants.MARGIN_BOTTOM_FONT_STYLE_PN,
            CPConstants.MARGIN_BOTTOM_FONT_DRAW_PN
        );
    }
}
