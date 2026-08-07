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

import com.acmemail.judah.color_primer.DialFrame;
import com.acmemail.judah.color_primer.SpectrumDial;
import com.acmemail.judah.color_primer.util.ComponentFinder;
import com.acmemail.judah.color_primer.util.RangeSlider;

/**
 * This class serves as an interface to the SpectrumFrame class.
 * It attempts to resolve all issues
 * related to Swing threading.
 * It assumes that all access to a SpectrumFrame
 * is limited to this class.
 * It guarantees that all operations
 * that modify a property or component
 * of the Spectrum frame 
 * are executed on the EDT.
 * Read access to properties/components
 * are not forced to execute on the EDT,
 * however all such operations
 * are restricted to a handful of methods
 * that can be adapted to use the EDT 
 * should that become a problem.
 * 
 * @see #setInt(int, IntConsumer)
 * @see #setText(JTextField, String)
 */
public class DialFrameTestHelper
{
    /** Convenient list of all component names in the SpectrumFrame class. */
    private static final List<String>   compNames   = 
        List.of( 
            DialFrame.HUE_MAX,
            DialFrame.HUE_MIN,
            DialFrame.HUE_SLIDER,
            DialFrame.BRIGHT_SLIDER,
            DialFrame.BRIGHT_TEXT,
            DialFrame.SAT_SLIDER,
            DialFrame.SAT_TEXT
        );
    
    /** Read-only map of component names to components. */
    private final Map<String,JComponent>    compMap;
    
    /** 
     * The application window that is managed by the SpectrumFrame. 
     * It is under the control of an instance of this class
     * because of possible threading issues.
     */
    private final SpectrumDial    spectrum;
    /** The SpectrumFrame being controlled. */
    private final DialFrame     root;
    /** The JFrame that encapsulates the SpectrumFrame GUI. */
    private final JFrame            frame;
    /** 
     * Used to locate components in the SpectrumFrame's
     * GUI hierarchy.
     */
    private final ComponentFinder   finder;

    public DialFrameTestHelper()
    {
        // Instantiate the application's drawing class. Probably
        // not on the EDT at the moment, but the class's constructor
        // is very limited in scope.
        spectrum = new SpectrumDial( 500 );
        
        // Instantiate the SpectrumFrame. There are no threading issues
        // because the constructor doesn't create any GUI components.
        root = new DialFrame( spectrum );
        
        // Start the application; note that the start method executes
        root.start();
        
        // root.start() is going to start a task on the EDT.
        // Sleep until that task is finished.
        invokeAndWait( () -> {} );
        
        // Get the application GUI's JFrame
        frame = ComponentFinder.getJFrameByName( DialFrame.APP_FRAME );
        assertNotNull( frame );
        
        // Find all the components we need and store them in a map
        finder = new ComponentFinder( frame );
        Map<String,JComponent>    tempMap = new HashMap<>();
        for ( String name : compNames )
            tempMap.put( name, getComponent( name ) );
        
        // Make the map read-only.
        compMap = Collections.unmodifiableMap( tempMap );
    }
    
    public int getHueMinFromText()
    {
        JTextField  textField   = getTextField( DialFrame.HUE_MIN );
        int         min         = getInt( textField );
        return min;
    }
    
    public void setHueMinText( int min )
    {
        JTextField  textField   = getTextField( DialFrame.HUE_MIN );
        setInt( textField, min );
    }
    
    public int getHueMaxFromText()
    {
        JTextField  textField   = getTextField( DialFrame.HUE_MAX );
        int         max         = getInt( textField );
        return max;
    }
    
    public void setHueMaxText( int max )
    {
        JTextField  textField   = getTextField( DialFrame.HUE_MAX );
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
        JTextField  textField   = getTextField( DialFrame.SAT_TEXT );
        int         val         = getInt( textField );
        return val;
    }
    
    public void setSatText( int val )
    {
        JTextField  textField   = getTextField( DialFrame.SAT_TEXT );
        setInt( textField, val );
    }
    
    public int getSatFromSlider()
    {
        JSlider     slider  = getSlider( DialFrame.SAT_SLIDER );
        int         val     = getInt( slider );
        return val;
    }
    
    public void setSatSlider( int val )
    {
        JSlider     slider  = getSlider( DialFrame.SAT_SLIDER );
        setInt( slider, val );
    }
    
    public int getSatProperty()
    {
        int max = getInt( () -> root.getSaturation() );
        return max;
    }

    public int getBrightFromText()
    {
        JTextField  textField   = getTextField( DialFrame.BRIGHT_TEXT );
        int         val         = getInt( textField );
        return val;
    }
    
    public void setBrightText( int val )
    {
        JTextField  textField   = getTextField( DialFrame.BRIGHT_TEXT );
        setInt( textField, val );
    }
    
    public int getBrightFromSlider()
    {
        JSlider     slider  = getSlider( DialFrame.BRIGHT_SLIDER );
        int         val     = getInt( slider );
        return val;
    }
    
    public void setBrightSlider( int val )
    {
        JSlider     slider  = getSlider( DialFrame.BRIGHT_SLIDER );
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
        setText( textField, text );
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
        String      name    =  DialFrame.HUE_SLIDER;
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
    }
    
    private void setInt( JSlider slider, int val )
    {
        setInt( val, slider::setValue );
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
