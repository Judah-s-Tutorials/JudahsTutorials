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

public class SpectrumDial extends JPanel
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
    
    private static final SpectrumDial dial      = 
        new SpectrumDial( 500 );
    private static final DialFrame  root        = 
        new DialFrame( dial );
    
    private Graphics2D      gtx;
    private int             currWidth;
    private int             currHeight;
    private boolean         gCircleSelected;
    
    private Ellipse2D       gCircle         = new Ellipse2D.Double();
    private Ellipse2D       refRect         = new Ellipse2D.Double();
    private Line2D          bar             = new Line2D.Double();
    private FeedbackRect    feedbackRect    = new FeedbackRect();
    
    /**
     * Application entry point.
     *  
     * @param args  command-line arguments, not used
     */
    public static void main( String[] args )
    {
        root.start();
    }
    
    /**
     * Constructor. 
     * Initializes the application window.
     * The preferred diameter for the window dimensions
     * includes the diameter of the dial 
     * and the diameter of the color globe.
     * 
     * @param diameter  the diameter of the 
     */
    public SpectrumDial( int diameter )
    {
        Dimension       dim = new Dimension( diameter, diameter );
        setPreferredSize( dim );
        MouseMonitor    mouseMonitor    = new MouseMonitor();
        addMouseListener( mouseMonitor );
        addMouseMotionListener( mouseMonitor );
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
        
        // Determine the dimensions and position the reference rectangle:
        // 1) the rectangle will be a square, based on min(width,height);
        // 2) the dimensions will not include the padding around the dial;
        // 3) the position will allowing for the padding around the dial.
        double  totalSpacing    = 2 * SPACING;
        double  currDiam = 
            Math.min( currWidth - totalSpacing, currHeight - totalSpacing );
        refRect.setFrame( SPACING, SPACING, currDiam, currDiam );
        
        // The radius of the dial does not include the space allocated
        // for the color globe.
        double  radius      = (currDiam - G_DIAMETER) / 2;
        
        // If the window gets too small the radius will turn negative.
        // Complete the paint only if radius is a somewhat reasonable value.
        if ( radius > 10 )
            nonDegeneratePaint( radius );
    }
    
    /**
     * This method draws all the components
     * interior to the application window.
     * It does not draw the window background or borders.
     * It must only be called after it has been determined
     * that the drawing can be completed.
     * 
     * @param radius    
     *      the radius of the dial
     * 
     * @throws IllegalArgumentException if radius <= 0
     */
    private void nonDegeneratePaint( double radius )
    {
        if ( radius <= 0 )
        {
            String  message =
                "Invalid radius: " + radius;
            throw new IllegalArgumentException( message );
        }
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
    
    /**
     * Draw the bar and feedback box.
     * 
     * @param feedback  encapsulation of the feedback box properties
     */
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
    
    /**
     * Draw the color circle at the end of the bar.
     * A ray coincident with the bar 
     * should intersect the center of the circle.
     * 
     * @param gtx   the graphics context to draw with
     * @param line  line encapsulating the bar
     */
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
    
    /**
     * Encapsulates the properties of the feedback box, such as:
     * <ul>
     * <li>The dimensions of the feedback box;</li>
     * <li>The fill and edge colors of the feedback box;</li>
     * <li>The color and font used to draw the feedback string.</li>
     * </ul>
     * <p>
     * Contains the logic to draw the feedback box.
     * 
     * @see #draw(Graphics2D, Line2D)
     */
    private static class FeedbackRect
    {
        /** Unicode character for the degree symbol. */
        private static final char   degreeSym   = '\u00b0';
        /** Reference string for determining the dimensions of the box. */
        private static final String refStr      = "360" + degreeSym;
        /** Padding between the edges of the box and the feedback string. */
        private static final int    padding     = 5;
        /** The box's edge width. */
        private static final float  lineWidth   = 2;
        /** Encapsulation of the box's edge. */
        private static final Stroke stroke      = 
            new BasicStroke( lineWidth );
        /** The background color. */
        private static final Color  bgColor     = Color.WHITE;
        /** The edge color. */
        private static final Color  borderColor = Color.BLACK;
        /** The font color. */
        private static final Color  fontColor   = Color.BLACK;
        
        /** Encapsulation of the box's position and dimensions. */
        private final Rectangle2D   rect        = new Rectangle2D.Double();

        /**
         * Draw the feedback rectangle and string
         * using the bar for reference.
         * The rectangle will be slightly larger than
         * the bounds of the string,
         * and will be centered on the bar.
         * The string will be positioned approximately
         * at the center of the box.
         * 
         * @param gtx   the graphics context to draw with
         * @param line  the bar, for centering the feedback box
         */
        public void draw( Graphics2D gtx, Line2D line )
        {
            // Use the bounds of the reference string and the padding
            // to determine the dimensions of the box.
            FontRenderContext   frc = gtx.getFontRenderContext();
            Font        font        = gtx.getFont();
            Rectangle2D refRect     = font.getStringBounds( refStr, frc );
            double      rectWidth   = refRect.getWidth() + 2 * padding;
            double      rectHeight  = refRect.getHeight() + 2 * padding;
            
            // Find the center of the bar; compute the coordinates
            // of the box's top left corner so that the center of
            // the box lands on the center of the bar.
            Rectangle2D lineRect    = line.getBounds2D();
            double      lineCenterX = lineRect.getCenterX();
            double      lineCenterY = lineRect.getCenterY();
            double      rectXco     = lineCenterX - rectWidth / 2;
            double      rectYco     = lineCenterY - rectHeight / 2;            
            rect.setRect( rectXco, rectYco, rectWidth, rectHeight );
            
            // Compute the feedback string.
            double      degrees     = Utils.getDegrees( line );
            long        iDegrees    = Math.round( degrees );
            if ( iDegrees < 0 )
                iDegrees += 360;
            String      sDegrees    = "" + iDegrees + degreeSym;
            
            // From the width and height of the feedback string,
            // compute the coordinates of the string so that it
            // falls in approximately the center or the box
            // (it wind up a little below the center line).
            Rectangle2D sRect       = font.getStringBounds( sDegrees, frc );
            double      strXco      = lineCenterX - sRect.getWidth() / 2;
            double      strYco      = 
                lineCenterY + sRect.getHeight() / 2;
            
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
    
    /**
     * Manages mouse events.
     * Recognized events are:
     * <ol>
     * <li>
     * Mouse pressed:<br>
     * Request the keyboard focus;
     * if the event occurs inside the bar handle,
     * select the gCircle.
     * </li>
     * <li>
     * Mouse released:<br>
     * Deselect the gCircle.
     * </li>
     * </li>
     * <li>
     * Mouse clicked:<br>
     * Recompute the angle of the bar,
     * so that the bar will be coincident with
     * a ray from the center of the dial
     * through the selected point
     * </li>
     * <li>
     * Mouse clicked:<br>
     * Recompute the angle of the bar,
     * so that the bar will be coincident with
     * a ray from the center of the dial
     * through the point at which
     * the draw was reported.
     * </li>
     * 
     */
    private class MouseMonitor extends MouseAdapter
    {
        @Override
        public void mousePressed( MouseEvent evt )
        {
            dial.requestFocusInWindow();
            if ( gCircle.contains( evt.getPoint() ) )
                gCircleSelected = true;
        }
        
        @Override
        public void mouseReleased( MouseEvent evt )
        {
            gCircleSelected = false;
        }
        
        @Override
        public void mouseClicked( MouseEvent evt )
        {
            int     button  = evt.getButton();
            Point   point   = evt.getPoint();
            if ( button == MouseEvent.BUTTON1 && refRect.contains( point ) )
            {
                double  degrees = getDegreesToPoint( evt );
                root.setBarAngle( degrees );
                dial.repaint();
            }
        }
        
        @Override
        public void mouseDragged( MouseEvent evt )
        {
            if ( gCircleSelected )
            {   
                double  degrees = getDegreesToPoint( evt );
                root.setBarAngle( degrees );
                dial.repaint();
            }
        }
    }
    
    /**
     * Find the angle in (degrees) of a line
     * drawn from the center of the dial
     * to a point selected via mouse click.
     * 
     * @param evt   the event encapsulating the mouse click
     * 
     * @return
     *      the angle of a line drawn from the center of the dial
     *      to the point encapsulated by evt.   
     */
    private double getDegreesToPoint( MouseEvent evt )
    {
        Point   point       = evt.getPoint();
        double  centerXco   = refRect.getCenterX();
        double  centerYco   = refRect.getCenterY();
        Point2D center      = 
            new Point2D.Double( centerXco, centerYco );
        double  radians     = Utils.getAngle( center, point );
        double  degrees = Math.toDegrees( radians );
        return degrees;
    }
    
    /**
     * Monitors keyboard events,
     * and executes assigned actions:
     * increment or decrement the angle of the bar
     * and repaint the dial;
     * or, do nothing.
     * Up-arrow and right-arrow increment,
     * down-arrow and left-arrow decrement.
     * Arrows on a keypad are equivalent to
     * their non-keypad counterparts.
     * For keyboards that don't have arrow keys,
     * A is treated as equivalent to up-arrow,
     * and S is equivalent to down-arrow.
     */
    private class KeyMonitor extends KeyAdapter
    {
        /** Default increment value, degrees. */
        private static final double DEF_INCR    = 1;
        /** 
         * Map of keys to increment values; most keys default to 0.
         */
        Map<Integer,Double>    incrMap =
            Map.of( 
                KeyEvent.VK_UP, DEF_INCR,
                KeyEvent.VK_KP_UP, DEF_INCR,
                KeyEvent.VK_RIGHT, DEF_INCR,
                KeyEvent.VK_KP_RIGHT, DEF_INCR,
                KeyEvent.VK_A, DEF_INCR,
                KeyEvent.VK_DOWN, -DEF_INCR,
                KeyEvent.VK_KP_DOWN, -DEF_INCR,
                KeyEvent.VK_LEFT, -DEF_INCR,
                KeyEvent.VK_KP_LEFT, -DEF_INCR,
                KeyEvent.VK_S, -DEF_INCR
            );

        @Override
        public void keyPressed( KeyEvent evt )
        {
            int     keyCode = evt.getKeyCode();
            double  incr    = incrMap.getOrDefault( keyCode, 0. );
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
                dial.repaint();
            }
        }
    }
}
