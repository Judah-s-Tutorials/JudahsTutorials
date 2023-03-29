package com.acmemail.judah.sandbox;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

public class FunctionDemo1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Function    radians = new Function( "toRadians", 1 ) {
            @Override
            public double apply( double... args ) {
                return args[0] * Math.PI / 180;
            }
        };
        
        Expression  expr    = new ExpressionBuilder( "toRadians( d )" )
            .variables("d")
            .function( radians )
            .build();
        
        expr.setVariable( "d", 180 );
        System.out.println( expr.evaluate() );
    }
}
