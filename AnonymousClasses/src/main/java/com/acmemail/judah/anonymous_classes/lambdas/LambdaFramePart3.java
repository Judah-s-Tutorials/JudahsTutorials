package com.acmemail.judah.anonymous_classes.lambdas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LambdaFramePart3
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> buildGUI() );
    }
    
    private static void buildGUI()
    {
        JFrame  frame   = new JFrame();
        frame.setContentPane( new Canvas() );
        frame.pack();
        frame.setVisible( true );
    }

    @SuppressWarnings("serial")
    private static class Canvas extends JPanel
    {
        public Canvas()
        {
            setPreferredSize( new Dimension( 500, 600 ) );
        }
        
        public void paintComponent( Graphics graphics )
        {
            Graphics2D  gtx = (Graphics2D)graphics.create();
            gtx.setColor( Color.blue );
            gtx.fillRect( 0,  0, getWidth(), getHeight() );
        }
    }
}
