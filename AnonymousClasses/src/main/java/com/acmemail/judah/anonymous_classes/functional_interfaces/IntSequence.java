package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.function.IntSupplier;

public class IntSequence implements IntSupplier
{
    private final int   increment;
    private final int   start;
    private int         next;
    
    public IntSequence()
    {
        this( 1, 1 );
    }
    
    public IntSequence( int start )
    {
        this( start, 1 );
    }
    
    public IntSequence( int start, int increment )
    {
        this.increment = increment;
        this.start = start;
        this.next = start;
    }
    
    public void reset()
    {
        next = start;
    }
    
    @Override
    public int getAsInt()
    {
        int result  = next;
        next += increment;
        return result;
    }
}
