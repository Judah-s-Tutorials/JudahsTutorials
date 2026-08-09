package com.acmemail.judah.color_primer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * This application demonstrates how to 
 * apply alpha values to a color.
 * It draws some text,
 * then draws blue rectangles
 * from the top of the window
 * to the bottom.
 * The blue rectangles begin
 * with full transparency (alpha = 0)
 * and become decreasingly transparent
 * until they are not transparent at all
 * (alhpa = 1).
 */
@SuppressWarnings("serial")
public class ColorPrimer extends JPanel
{
    /** The application window's background color. */
    private Color   bgColor             = new Color( .9f, .9f, .9f );

    ///////////////////////////////////////////////////////
    //
    // The following values are recalculated every time 
    // paintComponent is invoked.
    //
    ///////////////////////////////////////////////////////
    /** The current width of the application window. */
    private int             currWidth;
    /** The current heigt of the application window. */
    private int             currHeight;
    /** The font to use when drawing strings. */
    private Font            font;
    
    /**
     * Application entry point;.
     * 
     * @param args  command line arguments, not used.
     */
    public static void main( String[] args )
    {
        ColorPrimer primer  = new ColorPrimer( 750, 350 );
        Root        root    = new Root( primer );
        root.start();
    }
    
    /**
     * Constructor.
     * 
     * @param width     the desired width of the application window
     * @param height    the desired height of the application window
     */
    public ColorPrimer( int width, int height )
    {
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
        
        font = getFont().deriveFont( 70f );
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

        Graphics2D  gtx = (Graphics2D)graphics;
        gtx.setColor( bgColor );
        gtx.fillRect( 0,  0, currWidth, currHeight );
        // end boilerplate
        
        int         bYco    = 75;
        int         bXco    = 20;
        
        gtx.setFont( font );
        gtx.setColor( Color.RED );
        gtx.drawString( "I am the egg man", bXco, bYco );
        gtx.drawString( "They are the egg man", bXco, bYco + 80 );
        gtx.drawString( "I am the Walrus!", bXco, bYco + 160 );
        gtx.drawString( "Goo goo g'joob", bXco, bYco + 250 );
        
        paintTransparentOverlay( gtx );
    }
    
    /**
     * Paints a series of decreasingly transparent rectangles
     * over the application window.
     * 
     * @param gtx   the graphics context to draw with
     */
    private void paintTransparentOverlay( Graphics2D gtx )
    {
        int         divisions       = 200;
        float       rWidth          = currWidth;
        float       rHeight         = (float)currHeight / divisions;
        float       alpha           = 0; // percent
        float       alphaIncr       = 1f / divisions;
        int         counter         = 1;
        Rectangle2D rect            = new Rectangle2D.Float();
        for ( float yco = 0 ; yco < currHeight - rHeight ; yco += rHeight )
        {
            rect.setRect( 0, yco, rWidth, rHeight );
            Color   color   = new Color( 0f, 0f, 1f, alpha );
            gtx.setColor( color );
            gtx.fill( rect );
            alpha += alphaIncr;
            if ( alpha > 1 )
            {
                System.out.println( counter++ );
                alpha = 1;
            }
        }
    }
}
