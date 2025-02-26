package com.gmail.johnstraub1954.penrose;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Vertex
{
    private static final double toRadians   = Math.PI / 180;
    
    private final Point2D   coords;
    private final double    angle;
    private final double    adjSide;
    private final boolean   isDotted;
    public Vertex( 
        Point2D coords, 
        double  angle, 
        double  adjSide, 
        boolean isDotted 
    )
    {
        this.coords = coords;
        this.angle = angle * toRadians;
        this.adjSide = adjSide;
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
        this.adjSide = source.adjSide;
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
        this.adjSide = adjSide;
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
    
    public double getAdjSide()
    {
        return adjSide;
    }
    
    public boolean isDotted()
    {
        return isDotted;
    }
    
    public Point2D getNext()
    {
        double  xco     = coords.getX() + Math.cos( angle ) * adjSide;
        double  yco     = coords.getY() - Math.sin( angle ) * adjSide;
        Point2D end     = new Point2D.Double( xco, yco );
        return end;
    }

    public Line2D getAdjLine()
    {
        Point2D end     = getNext();
        Line2D  line    = new Line2D.Double( coords, end );
        return line;
    }
}
