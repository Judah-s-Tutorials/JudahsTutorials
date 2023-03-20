package com.acmemail.judah.cartesian_plane.temp2;

import java.awt.geom.Point2D;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.sandbox.ParamCircle;
import com.acmemail.judah.cartesian_plane.sandbox.ParametricCoordinates;

public class StreamSupplierDemo2
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
        
        ParamCircle             circle      = new ParamCircle( 3 );
        ParametricCoordinates   sequencer   = 
            new ParametricCoordinates( circle, 0d, 2 * Math.PI, .05 );
        Stream<PlotCommand>     cmdStream   = 
            sequencer.stream().map( StreamSupplierDemo2::getCommand );
        plane.setStreamSupplier( () -> cmdStream );
    }
    
    /**
     * Helper method to map a Point2D
     * to a PlotCoordinatesCommand.
     * The main purpose of this method
     * is to reduce the verbosity
     * of the stream generation expression.
     * 
     * @param point the point to map to a PlotCoordinatesCommand
     * 
     * @return  the result of mapping the give point
     */
    private static PlotPointCommand getCommand( Point2D point )
    {
        float   xco     = (float)point.getX();
        float   yco     = (float)point.getY();
        PlotPointCommand  cmd = 
            new PlotPointCommand( plane, xco, yco );
        return cmd;
    }
}
