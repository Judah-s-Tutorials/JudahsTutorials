package com.acmemail.judah.sandbox;

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
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class FontEditor
{
    private static final String     sampleString    =
        "<html>1313 Mockingbird Lane, Wisdom NB 68101</html>";
    
    private static final String     errorString     = "--ERROR--";
    private static final Color      errorColor      = Color.BLACK;
    private static final Font       errorFont       =
        new Font( Font.DIALOG, Font.ITALIC, 12 );
    
    private static final String[]   fontNames       =
    {
        Font.DIALOG,
        Font.DIALOG_INPUT,
        Font.MONOSPACED,
        Font.SANS_SERIF,
        Font.SERIF
    };
    private final JComboBox<String>     fontList        =
        new JComboBox<>( fontNames );
    private final JCheckBox             boldToggle      = 
        new JCheckBox( "Bold" );
    private final JCheckBox             italicToggle    = 
        new JCheckBox( "Italic" );
    private final SpinnerNumberModel    sizeModel       =
        new SpinnerNumberModel( 10, 1, 40, 1 );
    private final JSpinner              sizeEditor      = 
        new JSpinner( sizeModel );
    private final ColorEditor           colorEditor     =
        new ColorEditor();
    
    private final Feedback              feedback        = 
        new Feedback( sampleString );

    public FontEditor()
    {
        colorEditor.getTextComponent().setText( "0x000000" );
        
        boldToggle.addActionListener( e -> feedback.update() );
        italicToggle.addActionListener( e -> feedback.update() );
        fontList.addActionListener( e -> feedback.update() );
        colorEditor.addActionListener( e -> feedback.update() );
        sizeEditor.addChangeListener( e -> feedback.update() );
        
        feedback.update();
    }
    
    public JPanel getPanel()
    {
        JPanel  panel   = new JPanel( new GridLayout( 1, 2, 3, 0 ) );
        panel.add( getLeftPanel() );
        panel.add( feedback );
        
        return panel;
    }
    
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
    
    public String getName()
    {
        String  currFontName    = (String)fontList.getSelectedItem();
        return currFontName;
    }
    
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
    
    public boolean isBold()
    {
        boolean currIsBold  = boldToggle.isSelected();
        return currIsBold;
    }
    
    public boolean isItalic()
    {
        boolean currIsItalic    = italicToggle.isSelected();
        return currIsItalic;
    }
    
    public Optional<Color> getColor()
    {
        Optional<Color> optColor    = colorEditor.getColor();
        return optColor;
    }
    
    /**
     * @return the boldToggle
     */
    public JCheckBox getBoldToggle()
    {
        return boldToggle;
    }

    /**
     * @return the italicToggle
     */
    public JCheckBox getItalicToggle()
    {
        return italicToggle;
    }

    /**
     * @return the sizeEditor
     */
    public JSpinner getSizeEditor()
    {
        return sizeEditor;
    }

    /**
     * @return the colorEditor
     */
    public ColorEditor getColorEditor()
    {
        return colorEditor;
    }

    /**
     * @return the feedback
     */
    public JLabel getFeedback()
    {
        return feedback;
    }

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
        colorPanel.add( colorEditor.getTextComponent() );
        panel.add( colorPanel );
        
        float   align   = JPanel.CENTER_ALIGNMENT;
        fontList.setAlignmentX( align );
        boldToggle.setAlignmentX( align );
        italicToggle.setAlignmentX( align );
        sizePanel.setAlignmentX( align );
        colorPanel.setAlignmentX( align );
        
        return panel;
    }
    
    private class Feedback extends JLabel
    {
        public Feedback( String text )
        {
            super( text );
            setForeground( colorEditor.getColor().orElse( Color.BLACK ) );
            setOpaque( true );
            setBackground( new Color( 0xDDDDDD ) );
            Border  border  = BorderFactory.createLineBorder( Color.BLACK, 2 );
            setBorder( border );
        }
        
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
