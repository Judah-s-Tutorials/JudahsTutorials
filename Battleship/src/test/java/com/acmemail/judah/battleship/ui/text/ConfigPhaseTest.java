package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.CONFIG_SHIPS_END;
import static com.acmemail.judah.battleship.StatusMessages.CONFIG_SHIPS_START;
import static com.acmemail.judah.battleship.StatusMessages.DEPLOYED_HEADER;
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
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

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
        assertTrue( defProvisioner.getToDeploy().size() > 4 );
        List<String>    commands    = 
            List.of( 
                "deploy,1,A1,Horizontal",
                "deploy,1,A2,Horizontal",
                "update", 
                "q!"
            );
        RoundTripResult     result      = execRoundTrip( commands );
        List<String>        feedback    = result.feedback();
        UpdateParser        parser      = new UpdateParser( feedback );
        List<String>        toDeploy    = parser.getToDeploy();
        List<String>        deployed    = parser.getDeployed();
        assertTrue( deployed.isEmpty() );
        assertEquals( allDefaultTypeNames.size(), toDeploy.size() );
        assertTrue( containsAll( allDefaultTypeNames, toDeploy ) );
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
