package com.acmemail.judah.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.acmemail.judah.battleship2D.Grid2D;

/**
 * This class converts between 0-origin
 * x/y coordinates used as array indexes, 
 * and 1-origin row/column strings used by the operator.
 * For example,
 * the operator recognizes row numbers
 * using alphabetic labels
 * where "A" is the first row,
 * "Z" is the twenty-sixth row,
 * and "AA" is the 27th row.
 * When converted to y-coordinates, we get
 * "A"&#x2192;0, "B"&#x2192;1, ..., "Z"&#x2192;25, "AA"&#x2192;26, etc.
 * Likewise, the operator sees columns represented as numeric strings
 * beginning with 1. 
 * When converted to x-coordinates, we get "1"&#x2194;0, "2"&#x2194;1, etc.
 * When x- and y-coordinates are converted to row/column strings,
 * the strings are left-padded with spaces
 * in an effort to generate right-justified labels.
 * For example, if we have a grid with up to 100 rows/columns,
 * y-coordinate 0 &#x2192; row "&nbsp;&nbsp;A"
 * and y-coordinate 26 &#x2192; row "&nbsp;AA";
 * x-coordinate 0 &#x2192; column "&nbsp;&nbsp;1"
 * and x-coordinate 30 &#x2192; column "&nbsp;31."
 * 
 * @author Jack
 */
public class Label
{
    /** The length of a row. */
    private static final int        rowLen      = Grid2D.getNumRows();
    /** The length of a column. */
    private static final int        colLen      = Grid2D.getNumCols();
    /** The maximum number of digits to represent a row number. */
    private static final int        maxRowDigs;
    /** The maximum number of digits to represent a column number. */
    private static final int        maxColDigs;
    /** Format string to convert a decimal number to a string. */
    private static final String     decToStrFmt;
    /** 
     * Format string to convert an alpha number (A=1,...,Z=25,AA=26, etc.)
     * to a string.
     */
    private static final String     alphaToStrFmt;
    
    static
    {
        maxRowDigs  = (int)(log26( rowLen )) + 1;
        maxColDigs = (int)Math.log10( colLen ) + 1;
        decToStrFmt = "%" + maxColDigs + "d"; 
        alphaToStrFmt = "%" + maxRowDigs + "s";
    }
    
    /**
     * Default constructor; not used.
     */
    private Label()
    {
    }
    
    /**
     * Operator input is split into a String array
     * containing row and column coordinates.
     * Input format can be a row and column separated 
     * by spaces and/or commas ("B,10") or,
     * as in traditional Battleship,
     * glued together("B10").
     * If glued together,
     * the row coordinate ends,
     * and the column coordinate begins,
     * with the first digit.
     * The output is an array of two strings.
     * If the input can be divided into two tokens,
     * the first (presumably the row coordinate)
     * is returned in the first element of the array,
     * and the second is returned in the second element of the array.
     * If the input cannot be divided
     * into exactly two tokens,
     * one or both elements of the result array
     * will be the empty string.
     * No further validation is performed on the input.
     * Examples:
     * <ul>
     * <li>"B10" &#x2192; {"B","10"}</li>
     * <li>"B,10" &#x2192; {"B","10"}</li>
     * <li>"B   10" &#x2192; {"B","10"}</li>
     * <li>"B, 10" &#x2192; {"B","10"}</li>
     * <li>"q5"  &#x2192; {"q","5"}</li>
     * <li>"ABC"  &#x2192; {"ABC",""}</li>
     * <li>"10"  &#x2192; {"","10"}</li>
     * </ul>
     * <p>
     * If the output consists of at least one empty string,
     * you must assume that the input was invalid.
     * However, if two non-empty strings are returned,
     * there is no guarantee that either 
     * is a valid row or column,
     * as in the case of "q1A", 
     * which will yield {"q","1A"},
     * and "5, b", which will yield {"5","b"}.
     *
     * @param input the input to parse
     * 
     * @return  an array of two strings containing the result
     *          of dividing the input into two tokens
     */
    public static String[] parseRowCol( String input )
    {
        String[]        result      = { "", "" };
        StringTokenizer tizer       = new StringTokenizer( input, "\\ ," );
        int             numTokens   = tizer.countTokens();
        if ( numTokens < 1 || numTokens > 2 )
            ;
        else if ( numTokens == 2 )
        {
            result[0] = tizer.nextToken();
            result[1] = tizer.nextToken();
        }
        else
        {
            boolean split   = false;
            int     len     = input.length();
            for ( int inx = 0 ; inx < len && !split ; ++inx )
            {
                char    ccc = input.charAt( inx );
                if ( Character.isDigit( ccc ) )
                {
                    split = true;
                    result[0] = input.substring( 0, inx );
                    result[1] = input.substring( inx );
                }
            }
        }
        return result;
    }
    
