package sandbox;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.penrose.PCanvas;
import com.gmail.johnstraub1954.penrose.PDart;
import com.gmail.johnstraub1954.penrose.PKite;
import com.gmail.johnstraub1954.penrose.PShape;
import com.gmail.johnstraub1954.penrose.PToolbar;
import com.gmail.johnstraub1954.penrose.Vertex;
import com.gmail.johnstraub1954.penrose.utils.CQueue;
import com.gmail.johnstraub1954.penrose.utils.SelectionManager;
import com.gmail.johnstraub1954.penrose.utils.Utils;

public class PShapeAreaDemo1 implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 3168160266742270027L;
    
    private static double       longSide    = 100;
    private static final String chooserTitle    = "Choose File";
    private static final String appTitle        = "Penrose Tiling";
    private final JFrame        frame           = new JFrame( appTitle );
    private final PCanvas       canvas          = PCanvas.getDefaultCanvas();
    private final JFileChooser  chooser;
    
    public static void main(String[] args)
    {
        PShapeAreaDemo1 demo2   = new PShapeAreaDemo1();
        PShape.setLongSide( longSide );

        SwingUtilities.invokeLater( () -> {
            demo2.build();
            demo2.canvas.addShape( new PKite( 0, 0 ) );
            demo2.canvas.addShape( new PDart( longSide, 100, 120 ) );
            demo2.canvas.addShape( new PKite( longSide, 0, 150 ) );
            demo2.canvas.addShape( new PDart( longSide, 100, 250 ) );
            demo2.canvas.setTweaker( demo2::tweak );
            demo2.canvas.setGridSpacing( 20 );
            demo2.canvas.showGrid( true );
        });
    }
    
    public PShapeAreaDemo1()
    {
        String  userDir = System.getProperty( "user.dir" );
        File    baseDir = new File( userDir );
        chooser = new JFileChooser( baseDir );
        chooser.setDialogTitle( chooserTitle );
    }
    
    public void build()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel( new BorderLayout() );
        pane.add( canvas, BorderLayout.CENTER );
        PToolbar toolbar         = new PToolbar();
        pane.add( toolbar.getJToolbar(), BorderLayout.NORTH );
        
        frame.setContentPane( pane );
        frame.setLocation( 350, 100 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void tweak( Graphics2D gtx, SelectionManager mgr )
    {
        Color           saveColor   = gtx.getColor();
        Stroke          saveStroke  = gtx.getStroke();
//        findMismatchedLengths( gtx, mgr );
        findCommonEdges( gtx, mgr );
        findAllCommonEdges( gtx, mgr );
        printEdges( gtx, mgr );

        gtx.setStroke( saveStroke );
        gtx.setColor( saveColor );
    }
    
    private void printEdges( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    selected    = mgr.getSelected();
        if ( selected.size() >= 3 )
        {
            PShape          shape1      = selected.get( 0 );
            List<Vertex>    vertices1   = shape1.getTransformedVertices();
            PShape  shape2  = selected.get( 1 );
            List<Vertex>    vertices2   = shape2.getTransformedVertices();
            System.out.println( "##################################" );
            for ( Vertex vertex : vertices1 )
                System.out.println( "1: " + getAdjLine( vertex ) );
            System.out.println( "----------------------------------" );
            for ( Vertex vertex : vertices2 )
                System.out.println( "2: " + getAdjLine( vertex ) );
        }
    }
    
    private static String getAdjLine( Vertex vertex )
    {
        String  fmt     = "(%6.2f,%6.2f)";
        Line2D  line    = vertex.getAdjLine();
        
        double  xco1    = line.getX1();
        double  yco1    = line.getY1();
        String  str1    = String.format( fmt, xco1, yco1 );
        
        double  xco2    = line.getX2();
        double  yco2    = line.getY2();
        String  str2    = String.format( fmt, xco2, yco2 );
        
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( str1 ) .append( " -> " ).append( str2 );
        return bldr.toString();
    }
    
    private void findCommonEdges( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    selected    = mgr.getSelected();
        if ( selected.size() >= 2 )
        {
            PShape          shape1      = selected.get( 0 );
//            List<Vertex>    vertices1   = shape1.getTransformedVertices();
            PShape  shape2  = selected.get( 1 );
            findCommonEdges( gtx, shape1, shape2 );
//            List<Vertex>    vertices2   = shape2.getTransformedVertices();
//            for ( Vertex vertex1 : vertices1 )
//            {
//                Line2D  line1   = vertex1.getAdjLine();
//                for ( Vertex vertex2 : vertices2 )
//                {
//                    Line2D  line2   = vertex2.getAdjLine();
//                    if ( Utils.match( line1, line2 ) )
//                        gtx.draw( line1 );
//                }
//            }
        }
    }
    
    private void findAllCommonEdges( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    shapes  = mgr.getShapes();
        for ( PShape shape1 : shapes )
            for ( PShape shape2 : shapes )
                if ( shape1 != shape2 )
                    findCommonEdges( gtx, shape1, shape2 );
    }
    
    private void 
    findCommonEdges( Graphics2D gtx, PShape shape1, PShape shape2 )
    {
        gtx.setColor( Color.MAGENTA );
        gtx.setStroke( new BasicStroke( 4 ) );
        List<Vertex>    vertices1   = shape1.getTransformedVertices();
        List<Vertex>    vertices2   = shape2.getTransformedVertices();
        for ( Vertex vertex1 : vertices1 )
        {
            Line2D  line1   = vertex1.getAdjLine();
            for ( Vertex vertex2 : vertices2 )
            {
                Line2D  line2   = vertex2.getAdjLine();
                if ( Utils.match( line1, line2 ) )
                    gtx.draw( line1 );
            }
        }
    }
    
    /**
     * When exactly two PShapes are selected,
     * find the edges in the shapes 
     * that have the same slope.
     * 
     * @param gtx
     * @param mgr
     */
    private void compareSlopes( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    selected    = mgr.getSelected();
        double          epsilon     = .05;
        if ( selected.size() == 2 )
        {
            System.out.println( "#################################" );
            PShape          shape0  = selected.get( 0 );
            List<Vertex>    vList0  = shape0.getTransformedVertices();
            PShape          shape1  = selected.get( 1 );
            List<Vertex>    vList1  = shape1.getTransformedVertices();
            int             ident0  = 0;
            for ( Vertex vertex0 : vList0 )
            {
                double  slope0      = vertex0.getSlope();
                int     ident1      = 0;
                for ( Vertex vertex1 : vList1 )
                {
                    double  slope1  = vertex1.getSlope();
                    double  diff    = Math.abs( slope0 - slope1);
                    
                    if ( diff < epsilon )
                    {
                        double  len0        = vertex0.getLength();
                        String  strLen0     = String.format( "%6.2f", len0 );
                        String  strSlope0   = String.format( "%6.2f", slope0 );
                        double  len1        = vertex1.getLength();
                        String  strLen1     = String.format( "%6.2f", len1 );
                        String  strSlope1   = String.format( "%6.2f", slope1 );
                        StringBuilder   bldr        = new StringBuilder();
                        bldr.append( ident0 ).append( ": " )
                            .append( strSlope0 ).append( ", " )
                            .append( strLen0 ).append( "; " )
                            .append( ident1 ).append( ": " )
                            .append( strSlope1 ).append( ", " )
                            .append( strLen1 );
                        System.out.println( bldr );
                    }
                    ++ident1;
                }
                ++ident0;
            }
        }
    }
    
    /**
     * Find all edges with the same slope and exactly one
     * vertex in common.
     * 
     * @param gtx
     * @param mgr
     */
    private void findMismatchedLengths( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    shapes      = mgr.getShapes();
        if ( mgr.getSelected().isEmpty() )
        {
            gtx.setColor( Color.GREEN );
            gtx.setStroke( new BasicStroke( 4 ) );
            for ( PShape shape0 : shapes )
                for ( PShape shape1 : shapes )
                    if ( shape0 != shape1 )
                        findMismatchedLengths( gtx, shape0, shape1 );
        }
    }
    
    private void 
    findMismatchedLengths( Graphics2D gtx, PShape shape0, PShape shape1 )
    {
        List<Vertex>    vList0  = shape0.getTransformedVertices();
        CQueue<Vertex>  queue0  = new CQueue<>( vList0 );
        Vertex          last0   = queue0.getCurr();
        Vertex          next0   = last0;
        List<Vertex>    vList1  = shape1.getTransformedVertices();
        do
        {
            CQueue<Vertex>  queue1  = new CQueue<>( vList1 );
            Vertex          last1   = queue1.getCurr();
            Vertex          next1   = last1;
            do
            {
                if ( next0.matches( next1 ) )
                {
                    double  slope0  = next0.getSlope();
                    double  slope1  = next1.getSlope();
                    if ( Utils.match( slope0, slope1) )
                    {
                        Line2D  line    = getShortLine( queue0, queue1 );
                        if ( line != null )
                            gtx.draw( line );
                    }
                }
                next1 = queue1.getNext();
            } while ( next1 != last1 );
            
            next0 = queue0.getNext();
        } while ( next0 != last0 );
    }
    
    /**
     * Find lines that share one vertex but not two.
     * Precondition: the current vertex of queueA
     * matches the current vertex of queueB.
     * 
     * @param queueA
     * @param queueB
     * @return
     */
    private Line2D getShortLine( CQueue<Vertex> queueA, CQueue<Vertex> queueB )
    {
        Vertex  vertexA     = queueA.getCurr();
        Vertex  nextA       = queueA.peekNext();
        Vertex  prevA       = queueA.peekPrevious();
        Vertex  vertexB     = queueB.getCurr();
        Vertex  nextB       = queueB.peekNext();
        Vertex  prevB       = queueB.peekPrevious();
        Line2D  line        = null;
        if ( nextA.matches( nextB ) )
            ;
        else if ( nextA.matches( prevB ) )
            ;
        else if ( prevA.matches( prevB ) )
            ;
        else if ( prevA.matches( nextB ) )
            ;
        else
        {
            if ( liesOn( nextA, vertexB ) )
                line = vertexA.getAdjLine();
            else if ( liesOn( prevA, vertexB ) )
                line = prevA.getAdjLine();
            else if ( liesOn( nextB, vertexA ) )
                line = vertexB.getAdjLine();
            else if ( liesOn( nextB, prevA ) )
                line = prevB.getAdjLine();
            else
                System.out.println( "fail" );
        }
        return line;
    }
    
    /**
     * Returns true if the coordinates of the first vertex
     * line on the adjacent line of the second vertex.
     * 
     * @param vertexA
     * @param vertexB
     * @return
     */
    private static boolean liesOn( Vertex vertexA, Vertex vertexB )
    {
        Point2D coords  = vertexA.getCoords();
        Line2D  line    = vertexB.getAdjLine();
        boolean result  = Utils.liesOn( coords, line );
        return result;
    }
}
