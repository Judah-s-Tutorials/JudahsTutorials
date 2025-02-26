package com.gmail.johnstraub1954.penrose.matcher;

import java.awt.geom.Point2D;
import java.util.Collection;

import com.gmail.johnstraub1954.penrose.Vertex;

/**
 * Encapsulate the distance between two vertices.
 * The expectation is that the vertices are present
 * in different PShapes.
 * 
 * @author Jack Straub
 */
public class VDistance
{
    /** 
     * Encapsulation of one of the two vertices, arbitrarily labeled
     * the "from" vertex.
     */
    private final Vertex    fromVertex;
    /** 
     * Encapsulation of the second of the two vertices, arbitrarily 
     * labeled the "to" vertex.
     */
    private final Vertex    toVertex;
    /** The distance between the two vertices. */
    private final Distance  distance;
    
    /**
     * Constructor.
     * Characterize the distance between two given vertices.
     * 
     * @param fromVertex    the "from" vertex
     * @param toVertex      the "to" vertex
     */
    public VDistance( Vertex fromVertex, Vertex toVertex )
    {
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
        Point2D fromCoords  = fromVertex.getCoords();
        Point2D toCoords    = toVertex.getCoords();
        distance = new Distance( fromCoords, toCoords );
    }
    
    /**
     * Constructor.
     * Characterize the distance between the vertices
     * in the given pair.
     * 
     * @param vertices  the given pair
     */
    public VDistance( VertexPair vertices )
    {
        this.fromVertex = vertices.getFromVertex();
        this.toVertex = vertices.getToVertex();
        Point2D fromCoords  = fromVertex.getCoords();
        Point2D toCoords    = toVertex.getCoords();
        distance = new Distance( fromCoords, toCoords );
    }
    
    /**
     * Aggregate the encapsulated vertices.
     * 
     * @return  an aggregation of the encapsulated vertices
     */
    public VertexPair getVertexPair()
    {
        VertexPair  pair    = new VertexPair( fromVertex, toVertex );
        return pair;
    }
    
    /**
     * Given a list of distances between vertices,
     * find a pair of vertices 
     * separated by the same distance
     * as that encapsulated by this VDistance.
     * If this VDistance object is present in the given list
     * it will be ignored.
     * An array is returned encapsulating the vertex pairs
     * encapsulated by this VDistance (array index 0),
     * and the vertex pairs 
     * in the discovered VDistance instance  (array index 1).
     * If no such instance is found
     * null is returned.
     * 
     * @param list  the given list of VDistances
     * 
     * @return  
     *      an array of two VertexPairs encapsulating the vertices
     *      in this VDistance object and the VDistance object
     *      discovered in the given list, or null if no such VDistance
     *      object is discovered 
     */
    public VertexPair[] match( Collection<VDistance> list )
    {
        VertexPair[]    twoPair = null;
        for ( VDistance test : list )
        {
            if ( fromVertex != test.fromVertex )
            {
                if ( distance.equals( test.distance ) )
                {
                    twoPair = new VertexPair[2];
                    twoPair[0] = new VertexPair( this );
                    twoPair[1] = new VertexPair( test );
                    break;
                }
            }
        }
        return twoPair;
    }

    /**
     * Gets the encapsulated "from" vertex.
     * 
     * @return  the encapsulated "from" vertex
     */
    public Vertex getFromVertex()
    {
        return fromVertex;
    }

    /**
     * Gets the encapsulated "to" vertex.
     * 
     * @return  the encapsulated "to" vertex
     */
    public Vertex getToVertex()
    {
        return toVertex;
    }

    /**
     * Returns the distance between the encapsulated vertices.
     * 
     * @return  the distance between the encapsulated vertices
     */
    public Distance getDistance()
    {
        return distance;
    }
}
