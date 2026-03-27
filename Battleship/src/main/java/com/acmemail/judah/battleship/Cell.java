package com.acmemail.judah.battleship;

import java.util.Objects;

/**
 * An instance of this class encapsulates the properties
 * of a cell in the grid, including:
 * <ul>
 * <li>The (x,y) coordinates of the cell.</li>
 * <li>The ship that occupies this cell (if any).</li>
 * <li>Whether or not the cell has been attacked ("splatted").</li>
 * </ul>
 */
public class Cell
{
    /** The (x,y) coordinates of this cell. */
    private final GridCoords coords;    
    /** True if this  cell has been attacked.  */
    private boolean splatted    = false;
    /** The ship that occupies this cell (null if none). */
    private Ship    ship        = null;
    
    /**
     * Constructor.
     * The Ship property is set to null,
     * the splatted property is set to false.
     * 
     * @param coords    the (x,y) coordinates of this cell
     */
    public Cell( GridCoords coords )
    {
        this.coords = coords;
    }
    
    /**
     * Constructor. 
     * 
     * @param xco   the x-coordinate of this cell
     * @param yco   the y-coordinate of this cell
     */
    public Cell( int xco, int yco )
    {
        this( new GridCoords( xco, yco ) );
    }
    
    /**
     * Gets the x-coordinate of this cell.
     * 
     * @return  the x-coordinate of this cell
     */
    public int getXco()
    {
        return coords.getXco();
    }
    
    /**
     * Gets the y-coordinate of this cell.
     * 
     * @return  the y-coordinate of this cell
     */
    public int getYco()
    {
        return coords.getYco();
    }
    
    /**
     * Sets the state of the splatted property
     * of this cell
     * (true means the cell has been attacked).
     * 
     * @param state the state of the splatted property
     */
    public void setSplatted( boolean state )
    {
        this.splatted = state;
    }
    
    /**
     * Gets the value of the splatted property
     * of this cell.
     * 
     * @return  true if this cell has been splatted
     */
    public boolean isSplatted()
    {
        return splatted;
    }
    
    /**
     * Gets the (x,y) coordinates of this cell.
     * 
     * @return  the (x,y) coordinates of this cell
     */
    public GridCoords getCoords()
    {
        return coords;
    }
    
    /**
     * Returns true if this cell is occupied by a ship.
     * 
     * @return  true if this cell is occupied by a ship
     */
    public boolean hasShip()
    {
        boolean hasShip = getShip() != null;
        return hasShip;
    }

    /**
     * Gets the ship that occupies this cell,
     * or null if none.
     * 
     * @return  the ship that occupies this cell, null if none.
     */
    public Ship getShip()
    {
        return ship;
    }
    
    /**
     * Sets the ship that occupies this cell.
     * 
     * @param ship  the ship that occupies this cell, may be null
     */
    public void setShip( Ship ship )
    {
        this.ship = ship;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( coords ).append( "),splatted=").append( splatted )
            .append( "),ship=" ).append( "{" ).append( ship ).append( "}" );
        return bldr.toString();
    }
    
    @Override
    public int hashCode()
    {
        int hashCode    = Objects.hash( coords, splatted, ship );
        return hashCode;
    }
    
    @Override
    public boolean equals( Object obj )
    {
        boolean result  = false;
        if ( this == obj )
            result = true;
        else if ( !(obj instanceof Cell) )
            result = false;
        else
        {
            Cell    that    = (Cell)obj;
            if ( !this.coords.equals( that.coords ) )
                result = false;
            else if ( this.splatted != that.splatted )
                result = false;
            else if ( !Objects.equals( this.ship, that.ship ) )
                result = false;
            else
                result = true;
        }
        return result;
    }
}
