package com.acmemail.judah.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CanvasBoilerplate extends JPanel
{
    private final Color     bgColor     = new Color( .9f, .9f, .9f );
    
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
    public CanvasBoilerplate( int width, int height )
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
        super.paintComponent( graphics );
        currWidth = getWidth();
        currHeight = getHeight();
        gtx = (Graphics2D)graphics;
        gtx.setColor( bgColor );
        gtx.fillRect( 0,  0, currWidth, currHeight );
    }
}
