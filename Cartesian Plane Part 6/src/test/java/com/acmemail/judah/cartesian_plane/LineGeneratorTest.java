package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LineGeneratorTest
{
    /** 
     * Base x-coordinate for the rectangle in which line drawing
     * will take place.
     */
    private static final int    baseRectXco     = 100;
    /** 
     * Base y-coordinate for the rectangle in which line drawing
     * will take place. Deliberately chosen to be different from
     * the base x-coordinate.
     */
    private static final int    baseRectYco     = 2 * baseRectXco;
    /** 
     * Base width for the rectangle in which line drawing
     * will take place.
     */
    private static final float  baseWidth       = 200;
    /** 
     * Base height for the rectangle in which line drawing
     * will take place. Deliberately chosen to be different from
     * the base width.
     */
    private static final float  baseHeight      = baseWidth + 100;
    /** 
     * Base GPU (pixels-per-unit) for drawing test lines.
     */
    private static final float  baseGPU         = 50;
    /** 
     * Base LPU (lines-per-unit) for drawing test lines.
     */
    private static final float  baseLPU         = 1;
    /** 
     * Base length for drawing test lines.
     */
    private static final float  baseLen         = 15;
    
    /** 
     * Rectangle to be used for testing. Always initialized to 
     * base parameters in the before-each method. May be changed
     * during during testing.
     * @see #makeRect().
     */
    private Rectangle2D testRect;
    /** 
     * Width of the rectangle to be used for testing. Always initialized 
     * baseWidth in the before-each method. May be changed
     * during during testing.
     * @see #makeRect().
     */
    private float       testWidth;
    /** 
     * Height of the rectangle to be used for testing. Always initialized 
     * baseHeight in the before-each method. May be changed
     * during during testing.
     * @see #makeRect().
     */
    private float       testHeight;
    /** 
     * Default GPU for testing. Always initialized baseGPU in the 
     * before-each method. May be changed during during testing.
     * @see #setUp().
     */
    private float       testGPU;
    /** 
     * Default LPU for testing. Always initialized baseLPU in the 
     * before-each method. May be changed during during testing.
     * @see #setUp().
     */
    private float       testLPU;
    /** 
     * Default line length for testing. Always initialized baseLen in the 
     * before-each method. May be changed during during testing.
     * @see #setUp().
     */
    private float       testLen;
    
    @BeforeEach
    public void setUp() throws Exception
    {
        testWidth = baseWidth;
        testHeight = baseHeight;
        testGPU = baseGPU;
        testLPU = baseLPU;
        testLen = baseLen;
        makeRect();
    }

    @Test
    public void testNewLineGenRectangle2DFloatFloat()
    {
        float   dim     = 300;
        testWidth = dim;
        testHeight = dim;
        makeRect();
        LineGenerator   lineGen = 
            new LineGenerator( testRect, testGPU, testLPU );
        validateAxes( lineGen.axesIterator() );
        validateLength( lineGen.iterator(), dim );
        validateOrientation( lineGen, LineGenerator.BOTH );
    }

    @Test
    public void testNewLineGenRectangle2DFloatFloatFloat()
    {
        int     expHorCount     = 4;
        int     expVertCount    = 8;
        testWidth = calcFromExpCount( expVertCount );
        testHeight = calcFromExpCount( expHorCount );

        makeRect();
        for ( int len = 2 ; len < 50 ; ++len )
        {
            LineGenerator   lineGen = 
                new LineGenerator( testRect, testGPU, testLPU, len );
            assertEquals( expHorCount, lineGen.getHorLineCount() );
            assertEquals( expVertCount, lineGen.getVertLineCount() );
            validateAxes( lineGen.axesIterator() );
            validateLength( lineGen.iterator(), len );
            validateOrientation( lineGen, LineGenerator.BOTH );
        }
    }

    @Test
    public void testNewLineGenRectangle2DFloatFloatMinus1()
    {
        int     expCount    = 4;
        float   dim         = calcFromExpCount( expCount );

        testWidth = dim;
        testHeight = dim;
        makeRect();
        LineGenerator   lineGen = 
            new LineGenerator( testRect, testGPU, testLPU, -1 );
        assertEquals( expCount, lineGen.getHorLineCount() );
        assertEquals( expCount, lineGen.getVertLineCount() );
        validateAxes( lineGen.axesIterator() );
        validateLength( lineGen.iterator(), dim );
        validateOrientation( lineGen, LineGenerator.BOTH );
    }

    @ParameterizedTest
    @ValueSource( ints= {
        LineGenerator.HORIZONTAL, 
        LineGenerator.VERTICAL,
        LineGenerator.BOTH
    })
    public void 
    testNewLineGenRectangle2DFloatFloatFloatInt( int orientation )
    {
        int     expHorCount     = 4;
        int     expVertCount    = 8;
        testWidth  = calcFromExpCount( expVertCount );
        testHeight = calcFromExpCount( expHorCount );

        makeRect();
        LineGenerator   lineGen = new LineGenerator(
            testRect, 
            testGPU, 
            testLPU, 
            testLen, 
            orientation 
        );
        assertEquals( expHorCount, lineGen.getHorLineCount() );
        assertEquals( expVertCount, lineGen.getVertLineCount() );
        validateAxes( lineGen.axesIterator() );
        validateLength( lineGen.iterator(), testLen );
        validateOrientation( lineGen, orientation );
    }

    @ParameterizedTest
    @ValueSource( ints= {
        LineGenerator.HORIZONTAL, 
        LineGenerator.VERTICAL,
        LineGenerator.BOTH
    })
    public void 
    testNewLineGenRectangle2DFloatFloatMinus1Int( int orientation )
    {
        int     expCount    = 4;
        float   dim         = calcFromExpCount( expCount );
        testWidth  = dim;
        testHeight = dim;

        makeRect();
        LineGenerator   lineGen = new LineGenerator(
            testRect, 
            testGPU, 
            testLPU, 
            -1, 
            orientation 
        );
        assertEquals( expCount, lineGen.getHorLineCount() );
        assertEquals( expCount, lineGen.getVertLineCount() );
        validateAxes( lineGen.axesIterator() );
        validateLength( lineGen.iterator(), dim );
        validateOrientation( lineGen, orientation );
    }

    @Test
    public void testAxesIteratorRectangle2D()
    {
        Iterator<Line2D>    iter    =
            LineGenerator.axesIterator( testRect );
        validateAxes( iter );
    }

    @Test
    public void testIterator()
    {
        int     expHorCount     = 4;
        int     expVertCount    = 8;
        testWidth = calcFromExpCount( expVertCount );
        testHeight = calcFromExpCount( expHorCount );
        makeRect();
        LineGenerator   lineGen = 
            new LineGenerator( testRect, testGPU, testLPU, baseLen );


        List<Line2D>    expList = new ArrayList<Line2D>();  
        int     halfHorCount    = expHorCount / 2;
        IntStream.rangeClosed( -halfHorCount, halfHorCount)
            .filter( i -> i != 0 )
            .mapToDouble( i -> i * testGPU )
            .mapToObj( this::getHorLine )
            .forEach( expList::add );
        
        int     halfVertCount   = expVertCount / 2;
        IntStream.rangeClosed( -halfVertCount, halfVertCount)
            .filter( i -> i != 0 )
            .mapToDouble( i -> i * testGPU )
            .mapToObj( this::getVertLine )
            .forEach( expList::add );

        List<Line2D>    actList = new ArrayList<Line2D>(); 
        lineGen.forEach( actList::add );
        
        Comparator<Line2D>  comp    = 
            (l1, l2) -> l1.getX1() == l1.getX2() ? 1 : 0;
        expList.sort( comp );
        actList.sort( comp );
        expList.forEach( l -> System.out.println( l.getP1() + "," +l.getP2() ) );
        System.out.println( "========" );
        actList.forEach( l -> System.out.println( l.getP1() + "," +l.getP2() ) );
        int             expSize = expList.size();
        actList.forEach( l -> System.out.println( l.getP1() + "," +l.getP2() ) );
        assertEquals( expSize, actList.size() );
        IntStream.range( 0, expSize )
            .forEach( i -> 
                assertLineEquals( expList.get( i ), actList.get( i ) )
            );
        
    }

    @Test
    public void testAxesIterator()
    {
        LineGenerator   lineGen = 
            new LineGenerator( testRect, testGPU, testLPU );
        validateAxes( lineGen.axesIterator() );
    }

    @Test
    public void testGetHorLineCount()
    {
        testLPU = 2f;
        String  comment = 
            "GPU=" + testGPU + ",LPU=" + testLPU + ",Height=";
        for ( float height = 10 ; height < 500 ; ++height )
        {
            testHeight = height;
            makeRect();
            LineGenerator   lineGen = 
                new LineGenerator( testRect, testGPU, testLPU );
            float   halfHeight  = height / 2;
            int     halfCount   = (int)((halfHeight / testGPU) * testLPU );
            if ( (height % testGPU) == 0 )
                --halfCount;
            int     expCount    = halfCount * 2;
            int     actCount    = (int)lineGen.getHorLineCount();
            assertEquals( expCount, actCount, comment + height );
        }
    }

    @Test
    public void testGetVertLineCount()
    {
        testHeight = 500;
        testLPU = 2f;
        String  comment = 
            "GPU=" + testGPU + ",LPU=" + testLPU + ",Width=";
        for ( float width = 10 ; width < 500 ; ++width )
        {
            testWidth = width;
            makeRect();
            LineGenerator   lineGen = 
                new LineGenerator( testRect, testGPU, testLPU );
            float   halfHeight  = testHeight / 2;
            int     halfCount   = (int)((halfHeight / testGPU) * testLPU );
            if ( (testHeight % testGPU) == 0 )
                --halfCount;
            int     expCount    = halfCount * 2;
            int     actCount    = (int)lineGen.getHorLineCount();
            assertEquals( expCount, actCount, comment + width );
        }
    }
    
    private void validateLength( Iterator<Line2D> lineIter, double expLen )
    {
        int count   = 0;
        while ( lineIter.hasNext() )
        {
            ++count;
            Line2D  line    = lineIter.next();
            Point2D point1  = line.getP1();
            Point2D point2  = line.getP2();
            double  actLen  = 0;
            if ( point1.getX() == point2.getX() )
            {
                actLen = point2.getY() - point1.getY();
            }
            else
            {
                assertEquals( point1.getY(), point2.getY() );
                actLen = point2.getX() - point1.getX();
            }
            assertEquals( expLen, actLen );
        };
        assertTrue( count > 0 );
    }

    private void 
    validateOrientation( LineGenerator lineGen, int orientation )
    {
        boolean             actHasHor   = false;
        boolean             actHasVert  = false;
        Iterator<Line2D>    iter        = lineGen.iterator();
        while ( iter.hasNext() )
        {
            Line2D  line    = iter.next();
            Point2D point1  = line.getP1();
            Point2D point2  = line.getP2();
            if ( point1.getX() == point2.getX() )
                actHasVert = true;
            else if ( point1.getY() == point2.getY() )
                actHasHor = true;
            else
                fail( "Strange orientation" );
        }
        
        boolean expHasHor   = 
            (orientation & LineGenerator.HORIZONTAL) != 0;
        boolean expHasVert  = 
            (orientation & LineGenerator.VERTICAL) != 0;
        assertEquals( expHasHor, actHasHor );
        assertEquals( expHasVert, actHasVert );
    }
    
    /**
     * Verify that the given iterator
     * correctly generates 
     * the position and length
     * of the axes.
     * 
     * @param iter  the given iterator
     */
    private void validateAxes( Iterator<Line2D> iter )
    {
        double  centerY = testRect.getCenterY();
        Point2D xco1    = 
            new Point2D.Double( testRect.getMinX(), centerY );
        Point2D xco2    = 
            new Point2D.Double( testRect.getMaxX(), centerY );
        Line2D  xAxis   = new Line2D.Double( xco1, xco2 );

        double  centerX = testRect.getCenterX();
        Point2D yco1    = 
            new Point2D.Double( centerX, testRect.getMinY() );
        Point2D yco2    = 
            new Point2D.Double( centerX, testRect.getMaxY() );
        Line2D  yAxis   = new Line2D.Double( yco1, yco2 );
        
        Comparator<Line2D>  comp    = 
            (l1, l2) -> (int)(l1.getX1() - l1.getX2());
        List<Line2D> expSet  = new ArrayList<>();
        expSet.add( xAxis );
        expSet.add( yAxis );
        expSet.sort( comp );
        
        List<Line2D> actSet  = new ArrayList<>();
        actSet.add( iter.next() );
        actSet.add( iter.next() );
        assertFalse( iter.hasNext() );
        actSet.sort( comp );
        
        assertLineEquals( expSet.get( 0 ), actSet.get( 0 ) );
        assertLineEquals( expSet.get( 1 ), actSet.get( 1 ) );
    }
    
    /**
     * Verify that a given generated line
     * has the expected coordinates.
     * 
     * @param actLine   the given generated line
     * @param expLine   the expected coordinates.
     */
    private void assertLineEquals( Line2D expLine, Line2D actLine )
    {
        assertEquals( expLine.getX1(), actLine.getX1() );
        assertEquals( expLine.getX2(), actLine.getX2() );
        assertEquals( expLine.getY1(), actLine.getY1() );
        assertEquals( expLine.getY2(), actLine.getY2() );
    }
    
    /**
     * This method
     * generates a dimension
     * (height or width)
     * for the test rectangle
     * based on the expected number 
     * of horizontal or vertical lines
     * in that dimension.
     * The calculated value
     * is expected to be 
     * more than big enough
     * to accommodate the expected count,
     * but less then enough
     * to accommodate 
     * the expected count + 1.
     * The intent is to avoid
     * testing against a dimension
     * that is exactly the right size
     * for the expected count.
     * 
     * @param expCount
     *      the expected number of vertical or horizontal lines
     *      
     * @return  
     *      a value slightly greater than that to accommodate
     *      the expected number of lines
     */
    private float calcFromExpCount( int expCount )
    {
        testGPU = baseGPU;
        testLPU = 1;
        float   dim = expCount * (1.1f * testGPU);
        return dim;
    }
    
    /**
     * Create the default test rectangle
     * from the default test parameters.
     */
    private void makeRect()
    {
        testRect = new Rectangle2D.Double( 
            baseRectXco, 
            baseRectYco, 
            testWidth, 
            testHeight
        );
    }
    
    /**
     * Given the offset from the y-axis
     * to an x-coordinate,
     * generate a vertical line for that coordinate
     * given the current test rectangle
     * and test length.
     * 
     * @param xOffset   the given offset
     * 
     * @return  the generated vertical line
     */
    private Line2D getVertLine( double xOffset )
    {
        double  centerX = testRect.getCenterX();
        double  centerY = testRect.getCenterY();
        double  xco     = centerX + xOffset;
        double  halfLen = testLen / 2;
        double  yco1    = centerY - halfLen;
        double  yco2    = centerY + halfLen;
        Point2D point1  = new Point2D.Double( xco, yco1 );
        Point2D point2  = new Point2D.Double( xco, yco2 );
        Line2D  line    = new Line2D.Double( point1, point2 );
        return line;
    }
    
    /**
     * Given the offset from the x-axis
     * to a y-coordinate,
     * generate a horizontal line for that coordinate
     * given the current test rectangle
     * and test length.
     * 
     * @param yOffset   the given offset
     * 
     * @return  the generated vertical line
     */
    private Line2D getHorLine( double yOffset )
    {
        double  centerX = testRect.getCenterX();
        double  centerY = testRect.getCenterY();
        double  yco     = centerY + yOffset;
        double  halfLen = testLen / 2;
        double  xco1    = centerX - halfLen;
        double  xco2    = centerX + halfLen;
        Point2D point1  = new Point2D.Double( xco1, yco );
        Point2D point2  = new Point2D.Double( xco2, yco );
        Line2D  line    = new Line2D.Double( point1, point2 );
        return line;
    }
}
