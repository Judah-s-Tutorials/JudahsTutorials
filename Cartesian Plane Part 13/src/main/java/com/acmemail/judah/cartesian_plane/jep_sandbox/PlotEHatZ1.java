package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;

import org.nfunk.jep.type.Complex;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * Application to plot the equation e^z,
 * where: e is Euler's number, ^ is the power operator
 * and z is a complex number.
 * Given: z = (a + bi), 
 * the plot shows a increasing at a constant rate, ra,
 * and b increasing at a rate that is proportional
 * to ra.
 * 
 * @author Jack Straub
 */
public class PlotEHatZ1
{
    /** Euler's number, expressed in a complex format. */
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
        
        double  rco     = 0;
        double  ico     = 0;
        double  xier    = 10.5;
        plane.setStreamSupplier( () -> 
        DoubleStream.iterate( -4, d -> d <= 8, d -> d + .0005 )
            .mapToObj( d -> new Complex( rco + d, ico + d * xier ) )
            .map( ezed::power )
            .map( PlotEHatZ1::toPoint )
            .map( p -> PlotPointCommand.of( p, plane ) )
        );
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
