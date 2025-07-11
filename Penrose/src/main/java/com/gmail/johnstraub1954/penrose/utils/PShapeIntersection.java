package com.gmail.johnstraub1954.penrose.utils;

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import static java.awt.geom.PathIterator.SEG_CLOSE;
import static java.awt.geom.PathIterator.SEG_MOVETO;
import static java.awt.geom.PathIterator.SEG_LINETO;

import com.gmail.johnstraub1954.penrose.PShape;

public class PShapeIntersection
{
    private final PShape    pShapeA;
    private final PShape    pShapeB;
    private final Area      intersection;

    private final Line2D    edge;
    private final Point2D   point;

    public PShapeIntersection( PShape pShapeA, PShape pShapeB )
    {
        this.pShapeA = pShapeA;
        this.pShapeB = pShapeB;
        Area    areaA   = new Area( pShapeA.getWorkShape() );
        Area    areaB   = new Area( pShapeB.getWorkShape() );
        areaA.intersect( areaB );
        intersection = areaA;
        
        // temporary variables allow implementation of 
        // actual variable as final.
        Line2D  tempEdge    = null;
        Point2D tempPoint   = null;
        
        if ( intersection.isEmpty() )
            ;
        else if ( (tempEdge = computeEdge()) != null )
            ;
        else
            tempPoint = computeVertex();
        edge = tempEdge;
        point = tempPoint;
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
     * Gets the edge computed from the intersection,
     * or null if none.
     * 
     * @return the computed edge, or null if none
     */
    public Line2D getEdge()
    {
        return edge;
    }

    /**
     * Gets the single point derived from the intersection,
     * or null if none.
     * 
     * @return the computed edge, or null if none
     */
    public Point2D getPoint()
    {
        return point;
    }

    /**
     * Returns true if the intersection of the encapsulated PShapes
     * is non-empty, and the intersection is limited 
     * to a common edge.
     * 
     * @return 
     *      true if the intersection of the encapsulated PShapes
     *      is limited to a common edge
     */
    public boolean isEdge()
    {
        boolean isEdge  = edge != null;
        return isEdge;
    }

    /**
     * Returns true if the intersection of the encapsulated PShapes
     * is non-empty, and the intersection is limited 
     * to a single point.
     * 
     * @return 
     *      true if the intersection of the encapsulated PShapes
     *      is limited to a common edge
     */
    public boolean isPoint()
    {
        boolean isPoint = point != null;
        return isPoint;
    }
    
    /**
     * Returns true if the intersection 
     * of the encapsulated PShapes is empty
     * 
     * @return  true if the intersection of the encapsulated PShapes is empty
     */
    public boolean isEmpty()
    {
        boolean result  = intersection.isEmpty(); 
        return result;
    }
    
    /**
     * Returns true if this is a 'valid' intersection
     * in the context of two PShapes.
     * A valid intersection is one that is empty
     * (i.e. there is no intersection),
     * a line, or a single point.
     * 
     * @return  true if this is a valid intersection
     */
    public boolean isValid()
    {
        boolean result  = isEmpty() || isEdge() || isPoint();
        return result;
    }
    
    public static boolean intersect( PShape pShapeA, PShape pShapeB)
    {
        Area    areaA   = new Area( pShapeA.getWorkShape() );
        Area    areaB   = new Area( pShapeB.getWorkShape() );
        areaA.intersect( areaB );
        boolean result = !areaA.isEmpty();
        return result;
    }
    
    /**
     * If the intersection is non-empty and not an edge,
     * it ought to be a vertex.
     * 
     * @return
     */
    private Point2D computeVertex()
    {
        PathIterator    pathIter    = intersection.getPathIterator( null );
        double[]        coords      = new double[6];
        int             type        = pathIter.currentSegment( coords );
        boolean         isValid     = true;
        
        double          minXco  = coords[0];
        double          maxXco  = minXco;
        double          minYco  = coords[1];
        double          maxYco  = minYco;

        // It might be a vertex if the path iterator begins with a MOVE_TO,
        // and every remaining segment is a LINE_TO or a CLOSE.
        boolean         done        = type != SEG_MOVETO;
        while ( !done && !pathIter.isDone() )
        {
            // If the intersection is a vertex, every segment after
            // the first will be a close or a lineTo.
            if ( type == SEG_CLOSE )
                done = true;
            else if ( type != SEG_LINETO && type != SEG_MOVETO )
            {
                done = true;
                isValid = false;
            }
            else
            {
                // If the intersection is a vertex, the path iterator
                // may consist of many lineTos. Keep track of the min
                // and max x- and y-coordinates, for later inspection.
                double  xco = coords[0];
                double  yco = coords[1];
                minXco = Math.min( minXco, xco );
                maxXco = Math.max( maxXco, xco );
                minYco = Math.min( minYco, yco );
                maxYco = Math.max( maxYco, yco );
            }
            pathIter.next();
            type = pathIter.currentSegment( coords );
        }
        
        // If the path iterator describes a vertex, the difference between 
        // the min and max x-coordinates, and the min and max y-coordinates
        // will be less than 2.
        Point2D     vertex  = null;
        if ( isValid )
        {
            double  deltaX  = Math.abs( minXco - maxXco );
            double  deltaY  = Math.abs( minYco - maxYco );
            if ( deltaX < 2 && deltaY < 2 )
            {
                // Make a point representing a vertex with x- and y-coordinates
                // halfway between their respective min and max values.
                double  xco = (maxXco - minXco) / 2;
                double  yco = (maxYco - minYco) / 2;
                vertex = new Point2D.Double( xco, yco );
            }
        }
        return vertex;
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
        
        // If the path iterator consists of exactly two distinct points,
        // assume that the points constitute and edge.
        Line2D  edge    = null;
        if ( points.size() == 2 )
            edge = new Line2D.Double( points.get( 0 ), points.get( 1 ) );
        return edge;
    }

//    private Vertex computeVertex()
//    {
//        PathIterator    pathIter    = intersection.getPathIterator( null );
//        double[]        coords      = new double[6];
//        int             type        = pathIter.currentSegment( coords );
//        List<Point2D>   points      = new ArrayList<>();
//        Point2D         lastPoint   = getPoint( coords );
//        
//        // It might be an edge if the path iterator begins with a moveTo
//        // and contains at least two segments.
//        points.add( lastPoint );
//        boolean         done        = type != PathIterator.SEG_MOVETO;
//        while ( !done && !pathIter.isDone() )
//        {
//            pathIter.next();
//            type = pathIter.currentSegment( coords );
//            
//            // If the intersection is an edge, every segment after
//            // the first will be a close or a lineTo.
//            if ( type == PathIterator.SEG_CLOSE )
//                done = true;
//            else if ( type != PathIterator.SEG_LINETO )
//            {
//                done = true;
//                points.clear();
//            }
//            else
//            {
//                // If the intersection is an edge, the path iterator
//                // may consist of many lineTos, but some of them will
//                // be redundant.
//                Point2D nextPoint   = getPoint( coords );
//                if ( !points.contains( nextPoint ) )
//                    points.add( nextPoint );
//            }
//        }
//        
//        // If the path iterators consists of exactly two distinct points,
//        // assume that the points constitute and edge.
//        Line2D  edge    = null;
//        if ( points.size() == 2 )
//            edge = new Line2D.Double( points.get( 0 ), points.get( 1 ) );
//        return edge;
//    }
    
    // Given that there's an edge, do its endpoints map to vertices
    // in either/both shapes?
    
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
