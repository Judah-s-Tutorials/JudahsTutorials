package com.gmail.johnstraub1954.penrose2;

import static com.gmail.johnstraub1954.penrose2.PShape.D108;
import static com.gmail.johnstraub1954.penrose2.PShape.D36;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Deque;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VertexDemo2 extends JPanel
{
    private static final double     longSide    = 200;
    private static final double     shortSide   = 
        longSide * (Math.sin( D36 ) / Math.sin( D108 ));
    private static final Point2D    begin       = 
        new Point2D.Double( 0, 0 );
    private final Deque<Vertex>     queue   = new LinkedList<>();
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new VertexDemo2().build() );
    }
    
    public VertexDemo2()
    {
        Vertex  vertex  = new Vertex( begin, -36, longSide, true );
        queue.add( vertex );
        vertex = new Vertex( vertex, 72 - 180, longSide, false );
        queue.add( vertex );
        vertex = new Vertex( vertex, 36 - 180, shortSide, true );
        queue.add( vertex );
        vertex = new Vertex( vertex, 216 - 180, shortSide, false );
        queue.add( vertex );

        int winSize = (int)(2 * longSide );
        setPreferredSize( new Dimension( winSize, winSize ) );
    }

    private void build()
    {
        JFrame  frame   = new JFrame( "Vertex Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( this );
        frame.pack();
        frame.setVisible( true );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        Graphics2D  gtx = (Graphics2D)graphics.create();
        for ( Vertex vertex : queue )
        {
            Line2D  line    = vertex.getAdjLine();
            gtx.draw( line );
        }
    }
}