    /**
     * Convert a given integer column index (0-origin)
     * to a right-justified, 1-origin string.
     * For example, if a grid has 40 columns:
     * <ul style="font-family: monospace;">
     * <li>0 &#x2192; &nbsp;"&nbsp;1"</li>
     * <li>8 &#x2192; &nbsp;"&nbsp;9"</li>
     * <li>9 &#x2192; &nbsp;"10"</li>
     * <li>10 &#x2192; "11"</li>
     * </ul>
     * @param num   the given integer
     * 
     * @return the converted string
     */
    public static String intToString( int num )
    {
        if ( num < 0 )
        {
            String  fmt     = "Input (%d) must be >= 0";
            String  message = String.format( fmt,  num );
            throw new BattleshipException( message );
        }
        String  str = String.format( decToStrFmt, num + 1 );
        return str;
    }
    
    /**
     * Convert a 1-origin column number represented as a string
     * to a 0-origin integer index. 
     * For example:
     * <ul style="font-family: monospace;">
     * <li>&nbsp;"&nbsp;1" &#x2192; 0</li>
     * <li>&nbsp;"&nbsp;9" &#x2192; 8</li>
     * <li>&nbsp;"10" &#x2192; 9</li>
     * <li>&nbsp;"11" &#x2192; 10</li>
     * </ul>
     * 
     * @param str   column number represented as a 1-origin string
     * 
     * @return the converted, 0-origin integer index
     * 
     * @throws  NullPointerException if the input is null
     * @throws  BattleshipException 
     *          if the input string cannot be converted
     */
    public static int colStrToInt( String str )
    {
        int     result  = 0;
        String  worker  = str.trim();
        if ( worker.isEmpty() )
        {
            String  message = "Cannot convert empty string to int";
            throw new BattleshipException( message );
        }
        
        try
        {
            result = Integer.parseInt( worker );
            if ( result < 1 )
            {
                String  message = "Not a valid column number: " + worker;
                throw new BattleshipException( message );
            }
            // String column numbers start at 1; integer column numbers
            // start at 0;
            --result;
        }
        catch ( NumberFormatException exc )
        {
            String  message = "Cannot convert to int: " + worker;
            throw new BattleshipException( message );
        }
        
        return result;
    }
    
    /**
     * Convert a 0-origin, integer row index
     * to a 1-origin, right-justified string, 
     * where A=1, ... Z=26, etc.  
     * For example, if a grid has a maximum of 40 rows:
     * <ul style="font-family: monospace;">
     * <li>0 &#x2192; &nbsp;"&nbsp;A"</li>
     * <li>8 &#x2192; &nbsp;"&nbsp;I"</li>
     * <li>26 &#x2192; &nbsp;"&nbsp;Z"</li>
     * <li>27 &#x2192; &nbsp;"AA"
     * <li>28 &#x2192; &nbsp;"AB"
     * </ul>
     * 
     * @param num   the 0-origin row index
     * 
     * @return the converted, 1-origin string
     * 
     * @throws BattleshipException if input is less than 0
     */
    public static String decimalToAlpha( int num )
    {
        if ( num < 0 )
        {
            String  fmt     = "Input (%d) must be >= 0"; //$NON-NLS-1$
            String  message = String.format( fmt,  num );
            throw new BattleshipException( message );
        }
        int             worker  = num + 1;
        StringBuilder   bldr    = new StringBuilder();
        while ( worker > 0 )
        {
            int     digit   = (worker - 1) % 26;
            char    letter  = (char)('A' + digit);
            bldr.insert( 0, letter );
            worker = (worker - 1) / 26;
        }
        String  padded  = String.format( alphaToStrFmt, bldr );
        return padded;
    }
    
