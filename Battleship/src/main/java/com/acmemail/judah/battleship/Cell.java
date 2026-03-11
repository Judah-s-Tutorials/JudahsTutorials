package com.acmemail.judah.battleship;

import java.awt.Point;

public class Cell
{
    private final Point point;
    
    private boolean splatted    = false;
    private Ship    ship        = null;
    
    public Cell( Point coords )
    {
        point = coords;
    }
    
    public Cell( int xco, int yco )
    {
        this( new Point( xco, yco ) );
    }
    
    public int getXco()
    {
        return point.x;
    }
    
    public int getYco()
    {
        return point.y;
    }
    
    public void setSplatted( boolean state )
    {
        this.splatted = state;
    }
    
    public boolean isSplatted()
    {
        return splatted;
    }
    
    public Point getPoint()
    {
        return point;
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
    public int hashCode()
    {
        int xco     = point.x;
        int yco     = point.y;
        int rowLen  = Grid.getRowLen();
        int hashVal = rowLen * xco + yco;
        return hashVal;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "(" ).append( point.x ).append( "," )
            .append( point.y ).append( "),splatted=").append( splatted )
            .append( "),ship=" ).append( "{" ).append( ship ).append( "}" );
        return bldr.toString();
    }
}
