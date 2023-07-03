package com.acmemail.judah.sandbox;

import static com.acmemail.judah.sandbox.PropertyManager.AXIS;
import static com.acmemail.judah.sandbox.PropertyManager.COLOR;
import static com.acmemail.judah.sandbox.PropertyManager.GRID;
import static com.acmemail.judah.sandbox.PropertyManager.LENGTH;
import static com.acmemail.judah.sandbox.PropertyManager.MAJOR;
import static com.acmemail.judah.sandbox.PropertyManager.MINOR;
import static com.acmemail.judah.sandbox.PropertyManager.SPACING;
import static com.acmemail.judah.sandbox.PropertyManager.STROKE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.util.Optional;
import java.util.OptionalInt;

import org.junit.jupiter.api.Test;

class LinePropertySetTest
{
    private final PropertyManager   pMgr    = new PropertyManager();
    
	@Test
	void testLinePropertySet()
	{
        testLinePropertySet( AXIS );
        testLinePropertySet( MAJOR );
        testLinePropertySet( MINOR );
        testLinePropertySet( GRID );
	}
	
	private void testLinePropertySet( String major )
	{
	    Optional<Color>     color       = pMgr.getAsColor( major, COLOR );
        OptionalInt         length      = pMgr.getAsInt( major, LENGTH );
        OptionalInt         spacing     = pMgr.getAsInt( major, LENGTH );
        OptionalInt         stroke      = pMgr.getAsInt( major, LENGTH );
        LinePropertySet     set         = new LinePropertySet( major );
        
        assertEquals( color.isPresent(), set.hasColor(), major );
        assertEquals( length.isPresent(), set.hasLength(), major );
        assertEquals( spacing.isPresent(), set.hasSpacing(), major );
        assertEquals( stroke.isPresent(), set.hasStroke(), major );
        
        if ( color.isPresent() )
            assertEquals( color.get(), pMgr.getAsColor( major, COLOR ) );
        if ( length.isPresent() )
            assertEquals( length.getAsInt(), pMgr.getAsInt( major, LENGTH ) );
        if ( spacing.isPresent() )
            assertEquals( spacing.getAsInt(), pMgr.getAsInt( major, SPACING ) );
        if ( stroke.isPresent() )
            assertEquals( stroke.getAsInt(), pMgr.getAsInt( major, STROKE ) );
	}

	@Test
	void testApply() {
		fail("Not yet implemented");
	}

	@Test
	void testHasStroke() {
		fail("Not yet implemented");
	}

	@Test
	void testHasLength() {
		fail("Not yet implemented");
	}

	@Test
	void testHasSpacing() {
		fail("Not yet implemented");
	}

	@Test
	void testHasColor() {
		fail("Not yet implemented");
	}

	@Test
	void testGetStroke() {
		fail("Not yet implemented");
	}

	@Test
	void testGetLength() {
		fail("Not yet implemented");
	}

	@Test
	void testGetSpacing() {
		fail("Not yet implemented");
	}

	@Test
	void testGetColor() {
		fail("Not yet implemented");
	}

	@Test
	void testSetStroke() {
		fail("Not yet implemented");
	}

	@Test
	void testSetLength() {
		fail("Not yet implemented");
	}

	@Test
	void testSetSpacing() {
		fail("Not yet implemented");
	}

	@Test
	void testSetColor() {
		fail("Not yet implemented");
	}

}
