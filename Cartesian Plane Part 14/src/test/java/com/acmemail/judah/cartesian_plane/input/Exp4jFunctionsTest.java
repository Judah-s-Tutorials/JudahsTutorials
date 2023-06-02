package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

class Exp4jFunctionsTest
{
    private static final double epsilon = .0001;

    @Test
    void testToDegrees()
    {
        evaluate( "toDegrees( pi/2 )", 90 );
    }

    @Test
    void testToRadians()
    {
        evaluate( "toRadians( 90 )", Math.PI / 2 );
    }

    @Test
    void testSecant()
    {
        evaluate( "sec( pi/4 )", 1 / Math.cos( Math.PI / 4) );
    }

    @Test
    void testCosecant()
    {
        evaluate( "csc( pi/4 )", 1 / Math.sin( Math.PI / 4) );
    }

    @Test
    void testArcTan()
    {
        double  theta       = Math.PI / 4;
        double  tanTheta    = Math.tan( theta );
        String  atanStr     = "atan(" + tanTheta + ")";
        evaluate( atanStr, theta );
    }

    @Test
    void testCotangent()
    {
        evaluate( "cot( pi/4 )", 1 / Math.tan( Math.PI / 4 ) );
    }
    
    private void evaluate( String exprStr, double expVal )
    {
        Expression  expr    =
            new ExpressionBuilder( exprStr )
                .functions( Exp4jFunctions.getFunctions() )
                .build();
        ValidationResult    exp4jResult = expr.validate( true );
        if ( !exp4jResult.isValid() )
            throw new ValidationException();
        double      actVal  = expr.evaluate();
        assertEquals( expVal, actVal, epsilon, exprStr );
    }
}
