package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.FAILURE;
import static com.acmemail.judah.battleship.StatusMessages.GRID_NOT_FOUND;
import static com.acmemail.judah.battleship.StatusMessages.HELP_END;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ARG;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ARG_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_COMMAND;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_COORDINATES;
import static com.acmemail.judah.battleship.StatusMessages.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
    private static final String             UNSTRIKE        = "unstrike";
    private static final String             UNATTACK        = "unattack";
    private static final String             SHOW            = "show";
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
        resetConfig( 2 );
    }
    
    private void resetConfig( int numOpponents )
    {
        Grid2D.reset( NUM_ROWS, NUM_COLS );
        Configurator.reset();
        Configurator.nextState();
        assertTrue( Configurator.isConfig() );

        List<Ship2D>    homeShips   = deployVertical( 1, 1 );
        homeGrid = Grid2D.getHomeGrid();
        homeShips.forEach( homeGrid::put );

        oppOneGrid = new Grid2D( OPP_ONE );
        oppOneShips = deployHorizontal( 1, 1 );

        if ( numOpponents > 1 )
        {
            oppTwoGrid = new Grid2D( OPP_TWO );
            oppTwoShips = deployVertical( 2, 2 );
        }
        
        Configurator.nextState();
        assertTrue( Configurator.isConfigComplete() );
    }
    
    @Test
    public void testGamePhase()
    {
        new GamePhase();
    }
    
    @Test
    public void testGamePhaseReaderPrintStream()
    {        
        StringReader    strReader   = new StringReader( "abcd" );
        new GamePhase( strReader, System.out );
        
        // Exercise unusual path where caller passes a BufferedRead explicitly
        BufferedReader  bufReader   = new BufferedReader( strReader );
        new GamePhase( bufReader, System.out );
    }
    
    @Test
    public void testSimpleMiss()
    {
        Label           label       = new Label( "A1" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        Consumer<RoundTripResult>   validator   = 
            r -> assertIsMiss( OPP_ONE, label );
        testSimpleCommand( List.of( miss ), validator );
    }
    
    @Test
    public void testSimpleHit()
    {
        Label           label       = new Label( "A1" );
        String          hit         = buildCommand( HIT, OPP_ONE, label );
        Consumer<RoundTripResult>   validator   = 
            r -> assertIsHit( OPP_ONE, label );
        testSimpleCommand( List.of( hit ), validator );
    }
    
    @Test
    public void testSimpleStrike()
    {
        // Strike both an occupied cell and an unoccupied cell
        Label           unoccupied  = getUnoccupiedCell();
        Label           occupied    = getOccupiedCell();
        String          strikeU     = buildCommand( STRIKE, unoccupied );
        String          strikeO     = buildCommand( STRIKE, occupied );
        List<String>    commands    = List.of( strikeU, strikeO );
        testSimpleCommand( commands, r -> {
            assertIsStrike( unoccupied );
            assertIsStrike( occupied );
        });
    }
    
    @Test
    public void testSimpleUnstrike()
    {
        Label           occupied    = getOccupiedCell();
        Label           unoccupied  = getUnoccupiedCell();
        String          strikeO     = buildCommand( STRIKE, occupied );
        String          strikeU     = buildCommand( STRIKE, unoccupied );
        String          unstrikeO   = buildCommand( UNSTRIKE, occupied );
        String          unstrikeU   = buildCommand( UNSTRIKE, unoccupied );
        List<String>    commands    = 
            List.of( 
                strikeO, 
                strikeU, 
                unstrikeO, 
                unstrikeU
            );
        testSimpleCommand( commands, r -> {
            assertNotStrike( unoccupied );
            assertNotStrike( occupied );
        });
    }
    
    @Test
    public void testSimpleUnattack()
    {
        Label           labelMiss   = new Label( "A1" );
        Label           labelHit    = new Label( "B2" );
        String          miss        = buildCommand( MISS, OPP_ONE, labelMiss );
        String          hit         = buildCommand( HIT, OPP_ONE, labelHit );
        String          unmiss      = buildCommand( UNATTACK, OPP_ONE, labelMiss );
        String          unhit       = buildCommand( UNATTACK, OPP_ONE, labelHit );
        List<String>    script      = 
            List.of( 
                miss, 
                hit,
                unmiss,
                unhit
            );
        testSimpleCommand( script, r -> {
            assertNotAttacked( OPP_ONE, labelMiss );
            assertNotAttacked( OPP_ONE, labelHit );
        });
    }
    
    @Test
    public void testMissHit()
    {
        Label           label       = new Label( "A1" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        String          hit         = buildCommand( HIT, OPP_ONE, label );
        List<String>    script      = List.of( miss, hit, QUITB );
        testSimpleCommand( script, r -> assertIsHit( OPP_ONE, label ) );
    }
    
    @Test
    public void testHitMiss()
    {
        Label           label       = new Label( "A1" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        String          hit         = buildCommand( HIT, OPP_ONE, label );
        List<String>    script      = List.of( hit, miss, QUITB );
        testSimpleCommand( script, r -> assertIsMiss( OPP_ONE, label ) );
    }
    
    @Test
    public void testUndoHit()
    {
        Label           label       = new Label( "A1" );
        String          hit         = buildCommand( HIT, OPP_ONE, label );
        List<String>    script      = List.of( hit );
        testUndo1x1( script, r -> assertNotAttacked( OPP_ONE, label ) );
    }
    
    @Test
    public void testUndoMiss()
    {
        Label           label       = new Label( "A1" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        List<String>    script      = List.of( miss );
        testUndo1x1( script, r -> assertNotAttacked( OPP_ONE, label ) );
    }
    
    @Test
    public void testMissHitUndo()
    {
        Label           label       = new Label( "A1" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        String          hit         = buildCommand( HIT, OPP_ONE, label );
        List<String>    script      = List.of( miss, hit );
        testUndo1x1( script, r -> assertIsMiss( OPP_ONE, label ) );
    }
    
    @Test
    public void testHitMissUndo()
    {
        Label           label       = new Label( "A1" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        String          hit         = buildCommand( HIT, OPP_ONE, label );
        List<String>    script      = List.of( hit, miss );
        testUndo1x1( script, r -> assertIsHit( OPP_ONE, label ) );
    }
    
    public void testUndoStrike()
    {
        Label           label       = new Label( "A1" );
        String          strike      = buildCommand( STRIKE, label );
        List<String>    script      = List.of( strike );
        testUndo1x1( script, r -> assertNotStrike(  label ) );
    }
    
    @Test
    public void testUndoMiss2x1()
    {
        // Miss twice, undo once, final status should be miss
        Label           label       = new Label( "A1" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        testUndo2x1( miss, () -> assertIsMiss( OPP_ONE, label ) );
    }
    
    @Test
    public void testUndoMiss2x2()
    {
        // Miss twice, undo twice, final status should be not attacked
        Label           label       = new Label( "A1" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        testUndo2x2( miss, () -> assertNotAttacked( OPP_ONE, label ) );
    }
    
    @Test
    public void testUndoHit2x1()
    {
        // Hit twice, undo once, final status should be Hit
        Label           label       = new Label( "A1" );
        String          hit         = buildCommand( HIT, OPP_ONE, label );
        testUndo2x1( hit, () -> assertIsHit( OPP_ONE, label ) );
    }
    
    @Test
    public void testUndoHit2x2()
    {
        // Hit twice, undo twice, final status should be not attacked
        Label           label       = new Label( "A1" );
        String          hit         = buildCommand( HIT, OPP_ONE, label );
        testUndo2x2( hit, () -> assertNotAttacked( OPP_ONE, label ) );
    }
    
    @Test
    public void testUndoStrike2x1()
    {
        // Strike twice, undo once, final status should be is-strike
        Label           label       = new Label( "A1" );
        String          strike      = buildCommand( STRIKE, label );
        testUndo2x1( strike, () -> assertIsStrike( label ) );
    }
    
    @Test
    public void testUndoStrike2x2()
    {
        // Strike twice, undo twice, final status should be is-not-strike
        Label           label       = new Label( "A1" );
        String          strike      = buildCommand( STRIKE, label );
        testUndo2x2( strike, () -> assertNotStrike( label ) );
    }
    
    @Test
    public void testUndoUnstrike2x1()
    {
        // Strike, then unstrike twice; 
        // undo once, final status should be not-is-strike
        Label           label       = new Label( "A1" );
        String          strike      = buildCommand( STRIKE, label );
        String          unstrike    = buildCommand( UNSTRIKE, label );
        Runnable        validator   = () -> assertNotStrike( label );
        testUndo2x1( unstrike, validator, strike );
    }
    
    @Test
    public void testUndoUnstrike2x2()
    {
        // Strike, then unstrike twice; 
        // undo once, final status should be is-strike
        Label           label       = new Label( "A1" );
        String          strike      = buildCommand( STRIKE, label );
        String          unstrike    = buildCommand( UNSTRIKE, label );
        Runnable        validator   = () -> assertIsStrike( label );
        testUndo2x2( unstrike, validator, strike );
    }
    
    @Test
    public void testUndoUnattack2x1()
    {
        // Attack, then unattack once; 
        // undo once, final status should be is-attacked
        Label           label       = new Label( "A1" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        String          unattack    = buildCommand( UNATTACK, OPP_ONE, label );
        Runnable        validator   = () -> assertNotAttacked( OPP_ONE, label );
        testUndo2x1( unattack, validator, miss );
    }
    
    @Test
    public void testUndoUnattack2x2()
    {
        // Strike, then unattack twice; 
        // undo twice, final status should be is-attacked
        Label           label       = new Label( "A1" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        String          unattack    = buildCommand( UNATTACK, OPP_ONE, label );
        Runnable        validator   = () -> assertIsAttacked( OPP_ONE, label );
        testUndo2x2( unattack, validator, miss );
    }
    
    @Test
    public void testAutoNoArg()
    {
        List<String>                script      = List.of( AUTO, QUITB );
        Consumer<RoundTripResult>   validator   = r -> {
            List<String>        rawFeedback = r.feedback();
            RoundTripParser     parser      = new RoundTripParser( rawFeedback );
            List<CommandBucket> buckets     = parser.buckets;
            //sanity check
            assertTrue( buckets.size() > 0 );
            CommandBucket       bucket0     = buckets.get( 0 );
            assertTrue( hasAutoUpdate( bucket0 ) );
        };
        testSimpleCommand( script, validator );
    }
    
    @Test
    public void testAuto()
    {
        testAutoOff( auto( "off" ) );
    }
    
    @Test
    public void testAutoUndo()
    {
        testAutoOff( UNDO );
    }
    
    @Test
    public void testRedoHit()
    {
        Label           label       = new Label( "A1" );
        String          hit         = buildCommand( HIT, OPP_ONE, label );
        List<String>    script      = List.of( hit );
        testRedo( script, r -> assertIsAttacked( OPP_ONE, label ) );
    }
    
    @Test
    public void testRedoMiss()
    {
        Label           label       = new Label( "A1" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        List<String>    script      = List.of( miss );
        testRedo( script, r -> assertIsAttacked( OPP_ONE, label ) );
    }
    
    @Test
    public void testRedoStrike()
    {
        // Strike both an occupied cell and an unoccupied cell
        Label           label       = getUnoccupiedCell();
        String          strike      = buildCommand( STRIKE, label );
        List<String>    commands    = List.of( strike );
        testRedo( commands, r -> assertIsStrike( label ) );
    }
    
    @Test
    public void testRedoUnstrike()
    {
        Label           cell        = getOccupiedCell();
        String          strike      = buildCommand( STRIKE, cell );
        String          unstrike    = buildCommand( UNSTRIKE, cell );
        List<String>    script      = List.of( strike, unstrike );
        testRedo( script, r -> assertNotStrike( cell ) );
    }
    
    @Test
    public void testAutoRedo()
    {
        Label           label       = new Label( "a1" );
        String          autoOn      = auto( "on" );
        String          autoOff     = auto( "off" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        List<String>    script      =
            List.of(
                autoOff, // bucket[0]; expect no-update to follow
                autoOn,  // bucket[1]; expect update to follow
                UNDO,    // bucket[2]; expect no-update to follow
                REDO,    // bucket[3]; expect update to follow
                miss,    // bucker[4]; expect update to follow
                autoOff, // bucket[6]; expect no-update to follow
                UNDO,    // bucket[7]; expect update to follow
                miss,    // bucket[8]; expect update to follow
                QUITB    // bucket[9]; update not tested
            );
        List<Boolean>    expResults  =
            List.of(
                false,   // bucket[0]; expect no-update to follow
                true,    // bucket[1]; expect update to follow
                false,   // bucket[2]; expect no-update to follow
                true,    // bucket[3]; expect update to follow
                true,    // bucker[4]; expect update to follow
                false,   // bucket[6]; expect no-update to follow
                true,    // bucket[7]; expect update to follow
                true,    // bucket[8]; expect update to follow
                false    // bucket[9]; update not tested
            );
        // sanity check
        assertEquals( script.size(), expResults.size() );
        RoundTripResult     result      = execRoundTrip( script );
        List<String>        rawFeedback = result.feedback();
        RoundTripParser     parser      = new RoundTripParser( rawFeedback );
        List<CommandBucket> buckets     = parser.buckets;

        int                 bucketCount = buckets.size();
        // sanity check
        assertEquals( script.size(), bucketCount );

        assertFalse( TextTestUtils.contains( rawFeedback, FAILURE ) );
        assertTrue( TextTestUtils.contains( rawFeedback, SUCCESS ) );
        assertTrue( result.phase().isQuit() );
        
        int numLoops    = bucketCount - 1;
        for ( int inx = 0 ; inx < numLoops ; ++inx )
        {
            String          strInx      = String.valueOf( inx );
            CommandBucket   bucket      = buckets.get( inx );
            boolean         expResult   = expResults.get( inx );
            boolean         actResult   = hasAutoUpdate( bucket );
            assertEquals( expResult, actResult, strInx );
        }
    }

    @Test
    public void testShow()
    {
        String          autoOff     = auto( "off" );
        List<String>    script      =
            List.of(
                autoOff, // bucket[0]; expect no-update to follow
                SHOW,    // bucket[1]; expect no auto-update to follow;
                         //            expect command output to contain update
                QUITB    // bucket[2]
            );
        RoundTripResult     result      = execRoundTrip( script );
        List<String>        rawFeedback = result.feedback();
        RoundTripParser     parser      = new RoundTripParser( rawFeedback );
        List<CommandBucket> buckets     = parser.buckets;
        assertFalse( TextTestUtils.contains( rawFeedback, FAILURE ) );
        assertTrue( TextTestUtils.contains( rawFeedback, SUCCESS ) );
        assertTrue( result.phase().isQuit() );
        assertEquals( script.size(), buckets.size() );
        
        CommandBucket       bucket0     = buckets.get( 0 );
        CommandBucket       bucket1     = buckets.get( 1 );
        assertFalse( hasAutoUpdate( bucket0 ) );
        assertFalse( hasUpdateOutput( bucket0 ) );
        assertFalse( hasAutoUpdate( bucket1 ) );
        assertTrue( hasUpdateOutput( bucket1 ) );
    }
    
    @ParameterizedTest
    @ValueSource( strings= { "", "   " } )
    public void testNoop( String noop )
    {
        testSimpleCommand( List.of( noop ), r -> {} );
    }
    
    @ParameterizedTest
    @ValueSource( strings= { "?", "h", "help" } )
    public void testHelp( String help )
    {
        List<String>    script  = List.of( help, QUITB );
        RoundTripResult     result      = execRoundTrip( script );
        List<String>        rawFeedback = result.feedback();
        assertFalse( TextTestUtils.contains( rawFeedback, FAILURE ) );
        assertTrue( TextTestUtils.contains( rawFeedback, SUCCESS ) );
        assertTrue( TextTestUtils.contains( rawFeedback, HELP_END ) );
    }

    @ParameterizedTest
    @ValueSource( strings= { "q", "quit" } )
    public void testQuit( String quit )
    {
        List<String>    script  = List.of( quit, "NO", quit, "YES" );
        testGameExit( script, true );
    }
    @ParameterizedTest
    @ValueSource( strings= { "q!", "quit!" } )
    public void testQuitReally( String quit )
    {
        List<String>    script  = List.of( quit );
        testGameExit( script, true );
    }
    
    @ParameterizedTest
    @ValueSource( strings= { "go", "game-over" } )
    public void testGameOver( String gameOver )
    {
        List<String>    script  = List.of( gameOver, "NO", gameOver, "YES" );
        testGameExit( script, false );
    }
    
    @ParameterizedTest
    @ValueSource( strings= { "go!", "game-over!" } )
    public void testGameOverReally( String gameOver )
    {
        List<String>    script  = List.of( "go!" );
        testGameExit( script, false );
    }
    
    @Test
    public void testOneOpponent()
    {
        resetConfig( 1 );
        Label           missCell        = new Label( "A1" );
        Label           hitCell         = new Label( "A2" );
        Label           unattackCell    = new Label( "A3" );
        
        String          miss            = buildCommand( MISS, missCell );
        String          hit             = buildCommand( HIT, hitCell );
        String          attack          = buildCommand( HIT, unattackCell );
        String          unattack        = buildCommand( UNATTACK, unattackCell );
        
        List<String>    script          =
            List.of( miss, hit, attack, unattack );
        
        testSimpleCommand( script, r -> {
            assertIsMiss( OPP_ONE, missCell );
            assertIsHit( OPP_ONE, hitCell );
            assertNotAttacked( OPP_ONE, unattackCell );
        });
    }
    
    /**
     * Execute a given script
     * ending with a quit or game-over.
     * Every command in the script
     * must complete successfully.
     * Validate the result.
     * 
     * @param script    script to execute
     * @param expQuit   expected return from GamePhase.isQuit().
     */
    private void testGameExit( 
        List<String> script, 
        boolean expQuit
    )
    {
        RoundTripResult     result      = execRoundTrip( script );
        List<String>        rawFeedback = result.feedback();
        assertFalse( TextTestUtils.contains( rawFeedback, FAILURE ) );
        assertTrue( TextTestUtils.contains( rawFeedback, SUCCESS ) );
        assertEquals( expQuit, result.phase().isQuit() );
    }
    
    @ParameterizedTest
    @MethodSource( "invalidCommands" )
    public void testCommandsGoWrong( GoWrong context )
    {
        String          command     = context.badCommand();
        List<String>    script      = List.of( command, QUITB );
        RoundTripResult result      = execRoundTrip( script );
        List<String>    rawFeedback = result.feedback();
        assertTrue( TextTestUtils.contains( rawFeedback, FAILURE ) );
        String          expError    = context.expError();
        assertTrue( TextTestUtils.contains( rawFeedback, expError ), command );
    }
    
    private void testSimpleCommand( 
        List<String> commands, 
        Consumer<RoundTripResult> validator
    )
    {
        List<String>    script      = new ArrayList<>( commands );
        script.add( QUITB );
        RoundTripResult result      = execRoundTrip( script );
        List<String>    feedback    = result.feedback();
        assertTrue( TextTestUtils.contains( feedback, SUCCESS ) );
        assertFalse( TextTestUtils.contains( feedback, FAILURE ) );
        assertTrue( result.phase().isQuit() );
        validator.accept( result );
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
    private void testAutoOff( String turnOff )
    {
        Label           label       = new Label( "a1" );
        String          autoOn      = auto( "on" );
        String          autoOff     = auto( "off" );
        String          miss        = buildCommand( MISS, OPP_ONE, label );
        List<String>    script      =
            List.of(
                autoOff, // bucket[0]; expect no-update to follow
                autoOn,  // bucket[1]; expect update to follow
                turnOff, // bucket[2]; expect no-update to follow
                miss,    // bucket[3]; expect no-update to follow
                QUITB    // bucket[4]; update not tested
            );
        RoundTripResult     result      = execRoundTrip( script );
        List<String>        rawFeedback = result.feedback();
        RoundTripParser     parser      = new RoundTripParser( rawFeedback );
        List<CommandBucket> buckets     = parser.buckets;
        assertFalse( TextTestUtils.contains( rawFeedback, FAILURE ) );
        assertTrue( TextTestUtils.contains( rawFeedback, SUCCESS ) );
        assertTrue( result.phase().isQuit() );
        assertEquals( script.size(), buckets.size() );
        
        assertFalse( hasAutoUpdate( buckets.get( 0 ) ) );
        assertTrue( hasAutoUpdate( buckets.get( 1 ) ) );
        assertFalse( hasAutoUpdate( buckets.get( 2 ) ) );
        assertFalse( hasAutoUpdate( buckets.get( 3 ) ) );
        assertFalse( hasAutoUpdate( buckets.get( 4 ) ) );
    }
    
    /**
     * Add UNDO, QUITB to a given list of commands
     * and validate the result.
     * 
     * @param commands      given list of commands
     * @param validator     logic for validating a result.
     */
    private void testUndo1x1( 
        List<String> commands, 
        Consumer<RoundTripResult> validator
    )
    {
        List<String>    script      = 
            Stream.concat(
                commands.stream(),
                Stream.of( UNDO, QUITB )
            ).toList();
        RoundTripResult result      = execRoundTrip( script );
        List<String>    feedback    = result.feedback();
        assertTrue( TextTestUtils.contains( feedback, SUCCESS ) );
        assertTrue( result.phase().isQuit() );
        validator.accept( result );
    }

    /**
     * Execute a command twice, and undo once, followed by quit.
     * Validate the result.
     * The client can specify 0 or more commands
     * to insert into the command stream
     * prior to executing the given command
     * 
     * @param command   command to execute
     * @param validator logic to validate result
     * @param prefix    
     *      0 or more commands to insert into the command stream
     */
    private void testUndo2x1( 
        String command, 
        Runnable validator, 
        String... prefix
    )
    {
        // Execute twice, undo once, validate final status
        List<String>    script      = 
            Stream.concat(
                Arrays.stream( prefix ),
                Stream.of( command, command, UNDO, QUITB )
            ).toList();
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        validator.run();
    }

    /**
     * Execute a command twice, undo twice, and quit.
     * Validate the result.
     * The client can specify 0 or more commands
     * to insert into the command stream
     * prior to executing the given command
     * 
     * @param command   command to execute
     * @param validator logic to validate result
     * @param prefix    
     *      0 or more commands to insert into the command stream
     */
    private void testUndo2x2( 
        String command, 
        Runnable validator, 
        String... prefix
    )
    {
        // Execute twice, undo once, validate final status
        List<String>    script      = 
            Stream.concat(
                Arrays.stream( prefix ),
                Stream.of( command, command, UNDO, UNDO, QUITB )
            ).toList();
        RoundTripResult result      = execRoundTrip( script );
        assertTrue( result.phase().isQuit() );
        validator.run();
    }
    
    /**
     * Add UNDO, REDO, QUITB to a given list of commands
     * and validate the result.
     * 
     * @param commands      given list of commands
     * @param validator     logic for validating a result.
     */
    private void testRedo( 
        List<String> commands, 
        Consumer<RoundTripResult> validator
    )
    {
        List<String>    script      = 
            Stream.concat(
                commands.stream(),
                Stream.of( UNDO, REDO, QUITB )
            ).toList();
        RoundTripResult result      = execRoundTrip( script );
        List<String>    feedback    = result.feedback();
        assertTrue( TextTestUtils.contains( feedback, SUCCESS ) );
        assertTrue( result.phase().isQuit() );
        validator.accept( result );
    }

    /**
     * Construct a string incorporating
     * the given command, name, and label.
     * Example: {@code miss,Paul,A1}.
     * 
     * @param command   the given command
     * @param name      the given name
     * @param label     the given label
     * 
     * @return
     *      a string incorporating the given command, name, and label
     */
    private static String buildCommand( 
        String command, 
        String name, 
        Label  label
    )
    {
        String  cmd = command + "," + name + "," + label.getLabel();
        return cmd;
    }
    
    /**
     * Construct a string incorporating
     * the given command, name, and label.
     * Example: {@code miss,Paul,A1}.
     * 
     * @param command   the given command
     * @param name      the given name
     * @param label     the given label
     * 
     * @return
     *      a string incorporating the given command, name, and label
     */
    private static String buildCommand( 
        String command, 
        Label  label
    )
    {
        String  cmd = command  + "," + label.getLabel();
        return cmd;
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
    
    private static void assertNotStrike( Label label )
    {
        Grid2D  grid                = Grid2D.getHomeGrid();
        GridCoords      coords      = label.getGridCoords();
        Cell2DView      cell        = grid.getCellView( coords );
        assertFalse( cell.isSplatted() );
        assertEquals( cell.getShip(), getShip( label ) );
    }
    
    private static void assertIsAttacked( String name, Label label )
    {
        Grid2D  grid    = Grid2D.getGrid( name );
        assertIsAttacked( grid, label );
    }
    
    private static void assertIsAttacked( Grid2D grid, Label label )
    {
        assertNotNull( grid );
        GridCoords      coords      = label.getGridCoords();
        Cell2DView      cell        = grid.getCellView( coords );
        assertTrue( cell.isSplatted() );
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
     * Return true if the given bucket's suffix
     * appears to contain an update block.
     * Not to be confused with a bucket
     * that contains an update block
     * in it's output,
     * as will be the case of the SHOW command.
     * 
     * @param bucket    the given bucket
     * 
     * @return  true if bucket appears to contain an update block
     * 
     * @see #hasUpdate(List)
     */
    private static boolean hasAutoUpdate( CommandBucket bucket )
    {
        List<String>    suffix      = bucket.suffix();
        boolean         result      = hasUpdate( suffix );
        return result;
    }
    
    /**
     * Return true if the given bucket's output
     * appears to contain an update block.
     * Not to be confused with a bucket
     * that contains an update block
     * in it's suffix,
     * as will be the case when auto-update is turned on.
     * 
     * @param bucket    the given bucket
     * 
     * @return  true if bucket appears to contain an update block
     * 
     * @see #hasUpdate(List)
     */
    private static boolean hasUpdateOutput( CommandBucket bucket )
    {
        List<String>    output      = bucket.output();
        boolean         result      = hasUpdate( output );
        return result;
    }
    
    
    /**
     * Return true if the given lines of text
     * appear to contain an update block.
     * This is the block of text
     * that encapsulates the text-based display
     * of the game's physical grid.
     * An exhaustive analysis is not performed;
     * we're just looking for a few strings
     * that are expected to be contained 
     * in the update block
     * and not elsewhere.
     * 
     * @param text  the given lines of text
     * 
     * @return  true if bucket appears to contain an update block
     */
    private static boolean hasUpdate( List<String> text )
    {
        String          vLineStr    = String.valueOf( TextGridConstants.VLINE );
        boolean         result      =
            TextTestUtils.contains( text, OPP_ONE ) &&
            TextTestUtils.contains( text, OPP_TWO ) &&
            TextTestUtils.contains( text, vLineStr );
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
    
    private static Stream<GoWrong> invalidCommands()
    {
        List<GoWrong>   commands    =
            List.of( 
                new GoWrong( STRIKE, INVALID_ARG_COUNT ),
                new GoWrong( STRIKE + ",a1,o", INVALID_ARG_COUNT ),
                new GoWrong( STRIKE + ",xx", INVALID_COORDINATES ),
                new GoWrong( STRIKE + ",a20", INVALID_COORDINATES ),
                
                new GoWrong( UNSTRIKE, INVALID_ARG_COUNT ),
                new GoWrong( UNSTRIKE + ",a1,o", INVALID_ARG_COUNT ),
                new GoWrong( UNSTRIKE + ",xx", INVALID_COORDINATES ),
                new GoWrong( UNSTRIKE + ",a20", INVALID_COORDINATES ),
                
                new GoWrong( MISS + ",a1", GRID_NOT_FOUND ),
                new GoWrong( MISS + "," + OPP_ONE, GRID_NOT_FOUND ),
                new GoWrong( MISS + "," + OPP_ONE + ",A1,x", INVALID_ARG_COUNT ),
                new GoWrong( MISS + "," + OPP_ONE + ",xx", INVALID_COORDINATES ),
                new GoWrong( MISS + "," + OPP_ONE + ",a20", INVALID_COORDINATES ),
                
                new GoWrong( HIT + ",a1", GRID_NOT_FOUND ),
                new GoWrong( HIT + "," + OPP_ONE, GRID_NOT_FOUND ),
                new GoWrong( HIT + "," + OPP_ONE + ",A1,x", INVALID_ARG_COUNT ),
                new GoWrong( HIT + "," + OPP_ONE + ",xx", INVALID_COORDINATES ),
                new GoWrong( HIT + "," + OPP_ONE + ",20", INVALID_COORDINATES ),
                
                new GoWrong( UNATTACK + ",a1", GRID_NOT_FOUND ),
                new GoWrong( UNATTACK + "," + OPP_ONE, GRID_NOT_FOUND ),
                new GoWrong( UNATTACK + "," + OPP_ONE + ",A1,x", INVALID_ARG_COUNT ),
                new GoWrong( UNATTACK + "," + OPP_ONE + ",xx", INVALID_COORDINATES ),
                new GoWrong( UNATTACK + "," + OPP_ONE + ",20", INVALID_COORDINATES ),
                
                new GoWrong( AUTO + ",invalid", INVALID_ARG ),
                new GoWrong( AUTO + ",on,xx", INVALID_ARG_COUNT ),
                new GoWrong( "notacommand", INVALID_COMMAND )
            );
            
        return commands.stream();
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
    
    /**
     * Encapsulate an invalid command,
     * and the expected error message
     * obtained from attempting to execute the command.
     * 
     * @param   command     the invalid command
     * @param   expError    the expected error message
     */
    private record GoWrong( String badCommand, String expError )
    {
    }
}
