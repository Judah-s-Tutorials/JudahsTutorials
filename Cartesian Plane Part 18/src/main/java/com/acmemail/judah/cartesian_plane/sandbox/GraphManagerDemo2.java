package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.GraphManager;
import com.acmemail.judah.cartesian_plane.Profile;

public class GraphManagerDemo2
{
    private final JFrame        frame       = 
        new JFrame( "GraphManager Demo 2" );
    private final Profile       profile     = new Profile();
    private final Canvas        canvas      = new Canvas( profile );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( GraphManagerDemo2::new );
    }
    
    private GraphManagerDemo2()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        
        contentPane.add( canvas, BorderLayout.CENTER );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        return panel;
    }
    
    private class Canvas extends JPanel
    {
        private static final long   serialVersionUID = 1L;
        private static final String imagePath   = "sandbox/Bricks3.png";
        private final Rectangle rect    = new Rectangle();
        private final BufferedImage background;
        private int leftMargin  = 100;
        private int topMargin = 50;
        private int rightMargin = 25;
        private int bottomMargin = 25;
        
        /** 
         * Encapsulates to various drawing operations.
         * It is initialized in the constructor,
         * and utilized in the {@link #paintComponent(Graphics)} method.
         */
        private final GraphManager  drawManager;
        
        private int         width;
        private int         height;
        private Graphics2D  gtx;
        
        public Canvas( Profile profile )
        {
            drawManager = new GraphManager( this, profile );
            background = getBackgroundImage();
            Dimension   dim = new Dimension( 300, 500 );
            setPreferredSize( dim );
        }

        public void paintComponent( Graphics graphics )
        {
            width = getWidth();
            height = getHeight();
            gtx = (Graphics2D)graphics.create();
            tile();
            
            rect.x = leftMargin;
            rect.y = topMargin;
            rect.width = width - leftMargin - rightMargin;
            rect.height = height - topMargin - bottomMargin;
            drawManager.refresh( gtx, rect );
            drawManager.drawBackground();
            drawManager.drawGridLines();
            drawManager.drawAxes();
            drawManager.drawMinorTics();
            drawManager.drawMajorTics();
            drawManager.drawText();

            gtx.dispose();
        }
        
        private void tile()
        {
            int imageWidth  = background.getWidth();
            int imageHeight = background.getHeight();
        
            for ( int row = 0 ; row < height ; row += imageHeight )
                for ( int col = 0 ; col < width ; col += imageWidth )
                    gtx.drawImage( background, col, row, this );
        }
        
        private BufferedImage getBackgroundImage()
        {
            Class<?>    clazz       = getClass();
            ClassLoader loader      = clazz.getClassLoader();
            InputStream inStream    = 
                loader.getResourceAsStream( imagePath );
            if ( inStream == null )
            {
                System.err.println( imagePath + " not found" );
                System.exit( 1 );
            }
            
            BufferedImage   image   = null;
            try
            {
                image = ImageIO.read( inStream );
            }
            catch ( IOException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );
            }
            return image;
        }
    }
}
