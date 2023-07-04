package com.acmemail.judah.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class LinePanel extends JPanel
{
    private static final String STATUS_KEY      =
        "judah.JColorChooserDemo.status";
    private static final String TYPE_KEY        =
        "judah.property_type";
    
    private final ButtonGroup   lineGroup   = new ButtonGroup();
    private final JColorChooser colorPane   = new JColorChooser();
    private final JDialog       colorDialog =
        JColorChooser.createDialog(
            null, 
            "Choose a Color", 
            true, 
            colorPane, 
            e -> setAndClose( JOptionPane.OK_OPTION ), 
            e -> setAndClose( JOptionPane.CANCEL_OPTION )
        );
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> build() );
    }
    
    public static void build()
    {
        JFrame      frame   = new JFrame( "Properties Test" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( new LinePanel() );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void setAndClose( int choice )
    {
        colorPane.putClientProperty( STATUS_KEY, choice );
        colorDialog.setVisible( false );
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
    
    private LinePropertySet getPropertySet( AbstractButton button )
    {
        Object  value   = button.getClientProperty( TYPE_KEY );
        if ( value == null )
            throw new RuntimeException( "LinePropertySet not found" );
        if ( !(value instanceof LinePropertySet) )
        {
            String  message =
                "Expected LinePropertySet, was "
                + value.getClass().getName();
            throw new RuntimeException( message );
        }
        return (LinePropertySet)value;
    }

    private class MainPanel extends JPanel
    {
        public MainPanel()
        {
            BoxLayout layout    = new BoxLayout( this, BoxLayout.X_AXIS );
            Component horSpace  =
                Box.createRigidArea( new Dimension( 50, 0 ) );
            
            setLayout( layout );
            add( new RadioBoxPanel() );
            add( horSpace );
            add( new PropertiesPanel() );
        }
    }
    
    private class RadioBoxPanel extends JPanel implements ActionListener
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
            typeMap.put( axes, PropertyManager.AXIS );
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
                .peek( b -> b.addActionListener( this ) )
                .forEach( b -> lineGroup.add( b ) );
        }

        @Override
        public void actionPerformed( ActionEvent evt )
        {
            Object  source  = evt.getSource();
            if ( !(source instanceof JRadioButton ) )
                System.err.println( "Expected radio button, got..." );
            else
            {
                String  text    = ((JRadioButton)source).getText();
                System.out.println( "Radio button: " + text );
            }
        }
        
        private JRadioButton newRadioButton( String label )
        {
            JRadioButton    button  = new JRadioButton( label );
            String          type    = typeMap.get( label );
            LinePropertySet set     = new LinePropertySet( type );
            button.putClientProperty( TYPE_KEY, set );
            return button;
        }
    }
    
    private class PropertiesPanel extends JPanel 
        implements ItemListener
    {
        private static final int    defVal      = 1;
        private static final int    defMin      = 0;
        private static final int    defMax      = 500;
        private static final int    defStep     = 1;
        
        private static final String stroke      = "Stroke";
        private static final String length      = "Length";
        private static final String spacing     = "Spacing";
        private static final String color       = "Color";
        
        private final JLabel        strokeLabel     = 
            new JLabel( stroke, SwingConstants.RIGHT );
        private final JLabel        spacingLabel    = 
            new JLabel( spacing, SwingConstants.RIGHT );
        private final JLabel        lengthLabel     = 
            new JLabel( length, SwingConstants.RIGHT );
        private final JButton       colorButton     = new JButton( color );
        
        private final JSpinner      strokeSpinner   = getDefSpinner();
        private final JSpinner      lengthSpinner   = getDefSpinner();
        private final JSpinner      spacingSpinner  = getDefSpinner();         
        private final JTextField    colorField      = new JTextField();
        
        private final StrokeFeedback  strokeFB      = 
            new StrokeFeedback( strokeSpinner, strokeLabel );
        private final ColorFeedback  colorFB        = 
            new ColorFeedback( colorField, colorButton );
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
            strokeSpinner.addChangeListener( e -> strokeFB.repaint() );
            
            add( lengthLabel );
            add( lengthSpinner );
            add( lengthFB );
            lengthSpinner.addChangeListener( e -> lengthFB.repaint() );
            
            add( spacingLabel );
            add( spacingSpinner );
            add( spacingFB );
            spacingSpinner.addChangeListener( e -> spacingFB.repaint() );
            
            add( colorButton );
            add( colorField );
            add( colorFB );
            colorField.addActionListener( e -> colorFB.repaint() );
            colorButton.addActionListener( e -> showColorDialog() );
            
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

        public void showColorDialog()
        {
            colorDialog.setVisible( true );
            Object  objStatus   = colorPane.getClientProperty( STATUS_KEY );
            if ( objStatus == null || !(objStatus instanceof Integer) )
                throw new RuntimeException( "Color error: " + objStatus );
            int intStatus       = (int)objStatus;
            System.out.println( intStatus );
            if ( intStatus == JOptionPane.OK_OPTION )
            {
                Color       color   = colorPane.getColor();
                String      text    = Feedback.getText( color );
                colorField.setText( text );
                colorFB.repaint();
            }
        }

        @Override
        public void itemStateChanged( ItemEvent evt )
        {
            Object  source  = evt.getSource();
            if ( source instanceof JRadioButton )
            {
                JRadioButton    button  = (JRadioButton)source;
                if ( button.isSelected() )
                {
                    LinePropertySet set = getPropertySet( button );
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
        
        private void configureProperty(
            JComponent fbComp,
            BooleanSupplier hasProp,
            IntSupplier getter,
            IntConsumer setter
        )
        {
            if ( !hasProp.getAsBoolean() )
            {
                fbComp.setEnabled( false );
            }
            else
            {
                fbComp.setEnabled( true );
                int val = getter.getAsInt();
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
            Collections.list( lineGroup.getElements() )
            .stream()
            .map( LinePanel.this::getPropertySet )
            .forEach( LinePropertySet::apply );
        }
        
        private void reset( ActionEvent evt )
        {
            Collections.list( lineGroup.getElements() )
            .stream()
            .map( LinePanel.this::getPropertySet )
            .forEach( LinePropertySet::reset );
        }
    }
}
