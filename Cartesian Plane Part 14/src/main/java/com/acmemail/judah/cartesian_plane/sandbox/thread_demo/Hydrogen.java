package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.awt.Color;
import java.util.function.Consumer;

public class Hydrogen implements Element
{
    public static void hyMethod( Element obj )
    {
        ExecLog log = new ExecLog( "Hydrogen", "hyMethod", obj.getName() );
        ExecLog.add( log );
    }

    @Override
    public Consumer<Element> getFunk()
    {
        Consumer<Element>   funk    = Hydrogen::hyMethod;
        return funk;
    }
}
