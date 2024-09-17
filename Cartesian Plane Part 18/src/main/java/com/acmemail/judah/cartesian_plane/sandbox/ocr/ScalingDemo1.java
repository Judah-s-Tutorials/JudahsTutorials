package com.acmemail.judah.cartesian_plane.sandbox.ocr;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * This application incorporates a simple demonstration
 * of scaling in a Swing application.
 * The {@link #paintComponent(Graphics)} method
 * applies a scaling factor under the control of the operator
 * and draws some lines of text,
 * incorporating both alpha and numeric characters.
 * <p>
 * For emphasis,
 * the scaling logic is encapsulated in the {@link #applyScale()} method,
 * which creates a scaling operation
 * and concatenates it with the translation operation
 * that is typically present in the graphics context
 * of a Swing application.
 * 
 * @author Jack Straub
 * 
 * @see ScalingDemo3
 */
public class ScalingDemo1 extends JPanel
{
    private static final long serialVersionUID = -6779305390811349326L;
    
    private final int           widthOrig       = 200;
    private final int           heightOrig      = widthOrig;
    private final int           margin          = 1;
    private final float         scaleFactor     = 1.5f;
    private final int           defWeight       = 3;
    private final BufferedImage imageOrig;
    private final Image         imageScaled;

    /** Background color of the principal GUI window. */
    private final Color         bgColor         = Color.WHITE;
    /** Color for drawing text in the application window. */
    private final Color         fgColor         = Color.BLACK;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> {
            ScalingDemo1    demo  = new ScalingDemo1();
            demo.build();
        });
    }
    
    /**
     * Constructor.
     * Establishes the BufferedImages to paint.
     */
    public ScalingDemo1()
    {
        imageOrig = getImageOrig();
        imageScaled = getImageScaled();
    }

    /**
     * Builds and displays the application GUI.
     */
    public void build()
    {
        JFrame          frame       = new JFrame( "Scaling Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel          contentPane = new JPanel( new BorderLayout() );
        contentPane.add( this, BorderLayout.CENTER );
        
        int     origHeight      = imageOrig.getHeight();
        int     scaledWidth     = imageScaled.getWidth( null );
        int     scaledHeight    = imageScaled.getWidth( null );
        int     prefWidth       = scaledWidth + 2 * margin;
        int     prefHeight      = origHeight + scaledHeight + 3 * margin;
        Dimension   prefSize    = new Dimension( prefWidth, prefHeight );
        setPreferredSize( prefSize );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setLocation( 100, 200 );
        frame.setVisible( true );
        
        showDialog( frame );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        int         width   = getWidth();
        int         height  = getHeight();
        Graphics2D  gtx     = (Graphics2D)graphics.create();
        
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, width, height );
        
        gtx.setColor( fgColor );
        int         xco     = 0;
        int         yco     = 0;
        gtx.drawImage( imageOrig, xco, yco, this );
        
        xco = 0;
        yco += margin + imageOrig.getHeight();
        gtx.drawImage( imageScaled, xco, yco, this );
        
        gtx.dispose();
    }
    
    /**
     * Creates the non-scaled BufferedImage.
     * 
     * @return  the non-scaled BufferedImage
     */
    private BufferedImage getImageOrig()
    {
        int             type    = BufferedImage.TYPE_INT_RGB;
        BufferedImage   image   = 
            new BufferedImage( widthOrig, heightOrig, type );
        
        Graphics2D  gtx     = image.createGraphics();
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, widthOrig, heightOrig );
        
        gtx.setColor( fgColor );
        gtx.setColor( Color.BLACK );
        int     yco   = gtx.getFontMetrics().getAscent();
        int     xco     = 0;
        gtx.drawString( "-10", xco, yco );

        image = trim( image );
        return image;
    }
    
    /**
     * Creates the scaled BufferedImage.
     * <p>
     * Precondition: non-scaled BufferedImage is fully configured
     * 
     * @return  the scaled BufferedImage
     */
    private Image getImageScaled()
    {
        int             imageType   = imageOrig.getType();
        int             smooth      = Image.SCALE_SMOOTH;
        int             fast        = Image.SCALE_FAST;
        int             width       = 
            (int)(imageOrig.getWidth() * scaleFactor + .5);
        int             height      =  
            (int)(imageOrig.getHeight() * scaleFactor + .5);
        Image           smoothImage = 
            imageOrig.getScaledInstance( width, height, smooth );
        Image           fastImage   = 
            imageOrig.getScaledInstance( width, height, fast );
        
        int             smoothWidth     = smoothImage.getWidth( null );
        int             smoothHeight    = smoothImage.getHeight( null );
        int             fastWidth       = fastImage.getWidth( null );
        int             fastHeight      = fastImage.getHeight( null );
        int             totalWidth      = 
            smoothWidth + fastWidth + 3 * margin;
        int totalHeight                 =
            Math.max( smoothHeight, fastHeight ) + 3 * margin;
        BufferedImage   image           =
            new BufferedImage( totalWidth, totalHeight, imageType );
        Graphics2D      gtx             = image.createGraphics();
        int             xco             = margin;
        int             yco             = margin;
        gtx.setColor( Color.WHITE );
        gtx.fillRect( 0, 0, totalWidth, totalHeight );
        gtx.drawImage( smoothImage, xco, yco, this );
        xco += smoothWidth + 2 * margin;
        gtx.drawImage( fastImage, xco, yco, this );
        return image;
    }
    
    private void showDialog( JFrame frame )
    {
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JPanel      content     = new ScalingDemo1A( this );
        JScrollPane scrollPane  = new JScrollPane( content );
        JDialog     dialog      = new JDialog( frame, false );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        dialog.setContentPane( contentPane );
        
        Dimension   frameDim    = frame.getPreferredSize();
        Point       frameLoc    = frame.getLocation();
        int         frameXco    = frameLoc.x;
        int         frameYco    = frameLoc.y;
        int         dialogXco   = frameXco + frameDim.width + 10;
        dialog.setLocation( dialogXco, frameYco );
        
        dialog.pack();
        dialog.setVisible( true );
    }
    
    private void drawText( Graphics2D gtx )
    {
        int     height  = gtx.getFontMetrics().getAscent();
        int     xco     = 0;
        int     yco     = height;
//        gtx.drawString( "Spot", xco, yco );
        gtx.drawString( "-10", xco, yco );
    }
    
    private BufferedImage trim( BufferedImage image )
    {
        int type    = image.getType();
        int target  = fgColor.getRGB() & 0xffffff;
        int maxXco  = 0;
        int maxYco  = 0;
        int rows    = image.getHeight();
        int cols    = image.getWidth();
        for ( int row = 0 ; row < rows ; ++row )
            for ( int col = 0 ; col < cols ; ++ col )
            {
                int pixel = image.getRGB( col,  row ) & 0xffffff;
                if ( pixel == target )
                {
                    maxXco = Math.max( maxXco, col );
                    maxYco = Math.max( maxYco, row );
                }
            }
        BufferedImage   trimmed = new BufferedImage( maxXco + 1, maxYco + 1, type );
        Graphics2D      gtx     = trimmed.createGraphics();
        gtx.drawImage( image, 0, 0, this );
        return trimmed;
    }
}