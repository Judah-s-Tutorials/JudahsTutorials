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

/**
 * This class encapsulates the graphical portion
 * of the SpectrumDial application, which illustrates
 * the fundamentals of the HSB color model.
 * The graphics elements are divided into
 * the following parts.
 * <ul>
 * <li>
 * The dial.<br>
 * This is a circular region
 * which displays the hue progression
 * from 0&deg; through 360&deg;.
 * The circle can be partially displayed
 * by setting minimum and maximum hue values.
 * The saturation and brightness of the fill colors
 * can be controlled using the property setters.
 * </li>
 * <li>
 * The color bar.<br>
 * This is a line draw from the center of the dial
 * to its edge.
 * It can be moved by clicking in the dialog
 * or by grabbing the color globe and dragging.
 * </li>
 * <li>
 * The feedback box.<br>
 * A text box imposed over the center of the bar
 * that displays the angle of the bar in degrees.
 * </li>
 * <li>
 * The color globe.<br>
 * A circle drawn at the end of the color bar.
 * It is filled with the color [hue, 1.0, 1.0],
 * where hue is the angle of the color bar,
 * and the constants are the values
 * of the saturation and brightness.
 * </li>
 * <li>
 * The margin.<br>
 * An empty area around the edges of the application window.
 * </li>
 * </ul>
 * <p>
 * A main method starts the application
 * by instantiating the SpectrumDial inside of a DialFrame,
 * which contains GUI controls.
 * The SpectrumDial can be instantiated
 * independently of a DialFrame;
 */
public class SpectrumDial extends JPanel
{
    private static final long   serialVersionUID = 1L;
    
    /** Rendering hints map. */
    private static final Map<RenderingHints.Key, Object> renderingHints =
        Map.of(
            KEY_ANTIALIASING, VALUE_ANTIALIAS_ON,
            KEY_RENDERING, VALUE_RENDER_QUALITY
        );
    
    /** Color of the bar.  */
    private static final Color      BAR_COLOR   = Color.BLACK;
    /** Width of the bar. */
    private static final float      BAR_WIDTH   = 3;
    /** Width of the bar encapsulated as a Stroke. */
    private final Stroke            BAR_STROKE  = 
        new BasicStroke( BAR_WIDTH );

    /** Window margins. */
    private static final double     PADDING     = 5;
    /** Pre-compiled value of 2pi. */
    private static final double     TWO_PI      = Math.PI * 2;
    
    /** The SpectrumDial application. */
    private static final SpectrumDial dial      = 
        new SpectrumDial( 500 );
    
    // These are instance variables so that they don't have to be
    // instantiated every time paint() is invoked; they still need
    // to be configured with every invocation of paint().
    /** Pre-compiled instance to encapsulate the bounds of the dial. */
    private Ellipse2D       refRect         = new Ellipse2D.Double();
    /** Pre-compiled instance to encapsulate the bar. */
    private Line2D          bar             = new Line2D.Double();
    /** Pre-compiled instance to encapsulate bounds of the feedback box. */
    private FeedbackRect    feedbackRect    = new FeedbackRect();
    /** Pre-compiled instance to encapsulate bounds of the color globe. */
    private ColorGlobe      colorGlobe      = new ColorGlobe();
    
    // Basic properties
    /** The minimum angle from which the dial is displayed; degrees. */
    private int             hueMin;
    /** The maximum angle through which the dial is displayed; degrees. */
    private int             hueMax;
    /** The angle at which the color bar is drawn; degrees. */
    private int             barAngle;
    /** The saturation value applied to the color dial; percent. */
    private int             saturation;
    /** The brightness value applied to the color dial; percent. */
    private int             brightness;
    
