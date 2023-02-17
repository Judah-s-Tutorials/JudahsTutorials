package com.acmemail.judah.cartesian_plane.sandbox.lines;

import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class VLine extends DLine
{
    private static final int    margin  = 8;
    public VLine( int xco, int yco, int length, int width )
    {
        super( xco, yco, xco, yco + length, width );
    }
    
    public Point getTextCoords( TextLayout layout, int quad )
    {
        Rectangle2D bounds  = layout.getBounds();
        Line2D      line    = getLine();
        float       height  = (float)Math.ceil( bounds.getHeight() );
        float       width   = (float)Math.ceil( bounds.getWidth() );
        int         xco     = (int)line.getX1();
        int         yco     = (int)Math.ceil(line.getY1() + height);
        
        if ( quad == 0 || quad == 2 )
            xco -= Math.ceil( margin + width );
        else
            xco += margin;
        
        if ( quad > 1 )
            yco = (int)line.getY2();
        
        Point       point   = new Point( xco, yco );
        return point;
    }

    @Override
    public Point getWidthCoords( TextLayout layout )
    {
        Rectangle2D bounds      = layout.getBounds();
        Line2D      line        = getLine();
        int         yco1        = (int)line.getY1();
        int         halfLineLen = (int)(line.getY2() - yco1) / 2;
        int         xco         = (int)(line.getX1() - margin - bounds.getWidth());
        int         yco         = yco1 + halfLineLen;
        
        Point       point   = new Point( xco, yco );
        return point;
    }

    @Override
    public int getFirstX()
    {
        int xco = (int)(getLine().getX1()) - getWidth() / 2;
        return xco;
    }

    @Override
    public int getLastX()
    {
        int xco = (int)(getLine().getX1()) + getWidth() / 2;
        return xco;
    }
}
