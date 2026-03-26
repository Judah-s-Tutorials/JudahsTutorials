package com.acmemail.judah.battleship;
import static com.acmemail.judah.battleship.Orientation.HORIZONTAL;
import static com.acmemail.judah.battleship.Orientation.VERTICAL;

import java.awt.Rectangle;
import java.util.Objects;
/**
 * An instance of this class encapsulates the properties
 * of a ship in the Battleship game.
 * These properties are:
 * <ul>
 * <li>Ship type, e.g. Battleship, Destroyer.</li>
 * <li>Ship name: a string specified and solely used by the client.</li>
 * <li>Starting grid location, in (x,y) coordinates.</li>
 * <li>Orientation: HORIZONTAL or VERTICAL.</li>
 * </ul>
 */
public class Ship
{
    /** The name of a ship when not supplied by the client. */
    private static final String DEF_NAME    = "Default Name";
    
    /** The type of the ship. */
    private final ShipType      type;
    /** The name of the ship; only meaningful to the client. */
    private final String        name;
    /** The (x,y) coordinates of the ship. */
    private final GridCoords    firstSquare;
    /** The orientation of the ship. */
    private final Orientation   orientation;
    
    /**
     * Constructor.
     * The name of the ship is provided by {@link #DEF_NAME}.
     * 
     * @param type          the type of the instantiated Ship
     * @param first         the (x,y) coordinates of the instantiated Ship
     * @param orientation   the orientation of the instantiated Ship
     * 
     * @see Ship#Ship(ShipType, String, GridCoords, Orientation)
     */
    public Ship( 
        ShipType type, 
        GridCoords first, 
        Orientation orientation
    )
    {
        this( type, DEF_NAME, first, orientation );
    }
    
    /**
     * Constructor.
     * 
     * @param type          the type of the instantiated Ship
     * @param name          the name of the ship
     * @param first         the (x,y) coordinates of the instantiated Ship
     * @param orientation   the orientation of the instantiated Ship
     * 
     * @see Ship#Ship(ShipType, GridCoords, Orientation)
     */
    public Ship( 
        ShipType type, 
        String name, 
        GridCoords first, 
        Orientation orientation
    )
    {
        super();
        this.type = type;
        this.name = name;
        this.firstSquare = first;
        this.orientation = orientation;
    }
    
    /**
     * Returns true if this ship occupies the given (x,y) coordinates.
     * 
     * @param xco   the given x-coordinates
     * @param yco   the given y-coordinates
     * 
     * @return  true if this ship occupies the given (x,y) coordinates
     */
    public boolean contains( int xco, int yco )
    {
        boolean contains    =
            xco >= getMinX() && xco < getMaxX()
            && yco >= getMinY() && yco < getMaxY();
        return contains;
    }
    
    /**
     * Returns true if this ship occupies the given (x,y) coordinates.
     * 
     * @param pair  the given (x,y) coordinates
     * 
     * @return  true if this ship occupies the given (x,y) coordinates
     */
    public boolean contains( GridCoords pair )
    {
        int     xco         = pair.getXco();
        int     yco         = pair.getYco();
        boolean contains    = contains( xco, yco );
        return contains;
    }
    
    /**
     * Returns true if this ship intersects
     * the given rectangle in a grid.
     * 
     * @param rect  the given rectangle
     * 
     * @return  true if this ship intersects the given rectangle
     */
    public boolean intersects( Rectangle rect )
    {
        Rectangle   thisRect    = getRect();
        boolean     result      = rect.intersects( thisRect );
        return result;
    }
    
    /**
     * Returns true if this ship intersects
     * the rectangle in a grid occupied by a given ship.
     * 
     * @param ship  the given ship
     * 
     * @return  true if this ship intersects the given ship
     */
    public boolean intersects( Ship that )
    {
        Rectangle   rect    = that.getRect();
        boolean     result  = intersects( rect );
        return result;
    }
    
