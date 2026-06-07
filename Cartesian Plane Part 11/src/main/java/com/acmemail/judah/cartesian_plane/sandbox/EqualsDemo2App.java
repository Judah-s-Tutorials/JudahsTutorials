package com.acmemail.judah.cartesian_plane.sandbox;

/**
 * App to verify that the EqualsDemo2 class works.
 */
public class EqualsDemo2App
{
    /**
     * Application entry point.
     * 
     * @param args  command-line arguments; not used
     */
    public static void main( String[] args )
    {
        String  strA        = "pogo";
        String  strB        = "albert";
        EqualsDemo2 objA    = new EqualsDemo2( strA, strB );
        EqualsDemo2 objB    = new EqualsDemo2( strB, strA );
        EqualsDemo2 objC    = new EqualsDemo2( strA, strB );
        test( objA, objB );
        test( objA, objC );
    }
    
    /**
     * Test two objects for equality and print the result.
     * 
     * @param obj1  first object to test
     * @param obj2  second object to test
     */
    private static void test( EqualsDemo2 obj1, EqualsDemo2 obj2 )
    {
        String  relation    = obj1.equals( obj2 ) ?
            " is equal to " : " is not equal to ";
        System.out.printf( "(%s)%s(%s)%n", obj1, relation, obj2 );
    }
}
