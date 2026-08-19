package com.acmemail.judah.battleship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acmemail.judah.battleship2D.Grid2D;
import com.acmemail.judah.battleship2D.Ship2D;

/**
 * This class keeps track of all ships
 * that are associated with the home grid.
 */
public class Fleet_orig
{
    /**
     * Default constructor; not used.
     */
    private Fleet_orig()
    {
    }
    
    /** The grid associatd with this fleet. */
    private static final    Grid2D                  gridMap     =
        Grid2D.getHomeGrid();
    /** The count of ships of a particular type. */
    private static final    Map<String,Integer>     shipTypes   = 
        new HashMap<>();
    /** List of all ships in the fleet. */
    private static final    List<Ship2D>            allShips    = 
        new ArrayList<>();
    
    /**
     * Adds a given ship to the fleet.
     * 
     * @param ship  the given ship
     * 
     * @throws BattleshipException
     *      if the ship cannot be added to the grid
     */
    public static synchronized void add( Ship2D ship )
    {
        String      type    = ship.getTypeName();
        Integer     currNum = shipTypes.getOrDefault( type, 0 );
        // try to add to the grid first, which may throw
        // a BattleshipException
        gridMap.put( ship );
        shipTypes.put( type, currNum + 1 );
        allShips.add( ship );
    }
    
    /**
     * Removes a given ship from the fleet.
     * 
     * @param ship  the given ship
     */
    public static synchronized void remove( Ship2D ship )
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
            Ship2D  ship    = allShips.get( 0 );
            remove( ship );
        }
    }
    
    /**
     * Gets a list of all ships in the fleet.
     * 
     * @return  a list of all ships in the fleet
     */
    public static List<Ship2D> getShips()
    {
        List<Ship2D>    ships   = Collections.unmodifiableList( allShips );
        return ships;
    }
}
