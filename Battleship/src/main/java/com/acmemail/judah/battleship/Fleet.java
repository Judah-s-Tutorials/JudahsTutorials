package com.acmemail.judah.battleship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fleet
{
    private static final    Grid                    gridMap     =
        Grid.getHomeGrid();
    private static final    Map<String,Integer>     shipTypes   = 
        new HashMap<>();
    private static final    List<Ship>              allShips    = 
        new ArrayList<>();
    
    public static synchronized void add( Ship ship )
    {
        String      type    = ship.getTypeName();
        Integer     currNum = shipTypes.getOrDefault( type, 0 );
        shipTypes.put( type, currNum + 1 );
        allShips.add( ship );
        gridMap.put( ship );
    }
    
    public static List<Ship> getShips()
    {
        List<Ship>  ships   = Collections.unmodifiableList( allShips );
        return ships;
    }
}
