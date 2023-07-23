package com.acmemail.judah.sandbox;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ColorFeedbackTest
{
    private static String   initText    = "0x888888";
    
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
    void testGetColor()
    {
        fail("Not yet implemented");
    }

    private void postFeedbackComponent()
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> buildFeedbackFrame() );
        }
        catch ( InvocationTargetException | InterruptedException exc )
        {
            fail( exc );
        }
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
}
