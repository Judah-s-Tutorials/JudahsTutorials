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
 * ({@link JColorChooserDemo2a})
 * is that this application
 * puts action listeners
 * on the OK and Cancel buttons
 * (the last two arguments
 * of the call to <em>createDialog</em>).
 * As a result,
 * the application
 * is able to distinguish between
 * the operator pressing the OK button
 * and the operator pressing the Cancel button.
 * <p>
 * The GUI initially consists
 * of a feedback window
 * and a pushbutton.
 * To display the color-chooser dialog
 * press the pushbutton.
 * If you select a color in the dialog
 * and then press
 * the dialog's OK button,
 * that color will be reflected
 * in the feedback window.
 * If you dismiss the dialog
 * in any other fashion
 * the color selection
 * will be ignored.
 * </p>
 * 
 * @author Jack Straub
 * 
 * @see JColorChooserDemo2a
 */
public class JColorChooserDemo2b
{
    /** 
     * Indicates the operator chose the OK button 
     * in the color-chooser dialog.
     */
    private static final int        OK_CHOICE       = 0;
    /** 
     * Indicates the operator chose the Cancel button 
     * in the color-chooser dialog.
     */
    private static final int        CANCEL_CHOICE   = 1;
    /** JColorChooser component to display in the dialog. */
    private static JColorChooser    colorPane;
    /** The dialog containing the JColorChooser component. */
    private static JDialog          dialog;
    
    /** 
     * Feedback component for displaying the color selected
     * from the JColorChooser component.
     */
    private static JPanel           feedback;
    
    /** Records the operator's choice of OK/Cancel operations. */
    private static int  choice  = -1;
    
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
    public static void build()
    {
        colorPane = new JColorChooser();
        dialog      =
            JColorChooser.createDialog(
                null, 
                "Choose a Color", 
                true, 
                colorPane, 
                e -> choice = OK_CHOICE, 
                e -> choice = CANCEL_CHOICE
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
            if ( choice == OK_CHOICE )
                feedback.setBackground( colorPane.getColor() );
        });
        pane.add( pushMe, BorderLayout.SOUTH );

        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
}
