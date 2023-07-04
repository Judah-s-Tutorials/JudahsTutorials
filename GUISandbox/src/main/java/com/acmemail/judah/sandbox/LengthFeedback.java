package com.acmemail.judah.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.OptionalInt;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class LengthFeedback extends JLabel
{
    private final JSpinner      spinner;
    
    public LengthFeedback( JSpinner spinner, JComponent label )
    {
        this.spinner = spinner;
        Border  border  = BorderFactory.createLineBorder( Color.BLACK );
        setBorder( border );
        
        addPropertyChangeListener( e ->
            Feedback.setEnabled( this, spinner, label )
        );
    }
    
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        Graphics2D  gtx         = (Graphics2D)graphics.create();
        int         width       = getWidth();
        int         height      = getHeight();
        OptionalInt optValue    = Feedback.getValue( spinner );
        if ( optValue.isEmpty() )
            Feedback.showError( gtx, width, height );
        else
        {
            int     yco         = height / 2;
            int     midPoint    = width / 2;
            int     halfLen     = optValue.getAsInt() / 2;
            int     xco1        = midPoint - halfLen;
            int     xco2        = midPoint + halfLen;
            gtx.setColor( Feedback.DEF_BACKGROUND );
            gtx.fillRect( 0, 0, width, height );
            gtx.setColor( Feedback.DEF_FOREGROUND );
            gtx.setStroke( new BasicStroke( 3 ) );
            gtx.drawLine( xco1, yco, xco2, yco );
            paintBorder( gtx );
        }
    }
}
