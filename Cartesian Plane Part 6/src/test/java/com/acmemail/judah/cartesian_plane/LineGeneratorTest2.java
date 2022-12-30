package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
public class LineGeneratorTest2
{
    /** For testing decimal values for equality with a minimum error value. */
    private static final float  epsilon         = .001f;
    
    /** 
     * Random number generator.
     * Instantiated with a value
     * that guarantees it will 
     * always generate the same sequence of values.
     */
    private static final Random randy           = new Random( 5 );
    
    /** Minimum x-coordinate for the origin of the bounding rectangle. */
    private static final float  minRectXco      = 0;
    /** Maximum x-coordinate for the origin of the bounding rectangle. */
    private static final float  maxRectXco      = 100;
    /** Minimum y-coordinate for the origin of the bounding rectangle. */
    private static final float  minRectYco      = 0;
    /** Maximum y-coordinate for the origin of the bounding rectangle. */
    private static final float  maxRectYco      = 100;
    /**
     * Minimum width of the test grid's bounding rectangle.
     * @see <a href="#TestValueCoordination>Test Value Coordination</a>
     */
    private static final float  minRectWidth    = 300;
    /**
     * Maximum width of the test grid's bounding rectangle.
     * The choice of this value must be coordinated the
     * maxGridUnit value to ensure compliance
     * with the "Test Value Coordination" constraints, above.
     * @see <a href="#TestValueCoordination>Test Value Coordination</a>
     * @see #maxGridUnit
     */
    private static final float  maxRectWidth    = 3000;
    /**
     * Minimum height of the test grid's bounding rectangle.
     * The choice of this value must be coordinated the
     * maxGridUnit value to ensure compliance
     * with the "Test Value Coordination" constraints, above.
     * @see <a href="#TestValueCoordination>Test Value Coordination</a>
     */
    private static final float  minRectHeight   = 300;
    /**
     * Maximum height of the test grid's bounding rectangle.
     * @see <a href="#TestValueCoordination>Test Value Coordination</a>
     */
    private static final float  maxRectHeight   = 3000;
    
    /**
     * Minimum grid unit (pixels-per-unit).
     * @see <a href="#TestValueCoordination>Test Value Coordination</a>
     */
    private static final float  minGridUnit     = 5;
    /**
     * Maximum grid unit (pixels-per-unit).
     * The choice of this value must be coordinated the
     * minRectWidth and maxRectWidth values to ensure compliance
     * with the "Test Value Coordination" constraints, above.
     * @see <a href="#TestValueCoordination>Test Value Coordination</a>
     * @see #minRectWidth
     * @see #maxRectWidth
     */
    private static final float  maxGridUnit     = 50;
    /**
     * Minimum LPU (lines-per-unit).
     * Note that the maximum LPU
     * is chosen at run time.
     * @see <a href="#TestValueCoordination>Test Value Coordination</a>
     */
    private static final float  minLPU          = 1;
    
    /////////////////////////////////////////////////////////////////////
    //  TEST VALUES CHOSEN BEFORE THE START OF EACH TEST.
    //  These have been implemented as instance variables for the
    //  convenience of the individual tests.
    //  See beforeEach.
    /////////////////////////////////////////////////////////////////////
    private float       defRectXco;
    private float       defRectYco;
    private float       defRectWidth;
    private float       defRectHeight;
    private Rectangle2D defRect;
    private float       defLPU;
    private float       defGridUnit;
    private LineMetrics defMetrics;
    /////////////////////////////////////////////////////////////////////
    //  END TEST VALUES CHOSEN BEFORE THE START OF EACH TEST.
    /////////////////////////////////////////////////////////////////////
    
