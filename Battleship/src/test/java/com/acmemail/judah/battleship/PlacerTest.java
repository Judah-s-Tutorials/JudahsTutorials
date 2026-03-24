package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlacerTest
{
    private static final JOptionPaneI   mockedServer    =
        mock( JOptionPaneI.class );
    private static final JOptionPaneI   origServer      = 
        Placer.getJOptionPaneInterface();
    
    private Placer  placer;
    
    @BeforeAll
    public static void beforeAll()
    {
        ShipType.registerDefaultTypes();
    }
    
    @BeforeEach
    public void beforeEach()
    {
        Placer.setJOptionPaneInterface( mockedServer );
        placer = new Placer();
    }
    
    @AfterEach
    public void afterEach()
    {
        Placer.setJOptionPaneInterface( origServer );
    }

    @Test
    void testPlaceShipCancelGetType()
    {
        mockGetType( null );
        
        Ship    ship    = placer.placeShip();
        assertNull( ship );
    }

    @Test
    void testPlaceShipCancelGetOrientation()
    {
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( null );
        
        Ship    ship    = placer.placeShip();
        assertNull( ship );
    }

    @Test
    void testPlaceShipCancelGetCoords()
    {
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( Orientation.VERTICAL );
        mockGetCoordinates( null );
        
        Ship    ship    = placer.placeShip();
        assertNull( ship );
    }

    @Test
    void testPlaceShipExerciseGetCoordsThenCancel()
    {
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( Orientation.VERTICAL );
        mockGetCoordinates( null );
        
        Ship    ship    = placer.placeShip();
        assertNull( ship );
    }

    @Test
    public void testPlaceShipGetBattleship()
    {
//        doReturn( )
    }
    
    private void mockGetType( ShipType desiredReturn )
    {
        when ( mockedServer.
            showInputDialog( 
                ArgumentMatchers.isNull(), 
                any( Object.class ),
                anyString(),
                anyInt(),
                ArgumentMatchers.isNull(),
                any( ShipType[].class ),
                any( ShipType.class )
            )
        ).thenReturn( desiredReturn );
    }
    
    private void mockGetOrientation( Object desiredReturn )
    {

        when ( mockedServer.
            showInputDialog( 
                ArgumentMatchers.isNull(), 
                anyString(),
                anyString(),
                anyInt(),
                ArgumentMatchers.isNull(), 
                any( Orientation[].class ),
                any( Orientation.class )
            )
        ).thenReturn( desiredReturn );
    }
    
    private void mockGetCoordinates( String desiredReturn )
    {
        when( mockedServer.
            showInputDialog( null, "Enter Ship Coordinates" )
        ).thenReturn( desiredReturn );
    }
}
