package com.acmemail.judah.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class LinePanel extends JPanel
{
    /** Identifies the stroke spinner.  */
    public static final String  STROKE_SPINNER_CN   = "StrokeSpinner";
    /** Identifies the length spinner.  */
    public static final String  LENGTH_SPINNER_CN   = "LengthSpinner";
    /** Identifies the spacing spinner.  */
    public static final String  SPACING_SPINNER_CN  = "SpacingSpinner";
    /** Identifies the color text field.  */
    public static final String  COLOR_FIELD_CN      = "ColorField";
    
    private final ButtonGroup   lineGroup   = new ButtonGroup();
    private final RadioBoxPanel     radioBoxPanel   = new RadioBoxPanel();
    private final PropertiesPanel   propertiesPanel = new PropertiesPanel();
    
    private JRadioButton getSelectedButton()
    {
        JRadioButton    button  =
            Collections.list( lineGroup.getElements() )
            .stream()
            .filter( b -> b.isSelected() )
            .findFirst()
            .map( b -> (JRadioButton)b )
            .orElse( null );
        return button;
    }

    public LinePanel()
    {
        super( new BorderLayout() );
        Border  lineBorder      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        Border  titledBorder    = 
            BorderFactory.createTitledBorder( lineBorder, "Lines" );
        Border  emptyBorder     =
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        Border  border          =
            BorderFactory.createCompoundBorder( emptyBorder, titledBorder );
        
        setBorder( border );
        add( new MainPanel(), BorderLayout.CENTER );
        add( new ButtonPanel(), BorderLayout.SOUTH );
        
        lineGroup.
            getElements().
            nextElement().
            setSelected( true );
    }

    private class MainPanel extends JPanel
    {
        public MainPanel()
        {
            BoxLayout layout    = new BoxLayout( this, BoxLayout.X_AXIS );
            Component horSpace  =
                Box.createRigidArea( new Dimension( 50, 0 ) );
            
            setLayout( layout );
            add( radioBoxPanel );
            add( horSpace );
            add( propertiesPanel );
        }
    }
    
    private class RadioBoxPanel extends JPanel 
    {
        private static final String minorTics   = "Minor Tics";
        private static final String majorTics   = "Major Tics";
        private static final String gridLines   = "Grid Lines";
        private static final String axes        = "Axes";
        
        private static final String[]   labels  =
            { axes, majorTics, minorTics, gridLines };
        private static final Map<String,String> typeMap = new HashMap<>();
        static
        {
            typeMap.put( axes, PropertyManager.AXES );
            typeMap.put( majorTics, PropertyManager.MAJOR );
            typeMap.put( minorTics, PropertyManager.MINOR );
            typeMap.put( gridLines, PropertyManager.GRID );
        }
        
        public RadioBoxPanel()
        {
            super( new GridLayout( 4, 1 ) );
            
            Arrays.stream( labels )
                .map( this::newRadioButton )
                .peek( this::add )
                .forEach( b -> lineGroup.add( b ) );
        }
        
        private JRadioButton newRadioButton( String label )
        {
            JRadioButton    button  = new JRadioButton( label );
            String          type    = typeMap.get( label );
            LinePropertySet set     = new LinePropertySet( type );
            LinePropertySet.putPropertySet( button, set );
            return button;
        }
    }
    
    private class PropertiesPanel extends JPanel 
        implements ItemListener
    {
        private static final float  defVal      = 1.0f;
        private static final float  defMin      = 0.0f;
        private static final float  defMax      = 500.0f;
        private static final float  defStep     = .1f;
        
        private static final String stroke      = "Stroke";
        private static final String length      = "Length";
        private static final String spacing     = "Spacing";
        
        private final ColorEditor   colorEditor     = new ColorEditor();
        private final JLabel        strokeLabel     = 
            new JLabel( stroke, SwingConstants.RIGHT );
        private final JLabel        spacingLabel    = 
            new JLabel( spacing, SwingConstants.RIGHT );
        private final JLabel        lengthLabel     = 
            new JLabel( length, SwingConstants.RIGHT );
        private final JButton       colorButton     = 
            colorEditor.getColorButton();// JButton( color );
        
        private final JSpinner      strokeSpinner   = getDefSpinner();
        private final JSpinner      lengthSpinner   = getDefSpinner();
        private final JSpinner      spacingSpinner  = getDefSpinner();         
        private final JTextField    colorField      = 
            colorEditor.getTextComponent();// JTextField();
        
        private final StrokeFeedback  strokeFB      = 
            new StrokeFeedback( strokeSpinner, strokeLabel );
        private final JComponent    colorFB        = 
            colorEditor.getFeedbackComponent();// ColorFeedback( colorField, colorButton );
        private final LengthFeedback lengthFB       = 
            new LengthFeedback( lengthSpinner, lengthLabel );
        private final SpacingFeedback   spacingFB   = 
            new SpacingFeedback( spacingSpinner, spacingLabel );
        
        public PropertiesPanel()
        {
            super( new GridLayout( 4, 3, 5, 3 ) );
            
            // sanity check; radio buttons must be created prior to
            // this class being instantiated.
            if ( lineGroup.getButtonCount() < 1 )
                throw new RuntimeException( "no radio buttons found" );
            
            add( strokeLabel );
            add( strokeSpinner );
            add( strokeFB );
            strokeSpinner.setName( STROKE_SPINNER_CN );
            strokeSpinner.addChangeListener( e -> strokeFB.repaint() );
            
            add( lengthLabel );
            add( lengthSpinner );
            add( lengthFB );
            lengthSpinner.setName( LENGTH_SPINNER_CN );
            lengthSpinner.addChangeListener( e -> lengthFB.repaint() );
            
            add( spacingLabel );
            add( spacingSpinner );
            add( spacingFB );
            spacingSpinner.setName( SPACING_SPINNER_CN );
            spacingSpinner.addChangeListener( e -> spacingFB.repaint() );
            
            add( colorButton );
            add( colorField );
            add( colorFB );
            colorField.setName( COLOR_FIELD_CN );
                //colorField.addActionListener( e -> colorFB.repaint() );
                //colorButton.addActionListener( e -> showColorDialog() );
            
            lineGroup.getElements()
                .asIterator()
                .forEachRemaining( b -> b.addItemListener( this ) );
        }
        
        private JSpinner getDefSpinner()
        {
            SpinnerNumberModel  spinnerModel    =
                new SpinnerNumberModel( defVal, defMin, defMax, defStep );
            JSpinner            spinner = new JSpinner( spinnerModel );
            return spinner;
        }

//        private void showColorDialog()
//        {
//            Color   color   = colorDialog.showDialog();
//            if ( color != null )
//            {
//                String      text    = Feedback.getText( color );
//                colorField.setText( text );
//                colorFB.repaint();
//            }
//        }

        @Override
        public void itemStateChanged( ItemEvent evt )
        {
            Object  source      = evt.getSource();
            int     stateChange = evt.getStateChange();
            
            if ( source instanceof JRadioButton )
            {
                JRadioButton    button  = (JRadioButton)source;
                if ( stateChange == ItemEvent.DESELECTED )
                    commit( button );
                else if ( stateChange == ItemEvent.SELECTED )
                {
                    LinePropertySet set = 
                        LinePropertySet.getPropertySet( button );
                    configureProperty(
                        strokeFB,
                        set::hasStroke,
                        set::getStroke,
                        strokeSpinner::setValue
                    );
                    configureProperty(
                        spacingFB,
                        set::hasSpacing,
                        set::getSpacing,
                        spacingSpinner::setValue
                    );
                    configureProperty(
                        lengthFB,
                        set::hasLength,
                        set::getLength,
                        lengthSpinner::setValue
                    );
                    configureProperty(
                        colorFB,
                        set::hasColor,
                        set::getColor,
                        this::setColor
                    );
                }
            }
        }
        
        /**
         * Copies the current operator input
         * into the LinePropertySet object
         * of a given button.
         * 
         * @param button    the given button
         */
        private void commit( JRadioButton button )
        {
            LinePropertySet set = 
                LinePropertySet.getPropertySet( button );
            storeProperty(
                strokeSpinner,
                set::hasStroke,
                set::setStroke
            );
            storeProperty(
                lengthSpinner,
                set::hasLength,
                set::setLength
            );
            storeProperty(
                spacingSpinner,
                set::hasSpacing,
                set::setSpacing
            );
            storeProperty(
                colorField,
                set::hasColor,
                set::setColor
            );
        }
        
        private void configureProperty(
            JComponent fbComp,
            BooleanSupplier hasProp,
            DoubleSupplier getter,
            DoubleConsumer setter
        )
        {
            if ( !hasProp.getAsBoolean() )
            {
                fbComp.setEnabled( false );
            }
            else
            {
                fbComp.setEnabled( true );
                double  val = getter.getAsDouble();
                setter.accept( val );
            }
        }
        
        private void configureProperty(
            JComponent fbComp,
            BooleanSupplier hasProp,
            Supplier<Color> getter,
            Consumer<Color> setter
        )
        {
            if ( !hasProp.getAsBoolean() )
                fbComp.setEnabled( false );
            else
            {
                fbComp.setEnabled( true );
                setter.accept( getter.get() );
            }
        }
        
        private void storeProperty(
            JSpinner spinner,
            BooleanSupplier hasProp,
            DoubleConsumer setter
        )
        {
            if ( hasProp.getAsBoolean() )
            {
                try
                {
                    // Make sure operator input is committed.
                    // If commit fails (with a ParseException) abandon the store
                    // operation; leave LinePropertySet unchanged.
                    spinner.commitEdit();
                    OptionalDouble  optVal  = Feedback.getValue( spinner );
                    if ( optVal.isPresent() )
                        setter.accept( optVal.getAsDouble() );
                }
                catch ( ParseException exc )
                {
                    System.err.println( exc.getMessage() );
                }
            }
        }
        
        private void storeProperty(
            JTextField source,
            BooleanSupplier hasProp,
            Consumer<Color> setter
        )
        {
            if ( hasProp.getAsBoolean() )
            {
                String          text        = source.getText();
                Optional<Color> colorOpt    = Feedback.getColor( text );
                if ( colorOpt.isPresent() )
                    setter.accept( colorOpt.get() );
            }
        }
        
        private void setColor( Color color )
        {
            String  text    = Feedback.getText( color );
            colorField.setText( text );
            colorFB.repaint();
        }
    }
    
    private class ButtonPanel extends JPanel
    {
        public ButtonPanel()
        {
            Border  border  = BorderFactory.createEmptyBorder( 10, 0, 0, 0 );
            setBorder( border );
            
            JButton applyButton = new JButton( "Apply" );
            JButton resetButton = new JButton( "Reset" );
            add( applyButton );
            add( resetButton );
            
            applyButton.addActionListener( this::apply );
            resetButton.addActionListener( this::reset );
        }
        
        private void apply( ActionEvent evt )
        {
            // If necessary, commit the most recent changes
            JRadioButton    currButton  = getSelectedButton();
            if ( currButton == null )
                throw new ComponentException( "No category selected" );
            propertiesPanel.commit( currButton );
            
            Collections.list( lineGroup.getElements() )
                .stream()
                .map( LinePropertySet::getPropertySet )
                .forEach( LinePropertySet::apply );
        }
        
        private void reset( ActionEvent evt )
        {
            Collections.list( lineGroup.getElements() )
            .stream()
            .map( LinePropertySet::getPropertySet )
            .forEach( LinePropertySet::reset );
        }
    }
}
