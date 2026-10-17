package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.CONFIG_GRID_END;
import static com.acmemail.judah.battleship.StatusMessages.CONFIG_GRID_START;
import static com.acmemail.judah.battleship.StatusMessages.CONFIG_SHIPS_END;
import static com.acmemail.judah.battleship.StatusMessages.CONFIG_SHIPS_START;
import static com.acmemail.judah.battleship.StatusMessages.DEPLOYED_HEADER;
import static com.acmemail.judah.battleship.StatusMessages.FAILURE;
import static com.acmemail.judah.battleship.StatusMessages.HELP_END;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ARG_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_COORDINATES;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_C_COMMAND;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ORIENTATION;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_REC_NUM;
import static com.acmemail.judah.battleship.StatusMessages.NOT_ALL_DEPLOYED;
import static com.acmemail.judah.battleship.StatusMessages.SUCCESS;
import static com.acmemail.judah.battleship.StatusMessages.TEXT_PROMPT;
import static com.acmemail.judah.battleship.StatusMessages.TO_DEPLOY_HEADER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Fleet.Proto;
import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship.Provisioner;
import com.acmemail.judah.battleship.Result;
import com.acmemail.judah.battleship.artwork.text.BasicTextGrid;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.GridCoords;
import com.acmemail.judah.battleship.model.Orientation;
import com.acmemail.judah.battleship.model.Ship2D;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.util.LineWrapper;

public class ConfigPhase implements TextPhase
{
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
        
        "",
        "\tdeploy,1,a1,vertical",
        "",
        
        "Positions the first ship in the to-deploy list "
        + "in the upper-left corner of the grid "
        + "with a vertical orientation. "
        + "Commands are case-insensitive "
        + "and tolerant of whitespace, "
        + "so the following are equivalent commands: ",
        
        "",
        "\tdeploy,1,a1,vertical",
        "\tDEPLOY,1,a1,vertical",
        "\tdeploy , 1 , a1 , vertical",
        
        "Allowable commands are: ",
        "\t-- ?, h, help: show this message",
        "\t-- done: designate the configuration phase as complete; "
        + "the command will fail if not all ships in the to-deploy list "
        + "have been deployed; "
        + "the operator will be prompted "
        + "with an \"are you sure\" message. ",
        
        "\t-- done!: same as \"done,\" "
        + "but skips the \"are you sure\" prompt.",
        
        "\t-- q,quit: discards all work, terminates the application; "
        + "the operator will be prompted with an \"are you sure\" message. ",
        "\t-- q!,quit!: same as \"quit,\", "
        + "but skips the \"are you sure\" prompt. ",
        
        "\t--dep,deploy: "
        + "removes the designated ship "
        + "from the to-be-deployed list "
        + "and adds it to the deployed list. "
        + "The arguments are: ",
        
        "\t\tdeploy, ship-number, cell-ID, orientation",
        "\tWhere:",
        
        "\t\t-- deploy is the literal command",
        "\t\t-- ship-number is the number of the ship in the to-deploy list",
        "\t\t-- cell-ID is the row:column ID if the deployed ship's "
        + "home cell, for example A1",
        "\t\t-- orientation is either \"horizontal\" "
        + "or \"vertical.\" "
        + "any argument beginning with 'h' "
        + "will be taken for \"horizontal,\" "
        + "and any argument beginning with 'v' " 
        + "will be taken for \"vertical.\" ",
        
        "\t-- und,undeploy: "
        + "removes the designated ship "
        + "from the deployed list "
        + "and returns it to the to-be-deployed list. "
        + "The arguments are: ",
        
        "\t\tdeploy, ship-number",
        "\tWhere:",
        
        "\t\t-- undeploy is the literal command",
        "\t\t-- ship-number is the number of the ship in the deployed list",
        
        "\t-- show, show-grid: "
        + "display a textual a representation of the physical grid "
        + "as currently configured. ",
        
        "\t-- upd, update: "
        + "display the current disposition "
        + "of the to-be-deployed and deployed lists. ",
        
