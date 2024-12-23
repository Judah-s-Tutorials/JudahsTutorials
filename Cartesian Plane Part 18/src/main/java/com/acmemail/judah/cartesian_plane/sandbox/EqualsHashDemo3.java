package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class EqualsHashDemo3
{

    public static void main(String[] args)
    {
        List<Point> list1       = new ArrayList<>();
        Point       point1_1    = new Point( 1, 2 );
        Point       point1_2    = new Point( 3, 4 );
        list1.add( point1_1 );
        list1.add( point1_2 );

        List<Point> list2       = new ArrayList<>();
        Point       point2_1    = new Point( 1, 2 );
        Point       point2_2    = new Point( 3, 4 );
        list2.add( point2_1 );
        list2.add( point2_2 );
        System.out.println( list1.equals( list2 ) );
        
        point1_1.x = 100;
        point2_1.x = 100;
        System.out.println( list1.equals( list2 ) );
        System.out.println( list1.containsAll( list2 ) );

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
