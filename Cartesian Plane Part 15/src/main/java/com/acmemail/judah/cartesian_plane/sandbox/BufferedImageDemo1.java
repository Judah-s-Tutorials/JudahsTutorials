package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Creates a buffered image
 * of a given dimension.
 * The content of the image
 * is fairly arbitrary;
 * the whole point
 * of this exercise
 * is to show how
 * a buffered image
 * can be created
 * using the same tools
 * to draw in a window.
 * 
 * @author Jack Straub
 * 
 * @see BufferedImageShowDemo1
 */
public class BufferedImageDemo1
{
    /** Border of figure, in pixels. */
    private static final int    margin  = 10;
    /** Background color. */
    private static final Color  bgColor = Color.YELLOW;
    
    /** Width of figure, including margin. */
    private final int           width;
    /** Height of figure, including margin. */
    private final int           height;
    /** The generated buffered image. */
    private final BufferedImage image;
    /** 
     * Graphics context used to draw the figure; 
     * declared as a field for convenience.
     */
    private final Graphics2D    gtx;
    
    /**
     * Constructor.
     * Establishes the width and height
     * of the figure,
     * <em>not</em> including margins.
     * 
     * @param widthIn   width of figure
     * @param heightIn  height of figure
     */
    public BufferedImageDemo1( int widthIn, int heightIn )
    {   
        this.width = widthIn + 2 * margin;
        this.height = heightIn + 2 * margin;
        image = new BufferedImage( 
            width, 
            height, 
            BufferedImage.TYPE_INT_ARGB 
        );
        gtx = image.createGraphics();
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, width, height );
        
        gtx.setStroke( new BasicStroke( 5 ) );
        gtx.setColor( Color.BLUE );
        gtx.drawLine( margin, margin, width - margin, height - margin );
        gtx.drawLine( width - margin, margin, margin, height - margin );
        
        gtx.setStroke( new BasicStroke( 3 ) );
        gtx.setColor( Color.GREEN );
        gtx.drawLine( width / 2, margin, width / 2, height - margin );
        gtx.drawLine( margin, height / 2, width - margin, height / 2 );
        
        Rectangle   rect    = 
            new Rectangle( 0, 0, width / 3, height / 3 );
        centerRect( rect );
        gtx.setColor( Color.MAGENTA );
        gtx.fill( rect );
        
        Rectangle   oval    = 
            new Rectangle( 0, 0, width /  4, height /  4 );
        centerRect( oval );
        gtx.setColor( Color.ORANGE );
        gtx.fillOval( oval.x, oval.y, oval.width, oval.height );
        
        gtx.dispose();
    }
    
    /**
     * Returns the encapsulated buffered image.
     * 
     * @return  the encapsulated buffered image
     */
    public BufferedImage getImage()
    {
        return image;
    }
    
    /**
     * Given the width and height of a rectangle,
     * calculates the x- and y-coordinates
     * that will center the rectangle.
     * 
     * @param rect
     *      Input: contains the width and height of the rectangle;
     *      Output: updated with the calculated x- and y- coordinates
     */
    private void centerRect( Rectangle rect )
    {
        int     rWidth  = rect.width;
        int     rHeight = rect.height;
        int     cXco    = width / 2;
        int     cYco    = height / 2;

        int     xco     = cXco - rWidth / 2;
        int     yco     = cYco - rHeight / 2;
        rect.x = xco;
        rect.y = yco;
    }
}
