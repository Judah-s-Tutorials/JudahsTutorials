package com.acmemail.judah.battleship;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.acmemail.judah.battleship2D.GridCoords;

/**
 * This class converts between 0-origin
 * x- and y- coordinates used as array indexes, 
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
 * When converted to x-coordinates, we get "1"&#x2192;0, "2"&#x2192;1, etc.
 * <p>
 * The label supplied by the client
 * may use a sticky format
 * where the column identifier immediately follows the row identifier,
 * for example, "B9."
 * The format used may separate the row and column with whitespace ("B 9")
 * and/or a comma ("B,9", "B, 9").
 * The column identifier must follow the row identifier.
 * Whitespace at the beginning and end of the label is ignored.
 * 
 * @author Jack
 */
public class Label
{
    private static final String     regexStr    = 
        "^\\s*([a-zA-Z]+)[\\s,]*(\\d+)\\s*$";
    private static final Pattern    regexPat    = Pattern.compile( regexStr );
    
    /** The complete alphanumeric label, e.g. "A10". */
    private String  label;
    /** The 1-origin alphanumeric row ID, e.g. "A". */
    private String  rowStr;
    /** The 0-origin numeric row ID. */
    private int     yco;
    /** The 1-origin alphanumeric column ID, e.g. "10". */
    private String  colStr;
    /** The 0-origin numeric column ID. */
    private int     xco;
    /** Message describing operation outcome. */
    private String  message = StatusMessages.PARSE_FAILED;
    /** True if the operation completes successfully. */
    private boolean status  = false;
    
    /**
     * Encapsulates the given 0-origin numeric grid coordinates,
     * while converting them to 1-origin alphanumeric coordinates.
     * The operation can silently fail;
     * the client should explicitly consult the status 
     * after the operation completes.
     * 
     * @param coords    the given grid coordinates
     * 
     * @throws NullPointerException if coords is null
     */
    public Label( GridCoords coords )
    {
        this(
            Objects.requireNonNull( coords, "coords" ).xco(),
            coords.yco()
        );
    }
    
    /**
     * Encapsulates the given 0-origin numeric coordinates,
     * while converting them to 1-origin alphanumeric coordinates.
     * The operation can silently fail;
     * the client should explicitly consult the status 
     * after the operation completes.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     */
    public Label( int xco, int yco )
    {
        try
        {
            this.xco = xco;
            this.colStr = convertToColStr( xco );
            this.yco = yco;
            this.rowStr = convertToRowStr( yco );
            status = true;
            message = StatusMessages.SUCCESS;
            label = rowStr + colStr;
        }
        catch ( IllegalArgumentException exc )
        {
            status = false;
            message = exc.getMessage();
        }
    }
    
    /**
     * Validate a 1-origin alphanumeric label such as "B10."
     * Separated it into its row and column parts,
     * and calculate the corresponding 0-origin
     * x- and y-coordinates.
     * Additional details about the format of the input string
     * can be found in the {@linkplain Label} class documentation.
     * 
     * @param toParse   the label to parse
     * 
     * @throws NullPointerException if toParse is null
     * 
     * @see Label
     */
    public Label( String toParse )
    {
        Objects.requireNonNull( toParse, "toParse" );
        this.label = toParse;
        Matcher matcher = regexPat.matcher( toParse );
        if ( matcher.find() )
        {
            // Note: toUpperCase gives different results in some locales;
            // force it to use the root locale instead of the default.
            rowStr = matcher.group( 1 ).toUpperCase( Locale.ROOT );
            colStr = matcher.group( 2 );
            if ( parseRow() && parseCol() )
            {
                label = rowStr + colStr;
                status = true;
                message = StatusMessages.SUCCESS;
            }
        }
    }

    /**
     * Gets the 1-origin alpha-numeric label.
     * @return the the 1-origin alpha-numeric label
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * Gets the 1-origin alpha-numeric column ID.
     * @return the the 1-origin alpha-numeric column ID
     */
    public String getColStr()
    {
        return colStr;
    }

    /**
     * Gets the 0-origin x-coordinate (the numeric column ID).
     * @return the 0-origin x-coordinate
     */
    public int getXco()
    {
        return xco;
    }

    /**
     * Gets the 1-origin alpha-numeric row ID.
     * @return the 1-origin alpha-numeric row ID
     */
    public String getRowStr()
    {
        return rowStr;
    }

