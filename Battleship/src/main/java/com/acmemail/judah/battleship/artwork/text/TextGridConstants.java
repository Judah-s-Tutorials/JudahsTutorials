package com.acmemail.judah.battleship.artwork.text;

public class TextGridConstants
{
    public static final String  HOME        = "\u001B[H";
    public static final String  CLEAR       = "\u001B[2J";
    public static final String  CLEAR_SCR   = HOME+CLEAR;
    
    public static final String  FG_BLACK    = "\u001B[30m";
    public static final String  FG_RED      = "\u001B[31m";
    public static final String  FG_GREEN    = "\u001B[32m";
    public static final String  FG_YELLOW   = "\u001B[33m";
    public static final String  FG_BLUE     = "\u001B[34m";
    public static final String  FG_MAGENTA  = "\u001B[35m";
    public static final String  FG_CYAN     = "\u001B[36m";
    public static final String  FG_WHITE    = "\u001B[37m";
    public static final String  FG_DEFAULT  = "\u001B[39m";
    
    public static final String  BG_BLACK    = "\u001B[40m";
    public static final String  BG_RED      = "\u001B[41m";
    public static final String  BG_GREEN    = "\u001B[42m";
    public static final String  BG_YELLOW   = "\u001B[43m";
    public static final String  BG_BLUE     = "\u001B[44m";
    public static final String  BG_MAGENTA  = "\u001B[45m";
    public static final String  BG_CYAN     = "\u001B[46m";
    public static final String  BG_WHITE    = "\u001B[47m";
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

    /** Solid block filled with foreground color. */
    public static final char    SOLID_BLOCK = '\u2588';
}
