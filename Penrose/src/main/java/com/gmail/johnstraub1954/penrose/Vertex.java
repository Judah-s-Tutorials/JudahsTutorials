package com.gmail.johnstraub1954.penrose;

import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

import com.gmail.johnstraub1954.penrose.utils.Utils;

public class Vertex implements Serializable
{
    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = -5502764660017096967L;

    /**
     * Used to test the approximate equality of two 
     * floating point numbers.
     * See {@link #matches(Vertex)}.
     */
    private static final double epsilon     = .01;
    
    /**
     * The coordinates of this Vertex; 
     * the starting point of the Vertex's adjacent line.
     */
    private final Point2D   coords;
    /**
     * The starting point of the Vertex's adjacent line;
     * also, the coordinates of the next Vertex in the queue.
     */
    private final Point2D   nextCoords;
    /**
     * The angle, in radians, of the adjacent line of this Vertex.
     */
    private final double    angle;
    /**
     * The length of this Vertex's adjacent line.
     */
    private final double    length;
    /**
     * True, if this is a dotted vertex.
     */
    private final boolean   isDotted;
    
    /**
     * Constructor.
     * Fully initializes a new object
     * of the Vertex class.
     * 
     * @param coords        the coordinates of this Vertex
     * @param angle         the angle, in radians, of this Vertex's adjacent line
     *                      with respect to the adjacent line
     *                      of the previous Vertex
     * @param length        the length of this Vertex's adjacent line
     * @param isDotted      true, if this is a dotted vertex
     */
    public Vertex( 
        Point2D coords, 
        double  angle, 
        double  length, 
        boolean isDotted 
    )
    {
        this.coords = coords;
        this.angle = angle;
        this.length = length;
        this.isDotted = isDotted;
        nextCoords = calculateNext();
    }
    
    /**
     * Instantiates a new Vertex using an existing Vertex as a referent.
     * The client specifies the angle of the adjacent side
     * relative to the adjacent side of the referent.
     * The actual angle of this Vertex is defined as
     * the actual angle of the referent plus the specified angle.
     * The coordinates of this Vertex are computed
     * relative to the coordinates and angle of the referent.
     * This Vertex will be the successor of the source,
     * and the source will be the predecessor of this Vertex.
     * 
     * @param from      the referent Vertex
     * @param angle     the angle, in radians, of this Vertex relative
     *                  to the adjacent side of referent
     * @param adjSide   the length of the adjacent side of this Vertex
     * @param isDotted  true if this is a dotted Vertex
     */
    public Vertex( 
        Vertex from, 
        double angle, 
        double adjSide, 
        boolean isDotted
    )
    {
        this.coords = from.getNext();
        this.angle = (from.angle + angle );
        this.length = adjSide;
        this.isDotted = isDotted;
        nextCoords = calculateNext();
    }
    
    /**
     * Used internally to obtain a copy of a Vertex
     * with transformed coordinates.
     * 
     * @param from      the starting point of the adjacent side
     * @param next      the ending point of the adjacent side
     * @param adjSide   the length of the adjacent side
     * @param isDotted  true, if this Vertex is dotted
     */
    private Vertex( 
        Point2D from, 
        Point2D next,
        double adjSide, 
        boolean isDotted
    )
    {
        this.coords = from;
        this.nextCoords = next;
        this.angle = Utils.radians( from, next );
        this.length = adjSide;
        this.isDotted = isDotted;
    }
    
    /**
     * Given the starting point ({@link #coords}) 
     * and angle ({@link #angle}) of this Vertex,
     * calculate the coordinates of the endpoint of the adjacent line
     * (which are also the coordinates
     * of the next Vertex in this shape).
     * 
     * @return  a new Vertex with transformed coordinates
     */
    private Point2D calculateNext()
    {
        double  nextXco = coords.getX() + Math.cos( angle ) * length;
        double  nextYco = coords.getY() - Math.sin( angle ) * length;
        Point2D next    = new Point2D.Double( nextXco, nextYco );
        return next;
    }
    
    /**
     * Get the coordinates of this Vertex.
     * 
     * @return  the coordinates of this Vertex
     */
    public Point2D getCoords()
    {
        return coords;
    }

    /**
     * Get the angle of this Vertex.
     * 
     * @return  the angle of this Vertex
     */
    public double getAngle()
    {
        return angle;
    }
    
    /**
     * Get the slope of the adjacent line of this Vertex.
     * 
     * @return  the slope of the adjacent line of this Vertex
     */
    public double getSlope()
    {
        Point2D next    = getNext();
        double  slope   = Utils.slope( coords, next );
        return slope;
    }
    
    /**
     * Get the length of the adjacent line of this Vertex.
     * 
     * @return  the length of the adjacent line of this Vertex
     */
    public double getLength()
    {
        return length;
    }
    
    /**
     * Returns true if this is a dotted vertex.
     * 
     * @return  true if this is a dotted vertex
     */
    public boolean isDotted()
    {
        return isDotted;
    }
    
    /**
     * Get this Vertex's "next" coordinates.
     * The next coordinates constitute the endpoint of this Vertex,
     * and the starting point of the next.
     * 
     * @return  this Vertex's "next" coordinates
     */
    public Point2D getNext()
    {
        return nextCoords;
    }

    /**
     * Get this Vertex's adjacent line.
     * 
     * @return  this Vertex's adjacent line
     */
    public Line2D getAdjLine()
    {
        Point2D end     = getNext();
        Line2D  line    = new Line2D.Double( coords, end );
        return line;
    }
    
    /** 
     * Get a copy of this Vertex,
     * but with coordinates translated and rotated
     * as required by the given affine transform.
     * 
     * @param transform the given affine transform
     * 
     * @return  a copy of this Vertex with transformed coordinates
     */
    public Vertex getVertex( AffineTransform transform )
    {
        Point2D newCoords   = transform.transform( coords, null );
        Point2D newNext     = transform.transform( nextCoords, null );
        Vertex  vertex      = 
            new Vertex( newCoords, newNext, length, isDotted );
        return vertex;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr   = new StringBuilder();
        String  xco     = String.format( "%5.1f", coords.getX() );
        String  yco     = String.format( "%5.1f", coords.getY() );
        bldr.append( "(" );
        bldr.append( xco ).append( "," ).append( yco );
        bldr.append( ") " );
        return bldr.toString();
    }
    
    /**
     * Returns true if this Vertex has the same coordinates
     * as a given Vertex after accounting for floating point
     * rounding errors.
     * 
     * @param that  the given Vertex
     * 
     * @return  
     *      true if this Vertex has the same coordinates
     *      as a given Vertex after accounting for floating point
     *      rounding errors
     */
    public boolean matches( Vertex that )
    {
        boolean match   = 
            Math.abs( coords.getX() - that.coords.getX() ) < epsilon &&
            Math.abs( coords.getY() - that.coords.getY() ) < epsilon;
        return match;
    }
}
