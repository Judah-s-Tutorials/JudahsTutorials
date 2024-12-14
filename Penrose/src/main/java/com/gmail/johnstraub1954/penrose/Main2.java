package com.gmail.johnstraub1954.penrose;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main2
{
    private final JFrame    frame;
    private static Canvas    canvas;
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( Main2::new );
        
        try
        {
            while ( true )
            {
                Thread.sleep( 200 );
                SwingUtilities.invokeAndWait( () -> canvas.repaint() );
            }
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            System.out.println( "ooof" );
        }
    }
    
    public Main2()
    {
        frame = new JFrame( "Penrose" );
        canvas = new Canvas();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel( new BorderLayout() );
        pane.add( canvas, BorderLayout.CENTER );
        
        frame.setContentPane( pane );
        frame.pack();
        frame.setLocation( 200, 200 );
        frame.setVisible( true );
    }
    
    private class Canvas extends JPanel
    {
        private static final long serialVersionUID = 1L;
        
        private static final double   twoPI = 2 * Math.PI;

        private final double    side        = 50;
        private final double    angle       = Math.PI / 12;
        private final IsoTri    triangle    = new IsoTri( side, angle );
        private final Color     bgColor     = new Color( 0xeeeeee );
        private final Color     fgColor     = Color.BLACK;
        
        private Graphics2D      gtx;
        private int             width;
        private int             height;
        
        public Canvas()
        {
            Dimension   prefSize    = new Dimension( 500, 500 );
            setPreferredSize( prefSize );
        }
        
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            gtx = (Graphics2D)graphics.create();
            width = getWidth();
            height = getHeight();
            
            gtx.setColor( bgColor );
            gtx.fillRect( 0,  0, width, height );
            gtx.setColor( fgColor );
            gtx.setRenderingHint( 
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            
            double  centerXco   = width / 2.0;
            double  centerYco   = height / 2.0;
            gtx.translate( centerXco, centerYco );
            AffineTransform rotate0 = gtx.getTransform();
            for ( int inx = 0 ; inx < 10 ; ++inx )
            {
                gtx.setTransform( rotate0 );
                double  work    = angle / (inx + 1);
                for ( double sum = 0 ; sum < twoPI ; sum += work )
                {
                    triangle.draw( gtx, inx );
                    gtx.rotate( work );
                }
            }
            gtx.dispose();
        }
    }
    
    private static class IsoTri
    {
        private final Path2D    path    = new Path2D.Double();
        private final float     hueIncr = .001f;
        private final float     saturation  = .75f;
        private final float     brightness  = .75f;
        private final double    hypot;
        private final double    radians;
        private final double    side; 
        private final double    xco1;
        private final double    yco;
        private float hue = 0;
        
        public IsoTri( double side, double radians )
        {
            this.radians = radians;
            this.side = side;
            double  half    = radians / 2;
            xco1 = side * Math.cos( half );
            yco     = side * Math.sin( half );
            
            path.moveTo( 0, 0 );
            path.lineTo( xco1, yco );
            path.lineTo( xco1, -yco );
            path.lineTo( 0, 0 );
            hypot = xco1;
        }
        
        public void draw( Graphics2D gtx, int inx )
        {
            Path2D  path2   = new Path2D.Double();
            double  vertex  = inx * hypot;
            double  xco     = vertex + side * Math.cos( radians / 2 );
            path2.moveTo( vertex, 0 );
            path2.lineTo( xco, yco );
            path2.lineTo( xco, -yco );
            path2.lineTo( vertex, 0 );
            gtx.setColor( Color.BLACK );
            gtx.draw( path2 );
            Color   color   = Color.getHSBColor( hue, saturation, brightness );
            System.out.println( hue + ", " + color.getRGB() );
            gtx.setColor( color );
            gtx.fill( path2 );
            hue += hueIncr;
            hue %= 1;
        }
    }
}
