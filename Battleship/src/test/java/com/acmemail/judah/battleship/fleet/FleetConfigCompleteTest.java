package com.acmemail.judah.battleship.fleet;

import static com.acmemail.judah.battleship.fleet.FleetProvisionData.ADHOC_COORDS;
import static com.acmemail.judah.battleship.fleet.FleetProvisionData.ADHOC_NAME;
import static com.acmemail.judah.battleship.fleet.FleetProvisionData.ADHOC_ORIENT;
import static com.acmemail.judah.battleship.fleet.FleetProvisionData.ADHOC_SHIP1;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship2D.Grid2DTestSupport;

class FleetConfigCompleteTest
{
    private static  Fleet fleet;
    
    @BeforeAll
    public static void beforeAll()
    {
        Grid2DTestSupport.reset();
        Configurator.reset();
        assertTrue( Configurator.isSetup() );
        fleet = FleetProvisionData.getProvisionedFleet();
        Configurator.nextState();
        assertTrue( Configurator.isConfig() );
        FleetProvisionData.deployAll( fleet );
        assertTrue( fleet.isFullyDeployed() );
        Configurator.nextState();
        assertTrue( Configurator.isConfigComplete() );
    }
    
    @Test
    public void test()
    {
        Class<BattleshipException>  excClass    = BattleshipException.class;
        Fleet.Proto                 proto       =
            fleet.getAllDeployedProtos().get( 0 );
        
        assertThrows( excClass, () -> 
            fleet.addToBeDeployed( FleetProvisionData.TYPE2X2, "" ) 
        );
        assertThrows( excClass, () -> 
            fleet.getShip( ADHOC_COORDS, ADHOC_NAME, ADHOC_ORIENT, proto )
        );
        assertThrows( excClass, () -> fleet.deploy( ADHOC_SHIP1, proto ) );
        
        assertThrows( excClass, () -> fleet.undeploy( proto ) );
    }

}
