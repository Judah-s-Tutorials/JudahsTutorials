package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import util.LineMetrics;

/**
 * This class represents a base case test
 * for the line generation algorithm
 * (see {@link LineGenerator}).
 * Configuration parameters are chosen
 * so that the expected coordinates of grid lines
 * can be easily verified by hand.
 * Grid line generation rules
 * are then applied to obtain expected values
 * which are then compared to the actual values
 * produced by LineGenerator.
 * Some of the expected values are:
 * <p>
 * Given:
 * </p>
 * <ul>
 * <li>bounding rectangle coordinates = (0, 0)</li>
 * <li>grid width = 512</li>
 * <li>grid height = 256</li>
 * <li>grid unit = 64</li>
 * <li>lines-per-unit = 2
 * </ul>
 * <p>
 * Expected:
 * <li>Y-axis is given by x = 255.5</li>
 * <li>X-axis is given by y = 127.5</li>
 * <li>grid spacing (pixels-per-line or ppl) == 64 / 2 = 32</li>
 * <li>
 *     Number of vertical lines left of y-axis = 
 *     <em>floor( (512 - 1) / 2 / 32) = 7
 * </p>
 * 
 * @author Jack Straub
 * 
 * @see LineGenerator
 *
 */
public class LineGeneratorBaseCaseTest
{
    /** Tolerance for testing the equality of 2 decimal numbers. */
    private static final float          epsilon         = .001f;
    
    private static final float          gridXco         = 0;
    private static final float          gridYco         = 0;
    private static final float          gridWidth       = 512;
    private static final float          gridHeight      = 256;
    private static final float          gridUnit        = 64;
    private static final float          linesPerUnit    = 2;
    private static final Rectangle2D    gridRect        =
        new Rectangle2D.Float( gridXco, gridYco, gridWidth, gridHeight );
    
    /** Expected y-coordinate of the first horizontal line. */
    private static final float          firstYco        = -.5f;
    /** Expected x-coordinate of the first vertical line. */
    private static final float          firstXco        = -.5f;
    /** Expected number of horizontal lines. */
    private static final int            numHorizLines   = 7;
    /** Expected number of vertical lines. */
    private static final int            numVertLines    = 15;
    
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
            LineMetrics.assertLineEquals( expLine, actLine, epsilon );
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
            LineMetrics.assertLineEquals( expLine, actLine, epsilon );
        }
        
        assertEquals( count, expNumVertLines );
    }
}
