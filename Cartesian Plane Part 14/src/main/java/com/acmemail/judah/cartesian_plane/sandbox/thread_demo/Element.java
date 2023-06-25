package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.util.function.Consumer;

/**
 * Interface to be implemented
 * by all classes 
 * used for demonstration purposes.
 * 
 * @author Jack Straub
 */
public interface Element
{
    /**
     * Gets a reference to the method
     * to be called
     * if the implementing class
     * is selected for a demonstration.
     * 
     * @return  
     *      a reference to a method 
     *      to be invoked in an execution event
     */
    Consumer<Element>   getFunk();
    
    /**
     * Gets the name of the implementing class
     * without the package qualifier.
     * 
     * @return  name of the implementing class
     */
    default String getName()
    {
        String  longName    = this.getClass().getName();
        int     start       = longName.lastIndexOf( '.' ) + 1;
        String  name        = longName.substring( start );
        return name;
    }
}
