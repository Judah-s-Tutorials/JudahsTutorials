package com.acmemail.judah.battleship;

import static com.acmemail.judah.battleship.Orientation.HORIZONTAL;
import static com.acmemail.judah.battleship.Orientation.VERTICAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

class ShipTest
{
    private static final String     defNameHor  = "DefNameHorizontal";
    private static final String     defNameVer  = "DefNameVertical";
    private static final ShipType   defTypeHor  = ShipType.CARRIER;
    private static final ShipType   defTypeVer  = ShipType.CRUISER;
    private static final int        defLenHor   = defTypeHor.getLength();
    private static final int        defLenVer   = defTypeVer.getLength();
    private static final int        rowLen      = 10; //Grid.getRowLen();
    private static final int        colLen      = 10; //Grid.getColLen();
    private static final int        minXco      = 0;
    private static final int        maxXco      = minXco + colLen;
    private static final int        midXco      = (maxXco - minXco) / 2;
    private static final int        minYco      = 0;
    private static final int        maxYco      = minYco + rowLen;
    private static final int        midYco      = (maxYco - minYco) / 2;
    // Starting x- and y-coordinates of the default horizontal ship
    private static final int        horStartXco = midXco - defLenHor / 2;
    private static final int        horStartYco = midYco;
    // Starting x- and y-coordinates of the default horizontal ship
    private static final int        verStartXco = midXco;
    private static final int        verStartYco = midYco - defLenVer / 2;
    // Starting y-coordinate of the default horizontal ship
    private static final Ship       defHor      =
        new Ship(
            defTypeHor,
            defNameHor,
            new OrderedPair( horStartXco, midYco ),
            Orientation.HORIZONTAL
        );
    private static final Ship   defVer      =
        new Ship(
            defTypeVer,
            defNameVer,
            new OrderedPair( verStartXco, verStartYco ),
            Orientation.VERTICAL
        );

    @Test
    void testShipAllParams()
    {
        assertEquals( defTypeHor, defHor.getType() );
        assertEquals( defNameHor, defHor.getName() );
        assertEquals( horStartXco, defHor.getMinX() );
        assertEquals( midYco, defHor.getMinY() );
        assertEquals( defLenHor, defHor.getLength() );
        assertEquals( Orientation.HORIZONTAL, defHor.getOrientation() );

        assertEquals( defTypeVer, defVer.getType() );
        assertEquals( defNameVer, defVer.getName() );
        assertEquals( horStartXco, defVer.getMinX() );
        assertEquals( midYco, defVer.getMinY() );
        assertEquals( defLenVer, defVer.getLength() );
        assertEquals( Orientation.VERTICAL, defVer.getOrientation() );
    }

    @Test
    void testShipDefName()
    {
        assertEquals( defTypeHor, defHor.getType() );
        assertEquals( horStartXco, defHor.getMinX() );
        assertEquals( midYco, defHor.getMinY() );
        assertEquals( defLenHor, defHor.getLength() );
        assertEquals( Orientation.HORIZONTAL, defHor.getOrientation() );

        assertEquals( defTypeVer, defVer.getType() );
        assertEquals( horStartXco, defVer.getMinX() );
        assertEquals( midYco, defVer.getMinY() );
        assertEquals( defLenVer, defVer.getLength() );
        assertEquals( Orientation.VERTICAL, defVer.getOrientation() );
    }

    @Test
    void testContainsOrderedPair()
    {
        testContainsOrderedPair( defHor );
        testContainsOrderedPair( defVer );
    }
    
    private void testContainsOrderedPair( Ship ship )
    {
        int minRow  = ship.getMinY();
        int maxRow  = ship.getMaxY();
        int minCol  = ship.getMinX();
        int maxCol  = ship.getMaxX();
        for ( int row = minRow ; row < maxRow ; ++row )
            for ( int col = minCol ; col < maxCol ; ++col )
            {
                OrderedPair pair    = new OrderedPair( col, row );
                assertTrue( ship.contains( pair ) );
            }
        
        int rowLow  = minRow - 1;
        int rowHigh = maxRow;
        for ( int col = 0 ; col < colLen ; ++col )
        {
            OrderedPair pairLow     = new OrderedPair( col, rowLow );
            OrderedPair pairHigh    = new OrderedPair( col, rowHigh );
            assertFalse( ship.contains( pairLow ) );
            assertFalse( ship.contains( pairHigh ) );
        }
        
        int colLow  = minCol - 1;
        int colHigh = maxCol;
        for ( int row = 0 ; row < rowLen ; ++row )
        {
            OrderedPair pairLow     = new OrderedPair( colLow, row );
            OrderedPair pairHigh    = new OrderedPair( colHigh, row );
            assertFalse( ship.contains( pairLow ) );
            assertFalse( ship.contains( pairHigh ) );
        }
    }
    