    /**
     * Gets a rectangle that describes this ship's position
     * in a grid.
     * 
     * @return  a rectangle that describes this ship's position in a grid
     */
    public Rectangle getRect()
    {
        Rectangle   rect    =
            new Rectangle( getMinX(), getMinY(), getWidth(), getHeight() );
        return rect;
    }
    
    /**
     * Gets the width of this ship.
     * The width of a ship is its extent
     * in the x-dimension
     * (i.e. the number of columns it occupies).
     * 
     * @return  the width of this ship
     */
    public int getWidth()
    {
        int width   = orientation == HORIZONTAL ? type.getLength() : 1;
        return width;
    }
    
    /**
     * Gets the height of this ship.
     * The height of a ship is its extent
     * in the y-dimension
     * (i.e. the number of rows it occupies).
     * 
     * @return  the height of this ship
     */
    public int getHeight()
    {
        int height  = orientation == VERTICAL ? type.getLength() : 1;
        return height;
    }
    
    /**
     * Gets the smallest x-coordinate
     * encapsulated by this ship.
     * 
     * @return  the smallest x-coordinate encapsulated by this ship
     */
    public int getMinX()
    {
        return firstSquare.getXco();
    }
    
    /**
     * Gets the largest x-coordinate
     * encapsulated by this ship.
     * Equal to the minimum x-coordinate
     * plus the ship's length.
     * 
     * @return  the largest x-coordinate encapsulated by this ship
     */
    public int getMaxX()
    {
        int maxXco  = getMinX() + getWidth();
        return maxXco;
    }
    
    /**
     * Gets the smallest y-coordinate
     * encapsulated by this ship.
     * 
     * @return  the smallest y-coordinate encapsulated by this ship
     */
    public int getMinY()
    {
        return firstSquare.getYco();
    }
    
    /**
     * Gets the largest y-coordinate
     * encapsulated by this ship.
     * Equal to the minimum y-coordinate
     * plus the ship's height.
     * 
     * @return  the largest x-coordinate encapsulated by this ship
     */
    public int getMaxY()
    {
        int maxY    = getMinY() + getHeight();
        return maxY;
    }

    /**
     * Gets the ship type.
     * 
     * @return the ship type
     */
    public ShipType getType()
    {
        return type;
    }

    /**
     * Gets the name of this ship's type.
     * 
     * @return the ship type name
     */
    public String getTypeName()
    {
        return type.getTypeName();
    }

    /**
     * Gets the name of this ship.
     * 
     * @return the ship name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the (x,y) coordinates of this ship.
     * 
     * @return the (x,y) coordinates of this ship
     */
    public GridCoords getFirstSquare()
    {
        return firstSquare;
    }

    /**
     * Gets the length of this ship.
     * 
     * @return the length of this ship
     */
    public int getLength()
    {
        int len = type.getLength();
        return len;
    }

    /**
     * Gets the orientation of this ship.
     * 
     * @return the orientation of this ship
     */
    public Orientation getOrientation()
    {
        return orientation;
    }
    
    @Override
    public String toString()
    {
        int             len     = getLength();
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "name=" ).append( name )
            .append( ",type=" ).append( type )
            .append( ",start=" ).append( firstSquare )
            .append( ",orient=" ).append( orientation )
            .append( ",length=" ).append( len );
        
        return bldr.toString();
    }
    
    @Override
    public int hashCode()
    {
        int hashCode    = 
            Objects.hash( type, name, firstSquare, orientation );
        return hashCode;
    }
    
    @Override
    public boolean equals( Object obj )
    {
        boolean rcode   = false;
        if ( this == obj )
            rcode = true;
        else if ( !(obj instanceof Ship) )
            rcode = false;
        else
        {
            Ship    that    = (Ship)obj;
            if ( !this.type.equals( that.type ) )
                rcode = false;
            else if ( !this.name.equals( that.name ) )
                rcode = false;
            else if ( !this.firstSquare.equals( that.firstSquare ) )
                rcode = false;
            else if ( this.orientation != that.orientation )
                rcode = false;
            else
                rcode = true;
        }
        return rcode;
    }
}
