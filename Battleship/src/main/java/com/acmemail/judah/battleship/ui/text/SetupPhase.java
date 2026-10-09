package com.acmemail.judah.battleship.ui.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.TextProvisioner;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.util.LineWrapper;
import static com.acmemail.judah.battleship.StatusMessages.SETUP_TEXT_PROMPT;
import static com.acmemail.judah.battleship.StatusMessages.SUCCESS;
import static com.acmemail.judah.battleship.StatusMessages.ARE_YOU_SURE;
import static com.acmemail.judah.battleship.StatusMessages.FAILURE;

public class SetupPhase
{
    /** Maximum line length for printing help message. */
    private static final int        lineLen     = 60;
    /** Paragraphs in help message. */
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
        + "Names are case-sensitive, "
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
    /** List of paragraphs in help message; to be formatted for printing. */
    private static final List<String>   list    = List.of( graphs );
    
    /** The Provisioner populated during this phase. */
    private final   TextProvisioner provisioner;
    /** True, when the client says "no more provisioning to do." */
    private boolean setupComplete;
    /** True, if the client says "abandon; exit to desktop." */
    private boolean quit;
    /** 
     * When true, tells the UI to skip the confirmation step
     * ("are you sure?")
     * for commands like QUIT.
     */
    private boolean really;
    /** For reading operator input. */
    private final   BufferedReader  consoleReader;
    /** Destination for status and error output; defaults to stdout. */
    private final   PrintStream     outStream;

    /**
     * Default constructor.
     * Forces a TextProvisioner to be instantiated internally,
     * and reads/writes via stdin/stdout.
     */
    public SetupPhase()
    {
        this( null, null, null );
    }

    /**
     * Constructor.
     * Reads/writes via stdin/stdout.
     *
     * @param provisioner   TextProvisioner to be used internally; may be null
     */
    public SetupPhase( TextProvisioner provisioner )
    {
        this( provisioner, null, null );
    }

    /**
     * Constructor.
     * Allows the operator-input source and the output destination
     * to be supplied by the caller, for example by a test driver.
     * Any argument may be null,
     * in which case the corresponding default
     * ({@code provisioner}: a new TextProvisioner;
     * {@code reader}: stdin;
     * {@code outStream}: stdout)
     * is used instead.
     *
     * @param provisioner   TextProvisioner to be used internally; may be null
     * @param reader        source of operator input; may be null
     * @param outStream     destination for status and error output; may be null
     */
    public SetupPhase(
        TextProvisioner provisioner,
        Reader          reader,
        PrintStream     outStream
    )
    {
        this.provisioner = provisioner != null ?
            provisioner : TextProvisioner.of();
        Reader  actReader   = reader != null ?
            reader : new InputStreamReader( System.in );
        this.consoleReader  = actReader instanceof BufferedReader buffered ?
            buffered : new BufferedReader( actReader );
        this.outStream  = outStream != null ? outStream : System.out;
    }
    
    /**
     * Public entry point for starting operator-input-processing loop.
     */
    public void exec()
    {
        showHelp();
        execLoop();
    }
    
    /**
     * Returns true if the operator has issued a quit command.
     * 
     * @return  true if the operator has issued a quit command
     */
    public boolean isQuit()
    {
        return quit;
    }
    
