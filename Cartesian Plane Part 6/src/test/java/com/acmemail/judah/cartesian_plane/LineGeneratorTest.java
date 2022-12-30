package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
public class LineGeneratorTest
{
    /** For testing decimal values for equality with a minimum error value. */
    private static final float  epsilon         = .001f;
    
    private static final List<Line2D>   vLines          = new ArrayList<>();
    private static final List<Line2D>   hLines          = new ArrayList<>();
    
    private static final float          baseRectWidth   = 2048;
    private static final float          baseRectHeight  = 1024;
    private static final float          baseGridUnit    = 256;
    private static final Rectangle2D    baseRect        =
        new Rectangle2D.Float( 0, 0, baseRectWidth, baseRectHeight );
    
    // Choose LPU to be a small factor of 2.
    private static final float          baseLPU         = 4;
    
    private static final float          expBaseLPP      = 64;
    private static final float  expBaseNumVLines    = 
        baseRectWidth / expBaseLPP;
    private static final float  expBaseNumHLines    = 
        baseRectHeight / expBaseLPP;
    
    @BeforeAll
    public void beforeAll()
    {
        float   xAxis       = baseRectHeight / 2;
        float   yAxis       = baseRectWidth / 2;
        float   halfHoriz   = (float)Math.floor( expBaseNumHLines / 2 );
        float   halfVert    = (float)Math.floor( expBaseNumVLines / 2 );
        for ( float inx = -halfHoriz ; inx < 0 ; ++inx )
        {
            float   yco     = inx * expBaseLPP;
            Line2D  line    = new Line2D.Float( 0, yco, baseRectWidth, yco );
        }
    }
    
    /**
     * Initialization logic executed immediately before
     * the start of each test.
     */
    @BeforeEach
    public void beforeEach()
    {
    }

    /**
     * Test the constructor that sets all the parameters with no defaults.
     * Specify a specific orientation and a specific line length,
     * verify that both are honored.
     * <p>
     * Not too concerned with checking line generation detail here;
     * that will come later. 
     * Right now I just want to make sure
     * that the constructors are performing
     * the correct initialization.
     * </p>
     * 
     * case orientation = HORIZONTAL
     */
    @Test
    public void testLineGeneratorRectangle2DFloatFloat()
    {
        LineGenerator   gen         =
            new LineGenerator(
                baseRect,
                baseGridUnit,
                baseLPU
            );
        assertEquals( expBaseNumVLines, gen.getTotalVerticalLines() );
        assertEquals( expBaseNumHLines, gen.getTotalHorizontalLines() );
        boolean         hasHoriz    = false;
        boolean         hasVert     = false;
    }
    
    private static List<Line2D> getHLines( float width, float height, float lpp )
    {
        List<Line2D>    lines   = new ArrayList<>();
        float           xAxis       = height / 2;
        float           halfHoriz   = (float)Math.floor( height / 2 / lpp );
        for ( float inx = -halfHoriz ; inx < 0 ; ++inx )
        {
            float   yco     = inx * lpp;
            Line2D  line    = new Line2D.Float( 0, yco, width, yco );
            lines.add( line );
        }
        return lines;
    }
}
