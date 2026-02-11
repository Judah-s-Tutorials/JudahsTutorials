package com.acmemail.judah.cartesian_plane;

public class MainF
{
    public static void main(String[] args)
    {
        GridLines_01F   canvas  = new GridLines_01F( 600, 500 );
        Root            root    = new Root( canvas );
        root.start();
    }
}
