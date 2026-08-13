package com.acmemail.judah.battleship.default_ship_types;

import static com.acmemail.judah.battleship2D.Constants.DEF_DESTROYER_LEN;
import static com.acmemail.judah.battleship2D.Constants.DEF_DESTROYER_NAME;

import com.acmemail.judah.battleship.ShipType;
/**
 * Encapsulates the destroyer default ship type.
 */
public class Destroyer extends ShipType
{
    /**
     * Constructor.
     */
    public Destroyer()
    {
        super( DEF_DESTROYER_NAME, DEF_DESTROYER_LEN );
    }
}
