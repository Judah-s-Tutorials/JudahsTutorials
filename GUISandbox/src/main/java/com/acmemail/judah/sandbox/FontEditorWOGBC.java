package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class FontEditorWOGBC extends JPanel
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

    public FontEditorWOGBC()
    {
        super( new GridLayout( 1, 2, 3, 0 ) );
        
        add( getLeftPanel() );
        add( feedback );
        colorEditor.getTextComponent().setText( "0x000000" );
        
        boldToggle.addActionListener( e -> feedback.update() );
        italicToggle.addActionListener( e -> feedback.update() );
        fontList.addActionListener( e -> feedback.update() );
        colorEditor.addActionListener( e -> feedback.update() );
        sizeEditor.addChangeListener( e -> feedback.update() );
        
        feedback.update();
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
    
    private JPanel getLeftPanel()
    {
        JPanel              panel   = new JPanel( new GridLayout( 5, 1 ) );
//        JPanel              panel   = new JPanel( new GridLayout( 5, 1 ) );
        panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
        panel.add( fontList );
        panel.add( boldToggle );
        panel.add( italicToggle );
        
        sizeEditor.setAlignmentX( CENTER_ALIGNMENT );
        JLabel sizeLabel = new JLabel( "Size" );
        sizeLabel.setAlignmentX( CENTER_ALIGNMENT );
        
        JPanel  sizePanel   = new JPanel();
//        sizePanel.setLayout( new BoxLayout( sizePanel, BoxLayout.X_AXIS ) );
        sizePanel.setLayout( new GridLayout( 1, 2, 3, 0 ) );
        sizePanel.add( sizeEditor );
        sizePanel.add( sizeLabel );
        panel.add( sizePanel );
        
        JPanel  colorPanel   = new JPanel();
//        colorPanel.setLayout( new BoxLayout( colorPanel, BoxLayout.X_AXIS ) );
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
    
    private GridBagConstraints getGBC( 
        GridBagConstraints gbc,
        int xco,
        int yco,
        int width,
        int fill,
        int anchor
    )
    {
        if ( gbc == null )
            gbc = new GridBagConstraints();
        gbc.gridx = xco;
        gbc.gridy = yco;
        gbc.gridwidth = width;
        gbc.fill = fill;
        gbc.anchor = anchor;
        
        if ( anchor == GridBagConstraints.EAST )
            gbc.insets = new Insets( 0, 0, 0, 2 );
        else if ( anchor == GridBagConstraints.WEST )
            gbc.insets = new Insets( 0, 2, 0, 0 );
        else 
            gbc.insets = new Insets( 0, 0, 0, 0 );
        
        return gbc;
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
