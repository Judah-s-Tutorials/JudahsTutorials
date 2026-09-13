package com.acmemail.judah.battleship.artwork.awt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

class SplatTest
{
    @Test
    void testSplat()
    {
        Splat           splat   = new Splat();
        Splat.Params    params  = splat.getParams();
        Image           image   = splat.getImage();
        validateImage( params, image );
    }

    @Test
    void testSetParams()
    {
        Splat           splat   = new Splat();
        Splat.Params    params  = splat.getParams();
        params.crownRadius = Math.ceil( params.crownRadius + 5 );
        int             iFill   = params.fillColor.getRGB() + 5;
        int             iEdge   = params.edgeColor.getRGB() + 5;
        params.fillColor = new Color( iFill );
        params.edgeColor = new Color( iEdge );
        splat.setParams( params );
        Image           image   = splat.getImage();
        validateImage( params, image );
    }

    private static void validateImage( Splat.Params expParams, Image image )
    {
        int     width   = image.getWidth( null );
        int     height  = image.getHeight( null );
        int     expSize = (int)Math.round( expParams.crownRadius * 2 );
        assertApproximatelyEqual( expSize, width );
        assertApproximatelyEqual( expSize, height );
        assertTrue( contains( image, expParams.edgeColor ) );
        assertTrue( contains( image, expParams.fillColor ) );
    }
    
    private static void assertApproximatelyEqual( int exp, int act )
    {
        assertTrue( act >= exp - 1 );
        assertTrue( act <= exp + 1 );
    }
    
    private static boolean contains( Image image, Color color )
    {
        int             iColor      = color.getRGB();
        BufferedImage   bufImage    = convert( image );
        int             width       = bufImage.getWidth( null );
        int             height      = bufImage.getHeight( null );
        int             numPixels   = width * height;
        System.out.printf( "%08x%n", iColor );
        boolean         contains    =
            IntStream.range( 0, numPixels )
                .boxed()
                .map( i -> bufImage.getRGB( i % width, i / height ) )
                .peek( i -> System.out.printf( "%08x%n", i ) )
                .filter( i -> i == iColor )
                .findAny().isPresent();
        return contains;
    }
    
    private static BufferedImage convert( Image image )
    {
        int             width       = image.getWidth( null );
        int             height      = image.getHeight( null );
        int             type        = BufferedImage.TYPE_INT_ARGB;
        BufferedImage   bufImage    = new BufferedImage( width, height, type );
        Graphics2D      gtx         = bufImage.createGraphics();
        gtx.drawImage( image, 0, 0, null );
        return bufImage;
    }
}
