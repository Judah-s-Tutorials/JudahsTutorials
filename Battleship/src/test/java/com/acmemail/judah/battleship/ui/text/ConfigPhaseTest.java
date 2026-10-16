package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.CONFIG_SHIPS_END;
import static com.acmemail.judah.battleship.StatusMessages.CONFIG_SHIPS_START;
import static com.acmemail.judah.battleship.StatusMessages.DEPLOYED_HEADER;
import static com.acmemail.judah.battleship.StatusMessages.FAILURE;
import static com.acmemail.judah.battleship.StatusMessages.INTERSECTS_SHIP;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ARG_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_COORDINATES;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ORIENTATION;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_REC_NUM;
import static com.acmemail.judah.battleship.StatusMessages.OUT_OF_BOUNDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.battleship.BasicProvisioner;
import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Provisioner;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.model.default_ship_types.Battleship;
import com.acmemail.judah.battleship.model.default_ship_types.Carrier;
import com.acmemail.judah.battleship.model.default_ship_types.Cruiser;
import com.acmemail.judah.battleship.model.default_ship_types.Destroyer;
import com.acmemail.judah.battleship.model.default_ship_types.Submarine;

public class ConfigPhaseTest
{
    private static final Set<String>    allDefaultTypeNames =
        Set.of( 
            Battleship.getType().typeName(), 
            Carrier.getType().typeName(), 
            Cruiser.getType().typeName(), 
            Destroyer.getType().typeName(), 
            Submarine.getType().typeName()
        );
    private static final int        defaultGridRows = 11;
    private static final int        defaultGridCols = defaultGridRows + 2;
    
    private final Provisioner   defProvisioner  = getDefaultProvisioner();
    
    @BeforeEach
    public void beforeEach()
    {
        Grid2D.reset( defaultGridRows, defaultGridCols );
        Configurator.reset();
        Configurator.nextState();
        assertTrue( Configurator.isConfig() );
    }

    @Test
    public void testConfigPhaseProvisioner()
    {
        Configurator.reset();
        assertTrue( Configurator.isSetup() );
        ConfigPhase     config  = new ConfigPhase( defProvisioner );
        assertEquals( defProvisioner, config.getProvisioner() );
        
        assertThrows( NullPointerException.class, 
            () -> new ConfigPhase( null )
        );
    }

    @Test
    public void testConfigPhaseProvisionerReaderPrintStream()
    {
        List<String>    commands    = List.of( "quit!" );
        RoundTripResult result      = execRoundTrip( commands );
        assertTrue( result.phase().isQuit() );
        
        assertThrows( NullPointerException.class, 
            () -> new ConfigPhase( null, null, null )
        );
    }

    @Test
    public void testIsQuit()
    {
        RoundTripResult     result      = execRoundTrip( List.of( "q!" ) );
        assertTrue( result.phase().isQuit() );
    }

    @ParameterizedTest
    @ValueSource(strings={"q", "Q", "quit", "QUIT"})
    public void testQuit( String command )
    {
        RoundTripResult result  = execRoundTrip( List.of( command, "y" ) );
        assertTrue( result.phase().isQuit() );
    }

    @ParameterizedTest
    @ValueSource(strings={"q!", "Q!", "quit!", "QUIT!"})
    public void testQuitBang( String command )
    {
        RoundTripResult result  = execRoundTrip( List.of( command ) );
        assertTrue( result.phase().isQuit() );
    }

    @ParameterizedTest
    @ValueSource(strings={"done", "DONE", "done!", "DONE!"})
    public void testDone( String command )
    {
        // done command will fail unless the fleet is fully deployed
        ShipType2D          shipType    = Battleship.getType();
        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.setRows( defaultGridRows );
        provisioner.setCols( defaultGridRows );
        provisioner.addToRegister( shipType );
        provisioner.addToDeploy( shipType );
        List<String>        commands    = new ArrayList<>();
        commands.add( "deploy,1,A1,Horizontal" );
        commands.add( command );
        if ( !command.endsWith( "!" ) )
            commands.add( "y" );
        RoundTripResult result  = execRoundTrip( provisioner, commands );
        assertFalse( result.phase().isQuit() );
    }
    
