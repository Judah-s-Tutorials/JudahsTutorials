package com.acmemail.judah.battleship;
import static com.acmemail.judah.battleship2D.Constants.DEF_BATTLESHIP_NAME;
import static com.acmemail.judah.battleship2D.Constants.DEF_CARRIER_NAME;
import static com.acmemail.judah.battleship2D.Constants.DEF_CRUISER_NAME;
import static com.acmemail.judah.battleship2D.Constants.DEF_DESTROYER_NAME;
import static com.acmemail.judah.battleship2D.Constants.DEF_SUBMARINE_NAME;

import java.awt.Image;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.acmemail.judah.battleship.default_ship_types.Battleship;
import com.acmemail.judah.battleship.default_ship_types.Carrier;
import com.acmemail.judah.battleship.default_ship_types.Cruiser;
import com.acmemail.judah.battleship.default_ship_types.Destroyer;
import com.acmemail.judah.battleship.default_ship_types.Submarine;

/**
 * An instance of this class encapsulates a ship type,
 * which includes it's name (e.g. "Battleship," "Destroyer")
 * and the length of a ship of this type.
 * The class itself contains a collection
 * of all registered ship types.
 * Each ship type is registered upon instantiation;
 * duplicate ship types are not allowed.
 * 
 * @see #registerDefaultTypes()
 */
public abstract class ShipType
{
    /** 
     * Map of all registered ship types. 
     * Each ship type is registered in the constructor;
     * duplicates are not allowed.
     */
    private static final Map<String,ShipType> allTypes    = new HashMap<>();
    /** Type name, e.g. "Battleship," "Submarine." */
    private final String    typeName;
    /** Number of cells occupied by ship. */
    private final int       length;
    
    /**
     * Constructor.
     * 
     * @param typeName  
     *      the name of this ship type; duplicates are not allowed.
     * @param length    the length of a ship of this type
     */
    public ShipType( String typeName, int length )
    {
        this.typeName = typeName;
        this.length = length;
        if ( allTypes.containsKey( typeName ) )
        {
            String  message = 
                "Duplicate ship types not allowed: " + typeName;
            throw new BattleshipException( message );
        }
        allTypes.put( typeName, this );
    }
    
    /**
     * Gets the name of this ship type.
     * 
     * @return  the name of this ship type
     */
    public String getTypeName()
    {
        return typeName;
    }
    
    /**
     * Gets the length of a ship of this type.
     * 
     * @return  the length of a ship of this type
     */
    public int getLength()
    {
        return length;
    }
    
    /**
     * Gets an image of this ship (NOT IMPLEMENTED).
     * 
     * @return  null
     */
    public Image getImage()
    {
        return null;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( typeName )
            .append( " (").append( length ).append( ")" );
        return bldr.toString();
    }
    
    @Override
    public int hashCode()
    {
        int hashCode    = Objects.hash( typeName, length );
        return hashCode;
    }
    
    @Override
    public boolean equals( Object obj )
    {
        boolean result  = false;
        if ( this == obj )
            result = true;
        else if ( !(obj instanceof ShipType) )
            result = false;
        else
            result = typeName.equals( ((ShipType)obj).typeName );
        // a) It's impossible for two different ShipTypes
        //    to have the same name
        // b) If two ShipType names are equal, they will necessarily
        //    have the same length
        /*
        {
            ShipType    that    = (ShipType)obj;
            if ( !this.typeName.equals( that.typeName ) )
                result = false;
            else if ( this.length != that.length )
                result = false;
            else
                result = true;
        }
        */
        return result;
    }
    
    /**
     * Returns the ShipType object that encapsulates a given type name.
     * Returns null if no corresponding ShipType object is found.
     * 
     * @param typeName  the given type name
     * 
     * @return
     *      the ShipType object that associated with a given type name,
     *      or null if none
     */
    public static ShipType getShipType( String typeName )
    {
        ShipType    type    = allTypes.get( typeName );
        return type;
    }
    
    /**
     * Register the default ship types included
     * in this Battleship implementation,
     * e.g. Battleship, Carrier, Destroyer, etc.
     */
    public static void registerDefaultTypes()
    {
        if ( getShipType( DEF_BATTLESHIP_NAME ) == null )
            new Battleship();
        if ( getShipType( DEF_CARRIER_NAME ) == null )
            new Carrier();
        if ( getShipType( DEF_CRUISER_NAME ) == null )
            new Cruiser();
        if ( getShipType( DEF_DESTROYER_NAME ) == null )
            new Destroyer();
        if ( getShipType( DEF_SUBMARINE_NAME ) == null )
            new Submarine();
    }
    
    /**
     * Returns a Collection of all registered ship types.
     * @return  a Collection of all registered ship types
     */
    public static Collection<ShipType> getTypes()
    {
        Collection<ShipType>    values  = allTypes.values();
        return values;
    }
}
