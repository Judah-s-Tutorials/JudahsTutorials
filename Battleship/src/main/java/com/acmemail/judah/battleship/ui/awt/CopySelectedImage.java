package com.acmemail.judah.battleship.ui.awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class CopySelectedImage
{
    private static final int    IMAGE_TYPE      = BufferedImage.TYPE_INT_ARGB;
    private static final int    DEF_WIDTH       = 100;
    private static final int    DEF_HEIGHT      = 25;
    private static final int    DEF_MARGIN      = 0;
    private static final Color  TRANSPARENT     = new Color( 0, 0, 0, 0 );
    private static final Color  color           = Color.BLACK;
    
    private static final Map<?,?>   RENDERING_HINTS =
        Map.of(
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON,
            
            RenderingHints.KEY_FRACTIONALMETRICS, 
            RenderingHints.VALUE_FRACTIONALMETRICS_ON,
            
            RenderingHints.KEY_STROKE_CONTROL, 
            RenderingHints.VALUE_STROKE_PURE
        );

    public static void main(String[] args)
    {
        Image       image   = getImage();
        ImageIcon   icon    = new ImageIcon( image );

        JOptionPane.showMessageDialog(
            null,                            // Parent component (null for centered on screen)
            "view image",                    // The text message body
            "Testing",                       // The dialog title bar text
            JOptionPane.INFORMATION_MESSAGE, // The look/style type
            icon                             // Your custom ImageIcon object
        );
    }
    
    /**
     * Default constructor;
     * not used.
     */
    private CopySelectedImage()
    {
        // not used
    }
    
    public static BufferedImage getImage()
    {
        BufferedImage   image = getImage( DEF_WIDTH, DEF_HEIGHT, DEF_MARGIN );
        return image;
    }

    public static BufferedImage getImage( int width, int height, int margin )
    {
        BufferedImage   image = new BufferedImage( width, height, IMAGE_TYPE );
        Graphics2D      gtx     = image.createGraphics();
        gtx.setColor( TRANSPARENT );
        gtx.fillRect( 0, 0, width, height );
        gtx.setRenderingHints( RENDERING_HINTS );
        
        float       fudgedHeight    = height - margin;
        float       fudgedWidth     = width - 1;
        float       oneThird        = fudgeToHalf( fudgedHeight / 3.0 );
        float       beginHead       = 
            fudgeToHalf( fudgedWidth - fudgedHeight / 2 );
        float       oneHalf         = fudgeToHalf( fudgedHeight / 2.0);
        
        Rectangle2D     body        = 
            new Rectangle2D.Double( 0, oneThird, beginHead, oneThird );
        
        Path2D      head        = new Path2D.Double();
        double      fudgedHead  = beginHead - 1;
        head.moveTo( fudgedHead, 0 );
        head.lineTo( fudgedWidth, oneHalf );
        head.lineTo( fudgedHead, fudgedHeight );
        head.closePath();
        
        gtx.setColor( color );
        gtx.fill( body );
        gtx.fill( head );
//        addText( gtx, width, height );
        
        return image;
    }
    
    private static float fudgeToHalf( double coord )
    { 
        float   roundedCoord    = (int)(coord + .5);
        float   fudgedCoord     = roundedCoord + .5f;
        return fudgedCoord;
    }
}
