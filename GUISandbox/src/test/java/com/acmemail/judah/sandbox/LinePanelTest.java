package com.acmemail.judah.sandbox;

import static com.acmemail.judah.sandbox.PropertyManager.AXES;
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
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Predicate;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.sandbox.sandbox.SandboxUtils;
import com.acmemail.judah.sandbox.test_utils.LineTestData;
import com.acmemail.judah.sandbox.test_utils.Utils;

class LinePanelTest
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
    
    private JButton         applyButton;
    private JButton         resetButton;
    
    private JRadioButton[]  allRButtons;
    
    /** Settings for AXES category, as of start-of-test. */
    private LinePropertySet initAxesSet;
    /** Settings for MAJOR category, as of start-of-test. */
    private LinePropertySet initMajorSet;
    /** Settings for MINOR category, as of start-of-test. */
    private LinePropertySet initMinorSet;
    /** Settings for GRID category, as of start-of-test. */
    private LinePropertySet initGridSet;
    
    /**
     *  The JColorChooser component of the 
     *  JColorChooser dialog. Set by the 
     *  {@linkplain #startColorChooserThread()} method.
     */
    private JColorChooser   chooser;
    /**
     *  The OK button component of the 
     *  JColorChooser dialog. Set by the 
     *  {@linkplain #startChooserThread()} method.
     */
    private JButton         okButton;
    /**
     *  The Cancel button component of the 
     *  JColorChooser dialog. Set by the 
     *  {@linkplain #startChooserThread()} method.
     */
    private JButton         cancelButton;
    /**
     *  The value returned by the JColorChooser dialog
     *  when it is dismissed Set by the 
     *  {@linkplain #startChooser()} method.
     */
    private Color           colorChoice;
    
    @BeforeEach
    public void beforeEach()
    {
        initAxesSet = new LinePropertySet( AXES );
        initMajorSet = new LinePropertySet( MAJOR );
        initMinorSet = new LinePropertySet( MINOR );
        initGridSet = new LinePropertySet( GRID );
    }
    
    @AfterEach
    public void afterEach()
    {
        Arrays.stream( Window.getWindows() )
            .peek( w -> invokeAndWait( () -> w.setVisible( false ) ) )
            .forEach( Window::dispose );
    }
    
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
        verifyMajorCategory( axisRB, AXES );
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
    
    @ParameterizedTest
    @ValueSource( strings= {AXES,MAJOR,MINOR,GRID} )
    public void testApply( String cat )
    {
        postFrame();
        JRadioButton    button  = getButtonByCategory( cat );
        
        LineTestData    testDataOrig    = new LineTestData( cat );
        testDataOrig.assertMapsTo( button );
        invokeAndWait( () -> button.doClick() );
        
        LineTestData    testDataNew     = testDataOrig.getUniqueData();
        setCurrInput( testDataNew );
        invokeAndWait( () -> applyButton.doClick() );
        verifyProperties( button );
    }
    
    @Test
    public void testColorDialogOK()
    {
        postFrame();
        invokeAndWait( () -> axisRB.doClick() );
        LineTestData    currData    = getCurrInput();
        LineTestData    newData     = currData.getUniqueData();
        Color           newColor    = Color.blue;//.getColor();
        
        Thread  thread  = startChooserThread();
        invokeAndWait( () -> chooser.setColor( newColor ) );
        invokeAndWait( () -> okButton.doClick() );
        Utils.join( thread );
        
        assertEquals( newColor, colorFB.getCurrColor() );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {AXES,MAJOR,MINOR,GRID} )
    public void testReset( String cat )
    {
        postFrame();
        JRadioButton    button   = getButtonByCategory( cat );
        doClick( button );
        
        // sanity check: input fields match data stored with RadioButton
        LineTestData    origInput   = getCurrInput();
        origInput.assertMapsTo( button );
        
        // create unique data, and set in input fields
        LineTestData    newInput    = origInput.getUniqueData();
        setCurrInput( newInput );
        
        // click any other RadioButton,
        // forcing new input to be stored with RadioButton
        if ( button != majorRB )
            doClick( majorRB );
        else
            doClick( minorRB );
        
        // sanity check: new input data stored in RadioButton
        newInput.assertMapsTo( button );

        // reset; verify original data recovered in RadioButton
        doClick( resetButton );
        origInput.assertMapsTo( button );
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
        assertEquals( expMajor, getMajorCategory( button ) );
    }
    
    private String getMajorCategory( JRadioButton button )
    {
        LinePropertySet set = LinePropertySet.getPropertySet( button );
        String          cat = set.getMajorCategory();
        return cat;
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
        JRadioButton expNull    = Arrays.stream( allRButtons )
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
        
        applyButton = getButton( "Apply" );
        resetButton = getButton( "Reset" );
        
        allRButtons = new JRadioButton[] { axisRB, majorRB, minorRB, gridRB };
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
        Predicate<JComponent>   pred    =
            ComponentFinder.getButtonPredicate( label );
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
            Arrays.stream( allRButtons )
            .filter( JRadioButton::isSelected )
            .findFirst()
            .orElse( null );
        assertNotNull( selected, "No radio button selected" );
        return selected;
    }
    
    private void setCurrInput( LineTestData data )
    {
        if ( data.hasStroke() )
            setCurrInput( strokeSpinner, data.getStroke() );
        if ( data.hasLength() )
            setCurrInput( lengthSpinner, data.getLength() );
        if ( data.hasSpacing() )
            setCurrInput( spacingSpinner, data.getSpacing() );
        if ( data.hasColor() )
            setCurrInput( colorField, "" + data.getColor().getRGB() );
    }
    
    private void setCurrInput( JSpinner spinner, double val )
    {
        invokeAndWait( () -> spinner.setValue( val ) );
    }
    
    private void setCurrInput( JTextField field, String val )
    {
        invokeAndWait( () -> field.setText( val ) );
    }
    
    private void doClick( AbstractButton button )
    {
        invokeAndWait( () -> button.doClick() );
    }
    
    private JRadioButton getButtonByCategory( String category )
    {
        JRadioButton    button  =
            Arrays.stream( allRButtons )
                .filter( rb -> category.equals( getMajorCategory( rb ) ) )
                .findFirst()
                .orElse( null );
        assertNotNull( button );
        return button;
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
    
    /**
     * Starts a thread to display the JColorChooser dialog.
     * Interrogates the dialog after it's displayed,
     * and obtains the components needed
     * for programmatic interaction.
     * 
     * @return  the new Thread
     */
    private Thread startChooserThread() 
    {
        Runnable    runner  = 
            () -> invokeAndWait( () -> colorButton.doClick() );
        String      name    = "ChooserThread";
        Thread  thread      = new Thread( runner, name );
        thread.start();
        SandboxUtils.pause( 500 );
        
        // Assumption: color chooser dialog is the only JDialog
        // that is presently visible.
        ComponentFinder finder  = new ComponentFinder( true, false, true );
        Window  colorDialog = finder.findWindow( w -> true );
        assertNotNull( colorDialog );
        
        Predicate<JComponent>   pred    = jc -> (jc instanceof JColorChooser);
        chooser = (JColorChooser)ComponentFinder.find( colorDialog, pred );
        assertNotNull( chooser );
        
        pred = ComponentFinder.getButtonPredicate( "OK" );
        okButton = (JButton)ComponentFinder.find( colorDialog, pred );
        assertNotNull( okButton );
        
        pred = ComponentFinder.getButtonPredicate( "Cancel" );
        cancelButton = (JButton)ComponentFinder.find( colorDialog, pred );
        assertNotNull( cancelButton );
        
        return thread;
    }
    
    /**
     * Helper method to obtain the JColorChooser component
     * of the JColorChooser dialog.
     * 
     * @return  the JColorChooser component of the JColorChooser dialog.
     * 
     * @throws ComponentException if the operation fails
     */
    private JColorChooser getColorChooser()
    {
        ComponentFinder finder  = new ComponentFinder( true, false, true );
        JComponent  comp    = 
            finder.find( c -> (c instanceof JColorChooser) );
        if ( comp == null || !(comp instanceof JColorChooser) ) 
            throw new ComponentException( "JColorChooser not found" );
        return (JColorChooser)comp;
    }
}
