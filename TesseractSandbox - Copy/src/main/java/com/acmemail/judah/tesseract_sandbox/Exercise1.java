package com.acmemail.judah.tesseract_sandbox;

import java.io.File;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Exercise1
{
    private static final String imagePathStr    = 
        "src/main/resources/images";
    private static final File   imagePath       = 
        new File( imagePathStr );
    private static final String imageName       = 
        "TestImage16Point.png";
//        "Notepad++Snap.png";
    private static final File   imageFile       = 
        new File( imagePathStr, imageName );
    private static final String dataPathStr     = 
        System.getenv( "TESSDATA_PREFIX" );
    private static final File   dataPath        = 
        new File( dataPathStr );

    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        System.out.println( dataPathStr );
        new Exercise1();
    }
    
    public Exercise1()
    {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath( dataPathStr );
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        try
        {
            String  result  = tesseract.doOCR( imageFile );
            System.out.println( result );
        }
        catch ( TesseractException exc )
        {
            exc.printStackTrace();
        }
    }
}
