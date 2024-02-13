package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JDialog;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.gp_panel.GPPTestDataInitializer;
import com.acmemail.judah.cartesian_plane.test_utils.gp_panel.GPPTestDialog;

class GraphPropertiesPanelFuncTest
{
    private static final GPPTestDataInitializer initData    =
        new GPPTestDataInitializer();
    private static final GPSMap     setMapOrig  = new GPSMap();
    private static final GPSMap     setMapNew   = new GPSMap();
    
    private GPPTestDialog           dialog;
    private JDialog                 testDialog;
    private List<PRadioButton<GraphPropertySet>>    radioButtons;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception
    {
        Stream.of(
            GraphPropertySetMW.class,
            GraphPropertySetTM.class,
            GraphPropertySetRM.class,
            GraphPropertySetBM.class,
            GraphPropertySetLM.class
        )
            .map( initData::getOrigValues )
            .peek( setMapOrig::put )
            .map( s -> s.getClass() )
            .map( initData::getNewValues )
            .forEach( setMapNew::put );
    }
    
    @BeforeEach
    public void beforeEach()
    {
        setMapOrig.values().forEach( s -> s.apply() );
        if ( dialog != null )
            dialog.dispose();
        if ( testDialog != null )
        {
            testDialog.dispose();
            testDialog = null;
        }
        GUIUtils.schedEDTAndWait( () -> {
            dialog = GPPTestDialog.getDialog();
            dialog.setVisible( true );
        });
        radioButtons = dialog.getRBList();
    }
    
    @Test
    public void testInit()
    {
        // Validate the initial selection.
        PRadioButton<GraphPropertySet>  button  = getSelectedButton();
        GraphPropertySet                set     = button.get();
        validateComponents( set );
    }
    
    @Test
    public void testSelect()
    {
        // Click on each button, starting with one that is NOT
        // initially selected. Validate component settings.
        int     len         = radioButtons.size();
        int     startIndex  = getSelectedButtonIndex() + 1;
        IntStream.range( 0, len )
            .map( i -> (i + startIndex) % len )
            .mapToObj( radioButtons::get )
            .peek( dialog::doClick)
            .map( rb -> rb.get() )
            .forEach( this::validateComponents );
    }
    
    @Test
    public void testResetInit()
    {
        // Change values for the currently selected button;
        // do NOT select another button:
        //      Press reset;
        //      Verify that components return to original state;
        //      Verify that the property sets encapsulated in all
        //          the radio buttons have the original values.
        PRadioButton<GraphPropertySet>  button  = getSelectedButton();
        GraphPropertySet    origValues  = button.get();
        GraphPropertySet    newValues   = setMapNew.get( origValues );
        dialog.setProperties( newValues );
        validateComponents( newValues );
        dialog.selectReset();
        validateComponents( origValues );
        
        radioButtons.stream()
            .map( b -> b.get() )
            .forEach( s -> assertSetEquals( setMapOrig.get( s ), s ) );
    }
    
    @Test
    public void testResetAll()
    {
        // Change all the properties for all the buttons
        radioButtons.stream()
            .peek( dialog::doClick )
            .map( b -> b.get() )
            .map( setMapNew::get )
            .peek( dialog::setProperties )
            .forEach( this::validateComponents );
        // Verify that the properties associated with each button
        // have been updated with the new values.
        radioButtons.stream()
            .peek( dialog::doClick )
            .map( b -> b.get() )
            .forEach( s -> assertSetEquals( setMapNew.get( s ), s ) );
        // Select reset and verify that all the buttons' sets
        // return to their original values.
        dialog.selectReset();
        radioButtons.stream()
            .map( b -> b.get() )
            .forEach( s -> assertSetEquals( setMapOrig.get( s ), s ) );
    }
    
    @Test
    public void testApplyInit()
    {
        // Change the values of the components for the currently
        // selected button. Press apply.
        // ... verify that property set for selected button is updated
        // ... verify the the PropertyManager has been updated
        PRadioButton<GraphPropertySet>  button      = getSelectedButton();
        GraphPropertySet        startSet    = button.get();
        GraphPropertySet        newValues   = setMapNew.get( startSet );
        dialog.setProperties( newValues );
        dialog.selectApply();
        assertSetEquals( newValues, startSet );
        validateComponents( newValues );
        
        // New instance will be initialized from PropertyManager.
        GraphPropertySet    pMgrValues      = newInstance( startSet );
        assertSetEquals( newValues, pMgrValues );
        
        // Verify that nothing changed for non-selected buttons,
        // or in the property manager.
        radioButtons.stream()
            .filter( b -> !b.isSelected() )
            .map( b -> b.get() )
            .peek( s -> assertSetEquals( setMapOrig.get( s ), s ) )
            .map( this::newInstance )
            .peek( s -> assertSetEquals( setMapOrig.get( s ), s ) );       
    }
    
