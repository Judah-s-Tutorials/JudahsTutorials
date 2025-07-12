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

/**
 * An object of this class describes
 * the intersection of two PShapes,
 * allowing the user to determine if two PShapes
 * are positioned correctly.
 * In a perfect scenario, 
 * two correctly positioned PShapes
 * would not intersect at all.
 * However, due to rounding errors 
 * when rotating and positioning PShapes,
 * sometimes two PShapes will appear to overlap
 * even when they are correctly positioned.
 * If this is the case
 * the intersection will be either an edge
 * or a vertex.
 * If the intersection is non-empty,
 * and is not an edge or a vertex,
 * the positioning of the two PShapes
 * should be treated as invalid.
 * 
 * @see #isValid()
 */
public class PShapeIntersection
{
    /**
     * The first of the encapsulated shapes.
     */
    private final PShape    pShapeA;
    /**
     * The second of the encapsulated shapes.
     */
    private final PShape    pShapeB;
    /**
     * The intersection of the two encapsulated shapes;
     * may be empty.
     */
    private final Area      intersection;

    /**
     * If the intersection of the encapsulated PShapes is an edge,
     * this object describes the intersection;
     * null if the intersection is not an edge.
     */
    private final Line2D    edge;
    /**
     * If the intersection of the encapsulated PShapes is a point,
     * this object describes the intersection;
     * null if the intersection is not a point.
     */
    private final Point2D   point;

    /**
     * Evaluates the intersection of two PShapes.
     * Determines whether the intersection is empty,
     * a line, a point, or none of the above.
     *  
     * @param pShapeA   the first PShape to evaluate
     * @param pShapeB   the second PShape to evaluate
     */
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
     * Gets the first of the encapsulated PShapes.
     * 
     * @return the first of the encapsulated PShapes
     */
    public PShape getPShapeA()
    {
        return pShapeA;
    }

    /**
     * Gets the second of the encapsulated PShapes.
     * 
     * @return the second of the encapsulated PShapes
     */
    public PShape getPShapeB()
    {
        return pShapeB;
    }

    /**
     * Gets the intersection of the encapsulated PShapes.
     * 
     * @return 
     *      the intersection of the encapsulated PShapes
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
    
    /**
     * If the intersection is non-empty and not an edge,
     * it ought to be a vertex.
     * If it is a vertex, the approximate location is returned.
     * If it is not a vertex, null is returned.
     * 
     * @return  
     *      the approximate location of the vertex,
     *      or null if no vertex found
     */
    private Point2D computeVertex()
    {
        PathIterator    pathIter    = intersection.getPathIterator( null );
        boolean         isValid     = true;
        
        double          minXco  = Double.MAX_VALUE;
        double          maxXco  = Double.MIN_VALUE;
        double          minYco  = Double.MAX_VALUE;
        double          maxYco  = Double.MIN_VALUE;

        // It might be a vertex if the path iterator begins with a MOVE_TO,
        // and every remaining segment is a LINE_TO or a CLOSE.
        double[]        coords      = new double[6];
        boolean         done        = false;
        while ( !done && !pathIter.isDone() )
        {
            int type    = pathIter.currentSegment( coords );
            // If the intersection is a vertex, the first segment will
            // be a moveTo, and each following segment will be a
            // close or a lineTo.
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

    /**
     * Determine if two PShapes intersect at an edge.
     * If this is the case,
     * a Line2D object describing the edge
     * is created and returned.
     * If the intersection is not an edge
     * null is returned.
     * 
     * @return  
     *      an object describing the edge where the intersection occurred,
     *      or null if none
     */
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
