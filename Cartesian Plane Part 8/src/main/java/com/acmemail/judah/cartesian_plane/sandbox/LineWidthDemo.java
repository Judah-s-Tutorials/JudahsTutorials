package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * This class encapsulates logic to instantiate
 * and display a CartesianPlane.
 *
 * @author Jack Straub
 */
public class LineWidthDemo
{
    private final Color         bgColor         = new Color( .9f, .9f, .9f );
    private final Color         fgColor         = Color.BLACK;
    private final int           canvasWidth     = 1000;
    private final int           canvasHeight    = 700;
    private final List<HLine>   hLines  = new ArrayList<>();
    private final BufferedImage offline = 
        new BufferedImage( 
            canvasWidth, 
            canvasHeight, 
            BufferedImage.TYPE_INT_ARGB
        );
    
    private static final int    hLineXco        = 25;
    private static final int    hLineYco        = 10;
    private static final int    hLineLength     = 250;
    private static final int    hLineSpacing    = 100;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        new LineWidthDemo().exec();
    }
    
    private void exec()
    {
        Canvas          canvas  = new Canvas();
        Root            root    = new Root( canvas );
        root.start();
    }
    
    private class Canvas extends JPanel
    {
        private int                 currWidth;
        private int                 currHeight;
        private Graphics2D          gtx;
        private Font                font;
        private FontRenderContext   frc;
        
        public Canvas()
        {
            Dimension   size    = new Dimension( canvasWidth, canvasHeight );
            setPreferredSize( size );
            
            IntStream.range( 0, 5 ).forEach( 
                inx -> hLines.add( getHLine( inx ) )
            );
            paintComponentBI( (Graphics2D)offline.getGraphics() );
        }
        
        public void paintComponent( Graphics graphics )
        {
            // begin boilerplate
            super.paintComponent( graphics );
            currWidth = getWidth();
            currHeight = getHeight();
            gtx = (Graphics2D)graphics.create();
            gtx.setColor( bgColor );
            gtx.fillRect( 0,  0, currWidth, currHeight );
            // end boilerplate
            
            font = gtx.getFont();
            frc = gtx.getFontRenderContext();
            gtx.setColor( fgColor );
            gtx.drawImage( offline, 0, 0, null );
            
            hLines.forEach( l -> tagHLine( l.getLine() ) );
                        
            // begin boilerplate
            gtx.dispose();
            // end boilerplate
        }
        
        public void paintComponentBI( Graphics2D gtx )
        {
            currWidth = getWidth();
            currHeight = getHeight();
            gtx.setColor( bgColor );
            gtx.fillRect( 0,  0, currWidth, currHeight );

            gtx.setColor( fgColor );
            hLines.forEach( l -> l.draw( gtx ) );
        }
        
        private void tagHLine( Line2D line )
        {
            final int   fgRGB= fgColor.getRGB();
            
            int     xco1    = (int)line.getX1();
            int     xco2    = (int)line.getX2();
            int     yco     = (int)line.getY1();
            
            int     beforeXco1  = xco1 - 5;
            int     afterXco2   = xco2 + 5;
            
            int     actFirstXco     = -1;
            int     actLastXco      = -1;
           
            for ( int xco = beforeXco1 ; xco < afterXco2 ; ++xco )
            {
                int actFG   = offline.getRGB( xco, yco );
                if ( fgRGB == actFG )
                {
                    if ( actFirstXco < 0 )
                        actFirstXco = xco;
                    actLastXco = xco;
                }
            }
            
            String      expFirstStr     = "exp xco1 = " + xco1;
            String      actFirstStr     = "act xco1 = " + actFirstXco;
            String      expLastStr      = "exp xco2 = " + (xco2 - 1);
            String      actLastStr      = "act xco2 = " + actLastXco;
            
            TextLayout  expFirstLayout  = new TextLayout( expFirstStr, font, frc );
            Rectangle2D expFirstRect    = expFirstLayout.getBounds();
            
            TextLayout  actFirstLayout  = new TextLayout( actFirstStr, font, frc );
            Rectangle2D actFirstRect    = actFirstLayout.getBounds();
            
            TextLayout  expLastLayout   = new TextLayout( expLastStr, font, frc );
            Rectangle2D expLastRect     = expLastLayout.getBounds();
            
            TextLayout  actLastLayout   = new TextLayout( actLastStr, font, frc );
            Rectangle2D actLastRect     = actLastLayout.getBounds();
            
            int         textHeight      = (int)expFirstRect.getHeight();
            expFirstLayout.draw( gtx, xco1, yco + textHeight );
            actFirstLayout.draw( gtx, xco1, yco + 2 * textHeight );
            
            int         textWidth       = (int)expLastRect.getWidth();
            expLastLayout.draw( gtx, xco2 - textWidth, yco + textHeight );
            
            textWidth = (int)actLastRect.getWidth();
            actLastLayout.draw( gtx, xco2 - textWidth, yco + 2 * textHeight );
        }
    }
    
    private HLine getHLine( int inx )
    {
        HLine   line    =
            new HLine(
                hLineXco,
                hLineYco + inx * hLineSpacing,
                hLineLength,
                inx + 1
            );
        return line;
    }
    
    private class HLine
    {
        private final Line2D    line    = new Line2D.Float();
        private final Stroke    stroke;
        
        public HLine( int xco, int yco, int length, int width )
        {
            line.setLine(
                xco,
                yco,
                xco + length,
                yco
            );
            stroke = new BasicStroke( 
                width,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                1f
            );
        }
        
        public void draw( Graphics2D gtx )
        {
            gtx.setStroke( stroke );
            gtx.draw( line );
        }
        
        public Line2D getLine()
        {
            return line;
        }
    }
}
