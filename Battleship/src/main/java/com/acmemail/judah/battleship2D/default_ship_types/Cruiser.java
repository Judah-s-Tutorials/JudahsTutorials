package com.acmemail.judah.battleship2D.default_ship_types;

import com.acmemail.judah.battleship.Constants;
import com.acmemail.judah.battleship.model.ShipType2D;

/**
 * Encapsulates the cruiser default ship type.
 */

public class Cruiser
{
    private static final ShipType2D  type    =
        new ShipType2D(
            Constants.DEF_CRUISER_NAME,
            Constants.DEF_CRUISER_LEN,
            Constants.DEF_CRUISER_BREADTH,
            null
        );

    public static ShipType2D getType()
    {
        return type;
    }
}
