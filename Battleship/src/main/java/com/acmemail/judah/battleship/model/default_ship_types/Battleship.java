package com.acmemail.judah.battleship.model.default_ship_types;

import com.acmemail.judah.battleship.Constants;
import com.acmemail.judah.battleship.model.ShipType2D;

/**
 * Encapsulates the battleship default ship type.
 */
public class Battleship
{
    /** This ship type. */
    private static final ShipType2D  type    =
        new ShipType2D(
            Constants.DEF_BATTLESHIP_NAME,
            Constants.DEF_BATTLESHIP_LEN,
            Constants.DEF_BATTLESHIP_BREADTH,
            null
        );

    /**
     * Default constructor; not used.
     */
    private Battleship()
    {
        // not used
    }    

    /**
     * Gets this ship type.
     * 
     * @return this ship type
     */
    public static ShipType2D getType()
    {
        return type;
    }
}
