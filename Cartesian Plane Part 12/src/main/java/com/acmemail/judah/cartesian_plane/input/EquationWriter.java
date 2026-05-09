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
import java.util.function.Supplier;

public final class EquationWriter
{
    /** 
     * Reference to Equation intrinsic variable map, 
     * declared here for convenience.
     */
    private static final Map<String,Double> intrinsicVarMap     =
        Equation.INTRINSIC_VARIABLES;
    
    /** 
     * Reference to Equation intrinsic variable map, 
     * declared here for convenience.
     */
    private static final Map<Command,String> stringDefs       =
        Equation.SPECIAL_NAMES;
    
    public static void write( Equation equation, PrintWriter out )
    {
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
            out.println( SET + " " + strValue );
        }
    }
    
    private static String format( double dNum )
    {
        String  strNum  = String.format( Locale.ROOT, "%s", dNum );
        return strNum;
    }
}
