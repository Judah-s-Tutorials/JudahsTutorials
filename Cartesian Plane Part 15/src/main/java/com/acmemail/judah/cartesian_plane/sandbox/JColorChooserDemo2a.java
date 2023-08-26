package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This is one of two applications
 * that demonstrate
 * how to create a JColorChooser dialog
 * using the <em>createDialog</em> method
 * of the JColorChooser class.
 * The difference between this application
 * and the alternative
 * ({@link JColorChooserDemo2b})
 * is that this application
 * does not put action listeners
 * on the OK and Cancel buttons
 * (the last two arguments
 * of the call to <em>createDialog</em>).
 * As a result,
 * the application
 * cannot distinguish between
 * the operator pressing the OK button
 * and the operator pressing the Cancel button.
 * <p>
 * The GUI initially consists
 * of a feedback window
 * and a pushbutton.
 * To display the color-chooser dialog
 * press the pushbutton.
 * If you select a color in the dialog
 * that color will be reflected
 * in the feedback window
 * after you dismiss the dialog.
 * </p>
 * 
 * @author Jack Straub
 * 
 * @see JColorChooserDemo2b
 */
public class JColorChooserDemo2a
{
    /** JColorChooser component to display in the dialog. */
    private static JColorChooser    colorPane;
    /** The dialog containing the JColorChooser component. */
    private static JDialog          dialog;
    
    /** 
     * Feedback component for displaying the color selected
     * from the JColorChooser component.
     */
    private static JPanel           feedback;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> build() );
    }

    /**
     * Create and show the GUI.
     */
    private static void build()
    {
        colorPane = new JColorChooser();
        dialog      =
            JColorChooser.createDialog(
                null, 
                "Choose a Color", 
                true, 
                colorPane, 
                null, 
                null
            );

        JFrame  frame   = new JFrame( "Feedback Window" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        Container   pane    = frame.getContentPane();
        pane.setLayout( new BorderLayout() );
        feedback = new JPanel();
        feedback.setPreferredSize( new Dimension( 100, 100 ) );
        pane.add( feedback, BorderLayout.CENTER );
        
        JButton pushMe  = new JButton( "Push Me" );
        pushMe.addActionListener( e -> {
            dialog.setVisible( true );
            feedback.setBackground( colorPane.getColor() );
        });
        pane.add( pushMe, BorderLayout.SOUTH );

        frame.pack();
        frame.setLocation( 100, 100 );
        frame.setVisible( true );
    }
}
