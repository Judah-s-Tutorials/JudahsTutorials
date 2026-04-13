package com.acmemail.judah.cartesian_plane.sandbox.stream_supplier;

import java.awt.Point;

public class PointCommand implements Command
{
    private final   Point   point;
    
    public PointCommand( int xco, int yco )
    {
        point = new Point( xco, yco );
    }
    
    @Override
    public String toString()
    {
        String  str = "Point command: " + point;
        return str;
    }
}
