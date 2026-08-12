package battleship2D;

import com.acmemail.judah.battleship.Constants;

/**
 * Encapsulates the submarine default ship type.
 */

public class Submarine
{
    private static final ShipType2D  type    =
        ShipTypes.register(
            new ShipType2D(
                Constants.DEF_SUBMARINE_NAME,
                Constants.DEF_SUBMARINE_LEN,
                Constants.DEF_SUBMARINE_BREADTH,
                null
            )
        );

    public static ShipType2D getType()
    {
        return type;
    }
}
