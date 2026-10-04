package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.INVALID_ARG_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_COORDINATES;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_C_COMMAND;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ORIENTATION;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_REC_NUM;
import static com.acmemail.judah.battleship.StatusMessages.NOT_ALL_DEPLOYED;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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
import com.acmemail.judah.battleship.TextProvisioner;
import com.acmemail.judah.battleship.artwork.text.BasicTextGrid;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.GridCoords;
import com.acmemail.judah.battleship.model.Orientation;
import com.acmemail.judah.battleship.model.Ship2D;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.util.LineWrapper;

public class ConfigPhase
{
    private static final int        lineLen     = 60;
    private static final String[]   graphs      =   { 
        "This is the game's configuration phase. "
        + "In this phase you configure and deploy your fleet. "
        + "specify the names of the players "
        + "design a type of ship, "
        + "and establish the types of the ships "
        + "that must be deployed by each player. "
        + "Each task is performed by "
        + "entering a comma-separated list of a command "
        + "and its arguments, "
        + "one command per line. "
        + "For example, the command: ",
        
        "",
        "\tdim,20,30",
        "",
        
        "Establishes a grid with 20 rows and 30 columns. "
        + "Commands are case-insensitive "
        + "and tolerant of whitespace, "
        + "so the following are equivalent commands: ",
        
        "",
        "\tdim,20,30",
        "\tDIM,20,30",
        "\tDIM , 20 , 30",
        
        "Allowable commands are: ",
        "\t-- ?, h, help: show this message",
        "\t-- done: designate the setup phase as complete",
        "\t-- quit: discards all work, terminates the application",
        "\t-- dim,rows,cols: "
        + "where 'rows' is a decimal integer "
        + "specifying the number of rows in the game grid, "
        + "and 'cols' is the number of columns.",
        
        "\t-- show, show-grid: "
        + "show a representation of the grid "
        + "as currently configured",
        
        "\t-- type: "
        + "This command registers the type of a ship. "
        + "It has two forms:",
        
        "\t\t-- 'type,default' "
        + "registers all the traditional ship types ",
        "\t\t-- 'type,name,length,breadth' ",
        "\twhere:",
        "\t\t--'name' is the name of the type of a ship. "
        + "names are case-sensitive, "
        + "and must be unique",
        "\t\t--'length' is the length of the ship, "
        + "and 'breadth' is its breadth. ",
        "\tThe command: ",
        "\t\ttype,SuperCarrier,12,3",
        "\tcreates a type of ship named 'SuperCarrier' "
        + "with a length of 12 and a breadth of 3. "
        + "A ship of this type deployed horizontally "
        + "will have a width of 12 and a height of 3; "
        + "deployed vertically, it will have a width of 3 "
        + "and a height of 12.",
        
        "\t-- ?, h, help: show this help message",
        "\t-- u, update: display the updated configuration",
        "\t-- done: mark setup complete",
        "\t-- q, quit: exit from this application",
        "==================================="
    };
    private static final List<String>   list    = List.of( graphs );
    
    private final   Provisioner     provisioner;
    private final   Fleet           fleet;
    private final   BasicTextGrid   textGrid;
    private boolean configComplete;
    private boolean quit;
    private boolean really;
    private BufferedReader  consoleReader;
    
    public ConfigPhase( TextProvisioner provisioner )
    {
        Objects.requireNonNull( provisioner, "provisioner" );
        this.provisioner = provisioner;
        textGrid = new BasicTextGrid( Grid2D.getHomeGrid() );
        fleet = new Fleet();
        provisioner.getToDeploy().forEach( 
            t -> fleet.addToBeDeployed( t, null ) 
        );
        Configurator.nextState();
    }
    
    public void exec()
    {
        showHelp();
        exec( this::execLoop );
    }
    
    public boolean isQuit()
    {
        return quit;
    }
    
    private void exec( Runnable runner )
    {
        try ( 
            InputStreamReader inStream  = new InputStreamReader( System.in );
            BufferedReader    bufStream = new BufferedReader( inStream ); 
        )
        {
            consoleReader = bufStream;
            runner.run();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            throw new BattleshipException( exc );
        }
    }
    
    private void execLoop()
    {
        final String    prompt  = "Enter a command (? for help): ";
        configComplete = false;
        quit = false;
        really = false;
        while ( !quit && !configComplete )
        {
            showShipTable();
            printOut( prompt );
            String      command = readLine( consoleReader );
            execCommand( command );
            if ( quit )
            {
                if ( !really )
                {
                    if ( !areYouSure( "Quit" ) )
                        quit = false;
                }
            }
            else if ( configComplete )
            {
                if ( areYouSure( "Setup complete" ) )
                    configComplete = true;
            }
        }
    }
    
