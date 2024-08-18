package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.LineGenerator;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * This class for inclusion in the LineGenerator documentation.
 * It demonstrates how the LineGenerator class
 * lays out horizontal and vertical lines.
 * 
 * @author Jack Straub
 *
 */
public class LineGeneratorFigure extends JPanel
{
    private static final long serialVersionUID = 1L;
    /** Type of line cap to use when drawing lines. */
    private static final int    lineCap         = BasicStroke.CAP_BUTT;
    /**
     * Type of join to use when drawing lines. We only need this value
     * because the BasicStroke constructor that sets the cap requires
     * us to set the join at the same time.
     */
    private static final int    lineJoin         = BasicStroke.JOIN_ROUND;
    
    /** Background color of the ProfileEditorFeedback. */
    private static final Color          bgColor         = 
        new Color( .9f, .9f, .9f );
    
    /** Pixels/unit to display. */
    private static final float          gridUnit        = 200;
    /** Lines per unit to draw. */
    private static final float          lpu             = 4;
    
    /** Width of rectangle bounding the grid. */
    private static final float          rectWidth       = 325;
    /** Height of rectangle bounding the grid. */
    private static final float          rectHeight      = 450;
    /** Empty space above grid. */
    private static final float          topMargin       = 50;
    /** Empty space left of grid. */
    private static final float          leftMargin      = 50;
    /** X-coordinate of bounding rectangle. */
    private static final float          rectXco         = topMargin;
    /** Y-coordinate of bounding rectangle. */
    private static final float          rectYco         = leftMargin;
    /** Rectangle bounding grid. */
    private static final Rectangle2D    boundingRect    =
        new Rectangle2D.Float( rectXco, rectYco, rectWidth, rectHeight );
    
    /** Color of the edge of the bounding rectangle. */
    private static final Color          rectColor      = Color.BLACK;
    /** Color of the axes drawn at center of bounding rectangle. */
    private static final Color          axisColor      = Color.BLACK;
    /** Color of horizontal lines. */
    private static final Color          hlColor         = Color.BLUE;
    /** Color of vertical lines. */
    private static final Color          vlColor         = Color.MAGENTA;
    /** Thickness of horizontal and vertical lines. */
    private static final float          lineWeight      = 3;
    /** Thickness of the axes. */
    private static final float          axisWeight      = 5;
    
    /** 
     * The graphics context.
     * Made an instance variable for convenience
     * of helper methods;
     * set every time paintComponent is invoked. 
     */
    private Graphics2D  gtx         = null;
    /** 
     * The current width of the ProfileEditorFeedback.
     * Made an instance variable for convenience
     * of helper methods;
     * set every time paintComponent is invoked. 
     */
    private int         currWidth   = 0;
    /** 
     * The current height of the ProfileEditorFeedback; 
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
        LineGeneratorFigure canvas  = new LineGeneratorFigure();
        Root                root    = new Root( canvas );
        root.start();
    }
    
    /**
     * Constructor. Sets the initial size of the window
     * based on the size of the grid and its margins.
     */
    public LineGeneratorFigure()
    {
        int         initWidth   = (int)(2 * leftMargin + rectWidth);
        int         initHeight  = (int)(2 * topMargin + rectHeight);
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

        gtx.setColor( rectColor );
        gtx.draw( boundingRect );
        
        LineGenerator   hlGen   = 
            new LineGenerator( 
                boundingRect,
                gridUnit,
                lpu,
                -1,
                LineGenerator.HORIZONTAL
            );
        gtx.setStroke( new BasicStroke( lineWeight, lineCap, lineJoin ));
        gtx.setColor( hlColor );
        for ( Line2D line : hlGen )
            gtx.draw( line );
        
        LineGenerator   vlGen   = 
            new LineGenerator( 
                boundingRect,
                gridUnit,
                lpu,
                -1,
                LineGenerator.VERTICAL
            );
        gtx.setColor( vlColor );
        for ( Line2D line : vlGen )
            gtx.draw( line );
        
        gtx.setStroke( new BasicStroke( axisWeight, lineCap, lineJoin ));
        gtx.setColor( axisColor );
        Iterator<Line2D>    axisIterator    = 
            LineGenerator.axesIterator( boundingRect );
        gtx.draw( axisIterator.next() );
        gtx.draw( axisIterator.next() );

        ////////////////////////////////////
        // Begin boilerplate
        ////////////////////////////////////
        gtx.dispose();
        ////////////////////////////////////
        // End boiler plate
        ////////////////////////////////////
    }
}
