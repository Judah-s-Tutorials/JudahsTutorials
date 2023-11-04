package com.acmemail.judah.cartesian_plane.sandbox.fbc_test_demo;

import java.awt.image.BufferedImage;

public class FBCompTAUtils
{
    /**
     * Test two BufferedImages for equality.
     * If both images are null, 
     * they are considered equal,
     * otherwise:
     * <ol>
     * <li>Their dimensions must be equal;</li>
     * <li>Their types must be equal;</li>
     * <li>Corresponding RGB values must be equal.</li>
     * </ol>
     * @param image1    the first image to compare
     * @param image2    the second image to compare
     * 
     * @return  true if the images are equal
     */
    public static boolean 
    equals( BufferedImage image1, BufferedImage image2 )
    {
        boolean result  = image1 == image2;
        if ( result )
            ;
        else if ( image1 == null || image2 == null )
            result = false;
        else
        {
            int rows    = image1.getHeight();
            int cols    = image1.getWidth();
            if ( rows != image2.getHeight() )
                result = false;
            else if ( cols != image2.getWidth() )
                result = false;
            else if ( image1.getType() != image2.getType() )
                result = false;
            else
            {
                result = true;
                for ( int yco = 0 ; yco < rows && result ; ++yco )
                    for ( int xco = 0 ; xco < cols && result ; ++xco )
                    {
                        int rgb1 = image1.getRGB( xco, yco );
                        int rgb2 = image2.getRGB( xco, yco );
                        result = rgb1 == rgb2;
                    }
            }
        }
        return result;
    }
}
