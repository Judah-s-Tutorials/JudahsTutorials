package com.acmemail.judah.battleship.ui.awt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.BasicProvisioner;
import com.acmemail.judah.battleship.Provisioner;
import com.acmemail.judah.battleship.StatusMessages;
import com.acmemail.judah.battleship.components.ComponentFinder;
import com.acmemail.judah.battleship.components.ComponentUtils;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.ui.text.TextTestUtils;

class SetupPhaseAWTTest
{
    /** Pre-populated provisioner; initialized before ever test. */
    private BasicProvisioner        populatedProv;
    /** Empty provisioner; initialized before ever test. */
    private BasicProvisioner        emptyProv;
    /** 
     * Test assistant; allocated as needed by individual tests,
     * disposed after every test.
     */
    private SetupAWTTestAssistant   assistant;
    /**  
     * Provisioner that can be used to shadow test activities.
     * Instantiated and initialized before every test when the 
     * test assistant is  created; disposed after every test. 
     * Maintenance is the responsibility of individual tests.
     * @see #allocateAssistant(BasicProvisioner)
     */
    private BasicProvisioner        shadowProv;
    /**  
     * Provisioner used to create the test dialog
     * (this occurs inside the test assistant).
     * Initialized before every test, disposed after every test.
     */
    private BasicProvisioner        workingProv;
    /** 
     * Convenient object field for ad hoc use by test methods.
     * Disposed after every test.
     */
    private SetupPhaseAWT           adHocDialog;
    
    /** 
     * Parameters to drive the delete-selected-items 
     * from the to-register list test. 
     * @see DelSelParams
     * @see #testDeleteSelected(DelSelParams)
     */
    private final DelSelParams<ShipType2D> delToRegParams    =
        new DelSelParams<>(
            i -> new ShipType2D( "Type-" + i, 3, 2, null ),
            Provisioner::getToRegister,
            (prov,type) -> prov.addToRegister( type ),
            (prov,type) -> prov.removeFromRegister( type ),
            SetupAWTTestAssistant::getToRegister,
            (assist,type) -> assist.addShipType( type ),
            (assist,arr) -> assist.selectToRegister( arr ),
            UIConstants.SDN_REG_DEL_BUTTON,
            () -> assertEquivalentToRegister()
    );
    
    /** 
     * Parameters to drive the delete-selected-items 
     * from the to-deploy list test. 
     * @see DelSelParams
     * @see #testDeleteSelected(DelSelParams)
     */
    private final DelSelParams<ShipType2D> delToDepParams    =
        new DelSelParams<>(
            i -> new ShipType2D( "Type-" + i, 3, 2, null ),
            Provisioner::getToDeploy,
            (prov,type) -> prov.addToDeploy( type ),
            (prov,type) -> prov.removeFromDeploy( type ),
            SetupAWTTestAssistant::getToDeploy,
            (assist,type) -> populateToDeployFromToRegister( type ),
            (assist,arr) -> assist.selectToDeploy( arr ),
            UIConstants.SDN_DEP_DEL_BUTTON,
            () -> assertEquivalentToDeploy()
    );
    
    /** 
     * Parameters to drive the delete-selected-items 
     * from the to-deploy list test. 
     * @see DelSelParams
     * @see #testDeleteSelected(DelSelParams)
     */
    private final DelSelParams<String> delPlayerParams    =
        new DelSelParams<>(
            i -> "Player-" + i,
            Provisioner::getPlayers,
            (prov,player) -> prov.addPlayer( player ),
            (prov,player) -> prov.removePlayer( player ),
            SetupAWTTestAssistant::getPlayers,
            (assist,player) -> {
                assist.putPlayerText( player );
                assist.clickPlayerAdd();
            },
            (assist,arr) -> assist.selectPlayers( arr ), 
            UIConstants.SDN_PLAYERS_DEL_BUTTON,
            () -> assertEquivalentPlayers()
        );
    
    @BeforeEach
    public void beforeEach()
    {
        populatedProv = new PopulatedProvisioner();
        emptyProv = new BasicProvisioner();
    }
    
