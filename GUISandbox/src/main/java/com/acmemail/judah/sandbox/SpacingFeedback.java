package com.acmemail.judah.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.OptionalDouble;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class SpacingFeedback extends JLabel
{
    private final JSpinner      spinner;
    
    public SpacingFeedback( JSpinner spinner, JComponent label )
    {
        this.spinner = spinner;
        Border  border  = BorderFactory.createLineBorder( Color.BLACK );
        setBorder( border );
        
        this.addPropertyChangeListener( e -> 
            Feedback.setEnabled( this, spinner, label )
        );
    }
    
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        Graphics2D      gtx         = (Graphics2D)graphics.create();
        int             width       = getWidth();
        int             height      = getHeight();
        OptionalDouble  optValue    = Feedback.getValue( spinner );
        if ( optValue.isEmpty() )
            Feedback.showError( gtx, width, height );
        else
        {
            // Vertical line will take half the height of the box
            float   verticalLen = height * .6f;
            int     offset      = (int)((height - verticalLen) / 2f);
            int     yco         = height / 2;
            int     midPoint    = width / 2;
            double  halfSpacing = optValue.getAsDouble() / 2;
            gtx.setColor( Feedback.DEF_BACKGROUND );
            gtx.fillRect( 0, 0, width, height );
            gtx.setColor( Feedback.DEF_FOREGROUND );
            
            // this draws a thin, horizontal line down the middle
            gtx.setStroke( new BasicStroke( 1 ) );
            gtx.drawLine( 5, yco, width - 5, yco );
            
            // draw 2 vertical lines, len pixels apart, equidistant
            // from the midpoint; disregard rounding errors 
            int     xcoLeft     = (int)(midPoint - halfSpacing);
            int     xcoRight    = (int)(midPoint + halfSpacing);
            int     ycoTop      = offset;
            int     ycoBottom   = height - offset;
            gtx.setStroke( new BasicStroke( 3 ) );
            gtx.drawLine( xcoLeft, ycoTop, xcoLeft, ycoBottom );
            gtx.drawLine( xcoRight, ycoTop, xcoRight, ycoBottom );
            
            paintBorder( gtx );
        }
    }
}
