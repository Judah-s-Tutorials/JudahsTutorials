package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;

import org.nfunk.jep.type.Complex;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * Application to plot the equation e^z,
 * where: e is Euler's number, ^ is the power operator
 * and z is a complex number.
 * Expressed in polar coordinates,
 * the radius iterates over a range
 * while the angle remains constant.
 * 
 * @author Jack Straub
 */
public class PlotEHatZ2
{
    /** Notification manager instance. */
    private static final NotificationManager    nmgr    = 
        NotificationManager.INSTANCE;
    /** Euler's number, expressed in complex format. */
    private static final Complex    ezed    = new Complex( Math.E, 0 );

    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        CartesianPlane  plane   = new CartesianPlane();
        Root            root    = new Root( plane );
        root.start();
        
        double  theta   = Math.PI / 2.1;
        plane.setStreamSupplier( () -> 
        DoubleStream.iterate( -16, r -> r <= 16, r -> r + .005 )
            .mapToObj( r -> CPXPolar.of( r, theta ) )
            .map( prt -> prt.toComplex() )
            .map( c -> ezed.power( c ) )
            .map( c -> toPoint( c ) )
            .map( p -> PlotPointCommand.of( p, plane ) )
        );
        
        nmgr.propagateNotification( CPConstants.REDRAW_NP );
    }
    
    /**
     * Convenience method to convert a Complex object
     * to a Point2D object.
     * The real and imaginary parts of the Complex object
     * become the x- and y-coordinates
     * of the Point2D object.
     * 
     * @param zed   the Complex object to convert
     * 
     * @return  the resultant Point2D object
     */
    private static Point2D toPoint( Complex zed )
    {
        Point2D point   = new Point2D.Double( zed.re(), zed.im() );
        return point;
    }
}
