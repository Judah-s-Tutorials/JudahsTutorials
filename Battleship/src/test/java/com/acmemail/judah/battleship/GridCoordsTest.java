package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GridCoordsTest
{
    @Test
    void testGridCoords()
    {
        int         expXco      = 10;
        int         expYco      = expXco + 5;
        GridCoords  coords      = new GridCoords( expXco, expYco );
        assertEquals( expXco, coords.getXco() );
        assertEquals( expYco, coords.getYco() );
    }

    @Test
    void testGetXco()
    {
        // See testGridCoords
    }

    @Test
    void testGetYco()
    {
        // See testGridCoords
    }

    @Test
    void testToString()
    {
        int         expXco      = 10;
        int         expYco      = expXco + 5;
        GridCoords  coords      = new GridCoords( expXco, expYco );
        String      strCoords   = coords.toString();
        assertTrue( strCoords.contains( String.valueOf( expXco ) ) );
        assertTrue( strCoords.contains( String.valueOf( expYco ) ) );
    }

    @Test
    void testEqualsHash()
    {
        int         xcoA        = 10;
        int         xcoB        = xcoA + 5;
        int         ycoA        = xcoB + 5;
        int         ycoB        = ycoA + 5;
        GridCoords  coordsA     = new GridCoords( xcoA, ycoA );
        GridCoords  coordsB     = null;
        
        assertTrue( coordsA.equals( coordsA ) );
        assertFalse( coordsA.equals( null ) );
        assertFalse( coordsA.equals( new Object() ) );
        
        coordsB = new GridCoords( xcoB, ycoB );
        assertFalse( coordsA.equals( coordsB ) );
        assertFalse( coordsB.equals( coordsA ) );
        
        coordsB = new GridCoords( xcoA, ycoB );
        assertFalse( coordsA.equals( coordsB ) );
        assertFalse( coordsB.equals( coordsA ) );
        
        coordsB = new GridCoords( xcoB, ycoA );
        assertFalse( coordsA.equals( coordsB ) );
        assertFalse( coordsB.equals( coordsA ) );
        
        coordsB = new GridCoords( xcoA, ycoA );
        assertTrue( coordsA.equals( coordsB ) );
        assertTrue( coordsB.equals( coordsA ) );
        assertEquals( coordsA.hashCode(), coordsB.hashCode() );
    }

}
