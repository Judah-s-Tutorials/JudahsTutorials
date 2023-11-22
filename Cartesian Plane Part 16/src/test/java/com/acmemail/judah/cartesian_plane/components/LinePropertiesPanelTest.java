package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.test_utils.LinePropertySetInitializer;

public class LinePropertiesPanelTest
{
    private static LinePropertySet  axesSetOrig;
    private static LinePropertySet  gridLinesSetOrig;
    private static LinePropertySet  ticMajorSetOrig;
    private static LinePropertySet  ticMinorSetOrig;
    
    private PButtonGroup<LinePropertySet>   buttonGroup;
    private SpinnerNumberModel      strokeModel;
    private SpinnerNumberModel      lengthModel;
    private SpinnerNumberModel      spacingModel;
    private JTextField              colorField;
    private JCheckBox               drawCheckBox;
    
    private JButton                 applyButton;
    private JButton                 resetButton;
    private JButton                 closeButton;
    private JDialog                 dialog;
    private LinePropertiesPanel     propPanel;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        LinePropertySetInitializer.initProperties();
        axesSetOrig = new LinePropertySetAxes();
        gridLinesSetOrig = new LinePropertySetGridLines();
        ticMajorSetOrig = new LinePropertySetTicMajor();
        ticMinorSetOrig = new LinePropertySetTicMinor();
    }
    
    public LinePropertiesPanelTest()
    {
        makeDialog();
    }

    @Test
    void test()
    {
    }

    private void makeDialog()
    {
        dialog = new JDialog();
        propPanel = new LinePropertiesPanel();
        dialog.setContentPane( propPanel );
        dialog.pack();
        dialog.setVisible( true );
        
        parseMiscPropertyComponents();
        parseSpinnerModels();
        parseButtons();
    }
    
    private void parseMiscPropertyComponents()
    {
        colorField = 
            Stream.of( propPanel.getComponents() )
                .filter( c -> (c instanceof JTextField) )
                .map( c -> (JTextField)c )
                .findFirst().orElse( null );
        assertNotNull( colorField );

        drawCheckBox = 
            Stream.of( propPanel.getComponents() )
                .filter( c -> (c instanceof JCheckBox) )
                .map( c -> (JCheckBox)c )
                .findFirst().orElse( null );
        assertNotNull( colorField );
    }
    
    private void parseSpinnerModels()
    {
        List<SpinnerNumberModel>    allModels   = 
            Stream.of( propPanel.getComponents() )
                .filter( c -> (c instanceof JSpinner) )
                .map( s -> ((JSpinner)s).getModel() )
                .filter( m -> (m instanceof SpinnerNumberModel) )
                .map( m -> (SpinnerNumberModel)m )
                .toList();
        assertEquals( 3, allModels.size() );
        strokeModel = allModels.get( 0 );
        lengthModel = allModels.get( 1 );
        spacingModel = allModels.get( 2 );
    }
    
    private void parseButtons()
    {
        applyButton = parseButton( "Apply" );
        resetButton = parseButton( "Reset" );
        closeButton = parseButton( "Close" );
    }
    
    private JButton parseButton( String text )
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( text );
        JComponent              comp    =
            ComponentFinder.find( propPanel , pred );
        assertNotNull( comp, text );
        assertTrue( comp instanceof JButton, text );
        return (JButton)comp;
    }
}