    @Test
    public void testShipListsRightEmpty()
    {
        List<String>    commands    = 
            List.of( "update", "q!" );
        RoundTripResult     result      = execRoundTrip( commands );
        List<String>        feedback    = result.feedback();
        UpdateParser        parser      = new UpdateParser( feedback );
        List<String>        toDeploy    = parser.getToDeploy();
        List<String>        deployed    = parser.getDeployed();
        assertTrue( deployed.isEmpty() );
        assertEquals( allDefaultTypeNames.size(), toDeploy.size() );
        assertTrue( containsAll( allDefaultTypeNames, toDeploy ) );
    }
    
    @Test
    public void testShipListsRightShorter()
    {
        // 1. Provisioner should have a least 5 ships to deploy
        //    so when we deploy 2 the list of ships to deploy will
        //    be longer than the deployed list.
        // 2. The to-deploy list should contain no dupes, so that
        //    we can verify that the 2 deployed ships have been
        //    removed from the to-deploy list.
        assertTrue( defProvisioner.getToDeploy().size() > 4 );
        List<String>    commands    = 
            List.of( 
                "deploy,1,A1,Horizontal", // for result, see buckets[0]
                "deploy,1,B1,Horizontal", // for result, see buckets[1]
                "update",  // for result, see buckets[2]
                "q!"
            );
        RoundTripResult     result      = execRoundTrip( commands );
        List<String>        feedback    = result.feedback();
        RoundTripParser     rtParser    = new RoundTripParser( feedback );
        List<CommandBucket> buckets     = rtParser.buckets;
        // sanity check:
        assertEquals( commands.size(), buckets.size() );
        
        List<String>        updFeedback = buckets.get( 2 ).output();
        UpdateParser        parser      = new UpdateParser( updFeedback );
        List<String>        toDeploy    = parser.getToDeploy();
        List<String>        deployed    = parser.getDeployed();
        
        int                 origSize        = allDefaultTypeNames.size();
        int                 toDeploySize    = toDeploy.size();
        int                 deployedSize    = deployed.size();
        assertEquals( 2, deployedSize );
        assertEquals( origSize - 2, toDeploySize );
        assertEquals( allDefaultTypeNames.size() - 2, toDeploySize );
        deployed.forEach( s -> assertFalse( isShipInList( s, toDeploy ) ) );
    }
    
    @Test
    public void testShipListsLeftShorter()
    {
        // 1. Provisioner should have a least 3 ships to deploy
        //    so when we deploy toDeploy().size() - 1 ships
        //    the list of ships to deploy will have one element
        //    and the list of ships to deploy will have more
        //    than one element.
        // 2. The number of ships to deploy must be less than the
        //    number of rows in the grid in order for the deploy
        //    algorithm to work.
        // 3. The to-deploy list should contain no dupes, so that
        //    we can verify that the deployed ships have been
        //    removed from the to-deploy list.
        List<ShipType2D>    orig        = defProvisioner.getToDeploy();
        int                 origSize    = orig.size();
        assertTrue( origSize > 2 );
        
        // Deploy all the ships except one.
        List<String>    commands    = new ArrayList<>();
        IntStream.range( 0, origSize - 1 )
            .map( i -> 'A' + i )
            .mapToObj( i -> (char)i + "1" )
            .map( s -> "deploy,1," + s + ",Horizontal" )
            .forEach( commands::add );
        int             updateInx   = commands.size();
        commands.add( "update" );
        commands.add( "q!" );

        RoundTripResult     result      = execRoundTrip( commands );
        List<String>        feedback    = result.feedback();
        RoundTripParser     rtParser    = new RoundTripParser( feedback );
        List<CommandBucket> buckets     = rtParser.buckets;
        // sanity check:
        assertEquals( commands.size(), buckets.size() );
        
        List<String>        updFeedback = buckets.get( updateInx ).output();
        UpdateParser        parser      = new UpdateParser( updFeedback );
        List<String>        toDeploy    = parser.getToDeploy();
        List<String>        deployed    = parser.getDeployed();
        
        int                 toDeploySize    = toDeploy.size();
        int                 deployedSize    = deployed.size();
        assertEquals( 1, toDeploySize );
        assertEquals( origSize - 1, deployedSize );
        assertFalse( isShipInList( toDeploy.getFirst(), deployed ) );
    }
    
