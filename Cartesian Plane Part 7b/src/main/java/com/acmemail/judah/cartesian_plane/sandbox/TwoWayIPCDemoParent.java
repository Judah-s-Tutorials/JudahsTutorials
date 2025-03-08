package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import temp.IPCParentSimpleDemo2;

/**
 * This application demonstrates how to create
 * and communicate with a child process.
 * It creates a child process,
 * sends commands to its <em>stdin</em> stream
 * then reads a response from its <em>stdout</em> stream.
 * 
 * @author Jack Straub
 * 
 * @see TwoWayIPCDemoChild
 * @see IPCParentSimpleDemo2
 */
public class TwoWayIPCDemoParent
{
    /** Holds the names of environment variables to interrogate. */
    private static final List<String> envQueries  = new ArrayList<>();
    /** Holds the names of properties to interrogate. */
    private static final List<String> propQueries = new ArrayList<>();
    
    // Initialize the lists of environment variable names
    // and property names.
    static
    {
        envQueries.add( "JAVA_HOME" );
        envQueries.add( "XXX" );
        envQueries.add( "NONSENSE" );
        
        propQueries.add( "java.class.path" );
        propQueries.add( "sampleProp" );
        propQueries.add( "notExpectedToBeFound" );
    }
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        try
        {
            exec( TwoWayIPCDemoChild.class );
        }
        catch ( IOException | InterruptedException exc )
        {
            exc.printStackTrace();
        }
    }
    
    /**
     * Create and have a query/response dialog with
     * another Java process. 
     * Additional details can be found
     * in the IPCParentSimpleDemo2 class.
     * 
     * @param clazz Class class containing main method to execute
     * 
     * @throws IOException          if an I/O error occurs
     * @throws  InterruptedException 
     *          if interrupted while waiting for child process termination
     *          
     * @see IPCParentSimpleDemo2#exec
     */
    private static void exec(Class<?> clazz) 
        throws IOException, InterruptedException
    {
        String          javaHome    = System.getProperty( "java.home" );
        String          javaBin     = 
            javaHome + File.separator + "bin" + File.separator + "java";
        String          classpath   = 
            System.getProperty( "java.class.path" );
        String          className   = clazz.getName();
        
        List<String>    command     = new ArrayList<>();
        command.add( javaBin );
        command.add(  "--class-path" );
        command.add( classpath );
        command.add( "-DsampleProp=sampleValue" );
        command.add( className );
        
        ProcessBuilder builder = new ProcessBuilder( command );
        Map<String, String> env = builder.environment();
        env.put( "XXX", "yyy" );
        Process process = builder.start();
        try ( 
            BufferedReader bufReader = process.inputReader();
            OutputStream childStdIn = process.getOutputStream();
            PrintWriter printer = new PrintWriter( childStdIn, true );
        )
        {
            for ( String envName : envQueries )
            {
                printer.println( "getEnv " + envName );
                String              response    = bufReader.readLine();
                System.out.println( "env from child: " + envName + " = " + response );
            }
            for ( String propName : propQueries )
            {
                printer.println( "getProp " + propName );
                String              response    = bufReader.readLine();
                System.out.println( "prop from child: " + propName + " = " + response );
            }
            printer.println( "exit" );
            String  response    = bufReader.readLine();
            System.out.println( "child status: " + response );
        }

        // Wait for child process to terminate and print its exit
        // value. Note that process.waitFor() can throw an
        // InterruptedException.
        boolean childTerminated = 
            process.waitFor( 1000, TimeUnit.MILLISECONDS );
        String  message         = "";
        if ( childTerminated )
            message = "child exit value: " + process.exitValue();
        else
            message = "child failed to terminate as expected";
        System.out.println( message );
    }
}
