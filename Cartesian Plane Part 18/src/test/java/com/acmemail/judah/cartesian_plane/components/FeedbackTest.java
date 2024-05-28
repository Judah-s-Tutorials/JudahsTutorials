package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import javax.swing.JPanel;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;
import com.acmemail.judah.cartesian_plane.test_utils.fb_comp.FBCompTA;
import com.acmemail.judah.cartesian_plane.test_utils.fb_comp.FBCompTADetail;

public abstract class FeedbackTest
{
    private static final String baseSubdir      = 
        Utils.BASE_TEST_DATA_DIR + "/" + FBCompTA.FEEDBACK_DIR;
    private static String       strSubdir;
    private static File[]       allFiles;
    private static Feedback     feedback;
    private static double       currVal     = 0;
    private static double       currWeight  = -1;
    
    private BufferedImage       expImage;
    private BufferedImage       actImage;
    
    // The method must be invoked on the EDT.
    public static void initAll( 
        String subdir, 
        Function<DoubleSupplier,Feedback> fbSupplier
    )
    {
        strSubdir = subdir;
        loadFiles();
        feedback = fbSupplier.apply( () -> currVal );
        feedback.setPreferredSize( FBCompTA.COMP_SIZE );
        JFrame frame = new JFrame();
        JPanel  contentPane = new JPanel();
        contentPane.add( feedback );
        frame.setContentPane( contentPane );
        frame.pack();
    }

    @ParameterizedTest
    @MethodSource( "getFiles" )
    public void test( File file )
    {
        GUIUtils.schedEDTAndWait( () -> nextFile( file ) );
        assertEquals( currWeight, feedback.getWeight() );
        assertTrue( feedback.isOpaque() );
    }
    
    /**
     * Deserializes a file
     * and updates the state of the test
     * with the contents
     * of the encapsulated FBCompTADetail
     * THIS METHOD MUST BE CALLED
     * FROM THE EVENT DISPATCH THREAD.
     * 
     * @param nextFile
     */
    private void nextFile( File nextFile )
    {
        FBCompTADetail  detail      = getDetail( nextFile );
        currVal = detail.getPropertyValue();
        currWeight = detail.getWeight();
        expImage = detail.getBufferedImage();
        
        feedback.setWeight( (float)currWeight );
        feedback.repaint();
        actImage = getActualImage();
        
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "Dir: ").append( strSubdir );
        bldr.append( ", File: ").append( nextFile.getName() );
        bldr.append( ", Value: " ).append( currVal );
        bldr.append( ", Weight: ").append( currWeight );
        String          msg     = bldr.toString();
        System.out.println( msg );
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
    
    /**
     * Makes a copy of the actual image
     * currently displayed
     * in the Feedback component.
     * THIS METHOD MUST BE CALLED
     * FROM THE EVENT DISPATCH THREAD.
     * 
     * @return  
     *      a copy of the actual image currently displayed
     *      in the Feedback component
     */
    private BufferedImage getActualImage()
    {
        int             width       = feedback.getWidth();
        int             height      = feedback.getHeight();
        int             type        = expImage.getType();
        BufferedImage   actImage    = 
            new BufferedImage( width, height, type );
        Graphics        graphics    = actImage.createGraphics();
        feedback.paintComponent( graphics );
        
        return actImage;
    }
    
    private static void loadFiles()
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
