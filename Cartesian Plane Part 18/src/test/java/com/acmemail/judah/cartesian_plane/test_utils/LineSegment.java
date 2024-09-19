package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
/**
 * @author Jack Straub
 */
public class LineSegment
{
    /** The color of the given point (origin). */
    private final int           rgb;
    /** The bounding rectangle. */
    private final Rectangle2D   rect;
    
    /**
     * Constructs a LineSegment
     * derived  from the given starting point
     * and encapsulating image.
     * 
     * @param origin    the given starting point
     * @param image     the given image
     * 
     * @return LineSegment derived from the given origin and image
     */
    public static LineSegment of( Point2D origin, BufferedImage image )
    {
        LineSeg     seg     = new LineSeg( origin, image );
        LineSegment segment = new LineSegment( seg.rect, seg.rgb );
        return segment;
    }
    
    /**
     * Instantiates a LineSegment
     * encapsulating the given color and rectangle.
     *
     * @param rect  the given rectangle
     * @param rgb   the given color
     * 
     * @return  a LineSegment encapsulating the given color and rectangle
     */
    public static LineSegment of( Rectangle2D rect, int rgb )
    {
        LineSegment segment = new LineSegment( rect, rgb );
        return segment;
    }
    
    /**
     * Given a rectangle within an image
     * find the smallest rectangle
     * that encloses all points
     * of a given color.
     * For example, 
     * given the color 0xFF 
     * and the following rectangle:
     * <pre style="margin-left:2em">
     *      10 12 13 14 15 16 17 18 19 10
     *      -----------------------------
     * 100| 00 00 00 00 00 00 00 00 00 00
     * 101| 00 FF 00 00 00 00 FF FF 00 00
     * 102| 00 00 FF 00 00 FF 00 00 FF 00
     * 103| 00 00 FF 00 00 FF 00 00 FF 00
     * 104| 00 00 FF 00 00 FF 00 00 FF 00
     * 105| 00 FF FF FF FF 00 FF FF 00 00
     * 106| 00 00 00 00 00 00 00 00 00 00
     * 107| 00 00 00 00 00 00 00 00 00 00
     * 108| 00 00 00 00 00 00 00 00 00 00
     * 109| 00 00 00 00 00 00 00 00 00 00
     * 110| 00 00 00 00 00 00 00 00 00 00</pre>
     * <p>
     * Construct the rectangle x=12, y=101, width=8, and height=5.
     * 
     * @param rect  constraining rectangle
     * @param image given image
     * @param rgb   given color
     * 
     * @return  
     *      the smallest rectangle within the given constraints
     *      that encloses all pixels of the given color
     */
    public static LineSegment 
    ofRect( Rectangle2D rect, BufferedImage image, int rgb )
    {
        LineSeg seg =
            new LineSeg( 
                image,
                rgb,
                (int)rect.getMinX(),
                (int)rect.getMaxX(),
                (int)rect.getMinY(),
                (int)rect.getMaxY()
            );
        LineSegment lineSegment = new LineSegment( seg.rect, seg.rgb );
        return lineSegment;
    }
    
    /**
     * Create a LineSegment
     * from a given vertical line, stroke and color.
     * 
     * @param line      the given line
     * @param stroke    the given stroke
     * @param rgb       the given color
     * 
     * @return  the instantiated LineSegment
     */
    public static 
    LineSegment ofVertical( Line2D line, double stroke, int rgb )
    {
        double      lineXco     = line.getX1();
        double      lineYco     = line.getY1();
        double      length      = line.getY2() - lineYco;
        double      halfStroke  = stroke / 2;
        double      rectXco     = lineXco - halfStroke;
        double      rectYco     = lineYco;
        Rectangle2D rect        = 
            new Rectangle2D.Double( rectXco, rectYco, stroke, length );
        LineSegment seg         = new LineSegment( rect, rgb );
        return seg;
    }
    
    /**
     * Create a LineSegment
     * from a given horizontal line, stroke and color.
     * 
     * @param line      the given line
     * @param stroke    the given stroke
     * @param rgb       the given color
     * 
     * @return  the instantiated LineSegment
     */
    public static LineSegment 
    ofHorizontal( Line2D line, double stroke, int rgb )
    {
        double      lineXco     = line.getX1();
        double      lineYco     = line.getY1();
        double      length      = line.getX2() - lineXco;
        double      halfStroke  = stroke / 2;
        double      rectXco     = lineXco;
        double      rectYco     = lineYco - halfStroke;
        Rectangle2D rect        = 
            new Rectangle2D.Double( rectXco, rectYco, length, stroke );
        LineSegment seg         = new LineSegment( rect, rgb );
        return seg;
    }

    /**
     * Constructor.
     * Creates a LineSegment
     * encapsulating the given color and rectangle.
     *
     * @param rect  the given rectangle
     * @param rgb   the given color
     */
    private LineSegment( Rectangle2D rect, int rgb )
    {
        this.rect = rect;
        this.rgb = rgb;
    }
    
