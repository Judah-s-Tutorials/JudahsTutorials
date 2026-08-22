package com.acmemail.judah.battleship.fleet;

import static com.acmemail.judah.battleship.fleet.FleetProvisionData.ADHOC_COORDS;
import static com.acmemail.judah.battleship.fleet.FleetProvisionData.ADHOC_NAME;
import static com.acmemail.judah.battleship.fleet.FleetProvisionData.ADHOC_ORIENT;
import static com.acmemail.judah.battleship.fleet.FleetProvisionData.TYPE3X1;
import static com.acmemail.judah.battleship2D.Orientation.HORIZONTAL;
import static com.acmemail.judah.battleship2D.Orientation.VERTICAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Result;
import com.acmemail.judah.battleship.StatusMessages;
import com.acmemail.judah.battleship2D.Grid2D;
import com.acmemail.judah.battleship2D.Grid2DTestSupport;
import com.acmemail.judah.battleship2D.GridCoords;
import com.acmemail.judah.battleship2D.Orientation;
import com.acmemail.judah.battleship2D.Ship2D;
import com.acmemail.judah.battleship2D.ShipType2D;

class FleetConfigTest
{
    private static final Grid2D     homeGrid    = Grid2D.getHomeGrid();
    // Not provisioned here: FleetProvisionData.getProvisionedFleet()
    // requires Configurator to be in the SETUP state, and Configurator
    // is process-wide static state that may already have been advanced
    // by another test class by the time this field initializer would
    // run. Provisioning is deferred to beforeEach(), which resets
    // Configurator to SETUP first.
    private static Fleet   fleet;

    @BeforeEach
    public void beforeEach()
    {
        if ( fleet != null )
            fleet.dispose();
        Grid2DTestSupport.clear( homeGrid );
        Configurator.reset();
        assertTrue( Configurator.isSetup() );
        fleet = FleetProvisionData.getProvisionedFleet();
        Configurator.nextState();
        assertTrue( Configurator.isConfig() );
    }
    
    @Test
    public void testGetShipGoRight()
    {
        List<Fleet.Proto>   toBeDeployed    = fleet.getToBeDeployed();
        Fleet.Proto         proto           = toBeDeployed.get( 0 );
        assertNotNull( proto );
        Ship2D          ship    = 
            fleet.getShip( ADHOC_COORDS, ADHOC_NAME, ADHOC_ORIENT, proto );
        assertEquals( ADHOC_COORDS, ship.getCoords() );
        assertEquals( ADHOC_NAME, ship.getName() );
        assertEquals( ADHOC_ORIENT, ship.getOrientation() );
        assertEquals( proto.getType(), ship.getType() );
        assertTrue( toBeDeployed.contains( proto ) );
        
        ship = fleet.getShip( ADHOC_COORDS, null, ADHOC_ORIENT, proto );
        String                  actName         = ship.getName();
        assertNotNull( actName );
        assertTrue( ship.getName().isEmpty() );
    }
    
    @Test
    public void testGetShipGoWrongNPE()
    {
        final Class<NullPointerException>   npeClass    = 
            NullPointerException.class;
        
        List<Fleet.Proto>   toBeDeployed    = fleet.getToBeDeployed();
        Fleet.Proto         proto           = toBeDeployed.get( 0 );
        assertNotNull( proto );
        
        assertThrows( npeClass, () ->
            fleet.getShip( null, ADHOC_NAME, ADHOC_ORIENT, proto )
        );
        assertThrows( npeClass, () ->
            fleet.getShip( ADHOC_COORDS, ADHOC_NAME, null, proto )
        );
        assertThrows( npeClass, () ->
            fleet.getShip( ADHOC_COORDS, ADHOC_NAME, ADHOC_ORIENT, null )
        );
    }
    
    @Test
    public void testGetShipGoWrongInvalidProto()
    {
        // Proto's constructor is private, so a genuinely foreign
        // prototype can no longer be fabricated from outside Fleet.
        // Instead, verify that getShip() rejects a prototype that is
        // no longer in the to-be-deployed list because it has already
        // been deployed.
        final Class<BattleshipException>    excClass    =
            BattleshipException.class;

        Fleet.Proto         proto           = getNextToBeDeployed();
        deployShip( ADHOC_COORDS, ADHOC_NAME, ADHOC_ORIENT, proto );

        assertThrows( excClass, () ->
            fleet.getShip(  ADHOC_COORDS, ADHOC_NAME, ADHOC_ORIENT, proto )
        );
    }

