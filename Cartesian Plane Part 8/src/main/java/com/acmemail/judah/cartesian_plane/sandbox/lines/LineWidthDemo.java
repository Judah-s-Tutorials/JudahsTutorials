package com.acmemail.judah.cartesian_plane.sandbox.lines;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
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
    private final List<DLine>   dLines  = new ArrayList<>();
    private final BufferedImage offline = 
        new BufferedImage( 
            canvasWidth, 
            canvasHeight, 
            BufferedImage.TYPE_INT_ARGB
        );
    
    private static final int    hLineXco        = 32;
    private static final int    hLineYco        = 16;
    private static final int    hLineLength     = 256;
    private static final int    hLineSpacing    = 96;
    
    private static final int    vLineXco        = hLineXco + hLineLength + 128;
    private static final int    vLineYco        = 16;
    private static final int    vLineLength     = 64;
    private static final int    vLineSpacing    = vLineLength + 32;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String[]    parts   = System.getProperty( "java.class.path" ).split(";" );
        for ( String part : parts )
            System.out.println( part );
        new LineWidthDemo().exec();
    }
    
    private void exec()
    {
        Canvas          canvas  = new Canvas();
        Root            root    = new Root( canvas );
        root.start();
    }
    
    @SuppressWarnings("serial")
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
                inx -> dLines.add( getHLine( inx ) )
            );
            IntStream.range( 0, 5 ).forEach( 
                inx -> dLines.add( getVLine( inx ) )
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
            
            dLines.forEach( this::tagLine );
                        
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
            dLines.forEach( l -> l.draw( gtx ) );
            
            gtx.setStroke( new BasicStroke( 1 ) );
            int xco = hLineXco + hLineLength - 1;
            int yco = 5 * hLineSpacing;
            gtx.drawLine( xco, 0, xco, yco );
        }
        
        private void tagLine( DLine dLine )
        {
            final int   fgRGB= fgColor.getRGB();
            
            Line2D  line        = dLine.getLine();
            int     xco1        = (int)line.getX1();
            int     xco2        = (int)line.getX2();
            int     centerXco   = xco1 + (xco2 - xco1) / 2;
            int     yco         = (int)line.getY1();

            int actFirstXco = centerXco;
            for ( int xco = centerXco ; 
                  fgRGB == offline.getRGB( xco, yco ) ; 
                  --xco
                )
                actFirstXco = xco;
            
            int actLastXco = actFirstXco;
            for ( int xco = actFirstXco ; 
                fgRGB == offline.getRGB( xco, yco ) ; 
                ++xco
              )
              actLastXco = xco;
            
            String[]    labels          = 
            {
                "exp first x = " + dLine.getFirstX(),
                "exp last x = " + dLine.getLastX(),
                "act first x = " + actFirstXco,
                "act last x = " + actLastXco
            };
            
            int pos = 0;
            for ( String label : labels )
            {
                TextLayout  layout  = new TextLayout( label, font, frc );
                Point       point   = dLine.getTextCoords( layout, pos++ );
                layout.draw( gtx, point.x, point.y );
            }
            
            String      label   = "width = " + dLine.getWidth();
            TextLayout  layout  = new TextLayout( label, font, frc );
            Point       point   = dLine.getWidthCoords( layout );
            layout.draw( gtx, point.x, point.y );
        }
    }
    
    private HLine getHLine( int inx )
    {
        HLine   line    =
            new HLine(
                hLineXco,
                hLineYco + inx * hLineSpacing,
                hLineLength,
                2 * inx + 1
            );
        return line;
    }
    
    private VLine getVLine( int inx )
    {
        VLine   line    =
            new VLine(
                vLineXco,
                vLineYco + inx * vLineSpacing,
                vLineLength,
                2 * inx + 1
            );
        return line;
    }
}
