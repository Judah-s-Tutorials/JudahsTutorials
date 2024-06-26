package com.acmemail.judah.cartesian_plane.sandbox.ocr;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * Application to open an image file
 * and use Tesseract to extract text.
 * Sample images containing text
 * are in the src/main/resources/images.
 * 
 * @author Jack Straub
 */
public class HelloTesseract
{
    /** 
     * Location of Tesseract data files. These files are not in the 
     * Java library. The are part of the Tesseract application
     * installation.
     */
    private static final String dataPathStr     = 
        System.getenv( "TESSDATA_PREFIX" );
    /** String identifying the path to the project root directory. */
    private static final String strUserDir      = 
        System.getProperty( "user.dir" );
    /** The project root directory. */
    private static final File   userDir         =
        new File( strUserDir );

    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        JFileChooser    chooser     = new JFileChooser();
        FileNameExtensionFilter filter = 
            new FileNameExtensionFilter( "Images", "jpg", "gif", "png" );
        chooser.setCurrentDirectory( userDir );
        chooser.setFileFilter(filter);
        
        File        file    = null;
        int         rVal    = chooser.showOpenDialog( null );
        if( rVal == JFileChooser.APPROVE_OPTION )
            file = chooser.getSelectedFile();

        new HelloTesseract( file );
    }
    
    /**
     * Constructor.
     * Performs Tesseract initialization,
     * and decodes the user's selected image file.
     * 
     * @param imageFile the selected image file
     */
    public HelloTesseract( File imageFile )
    {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath( dataPathStr );
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        try
        {
            String  result  = tesseract.doOCR( imageFile );
            JOptionPane.showMessageDialog( null, result );
        }
        catch ( TesseractException exc )
        {
            exc.printStackTrace();
        }
    }
}