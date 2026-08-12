package battleship2D;

import com.acmemail.judah.battleship.Constants;

/**
 * Encapsulates the carrier default ship type.
 */

public class Carrier
{
    private static final ShipType2D  type    =
        ShipTypes.register(
            new ShipType2D(
                Constants.DEF_CARRIER_NAME,
                Constants.DEF_CARRIER_LEN,
                Constants.DEF_CARRIER_BREADTH,
                null
            )
        );

    public static ShipType2D getType()
    {
        return type;
    }
}