    @AfterEach
    public void afterEach()
    {
        if ( assistant != null && assistant.isVisible() )
            assistant.clickCancel();
        ComponentFinder.disposeAll();
        assistant = null;
        shadowProv = null;
        workingProv = null;
        adHocDialog = null;
    }
    
    @Test
    public void testSetupPhaseAWTBasicProvisioner()
    {
        allocateAssistant( false );
        assertSame( emptyProv, assistant.getProvisioner() );
    }

    @Test
    public void testSetupPhaseAWTStringBasicProvisioner()
    {
        String              title       = 
            "setupPhaseAWTWindowStringBasicProvisioner";
        BasicProvisioner    provisioner = new BasicProvisioner();
        
        ComponentUtils.invokeAndWait( () -> {
            adHocDialog = new SetupPhaseAWT( title, provisioner );
        });
        
        JDialog dialog  = SetupAWTTestAssistant.getJDialog( title );
        assertNotNull( dialog );
        assertSame( provisioner, adHocDialog.getProvisioner() );
    }

    @Test
    public void testSetupPhaseAWTWindowStringBasicProvisioner()
    {
        String              title       = 
            "setupPhaseAWTWindowStringBasicProvisioner";
        String              parentTitle = "AWT Dialog Parent";
        BasicProvisioner    provisioner = new BasicProvisioner();
        int                 rowCount    = 17;
        provisioner.setRows( rowCount );
        
        ComponentUtils.invokeAndWait( () -> {
            JFrame  parent  = new JFrame( parentTitle );
            parent.pack();
            adHocDialog = new SetupPhaseAWT( parent, title, provisioner );
        });
        
        JFrame  parent  = SetupAWTTestAssistant.getJFrame( parentTitle );
        assertNotNull( parent );
        JDialog dialog  = SetupAWTTestAssistant.getJDialog( title );
        assertNotNull( dialog );
        assertSame( dialog.getOwner(), parent );
        assertSame( provisioner, adHocDialog.getProvisioner() );
    }

    @Test
    public void testShowDialogOK()
    {
        allocateAssistant( false );
        assistant.clickOK();
        assertTrue( assistant.getCloseAction() );
    }

    @Test
    public void testShowDialogCancel()
    {
        allocateAssistant( false );
        assistant.clickCancel();
        assertFalse( assistant.getCloseAction() );
    }
    
    @Test
    public void testSetRowsCols()
    {
        allocateAssistant( false );
        int     currRows    = assistant.getNumRows();
        int     currCols    = assistant.getNumCols();
        int     newRows     = currRows + 3;
        int     newCols     = currCols + 3;
        if ( newCols == newRows )
            ++newCols;
        currRows += 3;
        currCols += 3;
        assistant.setNumRows( newRows );
        assistant.setNumCols( newCols );
        shadowProv.setRows( newRows );
        shadowProv.setCols( newCols );
        assertEquivalent( shadowProv, workingProv );
    }
    
    @Test
    public void testAddPlayerGoRight()
    {
        allocateAssistant( false );

        List<String>    listA   =
            IntStream.range( 0, 3 )
                .mapToObj( i -> "Player-" + i )
                .toList();
        List<String>    listB   =
            IntStream.range( 0, 3 )
                .mapToObj( i -> Character.valueOf( (char)('A' + i) ) )
                .map( c -> "Player-" + c )
                .toList();
        
        // Set a name in the player text field, then activate the text field
        listA.forEach( n -> {
            assistant.putPlayerText( n );
            assistant.activatePlayerText();
            shadowProv.addPlayer( n );
            assertEquivalentPlayers();
        });
        
        // Set a name in the player text field, then push the add button
        listB.forEach( n -> {
            assistant.putPlayerText( n );
            assistant.clickPlayerAdd();
            shadowProv.addPlayer( n );
            assertEquivalentPlayers();
            assertEquivalent( shadowProv, workingProv );
        });
        
        List<String>    messages    = assistant.getMessages();
        assertTrue( messages.isEmpty() );
    }
    
