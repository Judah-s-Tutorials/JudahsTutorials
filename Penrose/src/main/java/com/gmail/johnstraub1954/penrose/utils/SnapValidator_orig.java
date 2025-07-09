package com.gmail.johnstraub1954.penrose.utils;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.penrose.PShape;
import com.gmail.johnstraub1954.penrose.Vertex;
import com.gmail.johnstraub1954.penrose.sandbox.TempPanel;

public class SnapValidator_orig
{
    private static Toolkit              toolkit = Toolkit.getDefaultToolkit();
    private static DateTimeFormatter    formatter = 
        DateTimeFormatter.ofPattern("HH:mm:ss");
    private final List<PShape>      allShapes;
    private final PShape            fromShape;
    private final PShape            toShape;
    private Neighborhood            neighborhood    = null;
    
    private static TempPanel    panel;
    
//    public static void main( String[] args )
//    {
//        try
//        {
//            SwingUtilities.invokeAndWait( () -> panel = new TempPanel() );
//        }
//        catch ( InterruptedException | InvocationTargetException exc )
//        {
//            exc.printStackTrace();
//            System.exit( 1 );
//        }
//        validIntersectionAtOnePoint();
//        validIntersectionAtMultiplePoints();
//        invalidIntersectionAtMultiplePoints();
//        System.exit( 0 );
//    }
    
    private static void validIntersectionAtMultiplePoints()
    {
        Point2D pointA  = new Point2D.Double( 20, 20 );
        Point2D pointB  = new Point2D.Double( 100, 100 );
        Point2D pointC  = new Point2D.Double( 20, 100 );
        Line2D  line1   = new Line2D.Double( pointA, pointB );
        Line2D  line2   = new Line2D.Double( pointA, pointB );
        Line2D  line3   = new Line2D.Double( pointA, pointC );
        Line2D  line4   = new Line2D.Double( pointA, pointC );

        IntStream.iterate( 0, i -> i < 3, i -> i + 1 ).forEach(i -> {
//            test( line1, line2, i );
//            test( line3, line4, i );
        });
    }
    
    private static void validIntersectionAtOnePoint()
    {
        Point2D point10_10  = new Point2D.Double( 20, 10 );
        Point2D point20_10  = new Point2D.Double( 120, 10 );
        Point2D point10_20  = new Point2D.Double( 220, 10 );
        Line2D  line1       = new Line2D.Double( point10_10, point20_10 );
        Line2D  line2       = new Line2D.Double( point20_10, point10_20 );
        
        Point2D pointA  = new Point2D.Double( 20, 40 );
        Point2D pointB  = new Point2D.Double( 200, 40 );
        Point2D pointC  = new Point2D.Double( 20, 100 );
        Point2D pointD  = new Point2D.Double( 200, 100 );
        Line2D  line3   = new Line2D.Double( pointA, pointB );
        Line2D  line4   = new Line2D.Double( pointA, pointC );
        Line2D  line5   = new Line2D.Double( pointB, pointD );

        IntStream.iterate( 0, i -> i < 4, i -> i + 1 ).forEach(i -> {
            test( line1, line2, i );
            test( line3, line4, i );
            test( line3, line5, i );
        });
    }
    
    private static void invalidIntersectionAtMultiplePoints()
    {
        Point2D baseLeft    = new Point2D.Double( 40, 40 );
        Point2D baseRight   = new Point2D.Double( 100, 100 );
        Point2D farLeft     = new Point2D.Double( 20, 20 );
        Point2D farRight    = new Point2D.Double( 120, 120 );
        Point2D intLeft     = new Point2D.Double( 60, 60 );
        Point2D intRight    = new Point2D.Double( 90, 90 );
        Line2D  base        = new Line2D.Double( baseLeft, baseRight );
        Line2D  matchLeft   = new Line2D.Double( baseLeft, intRight );
        Line2D  matchRight  = new Line2D.Double( intLeft, baseRight );
        Line2D  matchNoneA  = new Line2D.Double( farLeft, farRight );
        Line2D  matchNoneB  = new Line2D.Double( intLeft, intRight );
        Line2D  matchNoneC  = new Line2D.Double( farLeft, intRight );
        Line2D  matchNoneD  = new Line2D.Double( intLeft, farRight );

        IntStream.iterate( 0, i -> i < 3, i -> i + 1 ).forEach(i -> {
            test( base, matchLeft, i );
            test( base, matchRight, i );
            test( base, matchNoneA, i );
            test( base, matchNoneB, i );
            test( base, matchNoneC, i );
            test( base, matchNoneD, i );
        });
    }
    