        HELP_END
    };
    /** List of lines in help message. */
    private static final List<String>   list    = List.of( helpGraphs );
    
    /** Source of the provisioning data. */
    private final   Provisioner     provisioner;
    /** Collection of ships after deployment. */
    private final   Fleet           fleet;
    /** Text-based grid showing ship deployment. */
    private final   BasicTextGrid   textGrid;
    /** True if configuration is complete; see {@link #execLoop()}. */
    private boolean configComplete;
    /** True if the configuration process is to be abandoned. */
    private boolean quit;
    /** If true, skips the "are you sure" prompt when terminating. */
    private boolean really;
    /** Source for command input; defaults to stdin. */
    private final BufferedReader  consoleReader;
    /** Destination for status and error output; defaults to stdout. */
    private final   PrintStream     outStream;
    
    /**
     * Constructor.
     * Creates a new instance
     * with the given Provisioner, 
     * and default input and output streams.

     * @param provisioner   the given Provisioner; may not be null.
     * 
     * @see #ConfigPhase(Provisioner, Reader, PrintStream)
     */
    public ConfigPhase( Provisioner provisioner )
    {
        this( provisioner, null, null );
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
    public ConfigPhase(
        Provisioner     provisioner,
        Reader          reader,
        PrintStream     outStream
    )
    {
        Objects.requireNonNull( provisioner, "provisioner" );
        this.provisioner = provisioner;
        Reader  actReader   = reader != null ?
            reader : new InputStreamReader( System.in );
        this.consoleReader  = actReader instanceof BufferedReader buffered ?
            buffered : new BufferedReader( actReader );
        this.outStream  = outStream != null ? outStream : System.out;
        
        textGrid = new BasicTextGrid( Grid2D.getHomeGrid() );
        fleet = new Fleet();
        provisioner.getToDeploy().forEach( 
            t -> fleet.addToBeDeployed( t, null ) 
        );
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
     * Returns the encapsulated TextProvisioner.
     * 
     * @return  the encapsulated TextProvisioner
     */
    public Provisioner getProvisioner()
    {
        return provisioner;
    }
    
    /**
     * Returns a list of ship types that must be deployed,
     * but haven't been deployed yet.
     * 
     * @return  a list of ship types that must be deployed
     */
    public List<ShipType2D> getToBeDeployed()
    {
        List<Proto>         toDeployProtos  = fleet.getToBeDeployedProtos();
        List<ShipType2D>    toDeploy        = 
            toDeployProtos.stream()
                .map( p -> p.getType() )
                .toList();
        return toDeploy;
    }
    
    /**
     * Returns a list of ship types that have been deployed.
     * 
     * @return  a list of ship types that have been deployed
     */
    public List<ShipType2D> getDeployedTypes()
    {
        List<Proto>         deployedProtos  = fleet.getAllDeployedProtos();
        List<ShipType2D>    deployed        = 
            deployedProtos.stream()
                .map( p -> p.getType() )
                .toList();
        return deployed;
    }
    
    /**
     * Returns a list of ship types that have been deployed.
     * 
     * @return  a list of ships that have been deployed
     */
    public List<Ship2D> getDeployedShips()
    {
        List<Ship2D>    deployed        = fleet.getAllDeployedShips();
        return deployed;
    }
    
    /**
     * Execute the command processing loop.
     * At the start of each loop execution,
     * the updated list of ship types to be deployed
     * and ship types that have been deployed
     * is displayed.
     * A prompt is displayed, 
     * and a command is read from the console reader
     * and forwarded to {@link #execCommand(String)} for processing.
     * The loop terminates
     * when the process is quit,
     * or declared complete.
     */
    private void execLoop()
    {
        configComplete = false;
        quit = false;
        really = false;
        while ( !quit && !configComplete )
        {
            showShipTable();
            printOut( TEXT_PROMPT );
            String      command = readLine( consoleReader );
            execCommand( command );
            if ( quit )
            {
                if ( !really )
                    quit = areYouSure( "Quit" );
            }
            else if ( configComplete )
            {
                if ( !really )
                    configComplete = areYouSure( "Setup complete" );
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
        String      workCommand = args.length == 0 ? 
            "" : args[0].trim().toUpperCase();
        Result      result      = new Result();
        result.setStatus( true );
        switch ( workCommand )
        {
        case "?", "H", "HELP"  -> showHelp();
        case "U", "UPDATE" -> showUpdate();
        case "SHOW", "SHOW-GRID" -> showGrid();
        case "Q", "QUIT" -> quit = true;
        case "Q!", "QUIT!" -> quit = really = true;
        case "DONE" -> done( command, result, false );
        case "DONE!" -> done( command, result, true );
        case "DEPLOY" -> deploy( command, result );
        case "UND", "UNDEPLOY" -> undeploy( command, result );
        case "" -> noop();
        default -> result.addMessage( INVALID_C_COMMAND, false );
        }
        printOut( workCommand + ": " );
        String          status  = result.getStatus() ? SUCCESS : FAILURE;
        List<String>    errors  = result.getMessages();
        writeOut( status );
        List<String>    errLines    = 
            errors.stream().map( "    "::concat ).toList();
        writeOut( errLines );
    }
    
    private void deploy( String command, Result result )
    {
        // deploy,num,coords,orientation[,remark]
        String[]            args        = split( command );
        int                 recNum      = 0;
        int                 argCount    = args.length;
        List<Proto>         toDeploy    = fleet.getToBeDeployedProtos();
        GridCoords          coords      = null;
        Orientation         orient      = null;
        if ( argCount != 4 || argCount > 5 )
            result.addMessage( INVALID_ARG_COUNT, false );
        else if ( (recNum = getRecNum( args[1] )) == 0 )
            result.addMessage( INVALID_REC_NUM, false );
        else if ( recNum > toDeploy.size() )
            result.addMessage( INVALID_REC_NUM, false );
        else if ( (coords = getGridCoords( args[2] ) ) == null )
            result.addMessage( INVALID_COORDINATES, false );
        else if ( (orient = getOrientation( args[3])) == null )
            result.addMessage( INVALID_ORIENTATION, false );
        else
        {
            Proto       proto       = toDeploy.get( recNum - 1 );
            ShipType2D  type        = proto.getType();
            Ship2D      ship        = new Ship2D( type, coords, orient );
            Result      depResult   = fleet.deploy( ship, proto );
            if ( !depResult.getStatus() )
            {
                result.setStatus( false );
                depResult.getMessages().forEach( result::addMessage );
            }
        }
    }
    
    private void done( String command, Result result, boolean really )
    {
        // deploy,num,coords,orientation[,remark]
        List<Proto>         toDeploy    = fleet.getToBeDeployedProtos();
        if ( toDeploy.size() != 0  )
            result.addMessage( NOT_ALL_DEPLOYED, false );
        else 
        {
            configComplete = true;
            this.really = really;
        }
    }
    
    private void undeploy( String command, Result result )
    {
        // undeploy,num
        String[]            args        = split( command );
        int                 recNum      = 0;
        int                 argCount    = args.length;
        List<Proto>         deploy      = fleet.getAllDeployedProtos();
        if ( argCount != 2 )
            result.addMessage( INVALID_ARG_COUNT, false );
        else if ( (recNum = getRecNum( args[1] )) == 0 )
            result.addMessage( INVALID_REC_NUM );
        else if ( recNum > deploy.size() )
            result.addMessage( INVALID_REC_NUM );
        else
        {
            Proto       proto       = deploy.get( recNum - 1 );
            Result      depResult   = fleet.undeploy( proto );
            if ( !depResult.getStatus() )
            {
                result.setStatus( false );
                depResult.getMessages().forEach( result::addMessage );
            }
        }
    }

    private void showGrid()
    {
        List<String>    lines   = textGrid.toPrintString();
        lines.add( 0, CONFIG_GRID_START );
        lines.add( CONFIG_GRID_END );
        writeOut( lines );
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
     * @param toSplit
     * @return
     */
    private String[] split( String toSplit )
    {
        String[]    tokens  = toSplit.split( "\\s*,\\s*");
        return tokens;
    }
    
    private static String readLine( BufferedReader reader )
    {
        String  line;
        try
        {
            line = reader.readLine().trim();
        }
        catch ( IOException exc )
        {
            String  msg = "Unexpected error reading from console";
            throw new BattleshipException( msg, exc);
        }
        return line;
    }
    
    private boolean areYouSure( String prompt )
    {
        final String    areYouSure  = "are you sure? (y/n) ";
        String  fullPrompt  = prompt + "; " + areYouSure;
        printOut( fullPrompt );
        String  reply       = readLine( consoleReader ).toUpperCase();
        boolean result      = reply.isEmpty() ? 
            false : reply.charAt( 0 ) == 'Y';
        return result;
    }
    
    private void noop()
    {
    }
    
    private Orientation getOrientation( String strOrientation )
    {
        Orientation orientation = null;
        if ( strOrientation.length() < 1 )
            ;
        else
        {
            char    firstChar       = strOrientation.charAt( 0 );
            char    firstCharUpper  = Character.toUpperCase( firstChar );
            orientation = switch ( firstCharUpper )
                {
                case 'H' -> Orientation.HORIZONTAL;
                case 'V' -> Orientation.VERTICAL;
                default -> null;
                };
        }
        return orientation;
    }
    
    private GridCoords getGridCoords( String strLabel )
    {
        Label       label   = new Label( strLabel );
        GridCoords  coords  = label.isStatus() ? label.getGridCoords() : null;
        return coords;
    }
    
    private int getRecNum( String strRecNum )
    {
        int     recNum  = 0;
        try
        {
            recNum = Integer.parseInt( strRecNum );
        }
        catch ( NumberFormatException exc )
        {
        }
        return recNum;
    }
    
    private void showHelp()
    {
        LineWrapper     wrapper     = new LineWrapper( lineLen, list );
        List<String>    wrappedList = wrapper.getWrappedList();
        writeOut( wrappedList );
    }
    
    private void showUpdate()
    {
        printOut( "%nCURRENT CONFIGURATION%n" );
        showDimensions();
        showShipTable();
        printOut( "" );
    }
    
    private void showDimensions()
    {
        final String    fmt     = "Grid: %d rows, %d columns";
        Integer     rows        = provisioner.getRows();
        Integer     cols        = provisioner.getCols();
        String      gridStr     = String.format( fmt, rows, cols );
        writeOut( List.of( gridStr ) );
    }

    private void showShipTable()
    {
        final String    fmt         = "%-25s %s";
        final String    headingFmt  = "%-25s %s";
        final String    underScore  = "===============";
        final String    heading1    = 
            String.format( headingFmt, TO_DEPLOY_HEADER, DEPLOYED_HEADER );
        final String    heading2    = 
            String.format( headingFmt, underScore, underScore );
        
        List<Proto>         toDeploy    = fleet.getToBeDeployedProtos();
        int                 toDepCount  = toDeploy.size();
        List<Proto>         deployed    = fleet.getAllDeployedProtos();
        int                 depCount    = deployed.size();
        int                 numLines    = Math.max( toDepCount, depCount );
        List<String>        lines       = new ArrayList<String>();
        lines.add( CONFIG_SHIPS_START );
        lines.add( heading1 );
        lines.add( heading2 );
        for ( int inx = 0 ; inx < numLines ; ++inx )
        {
            String  toDepString = inx < toDepCount ?
                getToDeployStr( inx + 1, toDeploy.get( inx ) ) : ""; 
            String  depString   = inx < depCount ?
                getDeployedStr( inx + 1, deployed.get( inx ) ) : ""; 
            String  output      = 
                String.format( fmt, toDepString, depString );
            lines.add( output );
        }
        lines.add( CONFIG_SHIPS_END );
        lines.add( "" );
        writeOut( lines );
    }
    
    private String getToDeployStr( int inx, Proto proto )
    {
        final String fmt    = "%3d. %s %dX%d";
        ShipType2D  type        = proto.getType();
        String      name        = type.typeName();
        int         length      = type.length();
        int         breadth     = type.breadth();
        String      configStr   = String.format( fmt, inx, name, length, breadth );
        return configStr;
    }
    
    private String getDeployedStr( int inx, Proto proto )
    {
        final String fmt    = "%3d. %s %dX%d %s %s";
        ShipType2D  type        = proto.getType();
        String      name        = type.typeName();
        int         length      = type.length();
        int         breadth     = type.breadth();
        Ship2D      ship        = fleet.getDeployedShip( proto );
        GridCoords  coords      = ship.getCoords();
        Label       label       = new Label( coords );
        String      labelStr    = label.getLabel();
        String      orient      = ship.getOrientation().toString();
        String      configStr   = 
            String.format( fmt, inx, name, length, breadth, labelStr, orient );
        return configStr;
    }

    /**
     * Write a single string to this instance's output stream
     * <em>without</em> a line separator,
     * and flush the stream before returning.
     *
     * @param line  the string to write
     */
    private void printOut( String line )
    {
        outStream.print( line );
        outStream.flush();
    }

    /**
     * Write a single string followed by a line-separator
     * to this instance's output stream,
     * and flush the stream before returning.
     *
     * @param line  the string to write
     */
    private void writeOut( String line )
    {
        writeOut( List.of( line ) );
    }

    /**
     * Write each string in a list to this instance's output stream.
     * Each string is written on a separate line;
     * the stream is flushed before returning.
     *
     * @param lines the strings to write
     */
    private void writeOut( List<String> lines )
    {
        lines.forEach( outStream::println );
        outStream.flush();
    }
}
