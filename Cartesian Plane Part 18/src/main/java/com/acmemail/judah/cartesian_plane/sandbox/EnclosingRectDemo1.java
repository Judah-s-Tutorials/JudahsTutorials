package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class EnclosingRectDemo1
{
    private static final String         family      = Font.MONOSPACED;
    private static final int            style       = Font.PLAIN;
    private static final int            width       = 60;
    private static final int            height      = 20;
    private static final int            bgColor     = 0;
    private static final int            fgColor     = 255;
    private static final BufferedImage  image       =
        new BufferedImage( 200, 200, BufferedImage.TYPE_INT_RGB );
    
    public static void main(String[] args)
    {
        display( 10 );
        display( 20 );
    }
    
    private static void display( int size )
    {
        String          str     = "1.00";
        Graphics2D      gtx     = image.createGraphics();
        gtx.setColor( new Color( bgColor ) );
        gtx.fillRect( 0, 0, width, height );
        
        Font            font    = new Font( family, style, size );
        gtx.setFont( font );
        FontMetrics     metrics = gtx.getFontMetrics( font );
        Rectangle2D     rect    = metrics.getStringBounds( str, gtx );
        int             strXco  = (int)(width / 2 - rect.getWidth() / 2);
        int             strYco  = (int)-rect.getY();
        gtx.setColor( new Color( fgColor ) );
        gtx.drawString( str, strXco, strYco );
        
        System.out.printf( "%n%s%n", rect );
        System.out.print( "    ");
        for ( int xco = 0 ; xco < width ; ++xco )
            System.out.printf( "%2d ", xco );
        for ( int yco = 0 ; yco < height ; ++yco )
        {
            System.out.printf( "%n%2d: ", yco );
            for ( int xco = 0 ; xco < width ; ++xco )
            {
                int color = image.getRGB( xco, yco ) & 0xFFFFFF;
                System.out.printf( "%02X ", color );
            }
        }
    }
}
 