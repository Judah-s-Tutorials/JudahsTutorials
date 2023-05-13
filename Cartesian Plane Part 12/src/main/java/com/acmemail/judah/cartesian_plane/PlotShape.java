package com.acmemail.judah.cartesian_plane;

import java.awt.Shape;

public interface PlotShape
{
    /**
     * Gets a Shape to be drawn 
     * at an x/y coordinate.
     * The caller provides the coordinates
     * of the pixel that would be
     * in the center of the Shape.
     * <p>
     * Example: suppose the Shape is a circle
     * with a radius of 3,
     * and the representative Shape
     * is an Ellipse2D. 
     * In order to place the center of the circle
     * at x=10, y=20
     * you would determine the upper-left corner
     * of the Ellipse2D's bounding rectangle
     * to be x = 10 - 3 = 7
     * and y = 20 - 3 = 17.
     * </p>
     * 
     * @param xco   x-coordinate of pixel at center of Shape
     * @param yco   y-coordinate of pixel at center of Shape
     * 
     * @return  a Shape to be drawn the given coordinates
     */
    Shape getShape( double xco, double yco );
}
