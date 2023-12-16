package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.image.BufferedImage;

public class MakeRaster
{
    private static final int            red     = 0x00FF0000;
    private static final int            blue    = 0x000000FF;
    private static final int            rows    = 100;
    private static final int            cols    = 200;
    private static final BufferedImage  raster  = 
        new BufferedImage( cols, rows, BufferedImage.TYPE_INT_ARGB );
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static BufferedImage get()
    {
        // first two rows
        paintRows( 0, 10, red );
        // last two rows
        paintRows( rows - 10, rows, red );
        // In-between rows
        paintRows( 10, rows - 10, blue );
        // first two columns
        paintCols( 0, 10, red );
        // last two columns
        paintCols( cols - 2, cols, red );
        return raster;
    }
    
    private static void paintRows( int row1, int row2, int color )
    {
        for ( int yco = row1 ; yco < row2 ; ++yco )
            for ( int xco = 0 ; xco < cols ; ++xco )
                raster.setRGB( xco, yco, color );
    }
    
    private static void paintCols( int col1, int col2, int color )
    {
        for ( int xco = col1 ; xco < col2 ; ++xco )
            for ( int yco = 0 ; yco < rows ; ++yco )
                raster.setRGB( xco, yco, color );
    }
}
