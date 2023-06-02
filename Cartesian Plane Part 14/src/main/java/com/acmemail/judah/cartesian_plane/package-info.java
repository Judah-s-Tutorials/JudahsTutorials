/**
 * This module contains classes that were developed for
 * part of the Cartesian Plane project.
 * You can expect least some of these
 * to be changed in a future lesson.
 * Some of the incorporated classes come from the 
 * Graphics Bootstrap project.
 * <p>
 * Users of this package
 * will instantiate a CartesianPlane class,
 * which is a subclass of JPanel.
 * This class can then be inserted
 * anywhere in a JFrame hierarchy.
 * The easiest thing to do
 * is to add it to a Root object,
 * then start the Root object:
 * </p>
 * <div class="js-codeblock" style="max-width: 50em;">
public static void main(String[] args)
{
    CartesianPlane   canvas  = new CartesianPlane();
    Root            root    = new Root( canvas );
    root.start();
}
 * </div>
 * <p>
 * The user can change many of the properties
 * of the CartesianPlane object such as:
 * </p>
 * <ul>
 * <li>Background color</li>
 * <li>Number of pixels to allocate per unit</li>
 * <li>Whether or not to display minor and/or major tic marks</li>
 * <li>etc.</li>
 * </ul>
 * <p>
 * For a full list of properties
 * that the user can control 
 * see {@linkplain CartesianPlane}.
 * </p>
 * <p>
 * The user can add points to plot
 * by (TO BE DETERMINED).
 * </p>
 * @see com.acmemail.judah.cartesian_plane.graphics_utils.Root
 * @see CartesianPlane
 */
package com.acmemail.judah.cartesian_plane;
