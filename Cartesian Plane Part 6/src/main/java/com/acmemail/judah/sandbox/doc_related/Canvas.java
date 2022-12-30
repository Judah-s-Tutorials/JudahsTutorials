package com.acmemail.judah.sandbox.doc_related;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

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
    
    private final float         xlateXco     = 50;
    private final float         xlateYco     = 50;
    private final float         gridWidth   = 512;
    private final float         gridHeight  = 256;
    private final float         xAxisYco    = (gridHeight - 1) / 2;
    private final float         yAxisXco    = (gridWidth - 1) / 2;
    private final Color         gridColor   = new Color( .7f, .7f, .7f );
    private final Rectangle2D   grid        = 
        new Rectangle2D.Float( 0, 0, gridWidth, gridHeight );

    private final int           capButt     = BasicStroke.CAP_BUTT;
    private final int           join        = BasicStroke.JOIN_BEVEL;
    private final Color         axisColor   = Color.ORANGE;
    private final Stroke        axisStroke  = new BasicStroke( 7, capButt, join );
    private final Color         unitColor   = new Color( 0x006400 );
    private final Stroke        unitStroke  = new BasicStroke( 3, capButt, join );
    private final Color         glColor     = Color.RED;
    private final Stroke        glStroke    = new BasicStroke( 1, capButt, join );
    
    private final Font          font        = new Font( "fixed", Font.PLAIN, 10 );
    private FontRenderContext   frc;
    
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    public static void main( String[] args )
    {
        Canvas  canvas  = new Canvas( 1200, 800 );
        Root    root    = new Root( canvas );
        root.start();
    }
    
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
        
        // adjust coordinates to allow for whitespace around grid
        gtx.translate( xlateXco, xlateYco );
        
        // get the FontRenderContext
        frc = gtx.getFontRenderContext();
                
        // Fill the rectangle
        gtx.setColor( gridColor );
        gtx.fill( grid );
        drawAxes();
        drawUnits();
        drawGridLines();
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    private void drawAxes()
    {
        gtx.setColor( axisColor );
        gtx.setStroke( axisStroke );

        Line2D  xAxis       =
            new Line2D.Float( 0, xAxisYco, gridWidth, xAxisYco );
        gtx.draw( xAxis );
        
        Line2D  yAxis       =
            new Line2D.Float( yAxisXco, 0, yAxisXco, gridHeight );
        gtx.draw( yAxis );
    }
    
    private void drawUnits()
    {
        gtx.setColor( unitColor );
        gtx.setStroke( unitStroke );
        
        float   ppu         = gridWidth / 8;

        System.out.println( "xAxisYco = " + xAxisYco );
        for ( int inx = -2 ; inx <= 2 ; ++inx )
        {
            float   yco     = xAxisYco + inx * ppu;
            System.out.println( "yco = " + yco );
            Line2D  line    = new Line2D.Float( 0, yco, gridWidth, yco );
            gtx.draw( line );
            labelHorizontalLine( line, 0 );
        }
        System.out.println( "**********" );

        System.out.println( "yAxisyco = " + yAxisXco );
        for ( int inx = -4 ; inx <= 4 ; ++inx )
        {
            float   xco     = yAxisXco + inx * ppu;
            System.out.println( "xco = " + xco );
            Line2D  line    = new Line2D.Float( xco, 0, xco, gridHeight );
            gtx.draw( line );
            labelVerticalLine( line, 0 );
        }
    }
    
    private void drawGridLines()
    {
        gtx.setColor( glColor );
        gtx.setStroke( glStroke );
        
        float   ppu         = gridWidth / 16;

        System.out.println( "xAxisYco = " + xAxisYco );
        for ( int inx = -4 ; inx <= 4 ; ++inx )
        {
            float   yco     = xAxisYco + inx * ppu;
            System.out.println( "yco = " + yco );
            Line2D  line    = new Line2D.Float( 0, yco, gridWidth, yco );
            gtx.draw( line );
            labelHorizontalLine( line, 1 );
        }
        System.out.println( "**********" );

        System.out.println( "yAxisyco = " + yAxisXco );
        for ( int inx = -8 ; inx <= 8 ; ++inx )
        {
            float   xco     = yAxisXco + inx * ppu;
            System.out.println( "xco = " + xco );
            Line2D  line    = new Line2D.Float( xco, 0, xco, gridHeight );
            gtx.draw( line );
            labelVerticalLine( line, 1 );
        }
    }
    
    private void labelVerticalLine( Line2D line, int column )
    {
        float       xco1    = (float)line.getX1();
        String      label   = String.format( "%3.1f", xco1 );
        TextLayout  layout  = new TextLayout( label, font, frc );
        Rectangle2D bounds  = layout.getBounds();
        float       leading = layout.getLeading();
        float       yOffset = 
            (column + 1) * ((float)bounds.getHeight() + 5) + column * leading;
        float       xOffset = (float)bounds.getWidth() / 2;
        float       xco     = (float)line.getX1() - xOffset;
        float       yco     = (float)line.getY2() + yOffset;
        layout.draw( gtx, xco, yco );
    }
    
    private void labelHorizontalLine( Line2D line, int column )
    {
        float       yco1    = (float)line.getY1();
        String      label   = String.format( "%5.1f", yco1 );
        TextLayout  layout  = new TextLayout( label, font, frc );
        Rectangle2D bounds  = layout.getBounds();
        float       yOffset = (float)bounds.getHeight() / 2;
        float       xOffset = 
            column * ((float)bounds.getWidth()) + (column + 1) * 5;
        float       xco     = (float)line.getX2() + xOffset;
        float       yco     = (float)line.getY2() + yOffset;
        layout.draw( gtx, xco, yco );
    }
}
