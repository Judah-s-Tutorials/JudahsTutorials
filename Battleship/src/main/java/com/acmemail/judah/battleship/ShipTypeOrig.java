package com.acmemail.judah.battleship;

public enum ShipTypeOrig
{
    BATTLESHIP( 4),
    CARRIER( 5 ),
    CRUISER( 3 ),
    DESTROYER( 3 ),
    SUBMARINE( 2 );
    
    private final int   length;
    
    ShipTypeOrig( int length )
    {
        this.length = length;
    }
    
    public int getLength()
    {
        return length;
    }
}