    /**
     * Application entry point.
     *  
     * @param args  command-line arguments, not used
     */
    public static void main( String[] args )
    {
        /** GUI frame for the SpectrunDial application. */
        DialFrame   root    = new DialFrame( dial );
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
    
    /**
     * Gets the minimum hue value.
     * 
     * @return the minimum hue value
     */
    public int getHueMin()
    {
        return hueMin;
    }

    /**
     * Sets the minimum hue value.
     * 
     * @param hueMin the value to apply.
     */
    public void setHueMin(int hueMin)
    {
        this.hueMin = hueMin;
    }

    /**
     * Gets the maximum hue value.
     * 
     * @return the maximum hue value
     */
    public int getHueMax()
    {
        return hueMax;
    }

    /**
     * Sets the maximum hue value.
     * 
     * @param hueMax the value to apply.
     */
    public void setHueMax(int hueMax)
    {
        this.hueMax = hueMax;
    }

    /**
     * Gets the saturation value.
     * 
     * @return the saturation value
     */
    public int getSaturation()
    {
        return saturation;
    }

    /**
     * Sets the saturation value.
     * 
     * @param saturation the value to apply.
     */
    public void setSaturation(int saturation)
    {
        this.saturation = saturation;
    }

    /**
     * Gets the brightness value.
     * 
     * @return the brightness value
     */
    public int getBrightness()
    {
        return brightness;
    }


    /**
     * Sets the brightness value.
     * 
     * @param brightness the value to apply.
     */
    public void setBrightness(int brightness)
    {
        this.brightness = brightness;
    }

    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        
        final Color BROWN       = new Color( 0xDAA06D );
        
        Graphics2D  gtx         = (Graphics2D)graphics;
        int         currWidth   = getWidth();
        int         currHeight  = getHeight();
        gtx.setRenderingHints( renderingHints );
        gtx.setColor( BROWN );
        gtx.fillRect( 0, 0, currWidth, currHeight );
        
        // Determine the dimensions and position the reference rectangle:
        // 1) the rectangle will be a square, based on min(width,height);
        // 2) the dimensions will not include the padding around the dial;
        // 3) the position will allowing for the padding around the dial.
        double  SPACING         = ColorGlobe.DIAMETER + PADDING;
        double  totalSpacing    = 2 * SPACING;
        double  currDiam = 
            Math.min( currWidth - totalSpacing, currHeight - totalSpacing );
        refRect.setFrame( SPACING, SPACING, currDiam, currDiam );
        
        // The radius of the dial does not include the space allocated
        // for the color globe.
        double  radius      = (currDiam - ColorGlobe.DIAMETER) / 2;
        
        // If the window gets too small the radius will turn negative.
        // Complete the paint only if radius is a somewhat reasonable value.
        if ( radius > 10 )
            nonDegeneratePaint( radius, gtx );
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
    private void nonDegeneratePaint( double radius, Graphics2D gtx )
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

        double  start       = Math.toRadians( hueMin );
        double  end         = Math.toRadians( hueMax );
        float   sat         = saturation / 100f;
        float   bright      = brightness / 100f;
        
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
        drawBar( feedbackRect, gtx );
        colorGlobe.draw( gtx, bar );
    }
    
