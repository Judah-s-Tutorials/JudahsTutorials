package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ColorEditor
{
    private final JButton       colorButton;
    private final JTextField    textEditor;
    private final ColorFeedback colorFB;
    private final ColorDialog   colorDialog;
    
    private final List<ActionListener>  actionListeners = new ArrayList<>();
    
    public ColorEditor()
    {
        colorButton = new JButton( "Color" );
        textEditor = new JTextField( "0xFFFFFF", 10 );
        colorFB = new ColorFeedback( textEditor );
        colorDialog = new ColorDialog();
        
        Font    baseFont    = textEditor.getFont();
        Font    editFont    = new Font(
            Font.MONOSPACED,
            baseFont.getStyle(),
            baseFont.getSize()
        );
        textEditor.setFont( editFont );
        
        colorButton.addActionListener( e -> showDialog() );
        textEditor.addActionListener( e -> commit() );
        
        textEditor.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized( ComponentEvent evt ){
                colorFB.setPreferredSize( textEditor.getSize() );
            }
        });
    }
    
    public JPanel getBoxPanel( int axis )
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, axis );
        
        panel.setLayout( layout );
        panel.add( colorButton );
        panel.add( textEditor );
        panel.add( colorFB );
        
        return panel;
    }
    
    public JPanel getGridPanel()
    {
        JPanel      masterPanel = new JPanel( new GridLayout( 1, 2 ) );
        JPanel      inputPanel  = new JPanel( new GridLayout( 1, 2, 2, 0 ) );
        inputPanel.add( colorButton );
        inputPanel.add( textEditor );
        masterPanel.add( inputPanel );
        masterPanel.add( colorFB );
        
        return masterPanel;
    }
    
    public JButton getColorButton()
    {
        return colorButton;
    }
    
    public JTextField getTextComponent()
    {
        return textEditor;
    }
    
    public JComponent getFeedbackComponent()
    {
        return colorFB;
    }
    
    public Optional<Color> getColor()
    {
        Optional<Color> optVal  = Optional.empty();
        String          text    = textEditor.getText().trim();
        try
        {
            int     intColor    = Integer.decode( text );
            Color   color       = new Color( intColor );
            optVal = Optional.of( color );
        }
        catch ( NumberFormatException exc )
        {
            // ignored; will return empty optional by default
        }
        
        return optVal;
    }
    
    public void commit()
    {
        colorFB.repaint();
        fireActionListeners();
    }
    
    public void addActionListener( ActionListener listener )
    {
        actionListeners.add( listener );
    }
    
    public void removeActionListener( ActionListener listener )
    {
        actionListeners.remove( listener );
    }
    
    private void fireActionListeners()
    {
        ActionEvent event   = 
            new ActionEvent( textEditor, ActionEvent.ACTION_FIRST, null );
        actionListeners.forEach( l -> l.actionPerformed( event ));
    }
    
    private void showDialog()
    {
        Color   color   = colorDialog.showDialog();
        if ( color != null )
        {
            int     intColor    = color.getRGB() & 0x00ffffff;
            String  strColor    = String.format( "0x%06x", intColor );
            textEditor.setText( strColor );
            commit();
        }
    }
}
