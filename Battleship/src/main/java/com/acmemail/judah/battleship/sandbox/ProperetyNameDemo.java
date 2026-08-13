package com.acmemail.judah.battleship.sandbox;

import com.acmemail.judah.battleship2D.Constants;

public class ProperetyNameDemo
{
    public static void main(String[] args)
    {
        String propName   = 
            Constants.NAME_PREFIX + Constants.KEY_NUM_ROWS;
        String numRowsVal = System.getProperty( propName );
        System.out.println( numRowsVal );
    }
}
