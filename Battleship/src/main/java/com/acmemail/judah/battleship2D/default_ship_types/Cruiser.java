package com.acmemail.judah.battleship2D.default_ship_types;

import com.acmemail.judah.battleship.Constants;
import com.acmemail.judah.battleship.model.ShipType2D;

/**
 * Encapsulates the cruiser default ship type.
 */

public class Cruiser
{
    /** This ship type. */
    private static final ShipType2D  type    =
        new ShipType2D(
            Constants.DEF_CRUISER_NAME,
            Constants.DEF_CRUISER_LEN,
            Constants.DEF_CRUISER_BREADTH,
            null
        );

    /**
     * Default constructor; not used.
     */
    private Cruiser()
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
