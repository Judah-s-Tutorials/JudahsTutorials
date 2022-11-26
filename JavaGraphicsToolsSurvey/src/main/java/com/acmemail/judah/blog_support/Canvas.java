package com.acmemail.judah.blog_support;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

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
