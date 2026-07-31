package com.acmemail.judah.color_primer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class ColorGlobe
{
    private static final double twoPI       = Math.PI * 2;
    private static final int    borderWidth = 2;
    private static final Stroke stroke      = new BasicStroke( borderWidth );
    private static final Color  borderColor = Color.BLACK;
    private static final double diameter    = 20;
    
    private final Ellipse2D     circle      = new Ellipse2D.Double();
    
    public void draw( Graphics2D gtx, LineExt line )
    {
        Color   origColor   = gtx.getColor();
        Stroke  origStroke  = gtx.getStroke();
        
        Line2D  line2D      = line.getLine2D();
        double  xco         = line2D.getX2() - diameter / 2;
        double  yco         = line2D.getY2() - diameter / 2;
        circle.setFrame( xco, yco, diameter, diameter );
        
        double  angleNorm   = line.getAngle() / twoPI;
        Color   hue         = Color.getHSBColor( (float)angleNorm, 1, 1 );
        gtx.setColor( hue );
        gtx.fill( circle );
        gtx.setStroke( stroke );
        gtx.setColor( borderColor );
        gtx.draw( circle );
        
        gtx.setColor( origColor );
        gtx.setStroke( origStroke );
    }
    
    public static double getDiameter()
    {
        return diameter;
    }
}
