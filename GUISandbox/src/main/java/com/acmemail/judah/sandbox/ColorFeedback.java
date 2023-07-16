package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class ColorFeedback extends JLabel
{
    private final   JTextField  input;
    private String  colorText   =   "";
    
    public ColorFeedback( JTextField input, JComponent label )
    {
        this.input = input;
//        input.setText( colorText );
        Border  border  = BorderFactory.createLineBorder( Color.BLACK );
        setBorder( border );
        
        this.addPropertyChangeListener( e -> 
            Feedback.setEnabled( this, input, label )
        );
    }
    
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        Graphics2D  gtx         = (Graphics2D)graphics;
        int         width       = getWidth();
        int         height      = getHeight();
        colorText = input.getText();
        Optional<Color> colorOpt    = getColor();
        
        if ( colorOpt.isEmpty() )
            Feedback.showError( gtx, width, height );
        else
        {
            Color   origColor   = gtx.getColor();
            Color   currColor   = colorOpt.get();
            gtx.setColor( currColor );
            gtx.fillRect( 0, 0, width, height );
            gtx.setColor( origColor );
        }
        paintBorder( gtx );
    }
    
    public Color getCurrColor()
    {
        Optional<Color> optColor    = getColor();
        Color           color       = optColor.orElse( null );
        return color;
    }
    
    private Optional<Color> getColor()
    {
        Optional<Color> colorOpt    = Optional.empty();
        if ( input != null )
        {
            try
            {
                colorText = colorText.toLowerCase();
                int     intVal  = Integer.decode( colorText );
                Color   color   = new Color( intVal );
                colorOpt = Optional.of( color );
            }
            catch ( NumberFormatException exc )
            {
            }
        }
        
        return colorOpt;
    }
}
