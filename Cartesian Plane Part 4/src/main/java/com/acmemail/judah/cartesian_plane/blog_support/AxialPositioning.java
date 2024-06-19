package com.acmemail.judah.cartesian_plane.blog_support;

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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This application shows how to computer
 * the axes of a grid
 * encapsulated in a bounding rectangle.
 * <img 
 *     src="doc-files/AxisPositioning.png" 
 *     alt="Axis Positioning Demo"
 *     style="float:left; width:10%; height:auto; margin-right: 1em;"
 * >
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class AxialPositioning extends JPanel
{
    /** The width of the application window. */
    private static final int            winWidth    = 360;
    /** The height of the application window. */
    private static final int            winHeight   = 400;
    /** The color of the application window. */
    private static final Color          winColor    = Color.WHITE;
    /** The color of the axes. */
    private static final Color          axisColor   = 
        new Color( 0xFF474C );
    /** The color of the text. */
    private static final Color          textColor   = Color.BLACK;
    /** The stroke used to draw lines in figure. */
    private static final Stroke         baseStroke  = 
        new BasicStroke( 3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL );
    
    /**
     * The length of the lines that delimit the dimension properties,
     * for example width: ------- 150 -------. 
     * Specified as a percentage of the dimension
     * of the bounding rectangle being tagged.
     */
    private static final double         dimLinePC   = .33;
    
    /** X-coordinate of the bounding rectangle. */
    private static final double         ulcXco      = 70;
    /** Y-coordinate of the bounding rectangle. */
    private static final double         ulcYco      = 50;
    /** Width of the bounding rectangle. */
    private static final int            rectWidth   = 250;
    /** Height of the bounding rectangle. */
    private static final int            rectHeight  = 325;
    /** Fill color of the bounding rectangle. */
    private static final Color          rectColor   = 
        new Color( 0xefefef );
    /** The bounding rectangle. */
    private static final Rectangle2D    rect        = 
        new Rectangle2D.Double( ulcXco, ulcYco, rectWidth, rectHeight );
    /** The x-coordinate of the origin. */
    private static final double         midXco      = rect.getCenterX();
    /** The y-coordinate of the origin. */
    private static final double         midYco      = rect.getCenterY();
    /** The x-coordinate of the left of the bounding rectangle. */
    private static final double         leftXco     = ulcXco;
    /** The x-coordinate of the right of the bounding rectangle. */
    private static final double         rightXco    = leftXco + rectWidth;
    /** The y-coordinate of the top of the bounding rectangle. */
    private static final double         topYco      = ulcYco;
    /** The y-coordinate of the bottom of the bounding rectangle. */
    private static final double         bottomYco   = topYco + rectHeight;
    
    /** Line object for miscellaneous line drawing. */
    private final Line2D        line    = new Line2D.Double();
    /** Copy of the graphics context. Initialized in paintComponent. */
    private Graphics2D          gtx;
    /** Font for drawing coordinates. */
    private Font                font;
    /** Context for position coordinates. */
    private FontRenderContext   frc;
    
    /**
     * Application entry point.
     * 
     * @param args  Command line arguments, not used.
     */
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> {
            JFrame  frame   = new JFrame( "Axes Positioning Figure" );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            
            frame.setLocation( 100, 100 );
            frame.setContentPane( new AxialPositioning() );
            frame.pack();
            frame.setVisible( true );
        });
    }
    
    /**
     * Constructor.
     * Sets the size of the application window.
     */
    public AxialPositioning()
    {
        Dimension   dim     = new Dimension( winWidth, winHeight );
        setPreferredSize( dim );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        gtx = (Graphics2D)graphics.create();
        font = gtx.getFont();
        frc = gtx.getFontRenderContext();
        
        gtx.setColor( winColor );
        gtx.fillRect( 0, 0, getWidth(), getHeight() );
        gtx.setColor( rectColor );
        gtx.fill( rect );
        
        gtx.setColor( axisColor );
        gtx.setStroke( baseStroke );
        line.setLine( midXco, topYco, midXco, bottomYco);
        gtx.draw( line );
        line.setLine( leftXco, midYco, rightXco, midYco);
        gtx.draw( line );
        
        gtx.setColor( textColor );
        drawLabel( leftXco, topYco );
        drawLabel( midXco, topYco );
        drawLabel( rightXco, topYco );
        drawLabel( leftXco, midYco );
        drawLabel( midXco, midYco );
        drawLabel( rightXco, midYco );
        drawLabel( leftXco, bottomYco );
        drawLabel( midXco, bottomYco );
        drawLabel( rightXco, bottomYco );
        drawWidth();
        drawHeight();
        
        gtx.dispose();
    }
    
    /**
     * Draw a label at the given coordintates.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     */
    private void 
    drawLabel( double xco, double yco )
    {
        String      label       = String.format( "(%.0f,%.0f)", xco, yco );
        TextLayout  layout      = new TextLayout( label, font, frc );
        Rectangle2D bounds      = layout.getBounds();
        double      textXco     = xco;
        double      textYco     = yco;
        double      xOffset     = -bounds.getWidth() / 2;
        double      yOffset     = -2;
        
        // The x- and y- offsets are for calculating the coordinates
        // of the text relative to the coordinates they describe. The
        // chosen initial values will place the text immediately above
        // the y-coordinate, and centered on the x-coordinate. They
        // must be adjusted based on the position of the point of the
        // coordinates relative the upper left corner of the bounding
        // rectangle.
        if ( yco == topYco )
            yOffset = bounds.getHeight(); // place text below y-coordinate
        else if ( yco == midYco )
        {
            if ( xco == midXco )
            {
                // place text below and right of origin.
                yOffset = bounds.getHeight() + 2;
                xOffset = 2;
            }
        }
        else 
            ;
        textXco += xOffset;
        textYco += yOffset;
        layout.draw( gtx, (float)textXco, (float)textYco );
    }
    
    /**
     * Draw the width dimension label
     * near the top of the application window.
     */
    private void drawWidth()
    {
        double      lineLen     = dimLinePC * rectWidth;
        String      label       = String.format( "%d", rectWidth );
        TextLayout  layout      = new TextLayout( label, font, frc );
        Rectangle2D bounds      = layout.getBounds();
        double      textXco     = midXco - bounds.getWidth() / 2;
        double      textYco     = topYco - bounds.getHeight() / 2;
        double      xco2        = textXco - 10;
        double      yco2        = topYco - bounds.getHeight();
        double      xco1        = xco2 - lineLen;
        double      yco1        = yco2;
        line.setLine( xco1, yco1, xco2, yco2 );
        gtx.draw( line );
        
        layout.draw( gtx, (float)textXco, (float)textYco );
        
        xco1 = textXco + bounds.getWidth() + 10;
        xco2 = xco1 + lineLen;
        line.setLine( xco1, yco1, xco2, yco2 );
        gtx.draw( line );
    }
    
    
    /**
     * Draw the height dimension label
     * near the top of the application window.
     */
    private void drawHeight()
    {
        double      lineLen     = dimLinePC * rectHeight;
        String      label       = String.format( "%d", rectHeight );
        TextLayout  layout      = new TextLayout( label, font, frc );
        Rectangle2D bounds      = layout.getBounds();
        double      textXco     = leftXco - 2.5 * bounds.getWidth();
        double      textYco     = midYco + bounds.getHeight() / 2;
        double      xco2        = textXco + bounds.getWidth() / 2;
        double      yco2        = textYco - 1.5 * bounds.getHeight();
        double      xco1        = xco2;
        double      yco1        = yco2 - lineLen;
        line.setLine( xco1, yco1, xco2, yco2 );
        gtx.draw( line );
        
        layout.draw( gtx, (float)textXco, (float)textYco );
         
        yco1 = textYco + bounds.getHeight() / 2;
        yco2 = yco1 + lineLen;
        line.setLine( xco1, yco1, xco2, yco2 );
        gtx.draw( line );
    }
}
