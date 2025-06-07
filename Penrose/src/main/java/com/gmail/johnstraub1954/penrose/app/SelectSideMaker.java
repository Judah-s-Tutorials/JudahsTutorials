package com.gmail.johnstraub1954.penrose.app;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Applic
 */
public class SelectSideMaker
{
    private static final String directoryName       = "src/main/resources";
    private static final File   directoryFile       = new File( directoryName );
    private static final String imageType           = "PNG";

    public static void main( String[] args )
    {
        SelectSideMaker    maker    = new SelectSideMaker();
        maker.execute();
    }

    private void execute()
    {
        shiftBlueToRed( "DestinationSelectRight.png", "SourceSelectRight.png" );
        shiftBlueToRed( "DestinationSelectLeft.png", "SourceSelectLeft.png" );
    }

    private void shiftBlueToRed( String inPath, String outPath )
    {
        BufferedImage   image   = getImage( inPath );
        int             width   = image.getWidth();
        int             height  = image.getHeight();
        for ( int yco = 0 ; yco < width ; ++yco )
        {
            for ( int xco = 0 ; xco < height ; ++xco )
            {
                int rgb     = image.getRGB( yco, xco );
                int alpha   = rgb & 0xFF000000;
                if ( alpha != 0 )
                {
                    float[] hsb     = getHSB( rgb );
                    int     hue     = (int)(360 * hsb[0] + .5);
                    float   sat     = hsb[1];
                    float   bright  = hsb[2];
                    float   blue    = ((hue + 120) % 360) / 360f;
                    
                    int blueShade   = 
                        Color.HSBtoRGB( blue, sat, bright ) + alpha;
                    image.setRGB( yco, xco, blueShade );
                }
            }
        }
        if ( showImage( image ) )
            writeImage( image, outPath );
    }
    
    private static BufferedImage getImage( String path )
    {
        ClassLoader     classLoader = SelectSideMaker.class.getClassLoader();
        BufferedImage   image       = null;
        try
        {
            URL         url     = classLoader.getResource( path );
            if ( url == null )
                throw new FileNotFoundException();
            image = ImageIO.read( url );
        }
        catch ( IOException | IllegalArgumentException exc )
        {
            System.err.println( path );
            exc.printStackTrace();
            System.exit( 1 );
        }
        return image;
    }
    
    private void writeImage( BufferedImage icon, String name )
    {
        File    destination = new File( directoryFile, name );
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

    private static boolean showImage( BufferedImage image )
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
    
    private static float[] getHSB( int rgb )
    {
        int     blue    = rgb & 0xFF;
        int     green   = (rgb >> 8) & 0xFF;
        int     red     = (rgb >> 16) & 0xFF;
        float[] hsb     = Color.RGBtoHSB( red, green, blue, null );
        return hsb;
    }
}
