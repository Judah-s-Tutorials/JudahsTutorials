package com.acmemail.judah.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.Root;

@SuppressWarnings("serial")
public class GridLines_03 extends JPanel
{
    private Color   bgColor             = new Color( .9f, .9f, .9f );
    private Color   gridColor           = new Color( .75f, .75f, .75f);
    private Color   marginColor         = new Color( 0x008080 );
    
    private float   gridLinesPerUnit    = 2;
    private float   pixelsPerUnit       = 75;
    private float   gridWeight  = 1;
    
    private int     leftMargin          = 60;
    private int     rightMargin         = 20;
    private int     topMargin           = 20;
    private int     bottomMargin        = 60;

    ///////////////////////////////////////////////////////
    //
    // The following values are recalculated every time 
    // paintComponent is invoked.
    //
    ///////////////////////////////////////////////////////
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    // These variables describe the shape of the rectangle, exclusive
    // of the margins, in which the grid is drawn. Their values
    // are recalculated every time paintComponent is invoked.
    private float           gridWidth;      // width of the rectangle
    private float           gridHeight;     // height of the rectangle
    private float           centerXco;      // center x-coordinate
    private float           minXco;         // left-most x-coordinate
    private float           maxXco;         // right-most x-coordinate
    private float           centerYco;      // center y-coordinate
    private float           minYco;         // top-most y-coordinate
    private float           maxYco;         // bottom-most y-coordinate
    
    public GridLines_03( int width, int height )
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
        
        // Describe the rectangle containing the grid
        gridWidth = currWidth - leftMargin - rightMargin;
        minXco = leftMargin;
        maxXco = minXco + gridWidth;
        centerXco = minXco + gridWidth / 2f;
        gridHeight = currHeight - topMargin - bottomMargin;
        minYco = topMargin;
        maxYco = minYco + gridHeight;
        centerYco = minYco + gridHeight / 2f;

        drawGrid();
        paintMargins();
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    private void drawGrid()
    {
        gtx.setColor( gridColor );
        gtx.setStroke( new BasicStroke( gridWeight ) );
        float   gridSpacing = pixelsPerUnit / gridLinesPerUnit;
        
        float   numLeft = (float)Math.floor( gridWidth / 2 / gridSpacing );
        float   leftXco = centerXco - numLeft * gridSpacing;
        for ( float xco = leftXco ; xco <= maxXco ; xco += gridSpacing )
        {
            Line2D  gridLine    = 
                new Line2D.Float( xco, minYco, xco, maxYco );
            gtx.draw( gridLine );
        }
        
        float   numTop  = (float)Math.floor( gridHeight / 2f / gridSpacing );
        float   topYco  = centerYco - numTop * gridSpacing;
        for ( float yco = topYco ; yco <= maxYco ; yco += gridSpacing )
        {
            Line2D  gridLine    = 
                new Line2D.Float( minXco, yco, maxXco, yco );
            gtx.draw( gridLine );
        }
    }
    
    private void paintMargins()
    {
        gtx.setColor( marginColor );
        Rectangle2D rect    = new Rectangle2D.Float();
        
        // top margin
        rect.setRect( 0, 0, currWidth, topMargin );
        gtx.fill( rect );
        
        // right margin
        rect.setRect( currWidth - rightMargin, 0, rightMargin, currHeight );
        gtx.fill( rect );
        
        // bottom margin
        rect.setRect( 0, currHeight - bottomMargin, currWidth, bottomMargin );
        gtx.fill( rect );
        
        // left margin
        rect.setRect( 0, 0, leftMargin, currHeight );
        gtx.fill( rect );
    }
}
