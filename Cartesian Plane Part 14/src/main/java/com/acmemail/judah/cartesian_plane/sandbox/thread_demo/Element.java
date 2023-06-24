package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.util.function.Consumer;

public interface Element
{
    Consumer<Element>   getFunk();
    default String getName()
    {
        String  longName    = this.getClass().getName();
        int     start       = longName.lastIndexOf( '.' ) + 1;
        String  name        = longName.substring( start );
        return name;
    }
}
