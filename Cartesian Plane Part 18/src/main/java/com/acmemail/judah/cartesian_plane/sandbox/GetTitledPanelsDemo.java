package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * This application demonstrates how to search a GUI
 * for all Containers (JPanels) with TitledBorders.
 * The application frame's content pane
 * contains a "master panel"
 * which in turn contains a "left panel" and a "right panel".
 * The left panel contains a sequence JPanels,
 * some with no titles, 
 * some with simple titles,
 * and some with a TitledBorder embedded in a CompoundBorder.
 * The right panel consists of
 * a set of deeply nested panels,
 * with the innermost having a border
 * consisting of several nested CompoundBorders
 * with a TitledBorder at the deepest level.
 * 
 * @author Jack Straub
 */
public class GetTitledPanelsDemo
{
    /** Title for the application frame. */
    private static final String     appTitle    =
        "Get All Titled Panels Demo";
    /** 
     * Largest 48 bit integer. Used to generate random colors.
     * @see #getNextColor()
     */
    private static final int        allColors   = (int)Math.pow( 2, 48 );
    /** Size for JPanels that need explicit sizing. */
    private static final Dimension  panelSize   = new Dimension( 100, 100 );
    /** Empty border, declared here for convenience. */
    private static final Border     emptyBorder =
        BorderFactory.createEmptyBorder( 5, 5, 5, 5 );

