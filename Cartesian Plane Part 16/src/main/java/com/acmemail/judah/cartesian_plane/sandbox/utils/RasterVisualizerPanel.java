package com.acmemail.judah.cartesian_plane.sandbox.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RasterVisualizerPanel extends JPanel
{
    private static final Color      bgColor     = Color.LIGHT_GRAY;
    private static final int        pixWidth    = 2;
    private static final int        pixHeight   = pixWidth;
    private static final int        pixColGap   = 2;
    private static final int        pixRowGap   = pixColGap;
    
    private final BufferedImage raster;
    private final int           rasterWidth;
    private final int           rasterHeight;
    private final int           prefWidth;
    private final int           prefHeight;
    
    public RasterVisualizerPanel( BufferedImage image )
    {
        raster = image;
        rasterWidth = raster.getWidth();
        rasterHeight = raster.getHeight();
        
        // Given: there is a gap at each side of the rectangle
        // 3 cols (or row) gives width of | x x x | 
        //      3 pixWidth + 4 pixColGaps
        prefWidth = 
            rasterWidth * pixWidth + (rasterWidth + 1)* pixColGap;
        prefHeight = 
            rasterHeight * pixHeight + (rasterHeight + 1) * pixRowGap;

    }
    
    @Override
    public Dimension getPreferredSize()
    {
        Dimension   preferredSize = 
            new Dimension( prefWidth, prefHeight );
        return preferredSize;
    }
    
    @Override
    public boolean isOpaque()
    {
        return true;
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        Graphics2D  gtx = (Graphics2D)graphics.create();
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, prefWidth, prefHeight );
//        gtx.drawImage( raster, 0, 0, this );
        
        int yco = pixRowGap;
        for ( int row = 0 ; row < rasterHeight ; ++row )
        {
            int xco = 0;
            for ( int col = 0 ; col < rasterWidth ; ++col )
            {
                int pix = raster.getRGB( col, row ) & 0x00ffffff;
                gtx.setColor( new Color( pix ) );
                gtx.fillRect( xco, yco, pixWidth, pixHeight );
                xco += pixColGap + pixWidth;
            }
            yco += pixRowGap + pixHeight;
        }
    }
}
