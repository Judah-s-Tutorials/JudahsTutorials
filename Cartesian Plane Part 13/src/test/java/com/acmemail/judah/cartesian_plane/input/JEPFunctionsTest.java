package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nfunk.jep.JEP;

class JEPFunctionsTest
{
    private static final double epsilon = .0001;

    @BeforeEach
    void setUp() throws Exception
    {
    }
    
    @Test
    public void testGetFunctions()
    {
        String[]                    expNames    =
            { "toDegrees", "toRadians", "sec", "csc", "cot" };
        List<JEPAbstractFunction>   list        = JEPFunctions.getFunctions();
        List<String>                actNames    =
            list.stream()
                .map( f -> f.getName() )
                .collect( Collectors.toList() );
        Arrays.stream( expNames )
            .forEach( n -> assertTrue( actNames.contains( n ), n ) );
    }

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
        JEP    parser  = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        JEPFunctions.addFunctions( parser );
        parser.setImplicitMul( true );
        parser.parseExpression( exprStr );
        double      actVal  = parser.getValue();
        assertEquals( expVal, actVal, epsilon, exprStr );
    }
}
