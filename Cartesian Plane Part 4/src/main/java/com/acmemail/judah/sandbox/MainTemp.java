package com.acmemail.judah.sandbox;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.cartesian_plane.Root;

public class MainTemp
{
    public static void main(String[] args)
    {
        CartesianPlaneTemp  canvas  = new CartesianPlaneTemp();
        Root                root    = new Root( canvas );
        List<Point2D>       plot    = new ArrayList<>();
        for ( double xco = -2 ; xco < 2.01 ; xco += .01 )
            plot.add( new Point2D.Double( xco, Math.pow( xco, 2 ) - 2 ) );
        canvas.addPlot( plot );
        root.start();
    }
}
