package com.acmemail.judah.cartesian_plane;

import java.awt.BasicStroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * An instance of this class
 * is used to generate the 
 * horizontal and/or vertical lines in a grid
 * encapsulated in a given rectangle.
 * The grid is divided into <em>units</em>
 * (the <em>gridUnit</em> parameter),
 * where <em>1 unit = gridUnit pixels</em>.
 * The number of lines generated
 * is determined by a given 
 * number of lines per unit (the <em>LPU</em> parameter).
 * The <em>gridUnit</em> and <em>LPU</em>
 * are provided by the user
 * in the constructor.
 * Users also specify,
 * via the constructor,
 * whether they want to generate
 * horizontal lines, vertical lines or both.
 * Once constructed,
 * the user can obtain an Iterator&lt;Line2D&gt;
 * to generate the lines,
 * or use a for-each loop.
 * Lines are never generated at the edges;
 * for example, 
 * given a rectangle=(0, 0, 200, 300)
 * the lines x=0, x=200, y=0, and y=300
 * <em>will not</em> be generated.
 * <img 
 *     src="doc-files/LineGeneratorFigure.png" 
 *     alt="LineGenerator Demo"
 *     style="float:right; width:15%; height:auto;"
 * >
 * <p>
 * Given a bounding rectangle for a grid,
 * one vertical line is always generated
 * at the horizontal center of the grid
 * (as though it were the y-axis
 * of a Cartesian plane).
 * A horizontal line is always generated
 * at the vertical center of the grid
 * (as though it were the x-axis
 * of a Cartesian plane).
 * These lines are the designated <em>axes</em>
 * and constitute a separate category
 * from all other lines.
 * In particular:
 * </p>
 * <ol>
 * <li>
 *      You can call {@link #axesIterator()}
 *      to obtain the axes for the encapsulated rectangle
 *      regardless of the values
 *      of the other configuration parameters
 *      (gridUnit, LPU, length, orientation).
 * </li>
 * <li>
 *      The method {@link #axesIterator()}
 *      always returns an object
 *      that iterates over exactly two objects
 *      (the horizontal and vertical axes).
 * </li>
 * <li>
 *      For convenience,
 *      the class method {@link #axesIterator(Rectangle2D)}
 *      returns an iterator for the two axes
 *      encapsulated in the given rectangle.
 * </li>
 * <li>
 *      Iterators for lines other than axes
 *      (non-axial lines)
 *      never include lines
 *      that are coincident with the axes.
 *      For example,
 *      given a LineGenerator object
 *      with rectangle = (0, 0, 400, 100 ),
 *      gridUnit = 50, and LPU = 1,
 *      vertical lines will be generated at
 *      x=50, x=100, x=150, 
 *      x=250, x=300, and x=350.
 * </li>
 * </ol>
 * <p>
 * The positions of horizontal lines
 * are determined using the x-axis as a base;
 * positions of vertical lines use the y-axis as a base.
 * For example,
 * given a LineGenerator object
 * with rectangle = (0, 0, 280, 180 ),
 * gridUnit = 50, and LPU = 1,
 * the axes will be generated at x=140 and y=90;
 * non-axial vertical lines will be generated
 * at x=40, x=90, x=190, and x=240;
 * non-axial horizontal lines 
 * will be generated
 * at y=40 and y=140.
 * It is guaranteed that 
 * iterators traverse horizontal lines sequentially,
 * from the top of the grid to the bottom,
 * and vertical lines are traversed sequentially,
 * from left to right.
 * </p>
 * <p>
 * Following is a code sample
 * that was used to generate the lines
 * in the figure at the right.
 * </p>
 * <div class="js-codeblock" style="max-width: 35em;">
gtx.setColor( rectColor );
gtx.draw( boundingRect );

LineGenerator   hlGen   = 
    new LineGenerator( 
        boundingRect,
        gridUnit,
        lpu,
        -1,
        LineGenerator.HORIZONTAL
    );
gtx.setStroke( new BasicStroke( lineWeight, lineCap, lineJoin ));
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

gtx.setStroke( new BasicStroke( axisWeight, lineCap, lineJoin ));
gtx.setColor( axisColor );
Iterator<Line2D>    axisIterator    = 
    LineGenerator.axesIterator( boundingRect );
gtx.draw( axisIterator.next() );
gtx.draw( axisIterator.next() );
</div>
 *
 * @author Jack Straub
 */
public class LineGenerator implements Iterable<Line2D>
{
    public static final int HORIZONTAL  = 1;
    /** Indicates that the iterable generates vertical lines. */
    public static final int VERTICAL    = 2;
    /** 
     * Indicates that the iterable generates horizontal 
     * and vertical lines. 
     */
    public static final int BOTH        = HORIZONTAL | VERTICAL;

    /** Bounding rectangle, set in constructor. */
    private final Rectangle2D   rect;
    /** Grid unit, set in constructor. */
    private final float         gpu;
    /** Lines-per-unit, set in constructor. */
    private final float         lpu;
    /** Horizontal line length, set in constructor. */
    private final float         horLength;
    /** Vertical line length, set in constructor. */
    private final float         vertLength;
    /** Orientation, set in constructor. */
    private final int           orientation;
    
    /** List containing the axial lines. */
    private final List<Line2D>  axes        = new LinkedList<>();
    /** List containing the non-axial horizontal lines. */
    private final List<Line2D>  horLines    = new LinkedList<>();
    /** List containing the non-axial vertical lines. */
    private final List<Line2D>  vertLines   = new LinkedList<>();
    
    /** 
     * X-coordinate of the origin of the encapsulated grid. Equivalent
     * to rect.getCenterX(), declared here for convenience. Set
     * in constructor.
     */
    private final float         originXco;
    /** 
     * Y-coordinate of the origin of the encapsulated grid. Equivalent
     * to rect.getCenterY(), declared here for convenience. Set
     * in constructor.
     */
    private final float         originYco;
    /**
     * X-coordinate of the left side of the rectangle. Equivalent
     * to rect.getMinX(), declared here for convenience. Set
     * in constructor.
     */
    private final float         leftLimit;
    /**
     * X-coordinate of the right side of the rectangle. Equivalent
     * to rect.getMaxX(), declared here for convenience. Set
     * in constructor.
     */
    private final float         rightLimit;
    /**
     * Y-coordinate of the top of the rectangle. Equivalent
     * to rect.getMinY(), declared here for convenience. Set
     * in constructor.
     */
    private final float         topLimit;
    /**
     * Y-coordinate of the bottom of the rectangle. Equivalent
     * to rect.getMaxY(), declared here for convenience. Set
     * in constructor.
     */
    private final float         bottomLimit;
    
    /**
     * Constructor.
     * Instantiates a LineGenerator
     * with the given bounding rectangle,
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
     * with the given bounding rectangle,
     * grid unit, lines-per-unit and line length.
     * The orientation will default to BOTH
     * (the iterator will generate
     * horizontal and vertical lines).
     * 
     * @param rect      the given bounding rectangle
     * @param gridUnit  the given grid unit
     * @param lpu       the given lines-per-unit
     * @param length    the given line length
     */
    public 
    LineGenerator( 
        Rectangle2D rect, 
        float       gridUnit,
        float       lpu,
        float       length 
    )
    {
        this( rect, gridUnit, lpu, length, BOTH );    
    }
    
    /**
     * Constructor.
     * Instantiates a LineGenerator
     * with the given bounding rectangle,
     * grid unit, lines-per-unit line length,
     * and orientation.
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
        this.rect = rect;
        this.gpu = gridUnit;
        this.lpu = lpu;
        this.horLength = length != -1 ? length : (float)rect.getWidth();
        this.vertLength = length != -1 ? length : (float)rect.getHeight();
        this.orientation = orientation;
        
        originXco = (float)rect.getCenterX();
        originYco = (float)rect.getCenterY();
        leftLimit = (float)rect.getMinX();
        rightLimit = (float)rect.getMaxX();
        topLimit = (float)rect.getMinY();
        bottomLimit = (float)rect.getMaxY();
        computeHorizontals();
        computeVerticals();
        computeAxes();
    }
    
    /**
     * Returns an iterator for the axes
     * of a grid drawn in the given rectangle.
     * 
     * @param rect  the given rectangle
     * 
     * @return  
     *      an iterator for the axes
     *      of a grid drawn in the given rectangle
     */
    public static Iterator<Line2D> axesIterator( Rectangle2D rect )
    {
        LineGenerator          lineGen = new LineGenerator( rect, 1, 1, -1 );
        Iterator<Line2D>    iter    = lineGen.axesIterator();
        return iter;
    }
    
    /**
     * Returns an iterator
     * to traverse the non-axial lines
     * of a grid drawn in the encapsulated rectangle.
     * The lines may include 
     * the vertical and/or horizontal lines
     * depending on the parameters
     * passed to the constructor.
     * 
     * @return 
     *      an iterator to traverse
     *      the non-axial lines of the encapsulated grid
     * 
     * See {@link #VERTICAL}, {@link #HORIZONTAL}, {@link #BOTH}.
     */
    @Override
    public Iterator<Line2D> iterator()
    {
        List<Line2D>    allLines    = new ArrayList<>();
        if ( (orientation & HORIZONTAL) != 0 )
            allLines.addAll( horLines );
        if ( (orientation & VERTICAL) != 0 )
            allLines.addAll( vertLines );
            
        Iterator<Line2D>    iter    = allLines.iterator();
        return iter;
    }
    
    /**
     * Returns an iterator
     * that traverses the x- and y-axes
     * of the encapsulated grid.
     * @return
     *      iterator that traverses the x- and y-axes
     *      of the encapsulated grid.
     */
    public Iterator<Line2D> axesIterator()
    {
        return axes.iterator();
    }
    
    /**
     * Returns the number of horizontal lines
     * in the encapsulated grid
     * excluding the x-axis.
     * 
     * @return  
     *      the number of non-axial horizontal lines
     *      in the encapsulated grid
     */
    public float getHorLineCount()
    {
        return horLines.size();
    }

    /**
     * Returns the number of vertical lines
     * in the encapsulated grid
     * excluding the y-axis.
     * 
     * @return  
     *      the number of non-axial vertical lines
     *      in the encapsulated grid
     */
    public float getVertLineCount()
    {
        return vertLines.size();
    }

    /**
     * Computes the non-axial horizontal lines
     * in the encapsulated grid.
     * Added to the horizontal line list
     * in ascending order of the y-coordinates.
     */
    private void computeHorizontals()
    {
        float   spacing = gpu / lpu;
        float   xco1    = originXco - horLength / 2;
        float   xco2    = originXco + horLength / 2;

        for ( 
            float yco = originYco - spacing ; 
            yco > topLimit ; 
            yco -= spacing
        )
        {
            Point2D left    = new Point2D.Double( xco1, yco );
            Point2D right   = new Point2D.Double( xco2, yco );
            Line2D  line    = new Line2D.Double( left, right );
            horLines.add( 0, line );
        }

        for ( 
            float yco = originYco + spacing ; 
            yco < bottomLimit ; 
            yco += spacing
        )
        {
            Point2D left    = new Point2D.Double( xco1, yco );
            Point2D right   = new Point2D.Double( xco2, yco );
            Line2D  line    = new Line2D.Double( left, right );
            horLines.add( line );
        }
    }
    
    /**
     * Computes the non-axial vertical lines
     * in the encapsulated grid.
     * Added to the vertical line list
     * in ascending order of the x-coordinates.
     */
    private void computeVerticals()
    {
        float   spacing = gpu / lpu;
        float   yco1    = originYco - vertLength / 2;
        float   yco2    = originYco + vertLength / 2;

        for ( 
            float xco = originXco - spacing ;
            xco > leftLimit ; 
            xco -= spacing
        )
        {
            Point2D top     = new Point2D.Double( xco, yco1 );
            Point2D bottom  = new Point2D.Double( xco, yco2 );
            Line2D  line    = new Line2D.Double( top, bottom );
            vertLines.add( 0, line );
        }

        for ( 
            float xco = originXco + spacing ; 
            xco < rightLimit ; 
            xco += spacing
        )
        {
            Point2D top     = new Point2D.Double( xco, yco1 );
            Point2D bottom  = new Point2D.Double( xco, yco2 );
            Line2D  line    = new Line2D.Double( top, bottom );
            vertLines.add( line );
        }
    }
    
    /**
     * Computes the axes of the encapsulated grid.
     */
    private void computeAxes()
    {
        Point2D hPoint1 = new Point2D.Double( leftLimit, originYco );
        Point2D hPoint2 = new Point2D.Double( rightLimit, originYco );
        Point2D vPoint1 = new Point2D.Double( originXco, topLimit );
        Point2D vPoint2 = new Point2D.Double( originXco, bottomLimit );
        axes.add( new Line2D.Double( hPoint1, hPoint2 ) );
        axes.add( new Line2D.Double( vPoint1, vPoint2 ) );
    }
}
