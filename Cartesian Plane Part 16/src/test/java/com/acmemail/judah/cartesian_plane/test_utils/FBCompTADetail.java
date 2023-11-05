package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * An instance of this class
 * stores date
 * to be used in relation
 * to testing
 * of feedback components,
 * such as LengthFeedback,
 * SpacingFeedback and StrokeFeedback.
 * The encapsulate data
 * consists of 
 * the value
 * to be applied to a property
 * (for example, 
 * length, spacing or stroke),
 * the weight to be applied
 * to the line segments
 * in the feedback components,
 * and a BufferedImage
 * containing the results
 * of rendering the associated
 * feedback component.
 * A weight value less than 0
 * indicates that the associated feedback component
 * does not have a weight property,
 * as is true
 * of the StrokeFeedback component,
 * or that the component
 * is configured 
 * with its default weight.
 * <p>
 * Note that 
 * this is a serializable class,
 * so a BufferedImage,
 * which is not serializable,
 * cannot be stored directly
 * in an instance
 * of this class.
 * Instead,
 * the image is represented
 * by its type
 * (such as BufferedImage.TYPE_INT_RGB)
 * and a raster,
 * a two-dimensional array
 * containing the contents
 * of the BufferedImage.
 * </p>
 *  
 * @author Jack Straub
 */
public class FBCompTADetail implements Serializable
{
    /** Generated Serial Version UID */
    private static final long serialVersionUID = 8492161342492933010L;
    
    /** The encapsulated property value. */
    private final double    propertyValue;
    /** The encapsulated weight. */
    private final double    weight;
    /** The type of the encapsulated BufferedImage. */
    private final int       imageType;
    /** The content of the encapsulated BufferedImage. */
    private final int[][]   raster;
    
    /**
     * Constructor.
     * Establishes the values
     * of an objects fields.
     * 
     * @param propertyValue value of the property value field
     * @param weight        value of the weight field
     * @param image         the BufferedImage to encapsulate
     */
    public FBCompTADetail(
        double propertyValue, 
        double weight,
        BufferedImage image
    )
    {
        super();
        this.propertyValue = propertyValue;
        this.weight = weight;
        this.imageType = image.getType();
        
        int cols    = image.getWidth();
        int rows    = image.getHeight();
        this.raster = new int[rows][cols];        
        for ( int row = 0 ; row < rows ; ++row )
            for ( int col = 0 ; col < cols ; ++col )
                raster[row][col] = image.getRGB( col, row );
    }

    /**
     * Gets the encapsulated property value.
     * 
     * @return the property value
     */
    public double getPropertyValue()
    {
        return propertyValue;
    }

    /**
     * Gets the encapsulated weight.
     * 
     * @return the weight
     */
    public double getWeight()
    {
        return weight;
    }
    
    /**
     * Gets the encapsulated BufferedImage.
     * 
     * @return  the encapsulated BufferedImage
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
}
