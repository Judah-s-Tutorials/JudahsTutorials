package com.acmemail.judah.battleship;

import static com.acmemail.judah.battleship.StatusMessages.DUP_PLAYER;
import static com.acmemail.judah.battleship.StatusMessages.DUP_SHIP_TYPE;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_NAME;
import static com.acmemail.judah.battleship.StatusMessages.SHIP_TYPE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.model.default_ship_types.Battleship;
import com.acmemail.judah.battleship.model.default_ship_types.Carrier;
import com.acmemail.judah.battleship.model.default_ship_types.Cruiser;
import com.acmemail.judah.battleship.model.default_ship_types.Destroyer;
import com.acmemail.judah.battleship.model.default_ship_types.Submarine;

class BasicProvisionerTest
{
    private static final ShipType2D battleshipType  = Battleship.getType();
    private static final ShipType2D carrierType     = Carrier.getType();
    private static final ShipType2D cruiserType     = Cruiser.getType();
    private static final ShipType2D destroyerType   = Destroyer.getType();
    private static final ShipType2D submarineType   = Submarine.getType();
    private static final Set<ShipType2D>    allDefaultTypes =
        Set.of(
            battleshipType,
            carrierType,
            cruiserType,
            destroyerType,
            submarineType
        );

    private static final ShipType2D testType   =
        new ShipType2D( "TestType", 5, 2, null );

