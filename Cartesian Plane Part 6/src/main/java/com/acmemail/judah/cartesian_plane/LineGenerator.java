package com.acmemail.judah.cartesian_plane;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An instance of this class
 * is used to generate the 
 * horizontal and/or vertical lines in a grid.
 * <div>
 * <img 
 *     src="doc-files/LineGeneratorFigure.png" 
 *     alt="LineGenerator Demo"
 *     style="float:right; width:15%; height:auto;"
 * >
 * Given a bounding rectangle for a grid,
 * one vertical line is always generated
 * at the horizontal center of the grid
 * (as though it were the y-axis
 * of a Cartesian plane).
 * A horizontal line is always generated
 * at the vertical center of the grid
 * (as though it were the x-axis
 * of a Cartesian plane).
 * <p>
 * The grid is divided into <em>units</em>
 * (the <em>gridUnit</em>),
 * where <em>1 unit = gridUnit pixels</em>.
 * The number of lines generated
 * is determined by a given 
 * number of lines per unit (the <em>lpu</em>).
 * The <em>gridUnit</em> and <em>lpu</em>
 * are provided by the user
 * in the constructor.
 * Users also specify,
 * via the constructor,
 * whether they want to generate
 * horizontal lines, vertical lines or both.
 * Once constructed,
 * the user can obtain an Iterator&lt;Line2D&gt;
 * to generate the lines
 * or use a for-each loop.
 * </p>
 * <p>
 * It is guaranteed that 
 * horizontal line are generated sequentially,
 * from the top of the grid to the bottom,
 * and vertical lines are generated sequentially
 * from left to right.
 * </p>
 * </div>
 * <p>
 * Following is a code sample
 * that was used to generate the lines
 * in the figure at the right.
 * </p>
 * <div class="js-codeblock" style="max-width: 35em;">
   LineGenerator   hlGen   = 
    new LineGenerator( 
        boundingRect,
        gridUnit,
        lpu,
        -1,
        LineGenerator.HORIZONTAL
    );
gtx.setStroke( new BasicStroke( lineWeight ));
gtx.setColor( hlColor );
for ( Line2D line : hlGen )
    gtx.draw( line );

LineGenerator   vlGen   = 
    new LineGenerator( 
        boundingRect,
        gridUnit,
        lpu,
        -1,
        LineGenerator.VERTICAL
    );
gtx.setColor( vlColor );
for ( Line2D line : vlGen )
    gtx.draw( line );
</div>
 * @author Jack Straub
 */
public class LineGenerator implements Iterable<Line2D>
{
    /** Indicates that the iterable generates horizontal lines. */
    public static final int HORIZONTAL  = 1;
    /** Indicates that the iterable generates vertical lines. */
    public static final int VERTICAL    = 2;
    /** 
     * Indicates that the iterable generates horizontal 
     * and vertical lines. 
     */
    public static final int BOTH        = 3;
    
    private final float gridWidth;      // width of the rectangle
    private final float gridHeight;     // height of the rectangle
    private final float centerXco;      // center x-coordinate
    private final float maxXco;         // right-most x-coordinate
    private final float centerYco;      // center y-coordinate
    private final float maxYco;         // bottom-most y-coordinate
    
    private final float gridUnit;       // pixels per unit
    
    private final float lpu;            // lines per unit
    private final float length;         // the length of a line
    private final int   orientation;    // HORIZONTAL, VERTICAL or BOTH
    
    private final float gridSpacing;    // pixels between lines
    private final float totalHorLines;  // total number of horizontal lines
    private final float totalVerLines;  // total number of vertical lines
    
    /**
     * Constructor.
     * Instantiates a LineGenerator
     * with a given bounding rectangle,
     * grid unit and lines-per-unit.
     * The line length defaults to -1
     * (grid lines will span the width or height
     * of the bounding rectangle)
     * and the orientation will default to BOTH
     * (the iterator will generate
     * horizontal and vertical lines).
     * 
     * @param rect      the given bounding rectangle
     * @param gridUnit  the given grid unit
     * @param lpu       the given lines-per-unit
     */
    public LineGenerator( Rectangle2D rect, float gridUnit, float lpu )
    {
        this( rect, gridUnit, lpu, -1, BOTH );
    }
    
    /**
     * Constructor.
     * Instantiates a LineGenerator
     * with a given bounding rectangle,
     * grid unit, lines-per-unit,
     * grid line length and orientation.
     * The line length may be -1
     * in which case grid lines will span the width or height
     * of the bounding rectangle.
     * The orientation may be HORIZONTAL
     * (the iterator will generate horizontal lines),
     * VERTICAL
     * (the iterator will generate vertical lines)
     * or BOTH
     * (the iterator will generate
     * horizontal and vertical lines)
     * 
     * @param rect          the given bounding rectangle
     * @param gridUnit      the given grid unit
     * @param lpu           the given lines-per-unit
     * @param length        the given line length
     * @param orientation   the given orientation
     */
    public LineGenerator( 
        Rectangle2D rect, 
        float       gridUnit,
        float       lpu,
        float       length, 
        int         orientation
    )
    {
        gridWidth = (float)rect.getWidth();
        gridHeight = (float)rect.getHeight();
        centerXco = (float)rect.getCenterX();
        maxXco = (float)rect.getMaxX();
        centerYco = (float)rect.getCenterY();
        maxYco = (float)rect.getMaxY();
        this.gridUnit = gridUnit;
        
        this.lpu = lpu;
        this.length = length;
        this.orientation = orientation;

        gridSpacing = gridUnit / lpu;
        System.out.println( gridWidth /gridSpacing );
        System.out.println( gridHeight /gridSpacing );
        totalVerLines = (float)Math.round( gridWidth / gridSpacing );
        totalHorLines = (float)Math.round( gridHeight / gridSpacing );
    }

