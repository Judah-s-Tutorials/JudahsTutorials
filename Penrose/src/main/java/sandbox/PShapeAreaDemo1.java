package sandbox;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.gmail.johnstraub1954.penrose.utils.Malfunction;
import com.gmail.johnstraub1954.penrose.utils.PShapeIntersection;
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
            demo2.canvas.addShape( new PDart( longSide, 100, 0 ) );
            demo2.canvas.addShape( new PKite( longSide, 0, 150 ) );
            demo2.canvas.addShape( new PDart( longSide, 100, 150 ) );
            demo2.canvas.setTweaker( demo2::tweak );
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
//        tweakA( gtx, mgr );
//      tweakB( gtx, mgr );
      tweakB1( gtx, mgr );
//        tweakC( gtx, mgr );
//        tweakD( gtx, mgr );
//        tweakE( gtx, mgr );
        gtx.setStroke( saveStroke );
        gtx.setColor( saveColor );
    }
    
    private void tweakA( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    shapes      = mgr.getShapes();
        List<PShape>    selected    = mgr.getSelected();
        if ( selected.size() > 1 )
        {
            System.out.println( "*************************" );
            PShape  shape0  = selected.get( 0 );
            PShape  shape1  = selected.get( 1 );
            Area    area0   = new Area( shape0.getWorkShape() );
            Area    area1   = new Area( shape1.getWorkShape() );
            area0.intersect( area1 );
            gtx.setColor( Color.GREEN );
            gtx.fill( area0 );
            System.out.println( area0.isEmpty() );
            
            List<Vertex>    vertices0   = shape0.getTransformedVertices();
            List<Vertex>    vertices1   = shape1.getTransformedVertices();
//            printVertices( vertices0 );
//            printVertices( vertices1 );
            
            CQueue<Vertex>      queue0  = new CQueue<>( vertices0 );
            CQueue<Vertex>      queue1  = new CQueue<>( vertices1 );
            Iterator<Vertex>    iter0   = queue0.iterator();
            Vertex  vertex0 = null;
            Vertex  vertex1 = null;
            while ( iter0.hasNext() && vertex1 == null )
            {
                vertex0 = iter0.next();
                Iterator<Vertex>    iter1   = queue1.iterator();
                while ( iter1.hasNext() && vertex1 == null )
                {
                    Vertex  test    = iter1.next();
                    if ( vertex0.matches( test ) ) 
                        vertex1 = test;
                }
            }
            Vertex  vertex0_1       = null;
            Vertex  vertex1_1       = null;
            Vertex  vertex0_next    = queue0.getNext( vertex0 );
            Vertex  vertex0_prev    = queue0.getPrevious( vertex0 );
            Vertex  vertex1_next    = queue1.getNext( vertex1 );
            Vertex  vertex1_prev    = queue1.getPrevious( vertex1 );
            if ( vertex1 == null )
                System.out.println( "no match" );
            else if ( vertex0_next.matches( vertex1_next ) )
            {   
                vertex0_1 = vertex0_next;
                vertex1_1 = vertex1_next;
            }
            else if ( vertex0_prev.matches( vertex1_prev ) )
            {   
                vertex0_1 = vertex0_prev;
                vertex1_1 = vertex1_prev;
            }
            else if ( vertex0_next.matches( vertex1_prev ) )
            {   
                vertex0_1 = vertex0_next;
                vertex1_1 = vertex1_prev;
            }
            else if ( vertex0_prev.matches( vertex1_next ) )
            {   
                vertex0_1 = vertex0_prev;
                vertex1_1 = vertex1_next;
            }
            else
                ;
            System.out.println( vertex0 + " " + vertex1 );
            System.out.println( vertex0_1 + " " + vertex1_1 );
        }
    }
    
    private void tweakB( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    shapes      = mgr.getShapes();
        List<PShape>    selected    = mgr.getSelected();
        if ( selected.size() == 1 )
        {
            gtx.setColor( Color.GREEN );
            PShape  selectedShape       = selected.get( 0 );
            Shape   selectedWorkShape   = selectedShape.getWorkShape();
            for ( PShape shape : shapes )
            {
                if ( shape != selectedShape )
                {
                    if ( PShapeIntersection.intersect( selectedShape, shape ) )
                    {
                        gtx.draw( shape.getWorkShape() );
                    }
                }
            }
        }
    }
    
    private void tweakB1( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    shapes      = mgr.getShapes();
        List<PShape>    selected    = mgr.getSelected();
        if ( selected.size() == 1 )
        {
            gtx.setColor( Color.GREEN );
            PShape  selectedShape       = selected.get( 0 );
            Shape   selectedWorkShape   = selectedShape.getWorkShape();
            for ( PShape shape : shapes )
            {
                if ( shape != selectedShape )
                {
                    if ( selectedShape.intersects( shape ) )
                    {
                        gtx.draw( shape.getWorkShape() );
                        printIntersection2( shape, selectedShape );
                    }
                }
            }
        }
    }
    
    private void tweakC( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    shapes      = mgr.getShapes();
        List<PShape>    selected    = mgr.getSelected();
        if ( selected.size() == 1 )
        {
            gtx.setColor( Color.GREEN );
            PShape  selectedShape       = selected.get( 0 );
            Shape   selectedWorkShape   = selectedShape.getWorkShape();
            Area    selectedWorkArea    = new Area( selectedWorkShape );
            for ( PShape shape : shapes )
            {
                if ( shape != selectedShape )
                {
                    Shape       workShape   = shape.getWorkShape();
                    Area        workArea    = new Area( workShape );
                    workArea.intersect( selectedWorkArea );
                    if ( !workArea.isEmpty() )
                        gtx.draw( workShape );
                }
            }
        }
    }
    
    private void tweakD( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    shapes      = mgr.getShapes();
        int             numShapes   = shapes.size();
        List<PShape>    selected    = mgr.getSelected();
        gtx.setColor( Color.BLUE );
        gtx.setStroke( new BasicStroke( 4  ));
        if ( selected.size() == 1 )
        {
            PShape          selShape    = selected.get( 0 );
            List<Vertex>    selVertices = selShape.getTransformedVertices();
            for ( int inx = 0 ; inx < numShapes ; ++inx )
            {
                PShape  shape   = shapes.get( inx );
                if ( shape != selShape )
                {
                    Line2D  edge    = getEdge( selVertices, shape );
                    if ( edge != null )
                        gtx.draw( edge );
                }
            }
        }
    }
    
    private void tweakE( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    shapes      = mgr.getShapes();
        List<PShape>    selected    = mgr.getSelected();
        if ( selected.size() == 1 )
        {
            PShape          selShape    = selected.get( 0 );
            for ( PShape pShape : shapes )
                if ( selShape != pShape )
                    printIntersection( selShape, pShape );
        }
    }
    
    private void printIntersection( PShape pShapeA, PShape pShapeB )
    {
        PShapeIntersection  intersection    = 
            new PShapeIntersection( pShapeA, pShapeB );
        Line2D              edge            = intersection.getEdge();
        if ( edge != null )
        {
            String  nameA   = pShapeA.getClass().getSimpleName();
            String  nameB   = pShapeB.getClass().getSimpleName();
            System.out.println( "********************************" );
            System.out.println( nameA + " -> " + nameB );
            System.out.println( edge.getP1() + "," + edge.getP2() );
            System.out.println( "################################\n\n\n" );
        }
    }
    
    private void printIntersection2( PShape pShapeA, PShape pShapeB )
    {
        String  str     = Utils.formatIntersection( pShapeA, pShapeB );
        if ( !str.isEmpty() )
        {
            String  nameA   = pShapeA.getClass().getSimpleName();
            String  nameB   = pShapeB.getClass().getSimpleName();
            System.out.println( "********************************" );
            System.out.println( nameA + " -> " + nameB );
            System.out.println( str );            
            System.out.println( "################################\n\n\n" );
        }
    }
    
    private Line2D getEdge( List<Vertex> selVertices, PShape shape )
    {
        List<Vertex>        shapeVertices   = shape.getTransformedVertices();
        List<Vertex>        matches         = new ArrayList<>();
        for ( Vertex selVertex : selVertices )
        {
            for ( Vertex shapeVertex : shapeVertices )
            {
                if ( selVertex.matches( shapeVertex ) )
                    matches.add( selVertex );
            }
        }
        int     numMatches  = matches.size();
        if ( numMatches > 2 )
            throw new Malfunction( "Too many matches" );
        Line2D  line    = null;
        if ( numMatches == 2 )
        {
            Point2D  pointA  = matches.get( 0 ).getCoords();
            Point2D  pointB  = matches.get( 1 ).getCoords();
            line = new Line2D.Double( pointA, pointB );
        }
        return line;
    }

    private void printVertices( List<Vertex> list )
    {
        StringBuilder   bldr   = new StringBuilder();
        list.forEach( v -> {
            Point2D point   = v.getCoords();
            String  xco     = String.format( "%5.1f", point.getX() );
            String  yco     = String.format( "%5.1f", point.getY() );
            bldr.append( "(" );
            bldr.append( xco ).append( "," ).append( yco );
            bldr.append( ") " );
        });
        System.out.println( bldr );
    }
}