    @Test
    public void testAddPlayerGoWrong()
    {
        allocateAssistant( false );

        String  name    = "Player-A";
        shadowProv.addPlayer( name );
        assistant.putPlayerText( name );
        assistant.clickPlayerAdd();
        assertEquivalentPlayers();
        List<String>    errors  = assistant.getMessages();
        assertTrue( errors.isEmpty() );
        
        assistant.putPlayerText( name );
        assistant.clickPlayerAdd();
        assertEquivalentPlayers();
        errors  = assistant.getMessages();
        assertTrue( 
            TextTestUtils.contains( errors, StatusMessages.DUP_PLAYER ) 
        );
    }

    @Test
    public void testAddDefaultsToRegister()
    {
        final String addDefaultButton   = UIConstants.SDN_REG_ADD_DEF_BUTTON;
        allocateAssistant( false );
        // Should be able to safely click the button twice; 
        // the second time will be a noop.
        assistant.clickJButton( addDefaultButton );
        assistant.clickJButton( addDefaultButton );
        shadowProv.addDefaultTypesToRegister();
        List<ShipType2D>    shadowList  = shadowProv.getToRegister();
        assertEquivalent( shadowList, assistant.getToRegister() );
        assertEquivalent( shadowList, workingProv.getToRegister() );
    }
    
    @Test
    public void testAddToRegisterOK()
    {
        allocateAssistant( false );

        List<ShipType2D>    listA   =
            IntStream.range( 0, 3 )
                .mapToObj( i -> "Type-" + i )
                .map( n -> new ShipType2D( n, 3, 2, null ) )
                .toList();
        
        // Set a name in the player text field, then activate the text field
        listA.stream().forEach( t -> {
            String  name    = t.typeName();
            assistant.addShipTypeOK( name, t.length(), t.breadth() );
            shadowProv.addToRegister( t );
            assertEquivalentToRegister();
        });
        
        List<String>    messages    = assistant.getMessages();
        assertTrue( messages.isEmpty() );
    }
    
    @Test
    public void testAddToRegisterCancel()
    {
        allocateAssistant( false );

        assistant.addShipTypeCancel();
        assertEquivalent( shadowProv, workingProv );
        List<String>    messages    = assistant.getMessages();
        assertTrue( messages.isEmpty() );
    }
    
    @Test
    public void testDeleteSelectedToRegister()
    {
        allocateAssistant( false );
        testDeleteSelected( delToRegParams );
    }
    
    @Test
    public void deleteFromToRegisterAndSynch()
    {
        allocateAssistant( true );
        
        // Deploy all the ships including some dupes. Delete one of the
        // to-register types, and make sure that type is also deleted
        // from the to-deploy list.
        
        // Copy-all from the register list to the deploy list
        List<ShipType2D>    dialogList  = assistant.getToRegister();
        int                 count       = dialogList.size();
        int[]               indices     =
            IntStream.range( 0, count ).toArray();
        assistant.selectToRegister( indices );
        assistant.clickJButton( UIConstants.SDN_COPY_BUTTON );
        dialogList.forEach( shadowProv::addToDeploy );
        // sanity check
        assertEquivalent( shadowProv, workingProv );
        assertEquivalent( dialogList, shadowProv.getToRegister() );

        // Copy the first item in the register list to the deploy
        // list, creating duplicates in the deploy list
        ShipType2D          toDeploy    = dialogList.get( 0 );
        assistant.selectToRegister( 0 );        
        IntStream.range( 1, 4 ).forEach( i -> {
            shadowProv.addToDeploy( toDeploy );
            List<ShipType2D>    shadowList  = shadowProv.getToDeploy();
            assistant.clickJButton( UIConstants.SDN_COPY_BUTTON );
            // sanity check
            String              num         = String.valueOf( i );
            assertEquals( shadowList, assistant.getToDeploy(), num );
            assertEquals( shadowList, workingProv.getToDeploy(), num );
            long                dupCount    = 
                assistant.getToDeploy()
                    .stream()
                    .filter( t -> t.equals( toDeploy ) )
                    .count();
            assertEquals( i + 1, dupCount );
        });
        
        // Delete the first item from the to-register list, which
        // will also remove matching items from the to-deploy list.
        // ... put the two lists in the shadow provisioner into
        // the state expected after the operation completes.
        List<ShipType2D>    shadowList  = shadowProv.getToDeploy();
        while ( shadowList.contains( toDeploy ) )
        {
            shadowProv.removeFromDeploy( toDeploy );
            shadowList  = shadowProv.getToDeploy();
        }
        shadowProv.removeFromRegister( toDeploy );
        // sanity check
        assertFalse( shadowList.contains(  toDeploy ) );
        assertEquivalent( shadowList, shadowProv.getToDeploy() );
        // delete the first item from the to-register list
        assistant.clickJButton( UIConstants.SDN_REG_DEL_BUTTON );
        
        assertEquivalent( shadowList, assistant.getToDeploy() );
        assertEquivalent( shadowProv, workingProv );
    }
    
