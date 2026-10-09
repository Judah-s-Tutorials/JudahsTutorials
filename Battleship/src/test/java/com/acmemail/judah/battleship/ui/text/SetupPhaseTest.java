package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.ARE_YOU_SURE;
import static com.acmemail.judah.battleship.StatusMessages.FAILURE;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ARG_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.SETUP_TEXT_PROMPT;
import static com.acmemail.judah.battleship.StatusMessages.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        fail("Not yet implemented");
    }

    @Test
    public void testSetupPhaseTextProvisioner()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testSetupPhaseTextProvisionerReaderPrintStream()
    {
        int                 expRows     = 13;
        int                 expCols     = 17;
        String              dimCommand  = "dim," + expRows + "," + expCols;
        List<String>        commands    =
            List.of( dimCommand, "done", "y" );
        RoundTripResult     result      = 
            execRoundTrip( null, commands, false );
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

    @Test
    public void testExec()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testIsQuit()
    {
        fail("Not yet implemented");
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
        RoundTripResult     result      = 
            execRoundTrip( null, commands, false );
        TextProvisioner     provisioner = result.provisioner();
        List<String>        feedback    = result.feedback();
        RoundTripParser     parser      = new RoundTripParser( feedback );
        assertEquals( 3, parser.buckets.size() );
        assertFalse( provisioner.getToRegister().isEmpty() );
        assertFalse( result.setup().isQuit() );
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
        RoundTripResult     result      = 
            execRoundTrip( null, commands, false );
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
        RoundTripResult     result      = 
            execRoundTrip( null, commands, false );
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
        RoundTripResult     result      = 
            execRoundTrip( null, commands, false );
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
        List<String>    commands    = List.of( "update", "done", "y" );
        RoundTripResult     result      = 
            execRoundTrip( null, commands, false );
        List<String>        feedback    = result.feedback();
        UpdateParser        parser      = new UpdateParser( feedback );
        assertTrue( parser.getToRegister().isEmpty() );
        assertTrue( parser.getToDeploy().isEmpty() );
    }
    
    @Test
    public void testUpdateRightEmpty()
    {
        List<String>    commands    = 
            List.of( "type,default", "update", "done", "y" );
        RoundTripResult     result      = 
            execRoundTrip( null, commands, false );
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
        RoundTripResult     result      = 
            execRoundTrip( null, commands, false );
        List<String>        feedback    = result.feedback();
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
        RoundTripResult result      = 
            execRoundTrip( null, commands, false );
        List<String>    feedback    = result.feedback();
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
        List<String> toSetup, 
        boolean addQuit
    )
    {
        String                  script      = 
            String.join( "\n", toSetup ) + "\n";
        if ( addQuit )
            script += "q\ny\n";
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
        @SuppressWarnings("unused")
        public final    List<String>          prolog;
        public final    List<CommandBucket>   buckets = new ArrayList<>();
        
        public RoundTripParser( List<String> feedback )
        {
            int     feedbackSize    = feedback.size();
            int     promptLen       = SETUP_TEXT_PROMPT.length();
            int     firstPrompt     =
                IntStream.range( 0, feedbackSize )
                    .filter( i -> 
                        feedback.get( i ).startsWith( SETUP_TEXT_PROMPT )
                    )
                    .findFirst().orElse( feedbackSize );
            
            prolog = feedback.subList( 0, firstPrompt );
            List<String>    commands        = 
                feedback.subList( firstPrompt, feedbackSize );
            int             commandsSize    = commands.size();
            int[]   prompts         = 
                IntStream.range( 0, commandsSize )
                    .filter( i -> 
                        commands.get( i ).startsWith( SETUP_TEXT_PROMPT )
                    )
                    .toArray();

            for ( int inx = 0 ; inx < prompts.length ; ++inx )
            {
                int             promptInx   = prompts[inx];
                int             end         = inx < prompts.length - 1 ? 
                    prompts[inx + 1] : commandsSize;
                String          firstLine   = feedback.get( promptInx );
                String          prompt      = 
                    firstLine.substring( 0, promptLen );
                String          status      = firstLine.substring( promptLen );
                List<String>    suffix      = 
                    feedback.subList( promptInx + 1, end );
                buckets.add( new CommandBucket( prompt, status, suffix ) );
            }
        }
    }
    
    private class UpdateParser
    {
        private final List<String>  toRegister;
        private final List<String>  toDeploy;
        
        public UpdateParser( List<String> feedback )
        {
            int     feedbackSize    = feedback.size();
            int     firstLineInx    = 
                IntStream.range( 0, feedbackSize )
                    .filter( i -> feedback.get( i ).contains( "To Register" ) )
                    .findFirst().orElse( -1 );
            if ( firstLineInx < 0 )
            {
                toRegister = Collections.emptyList();
                toDeploy = Collections.emptyList();
            }
            else
            {
                // sanity check...
                // First two lines look something like this:
                //     To Register       To Deploy
                //     =============     ===========
                assertTrue( firstLineInx < feedbackSize + 1 );
                String  headerLine1 = feedback.get( firstLineInx );
                String  headerLine2 = feedback.get( firstLineInx + 1 );
                assertTrue( headerLine1.contains( "To Deploy" ) );
                assertTrue( headerLine2.contains( "====" ) );
                int     midPoint    = headerLine1.indexOf( "To Deploy" );
                int     startInx    = firstLineInx + 2;
                int     endInx      = 
                    IntStream.range( startInx, feedbackSize )
                        .filter( i -> feedback.get( i ).isBlank() )
                        .findFirst().orElse( feedbackSize );
                toRegister = IntStream.range( startInx, endInx )
                    .mapToObj( feedback::get )
                    .map( s -> 
                        s.substring( 0, Math.min( s.length(), midPoint ) )
                            .trim()
                    )
                    .filter( s -> !s.isEmpty() )
                    .toList();
                toDeploy = IntStream.range( startInx, endInx )
                    .mapToObj( feedback::get )
                    .filter( s -> s.length() >= midPoint )
                    .map( s -> s.substring( midPoint ).trim() )
                    .filter( s -> !s.isEmpty() )
                    .toList();
            }
        }
        
        public List<String> toReportString()
        {
            List<String>    list    = new ArrayList<>();
            list.add( "To Register" );
            list.addAll( toRegister );
            list.add( "To Deploy" );
            list.addAll( toDeploy );
            return list;
        }
        
        public List<String> getToRegister()
        {
            return toRegister;
        }
        
        public List<String> getToDeploy()
        {
            return toDeploy;
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
