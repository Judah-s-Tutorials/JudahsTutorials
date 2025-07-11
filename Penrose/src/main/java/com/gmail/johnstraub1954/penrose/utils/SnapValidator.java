package com.gmail.johnstraub1954.penrose.utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import com.gmail.johnstraub1954.penrose.PShape;
import com.gmail.johnstraub1954.penrose.Vertex;

public class SnapValidator
{
    private final List<PShape>      allShapes;
    private final PShape            fromShape;
    private final PShape            toShape;
    private Neighborhood            neighborhood    = null;
    
    public SnapValidator( SelectionManager mgr )
    {
        allShapes = mgr.getShapes();
        List<PShape>    selectedShapes  = mgr.getSelected();
        int             numSelected     = selectedShapes.size();
        fromShape = numSelected > 0 ? selectedShapes.get( 0 ) : null;
        toShape = numSelected > 1 ? selectedShapes.get( 1 ) : null;
    }

    public boolean validate()
    {
        boolean result              = false;
        if ( fromShape == null || toShape == null )
            result = false;
        else if ( !validateSelectedSides() )
            result = false;
        else 
        {
            Point2D originalCoordinates = fromShape.getCoordinates();
            fromShape.snapTo( toShape );
            neighborhood = new Neighborhood( fromShape, allShapes );
            if ( !validateIntersection() )
                result = false;
            else if ( !validateAllVertices() )
                result = false;
            else if ( !validateAllSides() )
                result = false;
            else
                result = true;
            fromShape.moveTo( originalCoordinates );
        }
        return result;
    }
    
    private boolean validateIntersection()
    {
        long    count   = neighborhood.stream()
            .map( s -> new PShapeIntersection( fromShape, s ) )
            .filter( val -> !val.isValid() )
            .limit( 1 )
            .count();
        boolean result = count == 0;
        return result;
    }
    
    public boolean validateSelectedSides()
    {
        boolean     canMap      = false;
        double      fromSlope   = fromShape.getCurrSlope();
        double      toSlope     = toShape.getCurrSlope();
        double      fromLength  = fromShape.getCurrLength();
        double      toLength    = toShape.getCurrLength();
        boolean[]   fromDotted  = fromShape.getCurrDotState();
        boolean[]   toDotted    = toShape.getCurrDotState();
        if ( !Utils.match( fromSlope, toSlope ) )
            canMap = false;
        else if ( !Utils.match( fromLength, toLength ) )
            canMap = false;
        else if ( fromDotted[0] != toDotted[1] )
            canMap = false;
        else
            canMap = true;
        
        return canMap;
    }
    
    public boolean validateAllSides()
    {
        boolean         valid           = true;
        List<Vertex>    fromVertices    = fromShape.getTransformedVertices();
        int             numVertices     = fromVertices.size();
        List<PShape>    testShapes      = neighborhood.getNeighbors();
        int             numTests        = testShapes.size();
        for ( int inx = 0 ; inx < numVertices && valid ; ++inx )
        {
            Vertex  vertex  = fromVertices.get( inx );
            for ( int jnx = 0 ; jnx < numTests && valid ; ++jnx )
            {
                PShape  testShape   = testShapes.get( jnx );
                List<Vertex>    testVertices    = 
                    testShape.getTransformedVertices();
                valid = testOneSide( vertex, testVertices );
            }
        }
        return valid;
    }
    
    public boolean validateAllVertices()
    {
        boolean         valid           = true;
        List<Vertex>    fromVertices    = fromShape.getTransformedVertices();
        int             numVertices     = fromVertices.size();
        List<PShape>    testShapes      = neighborhood.getNeighbors();
        int             numTests        = testShapes.size();
        for ( int inx = 0 ; inx < numVertices && valid ; ++inx )
        {
            Vertex  vertex  = fromVertices.get( inx );
            for ( int jnx = 0 ; jnx < numTests && valid ; ++jnx )
            {
                PShape  testShape   = testShapes.get( jnx );
                List<Vertex>    testVertices    = 
                    testShape.getTransformedVertices();
                valid = testOneVertex( vertex, testVertices );
            }
        }
        return valid;
    }

    private boolean testOneVertex( Vertex toTest, List<Vertex> list )
    {
        long    count   = list.stream()
            .filter( toTest::matches )
            .filter( v -> toTest.isDotted() != v.isDotted() )
            .limit( 1 )
            .count();
        boolean valid   = count == 0;
        return valid;
    }
    
