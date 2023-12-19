package com.acmemail.judah.cartesian_plane.test_utils.lp_panel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.LinePropertiesPanel;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * This is a dialog containing a LinePropertiesPanel
 * for testing purposes.
 * All the components needed for testing
 * are available via this class.
 * The values of all components can be modified
 * (see {@linkplain #synchRight(LinePropertySet)})
 * or obtained,
 * (see {@linkplain #getAllProperties()})
 * and all buttons can be selected
 * (see {@linkplain #doClick(AbstractButton)})
 * via the enclosed facilities.
 * The facilities in this class
 * ensure that,
 * when necessary,
 * they are
 * executed on the Event Dispatch Thread (EDT).
 * <p>
 * This class is implemented
 * as a singleton.
 * To obtain its sole object
 * call {@linkplain #getDialog()}.
 * 
 * @author Jack Straub
 * 
 * @see #getDialog()
 */
@SuppressWarnings("serial")
public class LPPTestDialog extends JDialog
{
    private static LPPTestDialog        dialog          = null;
    
    private final LinePropertiesPanel   propertiesPanel = 
        new LinePropertiesPanel();
    private final List<PRadioButton<LinePropertySet>>      
        radioButtons;
    private final JPanel                propCompPanel;
    private final JSpinner              strokeSpinner;
    private final SpinnerNumberModel    strokeModel;
    private final JLabel                strokeLabel;
    private final JSpinner              lengthSpinner;
    private final SpinnerNumberModel    lengthModel;
    private final JLabel                lengthLabel;
    private final JSpinner              spacingSpinner;
    private final SpinnerNumberModel    spacingModel;
    private final JLabel                spacingLabel;
    private final JButton               colorButton;
    private final JTextField            colorField;
    private final JCheckBox             drawCheckBox;
    
    private final JButton               applyButton;
    private final JButton               resetButton;
    private final JButton               closeButton;
    
    // These are volatile variables for use in lambdas. They are used
    // in method return statements, after which their values are no
    // longer predictable.
    private boolean     tempBoolean;
    private CompConfig  tempConfig;
    private Object      tempObj;

    /**
     * Default constructor.
     * Private to prevent external instantiation.
     * 
     * @see #getDialog()
     */
    private LPPTestDialog()
    {
        propCompPanel = getPropCompPanel();
        radioButtons = parseRButtons();
        
        List<JSpinner>  allSpinners = parseSpinners();
        strokeSpinner = allSpinners.get( 0 );
        strokeModel = (SpinnerNumberModel)strokeSpinner.getModel();
        strokeLabel = parseJLabel( "Stroke" );
        lengthSpinner = allSpinners.get( 1 );
        lengthModel = (SpinnerNumberModel)lengthSpinner.getModel();
        lengthLabel = parseJLabel( "Length" );
        spacingSpinner = allSpinners.get( 2 );
        spacingModel = (SpinnerNumberModel)spacingSpinner.getModel();
        spacingLabel = parseJLabel( "Spacing" );
        
        colorButton = parseJButton( "Color" );
        colorField = parseColorField();
        drawCheckBox = parseJCheckBox();
        
        applyButton = parseJButton( "Apply" );
        resetButton = parseJButton( "Reset" );
        closeButton = parseJButton( "Close" );
        
        setTitle( "Line Properties Panel Test Dialog" );
        setContentPane( propertiesPanel );
        pack();
    }
    
    /**
     * Returns the JDialog object
     * comprising this class's singleton.
     * 
     * @return  the JDialog object comprising this class's singleton
     */
    public static LPPTestDialog getDialog()
    {
        if ( dialog == null )
        {
            if ( SwingUtilities.isEventDispatchThread() )
                dialog = new LPPTestDialog();
            else
                GUIUtils.schedEDTAndWait( () ->
                    dialog = new LPPTestDialog()
                );
        }
        return dialog;
    }
    
    /**
     * Selects the encapsulated LinePropertiesPanel Apply button.
     */
    public void apply()
    {
        doClick( applyButton );
    }
    
    /**
     * Selects the encapsulated LinePropertiesPanel Reset button.
     */
    public void reset()
    {
        doClick( resetButton );
    }
    
    /**
     * Selects the encapsulated LinePropertiesPanel Close button.
     */
    public void close()
    {
        doClick( closeButton );
    }

    /**
     * Clicks a given button.
     * 
     * @param button    the given button
     */
    public void doClick( AbstractButton button )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            button.doClick();
        else
            GUIUtils.schedEDTAndWait( () -> button.doClick() ); 
    }
    
    /**
     * Sets the visibility of the dialog.
     * 
     * @param visible   true to make the dialog visible
     */
    public void setDialogVisible( boolean visible )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            setVisible( visible );
        else
            GUIUtils.schedEDTAndWait( 
                () -> setVisible( visible )
            );
    }
    
    /**
     * Indicates whether this dialog is in the visible state.
     * 
     * @return  true if this dialog is visible
     */
    public boolean isDialogVisible()
    {
        if ( SwingUtilities.isEventDispatchThread() )
            tempBoolean = isVisible();
        else
            GUIUtils.schedEDTAndWait( 
                () -> tempBoolean = isVisible()
            );
        return tempBoolean;
    }
    
    /**
     * Indicates whether a given button is in the selected state.
     * 
     * @param button    the given button
     * 
     * @return  true if the given button is selected
     */
    public boolean isSelected( AbstractButton button )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            tempBoolean = button.isSelected();
        else
            GUIUtils.schedEDTAndWait( 
                () -> tempBoolean = button.isSelected()
            );
        return tempBoolean;
    }
    
    /**
     * Gets a list of the encapsulated LinePropertiesPanel's
     * radio buttons.
     * 
     * @return  
     *      a list of the encapsulated LinePropertiesPanel's
     *      radio buttons
     */
    public List<PRadioButton<LinePropertySet>> getRadioButtons()
    {
        return radioButtons;
    }
    
    /**
     * Returns the values of all 
     * the LinePropertiesPanel's components
     * used to configure properties.
     * 
     * @return  the values of all configuration components
     */
    public CompConfig getAllProperties()
    {
        if ( SwingUtilities.isEventDispatchThread() )
            tempConfig = new CompConfig();
        else
            GUIUtils.schedEDTAndWait( () -> 
                tempConfig = new CompConfig()
            );
        
        return tempConfig;
    }
    
    /**
     * Synchronize the values in a given LinePropertySet
     * with the components that manage them.
     * (The components on the right side
     * of the LinePropertiesPanel).
     * 
     * @param set   the given LinePropertySet
     */
    public void synchRight( LinePropertySet set )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            synchRightEDT( set );
        else
            GUIUtils.schedEDTAndWait( () -> synchRightEDT( set ) );
    }
    
    /**
     * Gets a BufferedImage reflecting 
     * the current state of the LinePropertiesPanel.
     * 
     * @return  
     *      a BufferedImage reflecting 
     *      the current state of the LinePropertiesPanel
     */
    public BufferedImage getPanelImage()
    {
        if ( SwingUtilities.isEventDispatchThread() )
            tempObj = getPanelImageEDT();
        else
            GUIUtils.schedEDTAndWait( () -> 
                tempObj = getPanelImageEDT()
            );
        return (BufferedImage)tempObj;
    }
    
    /**
     * Gets a BufferedImage reflecting 
     * the current state of the LinePropertiesPanel.
     * <p>
     * Precondition:
     *     Must be invoked on the EDT.
     * 
     * @return  
     *      a BufferedImage reflecting 
     *      the current state of the LinePropertiesPanel
     */
    private BufferedImage getPanelImageEDT()
    {
        int             type    = BufferedImage.TYPE_INT_ARGB;
        int             width   = propertiesPanel.getWidth();
        int             height  = propertiesPanel.getHeight();
        BufferedImage   image   = 
            new BufferedImage( width, height, type );
        Graphics2D      gtx     = image.createGraphics();
        propertiesPanel.paintComponents( gtx );
        return image;
    }
    
    /**
     * Synchronize the values in a given LinePropertySet
     * with the components that manage them.
     * (The components on the right side
     * of the LinePropertiesPanel).
     * 
     * Precondition:
     * This method must be invoked
     * on the Event Dispatch Thread.
     * 
     * @param set   the given LinePropertySet
     */
    private void synchRightEDT( LinePropertySet set )
    {
        if ( set.hasStroke() )
            strokeSpinner.setValue( set.getStroke() );
        if ( set.hasLength() )
            lengthSpinner.setValue( set.getLength() );
        if ( set.hasSpacing() )
            spacingSpinner.setValue( set.getSpacing() );
        if ( set.hasColor() )
            setColor( set.getColor() );
        if ( set.hasDraw() )
            drawCheckBox.setSelected( set.getDraw() );
    }

    /**
     * Converts a string to a Color.
     * The string is expected to contain
     * a valid integer value
     * in the range of an Integer;
     * alpha bits are stripped.
     * If the input is invalid
     * null is returned.
     * 
     * @param strColor  the string to convert to an integer
     * 
     * @return  the converted value, or null if the input is invalid
     */
    private static Color colorValue( String strColor )
    {
        Color   color   = null;
        try
        {
            int intColor    = Integer.decode( strColor ) & 0xFFFFFF;
            color = new Color( intColor );
        }
        catch ( NumberFormatException exc )
        {
        }
        
        return color;
    }
    
    /**
     * Converts a given color to a string
     * and uses it to set the value 
     * of the colorField.
     * The value is represented
     * as a hexadecimal number
     * with the alpha bits stripped.
     * After the text value is set,
     * the text field issues an ActionEvent,
     * just as though an operator
     * had typed the enter key
     * in the field.
     * <p>
     * It is save to call this method
     * from any thread.
     * 
     * @param color the given color
     */
    private void setColor( Color color )
    {
        int     rgb     = color.getRGB() & 0xffffff;
        String  strRGB  = String.format( "0x%06x", rgb );
        if ( SwingUtilities.isEventDispatchThread() )
        {
            colorField.setText( strRGB );
            colorField.postActionEvent();
        }
        else
            GUIUtils.schedEDTAndWait( () -> {
                colorField.setText( strRGB );
                colorField.postActionEvent();
            });
    }
    
    /**
     * Obtains, from the LinePropertiesPanel,
     * the panel that contains the controls
     * used to manage property values.
     * This is the panel that appears
     * on the right side of the LinePropertiesPanel,
     * and includes the spinners
     * and the color editor.
     * 
     * @return
     *      the panel that contains the controls
     *      used to manage property values
     */
    private JPanel getPropCompPanel()
    {
        Predicate<JComponent>   pred    = c -> (c instanceof JSpinner);
        JComponent              comp    =
            ComponentFinder.find( propertiesPanel, pred );
        
        assertNotNull( comp );
        assertTrue( comp instanceof JSpinner );
        Container               parent  = comp.getParent();
        assertNotNull( parent );
        assertTrue( parent instanceof JPanel );
        return (JPanel)parent;
    }
    
    /**
     * Obtains a list of all JSpinners
     * in the LinePropertiesPanel.
     * <p>
     * Precondition:
     * The panel containing the spinners 
     * ({@linkplain #propCompPanel})
     * has already been obtained
     * from the GUI.
     * 
     * @return  a list of all JSpinners in the LinePropertiesPanel
     * 
     * @see #getPropCompPanel()
     */
    private List<JSpinner> parseSpinners()
    {
        List<JSpinner>  allSpinners   = 
            Stream.of( propCompPanel.getComponents() )
                .filter( c -> (c instanceof JSpinner) )
                .map( c -> (JSpinner)c )
                .toList();
        assertEquals( 3, allSpinners.size() );
        return allSpinners;
    }
    
    /**
     * Obtains a list of all radio buttons
     * in the GUI.
     * These are the buttons
     * on the left side
     * of the {@link LinePropertiesPanel}.
     */
    private List<PRadioButton<LinePropertySet>> parseRButtons()
    {
        List<PRadioButton<LinePropertySet>> buttons =
            Stream.of( "Axes", "Major Tics", "Minor Tics", "Grid Lines" )
                .map( this::parseRButton )
                .toList();
        return buttons;
    }
    
    /**
     * Obtains the radio button
     * with the given text.
     * 
     * @param text  the given text
     * 
     * @return  the radio button with the given text
     */
    @SuppressWarnings("unchecked")
    private PRadioButton<LinePropertySet> parseRButton( String text )
    {
        Predicate<JComponent>   isButton    =
            c -> (c instanceof PRadioButton<?>);
        Predicate<JComponent>   hasText     =
            c -> ((PRadioButton<?>)c).getText().equals( text );
        Predicate<JComponent>   pred        =
            isButton.and( hasText );
        JComponent  comp    =
            ComponentFinder.find( propertiesPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof PRadioButton<?> );
        PRadioButton<?> testButton  = (PRadioButton<?>)comp;
        assertTrue( testButton.get() instanceof LinePropertySet );
        PRadioButton<LinePropertySet>   button  =
            (PRadioButton<LinePropertySet>)testButton;
        return button;
    }

    /**
     * Obtains the JButton
     * with the given text.
     * 
     * @param text  the given text
     * 
     * @return  the JButton with the given text
     */
    private JButton parseJButton( String text )
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( text );
        JComponent              comp    =
            ComponentFinder.find( propertiesPanel , pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton, text );
        return (JButton)comp;
    }
    
    /**
     * Parses from the GUI
     * the text field that manages the color property.
     * <p>
     * Precondition:
     * The panel containing the color editor 
     * ({@linkplain #propCompPanel})
     * has already been obtained
     * from the GUI.
     * 
     * @return  the text field that manages the color property
     * 
     * @see #getPropCompPanel()
     */
    private JTextField parseColorField()
    {
        // The color field is the JTextField that is a *direct* child
        // of the components panel, not to be confused with JTextFields
        // incorporated into the spinners.
        JTextField  textField   = 
            Stream.of( propCompPanel.getComponents() )
                .filter( c -> (c instanceof JTextField) )
                .map( c -> (JTextField)c )
                .findFirst().orElse( null );
        assertNotNull( textField );
        return textField;
    }
    
    /**
     * Parses the first JCheckBox
     * from the GUI.
     * 
     * @return  the first JCheckBox found in the GUI
     */
    private JCheckBox parseJCheckBox()
    {
        Predicate<JComponent>   pred    = c -> (c instanceof JCheckBox);
        JComponent              comp    =
            ComponentFinder.find( propertiesPanel , pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
    }
    
    /**
     * Parses from the GUI
     * the JLabel with the given text.
     *  
     * @param text  the given text
     * 
     * @return  the JLabel with the given text
     */
    private JLabel parseJLabel( String text )
    {
        Predicate<JComponent>   isLabel     =
            c -> (c instanceof JLabel);
        Predicate<JComponent>   hasText     =
            c -> ((JLabel)c).getText().equals( text );
        Predicate<JComponent>   pred        =
            isLabel.and( hasText );
        JComponent  comp    =
            ComponentFinder.find( propertiesPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JLabel );
        JLabel label  = (JLabel)comp;
        return label;
    }
    
    /**
     * Encapsulates the state
     * of all components used to manage property values.
     * (The components on the right side
     * of the LinePropertiesPanel).
     * "State" includes a component's value
     * and whetheror not it is enabled.
     * 
     * @author Jack Straub
     */
    public class CompConfig
    {
        /** True if the stroke spinner is enabled. */
        public final boolean    strokeSpinnerEnabled;
        /** True if the label on the stroke spinner is enabled. */
        public final boolean    strokeLabelEnabled;
        /** True if the length spinner is enabled. */
        public final boolean    lengthSpinnerEnabled;
        /** True if the label on the length spinner is enabled. */
        public final boolean    lengthLabelEnabled;
        /** True if the spacing spinner is enabled. */
        public final boolean    spacingSpinnerEnabled;
        /** True if the label on the spacing spinner is enabled. */
        public final boolean    spacingLabelEnabled;
        /** True if the color button is enabled. */
        public final boolean    colorButtonEnabled;
        /** True if the color text field is enabled. */
        public final boolean    colorFieldEnabled;
        /** True if the color "draw" check box is enabled. */
        public final boolean    drawCheckBoxEnabled;
        
        /** The value of the stroke spinner. */
        public final float      stroke;
        /** The value of the length spinner. */
        public final float      length;
        /** The value of the spacing spinner. */
        public final float      spacing;
        /** The value of the color field. */
        public final Color      color;
        /** The value of the "draw" property. */
        public final boolean    draw;
        
        /**
         * Constructor.
         * Initializes all fields.
         */
        private CompConfig()
        {
            strokeSpinnerEnabled = strokeSpinner.isEnabled();
            strokeLabelEnabled = strokeLabel.isEnabled();
            lengthSpinnerEnabled = lengthSpinner.isEnabled();
            lengthLabelEnabled = lengthLabel.isEnabled();
            spacingSpinnerEnabled = spacingSpinner.isEnabled();
            spacingLabelEnabled = spacingLabel.isEnabled();
            colorButtonEnabled = colorButton.isEnabled();
            colorFieldEnabled = colorField.isEnabled();
            drawCheckBoxEnabled = drawCheckBox.isEnabled();
            
            stroke = strokeModel.getNumber().floatValue();
            length = lengthModel.getNumber().floatValue();
            spacing = spacingModel.getNumber().floatValue();
            color = colorValue( colorField.getText() );
            draw = drawCheckBox.isSelected();
        }
    }
}
