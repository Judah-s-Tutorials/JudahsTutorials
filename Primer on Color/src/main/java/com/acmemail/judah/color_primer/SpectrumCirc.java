package com.acmemail.judah.color_primer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

public class SpectrumCirc extends JPanel
{
    private static final long serialVersionUID = 1L;
    private static final double TWO_PI      = Math.PI * 2;
    private static final Color  BROWN       = new Color( 0xDAA06D );
    private static final double BORDER      = 10;

    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    public static void main( String[] args )
    {
        SpectrumCirc    spectrum    = new SpectrumCirc( 500 );
        Root        root        = new Root( spectrum );
        root.start();
    }
    
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
        currWidth = getWidth();
        currHeight = getHeight();
        gtx = (Graphics2D)graphics;
        gtx.setColor( BROWN );
        gtx.fillRect( 0,  0, currWidth, currHeight );
        
        double  diam        = 
            Math.min( currWidth - 2 * BORDER, currHeight - 2 * BORDER );
        double  radius      = diam / 2;
        double  centerXco   = currWidth / 2.;
        double  centerYco   = currHeight / 2.;
        double  incr        = TWO_PI / 5000;
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
