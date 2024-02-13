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
public class GraphPropertySetLM extends GraphPropertySet
{    
    /**
     * Constructor.
     * Configures the super class
     * with the specific property names
     * for the Cartesian plane main window.
     */
    public GraphPropertySetLM()
    {
        super(
            CPConstants.MARGIN_LEFT_WIDTH_PN,
            CPConstants.MARGIN_LEFT_BG_COLOR_PN,
            CPConstants.MARGIN_LEFT_FONT_COLOR_PN,
            CPConstants.MARGIN_LEFT_FONT_NAME_PN,
            CPConstants.MARGIN_LEFT_FONT_SIZE_PN,
            CPConstants.MARGIN_LEFT_FONT_STYLE_PN,
            CPConstants.MARGIN_LEFT_FONT_DRAW_PN
        );
    }
}
