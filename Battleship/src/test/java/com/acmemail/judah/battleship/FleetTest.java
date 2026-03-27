package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    public void test()
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