    @Test
    public void testShipListsLeftEmpty()
    {
        List<String>    origNames       = 
            defProvisioner.getToDeploy().stream()
                .map( t -> t.typeName() )
                .toList();
        int                 origSize    = origNames.size();
        
        // Deploy all the ships.
        List<String>    commands    = new ArrayList<>();
        IntStream.range( 0, origSize )
            .map( i -> 'A' + i )
            .mapToObj( i -> (char)i + "1" )
            .map( s -> "deploy,1," + s + ",Horizontal" )
            .forEach( commands::add );
        int             updateInx   = commands.size();
        commands.add( "update" );
        commands.add( "q!" );

        RoundTripResult     result      = execRoundTrip( commands );
        List<String>        feedback    = result.feedback();
        RoundTripParser     rtParser    = new RoundTripParser( feedback );
        List<CommandBucket> buckets     = rtParser.buckets;
        // sanity check:
        assertEquals( commands.size(), buckets.size() );
        
        List<String>        updFeedback = buckets.get( updateInx ).output();
        UpdateParser        parser      = new UpdateParser( updFeedback );
        List<String>        toDeploy    = parser.getToDeploy();
        List<String>        deployed    = parser.getDeployed();
        
        int                 toDeploySize    = toDeploy.size();
        int                 deployedSize    = deployed.size();
        assertEquals( 0, toDeploySize );
        assertEquals( origSize, deployedSize );
        origNames.forEach( s -> assertTrue( contains( deployed, s ) ) );
    }
    
