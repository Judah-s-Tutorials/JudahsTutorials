package com.acmemail.judah.battleship.artwork.awt;

/**
 * Used to listen events occurring in a graphical grid.
 * Events are always associated with cell coordinates
 * ({@code GridCoords}).
 */
@FunctionalInterface
public interface CellListener
{
    /**
     * A cell in a graphical grid was clicked.
     * 
     * @param event     
     *      describes the event that occurred,
     *      including the coordinates of the clicked cell
     */
    void cellClicked( CellEvent event );
}
