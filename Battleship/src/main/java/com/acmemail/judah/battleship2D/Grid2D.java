package com.acmemail.judah.battleship2D;

import static com.acmemail.judah.battleship2D.Constants.KEY_NUM_COLS;
import static com.acmemail.judah.battleship2D.Constants.KEY_NUM_ROWS;
import static com.acmemail.judah.battleship2D.Constants.DEF_NUM_COLS;
import static com.acmemail.judah.battleship2D.Constants.DEF_NUM_ROWS;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.acmemail.judah.battleship.BattleshipException;

/**
 * Maintains a map of grid coordinates to cells in a rectangle.
 * Each cell indicates which ship occupies it, if any,
 * and whether or not the cell has been attacked.
 * If a cell does not exist for a location, 
 * then that location does not contain a ship,
 * and has not been attack.
 * A client can perform the following common operations:
 * <ol>
 * <li>Ask for the ship at some given coordinates.</li>
 * <li>Ask if a coordinate pair has been attacked.</li>
 * <li>Ask if a coordinate pair is valid.</li>
 * <li>Ask if any part of a ship is outside the grid.</li>
 * <li>Add a ship to the grid.</li>
 * <li>Remove a ship from the grid.</li>
 * <li>Mark a cell as attacked.</li>
 * </ol>
 * <p>
 * By design,
 * the client typically has no need to get a cell from the grid,
 * or add one to it.
 * <p>
 * A game typically has two or more grids,
 * the home grid
 * and one grid for each opponent;
 * each grid must have a unique name.
 * The client can ask for the home grid
 * (see {@link #getHomeGrid()}
 * or the grid for a given name
 * (see {@link #getGrid(String)}).
 * The size of the grid is controlled
 * by the values of the numRows and numCols system properties.
 */
public class Grid2D
{
    private static final String NO_HOME_GRID        = "Home grid not found.";
    private static final String GRID_EXISTS         = 
        "A grid with this name already exists: ";
    private static final String OVERLAPS_SHIP       = 
        "Overlaps existing ship: ";
    private static final String OUT_OF_BOUNDS       = "Out of bounds: ";
    
    private static final String DEF_GRID_NAME       = "HOME";
    private static final int    NUM_ROWS;
    private static final int    NUM_COLS;
    private static final Map<String,Grid2D> allGrids    = new HashMap<>();
    
    private final HashMap<GridCoords,Cell2D>    gridMap = new HashMap<>();
    private final List<Ship2D>  allShips    = new ArrayList<>();
    private final String        name;
    private final Rectangle     bounds;
    
    static
    {
        NUM_ROWS = parseIntProperty( KEY_NUM_ROWS, DEF_NUM_ROWS );
        NUM_COLS = parseIntProperty( KEY_NUM_COLS, DEF_NUM_COLS );
    }
    
    /**
     * Creates the home grid.
     */
    public Grid2D()
    {
        this( DEF_GRID_NAME );
    }
    
    /**
     * Creates a new Grid with the given name.
     * 
     * @param name  the given name, may not be null
     * 
     * @throws NullPointerException if name is null
     * @throws BattleshipException
     *         if a Grid with the given name already exists
     */
    public Grid2D( String name )
    {
        Objects.requireNonNull( name, "name" );
        this.name = name;
        if ( allGrids.containsKey( name ) )
            throw new BattleshipException( GRID_EXISTS + name );
        allGrids.put( name, this );
        bounds = new Rectangle( 0, 0, NUM_COLS, NUM_ROWS );
    }
    
