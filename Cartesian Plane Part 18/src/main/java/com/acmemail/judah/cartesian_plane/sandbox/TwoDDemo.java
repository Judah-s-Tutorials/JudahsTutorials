package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TwoDDemo
{
    private static final String testStr = "i";
    private static final int    xco     = 5;
    private static final int    size    = 30;
    private static final int    bgRGB   = 0xFFFFFF;
    private static final int    fgRGB   = 0xFF;
    private static final int    rows    = (int)(1.5 * size);
    private static final int    cols    = size + xco;
    
    public static void main(String[] args)
    {
        int             type    = BufferedImage.TYPE_INT_RGB;
        BufferedImage   image   = new BufferedImage( cols, rows, type );
        Graphics2D      gtx     = image.createGraphics();
        draw( gtx );
        
        SwingUtilities.invokeLater( () -> post( image ) );
        
        for ( int yco = 0 ; yco < rows ; ++yco )
        {
            System.out.println();
            for ( int xco = 4 ; xco < cols ; ++xco )
            {
                int rgb     = image.getRGB( xco, yco ) & 0xFFFFFF;
                if ( rgb == bgRGB )
                    rgb = 0;
                System.out.printf( "%02x ", rgb );
            }
        }        
    }
    
    private static void draw( Graphics2D gtx )
    {
        gtx.setColor( new Color( bgRGB ) );
        gtx.fillRect( 0, 0, cols, rows );
        
        Font            font    = 
            new Font( Font.MONOSPACED, Font.PLAIN, size );
        gtx.setColor( new Color( fgRGB ) );
        gtx.setFont( font );
        gtx.drawString( testStr, xco, size - (int)(.3 * size) );
        
        font = font.deriveFont( Font.BOLD );
        gtx.setFont( font );
        gtx.drawString( testStr, xco, 2 * size - (int)(.6 * size) );
    }
    
    @SuppressWarnings("serial")
    private static void post( BufferedImage image )
    {
        JFrame      frame      = new JFrame( "Bold Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      leftPanel   = new JPanel() {
            @Override
            public void paintComponent( Graphics gtx )
            {
                gtx.drawImage( image, 0, 0, null );
            }
        };
        JPanel      rightPanel  = new JPanel() {
            @Override
            public void paintComponent( Graphics gtx )
            {
                draw( (Graphics2D)gtx );
            }
        };
        Dimension   size        = 
            new Dimension( image.getWidth(), image.getHeight() );
        leftPanel.setPreferredSize( size );
        rightPanel.setPreferredSize( size );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( leftPanel, BorderLayout.WEST );
        contentPane.add( rightPanel, BorderLayout.EAST );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }

}
