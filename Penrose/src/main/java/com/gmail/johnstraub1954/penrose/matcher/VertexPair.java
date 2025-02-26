package com.gmail.johnstraub1954.penrose.matcher;

import com.gmail.johnstraub1954.penrose.Vertex;

public class VertexPair
{
    private final   Vertex    fromVertex;
    private final   Vertex    toVertex;
    
    public VertexPair( Vertex fromVertex, Vertex toVertex )
    {
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
    }
    
    public VertexPair( VDistance vDist )
    {
        this( vDist.getFromVertex(), vDist.getToVertex() );
    }
    
    public Vertex getFromVertex()
    {
        return fromVertex;
    }
    
    public Vertex getToVertex()
    {
        return toVertex;
    }
}
