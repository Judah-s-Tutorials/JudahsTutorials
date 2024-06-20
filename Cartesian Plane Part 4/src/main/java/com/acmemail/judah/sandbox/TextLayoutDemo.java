package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.Root;

/**
 * This class draws a figure for the blog.
 * 
 * @author Jack Straub
 *
 */
public class TextLayoutDemo extends JPanel
{
    private static final long serialVersionUID = 1L;
    /** The initial width of the window, in pixels. */
    private static int  initWidth           = 815;
    /** The initial height of the window, in pixels. */
    private static int  initHeight          = 515;
    /** Background color of the Canvas */
    private Color       bgColor             = new Color( .9f, .9f, .9f );
    
    /** 
     * The graphics context.
     * Made an instance variable for convenience
     * of helper methods;
     * set every time paintComponent is invoked. 
     */
    private Graphics2D  gtx         = null;
    /** 
     * The current width of the Canvas.
     * Made an instance variable for convenience
     * of helper methods;
     * set every time paintComponent is invoked. 
     */
    private int         currWidth   = 0;
    /** 
     * The current height of the Canvas; 
     * set every time paintComponent is invoked.
     */
    private int         currHeight  = 0;
    
    public static void main( String[] args )
    {
        TextLayoutDemo  canvas  = new TextLayoutDemo();
        Root                root    = new Root( canvas );
        root.start();
    }
    
    /**
     * Constructor. Sets the initial size of the window.
     * Based on
     */
    public TextLayoutDemo()
    {
        // The initial width and height of a window is set using 
        // a Dimension object.
        Dimension   size    = new Dimension( initWidth, initHeight );
        
        // IMPORTANT: set the size of the window using
        // setPreferredSize. Remember that the actual size of the
        // window may be different after being displayed.
        this.setPreferredSize( size );
    }
    
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        ////////////////////////////////////
        // Begin boilerplate
        ////////////////////////////////////
        super.paintComponent( graphics );
        gtx = (Graphics2D)graphics.create();
        currWidth = getWidth();
        currHeight = getHeight();
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, currWidth, currHeight );
        ////////////////////////////////////
        // End boiler plate
        ////////////////////////////////////

        gtx.setColor( Color.BLACK );
        Font                font    = 
            new Font( Font.MONOSPACED, Font.PLAIN, 25 );
        gtx.setFont( font );
        FontRenderContext   frc     = gtx.getFontRenderContext();
        
        String      label   = "3.14159";
        TextLayout  layout  = new TextLayout( label, font, frc );
        Rectangle2D bounds  = layout.getBounds();
        float       textXco = 50F;
        float       textYco = 50F;
        layout.draw( gtx, textXco, textYco );
        
        Rectangle2D rect    =
            new Rectangle2D.Float(
                textXco + (float)bounds.getX(),
                textYco + (float)bounds.getY(),
                (float)bounds.getWidth(),
                (float)bounds.getHeight()
            );
        gtx.draw( rect );

        ////////////////////////////////////
        // Begin boilerplate
        ////////////////////////////////////
        gtx.dispose();
        ////////////////////////////////////
        // End boiler plate
        ////////////////////////////////////
    }
}
