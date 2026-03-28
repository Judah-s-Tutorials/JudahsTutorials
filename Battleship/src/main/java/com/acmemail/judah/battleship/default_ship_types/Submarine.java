package com.acmemail.judah.battleship.default_ship_types;

import static com.acmemail.judah.battleship.Constants.DEF_SUBMARINE_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_SUBMARINE_NAME;

import com.acmemail.judah.battleship.ShipType;
/**
 * Encapsulates the submarine default ship type.
 */
public class Submarine extends ShipType
{
    /**
     * Constructor.
     */
    public Submarine()
    {
        super( DEF_SUBMARINE_NAME, DEF_SUBMARINE_LEN );
    }
}
