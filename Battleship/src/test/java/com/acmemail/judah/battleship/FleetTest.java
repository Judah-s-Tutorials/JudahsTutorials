package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder( MethodOrderer.OrderAnnotation.class )
class FleetTest
{
    private static Collection<ShipType> allTypes    = null;
    
    @BeforeAll
    public static void beforeAll()
    {
        ShipType.registerDefaultTypes();
        allTypes = ShipType.getTypes();
    }

    @Test
    @Order( 1 )
    public void testAdd()
    {
        int                 defXco          = 0;
        int                 defYco          = 1;
        Orientation         defOrientation  = Orientation.HORIZONTAL;
        GridCoords          coords          = null;
        Collection<Ship>    testShips       = new ArrayList<>();
        for ( ShipType type : allTypes )
        {
            coords = new GridCoords( defXco++, defYco++ );
            Ship    ship    = new Ship( type, coords, defOrientation );
            testAddShip( ship, testShips );
        }
    }

    @Test
    @Order( 2 )
    public void testRemove()
    {
        List<Ship>  testShips   =new LinkedList<>( Fleet.getShips() );
        assertFalse( testShips.isEmpty() );
        while ( !testShips.isEmpty() )
        {
            Ship    ship    = testShips.remove( 0 );
            Fleet.remove( ship );
            assertEquals( testShips, Fleet.getShips() );
        }
    }

    @Test
    @Order( 3 )
    public void testRemoveAll()
    {
        int                 defXco          = 0;
        int                 defYco          = 1;
        Orientation         defOrientation  = Orientation.HORIZONTAL;
        GridCoords          coords          = null;
        int                 expCount        = 0;
        for ( ShipType type : allTypes )
        {
            coords = new GridCoords( defXco++, defYco++ );
            Ship    ship    = new Ship( type, coords, defOrientation );
            Fleet.add( ship );
            ++expCount;
        }
        assertEquals( expCount, Fleet.getShips().size() );
        Fleet.removeAllShips();
        assertEquals( 0, Fleet.getShips().size() );
    }
    
    private void testAddShip( Ship ship, Collection<Ship> expShips )
    {
        Collection<Ship>    actShips    = Fleet.getShips();
        assertEquals( expShips, actShips );
        
        expShips.add( ship );
        Fleet.add( ship );
        actShips = Fleet.getShips();
        assertEquals( expShips, actShips );
    }
}