package com.acmemail.judah.battleship.sandbox;

public class StrToInt
{
    public static void main(String[] args)
    {
//        for ( int inx = 0 ; inx < 100 ; ++inx )
//        {
//            String  str = "" + inx;
//            int     num = strToInt( str );
//            System.out.printf( "%s -> %d%n", str, num );
//        }
        for ( int inx = 0 ; inx < 200 ; ++inx )
        {
            String  alpha   = decimalToAlpha( inx );
            int     decimal = alphaToInt( alpha );
            System.out.printf( "%d -> %s -> %d%n", inx, alpha, decimal );
        }
    }
    
    private static String decimalToAlpha( int num )
    {
        int             worker  = num + 1;
        StringBuilder   bldr    = new StringBuilder();
        while ( worker > 0 )
        {
            int     digit   = (worker - 1) % 26;
            char    letter  = (char)('A' + digit);
            bldr.insert( 0, letter );
            worker = (worker - 1) / 26;
        }
        return bldr.toString();
    }
    
    private static int alphaToDecimal( String alpha )
    {
        int len = alpha.length();
        int num = 0;
        for ( int inx = 0 ; inx < len ; ++inx )
        {
            char    letter  = alpha.charAt( inx );
            int     value   = letter - 'A' + 1;
            num = num * 26 + value;
        }
        --num;
        return num;
    }

    private static int strToInt( String str )
    {
        int     len     = str.length();
        int     num     = 0;
        for ( int inx = 0 ; inx < len ; ++inx )
        {
            int next    = str.charAt( inx ) - '0';
            num = num * 10 + next;
        }
        return num;
    }
}