    /**
     * Convert a 1-origin, alpha row number (A=1, B=2, etc.)
     * represented as a string to a 0-origin integer index. 
     * For example:
     * <ul style="font-family: monospace;">
     * <li>&nbsp;"&nbsp;A" &#x2192; 0</li>
     * <li>&nbsp;"&nbsp;I" &#x2192; 8</li>
     * <li>&nbsp;"&nbsp;Z" &#x2192; 26 </li>
     * <li>&nbsp;"AB" &#x2192; 28 </li>
     * </ul>
     * 
     * @param alpha row number represented as a 1-origin, 
     *              alphabetic string
     * 
     * @return the converted, 0-origin integer
     * 
     * @throws  NullPointerException 
     *          if the input string is null
     * 
     * @throws  BattleshipException 
     *          if the input string cannot be converted as expected
     */
    public static int alphaToDecimal( String alpha )
    {
        String  worker  = alpha.trim();
        if ( worker.isEmpty() )
        {
            String  message = "Cannot convert empty string to decimal";
            throw new BattleshipException( message );
        }
        int     len     = alpha.length();
        int     num     = 0;
        for ( int inx = 0 ; inx < len ; ++inx )
        {
            char    letter  = alpha.charAt( inx );
            if ( !Character.isUpperCase( letter) )
            {
                String  message = "Cannot convert \"" + worker + "\" to decimal";
                throw new BattleshipException( message );
            }
            int     value   = letter - 'A' + 1;
            num = num * 26 + value;
        }
        --num;
        return num;
    }
    
    /**
     * Verify that a given string can be converted to an 
     * alphabetic row number.
     * Alphabetic row numbers begin at 'A'
     * and only contain uppercase characters.
     * A list of errors is returned;
     * if no errors are found, the list will be empty.
     * 
     * @param strRow    the given string
     * 
     * @return  A list of error messages; empty if no errors are found
     */
    public static List<String> validateRowStr( String strRow )
    {
        List<String>    errors  = new ArrayList<>();
        if ( !validateNonNull( strRow, errors ) )
            ;
        else if ( !validateNonEmpty( strRow, errors ) )
            ;
        else if ( !validateAlpha( strRow, errors ) )
            ;
        else
            ;
        return errors;
    }
        
    /**
     * Verify that a given string can be converted to a 
     * numeric column index.
     * Column numbers begin at '1'
     * and only contain numeric characters.
     * A list of errors is returned;
     * if no errors are found, the list will be empty.
     * 
     * @param strCol    the given string
     * 
     * @return  A list of error messages; empty if no errors are found
     */
    public static List<String> validateColStr( String strCol )
    {
        List<String>    errors  = new ArrayList<>();
        if ( !validateNonNull( strCol, errors ) )
            ;
        else if ( !validateNonEmpty( strCol, errors ) )
            ;
        else if ( !validateNumeric( strCol, errors ) )
            ;
        else if ( !validateOneOrigin( strCol, errors ) )
            ;
        else
            ;
        return errors;
    }
    
    /**
     * Verify that an input string is not null.
     * If the test fails, an error message is added to the given list 
     * of error messages.
     * 
     * @param input     the input to test
     * @param errors    the given list of error messages
     * 
     * @return  true if the test succeeds
     */
    private static boolean 
    validateNonNull( String input, List<String> errors )
    {
        final String    error   = "Input may not be null";
        boolean result  = input != null;
        if ( !result )
            errors.add( error );
        return result;
    }
    
