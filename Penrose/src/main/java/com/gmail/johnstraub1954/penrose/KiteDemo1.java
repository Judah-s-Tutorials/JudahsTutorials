package com.gmail.johnstraub1954.penrose;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class KiteDemo1
{
    private PCanvas canvas;
    
    public static void main(String[] args)
    {
        String      kiteName    = PKite.class.getSimpleName();
        PShape.setDefaultFillColor( kiteName,  new Color( 0x900C3F ) );
        
        KiteDemo1   demo        = new KiteDemo1();
        SwingUtilities.invokeLater( () -> {
            demo.build(); 
            demo.canvas.addShape( new PKite( 50, 25, 0 ) );
            demo.canvas.addShape( new PKite( 50, 25, 100 ) );
        });
    }

    private void build()
    {
        JFrame  frame   = new JFrame( "Dart Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        canvas = PCanvas.getDefaultCanvas();
        frame.setContentPane( canvas );
        frame.pack();
        frame.setVisible( true );
    }
}
