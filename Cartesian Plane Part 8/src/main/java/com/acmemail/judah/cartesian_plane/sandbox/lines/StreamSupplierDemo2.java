package com.acmemail.judah.cartesian_plane.sandbox.lines;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

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
        Iterable<PlotCommand>   supplier    = 
            new ParametricCoordinates( plane, circle, 0d, 2 * Math.PI, .05 );
        Stream<PlotCommand>     cmdStream   = 
            StreamSupport.stream( supplier.spliterator(), false );
        plane.setStreamSupplier( () -> cmdStream );
    }
}
