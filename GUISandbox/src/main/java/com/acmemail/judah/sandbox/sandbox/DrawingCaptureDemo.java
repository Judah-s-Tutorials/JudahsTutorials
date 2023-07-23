package com.acmemail.judah.sandbox.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.acmemail.judah.sandbox.ColorFeedback;

public class DrawingCaptureDemo
{
    private static String   initText    = "0x888888";
    
    private JTextField      textField;
    private ColorFeedback   colorFB;
    private PictureFrame    pictureFrame;
    
    public static void main(String[] args)
    {
        new DrawingCaptureDemo().execute();
    }
    
    private void execute()
    {
        startPictureFrame();
        startFeedbackFrame();
        textField.addActionListener( e -> reset() );
    }
    
    private void reset()
    {
        colorFB.repaint();
        int width   = colorFB.getWidth();
        int height  = colorFB.getHeight();
        BufferedImage   bitmap  = 
            new BufferedImage( width, height, BufferedImage.TYPE_3BYTE_BGR );
        colorFB.paintComponent( bitmap.getGraphics() );
        pictureFrame.setBitmap( bitmap );
    }
    
    private void startPictureFrame()
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> {
                JFrame          frame           = new JFrame( "Show Bitmap" );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                pictureFrame = new PictureFrame();
                frame.setContentPane( pictureFrame );
                frame.setLocation( 200, 200 );
                frame.pack();
                frame.setVisible( true );
            });
        }
        catch ( InvocationTargetException | InterruptedException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );            }
    }
    
    private void startFeedbackFrame()
    {
        JFrame  frame   = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  contentPane = new JPanel( new GridLayout( 1, 2 ) );
        textField = new JTextField( initText, 12 );
        colorFB = new ColorFeedback( textField );
        contentPane.add( textField );
        contentPane.add( colorFB );
        frame.setLocation( 100, 100 );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }

    @SuppressWarnings("serial")
    private class PictureFrame extends JPanel
    {
        private final Color  bgColor = Color.LIGHT_GRAY;
        
        private BufferedImage   bitmap  = null;

        public PictureFrame()
        {
            setPreferredSize( new Dimension( 200, 200 ) );
        }
        
        @Override
        public void paintComponent( Graphics graphics )
        {
            Graphics2D  gtx     = (Graphics2D)graphics.create();
            int         width   = getWidth();
            int         height  = getHeight();
            gtx.setColor( bgColor );
            gtx.fillRect( 0,  0,  width,  height );
            
            if ( bitmap != null )
            {
                int     bmWidth     = bitmap.getWidth();
                int     bmHeight    = bitmap.getHeight();
                int     xco         = (width - bmWidth) / 2;
                int     yco         = (height - bmHeight) / 2;
                gtx.drawImage( bitmap, xco, yco, bgColor, null );
            }
        }
        
        public void setBitmap( BufferedImage bitmap )
        {
            this.bitmap = bitmap;
            repaint();
        }
    }
}
