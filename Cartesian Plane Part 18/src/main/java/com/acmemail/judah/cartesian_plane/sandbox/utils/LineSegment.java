package com.acmemail.judah.cartesian_plane.sandbox.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Encapsulates a line segment in a given BufferedImage.
 * The user provides the BufferedImage
 * and a point in the image.
 * The line segment is defined
 * as the rectangle containing the point
 * in which every point
 * is the same color.
 * 
 * @author Jack Straub
 */
public class LineSegment
{
    /** The point provided by the user. */
    private final Point         origin;
    /** The BufferedImage provided by the user. */
    private final BufferedImage image;
    /** the width of the given image. */
    private final int           imageWidth;
    /** the height of the given image. */
    private final int           imageHeight;
    /** The color of the given point (origin). */
    private final int           rgb;
    /** The bounding rectangle. */
    private final Rectangle     rect;
    
    /**
     * Constructor.
     * Calculates the bounding rectangle 
     * of the line segment containing the point.
     * 
     * @param origin
     *      the point from which to determine the bounding rectangle
     * @param image the image containing the given point.
     */
    public LineSegment( Point origin, BufferedImage image )
    {
        this.origin = new Point( origin.x, origin.y );
        this.image = image;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        rgb = image.getRGB( origin.x, origin.y ) & 0xffffff;
        
        int     left      = getLeft();
        int     right     = getRight();
        int     top       = getTop();
        int     bottom    = getBottom();
        int     width   = right - left + 1;
        int     height  = bottom - top + 1;
        rect = new Rectangle( left, top, width, height );
    }
    
    /**
     * Gets the bounding rectangle
     * of the encapsulated line segment.
     * 
     * @return  the bounding rectangle of the encapsulated line segment
     */
    public Rectangle getBounds()
    {
        return rect;
    }
    
    /**
     * Returns a string 
     * describing the color
     * and bounding rectangle
     * of the encapsulated line segment.
     * 
     * @return a string describing the encapsulated line segment
     */
    @Override
    public String toString()
    {
        String          strColor    = 
            String.format( "0x%06x", rgb );
        StringBuilder   bldr        = new StringBuilder();
        bldr.append( "x=" ).append( rect.x ).append( ", " );
        bldr.append( "y=" ).append( rect.y ).append( ", " );
        bldr.append( "width=" ).append( rect.width ).append( ", " );
        bldr.append( "height=" ).append( rect.height ).append( ", " );
        bldr.append( "xcolor=" ).append( strColor );
        return bldr.toString();
    }
    
    /**
     * Determines the left edge of the bounding rectangle.
     * 
     * @return  the left edge of the bounding rectangle
     */
    private int getLeft()
    {
        int     xco     = origin.x;
        int     yco     = origin.y;
        int     testRGB = getRGB( xco - 1, yco );
        while ( xco > 0 && testRGB == rgb )
        {
            --xco;
            testRGB = getRGB( xco - 1, yco ) & 0xFFFFFF;
        }
        return xco;
    }
    
    /**
     * Determines the right edge of the bounding rectangle.
     * 
     * @return  the right edge of the bounding rectangle
     */
    /**
     * @return
     */
    /**
     * @return
     */
    private int getRight()
    {
        int     xco     = origin.x;
        int     yco     = origin.y;
        int     testRGB = getRGB( xco + 1, yco );
        while ( xco < imageWidth && testRGB == rgb )
        {
            ++xco;
            testRGB = getRGB( xco + 1, yco );
        }
        return xco;
    }
    
    /**
     * Determines the top edge of the bounding rectangle.
     * 
     * @return  the top edge of the bounding rectangle
     */
    private int getTop()
    {
        int     xco     = origin.x;
        int     yco     = origin.y;
        int     testRGB = getRGB( xco, yco - 1 );
        while ( yco > 0 && testRGB == rgb )
        {
            --yco;
            testRGB = getRGB( xco, yco - 1 );
        }
        return yco;
    }
    
    /**
     * Determines the bottom edge of the bounding rectangle.
     * 
     * @return  the bottom edge of the bounding rectangle
     */
    private int getBottom()
    {
        int     xco     = origin.x;
        int     yco     = origin.y;
        int     testRGB = getRGB( xco, yco + 1 );
        while ( yco < imageHeight && testRGB == rgb )
        {
            ++yco;
            testRGB = getRGB( xco, yco + 1 );
        }
        return yco;
    }
    
    /**
     * Get the RGB value of the point in the BufferedImage
     * at the given x- and y-coordinates.
     * If the coordinates are out of bounds
     * -1 is returned.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @return
     *      the RGB value at the coordinates
     *      or -1 if the coordinates are out of bounds
     */
    private int getRGB( int xco, int yco )
    {
        int rgb = -1;
        if ( 
            xco >= 0 
            && xco < imageWidth 
            && yco >= 0 
            && yco < imageHeight
        )
            rgb = image.getRGB( xco, yco ) & 0xFFFFFF;
        return rgb;
    }
}
