package com.acmemail.judah.cartesian_plane.sandbox;

public class EnvironmentPropertiesDemo
{
    private static final String basePropName    = "propName";
    private static final String nameFmt         = "%s_%02d";
    
    public static void main( String[] args )
    {
        for ( String str : args )
            System.out.println( str );

        for ( int inx = 0 ; inx < 10 ; ++inx )
        {
            String  propName    = String.format( nameFmt, basePropName, inx );
            String  propVal     = System.getenv( propName );
            System.out.println( propName + ": " + propVal );
        }
    }
}
