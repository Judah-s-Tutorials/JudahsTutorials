package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.cartesian_plane.LineGenerator;

/**
 * This is an auxiliary test class
 * for validating the lines generated
 * by a LineGenerator instance against
 * the expected values
 * as determined by the algorithm
 * described by that class.
 * See <em>Line Generation Algorithm</em>"
 * in the LineGenerator class description.
 * 
 * 
 * <p>
 * <span style="font-size: 110%; font-weight: bold;" id="TestValueCoordination">
 *     About Coordinating Test Values for Grid Unit and LPU<br>
 * </span>
 * Values such as those 
 * for the rectangle bounding the grid,
 * grid unit and LPU
 * are often generated randomly. 
 * When doing so,
 * value generation must be designed
 * with the following constraints:
 * </p>
 * <ol>
 * <li>
 *     A bounding rectangle
 *     will always have a width and height
 *     that are at least 10 pixels.
 * </li>
 * <li>
 *     There will always be
 *     more than one unit
 *     on either side of the x- and y-axes
 *     (the "more than one" constraint
 *     can be satisfied by
 *     fractional values
 *     such as 1.1).
 * </li>
 * <li>
 *     The grid unit 
 *     and LPU values must be chosen
 *     so that there is never
 *     more than one grid line
 *     per pixel.
 * </li>
 * </ol>
 * <p>
 * Note that one consequence of the above constraints
 * is that a test rectangle
 * will always be suitable
 * for containing at least 6 visible lines;
 * the x- and y-axes,
 * and at least one grid line
 * on either side
 * of the x- and y-axes.
 * </p>
 * 
 * @author Jack Straub
 * 
 * @see LineGenerator
 *
 */
public class LineMetrics
{
    /**  List of horizontal lines generated by the LineGenerator algorithm. */
    private final List<Line2D>  hLines      = new ArrayList<>();
    /** List of Vertical lines generated by the LineGenerator algorithm. */
    private final List<Line2D>  vLines      = new ArrayList<>();
    /** 
     * Rectangle bounding the grid; 
     * see LineGenerator <b>[rule: boundingRect</b>]. 
     */
    private final Rectangle2D   boundingRect;
    /**
     * Pixels allocated per unit; 
     * see LineGenerator <b>[rule: gridUnit]</b>.
     */
    private final float         gridUnit;
    /** 
     * Grid lines per unit; 
     * see LineGenerator <b>[rule: lpu]</b>.
     */
    private final float         lpu;
    /** 
     * Spacing between lines; 
     * see LineGenerator <b>[rule: gridSpacing]</b>.
     */
    private final float         gridSpacing;
    
    /** 
     * X-coordinate of the y-axis; 
     * see LineGenerator <b>[rule: yAxis]</b>.
     */
    private final float         yAxisXco;
    /** 
     * Y-coordinate of the x-axis; 
     * see LineGenerator <b>[rule: xAxis]</b>.
     */
    private final float         xAxisYco;
    
    /**
     * Constructor.
     * Initializes all parameters.
     * 
     * @param rect          bounding rectangle for grid
     * @param gridUnit      pixels per unit
     * @param linesPerUnit  lines-per-unit
     */
    public LineMetrics(
        Rectangle2D rect,
        float       gridUnit, 
        float       linesPerUnit
    )
    {
        this.boundingRect = rect;
        this.gridUnit = gridUnit;
        this.lpu = linesPerUnit;
        
        // see LineGenerator rule: gridSpacing
        // pixels between lines
        this.gridSpacing = gridUnit / linesPerUnit;
        
        float   minXco = (float)rect.getX();
        float   maxXco = (float)rect.getWidth() + minXco;
        float   minYco = (float)rect.getY();
        float   maxYco = (float)rect.getHeight() + minYco;
        
        // see LineGenerator rule: yAxis
        // determines location of y-axis
        yAxisXco = minXco +((float)rect.getWidth() - 1) / 2;
        // determines location of x-axis
        xAxisYco = minYco + ((float)rect.getHeight() - 1) / 2;

        // see LineGenerator rule: numHLinesAbove
        // see LineGenerator rule: numHLinesBelow
        // horizontal lines above or below x-axis
        float   halfHoriz   = 
            (float)Math.floor( (rect.getHeight() - 1) / 2 / gridSpacing);
        // see LineGenerator rule: numVLinesLeft
        // see LineGenerator rule: numVLinesRight
        float   halfVert    = 
            (float)Math.floor( (rect.getWidth() - 1) / 2 / gridSpacing);

        // see LineGenerator rule: nthVLineLeft
        // see LineGenerator rule: nthVLineRight
        // generate vertical lines 
        for ( float nextVert = -halfVert ; nextVert <= halfVert ; ++nextVert )
        {
            float   xco         = yAxisXco + nextVert * gridSpacing;
            Line2D  line        = 
                new Line2D.Float( xco, minYco, xco, maxYco );
            vLines.add( line );
        }
        
        // see LineGenerator rule: nthHLineAbove
        // see LineGenerator rule: nthHLineBelow
        // generate horizontal lines 
        for ( float nextHoriz = -halfHoriz ; nextHoriz <= halfHoriz ; ++nextHoriz )
        {
            float   yco = xAxisYco + nextHoriz * gridSpacing;
            Line2D  line    = 
                new Line2D.Float( minXco, yco, maxXco, yco );
            hLines.add( line );
        }
    }
    