    /**
     * Gets the bounding rectangle
     * of the encapsulated line segment.
     * 
     * @return  the bounding rectangle of the encapsulated line segment
     */
    public Rectangle2D getBounds()
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
        bldr.append( "x=" ).append( rect.getX() ).append( ", " );
        bldr.append( "y=" ).append( rect.getY() ).append( ", " );
        bldr.append( "width=" ).append( rect.getWidth() ).append( ", " );
        bldr.append( "height=" ).append( rect.getHeight() ).append( ", " );
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
     * Derives the color and bounding rectangle of a given point.
     * For convenience,
     * this class declares many instance variables
     * which are useful to helper methods
     * in determining the encapsulated rectangle.
     * By encapsulating those here,
     * instead of the outer class,
     * we get the convenience of the of the transient variables
     * without adding them to the outer class's state.
     * 
     * @author Jack Straub
     */
    private static class LineSeg
    {
        /** The color of the given point (origin). */
        public final int            rgb;
        /** The bounding rectangle. */
        public final Rectangle      rect;
        
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
         * Derives the color and bounding rectangle
         * from the given starting point
         * and encapsulating image.
         * 
         * @param origin    the given origin
         * @param image     the given image
         */
        public LineSeg( Point2D origin, BufferedImage image )
        {
            this.image = image;
            this.originXco = (int)origin.getX();
            this.originYco = (int)origin.getY();
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
            rgb = image.getRGB( originXco, originYco ) & 0xffffff;
            
            int     left      = getLeft();
            int     right     = getRight();
            int     top       = getTop();
            int     bottom    = getBottom();
            int     width   = right - left + 1;
            int     height  = bottom - top + 1;
            rect = new Rectangle( left, top, width, height );
        }
        
        /**
         * 
         * In a given image
         * find the smallest rectangle
         * that encapsulates all pixels
         * of a given color
         * within the given constraints.
         * If necessary,
         * constraints will be adjusted
         * to be greater than or equal to 0,
         * and less than the width or height
         * of the given image.
         * 
         * @param image         the given image
         * @param rgb           the given color
         * @param leftLimit     the left constraint
         * @param rightLimit    the right constraint
         * @param topLimit      the top constraint
         * @param bottomLimit   the bottom constraint
         */
        public LineSeg( 
            BufferedImage image,
            int rgb,
            int leftLimit,
            int rightLimit,
            int topLimit,
            int bottomLimit 
        )
        {
            this.image = image;
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
            originXco = limitXco( leftLimit );
            originYco = limitYco( topLimit );
            this.rgb        = rgb;
            
            int xEnd        = limitXco( rightLimit );
            int yEnd        = limitYco( bottomLimit );
            
            // These four variables will ultimately 
            // describe the enclosing rectangle.
            int leftXco     = Integer.MAX_VALUE;
            int rightXco    = -1;
            int topYco      = Integer.MAX_VALUE;
            int bottomYco   = -1;
            
            for ( int xco = originXco ; xco <= xEnd ; ++xco )
                for ( int yco = originYco ; yco <= yEnd ; ++yco )
                {
                    int testRGB = image.getRGB( xco, yco ) & 0xffffff;
                    if ( testRGB == rgb )
                    {
                        if ( xco < leftXco )
                            leftXco = xco;
                        if ( xco > rightXco )
                            rightXco = xco;
                        if ( yco < topYco )
                            topYco = yco;
                        if ( yco > bottomYco )
                            bottomYco = yco;
                    }
                }
            if ( leftXco >= 0 )
            {
                int width   = rightXco - leftXco + 1;
                int height  = bottomYco - topYco + 1;
                rect = new Rectangle( leftXco, topYco, width, height );
            }
            else
                rect = null;
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
        
        /**
         * Makes sure that a given y-coordinate is sane.
         * If the input value is less than 0, the returned value
         * will be 0. 
         * If the input value is greater than the
         * height of the current image,
         * the returned value will be the height.
         * 
         * @param testLimit y-coordinate to test
         * 
         * @return  
         *      testLimit if within range of the bounds
         *      of the current image; 
         *      otherwise 0 or image height
         */
        private int limitYco( int testLimit )
        {
            int limit   = Math.min( testLimit, imageHeight );
            limit = Math.max( 0, limit );
            return limit;
            
        }
        
        /**
         * Makes sure that a given x-coordinate is sane.
         * If the input value is less than 0, the returned value
         * will be 0. 
         * If the input value is greater than the
         * width of the current image,
         * the returned value will be the width.
         * 
         * @param testLimit x-coordinate to test
         * 
         * @return  
         *      testLimit if within range of the bounds
         *      of the current image; 
         *      otherwise 0 or image width
         */
        private int limitXco( int testLimit )
        {
            int limit   = Math.min( testLimit, imageWidth );
            limit = Math.max( 0, limit );
            return limit;
            
        }
    }
}
