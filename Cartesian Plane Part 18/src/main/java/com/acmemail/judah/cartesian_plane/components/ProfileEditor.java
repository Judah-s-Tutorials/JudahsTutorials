package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * This facility represents a set of controls
 * that the operator can use
 * to edit the properties
 * encapsulated in a {@link Profile}.
 * The controls include a feedback component,
 * a sample graph that displays changes
 * to the profile as they are made
 * by the operator.
 * The feedback component 
 * is not displayed automatically;
 * it is a JComponent
 * that the user can obtain
 * and display in another container.
 * See {@link #getFeedBack()}.
 * 
 * @author Jack Straub
 * 
 * @see #getFeedBack()
 */
@SuppressWarnings("serial")
public class ProfileEditor extends JPanel
{
    /** Text describing the GUI name panel. */
    public static final String NAME_LABEL       = "Name";

    /** Text describing the GUI weight field. */
    public static final String STROKE_LABEL     = "Weight";
    /** Text describing the GUI lines/unit field. */
    public static final String SPACING_LABEL    = "Lines/Unit";
    /** Text describing the GUI length field. */
    public static final String LENGTH_LABEL     = "Length";
    /** Text describing the GUI draw field (for lines). */
    public static final String DRAW_LABEL       = "Draw";

    /** Text describing the GUI draw field (for text). */
    public static final String DRAW_FONT_LABEL  = "Labels";
    /** Text describing the GUI grid unit panel. */
    public static final String GRID_UNIT_LABEL  = "Grid Unit";
    /** Text on the edit-font button. " */
    public static final String EDIT_FONT_LABEL  = "Edit Font";
    /** Text describing the GUI width panel. */
    public static final String WIDTH_LABEL      = "Width";
    
    /** Text describing the grid panel. */
    public static final String GRID_TITLE       = "Grid";
    /** Text describing the GUI axis panel. */
    public static final String AXES_TITLE       = "Axes";
    /** Text describing the GUI major tics panel. */
    public static final String MAJOR_TICS_TITLE = "Major Tics";
    /** Text describing the GUI minor tics panel. */
    public static final String MINOR_TICS_TITLE = "Minor Tics";
    /** Text describing the GUI grid lines panel. */
    public static final String GRID_LINES_TITLE = "Grid Lines";
    
    /** Simple name of the LinePropertySetAxes class. */
    private static final String axesSet         =
        LinePropertySetAxes.class.getSimpleName();
    /** Simple name of the LinePropertySetTicMajor class. */
    private static final String ticMajorSet     =
        LinePropertySetTicMajor.class.getSimpleName();
    /** Simple name of the LinePropertySetTicMinor class. */
    private static final String ticMinorSet     =
        LinePropertySetTicMinor.class.getSimpleName();
    /** Simple name of the LinePropertySetTicMinor class. */
    private static final String gridLinesSet    =
        LinePropertySetGridLines.class.getSimpleName();
    
    /** The component that the sample graph is drawn on. */
    private final ProfileEditorFeedback        canvas;
    
    /** 
     * List of Runnables that set or reset the values of the 
     * GUI's components from the properties in the profile.
     */
    private final List<Runnable>            resetList   = 
        new ArrayList<>();
    
    /** 
     * List of Runnables that transfers the values of the 
     * GUI's components to the profile.
     */
    private final List<Runnable>            applyList   = 
        new ArrayList<>();
    /** 
     * Links a label associated with a line property
     * (e.g. {@link #STROKE_LABEL},{@link #LENGTH_LABEL})
     * and LineProperSet type 
     * (e.g. {@link #axesSet}, {@link #gridLinesSet}
     * to a SpinnerDescriptor that controls editing
     * for the a property.
     * The key is the line property set type
     * concatenated with the property label,
     * for example "gridLinesSet + LENGTH_LABEL."
     * 
     * @see #makeSpinnerDesc
     * @see #addSpinner
     */
    private final Map<String,SpinnerDesc>   descMap     = 
        new HashMap<>();
    /** Initialized in the constructor. */
    private final Profile   profile;

    /**
     * Constructor.
     * Initializes this ProfileEditor.
     * 
     * @param profile   profile to edit
     */
    public ProfileEditor( Profile profile )
    {
        super( new BorderLayout() );

        this.profile = profile;
        canvas = new ProfileEditorFeedback( profile );
        
        getDescMap();
        
        JScrollPane scrollPane      = new JScrollPane( getControlPanel() );
        add( BorderLayout.WEST, scrollPane );

        // The following logic gives the scroll pane a reasonable size.
        // The height is set as a percentage of the height of the
        // display, and the width is set to the width of the editor
        // plus the width of the vertical scrollbar.
        Dimension   scrollDim   = scrollPane.getPreferredSize();
        Dimension   sbarDim     = 
            scrollPane.getVerticalScrollBar().getPreferredSize();
        Dimension   screenSize  = 
            Toolkit.getDefaultToolkit().getScreenSize();
        scrollDim.height = (int)(.6 * screenSize.height);
        scrollDim.width += sbarDim.width;
        scrollPane.setPreferredSize( scrollDim );
    }
    
    /**
     * Gets this object's Profile.
     * 
     * @return   this object's Profile
     */
    public Profile getProfile()
    {
        return profile;
    }
    
    /**
     * Returns the feedback component.
     * 
     * @return  the feedback component.
     */
    public JComponent getFeedBack()
    {
        return canvas;
    }

    /**
     * Applies all edits
     * to the PropertyManager
     */
    public void apply()
    {
        applyList.forEach( i -> i.run() );
        profile.apply();
    }
    
    /**
     * Discards all edits present in the GUI,
     * and sets the properties of the profile
     * to the values stored by the PropertyManager.
     */
    public void reset()
    {
        profile.reset();
        refresh();
    }
    
    public void refresh()
    {
        resetList.forEach( i -> i.run() );
        repaint();
    }
    
    /**
     * Creates a panel with a vertical layout
     * that contains editors for the grid unit,
     * the main window property set, and all the 
     * line property sets.
     * 
     * @return  a panel containing all the property editors
     */
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      inner   =
            BorderFactory.createBevelBorder( BevelBorder.LOWERED );
        Border      outer   =
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        Border      border  =
            BorderFactory.createCompoundBorder( outer, inner );
        panel.setBorder( border );
        panel.setLayout( layout );
        
        panel.add( getNamePanel() );
        panel.add( getGridPanel() );
        
        LinePropertySet propSet;
        
        propSet = profile.getLinePropertySet( axesSet );
        JPanel  axisPanel    =
            getPropertyPanel( AXES_TITLE, propSet );
        panel.add( axisPanel );
        
        propSet = profile.getLinePropertySet( gridLinesSet );
        JPanel  gridLinePanel    =
            getPropertyPanel( GRID_LINES_TITLE, propSet );
        panel.add( gridLinePanel );

        propSet = profile.getLinePropertySet( ticMajorSet );
        JPanel  majorTicPanel    =
            getPropertyPanel( MAJOR_TICS_TITLE, propSet );
        panel.add( majorTicPanel );
        
        propSet = profile.getLinePropertySet( ticMinorSet );
        JPanel  minorTicPanel    =
            getPropertyPanel( MINOR_TICS_TITLE, propSet );
        panel.add( minorTicPanel );
        
        return panel;
    }
    
    /**
     * Constructs a panel that can be used
     * to edit a LinePropertySet.
     * 
     * @param title     
     *      the title for the panel (Grid Lines, Tic Major, etc.).
     * @param propSet   
     *      the property set to be edited by the constructed panel
     * 
     * @return  a panel that can be used to edit a LinePropertySet
     */
    private JPanel 
    getPropertyPanel( String title, LinePropertySet propSet )
    {
        int     rows    = 0;
        JPanel  panel   = new JPanel();
        String  type    = propSet.getClass().getSimpleName();
        if ( propSet.hasSpacing() )
        {
            addSpinner( type, SPACING_LABEL, panel );
            ++rows;
        }
        if ( propSet.hasLength() )
        {
            addSpinner( type, LENGTH_LABEL, panel );
            ++rows;
        }
        if ( propSet.hasStroke() )
        {
            addSpinner( type, STROKE_LABEL, panel );
            ++rows;
        }
        if ( propSet.hasDraw() )
        {
            addDraw( propSet, panel );
            ++rows;
        }
        if ( propSet.hasColor() )
        {
            addColorEditor( propSet, panel );
            ++rows;
        }
        
        Border      lineBorder  = 
            BorderFactory.createLineBorder( Color.BLACK );
        Border      border      = 
            BorderFactory.createTitledBorder( lineBorder, title );
        GridLayout  layout  = new GridLayout( rows, 2, 3, 0 );
        panel.setName( title );
        panel.setBorder( border );
        panel.setLayout( layout );
        return panel;
    }

    /**
     * Returns a panel containing the grid unit 
     * and main window editors.
     * 
     * @return  a panel containing the grid unit editor
     */
    private JPanel getGridPanel()
    {
        GraphPropertySet    propSet = profile.getMainWindow();
        JPanel      panel   = new JPanel( new GridLayout( 5, 2, 3, 0 ) );
        SpinnerDesc gUnit   = descMap.get( GRID_UNIT_LABEL );
        SpinnerDesc width   = descMap.get( WIDTH_LABEL );
        panel.setName( GRID_TITLE );
        panel.add( gUnit.label );
        panel.add( gUnit.spinner );
        panel.add( width.label );
        panel.add( width.spinner );
        addColorEditor( propSet, panel );
        addFontEditor( propSet, panel );
        addDraw( propSet, panel );
        
        Border      lineBorder  = 
            BorderFactory.createLineBorder( Color.BLACK );
        Border      border      = 
            BorderFactory.createTitledBorder( lineBorder, GRID_TITLE );
        panel.setBorder(border);
        return panel;
    }
    
    private JPanel getNamePanel()
    {
        Dimension       spacer  = new Dimension( 5, 0 );
        Border          border  = 
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        JPanel          panel   = new JPanel();
        LayoutManager   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( Box.createRigidArea( spacer ) );
        JTextField      nameField   = new JTextField( 10 );
        panel.add( nameField );
        nameField.setName( NAME_LABEL );
        nameField.setText( profile.getName() );

        Runnable        toProfile   = 
            () -> profile.setName( nameField.getText() );
        Runnable        toComponent = 
            () -> nameField.setText( profile.getName() ); 
            
        nameField.addActionListener( e -> toProfile.run() );
        nameField.addFocusListener( new FocusAdapter() {
            @Override
            public void focusLost( FocusEvent evt )
            {
                toProfile.run();
            }
        });
            
        resetList.add( toComponent );
        applyList.add( toProfile );

        return panel;
    }
    
    /**
     * Locate a SpinnerDesc
     * and add its JSpinner
     * to a given panel.
     * The user passes the type of LinePropertySet
     * (e.g. {@link #axesSet}, {@link #gridLinesSet}
     * and the label on the spinner
     * (e.g. {@link #STROKE_LABEL}, {@link #LENGTH_LABEL}
     * and these are used to 
     * locate the SpinnerDesc.
     * 
     * @param type      the type of the target spinner descriptor
     * @param label     the label on the target JSpinner
     * @param panel     the given panel
     * 
     * @see #descMap
     */
    private void addSpinner( String type, String label, JPanel panel )
    {
        SpinnerDesc desc    = descMap.get( type + label );
        panel.add( desc.label );
        panel.add( desc.spinner );
    }
    
    /**
     * To a given panel, 
     * adds a ColorEditor
     * and links it to a given GraphPropertySet.
     * 
     * @param propSet   the given GraphPropertySet
     * @param panel     the given panel
     */
    private void addColorEditor( GraphPropertySet propSet, JPanel panel )
    {
        ColorEditor colorEditor = new ColorEditor();
        colorEditor.setColor( propSet.getBGColor() );
        panel.add( colorEditor.getColorButton() );
        panel.add( colorEditor.getTextEditor() );
        colorEditor.addActionListener( e -> {
            propSet.setBGColor( colorEditor.getColor().orElse( null ) );
            canvas.repaint();
        });
        
        Runnable        toComponent = () ->
            colorEditor.setColor( propSet.getBGColor());
        Runnable        toProfile   = () ->
            propSet.setBGColor( colorEditor.getColor().orElse( null ) );
        resetList.add( toComponent );
        applyList.add( toProfile );
    }
    
    /**
     * To a given panel, 
     * adds a ColorEditor
     * and links it to a given LinePropertySet.
     * 
     * @param propSet   the given LinePropertySet
     * @param panel     the given panel
     */
    private void addColorEditor( LinePropertySet propSet, JPanel panel )
    {
        ColorEditor     colorEditor = new ColorEditor();
        colorEditor.setColor( propSet.getColor() );
        JTextComponent  textEditor  = colorEditor.getTextEditor();
        panel.add( colorEditor.getColorButton() );
        panel.add( textEditor );
        colorEditor.addActionListener( e -> {
            propSet.setColor( colorEditor.getColor().orElse( null ) );
            canvas.repaint();
        });
        
        Runnable        toComponent = () ->
            colorEditor.setColor( propSet.getColor() );
        Runnable        toProfile   = () ->
            propSet.setColor( colorEditor.getColor().orElse( null ) );
        resetList.add( toComponent );
        applyList.add( toProfile );
    }

    /**
     * To a given panel, 
     * adds a FontEditor
     * and links it to a given graph property set.
     * 
     * @param propSet   the given GraphPropertySet
     * @param panel     the given panel
     */
    private void addFontEditor( GraphPropertySet propSet, JPanel panel )
    {
        FontEditorDialog    fontDialog  = 
            new FontEditorDialog( null, propSet );
        JButton editButton  = new JButton( EDIT_FONT_LABEL );
        editButton.setName( EDIT_FONT_LABEL );
        editButton.addActionListener( e -> showFontDialog( fontDialog ) );
        panel.add( new JLabel( "" ) );
        panel.add( editButton );
        
        Runnable    toComponent = () -> fontDialog.reset();
        resetList.add( toComponent );
    }
    
    private void addDraw( GraphPropertySet propSet, JPanel panel )
    {
        JLabel      label       = 
            new JLabel( DRAW_FONT_LABEL, SwingConstants.RIGHT );
        boolean     val         = propSet.isFontDraw();
        JCheckBox   checkBox    = new JCheckBox( "", val );
        checkBox.setName( DRAW_FONT_LABEL );
        panel.add( label );
        panel.add( checkBox );
        checkBox.addItemListener( e -> {
            propSet.setFontDraw( checkBox.isSelected() );
            canvas.repaint();
        });
        
        Runnable    toComponent = () ->
            checkBox.setSelected( propSet.isFontDraw() );
        Runnable    toProfile   = () ->
            propSet.setFontDraw( checkBox.isSelected() );
        resetList.add( toComponent );
        applyList.add( toProfile );
    }
    
    private void addDraw( LinePropertySet propSet, JPanel panel )
    {
        JLabel      label       = 
            new JLabel( DRAW_LABEL, SwingConstants.RIGHT );
        boolean     val         = propSet.getDraw();
        JCheckBox   checkBox    = new JCheckBox( "", val );
        checkBox.setName( DRAW_LABEL );
        panel.add( label );
        panel.add( checkBox );
        checkBox.addItemListener( e -> {
            propSet.setDraw( checkBox.isSelected() );
            canvas.repaint();
        });
        
        Runnable    toComponent = () ->
            checkBox.setSelected( propSet.getDraw() );
        Runnable    toProfile   = () ->
            propSet.setDraw( checkBox.isSelected() );
        resetList.add( toComponent );
        applyList.add( toProfile );
    }

    /**
     * Initializes the {@linkplain #descMap}.
     */
    private void getDescMap()
    {
        LinePropertySet         propSet = null;
        
        makeSpinnerDesc( null, GRID_UNIT_LABEL, 1 );
        makeSpinnerDesc( null, WIDTH_LABEL, 1 );
        
        propSet = profile.getLinePropertySet( axesSet );
        makeSpinnerDesc( propSet, STROKE_LABEL, 1 );
        
        propSet = profile.getLinePropertySet( gridLinesSet );
        makeSpinnerDesc( propSet, SPACING_LABEL, 1 );
        makeSpinnerDesc( propSet, STROKE_LABEL, 1 );
        
        propSet = profile.getLinePropertySet( ticMajorSet );
        makeSpinnerDesc( propSet, SPACING_LABEL, .25 );
        makeSpinnerDesc( propSet, STROKE_LABEL, 1 );
        makeSpinnerDesc( propSet, LENGTH_LABEL, 1 );
        
        propSet = profile.getLinePropertySet( ticMinorSet );
        makeSpinnerDesc( propSet, SPACING_LABEL, .25 );
        makeSpinnerDesc( propSet, STROKE_LABEL, 1 );
        makeSpinnerDesc( propSet, LENGTH_LABEL, 1 );
    }
    
    /**
     * Create a JSpinner descriptor (SpinnerDesc)
     * that links a spinner to a given LinePropertySet.
     * The user specifies the text for a label
     * to be displayed next to the spinner;
     * the text is also used to map the spinner
     * to the given LinePropertySet,
     * and must be one of the labels defined above
     * (see {@linkplain #STROKE_LABEL}, {@link #LENGTH_LABEL}, etc.).
     * 
     * @param propSet   the given LinePropertySet
     * @param labelText the text with which to label the spinner
     * @param step      the step value for the spinner
     */
    private void makeSpinnerDesc( 
        LinePropertySet propSet,
        String labelText,
        double step
    )
    {
        SpinnerDesc desc        = 
            new SpinnerDesc( propSet, labelText, step );
        
        String      propSetType = 
            propSet == null ? "" : propSet.getClass().getSimpleName();
        String      key         = propSetType + labelText;
        descMap.put( key, desc );
    }
    
    /**
     * Posts the FontEditorDialog
     * passing a copy of the current
     * main window GraphPropertySet.
     * If the dialog result is OK
     * the properties from the copy
     * are copied to the encapsulated
     * main window GraphPropertySet.
     * 
     * @param dialog    dialog to post
     */
    private void showFontDialog( FontEditorDialog dialog )
    {
        int result  = dialog.showDialog();
        if ( result == JOptionPane.OK_OPTION )
        {
            canvas.repaint();
        }
    }
    
    /**
     * Given a JSpinner with an underlying SpinnerNumberModel
     * get and return the spinner's value.
     * 
     * @param spinner   given JSpinner
     * 
     * @return  the given spinner's value
     * 
     * @throws 
     *      ComponentException if the given spinner is not based
     *      on a SpinnerNumberModel
     */
    private static float getFloat( JSpinner spinner )
    {
        SpinnerModel    model   = spinner.getModel();
        if ( !(model instanceof SpinnerNumberModel) )
            throw new ComponentException( "Invalid SpinnerModel" );
        Number          number  = ((SpinnerNumberModel)model).getNumber();
        float           val     = number.floatValue();
        return val;
    }

    /**
     * An object of this type is used to configure
     * the parameters of a JSpinner
     * that controls editing of a line property.
     * The user provides the associated LinePropertySet,
     * the text of a label that describes
     * the property the spinner is editing,
     * and the spinner's step value.
     * The property within the LinePropertySet to edit
     * is determined by the text of the label;
     * see <em>STROKE_LABEL, lengthLabel, et al.</em>
     * in the outer class.
     * 
     * @author Jack Straub
     */
    private class SpinnerDesc
    {
        private static final float      minVal  = 0;
        private static final float      maxVal  = Integer.MAX_VALUE;
        public final    JSpinner        spinner;
        public final    JLabel          label;
        private final   DoubleConsumer  profileSetter;
        private final   DoubleSupplier  profileGetter;
        
        public SpinnerDesc(
            LinePropertySet propSet,
            String labelText,
            double step
        )
        {
            // These variables are used to temporarily configure the
            // property getters and setters for this JSpinner. Once
            // the values are determined they are assigned to the
            // profileGetter and profileSetter instance variables.
            DoubleConsumer  tempSetter  = null;
            DoubleSupplier  tempGetter  = null;
            
            switch ( labelText )
            {
            case STROKE_LABEL:
                tempSetter = d -> propSet.setStroke( (float)d );
                tempGetter = () -> propSet.getStroke();
                break;
            case SPACING_LABEL:
                tempSetter = d -> propSet.setSpacing( (float)d );
                tempGetter = () -> propSet.getSpacing();
                break;
            case LENGTH_LABEL:
                tempSetter = d -> propSet.setLength( (float)d );
                tempGetter = () -> propSet.getLength();
                break;
            case GRID_UNIT_LABEL:
                tempSetter = d -> profile.setGridUnit( (float)d );
                tempGetter = () -> profile.getGridUnit();
                break;
            case WIDTH_LABEL:
                GraphPropertySet    graphSet    = profile.getMainWindow();
                tempSetter = d -> graphSet.setWidth( (float)d );
                tempGetter = () -> graphSet.getWidth();
                break;
            default:
                throw new ComponentException( "Invalid Label" );
            }
            
            profileSetter = tempSetter;
            profileGetter = tempGetter;
            label = new JLabel( labelText, SwingConstants.RIGHT );
            
            double              val     = profileGetter.getAsDouble();
            SpinnerNumberModel  model   = 
                new SpinnerNumberModel( val, minVal, maxVal, step );
            spinner = new JSpinner( model );
            spinner.setName( labelText );
            
            JComponent  editor          = spinner.getEditor();
            if ( !(editor instanceof DefaultEditor) )
                throw new ComponentException( "Unexpected editor type" );
            JTextField  textField   = 
                ((DefaultEditor)editor).getTextField();
            textField.setColumns( 6 );
            
            model.addChangeListener( e -> {
                float   value   = model.getNumber().floatValue();
                profileSetter.accept( value );
                canvas.repaint();
            });
            
            Runnable    toComponent = () ->
                spinner.setValue( profileGetter.getAsDouble() );
            Runnable    toProfile   = () ->
                profileSetter.accept( getFloat( spinner ) );
            resetList.add( toComponent );
            applyList.add( toProfile );
        }
    }
}
