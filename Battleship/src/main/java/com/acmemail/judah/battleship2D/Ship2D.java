package com.acmemail.judah.battleship2D;
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
     * @throws NullPointerException if type is null
     * @throws NullPointerException if first is null
     * @throws NullPointerException if orientation is null
     * 
     * @see Ship2D#Ship2D(ShipType2D, String, GridCoords, Orientation)
     * 
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
     * @param type          the type of the instantiated Ship;
     *                      may not be null
     * @param name          the name of the ship; may be null
     * @param first         the (x,y) coordinates of the instantiated Ship;
     *                      may not be null
     * @param orientation   the orientation of the instantiated Ship
     * 
     * @throws NullPointerException if type is null
     * @throws NullPointerException if first is null
     * @throws NullPointerException if orientation is null
     * 
     * @see Ship2D#Ship2D(ShipType2D, GridCoords, Orientation)
     */
    public Ship2D( 
        ShipType2D type, 
        String name, 
        GridCoords first, 
        Orientation orientation
    )
    {
        Objects.requireNonNull( type, "type" );
        Objects.requireNonNull( first, "first" );
        Objects.requireNonNull( orientation, "orientation" );
        this.type = type;
        this.name = name;
        this.firstSquare = first;
        this.orientation = orientation;
        this.bounds = Utils.getBounds( type, first, orientation );
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
     * 
     * @throws NullPointerException if pair is null
     */
    public boolean contains( GridCoords pair )
    {
        Objects.requireNonNull( pair, "pair" );
        int     xco         = pair.xco();
        int     yco         = pair.yco();
        boolean contains    = contains( xco, yco );
        return contains;
    }
    
    /**
     * Returns true if this ship intersects
     * the rectangle in a grid occupied by a given ship.
     * 
     * @param ship  the given ship
     * 
     * @return  true if this ship intersects the given ship
     * 
     * @throws NullPointerException if ship is null
     */
    public boolean intersects( Ship2D ship )
    {
        Objects.requireNonNull( ship, "ship" );
        boolean     result  = ship.bounds.intersects( bounds );
        return result;
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
        return type.typeName();
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
    
    /**
     * Gets the bounds of this ship.
     * 
     * @return  the bounds of this ship
     */
    public Rectangle getBounds()
    {
        Rectangle   copy    = new Rectangle( bounds );
        return copy;
    }
    
    /**
     * Returns the coordinates of this ship's first square.
     * 
     * @return  the coordinates of this ship's first square
     */
    public GridCoords getCoords()
    {
        return firstSquare;
    }
    
    @Override
    public String toString()
    {
        int             width   = (int)bounds.getWidth();
        int             height  = (int)bounds.getHeight();
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "name=" ).append( name )
            .append( ",type=" ).append( type.typeName() )
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
        else if ( obj instanceof Ship2D that )
        {
            if ( !Objects.equals( this.type, that.type ) )
                rcode = false;
            else if ( !Objects.equals( this.name, that.name ) )
                rcode = false;
            else if ( !Objects.equals( this.firstSquare, that.firstSquare ) )
                rcode = false;
            else if ( this.orientation != that.orientation )
                rcode = false;
            else
                rcode = true;
        }
        return rcode;
    }
}
