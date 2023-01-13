package com.acmemail.judah.cartesian_plane.sandbox;

import static util.BaseCaseParameters.BASE_GRID_HEIGHT;
import static util.BaseCaseParameters.BASE_GRID_UNIT;
import static util.BaseCaseParameters.BASE_GRID_WIDTH;
import static util.BaseCaseParameters.BASE_LINES_PER_UNIT;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * Displays a grid representing the <em>base case</em>, 
 * as determined by the figures in {@linkplain util BaseCaseParameters}.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class BaseCase extends JPanel
{
    private static final float  marginTopWidth      =
        CPConstants.asFloat( CPConstants.MARGIN_TOP_WIDTH_DV );
    private static final float  marginRightWidth      =
        CPConstants.asFloat( CPConstants.MARGIN_RIGHT_WIDTH_DV );
    private static final float  marginBottomWidth    =
        CPConstants.asFloat( CPConstants.MARGIN_BOTTOM_WIDTH_DV );
    private static final float  marginLeftWidth      =
        CPConstants.asFloat( CPConstants.MARGIN_LEFT_WIDTH_DV );

    // Calculate the dimensions of the window needed if we want
    // the grid to be BASE_GRID_WIDTH x BASE_GRID_HEIGHT
    private static final int    windowWidth         = 
        (int)(BASE_GRID_WIDTH + marginLeftWidth + marginRightWidth);
    private static final int    windowHeight        = 
        (int)(BASE_GRID_HEIGHT + marginTopWidth + marginBottomWidth);
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        CartesianPlane  canvas  = new CartesianPlane( windowWidth, windowHeight );
        Root            root    = new Root( canvas );
        canvas.setGridUnit( BASE_GRID_UNIT );
        canvas.setGridLineLPU( BASE_LINES_PER_UNIT );
        root.start();
    }
}
