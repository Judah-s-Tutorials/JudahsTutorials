package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.FAILURE;
import static com.acmemail.judah.battleship.StatusMessages.FALSE;
import static com.acmemail.judah.battleship.StatusMessages.GRID_NOT_FOUND;
import static com.acmemail.judah.battleship.StatusMessages.HELP_END;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ARG;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ARG_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_COMMAND;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_COORDINATES;
import static com.acmemail.judah.battleship.StatusMessages.NO;
import static com.acmemail.judah.battleship.StatusMessages.OFF;
import static com.acmemail.judah.battleship.StatusMessages.ON;
import static com.acmemail.judah.battleship.StatusMessages.OUT_OF_BOUNDS;
import static com.acmemail.judah.battleship.StatusMessages.SUCCESS;
import static com.acmemail.judah.battleship.StatusMessages.TEXT_PROMPT;
import static com.acmemail.judah.battleship.StatusMessages.TRUE;
import static com.acmemail.judah.battleship.StatusMessages.YES;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.List;
import java.util.function.BooleanSupplier;

import org.apache.commons.lang3.function.BooleanConsumer;

import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship.Result;
import com.acmemail.judah.battleship.artwork.text.TextGridParent;
import com.acmemail.judah.battleship.model.Cell2DView;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.GridCoords;
import com.acmemail.judah.battleship.util.Command;
import com.acmemail.judah.battleship.util.LineWrapper;
import com.acmemail.judah.battleship.util.PrintStreamUtils;
import com.acmemail.judah.battleship.util.UndoRedoManager;

/**
 * <ul>
 * <li>
 *      h, hit --
 *      Record a hit on an opponent at a given row/column address.
 *      The arguments are the name of the opponent,
 *      and the coordinates to attack.
 *      If there are only two players,
 *      the opponent name may be omitted.
 *      Examples:
<pre style="padding-left: 2em;">
hit,john,b3
hit,e7
h,paul,f2</pre>
 * </li>
 * <li>
 *      m, miss --
 *      Record a miss on an opponent's grid
 *      at a given row/column address.
 *      The arguments are the name of the opponent,
 *      and the coordinates to attack.
 *      If there are only two players,
 *      the opponent name may be omitted.
 *      Examples:
<pre style="padding-left: 2em;">
miss,john,b3
miss,e7
m,paul,f2</pre>
 *      Players may not attack themselves.
 *      The attack is assumed to be a miss.
 * </li>
 * <li>
 *      s, strike --
 *      Record a strike on the home grid.
 *      The argument is the coordinate pair to attack.
 *      Examples:
<pre style="padding-left: 2em;">
strike,c9
s,a10</pre>
 * </li>
 * <li>
 *      unstrike --
 *      This command is intended to reverse a previously
 *      (and, presumably, erroneously) executed strike command.
 *      The argument is the coordinate pair to un-strike.
 *      The cell at the given coordinates
 *      is explicitly set to unattacked.
 *      Example:
<pre style="padding-left: 2em;">
unstrike,c9</pre>
 * </li>
 * <li>
 *      unattack --
 *      This command is intended to reverse a previously
 *      (and, presumably, erroneously) executed hit or miss command.
 *      The arguments are the name of the opponent,
 *      and the coordinates to attack.
 *      If there are only two players,
 *      the opponent name may be omitted.
 *      Examples:
<pre style="padding-left: 2em;">
unattack,paul,c9
unattack,a1</pre>
 * </li>
 * <li>
 *      q, quit --
 *      Quit the game without finishing it.
 *      The operator will be prompted with 
 *      "Are you sure?"
 *      Any response beginning with 'y'
 *      will terminate the game;
 *      any other response will result
 *      in game continuation.
 * </li>
 * <li>
 *      q!, quit! --
 *      This is the same as quit,
 *      except the "Are you sure?" logic is skipped,
 *      and the game is abruptly terminated.
 * </li>
 * <li>
 *      go, game-over --
 *      Declare the game finished,
 *      and exit to post-processing.
 *      The operator will be prompted with 
 *      "Are you sure?"
 *      Any response beginning with 'y'
 *      will exit the game;
 *      any other response will result
 *      in game continuation.
 * </li>
 * <li>
 *      go!, game-over! --
 *      This is the same as game-over,
 *      except the "Are you sure?" logic is skipped,
 *      and the game is abruptly exited.
 * </li>
 * <li>
 *      u, undo -- 
 *      undo the last action;
 *      not applicable to {@code quit}
 *      or {@code game-over}.
 * </li>
 * <li>
 *      r, redo -- 
 *      redo the last action;
 *      not applicable to {@code quit}
 *      or {@code game-over}.
 * </li>
 * </ul>
 */
