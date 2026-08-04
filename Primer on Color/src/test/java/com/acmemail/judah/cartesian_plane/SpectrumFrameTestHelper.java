package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.acmemail.judah.color_primer.SpectrumFrame;
import com.acmemail.judah.color_primer.SpectrumRanger;
import com.acmemail.judah.color_primer.util.ComponentFinder;
import com.acmemail.judah.color_primer.util.RangeSlider;

public class SpectrumFrameTestHelper
{
    private static final List<String>   compNames   = 
        List.of( 
            SpectrumFrame.HUE_MAX,
            SpectrumFrame.HUE_MIN,
            SpectrumFrame.HUE_SLIDER,
            SpectrumFrame.BRIGHT_SLIDER,
            SpectrumFrame.BRIGHT_TEXT,
            SpectrumFrame.SAT_SLIDER,
            SpectrumFrame.SAT_TEXT
        );
    private final Map<String,JComponent>    compMap;
    
    private final SpectrumRanger    spectrum;
    private final SpectrumFrame     root;
    private final JFrame            frame;
    private final ComponentFinder   finder;

    public SpectrumFrameTestHelper()
    {
        spectrum = new SpectrumRanger( 500 );
        root = new SpectrumFrame( spectrum );
        root.start();
        // root.start() is going to start a task on the EDT.
        // Sleep until that task is finished.
        invokeAndWait( () -> {} );
        frame = ComponentFinder.getJFrameByName( SpectrumFrame.APP_FRAME );
        assertNotNull( frame );
        finder = new ComponentFinder( frame );
        Map<String,JComponent>    tempMap = new HashMap<>();
        for ( String name : compNames )
            tempMap.put( name, getComponent( name ) );
        compMap = Collections.unmodifiableMap( tempMap );
    }
    
    public int getHueMinFromText()
    {
        JTextField  textField   = getTextField( SpectrumFrame.HUE_MIN );
        int         min         = getInt( textField );
        return min;
    }
    
    public void setHueMinText( int min )
    {
        JTextField  textField   = getTextField( SpectrumFrame.HUE_MIN );
        setInt( textField, min );
    }
    
    public int getHueMaxFromText()
    {
        JTextField  textField   = getTextField( SpectrumFrame.HUE_MAX );
        int         max         = getInt( textField );
        return max;
    }
    
    public void setHueMaxText( int max )
    {
        JTextField  textField   = getTextField( SpectrumFrame.HUE_MAX );
        setInt( textField, max ); 
    }
    
    public int getHueMinFromSlider()
    {
        RangeSlider slider  = getRangeSlider();
        int         min     = getInt( slider::getValue );
        return min;
    }
    
    public void setHueMinSlider( int min )
    {
        RangeSlider slider  = getRangeSlider();
        setInt( min, slider::setValue );
    }
    
    public int getHueMinProperty()
    {
        int max = getInt( () -> root.getHueLowerValue() );
        return max;
    }
    
    public void setHueMaxSlider( int max )
    {
        RangeSlider slider  = getRangeSlider();
        setInt( max, slider::setUpperValue );
    }
    
    public int getHueMaxFromSlider()
    {
        RangeSlider slider  = getRangeSlider();
        int         min     = getInt( slider::getUpperValue );
        return min;
    }
    
    public int getHueMaxProperty()
    {
        int max = getInt( () -> root.getHueUpperValue() );
        return max;
    }
    
    public int getSatFromText()
    {
        JTextField  textField   = getTextField( SpectrumFrame.SAT_TEXT );
        int         val         = getInt( textField );
        return val;
    }
    
    public void setSatText( int val )
    {
        JTextField  textField   = getTextField( SpectrumFrame.SAT_TEXT );
        setInt( textField, val );
    }
    
    public int getSatFromSlider()
    {
        JSlider     slider  = getSlider( SpectrumFrame.SAT_SLIDER );
        int         val     = getInt( slider );
        return val;
    }
    
