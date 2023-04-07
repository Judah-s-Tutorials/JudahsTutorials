package com.acmemail.judah.cartesian_plane.sandbox;

import com.acmemail.judah.cartesian_plane.exp4j.Command;

/**
 * Application to demonstrate the <em>usage</em> method
 * of the <em>Command</em> enum.
 * 
 * @author Jack Straub
 */
public class CommandUsageDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command-line arguments; not used
     */
    public static void main(String[] args)
    {
        System.out.println( Command.usage() );
    }

}
