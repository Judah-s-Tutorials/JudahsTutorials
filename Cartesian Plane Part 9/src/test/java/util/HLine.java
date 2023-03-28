package util;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * An instance of this class
 * encapsulates a horizontal line
 * with a given starting point, length and width.
 * Pixels constituting the line
 * always follow to the right
 * of the starting point.
 * Note that the width of the line
 * is defined by the number of rows of pixels
 * that constitute the line.
 * The purpose of this class
 * is to determine pixels
 * that can be reliably tested
 * as internal or external to the line.
 * The basic premise of this determination
 * is that the endpoints of the line
 * may or may not be drawn
 * when rendering the line;
 * we can, however,
 * predict first and last pixels
 * that can be within or without
 * the bounds of the line.
 * <p>
 * In the following figure
 * <em>x</em> designates a pixel
 * that can be reliably assumed
 * to be part of the encapsulated line;
 * <em>o</em> designates a pixel
 * that can be reliably assumed
 * to be outside the encapsulated line;
 * and <i> designates a pixel
 * that cannot be assumed
 * to be inside or outside the line.
 * <p>
 * Predicting the inner and outer bounds of a line.
 * <pre>
 *   90123456789012345678901
 *   oio                 oio
 *   oix-----------------xio
 *   oio                 oio
 *   Given line of: 
 *       width = 1
 *       start = (10, 10)
 *       length = 21
 *   Predicted interior points (x):
 *       (11, 10), (19, 10)
 *   Predicted exterior points (o):
 *       ( 9,  9),          (10,  9) ... (19, 9),          (21, 9)
 *       ( 9, 10)                    ...                   (21,10)
 *       ( 9, 11),          (10, 11) ... (19, 11),         (21,11)
 *   Not examined (i):
 *                (10,  9)           ...          (20,  9)
 *                (10, 10),          ...          (20, 10)
 *                (10, 11)           ...          (20, 11)
 *                
 *   90123456789012345678901
 *   oio                 oio
 *   oix-----------------xio
 *   oix-----------------xio
 *   oix-----------------xio
 *   oio                 oio
 *   Given line of: 
 *       width = 3
 *       start = (10, 10)
 *       length = 21
 * </pre>
 *
 * @author Jack Straub
 *
 */
public class HLine extends TLine
{
    /**
     * Formulates a horizontal line with a given 
     * starting point, length and width (stroke).
     * The widths indicate the number of rows of pixels
     * that constitute the line.
     * <p>
     * In order to be able to accurately predict
     * the bounds of the line after applying 
     * a stroke with the given width,
     * it is necessary that the width
     * be an odd number.
     * 
     * @param xco       x-coordinate of start of line
     * @param yco       y-coordinate of start of line
     * @param length    length of line
     * @param width     width of line
     */
    public HLine( int xco, int yco, int length, int width )
    {
        super( xco, yco, xco + length - 1, yco, width );
    }
    
    @Override
    public List<Point> getInteriorTestPoints()
    {
        List<Point> list        = new ArrayList<>();
        int         halfWidth   = getWidth() / 2;
        Line2D      line        = getLine();
        
        // base row of pixels constituting line
        int         centerYco   = (int)line.getY1();
        // first row of pixels constituting line
        // after applying stroke.
        int         firstYco    = (int)(centerYco - halfWidth);
        // last row of pixels constituting line
        // after applying stroke.
        int         lastYco     = centerYco + halfWidth;
        System.out.println( centerYco + "," + firstYco + "," + lastYco );
        
        // first x-coordinate constituting line before applying stroke;
        // may or may not be part of line after applying stroke
        int         xco1        = (int)line.getX1();
        // last x-coordinate constituting line before applying stroke;
        // may or may not be part of line after applying stroke
        int         xco2        = (int)line.getX2();
        
        for ( int yco = firstYco ; yco <= lastYco ; ++yco )
        {
            // xco1 and/or xco2 may or may not be interior
            list.add( new Point( xco1 + 1, yco ) );
            list.add( new Point( xco2 - 1, yco ) );
        }
        
        return list;
    }
    
    @Override
    public List<Point> getExteriorTestPoints()
    {
        List<Point> list        = new ArrayList<>();
        Line2D      line        = getLine();
        
        // base row of pixels constituting line
        int         centerYco   = (int)line.getY1();
        // number of rows constituting line above the base (center) row
        int         halfWidth   = getWidth() / 2;
        // first row of pixels above the line 
        // after applying stroke
        int         firstYco    = (int)(centerYco - halfWidth - 1);
        // first row of pixels below the line 
        // after applying stroke
        int         lastYco     = centerYco + halfWidth + 1;
        
        // first x-coordinate constituting line before applying stroke;
        // may or may not be part of line after applying stroke
        int         xco1        = (int)line.getX1();
        // last x-coordinate constituting line before applying stroke;
        // may or may not be part of line after applying stroke
        int         xco2        = (int)line.getX2();
        
        for ( int yco = firstYco ; yco <= lastYco ; ++yco )
        {
            list.add( new Point( xco1 - 1, yco ) );
            list.add( new Point( xco2 + 1, yco ) );
        }
        
        list.add( new Point( xco1 + 1, firstYco ) );
        list.add( new Point( xco1 + 1, lastYco ) );
        list.add( new Point( xco2 - 1, firstYco ) );
        list.add( new Point( xco2 - 1, lastYco ) );
        
        return list;
    }
    
    @Override
    public String toString()
    {
        String  str = toString( "HLine" );
        return str;
    }
}
