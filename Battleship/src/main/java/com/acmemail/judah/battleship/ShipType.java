package com.acmemail.judah.battleship;
import java.awt.Image;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.acmemail.judah.battleship.default_ship_types.Battleship;
import com.acmemail.judah.battleship.default_ship_types.Carrier;
import com.acmemail.judah.battleship.default_ship_types.Cruiser;
import com.acmemail.judah.battleship.default_ship_types.Destroyer;
import com.acmemail.judah.battleship.default_ship_types.Submarine;

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
    
    public String getTypeName()
    {
        return typeName;
    }
    
    public int getLength()
    {
        return length;
    }
    
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
    
    public static ShipType getShipType( String typeName )
    {
        ShipType    type    = allTypes.get( typeName );
        return type;
    }
    
    public static void registerDefaultTypes()
    {
        new Battleship();
        new Carrier();
        new Destroyer();
        new Submarine();
        new Cruiser();
    }
    
    public static Collection<ShipType> getTypes()
    {
        Collection<ShipType>    values  = allTypes.values();
        return values;
    }
}
