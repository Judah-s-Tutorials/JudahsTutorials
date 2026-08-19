package com.acmemail.judah.battleship2D.default_ship_types;

import com.acmemail.judah.battleship.Constants;
import com.acmemail.judah.battleship2D.ShipType2D;

/**
 * Encapsulates the destroyer default ship type.
 */

public class Destroyer
{
    private static final ShipType2D  type    =
        new ShipType2D(
            Constants.DEF_DESTROYER_NAME,
            Constants.DEF_DESTROYER_LEN,
            Constants.DEF_DESTROYER_BREADTH,
            null
        );

    public static ShipType2D getType()
    {
        return type;
    }
}
