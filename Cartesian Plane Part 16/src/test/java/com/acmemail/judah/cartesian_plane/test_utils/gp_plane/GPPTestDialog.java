package com.acmemail.judah.cartesian_plane.test_utils.gp_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import com.acmemail.judah.cartesian_plane.components.GraphPropertiesPanel;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;

@SuppressWarnings("serial")
public class GPPTestDialog extends JDialog
{
    /** This class's singleton. */
    private static GPPTestDialog    dialog;
    
    private final GraphPropertiesPanel  propertiesPanel =
        new GraphPropertiesPanel();
    
    private final JComboBox<String>     fontNames;
    private final JPanel                fontEditorPanel;
    private final JCheckBox             boldCheckBox;
    private final JCheckBox             italicCheckBox;
    private final JSpinner              sizeSpinner;
    private final SpinnerNumberModel    sizeModel;
    private final JTextField            fgColorTextField;
    private final JCheckBox             drawCheckBox;
    private final JSpinner              widthSpinner;
    private final SpinnerNumberModel    widthModel;
    private final JTextField            bgColorTextField;
    private final List<PRadioButton<GraphPropertySet>>  radioButtons;
    
    /**
     * Constructor.
     * Formulates this dialog,
     * then interrogates it,
     * initializing all fields.
     */
    private GPPTestDialog()
    {
        setContentPane( propertiesPanel );
        pack();
        
        fontNames = getFontNamesComboBox();
        fontEditorPanel = getFontPanel();
        boldCheckBox = getCheckBox( "Bold" );
        italicCheckBox = getCheckBox( "Italic" );
        sizeSpinner = getSizeSpinner();
        sizeModel = getNumberModel( sizeSpinner );
        fgColorTextField = getFGColorTextField();
        drawCheckBox = getCheckBox( "Draw" );
        widthSpinner = getWidthSpinner();
        widthModel = getNumberModel( widthSpinner );
        bgColorTextField = getBGColorTextField();
        radioButtons = getRadioButtons();
    }
    
    public static GPPTestDialog getDialog()
    {
        if ( dialog == null )
        {
//            if ( SwingUtilities.isEventDispatchThread() )
                dialog = new GPPTestDialog();
//            else
//                GUIUtils.schedEDTAndWait( 
//                    () -> dialog = new GPPTestDialog()
//            );
        }
        return dialog;
    }
    
    /**
     * Returns a list of radio buttons
     * contained in the GUI.
     * 
     * @return  list of radio buttons contained in the GUI
     */
    public List<PRadioButton<GraphPropertySet>> getRBList()
    {
        return radioButtons;
    }
    
    /**
     * Return the values of all properties
     * as represented in the GUI components.
     * 
     * @return  
     *  the values of all properties derived from the GUI components
     */
    public GraphPropertySet getProperties()
    {
        AllProperties   set     = new AllProperties();
        return set;
    }
    
    public void setProperties( GraphPropertySet set )
    {
        fontNames.setSelectedItem( set.getFontName() );
        boldCheckBox.setSelected( set.isBold() );
        italicCheckBox.setSelected( set.isItalic() );
        sizeSpinner.setValue( set.getFontSize() );
        setColor( set.getFGColor(), fgColorTextField );
        widthSpinner.setValue( set.getWidth() );
        drawCheckBox.setSelected( set.isFontDraw() );
        setColor( set.getBGColor(), bgColorTextField );
    }
    
    /**
     * Gets the panel that contains
     * the font configuration components.
     * <p>
     * Precondontion: fontNames is non-null
     * 
     * @return  the panel that contains the font configuration components
     */
    private JPanel getFontPanel()
    {
        assertNotNull( fontNames );
        Component   comp    = fontNames.getParent();
        assertTrue( comp instanceof JPanel );
        return (JPanel)comp;
    }
    
    /**
     * Gets the JComboBox that contains the font names.
     * @return  the JComboBox that contains the font names
     */
    private JComboBox<String> getFontNamesComboBox()
    {
        Predicate<JComponent>   pred    =
            c -> (c instanceof JComboBox);
        JComponent  comp    =
            ComponentFinder.find( this, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JComboBox );
        JComboBox<?>        comboBox    = (JComboBox<?>)comp;
        assertTrue( comboBox.getSelectedItem() instanceof String );
        
        @SuppressWarnings("unchecked")
        JComboBox<String>   sComboBox   = (JComboBox<String>)comboBox;
        return sComboBox;
    }
    
