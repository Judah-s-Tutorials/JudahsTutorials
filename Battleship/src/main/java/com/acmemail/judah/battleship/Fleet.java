package com.acmemail.judah.battleship;

import static com.acmemail.judah.battleship.StatusMessages.ALREADY_DEPLOYED;
import static com.acmemail.judah.battleship.StatusMessages.INTERSECTS_SHIP;
import static com.acmemail.judah.battleship.StatusMessages.OUT_OF_BOUNDS;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_PROTO_SHIP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.acmemail.judah.battleship2D.Grid2D;
import com.acmemail.judah.battleship2D.GridCoords;
import com.acmemail.judah.battleship2D.Orientation;
import com.acmemail.judah.battleship2D.Ship2D;
import com.acmemail.judah.battleship2D.ShipType2D;

/**
 * This class keeps track of all ships
 * that are associated with the home grid.
 * It begins by keep a list of type {@linkplain Proto}
 * which identifies the type of a ship
 * that must be deployed before the game can start.
 * When a ship identified with a given ProtShip is deployed,
 * the ProtoShip is removed from the to-be-deployed list
 * and moved to the deployed list.
 * If necessary,
 * the ship can be removed from deployed status
 * and its ProtoShip identifier returned
 * to the to-be-deployed list.
 * The client can verify the validity
 * of a ship before deployment
 * by calling {@link #intersectsAnother(Ship2D)}
 * and {@link #isInBounds(Ship2D).
 */
public class Fleet
{
    /** 
     * A list of ships that must be deployed before the game can commence.
     */
    private final   List<Proto>         toBeDeployed    = new ArrayList<>();
    private final   Map<Proto, Ship2D>  deployed        = new HashMap<>();
    private final   Grid2D              grid            = 
        Grid2D.getHomeGrid();    
    /**
     * Default constructor.
     */
    public Fleet()
    {
    }
    
    /**
     * Adds a ship type for deployment.
     * This operation is only available
     * when the game is in the SETUP state.
     * 
     * @param type      the type of the ship; may not be null
     * @param remark    an optional remark; may be null
     * 
     * @throws NullPointerException if type is null
     * @throws BattleshipException if the game is not in SETUP
     * 
     * @see Configurator
     */
    public void addToBeDeployed( ShipType2D type, String remark )
    {
        Objects.requireNonNull( type, "type" );
        throwIfNotSetup();
        toBeDeployed.add( new Proto( type, remark ) );
    }
    
    /**
     * Instantiate a ship.
     * The coordinates, name, and orientation of the ship
     * are given by the caller.
     * The type of the ship is taken from {@code ident},
     * which must be a value provided in the list
     * returned by {@linkplain #getToBeDeployed()}.
     * No additional validation of the instantiated ship is performed.
     * This operation is only available in game state CONFIG.
     * 
     * @param coords    the coordinates of the instantiated ship
     * @param name      the name of the instantiated ship; may be null
     * @param orient    the orientation of the instantiated ship
     * @param ident     
     *      the {@link Proto} associated with the instantiated ship
     * 
     * @return  the instantiated ship
     * 
     * @throws NullPointerException if coords is null
     * @throws NullPointerException if orient is null
     * @throws NullPointerException if ident is null
     * @throws BattleshipException 
     *      if ident is not in the shipsToBeDeployed list
     * @throws BattleshipException 
     *      if the game is not in the CONFIG state
     *      
     * @see #isInBounds(Ship2D)
     * @see #intersectsAnother(Ship2D)
     */
    public Ship2D getShip( 
        GridCoords  coords, 
        String      name, 
        Orientation orient,
        Proto ident
    )
    {
        Objects.requireNonNull( coords, "coords" );
        Objects.requireNonNull( orient, "orient" );
        Objects.requireNonNull( ident, "ident" );
        throwIfNotConfig();
        if ( !toBeDeployed.contains( ident ) )
            throw new BattleshipException( INVALID_PROTO_SHIP );
        String  intName = name == null ? "" : name;
        Ship2D  ship    = new Ship2D( ident.getType(), intName, coords, orient );
        return ship;
    }
    
