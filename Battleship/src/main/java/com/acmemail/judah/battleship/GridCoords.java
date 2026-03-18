package com.acmemail.judah.battleship;

public class GridCoords
{
    private static final int    HASH_PRIME  = 4231;
    
    private final int       xco;
    private final int       yco;
    
    public GridCoords( int xco, int yco )
    {
        this.xco = xco;
        this.yco = yco;
    }
    
    public int getXco()
    {
        return xco;
    }
    
    public int getYco()
    {
        return yco;
    }
    
    @Override
    public String toString()
    {
        String  result  = String.format( "(%d,%d", xco, yco );
        return result;
    }
    
    @Override
    public int hashCode()
    {
        int hash    = yco * HASH_PRIME + xco;
        return hash;
    }
    
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
