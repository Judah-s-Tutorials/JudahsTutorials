package com.acmemail.judah.color_primer;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.function.IntConsumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;

import com.acmemail.judah.color_primer.util.RangeSlider;

/**
 * Encapsulation of the GUI for the Spectrum project,
 * in which the visual spectrum is represented as a wheel,
 * with red at 0&#xB0;/360&#xB0;,
 * yellow at 60&#xB0;,
 * green at 120&#xB0;, 
 * cyan at 180&#xB0;
 * and magenta at300&#xB0;.
 * It controls the following properties:
 * <ol>
 * <li>
 * Hue minimum: 
 * the minimum hue value to display,
 * stored in degrees.
 * Valid values are integers between 0 and 360 (inclusive).
 * </li>
 * <li>
 * Hue maximum: 
 * the maximum hue value to display,
 * stored in degrees.
 * Valid values are integers between 0 and 360 (inclusive).
 * </li>
 * <li>
 * Saturation: 
 * the saturation property to apply to the wheel,
 * stored as a percent.
 * Valid values are integers between 0 and 100 (inclusive).
 * </li>
 * <li>
 * Brightness: 
 * the brightness property to apply to the wheel,
 * stored as a percent.
 * Valid values are integers between 0 and 100 (inclusive).
 * </li>
 * <li>
 * Bar angle: 
 * the angle of a ray drawn from the center of the wheel
 * to the wheel's edge.
 * stored in degrees.
 * Valid values are integers between 0 and 360 (inclusive).
 * </li>
 * </ol>
 * <p>
 * The operator can control the value
 * of the hue minimum and maximum
 * via a range slider or text fields;
 * the text fields and the range slider 
 * are synchronized.
 * Each of the saturation and brightness properties
 * can be controlled by the operator
 * via slider or text field;
 * the slider and text field are synchronized.
 * <p>
 * No operator access to the bar angle property
 * is provided via this class;
 * see, instead, {@link SpectrumRanger}.
 * 
 * @author Jack Straub
 */
public class SpectrumFrame implements Runnable
{
    /** Unicode value for the degree symbol. */
    private static final char   DEGREE      = '\u00b0';
    private static final Color  VALID_BG    = Color.WHITE;
    private static final Color  INVALID_BG  = Color.RED;
    
    /** The application frame.  */
    private JFrame  frame       = null;
    
    /** Control for the hue minimum and maximum values. */
    private final RangeSlider   hueSlider       = new RangeSlider( 0, 360 );
    /** Control for the saturation value. */
    private final JSlider       satSlider       = new JSlider( 0, 100 );
    /** Control for the brightness value. */
    private final JSlider       brightSlider    = new JSlider( 0, 100 );
    /** 
     * The frame's content pane. This window will ultimately
     * encapsulate all the components of our project's GUI.
     */
    private JPanel  contentPane = null;
    /** The window that we will be drawing on. */
    private JPanel  userPanel   = null;
    /** The bar angle. */
    private double  barAngle    = 0;
    
    /////////////////////////////////////////////////////////////////
    /// Component names to support testing
    /// 
    /** JFrame component name */
    public static final String  APP_FRAME       = "ApplicationFrame";
    /** Hue slider component name. */
    public static final String  HUE_SLIDER      = "HueSlider";
    /** Hue minimum text component name. */
    public static final String  HUE_MIN         = "HueMin";
    /** Hue maximum text component name. */
    public static final String  HUE_MAX         = "HueMax";
    /** Saturation slider component name. */
    public static final String  SAT_SLIDER      = "SatSlider";
    /** Saturation text component name. */
    public static final String  SAT_TEXT        = "SatText";
    /** Brightness slider component name. */
    public static final String  BRIGHT_SLIDER   = "BrightSlider";
    /** Brightness text component name. */
    public static final String  BRIGHT_TEXT     = "BrightText";

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
    
