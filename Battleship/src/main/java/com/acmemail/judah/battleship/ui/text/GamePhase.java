package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.ARE_YOU_SURE;
import static com.acmemail.judah.battleship.StatusMessages.FAILURE;
import static com.acmemail.judah.battleship.StatusMessages.FALSE;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ARG;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ARG_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_C_COMMAND;
import static com.acmemail.judah.battleship.StatusMessages.NOT_ALL_DEPLOYED;
import static com.acmemail.judah.battleship.StatusMessages.SUCCESS;
import static com.acmemail.judah.battleship.StatusMessages.TEXT_PROMPT;
import static com.acmemail.judah.battleship.StatusMessages.TRUE;
import static com.acmemail.judah.battleship.StatusMessages.ON;
import static com.acmemail.judah.battleship.StatusMessages.OFF;
import static com.acmemail.judah.battleship.StatusMessages.YES;
import static com.acmemail.judah.battleship.StatusMessages.NO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.List;
import java.util.Objects;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Provisioner;
import com.acmemail.judah.battleship.Result;
import com.acmemail.judah.battleship.Fleet.Proto;
import com.acmemail.judah.battleship.artwork.text.TextGridParent;
import com.acmemail.judah.battleship.util.LineWrapper;
import com.acmemail.judah.battleship.util.PrintStreamUtils;

public class GamePhase
{
    /** All the different ways the operator might say "on." */
    private static final List<String>   ON_ALIASES  = 
        List.of( ON, TRUE, YES, "1" );
    /** All the different ways the operator might say "off." */
    private static final List<String>   OFF_ALIASES = 
        List.of( OFF, FALSE, NO, "0" );
    /** The maximum length of a line when formatting the help message. */
    private static final int        lineLen     = 60;
    /** Paragraphs in the help message. */
    private static final String[]   helpGraphs  =   { 
        "This is the game's configuration phase. "
        + "In this phase you configure and deploy your fleet, "
        + "specifying the home square "
        + "and orientation of each ship. "
        + "Each task is performed by "
        + "entering a comma-separated list of a command "
        + "and its arguments, "
        + "one command per line. "
        + "For example, the command: ",
    };
    
    /** List of lines in help message. */
    private static final List<String>   help    = List.of( helpGraphs );

    /** Collection of ships after deployment. */
    private final   Fleet           fleet;
    /** Source for command input; defaults to stdin. */
    private final   BufferedReader  consoleReader;
    /** Destination for status and error output; defaults to stdout. */
    private final   PrintStream     outStream;
    /** Controls the grid display. */
    private final   TextGridParent  gridParent;
    
    /** True if the display is to be updated after every command. */
    private boolean                 autoUpdate;
    /** True if game is complete; see {@link #execLoop()}. */
    private boolean                 gameComplete;
    /** True if the configuration process is to be abandoned. */
    private boolean                 quit;
    /** If true, skips the "are you sure" prompt when terminating. */
    private boolean                 really;

    /**
     * Constructor.
     * Creates a new instance
     * with the given Provisioner, 
     * and default input and output streams.

     * @param provisioner   the given Provisioner; may not be null.
     * 
     * @see #ConfigPhase(Provisioner, Reader, PrintStream)
     */
    public GamePhase( Fleet fleet )
    {
        this( fleet, null, null );
    }

    /**
     * Constructor.
     * Allows the operator-input source and the output destination
     * to be supplied by the caller, for example by a test driver.
     * The source and destination may be null,
     * stdin and stdout will be used.
     *
     * @param provisioner   Provisioner to be used internally; may not be null
     * @param reader        source of operator input; may be null
     * @param outStream     destination for status and error output; may be null
     */
    public GamePhase(
        Fleet       fleet,
        Reader      reader,
        PrintStream outStream
    )
    {
        Objects.requireNonNull( fleet, "fleet" );
        this.fleet = fleet;
        Reader  actReader   = reader != null ?
            reader : new InputStreamReader( System.in );
        this.consoleReader  = actReader instanceof BufferedReader buffered ?
            buffered : new BufferedReader( actReader );
        this.outStream  = outStream != null ? outStream : System.out;
        gridParent = new TextGridParent();
        Configurator.nextState();
    }

    /**
     * Public entry for starting the command processing loop.
     */
    public void exec()
    {
        showHelp();
        execLoop();
    }
    
    /**
     * Returns true if the process has been abandoned
     * because a quit command was encountered.
     */
    public boolean isQuit()
    {
        return quit;
    }
    
    private void execLoop()
    {
        gameComplete = false;
        quit = false;
        really = false;
        while ( !quit && !gameComplete )
        {
            showGrids( autoUpdate );
            PrintStreamUtils.printOut( outStream, TEXT_PROMPT );
            String      command = 
                PrintStreamUtils.readLine( consoleReader );
            execCommand( command );
            if ( quit )
            {
                if ( !really )
                    quit = areYouSure( "Quit" );
            }
            else if ( gameComplete )
            {
                if ( !really )
                    gameComplete = areYouSure( "Setup complete" );
            }
        }
    }
    
