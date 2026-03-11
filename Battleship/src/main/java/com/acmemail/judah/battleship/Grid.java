package com.acmemail.judah.battleship;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid extends HashMap<Point, Ship>
{
    private static final long serialVersionUID = 1L;
    
    private static final String DEF_GRID_NAME       = "THIS GRID";
    private static final int    DEF_ROW_LEN         = 100;
    private static final int    DEF_COL_LEN         = 100;
    private static final int    ROW_LEN;
    private static final int    COL_LEN;
    private static final Map<String,Grid>  allGrids   = new HashMap<>();
    
    public final String     name;
    
    static
    {
        ROW_LEN = parseIntProperty( "ROW_LENGTH", DEF_ROW_LEN );
        COL_LEN = parseIntProperty( "COL_LENGTH", DEF_COL_LEN );
        new Grid();
    }
    
    private Grid()
    {
        this( DEF_GRID_NAME );
    }
    
    /**
     * Creates a new Grid with the given name.
     * 
     * @param name  the given name
     * 
     * @throws IllegalStateException
     *         if a Grid with the given name already exists
     */
    public Grid( String name )
    {
        this.name = name;
        if ( allGrids.containsKey( name ) )
        {
            String  msg =
                "A Grid with the name \""
                + name
                + "\"  already exists";
            throw new IllegalStateException( msg );
        }
        allGrids.put( name, this );
    }
    
    /**
     * Returns true if the the square if the given coordinates
     * identify a square that has been splatted.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @return  true if the square with the given coordinates
     *          has been splatted
     */
    public boolean isSplatted( int xco, int yco )
    {
//        Square  square  = get( xco, yco );
        return false;
    }
    
    /**
     * Determine if the given coordinates
     * are valid for this Grid.
     * They are valid if 
     * the x-coordinate is less than the grid row length
     * and the y-coordinate is less than the grid column length.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @return  true if the coordinates are valid
     *          for this grid
     */
    public static boolean isValidCoord( int xco, int yco )
    {
        boolean valid   = xco < ROW_LEN && yco < COL_LEN;
        return valid;
    }
    
    /**
     * Determine if the coordinates of a given Square
     * are valid for this Grid.
     * 
     * @param square  the given Square
     * 
     * @return  true if the coordinates of the given Square are valid
     *          for this grid
     *          
     * @see #isValidCoord(int, int)
     */
    public static boolean isValidCoord( Point square )
    {
        boolean valid   = isValidCoord( square.x, square.y );
        return valid;
    }
    
    /**
     * Get the row length of the grid.
     * @return the row length
     */
    public static int getRowLen()
    {
        return ROW_LEN;
    }

    /**
     * Get the column length of the grid;
     * @return the column length
     */
    public static int getColLen()
    {
        return COL_LEN;
    }
    
    /**
     * Evaluate the bounds of a Ship.
     * If out-of-bounds,
     * a list of remarks is returned
     * identifying the out-of-bounds coordinates.
     * If no out-of-bounds,
     * an empty list is returned.
     * 
     * @param ship  the ship to evaluate
     * 
     * @return  
     *      an empty list of remarks if valid,
     *      a list of descriptive remarks if invalid
     */
    public static List<String> evaluateBounds( Ship ship )
    {
        List<String>    remarks = new ArrayList<>();
        
        int minX    = ship.getMinX();
        if ( minX < 0 )
        {
            String  remark  =
                "Minimum x coordinate( "
                + minX
                + ") is out of bounds";
            remarks.add( remark );
        }
        
        int             maxX    = ship.getMaxX();
        if ( maxX >= ROW_LEN )
        {
            String  remark  =
                "Maximum x coordinate( "
                + maxX
                + ") is out of bounds";
            remarks.add( remark );
        }
        int             minY    = ship.getMinY();
        if ( minY < 0 )
        {
            String  remark  =
                "Minimum y coordinate( "
                + minY
                + ") is out of bounds";
            remarks.add( remark );
        }        
        int             maxY    = ship.getMaxY();
        {
            String  remark  =
                "Maximum y coordinate( "
                + maxY
                + ") is out of bounds";
            remarks.add( remark );
        }
        return remarks;
    }

    /**
     * Get the integer value of a system property.
     * If the attempt to read the property fails,
     * because the property is not present
     * or the property cannot be converted to an integer,
     * a default value is returned.
     * 
     * @param key       the property key
     * @param defVal    the value returned 
     *                  if the attempt to read the property fails
     *                  
     * @return the value of the property, or defVal
     *         if fetching the property value fails
     */
    private static int parseIntProperty( String key, int defVal )
    {
        int     val;
        String  strVal   = System.getProperty( "ROW_LENGTH" );
        if ( strVal == null )
            val = defVal;
        else if ( (val = parseInt( strVal )) <= 0)
            val = defVal;
        else
            ;
        return val;
    }
    
    /**
     * Parse a string into a positive integer.
     * If the parse fails, -1 is returned.
     * 
     * @param strVal    the value to parse
     * 
     * @return  the integer value of strVal
     *          or -1 if the parse operation fails
     */
    private static int parseInt( String strVal )
    {
        int val;
        try
        {
            val = Integer.parseInt( strVal );
        }
        catch( NumberFormatException exc )
        {
            val = -1;
        }
        
        return val;
    }
}