    /**
     * Gets the JCheckBox with the given text.
     * 
     * @param text  the given text
     * 
     * @return  the JCheckBox with the given text
     */
    private JCheckBox getCheckBox( String text )
    {
        Predicate<JComponent>   isCheckBox  =
            c -> (c instanceof JCheckBox);
        Predicate<JComponent>   hasText     =
            c -> text.equals( ((JCheckBox)c).getText() );
        Predicate<JComponent>   pred        = isCheckBox.and( hasText );
        JComponent  comp    =
            ComponentFinder.find( this, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JCheckBox );
        return (JCheckBox)comp;
        
    }
    
    /**
     * Gets the spinner
     * that is a child
     * of the font editor panel.
     * 
     * @return  the spinner that is a child of the font editor panel
     */
    private JSpinner getSizeSpinner()
    {
        assertNotNull( fontEditorPanel );
        Predicate<JComponent>   pred    = c -> (c instanceof JSpinner);
        JComponent  comp    =
            ComponentFinder.find( fontEditorPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JSpinner );
        return (JSpinner)comp;
        
    }
    
    /**
     * Gets the JTextField that is 
     * an immediate child
     * of the fontEditorPanel.
     * 
     * @return  
     *      the JTextField that is an immediate child
     *      of the fontEditorPanel
     */
    private JTextField getFGColorTextField()
    {
        assertNotNull( fontEditorPanel );
        
        // Find the panel containing the color button
        // (and presumably the color text field)
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( "Color" );
        JComponent              comp    =
            ComponentFinder.find( fontEditorPanel, pred );
        assertNotNull( comp );
        Component               parent  = comp.getParent();
        assertNotNull( parent );
        assertTrue( parent instanceof JPanel );
        JPanel                  panel   = (JPanel)parent;
        
        JTextField  field   =
            Stream.of( panel.getComponents() )
                .filter( c -> (c instanceof JTextField) )
                .map( c -> (JTextField)c )
                .findFirst().orElse( null );
        assertNotNull( field );
        return field;
    }
    
    
    /**
     * Gets the JTextField that is a child
     * of the Background Color panel.
     * 
     * @return  
     *      the JTextField that is an immediate child
     *      of the fontEditorPanel
     */
    private JTextField getBGColorTextField()
    {
        JPanel      panel   = getTitledPanel( "Background Color" );
        JComponent  comp    = 
            ComponentFinder.find( panel, c -> (c instanceof JTextField) );
        assertNotNull( comp );
        assertTrue( comp instanceof JTextField );
        return (JTextField)comp;
    }
    
    /**
     * Gets the JSpinner
     * that controls the width property.
     * 
     * @return  the JSpinner that controls the width property
     */
    private JSpinner getWidthSpinner()
    {
        JPanel                  widthPanel  = getTitledPanel( "Width" );
        Predicate<JComponent>   isSpinner   = c -> (c instanceof JSpinner);
        JComponent  comp        =
            ComponentFinder.find( widthPanel, isSpinner );
        assertNotNull( comp );
        assertTrue( comp instanceof JSpinner );
        return (JSpinner)comp;
        
    }
    
    /**
     * Gets a list of all PRadioButtons
     * in this dialog.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<PRadioButton<GraphPropertySet>> getRadioButtons()
    {
        // Find any radio button
        Predicate<JComponent>   pred    =
            c -> (c instanceof PRadioButton<?>);
        JComponent  comp    = 
            ComponentFinder.find( this, pred );
        // Get the panel that contains the radio buttons.
        assertNotNull( comp );
        Component   parent  = comp.getParent();
        assertTrue( parent instanceof JPanel );
        JPanel      panel   = (JPanel)parent;
        
        // Get the radio buttons from the panel.
        List<PRadioButton<GraphPropertySet>>   list    =
            Stream.of( panel.getComponents() )
                .filter( c -> (c instanceof PRadioButton<?>) )
                .map( c -> (PRadioButton<GraphPropertySet>)c )
                .toList();
        
        assertEquals( 5, list.size() );
        return list;
    }
    
    /**
     * Gets the JPanel
     * that has a TitledBorder
     * with the given title.
     * 
     * @param title the given title
     * 
     * @return  the JPanel with the given title
     */
    private JPanel getTitledPanel( String title )
    {
        Predicate<JComponent>   pred    = c -> hasTitle( c, title );
        JComponent  comp    = 
            ComponentFinder.find( this, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JPanel );
        return (JPanel)comp;
    }
    