    /** 
     * List of the titles of all encapsulated TitledBorders. Compiled
     * in {@link #getTitledBorder()}. Used to validate result of the
     * search for TitledBorders.
     * @see #actTitles
     * @see #execDemo()
     * @see #getAllTitledPanels(Container)
     */
    private final Set<String>   expTitles   = new HashSet<>();
    /** 
     * List of all titles found when searching for encapsulated 
     * TitledBorders. Compiled in {@link #getAllTitledPanels(Container)}. 
     * Used to validate result of thesearch  for TitledBorders.
     * @see #expTitles
     * @see #execDemo()
     * @see #getAllTitledPanels(Container)
     */
    private final Set<String>   actTitles   = new HashSet<>();
    /** 
     * Random number generator for generating random colors.
     * @see #getNextColor()
     */
    private final Random        randy       = new Random( 0 );
    /** 
     * Last in a sequence of IDs used to generate unique titles.
     * @see #getTitledBorder()
     */
    private int                 titleNum    = 0;

    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        GetTitledPanelsDemo demo    = new GetTitledPanelsDemo();
        GUIUtils.schedEDTAndWait( demo::buildAndShow );
        demo.execDemo();
    }

    /**
     * Private constructor to prevent external instantiation.
     */
    private GetTitledPanelsDemo()
    {
    }
    
    /**
     * Create and display the application GUI.
     */
    private void buildAndShow()
    {
        JFrame      appFrame        = new JFrame( appTitle );
        appFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane     = new JPanel( new BorderLayout() );
        JPanel      masterPanel     = new JPanel( new GridLayout( 1, 2 ) );
        masterPanel.add( getLeftPanel() );
        masterPanel.add( getRightPanel() );
        
        String  lastText    = "Last Title " + titleNum;
        contentPane.add( new JLabel( lastText ), BorderLayout.SOUTH );
        contentPane.add( masterPanel, BorderLayout.CENTER );
        appFrame.setContentPane( contentPane );
        appFrame.setLocation( 200, 100 );
        appFrame.pack();
        appFrame.setVisible( true );
        getAllTitledPanels( contentPane );
    }
    
    /**
     * Execute the search for titled JPanels.
     * List all titles expected to be found,
     * and all titles actually found.
     * Validate the list of expected titles
     * against the list of found titles.
     */
    private void execDemo()
    {
        actTitles.forEach( System.out::println );
        String  message = "Exp Titles == Act Titles? ";
        System.out.println( message + expTitles.equals( actTitles ) );
        expTitles.forEach( System.out::println );
    }
    
    /**
     * Create a JPanel containing additional JPanels
     * in a BoxLayout with a Y_AXIS orientation.
     * Some of the additional panels have TitledBorders
     * and some do not.
     * <p>
     * Postcondition:
     * </p>
     * <ul>
     * <li>
     *      At least three of the contained panels have TitledBorders.
     * </li>
     * <li>
     *      At least one of the contained panels 
     *      has a TitledBorder on the outside of a CompoundBorder.
     * </li>
     * <li>
     *      At least one of the contained panels 
     *      has a TitledBorder on the inside of a CompoundBorder.
     * </li>
     * </ul>
     * 
     * @return  the created JPanel
     */
    private JPanel getLeftPanel()
    {
        JPanel      leftPanel   = new JPanel();
        BoxLayout   layout      = 
            new BoxLayout( leftPanel, BoxLayout.Y_AXIS );
        leftPanel.setLayout( layout );
        
        IntStream.range( 0, 3 )
            .mapToObj( i -> getPlainTitledPanel() )
            .forEach( leftPanel::add );
        
        leftPanel.add( 
            getCompoundTitledPanel( false )
        );
        
        leftPanel.add( 
            getCompoundTitledPanel( false )
        );
        return leftPanel;
    }
    
    /**
     * Create a set of nested JPanels,
     * Some with TitledBorders and some without.
     * <p>
     * Postcondition:
     * </p>
     * <ul>
     * <li>
     *      At least one of the nested JPanels has a simple TitledBorder.
     * </li>
     * <li>
     *      At least one of the nested JPanels has no TitledBorder.
     * </li>
     * <li>
     *      At least one of the nested panels 
     *      has a TitledBorder on the outside of a CompoundBorder.
     * </li>
     * <li>
     *      At least one of the contained panels 
     *      has a TitledBorder on the inside of a CompoundBorder.
     * </li>
     * <li>
     *      At least one of the contained panels 
     *      has a border consisting of two or more
     *      nested compound borders,
     *      with a TitledBorder contained
     *      in the innermost CompoundBorder.
     * </li>
     * </ul>
     * 
     * @return  the created JPanel
     */
    private JPanel getRightPanel()
    {
        JPanel  rightPanel  = new JPanel( new BorderLayout() );
        rightPanel.setBorder( emptyBorder );
        rightPanel.setBackground( getNextColor() );
        
        JPanel  panel1          = new JPanel( new BorderLayout() );
        Border  border  = getCompoundBorder( true );
        panel1.setBorder( border );
        panel1.setBackground( getNextColor() );
        rightPanel.add( panel1, BorderLayout.CENTER );
        
        JPanel  panel2          = new JPanel( new BorderLayout() );
        panel2.setBorder( emptyBorder );
        panel2.setBackground( getNextColor() );
        panel1.add( panel2, BorderLayout.CENTER );
        
        JPanel  panel3          = new JPanel( new BorderLayout() );
        border  = getCompoundBorder( false );
        panel3.setBorder( border );
        panel3.setBackground( getNextColor() );
        panel2.add( panel3, BorderLayout.CENTER );
        
        JPanel  panel4          = new JPanel( new BorderLayout() );
        panel4.setBorder( emptyBorder );
        panel4.setBackground( getNextColor() );
        panel3.add( panel4, BorderLayout.CENTER );
        
        JPanel  panel5          = new JPanel( new BorderLayout() );
        border = getTitledBorder();
        panel5.setBorder( border );
        panel5.setPreferredSize( panelSize );
        panel5.setBackground( getNextColor() );
        panel4.add( panel5, BorderLayout.CENTER );
        
        JPanel  panel6          = getPanelWithNestedCompoundTitles();
        panel5.add( panel6 );
        
        return rightPanel;
    }
    
    /**
     * Create a JPanel with a fixed size
     * and a simple TitledBorder.
     * 
     * @return the created JPanel
     */
    private JPanel getPlainTitledPanel()
    {
        JPanel  panel   = new JPanel();
        Border  border  = getTitledBorder();
        panel.setBorder( border );
        panel.setPreferredSize( panelSize );
        panel.setBackground( getNextColor() );
        return panel;
    }
    
    /**
     * Create a JPanel with a fixed size
     * and a CompoundBorder containing a TitledBorder.
     * The caller  specifies whether the TitledBorder
     * is the outer border or the inner border.
     * 
     * @param outside
     *      true to put the TitledBorder 
     *      on the outside of the CompoundBorder.
     *      
     * @return the created JPanel
     */
    private JPanel getCompoundTitledPanel( boolean outside )
    {
        Border  border  = getCompoundBorder( outside );
        JPanel  panel   = new JPanel();

        panel.setBorder( border );
        panel.setPreferredSize( panelSize );
        panel.setBackground( getNextColor() );
        return panel;
    }
    
    /**
     * Created a JPanel with a fixed size
     * and a TitledBorder contained in a series
     * of nested CompoundBorders.
     * <p>
     * Postcondition:
     * The border consists of at least three nested CompoundBorders
     * with the TitledBorder in the innermost.
     * 
     * @return the created JPanel
     */
    private JPanel getPanelWithNestedCompoundTitles()
    {
        JPanel      panel       = new JPanel();
        panel.setPreferredSize( panelSize );
        panel.setBackground( getNextColor() );
        
        Border  redBorder       = 
            BorderFactory.createLineBorder( Color.RED, 3 );
        Border  blueBorder      = 
            BorderFactory.createLineBorder( Color.BLUE, 3 );
        Border  greenBorder     = 
            BorderFactory.createLineBorder( Color.GREEN, 3 );
        Border  titledBorder    = getTitledBorder();
        Border  cBorderA        =
            BorderFactory.createCompoundBorder( emptyBorder, titledBorder );
        Border  cBorderB        =
            BorderFactory.createCompoundBorder( cBorderA, redBorder );
        Border  cBorderC        =
            BorderFactory.createCompoundBorder( blueBorder, cBorderB );
        Border  cBorderD        =
            BorderFactory.createCompoundBorder( cBorderC, greenBorder );
        Border  cBorderE        =
            BorderFactory.createCompoundBorder( cBorderD, emptyBorder );
        panel.setBorder( cBorderE );
        return panel;
    }
    
    /**
     * Instantiate a CompoundBorder containing a TitledBorder.
     * The caller  specifies whether the TitledBorder
     * is the outer border or the inner border.
     * 
     * @param outside
     *      true to put the TitledBorder 
     *      on the outside of the CompoundBorder.
     *      
     * @return the created CompoundBorder
     */
    private Border getCompoundBorder( boolean outside )
    {
        Border  titledBorder    = getTitledBorder();
        Border  border          = outside ?
            BorderFactory.createCompoundBorder( titledBorder, emptyBorder ) :
            BorderFactory.createCompoundBorder( emptyBorder, titledBorder );
        return border;
    }
    
    /**
     * Create a TitledBorder
     * with a title that ends in a unique, sequential ID.
     * @see #titleNum
     * 
     * @return the created TitledBorder
     */
    private Border getTitledBorder()
    {
        String  title   = "Title " + ++titleNum;
        Border  border  = BorderFactory.createTitledBorder( title );
        expTitles.add( title );
        return border;
    }
    
    /**
     * Instantiate a color from a randomly generated integer.
     * @see #randy
     * 
     * @return  the instantiated color
     */
    private Color getNextColor()
    {
        int     intColor    = randy.nextInt( allColors );
        Color   color       = new Color( intColor );
        return color;
    }
    
    /**
     * Recursively search a set of nested Containers
     * for JPanels with a TitledBorder.
     * <p>
     * Postcondition:
     * the titles of all located JPanels
     * will be contained in the Actual Titles List
     * ({@link #actTitles}.
     * 
     * @param source    
     *      the container from which to conduct
     *      the recursive search
     */
    private void getAllTitledPanels( Container source )
    {
        if ( source instanceof JPanel )
        {
            JPanel  panel   = (JPanel)source;
            String  title   = getBorderTitle( panel.getBorder() );
            if ( title != null )
                actTitles.add( title );
        }
        Arrays.stream( source.getComponents() )
            .filter( c -> c instanceof Container )
            .map( c -> (Container)c )
            .forEach( c -> getAllTitledPanels( c ) );
    }
    
    /**
     * Attempt to a title from a given border.
     * If the given border is a CompoundBorder
     * the border is searched recursively
     * for a TitledBorder.
     * If no TitledBorder is found
     * null is returned.
     * 
     * @param border    the given border
     * 
     * @return
     *      the border title, or null if no title is found
     */
    private String getBorderTitle( Border border )
    {
        String  title   = null;
        if ( border instanceof TitledBorder )
        {
            title = ((TitledBorder)border).getTitle();
        }
        else if ( border instanceof CompoundBorder )
        {
            CompoundBorder  compBorder  = (CompoundBorder)border;
            Border          inside      = compBorder.getInsideBorder();
            Border          outside     = compBorder.getOutsideBorder();
            if ( (title = getBorderTitle( inside )) == null )
                title = getBorderTitle( outside );
        }
        return title;
    }
}
