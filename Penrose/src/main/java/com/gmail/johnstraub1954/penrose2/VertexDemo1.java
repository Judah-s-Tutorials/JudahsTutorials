package com.gmail.johnstraub1954.penrose2;

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

public class VertexDemo1 extends JPanel
{
    private static final double     side    = 100;
    private static final Point2D    begin   = 
        new Point2D.Double( 0, 2.5 * side );
    private final Deque<Vertex>     queue   = new LinkedList<>();
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new VertexDemo1().build() );
    }
    
    public VertexDemo1()
    {
        double  angle45 = Math.PI / 4;
        double  angle90 = 2 * angle45;
        Vertex  start   = 
            new Vertex( begin, angle45, side, false );
        queue.add( start );
        for ( int inx = 0 ; inx < 3 ; ++inx )
        {
            Vertex  prev    = queue.getLast();
            Line2D  line    = prev.getAdjLine();
            Point2D coords  = line.getP2();
            Vertex  next    = new Vertex( prev, -angle90, side, false );
            queue.add( next );
        }
        int winSize = (int)Math.ceil( 4 * side );
        setPreferredSize( new Dimension( winSize, winSize ) );
    }

    private void build()
    {
        JFrame  frame   = new JFrame( "Vertex Demo 1" );
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
