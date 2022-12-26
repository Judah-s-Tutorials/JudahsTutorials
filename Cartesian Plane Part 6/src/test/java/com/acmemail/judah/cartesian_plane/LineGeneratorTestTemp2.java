package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class LineGeneratorTestTemp2
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
    
    private static final int    minGridUnit     = 5;
    private static final int    maxGridUnit     = 50;
    private static final int    minLPU          = 1;
    
    private float       defRectXco;
    private float       defRectYco;
    private float       defRectWidth;
    private float       defRectHeight;
    private Rectangle2D defRect;
    private int         defLPU;
    private int         defGridUnit;
    private LineMetrics defMetrics;
    
    @BeforeEach
    public void beforeEach()
    {
        defRectXco = 22.925919f;
        defRectYco = 58.901428f;
        defRectWidth = 1298.028564f;
        defRectHeight = 500f;
        defRect = new Rectangle2D.Float( 
            defRectXco, 
            defRectYco, 
            defRectWidth, 
            defRectHeight 
        );
        
        defGridUnit = 30;
        defLPU = 5;
        
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
//        assertEquals( expNumLines, actNumLines );
        
        // do we get at least one line?
        Iterator<Line2D>    iter    = gen.iterator();
        assertTrue( iter.hasNext() );
    }

//    @RepeatedTest(100)
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
        float   rectXco = round( defRectXco );
        float   rectYco = round( defRectYco );
        float   maxXco  = round( defRectXco + defRectWidth );
        float   maxYco  = round( defRectYco + defRectHeight );
        for ( Line2D lineNext : gen )
        {
            Line2D  line    = round( lineNext );
            assertFloatGE( (float)line.getX1(), rectXco, "X" );
            assertFloatLT( (float)line.getX2(), maxXco, "X" );
            assertFloatGE( (float)line.getY1(), rectYco, "Y" );
            assertFloatLT( (float)line.getY2(), maxYco, "Y" );
        }
    }

//    @RepeatedTest(100)
    @Test
    public void testy()
    {
        float   testHeight  = defRectHeight;
        while ( testHeight < 2000 )
        {
            defRect.setFrame( defRectXco, defRectYco, defRectWidth, testHeight );
            defMetrics = new LineMetrics( defRect, defGridUnit, defLPU );  
            System.out.println( "testHeight=" + testHeight );
            testGetTotalHorizontalLines();
            testHeight += .01;
        }
    }
    void testGetTotalHorizontalLines()
    {
        LineGenerator   gen     =
            new LineGenerator(
                defRect,
                defGridUnit,
                defLPU,
                -1,
                LineGenerator.HORIZONTAL
            );
        float   exp = defMetrics.hLines.size();
        float   act = gen.getTotalHorizontalLines();
//        assertEquals( exp, act );
        Iterator<Line2D>    iter    = gen.iterator();
        String fmt  = "xco=%f, yco=%f%n";
        Line2D  line    = iter.next();
//        System.out.printf( fmt, line.getX1(), line.getY1() );
        while ( iter.hasNext() )
            line = iter.next();
//        System.out.printf( fmt, line.getX1(), line.getY1() );
    }

