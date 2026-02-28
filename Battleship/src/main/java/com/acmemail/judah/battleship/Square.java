package com.acmemail.judah.battleship;

public class Square
{
    private final OrderedPair   orderedPair;
    
    boolean splatted    = false;
    
    public Square( OrderedPair coords )
    {
        orderedPair = coords;
    }
    
    public Square( int xco, int yco )
    {
        this( new OrderedPair( xco, yco ) );
    }
    
    public int getXco()
    {
        return orderedPair.getXco();
    }
    
    public int getYco()
    {
        return orderedPair.getYco();
    }
    
    public void setSplatted( boolean state )
    {
        this.splatted = state;
    }
    
    public boolean isSplatted()
    {
        return splatted;
    }
    
    public OrderedPair getOrderedPair()
    {
        return orderedPair;
    }
    
    @Override
    public int hashCode()
    {
        int xco     = orderedPair.getXco();
        int yco     = orderedPair.getYco();
        int rowLen  = Grid.getRowLen();
        int hashVal = rowLen * xco + yco;
        return hashVal;
    }

}
