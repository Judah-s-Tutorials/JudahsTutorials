package com.acmemail.judah;

/**
 * This is a small class to store the result
 * of parsing a command string.
 * The expectation is that a string such as this
 * has been parsed into a command and an argument:
 * <br><code>&nbsp;&nbsp;&nbsp;&nbsp;y_equals x^2 - 1</code>.
 * Exactly how the parsing is performed
 * is up to the user, 
 * however the above line of text 
 * will likely be interpreted command = 
 * Command.Y_EQUALS, and argument = "x^2 - 1".
 * 
 * @author Jack Straub
 */
public class ParsedCommand
{
    /** The parsed command, is interpreted by the user. */
    private final Command   command;
    /** The string that we used to obtain the Command constant. */
    private final String    commandString;
    /** The argument that was parsed from the original command sgring. */
    private final String    argString;
    
    /**
     * Constructor.
     * Any null string will be converted to an empty string.
     * 
     * @param cmd       enumerated constant associated with this command
     * @param cmdStr    the source string for this command
     * @param argStr    the argument string for this command
     */
    public ParsedCommand( Command cmd, String cmdStr, String argStr )
    {
        super();
        this.command = cmd;
        this.commandString = cmdStr != null ? cmdStr : "";
        this.argString = argStr != null ? argStr : "";
    }

    /**
     * Gets the enumerated constant associated with a command.
     * 
     * @return enumerated constant associated with a command
     */
    public Command getCommand()
    {
        return command;
    }

    /**
     * Gets the string associated with the encapsulated command.
     * 
     * @return the string associated with the encapsulated command
     */
    public String getCommandString()
    {
        return commandString;
    }

    /**
     * Gets the command argument, if any.
     * If none, the empty string will be returned.
     * 
     * @return the argument associated with the command
     */
    public String getArgString()
    {
        return argString;
    }
}
