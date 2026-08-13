package com.acmemail.judah.battleship2D.default_ship_types;

import com.acmemail.judah.battleship2D.Constants;
import com.acmemail.judah.battleship2D.ShipType2D;

/**
 * Encapsulates the battleship default ship type.
 */
public class Battleship
{
    private static final ShipType2D  type    =
        new ShipType2D(
            Constants.DEF_BATTLESHIP_NAME,
            Constants.DEF_BATTLESHIP_LEN,
            Constants.DEF_BATTLESHIP_BREADTH,
            null
        );

    public static ShipType2D getType()
    {
        return type;
    }
}
