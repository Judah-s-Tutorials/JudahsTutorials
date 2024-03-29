package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.ColorEditor;
import com.acmemail.judah.cartesian_plane.components.LengthFeedback;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.components.PButtonGroup;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.components.SpacingFeedback;
import com.acmemail.judah.cartesian_plane.components.StrokeFeedback;

/**
 * This panel
 * builds the GUI
 * for the LinePropertiesPanel
 * without putting in
 * any of the management logic.
 * In other words,
 * if you display this panel
 * you'll see what 
 * the GUI should look like,
 * but the control buttons
 * (Reset, Apply, Close)
 * aren't hooked up.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class LinePropertiesPanelGUI extends JPanel
{
    /** Label for the minor tics radio button. */
    private static final String minorTics   = "Minor Tics";
    /** Label for the major tics radio button. */
    private static final String majorTics   = "Major Tics";
    /** Label for the grid lines radio button. */
    private static final String gridLines   = "Grid Lines";
    /** Label for axes radio button. */
    private static final String axes        = "Axes";
    
    /** 
     * Establishes, based on label, what subclass of 
     * LinePropertySet is required.
     */
    private static final 
    Map<String, Supplier<LinePropertySet>> typeMap = 
        Map.ofEntries( 
            Map.entry( minorTics, LinePropertySetTicMinor::new ),
            Map.entry( majorTics, LinePropertySetTicMajor::new ),
            Map.entry( gridLines, LinePropertySetGridLines::new ),
            Map.entry( axes, LinePropertySetAxes::new) 
        );

    /** 
     * Margin around the edge of this panel; 
     * implemented via an EmptyBorder.
     */
    private static final int        margin      = 10;
    
    /** Collection of all radio buttons in the panel. */
    private final PButtonGroup<LinePropertySet> buttonGroup =
        new PButtonGroup<>();
    
    /**
     * Constructor.
     * Fully configures the line properties GUI.
     */
    public LinePropertiesPanelGUI()
    {
        super( new BorderLayout() );
        Border  lineBorder      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        Border  titledBorder    = 
            BorderFactory.createTitledBorder( lineBorder, "Lines" );
        Border  emptyBorder     =
            BorderFactory.createEmptyBorder( 
                margin, 
                margin, 
                margin, 
                margin 
            );
        Border  border          =
            BorderFactory.createCompoundBorder( 
                emptyBorder, 
                titledBorder 
            );
        setBorder( border );
        
        add( getMainPanel(), BorderLayout.CENTER );
        add( getControlPanel(), BorderLayout.SOUTH );
        buttonGroup.selectIndex( 0 );
    }
    
    /**
     * Configures the main panel
     * for this GUI.
     * It consist
     * of a panel
     * containing the radio buttons
     * on the left,
     * and the feedback components
     * on the right.
     * 
     * @return  the main panel
     */
    private JPanel getMainPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        Component horSpace  =
            Box.createRigidArea( new Dimension( 25, 0 ) );
        
        panel.setLayout( layout );
        panel.add( getRadioButtonPanel() );
        panel.add( horSpace );
        panel.add( new PropertiesPanel() );
        return panel;
    }
    
    /**
     * Configures the radio button panel.
     * This consists of 
     * four PRadioButtons
     * arranged vertically.
     * 
     * @return the radio button panel
     */
    private JPanel getRadioButtonPanel()
    {
        JPanel  panel   = new JPanel( new GridLayout( 4, 1 ) );
        Stream.of( axes, majorTics, minorTics, gridLines )
            .map( this::newRadioButton )
            .peek( panel::add )
            .forEach( buttonGroup::add );
        return panel;
    }
    
    /**
     * Helper method
     * for getRadioButtonPanel.
     * Creates a PRadionButton 
     * with a given label,
     * and a LinePropertySet object.
     * 
     * @param label the given label
     * 
     * @return  
     *      a PRadioButton configured with a label
     *      and a LinePropertySet object
     *      
     * @see #typeMap
     */
    private PRadioButton<LinePropertySet> newRadioButton( String label )
    {
        LinePropertySet                 set     = 
            typeMap.get( label ).get();
        PRadioButton<LinePropertySet>   button  = 
            new PRadioButton<>( set, label );
        return button;
    }
    
    /**
     * Configures a panel
     * containing the control buttons.
     * This consists of 
     * the Apply, Reset and Close buttons
     * arranged horizontally.
     * 
     * @return  the control button panel
     */
    private JPanel getControlPanel()
    {
        JPanel  panel   = new JPanel();
        Border  border  = BorderFactory.createEmptyBorder( 10, 0, 0, 0 );
        panel.setBorder( border );
        
        JButton applyButton = new JButton( "Apply" );
        JButton resetButton = new JButton( "Reset" );
        JButton closeButton = new JButton( "Close" );
        panel.add( applyButton );
        panel.add( resetButton );
        panel.add( closeButton );
        
        applyButton.addActionListener( this::applyAction );
        resetButton.addActionListener( this::resetAction );
        closeButton.addActionListener( this::closeAction );
        
        return panel;
    }
    
    /**
     * Action method
     * that is executed
     * when the Apply button pushed.
     * 
     * @param evt   
     *      event object associated with the ActionEvent
     *      that caused this method to be invoked; not used
     */
    private void applyAction( ActionEvent evt )
    {
        
    }
    
    /**
     * Action method
     * that is executed
     * when the Reset button pushed.
     * 
     * @param evt   
     *      event object associated with the ActionEvent
     *      that caused this method to be invoked; not used
     */
    private void resetAction( ActionEvent evt )
    {
        
    }
    
    /**
     * Action method
     * that is executed
     * when the Close button pushed.
     * 
     * @param evt   
     *      event object associated with the ActionEvent
     *      that caused this method to be invoked; not used
     */
    private void closeAction( ActionEvent evt )
    {
        
    }

    /**
     * Encapsulation of the panel
     * that contains all the configuration
     * controls for the parent panel.
     * 
     * @author Jack Straub
     */
    /**
     * @author Jack Straub
     */
    /**
     * @author Jack Straub
     */
    /**
     * @author Jack Straub
     */
    private class PropertiesPanel extends JPanel 
        implements ItemListener
    {
        /** Default value for configuring all spinners. */
        private static final float  defVal          = 1.0f;
        /** Minimum value for configuring all spinners. */
        private static final float  defMin          = 0.0f;
        /** Maximum value for configuring all spinners. */
        private static final float  defMax          = 500.0f;
        /** Step value for configuring all spinners. */
        private static final float  defStep         = .1f;
        
        /** The text for the label on the StrokeFeedback component. */
        private static final String strokeText      = "Stroke";
        /** The text for the label on the LengthFeedback component. */
        private static final String lengthText      = "Length";
        /** The text for the label on the SpacingFeedback component. */
        private static final String spacingText     = "Spacing";
        /** The text for the label on the Draw check button. */
        private static final String drawText        = "Draw";
        
        /** Label to accompany the StrokeFeedback component. */
        private final JLabel                strokeLabel     = 
            new JLabel( strokeText, SwingConstants.RIGHT );
        /** Spinner model to accompany the StrokeFeedback spinner. */
        private final SpinnerNumberModel    strokeModel     =
            new SpinnerNumberModel( defVal, defMin, defMax, defStep );
        /** Spinner for control of the StrokeFeedback component. */
        private final JSpinner              strokeSpinner   = 
            new JSpinner( strokeModel );
        /** The StrokeFeedback component. */
        private final StrokeFeedback  strokeFB      = 
            new StrokeFeedback( () -> doubleValue( strokeModel ) );

        /** Label to accompany the LengthFeedback component. */
        private final JLabel        lengthLabel     = 
            new JLabel( lengthText, SwingConstants.RIGHT );
        /** Spinner model to accompany the LengthFeedback spinner. */
        private final SpinnerNumberModel    lengthModel     =
            new SpinnerNumberModel( defVal, defMin, defMax, defStep );
        /** Spinner for control of the LengthFeedback component. */
        private final JSpinner              lengthSpinner   =
            new JSpinner( lengthModel );
        /** The LengthFeedback component. */
        private final LengthFeedback lengthFB       = 
            new LengthFeedback( () -> doubleValue( lengthModel ) );

        /** Label to accompany the SpacinFeedback component. */
        private final JLabel                spacingLabel    = 
            new JLabel( spacingText, SwingConstants.RIGHT );
        /** Spinner model to accompany the SpacingFeedback spinner. */
        private final SpinnerNumberModel    spacingModel    =
            new SpinnerNumberModel( defVal, defMin, defMax, defStep );
        /** Spinner for control of the SpacingFeedback component. */
        private final JSpinner              spacingSpinner  =
            new JSpinner( spacingModel );
        /** The SpacingFeedback component. */
        private final SpacingFeedback   spacingFB   = 
            new SpacingFeedback( () -> doubleValue( spacingModel ) );
        
        /** Editor for managing colors. */
        private final ColorEditor   colorEditor     = new ColorEditor();
        /** Push button from the ColorEdtor. */
        private final JButton       colorButton     = 
            colorEditor.getColorButton();
        /** Text field from the color editor. */
        private final JTextField    colorField      = 
            colorEditor.getTextEditor();
        /** Feedback window from the color editor. */
        private final JComponent    colorFB         = 
            colorEditor.getFeedback();  
                
        /** Label to identify the Draw check box. */
        private final JLabel        drawLabel       = 
            new JLabel( drawText, SwingConstants.RIGHT );
        /** The draw check box. */
        private final JCheckBox     drawToggle      = new JCheckBox();
        
        /**
         * Constructor.
         * Fully configures
         * the panel containing
         * the properties components.
         */
        private PropertiesPanel()
        {
            super( new GridLayout( 5, 3, 5, 3 ) );
            
            // sanity check; radio buttons must be created prior to
            // this class being instantiated.
            if ( buttonGroup.getButtonCount() < 1 )
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
            
            add( drawLabel );
            add( drawToggle );
            add( new JLabel() ); // placeholder
            
            buttonGroup.getButtons()
                .forEach( b -> b.addItemListener( this ) );
        }
        
        /**
         * Convenience method
         * for configuring the DoubleSupplier
         * for the feedback components.
         * For a given SpinnerNumberModel,
         * returns the encapsulated double value.
         * 
         * @param model the given SpinnerNumberModel
         * 
         * @return  
         *      the double value encapsulated in 
         *      the given SpinnerNumberModel
         */
        private Double doubleValue( SpinnerNumberModel model )
        {
            Double  value   = model.getNumber().doubleValue();
            return value;
        }
        
        /**
         * Method to listen for 
         * ItemListener events.
         * Associated with this panel's
         * radio button objects.
         * 
         * @param evt   
         *      event object associated with 
         *      ItemListener activation
         */
        @Override
        public void itemStateChanged(ItemEvent evt)
        {
        }        
    }
}