    private void execCommand( String command )
    {
        String[]    args        = command.split( "," );
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
        case "DEPLOY" -> deploy( command, result );
        case "UND", "UNDEPLOY" -> undeploy( command, result );
        case "DONE" -> done( command, result );
        case "" -> noop();
        default -> result.addMessage( INVALID_C_COMMAND, false );
        }
        printOut( workCommand + ": " );
        String          status  = result.getStatus() ? "success" : "failure";
        List<String>    errors  = result.getMessages();
        writeOut( status );
        List<String>    errLines    = 
            errors.stream().map( "    "::concat ).toList();
        writeErr( errLines );
    }
    
    private void deploy( String command, Result result )
    {
        // deploy,num,coords,orientation[,remark]
        String[]            args        = command.split( ",", 5 );
        int                 recNum      = 0;
        int                 argCount    = args.length;
        List<Proto>         toDeploy    = fleet.getToBeDeployedProtos();
        GridCoords          coords      = null;
        Orientation         orient      = null;
        if ( argCount < 3 )
            result.addMessage( INVALID_ARG_COUNT, false );
        else if ( (recNum = getRecNum( args[1] )) == 0 )
            result.addMessage( INVALID_REC_NUM );
        else if ( recNum > toDeploy.size() )
            result.addMessage( INVALID_REC_NUM );
        else if ( (coords = getGridCoords( args[2] ) ) == null )
            result.addMessage( INVALID_COORDINATES );
        else if ( (orient = getOrientation( args[3])) == null )
            result.addMessage( INVALID_ORIENTATION );
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
    
    private void done( String command, Result result )
    {
        // deploy,num,coords,orientation[,remark]
        List<Proto>         toDeploy    = fleet.getToBeDeployedProtos();
        if ( toDeploy.size() != 0  )
            result.addMessage( NOT_ALL_DEPLOYED, false );
        else 
            configComplete = true;
    }    
    private void undeploy( String command, Result result )
    {
        // undeploy,num
        String[]            args        = command.split( ",", 5 );
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
        writeOut( lines );
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
        int         rows        = provisioner.getRows();
        int         cols        = provisioner.getCols();
        String      gridStr     = String.format( fmt, rows, cols );
        writeOut( List.of( gridStr ) );
    }

    private void showShipTable()
    {
        final String    fmt         = "%-25s %s";
        final String    headingFmt  = "%-25s %s";
        final String    regHeading  = "To Be Deployed";
        final String    depHeading  = "Deployed";
        final String    underScore  = "===============";
        final String    heading1    = 
            String.format( headingFmt, regHeading, depHeading );
        final String    heading2    = 
            String.format( headingFmt, underScore, underScore );
        
        List<Proto>         toDeploy    = fleet.getToBeDeployedProtos();
        int                 toDepCount  = toDeploy.size();
        List<Proto>         deployed    = fleet.getAllDeployedProtos();
        int                 depCount    = deployed.size();
        int                 numLines    = Math.max( toDepCount, depCount );
        List<String>        lines       = new ArrayList<String>();
        lines.add( heading1 );
        lines.add( heading2 );
//        System.out.println( heading1 );
//        System.out.println( heading2 );
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
        lines.add( "" );
        writeOut( lines );
    }
    
    private static void writeOut( String line )
    {
        writeOut( List.of( line ) );
    }
    
    private static void writeOut( List<String> lines )
    {
        writeLines( System.out, lines );
    }
    
    private static void writeErr( List<String> lines )
    {
        writeLines( System.err, lines );
    }
    
    private static synchronized void writeLines( 
        PrintStream str, 
        List<String> lines 
    )
    {
        lines.forEach( str::println );
        str.flush();
    }
    
    private static synchronized void printOut( String line )
    {
        System.out.print( line );
        System.out.flush();
    }
    
    private String getToDeployStr( int inx, Proto proto )
    {
        final String fmt    = "%3d. %s, %dX%d";
        ShipType2D  type        = proto.getType();
        String      name        = type.typeName();
        int         length      = type.length();
        int         breadth     = type.breadth();
        String      configStr   = String.format( fmt, inx, name, length, breadth );
        return configStr;
    }
    
    private String getDeployedStr( int inx, Proto proto )
    {
        final String fmt    = "%3d. %s, %dX%d, %s %s";
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
}
