package com.acmemail.judah.battleship.model;

/**
 * This interface defines the implementation
 * of an unmodifiable view
 * of a {@link Cell2D}.
 */
public interface Cell2DView
{
    /**
     * Indicates whether this cell has been attacked.
     * 
     * @return true if this cell has been attacked.
     */
    boolean isSplatted();
    /**
     * Gets the coordinates of this cell.
     * 
     * @return  the coordinates of this cell
     */
    GridCoords getCoords();
    /**
     * Gets the ship that owns this cell, if any.
     * 
     * @return  the ship that owns this cell; null if unknown
     */
    Ship2D getShip();
}
