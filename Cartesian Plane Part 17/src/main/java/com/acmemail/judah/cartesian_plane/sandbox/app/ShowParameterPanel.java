package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.ParameterPanel;
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
 * There options to start a new equation
 * or close the current equation.
 * 
 * @author Jack Straub
 */
public class ShowParameterPanel
{
    /** System-dependent line separator. */
    private static String       endl        = System.lineSeparator();
    /** Current equation */
    private static Equation     equation    = null;
    private static JTextArea    textArea;
    
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
//        JPanel  placeHolder = new JPanel();
//        placeHolder.setPreferredSize( new Dimension( 200, 100 ));
//        placeHolder.setBackground( Color.ORANGE );
//        contentPane.add( placeHolder, BorderLayout.CENTER );
        textArea = new JTextArea();
        JScrollPane scrollPane  = new JScrollPane( textArea );
        scrollPane.setPreferredSize( new Dimension( 200, 100 ));
        textArea.setBackground( Color.ORANGE );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      leftPanel   = new JPanel();
        BoxLayout   boxLayout   = 
            new BoxLayout( leftPanel, BoxLayout.Y_AXIS );
        leftPanel.setLayout( boxLayout );
        Border  border  = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        leftPanel.setBorder( border );
        
        JPanel      buttonPanel = new JPanel( new GridLayout( 3, 1 ) );
        leftPanel.add( buttonPanel );
        leftPanel.add( Box.createRigidArea( new Dimension( 0, 10 ) ) );

        ParameterPanel  pPanel  = new ParameterPanel();
        leftPanel.add( pPanel );
        
        JButton newEquation     = new JButton( "New" );
        buttonPanel.add( newEquation );
        newEquation.addActionListener( e-> pPanel.load( newEquation() ) );
        
        JButton nullEquation    = new JButton( "Close" );
        buttonPanel.add( nullEquation );
        nullEquation.addActionListener( e-> pPanel.load( null ) );
        
        JButton print           = new JButton( "Print" );
        buttonPanel.add( print );
        print.addActionListener( e -> printAction() );

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
        equation = new Exp4jEquation();
        equation.setVar( "x", 0 );
        equation.setVar( "y", 0 );
        equation.setVar( "a", 0 );
        equation.setVar( "b", 0 );
        equation.setVar( "c", 0 );
        
        equation.setRangeStart( "-2"  );
        equation.setRangeEnd( "2" );
        equation.setRangeStep( ".01" );
        equation.setPrecision( 3 );
        equation.setRadiusName( "r" );
        equation.setThetaName( "t" );
        equation.setParam( "t" );
        return equation;
    }
    
    private static void printAction()
    {
        if ( equation != null )
        {
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( "Start: " ).append( equation.getRangeStartExpr() )
                .append( endl )
                .append( "End: " ).append( equation.getRangeEndExpr() )
                .append( endl )
                .append( "Step: " ).append( equation.getRangeStepExpr() )
                .append( endl )
                .append( "Prec: " ).append( equation.getPrecision() )
                .append( endl )
                .append( "Radius: " ).append( equation.getRadiusName() )
                .append( endl )
                .append( "Theta: " ).append( equation.getThetaName() )
                .append( endl )
                .append( "Param: " ).append( equation.getParamName() )
                .append( endl )
                .append( "*************************" )
                .append( endl );

            textArea.append( bldr.toString() );
            textArea.setCaretPosition( textArea.getText().length() );
        }
    }
}
