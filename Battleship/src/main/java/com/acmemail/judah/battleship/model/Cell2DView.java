package com.acmemail.judah.battleship.model;

public interface Cell2DView
{
    boolean isSplatted();
    GridCoords getCoords();
    Ship2D getShip();
}
