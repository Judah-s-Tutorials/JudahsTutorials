package com.acmemail.judah.sandbox.sandbox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class ContrastCalculatorTest
{
    /**
     * @see <a href="https://contrastchecker.online/color-relative-luminance-calculator">
     *          Color Relative Luminance Calculator
     *      </a>
     */
    @Test
    void testGetLuminosity()
    {
        double  expWhiteLum     = 1.;
        double  expBlackLum     = 0.;
        double  expRedLum       = .2126;
        double  expGreenLum     = .7152;
        double  expBlueLum      = .7874;
        double  exp765432Lum    = .1042248872;
        double  exp234567Lum    = .055928412;
        double  exp888888Lum    = .2462013267;
        
        testLum( Color.WHITE, expWhiteLum );
        testLum( new Color( 0x888888 ), exp888888Lum );
        testLum( new Color( 0x765432 ), exp765432Lum );
    }
    
    @Test
    public void testMisc()
    {
        testMisc( Color.WHITE, Color.BLACK );
        testMisc( Color.WHITE, Color.WHITE );
        testMisc( Color.WHITE, new Color( 0x888888 ) );
        testMisc( Color.BLACK, new Color( 0x888888 ) );
    }
    
    public void testMisc( Color color1, Color color2 )
    {
        double  lum1    = ContrastCalculator.getLuminosity( color1 );
        double  lum2    = ContrastCalculator.getLuminosity( color2 );
        int     rgb1    = color1.getRGB() & 0xffffff;
        int     rgb2    = color2.getRGB() & 0xffffff;
        double  diff    = Math.abs( lum1 - lum2 );
        System.out.printf( "%06x, %06x: %f%n", rgb1, rgb2, diff );
    }

    private void testLum( Color color, double expLum )
    {
        double  actLum  = ContrastCalculator.getLuminosity( color );
        int     rgb     = color.getRGB() & 0x00ffffff;
        assertEquals( expLum, actLum, .00000001, Integer.toHexString( rgb ) );
    }
}
