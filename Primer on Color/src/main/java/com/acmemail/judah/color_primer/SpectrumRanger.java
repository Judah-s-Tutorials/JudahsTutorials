package com.acmemail.judah.color_primer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

public class SpectrumRanger extends JPanel
{
    private static final long   serialVersionUID = 1L;
    
    private static final double TWO_PI      = Math.PI * 2;
    private static final Color  BROWN       = new Color( 0xDAA06D );
    private static final double BORDER      = 10;
    
    private static final SpectrumRanger spectrum    = 
        new SpectrumRanger( 500 );
    private static final SpectrumFrame  root        = 
        new SpectrumFrame( spectrum );
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    public static void main( String[] args )
    {
        root.start();
    }
    
    public SpectrumRanger( int diameter )
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

        double  start       = Math.toRadians( root.getHueLowerValue() );
        double  end         = Math.toRadians( root.getHueUpperValue() );
        float   sat         = root.getSaturation() / 100f;
        float   bright      = root.getBrightness() / 100f;
        for ( double angle = start ; angle < end ; angle += incr  )
        {
            double  xco     = centerXco + radius * Math.cos( angle );
            double  yco     = centerYco - radius * Math.sin( angle );
            line.setLine( centerXco, centerYco, xco, yco );
            float   hue     = (float)(angle / TWO_PI);
            Color   color   = Color.getHSBColor( hue, sat, bright );
            gtx.setColor( color );
            gtx.draw( line );
        }
    }
}
