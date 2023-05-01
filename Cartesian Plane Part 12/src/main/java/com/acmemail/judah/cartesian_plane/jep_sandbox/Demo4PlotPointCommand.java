package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;

import org.nfunk.jep.JEP;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;

public class Demo4PlotPointCommand
{
    public static void main(String[] args)
    {
        CartesianPlane  plane   = new CartesianPlane();
        JEP parser  = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.setImplicitMul( true );
        parser.addVariable( "x", 0 );
        parser.parseExpression( "2x" );
        
        DoubleStream.iterate( -3, d -> d <= 3, d -> d + .1 )
            .peek( d -> parser.addVariable( "x", d ) )
            .mapToObj( d -> new Point2D.Double( d, parser.getValue() ) )
            .map( p -> PlotPointCommand.of( p, plane) )
            .forEach( System.out::println );
    }
}
