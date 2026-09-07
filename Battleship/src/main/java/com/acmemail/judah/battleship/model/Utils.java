package com.acmemail.judah.battleship.model;

import java.awt.Rectangle;

/**
 * Utilities to support classes in the model package.
 */
public class Utils
{
    /**
     * Default constructor, not used.
     */
    private Utils()
    {
        // Default constructor, not used.
    }
    
    /**
     * Given a ship type, its location, and its orientation,
     * calculate the bounds of a ship.
     * 
     * @param type          the given type
     * @param origin        the given location
     * @param orientation   the given orientation
     * 
     * @return  the bounds of a ship with the given properties
     */
    public static Rectangle 
    getBounds( ShipType2D type, GridCoords origin, Orientation orientation )
    {
        int xco     = origin.xco();
        int yco     = origin.yco();
        int length  = type.length();
        int breadth = type.breadth();
        Rectangle   rect    = orientation == Orientation.HORIZONTAL ?
            new Rectangle( xco, yco, length, breadth ) :
            new Rectangle( xco, yco, breadth, length );
        return rect;
    }
}
