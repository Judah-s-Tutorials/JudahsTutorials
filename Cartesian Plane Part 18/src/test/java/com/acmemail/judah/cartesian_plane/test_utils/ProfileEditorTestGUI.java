package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Robot;
import java.awt.Window;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.components.ProfileEditor;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * An instance of this class
 * is used to display and manage a ParameterPanel.
 * Interaction with the ParameterPanel
 * is conducted via operations
 * performed on the EDT.
 * 
 * @author Jack Straub
 */
public class ProfileEditorTestGUI
{
    private static final String graphSet        =
        GraphPropertySetMW.class.getSimpleName();
    private static final String axesSet         =
        LinePropertySetAxes.class.getSimpleName();
    private static final String gridLinesSet    =
        LinePropertySetGridLines.class.getSimpleName();
    private static final String ticMajorSet     =
        LinePropertySetTicMajor.class.getSimpleName();
    private static final String ticMinorSet     =
        LinePropertySetTicMinor.class.getSimpleName();

    private static final String gridTitle       = "Grid";
    private static final String axesTitle       = "Axes";
    private static final String gridLinesTitle  = "Grid Lines";
    private static final String ticMajorTitle   = "Major Tics";
    private static final String ticMinorTitle   = "Minor Tics";
    
    private static final String nameLabel       = "Name";
    private static final String spacingLabel    = "Lines/Unit";
    private static final String lengthLabel     = "Lines/Unit";
    private static final String weightLabel     = "Weight";
    private static final String drawLabel       = "Draw";
    
    private static final String fontDialogTitle = "Font Editor";
    private static final String boldLabel       = "Bold";
    private static final String italicLabel     = "Italic";
    private static final String sizeLabel       = "Size";
    private static final String okLabel         = "OK";
    private static final String resetLabel      = "Reset";
    private static final String cancelLabel     = "Cancel";
    
    private static final String gridUnitLabel   = "Grid Unit";
    private static final String editFontLabel   = "Edit Font";
    private static final String labelsLabel     = "Labels";
    
    /** The GUI test object. */
    private static ProfileEditorTestGUI testGUI;
    /* The Profile encapsulated by the ProfileEditor under test. */
    private final Profile               profile;
    /** The application frame that displays the ProfileEditor. */
    private final JFrame                editorFrame;
    /** The ProfileEditor under test. */
    private final ProfileEditor         profileEditor;
    private final Map<String,String>    titlePropSetMap =
        Map.of( 
            gridTitle, graphSet,
            axesTitle, axesSet,
            gridLinesTitle, gridLinesSet,
            ticMajorTitle, ticMajorSet,
            ticMinorTitle, ticMinorSet
        );
    private final Map<String,Class<?>>  titlePropSetMapA =
        Map.of( 
            gridTitle, GraphPropertySet.class,
            axesTitle, LinePropertySetAxes.class,
            gridLinesTitle, LinePropertySetGridLines.class,
            ticMajorTitle, LinePropertySetTicMajor.class,
            ticMinorTitle, LinePropertySetTicMinor.class
        );
    private final Map<String,JPanel>    propSetPanelMap = new HashMap<>();
    private final GraphPropertyComponents   graphPropertyComponents;
    private final Map<String, LinePropertyComponents>   
        propSetToCompMap    = new HashMap<>();
    private final JTextField            nameComponent;
    
    /** For simulating key strokes and mouse button pushes. */
    private final RobotAssistant    robotAsst;
    /** Robot object from RobotAssistant. */
    private final Robot             robot;
    
    /** 
     * For transient use in lambdas.
     * See, for example, {@link #getValue(Supplier)}.
     */
    private Object  adHocObject = null;
    
