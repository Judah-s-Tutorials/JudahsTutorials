package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This application demonstrates how to create
 * and communicate with a child process.
 * It creates a child process,
 * sends commands to its <em>stdin</em> stream
 * then reads a response from its <em>stdout</em> stream.
 * 
 * @author Jack Straub
 * 
 * @see IPCChildTwoWayCommunicationDemo
 * @see IPCParentSimpleDemo
 */
public class IPCParentTwoWayCommunicationDemo
{
    /** Holds the names of environment variables to interrogate. */
    private static List<String> envQueries  = new ArrayList<>();
    /** Holds the names of properties to interrogate. */
    private static List<String> propQueries = new ArrayList<>();
    
    // Initialize the lists of environment variable names
    // and property names.
    static
    {
        envQueries.add( "JAVA_HOME" );
        envQueries.add( "XXX" );
        envQueries.add( "nonsense" );
        
        propQueries.add( "sampleProp" );
        propQueries.add( "java.class.path" );
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
            exec( IPCChildTwoWayCommunicationDemo.class );
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
     * in the IPCParentSimpleDemo class.
     * 
     * @param clazz Class class containing main method to execute
     * 
     * @throws IOException          if an I/O error occurs
     * @throws  InterruptedException 
     *          if interrupted while waiting for child process termination
     *          
     * @see IPCParentSimpleDemo#exec
     */
    private static void exec(Class<?> clazz) 
        throws IOException, InterruptedException
    {
        String          javaHome    = System.getProperty( "java.home" );
        String          javaBin     = javaHome + File.separator + "bin" + File.separator + "java";
        String          classpath   = System.getProperty( "java.class.path" );
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
            InputStream childStdOut = process.getInputStream();
            OutputStream childStdIn = process.getOutputStream();
        )
        {
            PrintWriter         printer     = new PrintWriter( childStdIn, true );
            InputStreamReader   inReader    = new InputStreamReader( childStdOut );
            BufferedReader      bufReader   = new BufferedReader( inReader );
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
        }

        int exitVal = process.waitFor();
        System.out.println( process.exitValue() );
        System.out.println( "exitVal: " + exitVal );
    }
}