    /**
     * Execute a command;
     * identification of the command is case-insensitive.
     * After execution,
     * the outcome is printed to {@code outstream},
     * including error messages collected
     * during command processing.
     * 
     * @param command   the command to execute
     */
    private void execCommand( String command )
    {
        String[]    args        = split( command );
        // Note: split will never return a 0-length array
        String      workCommand = args[0].trim().toUpperCase();
        Result      result      = new Result();
        result.setStatus( true );
        switch ( workCommand )
        {
        case "?", "H", "HELP"  -> showHelp();
        case "SHOW", "SHOW-GRID" -> showGrids( true );
        case "Q", "QUIT" -> quit = true;
        case "Q!", "QUIT!" -> quit = really = true;
        case "AUTO", "AUTO-UPDATE" -> autoUpdate( command, result );
        case "S", "STRIKE" -> strike( command, result );
        case "A", "ATTACK" -> attack( command, result );
        case "REVISE" -> revise( command, result );
        case "" -> noop();
        default -> result.addMessage( INVALID_C_COMMAND, false );
        }
        PrintStreamUtils.printOut( outStream, workCommand + ": " );
        String          status  = result.getStatus() ? SUCCESS : FAILURE;
        List<String>    errors  = result.getMessages();
        PrintStreamUtils.writeOut( outStream, status );
        List<String>    errLines    = 
            errors.stream().map( "    "::concat ).toList();
        PrintStreamUtils.writeOut( outStream, errLines );
    }
    
    private Object revise(String command, Result result)
    {
        // TODO Auto-generated method stub
        return null;
    }

    private Object attack(String command, Result result)
    {
        // TODO Auto-generated method stub
        return null;
    }

    private Object strike(String command, Result result)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Writes the help message to the designated output stream.
     */
    private void showHelp()
    {
        LineWrapper     wrapper     = new LineWrapper( lineLen, help );
        List<String>    wrappedList = wrapper.getWrappedList();
        PrintStreamUtils.writeOut( outStream, wrappedList );
    }
    
    /**
     * Sets the auto-update flag to true or false.
     * If the command is issued without an argument,
     * the flag is set to true.
     * If there is an argument,
     * it must be one of 
     * "on," "off," "true," "false," "1" (true), or "0" (false).
     * If the argument is invalid,
     * or there are two or more arguments,
     * the operation will fail.
     * 
     * @param command   the command
     * @param result    a container for storing the result
     */
    private void autoUpdate( String command, Result result )
    {
        String[]    args        = split( command );
        int         argCount    = args.length;
        if ( argCount == 0 )
            autoUpdate = true;
        else if ( argCount > 1 )
            result.addMessage( INVALID_ARG_COUNT, false );
        else
        {
            boolean test    = evalOffOrOn( args[1], result );
            if ( result.getStatus() )
                autoUpdate = test;
        }
    }
    
    private static boolean evalOffOrOn( String token, Result result )
    {
        boolean isOn    = false;
        if ( ON_ALIASES.contains( token ) )
            isOn = true;
        else if ( OFF_ALIASES.contains( token ) )
            isOn = false;
        else
            result.addMessage( INVALID_ARG, false );
        return isOn;
    }

    /**
     * If {@code show} is true,
     * regenerate to the grids
     * and print them to the default output stream.
     * The purpose of the parameter
     * is to simplify use from both
     * {@link GamePhase#execLoop()} 
     * (update display only if autoUpdate is true}
     * and {@link #execCommand(String)}
     * (force regeneration of the grids).
     * 
     * @param show  true, if display is desired
     */
    private void showGrids( boolean show )
    {
        if ( show )
        {
            List<String>    lines   = gridParent.exec();
            PrintStreamUtils.writeOut( outStream, lines );
        }
    }
    
    /**
     * Prompt the operator with a yes/no "are you sure" message.
     * Read and interpret the operator's response:
     * return true if the string entered by the operator
     * starts with 'Y' or 'y,'
     * return false for anything else.
     * 
     * @param prompt    message to print before "are you sure?"
     * @param reader    source of input
     * @param outStream output destination
     * 
     * @return  true, if the operator replies "yes"
     * 
     * @throws IOException  
     *      if an I/O error occurs when reading the operator's response
     */
    private boolean areYouSure( String prompt )
    {
        boolean result  = 
            PrintStreamUtils.areYouSure( prompt, consoleReader, outStream );
        return result;
    }
    
    /**
     * "No-operation,"
     * a method that does nothing,
     * but can be convenient for use in lambdas.
     */
    private void noop()
    {
    }
    
    /**
     * Splits a string into tokens,
     * using "," as a delimiter 
     * and ignoring spaces.
     * The following strings all split into: {@code [a,b]}
     * <ul>
     *      <li>{@code "a,b"}</li>
     *      <li>{@code "a , b"}</li>
     *      <li>{@code "   a   ,   b   "}</li>
     * </ul>
     * @param toSplit   the string to be split
     * 
     * @return  the array of strings resulting from the split
     */
    private String[] split( String toSplit )
    {
        String[]    tokens  = toSplit.split( "\\s*,\\s*");
        return tokens;
    }
}
