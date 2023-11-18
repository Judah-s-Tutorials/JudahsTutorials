package com.acmemail.judah.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel
{
    /** Identifies the stroke spinner.  */
    public static final String  STROKE_SPINNER_CN   = "StrokeSpinner";
    /** Identifies the length spinner.  */
    public static final String  LENGTH_SPINNER_CN   = "LengthSpinner";
    /** Identifies the spacing spinner.  */
    public static final String  SPACING_SPINNER_CN  = "SpacingSpinner";
    /** Identifies the color text field.  */
    public static final String  COLOR_FIELD_CN      = "ColorField";
    
    private static final String STATUS_KEY          =
        "judah.JColorChooserDemo.status";
    
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
    
    private void setAndClose( int choice )
    {
        colorPane.putClientProperty( STATUS_KEY, choice );
        colorDialog.setVisible( false );
    }

    public GraphPanel()
    {
        super( new BorderLayout() );
        Border  lineBorder      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        Border  titledBorder    = 
            BorderFactory.createTitledBorder( lineBorder, "Graph" );
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

            radioBoxPanel.setAlignmentY( TOP_ALIGNMENT );
            propertiesPanel.setAlignmentY( TOP_ALIGNMENT );

            setLayout( layout );
            add( radioBoxPanel );
            add( horSpace );
            add( propertiesPanel );
        }
    }
    
    private class RadioBoxPanel extends JPanel 
    {
        private static final String graph           = "Graph";
        private static final String topMargin       = "Top Margin";
        private static final String rightMargin     = "Right Margin";
        private static final String bottomMargin    = "Bottom Margin";
        private static final String leftMargin      = "Right Margin";
        
        private static final String[]   labels  =
            { graph, topMargin, rightMargin, leftMargin, bottomMargin };
        private static final Map<String,String> typeMap = new HashMap<>();
        static
        {
            typeMap.put( bottomMargin, PropertyManager.AXES );
            typeMap.put( topMargin, PropertyManager.MAJOR );
            typeMap.put( graph, PropertyManager.MINOR );
            typeMap.put( rightMargin, PropertyManager.GRID );
        }
        
        public RadioBoxPanel()
        {
            //super( new GridLayout( 5, 1 ) );
            BoxLayout   layout  = new BoxLayout( this, BoxLayout.Y_AXIS );
            setLayout( layout );
            
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
    {
        public PropertiesPanel()
        {
            BoxLayout   layout  = new BoxLayout( this, BoxLayout.Y_AXIS );
            setLayout( layout );
            add( getFontPanel() );
            Dimension   dim     = new Dimension( 1, 5 );
            add( Box.createRigidArea( dim ) );
            add( getColorPanel() );
        }
    }
    
    private JPanel getFontPanel()
    {
        JPanel  panel   = new FontEditor().getPanel();
        Border  lineBorder      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        Border  titledBorder    = 
            BorderFactory.createTitledBorder( lineBorder, "Font" );
        panel.setBorder( titledBorder );

        return panel;
    }
    
    private JPanel getColorPanel()
    {
        JPanel  panel   = new ColorEditor().getGridPanel();
        Border  lineBorder      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        Border  titledBorder    = 
            BorderFactory.createTitledBorder( lineBorder, "Background Color" );
        panel.setBorder( titledBorder );

        return panel;
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
//            propertiesPanel.commit( currButton );
            
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