package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;
import java.util.function.DoubleFunction;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.app.FIUtils.ToPlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * Simple application that uses a parametric equation
 * to plot a rose.
 * 
 * @author Jack Straub
 */
public class ParametricCoordinatesDemo
{
    private static final CartesianPlane plane   = new CartesianPlane();
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        PropertyManager pmgr    = PropertyManager.INSTANCE;
        
        pmgr.setProperty( CPConstants.TIC_MAJOR_LEN_PN, 21 );
        pmgr.setProperty( CPConstants.TIC_MAJOR_WEIGHT_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MAJOR_MPU_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MINOR_LEN_PN, 11 );
        pmgr.setProperty( CPConstants.TIC_MINOR_WEIGHT_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MINOR_MPU_PN, 5 );
        Root    root    = new Root( plane );
        root.start();
        
        ToPlotPointCommand      toPlotPoint = 
            FIUtils.toPlotPointCommand( plane );
        
        DoubleFunction<Point2D> rose    = t ->
            new Point2D.Double(
                Math.cos( t ) * Math.sin( 4 * t ),
                Math.sin( t ) * Math.sin( 4 * t )                
            );
        ParametricCoordinates   coords  =
            new ParametricCoordinates( rose, 0, 2 * Math.PI, .005 );
        plane.setStreamSupplier( 
            () -> coords.stream()
                .map( toPlotPoint::of )
        );
    }
}
