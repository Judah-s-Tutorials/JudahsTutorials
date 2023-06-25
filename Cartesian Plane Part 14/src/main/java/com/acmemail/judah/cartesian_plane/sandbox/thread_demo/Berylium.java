package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.util.function.Consumer;

/**
 * A class used in this demonstration.
 * 
 * @author Jack Straub
 */
public class Berylium implements Element
{
    /**
     * The method to be called if this class
     * is selected for execution
     * in this demonstration.
     * 
     * @param obj   input parameter; required from demonstration purposes only
     */
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
