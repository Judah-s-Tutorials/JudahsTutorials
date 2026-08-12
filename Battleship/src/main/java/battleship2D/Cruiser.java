package battleship2D;

import com.acmemail.judah.battleship.Constants;

/**
 * Encapsulates the cruiser default ship type.
 */

public class Cruiser
{
    private static final ShipType2D  type    =
        ShipTypes.register(
            new ShipType2D(
                Constants.DEF_CRUISER_NAME,
                Constants.DEF_CRUISER_LEN,
                Constants.DEF_CRUISER_BREADTH,
                null
            )
        );

    public static ShipType2D getType()
    {
        return type;
    }
}
