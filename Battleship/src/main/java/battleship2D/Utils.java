package battleship2D;

import java.awt.Rectangle;

import com.acmemail.judah.battleship.GridCoords;
import com.acmemail.judah.battleship.Orientation;

public class Utils
{
    public static Rectangle 
    getBounds( ShipType2D type, GridCoords origin, Orientation orientation )
    {
        int xco     = origin.getXco();
        int yco     = origin.getYco();
        int length  = type.length();
        int breadth = type.breadth();
        Rectangle   rect    = orientation == Orientation.HORIZONTAL ?
            new Rectangle( xco, yco, length, breadth ) :
            new Rectangle( xco, yco, breadth, length );
        return rect;
    }
}
