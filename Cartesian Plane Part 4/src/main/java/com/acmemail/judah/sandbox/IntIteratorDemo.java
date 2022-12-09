package com.acmemail.judah.sandbox;

public class IntIteratorDemo
{
    public static void main(String[] args)
    {
        IntIterator iter    = new IntIterator( -5, 5 );
        while ( iter.hasNext() )
        {
            int iNum    = iter.next();
            System.out.println( iNum );
        }
    }
}
