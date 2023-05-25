package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Stack;
import java.util.stream.IntStream;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

public class CustomVarargFunctionDemo
{

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
            double      avg     = IntStream
                .range(0, curNumberOfParameters)
                .mapToDouble( i -> (Double)stack.pop() )
                .average()
                .orElse( 0 );
            stack.push( avg );
        }
    }
}
