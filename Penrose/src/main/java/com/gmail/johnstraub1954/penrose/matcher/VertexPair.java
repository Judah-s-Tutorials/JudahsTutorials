package com.gmail.johnstraub1954.penrose.matcher;

import com.gmail.johnstraub1954.penrose.Vertex;

public class VertexPair
{
    private final   Vertex      fromVertex;
    private final   Vertex      toVertex;
    private final   VDistance   vDistance;
    
    public VertexPair( Vertex fromVertex, Vertex toVertex )
    {
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
        vDistance = new VDistance( fromVertex, toVertex );
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
    
    @Override
    public String toString()
    {
        String          fromID  = getVertexID( fromVertex );
        String          toID    = getVertexID( toVertex );
        double          dDist   = getDistance( 1 );
        String          sDist   = String.format( "%6.2f", dDist );
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( fromID ).append( "->" )
            .append( toID ).append( ": " ).append( sDist );
        return bldr.toString();
    }
    
    public double getDistance()
    {
        double  dist    = vDistance.getDistance().getDistance();
        return dist;
    }
    
    public double getDistance( int places )
    {
        final double    xier    = 10 * places;
        double          dist    = getDistance();
        int             iDist   = (int)(dist * xier + .5);
        double          dDist   = iDist / xier;
        return dDist;
    }
    
    private static String getVertexID( Vertex vertex )
    {
        String  longString  = vertex.toString();
        int     inx         = longString.indexOf( "Vertex" );
        String  shortString = longString.substring( inx );
        return shortString;
    }
}
