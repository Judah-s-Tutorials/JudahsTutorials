package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
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
 * @author Jack Straub
 *
 */
public class LineGeneratorBaseCaseTest
{
    private static final float          gridWidth       = 512;
    private static final float          gridHeight      = 256;
    private static final float          gridUnit        = 64;
    private static final float          linesPerUnit    = 2;
    private static final Rectangle2D    gridRect        =
        new Rectangle2D.Float( 0, 0, gridWidth, gridHeight );
    
    /** Expected y-coordinate of the first horizontal line. */
    private static final float          firstYco        = -.5f;
    /** Expected x-coordinate of the first vertical line. */
    private static final float          firstXco        = -.5f;
    /** Expected number of horizontal lines. */
    private static final int            numHorizLines   = 9;
    /** Expected number of vertical lines. */
    private static final int            numVertLines    = 17;
    
    /** List of all horizontal lines, in order. Initialized in beforAll(). */
    private static final List<Line2D>   horizLines      = new ArrayList<>();
    /** List of all horizontal lines, in order. Initialized in beforAll(). */
    private static final List<Line2D>   vertLines       = new ArrayList<>();
        
    /**
     * Calculate all the horizontal and vertical lines
     * expected to be generated
     * given the width, height, gridUnit and lpu
     * specified above.
     * The upper left-hand corner of the grid
     * is assumed to be (0,0).
     */
    @BeforeAll
    public static void beforeAll()
    {
        float   ppl     = gridUnit / linesPerUnit;
        
        for ( int inx = 0 ; inx < numHorizLines ; ++inx )
        {
            float   yco     = firstYco + inx * ppl;
            Line2D  line    = new Line2D.Float( 0, yco, gridWidth, yco );
            horizLines.add( line );
        }
        
        for ( int inx = 0 ; inx < numVertLines ; ++inx )
        {
            float   xco     = firstXco + inx * ppl;
            Line2D  line    = new Line2D.Float( xco, 0, xco, gridHeight );
            vertLines.add( line );
        }
    }
    
    /**
     * Verify that the expected horizontal lines are generated.
     */
    @Test
    public void testLineGeneratorHoriz()
    {
        LineGenerator   gen                 =
            new LineGenerator(
                gridRect,
                gridUnit,
                linesPerUnit,
                -1,
                LineGenerator.HORIZONTAL
            );
        int expNumHorizLines    = horizLines.size();
        int actNumHorizLines    = (int)gen.getTotalHorizontalLines();
        assertEquals( expNumHorizLines, actNumHorizLines );
        
        int count   = 0;
        for ( Line2D actLine : gen )
        {
            Line2D  expLine = horizLines.get( count++ );
            assertLineEquals( expLine, actLine );
        }
        
        assertEquals( count, expNumHorizLines );
    }
    
    /**
     * Verify that the expected horizontal lines are generated.
     */
    @Test
    public void testLineGeneratorVert()
    {
        LineGenerator   gen                 =
            new LineGenerator(
                gridRect,
                gridUnit,
                linesPerUnit,
                -1,
                LineGenerator.VERTICAL
            );
        int expNumVertLines = vertLines.size();
        int actNumVertLines = (int)gen.getTotalVerticalLines();
        assertEquals( expNumVertLines, actNumVertLines );
        
        int count   = 0;
        for ( Line2D actLine : gen )
        {
            Line2D  expLine = vertLines.get( count++ );
            assertLineEquals( expLine, actLine );
        }
        
        assertEquals( count, expNumVertLines );
    }
    
    /**
     * Verify that two lines have the same end points,
     * within a given tolerance.
     * 
     * @param expLine     the expected line
     * @param actLine     the actual line
     * @param epsilon   the given tolerance
     */
    private void assertLineEquals( Line2D expLine, Line2D actLine )
    {
        final float epsilon = .001f;
        
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
}
