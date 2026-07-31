package com.acmemail.judah.color_primer;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_RENDER_QUALITY;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import javax.swing.JPanel;

import com.acmemail.judah.color_primer.util.Utils;

public class SpectrumRanger extends JPanel
{
    private static final long   serialVersionUID = 1L;
    
    private static final Map<RenderingHints.Key, Object> renderingHints =
        Map.of(
            KEY_ANTIALIASING, VALUE_ANTIALIAS_ON,
            KEY_RENDERING, VALUE_RENDER_QUALITY
        );
    
    private static final double     GLOBE_DIAM  = ColorGlobe.getDiameter();
    private static final double     PADDING     = 10;
    private static final double     SPACING     = GLOBE_DIAM + PADDING;
    private static final double     TWO_PI      = Math.PI * 2;
    private static final Color      BROWN       = new Color( 0xDAA06D );
    private static final Stroke     BAR_STROKE  = new BasicStroke( 3f );
    private static final Color      BAR_COLOR   = Color.BLACK;
    private static final Ellipse2D  colorGlobe  = new Ellipse2D.Double();
    
    private static final SpectrumRanger spectrum    = 
        new SpectrumRanger( 500 );
    private static final SpectrumFrame  root        = 
        new SpectrumFrame( spectrum );
    
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    private Rectangle2D     refRectInner    = new Rectangle2D.Double();
    private Line2D          bar             = new Line2D.Double();
    private FeedbackRect    feedbackRect    = new FeedbackRect();
    
    public static void main( String[] args )
    {
        root.start();
    }
    
    public SpectrumRanger( int diameter )
    {
        Dimension   dim = new Dimension( diameter, diameter );
        setPreferredSize( dim );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        // begin boilerplate
        super.paintComponent( graphics );
        gtx = (Graphics2D)graphics;
        currWidth = getWidth();
        currHeight = getHeight();
        gtx.setRenderingHints( renderingHints );
        gtx.setColor( BROWN );
        gtx.fillRect( 0, 0, currWidth, currHeight );
        
        double  diam        = 
            Math.min( currWidth - 2 * SPACING, currHeight - 2 * SPACING );
        refRectInner.setFrame( SPACING, SPACING, diam, diam );
        double  radius      = diam / 2 - ColorGlobe.getDiameter();
        double  centerXco   = currWidth / 2.;
        double  centerYco   = currHeight / 2.;
        double  incr        = TWO_PI / 5000;
        Line2D  line        = new Line2D.Double();

        double  start       = Math.toRadians( root.getHueLowerValue() );
        double  end         = Math.toRadians( root.getHueUpperValue() );
        float   sat         = root.getSaturation() / 100f;
        float   bright      = root.getBrightness() / 100f;
        for ( double angle = start ; angle < end ; angle += incr  )
        {
            double  xco     = centerXco + radius * Math.cos( angle );
            double  yco     = centerYco - radius * Math.sin( angle );
            line.setLine( centerXco, centerYco, xco, yco );
            float   hue     = (float)(angle / TWO_PI);
            Color   color   = Color.getHSBColor( hue, sat, bright );
            gtx.setColor( color );
            gtx.draw( line );
        }
        drawBar( feedbackRect );
    }
    
    private void drawBar( FeedbackRect feedback )
    {
        double  barAngle    = root.getBarAngle();
        double  radius      = refRectInner.getWidth() / 2;
        double  centerXco   = refRectInner.getCenterX();
        double  centerYco   = refRectInner.getCenterY();
        double  outerXco    = centerXco + radius * Math.cos( barAngle );
        double  outerYco    = centerYco - radius * Math.sin( barAngle );
        bar.setLine( centerXco, centerYco, outerXco, outerYco );
        gtx.setColor( BAR_COLOR );
        gtx.setStroke( BAR_STROKE );
        gtx.draw( bar );
        feedback.draw( gtx, bar );
//        colorGlobe.draw( gtx, bar );
    }
    
    private static class FeedbackRect
    {
        private static final char   degreeSym   = '\u00b0';
        private static final String refStr      = "360" + degreeSym;
        private static final int    padding     = 5;
        private static final float  lineWidth   = 2;
        private static final Stroke stroke      = 
            new BasicStroke( lineWidth );
        private static final Color  bgColor     = Color.WHITE;
        private static final Color  borderColor = Color.BLACK;
        private static final Color  fontColor   = Color.BLACK;
        
        private final Rectangle2D   rect        = new Rectangle2D.Double();

        public void draw( Graphics2D gtx, Line2D line )
        {
            FontRenderContext   frc = gtx.getFontRenderContext();
            Font        font        = gtx.getFont();
            Rectangle2D refRect     = font.getStringBounds( refStr, frc );
            double      rectWidth   = refRect.getWidth() + 2 * padding;
            double      rectHeight  = refRect.getHeight() + 2 * padding;
            
            Rectangle2D lineRect    = line.getBounds2D();
            double      lineCenterX = lineRect.getCenterX();
            double      lineCenterY = lineRect.getCenterY();
            
            double      degrees     = Utils.getDegrees( line );
            long        iDegrees    = Math.round( degrees );
            if ( iDegrees < 0 )
                iDegrees += 360;
            String      sDegrees    = "" + iDegrees + degreeSym;
            
            Rectangle2D sRect       = font.getStringBounds( sDegrees, frc );
            double      strXco      = lineCenterX - sRect.getWidth() / 2;
            double      strYco      = 
                lineCenterY + sRect.getHeight() / 2;// + padding;
            
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
        }
    }
}
