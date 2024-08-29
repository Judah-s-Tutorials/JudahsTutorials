package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Point;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class EqualsHashDemo1
{

    public static void main(String[] args)
    {
        Set<Point>  set1        = new ConcurrentSkipListSet<>();
        Point       point1_1    = new Point( 1, 2 );
        Point       point1_2    = new Point( 3, 4 );
        set1.add( point1_1 );
        set1.add( point1_2 );

        Set<Point>  set2        = new ConcurrentSkipListSet<>();
        Point       point2_1    = new Point( 1, 2 );
        Point       point2_2    = new Point( 3, 4 );
        set2.add( point2_1 );
        set2.add( point2_2 );
        
        point1_1.x = 100;
        point2_1.x = 100;
        System.out.println( set1 );
        System.out.println( set2 );
        System.out.println( set1.equals( set2 ) );
        System.out.println( set1.containsAll( set2 ) );

//        System.out.println( "set1 A: " + set1 );
//        point1_1.x = 100;
//        System.out.println( "set1 B: " + set1 );
//        set1.remove( point1_1 );
//        System.out.println( "set1 C: " + set1 );
//        set1.add( new Point( 1, 2 ) );
//        System.out.println( "set1 D: " + set1 );

//        set1.add( new Point( 1, 2 ) );
//        System.out.println( set1.equals( set2 ) );
//        
//        System.out.println( set1 );
//        System.out.println( set2 );
    }

}
