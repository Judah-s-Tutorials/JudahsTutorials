package com.acmemail.judah.blog_support;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StrokeDemo extends JPanel
{
    private final Color     bgColor     = new Color( .9f, .9f, .9f );
    
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    public static void main( String[] args )
    {
        StrokeDemo  demo    = new StrokeDemo( 150, 150 );
        Root        root    = new Root( demo );
        root.start();
    }
    
    public StrokeDemo( int width, int height )
    {
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
    }
    
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
        
        gtx.setColor( Color.black );
        
        ////////////////////////////////
        // Draw a thick, solid line.
        ////////////////////////////////
        Stroke  solidStroke = new BasicStroke( 3.5f );
        gtx.setStroke( solidStroke );
        gtx.drawLine( 20, 30, 120, 30 );
        
        ////////////////////////////////////////////////////////////
        // Draw the edge of a rectangle with a thin, dashed stroke
        ////////////////////////////////////////////////////////////
        
        // Stroke width. This stroke will be 1 pixel wide.
        float   width           = 1.0f;
        // The cap controls the appearance of the end of a line. 
        // BUTT means lines will have square ends.
        int     cap             = BasicStroke.CAP_BUTT;
        // The join determines how lines meet; BEVEL means lines
        // will meet with a straight edge.
        int     join            = BasicStroke.JOIN_BEVEL;
        // The miter limit determines the angle at which lines meet if 
        // they have a mitered join; it is not relevant to this example.
        float   miterLimit      = 1.0f;
        // The "dash array". This simply says that the array will consist
        // of dashes, all of which are 6 pixels long, separated by 5 pixels
        // of space.
        float[] dashes          = { 6.0f, 5.0f };
        // The "dash phase". Specifies the offset into the dash array 
        // to use at the start of a line. 0 means the line will begin
        // with a 6 pixel dash.
        float   dashPhase       = 0;
        Stroke  dashedStroke    =
            new BasicStroke(
                width,
                cap,
                join,
                miterLimit,
                dashes,
                dashPhase
            );
        gtx.setStroke( dashedStroke );
        gtx.drawRect( 20,  40, 100, 75 );

        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
}
