package com.acmemail.judah.cartesian_plane;

public class Main
{
    public static void main(String[] args)
    {
        CartesianPlane   canvas  = new CartesianPlane();
        Root            root    = new Root( canvas );
        root.start();
    }
}
