package com.acmemail.judah.glass_panes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.glass_panes.util.Display;

/**
 * This adaptation of {@link CoordSysDemoA}
 * demonstrate how to convert coordinates between windows,
 * and between a window and the screen.
 * In this adaptation we have made two changes:
 * <ol>
 * <li>
 * In addition to converting coordinates
 * from the main panel coordinate system
 * to each child system,
 * we also convert to the coordinate system
 * used by the screen.
 * The converted coordinates
 * are displayed in a log window.
 * </li>
 * <li>
 * Each child of the main panel
 * is a custom JPanel that can display a dot
 * at the converted coordinates.
 * Each custom panel will attempt to display the dot,
 * but the coordinates will be outside the boundaries 
 * of all but one window, 
 * so you will see only one dot displayed.
 * </li>
 * </ol>
 * 
 * @author Jack
 * 
 * @see CoordSysDemoA
 */
public class CoordSysDemoB
{
    /**  Log message destination. */
    private Display logger;
    
    /**
     * Default constructor; not used.
     */
    public CoordSysDemoB()
    {
    }

    /**
     * Application entry point.
     * 
     * @param args  command-line arguments; not used.
     */
    public static void main(String[] args)
    {
        CoordSysDemoB    demo    = new CoordSysDemoB();
        SwingUtilities.invokeLater( () -> demo.createGUI() );
    }

    /**
     * Create and display the GUI.
     */
    private void createGUI()
    {
        logger = Display.getDisplay();
        JFrame  appFrame    = new JFrame( "Coord Sys Demo" );
        appFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        appFrame.setContentPane( getMainPanel() );
        appFrame.pack();
        appFrame.setVisible( true );
    }
    
    /**
     * Creates the main panel and its four children.
     * 
     * @return  the main panel
     */
    private JPanel getMainPanel()
    {
        Color   lightBlue   = new Color( 0xADD8E6 );
        JPanel  leftPanel   = new DotPanel( "Left", 100, 200, lightBlue );
        JPanel  rightPanel  = new DotPanel( "Right", 100, 200, Color.GREEN );
        JPanel  centerPanel = new DotPanel( "Center", 150, 150, Color.YELLOW );
        JPanel  topPanel    = new DotPanel( "Top", 500, 50, Color.RED );
        JPanel  mainPanel   = new JPanel( new BorderLayout() );
        mainPanel.add( leftPanel, BorderLayout.WEST );
        mainPanel.add( rightPanel, BorderLayout.EAST );
        mainPanel.add( centerPanel, BorderLayout.CENTER );
        mainPanel.add( topPanel, BorderLayout.NORTH );
        
        mainPanel.setName( "Main Panel" );
        mainPanel.addMouseListener( new Mouser() );
        return mainPanel;
    }
    
    
    /**
     * Encapsulation of a MouseListener.
     * Only the mouseClicked method is utilized.
     */
    private class Mouser extends MouseAdapter
    {
        /** The main JPanel; made global for convenience. */
        private JPanel  mainPanel   = null;
        /** 
         * The coordinates where the mouse was clicked 
         * relative to the main panel; made global for convenience. 
         */
        private Point   mainPoint   = null;
        
        /**
         * Default constructor; not used.
         */
        public Mouser()
        {
        }

        /**
         * Processes mouse clicks.
         * Logs the the coordinates of each click
         * in the coordinates systems of the main panel,
         * each of its children,
         * and the screen.
         */
        @Override
        public void mouseClicked( MouseEvent evt )
        {
            Object  obj = evt.getSource();
            if ( obj instanceof JPanel )
            {
                mainPanel = (JPanel)obj;
                mainPoint = evt.getPoint();
                Component[] children    = mainPanel.getComponents();
                printCoords( mainPanel );
                for ( Component child : children )
                    printCoords( child );
                printScreenCoords();
                logger.println( "********************" );
            }
        }
        
        /**
         * Converts the coordinates of the mouse click
         * relative to the main panel
         * into coordinates relative to the given component.
         * Logs the converted coordinates
         * as well as the dimensions of the target component.
         * If the given component is type DotPoint,
         * a dot-point is set in the component
         * at the converted coordinates,
         * and the component is repainted.
         * 
         * @param comp  the given component
         */
        private void printCoords( Component comp )
        {
            Point   compPoint   = SwingUtilities.convertPoint(
                mainPanel,
                mainPoint,
                comp
            );
            int     compXco     = compPoint.x;
            int     compYco     = compPoint.y;
            int     compWidth   = comp.getWidth();
            int     compHeight  = comp.getHeight();
            String  compName    = comp.getName();
            String  format      = "%s: (%d, %d) %d x %d";
            String  descrip     = 
                String.format( 
                    format, 
                    compName, 
                    compXco, 
                    compYco, 
                    compWidth,
                    compHeight
                );
            logger.println( descrip );
            if ( comp instanceof DotPanel )
            {
                DotPanel    dotPanel    = (DotPanel)comp;
                dotPanel.setDotPoint( compPoint );
                dotPanel.repaint();
            }
        }
        
        /**
         * Converts the coordinates of the mouse click
         * relative to the main panel
         * into coordinates relative to the screen.
         * Logs the converted coordinates.
         */
        private void printScreenCoords()
        {
            // convertPointToScreen converts to screen coordinates
            // in-place, so if we don't want to corrupt the original
            // point, we have to make a copy of it, first.
            Point   destPoint   = new Point( mainPoint.x, mainPoint.y );
            SwingUtilities.convertPointToScreen( destPoint, mainPanel );
            int     destXco     = destPoint.x;
            int     destYco     = destPoint.y;
            String  format      = "Screen coordinates: (%d, %d)";
            String  descrip     = String.format( format, destXco, destYco );
            logger.println( descrip );
        }
    }
    
    /**
     * This is a JPanel with the ability to draw a dot
     * at a given point in its interior.
     * To see the dot, 
     * the client sets the coordinates of the dot
     * via {@link #setDotPoint} and calls repaint.
     */
    @SuppressWarnings("serial")
    private static class DotPanel extends JPanel
    {
        /** The color to use for displaying the dot. */
        private static final Color  dotColor    = Color.BLACK;
        /** The side of the rectangle that encapsulates the dot. */
        private static final int    dotSide     = 6;
        /** The point at which to display the dot. */
        private Point               dotPoint    = null;
        
        /**
         * Constructor.
         * Creates a JPanel with a FlowLayout, 
         * and the given name, color, and dimensions.
         *  
         * @param name      the given name
         * @param width     the given width
         * @param height    the given height
         * @param color     the given color
         */
        public DotPanel( String name, int width, int height, Color color )
        {
            Dimension   size    = new Dimension( width, height );
            setBackground( color );
            setPreferredSize( size );
            setName( name );
        }
        
        /**
         * Sets the coordinates of a dot in this DotPanel.
         * 
         * @param dot the dot coordinates
         */
        public void setDotPoint( Point dot )
        {
            dotPoint = dot;
        }
        
        @Override
        public void paintComponent( Graphics gtx )
        {
            super.paintComponent( gtx );
            if ( dotPoint != null )
            {
                int ovalXco = dotPoint.x - dotSide / 2;
                int ovalYco = dotPoint.y - dotSide / 2;
                gtx.setColor( dotColor );
                gtx.fillOval( ovalXco, ovalYco, dotSide, dotSide );
            }
        }
    }
}
