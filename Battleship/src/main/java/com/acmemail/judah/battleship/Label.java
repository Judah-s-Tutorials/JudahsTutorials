package com.acmemail.judah.battleship;

public class Label
{
    private static final int        rowLen      = Grid.getRowLen();
    private static final int        colLen      = Grid.getColLen();
    private static final int        maxRowDigs;
    private static final int        maxColDigs;
    private static final String     decToStrFmt;
    
    static
    {
        maxRowDigs  = (int)(Math.log10( rowLen )) + 1;
        maxColDigs = (int)log26( colLen ) + 1;
        decToStrFmt = "%" + maxRowDigs + "d";
    }
    
    public static void main( String[] args )
    {
        System.out.println( rowLen + ", " + colLen );
        System.out.println( maxRowDigs + ", " + maxColDigs );
        for ( int inx = 0 ; inx <= 26 ; ++inx )
            System.out.println( rowToString( inx ) );
    }
    
    public static String colToString( int index )
    {
        String  str     = String.format( decToStrFmt, index );
        return str;
    }
    
    // 1 = A
    // 26 = Z
    // 27 = AA
    // 52 = AZ
    // 53 = BA
    // 78 = BZ
    public static String rowToString( int index )
    {
        if ( index < 0 )
            throw new IllegalArgumentException( "Index must be >= 0" );
        int             num     = index;
        StringBuilder   bldr    = new StringBuilder();
        do
        {
            int     digit   = num % 26;
            char    letter  = (char)(digit + 'A');
            num = num / 26;
            bldr.append( letter );
        } while ( num > 0 );
        return bldr.toString();
    }
    
    private static double log26( int num )
    {
        double  log26   = Math.log10( num ) / Math.log10( 26 );
        return log26;
    }
}
