package com.gmail.johnstraub1954.penrose.matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gmail.johnstraub1954.penrose.Vertex;

public class Matcher
{
    private final List<VDistance>       distances;
    
    public Matcher( 
        Collection<Vertex> fromVertices, 
        Collection<Vertex> toVertices
    )
    {
        distances = new ArrayList<>();
        for ( Vertex fromVertex : fromVertices )
            for ( Vertex toVertex : toVertices )
                if ( fromVertex.isDotted() == toVertex.isDotted() )
                {
                    VDistance   dist    = 
                        new VDistance( fromVertex, toVertex );
                    distances.add( dist );
                }
    }
    
    public List<VertexPair[]> match()
    {
        List<VertexPair[]> results  = new ArrayList<>();
        for ( VDistance dist : distances )
        {
            VertexPair[]    pairs   = null;
            if ( (pairs = dist.match( distances )) != null )
                if ( !contains( pairs[0], results ) )
                {
                    results.add( pairs );
                }
        }
        return results;
    }
    
    private static boolean 
    contains( VertexPair pair, List<VertexPair[]> pairs )
    {
        boolean contains    = false;
        VDistance   targetVDist = new VDistance( pair );
        Distance    targetDist  = targetVDist.getDistance();
        for ( VertexPair[] testPair : pairs )
        {
            VDistance   testVDist   = new VDistance( testPair[0] );
            Distance    testDist    = testVDist.getDistance();
            if ( targetDist.equals( testDist ) )
                contains = true;
        }
        return contains;
    }
}
