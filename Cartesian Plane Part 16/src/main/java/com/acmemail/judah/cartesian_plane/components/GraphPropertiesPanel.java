package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class GraphPropertiesPanel extends JPanel
{
    /** Label for the Main Window radio button. */
    private static final String     mainWindow      = "Main Window";
    /** Label for the Top Margin radio button. */
    private static final String     topMargin       = "Top Margin";
    /** Label for the right margin radio button. */
    private static final String     rightMargin     = "Right Margin";
    /** Label for the left margin radio button. */
    private static final String     leftMargin      = "Left Margin";
    /** Label for the bottom margin radio button. */
    private static final String     bottomMargin    = "Bottom Margin";
    /** List of all radio button labels. */
    private static final String[]   rbLabels        =
    { mainWindow, topMargin, rightMargin, leftMargin, bottomMargin };
    
    /** 
     * Establishes, based on label, what subclass of 
     * GraphPropertySet is required.
     */
    private static final 
    Map<String, Supplier<GraphPropertySet>> typeMap = 
        Map.ofEntries( 
            Map.entry( mainWindow, GraphPropertySetMW::new ),
            Map.entry( topMargin, GraphPropertySetTM::new ),
            Map.entry( rightMargin, GraphPropertySetRM::new ),
            Map.entry( leftMargin, GraphPropertySetLM::new),
            Map.entry( bottomMargin, GraphPropertySetBM::new) 
        );

    /** Button group to manage radio buttons. */
    private final PButtonGroup<GraphPropertySet>    buttonGroup     = 
        new PButtonGroup<>();
    
    /** Component for editing font properties. */
    private final FontEditor    fontEditor      = new FontEditor();
    /** Component for editing background color. */
    private final ColorEditor   bgEditor        = new ColorEditor();
    
    public GraphPropertiesPanel()
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
        add( getLeftPanel(), BorderLayout.WEST );
        add( getRightPanel(), BorderLayout.EAST );
        add( getControlPanel(), BorderLayout.SOUTH );
    }
    
    /**
     * Gets a JPanel to be displayed
     * on the left side of the GraphPanel.
     * This includes all the radio buttons
     * arranged in a vertical layout.
     * 
     * @return  JPanel to be displayed on left side of GraphPanel
     */
    private JPanel getLeftPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        Border      empty   =
            BorderFactory.createEmptyBorder( 0, 0, 0, 50 );
        panel.setBorder( empty );
        
        Stream.of( rbLabels )
            .map( this::newRadioButton )
            .peek( panel::add )
            .forEach( buttonGroup::add );
        return panel;
    }
    
    /**
     * Helper method
     * for getLeftPanel.
     * Creates a PRadionButton 
     * with a given label,
     * and a GraphPropertySet object.
     * 
     * @param given label
     * 
     * @return  
     *      a PRadioButton configured with a label
     *      and a GraphPropertySet object
     *      
     * @see #typeMap
     */
    private PRadioButton<GraphPropertySet> newRadioButton( String label )
    {
        GraphPropertySet                set     = 
            typeMap.get( label ).get();
        PRadioButton<GraphPropertySet>  button  = 
            new PRadioButton<>( set, label );
        return button;
    }
    
    private JPanel getRightPanel()
    {
        JPanel  fontEditorPanel = fontEditor.getPanel();
        Border  lineBorder      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        Border  fontBorder      =
            BorderFactory.createTitledBorder( lineBorder, "Font" );
        fontEditorPanel.setBorder( fontBorder );
        
        JPanel  bgEditorPanel   = bgEditor.getPanel();
        Border  bgBorder        =
            BorderFactory.createTitledBorder( lineBorder, "Background Color" );
        bgEditorPanel.setBorder( bgBorder );
        
        JPanel      rightPanel  = new JPanel();
        BoxLayout   layout      = 
            new BoxLayout( rightPanel, BoxLayout.Y_AXIS );
        rightPanel.setLayout( layout );
        
        Dimension   spacer  = new Dimension( 1, 5 );
        rightPanel.add( fontEditorPanel );
        rightPanel.add( Box.createRigidArea( spacer ) );
        rightPanel.add( bgEditorPanel );
        
        return rightPanel;
    }
    
    private JPanel getControlPanel()
    {
        JPanel  panel   = new JPanel();
        JButton reset   = new JButton( "Reset" );
        JButton apply   = new JButton( "Apply" );
        JButton close   = new JButton( "Close" );
        close.addActionListener( e -> System.exit( 0 ) );
        
        panel.add( reset );
        panel.add( apply );
        panel.add( close );
        
        return panel;
    }
}
