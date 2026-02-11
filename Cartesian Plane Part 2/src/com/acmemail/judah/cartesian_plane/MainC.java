package com.acmemail.judah.cartesian_plane;

public class MainC
{
    public static void main(String[] args)
    {
        GridLines_01C   canvas  = new GridLines_01C( 600, 500 );
        Root            root    = new Root( canvas );
        root.start();
    }
}
