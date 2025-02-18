package com.gmail.johnstraub1954.penrose2;

import static com.gmail.johnstraub1954.penrose2.PShape.D108;
import static com.gmail.johnstraub1954.penrose2.PShape.D36;
import static com.gmail.johnstraub1954.penrose2.PShape.DOT_XIER;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Deque;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VertexDemo3 extends JPanel
{
    private static final double     longSide    = 200;
    private static final double     shortSide   = 
        longSide * (Math.sin( D36 ) / Math.sin( D108 ));
    private static final double     height      = 
        longSide * Math.sin( D36 );
    private static final Point2D    begin       = 
        new Point2D.Double( 0, height );
    private static final double     dotDiam     = longSide * DOT_XIER;
    private static final double     dotOffset   = dotDiam / 2;
    private final Deque<Vertex>     queue   = new LinkedList<>();
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new VertexDemo3().build() );
    }
    
    public VertexDemo3()
    {
        Vertex  vertex  = new Vertex( begin, 36, longSide, true );
        queue.add( vertex );
        vertex = new Vertex( vertex, 72 - 180, shortSide, false );
        queue.add( vertex );
        vertex = new Vertex( vertex, 144 - 180, shortSide, true );
        queue.add( vertex );
        vertex = new Vertex( vertex, 72 - 180, longSide, false );
        queue.add( vertex );

        int winSize = (int)(2 * longSide);
        setPreferredSize( new Dimension( winSize, winSize ) );
    }

    private void build()
    {
        JFrame  frame   = new JFrame( "Vertex Demo 3" );
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
            if ( vertex.isDotted() )
            {
                Point2D     coords  = vertex.getCoords();
                double      xco     = coords.getX();
                double      dotXco  =
                    xco == 0 ? xco + dotOffset : xco - dotDiam - dotOffset;
                double      dotYco  = coords.getY() - dotOffset;
                Ellipse2D   ellipse = 
                    new Ellipse2D.Double( dotXco, dotYco, dotDiam, dotDiam );
                gtx.fill( ellipse );
            }
        }
    }
}
