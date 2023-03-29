package com.acmemail.judah;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * An instance of this class is responsible
 * for reading and interpreting command lines.
 * Command lines beginning with '#' are assumed
 * to be comments. 
 * Comments and blank line are ignored.
 * Command lines are expected to be 
 * of the form:<br>
 * <code>&nbsp;&nbsp;&nbsp;&nbsp;COMMAND argument</code>
 * where "COMMAND" corresponds to
 * a constant in the Command enum, 
 * and argument may be empty.
 * 
 * @author Jack Straub
 */
public class CommandStringReader
{
    /** Source of the command line input. */
    private final BufferedReader    reader;
    
    /**
     * Constructor.
     * Establishes the source of the command lines.
     * 
     * @param reader    the source of the command lines
     */
    public CommandStringReader( BufferedReader reader )
    {
        this.reader = reader;
    }
    
    /**
     * Reads the next command from the source.
     * Blank lines and lines beginning with '#' are ignored.
     * If EOF is encountered,
     * a ParsedCommand reflecting Command.NONE is returned.
     * Otherwise the line is divided into
     * a command part and argument part (which may be empty);
     * the command part is converted to a Command constant
     * (which may be Command.INVALID).
     * The result is stored in a ParsedCommand object
     * and returned to the user.
     * 
     * @return  
     *      a ParsedCommand object representing
     *      an interpretation of the next line
     *      of the source
     *      
     * @throws IOException  if an IO error occurs
     */
    public ParsedCommand nextCommand() throws IOException
    {
        ParsedCommand   parsedCommand   = null;
        while ( parsedCommand == null )
        {
            String  line    = reader.readLine().trim();
            if ( line == null )
                parsedCommand = new ParsedCommand( Command.NONE, "", "" );
            else if ( line.isEmpty() )
                ;
            else if ( line.startsWith( "#" ) )
                ;
            else
            {
                int     split   = line.indexOf( ' ' );
                String  cmdStr  = line;
                String  argStr  = "";
                // If necessary, divide input string into command and argument
                // (everything after the command, excluding trimmings)
                if ( split > 0 )
                {
                    cmdStr = line.substring( 0, split );
                    argStr = line.substring( split + 1 ).trim();
                }
                Command command = Command.toCommand( cmdStr );
                parsedCommand = new ParsedCommand( command, cmdStr, argStr );
            }
        }
        return parsedCommand;
    }
}
