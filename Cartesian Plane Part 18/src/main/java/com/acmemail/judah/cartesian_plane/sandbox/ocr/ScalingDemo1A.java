package com.acmemail.judah.cartesian_plane.sandbox.ocr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ScalingDemo1A extends JPanel
{
    private static final char   white       = '\u25a1';
    private static final char   black       = '\u25a0';
    private static final char   gray        = '\u25a8';
    private static final int    imageType   = BufferedImage.TYPE_INT_RGB;
    
    private final ScalingDemo1  demo;
    
    private Graphics2D      gtx;
    private int             ascent;
    private BufferedImage   image;
    private int             xMin    = Integer.MAX_VALUE;
    private int             xMax    = 0;;
    public ScalingDemo1A( ScalingDemo1 demo )
    {
        this.demo = demo;
        Dimension   prefSize    = new Dimension( 1000, 1000 );
        setPreferredSize( prefSize );
        
        Font    font    = getFont().deriveFont( 8f );
        setFont( font );
    }
    
    public void paintComponent( Graphics graphics )
    {
        gtx = (Graphics2D)graphics.create();
        gtx.setColor( Color.WHITE );
        gtx.fillRect( 0,  0, getWidth(), getHeight() );
        gtx.setColor( Color.BLACK );
        
        FontMetrics metrics = gtx.getFontMetrics();
        ascent = metrics.getAscent();
        
        int width   = demo.getWidth();
        int height  = demo.getHeight();
        image = new BufferedImage( width, height, imageType );
        demo.paintComponent( image.getGraphics() );
        
        getXBounds();
        for ( int row = 0 ; row < height ; ++row )
            paintRow( row );
    }
    
    private void paintRow( int num )
    {
        StringBuilder   bldr    = new StringBuilder();
        for ( int inx = xMin ; inx < image.getWidth()    ; ++inx )
        {
            int     pixel   = image.getRGB( inx, num ) & 0xFFFFFF;
            char    square  = 0;
            if ( pixel == 0xffffff )
                square = white;
            else if ( pixel == 0 )
                square = black;
            else
                square = gray;
            bldr.append( square );
        }
        int             yco     = num * ascent;
        gtx.drawString( bldr.toString(), 0, yco );
    }
    
    private void getXBounds()
    {
        int width   = image.getWidth();
        int height  = image.getHeight();
        for ( int yco = 0 ; yco < height ; ++yco )
            for ( int xco = 0 ; xco < width ; ++xco )
            {
                int pixel   = image.getRGB( xco, yco ) & 0xffffff;
                if ( pixel == 0 )
                {
                    if ( xco < xMin )
                        xMin = xco;
                    if ( xco > xMax )
                        xMax = xco;
                }
            }
    }
}
