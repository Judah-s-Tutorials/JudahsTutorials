package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineGeneratorTest
{
    private Rectangle2D testRect;
    private float       testGPU;
    private float       testLPU;
    private float       testLen;
    
    @BeforeEach
    void setUp() throws Exception
    {
    }

    @Test
    void testNewLineGenRectangle2DFloatFloat()
    {
        testRect = new Rectangle2D.Double( 100, 200, 300, 400 );
        testGPU = 50;
        testLPU = 1;
        LineGenerator   lineGen = 
            new LineGenerator( testRect, testGPU, testLPU );
        validateAxes( lineGen.axesIterator() );
    }

    @Test
    void testNewLineGenRectangle2DFloatFloatFloat()
    {
        fail("Not yet implemented");
    }

    @Test
    void testNewLineGenRectangle2DFloatFloatFloatInt()
    {
        fail("Not yet implemented");
    }

    @Test
    void testAxesIteratorRectangle2D()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIterator()
    {
        fail("Not yet implemented");
    }

    @Test
    void testAxesIterator()
    {
        testRect = new Rectangle2D.Double( 100, 200, 300, 400 );
        validateAxes( LineGenerator.axesIterator( testRect ) );
    }

    @Test
    void testGetHorLineCount()
    {
        float   height  = 300;
        float   xco     = 100;
        float   yco     = 200;
        testGPU = 50;
        testLPU = 2;
        testRect = new Rectangle2D.Double( xco, yco, 10, height );
        LineGenerator   lineGen = 
            new LineGenerator( testRect, testGPU, testLPU );
        // Precondition: unit boundaries fall at edges of rectangle.
        // Postcondition: number horizontal lines = 
        // height / gpu * lpu - 1 (left edge) - 1 (y-axis)
        int     expCount    = (int)((height / testGPU * testLPU) - 2);
        assertEquals( expCount, lineGen.getHorLineCount() );
        
        testGPU = 55;
        lineGen = new LineGenerator( testRect, testGPU, testLPU );
        // Precondition: unit boundaries fall at edges of rectangle.
        // Postcondition: number horizontal lines = 
        // (int)(height / gpu * lpu) - 1 (y-axis)
        expCount = (int)(height / testGPU * testLPU);
        assertEquals( expCount, lineGen.getHorLineCount() );
    }

    @Test
    void testGetVertLineCount()
    {
        fail("Not yet implemented");
    }

    private void validateOrientation( int orientation )
    {
        
    }
    
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
    
    private void assertLineEquals( Line2D expLine, Line2D actLine )
    {
        assertEquals( expLine.getX1(), actLine.getX1() );
        assertEquals( expLine.getX2(), actLine.getX2() );
        assertEquals( expLine.getY1(), actLine.getY1() );
        assertEquals( expLine.getY2(), actLine.getY2() );
    }
}
