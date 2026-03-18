package com.acmemail.judah.battleship;

public class Label
{
    private static final int        rowLen      = Grid.getNumRows();
    private static final int        colLen      = Grid.getNumCols();
    private static final int        maxRowDigs;
    private static final int        maxColDigs;
    private static final String     decToStrFmt;
    private static final String     alphaToStrFmt;
    
    static
    {
        maxRowDigs  = (int)(Math.log10( rowLen )) + 1;
        maxColDigs = (int)log26( colLen ) + 1;
        decToStrFmt = "%" + maxRowDigs + "d";
        alphaToStrFmt = "%" + maxColDigs + "s";
    }
    
    public static void main( String[] args )
    {
        System.out.println( rowLen + ", " + colLen );
        System.out.println( maxRowDigs + ", " + maxColDigs );
        for ( int inx = 0 ; inx <= 100 ; ++inx )
        {
            String  base26  = decimalToAlpha( inx );
            int     base10  = alphaToDecimal( base26 );
            String  str = 
                String.format( "%d -> %s -> %d", inx, base26, base10 );
            System.out.println( str );
        }
    }
    
    public static String intToString( int num )
    {
        if ( num < 0 )
        {
            String  fmt     = "Input (%d) must be >= 0";
            String  message = String.format( fmt,  num );
            throw new BattleshipException( message );
        }
        String  str = String.format( decToStrFmt, num + 1 );
        return str;
    }
    
    public static int strToInt( String str )
    {
        int     result  = 0;
        String  worker  = str.trim();
        if ( worker.isEmpty() )
        {
            String  message = "Cannot convert empty string to int";
            throw new BattleshipException( message );
        }
        
        char    lead    = worker.charAt( 0 );
        if ( Character.isDigit( lead ) )
            result = Integer.parseInt( worker );
        else if ( Character.isUpperCase( lead ) )
            result = alphaToDecimal( worker );
        else
        {
            String  message = "Cannot convert " + worker + "to int";
            throw new BattleshipException( message );
        }
        
        return result;
    }
    
    // 0 = A
    // 25 = Z
    // 26 = AA
    // 51 = AZ
    // 52 = BA
    // 77 = BZ
    public static String decimalToAlpha( int num )
    {
        if ( num < 0 )
        {
            String  fmt     = "Input (%d) must be >= 0";
            String  message = String.format( fmt,  num );
            throw new BattleshipException( message );
        }
        int             worker  = num + 1;
        StringBuilder   bldr    = new StringBuilder();
        while ( worker > 0 )
        {
            int     digit   = (worker - 1) % 26;
            char    letter  = (char)('A' + digit);
            bldr.insert( 0, letter );
            worker = (worker - 1) / 26;
        }
        String  padded  = String.format( alphaToStrFmt, bldr );
        return padded;
    }
    
    public static int alphaToDecimal( String alpha )
    {
        String  worker  = alpha.trim();
        if ( worker.isEmpty() )
        {
            String  message = "Cannot convert empty string to decimal";
            throw new BattleshipException( message );
        }
        int     len     = alpha.length();
        int     num     = 0;
        for ( int inx = 0 ; inx < len ; ++inx )
        {
            char    letter  = alpha.charAt( inx );
            if ( !Character.isUpperCase( letter) )
            {
                String  message = "Cannot convert " + worker + " to decimal";
                throw new BattleshipException( message );
            }
            int     value   = letter - 'A' + 1;
            num = num * 26 + value;
        }
        --num;
        return num;
    }
    
    private static double log26( int num )
    {
        double  log26   = Math.log10( num ) / Math.log10( 26 );
        return log26;
    }
}
