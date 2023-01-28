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

public class StarterProcessA
{

    public static void main(String[] args) throws Exception
    {
        exec( TargetProcessA.class );
    }
    
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
        command.add( className );
        
        ProcessBuilder builder = new ProcessBuilder(command);
        Map<String, String> env = builder.environment();
        env.clear();
        env.put( "XXX", "yyy" );
        Process process = builder.start();
        try ( 
            InputStream outStr = process.getInputStream();
            OutputStream inStr = process.getOutputStream();
        )
        {
            PrintWriter         printer     = new PrintWriter( inStr, true );
            printer.println( "Hello from parent process" );
            
            InputStreamReader   inReader    = new InputStreamReader( outStr );
            BufferedReader      bufReader   = new BufferedReader( inReader );
            String              line        = null;
            while ( (line = bufReader.readLine()) != null )
                System.out.println( "from target process: " + line );
        }

        int exitVal = process.waitFor();
        System.out.println( "exitVal: " + exitVal );
    }
}
