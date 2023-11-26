package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
    private final LinePropertiesPanel   propertiesPanel = 
        new LinePropertiesPanel();
    private final List<PRadioButton<LinePropertySet>>      
        radioButtons;
    private final JPanel                propCompPanel;
    private final JSpinner              strokeSpinner;
    private final SpinnerNumberModel    strokeModel;
    private final JSpinner              lengthSpinner;
    private final SpinnerNumberModel    lengthModel;
    private final JSpinner              spacingSpinner;
    private final SpinnerNumberModel    spacingModel;
    private final JButton               colorButton;
    private final JTextField            colorField;
    private final JCheckBox             drawCheckBox;
    
    private final JButton               applyButton;
    private final JButton               resetButton;
    private final JButton               closeButton;

    public LPPTestDialog()
    {
        propCompPanel = getPropControlPanel();
        radioButtons = parseRButtons();
        
        List<JSpinner>  allSpinners = parseSpinners();
        strokeSpinner = allSpinners.get( 0 );
        lengthSpinner = allSpinners.get( 1 );
        spacingSpinner = allSpinners.get( 2 );
        strokeModel = (SpinnerNumberModel)strokeSpinner.getModel();
        lengthModel = (SpinnerNumberModel)lengthSpinner.getModel();
        spacingModel = (SpinnerNumberModel)spacingSpinner.getModel();
        
        colorButton = parseJButton( "Color" );
        colorField = parseJColorField();
        drawCheckBox = parseJCheckBox();
        
        applyButton = parseJButton( "Apply" );
        resetButton = parseJButton( "Reset" );
        closeButton = parseJButton( "Close" );
        
        setTitle( "Line Properties Panel Test Dialog" );
        setContentPane( propertiesPanel );
        pack();
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
        boolean[]   isVis   = new boolean[1];
        if ( SwingUtilities.isEventDispatchThread() )
            isVis[0] = isVisible();
        else
            GUIUtils.schedEDTAndWait( 
                () -> isVis[0] = isVisible()
            );
        return isVis[0];
    }
    
    public List<PRadioButton<LinePropertySet>> getRadioButtons()
    {
        return radioButtons;
    }
    
    public void getAllProperties( LinePropertySet set )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            getAllPropertiesEDT( set );
        else
            GUIUtils.schedEDTAndWait( () -> getAllPropertiesEDT( set ) );
    }
    
    public void getAllPropertiesEDT( LinePropertySet set )
    {
        if ( set.hasStroke() )
            set.setStroke( floatValue( strokeModel ) );
        if ( set.hasLength() )
            set.setLength( floatValue( lengthModel ) );
        if ( set.hasSpacing() )
            set.setSpacing( floatValue( spacingModel ) );
        if ( set.hasColor() )
            set.setColor( colorValue( colorField.getText() ) );
        if ( set.hasDraw() )
            set.setDraw( drawCheckBox.isSelected() );
    }
    
    public void synchRight( LinePropertySet set )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            synchRightEDT( set );
        else
            GUIUtils.schedEDTAndWait( () -> synchRightEDT( set ) );
    }
    
    public boolean isStrokeEnabled()
    {
        boolean enabled = isEnabled( strokeSpinner );
        return enabled;
    }
    
    public boolean isLengthEnabled()
    {
        boolean enabled = isEnabled( lengthSpinner );
        return enabled;
    }
    
    public boolean isSpacingEnabled()
    {
        boolean enabled = isEnabled( spacingSpinner );
        return enabled;
    }
    
    public boolean isColorButtonEnabled()
    {
        boolean enabled = isEnabled( colorButton );
        return enabled;
    }
    
    public boolean isColorFieldEnabled()
    {
        boolean enabled = isEnabled( colorField );
        return enabled;
    }
    
    public boolean isDrawEnabled()
    {
        boolean enabled = isEnabled( drawCheckBox );
        return enabled;
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
    
    private static float floatValue( SpinnerNumberModel model )
    {
        float   floatVal    = model.getNumber().floatValue();
        return floatVal;
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
            fail( "Invalid color string", exc );
        }
        
        return color;
    }
    
    private void setColor( Color color )
    {
        int     rgb     = color.getRGB() & 0xffffff;
        String  strRGB  = String.format( "0x%06x", rgb );
        colorField.setText( strRGB );
        colorField.postActionEvent();
    }
    
    public void doClick( AbstractButton button )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            button.doClick();
        else
            GUIUtils.schedEDTAndWait( () -> button.doClick() ); 
    }
    
    private static boolean isEnabled( JComponent comp )
    {
        boolean[]   bVal    = new boolean[1];
        if ( SwingUtilities.isEventDispatchThread() )
            bVal[0] = comp.isEnabled();
        else
            GUIUtils.schedEDTAndWait( () -> 
                bVal[0] = comp.isEnabled()
            );
        return bVal[0];
    }
    
    private JPanel getPropControlPanel()
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
    
    private JTextField parseJColorField()
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
}
