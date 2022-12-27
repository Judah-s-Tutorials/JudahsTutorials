package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class LineGeneratorTest
{
    private static final Random randy           = new Random( 5 );
    private static final float  minRectXco      = 0;
    private static final float  maxRectXco      = 100;
    private static final float  minRectYco      = 0;
    private static final float  maxRectYco      = 100;
    private static final float  minRectWidth    = 300;
    private static final float  maxRectWidth    = 3000;
    private static final float  minRectHeight   = 300;
    private static final float  maxRectHeight   = 3000;
    
    private static final float  minGridUnit     = 5;
    private static final float  maxGridUnit     = 50;
    private static final float  minLPU          = 1;
    
    private float       defRectXco;
    private float       defRectYco;
    private float       defRectWidth;
    private float       defRectHeight;
    private Rectangle2D defRect;
    private float       defLPU;
    private float       defGridUnit;
    private LineMetrics defMetrics;
    
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
        
        // Don't generate more lines-per-unit than
        // there are pixels-per-unit.
        defGridUnit = nextFloat( minGridUnit, maxGridUnit );
        defLPU = nextFloat( minLPU, defGridUnit );
        
        defMetrics = new LineMetrics( defRect, defGridUnit, defLPU );
    }

//    @Test
    void testLineGeneratorRectangle2DFloatFloat()
    {
        fail("Not yet implemented");
    }

    /**
     * Test the constructor that sets all the parameters with no defaults.
     * Specify a specific orientation and a specific line length,
     * verify that both are honored.
     * 
     * case orientation = HORIZONTAL
     */
//    @Test
    void testLineGeneratorRectangle2DFloatFloatFloatIntH()
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
        float   expNumLines = defMetrics.hLines.size();
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
     * 
     * case orientation = VERTICAL
     */
//    @Test
    void testLineGeneratorRectangle2DFloatFloatFloatIntV()
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
        float   expNumLines = defMetrics.vLines.size();
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
     * 
     * case orientation = BOTH
     */
//    @Test
    void testLineGeneratorRectangle2DFloatFloatFloatIntB()
    {
        float           expLen  = 5;
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU,
                expLen,
                LineGenerator.BOTH
            );
        
        // did we get the expected number of lines?
        float   expNumLines = 
            defMetrics.vLines.size() + defMetrics.hLines.size();
        float   actNumLines = 
             gen.getTotalVerticalLines() + gen.getTotalHorizontalLines();
        assertEquals( expNumLines, actNumLines );
        
        // do we get at least one line?
        Iterator<Line2D>    iter    = gen.iterator();
        assertTrue( iter.hasNext() );
    }

    @RepeatedTest(1000)
    void testIteratorH()
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
        List<Line2D>    vLines              = defMetrics.vLines;
        int             expNumLines         = vLines.size();
        for ( Line2D actLine : gen )
        {
            assertTrue( nextExpectedLine < expNumLines );
            Line2D  expLine = defMetrics.vLines.get( nextExpectedLine++ );
            assertLineEquals( expLine, actLine, .001f );
            ++actNumLines;
        }
        assertEquals( expNumLines, actNumLines );
    }

//    @Test
    void testGetTotalHorizontalLines()
    {
        fail("Not yet implemented");
    }

