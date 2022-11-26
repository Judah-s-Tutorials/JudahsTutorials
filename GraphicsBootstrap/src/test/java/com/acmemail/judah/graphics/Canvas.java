package com.acmemail.judah.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

/**
 * This is a simple class to provide boilerplate for a generic
 * graphics application in Java. To use it, make a copy of it
 * and modify the paintComponent method.
 * 
 * @author Jack Straub
 * 
 * @see <a href="https://judahstutorials.blogspot.com/p/java-project-graphics-bootstrap.html">
 * Graphics Bootstrap
 * </a>
 *
 */
@SuppressWarnings("serial")
public class Canvas extends JPanel
{
    private final Color     bgColor     = new Color( .9f, .9f, .9f );
    private final Color     fillColor   = Color.BLUE;
    private final Color     edgeColor   = Color.BLACK;
    private final int       edgeWidth   = 3;
    
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    /**
     * Constructor. Sets the initial height and width of this Canvas.
     * Note that the user can always change the geometry after the
     * window is displayed.
     * 
     * @param width		initial width of this window
     * @param height	initial height of this window
     */
    public Canvas( int width, int height )
    {
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
    }
    
    /**
     * This method is where you do all your drawing.
     * Note the the window must be COMPLETELY redrawn
     * every time this method is called;
     * Java does not remember anything you previously drew.
     * 
     * This simple example merely draws and fills a rectangle
     * which occupies some proportion of the window.
     * To substitute your own work, KEEP THE CODE THAT IS
     * MARKED BOILERPLATE, and substitute your code
     * for the code that displays the rectangle.
     * 
     * @param graphics  Graphics context, for doing all drawing.
     */
    @Override
    public void paintComponent( Graphics graphics )
    {
        // begin boilerplate
        super.paintComponent( graphics );
        currWidth = getWidth();
        currHeight = getHeight();
        gtx = (Graphics2D)graphics.create();
        gtx.setColor( bgColor );
        gtx.fillRect( 0,  0, currWidth, currHeight );
        // end boilerplate
        
        // Fill and draw a rectangle that is 60% 
        // the current width and height of this window.
        int     rectWidth   = (int)(currWidth * .6);
        int     rectHeight  = (int)(currHeight * .6);
        
        // To center the rectangle horizontally in the window, find
        // the difference between rectWidth and currWidth, 
        // then allocate half of it to the left of the rectangle.
        // Similarly, center the rectangle vertically using the
        // rectHeight and currHeight.
        int     rectXco     = (currWidth - rectWidth) / 2;
        int     rectYco     = (currHeight - rectHeight) / 2;
        
        // To draw the edge of the rectangle you'll need a 
        // Stroke object, which determines the edge width.
        Stroke  stroke      = new BasicStroke( edgeWidth );
        
        // Fill the rectangle before drawing the edge.
        gtx.setColor( fillColor );
        gtx.fillRect( rectXco, rectYco, rectWidth, rectHeight );
        gtx.setStroke( stroke );
        gtx.setColor( edgeColor );
        gtx.drawRect( rectXco, rectYco, rectWidth, rectHeight );
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
}
