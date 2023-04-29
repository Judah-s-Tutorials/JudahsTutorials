package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;

import org.nfunk.jep.JEP;
import org.nfunk.jep.type.Complex;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

public class PlotEHatZ
{
    private static final Complex    ezed    = new Complex( Math.E, 0 );
    private static final JEP        parser;
    static
    {
        parser = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.addComplex();
        parser.setImplicitMul( true );
    }

    public static void main(String[] args)
    {
        CartesianPlane  plane   = new CartesianPlane();
        Root            root    = new Root( plane );
        root.start();
        
        double  theta   = Math.PI / 3;
        plane.setStreamSupplier( () ->
            DoubleStream.iterate( -2, r -> r <= 2 , r -> r + .005 )
                .mapToObj( r -> new PolarZ( r, theta ) )
                .map( pz -> pz.toComplex() )
                .map( z -> z.power( ezed ) )
                .map( z -> new Point2D.Double( z.re(), z.im() ) )
                .map( p -> PlotPointCommand.of( p, plane ) )
        );
        NotificationManager.INSTANCE
        .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    private static Point2D toPoint( Complex cpx )
    {
        Point2D point   = new Point2D.Double( cpx.re(), cpx.im() );
        return point;
    }

    private static void print( PolarZ pzed, Point2D point )
    {
        String  fmt = "(%.1f, %.1f) ==> (%.1f, %.1f)%n";
        System.out.printf(
            fmt,
            pzed.getRadius(),
            pzed.getTheta(),
            point.getX(),
            point.getY()
        );
    }

    private static void print( Point2D point )
    {
        String  fmt = "(%.1f, %.1f)%n";
        System.out.printf(
            fmt,
            point.getX(),
            point.getY()
        );
    }
}
