package com.acmemail.judah.cartesian_plane;

public class Main
{
    public static void main(String[] args)
    {
        CartesianPlane   canvas  = new CartesianPlane( 600, 500 );
        Root            root    = new Root( canvas );
        root.start();
    }
}
