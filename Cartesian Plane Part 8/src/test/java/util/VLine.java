package util;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * An instance of this class
 * encapsulates a vertical line
 * with a given starting point, length and width.
 * Pixels constituting the line
 * always extend below
 * the starting point.
 * Note that the width of the line
 * is defined by the number of columns of pixels
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
 *    Example 1:
 *     9 ooo
 *     0 iii
 *     1 oxo
 *     2
 *     3
 *     4
 *     5
 *     6
 *     7
 *     8
 *     9 oxo
 *     0 iii
 *     1 ooo
 *   Given line of: 
 *       width = 1
 *       start = (10, 10)
 *       length = 11
 *   Predicted interior points (x):
 *       (10, 11), (10, 19)
 *   Predicted exterior points (o):
 *       ( 9,  9), (10,  9), (11,  9) 
 *       
 *       ( 9, 11),           (11, 11)
 *           ... 
 *       ( 9, 19),           (11, 19)
 *       
 *       ( 9, 21), (10, 21)  (11, 21) 
 *   Explicitly omitted (i):
 *       ( 9, 10), (10, 10), (11, 10) 
 *                 (10, 11)
 *           ... 
 *                 (10, 20)
 *       ( 9, 21), (10, 21), (11, 21) 
 *                
 *    Example 1:
 *     9 ooooo
 *     0 iiiii
 *     1 oxxxo
 *     2
 *     3
 *     4
 *     5
 *     6
 *     7
 *     8
 *     9 oxxxo
 *     0 iiiii
 *     1 ooooo
 *   Given line of: 
 *       width = 3
 *       start = (10, 10)
 *       length = 11
 *
 * @author Jack Straub
 *
 */
public class VLine extends TLine
{
    /**
     * Formulates a vertical line with a given 
     * starting point, length and width (stroke).
     * The width indicates the number of columns of pixels
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
    public VLine( int xco, int yco, int length, int width )
    {
        super( xco, yco, xco, yco + length - 1, width );
    }
    
    @Override
    public List<Point> getInteriorTestPoints()
    {
        List<Point> list        = new ArrayList<>();
        int         halfWidth   = getWidth() / 2;
        Line2D      line        = getLine();
        
        // base column of pixels constituting line
        int         centerXco   = (int)line.getX1();
        // first column of pixels constituting line
        // after applying stroke.
        int         firstXco    = (int)(centerXco - halfWidth);
        // last column of pixels constituting line
        // after applying stroke.
        int         lastXco     = centerXco + halfWidth;
        
        // first y-coordinate constituting line before applying stroke;
        // may or may not be part of line after applying stroke
        int         yco1        = (int)line.getY1();
        // last y-coordinate constituting line before applying stroke;
        // may or may not be part of line after applying stroke
        int         yco2        = (int)line.getY2();
        
        for ( int xco = firstXco ; xco <= lastXco ; ++xco )
        {
            // yco1 and/or yco2 may or may not be interior
            for ( int yco = yco1 + 1 ; yco < yco2 ; ++yco )
                list.add( new Point( xco, yco ) );
        }
        
        return list;
    }
    
    @Override
    public List<Point> getExteriorTestPoints()
    {
        List<Point> list        = new ArrayList<>();
        Line2D      line        = getLine();
        
        // number of columns constituting line left/right
        // of the base (center) column
        int         halfWidth   = getWidth() / 2;
        // base column of pixels constituting line
        int         centerXco   = (int)line.getX1();
        // first column of pixels left of the line 
        // after applying stroke
        int         firstXco    = (int)(centerXco - halfWidth);
        // first row of pixels right of the line 
        // after applying stroke
        int         lastXco     = centerXco + halfWidth;
        
        // first y-coordinate constituting line before applying stroke;
        // may or may not be part of line after applying stroke
        int         yco1        = (int)line.getY1();
        // last y-coordinate constituting line before applying stroke;
        // may or may not be part of line after applying stroke
        int         yco2        = (int)line.getY2();
        
        for ( int xco = firstXco - 1 ; xco <= lastXco + 1 ; ++xco )
        {
            list.add( new Point( xco, yco1 - 1) );
            list.add( new Point( xco, yco2 + 1 ) );
        }
        
        list.add( new Point( firstXco, yco1 + 1 ) );
        list.add( new Point( lastXco, yco1 + 1 ) );
        list.add( new Point( firstXco, yco2 - 1 ) );
        list.add( new Point( lastXco, yco2 - 1 ) );
        
        return list;
    }
}
