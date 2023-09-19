package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class BufferedImageDemo1
{
    private static final int    margin  = 10;
    private static final Color  bgColor = Color.YELLOW;
    
    private final int           width;
    private final int           height;
    private final BufferedImage image;
    private final Graphics2D    gtx;
    
    public BufferedImageDemo1( int widthIn, int heightIn )
    {   
        this.width = widthIn + 2 * margin;
        this.height = heightIn + 2 * margin;
        image = new BufferedImage( 
            width, 
            height, 
            BufferedImage.TYPE_INT_ARGB 
        );
        gtx = image.createGraphics();
        gtx.fillRect( 0, 0, width, height );
        
        gtx.setStroke( new BasicStroke( 5 ) );
        gtx.setColor( Color.BLUE );
        gtx.drawLine( margin, margin, width - margin, height - margin );
        gtx.drawLine( width - margin, margin, margin, height - margin );
        
        gtx.setStroke( new BasicStroke( 3 ) );
        gtx.setColor( Color.GREEN );
        gtx.drawLine( width / 2, margin, width / 2, height - margin );
        gtx.drawLine( margin, height / 2, width - margin, height / 2 );
        
        Rectangle   rect    = 
            new Rectangle( 0, 0, width / 3, height / 3 );
        centerRect( rect );
        gtx.setColor( Color.MAGENTA );
        gtx.fill( rect );
        
        Rectangle   oval    = 
            new Rectangle( 0, 0, width /  4, height /  4 );
        centerRect( oval );
        gtx.setColor( Color.YELLOW );
        gtx.fillOval( oval.x, oval.y, oval.width, oval.height );
        
        gtx.dispose();
    }
    
    public BufferedImage getImage()
    {
        return image;
    }
    
    private void centerRect( Rectangle rect )
    {
        int     rWidth  = rect.width;
        int     rHeight = rect.height;
        int     cXco    = width / 2;
        int     cYco    = height / 2;

        int     xco     = cXco - rWidth / 2;
        int     yco     = cYco - rHeight / 2;
        rect.x = xco;
        rect.y = yco;
    }
}
