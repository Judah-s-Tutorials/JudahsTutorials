package com.acmemail.judah.battleship2D.default_ship_types;

import com.acmemail.judah.battleship.Constants;
import com.acmemail.judah.battleship.model.ShipType2D;

/**
 * Encapsulates the submarine default ship type.
 */
public class Submarine
{
    /** This ship type. */
    private static final ShipType2D  type    =
        new ShipType2D(
            Constants.DEF_SUBMARINE_NAME,
            Constants.DEF_SUBMARINE_LEN,
            Constants.DEF_SUBMARINE_BREADTH,
            null
        );

    /**
     * Default constructor; not used.
     */
    private Submarine()
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