    @Test
    public void testDeployGoRight()
    {
        List<ShipType2D>    origToDeploy    = 
            new ArrayList<>( defProvisioner.getToDeploy() );
        // 1. Provisioner must have a least 3 ships to deploy
        //    so when we can deploy two and still have ships
        //    that haven't been deployed.
        // 2. The to-deploy list must contain no dupes, so that
        //    we can verify that the deployed ships have been
        //    removed from the to-deploy list.
        assertTrue( origToDeploy.size() > 2 );
        
        List<String>        commands        =
            List.of( 
                "deploy,1,A1,Horizontal", // for result, see buckets[0]
                "deploy,1,B1,Horizontal", // for result, see buckets[1]
                "q!"
            );
        RoundTripResult     result      = execRoundTrip( commands );
        List<String>        feedback    = result.feedback();
        RoundTripParser     rtParser    = new RoundTripParser( feedback );
        assertFalse( contains( rtParser.feedback, FAILURE ) );
        TextPhase           phase       = result.phase();
        assertTrue( phase instanceof ConfigPhase );
        
        ConfigPhase         configPhase = (ConfigPhase)phase;
        List<ShipType2D>    toDeploy    = configPhase.getToBeDeployed();
        List<ShipType2D>    deployed    = configPhase.getDeployedTypes();
        IntStream.range( 0, 2 )
            .mapToObj( origToDeploy::get )
            .forEach( t -> {
                assertFalse( toDeploy.contains( t ) );
                assertTrue( deployed.contains( t ) );
            });
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"deploy,1,A1,H,extra", "deploy,1,A1"} )
    public void testDeployGoWrongArgCount( String command )
    {
        List<String>    commands    = List.of( command, "q!" );
        RoundTripResult result      = execRoundTrip( commands );
        List<String>    feedback    = result.feedback();
        assertTrue( contains( feedback, FAILURE ) );
        assertTrue( contains( feedback, INVALID_ARG_COUNT ) );
    }
    
    @ParameterizedTest
    @ValueSource( strings= 
        {"deploy,0,A1,H", "deploy,100,A1,H", "deploy,a,A1,H", } )
    public void testDeployGoWrongRecNum( String command )
    {
        List<String>    commands    = List.of( command, "q!" );
        RoundTripResult result      = execRoundTrip( commands );
        List<String>    feedback    = result.feedback();
        assertTrue( contains( feedback, FAILURE ) );
        assertTrue( contains( feedback, INVALID_REC_NUM ) );
    }
    
    @ParameterizedTest
    @ValueSource( strings= 
        {"deploy,1,AA,H", "deploy,1,11,H", "deploy,1,1A,H" } )
    public void testDeployGoWrongCoords( String command )
    {
        List<String>    commands    = List.of( command, "q!" );
        RoundTripResult result      = execRoundTrip( commands );
        List<String>    feedback    = result.feedback();
        assertTrue( contains( feedback, FAILURE ) );
        assertTrue( contains( feedback, INVALID_COORDINATES ) );
    }
    
    @ParameterizedTest
    @ValueSource( strings= 
        {"deploy,1,A1,q", "deploy,1,A1,0" } )
    public void testDeployGoWrongOrientation( String command )
    {
        List<String>    commands    = List.of( command, "q!" );
        RoundTripResult result      = execRoundTrip( commands );
        List<String>    feedback    = result.feedback();
        assertTrue( contains( feedback, FAILURE ) );
        assertTrue( contains( feedback, INVALID_ORIENTATION ) );
    }
    
    @ParameterizedTest
    @ValueSource( strings= 
        {"deploy,1,Z1,V", "deploy,1,A100,H" } )
    public void testDeployGoWrongOOB( String command )
    {
        List<String>    commands    = List.of( command, "q!" );
        RoundTripResult result      = execRoundTrip( commands );
        List<String>    feedback    = result.feedback();
        assertTrue( contains( feedback, FAILURE ) );
        assertTrue( contains( feedback, OUT_OF_BOUNDS ) );
    }
    
    @Test
    public void testDeployGoWrongOverLapping()
    {
        List<String>    commands    = 
            List.of( 
                "deploy,1,A1,H",
                "deploy,1,A1,V",
                "q!"
        );
        RoundTripResult result      = execRoundTrip( commands );
        List<String>    feedback    = result.feedback();
        assertTrue( contains( feedback, FAILURE ) );
        assertTrue( contains( feedback, INTERSECTS_SHIP ) );
    }
    
    @Test
    public void testUnDeployGoRight()
    {
        List<ShipType2D>    origToDeploy    = 
            new ArrayList<>( defProvisioner.getToDeploy() );
        int                 origSize        = origToDeploy.size();
        
        List<String>        commands    = new ArrayList<>();
        // Deploy all ships
        IntStream.range( 0, origSize )
            .map( i -> 'A' + i )
            .mapToObj( i -> (char)i + "1" )
            .map( s -> "deploy,1," + s + ",Horizontal" )
            .forEach( commands::add );
        // Undeploy all ships
        IntStream.range( 0, origSize )
            .map( i -> 'A' + i )
            .mapToObj( i -> (char)i + "1" )
            .map( s -> "undeploy,1" )
            .forEach( commands::add );
        commands.add( "q!" );            
            
        RoundTripResult     result      = execRoundTrip( commands );
        List<String>        rawFeedback = result.feedback();
        RoundTripParser     rtParser    = new RoundTripParser( rawFeedback );
        List<String>        feedback    = rtParser.feedback;
        assertFalse( contains( feedback, FAILURE ) );
        TextPhase           phase       = result.phase();
        assertTrue( phase instanceof ConfigPhase );
        
        ConfigPhase         configPhase = (ConfigPhase)phase;
        List<ShipType2D>    toDeploy    = configPhase.getToBeDeployed();
        List<ShipType2D>    deployed    = configPhase.getDeployedTypes();
        assertEquals( toDeploy.size(), origSize );
        assertTrue( deployed.isEmpty() );
    }
    
    @Test
    public void testWhitespace()
    {
        List<String>    commands    =
            List.of(
                " deploy , 1 , A1 , Horizontal ",
                " undeploy , 1 ",
                " update ",
                " help ",
                " q! "
            );
        RoundTripResult     result      = execRoundTrip( commands );
        List<String>        rawFeedback = result.feedback();
        assertFalse( contains( rawFeedback, FAILURE ) );
    }

    /**
     * Determine whether the ship specified on the given line is present
     * on one of the lines in the given list.
     * We don't know the exact format of the lines,
     * but we assume the ship is named
     * in a line's first token
     * 
     * @param shipLine  the given line
     * @param list      the given list of lines
     * 
     * @return  
     *      true if the ship identified on shipLine is present
     *      on a line in list
     */
    private static boolean isShipInList( String shipLine, List<String> list )
    {
        // shipLine should be congruent to:
        //     1. Battleship 3x1 A1 horizontal
        // so ship name will be tokens[1]
        String[]    tokens  = shipLine.split( "\\s+" );
        String      ship    = tokens[1];
        boolean     result  =
            list.stream()
                .filter( s -> s.contains( ship ) )
                .findAny().isPresent();
        return result;
    }
    
    private static Provisioner getDefaultProvisioner()
    {
        Configurator.reset();
        assertTrue( Configurator.isSetup() );

        BasicProvisioner    provisioner = new BasicProvisioner();
        provisioner.addDefaultTypesToRegisterAndDeploy();
        provisioner.setCols( defaultGridCols );
        provisioner.setRows( defaultGridRows );
        return provisioner;
    }
    
    private static boolean containsAll( 
        Collection<String> exp, 
        Collection<String> act
    )
    {
        boolean containsAll =
            exp.stream()
                .filter( s -> !contains( act, s ) )
                .peek( System.out::println )
                .findAny().isEmpty();
        return containsAll;
    }
    
    private static boolean contains( Collection<String> source, String target )
    {
        boolean contains    =
            source.stream()
                .filter( s -> s.contains( target ) )
                .findAny()
                .isPresent();
        return contains;
    }

    private RoundTripResult execRoundTrip( 
        List<String> inputData
    )
    {
        RoundTripResult result  = execRoundTrip( defProvisioner, inputData );
        return result;
    }
    
    private static RoundTripResult execRoundTrip( 
        Provisioner  provisioner,
        List<String> inputData
    )
    {
        // Must be in setup phase to create a ConfigPhase object.
        // The ConfigPhase constructor automatically advances
        // to the Configurator to config.
        Configurator.reset();
        assertTrue( Configurator.isSetup() );
        PhaseGetter     getter      = 
            (r,p) -> new ConfigPhase( provisioner, r, p );
            
        RoundTripResult result      = 
            TextTestUtils.execRoundTrip( provisioner, getter, inputData );
        return result;
    }
    
    private class UpdateParser
    {
        private final List<String>  toDeploy;
        private final List<String>  deployed;
        
        private record ShipLists( List<String> reg, List<String> dep ) {}
        
        public UpdateParser( List<String> feedback )
        {
            ShipLists   lists   = getShipLists( feedback );
            toDeploy = lists.reg();
            deployed = lists.dep();
        }
        
        public List<String> getDeployed()
        {
            return deployed;
        }
        
        public List<String> getToDeploy()
        {
            return toDeploy;
        }
        
        private static int getSectionStart( 
            int          from,
            IntPredicate isStart, 
            List<String> feedback
        )
        {
            int size    = feedback.size();
            int start   = 
                IntStream.range( from, size )
                    .filter( isStart::test )
                    .findFirst().orElse( -1 );
            return start;
        }
        
        private static int getSectionEnd( 
            int          from,
            IntPredicate isEnd, 
            List<String> feedback
        )
        {
            int size    = feedback.size();
            int end     = 
                IntStream.range( from, size )
                    .filter( isEnd::test )
                    .findFirst().orElse( size );
            return end;
        }
        
        private static ShipLists getShipLists( List<String> feedback )
        {
            int     feedbackSize    = feedback.size();
            int     tableStart      = 
                getSectionStart( 
                    0,
                    i -> feedback.get( i ).contains( CONFIG_SHIPS_START ),
                    feedback
                );
            // hard-fail if start of ships table isn't found
            assertTrue( tableStart >= 0 );
            int     tableEnd        = 
                getSectionEnd(
                    tableStart + 2,
                    i -> feedback.get( i ).contains( CONFIG_SHIPS_END ),
                    feedback
                );

            // sanity check...
            // First three lines look something like this:
            //     Ships table start
            //     To Be Deployed       Deployed
            //     ===============      ===========
            assertTrue( tableStart < feedbackSize + 2 );
            String  headerLine1 = feedback.get( tableStart + 1 );
            String  headerLine2 = feedback.get( tableStart + 2 );
            assertTrue( headerLine1.contains( DEPLOYED_HEADER ) );
            assertTrue( headerLine2.contains( "====" ) );
            int     midPoint    = headerLine1.indexOf( DEPLOYED_HEADER );
            assertTrue( midPoint > 0 );

            int     startInx    = tableStart + 3;
            List<String> regList = IntStream.range( startInx, tableEnd )
                .mapToObj( feedback::get )
                .map( s -> 
                    s.substring( 0, Math.min( s.length(), midPoint ) )
                        .trim()
                )
                .filter( s -> !s.isEmpty() )
                .toList();
            List<String> depList = IntStream.range( startInx, tableEnd )
                .mapToObj( feedback::get )
                .filter( s -> s.length() >= midPoint )
                .map( s -> s.substring( midPoint ).trim() )
                .filter( s -> !s.isEmpty() )
                .toList();
            ShipLists   lists   = new ShipLists( regList, depList );
            return lists;
        }
    }
}
