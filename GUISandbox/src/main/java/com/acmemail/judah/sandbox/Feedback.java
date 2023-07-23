package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.awt.Graphics;
import java.text.ParseException;
import java.util.Optional;
import java.util.OptionalDouble;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.text.JTextComponent;

public class Feedback
{
    public static final Color     DEF_BACKGROUND    = Color.WHITE;
    public static final Color     DEF_FOREGROUND    = Color.BLACK;
    public static final Color     ERR_BACKGROUND    = Color.LIGHT_GRAY;
    public static final Color     ERR_FOREGROUND    = Color.BLACK;
    public static final String    ERR_TEXT          = "Error";
    
    public static OptionalDouble getValue( JSpinner spinner )
    {
        OptionalDouble  value   = OptionalDouble.empty();
        try
        {
            spinner.commitEdit();
            Double  doubleVal   = (Double)spinner.getValue();
            if ( doubleVal != null )
                value = OptionalDouble.of( doubleVal );
        }
        catch ( ParseException exc )
        {
            // no action necessary
        }
        return value;
    }
    
    public static Optional<Color> getColor( JTextComponent comp )
    {
        String          text        = comp.getText().trim();
        Optional<Color> optColor    = getColor( text );
        return optColor;
    }
    
    public static Optional<Color> getColor( String text )
    {
        Optional<Color> colorOpt    = Optional.empty();
        try
        {
            int     intVal  = Integer.decode( text );
            Color   color   = new Color( intVal );
            colorOpt = Optional.of( color );
        }
        catch ( NumberFormatException exc )
        {
        }
        return colorOpt;
    }
    
    public static String getText( Color color )
    {
        // Get int value and suppress alpha bits
        int     intColor    = color.getRGB() & ~0xff000000;
        String  strColor    = String.format( "0x%06x", intColor );
        return strColor;
    }

    public static void showError( Graphics gtx, int width, int height )
    {
        Color   origColor   = gtx.getColor();
        gtx.setColor( ERR_BACKGROUND );
        gtx.fillRect( 0, 0, width, height );
        gtx.setColor( ERR_FOREGROUND );
        
        int xco = 10;
        int yco = height / 2 + 5;
        gtx.drawString( ERR_TEXT, xco, yco );
        gtx.setColor( origColor );
    }
    
    public static void 
    setEnabled( JComponent source, JComponent dest1, JComponent dest2 )
    {
        boolean currVal = source.isEnabled();
        dest1.setEnabled( currVal );
        dest2.setEnabled( currVal );
    }
}
