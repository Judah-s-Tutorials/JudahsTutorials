package com.acmemail.judah.cartesian_plane.sandbox;

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

/**
 * Simple application that demonstrates
 * how to produce a stream from an iterator.
 * 
 * @author Jack Straub
 */
public class ParamIteratorDemo
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
        
        ParamCircle                 circle      = new ParamCircle( 3 );
        plane.setStreamSupplier( 
            () -> {
                Iterator<PlotCommand>       iter        = 
                    new CommandIterator( circle, 0, 2 * Math.PI, .05 );
                Spliterator<PlotCommand>    splitter    = 
                    Spliterators.spliteratorUnknownSize( iter, 0 );
                return StreamSupport.stream( splitter, false );
            }
        );
    }

    /**
     * Implementation of an Iterator<PlotCommand>.
     * Iterates over a sequence of doubles
     * producing a result by applying
     * a given <em>DoubleFunction&lt;Point2D&gt;
     * functional interface.
     * 
     * @author Jack Straub
     */
    private static class CommandIterator implements Iterator<PlotCommand>
    {
        private final DoubleFunction<Point2D>   funk;
        private final double                    last;
        private final double                    incr;
        private double                          angle;
        
        /**
         * Constructor.
         * Establishes the range to iterate over
         * and the given  <em>DoubleFunction&lt;Point2D&gt;
         * functional interface.
         * 
         * @param funk      the given functional interface
         * @param first     the first element in the sequence (inclusive)
         * @param last      the last element in the sequence (inclusive)
         * @param incr      the increment used to produce the "next"
         *                  element in the sequence
         */
        public CommandIterator( 
            DoubleFunction<Point2D> funk, 
            double first, 
            double last, 
            double incr 
        )
        {
            this.funk = funk;
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