    @Test
    public void testDeployGoRight()
    {
        GridCoords      coordsA     = new GridCoords( 0, 0 );
        deployShip( coordsA, HORIZONTAL );
        
        // Create a second ship that does not intersect the first...
        // choosing HORIZONTAL for both ships makes this work.
        GridCoords      coordsB     = new GridCoords( 0, coordsA.yco() + 1 );
        deployShip( coordsB, HORIZONTAL );
    }

    @Test
    public void testDeployGoWrongOOB()
    {
        List<Fleet.Proto>   toBeDeployed    = fleet.getToBeDeployed();
        assertFalse( toBeDeployed.isEmpty() );
        Fleet.Proto         proto           = toBeDeployed.get( 0 );
        assertNotNull( proto );
        ShipType2D              shipType        = proto.getType();
        
        Result                  result;
        Ship2D                  ship;
        GridCoords              coords;
        List<Ship2D>            deployed; 
        
        int                     badXco          = 
            Grid2D.getNumCols() - shipType.length() + 1;
        coords = new GridCoords( badXco, 0 );
        ship = fleet.getShip( coords, ADHOC_NAME, HORIZONTAL, proto );
        result = fleet.deploy( ship, proto );
        assertFalse( result.getStatus() );
        assertTrue( toBeDeployed.contains( proto ) );
        deployed = homeGrid.getAllShips();
        assertFalse( deployed.contains( ship ) );
        assertTrue( contains( result, StatusMessages.OUT_OF_BOUNDS ) );
        
        int                     badYco          = 
            Grid2D.getNumRows() - shipType.length() + 1;
        coords = new GridCoords( 0, badYco );
        ship = fleet.getShip( coords, ADHOC_NAME, VERTICAL, proto );
        result = fleet.deploy( ship, proto );
        assertFalse( result.getStatus() );
        assertTrue( toBeDeployed.contains( proto ) );
        deployed = homeGrid.getAllShips();
        assertFalse( deployed.contains( ship ) );
        assertTrue( contains( result, StatusMessages.OUT_OF_BOUNDS ) );
    }

    @Test
    public void testDeployGoWrongIntersection()
    {
        List<Fleet.Proto>   toBeDeployed    = fleet.getToBeDeployed();
        assertFalse( toBeDeployed.isEmpty() );
        Fleet.Proto         refProto        = toBeDeployed.get( 0 );
        assertNotNull( refProto );
        
        Result                  result;
        Ship2D                  ship;
        GridCoords              coords;
        List<Ship2D>            deployed; 
        
        int                     refXco          = 1;
        int                     refYco          = Grid2D.getNumRows() / 2;
        coords = new GridCoords( refXco, refYco );
        Ship2D                  refShip         = 
            fleet.getShip(coords, ADHOC_NAME, HORIZONTAL, refProto );
        Rectangle               refRect         = refShip.getBounds();
        result = fleet.deploy( refShip, refProto );
        assertTrue( result.getStatus() );
        assertFalse( toBeDeployed.contains( refProto ) );
        deployed = homeGrid.getAllShips();
        assertTrue( deployed.contains( refShip ) );
        
        assertFalse( toBeDeployed.isEmpty() );
        Fleet.Proto         proto         = toBeDeployed.get( 0 );        
        int                     badXco          = (int)refRect.getCenterX();
        int                     badYco          = (int)refRect.getCenterY();
        
        coords = new GridCoords( badXco, badYco );
        ship = fleet.getShip( coords, ADHOC_NAME, VERTICAL, proto );
        result = fleet.deploy( ship, proto );
        assertFalse( result.getStatus() );
        assertTrue( toBeDeployed.contains( proto ) );
        deployed = homeGrid.getAllShips();
        assertFalse( deployed.contains( ship ) );
        assertTrue( contains( result, StatusMessages.INTERSECTS_SHIP ) );
        
        ship = fleet.getShip( coords, ADHOC_NAME, HORIZONTAL, proto );
        result = fleet.deploy( ship, proto );
        assertFalse( result.getStatus() );
        assertTrue( toBeDeployed.contains( proto ) );
        deployed = homeGrid.getAllShips();
        assertFalse( deployed.contains( ship ) );
        assertTrue( contains( result, StatusMessages.INTERSECTS_SHIP ) );
    }

