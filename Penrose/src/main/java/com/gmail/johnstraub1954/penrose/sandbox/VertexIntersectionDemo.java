package com.gmail.johnstraub1954.penrose.sandbox;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

public class VertexIntersectionDemo
{    
    private static final String  newl   = System.lineSeparator();
    public static void main( String[] args )
    {
        int     xco     = 20;
        int     yco     = 30;
        Point2D pointA1 = new Point2D.Double( xco + 1, yco );
        Point2D pointA2 = new Point2D.Double( xco - 10, yco - 10  );
        Point2D pointA3 = new Point2D.Double( xco + 3, yco + 3 );
        Point2D pointB1 = new Point2D.Double( xco - 1, yco );
        Point2D pointB2 = new Point2D.Double( xco + 10, yco - 10 );
        Point2D pointB3 = new Point2D.Double( xco + 20, yco );
        Path2D  tri1    = triangle( pointA1, pointA2, pointA3 );
        Path2D  tri2    = triangle( pointB1, pointB2, pointB3 );
        Area    area1   = new Area( tri1 );
//        formatPathIterator( area1 ); 
        Area    area2   = new Area( tri2 );
//        formatPathIterator( area1 ); 
        area1.intersect( area2 );
        formatPathIterator( area1 ); 
    }

    private static Path2D 
    triangle( Point2D point1, Point2D point2, Point2D point3 )
    {
        Path2D  path    = new Path2D.Double();
        path.moveTo( point1.getX(), point1.getY() );
        path.lineTo( point2.getX(), point2.getY() );
        path.lineTo( point3.getX(), point3.getY() );
        path.closePath();
//        formatPathIterator( path.getPathIterator( null ) );
        return path;
    }
    
    public static void formatPathIterator( Area area )
    {
        formatPathIterator( area.getPathIterator( null ) );
    }
    
    public static void formatPathIterator( PathIterator iter )
    {
        System.out.println( "###############################" );
        StringBuilder   bldr    = new StringBuilder();
        double          minXco  = Double.MAX_VALUE;
        double          maxXco  = Double.MIN_VALUE;
        double          minYco  = Double.MAX_VALUE;
        double          maxYco  = Double.MIN_VALUE;
        while ( !iter.isDone() )
        {
            double[]        coords  = new double[6];
            int             type    = iter.currentSegment( coords );
            String          strType = decodeIteratorType( type );
            bldr.append( strType ).append( ":" );
            for ( int inx = 0 ; inx < 2 ; inx += 2 )
            {
                bldr.append( ' ' )
                    .append( decodeCoords( coords, inx ) )
                    .append( newl );
            }
            if ( type == PathIterator.SEG_MOVETO )
            {
                minXco = maxXco = coords[0];
            }
            else if ( type == PathIterator.SEG_LINETO )
            {
                double  xco = coords[0];
                double  yco = coords[1];
                minXco = Math.min( minXco, xco );
                maxXco = Math.max( maxXco, xco );
                minYco = Math.min( minYco, yco );
                maxYco = Math.max( maxYco, yco );
            }
            else
                ;
            iter.next();
        }
        bldr.append( newl );
        
        double  deltaX  = Math.abs( minXco - maxXco );
        double  deltaY  = Math.abs( minYco - maxYco );
        bldr.append( "delta X: " ).append( deltaX ).append( ", " )
            .append( "delta Y: " ).append( deltaY );
        System.out.println( bldr );
    }
    
    private static String decodeCoords( double[] coords, int from )
    {
        String          strXco  = String.format( "%6.2f", coords[from] );
        String          strYco  = String.format( "%6.2f", coords[from + 1] );
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( '(' ).append( strXco ).append( ',')
            .append( strYco ).append( ')' );
        return bldr.toString();
    }
    
    private static String decodeIteratorType( int type )
    {
        String  strType = "???";
        switch ( type )
        {
        case PathIterator.SEG_MOVETO:
            strType = "moveTo";
            break;
        case PathIterator.SEG_LINETO:
            strType = "lineTo";
            break;
        case PathIterator.SEG_QUADTO:
            strType = "quadTo";
            break;
        case PathIterator.SEG_CUBICTO:
            strType = "cubicTo";
            break;
        case PathIterator.SEG_CLOSE:
            strType = "close";
            break;
        }
        return strType;
    }
}
