package com.acmemail.judah.cartesian_plane.sandbox.lines;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.DoubleFunction;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotCoordinatesCommand;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

public class ParamIteratorDemo
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
        
        ParamCircle circle  = new ParamCircle( 3 );
        plane.setIterator( new CommandIterator( circle, 0, 2 * Math.PI, .05 ) );
    }

    private static class CommandIterator implements Iterator<PlotCommand>
    {
        private final DoubleFunction<Point2D>   funk;
        private final double                    last;
        private final double                    incr;
        private double                          angle;
        
        public CommandIterator( 
            DoubleFunction<Point2D> oper, 
            double first, 
            double last, 
            double incr 
        )
        {
            this.funk = oper;
            this.last = last;
            this.incr = incr;
            this.angle = first;
        }
        
        @Override
        public boolean hasNext()
        {
            return angle <= last;
        }
        
        @Override
        public PlotCommand next()
        {
            PlotCommand cmd = null;
            if ( angle > last )
            {
                throw new NoSuchElementException();
            }
            Point2D point   = funk.apply( angle );
            cmd = new PlotCoordinatesCommand(
                plane,
                (float)point.getX(),
                (float)point.getY()
            );
            angle += incr;
            return cmd;
        }   
    }
}
