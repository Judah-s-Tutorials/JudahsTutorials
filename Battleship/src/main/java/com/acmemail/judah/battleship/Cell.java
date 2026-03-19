package com.acmemail.judah.battleship;

public class Cell
{
    private final GridCoords coords;
    
    private boolean splatted    = false;
    private Ship    ship        = null;
    
    public Cell( GridCoords coords )
    {
        this.coords = coords;
    }
    
    public Cell( int xco, int yco )
    {
        this( new GridCoords( xco, yco ) );
    }
    
    public int getXco()
    {
        return coords.getXco();
    }
    
    public int getYco()
    {
        return coords.getYco();
    }
    
    public void setSplatted( boolean state )
    {
        this.splatted = state;
    }
    
    public boolean isSplatted()
    {
        return splatted;
    }
    
    public GridCoords getCoords()
    {
        return coords;
    }
    
    public boolean hasShip()
    {
        boolean hasShip = getShip() != null;
        return hasShip;
    }

    public Ship getShip()
    {
        return ship;
    }
    
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
}
