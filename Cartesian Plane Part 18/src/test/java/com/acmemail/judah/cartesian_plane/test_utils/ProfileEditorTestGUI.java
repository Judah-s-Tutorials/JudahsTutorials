package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.FontEditor;
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
    private static final String okLabel         = "OK";
    private static final String resetLabel      = "Reset";
    private static final String cancelLabel     = "Cancel";
    
    /** The GUI test object. */
    private static ProfileEditorTestGUI testGUI;
    /* The Profile encapsulated by the ProfileEditor under test. */
    private final Profile               profile;
    /** The application frame that displays the ProfileEditor. */
    private final JFrame                editorFrame;
    /** The ProfileEditor under test. */
    private final ProfileEditor         profileEditor;
    /** Title of the font editor dialog. */
    private static final String fontDialogTitle = "Font Editor";
    /**
     * Maps the name of a property set to a JPanel used to
     * interrogate and modify the property set.
     */
    private final Map<String,JPanel>    propSetPanelMap = new HashMap<>();
    /** The set of components in the panel titled "Grid." */
    private final GraphPropertyComponents   graphPropComps;
    /**
     * Maps the name of a LinePropertySet to the set of components
     * associated with the encapsulated properties.
     */
    private final Map<String, LinePropertyComponents>   
        propSetToCompMap    = new HashMap<>();
    /** 
     * The component in the ProfileEditor 
     * that contains the profile name.
     */
    private final JTextField            nameComponent;
    
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
        graphPropComps = new GraphPropertyComponents();
        nameComponent = 
            getTextFieldByName( ProfileEditor.NAME_LABEL, profileEditor );
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
            graphPropComps.setGridUnit( value ) 
        );
    }

    public float getGridUnit()
    {
        float   value   = getFloatValue( () -> 
            graphPropComps.getGridUnit()
        );
        return value;
    }

    public String getFontName()
    {
        String  value   = getStringValue( () -> 
            graphPropComps.getFontName()
        );
        return value;
    }

    public void setFontName( String value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setFontName( value ) 
        );
    }

    public float getFontSize()
    {
        float   value   = getFloatValue( () -> 
            graphPropComps.getFontSize()
        );
        return value;
    }

    public void setFontSize( float value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setFontSize( value ) 
        );
    }

    public boolean isFontBold()
    {
        boolean value   = getBooleanValue( () -> 
            graphPropComps.isBold()
        );
        return value;
    }

    public void setFontBold( boolean value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setBold( value ) 
        );
    }

    public boolean isFontItalic()
    {
        boolean value   = getBooleanValue( () -> 
            graphPropComps.isItalic()
        );
        return value;
    }

    public void setFontItalic( boolean value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setItalic( value ) 
        );
    }

    public boolean isFontDraw()
    {
        boolean value   = getBooleanValue( () -> 
            graphPropComps.isFontDraw()
        );
        return value;
    }

    public void setFontDraw( boolean value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setFontDraw( value ) 
        );
    }

    public int getFGColor()
    {
        int     value   = getIntValue( () -> 
            graphPropComps.getFGColor()
        );
        return value;
    }

    public void setFGColor( int value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setFGColor( value ) 
        );
    }

    public int getBGColor()
    {
        int     value   = getIntValue( () -> 
            graphPropComps.getBGColor()
        );
        return value;
    }

    public void setBGColor( int value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setBGColor( value ) 
        );
    }

    public void setDrawLabels( boolean value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setFontDraw( value ) 
        );
    }
    
    public Thread editFont()
    {
        Thread  thread  = graphPropComps.startFontEditor();
        return thread;
    }
    
    public void selectFDOK()
    {
        graphPropComps.selectOK();
    }
    
    public void selectFDReset()
    {
        graphPropComps.selectReset();
    }
    
    public void selectFDCancel()
    {
        graphPropComps.selectCancel();
    }
    
    public float getSpacing( String setName )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        float   value   = getFloatValue( () -> propComps.getSpacing() );
        return value;
    }
    
    public void setSpacing( String setName, float value )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        GUIUtils.schedEDTAndWait( () -> propComps.setSpacing( value ) );
    }
    
    public float getLength( String setName )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        float   value   = getFloatValue( () -> propComps.getLength() );
        return value;
    }
    
    public void setDraw( String setName, boolean value )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        GUIUtils.schedEDTAndWait( () ->  propComps.setDraw( value ) );
    }
    
    public int getColor( String setName )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        int value   = getIntValue( () -> propComps.getColor() );
        return value;
    }
    
    public void setColor( String setName, int value )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        GUIUtils.schedEDTAndWait( () -> propComps.setColor( value ) );
    }
    
    public boolean getDraw( String setName )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        boolean value   = getBooleanValue( () -> propComps.getDraw() );
        return value;
    }
    
    public void setLength( String setName, float value )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        GUIUtils.schedEDTAndWait( () -> 
            propComps.setLength( value ) 
        );
    }
    
    public float getStroke( String setName )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        float   value   = getFloatValue( () ->
            propComps.getStroke()
        );
        return value;
    }
    
    public void setStroke( String setName, float value )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        GUIUtils.schedEDTAndWait( () -> 
            propComps.setStroke( value ) 
        );
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
    
    private Object getValue( Supplier<Object> supplier )
    {
        GUIUtils.schedEDTAndWait( () -> 
            adHocObject = supplier.get()
        );
        return adHocObject;
    }
    
    private void getAllTitledPanels( Container source )
    {
        /* Define the correspondence between a titled panel in the GUI
         * and the property set that the panel maps to. For example
         * the panel labeled "Grid" maps to GraphPropertySetMW and the 
         * panel labeled "Axes" maps to LinePropertySetAxes.
         */
        Map<String,String>    titlePropSetMap =
            Map.of( 
                ProfileEditor.GRID_TITLE, graphSet,
                ProfileEditor.AXES_TITLE, axesSet,
                ProfileEditor.GRID_LINES_TITLE, gridLinesSet,
                ProfileEditor.MAJOR_TICS_TITLE, ticMajorSet,
                ProfileEditor.MINOR_TICS_TITLE, ticMinorSet
            );
        Stream.of(
            ProfileEditor.GRID_TITLE,
            ProfileEditor.AXES_TITLE,
            ProfileEditor.GRID_LINES_TITLE,
            ProfileEditor.MAJOR_TICS_TITLE,
            ProfileEditor.MINOR_TICS_TITLE
        ).forEach( title -> {
            JPanel  panel   = getPanelByName( title );
            String  propSet = titlePropSetMap.get( title );
            assertNotNull( propSet );
            propSetPanelMap.put( propSet, panel );
        });
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
    
    private JPanel getPanelByName( String name )
    {
        JComponent  comp    = getComponentByName( name, profileEditor );
        assertTrue( comp instanceof JPanel);
        return (JPanel)comp;
    }
    
    private JSpinner getSpinnerByName( String name, JComponent source )
    {
        JComponent  comp    = getComponentByName( name, source );
        assertNotNull( comp );
        assertTrue( comp instanceof JSpinner );
        return (JSpinner)comp;
    }
    
    private JCheckBox getCheckBoxByName( String name, JComponent source )
    {
        JComponent  comp    = getComponentByName( name, source );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
    }
    
    private JTextField getTextFieldByName( String name, JComponent source )
    {
        JComponent  comp    = getComponentByName( name, source );
        assertNotNull( comp );
        assertTrue( comp instanceof JTextField );
        return (JTextField)comp;
    }
    
    private JComponent 
    getComponentByName( String name, JComponent source )
    {
        Predicate<JComponent>   pred    = 
            jc -> name.equals( jc.getName() );
        JComponent  comp    = ComponentFinder.find( source, pred );
        assertNotNull( comp );
        return comp;
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
        JComponent              comp        =
            ComponentFinder.find( source, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
    }
    
    @SuppressWarnings("unchecked")
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
            iColor = Integer.decode( colorComponent.getText() );
        }
        catch ( NumberFormatException exc )
        {
            // ignore
        }
        return iColor;
    }
    
    private static String toHexString( int value )
    {
        String  hex = String.format( "0x%x", value );
        return hex;
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
            boldComponent = getJCheckBox( FontEditor.BOLD_LABEL, pane );
            italicComponent = getJCheckBox( FontEditor.ITALIC_LABEL, pane );
            sizeComponent = getSpinnerByName( FontEditor.SIZE_LABEL, pane );
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
            gridUnitComponent = 
                getSpinnerByName( ProfileEditor.GRID_UNIT_LABEL, panel );
            colorComponent = getColorText( panel );
            editFontComponent = 
                getJButton( ProfileEditor.EDIT_FONT_LABEL, panel );
            labelsComponent = 
                getCheckBoxByName( ProfileEditor.DRAW_FONT_LABEL, panel );
            
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
        
        public int getBGColor()
        {
            int iColor  = ProfileEditorTestGUI.getColor( colorComponent );
            return iColor;
        }
        
        public void setBGColor( int iColor )
        {
            colorComponent.setText( toHexString( iColor ) );
            colorComponent.postActionEvent();
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
        
        public int getFGColor()
        {
            int iColor  = ProfileEditorTestGUI
                .getColor( fontComponents.colorComponent );
            return iColor;
        }
        
        public void setFGColor( int iColor )
        {
            fontComponents.colorComponent
                .setText( toHexString( iColor ) );
            fontComponents.colorComponent.postActionEvent();
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
        private final JSpinner      strokeComponent;
        private final JSpinner      lengthComponent;
        private final JCheckBox     drawComponent;
        private final JTextField    colorComponent;
        
        public LinePropertyComponents( LinePropertySet propSet )
        {
            propSetName = propSet.getClass().getSimpleName();
            JPanel  panel   = propSetPanelMap.get( propSetName );
            assertNotNull( panel );
            spacingComponent = propSet.hasSpacing() ?
                getSpinnerByName( ProfileEditor.SPACING_LABEL, panel ) :
                null;
            strokeComponent = propSet.hasStroke() ?
                getSpinnerByName( ProfileEditor.STROKE_LABEL, panel ) :
                null;
            lengthComponent = propSet.hasLength() ?
                getSpinnerByName( ProfileEditor.LENGTH_LABEL, panel ) :
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
        
        public float getStroke()
        {
            float   val = getFloat( strokeComponent );
            return val;
        }
        
        public void setStroke( float val )
        {
            strokeComponent.setValue( val );
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
            colorComponent.setText( toHexString( iColor ) );
        }
        
        public int getColor()
        {
            int iColor  = ProfileEditorTestGUI.getColor( colorComponent );
            return iColor;
        }
    }
}
