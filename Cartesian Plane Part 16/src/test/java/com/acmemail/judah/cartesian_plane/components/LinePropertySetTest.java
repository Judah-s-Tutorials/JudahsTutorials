package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.test_utils.LinePropertySetInitializer;

public abstract class LinePropertySetTest
{
    private static final PropertyManager    pMgr    = 
        PropertyManager.INSTANCE;
    
    private final Supplier<LinePropertySet> setSupplier;
    
    private final String            drawProperty;
    private final String            strokeProperty;
    private final String            lengthProperty;
    private final String            spacingProperty;
    private final String            colorProperty;

    private final Optional<Boolean> drawOrig;
    private final Optional<Float>   strokeOrig;
    private final Optional<Float>   lengthOrig;
    private final Optional<Float>   spacingOrig;
    private final Optional<Color>   colorOrig;

    private Optional<Boolean>       drawCurr;
    private Optional<Float>         strokeCurr;
    private Optional<Float>         lengthCurr;
    private Optional<Float>         spacingCurr;
    private Optional<Color>         colorCurr;
    
  public LinePropertySetTest(
        String drawProperty,
        String strokeProperty, 
        String lengthProperty, 
        String spacingProperty, 
        String colorProperty,
        Supplier<LinePropertySet> setSupplier
    )
    {
        super();
        LinePropertySetInitializer.initProperties();
        this.setSupplier = setSupplier;

        this.drawProperty = drawProperty;
        this.strokeProperty = strokeProperty;
        this.lengthProperty = lengthProperty;
        this.spacingProperty = spacingProperty;
        this.colorProperty = colorProperty;
        
        drawCurr = drawOrig = asBoolean( drawProperty );
        strokeCurr = strokeOrig = asFloat( strokeProperty );
        lengthCurr = lengthOrig = asFloat( lengthProperty );
        colorCurr = colorOrig = asColor( colorProperty );
        spacingCurr = spacingOrig = asFloat( spacingProperty );
    }

    @Test
    public void test()
    {
        LinePropertySet set = setSupplier.get();
        
        // Test all has... methods.
        assertPresentIf( set );
        
        // Verify that newly created set has expected values
        assertHasOrigValues( set );
        
        // Get unique values for all properties, then reset and verify
        // that all properties are returned to their original values.
        getUniqueValues( set );
        assertHasCurrValues( set );
        set.reset();
        assertHasOrigValues( set );
        
        // Get unique values for all properties, then apply and verify
        // that all properties are updated via the PropertyManager.
        getUniqueValues( set );
        assertHasCurrValues( set );
        set.apply();
        assertHasAppliedValues();
    }
    
    private void assertPresentIf( LinePropertySet set )
    {
        assertEquals( set.hasDraw(), drawOrig.isPresent() );
        assertEquals( set.hasColor(), colorOrig.isPresent() );
        assertEquals( set.hasStroke(), strokeOrig.isPresent() );
        assertEquals( set.hasLength(), lengthOrig.isPresent() );
        assertEquals( set.hasSpacing(), spacingOrig.isPresent() );
    }
    
    private void assertHasOrigValues( LinePropertySet set )
    {
        assertEqualsIfPresent( set.getDraw(), drawOrig );
        assertEqualsIfPresent( set.getColor(), colorOrig );
        assertEqualsIfPresent( set.getStroke(), strokeOrig );
        assertEqualsIfPresent( set.getLength(), lengthOrig );
        assertEqualsIfPresent( set.getSpacing(), spacingOrig );
    }
    
    private void assertHasCurrValues( LinePropertySet set )
    {
        assertEqualsIfPresent( set.getDraw(), drawCurr );
        assertEqualsIfPresent( set.getColor(), colorCurr );
        assertEqualsIfPresent( set.getStroke(), strokeCurr );
        assertEqualsIfPresent( set.getLength(), lengthCurr );
        assertEqualsIfPresent( set.getSpacing(), spacingCurr );
    }
    
    private void assertHasAppliedValues()
    {
        assertEquals( drawCurr, asBoolean( drawProperty ) );
        assertEquals( colorCurr, asColor( colorProperty ) );
        assertEquals( strokeCurr, asFloat( strokeProperty ) );
        assertEquals( lengthCurr, asFloat( lengthProperty ) );
        assertEquals( spacingCurr, asFloat( spacingProperty ) );
    }
    
    private void getUniqueValues( LinePropertySet set )
    {
        if ( (drawCurr = newBoolean( drawCurr )).isPresent() )
            set.setDraw( drawCurr.get() );
        if ( (colorCurr = newColor( colorCurr )).isPresent() )
            set.setColor( colorCurr.get() );
        if ( (strokeCurr = newFloat( strokeCurr )).isPresent() )
            set.setStroke( strokeCurr.get() );
        if ( (lengthCurr = newFloat( lengthCurr )).isPresent() )
            set.setLength( lengthCurr.get() );
        if ( (spacingCurr = newFloat( spacingCurr )).isPresent() )
            set.setSpacing( spacingCurr.get() );            
    }
    
    private static void 
    assertEqualsIfPresent( Object actual, Optional<?> expected )
    {
        if ( expected.isPresent() )
            assertEquals( actual, expected.get() );
    }
    
    private static Optional<Float> asFloat( String propertyName )
    {
        Float           val         = pMgr.asFloat( propertyName );
        Optional<Float> optional    = 
            val != null ? Optional.of( val ) : Optional.empty();
        return optional;
    }
    
    private static Optional<Color> asColor( String propertyName )
    {
        Color           val         = pMgr.asColor( propertyName );
        Optional<Color> optional    = 
            val != null ? Optional.of( val ) : Optional.empty();
        return optional;
    }
    
    private static 
    Optional<Boolean> asBoolean( String propertyName )
    {
        Boolean             val         = pMgr.asBoolean( propertyName );
        Optional<Boolean>   optional    = 
            val != null ? Optional.of( val ) : Optional.empty();
        return optional;
    }
    
    private static 
    Optional<Float> newFloat( Optional<Float> currFloat )
    {
        Optional<Float> optional    = Optional.empty();
        if ( !currFloat.isEmpty() )
        {
            float newVal    = currFloat.get() + 10;
            optional = Optional.of( newVal );
        }
        return optional;
    }
    
    private static 
    Optional<Color> newColor( Optional<Color> currColor )
    {
        Optional<Color> optional    = Optional.empty();
        if ( !currColor.isEmpty() )
        {
            Color   val     = currColor.get();
            int     newRGB  = val.getRGB() & 0xFFFFFF + 10;
            Color   newVal  = new Color( newRGB );
            optional = Optional.of( newVal );
        }
        return optional;
    }
    
    private static Optional<Boolean> 
    newBoolean( Optional<Boolean> currBoolean )
    {
        Optional<Boolean> optional  = Optional.empty();
        if ( !currBoolean.isEmpty() )
        {
            boolean newVal  = !currBoolean.get();
            optional = Optional.of( newVal );
        }
        return optional;
    }
}
