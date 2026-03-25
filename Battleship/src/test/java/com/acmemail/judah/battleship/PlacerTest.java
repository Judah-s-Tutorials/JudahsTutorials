package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void testPlaceShipCancelGetType()
    {
        mockGetType( null );
        
        Ship    ship    = placer.placeShip();
        assertNull( ship );
    }

    @Test
    public void testPlaceShipCancelGetOrientation()
    {
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( null );
        
        Ship    ship    = placer.placeShip();
        assertNull( ship );
    }

    @Test
    public void testPlaceShipCancelGetCoords()
    {
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( Orientation.VERTICAL );
        mockGetCoordinates( null );
        
        Ship    ship    = placer.placeShip();
        assertNull( ship );
    }

    @Test
    public void testPlaceShipExerciseGetCoordsThenCancel()
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
    public void testPlaceShipExerciseGetCoordsThenOK()
    {
        ShipType    expType     = ShipType.getShipType( "Battleship" );
        Orientation expOrient   = Orientation.HORIZONTAL;
        GridCoords  expCoords   = new GridCoords( 0, 0 );
        Ship        expShip     = new Ship( expType, expCoords, expOrient );
        
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( expOrient );
        when( mockedServer.
                showInputDialog( null, "Enter Ship Coordinates" )
            ).thenReturn( "AA" )
            .thenReturn( "11" )
            .thenReturn( "1A" )
            .thenReturn( "A1" );
        
        Ship    actShip = placer.placeShip();
        assertEquals( expShip, actShip );
    }

    @Test
    public void testPlaceShipExerciseSplitCoords()
    {
        String[]    coords  =
        { "A,1", " A , 1 ", "  A  ,,,  1  ", ", , , A , , , 1", " A  1  " };
        for ( String str : coords )
            testSplitCoords( str, 0, 0 );
    }

    @Test
    public void testPlaceShipBoundsHorizontal()
    {
        ShipType    expType     = ShipType.getShipType( "Battleship" );
        Orientation expOrient   = Orientation.HORIZONTAL;
        int         len         = expType.getLength();
        
        // this x-coordinate should place the actual ship
        // one cell past the end of its row
        int         invXco      = numCols - len + 1;
        // this x-coordinate should place the actual ship 
        // flush with the end of its row.
        int         valXco      = invXco - 1;
        // this is the 1-based column coordinate corresponding
        // to invXco
        String      invCol      = String.valueOf( invXco + 1 );
        // this is the 1-based column coordinate corresponding
        // to valXco
        String      valCol      = String.valueOf( valXco + 1 );
        GridCoords  expCoords   = new GridCoords( valXco, 0 );
        Ship        expShip     = new Ship( expType, expCoords, expOrient );
        
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( expOrient );
        when( mockedServer.
            showInputDialog( null, "Enter Ship Coordinates" )
        ).thenReturn( "A" + invCol )
        .thenReturn( "A" + valCol );
        
        Ship    actShip = placer.placeShip();
        assertEquals( expShip, actShip );
    }

    @Test
    public void testPlaceShipBoundsVertical()
    {
        ShipType    expType     = ShipType.getShipType( "Battleship" );
        Orientation expOrient   = Orientation.VERTICAL;
        int         len         = expType.getLength();
        
        // this y-coordinate should place the actual ship
        // one cell past the bottom of its column
        int         invYco      = numRows - len + 1;
        // this y-coordinate should place the actual ship 
        // flush with the bottom of its column.
        int         valYco      = invYco - 1;
        // this is the 1-based row coordinate corresponding
        // to invYco
        String      invRow      = Label.decimalToAlpha( invYco );
        // this is the 1-based row coordinate corresponding
        // to valYco
        String      valRow      = Label.decimalToAlpha( valYco );
        GridCoords  expCoords   = new GridCoords( 0, valYco );
        Ship        expShip     = new Ship( expType, expCoords, expOrient );
        
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( expOrient );
        when( mockedServer.
            showInputDialog( null, "Enter Ship Coordinates" )
        ).thenReturn( invRow + "1" )
        .thenReturn( valRow + "1" );
        
        Ship    actShip = placer.placeShip();
        assertEquals( expShip, actShip );
    }

    @Test
    public void testPlaceShipColErrors()
    {
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( Orientation.VERTICAL );
        when( mockedServer.
            showInputDialog( null, "Enter Ship Coordinates" )
        ).thenReturn( "A,A" )
        .thenReturn( "A,%" )
        .thenReturn( "A,100" )
        .thenReturn( "A," )
        .thenReturn( null );
        
        Ship    ship    = placer.placeShip();
        assertNull( ship );
    }
    
    @Test
    public void testPlaceShipBadOrientation()
    {
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( "not a ShipType" );
        Class<BattleshipException>  excClass    = BattleshipException.class;
        assertThrows( excClass, () -> placer.placeShip() );
    }
    
    @Test
    public void testShipIntersection()
    {
        String      shipType1   = "Battleship";
        ShipType    expType1    = ShipType.getShipType( shipType1 );
        Orientation expOrient1  = Orientation.HORIZONTAL;
        GridCoords  expCoords1  = new GridCoords( 0, 0 );
        Ship        expShip1    = 
            new Ship( expType1, expCoords1, expOrient1 );

        mockGetType( expType1 );
        mockGetOrientation( expOrient1 );
        when( mockedServer.
            showInputDialog( null, "Enter Ship Coordinates" )
        ).thenReturn( "A1" );
        
        Ship    actShip1        = placer.placeShip();
        assertEquals( expShip1, actShip1 );
        
        String      shipType2   = "Destroyer";
        ShipType    expType2    = ShipType.getShipType( shipType2 );
        Orientation expOrient2  = Orientation.HORIZONTAL;
        GridCoords  expCoords2  = new GridCoords( 1, 1 );
        Ship        expShip2    = 
            new Ship( expType2, expCoords2, expOrient2 );

        mockGetType( expType2 );
        mockGetOrientation( expOrient2 );
        when( mockedServer.
            showInputDialog( null, "Enter Ship Coordinates" )
        ).thenReturn( "A2" ) // ship2 intersects ship1
        .thenReturn( "B2" ); // ship2 doesn't intersect ship1
        
        Ship    actShip2        = placer.placeShip();
        assertEquals( expShip2, actShip2 );
}
    
    private void testSplitCoords( String coords, int xco, int yco )
    {
        ShipType    expType     = ShipType.getShipType( "Battleship" );
        Orientation expOrient   = Orientation.VERTICAL;
        GridCoords  expCoords   = new GridCoords( xco, yco );
        Ship        expShip     = new Ship( expType, expCoords, expOrient );
        
        mockGetType( ShipType.getShipType( "Battleship" ) );
        mockGetOrientation( expOrient );
        when( mockedServer.
            showInputDialog( null, "Enter Ship Coordinates" )
        ).thenReturn( coords );
        
        Ship    actShip = placer.placeShip();
        assertEquals( expShip, actShip );
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
