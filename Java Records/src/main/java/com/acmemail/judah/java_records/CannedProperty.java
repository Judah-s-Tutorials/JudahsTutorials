package com.acmemail.judah.java_records;

import java.io.Serializable;

public record CannedProperty( double can ) implements Serializable
{
    private static int cansProduced = 0;
    
    public CannedProperty
    {
        ++cansProduced;
    }
    
    public double halfCan()
    {
        double  part    = canPart( .5 );
        return part;
    }
    
    public double quarterCan()
    {
        double  part    = canPart( .5 );
        return part;
    }
    
    public static int cansProduced()
    {
        return cansProduced;
    }
    
    private double canPart( double fraction )
    {
        double  part    = can * fraction;
        return part;
    }
}
