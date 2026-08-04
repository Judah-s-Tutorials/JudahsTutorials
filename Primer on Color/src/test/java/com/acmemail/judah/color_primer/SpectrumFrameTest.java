package com.acmemail.judah.color_primer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.acmemail.judah.cartesian_plane.SpectrumFrameTestHelper;

class SpectrumFrameTest
{    
    private static final char   DEGREE      = '\u00b0';
    private static final char   PERCENT     = '%';
    
    private static final int    HUE_MIN_DEF     = 20;
    private static final int    HUE_MIN_ALT     = HUE_MIN_DEF + 10;
    private static final int    HUE_MAX_DEF     = 340;
    private static final int    HUE_MAX_ALT     = HUE_MAX_DEF - 10;
    private static final int    SAT_DEF         = 90;
    private static final int    SAT_VAL_ALT     = SAT_DEF - 20;
    private static final int    BRIGHT_DEF      = SAT_DEF - 10;
    private static final int    BRIGHT_VAL_ALT  = BRIGHT_DEF - 20;
    
    private static final SpectrumFrameTestHelper   helper = 
        new SpectrumFrameTestHelper();
    
    @BeforeAll
    public static void beforeAll()
        throws InvocationTargetException, InterruptedException
    {
    }

    @BeforeEach
    void setUp() throws Exception
    {
        helper.setHueMinSlider( HUE_MIN_DEF );
        helper.setHueMaxSlider( HUE_MAX_DEF );
        helper.setSatSlider( SAT_DEF );
        helper.setBrightSlider( BRIGHT_DEF );
        validateHueMin( HUE_MIN_DEF );
        validateHueMax( HUE_MAX_DEF );
        validateSatVal( SAT_DEF );
        validateBrightVal( BRIGHT_DEF );
    }

    @Test
    public void testHueMinSlider()
    {
        validateHueMin();
        helper.setHueMinSlider( HUE_MIN_ALT );
        validateHueMin( HUE_MIN_ALT );
    }

    @Test
    public void testHueMinText()
    {
        validateHueMin();
        helper.setHueMinText( HUE_MAX_ALT );
        validateHueMin( HUE_MAX_ALT );
    }

    @Test
    public void testHueMaxSlider()
    {
        validateHueMax();
        helper.setHueMaxSlider( HUE_MAX_ALT );
        validateHueMax( HUE_MAX_ALT );
    }

    @Test
    public void testHueMaxText()
    {
        validateHueMax();
        helper.setHueMaxText( HUE_MAX_ALT );
        validateHueMax( HUE_MAX_ALT );
    }

    @Test
    public void testBrightSlider()
    {
        validateBrightVal();
        helper.setBrightSlider( BRIGHT_VAL_ALT );
        validateBrightVal( BRIGHT_VAL_ALT );
    }

    @Test
    public void testBrightText()
    {
        validateBrightVal();
        helper.setBrightText( BRIGHT_VAL_ALT );
        validateBrightVal( BRIGHT_VAL_ALT );
    }

    @Test
    public void testSatSlider()
    {
        validateSatVal();
        helper.setSatSlider( SAT_VAL_ALT );
        validateSatVal( SAT_VAL_ALT );
    }

    @Test
    public void testSatText()
    {
        validateSatVal();
        helper.setSatText( SAT_VAL_ALT );
        validateSatVal( SAT_VAL_ALT );
    }
    
    /**
     * Describes a single hue/sat/bright text field for the purposes
     * of {@link #testTextField(TextFieldConfig, String)}: the
     * component to drive, its alternate test value, the unit suffix
     * it accepts, and the getters used to confirm the slider,
     * property, and text field all agree.
     */
    private record TextFieldConfig(
        String       componentName,
        int          altValue,
        char         unit,
        IntSupplier  sliderGetter,
        IntSupplier  propertyGetter,
        IntSupplier  textGetter )
    {}

    private static Stream<TextFieldConfig> textFieldConfigs()
    {
        return Stream.of(
            new TextFieldConfig( SpectrumFrame.HUE_MIN, HUE_MIN_ALT, DEGREE,
                helper::getHueMinFromSlider, helper::getHueMinProperty,
                helper::getHueMinFromText ),
            new TextFieldConfig( SpectrumFrame.HUE_MAX, HUE_MAX_ALT, DEGREE,
                helper::getHueMaxFromSlider, helper::getHueMaxProperty,
                helper::getHueMaxFromText ),
            new TextFieldConfig( SpectrumFrame.SAT_TEXT, SAT_VAL_ALT, PERCENT,
                helper::getSatFromSlider, helper::getSatProperty,
                helper::getSatFromText ),
            new TextFieldConfig( SpectrumFrame.BRIGHT_TEXT, BRIGHT_VAL_ALT, PERCENT,
                helper::getBrightFromSlider, helper::getBrightProperty,
                helper::getBrightFromText )
        );
    }

