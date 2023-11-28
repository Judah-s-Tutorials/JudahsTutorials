package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Container;
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
    
    public static LPPTestDialog getDialog()
    {
        if ( dialog == null )
            GUIUtils.schedEDTAndWait( () ->
                dialog = new LPPTestDialog()
            );
        return dialog;
    }
    
    public void apply()
    {
        doClick( applyButton );
    }
    
    public void reset()
    {
        doClick( resetButton );
    }
    
    public void close()
    {
        doClick( closeButton );
    }
    
    public void setDialogVisible( boolean visible )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            super.setVisible( visible );
        else
            GUIUtils.schedEDTAndWait( 
                () -> super.setVisible( visible )
            );
    }
    
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
    
    public List<PRadioButton<LinePropertySet>> getRadioButtons()
    {
        return radioButtons;
    }
    
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
    
    public void synchRight( LinePropertySet set )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            synchRightEDT( set );
        else
            GUIUtils.schedEDTAndWait( () -> synchRightEDT( set ) );
    }

    public void doClick( AbstractButton button )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            button.doClick();
        else
            GUIUtils.schedEDTAndWait( () -> button.doClick() ); 
    }
    
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
    
    private List<PRadioButton<LinePropertySet>> parseRButtons()
    {
        List<PRadioButton<LinePropertySet>> buttons =
            Stream.of( "Axes", "Major Tics", "Minor Tics", "Grid Lines" )
                .map( this::parseRButton )
                .toList();
        return buttons;
    }
    
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
    
    private JCheckBox parseJCheckBox()
    {
        Predicate<JComponent>   pred    = c -> (c instanceof JCheckBox);
        JComponent              comp    =
            ComponentFinder.find( propertiesPanel , pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
    }
    
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
    
    public class CompConfig
    {
        public final boolean    strokeSpinnerEnabled;
        public final boolean    strokeLabelEnabled;
        public final boolean    lengthSpinnerEnabled;
        public final boolean    lengthLabelEnabled;
        public final boolean    spacingSpinnerEnabled;
        public final boolean    spacingLabelEnabled;
        public final boolean    colorButtonEnabled;
        public final boolean    colorFieldEnabled;
        public final boolean    drawCheckBoxEnabled;
        
        public final float      stroke;
        public final float      length;
        public final float      spacing;
        public final Color      color;
        public final boolean    draw;
        
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
