package com.gmail.johnstraub1954.penrose;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main
{
    private final JFrame    frame;
    private final Canvas    canvas;
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( Main::new );
    }
    
    public Main()
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
        
        private static double   r180    = Math.PI / 2;
        private static double   r30     = 30 * Math.PI / 180;

        private static final double triBase     = 35;
        private static final double triHeight   = 3 * triBase;
        private static final double vertex      = getVertex();
        private final Path2D        triangle    = new Path2D.Double();
        private final Color         bgColor     = new Color( 0xee, 0xee, 0xee );
        private final Color         fgColor     = Color.BLACK;
        
        private Graphics2D      gtx;
        private int             width;
        private int             height;
        
        public Canvas()
        {
            Dimension   prefSize    = new Dimension( 500, 500 );
            setPreferredSize( prefSize );
            triangle.moveTo( -triHeight, -triBase / 2 );
            triangle.lineTo( 0, 0 );
            triangle.lineTo( -triHeight, triBase / 2 );
            triangle.lineTo( -triHeight, -triBase / 2 );
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
            
            double  centerXco   = width / 2.0;
            double  centerYco   = height / 2.0;
            gtx.translate( centerXco, centerYco );
            for ( int inx = 0 ; inx < 10 ; ++inx )
            {
                gtx.rotate( vertex );
                gtx.draw(triangle );
            }
            
            gtx.dispose();
        }
        
        private static double getVertex()
        {
            double  baseAngle   = Math.atan( triHeight / (triBase / 2) );
            double  vertex      = (Math.PI / 2) - 2 * baseAngle;
            return vertex;
        }
    }
}
