package com.acmemail.judah.cartesian_plane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.acmemail.judah.color_primer.Root;

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
    private Graphics2D      gtx;
    
    public void main( String[] args )
    {
        ColorPrimer primer  = new ColorPrimer( 500, 600 );
        Root        root    = new Root( primer );
        root.start();
    }
    
    public ColorPrimer( int width, int height )
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
        
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
}
