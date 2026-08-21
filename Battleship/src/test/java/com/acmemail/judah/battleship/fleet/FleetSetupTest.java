package com.acmemail.judah.battleship.fleet;

import static com.acmemail.judah.battleship.fleet.FleetProvisionData.ADHOC_COORDS;
import static com.acmemail.judah.battleship.fleet.FleetProvisionData.ADHOC_SHIP1;
import static com.acmemail.judah.battleship.fleet.FleetProvisionData.ALL_TYPES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship2D.Orientation;
import com.acmemail.judah.battleship2D.ShipType2D;

class FleetSetupTest
{
    private static Fleet    fleet   = new Fleet();
    
    @BeforeAll
    public static void beforeAll()
    {
        Configurator.reset();
        assertTrue( Configurator.isSetup() );
    }
    
    @AfterEach
    public void afterEach()
    {
        fleet.dispose();
        fleet = new Fleet();
    }
    
    @Test
    public void testAddToBeDeployed()
    {
        List<ShipType2D>        expToBeDeployed = new ArrayList<>();
        List<Fleet.Proto>   actToBeDeployed = fleet.getToBeDeployed();
        assertTrue( actToBeDeployed.isEmpty() );
        
        int                     count           = 1;
        for ( ShipType2D type :  ALL_TYPES )
        {
            fleet.addToBeDeployed( type, "remark " + count++ );
            expToBeDeployed.add( type );
            int expSize = expToBeDeployed.size();
            assertEquals( expSize, actToBeDeployed.size() );
            for ( int inx = 0 ; inx < expSize ; ++inx )
            {
                ShipType2D      expType = expToBeDeployed.get( inx );
                Fleet.Proto proto   = actToBeDeployed.get( inx );
                assertEquals( expType, proto.getType() );
                assertEquals( "remark " + (inx + 1), proto.getRemark() );
            }
        }
    }
    
    @Test
    public void testSetupGoWrong()
    {
        // Need to add ships to be deployed before executing this test;
        // see testAddToBeDeployed
        fleet = FleetProvisionData.getProvisionedFleet();
        List<Fleet.Proto>   protoShips  = fleet.getToBeDeployed();
        assertFalse( protoShips.isEmpty() );
        Fleet.Proto         ident       = protoShips.get( 0 );
        Class<BattleshipException>  exc = BattleshipException.class;
        assertThrows( exc, () -> 
            fleet.getShip( ADHOC_COORDS, "", Orientation.HORIZONTAL, ident  )
        );
        assertThrows( exc, () -> fleet.deploy( ADHOC_SHIP1, ident ) );
        assertThrows( exc, () -> fleet.undeploy( ident ) );
    }
}
