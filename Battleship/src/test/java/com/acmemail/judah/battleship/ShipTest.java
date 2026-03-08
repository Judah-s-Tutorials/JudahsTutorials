package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.Test;

class ShipTest
{
    private static final String defNameHor  = "DefNameHorizontal";
    private static final String defNameVert = "DefNameVertical";
    private static final String defTypeHor  = "DefTypeHorizontal";
    private static final String defTypeVert = "DefTypeVertical";
    private static final int    defLenHor   = 5;
    private static final int    defLenVert  = 3;
    private static final int    rowLen      = Grid.getRowLen();
    private static final int    colLen      = Grid.getColLen();
    private static final int    minXco      = 0;
    private static final int    maxXco      = minXco + rowLen;
    private static final int    midXco      = (maxXco - minXco) / 2;
    // Starting x-coordinate of the default horizontal ship
    private static final int    startXco    = midXco - defLenHor / 2;
    private static final int    minYco      = 0;
    private static final int    maxYco      = minYco + colLen;
    private static final int    midYco      = (maxYco - minYco) / 2;
    // Starting y-coordinate of the default horizontal ship
    private static final int    startYco    = midYco - defLenVert / 2;
    private static final Ship   defHor      =
        new Ship(
            defTypeHor,
            defNameHor,
            new OrderedPair( startXco, midYco ),
            defLenHor,
            Orientation.HORIZONTAL
        );
    private static final Ship   defVert     =
        new Ship(
            defTypeVert,
            defNameVert,
            new OrderedPair( startYco, midXco ),
            defLenVert,
            Orientation.VERTICAL
        );

    @Test
    void testShipAllParams()
    {
        assertEquals( defTypeHor, defHor.getType() );
        assertEquals( defNameHor, defHor.getName() );
        assertEquals( startXco, defHor.getMinX() );
        assertEquals( midYco, defHor.getMinY() );
        assertEquals( defLenHor, defHor.getLength() );
        assertEquals( Orientation.HORIZONTAL, defHor.getOrientation() );

        assertEquals( defTypeVert, defVert.getType() );
        assertEquals( defNameVert, defVert.getName() );
        assertEquals( startXco, defVert.getMinX() );
        assertEquals( midYco, defVert.getMinY() );
        assertEquals( defLenVert, defVert.getLength() );
        assertEquals( Orientation.VERTICAL, defVert.getOrientation() );
    }

    @Test
    void testShipDefName()
    {
        assertEquals( defTypeHor, defHor.getType() );
        assertEquals( startXco, defHor.getMinX() );
        assertEquals( midYco, defHor.getMinY() );
        assertEquals( defLenHor, defHor.getLength() );
        assertEquals( Orientation.HORIZONTAL, defHor.getOrientation() );

        assertEquals( defTypeVert, defVert.getType() );
        assertEquals( startXco, defVert.getMinX() );
        assertEquals( midYco, defVert.getMinY() );
        assertEquals( defLenVert, defVert.getLength() );
        assertEquals( Orientation.VERTICAL, defVert.getOrientation() );
    }

    @Test
    void testContainsIntIntHor()
    {
        int minus1Row   = midYco - 1;
        int plus1Row    = midYco + 1;
        int horEnd      = startXco + defLenHor;
        
        // One row above and one below horizontal should be false
        for ( int xco = 0 ; xco < maxXco ; ++xco )
        {
            assertFalse( defHor.contains( xco, minus1Row ), "" + xco );
            assertFalse( defHor.contains( xco, plus1Row ), "" + xco );
        }

        // All columns to left of horizontal start should be false
        for ( int yco = 0 ; yco < maxYco ; ++yco )
            for ( int xco = 0 ; xco < startXco ; ++xco )
                assertFalse( defHor.contains( xco, yco ), "" + xco );

        // All columns to right of horizontal end should be false
        for ( int yco = 0 ; yco < maxYco ; ++yco )
            for ( int xco = horEnd ; xco < maxXco ; ++xco )
                assertFalse( defHor.contains( xco, midYco ), "" + xco );

        // All columns to between horizontal and end should be true
        for ( int xco = startXco ; xco < horEnd ; ++xco )
            assertTrue( defHor.contains( xco, midYco ), "" + xco );
    }

