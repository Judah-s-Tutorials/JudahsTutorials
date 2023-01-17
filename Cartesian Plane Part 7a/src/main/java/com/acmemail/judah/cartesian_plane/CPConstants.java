package com.acmemail.judah.cartesian_plane;

import java.awt.Color;
import java.awt.Font;

/**
 * The class contains the declarations
 * of all constants for the Cartesian Plane project.
 * 
 * @author Jack Straub
 */
public class CPConstants
{
    /////////////////////////////////////////////////
    //   General grid properties
    /////////////////////////////////////////////////
    /** Grid units (pixels-per-unit) default value: float. */
    public static final String  GRID_UNIT_DV        = "65";

    /////////////////////////////////////////////////
    //   Main window properties
    /////////////////////////////////////////////////
    /** Main window width default value: int. */
    public static final String  MW_WIDTH_DV         = "500";
    /** Main window height default value: int. */
    public static final String  MW_HEIGHT_DV        = "500";
    /** Main window background color default value: int. */
    public static final String  MW_BG_COLOR_DV      = "0xE6E6E6";
    
    /////////////////////////////////////////////////
    //   Margin properties
    /////////////////////////////////////////////////
    /** Top margin width default value: float. */
    public static final String  MARGIN_TOP_WIDTH_DV         = "20";
    /** Top background color default value: int. */
    public static final String  MARGIN_TOP_BG_COLOR_DV      = "0x008080";
    /** Right margin width default value: float. */
    public static final String  MARGIN_RIGHT_WIDTH_DV       = "20";
    /** Right margin background color: int. */
    public static final String  MARGIN_RIGHT_BG_COLOR_DV    = "0x008080";
    /** Bottom margin width default value: float. */
    public static final String  MARGIN_BOTTOM_WIDTH_DV      = "60";
    /** Bottom margin background color: int. */
    public static final String  MARGIN_BOTTOM_BG_COLOR_DV   = "0x008080";
    /** Left margin width default value: float*/
    public static final String  MARGIN_LEFT_WIDTH_DV        = "60";
    /** Left margin background color: int. */
    public static final String  MARGIN_LEFT_BG_COLOR_DV     = "0x008080";
    
    /////////////////////////////////////////////////
    //   Tic mark properties
    /////////////////////////////////////////////////
    /** Minor tic mark color default value default value: int. */
    public static final String  TIC_MINOR_COLOR_DV          = "0X000000";
    /** Minor tic mark weight default value: float. */
    public static final String  TIC_MINOR_WEIGHT_DV         = "2";
    /** Minor tic mark length default value: float. */
    public static final String  TIC_MINOR_LEN_DV            = "6";
    /** Minor tic marks per unit default value: float. */
    public static final String  TIC_MINOR_MPU_DV            = "10";
    /** Draw minor tic marks default value: boolean */
    public static final String  TIC_MINOR_DRAW_DV           = "true";
    
    /** Major tic mark color property name. */
    public static final String  TIC_MAJOR_COLOR_PN          = "ticMajorColor";
    /** Major tic mark color default value: int. */
    public static final String  TIC_MAJOR_COLOR_DV          = "0X000000";
    
    /** Major tic mark weight property name. */
    public static final String  TIC_MAJOR_WEIGHT_PN         = "ticMajorWeight";
    /** Major tic mark weight default value: float. */
    public static final String  TIC_MAJOR_WEIGHT_DV         = "2";
    
    /** Major tic mark length property name. */
    public static final String  TIC_MAJOR_LEN_PN            = "ticMajorLength";
    /** Major tic mark length default value: float. */
    public static final String  TIC_MAJOR_LEN_DV            = "16";
    
    /** Major tic marks per unit default value: float. */
    public static final String  TIC_MAJOR_MPU_DV            = "2";
    /** Draw major tic marks default value: boolean */
    public static final String  TIC_MAJOR_DRAW_DV           = "true";
    
    /////////////////////////////////////////////////
    //   Grid line properties
    /////////////////////////////////////////////////
    /** Grid line weight default value: float. */
    public static final String  GRID_LINE_WEIGHT_DV     = "1";
    /** Grid lines per unit default value: float. */
    public static final String  GRID_LINE_LPU_DV        = TIC_MAJOR_MPU_DV;
    /** Left margin background color: int. */
    public static final String  GRID_LINE_COLOR_DV      = "0xCBCBCB";
    /** Draw grid lines default value: boolean */
    public static final String  GRID_LINE_DRAW_DV       = "true";

