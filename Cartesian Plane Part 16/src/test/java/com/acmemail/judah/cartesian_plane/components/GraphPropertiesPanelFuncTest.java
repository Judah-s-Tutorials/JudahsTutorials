package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.test_utils.gp_plane.GPPTestDataInitializer;
import com.acmemail.judah.cartesian_plane.test_utils.gp_plane.GPPTestDialog;

class GraphPropertiesPanelFuncTest
{
    private static final GPPTestDataInitializer initData    =
        new GPPTestDataInitializer();
    private static final GPSMap     setMapOrig  = new GPSMap();
    private static final GPSMap     setMapNew   = new GPSMap();
    private GPPTestDialog           dialog;
    
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
//        GUIUtils.schedEDTAndWait( () -> {
            dialog = GPPTestDialog.getDialog();
            dialog.setVisible( true );
//        });
    }

    @Test
    void test()
    {
        new Scanner( System.in ).nextLine();
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
