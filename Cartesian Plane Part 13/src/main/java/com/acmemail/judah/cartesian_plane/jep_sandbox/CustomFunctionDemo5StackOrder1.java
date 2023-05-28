package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Stack;
import java.util.stream.IntStream;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

public class CustomFunctionDemo5StackOrder1
{
    public static void main(String[] args)
        throws ParseException
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

    public static class Leg extends PostfixMathCommand
    {
        public Leg()
        {
            numberOfParameters = 2;
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void run(Stack stack) 
            throws ParseException, ClassCastException
        {
            checkStack( stack );
            Object  oHypot  = stack.pop();
            Object  oLeg1   = stack.pop();
            if ( !(oHypot instanceof Double) || !(oLeg1 instanceof Double ) )
            {
                String  message = "hypot: invalid argument type";
                throw new ParseException( message );
            }
                
            double  hypot   = (Double)oHypot;
            double  leg1    = (Double)oLeg1;
            double  diffSq  = (leg1 * leg1) - (hypot * hypot);
            double  leg2    = Math.sqrt( diffSq );
            stack.push( leg2 );
        }
    }
}
