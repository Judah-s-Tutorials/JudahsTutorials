package com.acmemail.judah.cartesian_plane.temp2;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleFunction;
import java.util.stream.StreamSupport;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.sandbox.ParamCircle;

public class StreamSupplierDemo1
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
        
        ParamCircle                 circle      = new ParamCircle( 3 );
        Iterator<PlotCommand>       supplier    = 
            new CommandIterator( circle, 0d, 2 * Math.PI, .05 );
        Spliterator<PlotCommand>    splitter    =
            Spliterators.spliteratorUnknownSize( supplier, 0);
        plane.setStreamSupplier( 
            () -> StreamSupport.stream( splitter, false )
        );
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
            cmd = new PlotPointCommand(
                plane,
                (float)point.getX(),
                (float)point.getY()
            );
            angle += incr;
            return cmd;
        }   
    }
}
