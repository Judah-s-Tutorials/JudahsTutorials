package com.acmemail.judah.sandbox;

import com.acmemail.judah.cartesian_plane.Root;

public class RunGridlines
{
    public static void main(String[] args)
    {
        GridLines_01    canvas  = new GridLines_01( 600, 500 );
        Root            root    = new Root( canvas );
        root.start();
    }
}
