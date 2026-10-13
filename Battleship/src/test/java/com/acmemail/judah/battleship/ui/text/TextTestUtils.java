package com.acmemail.judah.battleship.ui.text;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.List;

import com.acmemail.judah.battleship.Provisioner;

/**
 * Convenience utilities for use
 * when testing classes in the ...ui.text package.
 */
public class TextTestUtils
{
    /**
     * Default constructor; not used.
     */
    private TextTestUtils()
    {
        // not used
    }

    /**
     * This method sets up a TextPhase object
     * for processing using custom input and output streams,
     * executes the TextPhase
     * and returns a result to the client.
     * TextPhase execution is completed,
     * and all streams are properly disposed (if necessary).
     * 
     * @param provisioner   the Provisioner to encapsulate in execution
     * @param phaseGetter   TextPhase supplier
     * @param toProcessor   
     *      lines of Strings (commands) to feed to the TextPhase object
     *      
     * @return  the result of the operation
     * 
     * @see RoundTripResult
     * @see PhaseGetter
     */
    public static RoundTripResult execRoundTrip( 
        Provisioner provisioner,
        PhaseGetter phaseGetter,
        List<String> toProcessor
    )
    {
        String                  script          = 
            String.join( "\n", toProcessor ) + "\n";
        StringReader            reader          = new StringReader( script );
        ByteArrayOutputStream   fromProcessor   = new ByteArrayOutputStream();
        PrintStream             outStream       = new PrintStream( fromProcessor );
        TextPhase               phase           = 
            phaseGetter.apply( reader, outStream );
        try
        {
            phase.exec();
        }
        catch ( Exception exc )
        {
            fail( "unexpected exception", exc );
        }
        String                  clientData  = fromProcessor.toString();
        List<String>            feedback    = clientData.lines().toList();
        RoundTripResult         result      =
            new RoundTripResult( provisioner, phase, toProcessor, feedback );
        return result;
    }

}
