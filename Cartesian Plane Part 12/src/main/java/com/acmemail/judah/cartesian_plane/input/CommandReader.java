package com.acmemail.judah.cartesian_plane.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
 * <p>
 * Two command shortcuts are recognized:
 * </p>
 * <ul>
 * <li>"x=argument" is a shortcut for XEQUALS argument</li>
 * <li>"y=argument" is a shortcut for YEQUALS argument</li>
 * </ul>
 * 
 * @author Jack Straub
 */
public class CommandReader
{
    /** Describes all known shortcuts, and the commands they map to. */
    private static final Shortcut[] shortcuts   =
    {
        new Shortcut( "x=", Command.XEQUALS ),
        new Shortcut( "y=", Command.YEQUALS ),
        new Shortcut( "r=", Command.REQUALS ),
        new Shortcut( "t=", Command.TEQUALS ),
        new Shortcut( "X=", Command.XEQUALS ),
        new Shortcut( "Y=", Command.YEQUALS ),
        new Shortcut( "R=", Command.REQUALS ),
        new Shortcut( "T=", Command.TEQUALS ),
    };
    
    /** Source of the command line input. */
    private final BufferedReader    reader;
    
    /**
     * Constructor.
     * Establishes the source of the command lines.
     * 
     * @param reader    the source of the command lines
     */
    public CommandReader( BufferedReader reader )
    {
        this.reader = reader;
    }
    
    /**
     * Reads the next command from the source.
     * A prompt is printed to stdout, if provided.
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
     * @param   prompt  
     *      the prompt to display; may be null
     *      in which case no prompt will be displayed
     * 
     * @return  
     *      a ParsedCommand object representing
     *      an interpretation of the next line
     *      of the source
     *      
     * @throws IOException  if an IO error occurs
     */
    public ParsedCommand nextCommand( String prompt ) throws IOException
    {
        ParsedCommand   parsedCommand   = null;
        while ( parsedCommand == null )
        {
            if ( prompt != null )
                System.out.print( prompt );
            String  line    = reader.readLine();
            if ( line == null )
                parsedCommand = new ParsedCommand( Command.NONE, "", "" );
            else if ( (line = line.trim()).isEmpty() )
                ;
            else if ( line.startsWith( "#" ) )
                ;
            else 
                parsedCommand = parseCommand( line );
        }
        return parsedCommand;
    }
    
    /**
     * Generates a stream of ParsedCommands
     * corresponding to lines from the input source.
     * Per the specification for parsing input lines,
     * leading and trailing spaces are discarded, and
     * blank lines and comments are skipped.
     * 
     * @return  a stream of ParsedCommands
     *          corresponding to lines from the input source
     */
    public Stream<ParsedCommand> stream()
    {
        Stream<ParsedCommand>   pcStream    =
            reader.lines()
            .map( String::trim )
            .filter( Predicate.not( String::isEmpty ) )
            .filter( s -> !s.startsWith( "#" ) )
            .map( CommandReader::parseCommand );
        
        return pcStream;
    }
    
    /**
     * Converts a given string to a ParsedCommand.
     * The input string is assumed to be 
     * non-null, non-empty and trimmed.
     * 
     * @param   line    the given string
     * 
     * @return  a ParsedCommand derived from the given string
     */
    public static ParsedCommand parseCommand( String line )
    {
        ParsedCommand   parsedCommand   = processShortcuts( line );
        if ( parsedCommand == null )
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
        
        return parsedCommand;
    }
    
    /**
     * Examines the start of a line of input
     * for the short form of a command.
     * If found, a ParsedCommand object is returned
     * describing the referenced command and argument;
     * the argument is trimmed.
     * If not found, null is returned.
     * 
     * @param line  line of input to examine
     * 
     * @return 
     *      ParsedCommand describing the referenced command,
     *      or null if none
     */
    private static ParsedCommand processShortcuts( String line )
    {
        ParsedCommand   parsedCommand   = null;
        Shortcut        shortcut        =
            Arrays.stream( shortcuts )
                .filter( s -> line.startsWith( s.shortStr ) )
                .findFirst()
                    .orElse( null );
        
        if ( shortcut != null )
        {
            int     shortStrLen = shortcut.shortStr.length();
            String  arg         = 
                line.substring( shortStrLen ).trim();
            parsedCommand = 
                new ParsedCommand( shortcut.cmd, shortcut.shortStr, arg );
        }
        
        return parsedCommand;
    }
    
    /**
     * Describes a String/Command pair,
     * where the String is 
     * a short name for a command,
     * and the Command 
     * is the referenced command.
     * 
     * @author Jack Straub
     */
    private static class Shortcut
    {
        /** Short name for command. */
        public final String     shortStr;
        /** Referenced command. */
        public final Command    cmd;
        
        /**
         * Constructor.
         * 
         * @param shortcut  short name of command
         * @param cmd       referenced command
         */
        public Shortcut( String shortcut, Command cmd )
        {
            this.shortStr = shortcut;
            this.cmd = cmd;
        }
    }
}
