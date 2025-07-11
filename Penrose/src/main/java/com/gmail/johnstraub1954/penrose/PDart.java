package com.gmail.johnstraub1954.penrose;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.gmail.johnstraub1954.penrose.utils.ColorMap;

public class PDart extends PShape implements Serializable
{
    private static final long serialVersionUID = 3715598631373436809L;
    private static final List<Vertex>   queue       = new LinkedList<>();
    private static final Path2D         path        = new Path2D.Double();
    private static final double         dotXier     = .1;
    
    private static double               longSide;
    
    private static final ColorMap   defColorMap     = 
        new ColorMap(
            FILL_COLOR, new Color( 0xeb4034 ),
            EDGE_COLOR, Color.BLACK,
            SELECTED_COLOR, new Color( 0x00bfff ),
            CURR_SIDE_COLOR, new Color( 0x0150ff )
    );

    public PDart()
    {
        this( getLongSide(), 0, 0 );
    }
    
    public PDart( double longSide )
    {
        this( longSide, 0, 0 );
    }

    public PDart( double xco, double yco )
    {
        this( getLongSide(), xco, yco );
    }
    
    public PDart( double longSide, double xco, double yco )
    {
        super( longSide );
        moveTo( xco, yco );
        putColors( getClass(), defColorMap );
    }
    
    @Override
    public Path2D getPath( double longSide )
    {
        if ( path.getCurrentPoint() == null )
        {
            path.reset();
    
            List<Vertex>        queue   = getVertices( longSide );
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
                    // Get the coordinates of the dot, then let the 
                    // superclass draw the dot, because the superclass
                    // knows the trick for simulating a filled Ellipse.
                    Point2D dotCoords   = getDotCoords( next );
                    appendDot( path, dotCoords );
                }
            }
        }

        return path;
    }
    
    @Override
    public List<Vertex> getVertices()
    {
        if ( queue == null )
        {
            String  msg = "Vertex queue not initialized";
            throw new NullPointerException( msg );
        }
        return queue;
    }
    
    private List<Vertex> getVertices( double longSide )
    {
        if ( queue.isEmpty() )
        {
            PDart.longSide = longSide;
            double          shortSide   = 
                longSide * (Math.sin( D36 ) / Math.sin( D108 ));
            Point2D         begin       = new Point2D.Double( 0, 0 );
            Vertex  vertex  = new Vertex( begin, -D36, longSide, true );
            queue.add( vertex );
            vertex = new Vertex( vertex, D72 - Math.PI, longSide, false );
            queue.add( vertex );
            vertex = new Vertex( vertex, D36 - Math.PI, shortSide, true );
            queue.add( vertex );
            vertex = new Vertex( vertex, D216 - Math.PI, shortSide, false );
            queue.add( vertex );
        }

        return queue;
    }
    
    /**
     * Get the coordinates of a dot to be drawn
     * in a given Vertex.
     * The dots for this shape are drawn in the 
     * northwest and southwest corners.
     * In both cases the x-coordinates are offset
     * to the east.
     * The northwest vertex has a y-coordinate of 0,
     * so the y-coordinate for the dot 
     * will be south of the vertex,
     * and the southwest dot will have a y-coordinate
     * that is north of the vertex.
     * 
     * @param vertex    the given vertex
     * 
     * @return  the coordinates of the bounding rectangle
     *          of the dot to be drawn in the given vertex
     */
    private Point2D getDotCoords( Vertex vertex )
    {
        Point2D     vertexCoords    = vertex.getCoords();
        double      dotDiam         = longSide * dotXier;
        double      dotOffset       = dotDiam / 2;
        double      xco             = vertexCoords.getX();
        double      yco             = vertexCoords.getY();
        double      dotXco          = xco + 1.1 * dotDiam;
        double      yOffset         = dotDiam + dotOffset;
        double      dotYco          = 
            yco == 0 ? yco + yOffset : yco - yOffset - dotDiam;
        Point2D     dotCoords   = new Point2D.Double( dotXco, dotYco );
        return dotCoords;

    }
}
