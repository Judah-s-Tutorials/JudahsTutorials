package battleship2D;

import java.util.HashMap;
import java.util.Map;

import com.acmemail.judah.battleship.BattleshipException;

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
     */
    public static ShipType2D register( ShipType2D type )
    {
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
     * Returns the TypeRec object with the given type name.
     * Returns null if no corresponding such object is found.
     *
     * @param typeName  the given type name
     *
     * @return
     *      the TypeRec object that associated with a given type name,
     *      or null if none
     */
    public static ShipType2D getShipType( String typeName )
    {
        ShipType2D type    = allTypes.get( typeName );
        return type;
    }

    /**
     * Register the default ship types included
     * in this Battleship implementation,
     * e.g. Battleship, Carrier, Destroyer, etc.
     */
    public static void registerDefaultTypes()
    {
        Battleship.getType();
        Carrier.getType();
        Cruiser.getType();
        Destroyer.getType();
        Submarine.getType();
    }
}
