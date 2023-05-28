package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Stack;
import java.util.stream.IntStream;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

public class CustomFunctionDemo6StackOrder2
{

    public static void main(String[] args)
        throws ParseException
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
                .forEach( a -> System.out.print( a + "    " ) );
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
                .forEach( a -> System.out.print( a + "    " ) );
            stack.push( 0 );
            System.out.println();
        }
    }
}
