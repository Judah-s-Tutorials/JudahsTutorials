package com.acmemail.judah.sandbox;

import static com.acmemail.judah.sandbox.PropertyManager.AXIS;
import static com.acmemail.judah.sandbox.PropertyManager.GRID;
import static com.acmemail.judah.sandbox.PropertyManager.MAJOR;
import static com.acmemail.judah.sandbox.PropertyManager.MINOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
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

import com.acmemail.judah.sandbox.test_utils.LineTestData;

class LinePanelTestOrig
{
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
    
    @Test
    public void testSelection()
    {
        postFrame();
        
        // Assume that "Axes" is initially selected
        testSelection( majorRB );
        testSelection( minorRB );
        testSelection( gridRB );
        testSelection( axisRB );
    }
    
    private void testSelection( JRadioButton nextSelected )
    {
        // Make sure all new data has been entered for the currently 
        // selected button.
        JRadioButton    origSelected    = getSelectedButton();
        System.out.println( "O: " + origSelected.getText() + ", N: " + nextSelected.getText() );
        LineTestData    oldData         = LineTestData.of( origSelected );
        LineTestData    newData         = oldData.getUniqueData();
        setCurrInput( newData );
        
        // Select the next button. Verify that data for the previously
        // selected button has been committed, and data from the
        // newly selected button has been used to update the UI.
        invokeAndWait( () -> nextSelected.doClick() );
        LineTestData    currData       = getCurrInput();
        newData.assertMapsTo( origSelected );
        currData.assertMapsTo( nextSelected );
    }
    
    @Test
    public void testSelectMajorTics() throws InterruptedException
    {
        postFrame();
        testSelectCategory( majorRB );
    }
    
    @Test
    public void testSelectMinorTics() throws InterruptedException
    {
        postFrame();
        testSelectCategory( minorRB );
    }
    
    @Test
    public void testSelectGrid() throws InterruptedException
    {
        postFrame();
        testSelectCategory( gridRB );
    }
    
    @Test
    public void testApplyAxis()
    {
        postFrame();
    }
    
    private void testSelectCategory( JRadioButton button )
    {
        invokeAndWait( () -> button.doClick() );
        verifySelected( button );
    }
    
    private void verifyMajorCategory( JRadioButton button, String expMajor )
    {
        LinePropertySet set = LinePropertySet.getPropertySet( button );
        assertEquals( expMajor, set.getMajorCategory() );
    }
    
    private void verifyProperties( JRadioButton button )
    {
        LineTestData    data    = LineTestData.of( button );
        LinePropertySet set     = LinePropertySet.getPropertySet( button );
        data.assertMapsTo( set );
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
        LinePropertySet set = LinePropertySet.getPropertySet( selected );
        LineTestData    data    =
            new LineTestData(
                strokeSpinner,
                lengthSpinner,
                spacingSpinner,
                colorField
            );
        data.assertMapsTo( set );
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

    private void buildFrame()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        
        linePanel = new LinePanel();
        contentPane.add( linePanel, BorderLayout.CENTER );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }

    private void invokeAndWait( Runnable runner )
    {
        try
        {
            SwingUtilities.invokeAndWait( runner );
            Thread.sleep( 1000 );
        }
        catch ( InvocationTargetException | InterruptedException exc )
        {
            exc.printStackTrace();
            fail( exc );
        }
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
    
    @SuppressWarnings("unchecked")
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
    
    private JRadioButton getSelectedButton()
    {
        JRadioButton    selected    =
            Arrays.stream( allButtons )
            .filter( JRadioButton::isSelected )
            .findFirst()
            .orElse( null );
        assertNotNull( selected, "No radio button selected" );
        return selected;
    }
    
    private void setCurrInput( LineTestData data )
    {
        if ( data.hasStroke() )
            strokeSpinner.setValue( data.getStroke() );
        if ( data.hasLength() )
            lengthSpinner.setValue( data.getLength() );
        if ( data.hasSpacing() )
            strokeSpinner.setValue( data.getSpacing() );
        if ( data.hasColor() )
            colorField.setText( "" + data.getColor().getRGB() );
    }
    
    private LineTestData getCurrInput()
    {
        LineTestData    data    =
            new LineTestData(
                strokeSpinner,
                lengthSpinner,
                spacingSpinner,
                colorField
            );
        return data;
    }
}