    @Test
    public void testIntersects()
    {
        testIntersects( VERTICAL, defHor );
        testIntersects( VERTICAL, defVer );
        testIntersects( HORIZONTAL, defHor );
        testIntersects( HORIZONTAL, defVer );
    }

    private void testIntersects( Orientation orient, Ship testShip )
    {
        Rectangle   rect    = getIntersectionRect( orient, testShip );
        runIntersectTest( rect, orient, testShip );
    }
    
    private void 
    runIntersectTest( Rectangle rect, Orientation orient, Ship testShip )
    {
        BiFunction<Integer,Integer,Ship>    shipGetter  =
            orient == HORIZONTAL ?
                (x,y) -> getHorizontalShip( x, y ) :
                (x,y) -> getVerticalShip( x, y );
        int         minCol      = rect.x;
        int         minRow      = rect.y;
        int         maxCol      = rect.width;
        int         maxRow      = rect.height;
        for ( int row = minRow ; row < maxRow ; ++row )
            for ( int col = minCol ; col < maxCol ; ++col )
            {
                Ship    ship    = shipGetter.apply( col, row );
                System.out.println( ship );
                String  comment = getComment( ship, testShip );
                assertTrue( ship.intersects( testShip ), comment );
                assertTrue( testShip.intersects( ship ), comment );
            }
        System.out.println( testShip );
    }

    @Test
    public void testIntersectsNeg()
    {
        testIntersectsNeg( VERTICAL, defHor );
        testIntersectsNeg( HORIZONTAL, defVer );
        testIntersectsNeg( VERTICAL, defVer );
        testIntersectsNeg( VERTICAL, defHor );
    }

    private void testIntersectsNeg( Orientation orient, Ship testShip )
    {
        List<Rectangle> allRects    = 
            getNegIntersectionRects( orient, testShip );
        for ( Rectangle rect : allRects )
            runNegIntersectTest( rect, orient, testShip );
    }
    
    private void 
    runNegIntersectTest( Rectangle rect, Orientation orient, Ship testShip )
    {
        BiFunction<Integer,Integer,Ship>    shipGetter  =
            orient == HORIZONTAL ?
                (x,y) -> getHorizontalShip( x, y ) :
                (x,y) -> getVerticalShip( x, y );
        int         minCol      = rect.x;
        int         minRow      = rect.y;
        int         maxCol      = rect.width;
        int         maxRow      = rect.height;
        for ( int row = minRow ; row < maxRow ; ++row )
            for ( int col = minCol ; col < maxCol ; ++col )
            {
                Ship    ship    = shipGetter.apply( col, row );
                String  comment = getComment( ship, testShip );
                assertFalse( ship.intersects( testShip ), comment );
                assertFalse( testShip.intersects( ship ), comment );
            }
    }
    
    private static String getComment( Ship ship1, Ship ship2 )
    {
        StringBuilder   bldr    = new StringBuilder();
        OrderedPair     pair1   = ship1.getFirstSquare();
        OrderedPair     pair2   = ship2.getFirstSquare();
        int             len1    = ship1.getLength();
        int             len2    = ship2.getLength();
        bldr.append( pair1 ).append( "/" ).append( len1 ).append( "->" )
            .append( pair2 ).append( "/" ).append( len2 );
        return bldr.toString();
    }

    @Test
    void testGetMinX()
    {
        int expMinHor   = horStartXco;
        int actMinHor   = defHor.getMinX();
        assertEquals( expMinHor, actMinHor );
        
        int expMinVer   = verStartXco;
        int actMinVer   = defVer.getMinX();
        assertEquals( expMinVer, actMinVer );
    }

    @Test
    void testGetMaxX()
    {
        int expMaxHor   = horStartXco + defLenHor;
        int actMaxHor   = defHor.getMaxX();
        assertEquals( expMaxHor, actMaxHor );
        
        int expMaxVer   = verStartXco + 1;
        int actMaxVer   = defVer.getMaxX();
        assertEquals( expMaxVer, actMaxVer );
    }

