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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.acmemail.judah.battleship2D.Grid2D;

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
    private final Border   outerBorder      = 
        BorderFactory.createEmptyBorder( 
            outerWidth, 
            outerWidth, 
            outerWidth, 
            outerWidth 
        );
    /** The inner border that encloses a GridWindow. */
    Border  innerBorder =
        BorderFactory.createLineBorder( border2Color, innerWidth );
    /** The compound border that encloses a GridWindow. */
    Border  gridWindowBorder                =
        BorderFactory.createCompoundBorder( innerBorder, outerBorder );
    /** The border that encloses a GridWindow in the selected state. */
    private final Border    selectedBorder  =
        BorderFactory.createLineBorder( Color.YELLOW, innerWidth );

    /** The currently selected GridWindow, null if none. */
    private JPanel  selectedWindow          = null;
    /** 
     * The border to restore to the selected GridWindow 
     * when it is deselected. 
     */
    private Border  selectedWindowBorder    = null;

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

    private JPanel getBorderPanel()
    {
        JPanel  panel   = new JPanel();
        panel.setBorder( gridWindowBorder );
        return panel;
    }

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

    private static JLabel   getTitle( String name )
    {
        String  titleText   =
            "<HTML><BODY style='font-size: 150%;'>"
            + name 
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
