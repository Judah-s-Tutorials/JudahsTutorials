package com.acmemail.judah.figures;

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
    private final float     gridWeight          = 1;
    private final Font      textFont            = 
        new Font( "Monospace", Font.PLAIN, 10 );
    
    private final int		minorTicsPerUnit	= 10;
    private final int		majorTicsPerUnit	= 2;
    private final float		pixelsPerUnit		= 75;
    
    private final float     minorTicLen         = 7;
    private final float     majorTicLen         = 2 * minorTicLen;
    
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
        
        drawGrid();
        drawMinorTics();
        drawMajorTics();
        drawAxes();
        drawText();
        
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
    
    private void drawMajorTics()
    {
        gtx.setColor( ticColor );
        gtx.setStroke( new BasicStroke( majorTicWeight ) );
        float   ticDist     = pixelsPerUnit / majorTicsPerUnit;
        
        float   numXUnits   = gridWidth / pixelsPerUnit;
        float   numXTicsNeg = (int)(numXUnits * majorTicsPerUnit / 2);
        float   firstXco    = centerXco - (int)numXTicsNeg * ticDist;
        float   yco1        = centerYco - majorTicLen / 2;
        float   yco2        = yco1 + majorTicLen;
        for ( float xco = firstXco ; xco <= maxXco ; xco += ticDist )
        {
            Line2D  tic     = new Line2D.Float( xco, yco1, xco, yco2 );
            gtx.draw( tic );
        }
        
        float   numYUnits   = gridHeight / pixelsPerUnit;
        float   numYTicsPos = (int)(numYUnits * majorTicsPerUnit / 2);
        float   firstYco    = centerYco - (int)numYTicsPos * ticDist;
        float   xco1        = centerXco - majorTicLen / 2;
        float   xco2        = xco1 + majorTicLen;
        for ( float yco = firstYco ; yco <= maxYco ; yco += ticDist )
        {
            Line2D  tic     = new Line2D.Float( xco1, yco, xco2, yco );
            gtx.draw( tic );
        }
    }
        
    private void drawMinorTics()
    {
        gtx.setColor( ticColor );
        gtx.setStroke( new BasicStroke( minorTicWeight ) );
        float   ticDist     = pixelsPerUnit / minorTicsPerUnit;
        
        float   numXUnits   = gridWidth / pixelsPerUnit;
        int     numXTicsNeg = (int)(numXUnits * minorTicsPerUnit / 2);
        float   firstXco    = centerXco - (int)numXTicsNeg * ticDist;
        float   yco1        = centerYco - minorTicLen / 2;
        float   yco2        = yco1 + minorTicLen;
        for ( float xco = firstXco ; xco <= maxXco ; xco += ticDist )
        {
            Line2D  tic     = new Line2D.Float( xco, yco1, xco, yco2 );
            gtx.draw( tic );
        }
        
        float   numYUnits   = gridHeight / pixelsPerUnit;
        int     numYTicsPos = (int)(numYUnits * minorTicsPerUnit / 2);
        float   firstYco    = centerYco - (int)numYTicsPos * ticDist;
        float   xco1        = centerXco - minorTicLen / 2;
        float   xco2        = xco1 + minorTicLen;
        for ( float yco = firstYco ; yco <= maxYco ; yco += ticDist )
        {
            Line2D  tic     = new Line2D.Float( xco1, yco, xco2, yco );
            gtx.draw( tic );
        }
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
    
    private void drawText()
    {
        gtx.setColor( textColor );
        gtx.setFont( textFont );
        FontMetrics metrics = gtx.getFontMetrics();
        float       ticDist = pixelsPerUnit / majorTicsPerUnit;
        
        float   numXUnits   = gridWidth / pixelsPerUnit;
        float   numXTicsNeg = (int)(numXUnits * majorTicsPerUnit / 2);
        float   firstXco    = centerXco - (int)numXTicsNeg * ticDist;
        float   nextXUnit   = -numXTicsNeg / majorTicsPerUnit;
        for ( float xco = firstXco ; xco <= maxXco ; xco += ticDist )
        {
            if ( nextXUnit != 0 )
            {
                String      label   = String.format( "%.1f", nextXUnit );
                Rectangle2D rect    = metrics.getStringBounds( label, gtx );
                float       baseXco = xco - (float)rect.getWidth() / 2;
                float       baseYco = 
                    centerYco + majorTicLen / 2 + (float)rect.getHeight();
                gtx.drawString( label, baseXco, baseYco );
            }
            nextXUnit += 1.0 / majorTicsPerUnit;
        }
        
        float   numYUnits   = gridHeight / pixelsPerUnit;
        float   numYTicsPos = (int)(numYUnits * majorTicsPerUnit / 2);
        float   firstYco    = centerYco - (int)numYTicsPos * ticDist;
        float   baseXco     = centerXco + majorTicLen / 2 + 3;
        float   nextYUnit   = numYTicsPos / majorTicsPerUnit;
        for ( float yco = firstYco ; yco <= maxYco ; yco += ticDist )
        {
            if ( nextYUnit != 0 )
            {
                String      label   = String.format( "%.1f", nextYUnit );
                Rectangle2D rect    = metrics.getStringBounds( label, gtx );
                float       baseYco = yco + (float)rect.getHeight() / 3;
                gtx.drawString( label, baseXco, baseYco );
            }
            nextYUnit -= 1.0 / majorTicsPerUnit;
        }
    }
}
