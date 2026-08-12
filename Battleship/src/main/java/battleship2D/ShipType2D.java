package battleship2D;

import java.awt.Image;
import java.util.Objects;

/**
 * An instance of this class encapsulates a ship type,
 * which includes it's name (e.g. "Battleship," "Destroyer")
 * and the length and breadth of a ship of this type.
 * <p>
 * An image may optionally be provided for a ship type.
 * If used,
 * the image should depict the ship in a horizontal position.
 * <p>
 * It is recommended
 * that the longer dimension of the ship
 * be associated with its length,
 * and the shorter dimension with its breadth.
 * During execution the length and breadth
 * are used to construct a Rectangle
 * for a ship of a given type.
 * Horizontal ships have a Rectangle
 * with width = length and height = breadth;
 * vertical ships have a Rectangle
 * with width = breadth and height = length.
 * <p>
 * Instances are registered with, and looked up via,
 * {@link ShipTypes}.
 *
 * @param typeName  the name of the type; must be unique
 * @param length    the length of a ship of this type
 * @param breadth   the breadth of a ship of this type
 * @param image     an image of a ship of this type; may be null
 *
 * @see ShipTypes
 */
public record ShipType2D(
    String typeName,
    int    length,
    int    breadth,
    Image  image
)
{
    /**
     * Compact constructor.
     * Validates that typeName is non-null.
     */
    public ShipType2D
    {
        Objects.requireNonNull( typeName, "typeName" );
    }
}
