package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.function.DoubleConsumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.ProfileEditorFeedback;

/**
 * Simple application to show the ProfileEditorFeedback window.
 * The window is displayed, 
 * and the operator has the ability
 * to change the grid unit property,
 * and the spacing property for the grid lines.
 * 
 * @author Jack Straub
 */
public class ShowProfileEditorFeedbackWindow
{
    /** Title for the JFrame. */
    private static final String title       = 
        "ProfileEditorFeedback Window";
    /** Name of the grid lines property set. */
    private static final String gridLines   = 
        LinePropertySetGridLines.class.getSimpleName();
    
    /** Working profile. */
    private final Profile           profile         = new Profile();
    /** LinePropertySetGridLines object from working profile. */
    private final LinePropertySet   gridLineProps   =
        profile.getLinePropertySet( gridLines );
    /** Feedback window. */
    private final JComponent        canvas          = 
        new ProfileEditorFeedback( profile );
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( ShowProfileEditorFeedbackWindow::new );
    }

    /**
     * Constructor. 
     * Creates and configures the application GUI.
     * Must be invoked from the EDT.
     */
    public ShowProfileEditorFeedbackWindow()
    {
        JFrame      frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JPanel      controls    = getControls();
        
        contentPane.add( controls, BorderLayout.WEST );
        contentPane.add( canvas, BorderLayout.CENTER );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Gets a panel containing controls
     * for changing the grid unit,
     * and spacing properties for grid lines.
     * 
     * @return  panel containing controls
     */
    private JPanel getControls()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border      =
            BorderFactory.createLineBorder( Color.BLACK, 3 );
        panel.setLayout( layout );
        panel.setBorder( border );
        
        JSpinner    spinner     = getSpinner(
            profile.getGridUnit(),
            d -> profile.setGridUnit( (float)d )
        );
        panel.add( new JLabel( "Grid Unit" ) );
        panel.add( spinner );
        
        spinner = getSpinner(
            gridLineProps.getSpacing(),
            d -> gridLineProps.setSpacing( (float)d )
        );
        panel.add( new JLabel( "Grid Lines Spacing" ) );
        panel.add( spinner );
        
        JButton exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 1 ) );
        panel.add( exit );
        
        // The BoxLayout on the main JPanel stretches the components
        // of its client. Work around the stretching by putting the 
        // main panel inside another panel.
        JPanel      base    = new JPanel();
        base.add( panel );
        
        return base;
    }
    
    /**
     * Create a JSpinner to be used
     * for modifying a property.
     * 
     * @param value     the initial value of the property
     * @param setter    
     *      consumer to change the property value
     *      when the value of the spinner changes
     *      
     * @return  the created spinner
     */
    private JSpinner getSpinner( double value, DoubleConsumer setter )
    {
        SpinnerNumberModel  model       = 
            new SpinnerNumberModel( value, 0, Integer.MAX_VALUE, 1 );
        JSpinner            spinner     = new JSpinner( model );
        spinner.addChangeListener( e -> {
            double  newValue    = model.getNumber().doubleValue();
            setter.accept( newValue );
            canvas.repaint();
        });
        return spinner;
    }
}
