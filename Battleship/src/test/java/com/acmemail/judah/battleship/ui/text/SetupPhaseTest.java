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
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.TextProvisioner;

class SetupPhaseTest
{
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
        feedback.forEach( System.out::println );
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
        feedback.forEach( System.out::println );
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
    
    private static boolean contains( List<String> source, String target )
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
