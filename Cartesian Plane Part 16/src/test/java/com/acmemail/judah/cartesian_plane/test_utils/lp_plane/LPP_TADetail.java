package com.acmemail.judah.cartesian_plane.test_utils.lp_plane;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import com.acmemail.judah.cartesian_plane.components.LinePropertySet;

public class LPP_TADetail implements Serializable
{
    private static final long serialVersionUID = 6319143056478680248L;
    
    private final Class<? extends LinePropertySet>  lppType;
    private final int                               imageType;
    private final int[][]                           raster;
    
    public LPP_TADetail(
        Class<? extends LinePropertySet>  clazz,
        BufferedImage image
    )
    {
        this.lppType = clazz;
        imageType = image.getType();
        raster = getRaster( image );
    }
    
    public Class<? extends LinePropertySet> getLPPType()
    {
        return lppType;
    }
    
    public BufferedImage getBufferedImage()
    {
        int             cols    = raster[0].length;
        int             rows    = raster.length;
        BufferedImage   image   = 
            new BufferedImage( cols, rows, imageType );
        for ( int yco = 0 ; yco < rows ; ++yco )
            for ( int xco = 0 ; xco < cols ; ++xco )
            {
                int rgb = raster[yco][xco];
                image.setRGB( xco, yco, rgb );
            }
        return image;
    }
    
    private static int[][] getRaster( BufferedImage image )
    {
        int             cols    = image.getWidth();
        int             rows    = image.getHeight();
        int[][]         raster  = new int[rows][cols];        
        for ( int row = 0 ; row < rows ; ++row )
            for ( int col = 0 ; col < cols ; ++col )
                raster[row][col] = image.getRGB( col, row );
        return raster;
    }
}