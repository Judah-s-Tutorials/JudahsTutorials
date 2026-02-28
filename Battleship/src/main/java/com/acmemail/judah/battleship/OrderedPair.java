package com.acmemail.judah.battleship;

public class OrderedPair
{
    private final int       xco;
    private final int       yco;
    
    boolean splatted    = false;
    
    public OrderedPair( int xco, int yco )
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

}
