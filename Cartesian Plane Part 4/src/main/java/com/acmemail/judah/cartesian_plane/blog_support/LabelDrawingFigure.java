package com.acmemail.judah.cartesian_plane.blog_support;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.Root;

/**
 * This class draws a figure for the blog.
 * 
 * @author Jack Straub
 *
 */
public class LabelDrawingFigure extends JPanel
{
    private static final long serialVersionUID = 1L;
    /** The initial width of the window, in pixels. */
    private static int  initWidth           = 815;
    /** The initial height of the window, in pixels. */
    private static int  initHeight          = 515;
    /** Background color of the Canvas */
    private Color       bgColor             = new Color( .9f, .9f, .9f );
    
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
    
    private Font                font    = 
        new Font( Font.MONOSPACED, Font.PLAIN, 25 );
    private FontRenderContext   frc;
    
    private float       labelPadding    = 5;
    private float       axisWeight      = 5;
    private float       axisLen         = 100;
    private float       ticWeight       = 7;
    private float       ticLen          = 25;
    
    private float       yAxisXco        = 25;
    private float       yAxisYco1       = 10;
    private float       yAxisYco2       = yAxisYco1 + axisLen;
    private Line2D      yAxis           = 
        new Line2D.Float( yAxisXco, yAxisYco1, yAxisXco, yAxisYco2 );
    
    private float       yAxisTicXco1    = yAxisXco - ticLen / 2;
    private float       yAxisTicXco2    = yAxisTicXco1 + ticLen;
    private float       yAxisTicYco     = yAxisYco1 + axisLen / 2;
    private Line2D      yAxisTic        =
        new Line2D.Float( yAxisTicXco1, yAxisTicYco, yAxisTicXco2, yAxisTicYco );
    
    private float       xAxisYco    = yAxisYco2 + 20;
    private float       xAxisXco1   = yAxisXco + 20;
    private float       xAxisXco2   = xAxisXco1 + axisLen;
    private Line2D      xAxis       = 
        new Line2D.Float( xAxisXco1, xAxisYco, xAxisXco2, xAxisYco );
    
    private float       xAxisTicXco     = xAxisXco1 + axisLen / 2;
    private float       xAxisTicYco1    = xAxisYco - ticLen / 2;
    private float       xAxisTicYco2    = xAxisTicYco1 + ticLen;
    private Line2D      xAxisTic        =
        new Line2D.Float( xAxisTicXco, xAxisTicYco1, xAxisTicXco, xAxisTicYco2 );
    
    private Stroke      axisStroke      = new BasicStroke( axisWeight );
    private int         ticCap          = BasicStroke.CAP_ROUND;
    private int         ticJoin         = BasicStroke.JOIN_ROUND;
    private Stroke      ticStroke       = 
        new BasicStroke( ticWeight, ticCap, ticJoin );
    
    public static void main( String[] args )
    {
        LabelDrawingFigure  canvas  = new LabelDrawingFigure();
        Root                root    = new Root( canvas );
        root.start();
    }
    
    /**
     * Constructor. Sets the initial size of the window.
     * Based on
     */
    public LabelDrawingFigure()
    {
        // The initial width and height of a window is set using 
        // a Dimension object.
        Dimension   size    = new Dimension( initWidth, initHeight );
        
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
        
        gtx.setRenderingHint( 
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        gtx.setFont( font );
        frc = gtx.getFontRenderContext();
        
        gtx.setColor( Color.BLACK );
        gtx.setStroke( axisStroke );
        gtx.draw( xAxis );
        gtx.draw( yAxis );
        gtx.setStroke( ticStroke );
        gtx.draw( xAxisTic );
        gtx.draw( yAxisTic );
        
        drawYTicLabel();
        drawXTicLabel();

        ////////////////////////////////////
        // Begin boilerplate
        ////////////////////////////////////
        gtx.dispose();
        ////////////////////////////////////
        // End boiler plate
        ////////////////////////////////////
    }
    
    private void drawYTicLabel()
    {
        String      label   = "1.50";
        TextLayout  layout  = new TextLayout( label, font, frc );
        Rectangle2D bounds  = layout.getBounds();
        float       yOffset = (float)(bounds.getHeight() / 2);
        float       xco     = (float)yAxisTic.getX2() + labelPadding;
        float       yco     = (float)yAxisTic.getY1() + yOffset;
        layout.draw( gtx, xco, yco );
    }
    
    private void drawXTicLabel()
    {
        String      label   = "3.25";
        TextLayout  layout  = new TextLayout( label, font, frc );
        Rectangle2D bounds  = layout.getBounds();
        float       yOffset = (float)bounds.getHeight() + labelPadding;
        float       xOffset = (float)bounds.getWidth() / 2;
        float       xco     = (float)xAxisTic.getX1() - xOffset;
        float       yco     = (float)xAxisTic.getY2() + yOffset;
        layout.draw( gtx, xco, yco );
    }
}
