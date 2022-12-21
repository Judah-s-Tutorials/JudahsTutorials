package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

class LineGeneratorTest
{

    @Test
    void testLineGeneratorRectangle2DFloatFloat()
    {
        fail("Not yet implemented");
    }

    @Test
    void testLineGeneratorRectangle2DFloatFloatFloatInt()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIterator()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetTotalHorizontalLines()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetTotalVerticalLines()
    {
        fail("Not yet implemented");
    }
    
    private void assertLinesEquals( Line2D line1, Line2D line2 )
    {
        final float epsilon = .001f;
        assertEquals( line1.getX1(), line2.getX1(), epsilon );
        assertEquals( line1.getY1(), line2.getY1(), epsilon );
        assertEquals( line1.getX2(), line2.getX2(), epsilon );
        assertEquals( line1.getY2(), line2.getY2(), epsilon );
    }
    
    private static boolean doubleEquals( double fVal1, double fVal2 )
    {
        final double    epsilon = .001f;
        double          diff    = Math.abs( fVal1 - fVal2 );
        boolean         result  = diff < epsilon;
        return result;
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
            float   pixelsPerLine   = pixelsPerUnit * linesPerUnit;
            
            // generate vertical lines left of Y axis
            for ( float xco = hCenterXco ; xco >= leftXco ; xco -= pixelsPerLine )
            {
                Line2D  line    = 
                    new Line2D.Float( xco, topYco, xco, bottomYco );
                vLines.add( 0, line );
            }
            
            // generate vertical lines right of Y axis
            for ( float xco = hCenterXco ; xco < rightXco ; xco += pixelsPerLine )
            {
                Line2D  line    = 
                    new Line2D.Float( xco, topYco, xco, bottomYco );
                vLines.add( 0, line );
            }
            
            // generate horizontal lines above X axis
            for ( float yco = vCenterYco ; yco >= topYco ; yco -= pixelsPerLine )
            {
                Line2D  line    = 
                    new Line2D.Float( leftXco, yco, rightXco, yco );
                hLines.add( 0, line );
            }
            
            // generate horizontal lines below X axis
            for ( float yco = vCenterYco ; yco < bottomYco ; yco += pixelsPerLine )
            {
                Line2D  line    = 
                    new Line2D.Float( leftXco, yco, rightXco, yco );
                hLines.add( 0, line );
            }
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
