package com.acmemail.judah.battleship2D;

public class Constants
{
    /**
     * When defining a value in the System properties, the name-prefix
     * is used to make sure property names are unique, for example:
     * <pre>    String propName   = 
     *         Constants.NAME_PREFIX + Constants.KEY_NUM_ROWS;
     *          String numRowsVal =
     */
    public static final String  NAME_PREFIX     = 
        "com.acmemail.judah.battleship.";
    
    /* Naming convention same as RenderingHints: "KEY_...description */
    /** Key to lookup number-of-rows property. */
    public static final String  KEY_NUM_ROWS    = "numRows";
    /** Key to lookup number-of-columns property. */
    public static final String  KEY_NUM_COLS    = "numCols";
    
    /*************************/
    /* Default ship lengths. */
    /*************************/
    /** Default length of a battleship. */
    public static final int     DEF_BATTLESHIP_LEN      = 4;
    /** Default length of a carrier. */
    public static final int     DEF_CARRIER_LEN         = 5;
    /** Default length of a cruiser. */
    public static final int     DEF_CRUISER_LEN         = 3;
    /** Default length of a destroyer. */
    public static final int     DEF_DESTROYER_LEN       = 2;
    /** Default length of a submarine. */
    public static final int     DEF_SUBMARINE_LEN       = 3;
    /** Default breadth of a battleship. */
    public static final int     DEF_BATTLESHIP_BREADTH  = 1;
    /** Default breadth of a carrier. */
    public static final int     DEF_CARRIER_BREADTH     = 1;
    /** Default breadth of a cruiser. */
    public static final int     DEF_CRUISER_BREADTH     = 1;
    /** Default breadth of a destroyer. */
    public static final int     DEF_DESTROYER_BREADTH   = 1;
    /** Default breadth of a submarine. */
    public static final int     DEF_SUBMARINE_BREADTH   = 1;
    
    /*************************/
    /* Default ship names. */
    /*************************/
    /** Default NAMEgth of a battleship. */
    public static final String  DEF_BATTLESHIP_NAME  = "Battleship";
    /** Default NAMEgth of a carrier. */
    public static final String  DEF_CARRIER_NAME     = "Carrier";
    /** Default NAMEgth of a cruiser. */
    public static final String  DEF_CRUISER_NAME     = "Cruiser";
    /** Default NAMEgth of a destroyer. */
    public static final String  DEF_DESTROYER_NAME   = "Destroyer";
    /** Default NAMEgth of a submarine. */
    public static final String  DEF_SUBMARINE_NAME   = "Submarine";

    /**
     * Constructor; not used.
     */
    private Constants()
    {
    }
}
