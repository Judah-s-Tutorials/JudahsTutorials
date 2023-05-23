package com.acmemail.judah.cartesian_plane.input;

import java.util.Arrays;

/**
 * Encapsulation of commands
 * used to configure and evaluate
 * a Exp4j expressions.
 * 
 * @author Jack Straub
 * 
 * @see Exp4jEquation
 */
public enum Command
{
    /** Creates a new equation. */
    EQUATION( "Creates a new equation" ),
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
    /** Sets the expression for generating the radius in a polar equation. */
    REQUALS( 
        "Describes any well-formed expression "
        + "for the evaluation of \"radius\" "
        + "in the polar equation \"r=f(t)\""
    ),
    /** Sets the expression for generating the angle in a polar equation. */
    TEQUALS( 
        "Describes any well-formed expression "
        + "for the evaluation of \"theta\" "
        + "in the polar equation \"t=f(r)\""
    ),
    /** Declares one or more variables. */
    SET( 
        "Describes a comma-separated list of "
        + "variables of the form name[=expression]" 
    ),
    /** Sets the start of the iteration range. */
    START( 
        "Expression that describes the start value "
            + "of the iteration range" ),
    /** Sets the end of the iteration range. */
    END( "Expression that describes the end value in the iteration range" ),
    /** Sets the increment value for traversing the iteration range. */
    STEP( 
        "Expression that describes the increment value "
            + "for traversing the iteration range"
    ),
    /** Sets the name of the parameter in a parametric equation. */
    PARAM( "Describes the name of the parameter in a parametric equation" ),
    /** Sets the name of the angle variable in a polar equation. */
    THETA( "Describes the name of the angle variable in a polar equation" ),
    /** Sets the name of the radius variable in a polar equation. */
    RADIUS( "Describes the name of the radius variable in a polar equation" ),
    /** Generates the plot of the function y=f(x). */
    YPLOT( "Generates a plot of the form (x,y) = f(x)" ),
    /** Generates the plot of the parametric equation (x,y)=f(t). */
    XYPLOT( "Generates a plot of the form (x,y) = f(t)" ),
    /** Generates the plot of the polar equation r = f(t). */
    RPLOT( "Generates a plot of the polar equation r = f(t)" ),
    /** Generates the plot of the polar equation t = f(r). */
    TPLOT( "Generates a plot of the polar equation t = f(r)" ),
    /** Identifies and empty command string. */
    NONE( "Identifies an empty command string" ),
    /** Identifies an invalid command. */
    INVALID( "Designates an invalid command." ),
    /** Exit the current operation. */
    EXIT( 
        "Application specific; probably "
        + "\"Exit from the current operation\""
    ),
    /** Load one or more named equations into the EquationMap */
    LOAD( "Load one or more equations into memory" ),
    /** Select an equation from the EquationMap. */
    SELECT( "Select an equation from a list" ),
    /** Open a file. */
    OPEN( "Application specific; probably \"open equation file\""),
    /** Save a file. */
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
                    .append( ": " )
                    .append( e.desc )
                    .append( lineSep )
            );
        return bldr.toString();
    }
}