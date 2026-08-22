package com.acmemail.judah.battleship2D;

import java.util.Objects;

public class Cell2D implements Cell2DView
{
    private final   GridCoords  coords;
    private final   Ship2D      ship;
    private boolean splatted    = false;
    
    /**
     * Constructor.
     * Creates a Cell with the given coordinates
     * and a null ship.
     * 
     * @param coords  the given coordinates
     * 
     * @throws NullPointerException if coords is null
     */
    public Cell2D( GridCoords coords )
    {
        Objects.requireNonNull( coords, "coords" );
        this.coords = coords;
        this.ship = null;
    }
    
    /**
     * Constructor.
     * Creates a Cell with the given coordinates
     * and ship.
     * 
     * @param coords    the given coordinates
     * @param ship      the given ship
     * 
     * @throws NullPointerException if coords is null
     * @throws NullPointerException if ship is null
     */
    public Cell2D( GridCoords coords, Ship2D ship )
    {
        Objects.requireNonNull( coords, "coords" );
        Objects.requireNonNull( ship, "ship" );
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
        String  shipName;
        if ( ship == null )
            shipName = "null";
        else if ( ship.getName() == null )
            shipName = "<unnamed>";
        else
            shipName = ship.getName();
        String	str	=
            "Cell2D [coords=" + coords 
            + ", ship=" + shipName
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