    @Test
    void testGetMinY()
    {
        int expMinHor   = horStartYco;
        int actMinHor   = defHor.getMinY();
        assertEquals( expMinHor, actMinHor );
        
        int expMinVer   = verStartYco;
        int actMinVer   = defVer.getMinY();
        assertEquals( expMinVer, actMinVer );
    }

    @Test
    void testGetMaxY()
    {
        int expMaxHor   = horStartYco + 1;
        int actMaxHor   = defHor.getMaxY();
        assertEquals( expMaxHor, actMaxHor );
        
        int expMaxVer   = verStartYco + defLenVer;
        int actMaxVer   = defVer.getMaxY();
        assertEquals( expMaxVer, actMaxVer );
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
    void testGetLength()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetOrientation()
    {
        fail("Not yet implemented");
    }
    
    private Rectangle
    getIntersectionRect( Orientation orient, Ship testShip )
    {
        int         width   = getShipWidth( orient );
        int         height  = getShipHeight( orient );
        int         minCol  = testShip.getMinX() - width + 1;
        int         maxCol  = testShip.getMaxX();
        int         minRow  = testShip.getMinY() - height + 1;
        int         maxRow  = testShip.getMaxY() - height;
        Rectangle   rect    = 
            new Rectangle( minCol, minRow, maxCol, maxRow );
        return rect;
    }
    
    private Rectangle
    getNegIntersectionTopRect( Orientation orient, Ship testShip )
    {
        int         width   = getShipWidth( orient );
        int         height  = getShipHeight( orient );
        int         maxCol  = maxXco - width;
        int         maxRow  = testShip.getMinY() - height;
        Rectangle   rect    = new Rectangle( 0, 0, maxCol, maxRow );
        return rect;
    }
    
    private Rectangle
    getNegIntersectionLeftRect( Orientation orient, Ship testShip )
    {
        int         width   = getShipWidth( orient );
        int         height  = getShipHeight( orient );
        int         maxCol  = testShip.getMinX() - width;
        int         maxRow  = maxYco - height;
        Rectangle   rect    = new Rectangle( 0, 0, maxCol, maxRow );
        return rect;
    }
    
    private Rectangle
    getNegIntersectionBottomRect( Orientation orient, Ship testShip )
    {
        int         width   = getShipWidth( orient );
        int         height  = getShipHeight( orient );
        int         maxCol  = maxXco - width;
        int         minRow  = testShip.getMaxY();
        int         maxRow  = maxYco - height;
        Rectangle   rect    = new Rectangle( 0, minRow, maxCol, maxRow );
        return rect;
    }
    
    private Rectangle
    getNegIntersectionRightRect( Orientation orient, Ship testShip )
    {
        int         width   = getShipWidth( orient );
        int         height  = getShipHeight( orient );
        int         minCol  = testShip.getMaxX();
        int         maxCol  = maxXco - width;
        int         maxRow  = maxYco - height;
        Rectangle   rect    = new Rectangle( minCol, 0, maxCol, maxRow );
        return rect;
    }
    
    private List<Rectangle>
    getNegIntersectionRects( Orientation orient, Ship testShip )
    {
        List<Rectangle> rects   = new ArrayList<>();
        rects.add( getNegIntersectionTopRect( orient, testShip ) );
        rects.add( getNegIntersectionLeftRect( orient, testShip ) );
        rects.add( getNegIntersectionBottomRect( orient, testShip ) );
        rects.add( getNegIntersectionRightRect( orient, testShip ) );
        return rects;
    }
    
    private static Ship getHorizontalShip( int xco, int yco )
    {
        OrderedPair start   = new OrderedPair( xco, yco );
        Ship        ship    = new Ship( 
            defTypeHor,  
            defNameHor, 
            start, 
            HORIZONTAL 
        );
        return ship;
    }
    
    private static Ship getVerticalShip( int xco, int yco )
    {
        OrderedPair start   = new OrderedPair( xco, yco );
        Ship        ship    = new Ship( 
            defTypeVer,  
            defNameVer, 
            start, 
            VERTICAL 
        );
        return ship;
    }
    
    private static int getShipWidth( Orientation orient )
    {
        int width   = orient == HORIZONTAL ? defLenHor : 1;
        return width;
    }
    
    private static int getShipHeight( Orientation orient )
    {
        int width   = orient == HORIZONTAL ? 1 : defLenVer;;
        return width;
    }
}
