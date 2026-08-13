package com.acmemail.judah.battleship2D;

import java.util.Objects;

public class Cell2D
{
    private final   GridCoords  coords;
    private final   Ship2D      ship;
    private boolean splatted    = false;
    
    /**
     * Constructor.
     * 
     * @param coords    the coordinates of this cell, may not be null
     * @param ship      the ship that owns this cell, may be null
     */
    public Cell2D( GridCoords coords, Ship2D ship )
    {
        Objects.requireNonNull( coords, "coords" );
        this.coords = coords;
        this.ship = ship;
    }

    /**
     * Returns true if this cell has been splatted.
     * 
     * @return true if this cell is splatted
     */
    public boolean isSplatted()
    {
        return splatted;
    }

    /**
     * Mark this cell splatted.
     * Note: once splatted, a cell cannot be unsplatted.
     */
    public void setSplatted()
    {
        this.splatted = true;
    }

    /**
     * The coordinates of this cell.
     * 
     * @return the coordinates of this cell
     */
    public GridCoords getCoords()
    {
        return coords;
    }

    /**
     * Gets the ship encapsulated in this cell.
     * If there is no ship, null is returned.
     * 
     * @return the ship encapsulated in this cell, or null if none
     */
    public Ship2D getShip()
    {
        return ship;
    }
    
    @Override
    public String toString()
    {
        String	str	=
            "Cell2D [coords=" + coords 
            + ", ship=" + ship 
            + ", splatted=" + splatted 
            + "]";
        return str;
    }

    @Override
    public int hashCode()
    {
        int hash = Objects.hash( coords, ship );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        boolean result  = false;
        if ( this == obj )
            result = true;
        else if ( obj instanceof Cell2D that )
        {
            if ( !Objects.equals( coords, that.coords ) )
                ;
            else if ( !Objects.equals( ship, that.ship ) )
                ;
            else
                result = true;
        }
        return result;
    }
}
