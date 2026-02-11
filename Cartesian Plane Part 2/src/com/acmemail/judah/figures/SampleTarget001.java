package com.acmemail.judah.figures;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.Root;

@SuppressWarnings("serial")
public class SampleTarget001 extends JPanel
{
    private final Color     bgColor     = new Color( .9f, .9f, .9f );
    private final Color     gridColor   = new Color( .75f, .75f, .75f);
    private final Color     marginColor = new Color( 0x008080 );
    
    private final float     gridWeight          = 1;
    
    private final int		majorTicsPerUnit	= 2;
    private final float		pixelsPerUnit		= 75;
    
    private final int       leftMargin          = 60;
    private final int       rightMargin         = 20;
    private final int       topMargin           = 20;
    private final int       bottomMargin        = 60;
    
//    private Rectangle2D     workingRect;
    private float           gridWidth;
    private float           gridHeight;
    private float           centerXco;
    private float           minXco;
    private float           maxXco;
    private float           centerYco;
    private float           minYco;
    private float           maxYco;
    
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    public static void main( String[] args )
    {
        SampleTarget001 canvas  = new SampleTarget001( 500, 600 );
        Root            root    = new Root( canvas );
        root.start();
    }
    
    public SampleTarget001( int width, int height )
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
        gridWidth = currWidth - leftMargin - rightMargin;
        minXco = leftMargin;
        maxXco = minXco + gridWidth;
        centerXco = minXco + gridWidth / 2f;
        gridHeight = currHeight - topMargin - bottomMargin;
        minYco = topMargin;
        maxYco = minYco + gridHeight;
        centerYco = minYco + gridHeight / 2f;
        
        drawMargins();
        drawGrid();
    }    
    
    private void drawGrid()
    {
        gtx.setColor( gridColor );
        gtx.setStroke( new BasicStroke( gridWeight ) );
        float   ticDist     = pixelsPerUnit / majorTicsPerUnit;
        
        float   numXUnits   = gridWidth / pixelsPerUnit;
        float   numXTicsNeg = (int)(numXUnits * majorTicsPerUnit / 2);
        float   firstXco    = centerXco - (int)numXTicsNeg * ticDist;
        for ( float xco = firstXco ; xco <= maxXco ; xco += ticDist )
        {
            Line2D  tic     = new Line2D.Float( xco, minYco, xco, maxYco );
            gtx.draw( tic );
        }
        
        float   numYUnits   = gridHeight / pixelsPerUnit;
        float   numYTicsPos = (int)(numYUnits * majorTicsPerUnit / 2);
        float   firstYco    = centerYco - (int)numYTicsPos * ticDist;
        for ( float yco = firstYco ; yco <= maxYco ; yco += ticDist )
        {
            Line2D  tic     = new Line2D.Float( minXco, yco, maxXco, yco );
            gtx.draw( tic );
        }
    }
    
    private void drawMargins()
    {
        gtx.setColor( marginColor );
        // top margin
        Rectangle2D rect    =
            new Rectangle2D.Float( 0, 0, currWidth, topMargin );
        gtx.fill( rect );
        
        // right margin
        rect = new Rectangle2D.Float( 
            maxXco, 
            0, 
            currWidth - maxXco, 
            currHeight
        );
        gtx.fill( rect );
        
        // bottom margin
        rect = new Rectangle2D.Float( 
            0, 
            maxYco, 
            currWidth, 
            currHeight - maxYco
        );
        gtx.fill( rect );
        
        // left margin
        rect = new Rectangle2D.Float( 
            0, 
            0, 
            minXco, 
            currHeight
        );
        gtx.fill( rect );
    }
}
