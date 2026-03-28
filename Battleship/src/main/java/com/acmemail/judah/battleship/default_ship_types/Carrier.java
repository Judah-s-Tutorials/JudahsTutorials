package com.acmemail.judah.battleship.default_ship_types;

import static com.acmemail.judah.battleship.Constants.DEF_CARRIER_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_CARRIER_NAME;

import com.acmemail.judah.battleship.ShipType;

/**
 * Encapsulates the carrier default ship type.
 */
public class Carrier extends ShipType
{
    /**
     * Constructor.
     */
    public Carrier()
    {
        super( DEF_CARRIER_NAME, DEF_CARRIER_LEN );
    }
}
