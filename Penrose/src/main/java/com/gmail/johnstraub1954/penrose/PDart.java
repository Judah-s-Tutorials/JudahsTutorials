package com.gmail.johnstraub1954.penrose;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class PDart extends PShape
{
    private static final long serialVersionUID = 3715598631373436809L;
    private static final Deque<Vertex>  queue       = new LinkedList<>();
    private static final Path2D         path        = new Path2D.Double();
    private static final double         dotXier     = .1;
    
    private static double               longSide;

    
    public PDart( double longSide )
    {
        this( longSide, 0, 0 );
    }
    
    public PDart( double longSide, double xco, double yco )
    {
        super( longSide );
        moveTo( xco, yco );
    }
    
    @Override
    public Path2D getPath( double longSide )
    {
        if ( path.getCurrentPoint() == null )
        {
            path.reset();
    
            Deque<Vertex>       queue   = getVertices( longSide );
            Iterator<Vertex>    iter    = queue.iterator();
            Vertex              vertex  = iter.next();
            Point2D             coords  = vertex.getCoords();
            path.moveTo( coords.getX(), coords.getY() );
            while ( iter.hasNext() )
            {
                vertex = iter.next();
                coords = vertex.getCoords();
                path.lineTo( coords.getX(), coords.getY() );
            }
            path.closePath();
            
            for ( Vertex next : queue )
            {
                if ( next.isDotted() )
                {
                    Shape   dot = getDot( next.getCoords() );
                    path.append( dot, false );
                }
            }
        }

        return path;
    }
    
    @Override
    public Deque<Vertex> getVertices()
    {
        if ( queue == null )
        {
            String  msg = "Vertex queue not initialized";
            throw new NullPointerException( msg );
        }
        return queue;
    }
    
    @Override
    public Deque<Vertex> getVertices( double longSide )
    {
        if ( queue.isEmpty() )
        {
            PDart.longSide = longSide;
            double          shortSide   = 
                longSide * (Math.sin( D36 ) / Math.sin( D108 ));
            Point2D         begin       = new Point2D.Double( 0, 0 );
            Vertex  vertex  = new Vertex( begin, -36, longSide, true );
            queue.add( vertex );
            vertex = new Vertex( vertex, 72 - 180, longSide, false );
            queue.add( vertex );
            vertex = new Vertex( vertex, 36 - 180, shortSide, true );
            queue.add( vertex );
            vertex = new Vertex( vertex, 216 - 180, shortSide, false );
            queue.add( vertex );
        }

        return queue;
    }
    
    private Shape getDot( Point2D coords )
    {
        double      dotDiam     = longSide * dotXier;
        double      dotOffset   = dotDiam;
        double      xco         = coords.getX();
        double      yco         = coords.getY();
        double      dotXco      = xco + 1.3 * dotDiam;
        double      yOffset     = dotDiam + dotOffset;
        double      dotYco      = 
            yco == 0 ? yco + yOffset : yco - yOffset - dotDiam;
        Ellipse2D   ellipse = 
            new Ellipse2D.Double( dotXco, dotYco, dotDiam, dotDiam );
        return ellipse;

    }
}
