package com.acmemail.judah.battleship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class keeps track of all ships
 * that are associated with the home grid.
 */
public class Fleet
{
    /**
     * Default constructor; not used.
     */
    private Fleet()
    {
    }
    
    /** The grid associatd with this fleet. */
    private static final    Grid                    gridMap     =
        Grid.getHomeGrid();
    /** The count of ships of a particular type. */
    private static final    Map<String,Integer>     shipTypes   = 
        new HashMap<>();
    /** List of all ships in the fleet. */
    private static final    List<Ship>              allShips    = 
        new ArrayList<>();
    
    /**
     * Adds a given ship to the fleet.
     * 
     * @param ship  the given ship
     */
    public static synchronized void add( Ship ship )
    {
        String      type    = ship.getTypeName();
        Integer     currNum = shipTypes.getOrDefault( type, 0 );
        shipTypes.put( type, currNum + 1 );
        allShips.add( ship );
        gridMap.put( ship );
    }
    
    /**
     * Removes a given ship from the fleet.
     * 
     * @param ship  the given ship
     */
    public static synchronized void remove( Ship ship )
    {
        String      type    = ship.getTypeName();
        Integer     currNum = shipTypes.getOrDefault( type, 0 );
        if ( currNum > 0 )
            shipTypes.put( type, currNum - 1 );
        allShips.remove( ship );
        gridMap.remove( ship );
    }
    
    /**
     * Removes all ships from the fleet.
     */
    public static void removeAllShips()
    {
        while ( !allShips.isEmpty() )
        {
            Ship    ship    = allShips.get( 0 );
            remove( ship );
        }
    }
    
    /**
     * Gets a list of all ships in the fleet.
     * 
     * @return  a list of all ships in the fleet
     */
    public static List<Ship> getShips()
    {
        List<Ship>  ships   = Collections.unmodifiableList( allShips );
        return ships;
    }
}
