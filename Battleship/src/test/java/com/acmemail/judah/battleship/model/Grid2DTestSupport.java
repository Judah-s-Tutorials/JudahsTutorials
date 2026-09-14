package com.acmemail.judah.battleship.model;

import static com.acmemail.judah.battleship.Constants.KEY_NUM_COLS;
import static com.acmemail.judah.battleship.Constants.KEY_NUM_ROWS;

import com.acmemail.judah.battleship.Constants;

/**
 * Test-only bridge exposing Grid2D's package-private
 * {@code reset()} and {@code clear()} operations
 * to test code outside the {@code battleship2D} package.
 * This class lives in the test source tree only,
 * so it is never present on a production classpath;
 * test code in this same package
 * can call the package-private operations directly
 * and has no need of this bridge.
 */
public class Grid2DTestSupport
{
    /**
     * Default constructor; not used.
     */
    private Grid2DTestSupport()
    {
        // not used
    }

    /**
     * Empties all Grid2D maps;
     * empties the collection of Grid2D objects.
     *
     * @see Grid2D#reset()
     */
    public static void reset()
    {
        Grid2D.reset();
    }

    /**
     * Clears the encapsulated grid map
     * and the list of registered ships
     * for the given grid.
     *
     * @param grid  the grid to clear
     *
     * @see Grid2D#clear()
     */
    public static void clear( Grid2D grid )
    {
        grid.clear();
    }
    
    /**
     * Invokes the package-private reinitDimensions method
     * in Grid2D.
     * 
     * @param strRows   the number of rows to reinit to
     * @param strCols   the number of columns to reinit to
     */
    public static void reinitDimensions( String strRows, String strCols )
    {
        Grid2D.reinitDimensions( strRows, strCols );
    }
    
    /**
     * Sets the number of rows and columns to the given values,
     * then resets the Grid2D framework.
     * <p>
     * Postcondition:
     * New number of rows and columns established,
     * all instantiated grids invalidated.
     * 
     * @param numRows   the given number of rows
     * @param numCols   the given number of columns
     */
    public static void setGridBounds( int numRows, int numCols )
    {
        String  strRows     = String.valueOf( numRows );
        String  strCols     = String.valueOf( numCols );
        String  rowsProp    = Constants.NAME_PREFIX + KEY_NUM_ROWS;
        String  colsProp    = Constants.NAME_PREFIX + KEY_NUM_COLS;
        System.setProperty( rowsProp, strRows );
        System.setProperty( colsProp, strCols );
        reset();
    }
}
