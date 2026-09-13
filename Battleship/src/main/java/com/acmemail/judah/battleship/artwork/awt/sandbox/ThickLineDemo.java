package com.acmemail.judah.battleship.artwork.awt.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.BattleshipException;

public class ThickLineDemo
{
    private static Demo demo;
    public static void main(String[] args)
    {
        invokeAndWait( () -> {
            JFrame  frame   = new JFrame();
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            demo = new Demo( 4 );
            frame.setContentPane( demo );
            frame.pack();
            frame.setVisible( true );
        });

        int             width       = demo.getWidth();
        int             height      = demo.getHeight();
        int             type        = BufferedImage.TYPE_INT_ARGB;
        BufferedImage   image       = new BufferedImage( width, height, type );
        Graphics2D      gtx         = image.createGraphics();
        invokeAndWait( () -> demo.paintComponent( gtx ) );
        doHorizontal( image );
        doVertical( image );
    }

    private static void invokeAndWait( Runnable runner )
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> runner.run() );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            throw new BattleshipException( "unexpect exception", exc );
        }
    }
    
    private static void doHorizontal( BufferedImage image )
    {
        List<Segment>   segs    = new ArrayList<>();
        int             height  = image.getHeight();
        int             xco     = image.getWidth() / 2;
        int             curr    = image.getRGB( xco, 0 );
        int             next    = curr;
        Point           point   = new Point( xco, 0 );
        int             count   = 1;
        for ( int yco = 1 ; yco < height ; ++yco )
        {
            next = image.getRGB( xco, yco );
            if ( next == curr )
                ++count;
            else
            {
                Segment seg     = new Segment( point, count, next );
                curr = next;
                segs.add( seg );
                count = 1;
                point = new Point( xco, yco );
            }
        }
        Segment seg     = new Segment( point, count, next );
        segs.add( seg );
        System.out.println( "*** horizontal ***" );
        segs.forEach( System.out::println );
    }
    
    private static void doVertical( BufferedImage image )
    {
        List<Segment>   segs    = new ArrayList<>();
        int             width   = image.getWidth();
        int             yco     = image.getHeight() / 2 - 10;
        int             curr    = image.getRGB( 0, yco );
        Point           point   = new Point( 0, yco );
        int             next    = curr;
        int             count   = 1;
        int             xco     = width / 2;
        for (  ; xco < width ; ++xco )
        {
            next = image.getRGB( xco, yco );
            if ( next == curr )
                ++count;
            else
            {
                Segment seg     = new Segment( point, count, next );
                curr = next;
                segs.add( seg );
                count = 1;
                point = new Point( xco, yco );
            }
        }
        Segment seg     = new Segment( point, count, next );
        segs.add( seg );
        System.out.println( "*** vertical ***" );
        segs.forEach( System.out::println );
    }

    @SuppressWarnings("serial")
    private static class Demo extends JPanel
    {
        private final Stroke    stroke;
        public Demo( int stroke ) 
        {
            setPreferredSize( new Dimension( 500, 500 ) );
            this.stroke = new BasicStroke( stroke );
        }

        @Override
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            Graphics2D  gtx     = (Graphics2D)graphics;
            int         width   = getWidth();
            int         height  = getHeight();
            System.out.println( width + ", " + height );
            gtx.setColor( Color.LIGHT_GRAY );
            gtx.fillRect( 0, 0, width, height );
            
            gtx.setColor( Color.BLACK );
            gtx.setStroke( stroke );
            
            Line2D  line    = new Line2D.Double();
            int     xco1    = 10;
            int     yco1    = 10;
            int     xco2    = width - 10;
            int     yco2    = yco1;
            line.setLine( xco1, yco1, xco2, yco2 );
            gtx.draw( line );
            yco1 += 20;
            yco2 = yco1;
            gtx.drawLine( xco1, yco1, xco2, yco2 );
            System.out.println( line.getP1() + ", " + line.getP2() );
            System.out.printf( "%d, %d, %d, %d%n", xco1, yco1, xco2, yco2 );
            
            xco1 = width / 2 + 20;
            yco1 += 10;
            xco2 = xco1;
            yco2 = height - 10;
            line.setLine( xco1, yco1, xco2, yco2 );
            gtx.draw( line );
            gtx.drawLine( xco1 + 20, yco1, xco2 + 20, yco2 );
            System.out.println( line.getP1() + ", " + line.getP2() );
            System.out.printf( "%d, %d, %d, %d%n", xco1, yco1, xco2, yco2 );
            System.out.println( "***************************" );
        }
    }
    
    private record Segment( Point point, int count, int color ) {}
}
