package com.acmemail.judah.battleship.default_ship_types;

import static com.acmemail.judah.battleship.Constants.DEF_CRUISER_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_CRUISER_NAME;

import com.acmemail.judah.battleship.ShipType;

/**
 * Encapsulates the cruiser default ship type.
 */
public class Cruiser extends ShipType
{
    /**
     * Constructor.
     */
    public Cruiser()
    {
        super( DEF_CRUISER_NAME, DEF_CRUISER_LEN );
    }
}
