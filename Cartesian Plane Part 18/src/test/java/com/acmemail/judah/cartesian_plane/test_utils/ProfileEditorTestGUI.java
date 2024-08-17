package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Window;
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
import com.acmemail.judah.cartesian_plane.components.ColorEditor;
import com.acmemail.judah.cartesian_plane.components.FontEditor;
import com.acmemail.judah.cartesian_plane.components.FontEditorDialog;
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
 * is used to display and manage a ProfileEditor.
 * Interaction with the ProfileEditor
 * is conducted via operations
 * performed on the EDT.
 * 
 * @author Jack Straub
 */
public class ProfileEditorTestGUI
{
    /** The simple name of the GraphPropertySetMW class. */
    private static final String graphSet        =
        GraphPropertySetMW.class.getSimpleName();
    /** The simple name of the LinePropertySetAxes class. */
    private static final String axesSet         =
        LinePropertySetAxes.class.getSimpleName();
    /** The simple name of the LinePropertySetGridLines class. */
    private static final String gridLinesSet    =
        LinePropertySetGridLines.class.getSimpleName();
    /** The simple name of the LinePropertySetTicMajor class. */
    private static final String ticMajorSet     =
        LinePropertySetTicMajor.class.getSimpleName();
    /** The simple name of the LinePropertySetTicMinor class. */
    private static final String ticMinorSet     =
        LinePropertySetTicMinor.class.getSimpleName();
    
    /** The singleton for this GUI test object. */
    private static ProfileEditorTestGUI testGUI;
    /** The ProfileEditor under test. */
    private final ProfileEditor         profileEditor;
    /**
     * Maps the name of a property set to a JPanel used to
     * interrogate and modify the property set. For example,
     * "LinePropertySetAxes" maps to the JPanel with the
     * "Axes" title.
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
        if ( testGUI != null )
            ;
        else if ( SwingUtilities.isEventDispatchThread() )
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
     * Must be invoked from the EDT.
     * 
     * @param profile   Profile to install in the ProfileEditor
     */
    private ProfileEditorTestGUI( Profile profile )
    {
        JFrame      editorFrame = new JFrame( "ProfileEditor Test GUI" );
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
    
    /**
     * Sets the profile name ProfileEditor GUI.
     * 
     * @param name  the name to set
     */
    public void setName( String name )
    {
        GUIUtils.schedEDTAndWait( () -> nameComponent.setText( name ) );
    }

    /**
     * Gets the profile name from the ProfileEditor GUI.
     * 
     * @return  the profile name from the ProfileEditor
     */
    public String getName()
    {
        String  name    = getStringValue( () -> nameComponent.getText() );
        return name;
    }
    
    /**
     * Sets the grid unit in the ProfileEditor text GUI.
     * 
     * @param value the value to set
     */
    public void setGridUnit( float value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setGridUnit( value ) 
        );
    }

    /**
     * Gets the grid unit from the ProfileEditor GUI. 
     * 
     * @return the grid unit from the ProfileEditor GUI.
     */
    public float getGridUnit()
    {
        float   value   = getFloatValue( () -> 
            graphPropComps.getGridUnit()
        );
        return value;
    }
    