    private static Stream<Arguments> textFieldArgs()
    {
        return textFieldConfigs().flatMap( cfg -> Stream.of(
            Arguments.of( cfg, "" + cfg.altValue() ),
            Arguments.of( cfg, "" + cfg.altValue() + cfg.unit() )
        ));
    }

    @ParameterizedTest
    @MethodSource( "textFieldArgs" )
    public void testTextField( TextFieldConfig cfg, String text )
    {
        Color   defColor    = helper.getBackgroundColor( cfg.componentName() );
        validateField( cfg );

        helper.setText( cfg.componentName(), text );
        validateField( cfg, cfg.altValue() );
        Color   testColor   = helper.getBackgroundColor( cfg.componentName() );
        assertEquals( defColor, testColor );

        String  errorText   =   "Q" + text;
        helper.setText( cfg.componentName(), errorText );
        assertEquals( cfg.altValue(), cfg.sliderGetter().getAsInt() );
        assertEquals( cfg.altValue(), cfg.propertyGetter().getAsInt() );
        testColor   = helper.getBackgroundColor( cfg.componentName() );
        assertNotEquals( defColor, testColor );

        helper.setText( cfg.componentName(), text );
        validateField( cfg, cfg.altValue() );
        testColor   = helper.getBackgroundColor( cfg.componentName() );
        assertEquals( defColor, testColor );
    }
    
    private void validateHueMin()
    {
        int         appMin      = helper.getHueMinProperty();
        validateHueMin( appMin );
    }
    
    /**
     * Verify that the hue slider min value, 
     * the corresponding text field value,
     * and the min property getter agree.
     */
    private void validateHueMin( int expMin )
    {
        int         appMin      = helper.getHueMinProperty();
        int         sliderMin   = helper.getHueMinFromSlider();
        int         textMin     = helper.getHueMinFromText();
        assertEquals( expMin, appMin );
        assertEquals( expMin, sliderMin );
        assertEquals( expMin, textMin );
    }
    
    private void validateHueMax()
    {
        int         appMax      = helper.getHueMaxProperty();
        validateHueMax( appMax );
    }
    
    /**
     * Verify that the hue slider max value, 
     * the corresponding text field value,
     * and the max property getter agree.
     */
    private void validateHueMax( int expMax )
    {
        int         appMax      = helper.getHueMaxProperty();
        int         sliderMax   = helper.getHueMaxFromSlider();
        int         textMax     = helper.getHueMaxFromText();
        assertEquals( expMax, appMax );
        assertEquals( expMax, sliderMax );
        assertEquals( expMax, textMax );
    }
    
    private void validateSatVal()
    {
        int         appSat      = helper.getSatProperty();
        validateSatVal( appSat );
    }
    
    /**
     * Verify that the saturation slider value, 
     * the corresponding text field value,
     * and the property getter agree.
     */
    private void validateSatVal( int expVal )
    {
        int         appVal      = helper.getSatProperty();
        int         sliderVal   = helper.getSatFromSlider();
        int         textVal     = helper.getSatFromText();
        assertEquals( expVal, appVal );
        assertEquals( expVal, sliderVal );
        assertEquals( expVal, textVal );
    }
    
    private void validateBrightVal()
    {
        int         appBright   = helper.getBrightProperty();
        validateBrightVal( appBright );
    }
    
    /**
     * Verify that the saturation slider value, 
     * the corresponding text field value,
     * and the property getter agree.
     */
    private void validateBrightVal( int expVal )
    {
        int         appVal      = helper.getBrightProperty();
        int         sliderVal   = helper.getBrightFromSlider();
        int         textVal     = helper.getBrightFromText();
        assertEquals( expVal, appVal );
        assertEquals( expVal, sliderVal );
        assertEquals( expVal, textVal );
    }

    private void validateField( TextFieldConfig cfg )
    {
        int         appVal      = cfg.propertyGetter().getAsInt();
        validateField( cfg, appVal );
    }

    /**
     * Verify that the given text field's slider value,
     * text field value, and property getter agree.
     */
    private void validateField( TextFieldConfig cfg, int expVal )
    {
        assertEquals( expVal, cfg.propertyGetter().getAsInt() );
        assertEquals( expVal, cfg.sliderGetter().getAsInt() );
        assertEquals( expVal, cfg.textGetter().getAsInt() );
    }
}
