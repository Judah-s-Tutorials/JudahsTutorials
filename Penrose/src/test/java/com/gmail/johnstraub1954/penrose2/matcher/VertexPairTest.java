package com.gmail.johnstraub1954.penrose2.matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

import com.gmail.johnstraub1954.penrose.Vertex;
import com.gmail.johnstraub1954.penrose.matcher.VDistance;
import com.gmail.johnstraub1954.penrose.matcher.VertexPair;

class VertexPairTest
{
    @Test
    void testVertexPairVertexVertex()
    {
        Point2D     coords  = new Point2D.Double( 0, 0 );
        Vertex      vertexA = new Vertex( coords, 1, 2, true );
        Vertex      vertexB = new Vertex( coords, 3, 4, false );
        VertexPair  pair    = new VertexPair( vertexA, vertexB );
        assertEquals( vertexA, pair.getFromVertex() );
        assertEquals( vertexB, pair.getToVertex() );
    }

    @Test
    void testVertexPairVDistance()
    {
        Point2D     coords  = new Point2D.Double( 0, 0 );
        Vertex      vertexA = new Vertex( coords, 1, 2, true );
        Vertex      vertexB = new Vertex( coords, 3, 4, false );
        VDistance   vDist   = new VDistance( vertexA, vertexB );
        VertexPair  pair    = new VertexPair( vDist );
        assertEquals( vertexA, pair.getFromVertex() );
        assertEquals( vertexB, pair.getToVertex() );
    }
}
