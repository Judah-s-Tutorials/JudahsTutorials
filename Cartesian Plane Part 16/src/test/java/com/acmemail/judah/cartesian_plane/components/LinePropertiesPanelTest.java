package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.awt.Container;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.LinePropertySetInitializer;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LinePropertiesPanelTest
{
    private final LPSMap    setMapOrig  = new LPSMap();
    private final LPSMap    setMapNew   = new LPSMap();
    
    private List<PRadioButton<LinePropertySet>>      
                                    radioButtons;
    private JSpinner                strokeSpinner;
    private SpinnerNumberModel      strokeModel;
    private JSpinner                lengthSpinner;
    private SpinnerNumberModel      lengthModel;
    private JSpinner                spacingSpinner;
    private SpinnerNumberModel      spacingModel;
    private JButton                 colorButton;
    private JTextField              colorField;
    private JCheckBox               drawCheckBox;
    
    private JButton                 applyButton;
    private JButton                 resetButton;
    private JButton                 closeButton;
    private JDialog                 dialog;
    private LinePropertiesPanel     propPanel;
    private JPanel                  propCompPanel;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        LinePropertySetInitializer.initProperties();
    }
    
    public LinePropertiesPanelTest()
    {
        makeDialog();
        
        setMapOrig.put( new LinePropertySetAxes() );
        setMapOrig.put( new LinePropertySetGridLines() );
        setMapOrig.put( new LinePropertySetTicMajor() );
        setMapOrig.put( new LinePropertySetTicMinor() );
        
        setMapNew.put( new LinePropertySetAxes() );
        setMapNew.put( new LinePropertySetGridLines() );
        setMapNew.put( new LinePropertySetTicMajor() );
        setMapNew.put( new LinePropertySetTicMinor() );
    }

    @Order( 5 )
    @Test
    void testInit()
    {
        GUIUtils.schedEDTAndWait( () -> {
            assertAllSetsSynched();
        });
    }
    
    @Order( 10 )
    @Test
    public void 
    testSynchOnSelected()
    {
        radioButtons.stream()
            // select given radio button
            .peek( b -> doClick( b ) )
            // get the encapsulated LinePropertySet
            .map( b -> b.get() )
            // verify that GUI is synched with selected button
            .forEach( s -> assertSetSynched( s ) );
    }
    
    @Order( 15 )
    @Test
    public void 
    testSynchOnDeselected()
    {
        for ( PRadioButton<LinePropertySet> button : radioButtons )
        {
            doClick( button );
            LinePropertySet currSet = button.get();
            LinePropertySet newSet  = setMapNew.get( currSet );
            if ( newSet.hasStroke() )
                strokeModel.setValue( newSet.getStroke() );
            if ( newSet.hasLength() )
                lengthModel.setValue( newSet.getLength() );
            if ( newSet.hasSpacing() )
                spacingModel.setValue( newSet.getSpacing() );
            if ( newSet.hasColor() )
                setHexColor( newSet.getColor() );
            if ( newSet.hasDraw() )
                drawCheckBox.setSelected( newSet.getDraw() );
            selectOther();
            assertEqualsSet( newSet, currSet );
        }
    }
    
    private void doClick( AbstractButton button )
    {
        try
        {
            GUIUtils.schedEDTAndWait( () -> button.doClick() );
        }
        catch ( RuntimeException exc )
        {
            String  text    = button.getText();
            fail( "doClick() failed on " + text, exc );
        }
    }
    
    private void selectOther()
    {
        PRadioButton    other   =
            radioButtons.stream()
                .filter( rb -> !rb.isSelected() )
                .findFirst().orElse( null );
        assertNotNull( other );
        doClick( other );
    }
    
    private void setHexColor( Color color )
    {
        GUIUtils.schedEDTAndWait( () -> {
            String  hexColor    = Integer.toHexString( color.getRGB() );
            colorField.setText( hexColor );
            colorField.postActionEvent();
        });
    }

    private void makeDialog()
    {
        dialog = new JDialog();
        propPanel = new LinePropertiesPanel();
        dialog.setContentPane( propPanel );
        dialog.pack();
        dialog.setVisible( true );
        
        getPropControlPanel();
        parseMiscPropertyComponents();
        parseSpinners();
        parseJButtons();
        parseRButtons();
    }
    
    /**
     * Find the JPanel that contains
     * the property components,
     * e.g. the stroke spinner or length spinner.
     * Assumption: all spinners
     * are children of a single panel.
     * <p>
     * Strategy:
     * find a spinner and get its parent.
     */
    private void getPropControlPanel()
    {
        Predicate<JComponent>   pred    = c -> (c instanceof JSpinner);
        JComponent              comp    =
            ComponentFinder.find( propPanel, pred );
        
        assertNotNull( comp );
        assertTrue( comp instanceof JSpinner );
        Container               parent  = comp.getParent();
        assertNotNull( parent );
        assertTrue( parent instanceof JPanel );
        propCompPanel = (JPanel)parent;
    }
    
    private void parseMiscPropertyComponents()
    {
        colorField = 
            Stream.of( propCompPanel.getComponents() )
                .filter( c -> (c instanceof JTextField) )
                .map( c -> (JTextField)c )
                .findFirst().orElse( null );
        assertNotNull( colorField );

        drawCheckBox = 
            Stream.of( propCompPanel.getComponents() )
                .filter( c -> (c instanceof JCheckBox) )
                .map( c -> (JCheckBox)c )
                .findFirst().orElse( null );
        assertNotNull( colorField );
    }
    
    private void parseSpinners()
    {
        List<JSpinner>  allSpinners   = 
            Stream.of( propCompPanel.getComponents() )
                .filter( c -> (c instanceof JSpinner) )
                .map( c -> (JSpinner)c )
                .toList();
        assertEquals( 3, allSpinners.size() );
        strokeSpinner = allSpinners.get( 0 );
        lengthSpinner = allSpinners.get( 1 );
        spacingSpinner = allSpinners.get( 2 );
        strokeModel = (SpinnerNumberModel)strokeSpinner.getModel();
        lengthModel = (SpinnerNumberModel)lengthSpinner.getModel();
        spacingModel = (SpinnerNumberModel)spacingSpinner.getModel();
    }
    
    private void parseJButtons()
    {
        applyButton = parseJButton( "Apply" );
        resetButton = parseJButton( "Reset" );
        closeButton = parseJButton( "Close" );
        colorButton = parseJButton( "Color" );
    }
    
    private JButton parseJButton( String text )
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( text );
        JComponent              comp    =
            ComponentFinder.find( propPanel , pred );
        assertNotNull( comp, text );
        assertTrue( comp instanceof JButton, text );
        return (JButton)comp;
    }
    
    private void parseRButtons()
    {
        radioButtons =
            Stream.of( "Axes", "Major Tics", "Minor Tics", "Grid Lines" )
                .map( this::parseRButton )
                .toList();
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
            ComponentFinder.find( propPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof PRadioButton<?> );
        PRadioButton<?> testButton  = (PRadioButton<?>)comp;
        assertTrue( testButton.get() instanceof LinePropertySet );
        
        PRadioButton<LinePropertySet>   button  =
            (PRadioButton<LinePropertySet>)testButton;
        return button;
    }
    
    private static void 
    initPropertySet( LinePropertySet set, int base )
    {
        if ( set.hasDraw() )
            set.setDraw( !set.getDraw() );
        if ( set.hasStroke() )
            set.setStroke( set.getStroke() + base );
        if ( set.hasLength() )
            set.setLength( set.getLength() + base + 5 );
        if ( set.hasSpacing() )
            set.setLength( set.getSpacing() + base + 10 );
        if ( set.hasColor() )
        {   
            int     rgb     = set.getColor().getRGB() + base + 15;
            set.setColor( new Color( rgb ) );
        }
        set.apply();
        assertEqualsSet( set, newInstance( set ) );
    }
    
    private static LinePropertySet 
    newLinePropertySet( LinePropertySet setIn )
    {
        LinePropertySet         setOut  = newInstance( setIn );
        
        if ( setIn.hasDraw() )
            setOut.setDraw( !setIn.getDraw() );
        if ( setIn.hasStroke() )
            setOut.setStroke( setIn.getStroke() + 5 );
        if ( setIn.hasLength() )
            setOut.setLength( setIn.getLength() + 10 );
        if ( setIn.hasSpacing() )
            setOut.setLength( setIn.getSpacing() + 15 );
        if ( setIn.hasColor() )
        {   
            int     rgb     = setIn.getColor().getRGB() + 20;
            setOut.setColor( new Color( rgb ) );
        }
        
        return setOut;
    }
    
    private static void 
    assertEqualsSet( LinePropertySet set1, LinePropertySet set2 )
    {
        boolean hasDraw = set1.hasDraw();
        boolean hasLength = set1.hasLength();
        boolean hasSpacing = set1.hasSpacing();
        boolean hasStroke = set1.hasStroke();
        boolean hasColor = set1.hasColor();
        
        assertEquals( hasDraw, set2.hasDraw() );
        assertEquals( hasLength, set2.hasLength() );
        assertEquals( hasSpacing, set2.hasSpacing() );
        assertEquals( hasStroke, set2.hasStroke() );
        assertEquals( hasColor, set2.hasColor() );
        
        if ( hasDraw )
            assertEquals( set1.getDraw(), set2.getDraw() );
        if ( hasLength )
            assertEquals( set1.getLength(), set2.getLength() );
        if ( hasSpacing )
            assertEquals( set1.getSpacing(), set2.getSpacing() );
        if ( hasStroke )
            assertEquals( set1.getStroke(), set2.getStroke() );
        if ( hasColor )
            assertEquals( set1.getColor(), set2.getColor() );
    }
    
    private void assertAllSetsSynched()
    {
        radioButtons.stream()
            .peek( b -> b.doClick() )
            .map( b -> b.get() )
            .peek( s -> Utils.pause( 1000 ) )
            .map( s -> (LinePropertySet)s )
            .forEach( s -> assertSetSynched( s ) );
    }
    
    private void assertSetSynched( LinePropertySet set )
    {
        boolean hasDraw = set.hasDraw();
        boolean hasLength = set.hasLength();
        boolean hasSpacing = set.hasSpacing();
        boolean hasStroke = set.hasStroke();
        boolean hasColor = set.hasColor();
        
        assertEquals( hasDraw, drawCheckBox.isEnabled() );
        assertEquals( hasStroke, strokeSpinner.isEnabled() );
        assertEquals( hasLength, lengthSpinner.isEnabled() );
        assertEquals( hasSpacing, drawCheckBox.isEnabled() );
        assertEquals( hasColor, colorButton.isEnabled() );
        assertEquals( hasColor, colorField.isEnabled() );
        
        if ( hasStroke )
            assertEquals( set.getStroke(),  floatValue( strokeModel ) );
        if ( hasLength )
            assertEquals( set.getLength(), floatValue( lengthModel ) );
        if ( hasSpacing )
            assertEquals( set.getSpacing(),  floatValue( spacingModel ) );
        if ( hasColor )
            assertEqualsColor( set.getColor(), colorField.getText() );
        if ( hasDraw )
            assertEquals( set.getDraw(), drawCheckBox.isSelected() );
    }
    
    /**
     * Create a line property set
     * and save to it
     * all the current values
     * of the GUI components.
     * 
     * @return LinePropertySet reflecting current GUI properties
     */
    private LinePropertySet saveProperties()
    {
        LinePropertySet configSet   = getSelectedButton().get();
        LinePropertySet saveSet     = newInstance( configSet );
        if ( saveSet.hasStroke() )
            saveSet.setStroke( floatValue( strokeModel ) );
        if ( saveSet.hasLength() )
            saveSet.setLength( floatValue( lengthModel ) );
        if ( saveSet.hasSpacing() )
            saveSet.setSpacing( floatValue( spacingModel ) );
        if ( saveSet.hasColor() )
            saveSet.setColor( getCurrColor() );
        return saveSet;
    }
    
    private PRadioButton<LinePropertySet> getSelectedButton()
    {
        PRadioButton<LinePropertySet>   selectedButton  =
            radioButtons.stream()
                .filter( b -> b.isSelected() )
                .map( b -> (PRadioButton<LinePropertySet>)b )
                .findFirst().orElse( null );
        assertNotNull( selectedButton );
        return selectedButton;
    }
    
    private Color getCurrColor()
    {
        Color   color   = null;
        try
        {
            String  strColor    = colorField.getText();
            int     intColor    = Integer.decode( strColor ) & 0xFFFFFF;
            color = new Color( intColor );
            
        }
        catch ( NumberFormatException exc )
        {
            fail( "Invalid color string", exc );
        }
        
        return color;
    }
    
    private static void 
    assertEqualsColor( Color color, String strColor )
    {
        try
        {
            int expColor    = color.getRGB() & 0xFFFFFF;
            int actColor    = Integer.decode( strColor ) & 0xFFFFFF;
            assertEquals( expColor, actColor );
            
        }
        catch ( NumberFormatException exc )
        {
            fail( "Invalid color string", exc );
        }
    }
    
    private static float floatValue( SpinnerNumberModel model )
    {
        return model.getNumber().floatValue();
    }
    
    private static LinePropertySet newInstance( LinePropertySet setIn )
    {
        @SuppressWarnings("unchecked")
        Class<LinePropertySet>  clazz   = 
            (Class<LinePropertySet>)setIn.getClass();
        LinePropertySet         setOut  = null;
        try
        {
            Constructor<LinePropertySet>    ctor    = 
                clazz.getConstructor();
            Object      inst    = ctor.newInstance();
            assertTrue( inst instanceof LinePropertySet );
            setOut = (LinePropertySet)inst;
        }
        catch ( 
            NoSuchMethodException 
            | InvocationTargetException
            | IllegalAccessException 
            | InstantiationException exc )
        {
            String  msg = "New LinePropertySet not instantiated.";
            fail( msg, exc );
        }
        
        return setOut;
    }
    
    private static class LPSMap extends 
        HashMap<Class<?>, LinePropertySet>
    {
        public void put( LinePropertySet set )
        {
            put( set.getClass(), set );
        }

        public LinePropertySet get( LinePropertySet setIn )
        {
            LinePropertySet setOut  = get( setIn.getClass() );
            assertNotNull( setOut );
            return setOut;
        }
    }
}