    /**
     * Initialization logic executed immediately before
     * the start of each test.
     */
    @BeforeEach
    public void beforeEach()
    {
        defRectXco = nextFloat( minRectXco, maxRectXco );
        defRectYco = nextFloat( minRectYco, maxRectYco );
        defRectWidth = nextFloat( minRectWidth, maxRectWidth );
        defRectHeight = nextFloat( minRectHeight, maxRectHeight );
        defRect = new Rectangle2D.Float( 
            defRectXco, 
            defRectYco, 
            defRectWidth, 
            defRectHeight 
        );
        
        // Don't generate more lines-per-unit than pixels-per-unit.
        defGridUnit = nextFloat( minGridUnit, maxGridUnit );
        defLPU = nextFloat( minLPU, defGridUnit );
        
        defMetrics = new LineMetrics( defRect, defGridUnit, defLPU );
        
        // Validation of generated test values:
        // Make sure generated values conform to required constraints.
        // See "Test Value Coordination" constraints, above.
        assertTrue( minRectWidth > 10 );
        assertTrue( defRectWidth >= minRectWidth );
        assertTrue( minRectHeight > 10 );
        assertTrue( defRectHeight >= minRectHeight );
        float   expGridSpacing  = defGridUnit / defLPU;
        assertTrue( expGridSpacing >= 1 );
        assertTrue( defMetrics.getNumHorizLines() >= 3 );
        assertTrue( defMetrics.getNumVertLines() >= 3 );
        
        // for thorough validation of LineMetrics
        assertEquals( defRect, defMetrics.getRect() );
        assertEquals( defGridUnit, defMetrics.getPpu() );
        assertEquals( defLPU, defMetrics.getLPU() );
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
    public void testLineGeneratorRectangle2DFloatFloatFloatIntH()
    {
        float           expLen  = 5;
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU,
                expLen,
                LineGenerator.HORIZONTAL
            );
        
        // did we get the expected number of lines?
        float   expNumLines = defMetrics.getNumHorizLines();
        float   actNumLines = gen.getTotalHorizontalLines();
        assertEquals( expNumLines, actNumLines );
        
        // do we get at least one line?
        Iterator<Line2D>    iter    = gen.iterator();
        assertTrue( iter.hasNext() );

        Line2D  line    = iter.next();
        float   xco1    = (float)line.getX1();
        float   yco1    = (float)line.getY1();
        float   xco2    = (float)line.getX2();
        float   yco2    = (float)line.getY2();
        
        // is it a horizontal line?
        assertEquals( yco1, yco2 );
        
        // is the line the correct length?
        float   actLen  = xco2 - xco1;
        assertEquals( expLen, actLen );
        
        // are only horizontal lines generated?
        while ( iter.hasNext() )
        {
            Line2D  next    = iter.next();
            assertEquals( next.getY1(), next.getY2() );
        }
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
     * case orientation = VERTICAL
     */
    @Test
    public void testLineGeneratorRectangle2DFloatFloatFloatIntV()
    {
        float           expLen  = 5;
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU,
                expLen,
                LineGenerator.VERTICAL
            );
        
        // did we get the expected number of lines?
        float   expNumLines = defMetrics.getNumVertLines();
        float   actNumLines = gen.getTotalVerticalLines();
        assertEquals( expNumLines, actNumLines );
        
        // do we get at least one line?
        Iterator<Line2D>    iter    = gen.iterator();
        assertTrue( iter.hasNext() );

        Line2D  line    = iter.next();
        float   xco1    = (float)line.getX1();
        float   yco1    = (float)line.getY1();
        float   xco2    = (float)line.getX2();
        float   yco2    = (float)line.getY2();
        
        // is it a vertical line?
        assertEquals( xco1, xco2 );
        
        // is the line the correct length?
        float   actLen  = yco2 - yco1;
        assertEquals( expLen, actLen );
        
        // are only vertical lines generated?
        while ( iter.hasNext() )
        {
            Line2D  next    = iter.next();
            assertEquals( next.getX1(), next.getX2() );
        }
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
     * case orientation = BOTH
     */
    @Test
    public void testLineGeneratorRectangle2DFloatFloatFloatIntB()
    {
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU
            );
        
        // has horizontal and vertical lines?
        assertImplementsBoth( gen );
        
        float   expLen;
        float   actLen;
        // test expected length...
        // before doing so we have to figure out if we're
        // testing a horizontal or vertical line.
        Line2D  line    = gen.iterator().next();
        float   xco1    = (float)line.getX1();
        float   xco2    = (float)line.getX2();
        float   yco1    = (float)line.getY1();
        float   yco2    = (float)line.getY2();
        if ( xco2 > xco1 )
        {
            expLen = defRectWidth;
            actLen = xco2 - xco1;
        }
        else
        {
            expLen = defRectHeight;
            actLen = yco2 - yco1;
        }
        assertEquals( expLen, actLen, epsilon );
    }

