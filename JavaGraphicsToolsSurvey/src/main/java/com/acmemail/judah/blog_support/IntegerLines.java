package com.acmemail.judah.blog_support;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class IntegerLines extends JPanel
{
    private final Color     bgColor     = new Color( .9f, .9f, .9f );
    
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    private FontMetrics     fontMetrics;
    
    public IntegerLines( int width, int height )
    {
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        // begin boilerplate
        super.paintComponent( graphics );
        currWidth = getWidth();
        currHeight = getHeight();
        gtx = (Graphics2D)graphics.create();
        fontMetrics = gtx.getFontMetrics();
        gtx.setColor( bgColor );
        gtx.fillRect( 0,  0, currWidth, currHeight );
        // end boilerplate
        
        // draw a line from lower left portion of window 
        // to upper right portion.
        gtx.setColor( Color.black );
        int xco1    = (int)(currWidth * .25);
        int yco1    = (int)(currHeight * .75);
        int xco2    = (int)(currWidth * .75);
        int yco2    = (int)(currHeight * .25);
        gtx.drawLine(xco1, yco1, xco2, yco2);
        drawCoordinates( xco1, yco1, false );
        drawCoordinates( xco2, yco2, true );
        
        String  comment = 
            String.format( 
                "gtx.drawLine(%d,%d,%d,%d)", 
                xco1, yco1, xco2, yco2
            );
        Rectangle2D rect    = fontMetrics.getStringBounds(comment, gtx);
        int strXco  = xco1;
        int strYco  = (int)(yco2 - 2 * rect.getHeight());
        gtx.drawString( comment, strXco, strYco );

        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    private void drawCoordinates( int xco, int yco, boolean above )
    {
        String      strCoords   = String.format( "(x=%d,y=%d)", xco, yco );
        Rectangle2D strRect     = 
            fontMetrics.getStringBounds( strCoords, gtx );
        float       strXco      = (float)(xco - strRect.getWidth() / 2);
        float       strYco      = 
            above? yco : (float)(yco + strRect.getHeight());
        gtx.drawString(strCoords, strXco, strYco);
    }
}
