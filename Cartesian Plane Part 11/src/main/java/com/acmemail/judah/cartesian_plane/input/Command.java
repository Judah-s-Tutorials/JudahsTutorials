package com.acmemail.judah.cartesian_plane.input;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * Encapsulation of commands
 * used to configure and evaluate
 * an Exp4j expression.
 * 
 * @author Jack Straub
 * 
 * @see Exp4jEquation
 */
public enum Command
{
    /** Names an equation. */
    EQUATION( "Names an equation" ),
    /** Establishes the expression for generating an x-coordinate. */
    XEQUALS( 
        "Describes any well-formed expression for the evaluation of \"x\" "
        + "in the coordinate pair \"(x,y)\""
    ),
    /** Establishes the expression for generating a y-coordinate. */
    YEQUALS( 
        "Describes any well-formed expression for the evaluation of \"y\" "
        + "in the coordinate pair \"(x,y)\""
    ),
    /** Declares one or more variables. */
    SET( 
        "Describes a comma-separated list of "
        + "variables of the form name[=expression]" 
    ),
    /** Sets the start of the iteration range. */
    START( 
        "Describes any well-formed expression that determines"
         + " the start value of the iteration range" 
    ),
    /** Sets the end of the iteration range. */
    END( 
        "Describes any well-formed expression that determines"
        + " the end value of the iteration range"
    ),
    /** Sets the increment value for traversing the iteration range. */
    STEP( 
        "Describes any well-formed expression that determines"
        + " the increment value for traversing the iteration range"
    ),
    /** Sets the name of the parameter in a parametric equation. */
    PARAM( "Describes the name of the parameter in a parametric equation" ),
    /** Generates the plot of the function y=f(x). */
    YPLOT( "Generates a plot of the form (x,y) = f(x)" ),
    /** Generates the plot of the parametric equation (x,y)=f(t). */
    XYPLOT( "Generates a plot of the form (x,y) = f(t)" ),
    /** Identifies an empty command string. */
    NONE( "Identifies an empty command string" ),
    /** Identifies an invalid command. */
    INVALID( "Designates an invalid command" ),
    /** Exit the current operation. */
    EXIT( "Exits from the current operation" ),
    /** Select an equation from the EquationMap. */
    SELECT( "Selects an equation from a list" ),
    /** Open a file. */
    OPEN( "Opens an equation file" ),
    /** Save a file. */
    SAVE( "Saves an equation file" );
    
    /** Line separator for the current platform. */
    private static final String lineSep         = System.lineSeparator();
    /** Description of command, mainly for use in "Usage": messages. */
    private final String        desc;
    
    /**
     * Constructor.
     * Establishes the description of the command.
     * 
     * @param desc  the command description
     */
    private Command( String desc )
    {
        this.desc = desc;
    }
    
    /**
     * Gets the description of this command.
     * 
     * @return the description of this command
     */
    public String getDescription()
    {
        return desc;
    }
    
    /**
     * Compares the value of a given string
     * to the names of the enumerated constants
     * and returns the matching constant.
     * The comparison is case-insensitive.
     * If the given string is empty or blank NONE is returned.
     * If no match is found INVALID is returned.
     * 
     * @param from  the given string
     * 
     * @return  
     *      the matching Command, or NONE for empty/blank input, 
     *      or INVALID if no name matches
     *      
     * @throws NullPointerException if from is null
     */
    public static Command toCommand( String from )
    {
        String  upperFrom   = 
            Objects.requireNonNull( from, "from" ).toUpperCase().trim();
        Command cmd         = NONE;
        if ( !upperFrom.isEmpty() )
            cmd = Arrays.stream( values() )
                .filter( e -> upperFrom.equals( e.name() ) )
                .findFirst()
                .orElse( INVALID );
        return cmd;
    }
    
    /**
     * Returns a usage statement for the commands
     * in this enum.
     * Omits usage of NONE and INVALID.
     * 
     * @return a usage statement for the commands in this enum.
     */
    public static String usage()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "Valid commands:")
            .append( lineSep );
        Arrays.stream( values() )
            .filter( e -> e != INVALID )
            .filter( e -> e != NONE )
            .sorted( Comparator.comparing( Command::name ) )
            .forEach( e -> 
                bldr.append( "    " )
                    .append( e )
                    .append( ": " )
                    .append( e.desc )
                    .append( lineSep )
            );
        return bldr.toString();
    }
}