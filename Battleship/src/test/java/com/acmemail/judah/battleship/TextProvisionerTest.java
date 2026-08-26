package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship2D.ShipType2D;
import com.acmemail.judah.battleship2D.default_ship_types.Carrier;
import com.acmemail.judah.battleship2D.default_ship_types.Cruiser;
import com.acmemail.judah.battleship2D.default_ship_types.Destroyer;
import com.acmemail.judah.battleship2D.default_ship_types.Submarine;

class TextProvisionerTest
{
    private static final ShipType2D carrier2D   = 
        new ShipType2D( "Carrier2D", 5, 3, null );
    private static final ShipType2D destroyer2D = 
        new ShipType2D( "Destroyer2D", 4, 2, null );
    private static final ShipType2D cruiser2D = 
        new ShipType2D( "Cruiser2D", 3, 2, null );

    private static final List<String>   testAStrings    =
        List.of(
            "dim,15,12",
            "type,default all",
            "type,Carrier2D,5,3",    
            "type,Destroyer2D , 4 , 2",
            "type,Cruiser2D,4,2",
            "deploy,Carrier2D",
            "deploy,Destroyer2D",
            "deploy,Destroyer2D",
            "deploy,Submarine",
            "deploy,Cruiser2D",
            "deploy,Cruiser"
        );
    private static final List<ShipType2D>   testAToRegister =
        List.of(
            Cruiser.getType(),
            Submarine.getType(),
            carrier2D,
            destroyer2D,
            cruiser2D
        );
    private static final List<ShipType2D>   testAToDeploy   =
        List.of(
            Carrier.getType(),
            Cruiser.getType(),
            Destroyer.getType(),
            Submarine.getType(),
            carrier2D,
            destroyer2D,
            destroyer2D,
            cruiser2D
        );
    private static final int        testARows   = 15;
    private static final int        testACols   = 12;
    private static final TestData   testAData   =
        new TestData( testAToRegister, testAToDeploy, testARows, testACols );

    @Test
    public void testOfFile()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testOfReader() throws IOException
    {
        String[]        stringArr   = new String[testAStrings.size()];
        testAStrings.toArray( stringArr );
        String          concat      = String.join( "\n", stringArr );
        StringReader    reader      = new StringReader( concat );
        TextProvisioner provisioner = TextProvisioner.ofReader( reader );
        provisioner.getErrors().forEach( System.out::println );
        testAData.compare( provisioner );
    }

    @Test
    public void testOf()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testAddRec()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testResetSuccess()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetToRegister()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetToDeploy()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetErrors()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testIsSuccess()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetRows()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetCols()
    {
        fail("Not yet implemented");
    }
    
    private static void confirm( 
        List<ShipType2D> expList, 
        List<ShipType2D> actList 
    )
    {
        assertEquals( expList.size(), actList.size() );
        List<ShipType2D>    temp   = new ArrayList<>( actList );
        for ( ShipType2D expType : expList )
        {
            ShipType2D  actType = getShipType( expType, temp );
            assertNotNull( actType );
            temp.remove( actType );
        }
    }
    
    /**
     * Given a ShipType2D object,
     * get from the list of ship types to be registered,
     * the object with the same name 
     * as the given object.
     * Null is returned if not found.
     * 
     * @param shipType  the given ship type
     * 
     * @return  the ShipType2D object, or null if not found
     */
    private static ShipType2D getShipType( 
        ShipType2D shipType, 
        List<ShipType2D> types
    )
    {
        ShipType2D  type    = getShipType( shipType.typeName(), types );
        return type;
    }
    
    /**
     * Obtain from the list of ship types to  be registered,
     * the object with the given name. 
     * Null is returned if not found.
     * 
     * @param shipType  the given ship type
     * 
     * @return  the ShipType2D object, or null if not found
     */
    private static ShipType2D getShipType( String name, List<ShipType2D> types )
    {
        ShipType2D  type    = 
            types.stream()
                .filter( t -> name.equals( t.typeName() ) )
                .findFirst().orElse( null );
        return type;
    }

    private record TestData( 
        Collection<ShipType2D> toRegister,
        Collection<ShipType2D> toDeploy,
        Integer    rows,
        Integer    cols
    )
    {
        public void compare( TextProvisioner expData )
        {
            Set<ShipType2D> expToRegister   = 
                new HashSet<>( expData.getToRegister() );
            Set<ShipType2D> actToRegister   = 
                new HashSet<>( toRegister );
            
            Set<ShipType2D> expToDeploy     = 
                new HashSet<>( expData.getToDeploy() );
            Set<ShipType2D> actToDeploy     = 
                new HashSet<>( toDeploy );
            
            assertEquals( expToRegister, actToRegister, "to register" );
            assertEquals( expToDeploy, actToDeploy, "to deploy" );
            assertEquals( expData.getCols(), rows );
            assertEquals( expData.getCols(), cols );
        }
    }
}
