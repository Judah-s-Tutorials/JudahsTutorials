package com.acmemail.judah.cartesian_plane.sandbox.lines;

import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class HLine extends DLine
{
    private static final int    topMargin   = 8;
    
    public HLine( int xco, int yco, int length, int width )
    {
        super( xco, yco, xco + length, yco, width );
    }
    
    public Point getTextCoords( TextLayout layout, int quad )
    {
        Rectangle2D bounds  = layout.getBounds();
        Line2D      line    = getLine();
        float       height  = (float)Math.ceil( bounds.getHeight() );
        int         xco     = (int)line.getX1();
        int         yco     = (int)Math.ceil(line.getY1() + topMargin + height);
        
        if ( quad > 1 )
            yco += Math.ceil( topMargin + height);
        if ( quad == 1 || quad == 3 )
            xco = (int)(line.getX2() - bounds.getWidth());
        
        Point       point   = new Point( xco, yco );
        return point;
    }
    
    @Override
    public Point getWidthCoords( TextLayout layout )
    {
        Rectangle2D bounds      = layout.getBounds();
        int         width       = (int)bounds.getWidth();
        Line2D      line        = getLine();
        int         xco1        = (int)line.getX1();
        int         xco2        = (int)line.getX2();
        int         halfLineLen = (xco2 - xco1) / 2;
        int         xco         = xco1 + halfLineLen - width / 2; 
        int         yco     = (int)Math.ceil(line.getY1() + 3 * topMargin);
        
        Point       point   = new Point( xco, yco );
        return point;
    }

    @Override
    public int getFirstX()
    {
        return (int)getLine().getX1();
    }

    @Override
    public int getLastX()
    {
        return (int)getLine().getX2() - 1;
    }
}
