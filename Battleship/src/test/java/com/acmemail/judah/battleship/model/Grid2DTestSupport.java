package com.acmemail.judah.battleship.model;

import com.acmemail.judah.battleship.model.Grid2D;

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
}