    @Test
    public void testCopySelected()
    {
        allocateAssistant( false );

        List<ShipType2D>    toRegisterTypes =
            IntStream.range( 0, 10 )
                .mapToObj( i -> "Type-" + i )
                .map( n -> new ShipType2D( n, 3, 2, null ) )
                .toList();
        toRegisterTypes.forEach( t -> {
            assistant.addShipType( t ) ;
            shadowProv.addToRegister( t );
            assertEquivalentToRegister();
        });
        
        // Individually copy the first and last items in the list
        List<ShipType2D>    initToRegister  = assistant.getToRegister();
        int                 initCount       = initToRegister.size();
        IntStream.of( 0, initCount - 1 ).forEach( i -> {
            ShipType2D  type    = initToRegister.get( i );
            shadowProv.addToDeploy( type );
            assistant.selectToRegister( i );
            assistant.clickJButton( UIConstants.SDN_COPY_BUTTON );
            assertEquivalent( shadowProv, workingProv );
            assertEquivalentToDeploy();
        });
        
        // Select several non-contiguous to-register items,
        // and copy them all in one operation.
        int[]   indices = 
            IntStream.iterate( 1, i -> i < initCount - 2, i -> i + 2 ).toArray();
        Arrays.stream( indices )
            .mapToObj( initToRegister::get )
            .forEach( shadowProv::addToDeploy );
        assistant.selectToRegister( indices );
        assistant.clickJButton( UIConstants.SDN_COPY_BUTTON );
        assertEquivalent( shadowProv, workingProv );
        assertEquivalentToDeploy();
    }
    
    @Test
    public void deployDuplicates()
    {
        allocateAssistant( true );
        
        List<ShipType2D>    dialogList  = assistant.getToRegister();
        ShipType2D          toDeploy    = dialogList.get( 0 );
        assistant.selectToRegister( 0 );
        
        IntStream.range( 0, 3 ).forEach( i -> {
            shadowProv.addToDeploy( toDeploy );
            List<ShipType2D>    shadowList  = shadowProv.getToDeploy();
            assistant.clickJButton( UIConstants.SDN_COPY_BUTTON );
            String              count       = String.valueOf( i );
            assertEquals( shadowList, assistant.getToDeploy(), count );
            assertEquals( shadowList, workingProv.getToDeploy(), count );
        });
    }

    @Test
    public void testDeleteSelectedToDeploy()
    {
        allocateAssistant( false );
        testDeleteSelected( delToDepParams );
    }

    @Test
    public void testDeleteSelectedPlayers()
    {
        allocateAssistant( false );
        testDeleteSelected( delPlayerParams );
    }

    /**
     * Configure test helpers before a test.
     * Clients can start choose between a populated
     * and an empty provisioner
     * (the "working provisioner").
     * The shadow provisioner is set up
     * with data to that of the working provisioner,
     * and the test assistant is instantiated
     * with the working provisioner.
     * 
     * @param populate  true to start with a populated provisioner
     */
    private void allocateAssistant( boolean populate )
    {
        if ( populate )
        {
            workingProv = populatedProv;
            shadowProv = new PopulatedProvisioner();
            assistant = new SetupAWTTestAssistant( workingProv );
        }
        else
        {
            workingProv = emptyProv;
            shadowProv = new BasicProvisioner();
            assistant = new SetupAWTTestAssistant( emptyProv );
        }
        // The SetupPhaseAWT constructor, which is invoked in
        // SetupAWTTestAssistant, tweaks the rows and column
        // counters in the working provisioner, so update the
        // shadow provisioner to keep them in synch.
        shadowProv.setCols( workingProv.getCols() );
        shadowProv.setRows( workingProv.getRows() );
    }
    
