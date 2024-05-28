package com.acmemail.judah.cartesian_plane.test_utils.gp_panel;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;

/**
 * An object of this class
 * is used to save details
 * related to data for testing
 * the GraphicPropertiesPanel class.
 * 
 * @author Jack Straub
 */
public class GPP_TADetail implements Serializable
{
    /** Generated serial version UID. */
    private static final long serialVersionUID = 6319143056478680248L;
    
    /**
     *  The specific property class from which data was extracted,
     *  e.g. GraphPropertySetMW, GraphPropertySetTM, etc.
     */
    private final Class<? extends GraphPropertySet> gppType;
    /** Type of image being saved, e.g. BUFFERED_IMAGE.TYPE_INT_ARGB. */
    private final int                               imageType;
    /** Array of pixel values for image being saved. */
    private final int[][]                           raster;
    
    /**
     * Constructor.
     * Fully initializes all fields
     * for an object of this class.
     * 
     * @param clazz the specific property class of data being monitored
     * @param image image to be saved
     */
    public GPP_TADetail(
        Class<? extends GraphPropertySet>  clazz,
        BufferedImage image
    )
    {
        this.gppType = clazz;
        imageType = image.getType();
        raster = getRaster( image );
    }
    
    /**
     * Returns the type of class being stored.
     * For example, GraphPropertySetMW, GraphPropertySetTM, etc.
     * 
     * @return  class of data being stored
     */
    public Class<? extends GraphPropertySet> getGPPType()
    {
        return gppType;
    }
    
    /**
     * Gets the image being stored
     * as part of the test data.
     * 
     * @return  the image being stored
     */
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
    
    /**
     * Parses the color values
     * from a given BufferedImage
     * into a 2-dimensional array of integers.
     * 
     * @param image the given BufferedImage.
     * 
     * @return  the 2-dimensional array of integers
     */
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
