package com.acmemail.judah.battleship.artwork.text;

/**
 * This class contains constant declarations
 * for the support of {@link BasicTextGrid}
 * and related classes.
 */
public class TextGridConstants
{
    /** ANSI escape sequence to move a console's cursor to the home position. */
    public static final String  HOME        = "\u001B[H";
    /** 
     * ANSI escape sequence to clear a console's display
     * from the current cursor position to the end of the screen.
     */
    public static final String  CLEAR       = "\u001B[2J";
    /** Combination of ANSI escape sequences to clear a console's screen. */
    public static final String  CLEAR_SCR   = HOME+CLEAR;
    
    /** ANSI escape sequence to set a console's foreground color to black. */
    public static final String  FG_BLACK    = "\u001B[30m";
    /** ANSI escape sequence to set a console's foreground color to red. */
    public static final String  FG_RED      = "\u001B[31m";
    /** ANSI escape sequence to set a console's foreground color to green. */
    public static final String  FG_GREEN    = "\u001B[32m";
    /** ANSI escape sequence to set a console's foreground color to yellow. */
    public static final String  FG_YELLOW   = "\u001B[33m";
    /** ANSI escape sequence to set a console's foreground color to blue. */
    public static final String  FG_BLUE     = "\u001B[34m";
    /** ANSI escape sequence to set a console's foreground color to magenta. */
    public static final String  FG_MAGENTA  = "\u001B[35m";
    /** ANSI escape sequence to set a console's foreground color to cyan. */
    public static final String  FG_CYAN     = "\u001B[36m";
    /** ANSI escape sequence to set a console's foreground color to white. */
    public static final String  FG_WHITE    = "\u001B[37m";
    /** ANSI escape sequence to set a console's foreground color to the default. */
    public static final String  FG_DEFAULT  = "\u001B[39m";
    
    /** ANSI escape sequence to set a console's background color to black. */
    public static final String  BG_BLACK    = "\u001B[40m";
    /** ANSI escape sequence to set a console's background color to red. */
    public static final String  BG_RED      = "\u001B[41m";
    /** ANSI escape sequence to set a console's background color to green. */
    public static final String  BG_GREEN    = "\u001B[42m";
    /** ANSI escape sequence to set a console's background color to yellow. */
    public static final String  BG_YELLOW   = "\u001B[43m";
    /** ANSI escape sequence to set a console's background color to blue. */
    public static final String  BG_BLUE     = "\u001B[44m";
    /** ANSI escape sequence to set a console's background color to magenta. */
    public static final String  BG_MAGENTA  = "\u001B[45m";
    /** ANSI escape sequence to set a console's background color to cyan. */
    public static final String  BG_CYAN     = "\u001B[46m";
    /** ANSI escape sequence to set a console's background color to white. */
    public static final String  BG_WHITE    = "\u001B[47m";
    /** ANSI escape sequence to set a console's background color to the default. */
    public static final String  BG_DEFAULT  = "\u001B[49m";

    /** Box-drawing character: horizontal line. */
    public static final char    HLINE       = '\u2500';
    /** Box-drawing character: vertical line. */
    public static final char    VLINE       = '\u2502';
    /** Box-drawing character: top/left. */
    public static final char    TLC         = '\u250C';
    /** Box-drawing character: top/middle. */
    public static final char    TMC         = '\u252C';
    /** Box-drawing character: top/right. */
    public static final char    TRC         = '\u2510';
    /** Box-drawing character: bottom/left. */
    public static final char    BLC         = '\u2514';
    /** Box-drawing character: bottom/middle. */
    public static final char    BMC         = '\u2534';
    /** Box-drawing character: bottom/right. */
    public static final char    BRC         = '\u2518';
    /** Box-drawing character: middle/left. */
    public static final char    MLC         = '\u251C';
    /** Box-drawing character: middle/middle. */
    public static final char    MMC         = '\u253C';
    /** Box-drawing character: middle/right. */
    public static final char    MRC         = '\u2524';

    /** Unicode character for a block filled with the foreground color. */
    public static final char    SOLID_BLOCK = '\u2588';

    /**
     * Default constructor, not used.
     */
    private TextGridConstants()
    {
        // not used
    }
}