    /**
     * Display the frame.
     */
    public void start()
    {
    	/* 
    	 * The invokeLater method displays the window
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
        frame.setName( APP_FRAME );
        
        /* 
         * This will cause your application to be terminated
         * when the frame is closed. If you forget this step,
         * when you close the frame it will disappear,
         * but your application will continue to run.
         */
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        /* 
         * A layout manager is responsible for fine-tuning the
         * layout of a panel. 
         * This project uses a BorderLayout
         * with the user display in the center region,
         * and the GUI controls in the bottom region.
         * To learn more
         * about layout managers see the Oracle tutorial at
         * https://docs.oracle.com/javase/tutorial/uiswing/layout/index.html.
         * see the JDK documentation.
         */
        BorderLayout    layout  = new BorderLayout();
        contentPane = new JPanel( layout );
        
        /* Make the panel a child of the content pane. */
        contentPane.add( userPanel, BorderLayout.CENTER );
        /* Set the range slider at the bottom of the frame. */
        contentPane.add( getSliderPanel(), BorderLayout.SOUTH ); 
        
        /* Set the content pane in the frame. */
        frame.setContentPane( contentPane );
        /* Initiate frame sizing, positioning etc. */
        frame.pack();
        /* Make the frame visible. */
        frame.setVisible( true );
    }
    
    /**
     * Gets the hue minimum value.
     * 
     * @return the hue minimum value
     */
    public int getHueLowerValue()
    {
        int val = hueSlider.getValue();
        return val;
    }
    
    /**
     * Gets the hue maximum value.
     * 
     * @return the hue maximum value
     */
    public int getHueUpperValue()
    {
        int val = hueSlider.getUpperValue();
        return val;
    }
    
    /**
     * Gets the saturation value.
     * 
     * @return the saturation value
     */
    public int getSaturation()
    {
        int val = satSlider.getValue();
        return val;
    }
    
    /**
     * Gets the brightness value.
     * 
     * @return the brightness value
     */
    public int getBrightness()
    {
        int val = brightSlider.getValue();
        return val;
    }
    
    /**
     * Sets the bar angle value in degrees.
     * 
     * @param degrees   the value to set
     */
    public void setBarAngle( double degrees )
    {
        barAngle = degrees;
    }
    
    /**
     * Gets the bar angle value.
     * 
     * @return the bar angle value
     */
    public double getBarAngle()
    {
        return barAngle;
    }
    
    /**
     * Get the panel containing the sliders
     * and synchronized text fields.
     * @return
     */
    private JPanel getSliderPanel()
    {
        final String    satText     = "Saturation: ";
        final String    brightText  = "Brightness: ";
        
        hueSlider.setValue( 90 );
        hueSlider.setUpperValue( 270 );
        hueSlider.setName( HUE_SLIDER );
        satSlider.setValue( 100 );
        satSlider.setName( SAT_SLIDER );
        brightSlider.setValue( 100 );
        brightSlider.setName( BRIGHT_SLIDER );

        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border      =
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( getHuePanel() );
        panel.add( getSliderPanel( satSlider, satText, SAT_TEXT ) );
        panel.add( getSliderPanel( brightSlider, brightText, BRIGHT_TEXT ) );
        return panel;
    }
    
    /**
     * Configure a panel containing a slider, text field,
     * and descriptive label. 
     * Add event listeners that will:
     * <ul>
     * <li>Keep the values of the slider and text field synchronized.</li>
     * <li>Change the color of text fields with invalid data.</li>
     * <li>Trigger a user panel repaint when the slider changes.</li>
     * </ul>
     * 
     * @param slider    the slider to add
     * @param text      the text for the descriptive label
     * @param textName  the component name of the text field
     * 
     * @return  the configured panel
     */
    private JPanel 
    getSliderPanel( JSlider slider, String text, String textName )
    {
        JPanel      mainPanel   = new JPanel();
        BoxLayout   mainLayout  = 
            new BoxLayout( mainPanel, BoxLayout.Y_AXIS );
        mainPanel.setLayout( mainLayout );
        
        JTextField  textField   = new JTextField( 4 );
        textField.setName( textName );
        JLabel      label       = new JLabel( text );
        JPanel      labelPanel  = new JPanel();
        labelPanel.add( label );
        labelPanel.add( textField );
        
        textField.setHorizontalAlignment( JTextField.RIGHT );
        
        mainPanel.add( slider );
        mainPanel.add( labelPanel );
        ChangeListener  sliderListener  = e -> {
            String  val = "" + slider.getValue() + "%";
            textField.setText( val );
            textField.setBackground( VALID_BG );
            userPanel.repaint();
        };
        slider.addChangeListener( sliderListener );
        sliderListener.stateChanged( null );

        ActionListener  actionListener  = e -> {
            int val = getIntValue( textField );
            if ( val < slider.getMinimum() || val > slider.getMaximum() )
                textField.setBackground( INVALID_BG );
            else
            {
                slider.setValue( val );
                textField.setBackground( VALID_BG );
                textField.setText( val + "%" );
            }
        };
        textField.addActionListener( actionListener );
        
        return mainPanel;
    }
    