    /**
     * Verify that LineGenerator generates the expected vertical lines.
     */
    @RepeatedTest(100)
    public void testIteratorV()
    {
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU,
                -1,
                LineGenerator.VERTICAL
            );
        int             actNumLines         = 0;
        int             nextExpectedLine    = 0;
        List<Line2D>    vLines              = defMetrics.getVertLines();
        int             expNumLines         = vLines.size();
        for ( Line2D actLine : gen )
        {
            assertTrue( nextExpectedLine < expNumLines );
            Line2D  expLine = vLines.get( nextExpectedLine++ );
            assertLineEquals( expLine, actLine, epsilon );
            ++actNumLines;
        }
        assertEquals( expNumLines, actNumLines );
    }

    /**
     * Verify that LineGenerator generates the expected horizontal lines.
     */
    @RepeatedTest(100)
    public void testIteratorH()
    {
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU,
                -1,
                LineGenerator.HORIZONTAL
            );
        int             actNumLines         = 0;
        int             nextExpectedLine    = 0;
        List<Line2D>    hLines              = defMetrics.getHorizLines();
        int             expNumLines         = hLines.size();
        for ( Line2D actLine : gen )
        {
            assertTrue( nextExpectedLine < expNumLines );
            Line2D  expLine = hLines.get( nextExpectedLine++ );
            assertLineEquals( expLine, actLine, epsilon );
            ++actNumLines;
        }
        assertEquals( expNumLines, actNumLines );
    }

    /**
     * Verify LineGenerator.getTotalHorizontalLines.
     */
    @RepeatedTest(1000)
    public void testGetTotalHorizontalLines()
    {
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU,
                -1,
                LineGenerator.HORIZONTAL
            );
        float   expNumLines = defMetrics.getNumHorizLines();
        float   actNumLines = gen.getTotalHorizontalLines();
        assertEquals( expNumLines, actNumLines );
    }

    /**
     * Verify LineGenerator.getTotalVerticalLines.
     */
    @RepeatedTest(1000)
    public void testGetTotalVerticalLines()
    {
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU,
                -1,
                LineGenerator.VERTICAL
            );
        float   expNumLines = defMetrics.getNumVertLines();
        float   actNumLines = gen.getTotalVerticalLines();
        assertEquals( expNumLines, actNumLines );
    }
    
    @ParameterizedTest
    @ValueSource( floats = {1, 1.5f, 2, 2.5f, 3, 3.5f, 4, 4.5f, 5 } )
    public void testLineLength( float expLen )
    {
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU,
                expLen,
                LineGenerator.BOTH
            );
        for ( Line2D actLine : gen )
        {
            Line2D  expLine = defMetrics.getLineSegment( actLine, expLen );
            assertLineEquals( expLine, actLine, epsilon );
        }
    }
    
    /**
     * Verify that the LineGenerator horizontal iterator throws
     * NoSuchElement exception when necessary.
     */
    @Test
    public void goWrongTestHorizontal()
    {
        Class<NoSuchElementException>   clazz   =
            NoSuchElementException.class;
        
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU,
                -1,
                LineGenerator.HORIZONTAL
            );
        Iterator<Line2D>    iter    = gen.iterator();
        // first exhaust the iterator...
        while ( iter.hasNext() )
            iter.next();
        // now blow it away
        assertThrows( clazz, () -> iter.next() );
    }
    
    /**
     * Verify that the LineGenerator vertical iterator throws
     * NoSuchElement exception when necessary.
     */
    @Test
    public void goWrongTestVertical()
    {
        Class<NoSuchElementException>   clazz   =
            NoSuchElementException.class;
        
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU,
                -1,
                LineGenerator.VERTICAL
            );
        Iterator<Line2D>    iter    = gen.iterator();
        // first exhaust the iterator...
        while ( iter.hasNext() )
            iter.next();
        // now blow it away
        assertThrows( clazz, () -> iter.next() );
    }
    
    /**
     * Verify that a given LineGenerator
     * generates both horizontal and vertical lines.
     * @param gen
     */
    private void assertImplementsBoth( LineGenerator gen )
    {
        // did we get the expected number of lines?
        float   expNumLines = 
            defMetrics.getNumVertLines() + defMetrics.getNumHorizLines();
        float   actNumLines = 
             gen.getTotalVerticalLines() + gen.getTotalHorizontalLines();
        assertEquals( expNumLines, actNumLines );
        
        boolean hasHorizontal   = false;
        boolean hasVertical     = false;
        for ( Line2D line : gen )
        {
            if ( line.getY1() == line.getY2() )
                hasHorizontal = true;
            if ( line.getX1() == line.getX2() )
                hasVertical = true;
        }
        // verify horizontal and vertical lines returned
        if ( !hasHorizontal || !hasVertical )
        {
            String  msg = "Expected BOTH; actual: ";
            if ( !hasHorizontal )
                msg += "NOT ";
            msg += "HORIZONTAL, ";
            if ( !hasVertical )
                msg += "NOT ";
            msg += "VERTICAL, ";
            fail( msg );
        }
    }
    
    /**
     * Verify that two lines have the same end points,
     * within a given tolerance.
     * 
     * @param line1     the expected line
     * @param line2     the actual line
     * @param epsilon   the given tolerance
     */
    private void assertLineEquals( Line2D line1, Line2D line2, float epsilon )
    {
        assertEquals( line1.getX1(), line2.getX1(), epsilon );
        assertEquals( line1.getY1(), line2.getY1(), epsilon );
        assertEquals( line1.getX2(), line2.getX2(), epsilon );
        assertEquals( line1.getY2(), line2.getY2(), epsilon );
    }
    
    /**
     * Generate the next float value
     * within a given range
     * as determined by the Random instance for this test.
     * 
     * @param min   the beginning of the range
     * @param max   the end of the range
     * 
     * @return  the next float value within a given range
     * 
     * @see #randy
     */
    private static float nextFloat( float min, float max )
    {
        float   diff    = max - min;
        float   next    = randy.nextFloat();
        next = next * diff + min;
        // validate result
        assertTrue( next >= min );
        assertTrue( next < max );
        return next;
    }
}
