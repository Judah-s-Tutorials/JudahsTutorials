package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class FontEditor extends JPanel
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
        super( new GridLayout( 1, 2 ) );
        
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
        final int both      = GridBagConstraints.BOTH;
        final int left      = GridBagConstraints.WEST;
        final int right     = GridBagConstraints.EAST;
        final int none      = GridBagConstraints.NONE;
        final int center    = GridBagConstraints.CENTER;
        
        JPanel              panel   = new JPanel( new GridBagLayout() );
        
        GridBagConstraints  gbc     = getGBC( null, 0, 0, 2, both, center );
        gbc.ipadx = 10;
        panel.add( fontList, gbc );
        
        getGBC( gbc, 0, 1, 2, none, center );
        panel.add( boldToggle, gbc );
        
        getGBC( gbc, 0, 2, 2, none, center );
        panel.add( italicToggle, gbc );
        
        getGBC( gbc, 0, 3, 1, none, right );
        panel.add( sizeEditor, gbc );
        getGBC( gbc, 1, 3, 1, none, left );
        panel.add( new JLabel( "Size" ), gbc );
        
        getGBC( gbc, 0, 4, 1, both, right );
        panel.add( colorEditor.getColorButton(), gbc );
        getGBC( gbc, 1, 4, 1, both, left );
        panel.add( colorEditor.getTextComponent(), gbc );
        
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
