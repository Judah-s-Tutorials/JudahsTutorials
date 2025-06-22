package com.gmail.johnstraub1954.penrose.utils;

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.gmail.johnstraub1954.penrose.PShape;

public class PShapeIntersection
{
    private final PShape    pShapeA;
    private final PShape    pShapeB;
    private final Area      intersection;

    private final Line2D    edge;
    private final boolean   isEdge;

    public PShapeIntersection( PShape pShapeA, PShape pShapeB )
    {
        this.pShapeA = pShapeA;
        this.pShapeB = pShapeB;
        Area    areaA   = new Area( pShapeA.getWorkShape() );
        Area    areaB   = new Area( pShapeB.getWorkShape() );
        areaA.intersect( areaB );
        intersection = areaA;
        if ( !intersection.isEmpty() )
            edge = computeEdge();
        else
            edge = null;
        isEdge = edge != null;
    }
    
    /**
     * @return the pShapeA
     */
    public PShape getPShapeA()
    {
        return pShapeA;
    }

    /**
     * @return the pShapeB
     */
    public PShape getPShapeB()
    {
        return pShapeB;
    }

    /**
     * @return the intersection
     */
    public Area getIntersection()
    {
        return intersection;
    }

    /**
     * @return the edge
     */
    public Line2D getEdge()
    {
        return edge;
    }

    /**
     * @return the isEdge
     */
    public boolean isEdge()
    {
        return isEdge;
    }
    
    public boolean isEmpty()
    {
        return intersection.isEmpty();
    }

    private Line2D computeEdge()
    {
        PathIterator    pathIter    = intersection.getPathIterator( null );
        double[]        coords      = new double[6];
        int             type        = pathIter.currentSegment( coords );
        List<Point2D>   points      = new ArrayList<>();
        Point2D         lastPoint   = getPoint( coords );
        
        // It might be an edge if the path iterator begins with a moveTo
        // and contains at least two segments.
        points.add( lastPoint );
        boolean         done        = type != PathIterator.SEG_MOVETO;
        while ( !done && !pathIter.isDone() )
        {
            pathIter.next();
            type = pathIter.currentSegment( coords );
            
            // If the intersection is an edge, every segment after
            // the first will be a close or a lineTo.
            if ( type == PathIterator.SEG_CLOSE )
                done = true;
            else if ( type != PathIterator.SEG_LINETO )
            {
                done = true;
                points.clear();
            }
            else
            {
                // If the intersection is an edge, the path iterator
                // may consist of many lineTos, but some of them will
                // be redundant.
                Point2D nextPoint   = getPoint( coords );
                if ( !points.contains( nextPoint ) )
                    points.add( nextPoint );
            }
        }
        
        // If the path iterators consists of exactly two distinct points,
        // assume that the points constitute and edge.
        Line2D  edge    = null;
        if ( points.size() == 2 )
            edge = new Line2D.Double( points.get( 0 ), points.get( 1 ) );
        return edge;
    }
    
    /**
     * Round a double pixel value to the nearest tenth of a pixel.
     * @param dValIn   the value to round 
     * @return  the rounded value
     */
    private static double round( double dValIn )
    {
        double  dValOut = dValIn * 100;
        dValOut = (int)(dValOut + .5);
        dValOut /= 100;
        return dValOut;
    }
    
    /**
     * Round a pair of coordinates and formulate a Point2D.
     * The coordinates are assumed to be present
     * in the first two elements of the given array.
     * 
     * @param coords    the given array
     * 
     * @return the Point2D containing the rounded coordinates
     */
    private Point2D getPoint( double[] coords )
    {
        double  xco     = round( coords[0] );
        double  yco     = round( coords[1] );
        Point2D point   = new Point2D.Double( xco, yco );
        return point;
    }
}
