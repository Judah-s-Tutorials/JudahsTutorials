package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
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
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.ColorEditor;
import com.acmemail.judah.cartesian_plane.components.FontEditorDialog;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
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
    
    /** Text describing the GUI weight field. */
    private static final String weightLabel     = "Weight";
    /** Text describing the GUI lines/unit field. */
    private static final String lpuLabel        = "Lines/Unit";
    /** Text describing the GUI length field. */
    private static final String lenLabel        = "Length";
    /** Text describing the GUI draw field (for lines). */
    private static final String drawLabel       = "Draw";
    /** Text describing the GUI draw field (for text). */
    private static final String drawLabelsLabel = "Labels";
    
    /** Text describing the GUI grid unit panel. */
    private static final String gridUnitLabel   = "Grid Unit";
    /** Text describing the GUI axis panel. */
    private static final String axisLabel       = "Axes";
    /** Text describing the GUI major tics panel. */
    private static final String majorTicsLabel  = "Major Tics";
    /** Text describing the GUI minor tics panel. */
    private static final String minorTicsLabel  = "Minor Tics";
    /** Text describing the GUI grid lines panel. */
    private static final String gridLinesLabel  = "Grid Lines";
    
    /** The component that the sample graph is drawn on. */
    private final Canvas        canvas;
    
    /** 
     * List of Runnables that set or reset the values of the 
     * GUI's JSpinners from the properties in the profile.
     * Initialized in the {@linkplain SpinnerDesc} constructor.
     */
    private final List<Runnable>            resetList   = 
        new ArrayList<>();
    /** 
     * Links a label associated with a line property
     * (e.g. {@link #weightLabel},{@link #lenLabel})
     * and LineProperSet type 
     * (e.g. {@link #axesSet}, {@link #gridLinesSet}
     * to a SpinnerDescriptor that controls editing
     * for the a property.
     * The key is the line property set type
     * concatenated with the property label,
     * for example "gridLinesSet + lenLabel."
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
     * Initializes the editor dialog.
     * 
     * @param profile   profile to edit
     */
    public ProfileEditor( Profile profile )
    {
        super( new BorderLayout() );

        this.profile = profile;
        canvas = new Canvas( profile );
        
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
     * and the PropertyManager
     */
    public void apply()
    {
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
    }

    /**
     * Creates a panel with a vertical layout
     * that encloses the panel containing
     * the property editors.
     * 
     * @return  
     *      panel enclosing the panel containing the property editors
     */
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border  =
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        panel.setBorder( border );
        panel.setLayout( layout );
        
        panel.add( getControls() );
                
        return panel;
    }
    
    /**
     * Creates a panel with a vertical layout
     * that contains editors for the grid unit,
     * the main window property set, and all the 
     * line property sets.
     * 
     * @return  a panel containing all the property editors
     */
    private JPanel getControls()
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
        
        panel.add( getGridPanel() );
        
        LinePropertySet propSet;
        
        propSet = profile.getLinePropertySet( axesSet );
        JPanel  axisPanel    =
            getPropertyPanel( axisLabel, propSet );
        panel.add( axisPanel );
        
        propSet = profile.getLinePropertySet( gridLinesSet );
        JPanel  gridLinePanel    =
            getPropertyPanel( gridLinesLabel, propSet );
        panel.add( gridLinePanel );

        propSet = profile.getLinePropertySet( ticMajorSet );
        JPanel  majorTicPanel    =
            getPropertyPanel( majorTicsLabel, propSet );
        panel.add( majorTicPanel );
        
        propSet = profile.getLinePropertySet( ticMinorSet );
        JPanel  minorTicPanel    =
            getPropertyPanel( minorTicsLabel, propSet );
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
            addSpinner( type, lpuLabel, panel );
            ++rows;
        }
        if ( propSet.hasLength() )
        {
            addSpinner( type, lenLabel, panel );
            ++rows;
        }
        if ( propSet.hasStroke() )
        {
            addSpinner( type, weightLabel, panel );
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
        panel.setBorder(border);
        panel.setLayout( layout );
        return panel;
    }
    
    /**
     * Locate a SpinnerDesc
     * and add its JSpinner
     * to a given panel.
     * The user passes the type of LinePropertySet
     * (e.g. {@link #axesSet}, {@link #gridLinesSet}
     * and the label on the spinner
     * (e.g. {@link #weightLabel}, {@link #lenLabel}
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
     * Returns a panel containing the grid unit 
     * and main window editors.
     * 
     * @return  a panel containing the grid unit editor
     */
    private JPanel getGridPanel()
    {
        GraphPropertySet    propSet = profile.getMainWindow();
        JPanel      panel   = new JPanel( new GridLayout( 3, 2, 3, 0 ) );
        SpinnerDesc desc    = descMap.get( gridUnitLabel );
        panel.add( desc.label );
        panel.add( desc.spinner );
        addColorEditor( propSet, panel );
        addFontEditor( propSet, panel );
        addDraw( propSet, panel );
        
        Border      lineBorder  = 
            BorderFactory.createLineBorder( Color.BLACK );
        Border      border      = 
            BorderFactory.createTitledBorder( lineBorder, "Grid" );
        GridLayout  layout  = new GridLayout( 4, 2, 3, 0 );
        panel.setBorder(border);
        panel.setLayout( layout );
        return panel;
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
        
        Supplier<Color> colorGetter = propSet::getBGColor;
        Runnable        runner      = 
            () -> colorEditor.setColor( colorGetter.get() );
        resetList.add( runner );
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
        ColorEditor colorEditor = new ColorEditor();
        colorEditor.setColor( propSet.getColor() );
        panel.add( colorEditor.getColorButton() );
        panel.add( colorEditor.getTextEditor() );
        colorEditor.addActionListener( e -> {
            propSet.setColor( colorEditor.getColor().orElse( null ) );
            canvas.repaint();
        });
        
        Supplier<Color> colorGetter = propSet::getColor;
        Runnable        runner      = 
            () -> colorEditor.setColor( colorGetter.get() );
        resetList.add( runner );
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
        JButton editButton  = new JButton( "Edit Font" );
        editButton.addActionListener( e -> showFontDialog( fontDialog ) );
        panel.add( new JLabel( "" ) );
        panel.add( editButton );
    }
    
    private void addDraw( GraphPropertySet propSet, JPanel panel )
    {
        JLabel      label       = 
            new JLabel( drawLabelsLabel, SwingConstants.RIGHT );
        boolean     val         = propSet.isFontDraw();
        JCheckBox   checkBox    = new JCheckBox( "", val );
        panel.add( label );
        panel.add( checkBox );
        checkBox.addItemListener( e -> {
            propSet.setFontDraw( checkBox.isSelected() );
            canvas.repaint();
        });
        
        resetList.add( () -> checkBox.setSelected( propSet.isFontDraw() ) );
    }
    
    private void addDraw( LinePropertySet propSet, JPanel panel )
    {
        JLabel      label       = 
            new JLabel( drawLabel, SwingConstants.RIGHT );
        boolean     val         = propSet.getDraw();
        JCheckBox   checkBox    = new JCheckBox( "", val );
        panel.add( label );
        panel.add( checkBox );
        checkBox.addItemListener( e -> {
            propSet.setDraw( checkBox.isSelected() );
            canvas.repaint();
        });
        
        resetList.add( () -> checkBox.setSelected( propSet.getDraw() ) );
    }

    /**
     * Initializes the {@linkplain #descMap}.
     */
    private void getDescMap()
    {
        LinePropertySet         propSet = null;
        
        makeSpinnerDesc( null, gridUnitLabel, 1 );
        
        propSet = profile.getLinePropertySet( axesSet );
        makeSpinnerDesc( propSet, weightLabel, 1 );
        
        propSet = profile.getLinePropertySet( gridLinesSet );
        makeSpinnerDesc( propSet, lpuLabel, 1 );
        makeSpinnerDesc( propSet, weightLabel, 1 );
        
        propSet = profile.getLinePropertySet( ticMajorSet );
        makeSpinnerDesc( propSet, lpuLabel, .25 );
        makeSpinnerDesc( propSet, weightLabel, 1 );
        makeSpinnerDesc( propSet, lenLabel, 1 );
        
        propSet = profile.getLinePropertySet( ticMinorSet );
        makeSpinnerDesc( propSet, lpuLabel, .25 );
        makeSpinnerDesc( propSet, weightLabel, 1 );
        makeSpinnerDesc( propSet, lenLabel, 1 );
    }
    
    /**
     * Create a JSpinner descriptor (SpinnerDesc)
     * that links a spinner to a given LinePropertySet.
     * The user specifies the text for a label
     * to be displayed next to the spinner;
     * the text is also used to map the spinner
     * to the given LinePropertySet,
     * and must be one of the labels defined above
     * (see {@linkplain #weightLabel}, {@link #lenLabel}, etc.).
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
     * An object of this type is used to configure
     * that parameters of a JSpinner
     * that controls editing of a line property.
     * The user provides the associated LinePropertySet,
     * the text of a label that describes
     * the property the spinner is editing,
     * and the spinner's step value.
     * The property within the LinePropertySet to edit
     * is determined by the text of the label;
     * see <em>weightLabel, lengthLabel, et al.</em>
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
        private final   DoubleConsumer  setter;
        private final   DoubleSupplier  getter;
        
        public SpinnerDesc(
            LinePropertySet propSet,
            String labelText,
            double step
        )
        {
            // These variables are used to temporarily configure the
            // property getters and setters for this JSpinner. Once
            // the values are determined they are assigned to the
            // getter and setter instance variables.
            DoubleConsumer  tempSetter  = null;
            DoubleSupplier  tempGetter  = null;
            
            switch ( labelText )
            {
            case weightLabel:
                tempSetter = d -> propSet.setStroke( (float)d );
                tempGetter = () -> propSet.getStroke();
                break;
            case lpuLabel:
                tempSetter = d -> propSet.setSpacing( (float)d );
                tempGetter = () -> propSet.getSpacing();
                break;
            case lenLabel:
                tempSetter = d -> propSet.setLength( (float)d );
                tempGetter = () -> propSet.getLength();
                break;
            case gridUnitLabel:
                tempSetter = d -> profile.setGridUnit( (float)d );
                tempGetter = () -> profile.getGridUnit();
                break;
            default:
                throw new ComponentException( "Invalid Label" );
            }
            
            setter = tempSetter;
            getter = tempGetter;
            label = new JLabel( labelText, SwingConstants.RIGHT );
            
            double              val     = getter.getAsDouble();
            SpinnerNumberModel  model   = 
                new SpinnerNumberModel( val, minVal, maxVal, step );
            spinner = new JSpinner( model );
            
            JComponent  editor          = spinner.getEditor();
            if ( !(editor instanceof DefaultEditor) )
                throw new ComponentException( "Unexpected editor type" );
            JTextField  textField   = 
                ((DefaultEditor)editor).getTextField();
            textField.setColumns( 6 );
            
            model.addChangeListener( e -> {
                float   value   = model.getNumber().floatValue();
                setter.accept( value );
                canvas.repaint();
            });
            
            resetList.add( 
                () -> spinner.setValue( getter.getAsDouble() )
            );
        }
    }
}
