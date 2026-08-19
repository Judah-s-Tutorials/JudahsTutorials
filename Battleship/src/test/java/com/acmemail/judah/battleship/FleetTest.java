package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.acmemail.judah.battleship2D.GridCoords;
import com.acmemail.judah.battleship2D.Orientation;
import com.acmemail.judah.battleship2D.Ship2D;
import com.acmemail.judah.battleship2D.ShipType2D;
import com.acmemail.judah.battleship2D.ShipTypes;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FleetTest
{
    private static final ShipType2D     type3By1    =
        new ShipType2D( "3X1", 3, 1, null );
    private static final ShipType2D     type3By2    =
        new ShipType2D( "3X2", 3, 2, null );
    private static final ShipType2D     type2By2    =
        new ShipType2D( "2X2", 2, 2, null );
    private static final ShipType2D[]   allTypes    =
    { type3By1, type3By2, type2By2 };
    
    private static final GridCoords     adHocCoords = new GridCoords( 0, 0 );
    private static final Ship2D         adHocTestShip;
    
    private static final Fleet          fleet   = new Fleet();
    
    static
    {
        ShipTypes.register( type3By1 );
        ShipTypes.register( type3By2 );
        ShipTypes.register( type2By2 );
        adHocTestShip = 
            new Ship2D( 
                type3By1,
                "Ad hoc test ship",
                new GridCoords( 0, 0 ),
                Orientation.HORIZONTAL
            );
    }
    
    
    @BeforeAll
    public static void beforeAll()
    {
    }
    
    @Test
    @Order( 5 )
    void testAddToBeDeployed()
    {
        List<ShipType2D>        expToBeDeployed = new ArrayList<>();
        List<Fleet.ProtoShip>   actToBeDeployed = fleet.getToBeDeployed();
        assertTrue( actToBeDeployed.isEmpty() );
        
        int                     count           = 1;
        for ( ShipType2D type :  allTypes )
        {
            fleet.addToBeDeployed( type, "remark " + count++ );
            expToBeDeployed.add( type );
            int expSize = expToBeDeployed.size();
            assertEquals( expSize, actToBeDeployed.size() );
            for ( int inx = 0 ; inx < expSize ; ++inx )
            {
                ShipType2D      expType = expToBeDeployed.get( inx );
                Fleet.ProtoShip proto   = actToBeDeployed.get( inx );
                assertEquals( expType, proto.type() );
                assertEquals( "remark " + (inx + 1), proto.remark() );
            }
        }
        System.out.println( actToBeDeployed.size() );
    }
    
    @Test
    @Order( 10 )
    void testSetupGoWrong()
    {
        // Need to add ships to be deployed before executing this test;
        // see testAddToBeDeployed
        List<Fleet.ProtoShip>   protoShips  = fleet.getToBeDeployed();
        assertFalse( protoShips.isEmpty() );
        Fleet.ProtoShip         ident       = protoShips.get( 0 );
        Class<BattleshipException>  exc = BattleshipException.class;
        assertThrows( exc, () -> 
            fleet.getShip( adHocCoords, "", Orientation.HORIZONTAL, ident  )
        );
        assertThrows( exc, () -> fleet.deploy( adHocTestShip, ident ) );
        assertThrows( exc, () -> fleet.undeploy( ident ) );
    }

    @Test
    void testGetShip()
    {
        fail("Not yet implemented");
    }

    @Test
    void testDeploy()
    {
        fail("Not yet implemented");
    }

    @Test
    void testUndeploy()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsInBounds()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIntersectsAnother()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetToBeDeployed()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsFullyDeployed()
    {
        fail("Not yet implemented");
    }

}
