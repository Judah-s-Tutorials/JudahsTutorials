package com.acmemail.judah.color_primer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

/**
 * Using the HSB color model,
 * fill a circle with sequential hues
 * between 0 and 360 degrees.
 */
public class SpectrumCirc extends JPanel
{
    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;
    /** Constant to use for 2 * pi. */
    private static final double TWO_PI      = Math.PI * 2;
    /** The color brown. */
    private static final Color  BROWN       = new Color( 0xDAA06D );
    /** 
     * The width of the empty border around the edges of the 
     * application window.
     */
    private static final double BORDER      = 10;
    
    /**
     * Application entry point.
     * 
     * @param args  command-line arguments, not used
     */
    public static void main( String[] args )
    {
        SpectrumCirc    spectrum    = new SpectrumCirc( 500 );
        Root        root        = new Root( spectrum );
        root.start();
    }
    
    /**
     * Constructor.
     * Sets the preferred size of the application window.
     * 
     * @param diameter  the diameter of the circle contained in the window
     */
    public SpectrumCirc( int diameter )
    {
        Dimension   dim = new Dimension( diameter, diameter );
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
        
        double  diam        = 
            Math.min( currWidth - 2 * BORDER, currHeight - 2 * BORDER );
        double  radius      = diam / 2;
        double  centerXco   = currWidth / 2.;
        double  centerYco   = currHeight / 2.;
        double  incr        = 1 / (2 * radius);
        Line2D  line        = new Line2D.Double();
        for ( double angle = 0 ; angle < TWO_PI ; angle += incr  )
        {
            double  xco     = centerXco + radius * Math.cos( angle );
            double  yco     = centerYco - radius * Math.sin( angle );
            line.setLine( centerXco, centerYco, xco, yco );
            float   hue     = (float)(angle / TWO_PI);
            System.out.println( hue );
            Color   color   = Color.getHSBColor( hue, 1f, 1f );
            gtx.setColor( color );
            gtx.draw( line );
        }
    }
}
