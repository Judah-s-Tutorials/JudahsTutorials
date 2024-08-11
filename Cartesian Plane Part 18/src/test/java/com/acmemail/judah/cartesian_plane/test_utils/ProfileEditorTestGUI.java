package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
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
        LinePropertySetTicMajor.class.getSimpleName();

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
    private final Map<String, LinePropertyComponents>   
        propSetToCompMap    = new HashMap<>();
    
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
        
        editorFrame.pack();
        editorFrame.setVisible( true );
        
        getAllTitledPanels( profileEditor );
        getAllLinePropertyComponents();
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
    
    private JCheckBox getJCheckBox( JComponent source )
    {
        JComponent  comp    =
            ComponentFinder.find( source, c -> (c instanceof JCheckBox) );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
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
                null :
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
        
        public int getColor()
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
        
        public void setColor( int iColor )
        {
            colorComponent.setText( String.valueOf( iColor ) );
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
    }
}
