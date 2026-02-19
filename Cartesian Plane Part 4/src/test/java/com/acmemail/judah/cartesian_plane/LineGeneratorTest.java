package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LineGeneratorTest
{
    // These figures are chosen so that the distance between lines and
    // all x- and y-coordinates have easily calculated integer values.
    // We also want spacing to be an even value, so that spacing / 2
    // is an integer value.
    private static final float          gpu         = 150;
    private static final float          lpu         = 3;
    private static final float          spacing     = gpu / lpu;
    // We want to choose even values for width and height. This will
    // ensure that the x- and y-coordinates of the origin are integers.
    // We also want width and height to be different values.
    private static final double         width       = 400;
    private static final double         height      = 300;
    // Choose an even number for length. That way the middle of the line
    // will fall on the x- or y-axis, and there will be the same number
    // of pixels on either side of the axis.
    private static final double         testLen     = 10;
    
    @BeforeAll
    public static void beforeAll()
    {
        assertEquals( Math.floor( gpu ), gpu );
        assertEquals( Math.floor( lpu ), lpu );
        assertEquals( spacing % 2, 0 );
        assertEquals( width % 2, 0 );
        assertEquals( height % 2, 0 );
        assertEquals( testLen % 2, 0 );
        assertNotEquals( width, height );
    }

    @Test
    void testLineGeneratorRectangle2DFloatFloat()
    {
        fail("Not yet implemented");
    }

    @Test
    void testLineGeneratorRectangle2DFloatFloatFloat()
    {
        fail("Not yet implemented");
    }

    @Test
    void testLineGeneratorRectangle2DFloatFloatFloatInt()
    {
        fail("Not yet implemented");
    }

    @Test
    void testAxesIteratorRectangle2D()
    {
        // See testAxesIterator()
    }

    @Test
    void testIterator()
    {
        // See:
        // testSpanning(), testNonSpanning()
    }

    @Test
    void testAxesIterator()
    {
        double      xco = spacing;
        double      yco = 2 * spacing;
        TestParams  params  = new TestParams( xco, yco, -1 );
        params.testAxes();
    }

    @Test
    void testGetLineCount()
    {
        // If xco and yco are even multiples of spacing, the generated
        // lines might fall on the rectangle boundaries. We want to make 
        // sure that doesn't happen.
        double      xco = spacing;
        double      yco = 2 * spacing;
        TestParams  params  = new TestParams( xco, yco, -1 );
        params.testHorizontalCount();
        params.testVerticalCount();
        
        // Improve coverage: make xco and yco not even multiples of spacing.
        xco += spacing / 2;
        yco += spacing / 2;
        params  = new TestParams( xco, yco, -1 );
        params.testHorizontalCount();
        params.testVerticalCount();
    }
    
    /**
     * Test the the generated grid lines;
     * i.e., create line generator with length = -1.
     */
    @Test
    public void testSpanning()
    {
        // If xco and yco are even multiples of spacing, the generated
        // lines might fall on the rectangle boundaries. We want to make 
        // sure that doesn't happen.
        double      xco = spacing;
        double      yco = 2 * spacing;
        TestParams  params  = new TestParams( xco, yco, -1 );
        params.testHorizontal();
        params.testVertical();
        
        // Improve coverage: make xco and yco not even multiples of spacing.
        xco += spacing / 2;
        yco += spacing / 2;
        params  = new TestParams( xco, yco, -1 );
        params.testHorizontal();
        params.testVertical();
    }
    
    /**
     * Test the the generated grid lines;
     * i.e., create line generator with length > 0.
    */
    @Test
    public void testNonSpanning()
    {
        // If xco and yco are even multiples of spacing, the generated
        // lines might fall on the rectangle boundaries. We want to make 
        // sure that doesn't happen.
        double      xco = spacing;
        double      yco = 2 * spacing;
        TestParams  params  = new TestParams( xco, yco, testLen );
        params.testHorizontal();
        params.testVertical();
        
        // Improve coverage: make xco and yco not even multiples of spacing.
        xco += spacing / 2;
        yco += spacing / 2;
        params  = new TestParams( xco, yco, testLen );
        params.testHorizontal();
        params.testVertical();
    }

    private static String getLineSpec(Line2D expLine, Line2D actLine)
    {
        final String    format  = "%s: (%4.1f, %4.1f) -> (%4.1f, %4.1f)";
        String          expStr  = 
            String.format( 
                format,
                "Expected",
                expLine.getX1(),
                expLine.getY1(),
                expLine.getX2(),
                expLine.getY2()
            );
        String          actStr  = 
            String.format( 
                format,
                "Actual",
                actLine.getX1(),
                actLine.getY1(),
                actLine.getX2(),
                actLine.getY2()
            );
        String          lineSpec    = expStr + ", " + actStr;
        return lineSpec;
    }

    /**
     * Determine whether two lines are equal.
     * The two lines are equal if they have the same endpoints.
     * Note that test parameters have been chosen so that
     * all coordinates will be integers, therefore we don't
     * have to worry about rounding errors when comparing two
     * double values.
     *  
     * @param line1
     * @param line2
     * @return
     */
    private static boolean equals( Line2D line1, Line2D line2 )
    {
        boolean result  =
            line1.getX1() == line2.getX1() &&
            line1.getX2() == line2.getX2() &&
            line1.getY1() == line2.getY1() &&
            line1.getY2() == line2.getY2();
        return result;
    }
    
    private static class TestParams
    {
        private final Rectangle2D   rect;
        private final double        originXco;
        private final List<Line2D>  allVert;
        private final double        originYco;
        private final List<Line2D>  allHoriz;
        private final List<Line2D>  axes;
        private final double        minXco;
        private final double        maxXco;
        private final double        leftXco;
        private final double        rightXco;
        private final double        minYco;
        private final double        maxYco;
        private final double        topYco;
        private final double        bottomYco;
        private final double        spacing;
        private final float         length;
        
        public TestParams(
            double      rectXco,
            double      rectYco,
            double      length
        )
        {
            rect = new Rectangle2D.Double( rectXco, rectYco, width, height );
            this.length = (float)length;
            originXco = rect.getCenterX();
            originYco = rect.getCenterY();
            minXco = rect.getMinX();
            maxXco = rect.getMaxX();
            minYco = rect.getMinY();
            maxYco = rect.getMaxY();
            if ( length <= 0 )
            {
                leftXco = minXco;
                rightXco = maxXco;
                topYco = minYco;
                bottomYco = maxYco;
            }
            else
            {
                double  halfLen = length / 2;
                leftXco = originXco - halfLen;
                rightXco = originXco + halfLen;
                topYco = originYco - halfLen;
                bottomYco = originYco + halfLen;
            }
            spacing = gpu / lpu;
            axes = computeAxes( rect );
            allHoriz = computeAllHoriz();
            allVert = computeAllVert();
        }
        
        public void testHorizontalCount()
        {
            int                 orient      = LineGenerator.HORIZONTAL;
            LineGenerator       lineGen     =
                new LineGenerator( rect, gpu, lpu, length, orient );
            int                 expCount    = allHoriz.size();
            int                 actCount    = lineGen.getHorLineCount();
            assertEquals( expCount, actCount );
        }
        
        public void testVerticalCount()
        {
            int                 orient      = LineGenerator.VERTICAL;
            LineGenerator       lineGen     =
                new LineGenerator( rect, gpu, lpu, length, orient );
            int                 expCount    = allVert.size();
            int                 actCount    = lineGen.getVertLineCount();
            assertEquals( expCount, actCount );
        }
        
        public void testHorizontal()
        {
            int                 orient  = LineGenerator.HORIZONTAL;
            LineGenerator       lineGen =
                new LineGenerator( rect, gpu, lpu, length, orient );
            testLines( lineGen, allHoriz );
        }
        
        public void testVertical()
        {
            int                 orient  = LineGenerator.VERTICAL;
            LineGenerator       lineGen =
                new LineGenerator( rect, gpu, lpu, length, orient );
            testLines( lineGen, allVert );
        }
        
        public void testAxes()
        {
            int                 orient  = LineGenerator.BOTH;
            LineGenerator       lineGen =
                new LineGenerator( rect, gpu, lpu, length, orient );
            Iterator<Line2D>    iter    = lineGen.axesIterator();
            testAxesIterator( iter );
            
            iter = LineGenerator.axesIterator( rect );
            testAxesIterator( iter );
        }
        
        private void testAxesIterator( Iterator<Line2D> iter )
        {
            Line2D  expAxisA    = axes.get( 0 );
            Line2D  expAxisB    = axes.get( 1 );
            assertTrue( iter.hasNext() );
            Line2D  actAxis1    = iter.next();
            assertTrue( iter.hasNext() );
            Line2D  actAxis2    = iter.next();
            assertFalse( iter.hasNext() );
            if ( LineGeneratorTest.equals( expAxisA, actAxis1 ) )
            {
                assertTrue( LineGeneratorTest.equals( expAxisB, actAxis2 ) );
            }
            else if ( LineGeneratorTest.equals( expAxisA, actAxis2 ) )
            {
                assertTrue( LineGeneratorTest.equals( expAxisB, actAxis1 ) );
            }
            else
                fail( "expAxisA not found" );
        }
        
        private void testLines( LineGenerator lineGen, List<Line2D> lines )
        {
            Iterator<Line2D>    iter    = lineGen.iterator();
            for ( Line2D expLine : lines )
            {
                assertTrue( iter.hasNext() );
                Line2D  actLine             = iter.next();
                boolean areEqual            = 
                    LineGeneratorTest.equals( expLine, actLine );
                String  lineSpec            = 
                    LineGeneratorTest.getLineSpec( expLine, actLine );
                assertTrue( areEqual, lineSpec );
            }
            assertFalse( iter.hasNext() );
        }
    
        private List<Line2D> computeAxes( Rectangle2D rect )
        {
            List<Line2D>    list    = new ArrayList<>();
            double          minX    = rect.getMinX();
            double          maxX    = rect.getMaxX();
            double          minY    = rect.getMinY();
            double          maxY    = rect.getMaxY();
            Line2D          xAxis   = 
                new Line2D.Double( minX, originYco, maxX, originYco ); 
            Line2D          yAxis   = 
                new Line2D.Double( originXco, minY, originXco, maxY ); 
            list.add( xAxis );
            list.add( yAxis );
            return list;
        }
        
        private List<Line2D> computeAllHoriz()
        {
            List<Line2D>    list    = new LinkedList<>();
            for ( double yco = originYco - spacing ; 
                  yco > minYco ; 
                  yco -= spacing
            )
            {
                Line2D  line    = 
                    new Line2D.Double( leftXco, yco, rightXco, yco );
                list.add( 0, line );
            }
            for ( double yco = originYco + spacing ; 
                yco < maxYco ; 
                yco += spacing
            )
            {
                Line2D  line    = 
                    new Line2D.Double( leftXco, yco, rightXco, yco );
                list.add( line );
            }
            return list;
        }
        
        private List<Line2D> computeAllVert()
        {
            List<Line2D>    list    = new LinkedList<>();
            for ( double xco = originXco - spacing ; 
                  xco > minXco ; 
                  xco -= spacing
            )
            {
                Line2D  line    = 
                    new Line2D.Double( xco, topYco, xco, bottomYco );
                list.add( 0, line );
            }
            for ( double xco = originXco + spacing ; 
                xco < minXco ; 
                xco += spacing
          )
          {
              Line2D  line    = 
                  new Line2D.Double( xco, topYco, xco, bottomYco );
              list.add( line );
          }
            return list;
        }
    }
}
