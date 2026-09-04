package com.acmemail.judah.battleship.model;

import java.awt.Rectangle;

public class Utils
{
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
