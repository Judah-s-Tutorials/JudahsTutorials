package com.acmemail.judah.battleship;

import static com.acmemail.judah.battleship.Orientation.HORIZONTAL;
import static com.acmemail.judah.battleship.Orientation.VERTICAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.default_ship_types.Battleship;
import com.acmemail.judah.battleship.default_ship_types.Carrier;
import com.acmemail.judah.battleship.default_ship_types.Cruiser;

class ShipTest
{
    private static final ShipType   battleshipType  = new Battleship();
    private static final ShipType   carrierType     = new Carrier();
    private static final ShipType   cruiserType     = new Cruiser();
    
    private static final String     defNameHor  = "DefNameHorizontal";
    private static final String     defNameVer  = "DefNameVertical";
    private static final ShipType   defTypeHor  = carrierType;
    private static final ShipType   defTypeVer  = cruiserType;
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
            new GridCoords( horStartXco, midYco ),
            Orientation.HORIZONTAL
        );
    private static final Ship   defVer      =
        new Ship(
            defTypeVer,
            defNameVer,
            new GridCoords( verStartXco, verStartYco ),
            Orientation.VERTICAL
        );
    
    @Test
    public void testShipTypeGridCoordsOrientation()
    {
        testShipTypeGridCoordsOrientation( defTypeVer, 10, 20, VERTICAL );
        testShipTypeGridCoordsOrientation( defTypeHor, 20, 10, HORIZONTAL );
    }
    
    private static void 
    testShipTypeGridCoordsOrientation( 
        ShipType type, 
        int xco, 
        int yco, 
        Orientation orient 
    )
    {
        GridCoords  coords      = new GridCoords( xco, yco );
        int         expLen      = type.getLength();
        int         expWidth;
        int         expHeight;
        if ( orient == HORIZONTAL )
        {
            expWidth = expLen;
            expHeight = 1;
        }
        else
        {
            expWidth = 1;
            expHeight = expLen;
        }
        Ship        ship        = new Ship( type, coords, orient );
        assertEquals( type, ship.getType() );
        assertEquals( orient, ship.getOrientation() );
        assertEquals( expWidth, ship.getWidth() );
        assertEquals( expHeight, ship.getHeight() );
        assertNotNull( ship.getName() );
        GridCoords  actCoords    = ship.getFirstSquare();
        assertEquals( xco, actCoords.getXco() );
        assertEquals( yco, actCoords.getYco() );
    }

    @Test
    public void testShipAllParams()
    {
        assertEquals( defTypeHor, defHor.getType() );
        assertEquals( defNameHor, defHor.getName() );
        assertEquals( horStartXco, defHor.getMinX() );
        assertEquals( midYco, defHor.getMinY() );
        assertEquals( defLenHor, defHor.getLength() );
        assertEquals( Orientation.HORIZONTAL, defHor.getOrientation() );

        assertEquals( defTypeVer, defVer.getType() );
        assertEquals( defNameVer, defVer.getName() );
        assertEquals( verStartXco, defVer.getMinX() );
        assertEquals( verStartYco, defVer.getMinY() );
        assertEquals( defLenVer, defVer.getLength() );
        assertEquals( Orientation.VERTICAL, defVer.getOrientation() );
    }

    @Test
    public void testContainsOrderedPair()
    {
        testContainsOrderedPair( defHor );
        testContainsOrderedPair( defVer );
    }
    
    @Test
    public void testIntersects()
    {
        testIntersects( VERTICAL, defHor );
        testIntersects( VERTICAL, defVer );
        testIntersects( HORIZONTAL, defHor );
        testIntersects( HORIZONTAL, defVer );
    }

    @Test
    public void testIntersectsNeg()
    {
        testIntersectsNeg( VERTICAL, defHor );
        testIntersectsNeg( HORIZONTAL, defVer );
        testIntersectsNeg( VERTICAL, defVer );
        testIntersectsNeg( VERTICAL, defHor );
    }

    @Test
    public void testGetMinX()
    {
        int expMinHor   = horStartXco;
        int actMinHor   = defHor.getMinX();
        assertEquals( expMinHor, actMinHor );
        
        int expMinVer   = verStartXco;
        int actMinVer   = defVer.getMinX();
        assertEquals( expMinVer, actMinVer );
    }

    @Test
    public void testGetMaxX()
    {
        int expMaxHor   = horStartXco + defLenHor;
        int actMaxHor   = defHor.getMaxX();
        assertEquals( expMaxHor, actMaxHor );
        
        int expMaxVer   = verStartXco + 1;
        int actMaxVer   = defVer.getMaxX();
        assertEquals( expMaxVer, actMaxVer );
    }

    @Test
    public void testGetMinY()
    {
        int expMinHor   = horStartYco;
        int actMinHor   = defHor.getMinY();
        assertEquals( expMinHor, actMinHor );
        
        int expMinVer   = verStartYco;
        int actMinVer   = defVer.getMinY();
        assertEquals( expMinVer, actMinVer );
    }

    @Test
    public void testGetMaxY()
    {
        int expMaxHor   = horStartYco + 1;
        int actMaxHor   = defHor.getMaxY();
        assertEquals( expMaxHor, actMaxHor );
        
        int expMaxVer   = verStartYco + defLenVer;
        int actMaxVer   = defVer.getMaxY();
        assertEquals( expMaxVer, actMaxVer );
    }

    @Test
    public void testGetType()
    {
        assertEquals( defTypeHor, defHor.getType() );
        assertEquals( defTypeVer, defVer.getType() );
    }

    @Test
    public void testGetName()
    {
        assertEquals( defNameHor, defHor.getName() );
        assertEquals( defNameVer, defVer.getName() );
    }

    @Test
    public void testGetFirstSquare()
    {
        GridCoords   horPair = defHor.getFirstSquare();
        assertEquals( horPair.getXco(), horStartXco );
        assertEquals( horPair.getYco(), horStartYco );

        GridCoords   verPair = defVer.getFirstSquare();
        assertEquals( verPair.getXco(), verStartXco );
        assertEquals( verPair.getYco(), verStartYco );
    }

    @Test
    public void testGetLength()
    {
        assertEquals( defLenHor, defHor.getLength() );
        assertEquals( defLenVer, defVer.getLength() );
    }

    @Test
    public void testGetOrientation()
    {
        assertEquals( HORIZONTAL, defHor.getOrientation() );
        assertEquals( VERTICAL, defVer.getOrientation() );
    }
    
    @Test
    public void testGetTypeName()
    {
        GridCoords  coords      = new GridCoords( 0, 0 );
        Orientation orientation = Orientation.HORIZONTAL;
        ShipType    typeA       = battleshipType;
        ShipType    typeB       = carrierType;
        Ship        shipA       = new Ship( typeA, coords, orientation );
        Ship        shipB       = new Ship( typeB, coords, orientation );
        
        assertNotEquals( shipA.getTypeName(), shipB.getTypeName() );
        shipB = new Ship( typeA, coords, orientation );
        assertEquals( shipA.getTypeName(), shipB.getTypeName() );
    }
    
    @Test
    public void testEqualsHash()
    {
        ShipType    typeA           = battleshipType;
        ShipType    typeB           = carrierType;
        Orientation orientationA    = Orientation.HORIZONTAL;
        Orientation orientationB    = Orientation.VERTICAL;
        GridCoords  coordsA         = new GridCoords( 1, 10 );
        GridCoords  coordsB         = new GridCoords( 2, 11 );
        String      nameA           = "NameA";
        String      nameB           = "NameB";
        Object      obj             = new Object();
        Ship        shipA           = 
            new Ship( typeA, nameA, coordsA, orientationA );
        Ship        shipB           = 
            new Ship( typeB, nameA, coordsA, orientationA );
        
        assertTrue( shipA.equals( shipA ) );
        assertFalse( shipA.equals( null ) );
        assertFalse( shipA.equals( obj ) );
        assertFalse( shipA.equals( shipB ) );
        assertFalse( shipB.equals( shipA ) );
        
        shipB = new Ship( typeA, nameB, coordsA, orientationA );
        assertFalse( shipA.equals( shipB ) );
        assertFalse( shipB.equals( shipA ) );
        
        shipB = new Ship( typeA, nameA, coordsB, orientationA );
        assertFalse( shipA.equals( shipB ) );
        assertFalse( shipB.equals( shipA ) );
        
        shipB = new Ship( typeA, nameA, coordsA, orientationB );
        assertFalse( shipA.equals( shipB ) );
        assertFalse( shipB.equals( shipA ) );
        
        shipB = new Ship( typeA, nameA, coordsA, orientationA );
        assertEquals( shipA, shipB );
        assertEquals( shipB, shipA );
        assertEquals( shipA.hashCode(), shipB.hashCode() );
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
        GridCoords      pair1   = ship1.getFirstSquare();
        GridCoords      pair2   = ship2.getFirstSquare();
        int             len1    = ship1.getLength();
        int             len2    = ship2.getLength();
        bldr.append( pair1 ).append( "/" ).append( len1 ).append( "->" )
            .append( pair2 ).append( "/" ).append( len2 );
        return bldr.toString();
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
    
    private void testContainsOrderedPair( Ship ship )
    {
        int minRow  = ship.getMinY();
        int maxRow  = ship.getMaxY();
        int minCol  = ship.getMinX();
        int maxCol  = ship.getMaxX();
        for ( int row = minRow ; row < maxRow ; ++row )
            for ( int col = minCol ; col < maxCol ; ++col )
            {
                GridCoords  pair    = new GridCoords( col, row );
                assertTrue( ship.contains( pair ) );
            }
        
        int rowLow  = minRow - 1;
        int rowHigh = maxRow;
        for ( int col = 0 ; col < colLen ; ++col )
        {
            GridCoords  pairLow     = new GridCoords( col, rowLow );
            GridCoords  pairHigh    = new GridCoords( col, rowHigh );
            assertFalse( ship.contains( pairLow ) );
            assertFalse( ship.contains( pairHigh ) );
        }
        
        int colLow  = minCol - 1;
        int colHigh = maxCol;
        for ( int row = 0 ; row < rowLen ; ++row )
        {
            GridCoords  pairLow     = new GridCoords( colLow, row );
            GridCoords  pairHigh    = new GridCoords( colHigh, row );
            assertFalse( ship.contains( pairLow ) );
            assertFalse( ship.contains( pairHigh ) );
        }
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
        GridCoords  start   = new GridCoords( xco, yco );
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
        GridCoords  start   = new GridCoords( xco, yco );
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
