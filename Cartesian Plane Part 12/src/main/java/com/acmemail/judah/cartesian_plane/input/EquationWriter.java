package com.acmemail.judah.cartesian_plane.input;

import static com.acmemail.judah.cartesian_plane.input.Command.END;
import static com.acmemail.judah.cartesian_plane.input.Command.EQUATION;
import static com.acmemail.judah.cartesian_plane.input.Command.PARAM;
import static com.acmemail.judah.cartesian_plane.input.Command.RADIUS;
import static com.acmemail.judah.cartesian_plane.input.Command.REQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.SET;
import static com.acmemail.judah.cartesian_plane.input.Command.START;
import static com.acmemail.judah.cartesian_plane.input.Command.STEP;
import static com.acmemail.judah.cartesian_plane.input.Command.TEQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.THETA;
import static com.acmemail.judah.cartesian_plane.input.Command.XEQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.YEQUALS;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * This class is responsible for formatting an Equation in text,
 * and writing it to an output stream.
 * Each line in the output contains a command
 * and its associated value.
 * The first command is always EQUATION followed by the equation name.
 * Here's an example of the output:
 * <pre style="left-padding:2em;">SET a=2, b=3
 * x= 3 * cos(a * t)
 * y= 3 * sin(b * t)
 * START 0
 * END   2 * pi
 * STEP  pi / 200</pre>
 * <p>
 * Not all data is necessarily written to the output stream.
 * The exceptions are:
 * </p>
 * <ul>
 *     <li>
 *          The value of an expression
 *          (XEQUALS, YEQUALS, REQUALS, TEQUALS)
 *          is only written to the output stream
 *          if it has a non-default value.
 *     </li>
 *          The parameter, radius, and angle variable names
 *          (PARAM, RADIUS, THETA)
 *          are only written to the output stream
 *          if there values are non-default.
 *     <li>
 *          The values of the intrinsic variables
 *          (x, y, t, etc.)
 *          are only written to the output stream
 *          if they have non-default values.
 *     </li>
 * </ul>
 * 
 * @author Jack Straub
 * @see Equation
 * @see Equation#DEF_STRINGS
 * @see Equation#INTRINSIC_VARIABLES
 */
public final class EquationWriter
{
    /** 
     * Reference to Equation intrinsic variable map, 
     * declared here for convenience.
     */
    private static final Map<String,Double> intrinsicVarMap =
        Equation.INTRINSIC_VARIABLES;
    
    /** 
     * Reference to Equation intrinsic variable map, 
     * declared here for convenience.
     */
    private static final Map<Command,String> stringDefs =
        Equation.DEF_STRINGS;
    
    /**
     * Formats an equation as text,
     * and writes it to a given output stream.
     * 
     * @param equation  the equation to convert; must not be null
     * @param out       the given output stream; must not be null
     * 
     * @throws NullPointerException if {@code equation} is null
     * @throws NullPointerException if {@code out} is null
     */
    public static void write( Equation equation, PrintWriter out )
    {
        Objects.requireNonNull( equation, "equation" );
        Objects.requireNonNull( out, "out" );
        
        // EQUATION command must be first
        out.println( EQUATION + " " + equation.getName() );
        
        // Always write out the range properties, whether they
        // have default values or not
        out.println( START + " " + equation.getRangeStart() );
        out.println( END + " " + equation.getRangeEnd() );
        out.println( STEP + " " + equation.getRangeStep() );
        
        // Write the expressions and the special names, but only
        // if they are unique.
        writeUnique( out, YEQUALS, equation::getYExpression );
        writeUnique( out, XEQUALS, equation::getXExpression );
        writeUnique( out, REQUALS, equation::getRExpression );
        writeUnique( out, TEQUALS, equation::getYExpression );
        writeUnique( out, RADIUS, equation::getRadiusName );
        writeUnique( out, THETA, equation::getThetaName );
        writeUnique( out, PARAM, equation::getParamName );
        
        // Make a separate SET command for each variable. If a variable
        // is intrinsic, only write a command if it has a unique value
        Map<String,Double>  varMap  = equation.getVars(); 
        varMap.forEach( (n,v) -> setUnique( out, n, v ) );
    }
    
    /**
     * Given command with a string value,
     * determine if it has a non-default value, and,
     * if it does, write it to the given output stream.
     * 
     * @param out           the given output stream
     * @param command       the given command
     * @param actGetter     supplier to obtain the command's value
     * 
     * @see Equation#DEF_STRINGS
     */
    private static void writeUnique( 
        PrintWriter out,
        Command command, 
        Supplier<String> actGetter
    )
    {
        String  actString   = actGetter.get();
        String  defString   = stringDefs.get( command );
        // null test is purely defensive; actString should never be null
        if ( actString != null && !actString.equals( defString ) )
            out.println( command + " " + actString );
    }
    
    /**
     * Determine if a variable has a unique value, and,
     * if it does, create a command to the variable's value
     * and write the command to the output stream.
     * A variable has a unique value if: a) it is an
     * intrinsic variable; and b) it has a non-default variable.
     * 
     * @param out       the output destination
     * @param name      the name of the variable
     * @param value     the value of the variable
     * 
     * @see Equation#INTRINSIC_VARIABLES
     */
    private static void 
    setUnique( PrintWriter out, String name, double value )
    {
        boolean unique  = true;
        if ( intrinsicVarMap.containsKey( name ) )
        {
            double  defValue    = intrinsicVarMap.get( name );
            if ( Double.compare( value, defValue ) == 0 )
                unique = false;
        }
        if ( unique )
        {
            String  strValue    = format( value );
            out.println( SET + " " + name + "=" + strValue );
        }
    }
    
    /**
     * Format a float point value as a string,
     * using the ROOT locale.
     * Using the ROOT locale ensures that the period (.)
     * is used as the decimal separator,
     * 
     * @param dNum  the floating point value to format
     * 
     * @return  the formatted value
     */
    private static String format( double dNum )
    {
        String  strNum  = String.format( Locale.ROOT, "%s", dNum );
        return strNum;
    }
}
