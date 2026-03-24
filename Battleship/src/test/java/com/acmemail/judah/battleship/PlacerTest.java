package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    private static final int            numRows     = Grid.getNumRows();
    private static final int            numCols     = Grid.getNumCols();
    private static final String         lastRow     = 
        String.valueOf( (char)('A' + (numRows - 1)) );
    private static final String         lastCol     = 
        String.valueOf( numCols);
    
    private Placer  placer;
    
    @BeforeAll
    public static void beforeAll()
    {
        ShipType.registerDefaultTypes();
        System.out.println( lastRow + ", " + lastCol );
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
        when( mockedServer.
            showInputDialog( null, "Enter Ship Coordinates" )
        ).thenReturn( "1A" )
        .thenReturn( "AA" )
        .thenReturn( "11" )
        .thenReturn( null );
        
        Ship    ship    = placer.placeShip();
        assertNull( ship );
    }

    @Test
    void testPlaceShipExerciseGetCoordsThenOK()
    {
        ShipType    expType     = ShipType.getShipType( "Battleship" );
        Orientation expOrient   = Orientation.HORIZONTAL;
        
        // This column number should make the last cell of the ship
        // fall 1 cell past the horizontal bounds of the grid.
        int         invalidCol      = numRows - expType.getLength();
        String      strInvalidCol   = String.valueOf( invalidCol + 1 );
        String      strValidCol     = String.valueOf( invalidCol - 1 );
        String      invalidCoords   = "A" + strInvalidCol;
        String      validCoords     = "A" + strValidCol;
        GridCoords  expCoords       = new GridCoords( invalidCol - 1, 0 );
        Ship        expShip         = new Ship( expType, expCoords, expOrient );
        
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( expOrient );
        when( mockedServer.
            showInputDialog( null, "Enter Ship Coordinates" )
        ).thenReturn( invalidCoords )
        .thenReturn( validCoords );
        
        Ship    actShip = placer.placeShip();
        assertEquals( expShip, actShip );
    }

    @Test
    void testPlaceShipBoundsHorizontal()
    {
        ShipType    expType     = ShipType.getShipType( "Battleship" );
        Orientation expOrient   = Orientation.HORIZONTAL;
        char        expRow      = 'A';
        char        expCol      = '1';
        GridCoords  expCoords   = new GridCoords( 0, 0 );
        Ship        expShip     = new Ship( expType, expCoords, expOrient );
        
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( expOrient );
        when( mockedServer.
            showInputDialog( null, "Enter Ship Coordinates" )
        ).thenReturn( "1A" )
        .thenReturn( "AA" )
        .thenReturn( "11" )
        .thenReturn( "A1" );
        
        Ship    actShip = placer.placeShip();
        assertEquals( expShip, actShip );
    }

    @Test
    void testPlaceShipExerciseOutOfBoundCoordsThenCancel()
    {
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( Orientation.VERTICAL );
        when( mockedServer.
            showInputDialog( null, "Enter Ship Coordinates" )
        ).thenReturn( "1A" )
        .thenReturn( "AA" )
        .thenReturn( "11" )
        .thenReturn( null );
        
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
