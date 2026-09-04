package com.acmemail.judah.battleship.artwork.awt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.acmemail.judah.battleship.model.Grid2D;

/**
 * An instance of this class
 * serves as the parent for an array of type {@link GridWindow}.
 * A {@code GridWindow} is created for each named logical grid.
 * Each {@code GridWindow} is placed in a scrolled window.
 * One {@code GridWindow} at a time can be selected.
 * The selected window will be highlighted,
 * and will receive key events.
 */
public class GridWindowParent extends JPanel
{
    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;
    
    /** The width of the outer border that encloses a GridWindow. */
    private final int       outerWidth      = 5;
    /** The width of the inner border that encloses a GridWindow. */
    private final int       innerWidth      = 5;
    /** The color of the inner border that encloses a GridWindow. */
    private final Color     border2Color    = Color.BLUE;
    /** The outer border that encloses a GridWindow. */
    private final Border    outerBorder     = 
        BorderFactory.createEmptyBorder( 
            outerWidth, 
            outerWidth, 
            outerWidth, 
            outerWidth 
        );
    /** The inner border that encloses a GridWindow. */
    private final Border    innerBorder     =
        BorderFactory.createLineBorder( border2Color, innerWidth );
    /** The compound border that encloses a GridWindow. */
    private final Border    gridWindowBorder    =
        BorderFactory.createCompoundBorder( innerBorder, outerBorder );
    /** The border that encloses a GridWindow in the selected state. */
    private final Border    selectedBorder  =
        BorderFactory.createLineBorder( Color.YELLOW, innerWidth );
    
    private final Map<String,GridWindow>    gridMap = new HashMap<>();

    /** The currently selected GridWindow, null if none. */
    private JPanel  selectedWindow          = null;
    /** 
     * The border to restore to the selected GridWindow 
     * when it is deselected. 
     */
    private Border  selectedWindowBorder    = null;

    /**
     * Default constructor.
     * For each named logical grid,
     * including the home grid,
     * a {@code GridWindow} child is added
     * to this window,
     * arranged in a {@code GridLayout}.
     * The home grid is placed in the top left.
     * Instantiation of the {@code GridWindows}
     * is the responsibility of the 
     * {@link #getTitleComponent(Grid2D)} method,
     * which is also responsible for titles,
     * and for configuring decorative borders
     * and the scrolled windows
     * enclosing the {@code GridWindows}.
     */
    public GridWindowParent()
    {
        Grid2D  home    = Grid2D.getHomeGrid();
        setGUILayout();
        setMaxDimensions();
        add( getTitleComponent( home ) );
        for ( Grid2D grid : Grid2D.getAllGrids() )
        {
            if ( grid != home )
                add( getTitleComponent( grid ) );
        }
    }
    
    /**
     * Gets the physical {@code GridWindow}
     * controlled by the logical grid map with the given name.
     * Returns null if no such component is found.
     * 
     * @param name  the given name
     * 
     * @return  the physical {@code GridMap}; null if none
     * 
     * @throws NullPointerException if {@code name} is null
     */
    public GridWindow getGridWindow( String name )
    {
        Objects.requireNonNull( name, "name" );
        GridWindow  window  = gridMap.get( name );
        return window;
    }
    
    /**
     * Based on the current number of logical grids,
     * establish a {@code GridLayout} for this panel.
     */
    private void setGUILayout()
    {
        List<Grid2D>    allGrids    = Grid2D.getAllGrids();
        int             gridCount   = allGrids.size();
        int             guiLayoutNumRows;
        int             guiLayoutNumCols;
        if ( gridCount <= 3 )
        {
            guiLayoutNumRows = 1;
            guiLayoutNumCols = gridCount;
        }
        else if ( gridCount == 4 )
        {
            guiLayoutNumRows = 2;
            guiLayoutNumCols = 2;
        }
        else
        {
            guiLayoutNumRows = (int)Math.ceil( gridCount  / 3.);
            guiLayoutNumCols = 3;
        }

        setLayout( new GridLayout( guiLayoutNumRows, guiLayoutNumCols ) );
    }
    
    /**
     * Based on screen layout,
     * determine the maximum, usable real estate
     * that can be occupied by this panel.
     */
    private void setMaxDimensions()
    {
        GraphicsConfiguration config    = 
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
        Insets      insets = 
            Toolkit.getDefaultToolkit().getScreenInsets( config );
        Rectangle   bounds = config.getBounds();
        
        int maxWidth    = bounds.width - insets.left - insets.right;
        int maxHeight   = bounds.height - insets.top - insets.bottom;
        setMaximumSize( new Dimension( maxWidth, maxHeight ) );
    }

    /**
     * Instantiate a JPanel with the default GridWindow border.
     * 
     * @return  a JPanel with the default GridWindow border
     */
    private JPanel getBorderPanel()
    {
        JPanel  panel   = new JPanel();
        panel.setBorder( gridWindowBorder );
        return panel;
    }

    /**
     * Instantiate a window with a border, title, and {@link GridWindow} child.
     * By design, 
     * the window should not resize
     * when its parent is resized.
     * <p>
     * Postcondition:
     * {@link #gridMap} has been updated 
     * with the physical {@code GridMap} instance.
     * 
     * @param grid  
     *      the logical grid to associate with 
     *      the physical {@link GridWindow}
     *      
     * @return  the instantiated window
     */
    private JComponent getTitleComponent( Grid2D grid)
    {
        // Title and controller should live inside the border
        // The border should be inside a scroll pane
        // The border component should never be resized
        JPanel      borderPanel = getBorderPanel();
        BoxLayout   layout = new BoxLayout( borderPanel, BoxLayout.Y_AXIS );
        borderPanel.setLayout( layout );

        String      name        = grid.getName();
        GridWindow  gridWindow  = new GridWindow( grid ); 
        gridMap.put( name, gridWindow );
        borderPanel.add( getTitle( name ) );
        borderPanel.add( gridWindow );
        
        // The dummy panel is to prevent the border panel from being resized
        JPanel  dummyPanel  = new JPanel();
        dummyPanel.add( borderPanel );
        JScrollPane pane    = new JScrollPane( dummyPanel );
        borderPanel.addMouseListener(
            new MouseAdapter()
            {
                @Override
                public void mouseClicked( MouseEvent evt )
                {
                    if ( evt.getButton() == MouseEvent.BUTTON1 )
                    {
                        selectWindow( borderPanel );
                        gridWindow.requestFocusInWindow();
                    }
                }
            }
        );

        return pane;
    }

    /**
     * Obtain a JLabel containing a title for a component.
     * The text of the label uses HTML to customize
     * the font it's displayed in.
     * 
     * @param text  the text for the title
     * 
     * @return  the instantiated JLabel
     */
    private static JLabel   getTitle( String text )
    {
        String  titleText   =
            "<HTML><BODY style='font-size: 150%;'>"
            + text 
            + "</BODY></HTML>";
        JLabel  title   = new JLabel( titleText );
        return title;
    }

    /**
     * Highlight the given window
     * and restore the border of the previously
     * selected window, if any.
     *
     * @param window    the window to select
     */
    private void selectWindow( JPanel window )
    {
        if ( selectedWindow != null )
            selectedWindow.setBorder( selectedWindowBorder );

        selectedWindowBorder = window.getBorder();
        window.setBorder( selectedBorder );
        selectedWindow = window;
        repaint();
    }
}
