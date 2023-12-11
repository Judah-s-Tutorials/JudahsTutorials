package com.acmemail.judah.cartesian_plane.test_utils.gp_plane;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.acmemail.judah.cartesian_plane.components.GraphPropertiesPanel;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;

public class GPPTestDialog extends JDialog
{
    /** This class's singleton. */
    private static GPPTestDialog    dialog;
    
    private final GraphPropertiesPanel  propertiesPanel =
        new GraphPropertiesPanel();
    
    private final List<PRadioButton<GraphPropertySet>>  radioButtons;
    private final JPanel                fontEditorPanel;
    private final JComboBox<String>     fontNames;
    private final JCheckBox             boldCheckBox;
    private final JCheckBox             italicCheckBox;
    private final JSpinner              sizeSpinner;
    private final SpinnerNumberModel    sizeModel;
    private final JTextField            fgColorTextField;
    private final JCheckBox             drawCheckBox;
    private final JSpinner              widthSpinner;
    private final SpinnerNumberModel    widthModel;
    private final JTextField            bgColorTextField;
    
    private JPanel getFontPanel()
    {
        assertNotNull( fontNames );
        Component   comp    = fontNames.getParent();
        assertTrue( comp instanceof JPanel );
        return (JPanel)comp;
    }
    
    private JComboBox<String> getFontNamesComboBox()
    {
        Predicate<JComponent>   pred    =
            c -> (c instanceof JComboBox);
        JComponent  comp    =
            ComponentFinder.find( this, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JComboBox );
        JComboBox<?>        comboBox    = (JComboBox)comp;
        assertTrue( comboBox.getSelectedItem() instanceof String );
        @SuppressWarnings("unchecked")
        JComboBox<String>   sComboBox   = (JComboBox<String>)comboBox;
        return sComboBox;
    }
    
    private JCheckBox getCheckBox( String text )
    {
        Predicate<JComponent>   isCheckBox  =
            c -> (c instanceof JCheckBox);
        Predicate<JComponent>   hasText     =
            c -> text.equals( ((JCheckBox)c).getText() );
        Predicate<JComponent>   pred        = isCheckBox.and( hasText );
        JComponent  comp    =
            ComponentFinder.find( this, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
        
    }
    
    private JSpinner getSizeSpinner()
    {
        assertNotNull( fontEditorPanel );
        Predicate<JComponent>   pred    = c -> (c instanceof JSpinner);
        JComponent  comp    =
            ComponentFinder.find( fontEditorPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JSpinner );
        return (JSpinner)comp;
        
    }
    
    private JTextField getFGColorTextField()
    {
        assertNotNull( fontEditorPanel );
        JTextField  field   =
            Stream.of( fontEditorPanel.getComponents() )
                .filter( c -> (c instanceof JTextField) )
                .map( c -> (JTextField)c )
                .findFirst().orElse( null );
        assertNotNull( field );
        return field;
    }
    
    private JSpinner getWidthSpinner()
    {
        Predicate<JComponent>   isPanel = c -> (c instanceof JPanel);
        Predicate<JComponent>   hasOne  = c -> 
            ((JPanel)c).getComponents().length == 1;
        Predicate<JComponent>   pred    =
            isPanel.and( hasOne );
        JComponent              comp    =
            ComponentFinder.find( this, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JPanel );
        Component[] comps   = comp.getComponents();
        assertTrue( comps.length == 1 );
        
        Component   comp2   = comps[0];
        assertTrue( comp2 instanceof JSpinner );
        return (JSpinner)comp2;
        
    }
    
    private List<PRadioButton<?>> getRadioButtons()
    {
        Predicate<JComponent>   isPanel = c -> (c instanceof JPanel);
        Predicate<JComponent>   hasOne  = c -> 
            ((JPanel)c).getComponents().length == 1;
        Predicate<JComponent>   pred    =
            isPanel.and( hasOne );
    }
}