    @Test
    public void testDeployGoWrongAlreadyDeployed()
    {
        Fleet.Proto     refProto    = getNextToBeDeployed();
        
        Ship2D          refShip     = 
            deployShip( ADHOC_COORDS, ADHOC_NAME, ADHOC_ORIENT, refProto );
        
        Result          result = fleet.deploy( refShip, refProto );
        assertFalse( result.getStatus() );
        assertTrue( contains( result, StatusMessages.ALREADY_DEPLOYED ) );
    }

    @Test
    public void testDeployGoWongNPE()
    {
        Class<NullPointerException> npeClass    = NullPointerException.class;

        List<Fleet.Proto>   toBeDeployed    = fleet.getToBeDeployed();
        assertFalse( toBeDeployed.isEmpty() );
        Fleet.Proto         refProto        = toBeDeployed.get( 0 );
        assertNotNull( refProto );
        
        Ship2D  ship    = 
            fleet.getShip( ADHOC_COORDS, ADHOC_NAME, ADHOC_ORIENT, refProto );
        assertThrows( npeClass, () -> fleet.deploy( null, refProto ) );
        assertThrows( npeClass, () -> fleet.deploy( ship, null ) );
    }

    @Test
    public void testUndeployGoRight()
    {
        Fleet.Proto     protoA  = getNextToBeDeployed();
        GridCoords      coordsA = new GridCoords( 0, 0 );
        Ship2D          shipA   = 
            deployShip( coordsA, ADHOC_NAME, HORIZONTAL, protoA );
        
        Fleet.Proto     protoB   = getNextToBeDeployed();
        GridCoords      coordsB  = new GridCoords( 0, 1 );
        Ship2D          shipB    = 
            deployShip( coordsB, ADHOC_NAME, HORIZONTAL, protoB );
        
        List<Fleet.Proto>   allPrototypes   = fleet.getToBeDeployed();
        List<Ship2D>        allShips        = homeGrid.getAllShips();
        assertTrue( allShips.contains( shipA ) );
        assertFalse( allPrototypes.contains( protoA ) );
        assertTrue( allShips.contains( shipB ) );
        assertFalse( allPrototypes.contains( protoB ) );
        
        fleet.undeploy( protoA );
        allShips = homeGrid.getAllShips();
        assertFalse( allShips.contains( shipA ) );
        assertTrue( allPrototypes.contains( protoA ) );
        assertTrue( allShips.contains( shipB ) );
        assertFalse( allPrototypes.contains( protoB ) );
        
        fleet.undeploy( protoB );
        allShips = homeGrid.getAllShips();
        assertFalse( allShips.contains( shipA ) );
        assertTrue( allPrototypes.contains( protoA ) );
        assertFalse( allShips.contains( shipB ) );
        assertTrue( allPrototypes.contains( protoB ) );
    }

    @Test
    public void testUndeployGoWrong()
    {
        Fleet.Proto     proto   = getNextToBeDeployed();
        Result          result  = fleet.undeploy( proto );
        assertFalse( result.getStatus() );
        assertTrue( contains( result, StatusMessages.NOT_DEPLOYED ) );
        
        assertThrows( NullPointerException.class, () -> 
            fleet.undeploy( null )
        );
    }

    @ParameterizedTest
    @MethodSource( "getPrototypeTypes" )
    public void testIsInBounds( ShipType2D type )
    {
        // Proto identity is tied to a specific Fleet instance, and
        // @BeforeEach installs a fresh Fleet before every parameterized
        // invocation, so the parameter here is the type, not a Proto;
        // a currently-valid Proto for that type is looked up below.
        Fleet.Proto proto   = getProtoForType( type );
        int         length  = type.length();
        int         breadth = type.breadth();
        Rectangle   bounds  = homeGrid.getBounds();
        
        Corners     corners = new Corners( bounds, length, breadth );
        corners.getCorners().forEach( p -> testIsInBounds( p, HORIZONTAL, proto ) );
        
        corners = new Corners( bounds, breadth, length );
        corners.getCorners().forEach( p -> testIsInBounds( p, VERTICAL, proto ) );
    }
    
