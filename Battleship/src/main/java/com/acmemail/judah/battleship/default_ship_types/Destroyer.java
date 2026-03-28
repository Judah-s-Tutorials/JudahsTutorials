package com.acmemail.judah.battleship.default_ship_types;

import com.acmemail.judah.battleship.Constants;
import com.acmemail.judah.battleship.ShipType;
import static com.acmemail.judah.battleship.Constants.*;
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
