package com.acmemail.judah.cartesian_plane.test_util;
import static com.acmemail.judah.cartesian_plane.input.Command.END;
import static com.acmemail.judah.cartesian_plane.input.Command.PARAM;
import static com.acmemail.judah.cartesian_plane.input.Command.RADIUS;
import static com.acmemail.judah.cartesian_plane.input.Command.REQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.START;
import static com.acmemail.judah.cartesian_plane.input.Command.STEP;
import static com.acmemail.judah.cartesian_plane.input.Command.TEQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.THETA;
import static com.acmemail.judah.cartesian_plane.input.Command.XEQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.YEQUALS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.Equation;

/**
 * Contains utilities for the support
 * of unit tests that require access
 * to Equation data.
 */
public class EquationTestUtil
{
    /** Commands mapped to expressions. */
    public static final Set<Command> EXPR_COMMANDS = 
        Set.of( XEQUALS, YEQUALS, TEQUALS, REQUALS );
    
    /** Commands mapped to names. */
    public static final Set<Command> NAME_COMMANDS = 
        Set.of( PARAM, RADIUS, THETA );

    /** Commands mapped to range properties. */
    public static final Set<Command>   RANGE_COMMANDS =
        Set.of( START, END, STEP );

    /**
     * Default constructor; not used.
     */
    private EquationTestUtil()
    {
        // not used
    }
    
    /**
     * Generate a Command->String map of all special name commands
     * (PARAM, etc.)
     * in a given equation.
     * 
     * @param equation  the given equation
     * 
     * @return  the generated map
     */
    public static Map<Command,String> getNameMap( Equation equation )
    {
        Map<Command,String> map = new HashMap<>();
        for ( Command command : NAME_COMMANDS )
        {
            String  val = getString( equation, command );
            map.put( command, val );
        }
        return map;
    }
    
    /**
     * Generate a Command->String map of all expressions
     * in a given equation.
     * 
     * @param equation  the given equation
     * 
     * @return  the generated map
     */
    public static Map<Command,String> getExprMap( Equation equation )
    {
        Map<Command,String> map = new HashMap<>();
        for ( Command command : EXPR_COMMANDS )
        {
            String  expr    = getString( equation, command );
            map.put( command, expr );
        }
        return map;
    }
    
    /**
     * Generate a Command->String map of all range properties
     * in a given equation.
     * 
     * @param equation  the given equation
     * 
     * @return  the generated map
     */
    public static Map<Command,Double> getRangeMap( Equation equation )
    {
        Map<Command,Double> map = new HashMap<>();
        for ( Command command : RANGE_COMMANDS )
        {
            double  val = getDouble( equation, command );
            map.put( command, val );
        }
        return map;
    }

    /**
     * Get from an equation the string associated with a given command.
     * Asserts fail() if command is not supported.
     * 
     * @param   equation    the equation to interrogate
     * @param   command     the given command
     * 
     * @return the string associated with command
     */
    public static String getString( Equation equation, Command command )
    {
        String  str = null;
        switch ( command )
        {
        case XEQUALS -> str = equation.getXExpression();
        case YEQUALS -> str = equation.getYExpression();
        case REQUALS -> str = equation.getRExpression();
        case TEQUALS -> str = equation.getTExpression();
        case PARAM -> str = equation.getParamName();
        case RADIUS -> str = equation.getRadiusName();
        case THETA -> str = equation.getThetaName();
        default -> fail( "unsupported command: " + command );
        }
        return str;
    }
    
    /**
     * Get from an equation the value associated with a given command.
     * Asserts fail() if command is not supported.
     * 
     * @param   equation    the equation to interrogate
     * @param   command     the given command
     * 
     * @return the value associated with command
     */
    public static double getDouble( Equation equation, Command command )
    {
        double  actVal  = 0;
        switch ( command )
        {
        case START -> actVal = equation.getRangeStart();
        case END -> actVal = equation.getRangeEnd();
        case STEP -> actVal = equation.getRangeStep();
        default -> fail( "unsupported command: " + command );
        }
        return actVal;
    }
    
    /**
     * Given two equations,
     * one of which was generated from data 
     * stored in the other,
     * verify that the two equations
     * contain equivalent data.
     * 
     * @param expEquation   the original equation
     * @param actEquation   the equation generated from the original equation
     */
    public static void 
    verifyEquation( Equation expEquation, Equation actEquation )
    {
        assertEquals( expEquation.getName(), actEquation.getName() );
        assertEquals( getExprMap( expEquation ), getExprMap( actEquation ) );
        assertEquals( getNameMap( expEquation ), getNameMap( actEquation ) );
        assertEquals( getRangeMap( expEquation ), getRangeMap( actEquation ) );
        assertEquals( expEquation.getVars(), actEquation.getVars() );
    }
}
