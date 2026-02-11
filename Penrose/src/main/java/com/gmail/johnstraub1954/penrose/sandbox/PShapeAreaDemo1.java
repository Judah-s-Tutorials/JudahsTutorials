package com.gmail.johnstraub1954.penrose.sandbox;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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
import com.gmail.johnstraub1954.penrose.utils.SelectionManager;
import com.gmail.johnstraub1954.penrose.utils.Utils;

public class PShapeAreaDemo1 implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 3168160266742270027L;
    
    private static double       longSide        = 100;
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
//        findCommonEdges( gtx, mgr );
//        findAllCommonEdges( gtx, mgr );
//        printEdges( gtx, mgr );
        findMisaligned( gtx, mgr );
//        findInvalidIntersections( gtx, mgr );

        gtx.setStroke( saveStroke );
        gtx.setColor( saveColor );
    }
    
    private void findMisaligned( Graphics2D gtx, SelectionManager mgr )
    {
        List<PShape>    selected    = mgr.getSelected();
        if ( selected.size() == 2 )
        {
            gtx.setColor( Color.BLUE );
            gtx.setStroke( new BasicStroke( 3 ) );
            PShape          shape1      = selected.get( 0 );
            List<Vertex>    vertices1   = shape1.getTransformedVertices();
            PShape  shape2  = selected.get( 1 );
            List<Vertex>    vertices2   = shape2.getTransformedVertices();
            List<Vertex>    solutions   = new ArrayList<>();
            for ( Vertex vertex1 : vertices1 )
            {
                Line2D  line1   = vertex1.getAdjLine();
                solutions.clear();
                for ( Vertex vertex2 : vertices2 )
                {
                    Line2D  line2       = vertex2.getAdjLine();
                    Line2D  shortLine   = getContainedLine( line1, line2 );
                    if ( shortLine != null )
                        gtx.draw( shortLine );
                }
            }
        }
    }
    
    /**
     * One line contains another if they have the same slope,
     * different lengths,
     * and both endpoints of the shorter line
     * intersect the longer line.
     * @param line1
     * @param line2
     * @return
     */
    private Line2D getContainedLine( Line2D line1, Line2D line2 )
    {
        Line2D  shortLine   = null;
        Line2D  longLine    = null;
        double  slope1      = Utils.slope( line1 );
        double  slope2      = Utils.slope( line2 );
        if ( Utils.match( slope1, slope2 ) )
        {
            double  len1    = Utils.length( line1 ) ;
            len1 = Utils.round( len1 );
            double  len2    = Utils.length( line2 );
            len2 = Utils.round( len2 );
            if ( len1 < len2 )
            {
                shortLine = line1;
                longLine = line2;
            }
            else if ( len2 < len1 )
            {
                shortLine = line2;
                longLine = line1;
            }
            else
                ;
        }
        if ( shortLine != null )
        {
            if ( !Utils.liesOn( shortLine.getP1(), longLine ) )
                shortLine = null;
            else if ( !Utils.liesOn( shortLine.getP2(), longLine ) )
                shortLine = null;
            else
                ;
        }
        return shortLine;
    }
}
