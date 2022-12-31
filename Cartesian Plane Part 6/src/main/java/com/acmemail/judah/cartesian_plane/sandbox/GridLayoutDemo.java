package com.acmemail.judah.cartesian_plane.sandbox;

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
 * This class demonstrates how a grid
 * representing the Cartesian plane
 * is laid out
 * using the algorithm specified
 * by the LineGenerator class.
 * Specifically:
 * 
 * <div style="max-width: 50em;">
 * <ul>
 * <li>
 *     The width of the grid is set to 512 pixels. 
 *     This puts the y-axis at x = (512 - 1)/2 = 255.5.
 *     That leaves 255.5 pixels to the left of the y-axis
 *     and 255.5 to the right 
 *     (we always want the same number of pixels
 *     to the left and right of the y-axis).
 * </li>
 * <li>
 *     The height of the grid is set to 256 pixels. 
 *     This puts the x-axis at y = (256 - 1)/2 = 127.5.
 *     That leaves 127.5 pixels above the x-axis
 *     and 127.5 below 
 *     (we always want the same number of pixels
 *     above and below the x-axis).
 * </li>
 * <li>
 *     The grid unit is set to 64 pixels.
 *     That puts unit divisions at:
 *     <ul style="list-style-type: lower-alpha;">
 *         <li>
 *             From the y-axis to the left, 
 *             x = 255.5, 191.5, 127.5, 64.5 and -.5.
 *         </li>
 *         <li>
 *             From the y-axis to the right, 
 *             x = 255.5, 319.5, 383.5, 447.5 and 511.5.
 *         </li>
 *         <li>
 *             From the x-axis to the top, 
 *             x = 127.5, 63.5 and -.5.
 *         </li>
 *         <li>
 *             From the x-axis to the bottom, 
 *             x = 127.5, 191.5 and 255.5.
 *         </li>
 *     </ul>
 * </li>
 * <li>
 *     The grid lines-per-unit is set to 2.
 *     That puts grid lines every 64 pixels, at:
 *     <ul style="list-style-type: lower-alpha;">
 *         <li>
 *             From the y-axis to the left, 
 *             x = 255.5, 223.5, 191.5, ... -.5.
 *         </li>
 *         <li>
 *             From the y-axis to the right, 
 *             x = 255.5, 287.5, 318.5, ... 511.5.
 *         </li>
 *         <li>
 *             From the x-axis to the top, 
 *             x = 127.5, 95.5, 63.5 ... -.5.
 *         </li>
 *         <li>
 *             From the x-axis to the bottom, 
 *             x = 127.5, 159.5, 191.5 ... 255.5.
 *         </li>
 *     </ul>
 * </li>
 * </ul>
 * </div>
 * <p>
 * This data is intended
 * to be used as a base case
 * for validating the output
 * from the LineGenerator iterators.
 * </p>
 * 
 * @author Jack Straub
 * 
 * @see LineGenerator
 *
 */
@SuppressWarnings("serial")
public class GridLayoutDemo extends JPanel
{
    private final Color         bgColor     = new Color( .9f, .9f, .9f );
    
    private final float         xlateXco    = 35;
    private final float         xlateYco    = 35;
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
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments, not used
     */
    public static void main( String[] args )
    {
        GridLayoutDemo  canvas  = new GridLayoutDemo();
        Root            root    = new Root( canvas );
        root.start();
    }
    
    /**
     * Constructor. Sets the initial height and width of this Canvas.
     * Note that the user can always change the geometry after the
     * window is displayed.
     */
    public GridLayoutDemo()
    {
        // set width and height so that demo grid + surrounding text 
        // will be roughly centered in the window
        int width   = (int)(gridWidth + 3 * xlateXco );
        int height  = (int)(gridHeight + 2 * xlateYco );
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
    }
    
    /**
     * Draws/redraws this window as necessary.
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
                
        gtx.setColor( gridColor );
        gtx.fill( grid );
        drawAxes();
        drawUnits();
        drawGridLines();
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    /**
     * Draw the axes in the demo grid.
     */
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
    
    /**
     * Draw the unit divisions in the demo grid.
     */
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

        System.out.println( "yAxisXco = " + yAxisXco );
        for ( int inx = -4 ; inx <= 4 ; ++inx )
        {
            float   xco     = yAxisXco + inx * ppu;
            System.out.println( "xco = " + xco );
            Line2D  line    = new Line2D.Float( xco, 0, xco, gridHeight );
            gtx.draw( line );
            labelVerticalLine( line, 0 );
        }
    }
    
    /**
     * Draw the grid lines in the demo grid.
     */
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
    
    /**
     * Draw a label below the grid, 
     * indicating the position of a given vertical line.
     * The label is drawn in a given row
     * below the grid. 
     * The first row is #0.
     * 
     * @param line  the given line
     * @param row   the given row
     */
    private void labelVerticalLine( Line2D line, int row )
    {
        float       xco1    = (float)line.getX1();
        String      label   = String.format( "%3.1f", xco1 );
        TextLayout  layout  = new TextLayout( label, font, frc );
        Rectangle2D bounds  = layout.getBounds();
        float       leading = layout.getLeading();
        float       yOffset = 
            (row + 1) * ((float)bounds.getHeight() + 5) + row * leading;
        float       xOffset = (float)bounds.getWidth() / 2;
        float       xco     = (float)line.getX1() - xOffset;
        float       yco     = (float)line.getY2() + yOffset;
        layout.draw( gtx, xco, yco );
    }
    
    /**
     * Draw a label to the right of the grid, 
     * indicating the position of a given horizontal line.
     * The label is drawn in a given column
     * next to the grid. 
     * The first column is #0.
     * 
     * @param line  the given line
     * @param row   the given row
     */
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
