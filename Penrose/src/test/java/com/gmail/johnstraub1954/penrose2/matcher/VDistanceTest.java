package com.gmail.johnstraub1954.penrose2.matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.gmail.johnstraub1954.penrose.Vertex;
import com.gmail.johnstraub1954.penrose.matcher.VDistance;
import com.gmail.johnstraub1954.penrose.matcher.VertexPair;

class VDistanceTest
{
    // The distance between any given point in list A
    // to any given point in list B must be unique. The distance
    // between any point in list A and any point in list B
    // must not be 5.
    private static final List<VDistance>    listA   = new ArrayList<>();
    private static final List<VDistance>    listB   = new ArrayList<>();
    
    /**
     * Base x-coordinate for a sample vertex to use for testing.
     * @see #vertexA
     */
    private final double    xco     = 4;
    /**
     * Base y-coordinate for a sample vertex to use for testing.
     * @see #vertexA
     */
    private final double    yco     = 0;
    /**
     * The expected distance between two sample vertices 
     * to use for testing.
     * @see #vertexA
     */
    private final double    diff    = 2;
    /**
     * The coordinates of the first of two sample vertices 
     * to use for testing.
     * @see #vertexA
     */
    private final Point2D   point1  = new Point2D.Double( xco, yco );
    /**
     * The coordinates of the second of two sample vertices 
     * to use for testing.
     * @see #vertexA
     * @see #vertexB
     */
    private final Point2D   point2  = 
        new Point2D.Double( xco + diff, yco );
    
    /** 
     * First of a pair of vertices that may be used for testing. It is
     * guaranteed that the distance between the vertices is not 5.
     */
    private final Vertex    vertexA = new Vertex( point1, 0, 0, true );
    /** 
     * First of a pair of vertices that may be used for testing. It is
     * guaranteed that the distance between the vertices is not 5.
     */
    private final Vertex    vertexB = new Vertex( point2, 1, 1, false );
    
    /**
     * This method is principally for creating a list
     * of VDistances for use in testing.
     * The VDistances must be unique
     * and none can encapsulate a value of 5.
     */
    @BeforeAll
    public static void beforeAll()
    {
        final double radians    = Math.PI / 180;
        final double sin45      = Math.sin( 45 * radians );
        final double cos45      = Math.cos( 45 * radians );
        
        Random  randy   = new Random( 1 );
        for ( int inx = 0 ; inx < 20 ; ++inx )
        {
            double      xco1    = randy.nextDouble( -50, 50 );
            double      yco1    = randy.nextDouble( -50, 50 );
            double      radius  = inx + .5;
            double      xco2    = xco1 + radius * cos45;
            double      yco2    = yco1 + radius * sin45;
            Point2D     pointA  = new Point2D.Double( xco1, yco1 );
            Point2D     pointB  = new Point2D.Double( xco2, yco2 );
            Vertex      vertexA = new Vertex( pointA, 0, 0, true );
            Vertex      vertexB = new Vertex( pointB, 0, 0, true );
            VDistance   vDist   = new VDistance( vertexA, vertexB );
            if ( inx % 2 == 0 )
                listA.add( vDist );
            else
                listB.add( vDist );
        }
    }

    /**
     * Validate the two-parameter constructor.
     */
    @Test
    public void testVDistanceVertexVertex()
    {
        VDistance   dist    = new VDistance( vertexA, vertexB );
        assertEquals( vertexA, dist.getFromVertex() );
        assertEquals( vertexB, dist.getToVertex() );
        assertEquals( diff, dist.getDistance().getDistance() );
        
        VertexPair  pair    = dist.getVertexPair();
        assertEquals( vertexA, pair.getFromVertex() );
        assertEquals( vertexB, pair.getToVertex() );
    }
    
    /**
     * Verify the match method in the case where the target distance
     * is not found.
     */
    @Test
    public void testMatch1()
    {
        Point2D     pointA1     = new Point2D.Double( 10, 20 );
        Vertex      vertexA1    = new Vertex( pointA1, 0, 0, true );
        Point2D     pointB1     = new Point2D.Double( 14, 23 );
        Vertex      vertexB1    = new Vertex( pointB1, 0, 0, true );
        VDistance   vDist       = new VDistance( vertexA1, vertexB1 );
        assertNull( vDist.match( listB ) );
    }
    
    /**
     * Verify that,
     * when attempting to match a VDistance object
     * to an equivalent object located in a list,
     * the match method returns
     * the correct value.
     */
    @Test
    public void testMatch2()
    {
        // Instantiate two points separated by a distance of 5
        Point2D     pointA1     = new Point2D.Double( 10, 20 );
        Point2D     pointB1     = new Point2D.Double( 14, 23 );
        
        // Instantiate two vertices separated by a distance of 5
        Vertex      vertexA1    = new Vertex( pointA1, 0, 0, true );
        Vertex      vertexB1    = new Vertex( pointB1, 0, 0, true );
        
        // Create two more points, distinct from the above two
        // separated by a distance of 5; use them to create two
        // vertices separated by a distance of 5
        Point2D     pointA2     = new Point2D.Double( 100, 200 );
        Point2D     pointB2     = new Point2D.Double( 104, 203 );
        Vertex      vertexA2    = new Vertex( pointA2, 0, 0, true );
        Vertex      vertexB2    = new Vertex( pointB2, 0, 0, true );
        
        // Encapsulate the distances between vertices A1 and B1 (5), 
        // and between A2 and B2 (5).
        VDistance   dist1       = new VDistance( vertexA1, vertexB1 );
        VDistance   dist2       = new VDistance( vertexA2, vertexB2 );
        
        // The size of the list containing sample distances.
        int         listSize    = listA.size();
        // The position in the test list at which to add the
        // source VDistance (see note1, below).
        
        int         posA        = listA.size() / 2;
        // The VDistances in listA are unique; none of them encapsulate
        // a value of 5.
        for ( int inx = 0 ; inx < listSize ; ++inx )
        {
            // Create a new list consisting of all the VDistances in
            // listA, plus the VDistance encapsulated in dist1. The
            // VDistance dist1 will be inserted in the new list at the
            // inxth position.
            List<VDistance> listTestA   = getList( dist1, inx );
            
            // (note1) If the VDistance encapsulating the source vertex
            // (dist2) is in the search list, the match logic should 
            // detect it and skip it.
            listTestA.add( posA, dist2 );
            
            // Match the distance encapsulated ion dist2 with all the
            // distances encapsulated in listTestA. A match should be
            // detected at the inxth position, where dist1 lives.
            VertexPair[]    pair    = dist2.match( listTestA );
            assertNotNull( pair, "inx = " + inx );
            assertEquals( dist2.getFromVertex(), pair[0].getFromVertex() );
            assertEquals( dist2.getToVertex(), pair[0].getToVertex() );
            assertEquals( dist1.getFromVertex(), pair[1].getFromVertex() );
            assertEquals( dist1.getToVertex(), pair[1].getToVertex() );
        }
    }
    
    /**
     * Return a list of VDistances
     * containing all the VDistance contained in {@linkplain #listA}
     * and a given VDistance
     * to be inserted at a given location.
     * 
     * @param dist  the given VDistance
     * @param inx   the given position
     * 
     * @return  
     *      a list consisting of 
     *      all the VDistance contained in {@linkplain #listA}
     *      plus a given VDistance object
     */
    private List<VDistance> getList( VDistance dist, int inx )
    {
        List<VDistance> testList   = new ArrayList<>();
        testList.addAll( listA );
        testList.add( inx, dist );
        return testList;
    }
}
