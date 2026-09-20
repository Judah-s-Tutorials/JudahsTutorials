package com.acmemail.judah.battleship.model;

import java.util.Objects;

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
     * Set the KEY_NUM_ROWS and KEY_NUM_COLS properties
     * to the given values,
     * then resets the Grid2D framework.
     * The original property values are returned as an opaque
     * {@link GridDimension} token; the client should not attempt
     * to interpret it, only pass it to {@link #resetGridBounds(GridDimension)}
     * once the caller no longer needs the given dimensions in effect.
     * <p>
     * Postcondition:
     * New number of rows and columns established,
     * all instantiated grids invalidated.
     * 
     * @param numRows   the given number of rows
     * @param numCols   the given number of columns
     */
    public static GridDimension setGridBounds( int numRows, int numCols )
    {
        String          strRows     = String.valueOf( numRows );
        String          strCols     = String.valueOf( numCols );
        String          rowsProp    = 
            Constants.NAME_PREFIX + Constants.KEY_NUM_ROWS;
        String          colsProp    = 
            Constants.NAME_PREFIX + Constants.KEY_NUM_COLS;
        GridDimension   origProps   = 
            new GridDimension(
                System.getProperty( rowsProp ),
                System.getProperty( colsProp )
            );
        System.setProperty( rowsProp, strRows );
        System.setProperty( colsProp, strCols );
        reset();
        return origProps;
    }
    
    /**
     * Restores the KEY_NUM_ROWS and KEY_NUM_COLS properties
     * to the values captured in the given token,
     * then resets the Grid2D framework.
     *
     * @param origProps
     *      the token returned by a prior call
     *      to {@link #setGridBounds(int, int)}
     */
    public static void resetGridBounds( GridDimension origProps )
    {
        Objects.requireNonNull( origProps, "origProps" );
        String  rowsProp    = 
            Constants.NAME_PREFIX + Constants.KEY_NUM_ROWS;
        String  colsProp    = 
            Constants.NAME_PREFIX + Constants.KEY_NUM_COLS;
        
        if ( origProps.rows == null )
            System.clearProperty( rowsProp );
        else
            System.setProperty( rowsProp, origProps.rows );
        
        if ( origProps.cols == null )
            System.clearProperty( colsProp );
        else
            System.setProperty( colsProp, origProps.cols );
        reset();
    }
    
    /**
     * Encapsulation of the row and column dimensions of a grid.
     * Intended for saving and restoring property values.
     * The content of an object of this type
     * is only accessible to the containing class.
     */
    public static class GridDimension
    {
        /** Number of grid rows. */
        private final String    rows;
        /** Number of grid columns. */
        private final String    cols;
        
        /**
         * Constructor.
         * Establishes the row and column dimensions of a grid.
         * 
         * @param rows  number of grid rows
         * @param cols  number of grid columns
         */
        private GridDimension( String rows, String cols )
        {
            this.rows = rows;
            this.cols = cols;
        }
    }
}
