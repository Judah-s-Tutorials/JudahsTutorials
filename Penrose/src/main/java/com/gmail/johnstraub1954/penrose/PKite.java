package com.gmail.johnstraub1954.penrose;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.gmail.johnstraub1954.penrose.utils.ColorMap;

public class PKite extends PShape implements Serializable
{
    private static final long serialVersionUID = 3715598631373436809L;
    private static final List<Vertex>   queue       = new LinkedList<>();
    private static final Path2D         path        = new Path2D.Double();
    
    private static final ColorMap   defColorMap     = 
        new ColorMap(
            FILL_COLOR, new Color( 0x8c9fe6 ),
            EDGE_COLOR, Color.BLACK,
            SELECTED_COLOR, new Color( 0x00bfff ),
            CURR_SIDE_COLOR, new Color( 0xb80031 )
    );
    
    public PKite()
    {
        this( getLongSide(), 0, 0 );
    }

    public PKite( double longSide )
    {
        this( longSide, 0, 0 );
    }
    
    public PKite( double xco, double yco )
    {
        this( getLongSide(), xco, yco );
    }
    
    public PKite( double longSide, double xco, double yco )
    {
        super( longSide );
        initInstance( xco, yco );
    }
    
    private void initInstance( double xco, double yco )
    {
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
                    Point2D dotCoords   = getDotCoords( next.getCoords() );
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
            double          shortSide   = 
                longSide * (Math.sin( D36 ) / Math.sin( D108 ));
            Point2D         begin       = new Point2D.Double( 0, shortSide );
            Vertex  vertex  = new Vertex( begin, 36, longSide, true );
            queue.add( vertex );
            vertex = new Vertex( vertex, 72 - 180, shortSide, false );
            queue.add( vertex );
            vertex = new Vertex( vertex, 144 - 180, shortSide, true );
            queue.add( vertex );
            vertex = new Vertex( vertex, 72 - 180, longSide, false );
            queue.add( vertex );
        }

        return queue;
    }
    
    private Point2D getDotCoords( Point2D coords )
    {
        double      dotDiam     = getDotDiam();
        double      dotOffset   = dotDiam / 2;
        double      xco         = coords.getX();
        double      yco         = coords.getY();
        double      dotYco      = yco - dotOffset;
        double      xOffset     = dotDiam + dotOffset;
        double      dotXco      = 
            xco == 0 ? xco + xOffset : xco - xOffset;
        Point2D     dotCoords   = new Point2D.Double( dotXco, dotYco ); 
        return dotCoords;
    }
}