    @Test
    public void testApplyAll()
    {
        // Change all the properties for all the buttons.
        // Select apply. Verify that all properties are updated
        // in the property manager.
        radioButtons.stream()
            .peek( dialog::doClick )
            .map( b -> b.get() )
            .map( setMapNew::get )
            .forEach( dialog::setProperties );
        
        dialog.selectApply();
        radioButtons.stream()
            .map( b -> b.get() )
            .map( s -> new GraphPropertySet[] { s, newInstance( s ) } )
            .forEach( arr -> assertSetEquals( arr[0], arr[1] ) );
    }
    
    @Test
    public void testClose()
    {
        // Change all property values for all buttons. Close and 
        // reopen the dialog. Verify that all new values are intact.
        radioButtons.stream()
            .peek( dialog::doClick )
            .map( b -> b.get() )
            .map( setMapNew::get )
            .forEach( dialog::setProperties );
        PRadioButton<GraphPropertySet>  selectedButton  = 
            getSelectedButton();
        dialog.selectApply();
        dialog.selectClose();
        
        assertFalse( dialog.isDialogVisible() );
        dialog.setDialogVisible( true );
        assertEquals( selectedButton, getSelectedButton() );
        
        radioButtons.stream()
            .map( b -> b.get() )
            .peek( s -> System.out.println( s.getClass().getName() ) )
            .map( s -> new GraphPropertySet[] { s, newInstance( s ) } )
            .forEach( arr -> assertSetEquals( arr[0], arr[1] ) );
    }
    
    @Test
    public void testGetDialog()
    {
        if ( dialog != null )
            dialog.dispose();
        GUIUtils.schedEDTAndWait( () -> {
            testDialog = GraphPropertiesPanel.getDialog( null );
            assertNotNull( testDialog );
            assertFalse( testDialog.isVisible() );
        });
    }
    
    private void validateComponents( GraphPropertySet set )
    {
        GraphPropertySet    compValues  = dialog.getProperties();
        assertSetEquals( set, compValues );
    }
    
    private void 
    assertSetEquals( GraphPropertySet exp, GraphPropertySet act )
    {
        assertEquals( exp.getFontName(), act.getFontName() );
        assertEquals( exp.isBold(), act.isBold() );
        assertEquals( exp.isItalic(), act.isItalic() );
        assertEquals( exp.getFontSize(), act.getFontSize() );
        assertEquals( exp.getFGColor(), act.getFGColor() );
        assertEquals( exp.isFontDraw(), act.isFontDraw() );
        assertEquals( exp.getWidth(), act.getWidth() );
        assertEquals( exp.getBGColor(), act.getBGColor() );
    }
    
    private PRadioButton<GraphPropertySet> getSelectedButton()
    {
        int                             index   = 
            getSelectedButtonIndex();
        PRadioButton<GraphPropertySet>  button  = 
            radioButtons.get( index );
        assertTrue( button.isSelected() );
        return button;
    }
    
    private int getSelectedButtonIndex()
    {
        int size    = radioButtons.size();
        int index   =
            IntStream.range( 0, size )
                .filter( i -> radioButtons.get( i ).isSelected() )
                .findFirst().orElse( -1 );
        assertTrue( index >= 0 );
        return index;
    }

    private GraphPropertySet newInstance( GraphPropertySet setIn )
    {
        Class<?>            clazz   = setIn.getClass();
        GraphPropertySet    setOut  = null;
        try
        {
            Constructor<?>  ctor    = clazz.getConstructor();
            Object          inst    = ctor.newInstance();
            assertTrue( inst instanceof GraphPropertySet );
            setOut = (GraphPropertySet)inst;
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
    private static class GPSMap  
        extends 
        HashMap<Class<? extends GraphPropertySet>, GraphPropertySet>
    {
        public void put( GraphPropertySet set )
        {
            put( set.getClass(), set );
        }

        public GraphPropertySet get( GraphPropertySet setIn )
        {
            GraphPropertySet    setOut  = get( setIn.getClass() );
            assertNotNull( setOut );
            return setOut;
        }
    }
}
