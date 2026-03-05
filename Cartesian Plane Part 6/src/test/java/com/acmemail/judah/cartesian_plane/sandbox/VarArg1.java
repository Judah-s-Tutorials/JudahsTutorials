package com.acmemail.judah.cartesian_plane.sandbox;

public class VarArg1
{
    public static void main( String[] args )
    {
        varArgMethodA( 5.1, 6.1 );
        varArgMethodA( 1.2, 1.3, 1.4, 1.5 );
        varArgMethodA( 8 );
        double[]    dArgs   = { 3.1, 3.2, 3.3 };
        varArgMethodA( dArgs );
        
        varArgMethodB( -1.1, -2.2 );
        
        varArgMethodC( 2, "Three bears:", "Fuzzy", "Wuzzy", "Anton" );
    }
    
    private static void varArgMethodA( double... args )
    {
        for ( double arg : args )
            System.out.print( arg + " " );
        System.out.println();
    }
    
    private static void varArgMethodB( double... args )
    {
        int count   = args.length;
        System.out.println( "Arg count: " + count );
        for ( int inx = 0 ; inx < count ; ++inx )
            System.out.print( args[inx] + " " );
        System.out.println();
    }
    
    private static void 
    varArgMethodC( int num, String comment, String... args )
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( num ).append( ". " ).append( comment ).append( " " );
        for ( String arg : args )
            bldr.append( arg ).append( " " );
        System.out.println( bldr );
    }
}
