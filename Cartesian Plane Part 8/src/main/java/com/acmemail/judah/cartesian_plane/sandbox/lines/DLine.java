package com.acmemail.judah.cartesian_plane.sandbox.lines;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;

public abstract class DLine
{
    private final Line2D    line    = new Line2D.Float();
    private final Stroke    stroke;
    private final int       width;
    
    public abstract Point getTextCoords( TextLayout layout, int quad );
    public abstract Point getWidthCoords( TextLayout layout );
    public abstract int getFirstX();
    public abstract int getLastX();
    
    public DLine( int xco1, int yco1, int xco2, int yco2, int width )
    {
        line.setLine( xco1, yco1, xco2, yco2 );
        this.width = width;
        stroke = new BasicStroke( 
            width,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            1f
        );
    }

    public void draw( Graphics2D gtx )
    {
        gtx.setStroke( stroke );
        gtx.draw( line );
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public Line2D getLine()
    {
        return line;
    }
}
