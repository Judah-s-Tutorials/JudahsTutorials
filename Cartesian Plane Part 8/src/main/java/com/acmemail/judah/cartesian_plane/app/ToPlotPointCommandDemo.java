package com.acmemail.judah.cartesian_plane.app;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.app.FIUtils.ToPlotPointCommand;

public class ToPlotPointCommandDemo
{
    public static void main(String[] args)
    {
        Point2D             points[]        = 
            { new Point( 1, 2 ), new Point( 3, 4 ), new Point( 5, 6 ) };
        List<Point2D>       list            = Arrays.asList( points );
        
        CartesianPlane      plane           = null;
        ToPlotPointCommand  toPlotPointCmd  = 
            FIUtils.toPlotPointCommand( plane );
        list.stream()
            .map( toPlotPointCmd::of )
            .forEach( System.out::println );
    }
}
