package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.ParseException;
import java.util.Optional;
import java.util.OptionalInt;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class FontEditor
{
    /** Text to display in feedback area. */
    private static final String     sampleString    =
        "<html>1313 Mockingbird Lane, Wisdom NB 68101</html>";
    
    /** Text to display when a configuration property is invalid. */
    private static final String     errorString     = "--ERROR--";
    /** Color of feedback text when a configuration property is invalid. */
    private static final Font       errorFont       =
        new Font( Font.DIALOG, Font.ITALIC, 12 );
    /** Text color to use when a configuration property is invalid. */
    private static final Color      errorColor      = Color.BLACK;
    
    /** Font names supported by this utility. */
    private static final String[]   fontNames       =
    {
        Font.DIALOG,
        Font.DIALOG_INPUT,
        Font.MONOSPACED,
        Font.SANS_SERIF,
        Font.SERIF
    };
    /** Drop-down list of supported font names. */
    private final JComboBox<String>     fontList        =
        new JComboBox<>( fontNames );
    /** Bold face toggle. */
    private final JCheckBox             boldToggle      = 
        new JCheckBox( "Bold" );
    /** Slope face toggle. */
    private final JCheckBox             italicToggle    = 
        new JCheckBox( "Italic" );
    /** Configuration parameter for size selector. */
    private final SpinnerNumberModel    sizeModel       =
        new SpinnerNumberModel( 10, 1, 40, 1 );
    /** Size selector. */
    private final JSpinner              sizeEditor      = 
        new JSpinner( sizeModel );
    /**
     * Color editor. The text editor and color pushbutton are extracted
     * from this object, and embedded in the FontEditor GUI.
     */
    private final ColorEditor           colorEditor     =
        new ColorEditor();
    
    /** Feedback window. */
    private final Feedback              feedback        = 
        new Feedback( sampleString );

    /**
     * Constructor.
     * This constructor configures GUI components
     * so it must be invoked
     * on the event dispatch thread (EDT).
     */
    public FontEditor()
    {
        JTextField  textEditor  = colorEditor.getTextEditor();
        textEditor.setText( "0x000000" );
        
        boldToggle.addActionListener( e -> feedback.update() );
        italicToggle.addActionListener( e -> feedback.update() );
        fontList.addActionListener( e -> feedback.update() );
        textEditor.addActionListener( e -> feedback.update() );
        sizeEditor.addChangeListener( e -> feedback.update() );
        
        feedback.update();
    }
    
    /**
     * Returns a JPanel that incorporates
     * all the FontEditor components.
     * 
     * @return  JPanel incorporating FontEditor components
     * 
     * @see  com.acmemail.judah.cartesian_plane.app.ShowFontEditor
     */
    public JPanel getPanel()
    {
        JPanel  panel   = new JPanel( new GridLayout( 1, 2, 3, 0 ) );
        panel.add( getLeftPanel() );
        panel.add( feedback );
        
        return panel;
    }
    
    /**
     * Returns an Optional
     * containing a Font composed of
     * the properties selected
     * in the FontEditor components.
     * If one of the properties
     * is invalid
     * an empty Optional is returned.
     * 
     * @return  
     *      Optional containing composed Font,
     *      or empty options if Font properties are invalid
     */
    public Optional<Font> getSelectedFont()
    {
        Optional<Font>  optFont = Optional.empty();
        
        String  fontName    = (String)fontList.getSelectedItem();
        int     fontSize    = 1;
        int     fontStyle   = 0;
        if ( boldToggle.isSelected() )
            fontStyle |= Font.BOLD;
        if ( italicToggle.isSelected() )
            fontStyle |= Font.ITALIC;
        try
        {
            sizeEditor.commitEdit();
            fontSize = (int)sizeEditor.getValue();
            Font    font    = new Font( fontName, fontStyle, fontSize );
            optFont = Optional.of( font );
        }
        catch ( ParseException exc )
        {
            // ignore; return will default to Optional.empty()
        }
        
        return optFont;
    }
    
    /**
     * Gets the font name.
     * 
     * @return  the font name
     */
    public String getName()
    {
        String  currFontName    = (String)fontList.getSelectedItem();
        return currFontName;
    }
    
    /**
     * Gets the font size.
     * 
     * @return  the font size
     */
    public OptionalInt getSize()
    {
        OptionalInt optSize = OptionalInt.empty();
        try
        {
            sizeEditor.commitEdit();
            int size = (int)sizeEditor.getValue();
            optSize = OptionalInt.of( size );
        }
        catch ( ParseException exc )
        {
        }
        return optSize;
    }
    
    /**
     * Indicates whether the bold property
     * is selected.
     * 
     * @return  true if the bold property is selected
     */
    public boolean isBold()
    {
        boolean currIsBold  = boldToggle.isSelected();
        return currIsBold;
    }
    
    /**
     * Indicates whether the slope property
     * is selected.
     * 
     * @return  true if the slope property is selected
     */
    public boolean isItalic()
    {
        boolean currIsItalic    = italicToggle.isSelected();
        return currIsItalic;
    }
    
    /**
     * Gets the selected text color.
     * 
     * @return  the selected text color
     */
    public Optional<Color> getColor()
    {
        Optional<Color> optColor    = colorEditor.getColor();
        return optColor;
    }
    
    /**
     * Gets the JComboBox
     * that lists
     * the support font names.
     * 
     * @return combo box for selecting the name property
     */
    public JComboBox<String> getNameCombo()
    {
        return fontList;
    }
    
    /**
     * Gets the JToggleButton
     * used to select the bold property.
     * 
     * @return toggle button for selecting the bold property
     */
    public JCheckBox getBoldToggle()
    {
        return boldToggle;
    }

    /**
     * Gets the JToggleButton
     * used to select the italic (sloped) property.
     * 
     * @return toggle button for selecting the italic property
     */
    public JCheckBox getItalicToggle()
    {
        return italicToggle;
    }

    /**
     * Gets the JSpinner used to configure
     * the font size.
     * 
     * @return the JSpinner for selecting the font size
     */
    public JSpinner getSizeEditor()
    {
        return sizeEditor;
    }

    /**
     * Returns the ColorEditor
     * used to select the text color.
     * 
     * @return 
     *      the ColorEditor for selecting
     *      the text color
     */
    public ColorEditor getColorEditor()
    {
        return colorEditor;
    }

    /**
     * Gets the JComponent used as a feedback window.
     * 
     * @return the JLabel used as a feedback window
     */
    public JComponent getFeedback()
    {
        return feedback;
    }

    /**
     * Composes the left-side panel
     * of the default FontEditor GUI.
     * 
     * @return  the left-side panel of the default FontEditor GUI
     */
    private JPanel getLeftPanel()
    {
        JPanel              panel   = new JPanel( new GridLayout( 5, 1 ) );
        panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
        panel.add( fontList );
        panel.add( boldToggle );
        panel.add( italicToggle );
        
        sizeEditor.setAlignmentX( JComponent.CENTER_ALIGNMENT );
        JLabel sizeLabel = new JLabel( "Size" );
        sizeLabel.setAlignmentX( JComponent.CENTER_ALIGNMENT );
        
        JPanel  sizePanel   = new JPanel();
        sizePanel.setLayout( new GridLayout( 1, 2, 3, 0 ) );
        sizePanel.add( sizeEditor );
        sizePanel.add( sizeLabel );
        panel.add( sizePanel );
        
        JPanel  colorPanel   = new JPanel();
        colorPanel.setLayout( new GridLayout( 1, 2, 3, 0 ) );
        colorPanel.add( colorEditor.getColorButton() );
        colorPanel.add( colorEditor.getTextEditor() );
        panel.add( colorPanel );
        
        float   align   = JPanel.CENTER_ALIGNMENT;
        fontList.setAlignmentX( align );
        boldToggle.setAlignmentX( align );
        italicToggle.setAlignmentX( align );
        sizePanel.setAlignmentX( align );
        colorPanel.setAlignmentX( align );
        
        return panel;
    }
    
    /**
     * Encapsulates the JComponent
     * used as a feedback window.
     * 
     * @author Jack Straub
     */
    private class Feedback extends JLabel
    {
        /**
         * Constructor.
         * Configures the feedback window.
         * 
         * @param text  text to display in the feedback window
         */
        public Feedback( String text )
        {
            super( text );
            setForeground( colorEditor.getColor().orElse( Color.BLACK ) );
            setOpaque( true );
            setBackground( new Color( 0xDDDDDD ) );
            Border  border  = BorderFactory.createLineBorder( Color.BLACK, 2 );
            setBorder( border );
        }
        
        /**
         * Updates the feedback window
         * to reflect the currently configured font
         * and text color.
         * If a configuration parameter is invalid
         * defaults are used.
         */
        public void update()
        {
            Optional<Font>  optFont     = getSelectedFont();
            Optional<Color> optColor    = colorEditor.getColor();
            Font            font        = null;
            String          text        = null;
            Color           color       = null;
            if ( optFont.isPresent() && optColor.isPresent() )
            {
                font = optFont.get();
                color = optColor.get();
                text = sampleString;
            }
            else
            {
                font = errorFont;
                text = errorString;
                color = errorColor;
            }
                
            setFont( font );
            setForeground( color );
            setText( text );
            repaint();
        }
    }
}
