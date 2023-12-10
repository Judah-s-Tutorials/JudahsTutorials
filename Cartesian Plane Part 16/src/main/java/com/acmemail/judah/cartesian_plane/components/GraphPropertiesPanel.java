package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * Panel to be used
 * for managing the properties
 * of the Cartesian plane window.
 * These are the <em>main window,</em>
 * in which the graph
 * is displayed,
 * and the <em>top-, right-, bottom-</em 
 * and <em>left-margins</em>.
 * <p>
 * The area to manage
 * is determined by
 * selecting a radio button
 * on the left side 
 * of the panel.
 * The properties for that area
 * are displayed on the right.
 * Properties include
 * the <em>font</em> and <em>background color.</em>
 * The font can be further divided
 * into <em>name, size</em> and <em>style</em> properties.
 * For the style property
 * the operator can select
 * <em>italic</em> and/or <em>bold</em>
 * (or <em>neither</em>).
 * <p>
 * To apply select the <em>Apply</em> button
 * at the bottom of the panel.
 * To discard changes
 * select the <em>Reset</em> button.
 * If the GraphPropertiesPanel
 * resides in a dialog,
 * selecting the <em>Close</em> button
 * will closed the dialog.
 * If the panel does not
 * reside in a dialog,
 * selecting the close button
 * does nothing.
 * All data is preserved
 * if the enclosing dialog
 * is closed and reopened.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
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
    /** Component for editing font draw property. */
    private final JCheckBox     drawEditor      = new JCheckBox( "Draw" );
    
    /**
     * Constructor.
     * Lays out this panel
     * using a BorderLayout
     * with a panel containing the radio buttons
     * in the west area,
     * a font editor panel
     * in the center area,
     * and a control panel
     * in the south area.
     */
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
        add( getRightPanel(), BorderLayout.CENTER );
        add( getControlPanel(), BorderLayout.SOUTH );
    }
    
    /**
     * Creates a dialog
     * with a given parent,
     * and a GraphPropertiesPanel
     * as the content pane.
     * The dialog is <em>not</em> made visible.
     * 
     * @param parent    the given parent
     * 
     * @return the created dialog
     */
    public static JDialog getDialog( Window parent )
    {
        final String    title   = "Graph Properties Dialog";
        JDialog dialog      = new JDialog( parent, title );
        dialog.setContentPane( new GraphPropertiesPanel() );
        dialog.pack();
        return dialog;
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
    
    /**
     * Creates a panel
     * to be displayed
     * on the right side
     * of the GraphPanel.
     * This includes a FontEditor
     * and ColorEditor
     * laid out vertically.
     * 
     * @return the created panel
     */
    private JPanel getRightPanel_()
    {
        JPanel  fontEditorPanel = fontEditor.getPanel();
        Border  lineBorder      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        Border  fontBorder      =
            BorderFactory.createTitledBorder( lineBorder, "Font" );
        fontEditorPanel.setBorder( fontBorder );
        
        JPanel  bgEditorPanel   = bgEditor.getPanel();
        Border  bgBorder        = BorderFactory
            .createTitledBorder( lineBorder, "Background Color" );
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
    
    private JPanel getRightPanel()
    {
        JPanel      fontPanel   = getFontPanel();
        
        Border  lineBorder      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        JPanel  bgEditorPanel   = bgEditor.getPanel();
        Border  bgBorder        = BorderFactory
            .createTitledBorder( lineBorder, "Background Color" );
        bgEditorPanel.setBorder( bgBorder );
        
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = 
            new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        Dimension   spacer  = new Dimension( 1, 5 );
        panel.add( fontPanel );
        panel.add( Box.createRigidArea( spacer ) );
        panel.add( bgEditorPanel );
        
        return panel;
    }
    
    private JPanel getFontPanel()
    {
        JPanel  drawPanel   = 
            new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        drawPanel.add( drawEditor );
        
        JPanel  fontPanel   = fontEditor.getPanel();
        
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        Border  lineBorder      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        Border  fontBorder      =
            BorderFactory.createTitledBorder( lineBorder, "Font" );
        panel.setBorder( fontBorder );
        
        panel.add( fontPanel );
        panel.add( drawPanel );
        
        return panel;
    }
    
    /**
     * Creates the control panel
     * for this GraphPanel.
     * This consists of
     * the Apply, Reset and Close buttons
     * in a FlowLayout.
     * @return
     */
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        JButton     reset   = new JButton( "Reset" );
        JButton     apply   = new JButton( "Apply" );
        JButton     close   = new JButton( "Close" );
        reset.addActionListener( this::resetAction );
        apply.addActionListener( this::applyAction );
        close.addActionListener( this::closeAction );
        
        panel.add( reset );
        panel.add( apply );
        panel.add( close );
        
        return panel;
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
    public void itemStateChanged(ItemEvent evt)
    {
        Object  source  = evt.getSource();
        if ( source instanceof PRadioButton<?> )
        {
            PRadioButton<?> button  = (PRadioButton<?>)source;
            Object          obj     = button.get();
            if ( obj instanceof GraphPropertySet )
            {
                GraphPropertySet    set = (GraphPropertySet)obj;
                if ( evt.getStateChange() == ItemEvent.SELECTED )
                    copyRight( set );
                else
                    copyLeft( set );
            }
        }
    }

    private void copyLeft( GraphPropertySet set )
    {
        set.setFontName( fontEditor.getName() );
        set.setFontSize( fontEditor.getSize().orElse( -1 ) );
        set.setItalic( fontEditor.isItalic() );
        set.setBold( fontEditor.isBold() );
//        set.setFontDraw( fontEditor.dr);
        
    }
    
    private void copyRight( GraphPropertySet set )
    {
        
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
        // Update the selected button's property set with the
        // current values of the components on the right side
        // of the panel. Do this by notifying all ItemListeners
        // with an ItemEvent.DESELECTED event.
        PRadioButton<GraphPropertySet>  selectedButton  = 
            buttonGroup.getSelectedButton();
        ItemEvent   event   = 
            new ItemEvent(
                selectedButton,
                ItemEvent.ITEM_FIRST,
                selectedButton,
                ItemEvent.DESELECTED
            );
        Stream.of( selectedButton.getItemListeners() )
            .forEach( l -> l.itemStateChanged( event ) );

        buttonGroup.getButtons().stream()
            .map( b -> b.get() )
            .forEach( s -> s.apply() );
    }
    
    /**
     * Action method
     * that is executed
     * when the Reset button pushed.
     * The reset action
     * will be executed 
     * for the GraphPropertySet
     * encapsulated in each radio button,
     * then the reset data
     * for the currently selected button
     * is copied to the components
     * on the right side
     * of the panel.
     * 
     * @param evt   
     *      ActionEvent object
     *      that caused this method to be invoked; not used
     */
    private void resetAction( ActionEvent evt )
    {
        // Reset all LinePropertySets to their original values
        buttonGroup.getButtons().stream()
            .map( b -> b.get() )
            .forEach( s -> s.reset() );
        
        // Invoke all ItemListeners on the selected button, passing
        // a SELECTED event. This will cause the GUI to be reinitialized
        // with the selected button's LinePropertySet values.
        PRadioButton<GraphPropertySet>  selectedButton  = 
            buttonGroup.getSelectedButton();
        ItemEvent   event   = 
            new ItemEvent(
                selectedButton,
                ItemEvent.ITEM_FIRST,
                selectedButton,
                ItemEvent.SELECTED
            );
        Stream.of( selectedButton.getItemListeners() )
            .forEach( l -> l.itemStateChanged( event ) );
    }

    /**
     * Action method
     * that is executed
     * when the Close button pushed.
     * If the root component
     * in the source's containment hierarchy
     * is a JDialog it is closed,
     * otherwise no action is taken.
     * if there is no top-level window
     * in the source's containment hierarchy
     * a ComponentException is thrown.
     * 
     * @param evt   
     *      event object associated with the ActionEvent
     *      that caused this method to be invoked; not used
     *      
     * @throws ComponentException
     *      if the event sources component
     *      does not have a top-level window parent
     *      in its containment hierarchy.
     */
    private void closeAction( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof JComponent )
        {
            Container   testObj = ((JComponent)source).getParent();
            while ( !(testObj instanceof Window ) && testObj != null )
                testObj = testObj.getParent();
            if ( testObj == null )
            {
                StringBuilder   bldr    = new StringBuilder()
                    .append( "Top-level window of LinePropertiesPanel " )
                    .append( "not found; " )
                    .append( "source type = " )
                    .append( source.getClass().getName() );
                throw new ComponentException( bldr.toString() );
            }
            
            if ( testObj instanceof JDialog )
                ((JDialog)testObj).setVisible( false );
        }
    }
}