    @Test
    void testContainsIntIntVert()
    {
        int minus1Col   = midXco - 1;
        int plus1Col    = midXco + 1;
        int vertEnd     = startYco + defLenVert;
        
        // One column left and one right of vertical should be false
        for ( int yco = 0 ; yco < maxYco ; ++yco )
        {
            assertFalse( defVert.contains( minus1Col, yco ), "" + yco );
            assertFalse( defVert.contains( plus1Col, yco ), "" + yco );
        }

        // All rows above vertical start should be false
        for ( int yco = 0 ; yco < startYco ; ++yco )
            for ( int xco = 0 ; xco < maxXco ; ++xco )
                assertFalse( defVert.contains( xco, yco ), "" + yco );
        
        // All rows below vertical end should be false
        for ( int yco = vertEnd ; yco < maxYco ; ++yco )
            for ( int xco = 0 ; xco < maxXco ; ++xco )
                assertFalse( defVert.contains( xco, yco ), "" + xco + "," + yco );

        // All columns to between vertical start and end should be true
        for ( int yco = startYco ; yco < vertEnd ; ++yco )
            assertTrue( defVert.contains( midXco, yco ), "" + yco );
    }

    @Test
    void testContainsOrderedPair()
    {
        fail("Not yet implemented");
    }

    @Test
    void testContainsSquare()
    {
        fail("Not yet implemented");
    }

    /*
        Ship        ship,
        Orientation orientation,
        int         length,
        int         rowStart, 
        int         rowEnd, 
        int         colStart, 
        int         colEnd, 
        boolean     expVal

     */
    @Test
    void testIntersectsVH()
    {
        testIntersection(
            defHor, 
            Orientation.VERTICAL, 
            defLenHor,
            0, 
            startYco - 1 - defLenHor,
            0,
            maxXco, 
            false
        );
    }

    @Test
    void testGetMinX()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetMaxX()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetMinY()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetMaxY()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetType()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetName()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetFirstSquare()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetLastSquare()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetLength()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetOrientation()
    {
        fail("Not yet implemented");
    }
    
    private Rectangle2D 
    getTopRect( int length, boolean isVertical, Ship testShip )
    {
        int         testShipLength      = testShip.getLength();
        int         testShipWidth       = 0;
        int         testShipHeight      = 0;
        if ( testShip.getOrientation() == Orientation.HORIZONTAL )
        {
            testShipWidth = length;
            testShipHeight = 0;
        }
        else
        {
            testShipWidth = 0;
            testShipHeight = length;
        }
        
        int testShipFirstXco    = testShip.getMinX();
        int testShipLastXco     = testShip.getMaxX();
        int testShipFirstYco    = testShip.getMinY();
        int testShipLastYco     = testShip.getMaxY();

        int         workShipWidth       = 0;
        int         workShipHeight      = 0;
        if ( isVertical )
        {
            workShipWidth = 0;
            workShipHeight = length;
        }
        else
        {
            workShipWidth = length;
            workShipHeight = 0;
        }
        
        // The last row that the work ship can live in without
        // intersecting the test ship.
        int workShipLastYco = testShipFirstYco - workShipHeight - 1;
        // The last column that the work ship can live in without
        // exceeding the width of the grid.
        int workShipLastXco = colLen - workShipWidth - 1;
        Rectangle   rect    = new Rectangle( 0, 0, workShipLastXco, workShopLastYco );

        OrderedPair testShipTopLeft     = testShip.getFirstSquare();
        OrderedPair testShipBottomRight = testShip.getLastSquare();
        
        // the last row that can contain the work ship
        // without intersecting the test ship.
        
        return null;
    }

    private static void 
    testIntersection(
        Ship        ship,
        Orientation orientation,
        int         length,
        int         rowStart, 
        int         rowEnd, 
        int         colStart, 
        int         colEnd, 
        boolean     expVal
    )
    {
        for ( int yco = rowStart ; yco <= rowEnd ; ++yco )
            for ( int xco = colStart ; xco <= colEnd ; ++xco )
            {
                OrderedPair coords      = new OrderedPair( xco, yco );
                Ship        testShip    = 
                    new Ship( "ship", coords, length, orientation );
                assertEquals( testShip.intersects( ship ), expVal );
            }
    }
}
