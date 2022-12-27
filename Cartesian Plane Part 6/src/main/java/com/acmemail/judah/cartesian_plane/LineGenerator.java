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
 * <p style = "font-size: 130%; font-weight: bold;" id="algorithm">
 * Line Generation Algorithm
 * </p>
 * <p>
 * Following are the constraints
 * and parameters
 * governing the generation
 * of lines within the bounds
 * of a given rectangle.
 * </p>
 * <div style="left-margin: 3em;">
 *     <p><b>Given:</b></p>
 *     <ul>
 *     <li>
 *         <b>rectXco:</b>
 *         The x-coordinate 
 *         of the upper left corner
 *         of the rectangle.
 *     </li>
 *     <li>
 *         <b>rectYco:</b>
 *         The y-coordinate 
 *         of the upper left corner
 *         of the rectangle.
 *     </li>
 *     <li>
 *         <b>rectWidth:</b>
 *         The width 
 *         of the rectangle.
 *     </li>
 *     <li>
 *         <b>rectHeight:</b>
 *         The height 
 *         of the rectangle.
 *     </li>
 *     </ul>
 * </div>
 * <ol style="font-family: Tahoma, Helvetica, sans-serif; max-width: 30em;">
 *     <li>
 *     The left-bound of the rectangle (rectXco)
 *     is inside the rectangle.
 *     </li>
 *     <li>
 *     The upper-bound of the rectangle (rectYco)
 *     is inside the rectangle.
 *     </li>
 *     <li>
 *     The right-bound of the rectangle,
 *     given by rectXco + rectWidth,
 *     is outside the rectangle.
 *     </li>
 *     <li>
 *     The lower-bound of the rectangle,
 *     given by rectYco + rectHeight,
 *     is outside the rectangle.
 *     </li>
 *     <li>
 *     The grid unit (gridUnit),
 *     is given by the user of this class;
 *     it is the number of pixels
 *     (pixels-per-unit or PPU)
 *     allocated to the length of a unit.
 *     </li>
 *     <li>
 *     The lines-per-unit (lpu),
 *     is given by the user of this class;
 *     it is the number of lines
 *     to be drawn for each unit.
 *     </li>
 *     <li>
 *     The distance 
 *     between two consecutive grid lines (gridSpacing)
 *     is given by 
 *     <em>gridSpacing = gridUnit / lpu.</em>
 *     </li>
 *     <li>
 *     The coordinates of the x-axis
 *     are given by <em>y = recHeight / 2 (centerYco)</em>
 *     for x in the range
 *     [rectXco, rectXco + rectWidth).
 *     </li>
 *     <li>
 *     The coordinates of the y-axis
 *     are given by <em>x = recWidth / 2 (centerXco)</em>
 *     for y in the range
 *     [rectYco, rectYco + rectHeight).
 *     </li>
 *     <li>
 *     The number of horizontal lines
 *     above the x-axis
 *     is calculated as
 *     <em>floor(rectHeight / 2 / gridSpacing)</em>.
 *     </li>
 *     <li>
 *     The number of horizontal lines
 *     below the x-axis
 *     is always the same
 *     as the number of horizontal lines
 *     above the x-axis.
 *     </li>
 *     <li>
 *     The total number of horizontal lines
 *     is calculated as
 *     <em>2 * floor(rectHeight / 2 / gridSpacing) + 1</em>.
 *     </li>
 *     <li>
 *     The number of vertical lines
 *     left of the y-axis
 *     is calculated as
 *     <em>floor(rectWidth / 2 / gridSpacing)</em>.
 *     </li>
 *     <li>
 *     The number of vertical lines
 *     right of the y-axis
 *     is always the same
 *     as the number of vertical lines
 *     left of the y-axis.
 *     </li>
 *     <li>
 *     The total number of vertical lines
 *     is calculated as
 *     <em>2 * floor(rectWidth / 2 / gridSpacing) + 1</em>.
 *     </li>
 *     <li>
 *         The y-coordinate 
 *         of the <em>n<sup>th</sup></em> horizontal line
 *         above the x-axis
 *         is given by <em>-n * gridSpacing</em>.
 *     </li>
 *     <li>
 *         The y-coordinate 
 *         of the <em>n<sup>th</sup></em> horizontal line
 *         below the x-axis
 *         is given by <em>n * gridSpacing</em>.
 *     </li>
 *     <li>
 *         The x-coordinate 
 *         of the <em>n<sup>th</sup></em> vertical line
 *         left of the y-axis
 *         is given by <em>-n * gridSpacing</em>.
 *     </li>
 *     <li>
 *         The x-coordinate 
 *         of the <em>n<sup>th</sup></em> vertical line
 *         right of the y-axis
 *         is given by <em>n * gridSpacing</em>.
 *     </li>
 * </ol>
 * @author Jack Straub
 * @see com.acmemail.judah.sandbox.Rect2DEdgeDemo
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
     * 
     * @see <a href="#algorithm">Line Generation Algorithm</a>
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
        
        // See description of line generation algorithm, above.
        // # lines left of y-axis 
        float   halfVerLines    = (float)Math.floor(gridWidth / 2 / gridSpacing);
        totalVerLines = 2 * halfVerLines + 1;
        float   halfHorLines    = (float)Math.floor(gridHeight / 2 / gridSpacing);
        totalHorLines = 2 * halfHorLines + 1;
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

    /**
     * An instance of this class
     * produces a sequence of vertical lines from left to right
     * of the y-axis.
     * 
     * @author Jack Straub
     *
     */
    private class Line2DVerticalIterator implements Iterator<Line2D>
    {
        /** The number of lines drawn to either side of the y-axis. */
        private final float halfNum;
        /** The upper y-coordinate of every vertical line. */
        private final float yco1;
        /** The lower y-coordinate of every vertical line. */
        private final float yco2;
        /** 
         * The number of the next line to be generated;
         * range is [-halfNum, halfNum].
         */
        private float       next;
        
        /**
         * Constructor.
         * Establishes the first line to draw
         * in the sequence of generated lines.
         * This is the left-most vertical line.
         * Subsequent lines will be generated sequentially
         * proceeding to the right.
         */
        public Line2DVerticalIterator()
        {
            // Number of lines left or right of y-axis
            halfNum = (float)Math.floor( totalVerLines / 2 );
            
            // Calculate the number of the first vertical line to draw;
            // note that this will be to the left of the y-axis.
            next    = -halfNum;
            
            // The actual length is the length passed by the user, or,
            // if the user passed a negative value, the height of the grid.
            float   actLength   = length >= 0 ? length : gridHeight;
            
            // Calculate the top (yco1) and bottom (yco2) of the line
            yco1 = centerYco - actLength / 2;
            yco2 = yco1 + actLength;
        }
        
        @Override
        /**
         * Indicates whether or not this iterator has been exhausted.
         * This method required by "implements Iterator<Line2D>".
         * 
         * @return true if this iterator has not been exhausted
         */
        public boolean hasNext()
        {
            // The iterator is exhausted after drawing 
            // the last line to the right of the y-axis.
            boolean hasNext = next <= halfNum;
            return hasNext;
        }

        @Override
        /**
         * Returns the next
         * in the sequence of lines generated
         * from the left of the grid to the right.
         * This method required by "implements Iterator<Line2D>".
         * 
         * @return next in the sequence of generated lines
         * 
         * @throws  NoSuchElementException
         *          if the iterator has been exhausted
         */
        public Line2D next()
        {
            // Throw an exception if there is no next line.
            if ( next > halfNum )
            {
                String  msg = "Grid bounds exceeded at next = " + next;
                throw new NoSuchElementException( msg );
            }
            float   xco = centerXco + next++ * gridSpacing;
            Line2D  line    = 
                new Line2D.Float( xco, yco1, xco, yco2 );
            return line;
        }
    }
    
    /**
     * An instance of this class
     * produces a sequence of horizontal lines from above to below
     * the x-axis.
     * 
     * @author Jack Straub
     *
     */
    private class Line2DHorizontalIterator implements Iterator<Line2D>
    {
        /** The number of lines drawn to either side of the y-axis. */
        private final float halfNum;
        /** The first x-coordinate of every horizontal line. */
        private final float xco1;
        /** The second x-coordinate of every horizontal line. */
        private final float xco2;
        /** 
         * The number of the next line to be generated;
         * range is [-halfNum, halfNum].
         */
        private float       next;
        
        /**
         * Constructor.
         * Establishes the first line to draw
         * in the sequence of generated lines.
         * This is the upper-most horizontal line.
         * Subsequent lines will be generated sequentially
         * proceeding to the bottom.
         */
        public Line2DHorizontalIterator()
        {
            // Number of lines left or right of y-axis
            halfNum = (float)Math.floor( totalHorLines / 2 );
            
            // Calculate the number of the first horizontal line to draw;
            // note that this will above the x-axis.
            next    = -halfNum;
            float   actLength = length >= 0 ? length : gridWidth;
            xco1 = centerXco - actLength / 2;
            xco2 = xco1 + actLength;
        }
        
        @Override
        /**
         * Indicates whether or not this iterator has been exhausted.
         * This method required by "implements Iterator<Line2D>".
         * 
         * @return true if this iterator has not been exhausted
         */
        public boolean hasNext()
        {
            boolean hasNext = next <= halfNum;
            return hasNext;
        }

        @Override
        /**
         * Returns the next
         * in the sequence of lines generated
         * from the top of the grid to the bottom.
         * This method required by "implements Iterator<Line2D>".
         * 
         * @return next in the sequence of generated lines
         * 
         * @throws  NoSuchElementException
         *          if the iterator has been exhausted
         */
        public Line2D next()
        {
            if ( next > halfNum )
            {
                String  msg = "Grid bounds exceeded at line #" + next;
                throw new NoSuchElementException( msg ); 
            }
            float   yco = centerYco + next++ * gridSpacing;
            Line2D  line    = 
                new Line2D.Float( xco1, yco, xco2, yco );
            return line;
        }
    }
    
    /**
     * Generates the sequences
     * of horizontal and vertical lines
     * required by the encapsulated rectangle.
     * 
     * @author Jack Straub
     *
     * @see Line2DHorizontalIterator
     * @see Line2DVerticalIterator
     */
    private class Line2DIterator implements Iterator<Line2D>
    {
        private final Iterator<Line2D>  horizontalIter  = 
            new Line2DHorizontalIterator();
        private final Iterator<Line2D>  verticalIter    = 
            new Line2DVerticalIterator();
        
        @Override
        /**
         * Indicates whether or not this iterator has been exhausted.
         * This method required by "implements Iterator<Line2D>".
         * 
         * @return true if this iterator has not been exhausted
         */
        public boolean hasNext()
        {
            boolean hasNext = horizontalIter.hasNext() | verticalIter.hasNext();
            return hasNext;
        }
        
        @Override
        /**
         * Returns the next
         * in the sequence of generated horizontal and vertical lines
         * required for the encapsulated rectangle.
         * This method required by "implements Iterator<Line2D>".
         * 
         * @return next in the sequence of generated lines
         * 
         * @throws  NoSuchElementException
         *          if the iterator has been exhausted
         */
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
