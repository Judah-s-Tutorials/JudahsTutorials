package com.acmemail.judah.cartesian_plane;

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
    /** Right margin width property name */
    public static final String  MARGIN_RIGHT_WIDTH_PN       = "marginRightWidth";
    /** Right margin width default value: float. */
    public static final String  MARGIN_RIGHT_WIDTH_DV       = "20";
    /** Right margin background color property name */
    public static final String  MARGIN_RIGHT_BG_COLOR_PN    = "marginRightBgColor";
    /** Right margin background color default value: int. */
    public static final String  MARGIN_RIGHT_BG_COLOR_DV    = "0x008080";
    /** Bottom margin width property name */
    public static final String  MARGIN_BOTTOM_WIDTH_PN      = "marginBottomWidth";
    /** Bottom margin width default value: float. */
    public static final String  MARGIN_BOTTOM_WIDTH_DV      = "60";
    /** Bottom margin background color property name */
    public static final String  MARGIN_BOTTOM_BG_COLOR_PN   = "marginBottomBgColor";
    /** Bottom margin background color default value: int. */
    public static final String  MARGIN_BOTTOM_BG_COLOR_DV   = "0x008080";
    /** Left margin width property name */
    public static final String  MARGIN_LEFT_WIDTH_PN        = "marginLeftWidth";
    /** Left margin width default value: float*/
    public static final String  MARGIN_LEFT_WIDTH_DV        = "60";
    /** Left margin background color property name */
    public static final String  MARGIN_LEFT_BG_COLOR_PN     = "marginLeftBgColor";
    /** Left margin background color default value: int. */
    public static final String  MARGIN_LEFT_BG_COLOR_DV     = "0x008080";
    
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
    public static final String  TIC_MAJOR_WEIGHT_DV         = "2";
    
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
    /**
     *  Label font style property name 
     *  One of the Font class constants:
     *  BOLD, ITALIC or PLAIN
     */
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
    
    /** Location of user properties file property name */
    public static final String  USER_PROPERTIES_PN      = "userProperties";
    /** Location of user properties file default value. */
    public static final String  USER_PROPERTIES_DV      = null;
}