    /**
     * Configure a panel containing the hue slider, 
     * minimum and maximum text fields,
     * and descriptive label. 
     * Add event listeners that will:
     * <ul>
     * <li>Keep the values of the slider and text fields synchronized.</li>
     * <li>Change the color of text fields with invalid data.</li>
     * <li>Trigger a user panel repaint when the slider changes.</li>
     * </ul>
     * 
     * @return  the configured panel
     */
    private JPanel getHuePanel()
    {
        JPanel      mainPanel   = new JPanel();
        BoxLayout   mainLayout  = 
            new BoxLayout( mainPanel, BoxLayout.Y_AXIS );
        mainPanel.setLayout( mainLayout );
        
        JTextField  minField    = new JTextField( 3 );
        JTextField  maxField    = new JTextField( 3 );
        JLabel      minLabel    = new JLabel( "Hue min:" );
        JLabel      maxLabel    = new JLabel( "Hue max:" );
        JPanel      labelPanel  = new JPanel();

        minField.setName( HUE_MIN );
        maxField.setName( HUE_MAX );
        labelPanel.add( minLabel );
        labelPanel.add( minField );
        labelPanel.add( maxLabel );
        labelPanel.add( maxField );
        
        minField.setHorizontalAlignment( JTextField.RIGHT );
        
        mainPanel.add( hueSlider );
        mainPanel.add( labelPanel );
        ChangeListener  hueListener     = e -> {
            String  minText = "" + getHueLowerValue() + DEGREE;
            String  maxText = "" + getHueUpperValue() + DEGREE;
            minField.setText( minText );
            maxField.setText( maxText );
            userPanel.repaint();
        };
        hueSlider.addChangeListener( hueListener );
        hueListener.stateChanged( null );
        
        addHueTextListener( minField, hueSlider::setValue );
        addHueTextListener( maxField, hueSlider::setUpperValue );
        
        return mainPanel;
    }
    
    /**
     * Create an ActionListener for the 
     * hue minimum and maximum text fields.
     * 
     * @param field     the target text field
     * @param setter    the the setter for the slider component 
     *                  used to synch with the slider and text field
     *                  
     * @see #getSliderPanel()
     */
    private void addHueTextListener( JTextField field, IntConsumer setter )
    {
        ActionListener  listener     = e -> {
            int val = getIntValue( field );
            int min = hueSlider.getMinimum();
            int max = hueSlider.getMaximum();
            if ( val < min || val > max )
                field.setBackground( INVALID_BG );
            else
            {
                setter.accept( val );
                field.setBackground( VALID_BG );
            }
        };
        field.addActionListener( listener );
    }
    
    /**
     * Get the text from a given text field
     * and convert it to an int.
     * The text may optionally end with 
     * the percent sign or degree symbol
     * which will be ignored.
     * -1 is returned if the text cannot be converted.
     * 
     * @param field the given text field
     * 
     * @return  the converted int; -1 if invalid
     */
    private int getIntValue( JTextField field )
    {
        String  text    = field.getText();
        int     len     = text.length() - 1;
        char    last    = len >= 0 ? text.charAt( len ) : 0;
        if ( last == '\u00b0' || last == '%' )
            text = text.substring( 0, len );
        int val = -1;
        try
        {
            val = Integer.parseInt( text );
        }
        catch ( NumberFormatException exc )
        {
        }
        return val;
    }
}
