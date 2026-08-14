package com.acmemail.judah.battleship2DT.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class RectUtilsTest
{
    @Test
    void testOfGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    void testCoordsOfPoint()
    {
        fail("Not yet implemented");
    }

    @Test
    void testCoordsOfRectangle()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetInteriorPoints()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetPerimeterPoints()
    {
        int         testXco     = 0;
        int         testYco     = 0;
        int         testWidth   = 5;
        int         testHeight  = 3;
        Rectangle   rect        = 
            new Rectangle( testXco, testYco, testWidth, testHeight );
        
        int minXco  = rect.x - 1;
        int maxXco  = (int)rect.getMaxX() + 1;
        int minYco  = rect.y - 1;
        int maxYco  = (int)rect.getMaxY() + 1;
        
        List<Point> expList     = new ArrayList<>();
        for ( int xco = minXco ; xco < maxXco ; ++ xco )
        {
            expList.add( new Point( xco, minYco ) );
            expList.add( new Point( xco, maxYco - 1 ) );
        }
        // The corners are already present; don't add them again
        for ( int yco = minYco + 1 ; yco < maxYco - 1 ; ++yco )
        {
            expList.add( new Point( minXco, yco ) );
            expList.add( new Point( maxXco - 1, yco ) );
        }
        
        List<Point> actList =
            RectUtils.getPerimeterPoints( rect ).toList();
        
        List<Point> workingList = new ArrayList<>();
        workingList.addAll( expList );
        for ( Point point : actList )
            assertTrue( workingList.remove( point ), point.toString() );
        
        assertTrue( workingList.isEmpty(), workingList.toString() );
    }

    @Test
    void testGetAllCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetAllValidPoints()
    {
        fail("Not yet implemented");
    }

}
