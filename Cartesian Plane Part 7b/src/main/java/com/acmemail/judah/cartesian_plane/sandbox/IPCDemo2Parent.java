package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
public class IPCDemo2Parent
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
            exec( IPCDemo2Child.class );
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
    private static void exec(Class<?> clazz) 
        throws IOException, InterruptedException
    {
        // Find the java executable to start the child process
        String          javaHome    = System.getProperty( "java.home" );
        StringBuilder   bldr        = new StringBuilder( javaHome );
        bldr.append( File.separator ).append( "bin" )
            .append( File.separator ).append( "java" );
        String          javaBin     = bldr.toString();
        
        // Get the classpath to use to start the child process
        String          classpath   = System.getProperty( "java.class.path" );
        
        // Get the name of the class that encapsulates the child process
        String          className   = clazz.getName();
        
        // Create a list that will encapsulate the command used to execute
        // the child process. Each token in the list will wind up as an
        // argument on the command line, for example:
        //     JAVA_HONME\bin\java --class-path .;lib/toots.jar SampleChildClass
        List<String>    command     = new ArrayList<>();
        command.add( javaBin );
        command.add(  "--class-path" );
        command.add( classpath );
        command.add( className );
        
        // Create the process builder and configure the environment
        // that will be used when the child process is executed.
        ProcessBuilder builder = new ProcessBuilder(command);
        Map<String, String> env = builder.environment();
        env.clear();
        env.put( "XXX", "yyy" );
        env.put( "ABC", "def" );
        env.put( "SPOT", "hound" );
        
        // Start the child process. Note that the start method can
        // throw an IOEception.
        Process process = builder.start();
        
        // Get an input stream that can be used to read the child
        // process's stdout. Try-with-resources is used in order to
        // facilitate closing the input stream. Note that when using
        // a try-with-resources statement a catch block is optional
        try ( 
            InputStream childStdout = process.getInputStream();
            InputStreamReader   inReader    = 
                new InputStreamReader( childStdout );
            BufferedReader      bufReader   = 
                new BufferedReader( inReader );
        )
        {
            String  line    = bufReader.readLine();
            while ( line != null && !line.equals( "done" ) )
            {
                System.out.println( "from child process: " + line );
                line = bufReader.readLine();
            }
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