    @Test
    public void testIntersectsAnother()
    {
        // Make a horizontal ship close to the left side of the grid,
        // but not in row 0 or column 0.
        GridCoords  hCoords = new GridCoords( 1, 1 );
        Ship2D      hShip   = deployShip( hCoords, HORIZONTAL );
        
        // Make a vertical ship close to the right side of the grid; make it
        // below the horizontal ship, not occupying the last column.
        int         vXco    = Grid2D.getNumCols() / 2;
        GridCoords  vCoords = new GridCoords( vXco, hCoords.yco() + 1 );
        Ship2D      vShip   = deployShip( vCoords, VERTICAL );
        
        // Make a vertical test ship that intersects neither existing ship.
        Fleet.Proto proto   = getNextToBeDeployed();
        ShipType2D  tType   = proto.getType();
        GridCoords  tCoords = 
            new GridCoords( hCoords.xco(), hCoords.yco() + 1 ); 
        Ship2D      tShip   = 
            fleet.getShip( tCoords, ADHOC_NAME, VERTICAL, proto );
        assertTrue( fleet.isInBounds( tShip ) );
        assertFalse( hShip.intersects( tShip ) );
        assertFalse( vShip.intersects( tShip ) );
        assertFalse( fleet.intersectsAnother( tShip ) );

        // Make a vertical test ship that intersects the horizontal ship 
        // but not the vertical.
        tCoords = new GridCoords( hCoords.xco(), hCoords.yco() - 1 ); 
        tShip = fleet.getShip( tCoords, ADHOC_NAME, VERTICAL, proto );
        assertTrue( fleet.isInBounds( tShip ) );
        assertTrue( hShip.intersects( tShip ) );
        assertFalse( vShip.intersects( tShip ) );
        assertTrue( fleet.intersectsAnother( tShip ) );

        // Make a vertical test ship that intersects the vertical ship 
        // but not the horizontal.
        int tXco    = vCoords.xco();
        int tYco    = vCoords.yco () + 1;
        tCoords = new GridCoords( tXco, tYco ); 
        tShip = fleet.getShip( tCoords, ADHOC_NAME, VERTICAL, proto );
        assertTrue( fleet.isInBounds( tShip ) );
        assertFalse( hShip.intersects( tShip ) );
        assertTrue( vShip.intersects( tShip ) );
        assertTrue( fleet.intersectsAnother( tShip ) );
        
        // Make a horizontal test ship that intersects neither existing ship.
        tXco = 0;
        tYco = hCoords.yco() + hShip.getBounds().height;
        tCoords = new GridCoords( tXco, tYco ); 
        tShip = fleet.getShip( tCoords, ADHOC_NAME, HORIZONTAL, proto );
        assertTrue( fleet.isInBounds( tShip ) );
        assertFalse( hShip.intersects( tShip ) );
        assertFalse( vShip.intersects( tShip ) );
        assertFalse( fleet.intersectsAnother( tShip ) );

        // Make a horizontal test ship that intersects the vertical ship 
        // but not the horizontal.
        tXco =  vCoords.xco() - tType.length() / 2;
        tYco =  (int)vShip.getBounds().getCenterY();
        tCoords = new GridCoords( tXco, tYco ); 
        tShip = fleet.getShip( tCoords, ADHOC_NAME, HORIZONTAL, proto );
        assertTrue( fleet.isInBounds( tShip ) );
        assertFalse( hShip.intersects( tShip ) );
        assertTrue( vShip.intersects( tShip ) );
        assertTrue( fleet.intersectsAnother( tShip ) );
    }

    @Test
    public void testGetToBeDeployed()
    {
        List<Fleet.Proto>   protoShips  = fleet.getToBeDeployed();
        int                 yco         = 0;
        while ( !protoShips.isEmpty() )
        {
            assertFalse( fleet.isFullyDeployed() );
            GridCoords  coords      = new GridCoords( 0, yco );
            Fleet.Proto nextProto   = getNextToBeDeployed();
            int         beforeSize  = protoShips.size();
            Ship2D      ship        =
                deployShip( coords, ADHOC_NAME, HORIZONTAL, nextProto );
            protoShips = fleet.getToBeDeployed();
            int         afterSize   = protoShips.size();
            assertFalse( protoShips.contains( nextProto ) );
            assertEquals( beforeSize - 1, afterSize );
            yco += ship.getBounds().height;
        }
        assertTrue( fleet.isFullyDeployed() );
    }
    
