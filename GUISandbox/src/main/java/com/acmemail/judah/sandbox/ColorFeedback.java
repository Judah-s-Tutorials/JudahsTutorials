package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

@SuppressWarnings("serial")
public class ColorFeedback extends JLabel
{
    private static final Color      errColor    = new Color( 0xCCCCCC );
    private static final String     errText     = "Error";
    private static final String     normalText  = "";

    private final JTextComponent    textEditor;
    
    public ColorFeedback( JTextComponent textEditor )
    {
        this.textEditor = textEditor;
        Border  border  = BorderFactory.createLineBorder( Color.BLACK );
        setBorder( border );
        setText( errText );
    }
    
    public Color getColor()
    {
        Color   color   = 
            Feedback.getColor( textEditor ).orElse( errColor  );
        return color;
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        Graphics2D      gtx         = (Graphics2D)graphics.create();
        Optional<Color> optColor    = Feedback.getColor( textEditor );
        Color           color       = optColor.orElse( errColor );
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
