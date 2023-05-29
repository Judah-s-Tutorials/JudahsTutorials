package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Stack;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * Program to demonstrate how to write
 * a varargs custom function in the JEP implementation.
 * 
 * @author Jack Straub
 * 
 * @see CustomFunctionDemo3Varargs
 */
public class CustomFunctionDemo2Varargs
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     * @throws ParseException   
     *      if an error occurs while parsing an expression
     */
    public static void main(String[] args)
        throws ParseException
    {
        String  strExpr = "avg( .5, -.5, 1, -1, 10 )";
        JEP     expr    = new JEP();
        expr.addStandardConstants();
        expr.addStandardFunctions();
        expr.setImplicitMul( true );
        expr.addFunction( "avg", new Average() );
        expr.parseExpression( strExpr );
        double  val = expr.getValue();
        System.out.println( "average = " + val );
    }

    /**
     * Custom function to compute the average
     * of a variable number of arguments.
     * Uses a traditional for loop
     * to compute the sum of the arguments
     * (see the {@link #run(Stack) method}.
     * 
     * @author Jack Straub
     */
    public static class Average extends PostfixMathCommand
    {
        public Average()
        {
            numberOfParameters = -1;
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void run(Stack stack) 
            throws ParseException, ClassCastException
        {
            checkStack( stack );
            if ( curNumberOfParameters < 1 )
                throw new ParseException( "No parameters passed" );
            double      avg     = 0;
            for ( int inx = 0 ; inx < curNumberOfParameters ; ++inx )
                avg += (Double)stack.pop();
            avg /= curNumberOfParameters;
            stack.push( avg );
        }
    }
}
