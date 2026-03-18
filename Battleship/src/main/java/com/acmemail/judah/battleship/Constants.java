package com.acmemail.judah.battleship;

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
    public static final String  KEY_NUM_ROWS    = "numRows";
    public static final String  KEY_NUM_COLS    = "numCols";

    private Constants()
    {
    }
}
