package com.acmemail.judah.cartesian_plane.components;


import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;

/**
 * This class encapsulates the frame that is required
 * to assemble the GUI for our project.
 * 
 * @author Jack Straub
 */
public class CPFrame extends JFrame
{
    /** Generated serial version UID. */
    private static final long serialVersionUID = -3943393354749039355L;

    /** Title for this frame. */
    private static final String     title       = "Cartesian Plane";
    
    /** Cartesian plane to be displayed at center of content pane. */
    private final CartesianPlane    cartPlane   = new CartesianPlane();
    /** 
     * Table of variable name/value pairs to be displayed 
     * at left of content pane. 
     */
    private final VariablePanel     varPanel    = new VariablePanel();
    /** 
     * Panel containing parameters, e.g. range start/end, to be displayed 
     * at left of content pane below NamePanel. 
     */
    private final ParameterPanel    paramPanel  = new ParameterPanel();
    /**
     * Panel containing equations and plot controls to be displayed
     * at bottom of content pane.
     */
    private final PlotPanel         plotPanel   = new PlotPanel();
    /**
     * Panel containing equation name to be displayed
     * at left of content pane above VariablePanel.
     */
    private final NamePanel         namePanel   = new NamePanel();
    
    /** 
     * Equation to be plotted. Initially set to empty Exp4jEquation,
     * can be loaded at will.
     * @see #loadEquation(Equation)
     */
    private Equation    equation    = null;

    /**
     * Constructor.
     * Fully initializes and makes visible 
     * main application JFrame.
     */
    public CPFrame()
    {
        /* 
         * Invoke the constructor in the superclass that sets
         * the title of the frame.
         */
        super( title );
        
        /*
         * Create the menu bar and the variable table. The frame
         * and variable table must be registered with the menu bar.
         */
        CPMenuBar       menuBar     = new CPMenuBar( this );
        
        /* 
         * This will cause your application to be terminated
         * when the frame is closed. If you forget this step,
         * when you close the frame it will disappear,
         * but your application will continue to run.
         */
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        /* 
         * Create content pane with BorderLayout layout manager.
         */
        JPanel  contentPane = new JPanel( new BorderLayout() );
        
        /* Make the menu bar the north child of the content pane. */
        contentPane.add( menuBar, BorderLayout.NORTH );
        
        /* Make the Canvas the center child of the content pane. */
        contentPane.add( cartPlane, BorderLayout.CENTER );
        
        /* Make the name, variable and parameter panels in the 
         * west child of the content pane. 
         */
        JPanel          leftPanel   = getLeftPanel();
        /* 
         * Put the name panel, variable panel, and parameter panel
         * on the left (west) side of the content pane.
         */
        contentPane.add( leftPanel, BorderLayout.WEST );
        /* Put the plot controls at bottom of content pane. */
        contentPane.add( plotPanel, BorderLayout.SOUTH );
        /* Set the content pane in the frame. */
        setContentPane( contentPane );
        /* Position frame in center of screen. */
        GUIUtils.center( this );
        /* Initiate frame sizing. */
        pack();
        /* Make the frame visible. */
        setVisible( true );
    }

    /**
     * Sets the equation to display in the GUI.
     * 
     * @param equation the equation to set
     */
    public void loadEquation(Equation equation)
    {
        this.equation = equation;
        varPanel.load( equation );
        paramPanel.load( equation );
        plotPanel.load( equation );
        namePanel.load( equation );
    }
    
    /**
     * Returns the currently loaded equation.
     * 
     * @return  the currently loaded equation
     */
    public Equation getEquation()
    {
        return equation;
    }
    
    /**
     * Gets the encapsulated CartesianPlane.
     * 
     * @return  the encapsulated CartesianPlane
     */
    public CartesianPlane getCartesianPlane()
    {
        return cartPlane;
    }
    
    /**
     * Combine the variable table and parameter panel
     * into a single JPanel with a vertical orientation.
     * @return
     */
    private JPanel getLeftPanel()
    {
        JPanel      outer   = new JPanel();
        BoxLayout   layout  = new BoxLayout( outer, BoxLayout.Y_AXIS );
        outer.setLayout( layout );
        
        outer.add( namePanel );
        outer.add( varPanel );
        outer.add( paramPanel );

        // If "panel" alone were made the west child of the
        // content pane's BorderLayout, panel's height would
        // be stretched to fill the height of the content pane
        // and its BoxLayout would distribute the extra space
        // amongst its components achieving a non-aesthestic
        // result. Instead, put "panel" in another JPanel ("outer")
        // with a FlowLayout (the default for JPanels); now the
        // extra space will be allocated to the end of "outer"
        // and "panel" will not be affected.
        JPanel      left    = new JPanel();
        left.add( outer );
        return left;
    }
}
