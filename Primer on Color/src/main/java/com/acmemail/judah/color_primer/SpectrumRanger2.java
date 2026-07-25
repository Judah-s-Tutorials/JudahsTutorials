package com.acmemail.judah.color_primer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import javax.swing.JPanel;

public class SpectrumRanger2 extends JPanel
{
    private static final long   serialVersionUID = 1L;
    
    private static final Map<RenderingHints.Key, Object> renderingHints =
        Map.of(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
            RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY
        );
    
    private static final double TWO_PI      = Math.PI * 2;
    private static final Color  BROWN       = new Color( 0xDAA06D );
    private static final double BORDER      = 10;
    private static final Stroke BAR_STROKE  = new BasicStroke( 3f );
    private static final Color  BAR_COLOR   = Color.BLACK;
    
    private static final SpectrumRanger2 spectrum    = 
        new SpectrumRanger2( 500 );
    private static final SpectrumFrame2  root        = 
        new SpectrumFrame2( spectrum );
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    private Rectangle2D     refRectInner    = new Rectangle2D.Double();
    private Line2D          bar             = new Line2D.Double();
    
    public static void main( String[] args )
    {
        root.start();
    }
    
    public SpectrumRanger2( int diameter )
    {
        Dimension   dim = new Dimension( diameter, diameter );
        setPreferredSize( dim );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        // begin boilerplate
        super.paintComponent( graphics );
        currWidth = getWidth();
        currHeight = getHeight();
        gtx = (Graphics2D)graphics;
        gtx.setRenderingHints( renderingHints );
        gtx.setColor( BROWN );
        gtx.fillRect( 0, 0, currWidth, currHeight );
        
        double  diam        = 
            Math.min( currWidth - 2 * BORDER, currHeight - 2 * BORDER );
        refRectInner.setFrame( BORDER, BORDER, diam, diam );
        double  radius      = diam / 2;
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
        drawBar();
    }
    
    private void drawBar()
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
    }
}
