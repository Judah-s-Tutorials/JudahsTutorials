package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;

/**
 * This program reads simple commands from <em>stdin</em>
 * and writes appropriate responses to <em>stdout.</em>
 * The intent is to demonstrate
 * how a child process create by a Java program
 * can communicate with its parent;
 * the parent process write's to the child's <em>stdin</em>
 * and reads from the child's stdout.
 * 
 * @author Jack Straub
 * 
 * @see IPCParentTwoWayCommunicationDemo
 */
public class IPCChildTwoWayCommunicationDemo
{
    private static final String exitResponse    = "exiting";
    
    /**
     * Application entry point.
     * Get a command from <em>stdin</em>
     * and write a response to <em>stdout</em>.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        try ( Reader reader  = new InputStreamReader( System.in ); )
        {
            BufferedReader  bufReader   = new BufferedReader( reader );
            String  response    = "";
            do
            {
                String  command = bufReader.readLine();
                response = getResponse( command );
                System.out.println( response );
            } while ( !response.equals( exitResponse ));
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
        }
        System.exit( 42 );
    }
    
    private static String getResponse( String command )
    {
        String          response    = null;
        String          arg1        = null;
        String          arg2        = null;
        StringTokenizer tizer       = new StringTokenizer( command );
        int             count       = tizer.countTokens();
        
        if ( tizer.hasMoreTokens() )
            arg1 = tizer.nextToken();
        if ( tizer.hasMoreTokens() )
            arg2 = tizer.nextToken();
        
        if ( count > 2 )
            response = " error: command too long";
        else if ( count < 1 )
            response = "error: no command found";
        else
        {
            switch ( arg1 )
            {
            case "exit":
                response = exitResponse;
                break;
            case "getEnv":
                if ( arg2 == null )
                    response = "invalid command";
                else
                {
                    response = System.getenv( arg2 );
                    if ( response == null )
                        response = "not found";
                }
                break;
            case "getProp":
                if ( arg2 == null )
                    response = "NULL: invalid command";
                else
                {
                    response = System.getProperty( arg2 );
                    if ( response == null )
                        response = "not found";
                }
                break;
            default:
                response = "unrecognized command";
            }
        }
        return response;
    }
}
