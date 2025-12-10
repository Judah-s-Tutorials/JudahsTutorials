package com.gmail.johnstraub1954.weather;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class MoonIcon
{
    private static final int    typeIntARGB = BufferedImage.TYPE_INT_ARGB;
    private static final Color  bgColor     = new Color( 0, 0, 0, 0 );
    private static final Color  lightColor  = new Color( 0xffff8f );
    private static final Color  darkColor   = new Color( 0xAAAAAA );
    private static final Color  edgeColor   = Color.BLACK;
    private static final int    edgeWidth   = 1;
    private static final int    iconWidth   = 64;
    private static final int    iconHeight  = iconWidth;
    private static final double moonPercent = .55;
    private static final int    moonWidth   = (int)(iconWidth * moonPercent);
    private static final int    moonHeight  = (int)(iconHeight * moonPercent);
    private static final int    moonXco     = (iconWidth - moonWidth) / 2;
    private static final int    moonYco     = (iconHeight - moonHeight) / 2;
    
    private final   Icon    icon;
    
    public static void main( String[] args )
    {
        for ( int inx = 0 ; inx <= 100 ; inx += 10 )
        {
            Icon    icon    = MoonIcon.of( inx, true );
            JOptionPane.showMessageDialog(
                null, 
                "Show Icon", 
                "Testing", 
                JOptionPane.INFORMATION_MESSAGE, 
                icon
            );
        }
        for ( int inx = 100 ; inx >= 0 ; inx -= 10 )
        {
            Icon    icon    = MoonIcon.of( inx, false );
            JOptionPane.showMessageDialog(
                null, 
                "Show Icon", 
                "Testing", 
                JOptionPane.INFORMATION_MESSAGE, 
                icon
            );
        }
    }
    
    private MoonIcon( int percentIllumination, String waxingOrWaning )
    {
        this( 
            percentIllumination, 
            waxingOrWaning.toUpperCase().contains( "WAX" )
        );
    }
    
    private MoonIcon( int percentIllumination, boolean waxing )
    {
        BufferedImage   image       = 
            new BufferedImage( iconWidth, iconHeight, typeIntARGB );
        Graphics2D      gtx         = image.createGraphics();
        double          percent     = ((double)percentIllumination) / 100;
        int             beginDark   = (int)(moonWidth * percent );
        if ( waxing )
            beginDark = -beginDark;
        gtx.setRenderingHint( 
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON 
        );
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, iconWidth, iconHeight );
        gtx.translate( moonXco, moonYco );
        
        Shape           background  = 
            new Ellipse2D.Double( 0, 0, moonWidth, moonHeight );
        Shape           foreground  = 
            new Ellipse2D.Double( beginDark, 0, moonWidth, moonHeight );
        Area            leftArea    = new Area( background );
        Area            rightArea   = new Area( foreground );
        leftArea.intersect( rightArea );
        gtx.setColor( lightColor );
        gtx.fill( background );
        gtx.setColor( darkColor );
        gtx.fill( leftArea );
        
        gtx.setColor( edgeColor );
        gtx.setStroke( new BasicStroke( edgeWidth ) );
        gtx.drawOval( 0, 0, moonWidth, moonHeight );
        
        ImageIcon   imageIcon   = new ImageIcon( image );
        icon = imageIcon;
    }
    
    public static Icon of( int percentIllumination, boolean waxing )
    {
        MoonIcon    moonIcon    = 
            new MoonIcon( percentIllumination, waxing );
        return moonIcon.icon;
    }
    
    public static Icon of( int percentIllumination, String waxingOrWaning )
    {
        boolean     waxing      = 
            waxingOrWaning.toUpperCase().contains( "WAX" );
        MoonIcon    moonIcon    = 
            new MoonIcon( percentIllumination, waxing );
        return moonIcon.icon;
    }
}
