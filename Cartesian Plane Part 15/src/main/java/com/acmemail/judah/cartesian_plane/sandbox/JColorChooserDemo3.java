package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * Application to demonstrate
 * how to embed 
 * a JColorChooser
 * in a GUI.
 * Note that this application
 * does <em>not</em> employ a dialog.
 * The application displays the color-chooser
 * on the left-side of the GUI,
 * and a feedback window and controls
 * on the right.
 * Note that the preview panel
 * of the JColorChooser
 * has been removed
 * (see {@link #JColorChooserDemo3()}, 
 * "chooser.setPreviewPanel( new JPanel() )").
 * To use the application,
 * select a color on the left,
 * and choose a button on the right,
 * which will set either 
 * the background color
 * or the foreground color
 * of the feedback window
 * to the selected color.
 * 
 * @author Jack Straub
 */
public class JColorChooserDemo3
{
    /** Test to display in the feedback window. */
    private static final String sampleText  =
        "<html><center>"
        + "An elephant's faithful,<br>one hundred percent"
        + "</center></html>";
    /** The feedback window. */
    private final JLabel        feedback    = new JLabel( sampleText );
    /** The JColorChooser displayed on the let of the application GUI. */
    private final JColorChooser chooser     = new JColorChooser( Color.BLUE );
    /** Button to change the text color of the feedback window. */
    private final JButton       fgButton    = new JButton( "Set Foreground" );
    /** Button to change the background color of the feedback window. */
    private final JButton       bgButton    = new JButton( "Set Background" );
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( JColorChooserDemo3::new );
    }

    /**
     * Constructor.
     * Creates and shows the GUI.
     */
    public JColorChooserDemo3()
    {
        JFrame  frame   = new JFrame( "Color Chooser Demo 3" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  pane    = new JPanel();
        pane.setLayout( new BoxLayout( pane, BoxLayout.X_AXIS ) );
        pane.add( chooser );
        pane.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
        pane.add( getFeedbackPanel() );
        
        // Turn off the preview panel in the color-chooser.
        chooser.setPreviewPanel( new JPanel() );

        frame.setContentPane( pane );
        frame.pack();
        SandboxUtils.center( frame );
        frame.setVisible( true );
    }
    
    /**
     * Creates the feedback windows
     * and controls (pushbuttons)
     * of the GUI.
     * 
     * @return panel containing the feedback window
     *         and controls (pushbuttons) of the GUI
     */
    private JPanel getFeedbackPanel()
    {
        Font    fbFont      = new Font( Font.SERIF, Font.PLAIN, 24 );
        Border  border      =
            BorderFactory.createLineBorder( Color.BLACK, 3 );
        feedback.setFont( fbFont );
        feedback.setHorizontalAlignment( SwingUtilities.CENTER );
        feedback.setOpaque( true );
        feedback.setBorder( border );
        
        JPanel  buttonPanel = new JPanel();
        buttonPanel.add( fgButton );
        buttonPanel.add( bgButton );
        
        int     type        = EtchedBorder.RAISED;
        Border  inBorder    = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        Border  outBorder   = BorderFactory.createEtchedBorder( type );
        border = BorderFactory.createCompoundBorder( outBorder, inBorder );
        
        JPanel  mainPanel   = new JPanel( new BorderLayout() );
        mainPanel.add( buttonPanel, BorderLayout.NORTH );
        mainPanel.add( feedback, BorderLayout.CENTER );
        mainPanel.setBorder( border );
        
        fgButton.addActionListener( this::apply );
        bgButton.addActionListener( this::apply );
        
        return mainPanel;
    }
    
    /**
     * Process a button selection,
     * changing either the text
     * or background color
     * of the feedback window.
     * 
     * @param evt   ActionEvent describing
     *              the pushbutton that was
     *              selected by the operator
     */
    private void apply( ActionEvent evt )
    {
        Color   color   = 
            chooser.getSelectionModel().getSelectedColor();
        System.out.println( color );
        if ( evt.getSource() == fgButton )
            feedback.setForeground( color );
        else
            feedback.setBackground( color );
    }
}