//    @Test
    void testGetTotalVerticalLines()
    {
        fail("Not yet implemented");
    }
    
    private void assertLineEquals( Line2D line1, Line2D line2 )
    {
        final float epsilon = .001f;
        assertEquals( line1.getX1(), line2.getX1() );
        assertEquals( line1.getY1(), line2.getY1() );
        assertEquals( line1.getX2(), line2.getX2() );
        assertEquals( line1.getY2(), line2.getY2() );
    }
    
    private void assertLineEquals( Line2D line1, Line2D line2, float epsilon )
    {
        assertEquals( line1.getX1(), line2.getX1(), epsilon );
        assertEquals( line1.getY1(), line2.getY1(), epsilon );
        assertEquals( line1.getX2(), line2.getX2(), epsilon );
        assertEquals( line1.getY2(), line2.getY2(), epsilon );
    }
    
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
    
    private static void assertFloatGE( double fVal1, double fVal2, String tag )
    {
        final double    epsilon = .001f;
        String          msg     = tag + 1 + "=" + fVal1 + ", " + tag + 2 + "=" + fVal2;
//        double          diff    = fVal1 - fVal2;
//        assertTrue( diff >= -epsilon, msg );
        double  dVal1   = roundToHundredths( fVal1 );
        double  dVal2   = roundToHundredths( fVal2 );
        assertTrue( dVal1 >= dVal2, msg );
    }
    
    private static void assertFloatLT( double fVal1, double fVal2, String tag )
    {
        final double    epsilon = .001f;
        String          msg     = tag + 1 + "=" + fVal1 + ", " + tag + 2 + "=" + fVal2;
        double          diff    = fVal1 - fVal2;
        assertTrue( diff < epsilon, msg );
//        assertTrue( fVal1 < fVal2, msg );
    }
    
    private static boolean doubleEquals( double fVal1, double fVal2 )
    {
        final double    epsilon = .001f;
        double          diff    = Math.abs( fVal1 - fVal2 );
        boolean         result  = diff < epsilon;
        return result;
    }
    
    private static Line2D roundToHundredths( Line2D lineIn )
    {
        float   xco1    = roundToHundredths( lineIn.getX1() );
        float   yco1    = roundToHundredths( lineIn.getY1() );
        float   xco2    = roundToHundredths( lineIn.getX2() );
        float   yco2    = roundToHundredths( lineIn.getY2() );
        Line2D  lineOut = new Line2D.Float( xco1, yco1, xco2, yco2 );
        return lineOut;
    }
    
    private static float roundToHundredths( double fVar )
    {
        float   varOut  = (int)(fVar * 100 + .5);
        varOut /= 100;
        return varOut;
    }

    private static class LineMetrics
    {
        private final List<Line2D>  hLines      = new ArrayList<>();
        private final List<Line2D>  vLines      = new ArrayList<>();
        private final float         centerXco;
        private final float         centerYco;
        
        public LineMetrics(
            Rectangle2D rect,
            float       pixelsPerUnit, 
            float       linesPerUnit
        )
        {
            // pixels between lines
            float   gridSpacing = pixelsPerUnit / linesPerUnit;
            
            // determines location of y-axis
            centerXco = (float)rect.getCenterX();
            // determines location of x-axis
            centerYco = (float)rect.getCenterY();
            
            // horizontal lines above or below x-axis
            float   halfHoriz   = 
                (float)Math.floor( rect.getHeight() / 2 / gridSpacing);
            float   halfVert    = 
                (float)Math.floor( rect.getWidth() / 2 / gridSpacing);
            float   leftXco         = (float)rect.getX();
            float   rightXco        = (float)rect.getWidth() + leftXco;
            float   topYco          = (float)rect.getY();
            float   bottomYco       = (float)rect.getHeight() + topYco;
            
            // generate vertical lines 
            for ( float nextVert = -halfVert ; nextVert <= halfVert ; ++nextVert )
            {
                float   xco = centerXco + nextVert * gridSpacing;
                Line2D  line    = 
                    new Line2D.Float( xco, topYco, xco, bottomYco );
                vLines.add( line );
            }
            
            // generate horizontal lines 
            for ( float nextHoriz = -halfHoriz ; nextHoriz <= halfHoriz ; ++nextHoriz )
            {
                float   yco = centerYco + nextHoriz * gridSpacing;
                Line2D  line    = 
                    new Line2D.Float( leftXco, yco, rightXco, yco );
                hLines.add( line );
            }
        }
        
        public Line2D getLineSegment( Line2D lineIn, float len )
        {
            Line2D  lineOut;
            
            float   lineInXco1  = (float)lineIn.getX1();
            if ( doubleEquals( lineInXco1, lineIn.getX2() ) )
            {
                float   xco     = lineInXco1;
                float   yco1    = centerYco - len / 2;
                float   yco2    = yco1 + len;
                lineOut = new Line2D.Float( xco, yco1, xco, yco2 );
            }
            else
            {
                float   yco     = (float)lineIn.getY1();
                float   xco1    = centerXco - len / 2;
                float   xco2    = xco1 + len;
                lineOut = new Line2D.Float( xco1, yco, xco2, yco );
            }
            return lineOut;
        }
    }
}
