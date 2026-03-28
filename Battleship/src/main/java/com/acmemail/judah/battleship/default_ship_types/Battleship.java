package com.acmemail.judah.battleship.default_ship_types;

import static com.acmemail.judah.battleship.Constants.DEF_BATTLESHIP_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_BATTLESHIP_NAME;

import com.acmemail.judah.battleship.ShipType;

/**
 * Encapsulates the battleship default ship type.
 */
public class Battleship extends ShipType
{
    /**
     * Constructor.
     */
    public Battleship()
    {
        super( DEF_BATTLESHIP_NAME, DEF_BATTLESHIP_LEN );
    }
}
