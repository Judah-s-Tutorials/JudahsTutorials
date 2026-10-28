package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.FAILURE;
import static com.acmemail.judah.battleship.StatusMessages.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.battleship.BasicProvisioner;
import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship.Provisioner;
import com.acmemail.judah.battleship.artwork.text.TextGridConstants;
import com.acmemail.judah.battleship.artwork.text.TextGridParent;
import com.acmemail.judah.battleship.model.Cell2DView;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.GridCoords;
import com.acmemail.judah.battleship.model.Orientation;
import com.acmemail.judah.battleship.model.Ship2D;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.model.default_ship_types.Battleship;
import com.acmemail.judah.battleship.model.default_ship_types.Carrier;

class GamePhaseTest
{
    private static final String             AUTO            = "auto";
    private static final String             UNDO            = "undo";
    private static final String             REDO            = "redo";
    private static final String             MISS            = "miss";
    private static final String             HIT             = "hit";
    private static final String             STRIKE          = "strike";
    private static final String             QUIT            = "quit";
    private static final String             QUITB           = "quit!";
    private static final int                NUM_COLS        = 11;
    private static final int                NUM_ROWS        = 13;
    private static final String             OPP_ONE         = "Opponent-1";
    private static final String             OPP_TWO         = "Opponent-2";
    private static final List<ShipType2D>   toDeploy        =
        List.of(
            Battleship.getType(),
            Carrier.getType()
        );
    
    private Grid2D              homeGrid        = Grid2D.getHomeGrid();
    private Grid2D              oppOneGrid;
    private Grid2D              oppTwoGrid;
    private List<Ship2D>        oppOneShips;
    private List<Ship2D>        oppTwoShips;
    private GamePhase           gamePhase;
    
    @BeforeEach
    public void beforeEach()
    {
        Grid2D.reset( NUM_ROWS, NUM_COLS );
        Configurator.reset();
        Configurator.nextState();
        assertTrue( Configurator.isConfig() );

        List<Ship2D>    homeShips   = deployVertical( 1, 1 );
        homeGrid = Grid2D.getHomeGrid();
        homeShips.forEach( homeGrid::put );

        oppOneGrid = new Grid2D( OPP_ONE );
        oppTwoGrid = new Grid2D( OPP_TWO );
        oppOneShips = deployHorizontal( 1, 1 );
        oppTwoShips = deployVertical( 2, 2 );
        
        Configurator.nextState();
        assertTrue( Configurator.isConfigComplete() );
    }
    
