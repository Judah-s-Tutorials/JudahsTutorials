package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.stream.DoubleStream;

import org.nfunk.jep.JEP;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.Polar;

public class JEPPolarDemo1
{

    public static void main(String[] args)
    {
        CartesianPlane  plane   = new CartesianPlane();
        Root            root    = new Root( plane );
        root.start();
        
        JEP jepExpr = new JEP();
        jepExpr.addStandardConstants();
        jepExpr.addStandardFunctions();
        jepExpr.setImplicitMul( true );
        jepExpr.addVariable( "a", 3 );
        jepExpr.addVariable( "n", 5 );
        jepExpr.addVariable( "t", 0 );
        jepExpr.parseExpression( "a cos( n t )" );
        
        if ( jepExpr.hasError() )
        {
            System.out.println( jepExpr.getErrorInfo() );
            System.exit( 1 );
        }
        
        plane.setStreamSupplier(  () ->
            DoubleStream.iterate( 0, t -> t < 2 * Math.PI, t -> t + .001 )
                .peek( t -> jepExpr.addVariable( "t", t ) )
                .mapToObj( t -> Polar.of( jepExpr.getValue(), t ) )
                .map( p -> p.toPoint() )
                .map( p -> PlotPointCommand.of( p, plane ) )
        );

    }

}
