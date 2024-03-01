package com.acmemail.judah.cartesian_plane.components;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PropertyManager;
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
     * at left of content pane. 
     */
    private final ParameterPanel    paramPanel  = new ParameterPanel();
    /**
     * Panel containing equations and plot controls to be displayed
     * at bottom of content pane.
     */
    private final PlotPanel         plotPanel   = new PlotPanel();
    
    /** 
     * Equation to be plotted. Initially set to empty Exp4jEquation,
     * can be loaded at will.
     * @see #loadEquation(Equation)
     */
    private Equation    equation    = null;
    /** The text field containing the name of the equation. */
    private JTextField  nameField   = new JTextField();

    /**
     * Constructor.
     * Fully initializes and makes visible 
     * main application JFrame.
     */
    public CPFrame()
    {
        super( title );
        
        /* Create the menu bar and the variable table. The frame
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
        
        /* Make the variable and parameter panels in the 
         * west child of the content pane. */
        JPanel          leftPanel   = getLeftPanel();
        
        /* Install the default equation in all the panels. */
        loadEquation( equation );
        
        /* Install the CartesianPlane in the plot panel. */
        plotPanel.setCartesianPlane( cartPlane );
        
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
        if ( equation != null )
        {
            nameField.setText( equation.getName() );
        }
        else
            nameField.setText( "" );
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
        JPanel  panel       = new JPanel();
        panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
        
        JPanel  namePanel   = new JPanel( new GridLayout( 2, 1 ) );
        namePanel.add( new JLabel( "Eq. Name" ) );
        namePanel.add( nameField );
        
        panel.add( namePanel );
        panel.add( varPanel );
        panel.add( paramPanel );
        
        // Sets the component name of the name field so
        // the test classes can find it
        nameField.setName( CPConstants.CP_EQUATION_NAME_CN );
        
        // Notify the property manager if the name field changes
        nameField.addKeyListener( new NameListener() );
        
        nameField.addActionListener( this::nameAction );
        return panel;
    }
    
    /**
     * Detects when the operator types enter in the name field,
     * and commits the name.
     * 
     * @param evt   object describing this event; not used
     */
    private void nameAction( ActionEvent evt )
    {
        PropertyManager.INSTANCE.setProperty(
            CPConstants.DM_MODIFIED_PN,
            true
        );
        if ( equation != null )
            equation.setName( nameField.getText() );
    }
    
    /**
     * Monitors key events in the equation name field.
     * When detected, sets the DM_MODIFIED_PN property
     * to true.
     * 
     * @author Jack Straub
     */
    private static class NameListener extends KeyAdapter
    {
        @Override
        public void keyTyped(KeyEvent e)
        {
            PropertyManager.INSTANCE.setProperty(
                CPConstants.DM_MODIFIED_PN,
                true
            );
        }
    }
}