    /**
     * Short cut to explicitly test the to-register list
     * from the working and shadow provisioners
     * against the to-register list in the GUI.
     */
    private void assertEquivalentToRegister()
    {
        assertEquivalent( Provisioner::getToRegister, assistant::getToRegister );
    }
    
    /**
     * Short cut to explicitly test the to-deploy list
     * from the working and shadow provisioners
     * against the to-deploy list in the GUI.
     */
    private void assertEquivalentToDeploy()
    {
        assertEquivalent( Provisioner::getToDeploy, assistant::getToDeploy );
    }
    
    /**
     * Short cut to explicitly test the players list
     * from the working and shadow provisioners
     * against the players list in the GUI.
     */
    private void assertEquivalentPlayers()
    {
        assertEquivalent( Provisioner::getPlayers, assistant::getPlayers );
    }
    
    /**
     * Test a list from from the working and shadow provisioners
     * against the corresponding list in the GUI.
     * 
     * @param provGetter    getter for the target list in a provisioner
     * @param dialogGetter  getter for the target list in the GUI
     */
    private void assertEquivalent(
        Function<Provisioner,List<?>> provGetter,
        Supplier<List<?>> dialogGetter
    )
    {
        List<?> shadowList  = provGetter.apply( shadowProv );
        List<?> workingList = provGetter.apply( workingProv );
        List<?> dialogList  = dialogGetter.get();
        assertEquivalent( shadowList, workingList );
        assertEquivalent( shadowList, dialogList );
    }
    
    /**
     * Asserts that two provisioner contain equivalent data.
     * 
     * @param provA the first provisioner to test
     * @param provB the second provisioner to test
     */
    private void assertEquivalent( Provisioner provA, Provisioner provB )
    {
        assertEquals( provA.getCols(), provB.getCols() );
        assertEquals( provA.getRows(), provB.getRows() );
        assertEquivalent( provA.getPlayers(), provB.getPlayers() );
        assertEquivalent( provA.getToRegister(), provB.getToRegister() );
        assertEquivalent( provA.getToDeploy(), provB.getToDeploy() );
    }
    
    /**
     * Asserts that two lists contain the same data.
     * Order is ignored.
     * The lists must not contain duplicate data.
     * 
     * @param listA the first list to test
     * @param listB the second list to test
     */
    private void assertEquivalent( List<?> listA, List<?> listB )
    {
        Set<?>  setA    = new HashSet<>( listA );
        Set<?>  setB    = new HashSet<>( listB );
        assertEquals( setA, setB );
    }
    
    /**
     * Adds a ship type the to-register list,
     * then transfers it to the to-deploy list.
     * 
     * @param type  the ship type to add
     */
    private void populateToDeployFromToRegister( ShipType2D type )
    {
        assistant.addShipType( type );
        shadowProv.addToRegister( type );
        int     endList = shadowProv.getToRegister().size() - 1;
        assistant.selectToRegister( endList );
        assistant.clickJButton( UIConstants.SDN_COPY_BUTTON );
    }
    
