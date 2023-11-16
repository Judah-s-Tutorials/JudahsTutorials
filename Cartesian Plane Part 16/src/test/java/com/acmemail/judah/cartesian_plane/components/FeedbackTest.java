package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.swing.JFrame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;
import com.acmemail.judah.cartesian_plane.test_utils.fb_comp.FBCompTA;
import com.acmemail.judah.cartesian_plane.test_utils.fb_comp.FBCompTADetail;

abstract class FeedbackTest
{
    private static final String baseSubdir      = 
        Utils.BASE_TEST_DATA_DIR + "/" + FBCompTA.FEEDBACK_DIR;
    private static File         subdir;
    private static File[]       allFiles = null;
    private static Feedback     feedback;
    private BufferedImage       expImage;
    
    private static double  currVal     = 0;
    
//    public FeedbackTest( Function<DoubleSupplier,Feedback> fbSupplier )
//    {
//        File    base    = new File( baseSubdir );
//        this.subdir = new File( base, subdir );
//        String  msg     = 
//            "Test directory doesn't exist: " + this.subdir.getName();
//        assertTrue ( this.subdir.exists(), msg );
//
//        allFiles = this.subdir.listFiles( (d,f) -> f.endsWith( ".ser" ) );
//        msg = "Directory has no test files: " + this.subdir.getName();
//        assertTrue( allFiles.length > 0, msg );

//        GUIUtils.schedEDTAndWait( () -> makeGUI( fbSupplier ) );
//    }
    
    public static void makeGUI( 
        String subdir, 
        Function<DoubleSupplier,Feedback> fbSupplier
    )
    {
        getFiles( subdir );
        feedback = fbSupplier.apply( () -> currVal );
        feedback.setPreferredSize( FBCompTA.COMP_SIZE );
        System.out.println( FBCompTA.COMP_SIZE );
        JFrame  frame   = new JFrame();
        frame.getContentPane().add( feedback );
        frame.pack();
        frame.setVisible( true );
    }

    @ParameterizedTest
    @MethodSource( "getFiles" )
    void test( File file )
    {
        System.out.println( file.getName() );
        Utils.pause( 2000 );
        nextFile( file );
    }

    @Test
    public void test2()
    {
        System.out.println( subdir );
    }
    
    @Test
    public void test3()
    {
        System.out.println( feedback.getClass().getName() );
    }
    
    private void nextFile( File nextFile )
    {
        FBCompTADetail  detail      = getDetail( nextFile );
        currVal = (float)detail.getPropertyValue();
        double          weight      = detail.getWeight();
        feedback.setWeight( (float)weight );
        expImage = detail.getBufferedImage();
        
        GUIUtils.schedEDTAndWait( () -> feedback.repaint() );
        BufferedImage   actImage    = getActualImage();
        
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "Value: " ).append( currVal );
        bldr.append( ",Weight: ").append( weight );
        bldr.append( ",File: ").append( nextFile.getName() );
        String          msg     = bldr.toString();
        assertTrue( Utils.equals( expImage, actImage ), msg );
    }

    private FBCompTADetail getDetail( File file )
    {
        FBCompTADetail  detail  = null;
        try (
            FileInputStream fileStream = new FileInputStream( file );
            ObjectInputStream inStream = 
                new ObjectInputStream( fileStream );
        )
        {
            Object  obj     = inStream.readObject();
            assertTrue( obj instanceof FBCompTADetail );
            detail = (FBCompTADetail)obj;
        }
        catch ( IOException | ClassNotFoundException exc )
        {
            fail( exc.getMessage() );
        }
        return detail;
    }
    
    private BufferedImage getActualImage()
    {
        int             width       = FBCompTA.COMP_SIZE.width;
        int             height      = FBCompTA.COMP_SIZE.height;
        int             type        = expImage.getType();
        BufferedImage   actImage    = 
            new BufferedImage( width, height, type );
        Graphics        graphics    = actImage.createGraphics();
        GUIUtils.schedEDTAndWait( () -> 
            feedback.paintComponent( graphics ) 
        );
        
        return actImage;
    }
    
    public static void getFiles( String strSubdir )
    {
        File    base    = new File( baseSubdir );
        File    subdir  = new File( base, strSubdir );
        String  msg     = 
            "Test directory doesn't exist: " + subdir.getName();
        assertTrue ( subdir.exists(), msg );

        allFiles = subdir.listFiles( (d,f) -> f.endsWith( ".ser" ) );
        msg = "Directory has no test files: " + subdir.getName();
        assertTrue( allFiles.length > 0, msg );
    }
    
    private static Stream<File> getFiles()
    {
        return Stream.of( allFiles );
    }
}
