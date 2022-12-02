package com.acmemail.judah.figures;

import com.acmemail.judah.cartesian_plane.Root;

public class Main_01
{

    public static void main(String[] args)
    {
        GridLines_Figure_01   plane   = new GridLines_Figure_01();
        Root                root    = new Root( plane );
        root.start();
    }

}