    private static void test( Line2D line1, Line2D line2, int config )
    {
        Point2D point11 = line1.getP1();
        Point2D point12 = line1.getP2();
        Point2D point21 = line2.getP1();
        Point2D point22 = line2.getP2();
        Line2D  lineA   = new Line2D.Double();
        Line2D  lineB   = new Line2D.Double();
        switch ( config )
        {
        case 0:
            // as configured
            lineA.setLine( point11, point12 );
            lineB.setLine( point21, point22 );
            break;
        case 1:
            // reverse endpoint in line 1
            lineA.setLine( point12, point11 );
            lineB.setLine( point21, point22 );
            break;
        case 2:
            // reverse endpoints in line 2
            lineA.setLine( point11, point12 );
            lineB.setLine( point22, point21 );
            break;
        case 3:
            // reverse endpoints in line both lines
            lineA.setLine( point12, point11 );
            lineB.setLine( point22, point21 );
            break;
        }
        
        panel.removeAll();
        panel.addShape( lineA );
        panel.addShape( lineB );
        panel.repaint();
        test( lineA, lineB );
        JOptionPane.showMessageDialog( null, "ready?" );
    }
    
    private static void test( Line2D line1, Line2D line2 )
    {
        boolean valid       = testEndpoints( line1, line2 );
        String  strLine1    = formatLine( line1 );
        String  strLine2    = formatLine( line2 );
        String  feedback    =
            strLine1 + " / " + strLine2 + ": " + valid;
        System.out.println( "###############################" );
        System.out.println( feedback );
    }
    
    private static String formatLine( Line2D line )
    {
        String  format  = "[%s -> %s]";
        String  point1  = formatPoint( line.getP1() );
        String  point2  = formatPoint( line.getP2() );
        String  result  = String.format( format, point1, point2 );
        return result;
        
    }
    
    private static String formatPoint( Point2D point )
    {
        String  format  = "(%3.0f,%3.0f)";
        String  result  = String.format( format, point.getX(), point.getY() );
        return result;
        
    }
    
    public SnapValidator_orig( SelectionManager mgr )
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
            print( fromShape.getTransformedVertices(), toShape.getTransformedVertices() );
            if ( !validateAllVertices() )
                result = false;
            else if ( !validateAllSides() )
                result = false;
            else
                result = true;
            fromShape.moveTo( originalCoordinates );
        }
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
    
    private static void 
    print( List<Vertex> fromVertices, List<Vertex> toVertices )
    {
        if ( toolkit.getLockingKeyState( KeyEvent.VK_CAPS_LOCK ) )
        {
            LocalTime   now     =  LocalTime.now();
            String      time    = now.format( formatter );
            String      fromStr = Utils.print( "from", fromVertices );
            String      toStr   = Utils.print( "to", toVertices );
            System.out.println( "########## " + time + " ##########" );
            System.out.println( fromStr );
            System.out.println( "**********************************" );
            System.out.println( toStr );
        }
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
//        long    count   = list.stream()
//            .filter( v -> Utils.match( toTest.getSlope(), v.getSlope() ) )
//            .filter( toTest::matches )
//            .map( Vertex::getAdjLine )
//            .filter( l -> !testOneLine( side, l ) )
//            .limit( 1 )
//            .count();
//        boolean valid   = count == 0;
        boolean valid   = true;
        double  toTestSlope = toTest.getSlope();
        int     numVertices = list.size();
        for ( int inx = 0 ; inx < numVertices && valid ; ++inx )
        {
            Vertex  vertex  = list.get( inx );
            if ( Utils.match( toTestSlope, vertex.getSlope() ) )
            {
                Line2D  side    = vertex.getAdjLine();
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

