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
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private static final int        G_BORDER_WIDTH  = 2;
    private static final Stroke     G_STROKE        = 
        new BasicStroke( G_BORDER_WIDTH );
    private static final Color      G_BORDER_COLOR  = Color.BLACK;
    private static final double     G_DIAMETER      = 20;
    
    private static final Color      BAR_COLOR   = Color.BLACK;
    private static final float      BAR_WIDTH   = 3;
    private final Stroke            BAR_STROKE  = 
        new BasicStroke( BAR_WIDTH );

    private static final double     PADDING     = 5;
    private static final double     SPACING     = G_DIAMETER + PADDING;
    private static final double     TWO_PI      = Math.PI * 2;
    private static final Color      BROWN       = new Color( 0xDAA06D );
    
    private static final SpectrumRanger spectrum    = 
        new SpectrumRanger( 500 );
    private static final SpectrumFrame  root        = 
        new SpectrumFrame( spectrum );
    
    private Graphics2D      gtx;
    private int             currWidth;
    private int             currHeight;
    
    private Ellipse2D       gCircle         = new Ellipse2D.Double();
    private Ellipse2D       refRect         = new Ellipse2D.Double();
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
        addMouseListener( new MouseMonitor() );
        addKeyListener( new KeyMonitor() );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        gtx = (Graphics2D)graphics;
        currWidth = getWidth();
        currHeight = getHeight();
        gtx.setRenderingHints( renderingHints );
        gtx.setColor( BROWN );
        gtx.fillRect( 0, 0, currWidth, currHeight );
        
        double  currDiam = 
            Math.min( currWidth - 2 * SPACING, currHeight - 2 * SPACING );
        refRect.setFrame( SPACING, SPACING, currDiam, currDiam );
        double  radius      = (currDiam - G_DIAMETER) / 2;
        
        // If the window gets too small the radius will turn negative.
        // Complete the paint only if radius is a somewhat reasonable value.
        if ( radius > 10 )
            nonDegeneratePaint( radius );
    }
    
    private void nonDegeneratePaint( double radius )
    {
        double  centerXco   = refRect.getCenterX();
        double  centerYco   = refRect.getCenterY();
        Line2D  line        = new Line2D.Double();

        double  start       = Math.toRadians( root.getHueLowerValue() );
        double  end         = Math.toRadians( root.getHueUpperValue() );
        float   sat         = root.getSaturation() / 100f;
        float   bright      = root.getBrightness() / 100f;
        
        double  incr        = 1 / (2 * radius);
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
        drawGlobe( gtx, bar );
    }
    
    private void drawBar( FeedbackRect feedback )
    {
        Color   saveColor   = gtx.getColor();
        Stroke  saveStroke  = gtx.getStroke();
        
        double  barAngle    = Math.toRadians( root.getBarAngle() );
        double  radius      = refRect.getWidth() / 2;
        double  centerXco   = refRect.getCenterX();
        double  centerYco   = refRect.getCenterY();
        double  outerXco    = centerXco + radius * Math.cos( barAngle );
        double  outerYco    = centerYco - radius * Math.sin( barAngle );
        bar.setLine( centerXco, centerYco, outerXco, outerYco );
        gtx.setColor( BAR_COLOR );
        gtx.setStroke( BAR_STROKE );
        gtx.draw( bar );
        feedback.draw( gtx, bar );

        gtx.setColor( saveColor );
        gtx.setStroke( saveStroke );
    }
    
    private void drawGlobe( Graphics2D gtx, Line2D line )
    {
        Color   origColor   = gtx.getColor();
        Stroke  origStroke  = gtx.getStroke();
        
        double  xco         = line.getX2() - G_DIAMETER / 2;
        double  yco         = line.getY2() - G_DIAMETER / 2;
        gCircle.setFrame( xco, yco, G_DIAMETER, G_DIAMETER );
        
        double  angleNorm   = Utils.getAngle( line ) / TWO_PI;
        Color   hue         = Color.getHSBColor( (float)angleNorm, 1, 1 );
        gtx.setColor( hue );
        gtx.fill( gCircle );
        gtx.setStroke( G_STROKE );
        gtx.setColor( G_BORDER_COLOR );
        gtx.draw( gCircle );
        
        gtx.setColor( origColor );
        gtx.setStroke( origStroke );
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
    
    private class MouseMonitor extends MouseAdapter
    {
        @Override
        public void mousePressed( MouseEvent evt )
        {
            spectrum.requestFocusInWindow();
        }
        
        @Override
        public void mouseClicked( MouseEvent evt )
        {
            int     button  = evt.getButton();
            Point   point   = evt.getPoint();
            if ( button == MouseEvent.BUTTON1 && refRect.contains( point ) )
            {
                double  centerXco   = refRect.getCenterX();
                double  centerYco   = refRect.getCenterY();
                Point2D center      = 
                    new Point2D.Double( centerXco, centerYco );
                double  radians     = Utils.getAngle( center, point );
                root.setBarAngle( Math.toDegrees( radians ) );
                spectrum.repaint();
            }
        }
    }
    
    private class KeyMonitor extends KeyAdapter
    {
        @Override
        public void keyPressed( KeyEvent evt )
        {
            final int       UP          = KeyEvent.VK_UP;
            final int       DOWN        = KeyEvent.VK_DOWN;
            final int       LEFT        = KeyEvent.VK_LEFT;
            final int       RIGHT       = KeyEvent.VK_RIGHT;
            final double    DEF_INCR    = 1;
            
            double  incr    = 0;
            int     keyCode = evt.getKeyCode();
            if ( keyCode == UP || keyCode == LEFT )
                incr = DEF_INCR;
            else if ( keyCode == DOWN || keyCode == RIGHT )
                incr = -DEF_INCR;
            else
                incr = 0;
            if ( incr != 0 )
            {
                double  barAngle    = root.getBarAngle();
                barAngle += incr;
                if ( barAngle < 0 )
                    barAngle += 360;
                else if ( barAngle > 360 )
                    barAngle -= 360;
                else
                    ;
                root.setBarAngle( barAngle );
                spectrum.repaint();
            }
        }
    }
}
