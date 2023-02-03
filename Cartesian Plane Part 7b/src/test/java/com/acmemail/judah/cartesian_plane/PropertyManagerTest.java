package com.acmemail.judah.cartesian_plane;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PropertyManagerTest
{
    /** Declared here for convenience. */
    private final PropertyManager   pmgr    = PropertyManager.INSTANCE;

    @Test
    public void testAddPropertyChangeListener()
    {
        String          propName    = CPConstants.TIC_MAJOR_WEIGHT_PN;
        float           expOldVal   = pmgr.asFloat( propName );
        float           expNewVal   = expOldVal + 1;
        TestListener    listener    = new TestListener();
        
        pmgr.addPropertyChangeListener( listener );
        pmgr.setProperty( propName, expNewVal );
        
        // Sanity check: verify that the value actually changed
        // and its different from the old value.
        float           actNewValue = pmgr.asFloat( propName );
        assertEquals( expNewVal, actNewValue );
        assertNotEquals( expOldVal, actNewValue );

        float   actOldVal   = Float.parseFloat( (String)listener.getOldVal() );
        float   actNewVal   = Float.parseFloat( (String)listener.getNewVal() );
        assertEquals( expOldVal, actOldVal );
        assertEquals( expNewVal, actNewVal );
        assertEquals( pmgr, listener.getSource() );
        assertEquals( propName, listener.getName() );
    }

    @Test
    public void testRemovePropertyChangeListener()
    {
        String          origName        = CPConstants.TIC_MAJOR_WEIGHT_PN;
        float           origOldVal      = pmgr.asFloat( origName );
        float           origNewVal      = origOldVal + 1;
        TestListener    listener        = new TestListener();
        
        pmgr.setProperty( origName, origOldVal );
        pmgr.addPropertyChangeListener( listener );
        pmgr.setProperty( origName, origNewVal );
        
        // Verify that the listener fired as expected
        float   actOldVal   = Float.parseFloat( (String)listener.getOldVal() );
        float   actNewVal   = Float.parseFloat( (String)listener.getNewVal() );
        assertEquals( origOldVal, actOldVal );
        assertEquals( origNewVal, actNewVal );
        assertEquals( origName, listener.getName() );
        
        // Remove the listener and verify that it does NOT fire
        // when a property is changed
        pmgr.removePropertyChangeListener( listener );
        String      nextName        = CPConstants.TIC_MAJOR_COLOR_PN;
        Color       nextVal         = Color.BLUE;
        
        // Sanity check; nextName should be different from origName,
        // nextVal should be different from origNewVal
        assertNotEquals( origName, nextName );
        assertNotEquals( origNewVal, nextVal );
        
        pmgr.setProperty( nextName, nextVal );
        
        actOldVal   = Float.parseFloat( (String)listener.getOldVal() );
        actNewVal   = Float.parseFloat( (String)listener.getNewVal() );
        assertEquals( origOldVal, actOldVal );
        assertEquals( origNewVal, actNewVal );
        assertEquals( origName, listener.getName() );
    }

    @Test
    public void testAddPerPropertyChangeListener()
    {
        // Set up per-property listeners for two properties. Change one
        // of them. Verify that the correct listener fires for the change
        // and the other does not.
        String          posProp     = CPConstants.TIC_MAJOR_WEIGHT_PN;
        float           expOldValue = pmgr.asFloat( posProp );
        float           expNewValue = expOldValue + 1;
        TestListener    posListener = new TestListener();
        pmgr.addPropertyChangeListener( posProp, posListener );
        
        String          negProp     = CPConstants.TIC_MINOR_WEIGHT_PN;
        TestListener    negListener = new TestListener();
        pmgr.addPropertyChangeListener( negProp, negListener );
        
        pmgr.setProperty( posProp, expNewValue );
        
        // Check the listener that should have fired.
        // Keep in mind old/new values in TestListener are stored as
        // type Object, and are actually type String.
        float   actOldVal   = 
            Float.parseFloat( (String)posListener.getOldVal() );
        float   actNewVal   = 
            Float.parseFloat( (String)posListener.getNewVal() );
        assertEquals( posProp, posListener.getName() );
        assertEquals( expNewValue, actNewVal );
        assertEquals( expOldValue, actOldVal );
        assertEquals( pmgr, posListener.getSource() );
        
        // Check the listener that should not have fired.
        assertNull( negListener.getName() );
        assertNull( negListener.getNewVal() );
        assertNull( negListener.getOldVal() );
    }
    
    @Test
    public void testRemovePerPropertyChangeListener()
    {
        String          propName    = CPConstants.TIC_MAJOR_WEIGHT_PN;
        float           oldVal      = pmgr.asFloat( propName );
        float           newVal      = oldVal + 1;
        TestListener    listener    = new TestListener();
        pmgr.addPropertyChangeListener( propName, listener );
        
        // Change property and verify that listener fires as expected.
        pmgr.setProperty( propName, newVal );
        float   actOldVal   = 
            Float.parseFloat( (String)listener.getOldVal() );
        float   actNewVal   = 
            Float.parseFloat( (String)listener.getNewVal() );
        assertEquals( propName, listener.getName() );
        assertEquals( newVal, actNewVal );
        assertEquals( oldVal, actOldVal );
        assertEquals( pmgr, listener.getSource() );
        
        // Remove the listener and verify that it does not fire 
        // when the property is changed.
        pmgr.removePropertyChangeListener( propName, listener );
        
        float   nextVal     = newVal + 1;
        // sanity check
        assertNotEquals( nextVal, newVal );
        pmgr.setProperty( propName, nextVal );
        actNewVal = Float.parseFloat( (String)listener.getNewVal() );
        assertNotEquals( nextVal, actNewVal );
    }

    @ParameterizedTest
    @ValueSource( ints={ 5, 10, 15, 20 } )
    void testIntAccessors( int expVal )
    {
        String  propName    = "phonyIntProp";
        pmgr.setProperty( propName, expVal );
        int     actVal      = pmgr.asInt( propName );
        assertEquals( expVal, actVal );
    }

    @ParameterizedTest
    @ValueSource(floats={ 5.5f, 10.5f, 15.5f, 20.5f } )
    void testFloatAccessors( float expVal )
    {
        String  propName    = "phonyFloatProp";
        pmgr.setProperty( propName, expVal );
        float   actVal      = pmgr.asFloat( propName );
        assertEquals( expVal, actVal );
    }

    @ParameterizedTest
    @ValueSource(booleans={ true, false, true, false } )
    void testBooleanAccessors( boolean expVal )
    {
        String  propName    = "phonyBooleanProp";
        pmgr.setProperty( propName, expVal );
        boolean actVal      = pmgr.asBoolean( propName );
        assertEquals( expVal, actVal );
    }

    @ParameterizedTest
    @ValueSource(strings={ "abc", "def", "ghi", "jkl" } )
    void testStringAccessors( String expVal )
    {
        String  propName    = "phonyStringProp";
        pmgr.setProperty( propName, expVal );
        String  actVal      = pmgr.asString( propName );
        assertEquals( expVal, actVal );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0xFF0000", "0x00FF00", "0x0000FF", "#FF00FF" } )
    void testColorAccessors( String strVal )
    {
        String  propName    = "phonyColorProp";
        int     intColor    = Integer.decode( strVal );
        Color   expColor    = new Color( intColor );
        pmgr.setProperty( propName, expColor );
        Color   actColor    = pmgr.asColor( propName );
        assertEquals( expColor, actColor );
    }

    @Test
    void testFontStyleAccessor()
    {
        String  propName    = "phonyFontStyleProp";
        
        pmgr.setProperty( propName, "PLAIN" );
        assertEquals( Font.PLAIN, pmgr.asFontStyle( propName ) );
        
        pmgr.setProperty( propName, "ITALIC" );
        assertEquals( Font.ITALIC, pmgr.asFontStyle( propName ) );
        
        pmgr.setProperty( propName, "BOLD" );
        assertEquals( Font.BOLD, pmgr.asFontStyle( propName ) );
        
        Class<IllegalArgumentException> clazz   = 
            IllegalArgumentException.class;
        pmgr.setProperty( propName, "INVALID_FONT_STYLE" );
        assertThrows( clazz, () -> pmgr.asFontStyle( propName ) );
    }
    
    @Test
    public void nullValueTests()
    {
        String  propName    = "notAValidPropertyName";
        assertNull( pmgr.asInt( propName ) );
        assertNull( pmgr.asFloat( propName ) );
        assertNull( pmgr.asString( propName ) );
        assertNull( pmgr.asFontStyle( propName ) );
        assertNull( pmgr.asColor( propName ) );
        assertNull( pmgr.asBoolean( propName ) );
    }
    
    /**
     * Keeps track of the contents
     * of a property change event
     * when it is fired.
     * 
     * @author Jack Straub
     */
    private static class TestListener implements PropertyChangeListener
    {
        private Object  source  = null;
        private Object  oldVal  = null;
        private Object  newVal  = null;
        private String  name    = null;
        
        @Override
        public void propertyChange( PropertyChangeEvent evt )
        {
            source = evt.getSource();
            oldVal = evt.getOldValue();
            newVal = evt.getNewValue();
            name = evt.getPropertyName();
        }

        /**
         * Gets the value of the source variable.
         * @return the source
         */
        public Object getSource()
        {
            return source;
        }

        /**
         * Gets the value of the old-value variable.
         * @return the oldVal
         */
        public Object getOldVal()
        {
            return oldVal;
        }

        /**
         * Gets the value of the new-value variable.
         * @return the newVal
         */
        public Object getNewVal()
        {
            return newVal;
        }

        /**
         * Gets the value of the property name variable.
         * @return the name
         */
        public String getName()
        {
            return name;
        }
    }
}