    /**
     * Get the line segment associated 
     * with a given vertical or horizontal line
     * of a given length.
     * 
     * @param lineIn    the given horizontal or vertical line
     * @param len       the given length
     * 
     * @return the line segment with the given length
     */
    public Line2D getLineSegment( Line2D lineIn, float len )
    {
        Line2D  lineOut;
        
        float   lineInXco1  = (float)lineIn.getX1();
        float   lineInXco2  = (float)lineIn.getX2();
        if ( floatEquals( lineInXco1, lineInXco2 ) )
        {
            // if x1 == x2 this is a vertical line
            float   xco     = lineInXco1;
            float   yco1    = xAxisYco - len / 2;
            float   yco2    = yco1 + len;
            lineOut = new Line2D.Float( xco, yco1, xco, yco2 );
        }
        else
        {
            // x1 != x2 this is not a vertical line,
            // it must be a horizontal line
            float   yco     = (float)lineIn.getY1();
            float   xco1    = yAxisXco - len / 2;
            float   xco2    = xco1 + len;
            lineOut = new Line2D.Float( xco1, yco, xco2, yco );
        }
        return lineOut;
    }
    
    /**
     * Gets the expected list of horizontal lines.
     * 
     * @return the expected list of horizontal lines.
     */
    public List<Line2D> getHorizLines()
    {
        return hLines;
    }
    
    /**
     * Gets the expected number of horizontal lines.
     * 
     * @return the expected number of horizontal lines.
     */
    public int getNumHorizLines()
    {
        return hLines.size();
    }

    /**
     * Gets the expected list of vertical lines.
     * 
     * @return the expected list of vertical lines.
     */
    public List<Line2D> getVertLines()
    {
        return vLines;
    }
    
    /**
     * Gets the expected number of vertical lines.
     * 
     * @return the expected number of vertical lines.
     */
    public int getNumVertLines()
    {
        return vLines.size();
    }

    /**
     * Gets the rectangle used to create this object.
     * 
     * @return the rectangle used to create this object
     */
    public Rectangle2D getRect()
    {
        return boundingRect;
    }

    /**
     * Gets the pixels-per-unit value used to create this object.
     * 
     * @return the pixels-per-unit value used to create this object
     */
    public float getGridSpacing()
    {
        return gridUnit;
    }

    /**
     * Gets the lines-per-unit value used to create this object.
     * 
     * @return the lines-per-unit value used to create this object
     */
    public float getLPU()
    {
        return lpu;
    }
    
    /**
     * Verify that two lines have the same end points,
     * within a given tolerance.
     * 
     * @param expLine     the expected line
     * @param actLine     the actual line
     * @param epsilon     the given tolerance
     */
    public static void assertLineEquals( Line2D expLine, Line2D actLine, float epsilon )
    {
        float   expLineXco1 = (float)expLine.getX1();
        float   expLineYco1 = (float)expLine.getY1();
        float   expLineXco2 = (float)expLine.getX2();
        float   expLineYco2 = (float)expLine.getY2();
        
        float   actLineXco1 = (float)actLine.getX1();
        float   actLineYco1 = (float)actLine.getY1();
        float   actLineXco2 = (float)actLine.getX2();
        float   actLineYco2 = (float)actLine.getY2();
        
        String  fmt         =
            "Expected: Line2D(%.1f,%.1f,%.1f,%.1f) "
            + "Actual: Line2D(%.1f,%.1f,%.1f,%.1f)";
        String  msg         =
            String.format(
                fmt,
                expLineXco1,
                expLineYco1,
                expLineXco2,
                expLineYco2,
                actLineXco1,
                actLineYco1,
                actLineXco2,
                actLineYco2
            );
        assertEquals( expLineXco1, actLineXco1, epsilon, msg );
        assertEquals( expLineYco1, actLineYco1, epsilon, msg );
        assertEquals( expLineXco2, actLineXco2, epsilon, msg );
        assertEquals( expLineYco2, actLineYco2, epsilon, msg );
    }

    /**
     * Determine if two decimal values are approximately equal
     * within a tolerance of .001.
     * 
     * @param dVal1 first decimal value
     * @param dVal2 second decimal value
     * 
     * @return  true if the two decimal values are approximately equal
     */
    private static boolean floatEquals( float dVal1, float dVal2 )
    {
        final float     epsilon = .001f;
        float           diff    = Math.abs( dVal1 - dVal2 );
        boolean         result  = diff < epsilon;
        return result;
    }
}
