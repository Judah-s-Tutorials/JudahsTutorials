package com.acmemail.judah.battleship2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship2D.default_ship_types.Battleship;
import com.acmemail.judah.battleship2D.default_ship_types.Carrier;
import com.acmemail.judah.battleship2D.default_ship_types.Cruiser;
import com.acmemail.judah.battleship2D.default_ship_types.Destroyer;
import com.acmemail.judah.battleship2D.default_ship_types.Submarine;

/**
 * Registry of all known {@link ShipType2D} ship types.
 * Ship types are registered via {@link #register(ShipType2D)};
 * duplicate type names are not allowed.
 *
 * @see #registerDefaultTypes()
 */
public class ShipTypes
{
    /** Map of all registered ship types, keyed by type name. */
    private static final Map<String,ShipType2D> allTypes = new HashMap<>();

    /**
     * Default constructor, not used.
     */
    private ShipTypes()
    {
    }

    /**
     * Registers a ship type.
     * Throws BattleshipException
     * if a type with the same name is already registered.
     *
     * @param type  the ship type to register
     *
     * @return  the given ship type, for convenience
     *      
     * @throws NullPointerException if type is null
     * @throws BattleshipException 
     *      if a type with the given name is already registered
     */
    public static ShipType2D register( ShipType2D type )
    {
        Objects.requireNonNull( type, "type" );
        String  typeName    = type.typeName();
        if ( allTypes.containsKey( typeName ) )
        {
            String  message = "Duplicate type name: " + typeName;
            throw new BattleshipException( message );
        }
        allTypes.put( typeName, type );
        return type;
    }

    /**
     * Returns the registered ShipType2D object with the given type name.
     * Returns null if no corresponding such object is found.
     *
     * @param typeName  the given type name
     *
     * @return
     *      the registered ShipType2D object with the given type name
     *      or null if none
     *      
     * @throws NullPointerException if typeName is null
     */
    public static ShipType2D getShipType( String typeName )
    {
        Objects.requireNonNull( typeName, "typeName" );
        ShipType2D type    = allTypes.get( typeName );
        return type;
    }
    
    /**
     * Gets a list of all registered types.
     * 
     * @return  a list of all registered types
     */
    public static List<ShipType2D> getAllRegisteredShipTypes()
    {
        List<ShipType2D>    list    = new ArrayList<>( allTypes.values() );
        return list;
    }

    /**
     * Register the default ship types included
     * in this Battleship implementation,
     * e.g. Battleship, Carrier, Destroyer, etc.
     */
    public static void registerDefaultTypes()
    {
        register( Battleship.getType() );
        register( Carrier.getType() );
        register( Cruiser.getType() );
        register( Destroyer.getType() );
        register( Submarine.getType() );
    }
    
    /**
     * Removes all registered types from the registry.
     * This method is provided for testing purposes.
     */
    static void reset()
    {
        allTypes.clear();
    }
}
