package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class ColorEditor
{
    private static final Color      errColor    = new Color( 0xCCCCCC );
    private static final String     errText     = "Error";
    private static final String     normalText  = "";
    
    private final JButton       colorButton;
    private final JTextField    textEditor;
    private final ColorFeedback colorFB;
    private final ColorDialog   colorDialog;
    
    private final List<ActionListener>  actionListeners = new ArrayList<>();
    
    public ColorEditor()
    {
        colorButton = new JButton( "Color" );
        textEditor = new JTextField( "0xFFFFFF" );
        colorFB = new ColorFeedback();
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
    
    public JPanel getPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        
        panel.setLayout( layout );
        panel.add( colorButton );
        panel.add( textEditor );
        panel.add( colorFB );
        
        return panel;
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
    
    @SuppressWarnings("serial")
    private class ColorFeedback extends JLabel
    {
        public ColorFeedback()
        {
            Border  border  = BorderFactory.createLineBorder( Color.BLACK );
            setBorder( border );
//            setPreferredSize( textEditor.getPreferredSize() );
            setText( errText );
        }
        
        @Override
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            Graphics2D      gtx         = (Graphics2D)graphics.create();
            Optional<Color> optColor    = getColor();
            Color           color       = getColor().orElse( errColor );
            String          text        = 
                optColor.isPresent() ? normalText : errText;
            int             width       = getWidth();
            int             height      = getHeight();
            int             style       =
                optColor.isPresent() ? Font.PLAIN : Font.ITALIC;
            gtx.setColor( color );
            gtx.fillRect( 0,  0, width, height );
            
            Font        actFont = getFont().deriveFont( style );
            gtx.setFont( actFont );
            FontMetrics metrics = gtx.getFontMetrics();
            Rectangle2D errRect = 
                metrics.getStringBounds( text, gtx );
            float   xco     = (float)(width - errRect.getWidth()) / 2;
            float   yco     = (float)(height + errRect.getHeight()) / 2;
            
            gtx.setColor( Color.BLACK );
            gtx.drawString( text, xco, yco );
        }
    }
}
