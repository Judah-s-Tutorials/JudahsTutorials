package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.Font;

/**
 * Manages the parameters to use
 * when testing with the Tess4J (Tesseract) API.
 * Parameters are:
 * <ul>
 * <li>
 *      Segmentation mode:
 *      the value to provide to Tesseract.setPageSegMode(int).
 * </li>
 * <li>
 *      OCR Engine mode:
 *      the value to provide to Tesseract.setOcrEngineMode(int).
 * </li>
 * <li>
 *      Scale Factor:
 *      the recommended factor by which to resize a BufferedImage
 *      before attempting to perform OCR analysis.
 * </li>
 * <li>
 *      Font Size:
 *      the recommended font size to use in an image
 *      that is to be inspected by the Tesseract OCR engine.
 * </li>
 * <li>
 *      Font Name:
 *      the name of the recommended font family to use in an image
 *      that is to be inspected by the Tesseract OCR engine.
 * </li>
 * </ul>
 * <p>
 * To locate a property value,
 * first properties set on the command line
 * (using the -D option)
 * are scanned. 
 * If not found on the command line,
 * the environment is scanned.
 * If the property is not set 
 * on the command line or in the environment
 * a default is applied.
 * </p>
 * @author Jack Straub
 */
public class Tess4JConfig
{
    /** Tesseract segmentation mode property name. */
    public static final String  SEG_MODE_PN     = "tess4jSegMode";
    /** Tesseract segmentation mode default value. */
    public static final int     SEG_MODE_DV     = 6;
    /** Tesseract OCR engine mode property name. */
    public static final String  ENG_MODE_PN     = "tess4jEnjMode";
    /** Tesseract  OCR engine mode default value. */
    public static final int     ENG_MODE_DV     = 1;
    /** Tesseract scale factor property name. */
    public static final String  SCALE_FACTOR_PN = "tess4jScaleFactor";
    /** Tesseract scale factor default value. */
    public static final float   SCALE_FACTOR_DV = 1.5f;
    /** Tesseract font size property name. */
    public static final String  FONT_SIZE_PN    = "tess4jFontSize";
    /** Tesseract font size default value. */
    public static final float   FONT_SIZE_DV    = 14f;
    /** Tesseract font name property name. */
    public static final String  FONT_NAME_PN    = "tess4jFontName";
    /** Tesseract font name default value. */
    public static final String  FONT_NAME_DV    = Font.MONOSPACED;
    
    /**
     * Prevent instantiation of this class.
     */
    private Tess4JConfig()
    {
    }
    
    /**
     * Gets the designated Tesseract segmentation mode. 
     * @return  the designated Tesseract segmentation mode
     */
    public static int getSegmentationMode()
    {
        int     val = getProperty( SEG_MODE_PN, SEG_MODE_DV );
        return val;
    }
    
    /**
     * Gets the designated Tesseract OCR engine mode. 
     * @return  the designated Tesseract OCR engine mode
     */
    public static int getOCREngineMode()
    {
        int     val = getProperty( ENG_MODE_PN, ENG_MODE_DV );
        return val;
    }
    
    /**
     * Gets the designated Tesseract scale factor. 
     * @return  the designated Tesseract scale factor
     */
    public static float getScaleFactor()
    {
        float   val = getProperty( SCALE_FACTOR_PN, SCALE_FACTOR_DV );
        return val;
    }
    
    /**
     * Gets the designated Tesseract font size. 
     * @return  the designated Tesseract font size
     */
    public static float getFontSize()
    {
        float   val = getProperty( FONT_SIZE_PN, FONT_SIZE_DV );
        return val;
    }
    
    /**
     * Gets the designated Tesseract font name. 
     * @return  the designated Tesseract font name
     */
    public static String getFontName()
    {
        String  val = getProperty( FONT_NAME_PN, FONT_NAME_DV );
        return val;
    }
    
    /**
     * Gets the decimal value of the given property.
     *  
     * @param propName  the name of the given property
     * @param defVal    
     *      the default value to use in case the given property
     *      isn't found
     * @return  
     *      the value of the given property,
     *      or defVal if the property isn't found. 
     */
    private static float getProperty( String propName, float defVal )
    {
        String  strVal  = null;
        float   val     = defVal;
        if ( (strVal = System.getProperty( propName )) != null )
            ;
        else 
            strVal = System.getenv( propName );
        if ( strVal != null )
        {
            try
            {
                val = Float.parseFloat( strVal );
            }
            catch ( NumberFormatException exc )
            {
                exc.printStackTrace();
                val = defVal;
            }
        }
        return val;
    }
    
    /**
     * Gets the integer value of the given property.
     *  
     * @param propName  the name of the given property
     * @param defVal    
     *      the default value to use in case the given property
     *      isn't found
     * @return  
     *      the value of the given property,
     *      or defVal if the property isn't found. 
     */
    private static int getProperty( String propName, int defVal )
    {
        String  strVal  = null;
        int     val     = defVal;
        if ( (strVal = System.getProperty( propName )) != null )
            ;
        else 
            strVal = System.getenv( propName );
        if ( strVal != null )
        {
            try
            {
                val = Integer.parseInt( strVal );
            }
            catch ( NumberFormatException exc )
            {
                exc.printStackTrace();
                val = defVal;
            }
        }
        return val;
    }
    
    /**
     * Gets the String value of the given property.
     *  
     * @param propName  the name of the given property
     * @param defVal    
     *      the default value to use in case the given property
     *      isn't found
     * @return  
     *      the value of the given property,
     *      or defVal if the property isn't found. 
     */
    private static String getProperty( String propName, String defVal )
    {
        String  strVal  = null;
        String  val     = defVal;
        if ( (strVal = System.getProperty( propName )) != null )
            ;
        else 
            strVal = System.getenv( propName );
        if ( strVal != null )
            val = strVal;
        return val;
    }
}
