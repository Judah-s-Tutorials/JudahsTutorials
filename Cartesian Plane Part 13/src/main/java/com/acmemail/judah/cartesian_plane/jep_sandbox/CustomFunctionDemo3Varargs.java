package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Stack;
import java.util.stream.IntStream;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * Program to demonstrate how to write
 * a varargs custom function in the JEP implementation.
 * The only difference between this demo
 * and {@link CustomFunctionDemo2Varargs}
 * is that this demo uses a stream
 * to compute the average of a list of numbers
 * while {@link CustomFunctionDemo2Varargs}
 * uses a traditional for loop.
 * 
 * @author Jack Straub
 * 
 * @see CustomFunctionDemo2Varargs
 */
public class CustomFunctionDemo3Varargs
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
     * Uses a stream
     * to compute the average of the arguments.
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
            double      avg     = IntStream.range(0, curNumberOfParameters)
                .mapToDouble( i -> (Double)stack.pop() )
                .average()
                .orElse( 0 );
            stack.push( avg );
        }
    }
}