    /////////////////////////////////////////////////
    //   Axis properties
    /////////////////////////////////////////////////
    /** Axis color default value: int. */
    public static final String  AXIS_COLOR_DV          = "0X000000";
    /** Axis weight default value: float. */
    public static final String  AXIS_WEIGHT_DV         = "2";

    /////////////////////////////////////////////////
    //   Label properties
    /////////////////////////////////////////////////
    /** Label font color default value: int. */
    public static final String  LABEL_FONT_COLOR_DV     = "0X000000";
    /** Label font name default value: String. */
    public static final String  LABEL_FONT_NAME_DV      = "Monospaced";
    /**
     *  Label font style default value: String.
     *  One of the Font class constants:
     *  BOLD, ITALIC or PLAIN
     */
    public static final String  LABEL_FONT_STYLE_DV     = "PLAIN";
    /** Label font size default value: float. */
    public static final String  LABEL_FONT_SIZE_DV      = "8";
    /**
     *  Label font units default value: String.
     *  em or px.
     */
    public static final String  LABEL_FONT_UNITS_DV     = "em";
    
    /** Location of user properties file property name. */
    public static final String  USER_PROPERTIES_PN      = "userProperties";
    /** Location of user properties file default value. */
    public static final String  USER_PROPERTIES_DV      = null;
    
    /**
     * Convert a String to an int and return the int.
     * 
     * @param sVal  the String to convert
     * 
     * @return the converted int
     * 
     * @throws  NumberFormatException if sVal
     *          cannot be converted to an int
     */
    public static int asInt( String sVal )
    {
        int iVal    = Integer.parseInt( sVal );
        return iVal;
    }
    
    /**
     * Convert a String to an float and return the float.
     * 
     * @param sVal  the String to convert
     * 
     * @return the converted float
     * 
     * @throws  NumberFormatException if sVal
     *          cannot be converted to a float
     */
    public static float asFloat( String sVal )
    {
        float fVal    = Float.parseFloat( sVal );
        return fVal;
    }
    
    /**
     * Convert a String to a boolean and return the boolean.
     * The operation is case-insensitive.
     * Any value other than "true" is converted to false.
     * 
     * @param sVal  the String to convert
     * 
     * @return the converted boolean
     */
    public static boolean asBoolean( String sVal )
    {
        boolean bVal    = Boolean.parseBoolean( sVal );
        return bVal;
    }
    
    /**
     * Convert a String to a Color and return the Color.
     * The String must be encoded as an integer value.
     * Decimal integer and Hexadecimal integer values
     * are accepted.
     * (A hexadecimal string value begins with "0x" or "#".)
     * 
     * @param sVal  the String to convert
     * 
     * @return the converted Color
     * 
     * @throws  NumberFormatException if sVal
     *          cannot be converted to an integer
     */
    public static Color asColor( String sVal )
    {
        int     iVal    = Integer.decode( sVal );
        Color   cVal    = new Color( iVal );
        return cVal;
    }
    
    /**
     * Convert a String to a font style and return the result.
     * Integer values for font styles are defined in the Font class.
     * Input is case-insensitive; valid values are 
     * PLAIN, BOLD and ITALIC.
     * 
     * @param sVal  the String to convert
     * 
     * @return the converted Color
     * 
     * @throws  IllegalArgumentException if sVal
     *          cannot be converted to a font style.
     */
    public static int asFontStyle( String sVal )
    {
        String  cisVal  = sVal.toUpperCase();
        int     iVal    = -1;
        switch ( cisVal )
        {
        case "PLAIN":
            iVal = Font.PLAIN;
            break;
        case "BOLD":
            iVal = Font.BOLD;
            break;
        case "ITALIC":
            iVal = Font.ITALIC;
            break;
        default:
            String  err = 
                "\"" + sVal + "\"" + "is not a valid font style";
            throw new IllegalArgumentException( err );
        }
        return iVal;
    }
}
