package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;
import java.util.function.DoubleFunction;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

public class ParametricCoordinatesDemo
{
    private static final CartesianPlane plane   = new CartesianPlane();
    
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
        
        DoubleFunction<Point2D> rose    = t ->
            new Point2D.Double(
                Math.cos( t ) * Math.sin( 4 * t ),
                Math.sin( t ) * Math.sin( 4 * t )                
            );
        ParametricCoordinates   coords  =
            new ParametricCoordinates( rose, 0, 2 * Math.PI, .005 );
        plane.setStreamSupplier( 
            () -> coords.stream()
                .map( ParametricCoordinatesDemo::toPointCommand )
        );
    }
    
    /**
     * Helper method to make a PlotPointCommand object
     * from a given Point2D.
     * 
     * @param point the given Point2D
     * 
     * @return  the derived PlotPointCommand
     */
    private static PlotPointCommand toPointCommand( Point2D point )
    {
        float   xco = (float)point.getX();
        float   yco = (float)point.getY();
        PlotPointCommand    cmd = new PlotPointCommand( plane, xco, yco );
        return cmd;
    }
}
