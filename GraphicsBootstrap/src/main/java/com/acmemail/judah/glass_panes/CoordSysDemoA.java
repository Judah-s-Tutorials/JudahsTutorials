package com.acmemail.judah.glass_panes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.glass_panes.util.Display;

/**
 * Application to demonstrate how to convert coordinates between windows.
 * The GUI consists of a main JPanel, which serves as the application frame's
 * content pane and listens for button clicks.
 * The main panel has a BoundaryLayout layout manager,
 * and is overlaid with four different JPanels
 * in the North, East, South, and West positions.
 * Each time the main panel detects a button click,
 * it converts the coordinates of the click in its own coordinate
 * system, to coordinates in each of its JPanel children.
 * The results of the conversion are displayed in a log window.
 * 
 * @author Jack
 * 
 * @see CoordSysDemoB
 */
public class CoordSysDemoA
{
    /**  Log message destination. */
    private Display logger;
    
    /**
     * Default constructor; not used.
     */
    public CoordSysDemoA()
    {
    }

    /**
     * Application entry point.
     * 
     * @param args  command-line arguments; not used.
     */
    public static void main(String[] args)
    {
        CoordSysDemoA    demo    = new CoordSysDemoA();
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
        JPanel  leftPanel   = getPanel( "Left", 100, 200, Color.BLUE );
        JPanel  rightPanel  = getPanel( "Right", 100, 200, Color.GREEN );
        JPanel  centerPanel = getPanel( "Center", 150, 150, Color.YELLOW );
        JPanel  topPanel    = getPanel( "Top", 500, 50, Color.RED );
        JPanel  mainPanel = new JPanel( new BorderLayout() );
        mainPanel.add( leftPanel, BorderLayout.WEST );
        mainPanel.add( rightPanel, BorderLayout.EAST );
        mainPanel.add( centerPanel, BorderLayout.CENTER );
        mainPanel.add( topPanel, BorderLayout.NORTH );
        
        mainPanel.setName( "Main Panel" );
        mainPanel.addMouseListener( new Mouser() );
        return mainPanel;
    }
    
    /**
     * Creates a JPanel with a FlowLayout, 
     * and the given name, color, and dimensions.
     *  
     * @param name      the given name
     * @param width     the given width
     * @param height    the given height
     * @param color     the given color
     * 
     * @return  the created JPanel
     */
    private JPanel 
    getPanel( String name, int width, int height, Color color )
    {
        JPanel      panel   = new JPanel();
        Dimension   size    = new Dimension( width, height );
        panel.setBackground( color );
        panel.setPreferredSize( size );
        panel.setName( name );
        return panel;
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
         * in the coordinates systems of the main panel
         * and each of its children.
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
                logger.println( "********************" );
            }
        }
        
        /**
         * Converts the coordinates of the mouse click
         * relative to the main panel
         * into coordinates relative to the given component.
         * Logs the converted coordinates
         * as well as the dimensions of the target component.
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
        }
    }
}
