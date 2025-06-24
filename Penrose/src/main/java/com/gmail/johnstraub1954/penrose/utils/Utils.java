package com.gmail.johnstraub1954.penrose.utils;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;

import com.gmail.johnstraub1954.penrose.PShape;

public class Utils
{
    private static final String  newl   = System.lineSeparator();
    /**
     * Default constructor; not used.
     */
    private Utils()
    {
        // not used
    }

    public static String formatIntersection( PShape pShapeA, PShape pShapeB )
    {
        String  result  = "";
        Area    areaA   = new Area( pShapeA.getWorkShape() );
        Area    areaB   = new Area( pShapeB.getWorkShape() );
        areaA.intersect(areaB);
        if ( !areaA.isEmpty() )
            result = formatPathIterator( areaA.getPathIterator( null ) );
        return result;
    }
    
    public static String formatPathIterator( PathIterator iter )
    {
        StringBuilder   bldr    = new StringBuilder();
        while ( !iter.isDone() )
        {
            double[]        coords  = new double[6];
            int             type    = iter.currentSegment( coords );
            String          strType = decodeIteratorType( type );
            bldr.append( strType ).append( ":" );
            for ( int inx = 0 ; inx < coords.length ; inx += 2 )
            {
                bldr.append( ' ' )
                    .append( decodeCoords( coords, inx ) )
                    .append( newl );
            }
            iter.next();
        }
        return bldr.toString();
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
