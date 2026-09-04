package com.acmemail.judah.battleship.model;

import static com.acmemail.judah.battleship.Constants.DEF_NUM_COLS;
import static com.acmemail.judah.battleship.Constants.DEF_NUM_ROWS;
import static com.acmemail.judah.battleship.Constants.KEY_NUM_COLS;
import static com.acmemail.judah.battleship.Constants.KEY_NUM_ROWS;
import static com.acmemail.judah.battleship.StatusMessages.GRID_EXISTS;
import static com.acmemail.judah.battleship.StatusMessages.INTERSECTS_SHIP;
import static com.acmemail.judah.battleship.StatusMessages.OUT_OF_BOUNDS;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Constants;

/**
 * Maintains a map of grid coordinates to cells in a rectangle.
 * Each cell indicates which ship occupies it, if any,
 * and whether or not the cell has been attacked.
 * If a cell does not exist for a location, 
 * then that location does not contain a ship,
 * and has not been attacked.
 * A client can perform the following common operations:
 * <ol>
 * <li>Ask for the ship at some given coordinates.</li>
 * <li>Ask if a coordinate pair has been attacked.</li>
 * <li>Ask if a coordinate pair is valid.</li>
 * <li>Ask if any part of a ship is outside the grid.</li>
 * <li>Add a ship to the grid.</li>
 * <li>Remove a ship from the grid.</li>
 * <li>Mark a cell as attacked.</li>
 * <li>Ask if a cell is known to belong to an opponent.</li>
 * <li>Mark a cell as belonging to an opponent.</li>
 * </ol>
 * <p>
 * By design,
 * the client typically has no need to get a cell from the grid,
 * or add one to it.
 * <p>
 * Several operations are gated by the game's configuration state,
 * as tracked by {@link Configurator}.
 * Ships may only be added ({@link #put(Ship2D)})
 * or removed ({@link #remove(Ship2D)})
 * while configuration is in progress;
 * once configuration is complete,
 * both operations throw {@link BattleshipException}.
 * Conversely, a cell may only be marked as attacked
 * ({@link #attack(GridCoords)})
 * once configuration is complete;
 * before that, the operation throws {@link BattleshipException}.
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
    private static final String CONFIG_OVER             =
        Messages.getString("Grid2D.1"); 
    private static final String CONFIG_NOT_COMPLETE             =
        Messages.getString("Grid2D.2"); 
    private static final String DEF_GRID_NAME           = "HOME"; 
    private static final Map<String,Grid2D> allGrids    = new HashMap<>();

    /** 
     * This field is effectively final, but, for testing purposes,
     * it's not declared final. There should be no way for a non-test
     * client to set this variable directly.
     */
    private static int          NUM_ROWS;
    private static int          NUM_COLS;
    
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
            throw new BattleshipException( GRID_EXISTS + ": " + name ); 
        allGrids.put( name, this );
        bounds = new Rectangle( 0, 0, NUM_COLS, NUM_ROWS );
    }
    
    /**
     * Return a list of all grids that have been instantiated.
     * 
     * @return  a list of all grids that have been instantiated
     */
    public static List<Grid2D> getAllGrids()
    {
        List<Grid2D>    list    = new ArrayList<>( allGrids.values() );
        return list;
    }
    
    /**
     * Returns a list of all ships in the grid.
     * The returned list is a copy, not the live list.
     * 
     * @return list of all ships in the grid
     */
    public List<Ship2D> getAllShips()
    {
        List<Ship2D>    list    = new ArrayList<>( allShips );
        return list;
    }
    
    /**
     * Gets the bounds of this grid.
     * 
     * @return the bounds of this grid
     */
    public Rectangle getBounds()
    {
        Rectangle   copy    = new Rectangle( bounds );
        return copy;
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
     * Gets the ship at the given GridCoords.
     * Returns null if none.
     *
     * @param coords    the given GridCoords
     *
     * @return  the ship at the given coordinates, or null if none
     *
     * @throws NullPointerException if coords is null
     */
    public Ship2D getShip( GridCoords coords )
    {
        Cell2D  cell    = get( coords );
        Ship2D  ship    = cell == null ? null : cell.getShip();
        return ship;
    }
    
    /**
     * Returns true if a given ship has been added to the grid.
     * 
     * @param ship  the given ship
     * 
     * @return  true if ship has been added to the grid
     * 
     * @throws NullPointerException if ship is null
     */
    public boolean isDeployed( Ship2D ship )
    {
        Objects.requireNonNull( ship, "ship" ); 
        boolean isDeployed  = allShips.contains( ship );
        return isDeployed;
    }
    
    /**
     * Marks the given coordinates splatted.
     * This operation is only available after 
     * configuration is complete.
     * 
     * @param coords    the given coordinates
     * 
     * @throws NullPointerException if coords is null
     * @throws BattleshipException if configuration stage is complete
     * @throws BattleshipException if coords is out of bounds
     */
    public void attack( GridCoords coords )
    {
        Objects.requireNonNull( coords, "coords" ); 
        throwIfNotConfigComplete();
        if ( !contains( coords ) )
            throw new BattleshipException( OUT_OF_BOUNDS +  ": " + coords );
        Cell2D  cell    = get( coords );
        if ( cell == null )
            cell = putEmptyCell( coords );
        cell.setSplatted();
    }
    
    /**
     * Mark a cell as belonging to an opponent.
     * 
     * @param coords    the coordinates of the cell to mark
     * 
     * @throws NullPointerException if coords is null
     * @throws BattleshipException if coords is out of bounds
     */
    public void setOpponent( GridCoords coords )
    {
        Objects.requireNonNull( coords, "coords" );
        if ( !contains( coords ) )
            throw new BattleshipException( OUT_OF_BOUNDS +  ": " + coords );
        Cell2D  cell    = get( coords );
        if ( cell == null )
            cell = putEmptyCell( coords );
        cell.setOpponent();
    }
    
    /**
     * Mark the cell at the given coordinates 
     * as belonging to an opponent.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @throws BattleshipException if coordinates are out of bounds
     */
    public void setOpponent( int xco, int yco )
    {
        GridCoords  coords  = new GridCoords( xco, yco );
        setOpponent( coords );
    }
    
    /**
     * Ask if a cell is known to belong to an opponent
     * 
     * @param coords    the coordinates of the cell to interrogate
     * 
     * @throws NullPointerException if coords is null
     * @throws BattleshipException if coords is out of bounds
     */
    public boolean isOpponent( GridCoords coords )
    {
        Objects.requireNonNull( coords, "coords" );
        if ( !contains( coords ) )
            throw new BattleshipException( OUT_OF_BOUNDS +  ": " + coords );
        Cell2D  cell        = get( coords );
        boolean isOpponent  = cell == null ? false : cell.isOpponent();
        return isOpponent;
    }
    
    /**
     * Ask if the cell at the given coordinates
     * is known to belong to an opponent
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @throws BattleshipException if coordinates are out of bounds
     */
    public boolean isOpponent( int xco, int yco )
    {
        GridCoords  coords      = new GridCoords( xco, yco );
        boolean     isOpponent  = isOpponent( coords );
        return isOpponent;
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
        Objects.requireNonNull( coords, "coords" );
        boolean result  = bounds.contains( coords.xco(), coords.yco() );
        return result;
    }

    /**
     * Returns true if the given ship is contained
     * within the bounds of the grid.
     * 
     * @param ship  the given ship
     * @return  
     *      true if the given ship is contained within the bounds of the grid
     *      
     * @throws NullPointerException if ship is null
     */
    public boolean contains( Ship2D ship )
    {
        Objects.requireNonNull( ship, "ship" );
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
     *      
     * @throws NullPointerException if ship is null
     */
    public boolean intersectsExisting( Ship2D ship )
    {
        Objects.requireNonNull( ship, "ship" ); 
        boolean result  = 
            allShips.stream()
                .anyMatch( ship::intersects );
        return result;
    }
    
    /**
     * Adds the given ship to the map.
     * This operation is not available
     * after configuration is complete.
     * 
     * @param ship  the given ship
     * 
     * @throws NullPointerException if ship is null
     * @throws BattleshipException if ship falls outside the grid
     * @throws BattleshipException if ship overlaps an existing ship
     * @throws BattleshipException if configuration stage is complete
     */
    public void put( Ship2D ship )
    {
        Objects.requireNonNull( ship, "ship" ); 
        throwIfPastConfig();
        if ( !contains( ship ) )
            throw new BattleshipException( OUT_OF_BOUNDS + ": " + ship ); 
        if ( intersectsExisting( ship ) )
            throw new BattleshipException( INTERSECTS_SHIP + ": " + ship ); 
        
        allShips.add( ship );
        getInteriorPoints( ship )
            .map( c -> new Cell2D( c, ship ) )
            .forEach( c -> put( c ) );
    }
    
    /**
     * Removes the given ship from the grid.
     * Has no effect if the given ship is not in the grid.
     * This operation is not available
     * after configuration is complete.
     * 
     * @param ship  the ship to remove
     * 
     * @throws NullPointerException if ship is null
     * @throws BattleshipException if configuration stage is complete
     */
    public void remove( Ship2D ship )
    {
        Objects.requireNonNull( ship, "ship" ); 
        throwIfPastConfig();
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
     * 
     * @throws NullPointerException if coords is null
     */
    public boolean isSplatted( GridCoords coords )
    {
        Objects.requireNonNull( coords, "coords" ); 
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
            .allMatch( c -> c.isSplatted() );
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
    
    /**
     * Gets the home grid,
     * instantiating it if necessary.
     * 
     * @return  the home grid
     */
    public static Grid2D getHomeGrid()
    {
        Grid2D    home    = allGrids.get( DEF_GRID_NAME );
        if ( home == null )
            home = new Grid2D();
        return home;
    }
    
    /**
     * Returns a stream of all cells registered in the grid.
     * This includes all cells allocated to ships,
     * and all cells that have been attacked.
     * Unallocated/unattacked cells will not be present.
     * 
     * @return  a stream of all cells registered in the grid
     */
    public Stream<Cell2DView> getCells()
    {
        List<Cell2DView>    list    = new ArrayList<>( gridMap.values() );
        return list.stream();
    }
    
    /**
     * Gets the grid with the given name.
     * 
     * @param name  the given name, may not be null
     * 
     * @return the grid with the given name
     * 
     * @throws NullPointerException if name is null
     */
    public static Grid2D getGrid( String name )
    {
        Objects.requireNonNull( name, "name" ); 
        Grid2D    grid    = allGrids.get( name );
        return grid;
    }
    
    /**
     * Reevaluate the num-rows, num-cols initialization.
     * Package-private: this is a testing aid only.
     * The caller passes strings for setting 
     * the num-rows and num-cols properties.
     * If null values are passed,
     * the properties are removed,
     * otherwise the properties are set without validation,
     * then the num-rows, num-cols properties are reinitialized.
     * The original values of the properties
     * are restored immediately after reinitialization.
     * 
     * @param strRows   exp number of rows; may be null
     * @param strCols   exp number of columns; may be null
     */
    static void reinitDimensions( String strRows, String strCols )
    {
        String  rowsProp    = Constants.NAME_PREFIX + KEY_NUM_ROWS;
        String  colsProp    = Constants.NAME_PREFIX + KEY_NUM_COLS;
        String  saveRows    = System.getProperty( rowsProp );
        String  saveCols    = System.getProperty( colsProp );
        
        if ( strRows == null )
            System.clearProperty( rowsProp );
        else
            System.setProperty( rowsProp, strRows );
        if ( strCols == null )
            System.clearProperty( colsProp );
        else
            System.setProperty( colsProp, strCols );
        NUM_ROWS = parseIntProperty( KEY_NUM_ROWS, DEF_NUM_ROWS );
        NUM_COLS = parseIntProperty( KEY_NUM_COLS, DEF_NUM_COLS );
        
        if ( saveRows == null )
            System.clearProperty( rowsProp );
        else
            System.setProperty( rowsProp, saveRows );
        if ( saveCols == null )
            System.clearProperty( colsProp );
        else
            System.setProperty( colsProp, saveCols );
    }

    /**
     * Clears the encapsulated grid map
     * and the list of registered ships.
     * Package-private: this is a testing aid only;
     * see {@code Grid2DTestSupport} for access
     * from outside this package.
     */
    void clear()
    {
        allShips.clear();
        gridMap.clear();
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
            throw new BattleshipException( OUT_OF_BOUNDS + ": " + coords ); 
        cell    = gridMap.get( coords );

        return cell;
    }
    
    /**
     * Puts the given cell into the cell map
     * 
     * @param cell  the given cell
     * 
     * @return  the previous cell at the given coordinates
     */
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
    
    /**
     * Creates a new cell not associated with a ship,
     * and adds it to the map at the given coordinates.
     * 
     * @param coords    the given coordinates
     * 
     * @return  the new, empty cell
     */
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
     * Throws a BattleshipException if the configuration phase
     * is over.
     * 
     * @throws BattleshipException  if the configuration phase is over
     */
    private void throwIfPastConfig()
    {
        if ( Configurator.getState() > Constants.CONFIG )
            throw new BattleshipException( CONFIG_OVER );
            
    }
    
    /**
     * Throws a BattleshipException if the configuration phase
     * is over.
     * 
     * @throws BattleshipException  if the configuration phase is not over
     */
    private void throwIfNotConfigComplete()
    {
        if ( Configurator.getState() < Constants.CONFIG_COMPLETE )
            throw new BattleshipException( CONFIG_NOT_COMPLETE );
            
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
    
    /**
     * Empties all Grid2D maps;
     * empties the collection of Grid2D objects.
     * Package-private: this is a testing aid only;
     * see {@code Grid2DTestSupport} for access
     * from outside this package.
     */
    static void reset()
    {
        for ( Grid2D grid : allGrids.values() )
            grid.clear();
        allGrids.clear();
    }
}
