package com.acmemail.judah.cartesian_plane.input;

import java.util.Objects;

/**
 * This is a small class to store the result
 * of parsing a command string.
 * The expectation is that a string such as this
 * has been parsed into a command and an argument:
 * <br><code>&nbsp;&nbsp;&nbsp;&nbsp;yequals x^2 - 1</code>.
 * Exactly how the parsing is performed
 * is up to the user, 
 * however the above line of text 
 * will likely be interpreted command = 
 * Command.YEQUALS, and argument = "x^2 - 1".
 *
 * Any null string will be converted to an empty string.
 * 
 * @param cmd       enumerated constant associated with this command;
 *                  may not be null
 * @param cmdStr    the source string for this command
 * @param argStr    the argument string for this command
 * 
 * @author Jack Straub
 */
public record ParsedCommand( 
    Command command, 
    String commandString, 
    String argString 
)
{
    /**
     * Describes a Command and its associated argument.
     * 
     * @param command       the command described
     * @param commandString the original string that the command
     *                      and argument were derived from
     * @param argString     the optional argument associated with command;
     *                      may be the empty string
     *                      
     * @throws NullPointerException if command is null
     */
    public ParsedCommand
    {
        Objects.requireNonNull( command, "Command" );
        if ( commandString == null )
            commandString = "";
        if ( argString    == null )
            argString    = "";
    }
}
