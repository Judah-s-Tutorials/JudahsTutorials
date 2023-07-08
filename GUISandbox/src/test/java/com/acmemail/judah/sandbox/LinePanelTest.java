package com.acmemail.judah.sandbox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import static com.acmemail.judah.sandbox.PropertyManager.AXIS;
import static com.acmemail.judah.sandbox.PropertyManager.MAJOR;
import static com.acmemail.judah.sandbox.PropertyManager.MINOR;
import static com.acmemail.judah.sandbox.PropertyManager.GRID;

import static com.acmemail.judah.sandbox.PropertyManager.STROKE;
import static com.acmemail.judah.sandbox.PropertyManager.SPACING;
import static com.acmemail.judah.sandbox.PropertyManager.LENGTH;
import static com.acmemail.judah.sandbox.PropertyManager.COLOR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Predicate;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

class LinePanelTest
{
    private static final PropertyManager pMgr   = 
        PropertyManager.instanceOf();
    
    private JFrame          frame;
    private LinePanel       linePanel;
    
    private JRadioButton    axisRB;
    private JRadioButton    majorRB;
    private JRadioButton    minorRB;
    private JRadioButton    gridRB;
    
    private JLabel          strokeLabel;
    private JLabel          lengthLabel;
    private JLabel          spacingLabel;
    private JButton         colorButton;
    
    private JSpinner        strokeSpinner;
    private JSpinner        lengthSpinner;
    private JSpinner        spacingSpinner;
    private JTextField      colorField;
    
    private StrokeFeedback  strokeFB;
    private LengthFeedback  lengthFB;
    private SpacingFeedback spacingFB;
    private ColorFeedback   colorFB;
    
    private JRadioButton[]  allButtons;
    
    @Test
    void testLinePanel() 
    {
        postFrame();
        verifySelected( axisRB );
    }
    
    @Test
    public void testInitialValues()
    {
        postFrame();
        
        // Verify radio buttons are set to correct major category
        verifyMajorCategory( axisRB, AXIS );
        verifyMajorCategory( majorRB, MAJOR );
        verifyMajorCategory( minorRB, MINOR );
        verifyMajorCategory( gridRB, GRID );
        
        // Verify initial major category properties are correct
        verifyProperties( axisRB );
        verifyProperties( majorRB );
        verifyProperties( minorRB );
        verifyProperties( gridRB );
    }
    
    private void verifyMajorCategory( JRadioButton button, String expMajor )
    {
        LinePropertySet set = getPropertySet( button );
        assertEquals( expMajor, set.getMajorCategory() );
    }
    
    private void verifyProperties( JRadioButton button )
    {
        LinePropertySet set         = getPropertySet( button );
        String          majorCat    = set.getMajorCategory();
        OptionalDouble  stroke = 
            pMgr.getAsDouble( majorCat, STROKE );
        OptionalDouble  length = 
            pMgr.getAsDouble( majorCat, LENGTH );
        OptionalDouble  spacing = 
            pMgr.getAsDouble( majorCat, SPACING );
        Optional<Color> color = 
            pMgr.getAsColor( majorCat, COLOR );
        
        assertEquals( stroke.isPresent(), set.hasStroke() );
        assertEquals( spacing.isPresent(), set.hasSpacing() );
        assertEquals( length.isPresent(), set.hasLength() );
        assertEquals( color.isPresent(), set.hasColor() );
        
        if ( stroke.isPresent() )
            assertEquals( stroke.getAsDouble(), set.getStroke() );
        if ( spacing.isPresent() )
            assertEquals( spacing.getAsDouble(), set.getSpacing() );
        if ( length.isPresent() )
            assertEquals( length.getAsDouble(), set.getLength() );
    }
    
    private void verifySelected( JRadioButton expSelected )
    {
        assertTrue( expSelected.isSelected() );
        JRadioButton expNull    = Arrays.stream( allButtons )
            .filter( b -> b != expSelected )
            .filter( JRadioButton::isSelected )
            .findFirst()
            .orElse( null );
        assertNull( expNull );
        
        verifyPropertySet( expSelected );
    }
    
    private void verifyPropertySet( JRadioButton selected )
    {
        LinePropertySet set     = getPropertySet( selected );
        verifyStroke( set );
        verifyLength( set );
        verifySpacing( set );
        verifyColor( set );
    }
    
    private void verifyStroke( LinePropertySet set )
    {
        if ( !set.hasStroke() )
        {
            assertFalse( strokeLabel.isEnabled() );
            assertFalse( strokeSpinner.isEnabled() );
        }
        else
        {
            assertTrue( strokeLabel.isEnabled() );
            assertTrue( strokeSpinner.isEnabled() );
            assertEquals( 
                set.getStroke(), 
                (double)strokeSpinner.getValue() 
            );
        }
    }
    
    private void verifyLength( LinePropertySet set )
    {
        if ( !set.hasLength() )
        {
            assertFalse( lengthLabel.isEnabled() );
            assertFalse( lengthSpinner.isEnabled() );
        }
        else
        {
            assertTrue( lengthLabel.isEnabled() );
            assertTrue( lengthSpinner.isEnabled() );
            assertEquals(
                set.getLength(), 
                (double)lengthSpinner.getValue()
            );
        }
    }
    
    private void verifySpacing( LinePropertySet set )
    {
        if ( !set.hasSpacing() )
        {
            assertFalse( spacingLabel.isEnabled() );
            assertFalse( spacingSpinner.isEnabled() );
        }
        else
        {
            assertTrue( spacingLabel.isEnabled() );
            assertTrue( spacingSpinner.isEnabled() );
            assertEquals( 
                set.getSpacing(), 
                (double)spacingSpinner.getValue()
            );
        }
    }
    
