package com.acmemail.judah.anonymous_classes.functional_interfaces;

public class OddIntSequence extends IntSequence
{
    public OddIntSequence()
    {
        this( 1 );
    }
    
    public OddIntSequence( int start )
    {
        super( start, 2 );
        if ( start % 2 == 0 )
        {
            String  fmt = "start value (%d) illegal; must be an odd number";
            String  msg = 
                String.format( fmt, start );
            throw new IllegalArgumentException( msg );
        }
}
}
