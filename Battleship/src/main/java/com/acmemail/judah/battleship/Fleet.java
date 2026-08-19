package com.acmemail.judah.battleship;

import static com.acmemail.judah.battleship.StatusMessages.ALREADY_DEPLOYED;
import static com.acmemail.judah.battleship.StatusMessages.INTERSECTS_SHIP;
import static com.acmemail.judah.battleship.StatusMessages.MALFUNCTION;
import static com.acmemail.judah.battleship.StatusMessages.OUT_OF_BOUNDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.acmemail.judah.battleship2D.Grid2D;
import com.acmemail.judah.battleship2D.GridCoords;
import com.acmemail.judah.battleship2D.Orientation;
import com.acmemail.judah.battleship2D.Ship2D;
import com.acmemail.judah.battleship2D.ShipType2D;

/**
 * This class keeps track of all ships
 * that are associated with the home grid.
 * It begins by keep a list of type {@linkPlain ProtoShip}
 * which identifies the type of a ship
 * that must be deployed before the game can start.
 * When a ship identified with a given ProtShip is deployed,
 * the ProtoShip is removed from the to-be-deployed list
 * and moved to the deployed list.
 * If necessary,
 * the ship can be removed from deployed status
 * and its ProtoShip identifier returned
 * to the to-be-deployed list.
 * The client can
 */
public class Fleet
{
    /** 
     * A list of ships that must be deployed before the game can commence.
     */
    private final   List<ProtoShip>         toBeDeployed    = new ArrayList<>();
    private final   Map<ProtoShip, Ship2D>  deployed        = new HashMap<>();
    private final   Grid2D                  grid            = 
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
     * @throws NullPointException if type is null
     * @throws BattleshipException if the game is not in SETUP
     * 
     * @see Configurator
     */
    public void addToBeDeployed( ShipType2D type, String remark )
    {
        Objects.requireNonNull( type, "type" );
        throwIfNotSetup();
        toBeDeployed.add( new ProtoShip( type, remark ) );
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
     *      the {@link ProtoShip} associated with the instantiated ship
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
        ProtoShip ident
    )
    {
        Objects.requireNonNull( coords, "coords" );
        Objects.requireNonNull( orient, "orient" );
        Objects.requireNonNull( ident, "ident" );
        throwIfNotConfig();
        if ( !toBeDeployed.contains( ident ) )
            throw new BattleshipException( StatusMessages.INTERSECTS_SHIP );
        String  intName = name == null ? "" : name; //$NON-NLS-1$
        Ship2D  ship    = new Ship2D( ident.type(), intName, coords, orient );
        return ship;
    }
    
    /**
     * Deploy a ship. 
     * The ship should have been instantiated via
     * {@linkplain #getShip(GridCoords, String, Orientation, ProtoShip)}.
     * The operation may fail if:
     * <ul>
     * </li>The ship has already been deployed.</li>
     * </li>The bounds of the ship fall outside the grid.</li>
     * </li>The ship overlays a previously deployed ship.</li>
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
     * @throws BattleshipException 
     *      if the ship was previously deployed
     *      outside of this facility
     */
    public Result deploy( Ship2D ship, ProtoShip ident )
    {
        Result  result  = new Result();
        Objects.requireNonNull( ship, "ship" );
        Objects.requireNonNull( ident, "ident" ); //$NON-NLS-1$
        throwIfNotConfig();
        if ( deployed.containsKey( ident ) )
            result.addMessage( ALREADY_DEPLOYED, false );
        else if ( !grid.contains( ship ) )
            result.addMessage( OUT_OF_BOUNDS, false );
        else if ( grid.intersectsExisting( ship ) )
            result.addMessage( INTERSECTS_SHIP, false );
        else
        {
            // One more sanity check: make sure that the ship wasn't added
            // to the grid outside of this facility. If it was, declare
            // a malfunction.
            if ( grid.isDeployed( ship ) )
            {
                String  message = MALFUNCTION + ": " + ALREADY_DEPLOYED;
                throw new BattleshipException( message );
            }
            toBeDeployed.remove( ident );
            deployed.put( ident, ship );
            result.setStatus( true );
        }
        return result;
    }
    
    /**
     * Deploy a ship. 
     * The ship should have been instantiated via
     * {@linkplain #getShip(GridCoords, String, Orientation, ProtoShip)}.
     * The operation may fail if the ship has not been deployed.
     * <p>
     * The status of the operation is returned
     * in a Result object.
     * <p>
     * This operation is only available 
     * when the game is in the CONFIG state.
     * 
     * @param ship  the ship to deploy
     * @param ident the deployment identifier
     * 
     * @return  the status of the operation
     * 
     * @see Result
     * 
     * @throws NullPointerException if ident is null
     * @throws BattleshipException 
     *      if the game is not in the CONFIG state
     * 
     */
    public Result undeploy( ProtoShip ident )
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
        }
        return result;
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
     * This operation is only valid
     * when the game is in the configuration state.
     * 
     * @return  a list of ships that need to be deployed
     */
    public List<ProtoShip> getToBeDeployed()
    {
        throwIfNotSetup();
        List<ProtoShip> list    = Collections.unmodifiableList( toBeDeployed );
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
     * Throws an exception if the game is not in the CONFIG state.
     * 
     * @throws BattleshipException if the game is not in the CONFIG state
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
            throw new BattleshipException( StatusMessages.NOT_SETUP );
    }
    
    public record ProtoShip( ShipType2D type, String remark )
    {
    }
}
