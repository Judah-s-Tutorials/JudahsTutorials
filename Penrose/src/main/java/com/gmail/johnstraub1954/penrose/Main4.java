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

public class Main4
{
    private final JFrame    frame;
    private static Canvas    canvas;
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( Main4::new );
        
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
    
    public Main4()
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
        private final double    angle       = Math.PI / 12;
        private final IsoTri    triangle    = new IsoTri( side, angle );
        private final TriColor  triColor    = new TriColor();
        private final Color     bgColor     = new Color( 0xeeeeee );
        private final Color     fgColor     = Color.BLACK;
        private final int       maxRows     = 6;
        
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
            work    = (angle / (row + 1)) * col++;
            if ( work >= twoPI )
            {
                work = 0;
                col = 0;
                if ( ++row > maxRows )
                    row = 0;
            }
            gtx.setColor( triColor.getColor() );
            gtx.rotate( work, centerXco, centerYco );
            gtx.translate( centerXco + row * triangle.hypot, centerYco );
            gtx.fill( triangle.path );
            gtx.dispose();
        }
    }
    
    private static class IsoTri
    {
        private final Path2D    path        = new Path2D.Double();
        private final double    hypot;
        
        public IsoTri( double side, double radians )
        {
            double  half    = radians / 2;
            double  xco1 = side * Math.cos( half );
            double  yco  = side * Math.sin( half );
            
            path.moveTo( 0, 0 );
            path.lineTo( xco1, yco );
            path.lineTo( xco1, -yco );
            path.lineTo( 0, 0 );
            hypot = xco1;
        }
    }
    
    private static class TriColor
    {
        private final float     hueIncr     = .01f;
        private final float     lightMin    = .75f;
        private final float     lightMax    = .75f;
        private final float     lightIncr   = .3f;
        
        private float hue      = 0;
        private float light    = lightMin;
        
        public Color getColor()
        {
            if ( (light += lightIncr) > lightMax )
            {
                light = lightMin;
                if ( (hue += hueIncr) > 1 )
                    hue = 0;
            }
            Color   color   = Color.getHSBColor( hue, light, light );
            return color;
        }
    }
}
