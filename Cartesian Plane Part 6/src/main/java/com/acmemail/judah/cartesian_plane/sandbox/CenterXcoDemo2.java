package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Rectangle2D;

public class CenterXcoDemo2
{
    public static void main(String[] args)
    {
        Rectangle2D rect    = new Rectangle2D.Float( 0, 0, 5, 4 );
        System.out.println( rect.getCenterX() );
    }
}