    /**
     * Instantiates and returns a ProfileEditorTestGUI.
     * May be invoked from within the EDT.
     * If not invoked from the EDIT,
     * instantiation is scheduled via a task on the EDT.
     * 
     * @param profile   
     *      the profile to be encapsulated in the ProfileEditor
     *      
     * @return the instantiated ProfileEditorTestGUI
     */
    public static ProfileEditorTestGUI getTestGUI( Profile profile )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            testGUI = new ProfileEditorTestGUI( profile );
        else
            GUIUtils.schedEDTAndWait( () -> 
                testGUI = new ProfileEditorTestGUI( profile )
            );
        return testGUI;
    }
    
    /**
     * Constructor.
     * Fully initializes this ProfileEditorTestGUI.
     * 
     * @param profile   Profile to install in the ProfileEditor
     */
    private ProfileEditorTestGUI( Profile profile )
    {
        this.profile = profile;
        editorFrame = new JFrame( "ProfileEditor Test GUI" );
        JPanel      contentPane     = new JPanel( new BorderLayout() );
        profileEditor = new ProfileEditor( profile );
        contentPane.add( profileEditor, BorderLayout.CENTER );
        
        editorFrame.setContentPane( contentPane );
        editorFrame.pack();
        editorFrame.setVisible( true );
        
        getAllTitledPanels( profileEditor );
        getAllLinePropertyComponents();
        graphPropertyComponents = new GraphPropertyComponents();
        nameComponent = getLabeledJTextField( nameLabel, profileEditor );
        
        robotAsst = makeRobot();
        robot = robotAsst.getRobot();
        robot.setAutoDelay( 100 );
    }
    
    public void setName( String name )
    {
        GUIUtils.schedEDTAndWait( () -> nameComponent.setText( name ) );
    }

    public String getName()
    {
        String  name    = getStringValue( () -> nameComponent.getText() );
        return name;
    }
    
    public void setGridUnit( float value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropertyComponents.setGridUnit( value ) 
        );
    }

    public float getGridUnit()
    {
        float   value   = getFloatValue( () -> 
            graphPropertyComponents.getGridUnit()
        );
        return value;
    }

    public String getFontName()
    {
        String  value   = getStringValue( () -> 
            graphPropertyComponents.getFontName()
        );
        return value;
    }

    public void setFontName( String value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropertyComponents.setFontName( value ) 
        );
    }

    public float getFontSize()
    {
        float   value   = getFloatValue( () -> 
            graphPropertyComponents.getFontSize()
        );
        return value;
    }

    public void setFontSize( float value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropertyComponents.setFontSize( value ) 
        );
    }

    public boolean isFontBold()
    {
        boolean value   = getBooleanValue( () -> 
            graphPropertyComponents.isBold()
        );
        return value;
    }

    public void setFontBold( boolean value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropertyComponents.setBold( value ) 
        );
    }

    public boolean isFontItalic()
    {
        boolean value   = getBooleanValue( () -> 
            graphPropertyComponents.isItalic()
        );
        return value;
    }

    public void setFontItalic( boolean value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropertyComponents.setItalic( value ) 
        );
    }

    public boolean isFontDraw()
    {
        boolean value   = getBooleanValue( () -> 
            graphPropertyComponents.isFontDraw()
        );
        return value;
    }

    public void setFontDraw( boolean value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropertyComponents.setFontDraw( value ) 
        );
    }

    public float getFontColor()
    {
        int     value   = getIntValue( () -> 
            graphPropertyComponents.getColor()
        );
        return value;
    }

    public void setFontColor( int value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropertyComponents.setColor( value ) 
        );
    }

    public boolean isDrawLabels()
    {
        boolean value   = getBooleanValue( () -> 
            graphPropertyComponents.isFontDraw()
        );
        return value;
    }

    public void setDrawLabels( boolean value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropertyComponents.setFontDraw( value ) 
        );
    }
    
    public Thread editFont()
    {
        Thread  thread  = graphPropertyComponents.startFontEditor();
        return thread;
    }
    
    public void selectFDOK()
    {
        graphPropertyComponents.selectOK();
    }
    
    public void selectFDReset()
    {
        graphPropertyComponents.selectReset();
    }
    
    public void selectFDCancel()
    {
        graphPropertyComponents.selectCancel();
    }
    
    private void setValue( Consumer<Float> consumer, float value )
    {
        GUIUtils.schedEDTAndWait( () -> consumer.accept( value ) );
    }
    
    private void setValue( Consumer<String> consumer, String value )
    {
        GUIUtils.schedEDTAndWait( () -> consumer.accept( value ) );
    }
    
    private boolean getBooleanValue( Supplier<Object> supplier )
    {
        Object  oVal    = getValue( supplier );
        assertTrue( oVal instanceof Boolean );
        return (boolean)oVal;
    }
    
    private float getFloatValue( Supplier<Object> supplier )
    {
        Object  oVal    = getValue( supplier );
        assertTrue( oVal instanceof Float );
        return (float)oVal;
    }
    
    private int getIntValue( Supplier<Object> supplier )
    {
        Object  oVal    = getValue( supplier );
        assertTrue( oVal instanceof Integer );
        return (int)oVal;
    }
    
    private String getStringValue( Supplier<Object> supplier )
    {
        Object  oVal    = getValue( supplier );
        assertTrue( oVal instanceof String );
        return (String)oVal;
    }
    
    private void setValue( Consumer<Object> consumer, Object value )
    {
        GUIUtils.schedEDTAndWait( () -> consumer.accept( value ) );
    }
    
    private Object getValue( Supplier<Object> supplier )
    {
        GUIUtils.schedEDTAndWait( () -> 
            adHocObject = supplier.get()
        );
        return adHocObject;
    }
    
    private void getAllTitledPanels( Container source )
    {
        if ( source instanceof JPanel )
        {
            JPanel  panel   = (JPanel)source;
            String  title   = getBorderTitle( panel.getBorder() );
            if ( title != null )
            {
                String  propSet = titlePropSetMap.get( title );
                assertNotNull( propSet );
                propSetPanelMap.put( propSet, panel );
            }
        }
        Arrays.stream( source.getComponents() )
            .filter( c -> c instanceof Container )
            .map( c -> (Container)c )
            .forEach( c -> getAllTitledPanels( c ) );
    }
    
    private String getBorderTitle( Border border )
    {
        String  title   = null;
        if ( border instanceof TitledBorder )
        {
            title = ((TitledBorder)border).getTitle();
        }
        else if ( border instanceof CompoundBorder )
        {
            CompoundBorder  compBorder  = (CompoundBorder)border;
            Border          inside      = compBorder.getInsideBorder();
            Border          outside     = compBorder.getOutsideBorder();
            if ( (title = getBorderTitle( inside )) == null )
                title = getBorderTitle( outside );
        }
        return title;
    }
    
    private void getAllGraphPropertyComponents()
    {
        
    }
    
    private void getAllLinePropertyComponents()
    {
        Stream.of(
            new LinePropertySetAxes(),
            new LinePropertySetGridLines(),
            new LinePropertySetTicMajor(),
            new LinePropertySetTicMinor()
        ).forEach( LinePropertyComponents::new );
    }
    
    /**
     * Within the given source,
     * find the JLabel with the given text.
     * Interrogate the JLabel's parent
     * for the first JComponent with the given type.
     * Throws assertion if the target JComponent
     * cannot be found.
     * 
     * @param text      the given text
     * @param source    the given source
     * @param type      the given type
     * 
     * @return 
     *      the first JComponent of the given type
     *      that is a sibling of the JLabel 
     *      with the given text
     */
    private JComponent 
    getComponentType( JComponent source, Class<? extends JComponent> type )
    {
        Predicate<JComponent>   pred    = 
            c -> type.isAssignableFrom( c.getClass() );
        JComponent              comp    = 
            ComponentFinder.find( source, pred );
        assertNotNull( comp );
        assertTrue( type.isAssignableFrom( comp.getClass() ) );
        return comp;
    }
    
    /**
     * Within the given source,
     * find the JLabel with the given text.
     * Interrogate the JLabel's parent
     * for the first JComponent with the given type.
     * Throws assertion if the target JComponent
     * cannot be found.
     * 
     * @param text      the given text
     * @param source    the given source
     * @param type      the given type
     * 
     * @return 
     *      the first JComponent of the given type
     *      that is a sibling of the JLabel 
     *      with the given text
     */
    private JComponent getLabeledComponent( 
        String text, 
        JComponent source, 
        Class<? extends JComponent> type
    )
    {
        Predicate<JComponent>   isLabel = c -> (c instanceof JLabel);
        Predicate<JComponent>   hasText = c -> 
            text.equals( ((JLabel)c).getText() );
        Predicate<JComponent>   pred    = isLabel.and( hasText );
        JComponent              comp    = 
            ComponentFinder.find( source, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JLabel );
        
        Container   parent  = comp.getParent();
        JComponent  target  = Arrays.stream( parent.getComponents() )
            .filter( c -> (c instanceof JComponent) )
            .map( c -> (JComponent)c )
            .filter( jc -> type.isAssignableFrom( jc.getClass() ) )
            .findFirst().orElse( null );
        assertNotNull( target );
        return target;
    }
    
    private JSpinner getLabeledJSpinner( String text, JComponent source )
    {
        JComponent  comp    =
            getLabeledComponent( text, source, JSpinner.class );
        assertNotNull( comp );
        assertTrue( comp instanceof JSpinner );
        return (JSpinner)comp;
    }
    
    private JTextField
    getLabeledJTextField( String text, JComponent source )
    {
        JComponent  comp    =
            getLabeledComponent( text, source, JTextField.class );
        assertNotNull( comp );
        assertTrue( comp instanceof JTextField );
        return (JTextField)comp;
    }
    
    private JCheckBox getLabeledJCheckBox( String text, JComponent source )
    {
        JComponent  comp    =
            getLabeledComponent( text, source, JCheckBox.class );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
    }
    
    private JCheckBox getJCheckBox( JComponent source )
    {
        JComponent  comp    =
            ComponentFinder.find( source, c -> (c instanceof JCheckBox) );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
    }
    
    private JCheckBox getJCheckBox( String text, JComponent source )
    {
        Predicate<JComponent>   isCheckBox  = 
            c -> (c instanceof JCheckBox);
        Predicate<JComponent>   hasText     =
            c -> text.equals( ((JCheckBox)c).getText() );
        Predicate<JComponent>   pred        = isCheckBox.and( hasText );        
        JComponent  comp    =
            ComponentFinder.find( source, c -> (c instanceof JCheckBox) );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
    }
    
    private JComboBox<String> getJComboBox( JComponent source )
    {
        JComponent  comp    =
            ComponentFinder.find( source, c -> (c instanceof JComboBox) );
        assertNotNull( comp );
        assertTrue( comp instanceof JComboBox );
        return (JComboBox<String>)comp;
    }
    
    private JButton getJButton( String text, JComponent source )
    {
        Predicate<JComponent>   pred    =
            ComponentFinder.getButtonPredicate( text );
        JComponent              comp    = 
            ComponentFinder.find( source, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
    }
    
    private JTextField getColorText( JComponent source )
    {
        Predicate<JComponent>   pred    =
            ComponentFinder.getButtonPredicate( "Color" );
        JComponent              comp    = 
            ComponentFinder.find( source, pred );
        assertNotNull( comp );
        
        Container               parent  = comp.getParent();
        Component[]             comps   = parent.getComponents();
        JTextField              target  = Arrays.stream( comps )
            .filter( c -> (c instanceof JTextField ) )
            .map( jc -> (JTextField)jc )
            .findFirst().orElse( null );
        assertNotNull( target );
        return target;
    }
    
    private static float getFloat( JSpinner spinner )
    {
        SpinnerModel        model       = spinner.getModel();
        assertTrue( model instanceof SpinnerNumberModel );
        SpinnerNumberModel  numberModel = (SpinnerNumberModel)model;    
        float           val             = 
            numberModel.getNumber().floatValue();
        return val;
    }
    
    private static int getColor( JTextField colorComponent )
    {
        int iColor  = -1;
        try
        {
            iColor = Integer.parseInt( colorComponent.getText() );
        }
        catch ( NumberFormatException exc )
        {
            // ignore
        }
        return iColor;
    }
        
    /**
     * Instantiates a RobotAssictant.
     * 
     * @return the instantiate RobotAssistant.
     */
    private RobotAssistant makeRobot()
    {
        RobotAssistant  robot   = null;
        try
        {
            robot = new RobotAssistant();
        }
        catch ( AWTException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        return robot;
    }

    private class FontDialogComponents
    {
        private final JDialog           fontDialog;
        private final JComboBox<String> nameComponent;
        private final JCheckBox         boldComponent;
        private final JCheckBox         italicComponent;
        private final JSpinner          sizeComponent;
        private final JTextField        colorComponent;
        private final JButton           okButton;
        private final JButton           resetButton;
        private final JButton           cancelButton;
        
        public FontDialogComponents( JDialog fontDialog )
        {
            this.fontDialog = fontDialog;
            Container   comp    = fontDialog.getContentPane();
            assertTrue( comp instanceof JComponent );
            JComponent  pane    = (JComponent)comp;
            
            nameComponent = getJComboBox( pane );
            boldComponent = getJCheckBox( boldLabel, pane );
            italicComponent = getJCheckBox( italicLabel, pane );
            sizeComponent = getLabeledJSpinner( sizeLabel, pane );
            colorComponent = getColorText( pane );
            okButton = getJButton( okLabel, pane );
            resetButton = getJButton( resetLabel, pane );
            cancelButton = getJButton( cancelLabel, pane );
        }
    }
    
    private class GraphPropertyComponents
    {
        private final FontDialogComponents  fontComponents;
        private final JSpinner              gridUnitComponent;
        private final JTextField            colorComponent;
        private final JButton               editFontComponent;
        private final JCheckBox             labelsComponent;
        
        public GraphPropertyComponents()
        {
            JPanel  panel   = propSetPanelMap.get( graphSet );
            gridUnitComponent = getLabeledJSpinner( gridUnitLabel, panel );
            colorComponent = getColorText( panel );
            editFontComponent = getJButton( editFontLabel, panel );
            labelsComponent = getLabeledJCheckBox( labelsLabel, panel );
            
            boolean         canBeDialog     = true;
            boolean         canBeFrame      = false;
            boolean         mustBeVisible   = false;
            Predicate<Window>   pred            = w -> 
                fontDialogTitle.equals( ((JDialog)w).getTitle() );
            ComponentFinder finder  = new ComponentFinder(
                canBeDialog, 
                canBeFrame, 
                mustBeVisible
            );
            Window  window  = finder.findWindow( pred );
            assertNotNull( window );
            assertTrue( window instanceof JDialog );
            fontComponents = new FontDialogComponents( (JDialog)window );
        }
        
        public float getGridUnit()
        {
            float   val = getFloat( gridUnitComponent );
            return val;
        }
        
        public void setGridUnit( float val )
        {
            gridUnitComponent.setValue( val );
        }
        
        public String getFontName()
        {
            String  val     = 
                (String)fontComponents.nameComponent.getSelectedItem();
            return val;
        }
        
        public void setFontName( String name )
        {
            fontComponents.nameComponent.setSelectedItem( name );
        }
        
        public float getFontSize()
        {
            float   val     = getFloat( fontComponents.sizeComponent );
            return val;
        }
        
        public void setFontSize( float size )
        {
            fontComponents.sizeComponent.setValue( size );
        }
        
        public boolean isBold()
        {
            boolean value   = fontComponents.boldComponent.isSelected();
            return value;
        }
        
        public void setBold( boolean value )
        {
            fontComponents.boldComponent.setSelected( value );
        }
        
        public boolean isItalic()
        {
            boolean value   = fontComponents.italicComponent.isSelected();
            return value;
        }
        
        public void setItalic( boolean value )
        {
            fontComponents.italicComponent.setSelected( value );
        }
        
        public int getColor()
        {
            int iColor  = ProfileEditorTestGUI.getColor( colorComponent );
            return iColor;
        }
        
        public void setColor( int iColor )
        {
            colorComponent.setText( String.valueOf( iColor ) );
        }
        
        public boolean isFontDraw()
        {
            return labelsComponent.isSelected();
        }
        
        public void setFontDraw( boolean draw )
        {
            labelsComponent.setSelected( draw );
        }
        
        public Thread startFontEditor()
        {
            Thread  thread  = new Thread( () ->
                editFontComponent.doClick()
            );
            thread.start();
            while ( !fontComponents.fontDialog.isVisible() )
                Utils.pause( 1 );
            return thread;
        }
        
        public void selectOK()
        {
            fontComponents.okButton.doClick();
        }
        
        public void selectReset()
        {
            fontComponents.resetButton.doClick();
        }
        
        public void selectCancel()
        {
            fontComponents.cancelButton.doClick();
        }
    }
    
    private class LinePropertyComponents
    {
        private final String        propSetName;
        private final JSpinner      spacingComponent;
        private final JSpinner      weightComponent;
        private final JSpinner      lengthComponent;
        private final JCheckBox     drawComponent;
        private final JTextField    colorComponent;
        
        public LinePropertyComponents( LinePropertySet propSet )
        {
            propSetName = propSet.getClass().getSimpleName();
            JPanel  panel   = propSetPanelMap.get( propSetName );
            assertNotNull( panel );
            spacingComponent = propSet.hasSpacing() ?
                getLabeledJSpinner( spacingLabel, panel ) :
                null;
            weightComponent = propSet.hasStroke() ?
                getLabeledJSpinner( weightLabel, panel ) :
                null;
            lengthComponent = propSet.hasLength() ?
                getLabeledJSpinner( lengthLabel, panel ) :
                null;
            drawComponent = propSet.hasDraw() ?
                getJCheckBox( panel ) :
                null;
            colorComponent = propSet.hasColor() ?
                getColorText( panel ) :
                null;
            propSetToCompMap.put( propSetName, this );
        }
        
        public float getSpacing()
        {
            float   val = getFloat( spacingComponent );
            return val;
        }
        
        public void setSpacing( float val )
        {
            spacingComponent.setValue( val );
        }
        
        public float getWeight()
        {
            float   val = getFloat( weightComponent );
            return val;
        }
        
        public void setWeight( float val )
        {
            weightComponent.setValue( val );
        }
        
        public float getLength()
        {
            float   val = getFloat( lengthComponent );
            return val;
        }
        
        public void setLength( float val )
        {
            lengthComponent.setValue( val );
        }

        public boolean getDraw()
        {
            boolean val = drawComponent.isSelected();
            return val;
        }
        
        public void setDraw( boolean val )
        {
            drawComponent.setSelected( val );
        }
        
        public void setColor( int iColor )
        {
            colorComponent.setText( String.valueOf( iColor ) );
        }
        
        public int getColor()
        {
            int iColor  = ProfileEditorTestGUI.getColor( colorComponent );
            return iColor;
        }
    }
}
