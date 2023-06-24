package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.util.function.Consumer;

public class Lithium implements Element
{
    public static void liMethod( Element obj )
    {
        ExecLog log = new ExecLog( "Lithium", "liMethod", obj.getName() );
        ExecLog.add( log );
    }

    @Override
    public Consumer<Element> getFunk()
    {
        Consumer<Element>   funk    = Lithium::liMethod;
        return funk;
    }
}