    /**
     * Operator-input-processing loop.
     */
    private void execLoop()
    {
        final String    prompt  = SETUP_TEXT_PROMPT;
        setupComplete = false;
        quit = false;
        try
        {
            while ( !setupComplete )
            {
                printOut( prompt );
                String  command = consoleReader.readLine();
                execCommand( command );
                if ( quit )
                {
                    if ( really || areYouSure( "Quit" ) )
                        setupComplete = true;
                    else
                        quit = false;
                }
                else if ( setupComplete )
                {
                    if ( !really )
                        setupComplete = areYouSure( "Setup complete" );
                }
            }
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            throw new BattleshipException( exc );
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
     * 
     * @throws IOException  
     *      if an I/O error occurs when reading the operator's response
     */
    private boolean areYouSure( String prompt ) throws IOException
    {
        String  fullPrompt  = prompt + "; " + ARE_YOU_SURE;
        printOut( fullPrompt );
        String  reply       = consoleReader.readLine().trim().toUpperCase();
        boolean result      = reply.charAt( 0 ) == 'Y';
        String  status      = result ? "yes" : "no";
        writeOut( status );
        return result;
    }
    
    /**
     * Execute the command entered by the operator.
     * <p>
     * Postconditions:
     * <ol>
     * <li>
     *      After processing the command,
     *      the command and the status of processing it
     *      are written to stdout.
     * </li>
     * <li>
     *      Error messages resulting from command processing
     *      are written to stderr.
     * </li>
     * </ol>
     * @param command
     */
    private void execCommand( String command )
    {
        String  workCommand = command.trim().toUpperCase();
        provisioner.resetSuccess();
        switch ( workCommand )
        {
        case "?", "H", "HELP"  -> showHelp();
        case "U", "UPDATE" -> showUpdate();
        case "DONE" -> setupComplete = true;
        case "D!", "DONE!" -> really = setupComplete = true;
        case "Q", "QUIT" -> quit = true;
        case "Q!", "QUIT!" -> quit = really = true;
        default -> provisioner.addRec( command );
        }
        printOut( command + ": " );
        String          status  = provisioner.isSuccess() ?
            SUCCESS : FAILURE;
        List<String>    errors  =
            provisioner.getErrors().stream().map( "    "::concat ).toList();
        writeOut( status );
        writeOut( errors );
    }

    /**
     * Write the help message to stdout.
     */
    private void showHelp()
    {
        LineWrapper     wrapper     = new LineWrapper( lineLen, list );
        List<String>    wrappedList = wrapper.getWrappedList();
        writeOut( wrappedList );
    }

    /**
     * Write the updated configuration to stdout.
     */
    private void showUpdate()
    {
        List<String>    lines   = new ArrayList<>();
        lines.add( "%nCURRENT CONFIGURATION%n" );
        lines.addAll( showPlayers() );
        lines.addAll( showDimensions() );
        lines.addAll( showShipTable() );
        lines.add( "" );
        writeOut( lines );
    }
    
    /**
     * Assemble the lines of a report
     * that lists all players
     * currently registered for the game.
     * 
     * @return  
     *      a list containing the lines in the report
     *      of all players registered for the game
     */
    private List<String> showPlayers()
    {
        List<String>    output  = new ArrayList<>();
        List<String>    players = provisioner.getPlayers();
        output.add( "Players");
        output.add( "=======");
        output.addAll( players );
        output.add( "" );
        return output;
    }
    
    /**
     * Assemble the lines of a report
     * that shows the dimensions of the game grid.
     * 
     * @return  
     *      a list containing the lines in a report
     *      showing the grid dimensions
     */
    private List<String> showDimensions()
    {
        final String    fmt     = "Grid: %d rows, %d columns";
        List<String>    output  = new ArrayList<>();
        Integer         rows    = provisioner.getRows();
        Integer         cols    = provisioner.getCols();
        String          gridStr = String.format( fmt, rows, cols );
        output.add( gridStr );
        return output;
    }

    /**
     * Assemble the lines of a report
     * that shows all the ships
     * registered for this game
     * and those marked for deployment.
     * 
     * @return  
     *      a list containing the lines in a report
     *      showing the ships registered for this game
     */
    private List<String> showShipTable()
    {
        final String    fmt         = "%-20s %-20s";
        List<String>    output      = new ArrayList<>();
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
        output.add( heading1 );
        output.add( heading2 );
        for ( int inx = 0 ; inx < numLines ; ++inx )
        {
            String  regString   = inx < regCount ?
                getConfigStr( toRegister.get( inx ) ) : ""; 
            String  depString   = inx < depCount ?
                getConfigStr( toDeploy.get( inx ) ) : ""; 
            String  outStr      = String.format( fmt, regString, depString );
            output.add( outStr );
        }
        output.add( "" );
        return output;
    }
    
    /**
     * Format a string describing the name
     * and dimensions of a given ship type.
     * 
     * @param type  the given ship type
     * 
     * @return  
     *      a string describing the name and dimensions
     *      of a given ship type
     */
    private static String getConfigStr( ShipType2D type )
    {
        final String  fmt       = "%s, %dX%d";
        String  name        = type.typeName();
        int     length      = type.length();
        int     breadth     = type.breadth();
        String  configStr   = String.format( fmt, name, length, breadth );
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
