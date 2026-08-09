package com.acmemail.judah.battleship;
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
public class Ship2D
{
    /** The name of a ship when not supplied by the client. */
    private static final String DEF_NAME    = "Default Name";
    
    /** The type of the ship. */
    private final ShipType2D    type;
    /** The name of the ship; only meaningful to the client. */
    private final String        name;
    /** The (x,y) coordinates of the ship. */
    private final GridCoords    firstSquare;
    /** The orientation of the ship. */
    private final Orientation   orientation;
    /** The bounds of this ship. */
    private final Rectangle     bounds;
    
    /**
     * Constructor.
     * The name of the ship is provided by {@link #DEF_NAME}.
     * 
     * @param type          the type of the instantiated Ship
     * @param first         the (x,y) coordinates of the instantiated Ship
     * @param orientation   the orientation of the instantiated Ship
     * 
     * @see Ship2D#Ship(ShipType, String, GridCoords, Orientation)
     */
    public Ship2D( 
        ShipType2D type, 
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
     * @see Ship2D#Ship(ShipType, GridCoords, Orientation)
     */
    public Ship2D( 
        ShipType2D type, 
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
        this.bounds = type.getBounds( first, orientation );
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
        boolean contains    = bounds.contains( xco, yco );
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
        boolean     result      = rect.intersects( this.bounds );
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
    public boolean intersects( Ship2D that )
    {
        boolean     result  = that.intersects( bounds );
        return result;
    }
    
    /**
     * Gets the smallest x-coordinate
     * encapsulated by this ship.
     * 
     * @return  the smallest x-coordinate encapsulated by this ship
     */
    public int getMinX()
    {
        return (int)(bounds.getMinX());
    }
    
    /**
     * Gets the largest x-coordinate
     * encapsulated by this ship.
     * 
     * @return  the largest x-coordinate encapsulated by this ship
     */
    public int getMaxX()
    {
        int maxXco  = (int)bounds.getMaxX() - 1;
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
        return (int)bounds.getMinY();
    }
    
    /**
     * Gets the largest y-coordinate
     * encapsulated by this ship.
     * 
     * @return  the largest x-coordinate encapsulated by this ship
     */
    public int getMaxY()
    {
        int maxY    = (int)bounds.getMaxY() - 1;
        return maxY;
    }

    /**
     * Gets the ship type.
     * 
     * @return the ship type
     */
    public ShipType2D getType()
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
        int             width   = (int)bounds.getWidth();
        int             height  = (int)bounds.getHeight();
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "name=" ).append( name )
            .append( ",type=" ).append( type )
            .append( ",start=" ).append( firstSquare )
            .append( ",orient=" ).append( orientation )
            .append( ",width=" ).append( width )
            .append( ",height=" ).append( height );
        
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
        else if ( !(obj instanceof Ship2D) )
            rcode = false;
        else
        {
            Ship2D    that    = (Ship2D)obj;
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
