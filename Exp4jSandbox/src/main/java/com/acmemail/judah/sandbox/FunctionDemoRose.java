package com.acmemail.judah.sandbox;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.function.Function;

public class FunctionDemoRose
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
        pmgr.setProperty( CPConstants.GRID_UNIT_PN, 50 );
        Root    root    = new Root( plane );
        root.start();
        
        Function    roseX   = new Function( "rosex", 3 ) {
            @Override
            public double apply( double... args )
            {
                double  t = args[0];
                double  a = args[1];
                double  b = args[2];
                
                double  x = a * Math.cos( b * t ) * Math.cos( t );
                return x;
            }
        };
        
        Function    roseY   = new Function( "rosey", 3 ) {
            @Override
            public double apply( double... args )
            {
                double  t = args[0];
                double  a = args[1];
                double  b = args[2];
                
                double  y =a * Math.cos( b * t ) * Math.sin( t );
                return y;
            }
        };
        
        
//        plane.setStreamSupplier( () ->
//            Stream.concat( Stream.of( shape, color), plotStream() )
//        );
        NotificationManager.INSTANCE.propagateNotification( CPConstants.REDRAW_NP );
    }
}
