package com.acmemail.judah.graphics.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.acmemail.judah.graphics.Root;

@SuppressWarnings("serial")
public class FontDemo extends JPanel
{
    private final Color     bgColor     = new Color( .9f, .9f, .9f );
    private final Color     fillColor   = Color.BLUE;
    private final Color     edgeColor   = Color.BLACK;
    private final int       edgeWidth   = 3;
    
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    public static void main( String[] args )
    {
        FontDemo    demo    = new FontDemo( 400, 500 );
        Root        root    = new Root( demo );
        root.start();
    }
    
    public FontDemo( int width, int height )
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
        
        gtx.setColor( Color.BLACK );
        Font    fontA   = new Font("Helvetica", Font.PLAIN, 24 );
        gtx.setFont( fontA );
        gtx.drawString( "Helvetica, plain, 24", 20, 30 );

        Font    fontB   = new Font("Helvetica", Font.BOLD, 12 );
        gtx.setFont( fontB );
        gtx.drawString( "Helvetica, bold, 12", 20, 60 );

        Font    fontC   = new Font("Monospaced", Font.PLAIN, 14 );
        gtx.setFont( fontC );
        gtx.drawString( "Monospaced, plain, 14", 20, 75 );
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
}
