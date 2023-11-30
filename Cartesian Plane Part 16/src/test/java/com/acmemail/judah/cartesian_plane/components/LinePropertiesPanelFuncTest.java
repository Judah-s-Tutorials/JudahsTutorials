package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.acmemail.judah.cartesian_plane.test_utils.LPPTestDialog;
import com.acmemail.judah.cartesian_plane.test_utils.LinePropertySetInitializer;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LinePropertiesPanelFuncTest
{
    private static final LPSMap     setMapOrig  = new LPSMap();
    private static final LPSMap     setMapNew   = new LPSMap();
    private static LPPTestDialog    dialog;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        LinePropertySetInitializer.initProperties();
        dialog = LPPTestDialog.getDialog();
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
            .peek( s -> assertSetsSynched( s, setMapOrig.get(s ) ) )
            .forEach( s -> assertPropertiesSynched( s ) );
    }
    
    @Order( 10 )
    @Test
    public void testSynchOnTraversal()
    {
        // Select a button ("initial button")
        // Give all components new values ("new values")
        // Select a different button
        // Verify that the LinePropertySet associated with the initial
        //     button has been updated with new values
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
            assertSetsSynched( storedValues, newValues );
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
        assertSetsSynched( storedValues, origValues );
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
            assertSetsSynched( storedValues, newValues );
        });
        
        dialog.apply();
        Utils.pause( 250 );
        dialog.getRadioButtons().forEach( button -> {
            LinePropertySet storedValues    = button.get();
            LinePropertySet newValues       = 
                setMapNew.get( storedValues );
            LinePropertySet pMgrValues      =
                newInstance( storedValues );
            assertSetsSynched( storedValues, newValues );
            assertSetsSynched( newValues, pMgrValues );
        });
    }
    
    @Order( 25 )
    @Test
    public void testClose()
    {
        PRadioButton<LinePropertySet>   button  = 
            getButton( b -> b.isSelected() );
        LinePropertySet buttonProperties    = button.get();
        
        // sanity check
        assertPropertiesSynched( buttonProperties );
        assertTrue( dialog.isDialogVisible() );
        
        dialog.close();
        Utils.pause( 250 );
        assertFalse( dialog.isDialogVisible() );
        dialog.setDialogVisible( true );
        Utils.pause( 250 );
        assertTrue( dialog.isDialogVisible() );
        
        assertTrue( dialog.isSelected( button ) );
        LinePropertySet testProperties      = button.get();
        assertSetsSynched( testProperties, buttonProperties );
        assertPropertiesSynched( buttonProperties );        
    }
    
    @Order( 25 )
    @Test
    public void testApplyReset()
    {
        // Special test case:
        // . change stroke value
        // . apply
        // . reset
        // . verify that the stroke value remains as modified
        final int   newVal  = 10;
        PRadioButton<LinePropertySet>   button  = 
            getButton( b -> b.isSelected() );
        LinePropertySet origSet = button.get();
        LinePropertySet testSet = newInstance( origSet );
        assertPropertiesSynched( origSet );
        assertNotEquals( newVal, origSet.getStroke() );
        
        testSet.setStroke( newVal );
        dialog.synchRight( testSet );
        dialog.apply();
        dialog.reset();
        
        LPPTestDialog.CompConfig  config  = dialog.getAllProperties();
        assertEquals( newVal, config.stroke );
        assertEquals( newVal, origSet.getStroke() );
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
    
    private void assertPropertiesSynched( LinePropertySet set1 )
    {
        LPPTestDialog.CompConfig    config  = dialog.getAllProperties();
        boolean hasStroke = set1.hasStroke();
        boolean hasLength = set1.hasLength();
        boolean hasSpacing = set1.hasSpacing();
        boolean hasColor = set1.hasColor();
        boolean hasDraw = set1.hasDraw();
        
        assertEquals( hasStroke, config.strokeSpinnerEnabled );
        assertEquals( hasStroke, config.strokeLabelEnabled );
        assertEquals( hasLength, config.lengthSpinnerEnabled );
        assertEquals( hasLength, config.lengthLabelEnabled );
        assertEquals( hasSpacing, config.spacingSpinnerEnabled );
        assertEquals( hasSpacing, config.spacingLabelEnabled );
        assertEquals( hasColor, config.colorButtonEnabled );
        assertEquals( hasColor, config.colorFieldEnabled );
        assertEquals( hasDraw, config.drawCheckBoxEnabled );

        if ( hasStroke )
            assertEquals( set1.getStroke(), config.stroke );
        if ( hasLength )
            assertEquals( set1.getLength(), config.length );
        if ( hasSpacing )
            assertEquals( set1.getSpacing(), config.spacing );
        if ( hasColor )
            assertEquals( set1.getColor(), config.color );
        if ( hasDraw )
            assertEquals( set1.getDraw(), config.draw );
}
    
    private static void 
    assertSetsSynched( LinePropertySet set1, LinePropertySet set2 )
    {
        boolean hasStroke = set1.hasStroke();
        boolean hasLength = set1.hasLength();
        boolean hasSpacing = set1.hasSpacing();
        boolean hasColor = set1.hasColor();
        boolean hasDraw = set1.hasDraw();
        
        assertEquals( hasStroke, set2.hasStroke() );
        assertEquals( hasLength, set2.hasLength() );
        assertEquals( hasSpacing, set2.hasSpacing() );
        assertEquals( hasColor, set2.hasColor() );
        assertEquals( hasDraw, set2.hasDraw() );
        
        if ( hasStroke )
            assertEquals( set1.getStroke(), set2.getStroke() );
        if ( hasLength )
            assertEquals( set1.getLength(), set2.getLength() );
        if ( hasSpacing )
            assertEquals( set1.getSpacing(), set2.getSpacing() );
        if ( hasColor )
            assertEquals( set1.getColor(), set2.getColor() );
        if ( hasDraw )
            assertEquals( set1.getDraw(), set2.getDraw() );
    }

    private void selectOther()
    {
        PRadioButton<LinePropertySet>   other   =
            getButton( b -> !b.isSelected() );
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
        Class<?>        clazz   = setIn.getClass();
        LinePropertySet setOut  = null;
        try
        {
            Constructor<?>  ctor    = clazz.getConstructor();
            Object          inst    = ctor.newInstance();
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
    private static class LPSMap  
        extends HashMap<Class<?>, LinePropertySet>
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