//    @Test
    void testGetTotalVerticalLines()
    {
        fail("Not yet implemented");
    }
    
    private void assertLinesEqual( Line2D line1, Line2D line2 )
    {
        final float epsilon = .001f;
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
        double  dVal1   = round( fVal1 );
        double  dVal2   = round( fVal2 );
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
    
    private static Line2D round( Line2D lineIn )
    {
        float   xco1    = round( lineIn.getX1() );
        float   yco1    = round( lineIn.getY1() );
        float   xco2    = round( lineIn.getX2() );
        float   yco2    = round( lineIn.getY2() );
        Line2D  lineOut = new Line2D.Float( xco1, yco1, xco2, yco2 );
        return lineOut;
    }
    
    private static float round( double fVar )
    {
        float   varOut  = (int)(fVar + .5);
        return varOut;
    }

    private static class LineMetrics
    {
        private final List<Line2D>  hLines      = new LinkedList<>();
        private final List<Line2D>  vLines      = new LinkedList<>();
        private final float         hCenterXco;
        private final float         vCenterYco;
        
        public LineMetrics(
            Rectangle2D rect,
            float       pixelsPerUnit, 
            float       linesPerUnit
        )
        {
            hCenterXco = (float)rect.getCenterX();
            vCenterYco = (float)rect.getCenterY();
            float   leftXco         = (float)rect.getX();
            float   rightXco        = (float)rect.getWidth() + leftXco;
            float   topYco          = (float)rect.getY();
            float   bottomYco       = (float)rect.getHeight() + topYco;
            float   pixelsPerLine   = pixelsPerUnit / linesPerUnit;
            
            // generate vertical lines left of Y axis
            for ( float xco = hCenterXco ; 
                  xco >= leftXco         ; 
                  xco -= pixelsPerLine 
            )
            {
                Line2D  line    = 
                    new Line2D.Float( xco, topYco, xco, bottomYco );
                vLines.add( 0, line );
            }
            
            // generate vertical lines right of Y axis
            for ( float xco = hCenterXco + pixelsPerLine ; 
                  xco < rightXco                         ;
                  xco += pixelsPerLine
                )
            {
                Line2D  line    = 
                    new Line2D.Float( xco, topYco, xco, bottomYco );
                vLines.add( line );
            }
            
//            System.out.println( "vCenterYco=" + vCenterYco );
            // generate horizontal lines above X axis
            int count   = 0;
            for ( float yco = vCenterYco ; 
                  yco >= topYco          ;
                  yco -= pixelsPerLine
                )
            {
                count++;
                Line2D  line    = 
                    new Line2D.Float( leftXco, yco, rightXco, yco );
                hLines.add( 0, line );
            }
//            System.out.println( count );
            
            // generate horizontal lines below X axis
            for ( float yco = vCenterYco + pixelsPerLine ;
                  yco < bottomYco                        ;
                  yco += pixelsPerLine
                )
            {
                Line2D  line    = 
                    new Line2D.Float( leftXco, yco, rightXco, yco );
                hLines.add( line );
            }
            Line2D  line    = hLines.get( 0 );
//            print( rect, pixelsPerUnit, linesPerUnit, pixelsPerLine, line );
            line    = hLines.get( hLines.size() - 1 );
//            print( rect, pixelsPerUnit, linesPerUnit, pixelsPerLine, line );
        }
        
        private static void print( Rectangle2D defRect, float defGridUnit, float defLPU, float gridSpacing, Line2D line )
        {
            String  fmt =
                "rectXco=%f, rectYco=%f, rectWidth=%f, rectHeight=%f%n"
                + "gridUnit=%f, lpu=%f, gridSpacing=%f%n";
            System.out.printf(
                fmt,
                defRect.getX(),
                defRect.getY(),
                defRect.getWidth(),
                defRect.getHeight(),
                defGridUnit,
                defLPU,
                gridSpacing
            );
            fmt = "xco1 = %f, yco1 = %f, xco2=%f, yco2=%f%n";
            System.out.printf( fmt, line.getX1(), line.getY1(), line.getX2(), line.getY2() );
        }
        
        public Line2D getLineSegment( Line2D lineIn, float len )
        {
            Line2D  lineOut;
            
            float   lineInXco1  = (float)lineIn.getX1();
            if ( doubleEquals( lineInXco1, lineIn.getX2() ) )
            {
                float   xco     = lineInXco1;
                float   yco1    = vCenterYco - len / 2;
                float   yco2    = yco1 + len;
                lineOut = new Line2D.Float( xco, yco1, xco, yco2 );
            }
            else
            {
                float   yco     = (float)lineIn.getY1();
                float   xco1    = hCenterXco - len / 2;
                float   xco2    = xco1 + len;
                lineOut = new Line2D.Float( xco1, yco, xco2, yco );
            }
            return lineOut;
        }
    }
}
