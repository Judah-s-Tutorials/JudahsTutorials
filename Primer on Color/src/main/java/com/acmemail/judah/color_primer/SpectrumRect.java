package com.acmemail.judah.color_primer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * Using the HSB color model,
 * fill a rectangle with gradually increasing hues
 * from 0&deg; to 360&deg;
 */
public class SpectrumRect extends JPanel
{
    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;
    /** The application window's background color. */
    private static final Color  BROWN       = new Color( 0xA52A2A );
    /** 
     * The width of the empty border around the 
     * edges of the application's window.
     */
    private static final double BORDER      = 10;
    /** Reusable object for encapsulating the color rectangle. */
    private final Rectangle2D   rect        = new Rectangle2D.Double();
    
    /**
     * Application entry point.
     * 
     * @param args  command-line arguments, not used
     */
    public static void main( String[] args )
    {
        SpectrumRect    spectrum    = new SpectrumRect( 1010, 350 );
        Root        root        = new Root( spectrum );
        root.start();
    }
    
    /**
     * Constructor.
     * Initializes the desired dimensions
     * of the application's window.
     * 
     * @param width     desired width
     * @param height    desired height
     */
    public SpectrumRect( int width, int height )
    {
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        // begin boilerplate
        super.paintComponent( graphics );
        int         currWidth   = getWidth();
        int         currHeight  = getHeight();
        Graphics2D  gtx         = (Graphics2D)graphics;
        gtx.setColor( BROWN );
        gtx.fillRect( 0,  0, currWidth, currHeight );
        
        double  rectXco     = BORDER;
        double  rectYco     = BORDER;
        double  rectWidth   = currWidth - 2 * BORDER;
        double  rectHeight  = currHeight - 2 * BORDER;
        double  rectYEnd    = rectYco + rectHeight;
        double  iRectWidth  = (int)(rectWidth + .5);
        rect.setRect( rectXco, rectYco, rectWidth, rectHeight );
        float   cPart       = (float)(1. / rectWidth);
        Line2D  line        = new Line2D.Double();
        for ( int inx = 0 ; inx < iRectWidth ; ++inx )
        {
            double  lXco    = rectXco + inx;
            line.setLine( lXco, rectYco, lXco, rectYEnd );
            float   hue     = inx * cPart;
            System.out.println( hue );
            Color   color   = Color.getHSBColor( hue, 1f, 1f );
            gtx.setColor( color );
            gtx.draw( line );
        }
    }
}
