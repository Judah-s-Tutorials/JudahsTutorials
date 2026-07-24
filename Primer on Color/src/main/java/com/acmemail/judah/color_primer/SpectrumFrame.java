package com.acmemail.judah.color_primer;


import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;

/**
 * This class encapsulates the frame that is required
 * to assemble the GUI for our project.
 * 
 * @author Jack Straub
 */
public class SpectrumFrame implements Runnable
{
    /** The application frame.  */
    private JFrame  frame       = null;
    
    private final RangeSlider   hueSlider       = new RangeSlider( 0, 360 );
    private final JSlider       satSlider       = new JSlider( 0, 100 );
    private final JSlider       brightSlider    = new JSlider( 0, 100 );
    /** 
     * The frame's content pane. This window will ultimately
     * encapsulate all the components of our project's GUI.
     */
    private JPanel  contentPane = null;
    /** The window that we will be drawing on. */
    private JPanel  userPanel   = null;
    
    /**
     * Constructor.
     * 
     * @param userPanel	The window that the application will be
     * 					drawing to. Will become a child of the
     * 					content pane.
     */
    public SpectrumFrame( JPanel userPanel )
    {
        this.userPanel = userPanel;
    }

    public SpectrumFrame(  )
    {
        this.userPanel = userPanel;
    }
    
    /**
     * Display the frame.
     */
    public void start()
    {
    	/* 
    	 * The invokeLater method display the window
    	 * and activates the process for managing things
    	 * like button clicks, window resizing and 
    	 * window minimization/maximization.
    	 * The object passed to the method must implement
    	 * Runnable. This will eventually result in this
    	 * object's run method being invoked.
    	 */
        SwingUtilities.invokeLater( this );
    }
    
    /**
     * Required by the Runnable interface.
     * This method is the place where the initial content
     * of the frame must be configured.
     */
    public void run()
    {
    	/* Instantiate the frame. */
        frame = new JFrame( "Graphics Frame" );
        
        /* 
         * This will cause your application to be terminated
         * when the frame is closed. If you forget this step,
         * when you close the frame it will disappear,
         * but your application will continue to run.
         */
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        /* 
         * A layout manager is responsible for fine-tuning the
         * layout of a panel. For now you should consider this
         * to be boilerplate for our application. To learn more
         * about layout managers see the Oracle tutorial at
         * https://docs.oracle.com/javase/tutorial/uiswing/layout/index.html.
         * see the JDK documentation.
         */
        BorderLayout    layout  = new BorderLayout();
        contentPane = new JPanel( layout );
        
        /* Make the Canvas a child of the content pane. */
        contentPane.add( userPanel, BorderLayout.CENTER );
        /* Set the content pane in the frame. */
        
        /* Set the range slider at the bottom of the frame. */
        contentPane.add( getSlider(), BorderLayout.SOUTH ); 
        frame.setContentPane( contentPane );
        /* Initiate frame sizing, positioning etc. */
        frame.pack();
        /* Make the frame visible. */
        frame.setVisible( true );
    }
    
    public int getHueLowerValue()
    {
        int val = hueSlider.getValue();
        return val;
    }
    
    public int getHueUpperValue()
    {
        int val = hueSlider.getUpperValue();
        return val;
    }
    
    public int getSaturation()
    {
        int val = satSlider.getValue();
        return val;
    }
    
    public int getBrightness()
    {
        int val = brightSlider.getValue();
        return val;
    }
    
    private JPanel getSlider()
    {
        final char      degree      = '\u00b0';
        final String    hueText     = "Hue: ";
        final String    satText     = "Saturation: ";
        final String    brightText  = "Brightness: ";
        
        hueSlider.setValue( 90 );
        hueSlider.setUpperValue( 270 );
        satSlider.setValue( 100 );
        brightSlider.setValue( 100 );
        JLabel      hueLabel        = new JLabel();
        JLabel      satLabel        = new JLabel();
        JLabel      brightLabel     = new JLabel();
        
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border      =
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( hueSlider );
        panel.add( hueLabel );
        panel.add( satSlider );
        panel.add( satLabel );
        panel.add( brightSlider );
        panel.add( brightLabel );
        
        ChangeListener  hueListener     = e -> {
            String  text    = "" + getHueLowerValue() + degree + " - " 
                + getHueUpperValue() + degree;
            hueLabel.setText( hueText + text );
            userPanel.repaint();
        };
        ChangeListener  satListener     = e -> {
            String  text    = "" + satSlider.getValue();
            satLabel.setText( satText + text );
            userPanel.repaint();
        };
        ChangeListener  brightListener  = e -> {
            String  text    = "" + brightSlider.getValue();
            brightLabel.setText( brightText + text );
            userPanel.repaint();
        };
        hueListener.stateChanged( null );
        satListener.stateChanged( null );
        brightListener.stateChanged( null );
        hueSlider.addChangeListener( hueListener );
        satSlider.addChangeListener( satListener );
        brightSlider.addChangeListener( brightListener );
        return panel;
    }
}
