package com.gmail.johnstraub1954.penrose.sandbox;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import com.gmail.johnstraub1954.penrose.utils.Utils;

public class IntersectDemo
{

    public IntersectDemo()
    {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args)
    {
        Point2D point1      = new Point2D.Double( -10, -10 );
        Point2D point3      = new Point2D.Double( 10, 10 );
        Point2D point2      = new Point2D.Double( 0, 0 );
        Point2D point4      = new Point2D.Double( -20, -20 );
        Point2D point5      = new Point2D.Double( 20, 20 );
        Line2D  longLine    = new Line2D.Double( point1, point3 );
        Line2D  shortLine   = new Line2D.Double( point1, point2 );
        System.out.println( Utils.liesOn( point1, longLine ) );
        System.out.println( Utils.liesOn( point3, longLine ) );
        System.out.println( Utils.liesOn( point2, longLine ) );
        System.out.println( Utils.liesOn( point4, longLine ) );
        System.out.println( Utils.liesOn( point5, longLine ) );
    }

}
