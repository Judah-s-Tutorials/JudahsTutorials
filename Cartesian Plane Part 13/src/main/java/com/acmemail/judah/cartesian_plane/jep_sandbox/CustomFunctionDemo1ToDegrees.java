package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Stack;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * Example of custom JEP function
 * with a fixed number of arguments.
 * Implements "toDegrees( radians )"
 * which converts radians to degrees.
 * 
 * @author Jack Straub
 */
public class CustomFunctionDemo1ToDegrees 
    extends PostfixMathCommand
{
    /**
     * Constructor.
     * Establishes the number of arguments
     * required by this function.
     */
    public CustomFunctionDemo1ToDegrees()
    {
        numberOfParameters = 1;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void run( Stack stack )
        throws ParseException
    {
        checkStack( stack );
        Object  oRadians    = stack.pop();
        if ( !(oRadians instanceof Double) )
        {
            String  message =
                "To degrees: invalid argument type ("
                + oRadians.getClass().getName()
                + ")";
            throw new ParseException( message );
        }
            
        double  radians = (double)oRadians;
        double  degrees = radians * 180 / Math.PI;
        stack.push( degrees );
    }
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        JEP     xExpr   = new JEP();
        xExpr.addStandardConstants();
        xExpr.addStandardFunctions();
        xExpr.setImplicitMul( true );
        xExpr.addFunction( "toDegrees", new CustomFunctionDemo1ToDegrees() );
        xExpr.parseExpression( "toDegrees( pi / 2 )" );
        System.out.println( xExpr.getValue() );
    }
}
