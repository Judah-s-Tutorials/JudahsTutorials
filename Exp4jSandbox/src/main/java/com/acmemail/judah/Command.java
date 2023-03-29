package com.acmemail.judah;

import java.util.Arrays;

/**
 * Encapsulation of commands
 * used to configure and evaluate
 * a Exp4j expressions.
 * 
 * @author Jack Straub
 * 
 * @see Equation
 */
public enum Command
{
    EQUATION( "Describes an equation of the form y = f(x)." ),
    X_EQUALS( 
        "Describes any well-formed expression for the evaluation of \"x\" "
        + "in the coordinate pair \"(x,y)\""
    ),
    Y_EQUALS( 
        "Describes any well-formed expression for the evaluation of \"y\" "
        + "in the coordinate pair \"(x,y)\""
    ),
    FUNCTION( "Describes any well-formed function" ),
    VARIABLES( 
        "Describes a comma-separated list of "
        + "variables of the form name[=value]" 
    ),
    START( "Describes the start value in the iteration range" ),
    END( "Describes the end value in the iteration range" ),
    INCREMENT( 
        "Describes the increment value "
        + "for traversing iteration range"
    ),
    PARAM( "Describes the name of the parameter in a parametric equation" ),
    Y_STREAM( "Generate a stream of the form (x,y) = f(x)" ),
    XY_STREAM( "Generate a stream of the form (x,y) = f(t)" ),
    EXIT( "Exit from the current operation" ),
    INVALID( "Designates an invalid command." ),
    NONE( "Identifies an empty command string" );
    
    /** Line separator for the current platform. */
    private static final String lineSep         = System.lineSeparator();
    /** 
     * Number of initial characters of constant name 
     * to use as an abbreviation.
     */
    private static final int    shortFormLen    = 3;
    /** Description of command, mainly for use in "Usage": messages. */
    private final String        desc;
    /** Command abbreviation, always from the initial value of the name. */
    private final String abbr;
    
    /**
     * Constructor.
     * Establishes the description of the command
     * and the command abbreviation.
     * The abbreviation is derived
     * from the first <em>n</em> characters
     * of the command name, where n = <em>shortFormLen.</em>
     * 
     * @param desc  the command description
     */
    private Command( String desc )
    {
        this.desc = desc;
        String  name    = this.name();
        int     nameLen = name.length();
        int     end     = nameLen < shortFormLen ? nameLen : shortFormLen;
        this.abbr = name.substring( 0, end );
    }
    
    /**
     * Gets the abbreviation of this command. 
     * 
     * @return  the abbreviation of this command
     */
    public String getAbbreviation()
    {
        return abbr;
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
     * Compares the initial value of a given string
     * to the abbreviated names of enumerated constant
     * and returns the first matching constant.
     * The comparison is case-insensitive.
     * If the given string is empty NONE is returned.
     * If no match is found INVALID.
     * 
     * @param from  the given string
     * 
     * @return  
     *      the first command whose abbreviated name
     *      matches the initial characters
     *      of the given string
     */
    public static Command toCommand( String from )
    {
        String  upperFrom   = from.toUpperCase();
        Command cmd         = NONE;
        if ( !upperFrom.isEmpty() )
            cmd = Arrays.stream( values() )
                .filter( e -> upperFrom.startsWith( e.abbr ) )
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
        bldr.append( "Valid commands; ")
            .append( "may be abbreviated to the first ")
            .append( shortFormLen ).append( "characters" )
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