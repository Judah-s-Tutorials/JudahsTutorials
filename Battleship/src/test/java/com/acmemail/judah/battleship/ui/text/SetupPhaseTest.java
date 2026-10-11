package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.ARE_YOU_SURE;
import static com.acmemail.judah.battleship.StatusMessages.FAILURE;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ARG_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.PLAYERS;
import static com.acmemail.judah.battleship.StatusMessages.SETUP_TEXT_PROMPT;
import static com.acmemail.judah.battleship.StatusMessages.SUCCESS;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.battleship.TextProvisioner;
import com.acmemail.judah.battleship.model.default_ship_types.Battleship;
import com.acmemail.judah.battleship.model.default_ship_types.Carrier;
import com.acmemail.judah.battleship.model.default_ship_types.Cruiser;
import com.acmemail.judah.battleship.model.default_ship_types.Destroyer;
import com.acmemail.judah.battleship.model.default_ship_types.Submarine;

class SetupPhaseTest
{
    private static final Set<String>    allDefaultTypeNames =
        Set.of( 
            Battleship.getType().typeName(), 
            Carrier.getType().typeName(), 
            Cruiser.getType().typeName(), 
            Destroyer.getType().typeName(), 
            Submarine.getType().typeName()
        );

    @BeforeEach
    public void beforeEach() throws IOException
    {
    }
    
    @Test
    public void testSetupPhase()
    {
        SetupPhase  setupPhase  = new SetupPhase();
        assertNotNull( setupPhase.getProvisioner() );
        assertFalse( setupPhase.isQuit() );
    }

    @Test
    public void testSetupPhaseTextProvisioner()
    {
        TextProvisioner provisioner = TextProvisioner.of();
        int             expRows     = 13;
        int             expCols     = 17;
        String          dimCommand  = "dim," + expRows + "," + expCols;
        List<String>    commands    =
            List.of( dimCommand, "done", "y" );
        RoundTripResult result      = execRoundTrip( provisioner, commands );
        assertTrue( contains( result.feedback, SUCCESS ) );
        
        assertEquals( expRows, provisioner.getRows() );
        assertEquals( expCols, provisioner.getCols() );

    }

