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
    //   Miscellaneous constants
    /////////////////////////////////////////////////
    /** The name of the application ini file. */
    public static final String  APP_PROPERTIES_NAME = "AppProperties.ini";
    
    /////////////////////////////////////////////////
    //   General grid properties
    /////////////////////////////////////////////////
    /** Grid units (pixels-per-unit) property name */
    public static final String  GRID_UNIT_PN        = "gridUnit";
    /** Grid units (pixels-per-unit) default value: float. */
    public static final String  GRID_UNIT_DV        = "65";

    /////////////////////////////////////////////////
    //   Main window properties
    /////////////////////////////////////////////////
    /** Main window width property name */
    public static final String  MW_WIDTH_PN         = "mwWidth";
    /** Main window width default value: int. */
    public static final String  MW_WIDTH_DV         = "500";
    /** Main window height property name */
    public static final String  MW_HEIGHT_PN        = "mwHeight";
    /** Main window height default value: int. */
    public static final String  MW_HEIGHT_DV        = "500";
    /** Main window background color property name */
    public static final String  MW_BG_COLOR_PN      = "mwBgColor";
    /** Main window background color default value: int. */
    public static final String  MW_BG_COLOR_DV      = "0xE6E6E6";
    
    /** Main window font color property name */
    public static final String  MW_FONT_COLOR_PN    = "mwFontColor";
    /** Main window font color default value: int. */
    public static final String  MW_FONT_COLOR_DV    = "0x000000";
    /** Main window font name property name */
    public static final String  MW_FONT_NAME_PN     = "mwFontName";
    /** Main window font color default value: String. */
    public static final String  MW_FONT_NAME_DV     = "Dialog";
    /** Main window font size property name */
    public static final String  MW_FONT_SIZE_PN     = "mwFontSize";
    /** Main window font color default value: int. */
    public static final String  MW_FONT_SIZE_DV     = "10";
    /** Main window font style property name */
    public static final String  MW_FONT_STYLE_PN    = "mwFontStyle";
    /**
     *  Label font style default value: String.
     *  One of the Font class constants:
     *  BOLD, ITALIC or PLAIN
     */
    public static final String  MW_FONT_STYLE_DV    = "PLAIN";
    /** Main window font draw property name */
    public static final String  MW_FONT_DRAW_PN     = "mwFontDraw";
    /** Main window font draw default value: boolean. */
    public static final String  MW_FONT_DRAW_DV     = "true";

    /////////////////////////////////////////////////
    //   Margin properties
    /////////////////////////////////////////////////
    /** Top margin width property name */
    public static final String  MARGIN_TOP_WIDTH_PN         = "marginTopWidth";
    /** Top margin width default value: float. */
    public static final String  MARGIN_TOP_WIDTH_DV         = "20";
    /** Top background color property name */
    public static final String  MARGIN_TOP_BG_COLOR_PN      = "marginTopBgColor";
    /** Top background color default value: int. */
    public static final String  MARGIN_TOP_BG_COLOR_DV      = "0x008080";
    
    /** Top margin font color property name */
    public static final String  MARGIN_TOP_FONT_COLOR_PN    = "marginTopFontColor";
    /** Top margin font color default value: int. */
    public static final String  MARGIN_TOP_FONT_COLOR_DV    = "0x000000";
    /** Top margin font name property name */
    public static final String  MARGIN_TOP_FONT_NAME_PN     = "marginTopFontName";
    /** Top margin font color default value: String. */
    public static final String  MARGIN_TOP_FONT_NAME_DV     = "Dialog";
    /** Top margin font size property name */
    public static final String  MARGIN_TOP_FONT_SIZE_PN     = "marginTopFontSize";
    /** Top margin font color default value: int. */
    public static final String  MARGIN_TOP_FONT_SIZE_DV     = "10";
    /** Top margin font size property name */
    public static final String  MARGIN_TOP_FONT_DRAW_PN     = "marginTopFontDraw";
    /** Top margin font draw default value: boolean. */
    public static final String  MARGIN_TOP_FONT_DRAW_DV     = "true";
    /** Top margin font style property name */
    public static final String  MARGIN_TOP_FONT_STYLE_PN    = "marginTopFontStyle";
    /**
     *  Top margin font style default value: String.
     *  One of the Font class constants:
     *  BOLD, ITALIC or PLAIN
     */
    public static final String  MARGIN_TOP_FONT_STYLE_DV    = "PLAIN";
    
    /** Right margin width property name */
    public static final String  MARGIN_RIGHT_WIDTH_PN       = "marginRightWidth";
    /** Right margin width default value: float. */
    public static final String  MARGIN_RIGHT_WIDTH_DV       = "20";
    /** Right margin background color property name */
    public static final String  MARGIN_RIGHT_BG_COLOR_PN    = "marginRightBgColor";
    /** Right margin background color default value: int. */
    public static final String  MARGIN_RIGHT_BG_COLOR_DV    = "0x008080";

    /** Right margin font color property name */
    public static final String  MARGIN_RIGHT_FONT_COLOR_PN    = "marginRightFontColor";
    /** Right margin font color default value: int. */
    public static final String  MARGIN_RIGHT_FONT_COLOR_DV    = "0x000000";
    /** Right margin font name property name */
    public static final String  MARGIN_RIGHT_FONT_NAME_PN     = "marginRightFontName";
    /** Right margin font color default value: String. */
    public static final String  MARGIN_RIGHT_FONT_NAME_DV     = "Dialog";
    /** Right margin font size property name */
    public static final String  MARGIN_RIGHT_FONT_SIZE_PN     = "marginRightFontSize";
    /** Right margin font color default value: int. */
    public static final String  MARGIN_RIGHT_FONT_SIZE_DV     = "10";
    /** Right margin font style property name */
    public static final String  MARGIN_RIGHT_FONT_STYLE_PN    = "marginRightFontStyle";
    /**
     *  Right margin font style default value: String.
     *  One of the Font class constants:
     *  BOLD, ITALIC or PLAIN
     */
    public static final String  MARGIN_RIGHT_FONT_STYLE_DV    = "PLAIN";
    /** Right margin font draw property name */
    public static final String  MARGIN_RIGHT_FONT_DRAW_PN     = "marginRightFontDraw";
    /** Right margin font draw default value: boolean. */
    public static final String  MARGIN_RIGHT_FONT_DRAW_DV     = "true";

    /** Bottom margin width property name */
    public static final String  MARGIN_BOTTOM_WIDTH_PN      = "marginBottomWidth";
    /** Bottom margin width default value: float. */
    public static final String  MARGIN_BOTTOM_WIDTH_DV      = "60";
    /** Bottom margin background color property name */
    public static final String  MARGIN_BOTTOM_BG_COLOR_PN   = "marginBottomBgColor";
    /** Bottom margin background color default value: int. */
    public static final String  MARGIN_BOTTOM_BG_COLOR_DV   = "0x008080";

    /** Bottom margin font color property name */
    public static final String  MARGIN_BOTTOM_FONT_COLOR_PN    = "marginBottomFontColor";
    /** Bottom margin font color default value: int. */
    public static final String  MARGIN_BOTTOM_FONT_COLOR_DV    = "0x000000";
    /** Bottom margin font name property name */
    public static final String  MARGIN_BOTTOM_FONT_NAME_PN     = "marginBottomFontName";
    /** Bottom margin font color default value: String. */
    public static final String  MARGIN_BOTTOM_FONT_NAME_DV     = "Dialog";
    /** Bottom margin font size property name */
    public static final String  MARGIN_BOTTOM_FONT_SIZE_PN     = "marginBottomFontSize";
    /** Bottom margin font color default value: int. */
    public static final String  MARGIN_BOTTOM_FONT_SIZE_DV     = "10";
    /** Bottom margin font style property name */
    public static final String  MARGIN_BOTTOM_FONT_STYLE_PN    = "marginBottomFontStyle";
    /**
     *  Bottom margin font style default value: String.
     *  One of the Font class constants:
     *  BOLD, ITALIC or PLAIN
     */
    public static final String  MARGIN_BOTTOM_FONT_STYLE_DV    = "PLAIN";
    /** Bottom margin font draw property name */
    public static final String  MARGIN_BOTTOM_FONT_DRAW_PN     = "marginBottomFontDraw";
    /** Bottom margin font draw default value: boolean. */
    public static final String  MARGIN_BOTTOM_FONT_DRAW_DV     = "true";

    /** Left margin width property name */
    public static final String  MARGIN_LEFT_WIDTH_PN        = "marginLeftWidth";
    /** Left margin width default value: float*/
    public static final String  MARGIN_LEFT_WIDTH_DV        = "60";
    /** Left margin background color property name */
    public static final String  MARGIN_LEFT_BG_COLOR_PN     = "marginLeftBgColor";
    /** Left margin background color default value: int. */
    public static final String  MARGIN_LEFT_BG_COLOR_DV     = "0x008080";

    /** Left margin font color property name */
    public static final String  MARGIN_LEFT_FONT_COLOR_PN    = "marginLeftFontColor";
    /** Left margin font color default value: int. */
    public static final String  MARGIN_LEFT_FONT_COLOR_DV    = "0x000000";
    /** Left margin font name property name */
    public static final String  MARGIN_LEFT_FONT_NAME_PN     = "marginLeftFontName";
    /** Left margin font color default value: String. */
    public static final String  MARGIN_LEFT_FONT_NAME_DV     = "Dialog";
    /** Left margin font size property name */
    public static final String  MARGIN_LEFT_FONT_SIZE_PN     = "marginLeftFontSize";
    /** Left margin font color default value: int. */
    public static final String  MARGIN_LEFT_FONT_SIZE_DV     = "10";
    /** Left margin font style property name */
    public static final String  MARGIN_LEFT_FONT_STYLE_PN    = "marginLeftFontStyle";
    /**
     *  Left margin font style default value: String.
     *  One of the Font class constants:
     *  BOLD, ITALIC or PLAIN
     */
    public static final String  MARGIN_LEFT_FONT_STYLE_DV    = "PLAIN";
    /** Left margin font draw property name */
    public static final String  MARGIN_LEFT_FONT_DRAW_PN     = "marginLeftFontDraw";
    /** Left margin font draw default value: boolean. */
    public static final String  MARGIN_LEFT_FONT_DRAW_DV     = "true";

    /////////////////////////////////////////////////
    //   Tic mark properties
    /////////////////////////////////////////////////
    /** Minor tic mark color property name */
    public static final String  TIC_MINOR_COLOR_PN          = "ticMinorColor";
    /** Minor tic mark color default value default value: int. */
    public static final String  TIC_MINOR_COLOR_DV          = "0X000000";
    /** Minor tic mark weight property name */
    public static final String  TIC_MINOR_WEIGHT_PN         = "ticMinorWeight";
    /** Minor tic mark weight default value: float. */
    public static final String  TIC_MINOR_WEIGHT_DV         = "2";
    /** Minor tic mark length property name */
    public static final String  TIC_MINOR_LEN_PN            = "ticMinorLen";
    /** Minor tic mark length default value: float. */
    public static final String  TIC_MINOR_LEN_DV            = "6";
    /** Minor tic marks per unit property name */
    public static final String  TIC_MINOR_MPU_PN            = "ticMinorMpu";
    /** Minor tic marks per unit default value: float. */
    public static final String  TIC_MINOR_MPU_DV            = "10";
    /** Draw minor tic marks property name */
    public static final String  TIC_MINOR_DRAW_PN           = "ticMinorDraw";
    /** Draw minor tic marks default value: boolean */
    public static final String  TIC_MINOR_DRAW_DV           = "true";
    
   /** Major tic mark color property name */
    public static final String  TIC_MAJOR_COLOR_PN          = "ticMajorColor";
   /** Major tic mark color default value: int. */
    public static final String  TIC_MAJOR_COLOR_DV          = "0X000000";
    
    /** Major tic mark weight property name */
    public static final String  TIC_MAJOR_WEIGHT_PN         = "ticMajorWeight";
    /** Major tic mark weight default value: float. */
    public static final String  TIC_MAJOR_WEIGHT_DV         = "5";
    
    /** Major tic mark length property name */
    public static final String  TIC_MAJOR_LEN_PN            = "ticMajorLen";
    /** Major tic mark length default value: float. */
    public static final String  TIC_MAJOR_LEN_DV            = "16";
    
    /** Major tic marks per unit property name */
    public static final String  TIC_MAJOR_MPU_PN            = "ticMajorMpu";
    /** Major tic marks per unit default value: float. */
    public static final String  TIC_MAJOR_MPU_DV            = "2";
    /** Draw major tic marks property name */
    public static final String  TIC_MAJOR_DRAW_PN           = "ticMajorDraw";
    /** Draw major tic marks default value: boolean */
    public static final String  TIC_MAJOR_DRAW_DV           = "true";
    
    /////////////////////////////////////////////////
    //   Grid line properties
    /////////////////////////////////////////////////
    /** Grid line weight property name */
    public static final String  GRID_LINE_WEIGHT_PN     = "gridLineWeight";
    /** Grid line weight default value: float. */
    public static final String  GRID_LINE_WEIGHT_DV     = "1";
    /** Grid lines per unit property name */
    public static final String  GRID_LINE_LPU_PN        = "gridLineLpu";
    /** Grid lines per unit default value: float. */
    public static final String  GRID_LINE_LPU_DV        = TIC_MAJOR_MPU_DV;
    /** Left margin background color property name */
    public static final String  GRID_LINE_COLOR_PN      = "gridLineColor";
    /** Left margin background color default value: int. */
    public static final String  GRID_LINE_COLOR_DV      = "0xCBCBCB";
    /** Draw grid lines property name */
    public static final String  GRID_LINE_DRAW_PN       = "gridLineDraw";
    /** Draw grid lines default value: boolean */
    public static final String  GRID_LINE_DRAW_DV       = "true";

    /////////////////////////////////////////////////
    //   Axis properties
    /////////////////////////////////////////////////
    /** Axis color property name */
    public static final String  AXIS_COLOR_PN          = "axisColor";
    /** Axis color default value: int. */
    public static final String  AXIS_COLOR_DV          = "0X000000";
    /** Axis weight property name */
    public static final String  AXIS_WEIGHT_PN         = "axisWeight";
    /** Axis weight default value: float. */
    public static final String  AXIS_WEIGHT_DV         = "2";

    /////////////////////////////////////////////////
    //   Label properties
    /////////////////////////////////////////////////
    /** Label font color property name */
    public static final String  LABEL_FONT_COLOR_PN     = "labelFontColor";
    /** Label font color default value: int. */
    public static final String  LABEL_FONT_COLOR_DV     = "0X000000";
    /** Label font name property name */
    public static final String  LABEL_FONT_NAME_PN      = "labelFontName";
    /** Label font name default value: String. */
    public static final String  LABEL_FONT_NAME_DV      = "Monospaced";
    /** Label font style property name */
    public static final String  LABEL_FONT_STYLE_PN     = "labelFontStyle";
    /**
     *  Label font style default value: String.
     *  One of the Font class constants:
     *  BOLD, ITALIC or PLAIN
     */
    public static final String  LABEL_FONT_STYLE_DV     = "PLAIN";
    /** Label font size property name */
    public static final String  LABEL_FONT_SIZE_PN      = "labelFontSize";
    /** Label font size default value: float. */
    public static final String  LABEL_FONT_SIZE_DV      = "8";
    /** Draw label property name */
    public static final String  LABEL_DRAW_PN           = "labelDraw";
    /** Draw label default value: boolean. */
    public static final String  LABEL_DRAW_DV           = "true";
    
    /** Location of user properties file property name */
    public static final String  USER_PROPERTIES_PN      = "userProperties";
    /** Location of user properties file default value. */
    public static final String  USER_PROPERTIES_DV      = "null";
    
    /////////////////////////////////////////////////
    //   Plot properties
    /////////////////////////////////////////////////
    /** Color to use when plotting a point on the grid. */
    public static final String  PLOT_COLOR_PN           = "plotColor";
    /** Default value of color to use when plotting a point on the grid. */
    public static final String  PLOT_COLOR_DV           = "20";
    
    /////////////////////////////////////////////////
    //   Profile properties
    /////////////////////////////////////////////////
    /** Name of the current profile (if any). */
    public static final String  PROFILE_NAME_PN         = "profileName";
    /** Default value of profile name */
    public static final String  PROFILE_NAME_DV         = "default";
    
    /////////////////////////////////////////////////
    //   VaiablePanelProperties properties
    /////////////////////////////////////////////////
    /** Decimal precision for displaying variable values. */
    public static final String  VP_DPRECISION_PN        = "dPrecision";
    /** Default value of precision for displaying variable values. */
    public static final String  VP_DPRECISION_DV        = "4";
    
    /////////////////////////////////////////////////
    //   Data model properties
    /////////////////////////////////////////////////
    /** Indicates if an equation is open and can be modified. */
    public static final String  DM_OPEN_EQUATION_PN     = "openEquation";
    /** Default value for "data model modified. */
    public static final String  DM_OPEN_EQUATION_DV     = "false";
    /** Indicates if data model modified since last save. */
    public static final String  DM_MODIFIED_PN          = "dataModified";
    /** Default value for "data model modified. */
    public static final String  DM_MODIFIED_DV          = "false";
    /** True if an equation file is open. */
    public static final String  DM_OPEN_FILE_PN         = "fileOpen";
    /** Default value for "equation file is open". */
    public static final String  DM_OPEN_FILE_DV         = "false";
    
    /////////////////////////////////////////////////
    //   Notification properties
    /////////////////////////////////////////////////
    /** Notifies the application that the graphic must be redrawn. */
    public static final String  REDRAW_NP               = "redraw";
    
    /////////////////////////////////////////////////
    //   Component names
    /////////////////////////////////////////////////
    /** The component name of the main application frame. */
    public static final String  CP_FRAME_CN             = "cpFrame";
    /** The component name of the equation name field. */
    public static final String  CP_EQUATION_NAME_CN     = 
        CP_FRAME_CN + "equationName";
    
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
