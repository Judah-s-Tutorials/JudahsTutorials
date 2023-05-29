package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.nfunk.jep.JEP;

class JEPAbstractFunctionTest
{
    /**
     * Validate execution of a custom function
     * with a fixed number arguments, n, where
     * n &gt; 1. The order of the arguments
     * must be significant.
     */
    @Test
    void testFixedArgs()
    {
        FixedArgs   funk    = new FixedArgs();
        String  exprStr = "div( 10, 2 )";
        JEP     parser  = new JEP();
        parser.addFunction( funk.getName(), funk );
        parser.parseExpression( exprStr );
        assertFalse( parser.hasError() );
        
        double  val     = parser.getValue();
        assertEquals( 5, val );
    }

    /**
     * Validate execution of a custom function
     * with a variable number arguments
     * The order of the arguments
     * must be significant.
     * Tests must be run multiple times,
     * including tests with 0, 1, 2 and &gt; 2 arguments.
     */
    @ParameterizedTest
    @ValueSource( ints= {5, 4, 3, 2, 1, 0} )
    void testVarArgs( int numParams )
    {
        StringBuilder   bldr    = new StringBuilder( "sub(" );
        double          expVal  = Integer.MIN_VALUE;      
        if ( numParams > 0 )
        {
            expVal = numParams;
            bldr.append( numParams );
            for ( int inx = numParams - 1 ; inx > 0 ; --inx )
            {
                expVal += inx;
                bldr.append( "," ).append( inx );
            }
            expVal -= 2;
        }
        bldr.append( ")" );
        String  exprStr = bldr.toString();
        VarArgs funk    = new VarArgs();
        JEP     parser  = new JEP();
        parser.addFunction( funk.getName(), funk );
        parser.parseExpression( exprStr );
        assertFalse( parser.hasError() );
        
        double  val     = parser.getValue();
        System.out.println( val );
        assertEquals( expVal, val );
    }

    /**
     * Custom function with a fixed number of arguments.
     * Order of parameters matters.
     * 
     * @author Jack Straub
     */
    public static class FixedArgs extends JEPAbstractFunction
    {
        public FixedArgs()
        {
            super( "div", 2 );
        }
        
        @Override 
        public double evaluate( double... args )
        {
            double  dividend    = args[0];
            double  divisor     = args[1];
            double  quotient    = dividend / divisor;
            return quotient;
        }
    }

    /**
     * Custom function with a variable number of arguments.
     * Order of parameters matters.
     * 
     * Given n arguments, 
     * add the first n - 1 arguments
     * then subtract the last.
     * If 0 arguments are passed,
     * returns Integer.MIN_VALUE.
     * 
     * @author Jack Straub
     */
    public static class VarArgs extends JEPAbstractFunction
    {
        public VarArgs()
        {
            super( "sub", -1 );
        }
        
        @Override 
        public double evaluate( double... args )
        {
            double  result  = Integer.MIN_VALUE;
            int     numArgs = args.length;
            if ( numArgs > 0 )
            {
                double  last    = args[numArgs - 1];
                double  sum     = Arrays.stream( args ).sum();
                result  = sum - 2 * last;
            }
            return result;
        }
    }
}