    @Test
    public void testSetupPhaseTextProvisionerReaderPrintStream()
    {
        int                 expRows     = 13;
        int                 expCols     = 17;
        String              dimCommand  = "dim," + expRows + "," + expCols;
        List<String>        commands    =
            List.of( dimCommand, "done", "y" );
        RoundTripResult     result      = execRoundTrip( null, commands );
        TextProvisioner     provisioner = result.provisioner();
        List<String>        feedback    = result.feedback();
        
        assertEquals( expRows, provisioner.getRows() );
        assertEquals( expCols, provisioner.getCols() );
        assertFalse( result.setup().isQuit() );
        assertTrue( contains( feedback, SETUP_TEXT_PROMPT ) );
        assertTrue( contains( feedback, ARE_YOU_SURE ) );
        assertTrue( contains( feedback, "done" ) );
        assertTrue( contains( feedback, SUCCESS ) );
        assertTrue( contains( feedback, "Setup complete" ) );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"?","h","help" })
    public void testHelp( String command )
    {
        List<String>        commands        = List.of( command, "d!" );
        RoundTripResult     result          = execRoundTrip( null, commands );
        List<String>        rawFeedback     = result.feedback();
        RoundTripParser     rtParser        = new RoundTripParser( rawFeedback );
        int                 helpSize        = rtParser.prolog.size();
        int                 feedbackSize    = rtParser.feedback.size();
        
        // Prolog contains the help message; feedback should contain help
        // message, plus a few lines for command feedback.
        assertTrue( feedbackSize > helpSize );
    }

    @Test
    public void testIsQuit()
    {
        RoundTripResult     result      = 
            execRoundTrip( null, List.of( "q!" ) );
        assertTrue( result.setup().isQuit() );
     
        result      = 
            execRoundTrip( null, List.of( "d!" ) );
        assertFalse( result.setup().isQuit() );
    }
    
    @Test
    public void testDone()
    {
        String              done        = "done";
        String              defTypes    = "type,default";
        List<String>        commands    =
            List.of( 
                done,
                "n",
                defTypes,
                done,
                "y"
            );
        RoundTripResult     result      = execRoundTrip( null, commands );
        TextProvisioner     provisioner = result.provisioner();
        List<String>        feedback    = result.feedback();
        RoundTripParser     parser      = new RoundTripParser( feedback );
        assertEquals( 3, parser.buckets.size() );
        assertFalse( provisioner.getToRegister().isEmpty() );
        assertFalse( result.setup().isQuit() );
    }
    
    @Test
    public void testGetProvisioner()
    {
        TextProvisioner provisioner = TextProvisioner.of();
        SetupPhase      setupPhase  = new SetupPhase( provisioner );
        assertNotNull( setupPhase.getProvisioner() );
        assertSame( provisioner, setupPhase.getProvisioner() );
    }

    @Test
    public void testQuit()
    {
        String              quit        = "quit";
        String              defTypes    = "type,default";
        List<String>        commands    =
            List.of( 
                quit,
                "n",
                defTypes,
                quit,
                "y"
            );
        RoundTripResult     result      = execRoundTrip( null, commands );
        TextProvisioner     provisioner = result.provisioner();
        List<String>        feedback    = result.feedback();
        RoundTripParser     parser      = new RoundTripParser( feedback );
        assertEquals( 3, parser.buckets.size() );
        assertFalse( provisioner.getToRegister().isEmpty() );
        assertTrue( result.setup().isQuit() );
    }
    
    @Test
    public void testDimGoRight()
    {
        int                 expRows     = 13;
        int                 expCols     = 17;
        String              command     = "dim," + expRows + "," + expCols;
        List<String>        commands    =
            List.of( 
                command,
                "done",
                "y"
            );
        RoundTripResult     result      = execRoundTrip( null, commands );
        TextProvisioner     provisioner = result.provisioner();
        List<String>        feedback    = result.feedback();

        assertEquals( expRows, provisioner.getRows() );
        assertEquals( expCols, provisioner.getCols() );
        assertFalse( result.setup().isQuit() );
        assertTrue( contains( feedback, SUCCESS ) );
    }
    
    @Test
    public void testDimGoWrong()
    {
        int                 expRows     = 13;
        int                 expCols     = 17;
        String              goodCommand = "dim," + expRows + "," + expCols;
        String              badCommand  = "dim,20,30,40";
        List<String>        commands    =
            List.of( 
                badCommand, 
                goodCommand,
                "done",
                "y"
            );
        RoundTripResult     result      = execRoundTrip( null, commands );
        TextProvisioner     provisioner = result.provisioner();
        List<String>        feedback    = result.feedback();

        assertEquals( expRows, provisioner.getRows() );
        assertEquals( expCols, provisioner.getCols() );
        assertFalse( result.setup().isQuit() );
        assertTrue( contains( feedback, INVALID_ARG_COUNT ) );
        assertTrue( contains( feedback, FAILURE ) );
        assertTrue( contains( feedback, SUCCESS ) );
    }
    
    @Test
    public void testUpdateBothEmpty()
    {
        List<String>        commands    = List.of( "update", "done", "y" );
        RoundTripResult     result      = execRoundTrip( null, commands );
        List<String>        rawFeedback = result.feedback();
        RoundTripParser     rtParser    = new RoundTripParser( rawFeedback );
        List<String>        feedback    = rtParser.feedback;
        UpdateParser        parser      = new UpdateParser( feedback );
        assertTrue( parser.getToRegister().isEmpty() );
        assertTrue( parser.getToDeploy().isEmpty() );
    }
    
    @Test
    public void testUpdateRightEmpty()
    {
        List<String>    commands    = 
            List.of( "type,default", "update", "done", "y" );
        RoundTripResult     result      = execRoundTrip( null, commands );
        List<String>        feedback    = result.feedback();
        UpdateParser        parser      = new UpdateParser( feedback );
        List<String>        toRegister  = parser.getToRegister();
        List<String>        toDeploy    = parser.getToDeploy();
        assertTrue( toDeploy.isEmpty() );
        assertEquals( allDefaultTypeNames.size(), toRegister.size() );
        assertTrue( containsAll( allDefaultTypeNames, toRegister ) );
    }
    
    @Test
    public void testUpdateLeftLonger()
    {
        List<String>    ships       = List.of( "Battleship", "Carrier" );
        List<String>    commands    = new ArrayList<>();
        commands.add( "type,default" );
        ships.forEach( s -> commands.add( "deploy," + s ) );
        commands.addAll( List.of( "update", "done", "y" ) );
        RoundTripResult     result      = execRoundTrip( null, commands );
        List<String>        rawFeedback = result.feedback();
        RoundTripParser     rtParser    = new RoundTripParser( rawFeedback );
        List<String>        feedback    = rtParser.feedback;
        UpdateParser        parser      = new UpdateParser( feedback );
        List<String>        toRegister  = parser.getToRegister();
        List<String>        toDeploy    = parser.getToDeploy();
        assertEquals( allDefaultTypeNames.size(), toRegister.size() );
        assertEquals( ships.size(), toDeploy.size() );
        assertTrue( containsAll( allDefaultTypeNames, toRegister ) );
        assertTrue( containsAll( ships, toDeploy ) );
    }
    
    @Test
    public void testUpdateRightLonger()
    {
        List<String>    addShips    = List.of( "Battleship", "Carrier" );
        List<String>    commands    = new ArrayList<>();
        commands.add( "type,default" );
        allDefaultTypeNames.forEach( s -> commands.add( "deploy," + s ) );
        addShips.forEach( s -> commands.add( "deploy," + s ) );
        commands.addAll( List.of( "update", "done", "y" ) );
        RoundTripResult result      = execRoundTrip( null, commands );
        List<String>        rawFeedback = result.feedback();
        RoundTripParser     rtParser    = new RoundTripParser( rawFeedback );
        List<String>        feedback    = rtParser.feedback;

        UpdateParser    parser      = new UpdateParser( feedback );
        List<String>    toRegister  = parser.getToRegister();
        List<String>    toDeploy    = parser.getToDeploy();
        
        int             expRegSize  = allDefaultTypeNames.size();
        int             expDepSize  = expRegSize + addShips.size();
        assertEquals( expRegSize, toRegister.size() );
        assertEquals( expDepSize, toDeploy.size() );
        assertTrue( containsAll( allDefaultTypeNames, toRegister ) );
        assertTrue( containsAll( allDefaultTypeNames, toDeploy ) );
    }
    
    @ParameterizedTest
    @ValueSource( strings = {"", "player1","player1,player2,player3"} )
    public void testAddPlayer( String list )
    {
        List<String>    suffix      = List.of( "update", "done!" );
        List<String>    commands    = new ArrayList<>();
        List<String>    expList     = new ArrayList<>();
        String[]        allPlayers  = list.split( "," );
        for ( String player : allPlayers )
        {
            if ( !player.isBlank() )
            {
                commands.add( "add," + player );
                expList.add( player );
            }
        }
        commands.addAll( suffix );
        RoundTripResult     result      = execRoundTrip( null, commands );
        RoundTripParser     rtParser    = 
            new RoundTripParser( result.feedback() );
        UpdateParser        parser      = new UpdateParser( rtParser.feedback );
        TextProvisioner     provisioner = result.provisioner();
        
        assertEquals( expList, parser.getPlayers() );
        assertEquals( expList, provisioner.getPlayers() );
    }
    
    @Test
    public void addTestPlayerInvalidName()
    {
        List<String>        commands    = List.of( "add," + "", "done!" );
        RoundTripResult     result      = execRoundTrip( null, commands );
        TextProvisioner     provisioner = result.provisioner();
        List<String>        feedback    = result.feedback();
        SetupPhase          setup       = result.setup();
        assertTrue( contains( feedback, FAILURE ) );
        assertTrue( contains( feedback, INVALID_NAME ) );
        assertTrue( provisioner.getPlayers().isEmpty() );
        assertFalse( setup.isQuit() );
    }
    
    private static boolean containsAll( 
        Collection<String> exp, 
        Collection<String> act
    )
    {
        boolean containsAll =
            exp.stream()
                .filter( s -> !contains( act, s ) )
                .peek( System.out::println )
                .findAny().isEmpty();
        return containsAll;
    }
    
    private static boolean contains( Collection<String> source, String target )
    {
        boolean contains    =
            source.stream()
                .filter( s -> s.contains( target ) )
                .findAny()
                .isPresent();
        return contains;
    }
    
    private static RoundTripResult execRoundTrip( 
        TextProvisioner provisioner,
        List<String> toSetup
    )
    {
        String                  script      = 
            String.join( "\n", toSetup ) + "\n";
        StringReader            reader      = new StringReader( script );
        ByteArrayOutputStream   fromSetup   = new ByteArrayOutputStream();
        PrintStream             outStream   = new PrintStream( fromSetup );
        TextProvisioner         worker      = provisioner != null ? 
            provisioner : TextProvisioner.of();
        SetupPhase              setup       =
            new SetupPhase( worker, reader, outStream );
        try
        {
            setup.exec();
        }
        catch ( Exception exc )
        {
            fail( "unexpected exception", exc );
        }
        String                  clientData  = fromSetup.toString();
        List<String>            feedback    = clientData.lines().toList();
        RoundTripResult         result      =
            new RoundTripResult( worker, setup, toSetup, feedback );
        return result;
    }
    
    private class RoundTripParser
    {
        public final List<String>           prolog;
        public final List<String>           feedback; 
        public final List<CommandBucket>    buckets = new ArrayList<>();
        
        public RoundTripParser( List<String> rawFeedback )
        {
            int     rawFeedbackSize = rawFeedback.size();
            int     promptLen       = SETUP_TEXT_PROMPT.length();
            int     firstPrompt     =
                IntStream.range( 0, rawFeedbackSize )
                    .filter( i -> 
                        rawFeedback.get( i ).startsWith( SETUP_TEXT_PROMPT )
                    )
                    .findFirst().orElse( rawFeedbackSize );
            
            prolog = rawFeedback.subList( 0, firstPrompt );
            feedback = rawFeedback.subList( firstPrompt, rawFeedbackSize );
            int     feedbackSize    = feedback.size();
            int[]   prompts         = 
                IntStream.range( 0, feedbackSize )
                    .filter( i -> 
                        feedback.get( i ).startsWith( SETUP_TEXT_PROMPT )
                    )
                    .toArray();

            for ( int inx = 0 ; inx < prompts.length ; ++inx )
            {
                int             promptInx   = prompts[inx];
                int             end         = inx < prompts.length - 1 ? 
                    prompts[inx + 1] : feedbackSize;
                String          firstLine   = feedback.get( promptInx );
                String          prompt      = 
                    firstLine.substring( 0, promptLen );
                String          status      = firstLine.substring( promptLen );
                List<String>    suffix      = feedback.subList( promptInx + 1, end );
                buckets.add( new CommandBucket( prompt, status, suffix ) );
            }
        }
    }
    
    private class UpdateParser
    {
        private final List<String>  toRegister;
        private final List<String>  toDeploy;
        private final List<String>  players;
        
        private record ShipLists( List<String> reg, List<String> dep ) {}
        
        public UpdateParser( List<String> feedback )
        {
            ShipLists   lists   = getShipLists( feedback );
            toRegister = lists.reg();
            toDeploy = lists.dep();
            players = getPlayers( feedback );
        }
        
        public List<String> getToRegister()
        {
            return toRegister;
        }
        
        public List<String> getToDeploy()
        {
            return toDeploy;
        }
        
        public List<String> getPlayers()
        {
            return players;
        }
        
        private static List<String> getPlayers( List<String> feedback )
        {
            int     start           = 
                getSectionStart( 
                    0,
                    i -> feedback.get( i ).contains( PLAYERS ),
                    feedback
                );
            assertTrue( start >= 0 );
            
            int             end     =
                getSectionEnd(
                    start + 2,
                    i -> feedback.get( i ).isBlank(),
                    feedback
                );
            assertTrue( end > start );
            
            // sanity check; this section looks something like:
            //    Players
            //    =======
            //    John
            //    Jane
            assertTrue( feedback.get( start + 1 ).contains( "=" ) );
            
            List<String>    players = feedback.subList( start + 2, end );
            return players;
        }
        
        private static int getSectionStart( 
            int          from,
            IntPredicate isStart, 
            List<String> feedback
        )
        {
            int size    = feedback.size();
            int start   = 
                IntStream.range( from, size )
                    .filter( isStart::test )
                    .findFirst().orElse( -1 );
            return start;
        }
        
        private static int getSectionEnd( 
            int          from,
            IntPredicate isEnd, 
            List<String> feedback
        )
        {
            int size    = feedback.size();
            int end     = 
                IntStream.range( from, size )
                    .filter( isEnd::test )
                    .findFirst().orElse( size );
            return end;
        }
        
        private static ShipLists getShipLists( List<String> feedback )
        {
            int     feedbackSize    = feedback.size();
            int     tableStart      = 
                getSectionStart( 
                    0,
                    i -> feedback.get( i ).contains( "To Register" ),
                    feedback
                );
            // hard-fail if start of ships table isn't found
            assertTrue( tableStart >= 0 );
            int     tableEnd        = 
                getSectionEnd(
                    tableStart + 2,
                    i -> feedback.get( i ).isBlank(),
                    feedback
                );

            // sanity check...
            // First two lines look something like this:
            //     To Register       To Deploy
            //     =============     ===========
            assertTrue( tableStart < feedbackSize + 1 );
            String  headerLine1 = feedback.get( tableStart );
            String  headerLine2 = feedback.get( tableStart + 1 );
            assertTrue( headerLine1.contains( "To Deploy" ) );
            assertTrue( headerLine2.contains( "====" ) );
            int     midPoint    = headerLine1.indexOf( "To Deploy" );
            assertTrue( midPoint > 0 );

            int     startInx    = tableStart + 2;
            List<String> regList = IntStream.range( startInx, tableEnd )
                .mapToObj( feedback::get )
                .map( s -> 
                    s.substring( 0, Math.min( s.length(), midPoint ) )
                        .trim()
                )
                .filter( s -> !s.isEmpty() )
                .toList();
            List<String> depList = IntStream.range( startInx, tableEnd )
                .mapToObj( feedback::get )
                .filter( s -> s.length() >= midPoint )
                .map( s -> s.substring( midPoint ).trim() )
                .filter( s -> !s.isEmpty() )
                .toList();
            ShipLists   lists   = new ShipLists( regList, depList );
            return lists;
        }
    }    
    private record RoundTripResult( 
        TextProvisioner provisioner,
        SetupPhase      setup,
        List<String>    input,
        List<String>    feedback
    )
    {
    }
    
    private record CommandBucket( 
        String prompt,
        String status,
        List<String> suffix
    )
    {
    }
}
