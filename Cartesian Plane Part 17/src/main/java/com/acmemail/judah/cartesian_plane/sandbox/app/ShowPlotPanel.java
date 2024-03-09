package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.PlotPanel;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

/**
 * Simple program to display an object
 * of the PlotPanel class.
 * The panel is set up with an equation
 * in which variables x, y, a, b, and c
 * have been declared
 * (i.e., you can experiment with expressions
 * using these variable names).
 * 
 * @author Jack Straub
 */
public class ShowPlotPanel
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> buildGUI() );
    }
    
    /**
     * Creates and displays the main application frame.
     */
    public static void buildGUI()
    {
        JFrame  frame   = new JFrame( "Show Variable Panel" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        JPanel  placeHolder = new JPanel();
        placeHolder.setPreferredSize( new Dimension( 200, 100 ));
        placeHolder.setBackground( Color.ORANGE );
        contentPane.add( placeHolder, BorderLayout.CENTER );
        
        PlotPanel        pPanel      = new PlotPanel();
        contentPane.add( pPanel, BorderLayout.SOUTH );
        
        JPanel      leftPanel   = new JPanel();
        JPanel      buttonPanel = new JPanel( new GridLayout( 2, 1 ) );
        leftPanel.add( buttonPanel );
        
        JButton newEquation     = new JButton( "New" );
        buttonPanel.add( newEquation );
        newEquation.addActionListener( e-> pPanel.load( newEquation() ) );
        
        JButton nullEquation    = new JButton( "Close" );
        buttonPanel.add( nullEquation );
        nullEquation.addActionListener( e-> pPanel.load( null ) );
        
        contentPane.add( leftPanel, BorderLayout.WEST );
        
        PropertyManager.INSTANCE.setProperty(
            CPConstants.DM_OPEN_EQUATION_PN, true
        );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }

    private static Equation newEquation()
    {
        Equation    equation    = new Exp4jEquation();
        equation.setVar( "x", 0 );
        equation.setVar( "y", 0 );
        equation.setVar( "a", 0 );
        equation.setVar( "b", 0 );
        equation.setVar( "c", 0 );
        return equation;
    }
}
