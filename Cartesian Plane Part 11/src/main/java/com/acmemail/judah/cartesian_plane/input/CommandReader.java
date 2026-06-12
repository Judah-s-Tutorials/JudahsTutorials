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
 * <p>
 * Shortcut processing is case-insensitive;
 * "x=" and "X=" are equivalent.
 * </p>
 * <p>
 * Note that the input source
 * belongs to the client,
 * and it's up to the client
 * if the source needs to be reset.
 * The methods {@linkplain #stream()}
 * and {@linkplain #nextCommand(String)}
 * read from the same source;
 * traversal of the source should be restricted
 * to one of these methods.
 * </p>
 * 
 * @author Jack Straub
 * 
 * @see #stream()
 * @see #nextCommand(String)
 */
public class CommandReader
{
    /** Describes all known shortcuts, and the commands they map to. */
    private static final Shortcut[] shortcuts   =
    {
        new Shortcut( "x=", Command.XEQUALS ),
        new Shortcut( "y=", Command.YEQUALS ),
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
     * <p>
     * Note: this method and {@linkplain CommandReader#stream()}
     * read from the same input source.
     * It's expected that the input source
     * will only be traversed once,
     * so interleaving calls to these two methods
     * will yield unpredictable results.
     * </p>

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
            String  line    = filter( reader.readLine() );
            if ( line == null )
                parsedCommand = new ParsedCommand( Command.NONE, "", "" );
            else if ( line.isEmpty() )
                ;
            else 
            {
                parsedCommand = parseCommand( line );
            }
        }
        return parsedCommand;
    }
    
    /**
     * Generates a stream of ParsedCommands
     * corresponding to lines from the input source.
     * Per the specification for parsing input lines,
     * leading and trailing spaces are discarded, and
     * blank lines and comments are skipped.
     * <p>
     * Note: this method and {@linkplain CommandReader#nextCommand(String)}
     * read from the same input source.
     * It's expected that the input source
     * will only be traversed once,
     * so interleaving calls to these two methods
     * will yield unpredictable results.
     * </p>
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
     * If the given string is null, empty, or blank,
     * a ParsedCommand wrapping Command.NONE is returned.
     * Otherwise, if it cannot be matched to a Command constant,
     * a ParsedCommand wrapping Command.INVALID is returned.
     * 
     * @param   line    the given string
     * 
     * @return  a ParsedCommand derived from the given string
     */
    public static ParsedCommand parseCommand( String line )
    {
        String          workLine        = line == null ? "" : line.trim(); 
        ParsedCommand   parsedCommand   = null;
        if ( (parsedCommand = processShortcuts( workLine )) == null )
        {
            // If necessary, divide input string into command and argument
            // (everything after the command, excluding trimmings);
            // "cmd" -> command = cmd, no argument;
            // "cmd aaa" -> command = cmd, arg = "aaa"
            // "cmd aaa bbb ccc" -> command = cmd, arg = "aaa bbb ccc"
            String[]    parts       = workLine.split( "\\s+", 2 );
            
            // Note: a split on the empty string yields a one element
            // array, with "" in [0].
            int         numParts    = parts.length;
            String      cmdStr      = parts[0];
            String      argStr      = numParts < 2  ? "" : parts[1];
            Command command = Command.toCommand( cmdStr );
            parsedCommand = new ParsedCommand( command, cmdStr, argStr );
        }
        
        return parsedCommand;
    }
    
    /**
     * Filter out whitespace and comments
     * (lines that start with "#").
     * If input is null, this method returns null.
     * If, after trimming, line begins with "#"
     * this method returns an empty string,
     * else it returns the trimmed string.
     *  
     * @param line  the line to test
     * 
     * @return  
     *      null if the input is null;
     *      empty string if string consists of whitespace
     *      or a comment;
     *      the trimmed input otherwise
     */
    private static String filter( String line )
    {
        String  result  = line;
        if ( result != null )
        {
            result = result.trim();
            if ( result.startsWith( "#" ) )
                result =  "";
        }
        return result;
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
                .filter( s -> s.matches( line ) )
                .findFirst()
                    .orElse( null );
        
        if ( shortcut != null )
        {
            int     shortStrLen = shortcut.shortStrLen;
            String  cmdString   = line.substring( 0, shortStrLen );
            String  argString   = 
                line.substring( shortStrLen ).trim();
            parsedCommand = 
                new ParsedCommand( shortcut.cmd, cmdString, argString );
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
        /** Length of short name for command. */
        public final int        shortStrLen;
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
            this.shortStrLen = shortStr.length();
            this.cmd = cmd;
        }
        
        /**
         * Tests the beginning of a given string
         * for a case-insensitive match
         * to the encapsulated shortcut.
         * 
         * @param input the given string
         * 
         * @return  
         *      true if the start of input is a case-insensitive match
         *      for the encapsulated shortcut
         */
        public boolean matches( String input )
        {
            boolean result  =
                input.regionMatches( true, 0, shortStr, 0, shortStrLen );
            return result;
        }
    }
}
