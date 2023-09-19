package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

public class GreekTile
{
    private static final Color  background      = new Color( 0xcccccc );
    private static final char   thinSpace       = '\u2009';
    private static final char   alpha           = '\u0391';
    private static final char   pie             = '\u03A0';
    private static final char   delta           = '\u0394';
    private static final char   eta             = '\u0397';
    private static final char   omega           = '\u03A9';
    private static final char   sigma           = '\u03a3';
    private static final char   psi             = '\u03a8';
    private static final char   gamma           = '\u0393';
    private static final char   tau             = '\u03a4';
    private static final char   theta           = '\u0398';
    private static final char   phi             = '\u03a6';
    private static final char   epsilon         = '\u0395';
    private static final char   zeta            = '\u0396';
    private static final char   lambda          = '\u039B';
    private static final char   rho             = '\u039E';
    private static final char   xii             = '\u03A1';
    private static final char   beta            = '\u0392';
    private static final int    imageType       = 
        BufferedImage.TYPE_INT_ARGB;
    private static final int    tileMargin      = 0;
    private static final String tileFontName    = "Helvetica";
    private static final int    tileFontStyle   = Font.ITALIC;
    private static final int    tileFontSize    = 40;
    private static final Color  tileFontColor   = Color.YELLOW;
    private static final String tileString;
    static
    {
        Character[]  chars   =
        { 
            alpha, pie, delta, theta, omega, phi, gamma, sigma, 
            psi, tau, epsilon, zeta, eta, lambda, xii, beta, rho
        };
        StringBuilder   bldr    = new StringBuilder();
        Arrays.stream( chars )
            .peek( bldr::append )
            .forEach( c -> bldr.append( thinSpace ) );
        tileString = bldr.toString();
    }
    
    private final BufferedImage tile;
    private final int           randyMax;
    private Random              randy;

    public GreekTile()
    {
        randy = new Random( 0 );
        
        // This is just temporary BufferedImage which is used to
        // obtain a graphics context to calculate font metrics.
        BufferedImage   temp    = new BufferedImage( 10, 10, imageType );
        Graphics2D      gtx     = temp.createGraphics();
        
        Font            font        = 
            new Font( tileFontName, tileFontStyle, tileFontSize );
        FontMetrics     metrics     = gtx.getFontMetrics( font );
        Rectangle2D     strDim      = 
            metrics.getStringBounds( tileString, gtx );
        gtx.dispose();
        
        int             rectWidth   = (int)(strDim.getWidth() + .5);
        int             rectHeight  = (int)(strDim.getHeight() + .5);
        int             strAscent   = metrics.getAscent();
        int             strXco      = tileMargin;
        int             strYco      = tileMargin + strAscent;
        int             tileWidth   = rectWidth + tileMargin;
        int             tileHeight  = rectHeight +  tileMargin;
        
        BufferedImage   tile        = 
            new BufferedImage( tileWidth, tileHeight, imageType );
        gtx = tile.createGraphics();
        gtx.setColor( background );
        gtx.fillRect( 0, 0, tileWidth, tileHeight );
        
        gtx.setFont( font );
        gtx.setColor( tileFontColor ); 
        gtx.drawString( tileString, strXco, strYco );
        
        gtx.dispose();
        
        this.tile = tile;
        randyMax = (int)( 3 * tileWidth / 4D + .5);
    }
    
    public BufferedImage getTile()
    {
        return tile;
    }
    
    public void resetTileOffset()
    {
        randy = new Random( 0 );
    }
    
    public int getTileOffset()
    {
        int offset  = randy.nextInt( randyMax );
        return offset;
    }
}