public class GamePhase implements TextPhase
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
        
        HELP_END
    };
    
    /** List of lines in help message. */
    private static final List<String>   help    = List.of( helpGraphs );

    /** Undo/redo stacks. */
    private final   UndoRedoManager undoStack;
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
     * Default constructor.
     * Creates a new instance
     * with the default input and output streams.
     * 
     * @see #GamePhase( Reader, PrintStream)
     */
    public GamePhase()
    {
        this( null, null );
    }

    /**
     * Constructor.
     * Create a new instance encapsulating the current grid configuration.
     * Allows the operator-input source and the output destination
     * to be supplied by the caller, for example by a test driver.
     * The source and destination may be null,
     * in which case stdin and stdout will be used.
     *
     * @param reader        source of operator input; may be null
     * @param outStream     destination for status and error output; may be null
     */
    public GamePhase(
        Reader      reader,
        PrintStream outStream
    )
    {
        Reader  actReader   = reader != null ?
            reader : new InputStreamReader( System.in );
        this.consoleReader  = actReader instanceof BufferedReader buffered ?
            buffered : new BufferedReader( actReader );
        this.outStream  = outStream != null ? outStream : System.out;
        undoStack = new UndoRedoManager();
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
    
    /**
     * Execute a loop,
     * reading and executing commands
     * read from {@link #consoleReader}.
     * The loop terminates
     * when a quit, or game-over command is encountered.
     */
    private void execLoop()
    {
        gameComplete = false;
        quit = false;
        really = false;
        boolean done    = false;
        while ( !done )
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
                done = quit;
            }
            else if ( gameComplete )
            {
                if ( !really )
                    gameComplete = areYouSure( "Setup complete" );
                done = gameComplete;
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
        case "AUTO", "AUTO-UPDATE" -> autoUpdate( command, result );
        case "S", "STRIKE" -> strike( command, result );
        case "UNSTRIKE" -> unstrike( command, result );
        case "HIT" -> attack( command, true, result );
        case "MISS" -> attack( command, false, result );
        case "U", "UNDO" -> undoStack.undo();
        case "R", "REDO" -> undoStack.redo();
        case "UNATTACK" -> unattack( command, result );
        case "" -> noop();
        case "Q", "QUIT" -> quit = true;
        case "Q!", "QUIT!" -> quit = really = true;
        case "GO", "GAME-OVER" -> gameComplete = true;
        case "GO!", "GAME-OVER!" -> gameComplete = really = true;
        default -> result.addMessage( INVALID_COMMAND, false );
        }
        PrintStreamUtils.printOut( outStream, workCommand + ": " );
        String          status  = result.getStatus() ? SUCCESS : FAILURE;
        List<String>    errors  = result.getMessages();
        PrintStreamUtils.writeOut( outStream, status );
        List<String>    errLines    = 
            errors.stream().map( "    "::concat ).toList();
        PrintStreamUtils.writeOut( outStream, errLines );
    }    
    
    private void unattack( String command, Result result )
    {
        String[]    args        = split( command );
        int         argCount    = args.length;
        Grid2D      grid        = null;
        Label       labelCoords = null;
        
        if ( argCount < 2 || argCount > 3 )
            result.addMessage( INVALID_ARG_COUNT, false );
        else if ( (grid = getGrid( args )) == null )
            result.addMessage( GRID_NOT_FOUND, false );
        else if ( (labelCoords = getLabelCoords( args )) == null )
            result.addMessage( INVALID_COORDINATES, false );
        else
        {
            UnattackCommand unattack    = 
                new UnattackCommand( grid, labelCoords );
            undoStack.executeCommand( unattack );
        }
    }

    /**
     * Validate and execute an {@code attack} command.
     * Including the command,
     * the client passes two or three arguments.
     * If there are three arguments
     * the second is taken as the name of the grid,
     * and the third as the coordinates to attack.
     * If there are two arguments,
     * the second is taken as the coordinates to attack,
     * and the grid is assumed to be the home grid.
     * The command can fail because
     * there are an incorrect number of arguments,
     * the target grid cannot be found,
     * the coordinates argument is malformed,
     * or the coordinates are out-of-bounds.
     * The result of command execution
     * is recorded in the given Result container.
     * 
     * @param command   the command arguments
     * @param isHit     true, if the attack hit an opponent's ship
     * @param result    the given result container
     */
    private void attack( String command, boolean isHit, Result result )
    {
        String[]    args        = split( command );
        int         argCount    = args.length;
        Grid2D      grid        = null;
        
        if ( argCount < 2 || argCount > 3 )
            result.addMessage( INVALID_ARG_COUNT, false );
        else if ( (grid = getGrid( args )) == null )
            result.addMessage( GRID_NOT_FOUND, false );
        else 
            attack( grid, args, isHit, result );
    }

    /**
     * Validate and execute a {@code strike} command.
     * Including the command,
     * the client passes two arguments,
     * the second being the coordinates
     * on the home grid to attack.
     * The command can fail because
     * there are an incorrect number of arguments,
     * the coordinates argument is malformed,
     * or the coordinates are out-of-bounds.
     * The result of command execution
     * is recorded in the given Result container.
     * 
     * @param command   the command arguments
     * @param result    the given result container
     */
    private void strike( String command, Result result )
    {
        String[]    args        = split( command );
        int         argCount    = args.length;
        Label       labelCoords;
        
        if ( argCount != 2 )
            result.addMessage( INVALID_ARG_COUNT, false );
        else if ( (labelCoords = getLabelCoords( args )) == null )
            result.addMessage( INVALID_COORDINATES, false );
        else
        {
            StrikeCommand   cmd = new StrikeCommand( labelCoords );
            undoStack.executeCommand( cmd );
        }
    }

    /**
     * Validate and execute an {@code unstrike} command.
     * Including the command,
     * the client passes two arguments,
     * the second being the coordinates
     * on the home grid to unstrike.
     * The command can fail because
     * there are an incorrect number of arguments,
     * the coordinates argument is malformed,
     * or the coordinates are out-of-bounds.
     * The result of command execution
     * is recorded in the given Result container.
     * 
     * @param command   the command arguments
     * @param result    the given result container
     */
    private void unstrike( String command, Result result )
    {
        String[]    args        = split( command );
        int         argCount    = args.length;
        Label       labelCoords;
        
        if ( argCount != 2 )
            result.addMessage( INVALID_ARG_COUNT, false );
        else if ( (labelCoords = getLabelCoords( args )) == null )
            result.addMessage( INVALID_COORDINATES, false );
        else
        {
            UnstrikeCommand   cmd = new UnstrikeCommand( labelCoords );
            undoStack.executeCommand( cmd );
        }
    }
    
    /**
     * This helper method is for the direct support of
     * {@link #attack(String, boolean, Result)} and
     * {@link #attack(String, boolean, Result)}.
     * The argument list from the hit/miss command is passed in.
     * If there are two arguments,
     * the second is taken to be the grid coordinates,
     * otherwise the third is used.
     * If valid, 
     * a HitCoomand or MissCommand is generated and executed.
     * <p>
     * Precondition: 
     * The length of the args array has been validated.
     * 
     * @param grid      the grid to attack
     * @param args      the command arguments
     * @param isHit     
     *      true if this is a hit command, false for a miss command
     * @param result    container for recording the operation result
     */
    private void attack( 
        Grid2D grid, 
        String[] args, 
        boolean isHit, 
        Result result 
    )
    {
        Label   labelCoords = getLabelCoords( args );
        if ( labelCoords == null )
            result.addMessage( INVALID_COORDINATES, false );
        else
        {
            Command attack  = new AttackCommand( grid, labelCoords, isHit );
            undoStack.executeCommand( attack );
        }
    }
    
    /**
     * This helper method is for the direct support of
     * {@link #attack(String, boolean, Result)}.
     * The argument list from the attack command is passed in,
     * and, if there are more than two arguments,
     * the second is taken to be the name of the target grid.
     * The name of the target grid may be omitted 
     * if there is a single opponent.
     * If there are only two arguments,
     * and there is a single opponent,
     * the target grid is assumed to be the opponent's.
     * If found, the target grid is returned,
     * otherwise null is returned.
     * 
     * @param args  the argument list from the attack command
     * 
     * @return  the target grid, or null if not found
     */
    private static Grid2D getGrid( String[] args )
    {
        Grid2D  grid    = null;
        if ( args.length > 2 )
            grid = Grid2D.getGrid( args[1].trim() );
        else
        {
            Grid2D          homeGrid    = Grid2D.getHomeGrid();
            List<Grid2D>    allGrids    = Grid2D.getAllGrids();
            if ( allGrids.size() == 2 )
            {
                grid = allGrids.get( 0 );
                if ( grid == homeGrid )
                    grid = allGrids.get( 1 );
            }
        }
        return grid;
    }
    
    /**
     * This helper method is for the direct support of
     * {@link #attack(String, boolean, Result)} and
     * {@link #strike(String, Result)}.
     * The argument list from the attack/strike command is passed in.
     * If there are two arguments,
     * the second is taken to be the grid coordinates,
     * otherwise the third is used.
     * If valid, 
     * the given argument is converted to a GridCoords object
     * and returned,
     * otherwise null is returned.
     * 
     * @param args      the argument list from the attack command
     * 
     * @return  the converted GridCoords object, or null if not found
     */
    private static Label getLabelCoords( String[] args )
    {
        Grid2D      homeGrid    = Grid2D.getHomeGrid();
        String      labelArg    = args.length > 2 ? args[2] : args[1];
        Label       labelCoords = new Label( labelArg.trim() );
        if ( !labelCoords.isStatus() )
            labelCoords = null;
        // TODO Grid2D.contains should be static
        else if ( !homeGrid.contains( labelCoords.getGridCoords() ) )
            labelCoords = null;
        
        return labelCoords;
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
        if ( argCount > 2 )
            result.addMessage( INVALID_ARG_COUNT, false );
        else
        {
            boolean test    = evalOffOrOn( args, result );
            if ( result.getStatus() )
            {
                AutoCommand auto    =
                    new AutoCommand( test, b -> autoUpdate = b, () -> autoUpdate );
                undoStack.executeCommand( auto );
            }
        }
    }
    
    /**
     * Examine an argument list representing a boolean, 
     * and determine whether it should be interpreted
     * as true or false:
     * a) if there's only one argument, the result is true;
     * b) if there is more than one argument,
     * the result is determined by comparing args[1]
     * to the tokens in {@link #ON_ALIASES} (true)
     * and {@link #OFF_ALIASES} (false).
     * If args[1] exists, and contains value other than
     * the on- and off-aliases,
     * an illegal argument results,
     * and the result object is updated accordingly.
     * Arguments after args[1] are ignored.
     * 
     * @param args      the arguments to examine
     * @param result    the result of the examination
     * 
     * @return  
     *      true, if args is valid and evaluates to true,
     *      otherwise false
     */
    private static boolean evalOffOrOn( String[] args, Result result )
    {
        boolean isOn    = true;
        if ( args.length > 1 )
        {
            String  token   = args[1];
            if ( ON_ALIASES.contains( token ) )
                isOn = true;
            else if ( OFF_ALIASES.contains( token ) )
                isOn = false;
            else
                result.addMessage( INVALID_ARG, false );
        }
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
     * 
     * @return  true, if the operator replies "yes"
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
    
    /**
     * Defines the actions to take when attacking a cell,
     * or undoing a previously executed attack.
     */
    private static class AttackCommand implements Command
    {
        private final Grid2D        grid;
        private final GridCoords    gridCoords;
        private final boolean       isHit;
        
        private boolean isSplattedOrig;
        private boolean isOpponentOrig;
        
        /**
         * Constructor.
         * Fully initializes an object of this type.
         * 
         * @param grid          the grid being attacked
         * @param labelCoords   the coordinates being attacked
         */
        public AttackCommand( Grid2D grid, Label labelCoords, boolean isHit )
        {
            super();
            this.grid = grid;
            this.gridCoords = labelCoords.getGridCoords();
            this.isHit = isHit;
        }

        @Override
        public void execute()
        {
            Cell2DView  cell    = grid.getCellView( gridCoords );
            isSplattedOrig = cell.isSplatted();
            isOpponentOrig = cell.isOpponent();
            grid.attack( gridCoords );
            grid.setOpponent( gridCoords, isHit );
        }

        @Override
        public void undo()
        {
            if ( isSplattedOrig )
                grid.attack( gridCoords );
            else
                grid.unattack( gridCoords );
            grid.setOpponent( gridCoords, isOpponentOrig );
        }        
    }
    
    /**
     * Encapsulates undo/redo logic for the strike command.
     */
    private static class StrikeCommand implements Command
    {
        private final GridCoords    gridCoords;
        private boolean             isSplattedOrig;
        
        public StrikeCommand( Label labelCoords )
        {
            gridCoords = labelCoords.getGridCoords();
        }
        
        @Override
        public void execute()
        {
            Grid2D      home    = Grid2D.getHomeGrid();
            Cell2DView  cell    = home.getCellView( gridCoords );
            isSplattedOrig = cell.isSplatted();
            home.attack( gridCoords );
        }
        
        @Override
        public void undo()
        {
            Grid2D      home    = Grid2D.getHomeGrid();
            if ( !isSplattedOrig )
                home.unattack( gridCoords );
        }
    }
    
    /**
     * Encapsulates undo/redo logic for the unstrike command.
     */
    private static class UnstrikeCommand implements Command
    {
        private final GridCoords    gridCoords;
        private boolean             isSplattedOrig;
        
        public UnstrikeCommand( Label labelCoords )
        {
            gridCoords = labelCoords.getGridCoords();
        }
        
        @Override
        public void execute()
        {
            Grid2D      home    = Grid2D.getHomeGrid();
            Cell2DView  cell    = home.getCellView( gridCoords );
            isSplattedOrig = cell.isSplatted();
            home.unattack( gridCoords );
        }
        
        @Override
        public void undo()
        {
            Grid2D      home    = Grid2D.getHomeGrid();
            if ( isSplattedOrig )
                home.attack( gridCoords );
        }
    }

    /**
     * Defines the actions to take when unattacking a cell,
     * or undoing a previously executed unattack.
     */
    private static class UnattackCommand implements Command
    {
        private final Grid2D        grid;
        private final GridCoords    gridCoords;
        
        private boolean isSplattedOrig;
        private boolean isOpponentOrig;
        
        /**
         * Constructor.
         * Fully initializes an object of this type.
         * 
         * @param grid          the grid being unattacked
         * @param labelCoords   the coordinates being unattacked
         */
        public UnattackCommand( Grid2D grid, Label labelCoords )
        {
            super();
            this.grid = grid;
            this.gridCoords = labelCoords.getGridCoords();
        }

        @Override
        public void execute()
        {
            Cell2DView  cell    = grid.getCellView( gridCoords );
            isSplattedOrig = cell.isSplatted();
            grid.unattack( gridCoords );
            grid.setOpponent( gridCoords, false );
        }

        @Override
        public void undo()
        {
            if ( isSplattedOrig )
                grid.attack( gridCoords );
            else
                grid.unattack( gridCoords );
            grid.setOpponent( gridCoords, isOpponentOrig );
        }
    }
    
    /**
     * Encapsulates undo/redo logic for the auto command.
     */
    private static class AutoCommand implements Command
    {
        private final BooleanConsumer   setter;
        private final BooleanSupplier   getter;
        private final boolean           newState;
        private boolean origState;
        
        public AutoCommand( 
            boolean newState, 
            BooleanConsumer setter, 
            BooleanSupplier getter 
        )
        {
            this.newState = newState;
            this.setter = setter;
            this.getter = getter;
        }
        
        public void execute()
        {
            origState = getter.getAsBoolean();
            setter.accept( newState );
        }
        
        public void undo()
        {
            setter.accept( origState );
        }
    }
}
