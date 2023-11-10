package com.acmemail.judah.cartesian_plane.sandbox.fbc_test_demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ImageIconDemo1
{
    private final Image testImage;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        ImageIconDemo1  demo    = new ImageIconDemo1();
        SwingUtilities.invokeLater( () -> demo.makeGUI() );
    }
    
    public ImageIconDemo1()
    {
        int     type        = BufferedImage.TYPE_INT_ARGB;
        int     width       = 300;
        int     height      = 75;
        Stroke  stroke      = new BasicStroke( 3 );
        Color   background  = Color.ORANGE;
        Color   foreground  = Color.MAGENTA;
        Line2D  line1       = new Line2D.Double( 0, 0, width, height );
        Line2D  line2       = new Line2D.Double( width, 0, 0, height );
        
        BufferedImage   bufImage    = 
            new BufferedImage( width, height, type );
        Graphics2D      gtx         = bufImage.createGraphics();
        gtx.setColor( background );
        gtx.fillRect( 0, 0, width, height );
        gtx.setColor( foreground );
        gtx.setStroke( stroke );
        gtx.draw( line1 );
        gtx.draw( line2 );
        
        testImage = bufImage;
    }
    
    private void makeGUI()
    {
        JFrame  frame   = new JFrame( "ImageIcon Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel();
        Icon    icon    = new ImageIcon( testImage );
        JLabel  label   = new JLabel( icon );
        pane.add( label );
        
        frame.setContentPane( pane );
        frame.setLocation( 300, 300 );
        frame.pack();
        frame.setVisible( true );
    }
}
