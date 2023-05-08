package com.acmemail.judah.cartesian_plane.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;

/**
 * Application to read operator input
 * from the console
 * and produce a plot.
 * 
 * @author Jack Straub
 */
public class FileInputApp
{
    private static final CartesianPlane plane       = new CartesianPlane();
    private static final String         filePath    = 
        "Equations" + File.separator + "FileInputAppData.txt";
    private static CommandReader        reader;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Root    root    = new Root( plane );
        root.start();
        try (
            FileReader inReader  = new FileReader( filePath );
            BufferedReader bufReader = new BufferedReader( inReader );
        )
        {
            reader  = new CommandReader( bufReader );
            CommandExecutor executor    = new CommandExecutor( plane );
            executor.exec( FileInputApp::nextCommand, null );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        System.exit( 0 );
    }
    
    /**
     * Obtain the "next command"
     * from the encapsulated CommandReader.
     * This method is used as a wrapper
     * for CommandReader::nextCommand
     * because the wrapped method 
     * declares <em>throws IOException,</em>
     * which prevents it from being used
     * as a Supplier.
     * 
     * @return 
     *      a ParsedCommand object as constructed
     *      by the nextCommand method
     *      of the CommandReader class.
     *      
     * @see CommandReader#nextCommand(String)
     */
    private static ParsedCommand nextCommand()
    {
        ParsedCommand   command = null;
        try
        {
            command = reader.nextCommand( null );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return command;
    }
}
