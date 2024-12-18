package com.gmail.johnstraub1954.penrose;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main3
{
    private final JFrame    frame;
    private static Canvas    canvas;
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( Main3::new );
        
        try
        {
            while ( true )
            {
                Thread.sleep( 10 );
                SwingUtilities.invokeAndWait( () -> canvas.repaint() );
            }
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            System.out.println( "ooof" );
        }
    }
    
    public Main3()
    {
        frame = new JFrame( "Penrose" );
        canvas = new Canvas();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel( new BorderLayout() );
        pane.add( canvas, BorderLayout.CENTER );
        
        frame.setContentPane( pane );
        setGeometry();
        frame.pack();
        frame.setVisible( true );
    }
    
    private void setGeometry()
    {
        Toolkit     toolkit     = Toolkit.getDefaultToolkit();
        Dimension   size        = toolkit.getScreenSize();
        int         width       = (int)(size.width * .9);
        int         height      = (int)(size.height * .9);
        Dimension   prefSize    = new Dimension( width, height );
        frame.setPreferredSize( prefSize );
        
        int         xOffset     = (size.width - prefSize.width) / 2;
        int         yOffset     = (size.height - prefSize.height) / 2;
        frame.setLocation( xOffset, yOffset );
    }
    
    private class Canvas extends JPanel
    {
        private static final long serialVersionUID = 1L;
        
        private static final double   twoPI = 2 * Math.PI;

        private final double    side        = 50;
        private final double    angle       = Math.PI / 3;
        private final IsoTri    triangle    = new IsoTri( side, angle );
        private final Color     bgColor     = new Color( 0xeeeeee );
        private final Color     fgColor     = Color.BLACK;
        
        private int             row         = 0;
        private int             col         = 0;
        private double          work        = 0;
        private Graphics2D      gtx;
        private int             width;
        private int             height;
        
        public Canvas()
        {
        }
        
        public void paintComponent( Graphics graphics )
        {
            gtx = (Graphics2D)graphics.create();
            width = getWidth();
            height = getHeight();
            
            gtx.setColor( bgColor );
            gtx.setColor( fgColor );
            gtx.setRenderingHint( 
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            
            double  centerXco   = width / 2.0;
            double  centerYco   = height / 2.0;
            gtx.translate( centerXco, centerYco );
            work    = angle / (row + 1) * col++;
            if ( work >= twoPI )
            {
                work = 0;
                col = 0;
                if ( ++row > 5 )
                    row = 0;
            }
            gtx.rotate( work );
            triangle.draw( gtx, row );
            gtx.dispose();
        }
    }
    
    private static class IsoTri
    {
        private final Path2D    path        = new Path2D.Double();
        private final float     hueIncr     = .001f;
        private final float     satMin      = .1f;
        private final float     satMax      = .9f;
        private final float     satIncr     = .1f;
        private final float     brightMin   = .1f;
        private final float     brightMax   = .9f;
        private final float     brightIncr  = .1f;
        
        private final double    hypot;
        private final double    radians;
        private final double    side; 
        private final double    xco1;
        private final double    yco;
        
        private float hue           = 0;
        private float saturation    = satMin;
        private float brightness    = brightMin;
        
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
            if ( (hue += hueIncr) > 1 )
                hue = 0;
            if ( (saturation += satIncr) > satMax )
                saturation = satMin;
            if ( (brightness += brightIncr) > brightMax )
                brightness = brightMin;
            Color   color   = Color.getHSBColor( hue, saturation, brightness );
            gtx.setColor( color );
            gtx.fill( path2 );
            hue += hueIncr;
            hue %= 1;
        }
    }
}
