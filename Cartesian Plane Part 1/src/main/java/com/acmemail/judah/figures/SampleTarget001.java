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
    private final Color     ticColor    = Color.BLACK;
    private final Color     axisColor   = Color.BLACK;
    private final Color     gridColor   = new Color( .75f, .75f, .75f);
    private final Color     textColor   = Color.BLACK;
    
    private final float		axisWeight			= 4;
    private final float		majorTicWeight		= 3;
    private final float		minorTicWeight	    = 2;
    
    private final int		minorTicsPerUnit	= 10;
    private final int		majorTicsPerUnit	= 1;
    private final float		pixelsPerUnit		= 100;
    
    private final float     minorTicLen         = 20;
    private final float     majorTicLen         = 2 * minorTicLen;
    
    private final int       leftMargin          = 40;
    private final int       rightMargin         = 10;
    private final int       topMargin           = 10;
    private final int       bottomMargin        = 40;
    
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
        
        drawMinorTics();
        drawAxes();
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    private void drawAxes()
    {
        Line2D  xAxis   = 
            new Line2D.Float( minXco, centerYco, maxXco, centerYco );
        Line2D  yAxis   = 
            new Line2D.Float( centerXco, minYco, centerXco, maxYco );
        gtx.setStroke( new BasicStroke( axisWeight ) );
        gtx.setColor( axisColor );
        gtx.draw( xAxis );
        gtx.draw( yAxis );
    }
    
    private void drawMinorTics()
    {
        gtx.setColor( ticColor );
        gtx.setStroke( new BasicStroke( minorTicWeight ) );
        float   distPerTic  = gridWidth / pixelsPerUnit * minorTicsPerUnit;
        float   ticYco1     = centerYco - minorTicLen / 2;
        float   ticYco2     = ticYco1 + minorTicLen;
        
        float   totalTics   = 
            gridWidth / pixelsPerUnit * minorTicsPerUnit;
        float   firstXco    = centerXco - totalTics / 2 * pixelsPerUnit;
        for ( float xco = firstXco ; xco <= maxXco ;  xco += distPerTic )
        {
            Line2D  tic = new Line2D.Float( xco, ticYco1, xco, ticYco2 );
            gtx.draw( tic );
        }
        
        float   ticXco1     = centerXco - minorTicLen / 2;
        float   ticXco2     = ticXco1 + minorTicLen;
        float   firstYco    = centerYco - totalTics / 2 * pixelsPerUnit;
        for ( float yco = firstYco ; yco <= maxYco ;  yco += distPerTic )
        {
            Line2D  tic = new Line2D.Float( ticXco1, yco, ticXco2, yco );
            gtx.draw( tic );
        }
    }
}
