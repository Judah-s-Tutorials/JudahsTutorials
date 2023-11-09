package com.acmemail.judah.cartesian_plane.sandbox.fbc_test_demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerializationDemo2a
{
    private static final    String  testBase    = "test_data";
    private static final    String  subdir      = "SerialDemo2";
    private static final    File    testDir     = 
        new File( testBase, subdir );
    private static final    String  fileName    = "TestFile";
    private static final    String  fileNameFmt = "%s%04d.ser";
    
    private int     fileSeqNumber   = 0;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        new SerializationDemo2a();
    }
    
    public SerializationDemo2a()
    {
        if ( !testDir.exists() )
        {
            if ( !testDir.mkdirs() )
            {
                String  msg     = "Unable to create " + testDir.getName();
                System.err.println( msg );
                System.exit( 1 );
            }
        }
        makeLine( 50, 3, Color.ORANGE, Color.BLUE );
        makeLine( 25, 5, Color.RED, Color.GREEN );
        makeLine( 15, 13, Color.PINK, Color.BLACK );
        System.out.println( "Done." );
    }
    
    public static File getTestDir()
    {
        return testDir;
    }
    
    private void makeLine( 
        double length, 
        double weight,
        Color bgColor,
        Color fgColor
    )
    {
        final   int     width       = 150;
        final   int     height      = 100;
        final   double  centerXco   = width / 2.0;
        final   double  centerYco   = height / 2.0;
        final   int     imageType   = BufferedImage.TYPE_INT_ARGB;
        
        BufferedImage   image       = 
            new BufferedImage( width, height, imageType );
        Graphics2D      gtx         = image.createGraphics();
        double          xco1        = centerXco - length / 2;
        double          xco2        = xco1 + length;
        Line2D          line        = 
            new Line2D.Double( xco1, centerYco, xco2, centerYco );
        
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, width, height );
        
        gtx.setColor( fgColor );
        gtx.setStroke( new BasicStroke( (float)weight ) );
        gtx.draw( line );
        
        gtx.dispose();
        FBCompTADetail  detail  = 
            new FBCompTADetail( length, weight, image );
        writeDetail( detail );
    }
    
    private void writeDetail( FBCompTADetail detail )
    {
        String  fileSpec    = 
            String.format( fileNameFmt, fileName, fileSeqNumber++ );
        File    file        = new File( testDir, fileSpec );
        System.out.println( "Writing: " + file.getName() );
        
        try ( 
            FileOutputStream fileStr = new FileOutputStream( file );
            ObjectOutputStream objStr = new ObjectOutputStream( fileStr );
        )
        {
            objStr.writeObject( detail );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
}