    /**
     * Execute the delete-selected-items test for a given list.
     * 
     * @param <T>       the type of the list under test
     * @param params    
     *      parameters that control the generation of test data,
     *      and the given list under test
     *      
     * @see DelSelParams
     */
    private <T> void testDeleteSelected( DelSelParams<T> params  )
    {
        // Generate and deploy test data, keeping the GUI and 
        // shadow provisioner lists in synch
        IntStream.range( 0, 10 )
            .mapToObj( params.generator()::apply )
            .forEach( t -> {
                params.assistSetter.accept( assistant, t );
                params.provSetter.accept( shadowProv, t );
                params.validator().run();
                assertEquivalentSelected( params );
                assertTrue( assistant.getMessages().isEmpty() );
            });
        
        // sanity check - verify that all data sets start out in synch.
        List<T> initialDialogList   = params.assistGetter().apply( assistant );
        List<T> initialShadowList   = params.provGetter().apply( shadowProv );
        List<T> initialWorkingList  = params.provGetter().apply( workingProv );
        assertEquivalent( initialShadowList, initialDialogList );
        assertEquivalent( initialShadowList, initialWorkingList );
        
        // delete the last and first items in the list, keeping the 
        // GUI and shadow lists in synch.
        int     initialSize         = initialDialogList.size();
        IntStream.of( initialSize - 1, 0 ).forEach( inx -> {
            params.provRemover.accept( shadowProv, initialDialogList.get( inx ) );
            params.assistSelector.accept( assistant, new int[] { inx } );
            assistant.clickJButton( params.deleteButton() );
            params.validator().run();
            assertEquivalentSelected( params );
            assertTrue( assistant.getMessages().isEmpty() );
        });
        
        // delete multiple, non-contiguous items in the list, keeping the 
        // GUI and shadow lists in synch.
        List<T> currDialogList  = params.assistGetter.apply( assistant );
        int     currSize        = currDialogList.size();
        int[]           indices = 
            IntStream.iterate( 1, i -> i < currSize, i -> i + 2 ).toArray();
        Arrays.stream( indices )
            .mapToObj( currDialogList::get )
            .forEach( t -> params.provRemover.accept( shadowProv, t ) );
        params.assistSelector.accept( assistant, indices );
        assistant.clickJButton( params.deleteButton() );
        params.validator().run();
        assertEquivalentSelected( params );
        assertTrue( assistant.getMessages().isEmpty() );
    }
    
    /**
     * Verify that the corresponding lists
     * from the shadow provisioner, working provisioner,
     * and GUI list are synched.
     * 
     * @param <T>       the type of the lists being compared
     * @param params    
     *      parameters that determine the selection of lists to compare
     */
    private <T> void assertEquivalentSelected( DelSelParams<T> params )
    {
        List<T> shadowList  = params.provGetter.apply( shadowProv );
        List<T> workingList = params.provGetter.apply( workingProv );
        List<T> assistList  = params.assistGetter.apply( assistant );
        assertEquivalent( shadowList, assistList );
        assertEquivalent( shadowList, workingList );
    }
    
    /**
     * Parameters to drive delete-selected-from tests.
     * 
     * @param <T>               the type of data being tested
     * @param generator     
     *      logic to generate data from a sequence of integers,
     *      for example "Player-0", "Player-1," etc.
     * @param provGetter        
     *      getter for the list under test in a provisioner
     *      for example, "shadowProvisioner.getToRegister()"
     * @param provSetter    
     *      setter to add items to the list under test in a provisioner,
     *      for example, shadowProvisioner.addPlayer( name )"
     * @param provRemover   
     *      mutator to remove items from the list under test in a provisioner
     *      for example, shadowProvisioner.removePlayer( name )"
     * @param assistGetter      
     *      getter for the list under test in the GUI,
     *      for example, "assistant.getToRegister()"
     * @param assistSetter
     *      setter to add items to the list under test in the GUI,
     *      for example, "{launch the add to-register dialog}"
     * @param assistSelector    
     *      selector for items in the list under test in the GUI,
     *      for example, "assistant.selectToRegister( 1, 2, 3 )"
     * @param deleteButton
     *      name of the pushbutton that starts the delete-seleced task,
     *      for example "UIConstants.SDN_PLAYERS_DEL_BUTTON"
     * @param validator
     *      logic to perform validation between mutations in the test,
     *      for example, assertEquivalentToDeploy()
     */
    private record DelSelParams<T>( 
        Function<Integer,T> generator,
        Function<Provisioner,List<T>> provGetter,
        BiConsumer<BasicProvisioner,T> provSetter,
        BiConsumer<BasicProvisioner,T> provRemover,
        Function<SetupAWTTestAssistant,List<T>> assistGetter,
        BiConsumer<SetupAWTTestAssistant,T> assistSetter,
        BiConsumer<SetupAWTTestAssistant,int[]> assistSelector,
        String deleteButton,
        Runnable validator
    )
    {
    }
}
