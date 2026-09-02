package com.acmemail.judah.battleship.artwork.awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

/**
 * <img 
 *     src="doc-files/SplatPic.png" alt="Splat picture"
 *     style="max-width: 10%;"
 * > 
 * This class encapsulates a <em>splat,</em>
 * an image used to mark a cell on a grid
 * where a ship has been hit.
 * The image is created as follows:
 * <img 
 *     src="doc-files/SplatTrimmed.png" alt="Splat picture"
 *     style="display: block; float: right; margin: 0 0 0 .25em; max-width: 20%;"
 * > 
 *
 * <ol>
 * <li>
 * Create two concentric circles.
 * The outer circle is called the <em>crown circle,</em>
 * the inner circle we will refer to as just <em>inner circle.</em>
 * The size of the circles is configurable.
 * </li>
 * <li>
 * Inscribe an imaginary,
 * regular polygon in the inner circle.
 * The number of sides of the polygon is configurable.
 * </li>
 * <li>
 * For each side:
 * <ul>
 * <li>
 * Sketch an imaginary radial line
 * that bisects the side
 * and intersects the crown circle.
 * </li>
 * <li>
 * Draw a line from the right endpoint of the side
 * to the point where the radial line 
 * intersects the crown circle,
 * and a second line from the intersection point on the crown circle
 * to the left endpoint of the side.
 * </li>
 * </ul>
 * </li>
 * <li>
 * Close the figure,
 * fill it,
 * and draw an edge around it.
 * </li>
 * </ol>
 * <p>
 * All parameters that control the appearance of the splat image
 * have reasonable default.
 * The ones that the client may control are:
 * <ol>
 * <li>
 * fillColor<br>
 * The color to fill the figure with.
 * </li>
 * <li>
 * edgeColor<br>
 * The color used to draw the edge of the figure.
 * </li>
 * <li>
 * backgroundColor<br>
 * The background color of the image.
 * The default is 100% transparent.
 * </li>
 * <li>
 * crownRadius<br>
 * The radius of the crown circle.
 * </li>
 * <li>
 * innerRadiusPC<br>
 * The radius of the inner circle
 * <u>as a percentage of the radius of the crown circle</u>.
 * This figure should be less than one.
 * </li>
 * <li>
 * numSides<br>
 * The number of sides
 * of the regular polygon
 * inscribed in the inner circle.
 * </li>
 * </ol>
 * <p>
 * To examine the parameters,
 * invoke the {@linkplain #getParams()} method.
 * This returns a instance of a {@linkplain Params} class,
 * which contains the current configuration
 * in public, read/write fields.
 * To change the parameters,
 * get the current parameters,
 * edit the returned instance,
 * and pass the modified instance to {@linkplain #setParams(Params)}.
 * To obtain an image
 * constructed with the current set of parameters,
 * call the {@linkplain #getImage()} method.
 * {@linkplain com.acmemail.judah.battleship.artwork.awt.sandbox.SplatDemo}
 * is an application that let's you experiment
 * with different configuration parameters.
 * 
 * @author Jack Straub
 * 
 * @see com.acmemail.judah.battleship.artwork.awt.sandbox.SplatDemo
 */
public class Splat
{
    /** Constant for working with circles and angles. */
    private static final double TWO_PI             = 2 * Math.PI;
    /** The type of the image of the splat. */
    private static final int    imageType   = BufferedImage.TYPE_INT_ARGB;
    
    /** Background color; the color of the rectangle the splat is drawn on. */
    private Color       backgroundColor = new Color( 0x00_FF_FF_FF, true );
    /** Splat fill color. */
    private Color       fillColor       = Color.RED;
    /** Splat edge color. */
    private Color       edgeColor       = Color.BLACK;
    /** Radius of the outer circle. */
    private double      crownRadius     = 10;
    /** Radius of the inner circle as a percentage of the crown radius. */
    private double      innerRadiusPC   = .8;
    /** Number of sides in the polygon inscribed in the inner circle. */
    private int         numSides        = 10;
    
    /**
     * Default constructor.
     */
    public Splat()
    {
    }
    
    /**
     * Get the parameters used to draw this splat.
     * 
     * @return the parameters used to draw this splat
     */
    public Params getParams()
    {
        return new Params();
    }
    
    /**
     * Set the parameters used to draw this splat.
     * 
     * @param params    the parameters used to draw this splat
     */
    public void setParams( Params params )
    {
        this.backgroundColor = params.backgroundColor;
        this.fillColor = params.fillColor;
        this.edgeColor = params.edgeColor;
        this.crownRadius = params.crownRadius;
        this.innerRadiusPC = params.innerRadiusPC;
        this.numSides = params.numSides;
    }
    
    /**
     * Gets an image of the splat.
     * 
     * @return  an image of the splat
     */
    public Image getImage()
    {
        double          innerRadius = crownRadius * innerRadiusPC;
        double          center      = crownRadius;
        double          extAngle    = TWO_PI / numSides;
        double          theta       = 0;
        Path2D          splat       = new Path2D.Double();
        
        splat.moveTo( center + innerRadius, center );
        for ( int inx = 0 ; inx <= numSides ; ++inx )
        {
            // Path is already positioned at one end of a side of the polygon;
            // calculate the position of the other end.
            double  theta2  = theta + extAngle;
            double  xco2    = center + innerRadius * Math.cos( theta2 );
            double  yco2    = center + innerRadius * Math.sin( theta2 );
            
            // Angle of a radial line that bisects the side defined above.
            double  thetaP  = (theta + theta2) / 2;
            // The point at which the radial line intersects the crown.
            double  xcoP    = center + crownRadius * Math.cos( thetaP );
            double  ycoP    = center + crownRadius * Math.sin( thetaP );

            // Line from one end of the side to the crown.
            splat.lineTo( xcoP, ycoP );
            // Line from the crown to the other end of the side..
            splat.lineTo( xco2, yco2 );
            
            theta = theta2;
        }
        int             side    = 2 * (int)Math.ceil( crownRadius );
        BufferedImage   image   = new BufferedImage( side, side, imageType );
        Graphics2D      gtx     = image.createGraphics();
        
        gtx.setColor( backgroundColor );
        gtx.fillRect( 0, 0, side, side );
        gtx.setColor( fillColor );
        gtx.fill( splat );
        gtx.setColor( edgeColor );
        gtx.draw( splat );
        
        return image;
    }
    
    /**
     * Encapsulation of parameters used to draw the splat.
     */
    public class Params
    {
        /**
         * Default constructor, not used.
         */
        private Params()
        {
            // not used
        }
        
        /** Background color; the color of the rectangle the splat is drawn on. */
        public Color       backgroundColor = Splat.this.backgroundColor;
        /** Splat fill color. */
        public Color       fillColor       = Splat.this.fillColor;
        /** Splat edge color. */
        public Color       edgeColor       = Splat.this.edgeColor;
        /** Radius of the crown circle. */
        public double      crownRadius     = Splat.this.crownRadius;
        /** Radius of the inner circle as a percentage of the crown radius. */
        public double      innerRadiusPC   = Splat.this.innerRadiusPC;
        /** Number of sides in the polygon inscribed in the inner circle. */
        public int         numSides        = Splat.this.numSides;
    }
}
