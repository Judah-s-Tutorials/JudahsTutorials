package com.acmemail.judah.cartesian_plane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

@SuppressWarnings( { "serial", "unused" } )
public class GridLines_01C extends JPanel
{
    private Color           bgColor     = new Color( .9f, .9f, .9f );
    private Color           gridColor   = new Color( .75f, .75f, .75f);
    
    private float           gridSpacing = 40;
    private float           gridWeight  = 1;
        
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    public GridLines_01C( int width, int height )
    {
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
    }
    
    /**
     * This method is where you do all your drawing.
     * Note that the window must be COMPLETELY redrawn
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
        gtx = (Graphics2D)graphics;
        gtx.setColor( bgColor );
        gtx.fillRect( 0,  0, currWidth, currHeight );
        // end boilerplate
        
        drawGrid();
    }
    
    private void drawGrid()
    {
        gtx.setColor( gridColor );
        gtx.setStroke( new BasicStroke( gridWeight ) );
        
        float   centerXco   = currWidth / 2f;
        float   numLeft = (float)Math.floor( currWidth / 2f / gridSpacing );
        float   leftXco = centerXco - numLeft * gridSpacing;
        for ( float xco = leftXco ; xco <= currWidth ; xco += gridSpacing )
        {
            Line2D  gridLine    = new Line2D.Float( xco, 0, xco, currHeight );
            gtx.draw( gridLine );
        }
    }
}
