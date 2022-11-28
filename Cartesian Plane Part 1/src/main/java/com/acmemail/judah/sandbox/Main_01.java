package com.acmemail.judah.sandbox;

import com.acmemail.judah.cartesian_plane.Root;

public class Main_01
{

    public static void main(String[] args)
    {
        CartesianPlane_01   plane   = new CartesianPlane_01();
        Root                root    = new Root( plane );
        root.start();
    }

}