    @Test
    public void testConfigGoWrong()
    {
        // Need to add ships to be deployed before executing this test;
        // see testAddToBeDeployed
        List<Fleet.Proto>   protoShips  = fleet.getToBeDeployed();
        assertFalse( protoShips.isEmpty() );
        Class<BattleshipException>  exc = BattleshipException.class;
        assertThrows( exc, () -> 
            fleet.addToBeDeployed( TYPE3X1, null)
        );
    }
    
    @Test
    public void testGetAllDeployedProtos()
    {
        List<Fleet.Proto>   workingList =
            new ArrayList<>( fleet.getToBeDeployed() );
        int                 nextYco     = 0;

        assertTrue( fleet.getAllDeployedProtos().isEmpty() );
        for ( Fleet.Proto proto : workingList )
        {
            assertTrue( fleet.getToBeDeployed().contains( proto ) );
            assertFalse( fleet.getAllDeployedProtos().contains( proto ) );
            assertNull( fleet.getDeployedShip( proto ) );
            GridCoords  coords  = new GridCoords( 0, nextYco );
            Ship2D  ship    =
                deployShip( coords, ADHOC_NAME, HORIZONTAL, proto );
            assertFalse( fleet.getToBeDeployed().contains( proto ) );
            assertTrue( fleet.getAllDeployedProtos().contains( proto ) );
            nextYco += ship.getBounds().height;
            assertEquals( ship, fleet.getDeployedShip( proto ) );
        }
    }

    @Test
    public void testGetAllDeployedShips()
    {
        List<Fleet.Proto>   workingList =
            new ArrayList<>( fleet.getToBeDeployed() );
        Set<Ship2D>         expShips    = new HashSet<>();
        int                 nextYco     = 0;

        assertTrue( fleet.getAllDeployedShips().isEmpty() );
        for ( Fleet.Proto proto : workingList )
        {
            GridCoords  coords  = new GridCoords( 0, nextYco );
            Ship2D      ship    =
                deployShip( coords, ADHOC_NAME, HORIZONTAL, proto );
            expShips.add( ship );
            Set<Ship2D> actShips    = new HashSet<>( fleet.getAllDeployedShips() );
            assertEquals( expShips, actShips );
            nextYco += ship.getBounds().height;
        }
    }
    
    /**
     * Streams the ship types provisioned by FleetProvisionData.
     * A ShipType2D, unlike a Fleet.Proto, is not tied to the identity
     * of a particular Fleet instance, so it is safe to capture here,
     * at method-source resolution time, and use across every
     * parameterized invocation even though {@code fleet} itself
     * is replaced by {@link #beforeEach()} before each invocation.
     *
     * @return  a stream of all ship types provisioned for testing
     */
    private static Stream<ShipType2D> getPrototypeTypes()
    {
        return Stream.of( FleetProvisionData.ALL_TYPES );
    }

    /**
     * Finds a currently-valid prototype, from the current fleet's
     * own to-be-deployed list, for the given ship type.
     *
     * @param type  the given ship type
     *
     * @return  a currently-valid prototype for the given ship type
     */
    private Fleet.Proto getProtoForType( ShipType2D type )
    {
        Fleet.Proto proto   =
            fleet.getToBeDeployed().stream()
                .filter( p -> p.getType().equals( type ) )
                .findFirst()
                .orElseThrow();
        return proto;
    }

    /**
     * Given a point, an orientation, and a prototype ship,
     * create a ship and verify if against Fleet.testIsInBounds.
     * The point is assumed to position a ship 
     * of the given type and orientation
     * in a corner of the grid;
     * Point( 0, 0 ) would be one possible point,
     * and Point( grid width - ship width, 0 ) would be another.
     * Validate that a ship positioned at that point
     * satisfies Fleet.isInBounds().
     * Then, create off-by-one coordinates,
     * and verify that the modified coordinates
     * cause isInBounds() to fail.
     * 
     * @param point     the given point
     * @param orient    the given orientation
     * @param proto     the given prototype
     */
    private void testIsInBounds( 
        Point point, 
        Orientation orient, 
        Fleet.Proto proto 
    )
    {
        int         xco     = point.x;
        int         yco     = point.y;
        int         offXco  = xco == 0 ? xco - 1 : xco + 1;
        int         offYco  = yco == 0 ? yco - 1 : yco + 1;
        
        GridCoords  coords  = new GridCoords( xco, yco );
        Ship2D      ship    = fleet.getShip( coords, ADHOC_NAME, orient, proto );
        assertTrue( fleet.isInBounds( ship ) );
        
        coords = new GridCoords( offXco, yco );
        ship = fleet.getShip( coords, ADHOC_NAME, orient, proto );
        assertFalse( fleet.isInBounds( ship ) );
        
        coords = new GridCoords( xco, offYco );
        ship = fleet.getShip( coords, ADHOC_NAME, orient, proto );
        assertFalse( fleet.isInBounds( ship ) );
        
        coords = new GridCoords( offXco, offYco );
        ship = fleet.getShip( coords, ADHOC_NAME, orient, proto );
        assertFalse( fleet.isInBounds( ship ) );
    }
    
