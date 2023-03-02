package com.acmemail.judah.anonymous_classes.lambdas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Class to display a simple window.
 * It is for demonstration purposes only.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class Canvas extends JPanel
{
    /**
     * Constructor.
     */
    public Canvas()
    {
        setPreferredSize( new Dimension( 500, 600 ) );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        Graphics2D  gtx = (Graphics2D)graphics.create();
        gtx.setColor( Color.blue );
        gtx.fillRect( 0,  0, getWidth(), getHeight() );
    }
}
