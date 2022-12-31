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
 * This class draws a figure
 * representing a four-pixel wide rectangle,
 * and discussing where the center of the rectangle
 * should be.
 *
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class Rectangle2DCenterFigure extends JPanel
{
    private final Color         bgColor     = new Color( .9f, .9f, .9f );
    
    private final float         startXco    = 35;
    private final float         startYco    = 35;
    private final float         rectWidth   = 50;
    private final float         rectHeight  = 25;
    private final Color         rectColor   = new Color( .7f, .7f, .7f );
    private final float         pixelGap    = 10;
    
    private final float         lineXco     = 2 * rectWidth + 1.5f * pixelGap;
    private final float         yTweak      = rectWidth / 2;
    private final float         lineYco1    = -yTweak;
    private final float         lineYco2    = rectHeight + yTweak;
    private final Line2D        line        = 
        new Line2D.Float( lineXco, lineYco1, lineXco, lineYco2 );
    private final Color         lineColor   = Color.BLACK;
    
    private final Font          font        = new Font( "fixed", Font.PLAIN, 10 );
    private final Color         fontColor   = Color.BLACK;
    
    private FontRenderContext   frc;
    private int                 currWidth;
    private int                 currHeight;
    private Graphics2D          gtx;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments, not used
     */
    public static void main( String[] args )
    {
        Rectangle2DCenterFigure canvas  = new Rectangle2DCenterFigure();
        Root                    root    = new Root( canvas );
        root.start();
    }
    
    /**
     * Constructor. Sets the initial height and width of this Canvas.
     * Note that the user can always change the geometry after the
     * window is displayed.
     */
    public Rectangle2DCenterFigure()
    {
        // set width and height so that demo grid + surrounding text 
        // will be roughly centered in the window
        int width   = (int)(rectWidth  * 4 + pixelGap * 3 + startXco * 2 );
        int height  = (int)(rectHeight + 2 * startYco );
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
        
        // adjust coordinates to allow for whitespace around figure
        gtx.translate( startXco, startYco );
        
        // get the FontRenderContext
        frc = gtx.getFontRenderContext();
        
        String  label   = "pixel ";
        for ( int inx = 0 ; inx < 4 ; ++inx )
        {
            float       xco     = inx * (rectWidth + pixelGap);
            gtx.setColor( rectColor );
            Rectangle2D rect    = 
                new Rectangle2D.Float( xco, 0, rectWidth, rectHeight );
            gtx.fill( rect );
            centerLabel( rect, label + inx );
        }
        
        gtx.setColor( lineColor );
        gtx.draw( line );
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    /**
     * Draw a label centered (approximately)
     * in a rectangle.
     * 
     * @param rect  the given rectangle
     * @param label the label to draw
     */
    private void centerLabel( Rectangle2D rect, String label )
    {
        TextLayout  layout  = new TextLayout( label, font, frc );
        Rectangle2D bounds  = layout.getBounds();
        float       xOffset = (float)bounds.getWidth() / 2;
        float       yOffset = (float)bounds.getHeight() / 2 - layout.getDescent();
        float       xco     = (float)rect.getCenterX() - xOffset;
        float       yco     = (float)rect.getCenterY() + yOffset;
        gtx.setColor( fontColor );
        layout.draw( gtx, xco, yco );
    }
}
