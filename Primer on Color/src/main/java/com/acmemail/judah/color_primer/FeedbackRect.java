package com.acmemail.judah.color_primer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class FeedbackRect
{
    private static final char   degreeSym   = '\u00b0';
    private static final String baseStr     = "360" + degreeSym;
    private static final int    padding     = 5;
    private static final float  lineWidth   = 2;
    private static final Stroke stroke      = new BasicStroke( lineWidth );
    private static final Color  bgColor     = Color.WHITE;
    private static final Color  borderColor = Color.BLACK;
    private static final Color  fontColor   = Color.BLACK;
    
    private final Rectangle2D   rect        = new Rectangle2D.Double();
    private Font                font;
    private FontRenderContext   frc;
    
    public FeedbackRect( Graphics2D gtx )
    {
        reconfig( gtx );
    }
    
    public void draw( Graphics2D gtx, LineExt line )
    {
        Stroke      origStroke  = gtx.getStroke();
        Font        origFont    = gtx.getFont();
        Color       origColor   = gtx.getColor();
        Rectangle2D lineRect    = line.getBounds2D();
        double      lineCenterX = lineRect.getCenterX();
        double      lineCenterY = lineRect.getCenterY();
        double      rectWidth   = rect.getWidth();
        double      rectHeight  = rect.getHeight();
        
        double      degrees     = line.getDegrees();
        long        iDegrees    = Math.round( degrees );
        if ( iDegrees < 0 )
            iDegrees += 360;
        String      sDegrees    = "" + iDegrees + degreeSym;
        
        Rectangle2D sRect       = font.getStringBounds( sDegrees, frc );
        double      strXco      = lineCenterX - sRect.getWidth() / 2;
        double      strYco      = lineCenterY + rect.getHeight() / 2 - padding;
        
        double      rectXco     = lineCenterX - rectWidth / 2;
        double      rectYco     = lineCenterY - rectHeight / 2;
        rect.setRect( rectXco, rectYco, rectWidth, rectHeight );
        
        gtx.setStroke( stroke );
        gtx.setFont( font );
        gtx.setColor( bgColor );
        gtx.fill( rect );
        gtx.setColor( borderColor );
        gtx.draw( rect );
        gtx.setColor( fontColor );
        gtx.drawString( sDegrees, (float)strXco, (float)strYco);
        
        gtx.setColor( origColor );
        gtx.setStroke( origStroke );
        gtx.setFont( origFont );
    }
    
    public void reconfig( Graphics2D gtx )
    {
        font = gtx.getFont();
        frc = gtx.getFontRenderContext();
        Rectangle2D baseBounds  = font.getStringBounds( baseStr, frc );
        double      baseWidth   = baseBounds.getWidth() + 2 * padding;
        double      baseHeight  = baseBounds.getHeight() + 2 * padding;
        rect.setRect( 0, 0, baseWidth, baseHeight );
    }
}
