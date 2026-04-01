package com.acmemail.judah.battleship;

import static com.acmemail.judah.battleship.Constants.KEY_NUM_COLS;
import static com.acmemail.judah.battleship.Constants.KEY_NUM_ROWS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid extends HashMap<GridCoords,Cell>
{
    private static final long serialVersionUID = 1368725798587899020L;
    
    private static final String DEF_GRID_NAME       = "HOME";
    private static final int    DEF_NUM_ROWS        = 10;
    private static final int    DEF_NUM_COLS        = 15;
    private static final int    NUM_ROWS;
    private static final int    NUM_COLS;
    private static final Map<String,Grid>  allGrids   = new HashMap<>();
    
    public final String     name;
    
    static
    {
        NUM_ROWS = parseIntProperty( KEY_NUM_ROWS, DEF_NUM_ROWS );
        NUM_COLS = parseIntProperty( KEY_NUM_COLS, DEF_NUM_COLS );
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
                "A Grid with this name already exists: " + name;
            throw new BattleshipException( msg );
        }
        allGrids.put( name, this );
        // initialize the grid
        clean();
    }
    
    public static Grid getHomeGrid()
    {
        Grid    home    = allGrids.get( DEF_GRID_NAME );
        if ( home == null )
        {
            String  message = "Can't find home grid: " + DEF_GRID_NAME;
            throw new BattleshipException( message );
        }
        return home;
    }
    
    /**
     * Gets the name of this grid.
     * 
     * @return  the name of this grid
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Gets the grid with the given name.
     * 
     * @param name  the given name
     * 
     * @return the grid with the given name
     */
    public static Grid getGrid( String name )
    {
        Grid    grid    = allGrids.get( name );
        return grid;
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
        GridCoords  coords  = new GridCoords( xco, yco );
        boolean splatted    = isSplatted( coords );
        return splatted;
    }
    
    public Ship getShip( int xco, int yco )
    {
        Cell    cell    = get( xco, yco );
        Ship    ship    = cell.getShip();
        return ship;
    }
    
    public void attack( int xco, int yco )
    {
        GridCoords  coords  = new GridCoords( xco, yco );
        attack( coords );
    }
    
    public void attack( GridCoords coords )
    {
        List<String>    errors  = evaluateBounds( coords );
        if ( !errors.isEmpty() )
            throw new BattleshipException( errors.get( 0 ) );
        Cell    cell    = get( coords );
        cell.setSplatted( true );
    }
    
    public void put( Ship ship )
    {
        List<String>    errors  = evaluateBounds( ship );
        if ( !errors.isEmpty() )
            throw new BattleshipException( errors.get( 0 ) );
        int     minXco      = ship.getMinX();
        int     maxXco      = ship.getMaxX();
        int     minYco      = ship.getMinY();
        int     maxYco      = ship.getMaxY();
        for ( int yco = minYco ; yco < maxYco ; ++yco )
            for ( int xco = minXco ; xco < maxXco ; ++xco )
            {
                GridCoords  coords  = new GridCoords( xco, yco );
                Cell        cell    = get( coords );
                if ( cell == null )
                {
                    String  message = 
                        "Unexpected failure to find Cell: " + coords;
                    throw new BattleshipException( message );
                }
                cell.setShip( ship );
                put( coords, cell );
            }
    }
    
    public void remove( Ship ship )
    {
        List<String>    errors  = evaluateBounds( ship );
        if ( !errors.isEmpty() )
            throw new BattleshipException( errors.get( 0 ) );
        int         minXco  = ship.getMinX();
        int         minYco  = ship.getMinY();
        int         maxXco  = ship.getMaxX();
        int         maxYco  = ship.getMaxY();
        for ( int yco = minYco ; yco < maxYco ; ++yco )
            for ( int xco = minXco ; xco < maxXco ; ++xco )
            {
                Cell    cell   = get( xco, yco );
                cell.setShip( null );
            }
    }

    public boolean isSplatted( GridCoords coords )
    {
        Cell    cell    = get( coords );
        boolean splatted    = cell.isSplatted();
        return splatted;
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
        boolean valid   = 
            xco >= 0 && yco >= 0 &&
            xco < NUM_COLS && yco < NUM_ROWS;
        return valid;
    }
    
    /**
     * Initialize the grid with new Cell objects.
     * Any data regarding previously added ships
     * or attacked cells
     * will be lost.
     */
    public void clean()
    {
        for ( int yco = 0 ; yco < NUM_ROWS ; ++ yco )
            for ( int xco = 0 ; xco < NUM_COLS ; ++xco )
            {
                Cell    cell    = new Cell( xco, yco );
                put( cell );
            }
    }
    
    /**
     * Determine if the coordinates of a given Square
     * are valid for this Grid.
     * 
     * @param coords  the given Square
     * 
     * @return  true if the coordinates of the given Square are valid
     *          for this grid
     *          
     * @see #isValidCoord(int, int)
     */
    public static boolean isValidCoord( GridCoords coords )
    {
        boolean valid   = isValidCoord( coords.getXco(), coords.getYco() );
        return valid;
    }
    
    public Cell get( int xco, int yco )
    {
        GridCoords  coords  = new GridCoords( xco, yco );
        Cell        cell    = get( coords );
        return cell;
    }
    
    @Override
    public Cell get( Object obj )
    {
        Cell    cell    = super.get( obj );
        if ( cell == null )
        {
            String  message = "There is no cell at coordinates: " + obj;
            throw new BattleshipException( message );
        }
        return cell;
    }
    
    public void put( Cell cell )
    {
        put( cell.getCoords(), cell );
    }
    
    @Override
    public Cell put( GridCoords coords, Cell cell )
    {
        List<String>    errors  = evaluateBounds( coords );
        if ( !errors.isEmpty() )
            throw new BattleshipException( errors.get( 0 ) );
        Cell    oldVal  = super.put( coords, cell );
        return oldVal;
    }
    
    /**
     * Get the number of rows in this grid.
     * @return the number of rows in this grid
     */
    public static int getNumRows()
    {
        return NUM_ROWS;
    }

    /**
     * Get the number of columns in this grid.
     * @return the number of columns in this grid
     */
    public static int getNumCols()
    {
        return NUM_COLS;
    }
    
    public static List<String> evaluateBounds( GridCoords coords )
    {
        List<String>    list    = new ArrayList<>();
        int             xco     = coords.getXco();
        int             yco     = coords.getYco();
        if ( xco < 0 || xco >= getNumCols() )
        {
            String  message = "X-coordinates out of bounds: " + xco;
            list.add( message );
        }
        if ( yco < 0 || yco >= getNumRows() )
        {
            String  message = "Y-coordinates out of bounds: " + yco;
            list.add( message );
        }
        return list;
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
                "Minimum x coordinate is out of bounds: " + minX;
            remarks.add( remark );
        }
        
        int             maxX    = ship.getMaxX();
        if ( maxX > NUM_COLS )
        {
            String  remark  =
                "Maximum x coordinate is out of bounds: " + maxX;
            remarks.add( remark );
        }
        int             minY    = ship.getMinY();
        if ( minY < 0 )
        {
            String  remark  =
                "Minimum y coordinate is out of bounds: " + minY;
            remarks.add( remark );
        }        
        int             maxY    = ship.getMaxY();
        if ( maxY > NUM_ROWS )
        {
            String  remark  =
                "Minimum y coordinate is out of bounds: " + maxY;
            remarks.add( remark );
        }
        List<Ship>  ships           = Fleet.getShips();
        Ship        existingShip    =
            ships.stream()
                .filter( s -> !s.equals( ship ) )
                .filter( s -> s.intersects( ship ) )
                .findFirst()
                .orElse( null );
        if ( existingShip != null )
        {
            String  remark  =
                "Overlaps existing ship: " + existingShip;
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
        String  propName = Constants.NAME_PREFIX + key;
        String  strVal   = System.getProperty( propName );
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
        int     val;
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
