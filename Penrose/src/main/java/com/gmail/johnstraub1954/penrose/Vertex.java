package com.gmail.johnstraub1954.penrose;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Vertex
{
    private static final double toRadians   = Math.PI / 180;
    
    private final Point2D   coords;
    private final double    angle;
    private final double    length;
    private final boolean   isDotted;
    public Vertex( 
        Point2D coords, 
        double  angle, 
        double  length, 
        boolean isDotted 
    )
    {
        this.coords = coords;
        this.angle = angle * toRadians;
        this.length = length;
        this.isDotted = isDotted;
    }
    
    /**
     * Creates a Vertex derived from a given source Vertex.
     * The new Vertex will have the same values as the source
     * except for the coordinates,
     * which will be set to the given value.
     * 
     * @param source    the given source
     * @param coords    the given coordinates
     */
    public Vertex( Vertex source, Point2D coords )
    {
        this.coords = coords;
        this.angle = source.angle;
        this.length = source.length;
        this.isDotted = source.isDotted;
    }
    
    public Vertex( 
        Vertex from, 
        double angle, 
        double adjSide, 
        boolean isDotted
    )
    {
        this.coords = from.getNext();
        this.angle = (from.angle + angle * toRadians );
        this.length = adjSide;
        this.isDotted = isDotted;
    }
    
    public Point2D getCoords()
    {
        return coords;
    }

    public double getAngle()
    {
        return angle;
    }
    
    public double getSlope()
    {
        Point2D next    = getNext();
        double  slope   = (coords.getY() - next.getY()) / (coords.getX() - next.getX() );
        return slope;
    }
    
    public double getLength()
    {
        return length;
    }
    
    public boolean isDotted()
    {
        return isDotted;
    }
    
    public Point2D getNext()
    {
        double  xco     = coords.getX() + Math.cos( angle ) * length;
        double  yco     = coords.getY() - Math.sin( angle ) * length;
        Point2D end     = new Point2D.Double( xco, yco );
        return end;
    }

    public Line2D getAdjLine()
    {
        Point2D end     = getNext();
        Line2D  line    = new Line2D.Double( coords, end );
        return line;
    }
    
    public Line2D getEdge()
    {
        Point2D next    = getNext();
        Line2D  edge    = new Line2D.Double( coords, next );
        return edge;
    }

    public Line2D getEdge( Vertex other )
    {
        Line2D  edge    =
            new Line2D.Double( coords, other.getCoords() );
        return edge;
    }
}
