package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This application demonstrates 
 * how to start a Java program as a subprocess
 * and read from its <em>stdout.</em>
 * 
 * @author Jack Straub
 *
 * @see IPCDemo2Child
 */
/**
 * @author Jack Straub
 *
 */
public class IPCDemo1Parent
{
    /** 
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        try
        {
            exec( IPCDemo1Child.class );
        }
        catch ( IOException | InterruptedException exc )
        {
            exc.printStackTrace();
        }
    }
    
    /**
     * Start a child process.
     * Read from its stdout stream until it exits.
     * 
     * @param clazz Class class containing main method to execute
     * 
     * @throws IOException          if an I/O error occurs
     * @throws  InterruptedException 
     *          if interrupted while waiting for child process termination
     */
    private static void exec( Class<?> clazz ) 
        throws IOException, InterruptedException
    {
        // Get the name of the class the encapsulates the child process
        String          className   = clazz.getName();
        
        // Create a list that will encapsulate the command used to execute
        // the child process. Each token in the list will wind up as an
        // element on the command line, for example:
        //     java --class-path target/classes IPCDemo1Child
        List<String>    command     = new ArrayList<>();
        command.add( "java" );
        command.add(  "--class-path" );
        command.add( "target\\classes" );
        command.add( className );
        command.add( "Jane" );
        
        // Create the process builder and configure the environment
        // that will be used when the child process is executed.
        ProcessBuilder builder = new ProcessBuilder(command);
        
        // Start the child process. Note that the start method can
        // throw an IOException.
        Process process = builder.start();

        // Wait for child process to terminate and print its exit
        // value. Note that process.waitFor() can throw an
        // InterruptedException.
        int exitVal = process.waitFor();
        System.out.println( "exitVal: " + exitVal );
    }
}
