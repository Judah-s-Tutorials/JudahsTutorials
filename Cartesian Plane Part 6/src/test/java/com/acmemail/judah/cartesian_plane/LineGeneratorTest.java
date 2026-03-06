package com.acmemail.judah.cartesian_plane;

import static com.acmemail.judah.cartesian_plane.LineGenerator.BOTH;
import static com.acmemail.judah.cartesian_plane.LineGenerator.HORIZONTAL;
import static com.acmemail.judah.cartesian_plane.LineGenerator.VERTICAL;

import org.junit.jupiter.api.Test;

import util.LineGeneratorTestUtil;

class LineGeneratorTest
{
    private static final LineGeneratorTestUtil  testUtil    =
        new LineGeneratorTestUtil();
    
    @Test
    public void testLineGeneratorRectangle2DFloatFloat()
    {
        testUtil.getLineGenerator( 0, 0, true );
        testUtil.validateCount();
        testUtil.validateLength();
        
        testUtil.getLineGenerator( 0, 0, false );
        testUtil.validateCount();
        testUtil.validateLength();
    }

    @Test
    public void testLineGeneratorRectangle2DFloatFloatFloat()
    {
        testUtil.getLineGenerator( 0, 0, true, -1 );
        testUtil.validateCount();
        testUtil.validateLength();
        
        testUtil.getLineGenerator( 0, 0, false, -1 );
        testUtil.validateCount();
        testUtil.validateLength();
        
        testUtil.getLineGenerator( 0, 0, true, 20 );
        testUtil.validateCount();
        testUtil.validateLength();
        
        testUtil.getLineGenerator( 0, 0, false, 20 );
        testUtil.validateCount();
        testUtil.validateLength();
    }

    @Test
    public void testLineGeneratorRectangle2DFloatFloatFloatInt()
    {
        testUtil.getLineGenerator( 0, 0, true, -1, HORIZONTAL );
        testUtil.validateCount();
        testUtil.validateLength();
        
        testUtil.getLineGenerator( 0, 0, false, -1, VERTICAL );
        testUtil.validateCount();
        testUtil.validateLength();
        
        testUtil.getLineGenerator( 0, 0, false, -1, BOTH );
        testUtil.validateCount();
        testUtil.validateLength();
        
        testUtil.getLineGenerator( 0, 0, true, 20, HORIZONTAL );
        testUtil.validateCount();
        testUtil.validateLength();
        
        testUtil.getLineGenerator( 0, 0, false, 20, VERTICAL );
        testUtil.validateCount();
        testUtil.validateLength();
        
        testUtil.getLineGenerator( 0, 0, false, -1, BOTH );
        testUtil.validateCount();
        testUtil.validateLength();
    }

    @Test
    public void testIteratorTicsHorizontal()
    {
        testUtil.getLineGenerator( 0, 0, false, 20, HORIZONTAL );
        testUtil.validateLines();
        
        testUtil.getLineGenerator( 0, 1, false, 20, HORIZONTAL );
        testUtil.validateLines();
        
        testUtil.getLineGenerator( 1, 0, false, 20, HORIZONTAL );
        testUtil.validateLines();
        
        testUtil.getLineGenerator( 1, 1, false, 20, HORIZONTAL );
        testUtil.validateLines();  
    }

    @Test
    public void testIteratorTicsVertical()
    {
        testUtil.getLineGenerator( 0, 0, false, 20, VERTICAL );
        testUtil.validateLines();
        
        testUtil.getLineGenerator( 0, 1, false, 20, VERTICAL );
        testUtil.validateLines();
        
        testUtil.getLineGenerator( 1, 0, false, 20, VERTICAL );
        testUtil.validateLines();
        
        testUtil.getLineGenerator( 1, 1, false, 20, VERTICAL );
        testUtil.validateLines();  
    }

    @Test
    public void testIteratorTicsBoth()
    {
        testUtil.getLineGenerator( 0, 0, false, 20, BOTH );
        testUtil.validateLines();
        
        testUtil.getLineGenerator( 0, 1, false, 20, BOTH );
        testUtil.validateLines();
        
        testUtil.getLineGenerator( 1, 0, false, 20, BOTH );
        testUtil.validateLines();
        
        testUtil.getLineGenerator( 1, 1, false, 20, BOTH );
        testUtil.validateLines();  
    }

    @Test
    public void testAxesIteratorRectangle2D()
    {
        testUtil.getLineGenerator( 0, 0, true, -1, HORIZONTAL );
        testUtil.validateClassAxesIterator();
    }

    @Test
    public void testAxesIterator()
    {
        testUtil.getLineGenerator( 0, 0, true, -1, HORIZONTAL );
        testUtil.validateInstanceAxesIterator();
    }

    @Test
    public void testGetHorLineCount()
    {
        testUtil.getLineGenerator( 0, 0, false );
        testUtil.validateGetHorizontalCount();

        testUtil.getLineGenerator( 0, 1, false );
        testUtil.validateGetHorizontalCount();

        testUtil.getLineGenerator( 1, 0, false );
        testUtil.validateGetHorizontalCount();

        testUtil.getLineGenerator( 1, 1, false );
        testUtil.validateGetHorizontalCount();        
    }

    @Test
    public void testGetVertLineCount()
    {
        testUtil.getLineGenerator( 0, 0, false );
        testUtil.validateGetVerticalCount();

        testUtil.getLineGenerator( 0, 1, false );
        testUtil.validateGetVerticalCount();

        testUtil.getLineGenerator( 1, 0, false );
        testUtil.validateGetVerticalCount();

        testUtil.getLineGenerator( 1, 1, false );
        testUtil.validateGetVerticalCount();
    }

    @Test
    public void testHorizontalTics()
    {
        testUtil.getLineGenerator( 0, 0, true, 10, HORIZONTAL );
        testUtil.validateLines();
        testUtil.validateCount();
    }

    @Test
    public void testHorizontalGridLines()
    {
        testUtil.getLineGenerator( 0, 0, true, -1, HORIZONTAL );
        testUtil.validateLines();
        testUtil.validateCount();
    }

    @Test
    public void testVerticalTics()
    {
        testUtil.getLineGenerator( 0, 0, true, 10, HORIZONTAL );
        testUtil.validateLines();
        testUtil.validateCount();
    }

    @Test
    public void testVerticalGridLines()
    {
        testUtil.getLineGenerator( 0, 0, true, -1, HORIZONTAL );
        testUtil.validateLines();
    }

    @Test
    public void testBothTics()
    {
        testUtil.getLineGenerator( 0, 0, true, 10, HORIZONTAL );
        testUtil.validateLines();
    }

    @Test
    public void testBothGridLines()
    {
        testUtil.getLineGenerator( 0, 0, true, -1, HORIZONTAL );
        testUtil.validateLines();
    }
}