    public void setSatSlider( int val )
    {
        JSlider     slider  = getSlider( SpectrumFrame.SAT_SLIDER );
        setInt( slider, val );
    }
    
    public int getSatProperty()
    {
        int max = getInt( () -> root.getSaturation() );
        return max;
    }

    public int getBrightFromText()
    {
        JTextField  textField   = getTextField( SpectrumFrame.BRIGHT_TEXT );
        int         val         = getInt( textField );
        return val;
    }
    
    public void setBrightText( int val )
    {
        JTextField  textField   = getTextField( SpectrumFrame.BRIGHT_TEXT );
        setInt( textField, val );
    }
    
    public int getBrightFromSlider()
    {
        JSlider     slider  = getSlider( SpectrumFrame.BRIGHT_SLIDER );
        int         val     = getInt( slider );
        return val;
    }
    
    public void setBrightSlider( int val )
    {
        JSlider     slider  = getSlider( SpectrumFrame.BRIGHT_SLIDER );
        setInt( slider, val );
    }    
    
    public int getBrightProperty()
    {
        int max = getInt( () -> root.getBrightness() );
        return max;
    }
    
    public Color getBackgroundColor( String componentName )
    {
        JComponent  component   = getTextField( componentName );
        Color       color       = component.getBackground();
        return color;
    }
    
    public void setText( String componentName, String text )
    {
        JTextField  textField   = getTextField( componentName );
        invokeAndWait( () -> {
            textField.setText( text );
            textField.postActionEvent();
        });
    }
    
    private JTextField getTextField( String name )
    {
        JComponent  comp    = compMap.get( name );
        assertNotNull( comp );
        assert( comp instanceof JTextField );
        return (JTextField)comp;
    }
    
    private JSlider getSlider( String name )
    {
        JComponent  comp    = compMap.get( name );
        assertNotNull( comp );
        assert( comp instanceof JSlider );
        return (JSlider)comp;
    }
    
    private RangeSlider getRangeSlider()
    {
        String      name    =  SpectrumFrame.HUE_SLIDER;
        JComponent  comp    = compMap.get( name );
        assertNotNull( comp );
        assertTrue( comp instanceof RangeSlider, name );
        return (RangeSlider)comp;
    }
    
    private int getInt( JTextField textField )
    {
        final char  degree  = '\u00b0';
        String      text    = textField.getText();
        int         len     = text.length();
        int         result  = 0;
        if ( len > 0 )
        {
            char    last    = text.charAt( len - 1 );
            if ( last == '%' || last == degree )
                text = text.substring( 0, len - 1 );
        }
        try
        {
            result = Integer.parseInt( text );
        }
        catch ( NumberFormatException exc )
        {
            fail( "Invalid integer: " + text, exc );
        }
        return result;
    }
    
    private int getInt( JSlider slider )
    {
        int val = slider.getValue();
        return val;
    }
    
    private int getInt( IntSupplier supplier )
    {
        int val = supplier.getAsInt();
        return val;
    }
    
    private void setInt( JTextField textField, int val )
    {
        String  text    = String.valueOf( val );
        setText( textField, text );
        textField.postActionEvent();
    }    
    
    private void setInt( JSlider slider, int val )
    {
        invokeAndWait( () -> slider.setValue( val ) );
    }    
    
    private void setInt( int val, IntConsumer consumer )
    {
        invokeAndWait( () -> consumer.accept( val ) );
    }    
    
    private void setText( JTextField textField, String text )
    {
        invokeAndWait( () -> {
            textField.setText( text );
            textField.postActionEvent();
        });
    }
    
    private JComponent getComponent( String name )
    {
        JComponent  comp    = finder.getComponentByName( name );
        assertNotNull( comp );
        return comp;
    }
    
    private static void invokeAndWait( Runnable runner )
    {
        try
        {
            if ( SwingUtilities.isEventDispatchThread() )
                runner.run();
            else
                SwingUtilities.invokeAndWait( runner );
        }
        catch ( InvocationTargetException | InterruptedException exc )
        {
            fail( "invokeAndWait failure", exc );
        }
    }
}