    /**
     * Deploy a ship. 
     * The ship should have been instantiated via
     * {@linkplain #getShip(GridCoords, String, Orientation, Proto)}.
     * The operation may fail if:
     * <ul>
     * <li>The given prototype is invalid.</li>
     * <li>The ship has already been deployed.</li>
     * <li>The bounds of the ship fall outside the grid.</li>
     * <li>The ship overlays a previously deployed ship.</li>
     * </ul>
     * <p>
     * The status of the operation is returned
     * in a Result object.
     *
     * @param ship  the ship to deploy
     * @param ident the deployment identifier
     *
     * @return  the status of the operation
     *
     * @see Result
     *
     * @throws NullPointerException if ship is null
     * @throws NullPointerException if ident is null
     * @throws BattleshipException if the game is not in the CONFIG state
     */
    public Result deploy( Ship2D ship, Proto ident )
    {
        Result  result  = new Result();
        Objects.requireNonNull( ship, "ship" );
        Objects.requireNonNull( ident, "ident" );
        throwIfNotConfig();
        if ( deployed.containsKey( ident ) )
            result.addMessage( ALREADY_DEPLOYED, false );
        else if ( !toBeDeployed.contains( ident ) )
            result.addMessage( INVALID_PROTO_SHIP );
        else if ( !grid.contains( ship ) )
            result.addMessage( OUT_OF_BOUNDS, false );
        else if ( grid.intersectsExisting( ship ) )
            result.addMessage( INTERSECTS_SHIP, false );
        else
        {
            grid.put( ship );
            toBeDeployed.remove( ident );
            deployed.put( ident, ship );
            result.setStatus( true );
        }
        return result;
    }
    
    /**
     * Undeploy a ship,
     * returning its prototype to the to-be-deployed list.
     * The operation may fail if no ship is currently deployed
     * under the given identifier.
     * <p>
     * The status of the operation is returned
     * in a Result object.
     * <p>
     * This operation is only available
     * when the game is in the CONFIG state.
     *
     * @param ident the deployment identifier
     *
     * @return  the status of the operation
     *
     * @see Result
     *
     * @throws NullPointerException if ident is null
     * @throws BattleshipException
     *      if the game is not in the CONFIG state
     */
    public Result undeploy( Proto ident )
    {
        Objects.requireNonNull( ident, "ident" );
        throwIfNotConfig();
        Result  result  = new Result();
        Ship2D  ship    = deployed.remove( ident );
        if ( ship == null )
            result.addMessage( StatusMessages.NOT_DEPLOYED );
        else
        {
            grid.remove( ship );
            toBeDeployed.add( ident );
            result.setStatus( true );
        }
        return result;
    }
    
    /** 
     * Find the deployed ship associated with a prototype
     * and return it.
     * Returns null if no deployed ship is found.
     * 
     * @param proto the prototype associated with the deployed ship
     * 
     * @return  the deployed ship of null if not found
     * 
     * @throws NullPointerException if proto is null
     */
    public Ship2D getDeployedShip( Proto proto )
    {
        Objects.requireNonNull( proto, "proto" );
        Ship2D  ship    = deployed.get( proto );
        return ship;
    }
    
    /**
     * Returns an unmodifiable list of all ships that have been deployed.
     *
     * @return  an unmodifiable list of all ships that have been deployed
     */
    public List<Ship2D> getAllDeployedShips()
    {
        List<Ship2D>    list    = List.copyOf( deployed.values() );
        return list;
    }

    /**
     * Returns an unmodifiable list of prototypes
     * for all ships that have been deployed.
     *
     * @return  an unmodifiable list of prototypes
     *          for all ships that have been deployed
     */
    public List<Proto> getAllDeployedProtos()
    {
        Set<Proto>    set     = deployed.keySet();
        List<Proto>   list    = List.copyOf( set );
        return list;
    }

