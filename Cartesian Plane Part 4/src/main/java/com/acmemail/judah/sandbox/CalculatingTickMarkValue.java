package com.acmemail.judah.sandbox;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.Root;

public class CalculatingTickMarkValue
{
    public static void main( String[] args )
    {
        CartesianPlane  plane   = new CartesianPlane( 1000, 500 );
        plane.setGridUnit( 400 );
        plane.setGridLineDraw( false );
        plane.setTicMinorDraw( false );
        plane.setTicMajorMPU( 10 );
        
        Root            root    = new Root( plane );
        root.start();

    }
}
