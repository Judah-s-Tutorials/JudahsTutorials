package com.acmemail.judah.battleship.model;

/**
 * Encapsulation of the x- and y- coordinates of a cell in the grid.
 * X-coordinates begin at 0 in the upper-left corner of the grid,
 * and increase to the right,
 * and y-coordinates begin at 0 in the upper-left corner of the grid
 * and increase downwards.
 * In the expression <em>(a,b),</em>
 * <em>a</em> is the x-coordinate and <em>b</em> is the y-coordinated.
 */
public record GridCoords(int xco, int yco)
{
}
