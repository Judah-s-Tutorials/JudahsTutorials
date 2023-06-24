package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.util.function.Consumer;

public class Helium implements Element
{
    public static void heMethod( Element obj )
    {
        ExecLog log = new ExecLog( "Helium", "heMethod", obj.getName() );
        ExecLog.add( log );
    }

    @Override
    public Consumer<Element> getFunk()
    {
        Consumer<Element>   funk    = Helium::heMethod;
        return funk;
    }
}
