package com.acmemail.judah.cartesian_plane;

public class MainD
{
    public static void main(String[] args)
    {
        GridLines_01D   canvas  = new GridLines_01D( 600, 500 );
        Root            root    = new Root( canvas );
        root.start();
    }
}