    @Test
    public void testSimpleMiss()
    {
        Label           label       = new Label( "A1" );
        String          miss        = miss( OPP_ONE, label );
        List<String>    script      = List.of( miss, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        assertIsMiss( OPP_ONE, label );
    }
    
    @Test
    public void testSimpleHit()
    {
        Label           label       = new Label( "A1" );
        String          hit         = "hit," + OPP_ONE + "," + label.getLabel();
        List<String>    script      = List.of( hit, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        assertIsHit( OPP_ONE, label );
    }
    
    @Test
    public void testSimpleStrike()
    {
        Label           labelOccupied   = getOccupiedCell();
        Label           labelUnoccupied = getUnoccupiedCell();
        String          strikeO         = strike( labelOccupied );
        String          strikeU         = strike( labelUnoccupied );
        List<String>    script          = List.of( strikeO, strikeU, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        assertIsStrike( labelOccupied );
        assertIsStrike( labelUnoccupied );
    }
    
    @Test
    public void testMissHit()
    {
        Label           label       = new Label( "A1" );
        String          miss        = miss( OPP_ONE, label );
        String          hit         = hit(  OPP_ONE, label );
        List<String>    script      = List.of( miss, hit, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        assertIsHit( OPP_ONE, label );
    }
    
    @Test
    public void testHitMiss()
    {
        Label           label       = new Label( "A1" );
        String          miss        = miss( OPP_ONE, label );
        String          hit         = hit(  OPP_ONE, label );
        List<String>    script      = List.of( hit, miss, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        assertIsMiss( OPP_ONE, label );
    }
    
    @Test
    public void testMissHitUndo()
    {
        Label           label       = new Label( "A1" );
        String          miss        = miss( OPP_ONE, label );
        String          hit         = hit(  OPP_ONE, label );
        List<String>    script      = List.of( miss, hit, UNDO, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        assertIsMiss( OPP_ONE, label );
    }
    
    @Test
    public void testHitMissUndo()
    {
        Label           label       = new Label( "A1" );
        String          miss        = miss( OPP_ONE, label );
        String          hit         = hit(  OPP_ONE, label );
        List<String>    script      = List.of( hit, miss, UNDO, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        assertIsHit( OPP_ONE, label );
    }
    
    @Test
    public void testUndoHit()
    {
        Label           label       = new Label( "A1" );
        String          hit         = "hit," + OPP_ONE + "," + label.getLabel();
        List<String>    script      = List.of( hit, UNDO, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        assertNotAttacked( OPP_ONE, label );
    }
    
    @Test
    public void testUndoMiss()
    {
        Label           label       = new Label( "A1" );
        String          miss        = miss( OPP_ONE, label);
        List<String>    script      = List.of( miss, UNDO, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        assertNotAttacked( OPP_ONE, label );
    }
    
    public void testUndoStrike()
    {
        Label           label       = new Label( "A1" );
        String          strike      = strike( label );
        List<String>    script      = List.of( strike, UNDO, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        assertNotIsStrike(  label );
    }
    
    @Test
    public void testUndoMiss2x1()
    {
        // Miss twice, undo once, final status should be miss
        Label           label       = new Label( "A1" );
        String          miss        = miss( OPP_ONE, label );
        testUndo2x1( miss, () -> assertIsMiss( OPP_ONE, label ) );
    }
    
    @Test
    public void testUndoMiss2x2()
    {
        // Miss twice, undo twice, final status should be not attacked
        Label           label       = new Label( "A1" );
        String          miss        = miss( OPP_ONE, label );
        testUndo2x2( miss, () -> assertNotAttacked( OPP_ONE, label ) );
    }
    
    @Test
    public void testUndoHit2x1()
    {
        // Hit twice, undo once, final status should be Hit
        Label           label       = new Label( "A1" );
        String          hit         = hit( OPP_ONE, label );
        testUndo2x1( hit, () -> assertIsHit( OPP_ONE, label ) );
    }
    
    @Test
    public void testUndoHit2x2()
    {
        // Hit twice, undo twice, final status should be not attacked
        Label           label       = new Label( "A1" );
        String          hit         = hit( OPP_ONE, label );
        testUndo2x2( hit, () -> assertNotAttacked( OPP_ONE, label ) );
    }
    
    @Test
    public void testUndoStrike2x1()
    {
        // Strike twice, undo once, final status should be is-strike
        Label           label       = new Label( "A1" );
        String          strike      = strike( label );
        testUndo2x1( strike, () -> assertIsStrike( label ) );
    }
    
    @Test
    public void testUndoStrike2x2()
    {
        // Strike twice, undo twice, final status should be is-not-strike
        Label           label       = new Label( "A1" );
        String          strike      = strike( label );
        testUndo2x2( strike, () -> assertNotIsStrike( label ) );
    }
    
    @Test
    public void testAuto()
    {
        testAuto( auto( "off" ) );
    }
    
    @Test
    public void testAutoUndo()
    {
        testAuto( UNDO );
    }
    
    /**
     * Test toggling the auto command.
     * First: explicitly make sure auto is off.
     * Second: Turn auto on using "auto,on"
     * and verify that auto is on.
     * Third: turn auto off, using the user supplied command;
     * this should be either "auto,off" or "undo".
     * Verify that auto is off.
     * 
     * @param turnOff   the command to turn auto off
     */
    private void testAuto( String turnOff )
    {
        Label           label       = new Label( "a1" );
        String          autoOn      = auto( "on" );
        String          autoOff     = auto( "off" );
        String          miss        = miss( OPP_ONE, label );
        List<String>    script      =
            List.of(
                autoOff, // bucket[0]; expect no-update to follow
                autoOn,  // bucket[1]; expect update to follow
                miss,    // bucket[2]; expect update to follow
                turnOff, // bucket[3]; expect no-update to follow
                miss,    // bucket[4]; expect no-update to follow
                QUITB    // bucket[5]; update not tested
            );
        RoundTripResult     result      = execRoundTrip( script );
        List<String>        rawFeedback = result.feedback();
        RoundTripParser     parser      = new RoundTripParser( rawFeedback );
        List<CommandBucket> buckets     = parser.buckets;
        assertFalse( TextTestUtils.contains( rawFeedback, FAILURE ) );
        assertTrue( TextTestUtils.contains( rawFeedback, SUCCESS ) );
        assertTrue( result.phase().isQuit() );
        assertEquals( script.size(), buckets.size() );
        
        assertFalse( hasUpdate( buckets.get( 0 ) ) );
        assertTrue( hasUpdate( buckets.get( 1 ) ) );
        assertTrue( hasUpdate( buckets.get( 2 ) ) );
        assertFalse( hasUpdate( buckets.get( 3 ) ) );
        assertFalse( hasUpdate( buckets.get( 4 ) ) );
    }

    /**
     * Execute a command twice, and undo once, followed by quit.
     * Validate the result.
     * 
     * @param command   command to execute
     * @param validator logic to validate result
     */
    private void testUndo2x1( String command, Runnable validator )
    {
        // Execute twice, undo once, validate final status
        List<String>    script      = List.of( command, command, UNDO, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        validator.run();
    }

    /**
     * Execute a command twice, undo twice, and quit.
     * Validate the result.
     * 
     * @param command   command to execute
     * @param validator logic to validate result
     */
    private void testUndo2x2( String command, Runnable validator )
    {
        // Execute twice, undo once, validate final status
        List<String>    script      = 
            List.of( command, command, UNDO, UNDO, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        validator.run();
    }

    /**
     * Generate a hit command from a given name and a label.
     * 
     * @param name  the given name
     * @param label the given label
     * 
     * @return a hit command incorporating name and label
     */
    private static String hit( String name, Label label )
    {
        String  hit = HIT + "," + name + "," + label.getLabel();
        return hit;
    }
    
    /**
     * Generate a miss command from a given name and a label.
     * 
     * @param name  the given name
     * @param label the given label
     * 
     * @return a miss command incorporating name and label
     */
    private static String miss( String name, Label label )
    {
        String  hit = MISS + "," + name + "," + label.getLabel();
        return hit;
    }
    
    /**
     * Generate a strike command from the given label.
     * 
     * @param label the given label
     * 
     * @return a strike command incorporating name and label
     */
    private static String strike( Label label )
    {
        String  strike  = STRIKE + "," + label.getLabel();
        return strike;
    }
    
    /**
     * Generate an auto-update command using the given label.
     * 
     * @param arg   the given argument
     * 
     * @return an auto-update command incorporating arg
     */
    private static String auto( String arg )
    {
        String  auto    = AUTO + "," + arg;
        return auto;
    }
    
    private static void assertIsMiss( String name, Label label )
    {
        Grid2D  grid    = Grid2D.getGrid( name );
        assertIsMiss( grid, label );
    }
    
    private static void assertIsMiss( Grid2D grid, Label label )
    {
        assertNotNull( grid );
        GridCoords      coords      = label.getGridCoords();
        Cell2DView      cell        = grid.getCellView( coords );
        assertTrue( cell.isSplatted() );
        assertFalse( cell.isOpponent() );
    }
    
    private static void assertIsHit( String name, Label label )
    {
        Grid2D  grid    = Grid2D.getGrid( name );
        assertIsHit( grid, label );
    }
    
    private static void assertIsHit( Grid2D grid, Label label )
    {
        assertNotNull( grid );
        GridCoords      coords      = label.getGridCoords();
        Cell2DView      cell        = grid.getCellView( coords );
        assertTrue( cell.isSplatted() );
        assertTrue( cell.isOpponent() );
    }
    
    private static void assertIsStrike( Label label )
    {
        Grid2D  grid                = Grid2D.getHomeGrid();
        GridCoords      coords      = label.getGridCoords();
        Cell2DView      cell        = grid.getCellView( coords );
        assertTrue( cell.isSplatted() );
        assertEquals( cell.getShip(), getShip( label ) );
    }
    
    private static void assertNotIsStrike( Label label )
    {
        Grid2D  grid                = Grid2D.getHomeGrid();
        GridCoords      coords      = label.getGridCoords();
        Cell2DView      cell        = grid.getCellView( coords );
        assertFalse( cell.isSplatted() );
        assertEquals( cell.getShip(), getShip( label ) );
    }
    
    private static void assertNotAttacked( String name, Label label )
    {
        Grid2D  grid    = Grid2D.getGrid( name );
        assertNotAttacked( grid, label );
    }
    
    private static void assertNotAttacked( Grid2D grid, Label label )
    {
        assertNotNull( grid );
        GridCoords      coords      = label.getGridCoords();
        Cell2DView      cell        = grid.getCellView( coords );
        assertFalse( cell.isSplatted() );
        assertFalse( cell.isOpponent() );
    }
    
    /**
     * Return true if the given bucket
     * appears to contain an update block.
     * This is the block of text
     * that encapsulates the text-based display
     * of the game's physical grid.
     * An exhaustive analysis is not performed;
     * we're just looking for a few strings
     * that are expected to be contained 
     * in the update block
     * and not elsewhere.
     * 
     * @param bucket    the given bucket
     * 
     * @return  true if bucket appears to contain an update block
     */
    private static boolean hasUpdate( CommandBucket bucket )
    {
        String          vLineStr    = String.valueOf( TextGridConstants.VLINE );
        List<String>    suffix      = bucket.suffix();
        boolean         result      =
            TextTestUtils.contains( suffix, OPP_ONE ) &&
            TextTestUtils.contains( suffix, OPP_TWO ) &&
            TextTestUtils.contains( suffix, vLineStr );
        return result;
    }
    
    private static Label getUnoccupiedCell()
    {
        Label       label   = getCell( c -> c.getShip() == null );
        return label;
    }
    
    private static Label getOccupiedCell()
    {
        Label       label   = getCell( c -> c.getShip() != null );
        return label;
    }
    
    private static Label getCell( Predicate<Cell2DView> pred )
    {
        Grid2D      home    = Grid2D.getHomeGrid();
        GridCoords  coords  = null;
        for ( int row = 0 ; coords == null && row < NUM_ROWS ; ++row )
            for ( int col = 0 ; col < NUM_COLS ; ++col )
            {
                coords = new GridCoords( col, row );
                Cell2DView  cell    = home.getCellView( coords );
                if ( pred.test( cell ) )
                    break;
                else
                    coords = null;
            }
        assertNotNull( coords );
        Label       label   = new Label( coords );
        return label;
    }
    
    private static Ship2D getShip( Label label )
    {
        Grid2D          grid        = Grid2D.getHomeGrid();
        GridCoords      coords      = label.getGridCoords();
        Ship2D          ship        =
            grid.getAllShips()
                .stream()
                .filter( s -> s.contains( coords ) )
                .findFirst().orElse( null );
        return ship;
    }
    
    private List<Ship2D> deployVertical( int row, int startCol )
    {
        Orientation orient  = Orientation.VERTICAL;
        List<Ship2D>    ships   = new ArrayList<>();
        IntStream.range( 0, toDeploy.size() ).forEach( i -> {
                ShipType2D  type    = toDeploy.get( i );
                int         xco     = startCol + 2 * i + 1;
                GridCoords  coords  = new GridCoords( xco, row );
                Ship2D      ship    = new Ship2D( type, coords, orient );
                ships.add( ship );
            });
        return ships;
    }
    
    private List<Ship2D> deployHorizontal( int col, int startRow )
    {
        Orientation     orient  = Orientation.HORIZONTAL;
        List<Ship2D>    ships   = new ArrayList<>();
        IntStream.range( 0, toDeploy.size() ).forEach( i -> {
                ShipType2D  type    = toDeploy.get( i );
                int         yco     = startRow + 2 * i + 1;
                GridCoords  coords  = new GridCoords( col, yco );
                Ship2D      ship    = new Ship2D( type, coords, orient );
                ships.add( ship );
            });
        return ships;
    }
    
    private void visualize()
    {
      TextGridParent  parent  = new TextGridParent();
      parent.exec().forEach( System.out::println );
    }
    
    private static RoundTripResult execRoundTrip( 
        List<String> inputData
    )
    {
        assertTrue( Configurator.isConfigComplete() );
        Provisioner     prov        = new BasicProvisioner();
        PhaseGetter     getter      = 
            (r,p) -> new GamePhase( r, p );
            
        RoundTripResult result      = 
            TextTestUtils.execRoundTrip( prov, getter, inputData );
        return result;
    }
}
