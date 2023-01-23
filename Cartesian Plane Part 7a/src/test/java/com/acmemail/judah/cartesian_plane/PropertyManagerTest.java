package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.jupiter.api.Test;

class PropertyManagerTest
{
    @Test
    void testInit()
    {
        PropertyManager pmgr            = PropertyManager.INSTANCE;
        float           ticMajorWeight  = 
            pmgr.asFloat( CPConstants.TIC_MAJOR_WEIGHT_PN );
        assertEquals( 3.5f, ticMajorWeight );
        
        float           ticMajorLen     = 
            pmgr.asFloat( CPConstants.TIC_MAJOR_LEN_PN );
        assertEquals( 17, ticMajorLen );
        
        Color           expColor        = new Color( 0xFF0000 );
        Color           ticMajorColor   =
            pmgr.asColor( CPConstants.TIC_MAJOR_COLOR_PN );
        assertEquals( expColor, ticMajorColor );
    }
    
    @Test
    public void testPropertyChangeFire()
    {
        PropertyManager pmgr        = PropertyManager.INSTANCE;
        float           expOldValue = 5;
        float           expNewValue = expOldValue + 1;
        String          expName     = CPConstants.TIC_MAJOR_WEIGHT_PN;
        TestListener    listener    = new TestListener();
        
        pmgr.setProperty( expName, expOldValue );
        pmgr.addPropertyChangeListener( listener );
        pmgr.setProperty( expName, expNewValue );
        float           actNewValue = pmgr.asFloat( expName );
        
        assertEquals( expNewValue, actNewValue );
        assertEquals( "" + expOldValue, listener.getOldVal() );
        assertEquals( "" + expNewValue, listener.getNewVal() );
        assertEquals( pmgr, listener.getSource() );
    }
    
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
