package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.JSpinner.DefaultEditor;

import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

public class ProfileEditorDialog extends JDialog
{
    /** Text describing the GUI weight field. */
    private static final String weightLabel     = "Weight";
    /** Text describing the GUI lines/unit field. */
    private static final String lpuLabel        = "Lines/Unit";
    /** Text describing the GUI length field. */
    private static final String lenLabel        = "Length";
    /** Text describing the GUI draw field. */
    private static final String drawLabel       = "Draw";
    
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
    /** The object that controls detailed drawing on the canvas. */
    private final GraphManager  drawManager;

    /** 
     * List of Runnables that set or reset the values of the 
     * GUI's JSpinners from the properties in the profile.
     * Initialized in the {@linkplain SpinnerDesc} constructor.
     */
    private final List<Runnable>    resetList   = new ArrayList<>();

    /** Initialized in the constructor. */
    private final Profile   profile;

    public ProfileEditorDialog( Profile profile )
    {
        this.profile = profile;
        canvas = new Canvas( profile );
        drawManager = canvas.getDrawManager();
    }

    public void apply()
    {
        profile.apply();
    }

    public void reset()
    {
        profile.reset();
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
