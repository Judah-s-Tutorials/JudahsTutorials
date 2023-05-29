package com.acmemail.judah.cartesian_plane.input;

import java.util.Stack;
import java.util.stream.IntStream;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

public abstract class JEPAbstractFunction extends PostfixMathCommand
{
    public abstract double evaluate( double... param );
    private final String    name;
    
    public JEPAbstractFunction( String name, int numParams )
    {
        this.name = name;
        numberOfParameters = numParams;
    }
    
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
