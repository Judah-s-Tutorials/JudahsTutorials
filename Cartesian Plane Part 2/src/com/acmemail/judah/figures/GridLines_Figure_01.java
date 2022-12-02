package com.acmemail.judah.figures;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

/**
 * This class draws a figure for the blog.
 * 
 * @author Jack Straub
 *
 */
public class GridLines_Figure_01 extends JPanel
{
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
    
    /**
     * Constructor. Sets the initial size of the window.
     * Based on
     */
    public GridLines_Figure_01()
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
        
        drawAxes();
        // drawGridLines();
        // drawMinorTicMarks();
        // drawMajorTicMarks();
        // drawXYLabels();

        ////////////////////////////////////
        // Begin boilerplate
        ////////////////////////////////////
        gtx.dispose();
        ////////////////////////////////////
        // End boiler plate
        ////////////////////////////////////
    }
    
    private void drawAxes()
    {
        float   centerXco   = currWidth / 2.0f;
        float   centerYco   = currHeight / 2.0f;
        Stroke  weight      = new BasicStroke( 3 );
        gtx.setStroke( weight );
        gtx.setColor( Color.BLACK );
        
        // draw the axes
        Line2D  xAxis       = 
            new Line2D.Double(0, centerYco, currWidth, centerYco );
        Line2D  yAxis       = 
            new Line2D.Double( centerXco, 0, centerXco, currHeight );
        gtx.draw( xAxis );
        gtx.draw( yAxis );
    }
}
