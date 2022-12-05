package com.acmemail.judah.color_primer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ColorPrimer extends JPanel
{
    private Color   bgColor             = new Color( .9f, .9f, .9f );

    ///////////////////////////////////////////////////////
    //
    // The following values are recalculated every time 
    // paintComponent is invoked.
    //
    ///////////////////////////////////////////////////////
    private int             currWidth;
    private int             currHeight;
    private Font            font;
    private Graphics2D      gtx;
    
    public static void main( String[] args )
    {
        ColorPrimer primer  = new ColorPrimer( 750, 350 );
        Root        root    = new Root( primer );
        root.start();
    }
    
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
        gtx = (Graphics2D)graphics.create();
        gtx.setColor( bgColor );
        gtx.fillRect( 0,  0, currWidth, currHeight );
        // end boilerplate
        
        int         bYco    = 75;
        int         bXco    = 20;
        
        // FontMetrics seem to have completely out to lunch.
        // Eyeball dimensions, and plan on never changing anything.
        float       rXco        = 10;
        float       rYco        = 20;
        float       rWidth      = currWidth - 2 * rXco;
        float       rHeight     = rYco + 150;
        Rectangle2D rect        =
            new Rectangle2D.Float( rXco, rYco, rWidth, rHeight );
        gtx.setColor( Color.CYAN );
        gtx.fill( rect );
        
        gtx.setFont( font );
        gtx.setColor( Color.RED );
        gtx.drawString( "I am the egg man", bXco, bYco );
        gtx.drawString( "They are the egg man", bXco, bYco + 80 );
        gtx.drawString( "I am the Walrus!", bXco, bYco + 160 );
        gtx.drawString( "Goo goo g'joob", bXco, bYco + 250 );
        
        paintTransparentOverlay();
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    private void paintTransparentOverlay()
    {
        int         divisions       = 50;
        float       rWidth          = currWidth;
        float       rHeight         = currHeight / divisions;
        float       alpha           = 0; // percent
        float       alphaIncr       = 1f / divisions;
        Rectangle2D rect            = new Rectangle2D.Float();
        for ( float yco = 0 ; yco < currHeight - rHeight ; yco += rHeight )
        {
            rect.setRect( 0, yco, rWidth, rHeight );
            Color   color   = new Color( 0f, 0f, 1f, alpha );
            gtx.setColor( color );
            gtx.fill( rect );
            alpha += alphaIncr;
        }
    }
}