    /**
     * Draw the bar and feedback box.
     * 
     * @param feedback  encapsulation of the feedback box properties
     */
    private void drawBar( FeedbackRect feedback, Graphics2D gtx )
    {
        Color   saveColor   = gtx.getColor();
        Stroke  saveStroke  = gtx.getStroke();
        
        double  radians     = Math.toRadians( barAngle );
        double  radius      = refRect.getWidth() / 2;
        double  centerXco   = refRect.getCenterX();
        double  centerYco   = refRect.getCenterY();
        double  outerXco    = centerXco + radius * Math.cos( radians );
        double  outerYco    = centerYco - radius * Math.sin( radians );
        bar.setLine( centerXco, centerYco, outerXco, outerYco );
        gtx.setColor( BAR_COLOR );
        gtx.setStroke( BAR_STROKE );
        gtx.draw( bar );
        feedback.draw( gtx, bar );

        gtx.setColor( saveColor );
        gtx.setStroke( saveStroke );
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
     * Encapsulates the properties of the color globe, such as:
     * <ul>
     * <li>Its dimensions;</li>
     * <li>Its fill and edge colors;</li>
     * <li>Its fill color.</li>
     * </ul>
     * <p>
     * Contains the logic to draw the color globe.
     * 
     * @see #draw(Graphics2D, Line2D)
     */
    private static class ColorGlobe
    {
        /** Edge width of the color globe. */
        private static final int        BORDER_WIDTH    = 2;
        /** Edge width of the color globe encapsulated as a Stroke. */
        private static final Stroke     STROKE          =
            new BasicStroke( BORDER_WIDTH );
        /** Edge color of the color globe. */
        private static final Color      BORDER_COLOR    = Color.BLACK;
        /** Diameter of the color globe. */
        private static final double     DIAMETER        = 20;

        /**
         * Pre-compiled instance to encapsulate the color globe;
         * reconfigured each time the draw method is invoked.
         * Instance-level (not static) because each SpectrumDial
         * instance's globe position is independent of every
         * other instance's.
         */
        private final Ellipse2D   gCircle        = new Ellipse2D.Double();

        /**
         * Draw the color circle at the end of the bar.
         * A ray coincident with the bar 
         * should intersect the center of the circle.
         * 
         * @param gtx   the graphics context to draw with
         * @param line  line encapsulating the bar
         */
        public void draw( Graphics2D gtx, Line2D line )
        {
            Color   origColor   = gtx.getColor();
            Stroke  origStroke  = gtx.getStroke();
            
            double  xco         = line.getX2() - DIAMETER / 2;
            double  yco         = line.getY2() - DIAMETER / 2;
            gCircle.setFrame( xco, yco, DIAMETER, DIAMETER );
            
            double  angleNorm   = Utils.getAngle( line ) / TWO_PI;
            Color   hue         = Color.getHSBColor( (float)angleNorm, 1, 1 );
            gtx.setColor( hue );
            gtx.fill( gCircle );
            gtx.setStroke( STROKE );
            gtx.setColor( BORDER_COLOR );
            gtx.draw( gCircle );
            
            gtx.setColor( origColor );
            gtx.setStroke( origStroke );
        }

        public boolean contains( Point point )
        {
            boolean result  = gCircle.contains( point );
            return result;
        }
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
    private record FeedbackRect()
    {
        /** Unicode character for the degree symbol. */
        private static final char   DEGREE_SYM  = '\u00b0';
        /** Reference string for determining the dimensions of the box. */
        private static final String REF_STR     = "360" + DEGREE_SYM;
        /** Padding between the edges of the box and the feedback string. */
        private static final int    PADDING     = 5;
        /** The box's edge width. */
        private static final float  EDGE_WIDTH  = 2;
        /** Encapsulation of the box's edge. */
        private static final Stroke stroke      = 
            new BasicStroke( EDGE_WIDTH );
        /** The background color. */
        private static final Color  BG_COLOR    = Color.WHITE;
        /** The edge color. */
        private static final Color  EDGE_COLOR  = Color.BLACK;
        /** The color of the feedback string. */
        private static final Color  TEXT_COLOR  = Color.BLACK;
        
        /** 
         * Encapsulation of the box's position and dimension;
         * configured every time the draw method is invoked.
         */
        private static final Rectangle2D   rect = new Rectangle2D.Double();

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
         * @param bar   the bar, for centering the feedback box
         */
        public void draw( Graphics2D gtx, Line2D bar )
        {
            // Use the bounds of the reference string and the padding
            // to determine the dimensions of the box.
            FontRenderContext   frc = gtx.getFontRenderContext();
            Font        font        = gtx.getFont();
            Rectangle2D refRect     = font.getStringBounds( REF_STR, frc );
            double      rectWidth   = refRect.getWidth() + 2 * PADDING;
            double      rectHeight  = refRect.getHeight() + 2 * PADDING;
            
            // Find the center of the bar; compute the coordinates
            // of the box's top left corner so that the center of
            // the box lands on the center of the bar.
            Rectangle2D lineRect    = bar.getBounds2D();
            double      lineCenterX = lineRect.getCenterX();
            double      lineCenterY = lineRect.getCenterY();
            double      rectXco     = lineCenterX - rectWidth / 2;
            double      rectYco     = lineCenterY - rectHeight / 2;            
            rect.setRect( rectXco, rectYco, rectWidth, rectHeight );
            
            // Compute the feedback string.
            double      degrees     = Utils.getDegrees( bar );
            long        iDegrees    = Math.round( degrees );
            if ( iDegrees < 0 )
                iDegrees += 360;
            String      sDegrees    = "" + iDegrees + DEGREE_SYM;
            
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
            gtx.setColor( BG_COLOR );
            gtx.fill( rect );
            gtx.setColor( EDGE_COLOR );
            gtx.draw( rect );
            gtx.setColor( TEXT_COLOR );
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
     * through the  point
     * </li>
     * <li>
     * Mouse clicked:<br>
     * Recompute the angle of the bar,
     * so that the bar will be coincident with
     * a ray from the center of the dial
     * through the point at which
     * the draw was reported.
     * </li>
     * </ol>
     */
    private class MouseMonitor extends MouseAdapter
    {        
        /** When true indicates that a drag operation is in progress. */
        private boolean         gCircleSelected;
        
        @Override
        public void mousePressed( MouseEvent evt )
        {
            requestFocusInWindow();
            if ( colorGlobe.contains( evt.getPoint() ) )
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
                barAngle = (int)(getDegreesToPoint( evt ) + .5);
                repaint();
            }
        }

        @Override
        public void mouseDragged( MouseEvent evt )
        {
            if ( gCircleSelected )
            {
                barAngle = (int)(getDegreesToPoint( evt ) + .5);
                repaint();
            }
        }
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
                double  angle   = barAngle;
                angle += incr;
                if ( angle < 0 )
                    angle += 360;
                else if ( angle > 360 )
                    angle -= 360;
                else
                    ;
                barAngle = (int)(angle + .5);
                repaint();
            }
        }
    }
}
