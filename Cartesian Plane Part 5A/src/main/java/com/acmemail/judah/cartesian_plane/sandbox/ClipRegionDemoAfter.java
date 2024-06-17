package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * Part of the clip region demo.
 * This class draws a figure
 * after applying a clip region.
 * 
 * @author Jack Straub
 *
 * @see ClipRegionDemoBefore
 */
@SuppressWarnings("serial")
public class ClipRegionDemoAfter extends JPanel
{
    /** Background color of the Canvas. */
    private static final Color          bgColor     = 
        new Color( .9f, .9f, .9f );
    
    /** Width of demo rectangle. */
    private static final float          rectWidth   = 300;
    /** Height of demo rectangle. */
    private static final float          rectHeight  = 200;
    /** X-coordinate of demo rectangle. */
    private static final float          rectXco     = 25;
    /** Y-coordinate of demo rectangle. */
    private static final float          rectYco     = 25;
    /** Right edge of rectangle (pre-computed for convenience). */
    private static final float          rectRight   = rectXco + rectWidth;
    /** Bottom edge of rectangle (pre-computed for convenience). */
    private static final float          rectBottom  = rectYco + rectHeight;
    /** Demop rectangle. */
    private static final Rectangle2D    rectDemo    =
        new Rectangle2D.Float( rectXco, rectYco, rectWidth, rectHeight );
    
    /** Color of the edge of the bounding rectangle. */
    private static final Color          rectColor   = Color.RED;
    /** Thickness of the axes. */
    private static final Stroke         rectWeight  = new BasicStroke( 1 );
    /** Color of horizontal and vertical lines. */
    private static final Color          lineColor   = Color.BLACK;
    /** Thickness of horizontal and vertical lines. */
    private static final Stroke         lineWeight  = new BasicStroke( 7 );
    /** How far demo lines extend past demo rectangle */
    private static final int            lineOffset  = 10;
    
    /** Line to draw at left edge of demo rectangle. */
    private static final Line2D         topLine     =
        new Line2D.Float( 
            rectXco - lineOffset,
            rectYco,
            rectRight  + lineOffset,
            rectYco
        );
    /** Line to draw at right edge of demo rectangle. */
    private static final Line2D         rightLine   =
        new Line2D.Float( 
            rectRight,
            rectYco - lineOffset,
            rectRight,
            rectBottom + lineOffset
        );
    /** Line to draw at bottom edge of demo rectangle. */
    private static final Line2D         bottomLine  =
        new Line2D.Float( 
            rectXco - lineOffset,
            rectBottom,
            rectRight  + lineOffset,
            rectBottom
        );
    /** Line to draw at left edge of demo rectangle. */
    private static final Line2D         leftLine    =
        new Line2D.Float( 
            rectXco,
            rectYco - lineOffset,
            rectXco,
            rectBottom + lineOffset
        );
    
    /** Description to display in window. */
    private static final String         text        = 
        "After setting clip region";
    /** Color of descriptive text. */
    private static final Color          textColor   = Color.BLACK;
    /** Size of font for displaying descriptive text. */
    private static final float          fontSize    = 15;
    /** Style of font for displaying descriptive text. */
    private static final int            fontStyle  = Font.BOLD;
    
    /** 
     * The graphics context.
     * Made an instance variable for convenience
     * of helper methods;
     * set every time paintComponent is invoked. 
     */
    private Graphics2D  gtx         = null;
    /** 
     * The current width of the Canvas.
     * Made an instance variable for convenience
     * of helper methods;
     * set every time paintComponent is invoked. 
     */
    private int         currWidth   = 0;
    /** 
     * The current height of the Canvas; 
     * set every time paintComponent is invoked.
     */
    private int         currHeight  = 0;
    
    /**
     * Program entry point.
     * 
     * @param args command-line arguments, not used
     */
    public static void main( String[] args )
    {
        ClipRegionDemoAfter canvas  = new ClipRegionDemoAfter();
        Root                root    = new Root( canvas );
        root.start();
    }
    
    /**
     * Constructor. Sets the initial size of the window
     * based on the size of the grid and its margins.
     */
    public ClipRegionDemoAfter()
    {
        int         initWidth   = (int)rectWidth + 5 * lineOffset;
        int         initHeight  = (int)rectHeight + 5 * lineOffset;
        Dimension   size        = new Dimension( initWidth, initHeight );
        
        // IMPORTANT: set the size of the window using
        // setPreferredSize. Remember that the actual size of the
        // window may be different after being displayed.
        this.setPreferredSize( size );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        ////////////////////////////////////
        // Begin boilerplate
        ////////////////////////////////////
        super.paintComponent( graphics );
        gtx = (Graphics2D)graphics.create();
        currWidth = getWidth();
        currHeight = getHeight();
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, currWidth, currHeight );
        ////////////////////////////////////
        // End boiler plate
        ////////////////////////////////////
        
        gtx.setClip( rectDemo );
        
        gtx.setStroke( lineWeight );
        gtx.setColor( lineColor );
        gtx.draw( topLine );
        gtx.draw( rightLine );
        gtx.draw( bottomLine );
        gtx.draw( leftLine );
        
        gtx.setStroke( rectWeight );
        gtx.setColor( rectColor );
        gtx.draw( rectDemo );
        
        Font    font    = gtx.getFont().deriveFont( fontStyle, fontSize );
        gtx.setFont( font );
        gtx.setColor( textColor );
        
        float   textWidth   = gtx.getFontMetrics().stringWidth( text );
        int     textXco     = (int)(rectDemo.getCenterX() - textWidth / 2);
        int     textYco     = (int)rectDemo.getCenterY();
        gtx.drawString( text, textXco, textYco );

        ////////////////////////////////////
        // Begin boilerplate
        ////////////////////////////////////
        gtx.dispose();
        ////////////////////////////////////
        // End boiler plate
        ////////////////////////////////////
    }
}