    @Test
    public void testInitialState()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        assertNull( provisioner.getRows() );
        assertNull( provisioner.getCols() );
        assertTrue( provisioner.getToRegister().isEmpty() );
        assertTrue( provisioner.getToDeploy().isEmpty() );
        assertTrue( provisioner.getPlayers().isEmpty() );
    }

    @Test
    public void testSetRowsCols()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        assertNull( provisioner.getRows() );
        assertNull( provisioner.getCols() );

        provisioner.setRows( 15 );
        provisioner.setCols( 12 );
        assertEquals( 15, provisioner.getRows() );
        assertEquals( 12, provisioner.getCols() );

        provisioner.setRows( null );
        provisioner.setCols( null );
        assertNull( provisioner.getRows() );
        assertNull( provisioner.getCols() );
    }

    @Test
    public void testGettersAreUnmodifiable()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addToRegister( testType );
        provisioner.addToDeploy( testType );
        provisioner.addPlayer( "player" );

        Class<UnsupportedOperationException>   uoeClass    =
            UnsupportedOperationException.class;
        assertThrows(
            uoeClass, () -> provisioner.getToRegister().add( testType )
        );
        assertThrows(
            uoeClass, () -> provisioner.getToDeploy().add( testType )
        );
        assertThrows(
            uoeClass, () -> provisioner.getPlayers().add( "other" )
        );
    }

    @Test
    public void testAddToRegisterGoRight()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addToRegister( testType );
        List<ShipType2D>    actList = provisioner.getToRegister();
        assertEquals( 1, actList.size() );
        assertTrue( actList.contains( testType ) );
    }

    @Test
    public void testAddToRegisterGoWrongNull()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        assertThrows(
            NullPointerException.class,
            () -> provisioner.addToRegister( null )
        );
    }

    @Test
    public void testAddToRegisterGoWrongDup()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addToRegister( testType );
        BattleshipException exc =
            assertThrows(
                BattleshipException.class,
                () -> provisioner.addToRegister( testType )
            );
        assertEquals( DUP_SHIP_TYPE, exc.getMessage() );
        assertEquals( 1, provisioner.getToRegister().size() );
    }

    @Test
    public void testRemoveFromRegisterGoRight()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addToRegister( testType );
        assertTrue( provisioner.getToRegister().contains( testType ) );

        provisioner.removeFromRegister( testType );
        assertFalse( provisioner.getToRegister().contains( testType ) );
    }

    @Test
    public void testRemoveFromRegisterGoRightNotFound()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        // should not throw
        provisioner.removeFromRegister( testType );
        assertTrue( provisioner.getToRegister().isEmpty() );
    }

    @Test
    public void testRemoveFromRegisterCascadesToDeploy()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addToRegister( testType );
        provisioner.addToDeploy( testType );
        provisioner.addToDeploy( testType );
        assertEquals( 2, provisioner.getToDeploy().size() );

        provisioner.removeFromRegister( testType );
        assertFalse( provisioner.getToRegister().contains( testType ) );
        assertTrue( provisioner.getToDeploy().isEmpty() );
    }

    @Test
    public void testAddToDeployGoRight()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addToRegister( testType );
        provisioner.addToDeploy( testType );
        List<ShipType2D>    actList = provisioner.getToDeploy();
        assertEquals( 1, actList.size() );
        assertTrue( actList.contains( testType ) );
    }

    @Test
    public void testAddToDeployGoRightAllowsDuplicates()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addToRegister( testType );
        provisioner.addToDeploy( testType );
        provisioner.addToDeploy( testType );
        assertEquals( 2, provisioner.getToDeploy().size() );
    }

    @Test
    public void testAddToDeployGoWrongNull()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        assertThrows(
            NullPointerException.class,
            () -> provisioner.addToDeploy( null )
        );
    }

    @Test
    public void testAddToDeployGoWrongNotRegistered()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        BattleshipException exc =
            assertThrows(
                BattleshipException.class,
                () -> provisioner.addToDeploy( testType )
            );
        assertEquals( SHIP_TYPE_NOT_FOUND, exc.getMessage() );
        assertTrue( provisioner.getToDeploy().isEmpty() );
    }

    @Test
    public void testRemoveFromDeployGoRight()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addToRegister( testType );
        provisioner.addToDeploy( testType );
        provisioner.addToDeploy( testType );
        assertEquals( 2, provisioner.getToDeploy().size() );

        // removes a single occurrence; type remains registered
        provisioner.removeFromDeploy( testType );
        assertEquals( 1, provisioner.getToDeploy().size() );
        assertTrue( provisioner.getToRegister().contains( testType ) );
    }

    @Test
    public void testRemoveFromDeployGoRightNotFound()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        // should not throw
        provisioner.removeFromDeploy( testType );
        assertTrue( provisioner.getToDeploy().isEmpty() );
    }

    @Test
    public void testAddPlayerGoRight()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        String[]            names       = { "sam", "georgia", "gabe" };
        for ( String name : names )
            provisioner.addPlayer( name );
        assertEquals( List.of( names ), provisioner.getPlayers() );
    }

    @Test
    public void testAddPlayerGoWrongNull()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        assertThrows(
            NullPointerException.class,
            () -> provisioner.addPlayer( null )
        );
    }

    @ParameterizedTest
    @ValueSource( strings = { "", " ", "   " } )
    public void testAddPlayerGoWrongBlank( String name )
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        BattleshipException exc =
            assertThrows(
                BattleshipException.class,
                () -> provisioner.addPlayer( name )
            );
        assertEquals( INVALID_NAME, exc.getMessage() );
        assertTrue( provisioner.getPlayers().isEmpty() );
    }

    @Test
    public void testAddPlayerGoWrongDup()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        String              name        = "gorilla";
        provisioner.addPlayer( name );
        BattleshipException exc =
            assertThrows(
                BattleshipException.class,
                () -> provisioner.addPlayer( name )
            );
        assertEquals( DUP_PLAYER, exc.getMessage() );
        assertEquals( List.of( name ), provisioner.getPlayers() );
    }

    @Test
    public void testRemovePlayerGoRight()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        String              name        = "gorilla";
        provisioner.addPlayer( name );
        assertTrue( provisioner.getPlayers().contains( name ) );

        provisioner.removePlayer( name );
        assertFalse( provisioner.getPlayers().contains( name ) );
    }

    @Test
    public void testRemovePlayerGoRightNotFound()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        // should not throw
        provisioner.removePlayer( "nobody" );
        assertTrue( provisioner.getPlayers().isEmpty() );
    }

    @Test
    public void testAddDefaultTypesToRegister()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addDefaultTypesToRegister();
        Set<ShipType2D>     actSet      =
            Set.copyOf( provisioner.getToRegister() );
        assertEquals( allDefaultTypes, actSet );
        assertTrue( provisioner.getToDeploy().isEmpty() );
    }

    @Test
    public void testAddDefaultTypesToRegisterGoWrongDup()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addDefaultTypesToRegister();
        assertThrows(
            BattleshipException.class,
            provisioner::addDefaultTypesToRegister
        );
    }

    @Test
    public void testAddDefaultTypesToDeployGoWrongNotRegistered()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        assertThrows(
            BattleshipException.class,
            provisioner::addDefaultTypesToDeploy
        );
    }

    @Test
    public void testAddDefaultTypesToDeployGoRight()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addDefaultTypesToRegister();
        provisioner.addDefaultTypesToDeploy();
        Set<ShipType2D>     actSet      =
            Set.copyOf( provisioner.getToDeploy() );
        assertEquals( allDefaultTypes, actSet );
    }

    @Test
    public void testAddDefaultTypesToRegisterAndDeploy()
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addDefaultTypesToRegisterAndDeploy();
        Set<ShipType2D>     actToRegister   =
            Set.copyOf( provisioner.getToRegister() );
        Set<ShipType2D>     actToDeploy     =
            Set.copyOf( provisioner.getToDeploy() );
        assertEquals( allDefaultTypes, actToRegister );
        assertEquals( allDefaultTypes, actToDeploy );
    }
}
