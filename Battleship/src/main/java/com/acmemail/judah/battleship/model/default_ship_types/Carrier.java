package com.acmemail.judah.battleship.model.default_ship_types;

import com.acmemail.judah.battleship.Constants;
import com.acmemail.judah.battleship.model.ShipType2D;

/**
 * Encapsulates the carrier default ship type.
 */

public class Carrier
{
    /** This ship type. */
    private static final ShipType2D  type    =
        new ShipType2D(
            Constants.DEF_CARRIER_NAME,
            Constants.DEF_CARRIER_LEN,
            Constants.DEF_CARRIER_BREADTH,
            null
        );

    /**
     * Default constructor; not used.
     */
    private Carrier()
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