    /**
     * Gets the 0-origin y-coordinate (the numeric row ID).
     * @return the 0-origin y-coordinate
     */
    public int getYco()
    {
        return yco;
    }

    /**
     * Gets the 0-origin x- and y-coordinates encapsulated
     * in a GridCoords object.
     * 
     * @return the 0-origin y-coordinate
     */
    public GridCoords getGridCoords()
    {
        GridCoords  coords  = new GridCoords( xco, yco );
        return coords;
    }

    /**
     * Returns the message associated with this operation.
     * 
     * @return the message associated with this operation
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Returns the status of this operation.
     * 
     * @return the status of this operation
     */
    public boolean isStatus()
    {
        return status;
    }
    
    @Override
    public String toString()
    {
        String  str = 
            String.format( "%s: (%d,%d)", label, xco, yco );
        return str;
    }
    
    /**
     * Convert the 1-origin rowStr field into a 0-origin y-coordinate.
     * To prevent overflow,
     * the operation explicitly fails
     * if the length of the rowStr field
     * is greater than 5.
     * <p>
     * Precondition: the rowStr field has been properly initialized
     * <p>
     * Postcondition: 
     *      If the operation is successful,
     *      the yco field is initialized to the result
     *      
     * @return  true if the operation completes successfully
     */
    private boolean parseRow()
    {
        boolean result  = true;
        int     len     = rowStr.length();
        if ( len > 5 )
            result = false;
        else
        {
            int     accum   = 0;
            for ( int inx = 0 ; inx < len && result ; ++inx )
            {
                char    end = rowStr.charAt( inx );
                int     num = end - 'A';
                if ( num < 0 || num > 25 )
                    result = false;
                else
                    accum = 26 * accum + num + 1;
            }
            if ( result )
                yco = accum - 1;
        }
        return result;
    }
    
    /**
     * Convert the 1-origin colStr field into a 0-origin x-coordinate.
     * To prevent overflow,
     * the operation explicitly fails
     * if the length of the colStr field exceeds 9.
     * <p>
     * Precondition: the colStr field has been properly initialized
     * <p>
     * Postcondition: 
     *      If the operation is successful,
     *      the xco field is initialized to the result
     *      
     * @return  true if the operation completes successfully
     */
    private boolean parseCol()
    {
        boolean result  = true;
        int     num     = -1;
        if ( colStr.length() > 9 )
            result = false;
        else
        {
            try
            {
                num = Integer.parseInt( colStr );
                if ( num == 0 )
                    result = false;
                else
                    xco = num - 1;
            }
            catch ( NumberFormatException exc )
            {
                result = false;
            }
        }
        return result;
    }
    
    /**
     * Converts a 0-origin numeric column ID into a 1-origin 
     * alphanumeric column ID.
     * 
     * @param   xco the numeric column ID to convert
     *      
     * @return  the converted string
     * 
     * @throws IllegalArgumentException if the input is less than zero
     */
    private static String convertToColStr( int xco )
    {
        if ( xco < 0 )
        {
            throw new IllegalArgumentException( StatusMessages.INVALID_COL );
        }
        return String.valueOf( xco + 1 );
    }
    
    /**
     * Converts a 0-origin numeric row ID into a 1-origin 
     * alphanumeric row ID.
     * 
     * @param   yco the numeric row ID to convert
     *      
     * @return  the converted string
     * 
     * @throws IllegalArgumentException if the input is less than zero
     */
    private static String convertToRowStr( int yco )
    {
        if ( yco < 0 )
        {
            throw new IllegalArgumentException( StatusMessages.INVALID_ROW );
        }

        String          alphaID = "";
        StringBuilder   bldr = new StringBuilder();
        
        // Convert the 0-origin index 1-origin
        int num = yco + 1;

        while (num > 0)
        {
            // Divide by 26 and get the remainder
            int remainder = (num - 1) % 26;
            
            // Convert the remainder to the alpha
            char ccc = (char) ('A' + remainder);
            bldr.append(ccc);
            
            // Move to the next digit position
            num = (num - 1) / 26;
        }

        // The characters were calculated from right to left; 
        // reverse the result
        alphaID = bldr.reverse().toString();

        return alphaID;
    }
}