    /**
     * Gets the bounds of this grid.
     * 
     * @return the bounds of this grid
     */
    public Rectangle getBounds()
    {
        return bounds;
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
     * Returns true if the the square if the given coordinates
     * identify a square that has been splatted.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @return  true if the square with the given coordinates
     *          has been splatted
     *          
     * @throws BattleshipException if coordinates are out of bounds
     */
    public boolean isSplatted( int xco, int yco )
    {
        if ( !contains( xco, yco ) )
        {
            String  message = OUT_OF_BOUNDS + "(" + xco + "," + yco + ")";
            throw new BattleshipException( message );
        }

        Cell2D  cell        = get( xco, yco );
        boolean splatted    = cell == null ? false : cell.isSplatted();
        return splatted;
    }
    
    /**
     * Gets the ship at the given coordinates.
     * Returns null if none.
     * 
     * @param xco   the x-coordinate
     * @param yco   the y-coordinate
     * 
     * @return  the ship at the given coordinates, or null if none
     */
    public Ship2D getShip( int xco, int yco )
    {
        Cell2D  cell    = get( xco, yco );
        Ship2D  ship    = cell == null ? null : cell.getShip();
        return ship;
    }
    
    /**
     * Marks the given coordinates splatted.
     * 
     * @param xco    the given x-coordinate
     * @param yco    the given y-coordinate
     * 
     * @throws BattleshipException if coords fall outside the grid
     */
    public void attack( int xco, int yco )
    {
        if ( !contains( xco, yco ) )
        {
            String  message = OUT_OF_BOUNDS + "(" + xco + "," + yco + ")";
            throw new BattleshipException( message );
        }
        GridCoords  coords  = new GridCoords( xco, yco );
        attack( coords );
    }
    
    /**
     * Marks the given coordinates splatted.
     * 
     * @param coords    the given coordinates
     * 
     * @throws NullPointerException if coords is null
     */
    public void attack( GridCoords coords )
    {
        Objects.requireNonNull( coords, " coords" );
        if ( !contains( coords ) )
            throw new BattleshipException( OUT_OF_BOUNDS + coords );
        Cell2D  cell    = get( coords );
        if ( cell == null )
            cell = putEmptyCell( coords );
        cell.setSplatted();
    }
    
    /**
     * Returns true if the given coordinates
     * fall within the bounds of the grid.
     * 
     * @param coords    the given coordinates
     * 
     * @return  true if the given coordinates fall within the bounds of the grid
     * 
     * @throws NullPointerException if coords is null
     */
    public boolean contains( GridCoords coords )
    {
        Objects.requireNonNull( coords, " coords" );
        boolean result  = bounds.contains( coords.xco(), coords.yco() );
        return result;
    }
    
    /**
     * Returns true if the given coordinates
     * fall within the bounds of the grid.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * @return  true if the given coordinates fall within the bounds of the grid
     */
    public boolean contains( int xco, int yco )
    {
        boolean result  = bounds.contains( xco, yco );
        return result;
    }
    
    /**
     * Returns true if the given ship is contained
     * within the bounds of the grid.
     * 
     * @param ship  the given ship
     * @return  
     *      true if the given ship is contained within the bounds of the grid
     */
    public boolean contains( Ship2D ship )
    {
        boolean result  = bounds.contains( ship.getBounds() );
        return result;
    }
    
    /**
     * Returns true if the given ship overlaps
     * an already mapped ship.
     * 
     * @param ship  the given ship
     * 
     * @return  true if the given ship overlaps an already mapped ship
     */
    public boolean intersectsExisting( Ship2D ship )
    {
        boolean result  = 
            allShips.stream()
            .filter( ship::intersects )
            .findFirst().orElse( null ) != null;
        return result;
    }
    
    /**
     * Adds the given ship to the map.
     * 
     * @param ship  the given ship
     * 
     * @throws NullPointerException if ship is null
     * @throws BattleshipException if ship falls outside the grid
     * @throws BattleshipException if ship overlaps an existing ship
     */
    public void put( Ship2D ship )
    {
        Objects.requireNonNull( ship, "ship" );
        if ( !contains( ship ) )
            throw new BattleshipException( OUT_OF_BOUNDS + ship );
        if ( intersectsExisting( ship ) )
            throw new BattleshipException( OVERLAPS_SHIP + ship );
        
        getInteriorPoints( ship )
            .map( c -> new Cell2D( c, ship ) )
            .forEach( c -> put( c ) );
    }
    
    /**
     * Removes the given ship from the grid.
     * Has no effect if the given ship is not in the grid.
     * 
     * @param ship
     * 
     * @throws NullPointerException if ship is null
     */
    public void remove( Ship2D ship )
    {
        Objects.requireNonNull( ship, "ship" );
        if ( allShips.remove( ship ) )
            getInteriorPoints( ship ).forEach( gridMap::remove );
    }

    /**
     * Returns true if the cell at the given coordinates
     * has been splatted.
     * 
     * @param coords    the given coordinates
     * 
     * @return  true if the cell at the given coordinates has been splatted
     */
    public boolean isSplatted( GridCoords coords )
    {
        Cell2D  cell        = get( coords );
        boolean splatted    = cell == null ? false : cell.isSplatted();
        return splatted;
    }
    
    /**
     * Returns true if every cell in the given ship
     * has been splatted.
     * 
     * @param ship  the given ship
     * 
     * @return  true if every cell in the given ship has been splatted
     * 
     * @throws NullPointerException if ship is null
     */
    public boolean isSunk( Ship2D ship )
    {
        Objects.requireNonNull( ship, "ship" );
        boolean result  = getInteriorPoints( ship )
            .map( this::get )
            .filter( c -> c != null )
            .filter( c -> !c.isSplatted() )
            .findFirst().orElse( null ) == null;
        return result;
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
    
    public static Grid2D getHomeGrid()
    {
        Grid2D    home    = allGrids.get( DEF_GRID_NAME );
        if ( home == null )
            throw new BattleshipException( NO_HOME_GRID );
        return home;
    }
    
    /**
     * Gets the grid with the given name.
     * 
     * @param name  the given name, may not be null
     * 
     * @return the grid with the given name
     */
    public static Grid2D getGrid( String name )
    {
        Objects.requireNonNull( name, "name" );
        Grid2D    grid    = allGrids.get( name );
        return grid;
    }
    
    /**
     * Empties all Grid2D maps;
     * empties the collection of Grid2D objects.
     */
    public static void reset()
    {
        for ( Grid2D grid : allGrids.values() )
            grid.clear();
        allGrids.clear();
    }
    
    /**
     * Clears the encapsulated grid map
     * and the list of registered ships.
     */
    public void clear()
    {
        allShips.clear();
        gridMap.clear();
    }

    private Cell2D get( int xco, int yco )
    {
        GridCoords  coords  = new GridCoords( xco, yco );
        Cell2D      cell    = get( coords );
        return cell;
    }
    
    /**
     * Get the cell at the given coordinates.
     * 
     * @param coords    the given coordinates
     * 
     * @return  the cell at the given coordinates
     * 
     * @throws NullPointerException if coords is null
     */
    private Cell2D get( GridCoords coords )
    {
        Objects.requireNonNull( coords, "coords" );
        Cell2D  cell    = null;
        if ( !contains( coords ) )
            throw new BattleshipException( OUT_OF_BOUNDS + coords );
        cell    = gridMap.get( coords );

        return cell;
    }
    
    private Cell2D put( Cell2D cell )
    {
        Cell2D  oldVal  = gridMap.put( cell.getCoords(), cell );
        return oldVal;
    }
    
    /**
     * Returns a stream of all points interior to a given ship.
     * 
     * @param ship the given ship
     * 
     * @return  a stream of all points interior to a given ship
     */
    private static Stream<GridCoords> getInteriorPoints( Ship2D ship )
    {
        Rectangle       rect    = ship.getBounds();
        int minXco  = rect.x;
        int minYco  = rect.y;
        int maxXco  = (int)rect.getMaxX();
        int maxYco  = (int)rect.getMaxY();
        Stream<GridCoords>  stream  = 
            IntStream.range( minXco, maxXco )
            .boxed()
            .flatMap( x -> IntStream.range( minYco, maxYco )
                .mapToObj(y -> new GridCoords(x, y)
            )
        );
        return stream;
    }
    
    private Cell2D putEmptyCell( GridCoords coords )
    {
        Cell2D  cell    = new Cell2D( coords );
        put( cell );
        return cell;
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
