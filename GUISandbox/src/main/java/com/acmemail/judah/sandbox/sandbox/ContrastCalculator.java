package com.acmemail.judah.sandbox.sandbox;

import java.awt.Color;

public class ContrastCalculator
{
    private static final double xierRed         = .2126;
    private static final double xierGreen       = .7152;
    private static final double xierBlue        = .0722;

    private Color       baseColor       = Color.WHITE;
    private Color       contrastColor   = Color.BLACK;
    
    public void setBaseColor( Color baseColor )
    {
        this.baseColor = baseColor;
        int     baseRGB         = baseColor.getRGB() & 0xffffff;
        int     inverseRGB      = 0xffffff - baseRGB;
        
        Color   inverseColor    = new Color( inverseRGB );
        double  baseLum         = getLuminosity( baseColor );
        double  inverseLum      = getLuminosity( inverseColor );
        double  inverseDiff     = Math.abs( baseLum - inverseLum );
        
        if (inverseDiff >= .5 )
            contrastColor = inverseColor;
        else
        {
            double  whiteDiff   = 1 - baseLum;
            double  blackDiff   = baseLum;
            contrastColor = 
                whiteDiff > blackDiff ? Color.WHITE : Color.BLACK;
        }
        System.out.println( inverseDiff + ", " + contrastColor );
    }
    
    public Color getBaseColor()
    {
        return baseColor;
    }
    
    public Color getContrastColor()
    {
        return contrastColor;
    }
    
    public static double getLuminosity( Color color )
    {
        double  fRed        = color.getRed() /  255.;
        double  fGreen      = color.getGreen() / 255.;
        double  fBlue       = color.getBlue() / 255.;
        
        double  facRed      = getComponentFactor( fRed );
        double  facGreen    = getComponentFactor( fGreen );
        double  facBlue     = getComponentFactor( fBlue );
        
        double  lum         = getLuminosity( facRed, facGreen, facBlue );
        return lum;
    }
    
    private static double getComponentFactor( double comp )
    {
        double  factor  = 0;
        if ( comp <= .03928 )
            factor = comp / 12.92;
        else
        {
            double    base = (comp + .055) / 1.055;
            factor = Math.pow( base, 2.4 );
        }
        return factor;
    }
    
    private static double 
    getLuminosity( double facRed, double facGreen, double facBlue )
    {
        double  lum     = 
            xierRed * facRed + xierGreen * facGreen + xierBlue * facBlue;
        return lum;
    }
}
