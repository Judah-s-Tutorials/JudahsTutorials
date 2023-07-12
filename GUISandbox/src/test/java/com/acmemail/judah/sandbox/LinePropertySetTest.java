package com.acmemail.judah.sandbox;

import static com.acmemail.judah.sandbox.PropertyManager.AXES;
import static com.acmemail.judah.sandbox.PropertyManager.COLOR;
import static com.acmemail.judah.sandbox.PropertyManager.GRID;
import static com.acmemail.judah.sandbox.PropertyManager.LENGTH;
import static com.acmemail.judah.sandbox.PropertyManager.MAJOR;
import static com.acmemail.judah.sandbox.PropertyManager.MINOR;
import static com.acmemail.judah.sandbox.PropertyManager.SPACING;
import static com.acmemail.judah.sandbox.PropertyManager.STROKE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import org.junit.jupiter.api.Test;

class LinePropertySetTest
{
    private final PropertyManager   pMgr    = PropertyManager.instanceOf();
    
	@Test
	void testLinePropertySet()
	{
        testLinePropertySet( AXES );
        testLinePropertySet( MAJOR );
        testLinePropertySet( MINOR );
        testLinePropertySet( GRID );
	}
	
	private void testLinePropertySet( String major )
	{
	    Optional<Color>     color       = pMgr.getAsColor( major, COLOR );
        OptionalDouble      length      = pMgr.getAsDouble( major, LENGTH );
        OptionalDouble      spacing     = pMgr.getAsDouble( major, SPACING );
        OptionalDouble      stroke      = pMgr.getAsDouble( major, STROKE );
        LinePropertySet     set         = new LinePropertySet( major );
        
        assertEquals( color.isPresent(), set.hasColor(), major );
        assertEquals( length.isPresent(), set.hasLength(), major );
        assertEquals( spacing.isPresent(), set.hasSpacing(), major );
        assertEquals( stroke.isPresent(), set.hasStroke(), major );
        
        if ( color.isPresent() )
            assertEquals( color.get(), set.getColor() );
        if ( length.isPresent() )
            assertEquals( length.getAsDouble(), set.getLength(), major );
        if ( spacing.isPresent() )
            assertEquals( spacing.getAsDouble(), set.getSpacing(), major );
        if ( stroke.isPresent() )
            assertEquals( stroke.getAsDouble(), set.getStroke(), major );
	}

	@Test
	void testApply()
	{
        testApply( AXES );
//        testApply( MAJOR );
//        testApply( MINOR );
//        testApply( GRID );
	}
    
    private void testApply( String major )
    {
        Optional<Color>     color       = pMgr.getAsColor( major, COLOR );
        OptionalInt         length      = pMgr.getAsInt( major, LENGTH );
        OptionalInt         spacing     = pMgr.getAsInt( major, SPACING );
        OptionalInt         stroke      = pMgr.getAsInt( major, STROKE );
        LinePropertySet     set         = new LinePropertySet( major );
        
        Color               expColor    = 
            getUniqueColor( color.orElse( Color.WHITE ) );
        int                 expLength   = length.orElse( 0 ) + 5;
        int                 expSpacing  = spacing.orElse( 0 ) + 5;
        int                 expStroke   = stroke.orElse( 0 ) + 5;
        
        set.setColor( expColor );
        set.setLength( expLength );
        set.setSpacing( expSpacing );
        set.setStroke( expStroke );
        set.apply();
        
        Optional<Color>     actColor    = pMgr.getAsColor( major, COLOR );
        OptionalInt         actLength   = pMgr.getAsInt( major, LENGTH );
        OptionalInt         actSpacing  = pMgr.getAsInt( major, SPACING );
        OptionalInt         actStroke   = pMgr.getAsInt( major, STROKE );
        
        assertEquals( color.isPresent(), actColor.isPresent() );
        assertEquals( length.isPresent(), actLength.isPresent() );
        assertEquals( spacing.isPresent(), actSpacing.isPresent() );
        assertEquals( stroke.isPresent(), actStroke.isPresent() );
        
        if ( color.isPresent() )
            assertEquals( expColor, actColor.get(), major );
        if ( length.isPresent() )
            assertEquals( expLength, actLength.getAsInt(), major );
        if ( spacing.isPresent() )
            assertEquals( expSpacing, actSpacing.getAsInt(), major );
        if ( length.isPresent() )
            assertEquals( expStroke, actStroke.getAsInt(), major );
    }

	@Test
	void testGetStroke()
	{
        testGetStroke( AXES );
        testGetStroke( MAJOR );
        testGetStroke( MINOR );
        testGetStroke( GRID );
	}
	
	private void testGetStroke( String major )
	{
	    LinePropertySet    set     = new LinePropertySet( major );
        OptionalDouble     stroke  = pMgr.getAsDouble( major, STROKE );
        if ( stroke.isPresent() )
        {
            double  init    = set.getStroke();
            double  exp     = init += 5;
            set.setStroke( exp );
            assertEquals( exp, set.getStroke() );
        }
	}

	@Test
	void testGetLength()
	{
        testGetLength( AXES );
        testGetLength( MAJOR );
        testGetLength( MINOR );
        testGetLength( GRID );
	}
    
    private void testGetLength( String major )
    {
        LinePropertySet    set     = new LinePropertySet( major );
        OptionalDouble     length  = pMgr.getAsDouble( major, LENGTH );
        if ( length.isPresent() )
        {
            double  init    = set.getLength();
            double  exp     = init += 5;
            set.setLength( exp );
            assertEquals( exp, set.getLength() );
        }
    }

    @Test
    void testGetSpacing()
    {
        testGetSpacing( AXES );
        testGetSpacing( MAJOR );
        testGetSpacing( MINOR );
        testGetSpacing( GRID );
    }
    
    private void testGetSpacing( String major )
    {
        LinePropertySet    set     = new LinePropertySet( major );
        OptionalDouble     spacing = pMgr.getAsDouble( major, LENGTH );
        if ( spacing.isPresent() )
        {
            double  init    = set.getSpacing();
            double  exp     = init += 5;
            set.setSpacing( exp );
            assertEquals( exp, set.getSpacing() );
        }
    }

    @Test
    void testGetColor()
    {
        testGetColor( AXES );
        testGetColor( MAJOR );
        testGetColor( MINOR );
        testGetColor( GRID );
    }
    
    private void testGetColor( String major )
    {
        LinePropertySet    set     = new LinePropertySet( major );
        Optional<Color>    color   = pMgr.getAsColor( major, COLOR );
        if ( color.isPresent() )
        {
            Color   initColor       = set.getColor();
            Color   expColor        = getUniqueColor( initColor );
            set.setColor( expColor );
            assertEquals( expColor, set.getColor() );
        }
    }
    
    private Color getUniqueColor( Color initColor )
    {
        int     initColorInt    = initColor.getRGB();
        int     blue            = initColorInt & 0x000000FF;
        int     newColorInt     = initColorInt + 
            blue < 0xFF ? 1 : -1;
        Color   newColor        = new Color( newColorInt );
        return newColor;
    }
}
