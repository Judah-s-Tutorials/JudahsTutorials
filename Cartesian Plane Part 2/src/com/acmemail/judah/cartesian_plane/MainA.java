package com.acmemail.judah.cartesian_plane;

public class MainA
{
    public static void main(String[] args)
    {
        GridLines_01A   canvas  = new GridLines_01A( 600, 500 );
        Root            root    = new Root( canvas );
        root.start();
    }
}