    /**
     * Returns an iterator for horizontal and/or vertical lines.
     * The lines returned are determined by the orientation,
     * HORIZONTAL (horizontal lines only),
     * VERTICAL (vertical lines only)
     * or BOTH (horizontal and vertical lines).
     * The user may assume
     * that horizontal lines are generated 
     * starting at the top of the bounding rectangle,
     * then sequentially to the bottom.
     * Vertical lines are generated 
     * beginning at the left of the bounding rectangle
     * then sequentially to the far right.
     * 
     * @return  an iterator for horizontal and/or vertical lines
     */
    @Override
    public Iterator<Line2D> iterator()
    {
        Iterator<Line2D>    iter    = null;
        switch ( orientation )
        {
        case HORIZONTAL:
            iter = new Line2DHorizontalIterator();
            break;
        case VERTICAL:
            iter = new Line2DVerticalIterator();
            break;
        default:
            iter = new Line2DIterator();
        }
        return iter;
    }
    
    /**
     * Gets the total number of horizontal lines
     * generated by this iterator.
     * (Only meaningful if orientation is HORIZONTAL or BOTH.)
     * 
     * @return  the total number of horizontal lines
     *          generated by the iterator
     */
    public float getTotalHorizontalLines()
    {
        return totalHorLines;
    }
    
    /**
     * Gets the total number of vertical lines
     * generated by this iterator.
     * (Only meaningful if orientation is VERTICAL or BOTH.)
     * 
     * @return  the total number of vertical lines
     *          generated by the iterator
     */
    public float getTotalVerticalLines()
    {
        return totalVerLines;
    }

    // produces a sequence of vertical lines from left to right.
    private class Line2DVerticalIterator implements Iterator<Line2D>
    {
        private final float yco1;
        private final float yco2;
        private float       xco;
        
        public Line2DVerticalIterator()
        {
            // Calculate the number of lines drawn left of the origin.
            float   numLeft     = (float)Math.floor( totalVerLines / 2 );
            
            // The actual length is the length passed by the user, or,
            // if the user passed a negative value, the height of the grid.
            float   actLength   = length >= 0 ? length : gridHeight;
            
            // Calculate the top (yco1) and bottom (yco2) of the line
            yco1 = centerYco - actLength / 2;
            yco2 = yco1 + actLength;
            
            // Calculate the x-coordinate of the leftmost line. The
            // x-coordinate is the same for both endpoints of a line.
            // After generating a line, the x-coordinate is incremented
            // to the next line.
            xco = centerXco - numLeft * gridSpacing;
        }
        
        @Override
        // This method required by "implements Iterator<Line2D>"
        public boolean hasNext()
        {
            // The iterator is exhausted when the x-coordinate
            // exceeds the bounds of the grid.
            boolean hasNext = xco < maxXco;
            return hasNext;
        }

        @Override
        // This method required by "implements Iterator<Line2D>"
        public Line2D next()
        {
            // Throw an exception if there is no next line.
            if ( xco > maxXco )
            {
                String  msg = "Grid bounds exceeded at x = " + xco;
                throw new NoSuchElementException( msg );
            }
            Line2D  line    = 
                new Line2D.Float( xco, yco1, xco, yco2 );
            xco += gridSpacing;
            return line;
        }
    }
    
    // Produces a sequence of horizontal lines from top to bottom
    private class Line2DHorizontalIterator implements Iterator<Line2D>
    {
        private final float gridSpacing;
        private final float xco1;
        private final float xco2;
        private float       yco;
        
        public Line2DHorizontalIterator()
        {
            float   actLength = length >= 0 ? length : gridWidth;
            float   numTop  = (float)Math.floor( totalHorLines / 2 );
            gridSpacing = gridUnit / lpu;
            xco1 = centerXco - actLength / 2;
            xco2 = xco1 + actLength;
            yco = centerYco - numTop * gridSpacing;
        }
        
        @Override
        public boolean hasNext()
        {
            boolean hasNext = yco < maxYco;
            return hasNext;
        }

        @Override
        public Line2D next()
        {
            if ( yco >= maxYco )
            {
                String  msg = "Grid bounds exceeded at y = " + yco;
                throw new NoSuchElementException( msg ); 
            }
            Line2D  line    = 
                new Line2D.Float( xco1, yco, xco2, yco );
            yco += gridSpacing;
            return line;
        }
    }
    
    private class Line2DIterator implements Iterator<Line2D>
    {
        private final Iterator<Line2D>  horizontalIter  = 
            new Line2DHorizontalIterator();
        private final Iterator<Line2D>  verticalIter    = 
            new Line2DVerticalIterator();
        
        @Override
        public boolean hasNext()
        {
            boolean hasNext = horizontalIter.hasNext() | verticalIter.hasNext();
            return hasNext;
        }
        
        @Override
        public Line2D next()
        {
            Line2D  next    =
                horizontalIter.hasNext() ?
                horizontalIter.next() :
                verticalIter.next();
            return next;
        }
    }
}
