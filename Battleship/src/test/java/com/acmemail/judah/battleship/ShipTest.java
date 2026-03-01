package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ShipTest
{
    @Test
    void testShip()
    {
        fail("Not yet implemented");
    }

    @Test
    void testContainsIntInt()
    {
        fail("Not yet implemented");
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

    @Test
    void testIntersectsVH()
    {
        int         rowLen  = Grid.getRowLen();
        int         colLen  = Grid.getColLen();
        String      type    = "5";
        int         length  = 5;
        
        int         hYco    = colLen / 2;
        int         hXco1   = rowLen / 2 - length / 2;
        int         hXco2   = hXco1 + length;
        OrderedPair hStart  = new OrderedPair( hXco1, hYco );
        Ship        hShip   = 
            new Ship( type, hStart, length, Orientation.HORIZONTAL );
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

}
