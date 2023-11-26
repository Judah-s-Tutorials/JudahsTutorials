package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.LPPTestDialog;
import com.acmemail.judah.cartesian_plane.test_utils.LinePropertySetInitializer;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LinePropertiesPanelTest
{
    private static final LPSMap     setMapOrig  = new LPSMap();
    private static final LPSMap     setMapNew   = new LPSMap();
    private static LPPTestDialog    dialog;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        LinePropertySetInitializer.initProperties();
            GUIUtils.schedEDTAndWait( () ->
                dialog = new LPPTestDialog()
        );
        dialog.setDialogVisible( true );
            
        setMapOrig.put( new LinePropertySetAxes() );
        setMapOrig.put( new LinePropertySetGridLines() );
        setMapOrig.put( new LinePropertySetTicMajor() );
        setMapOrig.put( new LinePropertySetTicMinor() );
        
        setMapOrig.values().stream()
            .map( s -> newLinePropertySet( s ) )
            .forEach( setMapNew::put );
    }

    @Order( 5 )
    @Test
    public void testInit()
    {
        dialog.getRadioButtons().stream()
            .peek( dialog::doClick )
            .map( b -> b.get() )
            .peek( s -> Utils.pause( 250 ) )
            .map( s -> (LinePropertySet)s )
            .peek( s -> assertEnabledSynch( s ) )
            .map( s -> new LinePropertySet[] {s, null} )
            .peek( a -> a[1] = newInstance( a[0] ) )
            .peek( a -> dialog.getAllProperties( a[1] ) )
            .forEach( a -> assertEqualsSet( a[0], a[1] ) );
    }
    
    @Order( 10 )
    @Test
    public void testSynchOnTraversal()
    {
        // Select a button ("initial button")
        // Give all components new values ("new values")
        // Select a different button
        // Verify that the LinePropertySet associated with the initia;l
        //     button has been updated with new values
        dialog.getRadioButtons().forEach( button -> {
            LinePropertySet storedValues    = button.get();
            LinePropertySet newValues       = 
                setMapNew.get( storedValues );
            dialog.doClick( button );
            Utils.pause( 250 );
            assertEnabledSynch( storedValues );
            dialog.synchRight( newValues );
            Utils.pause( 250 );
            selectOther();
            Utils.pause( 250 );
            assertEqualsSet( storedValues, newValues );
        });
    }
    
    @Order( 15 )
    @Test
    public void testReset()
    {
        dialog.reset();
        dialog.getRadioButtons().forEach( button -> {
            LinePropertySet storedValues    = button.get();
            LinePropertySet origValues      = 
                setMapOrig.get( storedValues );
            assertEqualsSet( storedValues, origValues );
        });
    }
    
    @Order( 20 )
    @Test
    public void testApply()
    {
        dialog.getRadioButtons().forEach( button -> {
            LinePropertySet storedValues    = button.get();
            LinePropertySet newValues       = 
                setMapNew.get( storedValues );
            dialog.doClick( button );
            Utils.pause( 250 );
            dialog.synchRight( newValues );
            Utils.pause( 250 );
            selectOther();
            Utils.pause( 250 );
            assertEqualsSet( storedValues, newValues );
        });
        
        dialog.apply();
        Utils.pause( 250 );
        dialog.getRadioButtons().forEach( button -> {
            LinePropertySet storedValues    = button.get();
            LinePropertySet newValues       = 
                setMapNew.get( storedValues );
            LinePropertySet pMgrValues      =
                newInstance( storedValues );
            assertEqualsSet( storedValues, newValues );
            assertEqualsSet( newValues, pMgrValues );
        });
    }
    
    @Order( 25 )
    @Test
    public void testClose()
    {
        PRadioButton<LinePropertySet>   button  = 
            getButton( b -> b.isSelected() );
        LinePropertySet buttonProperties    = button.get();
        LinePropertySet compValues          = 
            newInstance( buttonProperties );
        dialog.getAllProperties( compValues );
        
        // sanity check
        assertEqualsSet( buttonProperties, compValues );
        assertTrue( dialog.isDialogVisible() );
        
        dialog.close();
        Utils.pause( 500 );
        assertFalse( dialog.isDialogVisible() );
        Utils.pause( 500 );
        dialog.setDialogVisible( true );
        Utils.pause( 500 );
        
        assertTrue( button.isSelected() );
        LinePropertySet testProperties      = button.get();
        dialog.getAllProperties( compValues );
        assertEqualsSet( testProperties, buttonProperties );
        assertEqualsSet( buttonProperties, compValues );        
    }
    
    private PRadioButton<LinePropertySet> 
    getButton( Predicate<PRadioButton<LinePropertySet>> pred )
    {
        PRadioButton<LinePropertySet>   button  = 
            dialog.getRadioButtons().stream()
                .filter( pred )
                .findFirst().orElse( null );
        assertNotNull( button );
        return button;
    }
    private static void assertEnabledSynch( LinePropertySet set )
    {
        assertEquals( set.hasStroke(), dialog.isStrokeEnabled() );
        assertEquals( set.hasLength(), dialog.isLengthEnabled() );
        assertEquals( set.hasSpacing(), dialog.isSpacingEnabled() );
        assertEquals( set.hasColor(), dialog.isColorButtonEnabled() );
        assertEquals( set.hasColor(), dialog.isColorFieldEnabled() );
        assertEquals( set.hasDraw(), dialog.isDrawEnabled() );
    }
    
    private static void 
    assertEqualsSet( LinePropertySet set1, LinePropertySet set2 )
    {
        boolean hasDraw = set1.hasDraw();
        boolean hasLength = set1.hasLength();
        boolean hasSpacing = set1.hasSpacing();
        boolean hasStroke = set1.hasStroke();
        boolean hasColor = set1.hasColor();
        
        assertEquals( hasDraw, set2.hasDraw() );
        assertEquals( hasLength, set2.hasLength() );
        assertEquals( hasSpacing, set2.hasSpacing() );
        assertEquals( hasStroke, set2.hasStroke() );
        assertEquals( hasColor, set2.hasColor() );
        
        if ( hasDraw )
            assertEquals( set1.getDraw(), set2.getDraw() );
        if ( hasLength )
            assertEquals( set1.getLength(), set2.getLength() );
        if ( hasSpacing )
            assertEquals( set1.getSpacing(), set2.getSpacing() );
        if ( hasStroke )
            assertEquals( set1.getStroke(), set2.getStroke() );
        if ( hasColor )
            assertEquals( set1.getColor(), set2.getColor() );
    }

    private void selectOther()
    {
        PRadioButton<LinePropertySet>   other   =
            dialog.getRadioButtons().stream()
                .filter( rb -> !rb.isSelected() )
                .findFirst().orElse( null );
        assertNotNull( other );
        dialog.doClick( other );
    }
    
    private static LinePropertySet 
    newLinePropertySet( LinePropertySet setIn )
    {
        LinePropertySet         setOut  = newInstance( setIn );
        
        if ( setIn.hasDraw() )
            setOut.setDraw( !setIn.getDraw() );
        if ( setIn.hasStroke() )
            setOut.setStroke( setIn.getStroke() + 5 );
        if ( setIn.hasLength() )
            setOut.setLength( setIn.getLength() + 10 );
        if ( setIn.hasSpacing() )
            setOut.setLength( setIn.getSpacing() + 15 );
        if ( setIn.hasColor() )
        {   
            int     rgb     = setIn.getColor().getRGB() + 20;
            setOut.setColor( new Color( rgb ) );
        }
        
        return setOut;
    }

    private static LinePropertySet newInstance( LinePropertySet setIn )
    {
        @SuppressWarnings("unchecked")
        Class<LinePropertySet>  clazz   = 
            (Class<LinePropertySet>)setIn.getClass();
        LinePropertySet         setOut  = null;
        try
        {
            Constructor<LinePropertySet>    ctor    = 
                clazz.getConstructor();
            Object      inst    = ctor.newInstance();
            assertTrue( inst instanceof LinePropertySet );
            setOut = (LinePropertySet)inst;
        }
        catch ( 
            NoSuchMethodException 
            | InvocationTargetException
            | IllegalAccessException 
            | InstantiationException exc )
        {
            String  msg = "New LinePropertySet not instantiated.";
            fail( msg, exc );
        }
        
        return setOut;
    }

    @SuppressWarnings("serial")
    private static class LPSMap extends 
        HashMap<Class<?>, LinePropertySet>
    {
        public void put( LinePropertySet set )
        {
            put( set.getClass(), set );
        }

        public LinePropertySet get( LinePropertySet setIn )
        {
            LinePropertySet setOut  = get( setIn.getClass() );
            assertNotNull( setOut );
            return setOut;
        }
    }
}
