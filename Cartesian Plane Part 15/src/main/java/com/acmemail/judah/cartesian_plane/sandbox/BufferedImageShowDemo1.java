package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BufferedImageShowDemo1
{
    private static final int    width   = 300;
    private static final int    height  = 300;
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        BufferedImageShowDemo1 demo = new BufferedImageShowDemo1();
        SwingUtilities.invokeLater( () -> demo.showGUI() );
    }
    
    private void showGUI()
    {
        JFrame  frame   = new JFrame( "Buffered Image Demo GUI" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( new DemoPanel() );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    @SuppressWarnings("serial")
    private class DemoPanel extends JPanel
    {
        private static final Color  bgColor = Color.LIGHT_GRAY;
        private final BufferedImage image;
        
        public DemoPanel()
        {
            BufferedImageDemo1  demo    = 
                new BufferedImageDemo1( width, height );
            image = demo.getImage();
            setPreferredSize( new Dimension( width + 100, height + 100 ) );
        }
        
        @Override
        public void paintComponent( Graphics gtx )
        {
            Color   color   = gtx.getColor();
            gtx.setColor( bgColor );
            int width   = getWidth();
            int height  = getHeight();
            gtx.fillRect( 0, 0, width, height );
            gtx.setColor( color );

            int xco     = width / 2 - image.getWidth() / 2;
            int yco     = height / 2 - image.getHeight() / 2;
            gtx.drawImage( image, xco, yco, this );
        }
    }
}