    /**
     * Returns true if the given ship is contained within 
     * the bounds of the grid.
     * 
     * @param ship  the given ship
     * 
     * @return  
     *      true if ship is contained within the bounds of the grid
     * 
     * @throws NullPointerException if ship is null
     */
    public boolean isInBounds( Ship2D ship )
    {
        Objects.requireNonNull( ship, "ship" );
        boolean inBounds    = grid.contains( ship );
        return inBounds;
    }
    
    /**
     * Returns true if the given ship is intersects another ship in the grid.
     * 
     * @param ship  the given ship
     * 
     * @return  true if the given ship intersects another ship in the grid
     * 
     * @throws NullPointerException if ship is null
     */
    public boolean intersectsAnother( Ship2D ship )
    {
        Objects.requireNonNull( ship, "ship" );
        boolean intersects  = grid.intersectsExisting( ship );
        return intersects;
    }
    
    /**
     * Returns an unmodifiable wrapper
     * around a list of ships that need to be instantiated and deployed.
     * While the client cannot modify the list,
     * changes to the list by this facility
     * will be reflected in the client's list.
     * 
     * @return  a list of ships that need to be deployed
     */
    public List<Proto> getToBeDeployed()
    {
        List<Proto> list    = Collections.unmodifiableList( toBeDeployed );
        return list;
    }
    
    /**
     * Returns true if every ship designated for deployment
     * has been deployed.
     * 
     * @return  true if every ship designated for deployment has been deployed
     */
    public boolean isFullyDeployed()
    {
        boolean deployed    = toBeDeployed.isEmpty();
        return deployed;
    }
    
    /**
     * Frees all resources associated with this object,
     * including removing this fleet's ships from the grid.
     * Note that the grid is a shared resource
     * that may be associated with other Fleet objects;
     * only this fleet's own ships are removed from it.
     * Ships are only removed from the grid
     * while configuration is still in progress;
     * once configuration is complete,
     * the grid's ship layout is permanent,
     * so only this fleet's own bookkeeping is cleared.
     * This is mainly a testing aid.
     * After disposing this object
     * you should not attempt to reuse it;
     * instead, create a new object.
     */
    public void dispose()
    {
        if ( Configurator.getState() <= Constants.CONFIG )
            deployed.values().forEach( grid::remove );
        toBeDeployed.clear();
        deployed.clear();
    }
    
    /**
     * Throws an exception if the game is not in the SETUP state.
     * 
     * @throws BattleshipException if the game is not in the SETUP state
     */
    private static void throwIfNotSetup()
    {
        if ( !Configurator.isSetup() )
            throw new BattleshipException( StatusMessages.NOT_SETUP );
    }
    
    /**
     * Throws an exception if the game is not in the CONFIG state.
     * 
     * @throws BattleshipException if the game is not in the CONFIG state
     */
    private static void throwIfNotConfig()
    {
        if ( !Configurator.isConfig() )
            throw new BattleshipException( StatusMessages.NOT_CONFIG );
    }
    
    /**
     * Encapsulates the type of a ship
     * and a remark associated with it.
     * The principal purpose of an instance of this type 
     * is to serve as a token
     * that can be moved between a list of ships that need to be deployed
     * and a list of ships that have been deployed.
     * The only way two instances can be considered equal
     * is via identity.
     * <p>
     * The remark is present for the convenience of the client.
     * If null is passed for the remark
     * it is converted to an empty string,
     * but is otherwise unexamined and uninterpreted by the implementation.
     */
    public static class Proto
    {
        private final ShipType2D    type;
        private final String        remark;
        
        /**
         * Establish the type and remark associated with this instance.
         * 
         * @param type      
         *      the type encapsulated by this instance; may not be null
         * @param remark
         *      the remark encapsulated by this instance; may be null
         */
        private Proto( ShipType2D type, String remark )
        {
            this.type = type;
            this.remark = remark == null ? "" : remark;
        }
        
        /**
         * Gets the type associated with this prototype.
         * 
         * @return the type associated with this prototype
         */
        public ShipType2D getType()
        {
            return type;
        }

        /**
         * Gets the remark associated with this prototype.
         *
         * @return the remark associated with this prototype
         */
        public String getRemark()
        {
            return remark;
        }
    }
}
