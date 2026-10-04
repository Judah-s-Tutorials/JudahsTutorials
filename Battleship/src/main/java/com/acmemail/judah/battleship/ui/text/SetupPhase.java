package com.acmemail.judah.battleship.ui.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.TextProvisioner;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.util.LineWrapper;

public class SetupPhase
{
    private static final int        lineLen     = 60;
    private static final String     lineSep     = System.lineSeparator();
    private static final String[]   graphs      =   { 
        "This is the setup phase of the game. "
        + "In phase you can set the dimensions of the game grid, "
        + "specify the names of the players "
        + "design a type of ship, "
        + "and establish the types of the ships "
        + "that must be deloyed by each player. "
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
        "\t-- quit: discards all work, terminated the application",
        "\t-- dim,rows,cols: "
        + "where 'rows' is a decmimal integer "
        + "specifying the number of rows in the game grid, "
        + "and 'cols' is the number of columns.",
        
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
        
        "\t-- add: "
        + "This command adds the name of an opponent "
        + "to the game."
        + "Names are case-sensitive, "
        + "and may not contain spaces, e.g.:",
        
        "\t\tplayer,Albert",
        "\t\tplayer,Marie-Curie",
        "\t\tSir-Isaac-Newton",
        
        "\t-- ?, h, help: show this help message",
        "\t-- u, update: display the updated configuration",
        "\t-- done: mark setup complete",
        "\t-- q, quit: exit from this application",
        "==================================="
    };
    private static final List<String>   list    = List.of( graphs );
    
    private final   TextProvisioner provisioner;
    private boolean setupComplete;
    private boolean quit;
    private BufferedReader  consoleReader;
    
    public static void main( String[] args )
    {
        SetupPhase  setup   = new SetupPhase();
        setup.exec();
        if ( setup.isQuit() )
            System.out.println( "quitting" );
    }
    
    public SetupPhase()
    {
        this( null );
    }
    
    public SetupPhase( TextProvisioner provisioner )
    {
        this.provisioner = provisioner != null ?
            provisioner : TextProvisioner.of();
    }
    
    public void exec()
    {
        showHelp();
        execLoop();
    }
    
    public boolean isQuit()
    {
        return quit;
    }
    
    private void execLoop()
    {
        final String    prompt  = "Enter a command (? for help): ";
        setupComplete = false;
        quit = false;
        try ( 
            InputStreamReader inStream  = new InputStreamReader( System.in );
            BufferedReader    bufStream = new BufferedReader( inStream ); 
        )
        {
            consoleReader = bufStream;
            while ( !setupComplete )
            {
                System.out.print( prompt );
                String  command = bufStream.readLine();
                execCommand( command );
                if ( quit )
                {
                    if ( areYouSure( "Quit" ) )
                        setupComplete = true;
                    else
                        quit = false;
                }
                else if ( setupComplete )
                {
                    if ( areYouSure( "Setup complete" ) )
                        setupComplete = true;
                }
            }
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            throw new BattleshipException( exc );
        }
    }
    
    private boolean areYouSure( String prompt ) throws IOException
    {
        final String    areYouSure  = "are you sure? (y/n) ";
        String  fullPrompt  = prompt + "; " + areYouSure;
        System.out.print( fullPrompt );
        String  reply       = consoleReader.readLine().trim().toUpperCase();
        boolean result      = reply.charAt( 0 ) == 'Y';
        return result;
    }
    
    private void execCommand( String command )
    {
        String  workCommand = command.trim().toUpperCase();
        provisioner.resetSuccess();
        switch ( workCommand )
        {
        case "?", "H", "HELP"  -> showHelp();
        case "U", "UPDATE" -> showUpdate();
        case "DONE" -> setupComplete = true;
        case "Q, QUIT" -> quit = true;
        default -> provisioner.addRec( command );
        }
        System.out.print( command + ": " );
        String          status  = provisioner.isSuccess() ? 
            "success" : "failure";
        List<String>    errors  = provisioner.getErrors();
        System.out.println( status );
        errors.forEach( s -> System.out.println( "    " + s ) );
    }
    
    private void showHelp()
    {
        LineWrapper     wrapper     = new LineWrapper( lineLen, list );
        List<String>    wrappedList = wrapper.getWrappedList();
        System.out.println( String.join( lineSep, wrappedList ) );
    }
    
    private void showUpdate()
    {
        System.out.printf( "%nCURRENT CONFIGURATION%n" );
        showPlayers();
        showDimensions();
        showShipTable();
        System.out.println();
    }
    
    private void showPlayers()
    {
        List<String>    players = provisioner.getPlayers();
        System.out.println( "Players");
        System.out.println( "=======");
        players.forEach( System.out::println );
        System.out.println();
    }
    
    private void showDimensions()
    {
        final String    fmt     = "Grid: %d rows, %d columns";
        int         rows        = provisioner.getRows();
        int         cols        = provisioner.getCols();
        String      gridStr     = String.format( fmt, rows, cols );
        System.out.println( gridStr );
    }

    private void showShipTable()
    {
        final String    fmt         = "%-20s %-20s";
        final String    regHeading  = "To Register";
        final String    depHeading  = "To Deploy";
        final String    underScore  = "===============";
        final String    heading1    = 
            String.format( fmt, regHeading, depHeading );
        final String    heading2    = 
            String.format( fmt, underScore, underScore );
        
        List<ShipType2D>    toRegister  = provisioner.getToRegister();
        int                 regCount    = toRegister.size();
        List<ShipType2D>    toDeploy    = provisioner.getToDeploy();
        int                 depCount    = toDeploy.size();
        int                 numLines    = Math.max( regCount, depCount );
        System.out.println( heading1 );
        System.out.println( heading2 );
        for ( int inx = 0 ; inx < numLines ; ++inx )
        {
            String  regString   = inx < regCount ?
                getConfigStr( toRegister.get( inx ) ) : ""; 
            String  depString   = inx < depCount ?
                getConfigStr( toDeploy.get( inx ) ) : ""; 
            String  output      = String.format( fmt, regString, depString );
            System.out.println( output );
        }
        System.out.println();
    }
    
    private static String getConfigStr( ShipType2D type )
    {
        final String  fmt       = "%s, %dX%d";
        String  name        = type.typeName();
        int     length      = type.length();
        int     breadth     = type.breadth();
        String  configStr   = String.format( fmt, name, length, breadth );
        return configStr;
    }
}
