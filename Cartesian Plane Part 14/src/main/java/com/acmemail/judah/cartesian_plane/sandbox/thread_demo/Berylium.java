package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.util.function.Consumer;

public class Berylium implements Element
{
    public static void beMethod( Element obj )
    {
        ExecLog log = new ExecLog( "Berylium", "beMethod", obj.getName() );
        ExecLog.add( log );
    }

    @Override
    public Consumer<Element> getFunk()
    {
        Consumer<Element>   funk    = Berylium::beMethod;
        return funk;
    }
}
