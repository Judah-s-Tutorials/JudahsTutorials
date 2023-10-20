package com.acmemail.judah.cartesian_plane.input;

import java.util.Stack;
import java.util.stream.IntStream;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * Abstract class that encapsulates
 * a JEP custom function.
 * implements PostfixMathCommand
 * as required by JEP.
 * 
 * @author Jack Straub
 */
public abstract class JEPAbstractFunction extends PostfixMathCommand
{
    /**
     * Method to evaluate a JEP function
     * based on a given array of values.
     * 
     * @param param the given array of values.
     * 
     * @return  the result of the evaluation
     */
    public abstract double evaluate( double... param );
    
    /** The name of the function. */
    private final String    name;
    
    /**
	 * Constructor.
	 * Establishes the name of the function
	 * and the number of required arguments.
	 * 
     * @param name      the name of the function    
     * @param numParams the number of required arguments
     */
    public JEPAbstractFunction( String name, int numParams )
    {
        this.name = name;
        numberOfParameters = numParams;
    }
    
    /**
     * Gets the name of the function.
     * 
     * @return  the name of the function
     */
    public String getName()
    {
        return name;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void run( Stack inStack )
        throws ParseException, ClassCastException
    {
        // If this number &lt; 0, it means we're evaluating 
        // a varargs function, and we have to use curNumberOfParameters.
        // If this number >= 0, curNumberOfParameters is unpredictable
        int actNumParams    = 
            numberOfParameters < 0 ? 
            curNumberOfParameters : 
            numberOfParameters; 

        checkStack( inStack );
        double[]    params  = new double[actNumParams];
        IntStream.iterate( actNumParams - 1, i -> i >= 0, i -> i - 1 )
            .forEach( i -> params[i] = (Double)inStack.pop() );
        double      result  = evaluate( params );
        inStack.push( result );
    }
}
