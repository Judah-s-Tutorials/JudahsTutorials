package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static util.BaseCaseParameters.BASE_GRID_HEIGHT;
import static util.BaseCaseParameters.BASE_GRID_UNIT;
import static util.BaseCaseParameters.BASE_GRID_WIDTH;
import static util.BaseCaseParameters.BASE_LINES_PER_UNIT;

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
 * (see {@linkplain LineGenerator}).
 * Configuration parameters are chosen
 * so that the expected coordinates of grid lines
 * can be easily verified by hand
 * {@linkplain util.BaseCaseParameters}).
 * Grid line generation rules
 * are applied to obtain expected values
 * which are then compared to the actual values
 * produced by LineGenerator.
 * <p>
 * This is <em>not</em> intended to be
 * a thorough unit test for the LineGenerator class;
 * see {@linkplain LineGeneratorTest}.
 * </p>
 * 
 * @author Jack Straub
 * 
 * @see util.BaseCaseParameters
 * @see LineGenerator
 * @see LineGeneratorTest
 *
 */
public class LineGeneratorBaseCaseTest
{
    /** Tolerance for testing the approx. equality of 2 decimal numbers. */
    private static final float          epsilon         = .001f;
    
    private static final float          gridXco         = 0;
    private static final float          gridYco         = 0;
    private static final float          gridWidth       = BASE_GRID_WIDTH;
    private static final float          gridHeight      = BASE_GRID_HEIGHT;
    private static final float          gridUnit        = BASE_GRID_UNIT;
    private static final float          gridLPU         = BASE_LINES_PER_UNIT;
    private static final Rectangle2D    gridRect        =
        new Rectangle2D.Float( gridXco, gridYco, gridWidth, gridHeight );
    
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
        // All of the following calculations assume that the coordinates
        // of the bounding rectangle are (0, 0)
        
        // grid spacing; pixels-per-line [rule: gridSpacing]
        float   gridSpacing = gridUnit / gridLPU;
        // Horizontal lines in half the grid (not including x-axis)
        // [rule: numHLinesAbove]
        int     halfHoriz   = 
            (int)Math.floor( (gridHeight - 1) / 2 / gridSpacing );
        // Vertical lines in half the grid (not including y-axis)
        // [rule: numVLinesLeft]
        int     halfVert    = 
            (int)Math.floor( (gridWidth - 1) / 2 / gridSpacing );
        // Y-coordinate of the x-axis [rule: xAxis] 
        float   xAxisYco    = (gridHeight - 1) / 2;
        // X-coordinate of the y-axis [rule: yAxis] 
        float   yAxisXco    = (gridWidth - 1) / 2;
        
        // Calculate all horizontal lines 
        //[rule: nthHLineAbove], [rule: nthHLineAbove]
        for ( int inx = -halfHoriz ; inx <= halfHoriz ; ++inx )
        {
            float   yco     = xAxisYco + inx * gridSpacing;
            Line2D  line    = new Line2D.Float( 0, yco, gridWidth, yco );
            horizLines.add( line );
        }
        
        // Calculate all the vertical lines 
        //[rule: nthVLineLeft], [rule: nthVLineRight]
        for ( int inx = -halfVert ; inx <= halfVert ; ++inx )
        {
            float   xco     = yAxisXco + inx * gridSpacing;
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
                gridLPU,
                -1,
                LineGenerator.HORIZONTAL
            );
        int expNumHorizLines    = horizLines.size();
        int actNumHorizLines    = (int)gen.getHorLineCount();
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
     * Verify that the expected vertical lines are generated.
     */
    @Test
    public void testLineGeneratorVert()
    {
        LineGenerator   gen                 =
            new LineGenerator(
                gridRect,
                gridUnit,
                gridLPU,
                -1,
                LineGenerator.VERTICAL
            );
        int expNumVertLines = vertLines.size();
        int actNumVertLines = (int)gen.getVertLineCount();
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
