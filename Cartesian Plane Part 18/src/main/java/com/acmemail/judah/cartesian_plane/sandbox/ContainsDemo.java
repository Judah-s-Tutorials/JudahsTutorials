package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * This application demonstrates a use of the Shape interface.
 * The application window has two closed, non-rectangular shapes,
 * and a "Fill Exterior" button.
 * When you push the button
 * the window outside of the two shapes
 * if filled with a new color.
 * 
 * @author Jack Straub
 * 
 * @see DemoPanel#fillExterior()
 */
public class ContainsDemo
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used.
     *
    */
    public static void main(String[] args)
    {
        ContainsDemo    demo    = new ContainsDemo();
        demo.buildGUI();
    }
    
    /**
     * Creates and displays the GUI.
     */
    private void buildGUI()
    {
        JFrame      frame       = new JFrame( "Shape Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      pane        = new JPanel( new BorderLayout() );
        DemoPanel   demo        = new DemoPanel();
        pane.add( demo, BorderLayout.CENTER );
        
        JPanel      controls    =  new JPanel();
        JButton     fill        = new JButton( "Fill Exterior" );
        JButton     exit        = new JButton( "Exit" );
        controls.add( fill );
        controls.add( exit );
        pane.add( controls, BorderLayout.SOUTH );
        
        exit.addActionListener( e -> System.exit( 0 ) );
        fill.addActionListener( e -> {
            demo.fillExterior();
            demo.repaint();
            fill.setEnabled( false );
        });
        
        frame.setContentPane( pane );
        frame.pack();
        GUIUtils.center( frame );
        frame.setVisible( true );
    }
    
    /**
     * JPanel subclass that controls
     * drawing and filling of main window.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private static class DemoPanel extends JPanel
    {
        private final Color     extFill     = Color.MAGENTA;
        private final int       imageWidth  = 210;
        private final int       imageHeight = 320;
        private final int       imageType   = BufferedImage.TYPE_INT_RGB;
        
        private final BufferedImage image       =
            new BufferedImage( imageWidth, imageHeight, imageType );
        private final Graphics2D    imageGTX    = image.createGraphics();
        private final Shape         shape1;
        private final Shape         shape2;
        
        /**
         * Constructor.
         * Sets the window size
         * and draws the BufferedImate
         * that is displayed in the window.
         */
        public DemoPanel()
        {
            Dimension   dim     = new Dimension( imageWidth, imageHeight );
            setPreferredSize( dim );
            
            imageGTX.setColor( getBackground() );
            imageGTX.fillRect( 0, 0, imageWidth, imageHeight );
            imageGTX.setColor(Color.BLACK );
            shape1 = getPolygon();
            shape2 = getEllipse();
        }
        
        /**
         * Fills the window with the background
         * and draws the image
         * containing the sample figures.
         * @param graphics 
         *     the graphics context used for drawing
         *     in the window
         */
        @Override
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            Graphics    gtx     = (Graphics2D)graphics.create();
            int         width   = getWidth();
            int         height  = getHeight();
            gtx.setColor( getBackground() );
            gtx.fillRect( 0, 0, width, height );
            gtx.drawImage( image, 0, 0, this );
            gtx.dispose();
        }
        
        /**
         * Within the BufferedImage ({@link #image})
         * set all pixels except those 
         * in the interior of the sample figures
         * to a new color.
         */
        public void fillExterior()
        {
            int rgb = extFill.getRGB() & 0xffffff;
            for ( int row = 0 ; row < imageHeight ; ++row )
                for ( int col = 0 ; col < imageWidth ; ++col )
                    if ( !shape1.contains( col, row ) && 
                        !shape2.contains( col, row )
                    )
                        image.setRGB( col, row, rgb );
        }

        /**
         * Draws an irregular polygon in the encapsulated
         * BufferedImage ({@link #image}).
         * 
         * @return  the Shape object encapsulating the polygon
         */
        private Shape getPolygon()
        {
            int[]   xPoints = {  10,  25, 100, 150, 200, 175, 125, 100 };
            int[]   yPoints = { 100,  50,  95,  25, 150, 125,  75, 150 };
            int     nPoints = xPoints.length;
            Shape   shape   = new Polygon( xPoints, yPoints, nPoints );
            imageGTX.draw( shape );
            return shape;
        }
        
        /**
         * Draws an ellipse in the encapsulated
         * BufferedImage ({@link #image}).
         * 
         * @return  the Shape object encapsulating the ellipse
         */
        private Shape getEllipse()
        {
            Shape   shape   = new Ellipse2D.Double( 10, 200, 190, 100 );
            imageGTX.draw( shape );
            return shape;
        }
    }
}
