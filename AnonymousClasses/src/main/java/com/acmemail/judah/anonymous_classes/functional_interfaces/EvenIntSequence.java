package com.acmemail.judah.anonymous_classes.functional_interfaces;

public class EvenIntSequence extends IntSequence
{
    public EvenIntSequence()
    {
        this( 0 );
    }
    
    public EvenIntSequence( int start )
    {
        super( start, 2 );
        if ( start % 2 != 0 )
        {
            String  fmt = "start value (%d) illegal; must be an even number";
            String  msg = 
                String.format( fmt, start );
            throw new IllegalArgumentException( msg );
        }
    }
}
