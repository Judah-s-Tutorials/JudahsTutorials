package com.acmemail.judah;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ParsedCommandTest
{
    @Test
    void test()
    {
        Command command = Command.END;
        String  cmdStr  = "command";
        String  argStr  = "this is an argument";
        ParsedCommand   parsedCommand   = 
            new ParsedCommand( command, cmdStr, argStr );
        assertEquals( command, parsedCommand.getCommand() );
        assertEquals( cmdStr, parsedCommand.getCommandString() );
        assertEquals( argStr, parsedCommand.getArgString() );
    }
}
