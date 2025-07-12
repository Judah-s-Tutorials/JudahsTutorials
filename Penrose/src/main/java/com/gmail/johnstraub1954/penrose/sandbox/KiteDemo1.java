package com.gmail.johnstraub1954.penrose.sandbox;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.penrose.PCanvas;
import com.gmail.johnstraub1954.penrose.PKite;

public class KiteDemo1
{
    private PCanvas    canvas;
    
    public static void main(String[] args)
    {
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
