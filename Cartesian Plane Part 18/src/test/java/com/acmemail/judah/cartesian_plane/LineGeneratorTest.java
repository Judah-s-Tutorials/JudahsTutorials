package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineGeneratorTest
{
    private static final float  baseRectXco     = 100;
    private static final float  baseRectYco     = 200;
    private static final float  baseRectWidth   = 105;
    private static final float  baseRectHeight  = 125;
    private static final float  baseGPU         = 100;
    private static final float  baseLPU         = 2;
    private static final float  baseLength      = 21;

    private Rectangle2D baseRect;
    
    @BeforeEach
    void setUp() throws Exception
    {
        baseRect = new Rectangle2D.Float( 
            baseRectXco,
            baseRectYco,
            baseRectWidth,
            baseRectHeight
        );
    }

    @Test
    void testLineGeneratorRectangle2DFloatFloat()
    {
        fail("Not yet implemented");
    }

    @Test
    void testLineGeneratorRectangle2DFloatFloatFloatIntH()
    {
//        float       decimalHLines   
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

    @Test
    void testIsXAxis()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsYAxis()
    {
        fail("Not yet implemented");
    }

    private class Parameters
    {
        public final Rectangle2D rect;
        public final float  gpu;
        public final float  lpu;
        public final float  length;
        
        public final float  originXco;
        public final float  originYco;

        public final float  decHorUnits;
        public final int    intHorUnits;
        public final float  decHorLines;
        public final int    intHorLines;
        public final float  decVertUnits;
        public final int    intVertUnits;
        public final float  decVertLines;
        public final int    intVertLines;
        public final int    intLength;
        
        public 
        Parameters( Rectangle2D rect, float gpu, float lpu, float length )
        {
            this.rect = rect;
            this.gpu = gpu;
            this.lpu = lpu;
            this.length = length;
            
            originYco = (float)rect.getHeight() / 2;
            originXco = (float)rect.getWidth() / 2;
            
            decHorUnits = (float)rect.getHeight() / gpu;
            intHorUnits = (int)decHorUnits;
            decHorLines = (float)decHorUnits * lpu;
            intHorLines = (int)decHorLines;

            decVertUnits = (float)rect.getWidth() / gpu;
            intVertUnits = (int)decVertUnits;
            decVertLines = (float)decVertUnits * lpu;
            intVertLines = (int)decVertLines;   
            
            intLength = (int)length;
        }
    }
}
