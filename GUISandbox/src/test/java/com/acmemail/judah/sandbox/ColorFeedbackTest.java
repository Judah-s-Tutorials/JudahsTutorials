package com.acmemail.judah.sandbox;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.sandbox.test_utils.Utils;

class ColorFeedbackTest
{
    private static String   initText    = "0x888888";
    private static String   uniqueText  = "0x0000ff";
    
    private JTextField      textField;
    private ColorFeedback   colorFB;
    
    @BeforeEach
    public void beforeEach()
    {
        postFeedbackComponent();
    }
    
    @Test
    void testColorFeedback()
    {
        fail("Not yet implemented");
    }
    
    @Test
    public void testErrorInput()
    {
        textField.setText( "q" );
        pushEnter();
        testErrorBackground();
    }
    
    @Test
    public void testColors()
    {
        int     initInt     = Integer.decode( initText );
        Color   initColor   = new Color( initInt );
        testBackground( initColor );
        
        textField.setText( uniqueText );
        pushEnter();
        int     uniqueInt   = Integer.decode( uniqueText );
        Color   uniqueColor = new Color( uniqueInt );
        testBackground( uniqueColor );
    }

    @Test
    void testGetColor()
    {
        int     initInt     = Integer.decode( initText );
        Color   initColor   = new Color( initInt );
        assertEquals( initColor, colorFB.getColor() );
        
        textField.setText( uniqueText );
        pushEnter();
        int     uniqueInt   = Integer.decode( uniqueText );
        Color   uniqueColor = new Color( uniqueInt );
        assertEquals( uniqueColor, colorFB.getColor() );
    }

    private void postFeedbackComponent()
    {
        Exception   exc = Utils.invokeAndWait( () -> buildFeedbackFrame() );
        assertNull( exc );
    }
    
    private void buildFeedbackFrame()
    {
        JFrame  frame   = new JFrame();
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        
        JPanel  contentPane = new JPanel( new GridLayout( 1, 2 ) );
        textField = new JTextField( initText, 12 );
        colorFB = new ColorFeedback( textField );
        contentPane.add( textField );
        contentPane.add( colorFB );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    private BufferedImage getImage()
    {
        int width   = textField.getWidth();
        int height  = textField.getHeight();
        int type    = BufferedImage.TYPE_INT_RGB;
        BufferedImage   image   = new BufferedImage( width, height, type );
        Graphics        graph   = image.getGraphics();
        Utils.invokeAndWait( () -> colorFB.paintComponent( graph ) );
        return image;
    }
    
    private void testBackground( Color expColor )
    {
        final int colorMask = 0x00ffffff;
        int width       = colorFB.getWidth();
        int height      = colorFB.getHeight();
        int expRGB      = expColor.getRGB() & colorMask;
        BufferedImage   bitmap  = 
            new BufferedImage( width, height, BufferedImage.TYPE_3BYTE_BGR );
        colorFB.paintComponent( bitmap.getGraphics() );
        
        IntStream.range( 0, height )
            .forEach( i -> {
                IntStream.range( 0,  width )
                    .map( j -> bitmap.getRGB( j, i ) & colorMask )
                    .forEach( actRGB -> assertEquals( expRGB, actRGB ) );
            });
    }
    
    private void testErrorBackground()
    {
        int             width       = colorFB.getWidth();
        int             height      = colorFB.getHeight();
        int             type        = BufferedImage.TYPE_INT_RGB;
        BufferedImage   image       = new BufferedImage( width, height, type );
        Graphics        graph       = image.getGraphics();
        Utils.invokeAndWait( () -> colorFB.paintComponent( graph ) );
        List<Integer>   allColors   = new ArrayList<>();
        for ( int row = 0 ; row < height ; ++row )
            for ( int col = 0 ; col < width ; ++col )
            {
                int color   = image.getRGB( col, row );
                if ( !allColors.contains( color ) )
                    allColors.add( color );
            }
        System.out.println( allColors.size() );
    }
    
    private void pushEnter()
    {
        try
        {
            Robot   robot   = new Robot();
            robot.keyPress( KeyEvent.VK_ENTER );
            robot.keyRelease( KeyEvent.VK_ENTER );
            Utils.pause( 500 );
        }
        catch ( AWTException exc )
        {
            fail( exc );
        }
    }
}
