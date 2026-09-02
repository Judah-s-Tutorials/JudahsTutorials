package com.acmemail.judah.battleship.artwork.awt;

/**
 * Used to listen events occurring in a graphical grid.
 * Events are always associated with cell coordinates
 * ({@code GridCoords}).
 * <p>
 * The cell-event processing facility is currently undergoing development.
 * At this time,
 * the interface contains a single method,
 * but it should not be treated as a functional interface
 * as this may change in the future.
 */
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
