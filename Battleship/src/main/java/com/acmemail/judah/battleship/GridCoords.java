package com.acmemail.judah.battleship;

/**
 * Encapsulation of the x- and y- coordinates of a cell in the grid.
 * X-coordinates begin at 0 in the upper-left corner of the grid,
 * and increase to the right,
 * and y-coordinates begin at 0 in the upper-left corner of the grid
 * and increase downwards.
 * In the expression <em>(a,b),</em>
 * <em>a</em> is the x-coordinate and <em>b</em> is the y-coordinated.
 */
public class GridCoords
{
    /**
     * Prime number for generating hash code.
     * If the grid dimensions are limited to values
     * less than this prime,
     * the <em>hashCode()</em> method
     * produces a perfect hash.
     */
    private static final int    HASH_PRIME  = 4231;
    
    /** The encapsulated x-coordinate. */
    private final int       xco;
    /** The encapsulated y-coordinate. */
    private final int       yco;
    
    /**
     * Constructor,
     * 
     * @param xco   the x-coordinate to encapsulate
     * @param yco   the y-coordinate to encapsulate
     */
    public GridCoords( int xco, int yco )
    {
        this.xco = xco;
        this.yco = yco;
    }
    
    /**
     * Gets the encapsulated x-coordinate.
     * @return the encapsulated x-coordinate
     */
    public int getXco()
    {
        return xco;
    }
    
    /**
     * Gets the encapsulated y-coordinate.
     * @return the encapsulated y-coordinate
     */    public int getYco()
    {
        return yco;
    }
    
    /**
     * Generates a stringin the form <em>(xco,yco)</em>.
     */
    @Override
    public String toString()
    {
        String  result  = String.format( "(%d,%d)", xco, yco );
        return result;
    }
    
    /**
     * Produces a hash code for an instance of this class.
     */
    @Override
    public int hashCode()
    {
        int hash    = yco * HASH_PRIME + xco;
        return hash;
    }
    
    /**
     * Returns true if the <em>other</em> object
     * is type GridCoord
     * with the same x- and y-coordinates
     * as this object.
     */
    @Override
    public boolean equals( Object other )
    {
        boolean result  = false;
        if ( !(other instanceof GridCoords) )
            result = false;
        else if ( this == other )
            result = true;
        else
        {
            GridCoords   that    = (GridCoords)other;
            result = this.xco == that.xco && this.yco == that.yco;
        }
        return result;
    }
}
