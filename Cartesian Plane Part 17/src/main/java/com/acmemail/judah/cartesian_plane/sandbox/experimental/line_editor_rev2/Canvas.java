package com.acmemail.judah.cartesian_plane.sandbox.experimental.line_editor_rev2;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Canvas extends JPanel
{
    private final DrawManager  drawManager = new DrawManager( this );

    private Graphics2D          gtx;
    
    public Canvas()
    {
        Dimension   screenSize  = 
            Toolkit.getDefaultToolkit().getScreenSize();
        int         targetWidth     = (int)(.5 * screenSize.width + .5);
        int         targetHeight    = (int)(.5 * screenSize.height + .5);
        
        Dimension   canvasSize  =
            new Dimension( targetWidth, targetHeight );
        setPreferredSize( canvasSize );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        gtx = (Graphics2D)graphics.create();
        
        drawManager.update( gtx );
        drawManager.drawBackground();
        drawManager.drawGridLines();
        drawManager.drawAxes();
        drawManager.drawMinorTics();
        drawManager.drawMajorTics();
        drawManager.drawText();
        
        gtx.dispose();
    }
    
    public DrawManager getDrawManager()
    {
        return drawManager;
    }
}