    /**
     * Validate a line defined by a given Vertex 
     * against all lines in a given list of Vertices.
     * Two lines do <em>not</em> conflict if:
     * <ul>
     *      <li>They have different slopes.</li>
     *      <li>They have exactly one common endpoint.</li>
     *      <li>They have two common endpoints.
     * </ul>
     * <p>
     * False is returned if any line extrapolated from the given list
     * conflicts with the line defined by the given Vertex.
     * 
     * @param toTest    the given Vertex
     * @param list      the given list of vertices
     * 
     * @return  
     *      true, if the given Vertex is valid with respect to
     *      every Vertex in the given list
     */
    private boolean testOneSide( Vertex toTest, List<Vertex> list )
    {
        Line2D  toTestSide  = toTest.getAdjLine();
        toTestSide = Utils.round( toTestSide );
        boolean valid   = true;
        double  toTestSlope = toTest.getSlope();
        int     numVertices = list.size();
        for ( int inx = 0 ; inx < numVertices && valid ; ++inx )
        {
            Vertex  vertex  = list.get( inx );
            if ( Utils.match( toTestSlope, vertex.getSlope() ) )
            {
                Line2D  side    = vertex.getAdjLine();
                side = Utils.round( side );
                valid = testOneLine( toTestSide, side );
            }
        }
        return valid;
    }
    
    /**
     * 
     * Precondition: line1 and line2 have the same slope.
     * 
     * @param line1
     * @param line2
     * @return
     */
    private static boolean testOneLine( Line2D line1, Line2D line2 )
    {
        boolean result      = true;
        if ( Utils.intersect( line1, line2 ) )
            result = testEndpoints( line2, line1 );
        return result;
    }
    
    /**
     * Precondition: line1 and line2 have the same slope.<br>
     * Precondition: line1 and line2 intersect.<br>
     * Return true if the intersection of two given lines:
     * <ol>
     *      <li>
     *          Is a single point corresponding 
     *          to a common endpoint.
     *      </li>
     *      <li>
     *          Is more than one point
     *          and the lines have two common endpoints.
     *      </li>
     * </ol>
     * 
     * @param line2
     * @param line1
     * 
     * @return
     */
    private static boolean testEndpoints( Line2D line1, Line2D line2 )
    {
        boolean result      = false;
        Point2D line1End1   = line1.getP1();
        Point2D line1End2   = line1.getP2();
        Point2D line2End1   = line2.getP1();
        Point2D line2End2   = line2.getP2();
        // Both lines must have at least one endpoint in common
        
        // Case 1-1
        if (  Utils.match( line1End1, line2End1 ) )
        {
            // if line 1 point 1 matches line 2 point 1, then either
            // line 1 point 2 matches line 2 point 2, or neither of the
            // other endpoints falls on both lines.
            if ( Utils.match( line1End2, line2End2 ) )
                result = true;
            else if ( !Utils.liesOn( line1End2 , line2 ) &&
                    !Utils.liesOn( line2End2, line1 ) 
                )
                result = true;
            else
                result = false;
        }
        // Case 1-2
        else if (  Utils.match( line1End1, line2End2 ) )
        {
            // if line 1 point 1 match line 2 point 2, then either
            // line1 point 2 matches line 2 point 1, or neither of the
            // other endpoints falls on both lines.
            if ( Utils.match( line1End2, line2End1 ) )
                result = true;
            else if ( !Utils.liesOn( line1End2, line2 )  &&
                !Utils.liesOn( line2End1, line1 ) 
            )
                result = true;
            else
                result = false;
        }
        // Case 2-1
        else if (  Utils.match( line1End2, line2End1 ) )
        {
            // if line 1 point 2 match line 2 point 1, then either
            // line 1 point 1 matches line 2 point 2, or neither of the
            // other endpoints falls on both lines.
            if ( Utils.match( line1End1, line2End2 ) )
                result = true;
            else if ( !Utils.liesOn( line1End1, line2 ) &&
                !Utils.liesOn( line2End2, line1 ) 
            )
                result = true;
            else
                result = false;
        }
        // Case 2-2
        else if (  Utils.match( line1End2, line2End2 ) )
        {
            // if line 1 point 2 matches line 2 point 2, then either
            // line 1 point 1 matches line 2 point 1, or neither of the
            // other endpoints falls on both lines.
            if ( Utils.match( line1End1, line2End1 ) )
                result = true;
            else if ( !Utils.liesOn( line1End1, line2 ) &&
                !Utils.liesOn( line2End1, line1 ) 
            )
                result = true;
            else
                result = false;
        }
        else 
            result = false;
        return result;
    }
}

