package com.gmail.johnstraub1954.penrose.app;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Applic
 */
public class LEDMaker
{
    private static final String directoryName       = "src/main/resources";
    private static final File   directoryFile       = new File( directoryName );
    private static final String imageType           = "PNG";
    private static final int    iconSide            = 16;
    private static final int    colorMask           = 0x00FFFF00;
    private static final Color  transparentColor    = 
        new Color( colorMask, true );
    private static final int    edgeWidth           = 2;
    private static final Stroke edge                = 
        new BasicStroke( edgeWidth );
    private static final Color  edgeColor           = Color.BLACK;
    
    private BufferedImage       redLED;
    private BufferedImage       yellowLED;
    private BufferedImage       greenLED;
    private BufferedImage       blackLED;

    public static void main( String[] args )
    {
        LEDMaker    ledMaker    = new LEDMaker();
        ledMaker.execute();
    }
    
    private void execute()
    {
        redLED = paint( Color.RED );
        yellowLED = paint( Color.YELLOW );
        greenLED = paint( Color.GREEN );
        blackLED = paint( Color.BLACK );
        if ( showImage( redLED) )
            writeImage( redLED, "Red" );
        if ( showImage( yellowLED) )
            writeImage( yellowLED, "Yellow" );
        if ( showImage( greenLED) )
            writeImage( greenLED, "Green" );
        if ( showImage( blackLED) )
            writeImage( blackLED, "Black" );
    }
    
    private BufferedImage paint( Color color )
    {
        int             imageType   = BufferedImage.TYPE_INT_ARGB;
        BufferedImage   image       = 
            new BufferedImage( iconSide, iconSide, imageType );
        Graphics2D      gtx         = image.createGraphics();
        gtx.setColor( transparentColor );
        gtx.fillRect( 0, 0, iconSide, iconSide );
        
        gtx.setColor( edgeColor );
        gtx.setStroke( edge );
        int offset  = edgeWidth / 2;
        int diam    = iconSide - 2 * offset;
        gtx.drawOval( offset, offset, diam, diam );

        Point2D         start       = new Point2D.Float( offset, offset );
        Point2D         end         = new Point2D.Float( diam, diam );
        GradientPaint   gradient    = 
            new GradientPaint( start, Color.WHITE, end, color );
        gtx.setPaint( gradient );
        gtx.fillOval( offset, offset, diam, diam );
        
        return image;
    }
    
    private void writeImage( BufferedImage icon, String name )
    {
        String  fullName    = name + "LED.png";
        File    destination = new File( directoryFile, fullName );
        try
        {
            ImageIO.write( icon, imageType, destination );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }

    private boolean showImage( BufferedImage image )
    {
        Icon    icon    = new ImageIcon( image );
        int     result  =
            JOptionPane.showConfirmDialog(
                null,
                "Is cool?",
                "Eyeball LED Icon",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                icon
            );
        boolean rcode   = result == JOptionPane.OK_OPTION;
        return rcode;
    }
}