    /**
     * Given coordinates and orientation,
     * create a new ship and deploy it.
     * See {@link #deployShip(GridCoords, String, Orientation, com.acmemail.judah.battleship.Fleet.Proto)}
     * for important, additional details.
     * 
     * @param coords    the given coordinates
     * @param orient    the given orientation
     * 
     * @return  the created and deployed ship
     * 
     * @see #deployShip(GridCoords, String, Orientation, com.acmemail.judah.battleship.Fleet.Proto)
     */
    private Ship2D deployShip( GridCoords coords, Orientation orient )
    {
        Fleet.Proto proto   = getNextToBeDeployed();
        Ship2D      ship    = deployShip( coords, ADHOC_NAME, orient, proto );
        return ship;
    }
    
    /**
     * Given information about a ship,
     * create a ship and deploy it.
     * The given details are assumed to describe a ship
     * that can be deployed in the grid.
     * An assertion is raised if that is not the case.
     * <p>
     * The motivation for this method 
     * is to encapsulate the many lines of code
     * needed to create and validate a ship,
     * then to deploy it and validate its deployment.
     * 
     * @param coords    the coordinates of the deployed ship
     * @param name      the name of the deployed ship
     * @param orient    the orientation of the deployed ship
     * @param proto     a prototype describing the deployed ship
     * 
     * @return  the created ship
     */
    private Ship2D deployShip( 
        GridCoords coords, 
        String name, 
        Orientation orient,
        Fleet.Proto proto
    )
    {
        List<Fleet.Proto>   protoShips  = fleet.getToBeDeployed();
        // sanity check
        assertTrue( protoShips.contains( proto ) );
        Ship2D  ship    = fleet.getShip( coords, name, orient, proto );
        assertTrue( fleet.isInBounds( ship ) );
        assertFalse( fleet.intersectsAnother( ship ) );
        Result  result  = fleet.deploy( ship, proto );
        assertTrue( result.getStatus() );
        assertFalse( protoShips.contains( proto ) );
        assertTrue( homeGrid.getAllShips().contains( ship ) );
        
        return ship;
 
    }
    
    /**
     * Gets the next ship prototype
     * in the list of prototypes to be deployed,
     * and returns.
     * An assertion is raised if the ship is empty.
     * 
     * @return  the next prototype in the list of prototypes to be deployed
     */
    private Fleet.Proto getNextToBeDeployed()
    {
        List<Fleet.Proto>   list    = fleet.getToBeDeployed();
        assertFalse( list.isEmpty() );
        Fleet.Proto         proto   = list.get( 0 );
        return proto;
    }
    
    /**
     * Search the messages in a Result object
     * and return true if one of them
     * contains a given string.
     * 
     * @param result    the result object to search
     * @param str       the given string
     * 
     * @return true if a message in result contains str
     */
    private static boolean contains( Result result, String str )
    {
        boolean rcode   =
            result.getMessages().stream()
                .filter( s -> s.contains( str ) )
                .findAny().isPresent();
        return rcode;
    }
    
    /**
     * Given a fixed rectangle, plus a width and a height,
     * determine the coordinates that would position
     * a new rectangle with the given width and height
     * int the corners of the fixed rectangle.
     */
    private static class Corners
    {
        public final Point      ulc;
        public final Point      urc;
        public final Point      llc;
        public final Point      lrc;
        
        public Corners( Rectangle outer, int width, int height )
        {
            int rightXco    = outer.width - width;
            int lowerYco    = outer.height - height;
            ulc = new Point( 0, 0 );
            urc = new Point( rightXco, 0 );
            llc = new Point( 0, lowerYco );
            lrc = new Point( rightXco, lowerYco );
        }
        
        public Stream<Point> getCorners()
        {
            Stream<Point>   stream  = Stream.of( ulc, urc, llc, lrc );
            return stream;
        }
    }
}
