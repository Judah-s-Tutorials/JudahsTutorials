package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.VariablePanel;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

/**
 * Simple program to display an object
 * of the VariablePanel class.
 * Variable values can be changed
 * by editing a variable value.
 * Variables can be added/removed using the buttons
 * at the bottom of the window.
 * 
 * @author Jack Straub
 */
public class ShowVariablePanel
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
        
        VariablePanel   vPanel  = new VariablePanel();
        Border          oBorder = 
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        Border          iBorder = 
            BorderFactory.createRaisedBevelBorder();
        Border          border  =
            BorderFactory.createCompoundBorder( oBorder, iBorder );
        vPanel.setBorder( border );
        contentPane.add( vPanel, BorderLayout.WEST );
        contentPane.add( getControlPanel( vPanel ), BorderLayout.SOUTH );
        
        PropertyManager.INSTANCE.setProperty(
            CPConstants.DM_OPEN_EQUATION_PN, true
        );
        Equation    equation    = new Exp4jEquation();
        vPanel.load( equation );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }

    private static JPanel getControlPanel( VariablePanel vPanel )
    {
        JPanel  panel   = new JPanel();
        JButton prec    = new JButton( "Change Precision" );
        JButton exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        prec.addActionListener( e -> {
            int precision   = vPanel.getDPrecision() + 1;
            precision %= 6;
            vPanel.setDPrecision( precision );
        });
        
        panel.add( prec );
        panel.add( exit );
        return panel;
    }
}