    private void verifyColor( LinePropertySet set )
    {
        if ( !set.hasColor() )
        {
            assertFalse( colorButton.isEnabled() );
            assertFalse( colorField.isEnabled() );
        }
        else
        {
            assertTrue( colorButton.isEnabled() );
            assertTrue( colorField.isEnabled() );
            assertTrue( equals( set.getColor(), colorField.getText() ) );
        }
    }
    
    private boolean equals( Color color, String strColor )
    {
        boolean result      = false;
        int     intColor    = color.getRGB() & 0x00ffffff;
        try
        {
            int actColor    = Integer.decode( strColor );
            result = intColor == actColor;
        }
        catch ( NumberFormatException exc )
        {
            result = false;
        }
        return result;
    }

    private LinePropertySet getPropertySet( AbstractButton button )
    {
        Object  value   = button.getClientProperty( LinePanel.TYPE_KEY );
        assertNotNull( value );
        assertTrue( value instanceof LinePropertySet );
        return (LinePropertySet)value;
    }

    private void postFrame()
    {
        invokeAndWait( () -> buildFrame() );

        axisRB = getRadioButton( "Axes" );
        majorRB = getRadioButton( "Major Tics" );
        minorRB = getRadioButton( "Minor Tics" );
        gridRB = getRadioButton( "Grid Lines" );
        
        strokeLabel = getLabel( "Stroke" );
        lengthLabel = getLabel( "Length" );
        spacingLabel = getLabel( "Spacing" );
        colorButton = getButton( "Color" );

        strokeSpinner = getSpinner( LinePanel.STROKE_SPINNER_CN );
        lengthSpinner = getSpinner( LinePanel.LENGTH_SPINNER_CN );
        spacingSpinner = getSpinner( LinePanel.SPACING_SPINNER_CN );
        colorField = getTextField( LinePanel.COLOR_FIELD_CN );
        
        strokeFB = getByType( StrokeFeedback.class );
        lengthFB = getByType( LengthFeedback.class );
        spacingFB = getByType( SpacingFeedback.class );
        colorFB = getByType( ColorFeedback.class );
        
        allButtons = new JRadioButton[] { axisRB, majorRB, minorRB, gridRB };
    }

    private void invokeAndWait( Runnable runner )
    {
        try
        {
            SwingUtilities.invokeAndWait( runner );
        }
        catch ( InvocationTargetException | InterruptedException exc )
        {
            exc.printStackTrace();
            fail( "unexpected exception" );
        }
    }    
    private void buildFrame()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        
        linePanel = new LinePanel();
        contentPane.add( linePanel, BorderLayout.CENTER );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JRadioButton getRadioButton( String label )
    {
        final Predicate<JComponent> isRadioButton   = 
            jc -> (jc instanceof JRadioButton);
        Predicate<JComponent>   hasText             =
            jc -> hasName( (AbstractButton)jc, label );
        Predicate<JComponent>   pred                =
            isRadioButton.and( hasText );
        JComponent  comp    = ComponentFinder.find( linePanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JRadioButton );
        return (JRadioButton)comp;
    }
    
    private JLabel getLabel( String label )
    {
        final Predicate<JComponent> isLabel   = 
            jc -> (jc instanceof JLabel);
        Predicate<JComponent>   hasText             =
            jc -> hasName( (JLabel)jc, label );
        Predicate<JComponent>   pred                =
            isLabel.and( hasText );
        JComponent  comp    = ComponentFinder.find( linePanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JLabel );
        return (JLabel)comp;
    }
    
    private JButton getButton( String label )
    {
        final Predicate<JComponent> isLabel   = 
            jc -> (jc instanceof JButton);
        Predicate<JComponent>   hasText             =
            jc -> hasName( (JButton)jc, label );
        Predicate<JComponent>   pred                =
            isLabel.and( hasText );
        JComponent  comp    = ComponentFinder.find( linePanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
    }
    
    private JSpinner getSpinner( String name )
    {
        final Predicate<JComponent> isSpinner   = 
            jc -> (jc instanceof JSpinner);
        Predicate<JComponent>   hasName         =
            jc -> name.equals( jc.getName() );
        Predicate<JComponent>   pred                =
            isSpinner.and( hasName );
        JComponent  comp    = ComponentFinder.find( linePanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JSpinner );
        return (JSpinner)comp;
    }
    
    private JTextField getTextField( String name )
    {
        final Predicate<JComponent> isTextField = 
            jc -> (jc instanceof JTextField );
        Predicate<JComponent>   hasName         =
            jc -> name.equals( jc.getName() );
        Predicate<JComponent>   pred                =
            isTextField.and( hasName );
        JComponent  comp    = ComponentFinder.find( linePanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JTextField );
        return (JTextField)comp;
    }
    
    private <T extends JComponent> T getByType( Class<T> clazz )
    {
        Predicate<JComponent> isType   = jc -> (jc.getClass() == clazz);
        JComponent  comp    = ComponentFinder.find( linePanel, isType );
        assertNotNull( comp );
        return (T)comp;
    }
    
    private boolean hasName( JComponent comp, String text )
    {
        String  compText    = null;
        if ( comp instanceof AbstractButton)
            compText = ((AbstractButton)comp).getText();
        else if ( comp instanceof JLabel )
            compText = ((JLabel)comp).getText();
        else
            fail ( "Invalid component: " + comp.getClass().getName() );
        
        String  expText     = text.toUpperCase();
        if ( compText != null )
            compText = compText.toUpperCase();
        boolean result  = expText.equals( compText );
        return result;
    }
    
    private Color nextColor( Color colorIn )
    {
        int     intColorIn  = colorIn.getRGB();
        int     intColorOut = (intColorIn & 0xffffff00) + 1;
        Color   colorOut    = new Color( intColorOut );
        return colorOut;
    }
}
