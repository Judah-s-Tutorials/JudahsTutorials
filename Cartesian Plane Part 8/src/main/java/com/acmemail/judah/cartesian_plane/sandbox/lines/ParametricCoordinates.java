package com.acmemail.judah.cartesian_plane.sandbox.lines;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.DoubleFunction;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotCoordinatesCommand;

public class ParametricCoordinates implements Iterable<PlotCommand>
{
    private final DoubleFunction<Point2D>   funk;
    private final CartesianPlane            plane;
    private final double    first;
    private final double    last;
    private final double    incr;
    
    public ParametricCoordinates(
        CartesianPlane plane,
        DoubleFunction<Point2D> oper, 
        double first, 
        double last, 
        double incr 
    )
    {
        this.plane = plane;
        this.funk = oper;
        this.first = first;
        this.last = last;
        this.incr = incr;
    }
    
    @Override
    public Iterator<PlotCommand> iterator()
    {
        // TODO Auto-generated method stub
        return new CommandIterator();
    }

    private class CommandIterator implements Iterator<PlotCommand>
    {
        private double  control;
        
        public CommandIterator()
        {
            control = first;
        }
        
        @Override
        public boolean hasNext()
        {
            return control <= last;
        }
        
        @Override
        public PlotCommand next()
        {
            PlotCommand cmd = null;
            if ( control > last )
            {
                throw new NoSuchElementException();
            }
            Point2D point   = funk.apply( control );
            cmd = new PlotCoordinatesCommand(
                plane,
                (float)point.getX(),
                (float)point.getY()
            );
            control += incr;
            return cmd;
        }   
    }
}
