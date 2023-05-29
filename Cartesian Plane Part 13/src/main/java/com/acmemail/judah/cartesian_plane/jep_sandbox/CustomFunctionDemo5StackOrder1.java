package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Stack;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * Program to demonstrate the order in which
 * JEP pushes arguments onto the stack when 
 * evaluating a custom function.
 * The order is left-to-right, so the last argument
 * is pushed onto the stack last;
 * therefore the the last argument will be
 * the first one popped off the stack.
 *  
 * @author Jack Straub
 */
public class CustomFunctionDemo5StackOrder1
{
    /**
     * Program entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        try
        {
            JEP     expr    = new JEP();
            expr.addStandardConstants();
            expr.addStandardFunctions();
            expr.setImplicitMul( true );
            expr.addFunction( "leg", new Leg() );
            
            expr.addVariable( "h", 5 );
            expr.addVariable( "l", 4 );
            expr.parseExpression( "leg( h, l )" );
            double  leg2    = expr.getValue();
            System.out.println( "leg2 = " + leg2 );
        }
        catch ( Exception exc )
        {
            // Why are we catching every exception?
            // The only checked exception we're expecting 
            // is ParseException, but if there's a bug in
            // the code several things could go wrong, 
            // including, for example, EmptyStackException.
            exc.printStackTrace();
        }
    }

    /**
     * Custom JEP function to calculate the missing leg
     * of a right triangle. The expected invocation of
     * this function is "leg(h, 1)", where "h" is the
     * length of the hypotenuse, and "l" is the length
     * of the known leg.
     * 
     * @author Jack Straub
     */
    public static class Leg extends PostfixMathCommand
    {
        /**
         * Constructor.
         * Establishes the number of arguments
         * required by this function.
         */
        public Leg()
        {
            numberOfParameters = 2;
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void run(Stack stack) 
            throws ParseException
        {
            checkStack( stack );
            // JEP processes function arguments from left-to-right.
            // Given "leg( hypot, leg1 )" first "hypot" will be pushed
            // and then "leg1". So the first thing popped will 
            // be "leg1".
            Object  oLeg1   = stack.pop();
            Object  oHypot  = stack.pop();
            if ( !(oHypot instanceof Double) || !(oLeg1 instanceof Double ) )
            {
                String  message = "hypot: invalid argument type";
                throw new ParseException( message );
            }
                
            double  hypot   = (Double)oHypot;
            double  leg1    = (Double)oLeg1;
            double  diffSq  = (hypot * hypot) - (leg1 * leg1);
            double  leg2    = Math.sqrt( diffSq );
            stack.push( leg2 );
        }
    }
}
