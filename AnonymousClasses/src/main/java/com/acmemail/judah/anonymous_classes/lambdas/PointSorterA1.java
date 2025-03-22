package com.acmemail.judah.anonymous_classes.lambdas;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.anonymous_classes.Utils;

public class PointSorterA1
{
    public static void main(String[] args)
    {
        Point       origin  = new Point( 0, 0 );
        List<Point> points  = new ArrayList<>();
        for ( int inx = 0 ; inx < 25 ; ++inx )
            points.add( Utils.randomPoint() );
        points.sort( (Point p1, Point p2) -> {
            double  p1Dist  = origin.distance( p1 );
            double  p2Dist  = origin.distance( p2 );
            int     result  = (int)(p1Dist - p2Dist);
            return result;
        });
        for ( Point point : points )
            System.out.println( point );
    }
}
