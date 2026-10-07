package com.acmemail.judah.battleship.ui.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.TextProvisioner;

class SetupPhaseClaudeTest
{
    @Test
    void testSetupPhaseTextProvisionerReaderPrintStream()
    {
        // Commands fed to the injected Reader, in order:
        // 1) "dim,10,10" -- set the grid dimensions
        // 2) "done"      -- mark setup complete
        // 3) "y"         -- confirm the "are you sure?" prompt for "done"
        String              script      = "dim,10,10\ndone\ny\n";
        StringReader        reader      = new StringReader( script );
        ByteArrayOutputStream   captured    = new ByteArrayOutputStream();
        PrintStream         outStream   = new PrintStream( captured );
        TextProvisioner     provisioner = TextProvisioner.of();

        SetupPhase  setupPhase  =
            new SetupPhase( provisioner, reader, outStream );
        setupPhase.exec();

        // The injected provisioner should reflect the "dim" command;
        // the injected Reader (not System.in) must have supplied it.
        assertEquals( 10, provisioner.getRows() );
        assertEquals( 10, provisioner.getCols() );
        assertFalse( setupPhase.isQuit() );

        // The injected PrintStream (not System.out) must have
        // received all of the application's output.
        String  output  = captured.toString();
        assertTrue( output.contains( "Enter a command" ) );
        assertTrue( output.contains( "dim,10,10: " ) );
        assertTrue( output.contains( "done: " ) );
        assertTrue( output.contains( "success" ) );
        assertTrue( output.contains( "Setup complete" ) );
    }
}
