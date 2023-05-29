package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Stack;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * Program to investigate
 * whether JEP builtin functions
 * can be overridden with a custom function.
 * 
 * @author Jack Straub
 */
public class OverridingBuiltinFunctionsDemo
{
    /**
     * Program entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String  str     = "log( 2 )";
        JEP     expr    = new JEP();
        expr.addStandardConstants();
        expr.addStandardFunctions();
        expr.setImplicitMul( true );
        expr.addFunction( "log", new LogE() );
        expr.addVariable( "a", 1.5 );
        expr.addVariable( "h", -1 );
        expr.addVariable( "t", Math.PI );
        expr.parseExpression( str );
        System.out.println( expr.getValue() );
        System.out.println( Math.log( 2 ) );
    }
    
    private static class LogE extends PostfixMathCommand
    {
        public LogE()
        {
            numberOfParameters = 1;
        }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public void run( Stack stack )
            throws ParseException
        {
            checkStack( stack );
            Object  oDbouble    = stack.pop();
            if ( !(oDbouble instanceof Double) )
            {
                String  message =
                    "To degrees: invalid argument type ("
                    + oDbouble.getClass().getName()
                    + ")";
                throw new ParseException( message );
            }
                
            double  arg     = (double)oDbouble;
            double  result  = Math.log( arg );
            stack.push( result );
        }

    }
}
