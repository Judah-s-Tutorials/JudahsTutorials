package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.util.function.Consumer;

/**
 * A class used in this demonstration.
 * 
 * @author Jack Straub
 */
public class Helium implements Element
{
    /**
     * The method to be called if this class
     * is selected for execution
     * in this demonstration.
     * 
     * @param obj   input parameter; required from demonstration purposes only
     */
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
