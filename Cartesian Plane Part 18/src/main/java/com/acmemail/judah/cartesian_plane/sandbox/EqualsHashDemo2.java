package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqualsHashDemo2
{

    public static void main(String[] args)
    {
        Map<String,Point> map1  = new HashMap<>();
        Point       point1_1    = new Point( 1, 2 );
        Point       point1_2    = new Point( 3, 4 );
        map1.put( "A", point1_1 );
        map1.put( "B", point1_2 );

        Map<String,Point>   map2          = new HashMap<>();
        Point       point2_1    = new Point( 1, 2 );
        Point       point2_2    = new Point( 3, 4 );
        map2.put( "A", point2_1 );
        map2.put( "B", point2_2 );
        System.out.println( map1.equals( map2 ) );
        
        point1_1.x = 100;
        point2_1.x = 100;
        System.out.println( map1.equals( map2 ) );
        System.out.println( map1.entrySet() );
        System.out.println( map2.entrySet() );
    }

}