    /**
     * Sets the grid width in the ProfileEditor text GUI.
     * 
     * @param value the value to set
     */
    public void setGridWidth( float value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setWidth( value ) 
        );
    }

    /**
     * Gets the grid width from the ProfileEditor GUI. 
     * 
     * @return the grid width from the ProfileEditor GUI.
     */
    public float getGridWidth()
    {
        float   value   = getFloatValue( () -> 
            graphPropComps.getWidth()
        );
        return value;
    }

    /**
     * Gets the font name from the ProfileEditor GUI. 
     * 
     * @return the font name from the ProfileEditor GUI.
     */
    public String getFontName()
    {
        String  value   = getStringValue( () -> 
            graphPropComps.getFontName()
        );
        return value;
    }

    /**
     * Sets the font name in the ProfileEditor GUI.
     * 
     * @param value the value to set
     */
    public void setFontName( String value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setFontName( value ) 
        );
    }

    /**
     * Gets the font size from the ProfileEditor GUI. 
     * 
     * @return the font size from the ProfileEditor GUI.
     */
    public float getFontSize()
    {
        float   value   = getFloatValue( () -> 
            graphPropComps.getFontSize()
        );
        return value;
    }

    /**
     * Sets the font size in the ProfileEditor GUI.
     * 
     * @param value the value to set
     */
    public void setFontSize( float value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setFontSize( value ) 
        );
    }

    /**
     * Gets value of the bold component from the ProfileEditor GUI. 
     * 
     * @return the state of the bold check box from the ProfileEditor GUI.
     */
    public boolean getFontBold()
    {
        boolean value   = getBooleanValue( () -> 
            graphPropComps.getBold()
        );
        return value;
    }

    /**
     * Sets value of the bold component in the ProfileEditor GUI.
     * 
     * @param value the value to set
     */
    public void setFontBold( boolean value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setBold( value ) 
        );
    }

    /**
     * Gets the value of the italic component from the ProfileEditor GUI. 
     * 
     * @return 
     *      the value of the italic component from the ProfileEditor GUI.
     */
    public boolean getFontItalic()
    {
        boolean value   = getBooleanValue( () -> 
            graphPropComps.getItalic()
        );
        return value;
    }

    /**
     * Sets the value of the italic component in the ProfileEditor GUI.
     * 
     * @param value the value to set
     */
    public void setFontItalic( boolean value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setItalic( value ) 
        );
    }

    /**
     * Gets value of the draw component from the ProfileEditor GUI. 
     * 
     * @return the value of the draw component from the ProfileEditor GUI.
     */
    public boolean getFontDraw()
    {
        boolean value   = getBooleanValue( () -> 
            graphPropComps.getFontDraw()
        );
        return value;
    }

    /**
     * Sets the value of the draw component in the ProfileEditor GUI.
     * 
     * @param value the value to set
     */
    public void setFontDraw( boolean value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setFontDraw( value ) 
        );
    }

    /**
     * Gets the foreground color from the ProfileEditor GUI. 
     * 
     * @return the foreground from the ProfileEditor GUI.
     */
    public int getFGColor()
    {
        int     value   = getIntValue( () -> 
            graphPropComps.getFGColor()
        );
        return value;
    }

    /**
     * Sets the value of the foreground color component
     * of the ProfileEditor GUI.
     * 
     * @param value the value to set
     */
    public void setFGColor( int value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setFGColor( value ) 
        );
    }

    /**
     * Gets the background color from the ProfileEditor GUI. 
     * 
     * @return the background from the ProfileEditor GUI.
     */
    public int getBGColor()
    {
        int     value   = getIntValue( () -> 
            graphPropComps.getBGColor()
        );
        return value;
    }

    /**
     * Sets the value of the background color component
     * of the ProfileEditor GUI.
     * 
     * @param value the value to set
     */
    public void setBGColor( int value )
    {
        GUIUtils.schedEDTAndWait( () -> 
            graphPropComps.setBGColor( value ) 
        );
    }
    
    /**
     * From the ProfileEditor GUI
     * gets the spacing property 
     * for the given LinePropertySet.
     * 
     * @param setName   the give LinePropertySet
     * 
     * @return  spacing property for the given LinePropertySet
     */
    public float getSpacing( String setName )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        float   value   = getFloatValue( () -> propComps.getSpacing() );
        return value;
    }
    
    /**
     * In the ProfileEditor GUI
     * sets the spacing value
     * for the given LinePropertySet.
     * 
     * @param setName   the given LinePropertySet
     * @param value     the value to set
     */
    public void setSpacing( String setName, float value )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        GUIUtils.schedEDTAndWait( () -> propComps.setSpacing( value ) );
    }
    
    /**
     * From the ProfileEditor GUI
     * gets the length property 
     * for the given LinePropertySet.
     * 
     * @param setName   the give LinePropertySet
     * 
     * @return  length property for the given LinePropertySet
     */
    public float getLength( String setName )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        float   value   = getFloatValue( () -> propComps.getLength() );
        return value;
    }
    
    /**
     * In the ProfileEditor GUI
     * sets the length value
     * for the given LinePropertySet.
     * 
     * @param setName   the given LinePropertySet
     * @param value     the value to set
     */
    public void setLength( String setName, float value )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        GUIUtils.schedEDTAndWait( () -> 
            propComps.setLength( value ) 
        );
    }
    
    /**
     * From the ProfileEditor GUI
     * gets the stroke property 
     * for the given LinePropertySet.
     * 
     * @param setName   the give LinePropertySet
     * 
     * @return  stroke property for the given LinePropertySet
     */
    public float getStroke( String setName )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        float   value   = getFloatValue( () ->
            propComps.getStroke()
        );
        return value;
    }
    
    /**
     * In the ProfileEditor GUI
     * sets the stroke value
     * for the given LinePropertySet.
     * 
     * @param setName   the given LinePropertySet
     * @param value     the value to set
     */
    public void setStroke( String setName, float value )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        GUIUtils.schedEDTAndWait( () -> 
            propComps.setStroke( value ) 
        );
    }
    
    /**
     * In the ProfileEditor GUI
     * sets the draw value
     * for the given LinePropertySet.
     * 
     * @param setName   the given LinePropertySet
     * @param value     the value to set
     */
    public void setDraw( String setName, boolean value )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        GUIUtils.schedEDTAndWait( () ->  propComps.setDraw( value ) );
    }
    
    /**
     * From the ProfileEditor GUI
     * gets the draw property 
     * for the given LinePropertySet.
     * 
     * @param setName   the give LinePropertySet
     * 
     * @return  draw property for the given LinePropertySet
     */
    public boolean getDraw( String setName )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        boolean value   = getBooleanValue( () -> propComps.getDraw() );
        return value;
    }
    
    /**
     * From the ProfileEditor GUI
     * gets the color property 
     * for the given LinePropertySet.
     * 
     * @param setName   the give LinePropertySet
     * 
     * @return  color property for the given LinePropertySet
     */
    public int getColor( String setName )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        int value   = getIntValue( () -> propComps.getColor() );
        return value;
    }
    
    /**
     * In the ProfileEditor GUI
     * sets the color value
     * for the given LinePropertySet.
     * 
     * @param setName   the given LinePropertySet
     * @param value     the value to set
     */
    public void setColor( String setName, int value )
    {
        LinePropertyComponents  propComps   = 
            propSetToCompMap.get( setName );
        GUIUtils.schedEDTAndWait( () -> propComps.setColor( value ) );
    }
    
    /**
     * Posts the font editor dialog in a separate thread.
     * 
     * @return  the Thread object used to post the dialog
     */
    public Thread editFont()
    {
        Thread  thread  = graphPropComps.startFontEditor();
        return thread;
    }
    
    /**
     * Selects the OK button in the font editor dialog.
     */
    public void selectFDOK()
    {
        graphPropComps.selectOK();
    }
    
    /**
     * Selects the Reset button in the font editor dialog.
     */
    public void selectFDReset()
    {
        graphPropComps.selectReset();
    }
    
    /**
     * Selects the Cancel button in the font editor dialog.
     */
    public void selectFDCancel()
    {
        graphPropComps.selectCancel();
    }
    
    /**
     * Exercises the apply method
     * of the ProfileEditor.
     */
    public void apply()
    {
        GUIUtils.schedEDTAndWait( () -> profileEditor.apply() );
    }
    
    /**
     * Exercises the reset method
     * of the ProfileEditor.
     */
    public void reset()
    {
        GUIUtils.schedEDTAndWait( () -> profileEditor.apply() );
    }
    
    /**
     * Gets and validates a Boolean value
     * from a given supplier.
     * The value is obtained 
     * in the context of the EDT.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the requested value
     */
    private boolean getBooleanValue( Supplier<Object> supplier )
    {
        Object  oVal    = getValue( supplier );
        assertTrue( oVal instanceof Boolean );
        return (boolean)oVal;
    }
    
    /**
     * Gets and validates a Float value
     * from a given supplier.
     * The value is obtained 
     * in the context of the EDT.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the requested value
     */
    private float getFloatValue( Supplier<Object> supplier )
    {
        Object  oVal    = getValue( supplier );
        assertTrue( oVal instanceof Float );
        return (float)oVal;
    }
    
    /**
     * Gets and validates an integer value
     * from a given supplier.
     * The value is obtained 
     * in the context of the EDT.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the requested value
     */
    private int getIntValue( Supplier<Object> supplier )
    {
        Object  oVal    = getValue( supplier );
        assertTrue( oVal instanceof Integer );
        return (int)oVal;
    }
    
    /**
     * Gets and validates a String value
     * from a given supplier.
     * The value is obtained 
     * in the context of the EDT.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the requested value
     */
    private String getStringValue( Supplier<Object> supplier )
    {
        Object  oVal    = getValue( supplier );
        assertTrue( oVal instanceof String );
        return (String)oVal;
    }
    
    /**
     * Gets and validates value
     * from a given supplier.
     * The value is obtained 
     * in the context of the EDT.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the requested value
     */
    private Object getValue( Supplier<Object> supplier )
    {
        GUIUtils.schedEDTAndWait( () -> 
            adHocObject = supplier.get()
        );
        return adHocObject;
    }
    
    /**
     * Discover the JPanels containing the components
     * used to edit the properties encapsulated 
     * in a Profile.
     * There are separate panels 
     * for the properties
     * contained in the GraphPropertySetMW
     * and concrete LinePropertySet components
     * of the Profile.
     * The discovered panels
     * are added to the {@link #propSetPanelMap},
     * using the name of the property set as the key
     * and the JPanel as the value.
     * 
     * @param source    
     *      a parent component of all the target JPanels,
     *      probably the ProfileEditor panel.
     */
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
    
    /**
     * Within the ProfileEditor,
     * discover the components
     * used to edit the properties
     * in each LinePropertySet object
     * encapsulated in a Profile.
     * The discovered components 
     * are stored in {@link #propSetToCompMap}
     * using the property set class name
     * as a key.
     */
    private void getAllLinePropertyComponents()
    {
        Stream.of(
            new LinePropertySetAxes(),
            new LinePropertySetGridLines(),
            new LinePropertySetTicMajor(),
            new LinePropertySetTicMinor()
        )
        .map( LinePropertyComponents::new )
        .forEach( s -> propSetToCompMap.put( s.propSetName, s ) );
    }
    
    /**
     * Gets a float value from a given JSpinner.
     * <p>
     * Precondition:
     * The given JSpinner incorporates a SpinnerNumberModel.
     * 
     * @param spinner   the given JSpinner
     * 
     * @return  the value obtained from the JSpinner
     */
    private static float getFloat( JSpinner spinner )
    {
        SpinnerModel        model       = spinner.getModel();
        assertTrue( model instanceof SpinnerNumberModel );
        SpinnerNumberModel  numberModel = (SpinnerNumberModel)model;    
        float           val             = 
            numberModel.getNumber().floatValue();
        return val;
    }
    
    /**
     * Obtains the text from a given JTextField
     * and converts it to an integer representing 
     * an RGB color.
     * If the conversion fails, -1 is returned.
     * 
     * @param colorComponent    the given JTextField
     * 
     * @return  the converted integer, or -1 if conversion fails
     */
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
    
    /**
     * Formats a given integer value
     * in hexadecimal string representation.
     * 
     * @param value the given integer value
     * 
     * @return  the formatted string
     */
    private static String toHexString( int value )
    {
        String  hex = String.format( "0x%x", value );
        return hex;
    }
    
    /**
     * Given the component name of a JPanel
     * search the ProfileEditor under test
     * for the target panel and return it.
     * An assertion is raised
     * if the JPanel can't be found.
     * 
     * @param name  the given component name
     * 
     * @return  the target component
     */
    private JPanel getPanelByName( String name )
    {
        JComponent  comp    = getComponentByName( name, profileEditor );
        assertTrue( comp instanceof JPanel);
        return (JPanel)comp;
    }
    
    /**
     * Given the component name of a JSpinner
     * and its parent component,
     * returns the target JSpinner.
     * An assertion is raised
     * if the JSpinner can't be found.
     * 
     * @param name  the given component name
     * @param source  the component parent
     * 
     * @return  the target component
     */
    private JSpinner getSpinnerByName( String name, JComponent source )
    {
        JComponent  comp    = getComponentByName( name, source );
        assertNotNull( comp );
        assertTrue( comp instanceof JSpinner );
        return (JSpinner)comp;
    }
    
    /**
     * Given the component name of a JCheckBox
     * and its parent component,
     * returns the target JCheckBox.
     * An assertion is raised
     * if the JCheckBox can't be found.
     * 
     * @param name  the given component name
     * @param source  the component parent
     * 
     * @return  the target component
     */
    private JCheckBox getCheckBoxByName( String name, JComponent source )
    {
        JComponent  comp    = getComponentByName( name, source );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
    }
    
    /**
     * Given the component name of a JTextField
     * and its parent component,
     * returns the target JTextField.
     * An assertion is raised
     * if the JTextField can't be found.
     * 
     * @param name  the given component name
     * @param source  the component parent
     * 
     * @return  the target component
     */
    private JTextField 
    getTextFieldByName( String name, JComponent source )
    {
        JComponent  comp    = getComponentByName( name, source );
        assertNotNull( comp );
        assertTrue( comp instanceof JTextField );
        return (JTextField)comp;
    }
    
    /**
     * Given the component name of a JButton
     * and its parent component,
     * returns the target JButton.
     * An assertion is raised
     * if the JButton can't be found.
     * 
     * @param name  the given component name
     * @param source  the component parent
     * 
     * @return  the target component
     */
    private JButton getJButtonByName( String name, JComponent source )
    {
        JComponent  comp    = getComponentByName( name, source );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
    }
    
    /**
     * Given the name of a component
     * and its parent container,
     * returns the target component.
     * An assertion is raised
     * if the component can't be found.
     * 
     * @param name  the given component name
     * @param source  the component parent
     * 
     * @return  the target component
     */
    private JComponent 
    getComponentByName( String name, JComponent source )
    {
        Predicate<JComponent>   pred    = 
            jc -> name.equals( jc.getName() );
        JComponent  comp    = ComponentFinder.find( source, pred );
        assertNotNull( comp );
        return comp;
    }
    
    /**
     * Returns the first JCheckBox child
     * of a given container.
     * An assertion is raised
     * if the component can't be found.
     * 
     * @param source    the given container
     * 
     * @return  the target JCheckBox
     */
    private JCheckBox getCheckBox( JComponent source )
    {
        JComponent  comp    =
            ComponentFinder.find( source, c -> (c instanceof JCheckBox) );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
    }
    
    
    /**
     * Returns the first JComboBox child
     * of a given container.
     * An assertion is raised
     * if the component can't be found.
     * 
     * @param source    the given container
     * 
     * @return  the target JCheckBox
     */
    @SuppressWarnings("unchecked")
    private JComboBox<String> getComboBox( JComponent source )
    {
        JComponent  comp    =
            ComponentFinder.find( source, c -> (c instanceof JComboBox) );
        assertNotNull( comp );
        assertTrue( comp instanceof JComboBox );
        return (JComboBox<String>)comp;
    }

    /**
     * Collects those components of a FontEditorDialog
     * that are necessary for editing
     * the font properties in a ProfileEditor.
     * 
     * @author Jack Straub
     */
    private class FontDialogComponents
    {
        /** The FontEditorDialog. */
        private final JDialog           fontDialog;
        /** The component for editing the font name. */
        private final JComboBox<String> nameComponent;
        /** The component for editing the font size. */
        private final JSpinner          sizeComponent;
        /** The component for editing the font color. */
        private final JTextField        colorComponent;
        /** The component for editing the bold property. */
        private final JCheckBox         boldComponent;
        /** The component for editing the italic property. */
        private final JCheckBox         italicComponent;
        /** The dialog's OK button. */
        private final JButton           okButton;
        /** The dialog's Reset button. */
        private final JButton           resetButton;
        /** The dialog's Cancel button. */
        private final JButton           cancelButton;
        
        /**
         * Constructor.
         * Discovers all the components of a FontEditorDialog
         * needed to edit a font.
         * 
         * @param fontDialog    the source FontEditorDialog
         */
        public FontDialogComponents( JDialog fontDialog )
        {
            this.fontDialog = fontDialog;
            Container   comp    = fontDialog.getContentPane();
            assertTrue( comp instanceof JComponent );
            JComponent  pane    = (JComponent)comp;
            
            nameComponent = getComboBox( pane );
            boldComponent = 
                getCheckBoxByName( FontEditor.BOLD_LABEL, pane );
            italicComponent = 
                getCheckBoxByName( FontEditor.ITALIC_LABEL, pane );
            sizeComponent = 
                getSpinnerByName( FontEditor.SIZE_LABEL, pane );
            colorComponent = 
                getTextFieldByName( ColorEditor.TEXT_EDITOR_NAME, pane );
            okButton = 
                getJButtonByName( FontEditorDialog.OK_LABEL, pane );
            resetButton = 
                getJButtonByName( FontEditorDialog.RESET_LABEL, pane );
            cancelButton = 
                getJButtonByName( FontEditorDialog.CANCEL_LABEL, pane );
        }
    }
    
    /**
     * Collects and manages
     * all the components necessary
     * to edit the GraphPropertySetMW properties
     * of a Profile. 
     *
     * @author Jack Straub
     * 
     * @see FontDialogComponents
     */
    private class GraphPropertyComponents
    {
        /** Collection of components for editor font properties. */
        private final FontDialogComponents  fontComponents;
        /** Component for editing the grid unit. */
        private final JSpinner              gridUnitComponent;
        /** Component for editing the grid width. */
        private final JSpinner              widthComponent;
        /** Component for editing the grid color. */
        private final JTextField            colorComponent;
        /** Component for editing the draw-labels property. */
        private final JCheckBox             labelsComponent;
        /** Component for launching the FontEditorDialog. */
        private final JButton               editFontComponent;
        
        /**
         * Constructor.
         * Discovers all the components 
         * needed to edit the GraphPropertySetMW properties
         * of a Profile.
         */
        public GraphPropertyComponents()
        {
            JPanel  panel   = propSetPanelMap.get( graphSet );
            gridUnitComponent = 
                getSpinnerByName( ProfileEditor.GRID_UNIT_LABEL, panel );
            widthComponent = 
                getSpinnerByName( ProfileEditor.WIDTH_LABEL, panel );
            colorComponent = 
                getTextFieldByName( ColorEditor.TEXT_EDITOR_NAME, panel );
            editFontComponent = 
                getJButtonByName( ProfileEditor.EDIT_FONT_LABEL, panel );
            labelsComponent = 
                getCheckBoxByName( ProfileEditor.DRAW_FONT_LABEL, panel );
            
            boolean     canBeDialog     = true;
            boolean     canBeFrame      = false;
            boolean     mustBeVisible   = false;
            String      dialogTitle     = FontEditorDialog.DIALOG_TITLE;
            Predicate<Window>   pred    = 
                w -> dialogTitle.equals( ((JDialog)w).getTitle() );
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
        
        /**
         * Gets the value of the component
         * used to edit the grid unit.
         * 
         * @return  the value of the grid unit component
         */
        public float getGridUnit()
        {
            float   val = getFloat( gridUnitComponent );
            return val;
        }
        
        /**
         * Sets the value of the component
         * used to edit the grid unit.
         * 
         * @param val   the value to set
         */
        public void setGridUnit( float val )
        {
            gridUnitComponent.setValue( val );
        }
        
        /**
         * Gets the value of the component
         * used to edit the grid width.
         * 
         * @return  
         *      the value of the component used to edit the grid width 
         */
        public float getWidth()
        {
            float   width   = getFloat( widthComponent );
            return width;
        }
        
        /**
         * Sets the value of the component
         * used to edit the grid width
         * 
         * @param width the new value
         */
        public void setWidth( float width )
        {
            widthComponent.setValue( width );
        }
        
        /**
         * Gets the value of the component
         * used to edit the grid color.
         * 
         * @return  the value of the grid color component
         */
        public int getBGColor()
        {
            int iColor  = ProfileEditorTestGUI.getColor( colorComponent );
            return iColor;
        }
        
        /**
         * Sets the value of the component
         * used to edit the grid color.
         * 
         * @param iColor   the value to set
         */
        public void setBGColor( int iColor )
        {
            colorComponent.setText( toHexString( iColor ) );
            colorComponent.postActionEvent();
        }
        
        /**
         * Gets the value of the component
         * used to edit the font name.
         * 
         * @return  the value of the font name component
         */
        public String getFontName()
        {
            String  val     = 
                (String)fontComponents.nameComponent.getSelectedItem();
            return val;
        }
        
        /**
         * Sets the value of the component
         * used to edit the font name.
         * 
         * @param name   the value to set
         */
        public void setFontName( String name )
        {
            fontComponents.nameComponent.setSelectedItem( name );
        }
        
        /**
         * Gets the value of the component
         * used to edit the font size.
         * 
         * @return  the value of the font size component
         */
        public float getFontSize()
        {
            float   val     = getFloat( fontComponents.sizeComponent );
            return val;
        }
        
        /**
         * Sets the value of the component
         * used to edit the font size.
         * 
         * @param size   the value to set
         */
        public void setFontSize( float size )
        {
            fontComponents.sizeComponent.setValue( size );
        }
        
        /**
         * Gets the value of the component
         * used to edit the bold property.
         * 
         * @return  the value of the bold component
         */
        public boolean getBold()
        {
            boolean value   = fontComponents.boldComponent.isSelected();
            return value;
        }
        
        /**
         * Sets the value of the component
         * used to edit the bold property.
         * 
         * @param value   the value to set
         */
        public void setBold( boolean value )
        {
            fontComponents.boldComponent.setSelected( value );
        }
        
        /**
         * Gets the value of the component
         * used to edit the italic property.
         * 
         * @return  the value of the italic component
         */
        public boolean getItalic()
        {
            boolean value   = fontComponents.italicComponent.isSelected();
            return value;
        }
        
        /**
         * Sets the value of the component
         * used to edit the italic property.
         * 
         * @param value   the value to set
         */
        public void setItalic( boolean value )
        {
            fontComponents.italicComponent.setSelected( value );
        }
        
        /**
         * Gets the value of the component
         * used to edit the font color.
         * 
         * @return  the value of the font color component
         */
        public int getFGColor()
        {
            int iColor  = ProfileEditorTestGUI
                .getColor( fontComponents.colorComponent );
            return iColor;
        }
        
        /**
         * Sets the value of the component
         * used to edit the font color.
         * 
         * @param iColor   the value to set
         */
        public void setFGColor( int iColor )
        {
            fontComponents.colorComponent
                .setText( toHexString( iColor ) );
            fontComponents.colorComponent.postActionEvent();
        }
        
        /**
         * Gets the value of the component
         * used to edit the font-draw property.
         * 
         * @return  the value of the font-draw component
         */
        public boolean getFontDraw()
        {
            return labelsComponent.isSelected();
        }
        
        /**
         * Sets the value of the component
         * used to edit the font-draw property.
         * 
         * @param draw   the value to set
         */
        public void setFontDraw( boolean draw )
        {
            labelsComponent.setSelected( draw );
        }
        
        /**
         * Starts the FontEditorDialog
         * in a dedicated thread.
         * Doesn't return until
         * the dialog has become visible.
         * 
         * @return  the thread running the dialog
         */
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
        
        /**
         * Pushes the FontEditorDialog OK button.
         */
        public void selectOK()
        {
            fontComponents.okButton.doClick();
        }
        
        /**
         * Pushes the FontEditorDialog Reset button.
         */
        public void selectReset()
        {
            fontComponents.resetButton.doClick();
        }
        
        /**
         * Pushes the FontEditorDialog Cancel button.
         */
        public void selectCancel()
        {
            fontComponents.cancelButton.doClick();
        }
    }

    /**
     * Collects all those components
     * necessary to edit
     * a LinePropertySet contained in a Profile.
     * 
     * @author Jack Straub
     */
    private class LinePropertyComponents
    {
        /** 
         * The simple name of the LinePropertySet subclass
         * containing the properties to be edited.
         */
        private final String        propSetName;
        /** The component used to edit the spacing property. */
        private final JSpinner      spacingComponent;
        /** The component used to edit the stroke property. */
        private final JSpinner      strokeComponent;
        /** The component used to edit the length property. */
        private final JSpinner      lengthComponent;
        /** The component used to edit the draw property. */
        private final JCheckBox     drawComponent;
        /** The component used to edit the color property. */
        private final JTextField    colorComponent;
        
        /**
         * Constructor.
         * Discovers the components used to edit the properties
         * of the given LinePropertySet.
         * Properties not supported by the given LinePropertySet
         * are ignored.
         * 
         * @param propSet   the given LinePropertySet
         */
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
                getCheckBox( panel ) :
                null;
            colorComponent = propSet.hasColor() ?
                getTextFieldByName( ColorEditor.TEXT_EDITOR_NAME, panel ) :
                null;
        }
        
        /**
         * Gets the value of the component
         * used to edit the spacing property.
         * 
         * @return 
         *      the value of the component for editing 
         *      the spacing property 
         */
        public float getSpacing()
        {
            float   val = getFloat( spacingComponent );
            return val;
        }
        
        /**
         * Sets the value of the component
         * for editing the spacing property.
         * 
         * @param val   the new value
         */
        public void setSpacing( float val )
        {
            spacingComponent.setValue( val );
        }
        
        /**
         * Gets the value of the component
         * used to edit the stroke property.
         * 
         * @return 
         *      the value of the component for editing 
         *      the stroke property 
         */
        public float getStroke()
        {
            float   val = getFloat( strokeComponent );
            return val;
        }
        
        /**
         * Sets the value of the component
         * for editing the stroke property.
         * 
         * @param val   the new value
         */
        public void setStroke( float val )
        {
            strokeComponent.setValue( val );
        }
        
        /**
         * Gets the value of the component
         * used to edit the length property.
         * 
         * @return 
         *      the value of the component for editing 
         *      the length property 
         */
        public float getLength()
        {
            float   val = getFloat( lengthComponent );
            return val;
        }
        
        /**
         * Sets the value of the component
         * for editing the length property.
         * 
         * @param val   the new value
         */
        public void setLength( float val )
        {
            lengthComponent.setValue( val );
        }

        /**
         * Gets the value of the component
         * used to edit the draw property.
         * 
         * @return 
         *      the value of the component for editing 
         *      the draw property 
         */
        public boolean getDraw()
        {
            boolean val = drawComponent.isSelected();
            return val;
        }
        
        /**
         * Sets the value of the component
         * for editing the draw property.
         * 
         * @param val   the new value
         */
        public void setDraw( boolean val )
        {
            drawComponent.setSelected( val );
        }
        
        /**
         * Gets the value of the component
         * used to edit the color property.
         * 
         * @return 
         *      the value of the component for editing 
         *      the color property 
         */
        public int getColor()
        {
            int iColor  = ProfileEditorTestGUI.getColor( colorComponent );
            return iColor;
        }
        
        /**
         * Sets the value of the component
         * for editing the color property.
         * 
         * @param iColor   the new value
         */
        public void setColor( int iColor )
        {
            colorComponent.setText( toHexString( iColor ) );
        }
    }
}
