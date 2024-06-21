package com.acmemail.judah.cartesian_plane.sandbox.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Encapsulates a line segment in a given BufferedImage.
 * The user provides the BufferedImage,
 * a point in the image,
 * and a color.
 * The line segment is defined
 * as the rectangle containing the point
 * in which every point
 * is the same color.
 * 
 * @author Jack Straub
 */
public class LineSegment
{
    /** The color of the given point (origin). */
    private final int           rgb;
    /** The bounding rectangle. */
    private final Rectangle     rect;
    
    /** X-coordintate of the point provided by the user. */
    private final int           originXco;
    /** Y-coordintate of the point provided by the user. */
    private final int           originYco;
    /** The BufferedImage provided by the user. */
    private final BufferedImage image;
    /** the width of the given image. */
    private final int           imageWidth;
    /** the height of the given image. */
    private final int           imageHeight;
    
    /**
     * Constructor.
     * Calculates the bounding rectangle 
     * of the line segment containing the given point.
     * 
     * @param origin
     *      the point from which to determine the bounding rectangle
     * @param image the image containing the given point.
     */
    public LineSegment( Point origin, BufferedImage image )
    {
        this.image = image;
        this.originXco = origin.x;
        this.originYco = origin.y;
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
     * Returns the color of the line segment.
     * 
     * @return  the color of the line segment
     */
    public int getColor()
    {
        return rgb;
    }
    
    /**
     * Returns true
     * if the bounding rectangle and color
     * of this object
     * are equal to 
     * the given rectangle and color.
     * 
     * @param rect  the given rectangle
     * @param rgb   the given color
     * 
     * @return  
     *      true if this object is equivalent to
     *      the given rectangle and color 
     */
    public boolean equivalentTo( Rectangle rect, int rgb )
    {
        boolean result  = this.rect.equals( rect ) && this.rgb == rgb;
        return result;
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
    
    @Override
    public int hashCode()
    {
        int hash    = Objects.hash( rgb, rect );
        return hash;
    }
    
    @Override
    public boolean equals( Object other )
    {
        boolean result  = false;
        if ( other == null )
            ;
        else if ( this == other )
            result = true;
        else if ( !(other instanceof LineSegment) )
            ;
        else
        {
            LineSegment that    = (LineSegment)other;
            if ( !this.rect.equals( that.rect ) )
                ;
            else if ( this.rgb != that.rgb )
                ;
            else
                result = true;
        }
        return result;
    }
    
    /**
     * Determines the left edge of the bounding rectangle.
     * 
     * @return  the left edge of the bounding rectangle
     */
    private int getLeft()
    {
        int     xco     = originXco;
        int     yco     = originYco;
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
    private int getRight()
    {
        int     xco     = originXco;
        int     yco     = originYco;
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
        int     xco     = originXco;
        int     yco     = originYco;
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
        int     xco     = originXco;
        int     yco     = originYco;
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