    /**
     * Returns true if the given component
     * is a JPanel that has a TitledBorder
     * with the given title.
     * 
     * @param comp      the given component
     * @param title     the given title
     * 
     * @return  
     *      true if the given component has a border
     *      with the given title
     */
    private boolean hasTitle( JComponent comp, String title )
    {
        boolean result  = false;
        if ( comp instanceof JPanel )
        {
            TitledBorder    border  = getTitledBorder( comp );
            if ( border != null )
                if ( border.getTitle().equals( title ) )
                    result = true;
        }
        return result;
    }
    
    private TitledBorder getTitledBorder( JComponent panel )
    {
        TitledBorder    titledBorder    = null;
        Border          border          = panel.getBorder();
        if ( border == null )
            ;
        else if ( border instanceof TitledBorder )
            titledBorder = (TitledBorder)border;
        else if ( !(border instanceof CompoundBorder) )
            ;
        else
        {
            CompoundBorder  cBorder     = (CompoundBorder)border;
            Border          inBorder    = cBorder.getInsideBorder();
            Border          outBorder   = cBorder.getOutsideBorder();
            if ( inBorder instanceof TitledBorder )
                titledBorder = (TitledBorder)inBorder;
            else if ( outBorder instanceof TitledBorder )
                titledBorder = (TitledBorder)outBorder;
            else
                ;
        }
        return titledBorder;
    }
    
    /**
     * Convenience method
     * to get SpinnerNumberModel
     * from a given JSpinner
     * without compiler warnings.
     * 
     * @param spinner   given JSpinner
     * 
     * @return SpinnerNumberModel extracted from spinner
     */
    private static SpinnerNumberModel getNumberModel( JSpinner spinner )
    {
        SpinnerModel    model   = spinner.getModel();
        assertTrue( model instanceof SpinnerNumberModel );
        return (SpinnerNumberModel)model;
    }
    
    /**
     * Convenience method to convert
     * the integer RGB value of a given color
     * to a hex string
     * and store it in a given JTextField.
     * The alpha bits of the RGB value
     * are discarded.
     * After the text is set
     * the text field's postActionEvent() method is called.
     * 
     * @param color         the given color
     * @param textField     the given text field
     */
    private static void setColor( Color color, JTextField textField )
    {
        int     iColor  = color.getRGB() & 0x00FFFFFF;
        String  sColor  = String.format( "0x%06x", iColor );
        textField.setText( sColor );
        textField.postActionEvent();
    }
    
    /**
     * Parses an integer from a given JTextField
     * and converts it to a color.
     * If the text of the JTextField
     * cannot be converted to an integer,
     * Color.BLACK is returned.
     * 
     * @param textField the given JTextField
     * 
     * @return
     *      The parsed from the integer content of the given
     *      text field, or BLACK if the text field does not contain
     *      a valid integer.
     */
    private static Color getColor( JTextField textField )
    {
        Color   color   = Color.BLACK;
        String  text    = textField.getText();
        try
        {
            int iColor  = Integer.decode( text );
            color = new Color( iColor );
        }
        catch ( NumberFormatException exc )
        {
            // ignore
        }
        return color;
    }
    
    private class AllProperties extends GraphPropertySet
    {
        public AllProperties()
        {
            super( 
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            );
            Object  item    = fontNames.getSelectedItem();
            assertTrue( item instanceof String );
            setFontName( (String)item );
            setBold( boldCheckBox.isSelected() );
            setItalic( italicCheckBox.isSelected() );
            setFontSize( sizeModel.getNumber().floatValue() );
            setFGColor( getColor( fgColorTextField ) );
            setFontDraw( drawCheckBox.isSelected() );
            setWidth( widthModel.getNumber().floatValue() );
            setBGColor( getColor( bgColorTextField ) );
        }
        
        @Override
        public void reset()
        {
            String  msg =
                "The reset operation is not supported by the "
                + "AllProperties class.";
            throw new UnsupportedOperationException( msg );
        }
        
        @Override
        public void apply()
        {
            String  msg =
                "The reset operation is not supported by the "
                + "AllProperties class.";
            throw new UnsupportedOperationException( msg );
        }
    }
}
