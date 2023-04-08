package com.acmemail.judah.cartesian_plane.input;

import java.util.Arrays;

/**
 * Encapsulation of commands
 * used to configure and evaluate
 * a Exp4j expressions.
 * 
 * @author Jack Straub
 * 
 * @see Expr4Equation
 */
public enum Command
{
    EQUATION( "Describes an equation of the form y = f(x)." ),
    XEQUALS( 
        "Describes any well-formed expression for the evaluation of \"x\" "
        + "in the coordinate pair \"(x,y)\""
    ),
    YEQUALS( 
        "Describes any well-formed expression for the evaluation of \"y\" "
        + "in the coordinate pair \"(x,y)\""
    ),
    SET( 
        "Describes a comma-separated list of "
        + "variables of the form name[=value]" 
    ),
    START( 
        "Expression that describes the start value "
            + "of the iteration range" ),
    END( "Expression that describes the end value in the iteration range" ),
    STEP( 
        "Expression that describes the increment value "
            + "for traversing iteration range"
    ),
    PARAM( "Describes the name of the parameter in a parametric equation" ),
    YPLOT( "Generate a plot of the form (x,y) = f(x)" ),
    XYPLOT( "Generate a plot of the form (x,y) = f(t)" ),
    NONE( "Identifies an empty command string" ),
    INVALID( "Designates an invalid command." ),
    EXIT( 
        "Application specific; probably "
        + "\"Exit from the current operation\""
    ),
    OPEN( "Application specific; probably \"open equation file\""),
    SAVE( "Application specific; probably \"save equation file\"");
    
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
     * and returns the first matching constant.
     * The comparison is case-insensitive.
     * If the given string is empty NONE is returned.
     * If no match is found INVALID is returned.
     * 
     * @param from  the given string
     * 
     * @return  
     *      the first command whose name
     *      matches the given string
     */
    public static Command toCommand( String from )
    {
        String  upperFrom   = from.toUpperCase();
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
            .filter( e -> e!= NONE )
            .sorted( (e1,e2) -> e1.name().compareTo( e2.name() ) )
            .forEach( e -> 
                bldr.append( "    " )
                    .append( e )
                    .append( ' ' )
                    .append( e.desc )
                    .append( lineSep )
            );
        return bldr.toString();
    }
}