    /**
     * Verify that an input string is not empty.
     * If the test fails, an error message is added to the given list 
     * of error messages.
     * <p>
     * Precondition: input is not null
     * 
     * @param input     the input to test
     * @param errors    the given list of error messages
     * 
     * @return  true if the test succeeds
     */
    private static boolean 
    validateNonEmpty(String input, List<String> errors )
    {
        final String    error   = "Input may not be empty";
        boolean         result  = !input.isEmpty();
        if ( !result )
            errors.add( error );
        return result;
    }
    
    /**
     * Verify that an input string 
     * consists solely of numeric characters (0-9).
     * If the test fails, an error message is added to the given list 
     * of error messages.
     * <p>
     * Precondition: input is not null
     * <p>
     * Precondition: input is not empty
     * 
     * @param input     the input to test
     * @param errors    the given list of error messages
     * 
     * @return  true if the test succeeds
     */
    private static boolean 
    validateNumeric( String input, List<String> errors )
    {
        final String    errorMessage    = "Not a numeric character: ";
        boolean         result          = true;
        for ( char ccc : input.toCharArray() )
        {
            if ( ccc < '0' || ccc > '9' )
            {
                errors.add( errorMessage + ccc );
                result = false;
            }
        }
        return result;
    }
    
    /**
     * Verify that an input string 
     * consists solely of uppercase characters (A-Z).
     * If the test fails, an error message is added to the given list 
     * of error messages.
     * <p>
     * Precondition: input is not null
     * <p>
     * Precondition: input is not empty
     * 
     * @param input     the input to test
     * @param errors    the given list of error messages
     * 
     * @return  true if the test succeeds
     */
    private static boolean 
    validateAlpha( String input, List<String> errors )
    {
        final String    errorMessage    = "Not an alphabetic character: ";
        boolean         result          = true;
        for ( char ccc : input.toCharArray() )
        {
            if ( !Character.isUpperCase( ccc ) )
            {
                errors.add( errorMessage + ccc );
                result = false;
            }
        }
        return result;
    }
    
    /**
     * Verify that a numeric input string 
     * can be converted to an integer &#x2265; 1.
     * If the test fails, an error message is added to the given list 
     * of error messages.
     * <p>
     * Precondition: input is a valid numeric string
     * 
     * @param input     the input to test
     * @param errors    the given list of error messages
     * 
     * @return  true if the test succeeds
     */
    private static boolean 
    validateOneOrigin( String input, List<String> errors )
    {
        final String    errorMessage    = "Input must be greater than 0: ";
        int             intNum          = Integer.parseInt( input );
        boolean         result          = intNum > 0;
        if ( !result )
        {
            errors.add( errorMessage + input );
            result = false;
        }
        return result;
    }
    
    /**
     * Given a decimal integer,
     * calculate its logarithm in base 26
     * (log<sub>26</sub>(num)).
     * This tells us how many places are required
     * to count up to <em>num</em> in base 26, where 
     * 'A' = 0 and 'Z' = 25.
     * Example 1:
     * <pre style="padding-left:2em;"> // A grid has 10 rows, labeled A-J.
     * // To determine how much space to allocate for the labels,
     * // calculate the maximum number of characters to be used in 
     * // a label:
     * //     (int)(log26(10) + 1) = (int)(.7067 + 1) = 10</pre>
     * <p>
     * Example 2:
     * <pre style="padding-left:2em;"> // A grid has 40 rows, labeled A-AT.
     * // To determine how much space to allocate for the labels,
     * // calculate the maximum number of characters needed for
     * // a label:
     * //     (int)(log26(40) + 1) = (int)(1.132 + 1) = 2</pre>
     * 
     * @param num   the given decimal number
     * 
     * @return log<sub>26</sub>(num)
     */
    private static double log26( int num )
    {
        double  log26   = Math.log10( num ) / Math.log10( 26 );
        return log26;
    }
}
