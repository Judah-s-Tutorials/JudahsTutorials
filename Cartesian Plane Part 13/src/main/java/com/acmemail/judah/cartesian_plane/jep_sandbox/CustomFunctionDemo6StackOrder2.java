package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Stack;
import java.util.stream.IntStream;

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
public class CustomFunctionDemo6StackOrder2
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
            expr.addFunction( "fixed", new FixedArgs() );
            expr.addFunction( "var", new VarArgs() );
            
            expr.parseExpression( "fixed( 1, 2, 3 )" );
            expr.getValue();
            expr.parseExpression( "var( 1, 2, 3, 4, 5 )" );
            expr.getValue();
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

    public static class FixedArgs extends PostfixMathCommand
    {
        public FixedArgs()
        {
            numberOfParameters = 3;
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void run(Stack stack) 
            throws ParseException, ClassCastException
        {
            System.out.print( "fixed (3): ");
            IntStream.range( 0, 3 )
                .forEach( a -> System.out.print( stack.pop() + "    " ) );
            stack.push( 0 );
            System.out.println();
        }
    }

    public static class VarArgs extends PostfixMathCommand
    {
        public VarArgs()
        {
            numberOfParameters = -1;
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void run(Stack stack) 
            throws ParseException, ClassCastException
        {
            System.out.print( "var: ");
            IntStream.range( 0, curNumberOfParameters )
                .forEach( a -> System.out.print( stack.pop() + "    " ) );
            stack.push( 0 );
            System.out.println();
        }
    }
}
