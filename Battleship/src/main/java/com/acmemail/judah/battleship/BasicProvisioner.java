package com.acmemail.judah.battleship;

import static com.acmemail.judah.battleship.StatusMessages.DUP_PLAYER;
import static com.acmemail.judah.battleship.StatusMessages.DUP_SHIP_TYPE;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_NAME;
import static com.acmemail.judah.battleship.StatusMessages.SHIP_TYPE_NOT_FOUND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.model.default_ship_types.Battleship;
import com.acmemail.judah.battleship.model.default_ship_types.Carrier;
import com.acmemail.judah.battleship.model.default_ship_types.Cruiser;
import com.acmemail.judah.battleship.model.default_ship_types.Destroyer;
import com.acmemail.judah.battleship.model.default_ship_types.Submarine;

/**
 * A simple, programmatic implementation of {@link Provisioner}.
 * Unlike {@link TextProvisioner},
 * which is populated by parsing CSV records,
 * this class is populated directly
 * via its add/remove methods.
 */
public class BasicProvisioner implements Provisioner
{
    /** List of ships types to register at the start of the game. */
    private final List<ShipType2D>  toRegister  = new ArrayList<>();
    /** List of ships types to deploy at the start of the game. */
    private final List<ShipType2D>  toDeploy    = new ArrayList<>();
    /** List of ships types to deploy at the start of the game. */
    private final List<String>      players     = new ArrayList<>();
    /** Number of grid rows configured during parsing; null if none. */
    private Integer                 rows        = null;
    /** Number of grid columns configured during parsing; null if none. */
    private Integer                 cols        = null;

    public BasicProvisioner()
    {
    }

    @Override
    public Integer getRows()
    {
        return rows;
    }

    @Override
    public Integer getCols()
    {
        return cols;
    }

    /**
     * Sets the number of grid rows.
     *
     * @param rows  the number of grid rows; may be null
     */
    public void setRows( Integer rows )
    {
        this.rows = rows;
    }

    /**
     * Sets the number of grid columns.
     *
     * @param cols  the number of grid columns; may be null
     */
    public void setCols( Integer cols )
    {
        this.cols = cols;
    }

    @Override
    public List<ShipType2D> getToDeploy()
    {
        List<ShipType2D>    list    = Collections.unmodifiableList( toDeploy );
        return list;
    }

    @Override
    public List<ShipType2D> getToRegister()
    {
        List<ShipType2D>    list    = Collections.unmodifiableList( toRegister );
        return list;
    }

    @Override
    public List<String> getPlayers()
    {
        List<String>    list    = Collections.unmodifiableList( players );
        return list;
    }

    /**
     * Adds a ship type to the list of types to be registered.
     *
     * @param type  the ship type to add
     *
     * @throws NullPointerException if type is null
     * @throws BattleshipException if type is already in the list
     */
    public void addToRegister( ShipType2D type )
    {
        Objects.requireNonNull( type, "type" );
        if ( toRegister.contains( type ) )
            throw new BattleshipException( DUP_SHIP_TYPE );
        toRegister.add( type );
    }

    /**
     * Removes a ship type from the list of types to be registered.
     * If the type is not in the list, this method does nothing.
     * Any element of the to-deploy list
     * that matches the given type
     * is also removed.
     *
     * @param type  the ship type to remove
     *
     * @return  true if the type was found and removed
     */
    public boolean removeFromRegister( ShipType2D type )
    {
        boolean removed = toRegister.remove( type );
        toDeploy.removeIf( t -> t.equals( type ) );
        return removed;
    }

    /**
     * Adds a ship type to the list of types to be deployed.
     * The given type must already be
     * in the list of types to be registered.
     *
     * @param type  the ship type to add
     *
     * @throws NullPointerException if type is null
     * @throws BattleshipException if type is not in the to-register list
     */
    public void addToDeploy( ShipType2D type )
    {
        Objects.requireNonNull( type, "type" );
        if ( !toRegister.contains( type ) )
            throw new BattleshipException( SHIP_TYPE_NOT_FOUND );
        toDeploy.add( type );
    }

    /**
     * Removes a single occurrence of a ship type
     * from the list of types to be deployed.
     * If the type is not in the list, this method does nothing.
     *
     * @param type  the ship type to remove
     *
     * @return  true if the type was found and removed
     */
    public boolean removeFromDeploy( ShipType2D type )
    {
        return toDeploy.remove( type );
    }

    /**
     * Adds a name to the list of players.
     * The name may not be null, empty, or blank,
     * and may not already be in the list.
     *
     * @param name  the name to add
     *
     * @throws NullPointerException if name is null
     * @throws BattleshipException
     *      if name is empty, blank, or already in the list
     */
    public void addPlayer( String name )
    {
        Objects.requireNonNull( name, "name" );
        if ( name.isBlank() )
            throw new BattleshipException( INVALID_NAME );
        if ( players.contains( name ) )
            throw new BattleshipException( DUP_PLAYER );
        players.add( name );
    }

    /**
     * Removes a name from the list of players.
     * If the name is not in the list, this method does nothing.
     *
     * @param name  the name to remove
     *
     * @return  true if the name was found and removed
     */
    public boolean removePlayer( String name )
    {
        return players.remove( name );
    }

    /**
     * Adds all default ship types
     * (see {@link com.acmemail.judah.battleship.model.default_ship_types})
     * to the list of types to be registered.
     *
     * @throws BattleshipException
     *      if a default type is already in the to-register list
     */
    public void addDefaultTypesToRegister()
    {
        getDefaultTypes().forEach( this::addToRegister );
    }

    /**
     * Adds all default ship types
     * (see {@link com.acmemail.judah.battleship.model.default_ship_types})
     * to the list of types to be deployed.
     * Each default type must already be
     * in the list of types to be registered.
     *
     * @throws BattleshipException
     *      if a default type is not in the to-register list
     */
    public void addDefaultTypesToDeploy()
    {
        getDefaultTypes().forEach( this::addToDeploy );
    }

    /**
     * Adds all default ship types
     * (see {@link com.acmemail.judah.battleship.model.default_ship_types})
     * to the lists of types to be registered and deployed.
     *
     * @throws BattleshipException
     *      if a default type is already in the to-register list
     */
    public void addDefaultTypesToRegisterAndDeploy()
    {
        addDefaultTypesToRegister();
        addDefaultTypesToDeploy();
    }

    /**
     * Gets a list of all default ship types.
     * Package-private to allow reuse by {@link TextProvisioner}.
     *
     * @return a list of all default ship types
     */
    static List<ShipType2D> getDefaultTypes()
    {
        List<ShipType2D>   list    =
            List.of(
                Battleship.getType(),
                Carrier.getType(),
                Cruiser.getType(),
                Destroyer.getType(),
                Submarine.getType()
            );
        return list;
    }
}
