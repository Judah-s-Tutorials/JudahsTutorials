package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Rectangle2D;

public class Rectangle2DCenterDemo
{
    public static void main(String[] args)
    {
        Rectangle2D rect    = new Rectangle2D.Float( 0, 0, 4, 4 );
        System.out.println( rect.getCenterX() );
        System.out.println( rect.getCenterY() );
    }
}
