package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.ARE_YOU_SURE;
import static com.acmemail.judah.battleship.StatusMessages.SETUP_TEXT_PROMPT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
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
        assertTrue( contains( feedback, "success" ) );
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
        feedback.forEach( System.out::println );

        assertEquals( expRows, provisioner.getRows() );
        assertEquals( expCols, provisioner.getCols() );
        
        RoundTripParser parser  = new RoundTripParser( feedback );
        System.out.println();
//        assertFalse( result.setup().isQuit() );
//        assertTrue( contains( feedback, SETUP_TEXT_PROMPT ) );
//        assertTrue( contains( feedback, ARE_YOU_SURE ) );
//        assertTrue( contains( feedback, "done" ) );
//        assertTrue( contains( feedback, "success" ) );
//        assertTrue( contains( feedback, "Setup complete" ) );
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

    private static RoundTripResult execRoundTrip( List<String> toClient )
    {
        return execRoundTrip( null, toClient, true );
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
        public final    List<String>          prolog;
        public final    List<CommandBucket>   buckets = new ArrayList<>();
        
        public RoundTripParser( List<String> feedback )
        {
            int     feedbackSize    = feedback.size();
            int     promptLen       = SETUP_TEXT_PROMPT.length();
            int[]   prompts         = 
                IntStream.range( 0, feedbackSize )
                    .filter( i -> 
                        feedback.get( i ).startsWith( SETUP_TEXT_PROMPT )
                    )
                    .toArray();
            prolog = prompts.length == 0 ? 
                feedback : feedback.subList( 0, prompts[0] );
            for ( int inx = 0 ; inx < prompts.length ; ++inx )
            {
                int             promptInx   = prompts[inx];
                int             end         = inx < prompts.length - 1 ? 
                    prompts[inx + 1] : feedbackSize;
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
