package com.acmemail.judah.sandbox;

import java.util.ArrayList;
import java.util.List;

public class AutoboxingDemo
{
    public static void main(String[] args)
    {
        List<Integer>   iList   = new ArrayList<>();
        for ( int inx = 0 ; inx < 10 ; ++inx )
            iList.add( inx );
        iList.set( 5, 42 );
        
        for ( int inx = 0 ; inx < 10 ; ++inx )
            iList.add( Integer.valueOf( inx ) );
        iList.set( 5, Integer.valueOf( 42 ) );
    }
}
