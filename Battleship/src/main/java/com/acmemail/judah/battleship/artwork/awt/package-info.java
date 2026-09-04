/** 
 * This package encapsulates
 * a graphical representation the Battleship game's logical grid.
 * <ul>
 * <li>
 *      {@link GridWindow}
 *      is the component most closely tied to the logical grid.
 *      It displays a representation of the grid
 *      illustrating individual cells,
 *      the position of ships
 *      (in the case of the home grid),
 *      which cells have been attacked,
 *      and which attacks successfully targeted ships.
 * </li>
 * <li>
 *      {@link GridFrame}
 *      is the frame for the application GUI.
 *      The client provides the main application window
 *      which become's the frame's content pane.
 *      The frame assumes no interpretation of the client window,
 *      and provides no decorations
 *      aside from those that are 
 *      normally encapsulated in an application frame,
 *      such as the title bar,
 *      close/minimize/maximize buttons,
 *      and resize handles.
 *      Closing the frame automatically terminates the application.
 * </li>
 * <li>
 *      {@link GridWindowParent}
 *      is responsible for enclosing an array of {@code GridWindows},
 *      providing borders, scroll bars, 
 *      and a mechanism for selecting individual {@code GridWindows}
 *      for the purpose of directing keyboard focus.
 * </li>
 * <li>
 *      {@link Splat}
 *      is a simple utility that provides an image
 *      that is used to indicating cells on ships
 *      that have been successfully attacked.
 * </li>
 * <li>
 *      {@link CellListener}, {@link CellEvent}
 *      provide the basis for delivering cell selection events
 *      to clients
 *      (see {@link GridWindow}).
 * </li>
 * </ul>
 */

package com.acmemail.judah.battleship.artwork.awt;
