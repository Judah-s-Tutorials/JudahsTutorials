package com.acmemail.judah.cartesian_plane;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MainTemp
{
    public static void main(String[] args)
    {
        CartesianPlaneTemp  canvas  = new CartesianPlaneTemp();
        Root                root    = new Root( canvas );
        root.start();
        
        List<Point2D>   list    = new ArrayList<>();
        for ( float xco = -1.7f ; xco < 1.71f ; xco += .05f )
        {
            // 2x^2 - 3;
            float   yco = 2 * xco * xco - 3;
            Point2D point   = new Point2D.Float( xco, yco );
            list.add( point );
        }
        canvas.setPlot( list );
        canvas.repaint();
    }
}
