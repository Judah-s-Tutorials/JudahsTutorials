package com.acmemail.judah.battleship;

import static com.acmemail.judah.battleship.StatusMessages.DUP_SHIP_TYPE;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_BREADTH;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_COL_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_LENGTH;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_P_COMMAND;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_P_RECORD;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ROW_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.SHIP_TYPE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Dimension;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.acmemail.judah.battleship2D.ShipType2D;
import com.acmemail.judah.battleship2D.default_ship_types.Battleship;
import com.acmemail.judah.battleship2D.default_ship_types.Carrier;
import com.acmemail.judah.battleship2D.default_ship_types.Cruiser;
import com.acmemail.judah.battleship2D.default_ship_types.Destroyer;
import com.acmemail.judah.battleship2D.default_ship_types.Submarine;

class TextProvisionerTest
{
    @TempDir
    Path tempDir;
    
    private static final ShipType2D battleshipType  = Battleship.getType();
    private static final ShipType2D carrierType     = Carrier.getType();
    private static final ShipType2D cruiserType     = Cruiser.getType();
    private static final ShipType2D destroyerType   = Destroyer.getType();
    private static final ShipType2D submarineType   = Submarine.getType();
    private static final Set<ShipType2D>    allDefaultTypes =
        Set.of( 
            battleshipType, 
            carrierType, 
            cruiserType, 
            destroyerType, 
            submarineType 
        );
    
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
            "type,Cruiser2D,3,2",
            "deploy,Carrier2D",
            "deploy,Destroyer2D",
            "deploy,Destroyer2D",
            "deploy,Submarine",
            "deploy,Cruiser2D",
            "deploy,Cruiser"
        );
    private static final List<ShipType2D>   testAToRegister =
        List.of(
            battleshipType, 
            carrierType, 
            cruiserType, 
            destroyerType, 
            submarineType,
            carrier2D,
            destroyer2D,
            cruiser2D
        );
    private static final List<ShipType2D>   testAToDeploy   =
        List.of(
            carrier2D, 
            destroyer2D,
            destroyer2D,
            submarineType,
            cruiser2D,
            cruiserType 
        );
    private static final int        testARows   = 15;
    private static final int        testACols   = 12;
    private static final TestData   testAData   =
        new TestData( testAToRegister, testAToDeploy, testARows, testACols );
    
    @Test
    public void testDimGoRight()
    {
        Dimension[] allDims = 
        {
            new Dimension( 10, 11 ),
            new Dimension( 20, 25 ),
            new Dimension( 30, 35 ),
            new Dimension( 40, 45 )
        };
        
        for ( Dimension dim : allDims )
        {
            String  text    = 
                listToBasicString( "dim", dim.height, + dim.width );
            testDimGoRight( text, dim );
            text = listToSpaceyString( "dim", dim.height, + dim.width );
            testDimGoRight( text, dim );
        }
    }
    
    @Test
    public void testDimGoWrong()
    {
        String[][]  allDims = 
        {
            { "bad", "5" },
            { "5", "bad" },
            { "bad", "bad" },

            { "0", "5" },
            { "5", "0" },
            { "0", "0" },

            { "-1", "5" },
            { "5", "-1" },
            { "-1", "-1" },
        };
        for ( String[] dims : allDims )
        {
            String  rec     = listToBasicString( "dim", dims[0], dims[1] );
            testDimGoWrong( rec, dims[0], dims[1] );
            rec = listToSpaceyString( "dim", dims[0], dims[1] );
            testDimGoWrong( rec, dims[0], dims[1] );
        }
        
        String  text    = listToBasicString( "dim", "5" );
        testCommandGoWrong( text, "dim" );
        text    = listToBasicString( "dim", "5", "5", "arg" );
        testCommandGoWrong( text, "dim" );
    }

    @Test
    public void testTypeGoRight()
    {
        ShipType2D[]    allTypes = { carrier2D, destroyer2D, cruiser2D };
        for ( ShipType2D type : allTypes )
        {
            String  name    = type.typeName();
            int     length  = type.length();
            int     breadth = type.breadth();
            String  text    = 
                listToBasicString( "type", name, length, breadth );
            testTypeGoRight( text, type );
            text = listToSpaceyString( "type", name, length, breadth );
            testTypeGoRight( text, type );
        }
    }
    
    @Test
    public void testTypeDefGoRight()
    {
        String[]    allRecs = 
        {
            "type, default all",
            "   type  ,  all   default   ",
            "type, all defaults",
            "   type  ,  all   defaults   ",
            "type,default",
            "type,defaults",
            "  type  ,  default  ",
            "  type  ,  defaults  ",
        };
        
        for ( String rec : allRecs )
            testTypeDefGoRight( rec );
    }
    
    @Test
    public void testTypeGoWrong()
    {
        String[][]  allDims = 
        {
            { "bad", "5" },
            { "5", "bad" },
            { "bad", "bad" },

            { "0", "5" },
            { "5", "0" },
            { "0", "0" },

            { "-1", "5" },
            { "5", "-1" },
            { "-1", "-1" },
        };
        for ( String[] dims : allDims )
        {
            String  rec     = 
                listToBasicString( "type", "name", dims[0], dims[1] );
            testTypeGoWrong( rec, dims[0], dims[1] );
            rec = listToSpaceyString( "type", "name", dims[0], dims[1] );
            testTypeGoWrong( rec, dims[0], dims[1] );
        }
        
        String  text    = listToBasicString( "type", "name", "5" );
        testCommandGoWrong( text, "type" );
        text    = listToBasicString( "type", "name", "5", "5", "arg" );
        testCommandGoWrong( text, "type" );
    }
    
    @Test
    public void testTypeGoWrongDup()
    {
        TextProvisioner provisioner = TextProvisioner.of();
        ShipType2D      type        = battleshipType;
        String          text        =
            listToBasicString( 
                "type", 
                type.typeName(), 
                type.length(), 
                type.breadth() 
            );
        
        // First time should work, second time should fail with dupe
        provisioner.addRec( text );
        assertTrue( provisioner.isSuccess() );
        provisioner.addRec( text );
        assertFalse( provisioner.isSuccess() );
        
        List<String>    errors  = provisioner.getErrors();
        String          error   =
            getContainingString( errors,  INVALID_P_RECORD );
        assertNotNull( error );
        assertContains( error, "type" );
        
        error = getContainingString( errors, DUP_SHIP_TYPE );
        assertNotNull( error );
        assertContains( error, type.typeName() );
    }
    
    @Test
    public void testTypeGoWrongDefDup()
    {
        TextProvisioner provisioner = TextProvisioner.of();
        ShipType2D      type        = battleshipType;
        
        // First time should work, second time should fail with many dupes
        provisioner.addRec( "type,default" );
        assertTrue( provisioner.isSuccess() );
        provisioner.addRec( "type,default" );
        assertFalse( provisioner.isSuccess() );
        
        List<String>    errors  = provisioner.getErrors();
        String          error   =
            getContainingString( errors,  INVALID_P_RECORD );
        assertNotNull( error );
        assertContains( error, "type" );
        
        List<String>    dupeErrors  =
            errors
                .stream()
                .filter( e -> e.contains( DUP_SHIP_TYPE ) )
                .toList();
        for ( ShipType2D defType : allDefaultTypes )
        {
            String  typeName    = defType.typeName();
            String  dupeError   = 
                getContainingString( dupeErrors, typeName );
            assertNotNull( dupeError, typeName );
        }
    }
    
    @Test
    public void testDeployGoRight()
    {
        ShipType2D[]    allTypes = { carrier2D, destroyer2D, cruiser2D };
        for ( ShipType2D type : allTypes )
        {
            String  name    = type.typeName();
            String  text    = 
                listToBasicString( "deploy", name );
            testDeployGoRight( text, type );
            text = listToSpaceyString( "deploy", name );
            testDeployGoRight( text, type );
        }
    }
    
    @Test
    public void testDeployGoWrong()
    {
        ShipType2D[]    allTypes = { carrier2D, destroyer2D, cruiser2D };
        for ( ShipType2D type : allTypes )
        {
            String  name    = type.typeName();
            String  text    = 
                listToBasicString( "deploy", name );
            testDeployGoWrong( text, type );
            text = listToSpaceyString( "deploy", name );
            testDeployGoWrong( text, type );
        }
        
        testCommandGoWrong( "deploy", "deploy" );
        String  text    = listToBasicString( "deploy", "name", "arg" );
        testCommandGoWrong( text, "deploy" );
    }

    @Test
    public void testOfFile() throws IOException
    {
        Path    testFile    = tempDir.resolve( "test.txt" );
        String  pathStr     = testFile.toAbsolutePath().toString();
        String  data        = String.join( "\n", testAStrings );
        Files.writeString( testFile, data );
        TextProvisioner provisioner = TextProvisioner.ofFile( pathStr );
        testAData.compare( provisioner );
    }

    @Test
    public void testOfFileNotFound() throws IOException
    {
        TextProvisioner provisioner = TextProvisioner.ofFile( "not found" );
        assertFalse( provisioner.isSuccess() );
    }

    @Test
    public void testOfReaderGoRight() throws IOException
    {
        TextProvisioner provisioner = feedReader( testAStrings );
        testAData.compare( provisioner );
    }

    @Test
    public void testOf()
    {
        TextProvisioner provisioner = TextProvisioner.of();
        assertTrue( provisioner.isSuccess() );
        assertTrue( provisioner.getErrors().isEmpty() );
        assertTrue( provisioner.getToRegister().isEmpty() );
        assertTrue( provisioner.getToDeploy().isEmpty() );
        assertNull( provisioner.getRows() );
        assertNull( provisioner.getCols() );
    }

    @Test
    public void testAddRec()
    {
        TextProvisioner provisioner = TextProvisioner.of();
        String          typeName    = "SomeType";
        int             rows        = 5;
        int             cols        = 10;
        ShipType2D      testType    = 
            new ShipType2D( typeName, rows, cols, null );
        
        String          text        = 
            listToBasicString( "type", typeName, rows, cols );
        provisioner.addRec( text );
        assertTrue( provisioner.isSuccess() );
        assertTrue( provisioner.getErrors().isEmpty() );
        assertTrue( provisioner.getToRegister().contains( testType ) );
        assertTrue( provisioner.getToDeploy().isEmpty() );
        assertNull( provisioner.getRows() );
        assertNull( provisioner.getCols() );
        
        text = listToBasicString( "deploy", typeName );
        provisioner.addRec( text );
        assertTrue( provisioner.isSuccess() );
        assertTrue( provisioner.getErrors().isEmpty() );
        assertTrue( provisioner.getToRegister().contains( testType ) );
        assertTrue( provisioner.getToDeploy().contains( testType ) );
        assertNull( provisioner.getRows() );
        assertNull( provisioner.getCols() );
    }
    
    @Test
    public void testInvalidCommand()
    {
        String          command     = "bad";
        TextProvisioner provisioner = TextProvisioner.of();
        provisioner.addRec( command );
        
        assertFalse( provisioner.isSuccess() );
        List<String>    errors      = provisioner.getErrors();
        String          error       =
            getContainingString( errors, INVALID_P_RECORD );
        assertNotNull( error );
        
        assertTrue( error.contains( command ) );
        error       =
            getContainingString( errors, INVALID_P_COMMAND );
        assertNotNull( error );
        assertTrue( error.contains( command ) );
    }
    
    @Test
    public void mixedRightWrong()
    {
        TestList    testList    = new TestList();
        testList.add( 
            "dim,5,10", p -> {
                assertEquals( 5, p.getRows() );
                assertEquals( 10, p.getCols() );
            });
        testList.execute();
        testList.test();
        
        testList.add(
            "type,typeName",
            p -> {
                String  msg = 
                    getContainingString( p.getErrors(), INVALID_P_RECORD );
                assertNotNull( msg );
            });
        testList.execute();
        testList.test();
        
        ShipType2D  type1   = battleshipType;
        String      type1Text    =
            listToBasicString( 
                "type", 
                type1.typeName(), 
                type1.length(), 
                type1.breadth()
            );
        testList.add(
            type1Text,
            p -> {
                assertTrue( p.getToRegister().contains( type1 ) );
            });
        testList.execute();
        testList.test();
        
        testList.add(
            type1Text,
            p -> {
                String  error   = 
                    getContainingString( p.getErrors(), DUP_SHIP_TYPE );
                assertNotNull( error );
                assertContains( error, type1.typeName() );
            });
        testList.execute();
        testList.test();
    }

    @Test
    public void testResetSuccess()
    {
        TextProvisioner provisioner = TextProvisioner.of();        
        String          text        = "arg,arg";
        provisioner.addRec( text );
        assertFalse( provisioner.isSuccess() );
        assertFalse( provisioner.getErrors().isEmpty() );
        
        provisioner.resetSuccess();
        assertTrue( provisioner.isSuccess() );
        assertTrue( provisioner.getErrors().isEmpty() );

        provisioner.addRec( text );
        assertFalse( provisioner.isSuccess() );
        assertFalse( provisioner.getErrors().isEmpty() );
    }

    @Test
    public void testGetToRegister()
    {
        TextProvisioner     provisioner = TextProvisioner.of();
        List<ShipType2D>    expList     = new ArrayList<>();
        for ( ShipType2D type : allDefaultTypes )
        {
            String  text    = 
                listToBasicString( 
                    "type", 
                    type.typeName(), 
                    type.length(), 
                    type.breadth() 
                );
            provisioner.addRec( text );
            expList.add( type );
            assertEquals( expList, provisioner.getToRegister() );
        }
    }

    @Test
    public void testGetToDeploy()
    {
        TextProvisioner     provisioner = TextProvisioner.of();
        List<ShipType2D>    expList     = new ArrayList<>();
        for ( ShipType2D type : allDefaultTypes )
        {
            String  text    = 
                listToBasicString( 
                    "type", 
                    type.typeName(), 
                    type.length(), 
                    type.breadth() 
                );
            provisioner.addRec( text );
            text = listToBasicString( "deploy", type.typeName() );
            provisioner.addRec( text );
            expList.add( type );
            List<ShipType2D>    actList = provisioner.getToDeploy();
            assertEquals( expList, actList );
        }
    }

    @Test
    public void testGetErrors()
    {
        TextProvisioner     provisioner = TextProvisioner.of();
        assertTrue( provisioner.getErrors().isEmpty() );
        provisioner.addRec( "arg" );
        assertFalse( provisioner.getErrors().isEmpty() );
    }

    @Test
    public void testIsSuccess()
    {
        TextProvisioner     provisioner = TextProvisioner.of();
        assertTrue( provisioner.isSuccess() );
        provisioner.addRec( "arg" );
        assertFalse( provisioner.isSuccess() );
    }

    @Test
    public void testGetRowsCols()
    {
        int             expRows     = 5;
        int             expCols     = 10;
        String          text        = 
            listToBasicString( "dim", expRows, expCols );
        TextProvisioner provisioner = TextProvisioner.of();
        
        assertNull( provisioner.getCols() );
        assertNull( provisioner.getRows() );
        provisioner.addRec( text );
        assertEquals( expRows, provisioner.getRows() );
        assertEquals( expCols, provisioner.getCols() );
    }
    
    @Test
    public void testEmptyFile() throws IOException
    {
        Path    testFile    = tempDir.resolve( "test.txt" );
        String  pathStr     = testFile.toAbsolutePath().toString();
        String  data        = "";
        Files.writeString( testFile, data );
        TextProvisioner provisioner = TextProvisioner.ofFile( pathStr );
        assertTrue( provisioner.isSuccess() );
        assertTrue( provisioner.getErrors().isEmpty() );
        assertTrue( provisioner.getToRegister().isEmpty() );
        assertTrue( provisioner.getToDeploy().isEmpty() );
        assertNull( provisioner.getRows() );
        assertNull( provisioner.getCols() );
    }
    
    @Test
    public void testRequireNonNull()
    {
        TextProvisioner provisioner;
        Class<NullPointerException> npeClass    = NullPointerException.class;
        assertThrows( npeClass, () -> TextProvisioner.ofReader( null ) );
        assertThrows( npeClass, () -> TextProvisioner.ofFile( null ) );
        
        provisioner = TextProvisioner.of();
        assertThrows( npeClass, () -> provisioner.addRec( null ) );
    }
    
    @Test
    public void testEmptyLines()
    {
        List<String>    commands    = List.of( "#comment", "", "   " );
        TextProvisioner provisioner = feedReader( commands );
        assertTrue( provisioner.isSuccess() );
        assertTrue( provisioner.getErrors().isEmpty() );
        assertTrue( provisioner.getToRegister().isEmpty() );
        assertTrue( provisioner.getToDeploy().isEmpty() );
        assertNull( provisioner.getRows() );
        assertNull( provisioner.getCols() );
        
        for ( String command : commands )
        {
            provisioner.addRec( command );
            assertTrue( provisioner.isSuccess() );
            assertTrue( provisioner.getErrors().isEmpty() );
        }
        
        provisioner.addRec( "type,Name,10,5" );
        provisioner.addRec( "deploy,Name" );
        assertTrue( provisioner.isSuccess() );
        assertTrue( provisioner.getErrors().isEmpty() );
        assertEquals( 1, provisioner.getToRegister().size() );
        assertEquals( 1, provisioner.getToDeploy().size() );
    }
    
    /**
     * Convert a given list of commands to a string reader,
     * and feed it to TextProvisioner.ofReader
     * to obtain a TextProvisioner object.
     * The TextProvisioner object is returned to the caller.
     * An assertion is raised if an IOException occurs.
     * 
     * @param commands  the given list of commands
     * 
     * @return the object obtained from TextProvisioner.ofReader
     */
    private TextProvisioner feedReader( List<String> commands )
    {
        String          str         = String.join( "\n", commands );
        TextProvisioner provisioner = null;
        try ( StringReader reader  = new StringReader( str ); )
        {
            provisioner = TextProvisioner.ofReader( reader );
        }
        catch ( IOException exc )
        {
            fail( "Unexpected IOException", exc );
        }
        return provisioner;
    }
    
    /**
     * Instantiate a TextProvisioner
     * and feed it a CSV command.
     * This is a companion method to {@link #testTypeDefGoRight()}.
     * The command is expected to a well-formed variation
     * of {@code type,default all}.
     * An exception is raised if 
     * the expected result is not obtained.
     * 
     * @param rec   the given CSV command
     */
    private void testTypeDefGoRight( String rec )
    {
        TextProvisioner provisioner = TextProvisioner.of();
        provisioner.addRec( rec );
        List<ShipType2D>    actList = provisioner.getToRegister();
        Set<ShipType2D>     actSet  = new HashSet<>( actList );
        assertEquals( allDefaultTypes, actSet );        
    }
    
    /**
     * Instantiate a TextProvisioner
     * and feed it a CSV command.
     * This is a companion method to {@link #testTypeDefGoRight()}.
     * The command is expected to a well-formed variation
     * of {@code type,name,length,breadth}.
     * The caller passes ShipType2D object
     * that encapsulates the expected result.
     * An exception is raised if 
     * the expected result is not obtained.
     * 
     * @param rec       the command to issue
     * @param expType   representation of the expected result
     */
    private static void testTypeGoRight( String rec, ShipType2D expType )
    {
        TextProvisioner provisioner = TextProvisioner.of();
        provisioner.addRec( rec );
        List<ShipType2D>    actList = provisioner.getToRegister();
        assertEquals( 1, actList.size() );
        assertEquals( expType, actList.get( 0 ) );
    }
    
    /**
     * Instantiate a TextProvisioner
     * and feed it a CSV command.
     * This is a companion method to {@link #testTypeGoWrong()}.
     * The command is expected to a well-formed variation
     * of {@code type,name,length,breadth},
     * <em>except</em> the length and/or breadth arguments
     * are invalid.
     * The caller passes the actual (possibly invalid)
     * length and breadth strings used in the command,
     * which are used to validate the expected error message(s).
     * 
     * @param rec           the command to pass
     * @param strLength     the length argument contained in rec
     * @param strBreadth    the breadth argument contained in rec
     */
    private void testTypeGoWrong( 
        String rec, 
        String strLength, 
        String strBreadth 
    )
    {
        int     iBreadth    = getPositiveInt( strBreadth );
        int     iLength     = getPositiveInt( strLength );
        Integer expLength   = iLength <= 0 ? null : iLength;
        Integer expBreadth  = iBreadth <= 0 ? null : iBreadth;
        
        // Up to three error messages are expected, one for
        // "invalid record," and one or two more for "invalid length"
        // and/or "invalid breadth."
        int     expErrs = 1;
        if ( expLength == null )
            ++expErrs;
        if ( expBreadth == null )
            ++expErrs;
        
        TextProvisioner provisioner = TextProvisioner.of();
        provisioner.addRec( rec );
        List<String>    errors      = provisioner.getErrors();
        assertFalse( provisioner.isSuccess() );
        assertFalse( provisioner.getErrors().isEmpty() );
        assertEquals( expErrs, errors.size() );
        
        String  badRec  = getContainingString( errors, INVALID_P_RECORD );
        assertNotNull( badRec );
        assertContains( badRec, "type" );
        if ( expLength == null )
        {
            String  lengthErr   = getContainingString( errors, INVALID_LENGTH );
            assertNotNull( lengthErr );
            assertTrue( lengthErr.contains( strLength ) );
        }
        if ( expBreadth == null )
        {
            String  breadthErr  = getContainingString( errors, INVALID_BREADTH );
            assertNotNull( breadthErr );
            assertTrue( breadthErr.contains( strBreadth ) );
        }
    }
    
    /**
     * Instantiate a TextProvisioner
     * and feed it a CSV command.
     * This is a companion method to {@link #testDimGoRight()}.
     * The command is expected to a well-formed variation
     * of {@code dim,numRows,numCols}.
     * The caller passes the dimensions encapsulated in rec.
     * An exception is raised if 
     * the expected result is not obtained.
     *
     * @param rec   the CSV command
     * @param dim   the expected dimensions
     */
    private void testDimGoRight( String rec, Dimension dim )
    {
        TextProvisioner provisioner = TextProvisioner.of();
        provisioner.addRec( rec );
        assertTrue( provisioner.isSuccess() );
        assertTrue( provisioner.getErrors().isEmpty() );
        assertEquals( dim.height, provisioner.getRows() );
        assertEquals( dim.width, provisioner.getCols() );
    }
    
    /**
     * Instantiate a TextProvisioner
     * and feed it a CSV command.
     * This is a companion method to {@link #testDimGoWrong()}.
     * The command is expected to a well-formed variation
     * of {@code dim,numRows,numColumns},
     * <em>except</em> the numRows and/or numCols arguments are invalid.
     * The caller passes the actual (possibly invalid)
     * numRows and numCols strings used in the command,
     * which are used to validate the expected error message(s).
     *
     * @param rec       the given CSV command
     * @param strRows   the strRows argument in rec
     * @param strCols   the strCols argument in rec
     */
    private void testDimGoWrong( String rec, String strRows, String strCols )
    {
        int     iCols   = getPositiveInt( strCols );
        int     iRows   = getPositiveInt( strRows );
        Integer expRows = iRows <= 0 ? null : iRows;
        Integer expCols = iCols <= 0 ? null : iCols;
        
        // Expect up to three error messages, one for
        // "invalid record," and one or two more for "invalid rows"
        // and/or "invalid cols."
        int     expErrs = 1;
        if ( expRows == null )
            ++expErrs;
        if ( expCols == null )
            ++expErrs;
        
        TextProvisioner provisioner = TextProvisioner.of();
        provisioner.addRec( rec );
        List<String>    errors      = provisioner.getErrors();
        assertFalse( provisioner.isSuccess() );
        assertFalse( provisioner.getErrors().isEmpty() );
        assertEquals( expRows, provisioner.getRows() );
        assertEquals( expCols, provisioner.getCols() );
        assertEquals( expErrs, errors.size() );
        
        String  badRec  = getContainingString( errors, INVALID_P_RECORD );
        assertNotNull( badRec );
        assertContains( badRec, "dim" );
        if ( expRows == null )
        {
            String  rowErr  = getContainingString( errors, INVALID_ROW_COUNT );
            assertNotNull( rowErr );
            assertTrue( rowErr.contains( strRows ) );
        }
        if ( expCols == null )
        {
            String  colErr  = getContainingString( errors, INVALID_COL_COUNT );
            assertNotNull( colErr );
            assertTrue( colErr.contains( strCols ) );
        }
    }
    
    /**
     * Instantiate a TextProvisioner
     * and feed it a CSV command.
     * This is a companion method to {@link #testDeployGoRight()}.
     * The command is expected to a well-formed variation
     * of {@code deploy,typeName}.
     * The caller passes a ShipType2D that encapsulates
     * the expected result.
     * An exception is raised if 
     * the expected result is not obtained.
     *
     * @param rec       the given command
     * @param expType   encapsulation of the expected result
     */
    private static void testDeployGoRight( String rec, ShipType2D expType )
    {
        TextProvisioner provisioner = TextProvisioner.of();
        
        // the type has to be registered before we can successfully deploy it
        String          expName     = expType.typeName();
        int             expLength   = expType.length();
        int             expBreadth  = expType.breadth();
        String          regRec      =
            listToBasicString( "type", expName, expLength, expBreadth );
        provisioner.addRec( regRec );
        assertTrue( provisioner.isSuccess() );
        assertTrue( provisioner.getErrors().isEmpty() );
        
        provisioner.addRec( rec );
        assertTrue( provisioner.isSuccess() );
        assertTrue( provisioner.getErrors().isEmpty() );
        
        List<ShipType2D>    actList = provisioner.getToDeploy();
        assertEquals( 1, actList.size() );
        assertEquals( expType, actList.get( 0 ) );
    }
    
    /**
     * Instantiate a TextProvisioner
     * and feed it a CSV command.
     * This is a companion method to {@link #testDeployGoWrong()}.
     * The command is expected to a well-formed variation
     * of {@code deploy,typeName},
     * however {@code typeName} is expected <em>not</em>
     * to be present in {@code toBeRegistered} list,
     * leading to an error being declared.
     * The caller passes a ShipType2D that encapsulates
     * the type in the attempted deployment.
     *
     * @param rec       the given command
     * @param expType   encapsulation of the type in the failed deployment
     */
    private static void testDeployGoWrong( String rec, ShipType2D expType )
    {
        TextProvisioner provisioner = TextProvisioner.of();
        
        // operation should fail because type is not registered
        provisioner.addRec( rec );
        List<String>        errors  = provisioner.getErrors();
        assertFalse( provisioner.isSuccess() );
        assertEquals( 2, errors.size() );
        assertTrue( provisioner.getToDeploy().isEmpty() );
        
        String  badRec      = getContainingString( errors, INVALID_P_RECORD );
        assertNotNull( badRec );
        assertContains( badRec, "deploy" );
        
        String  notFound    = getContainingString( errors, SHIP_TYPE_NOT_FOUND );
        assertNotNull( notFound );
        assertContains( notFound, expType.typeName() );
    }
    
    /**
     * Instantiate a TextProvisioner
     * and feed it a CSV command.
     * The command must include at least one field,
     * which is identified via a second the {@code command} argument.
     * The command should begin with a) an unrecognized directive,
     * or b) a valid directive following by the wrong number of fields.
     *
     * @param rec       the given command
     * @param directive the encapsulated directive
     */
    private void testCommandGoWrong( String rec, String directive )
    {
        TextProvisioner provisioner = TextProvisioner.of();
        provisioner.addRec( rec );
        List<String>    errors      = provisioner.getErrors();
        assertFalse( provisioner.isSuccess() );
        assertFalse( provisioner.getErrors().isEmpty() );
        
        String          error       =
            getContainingString( errors, INVALID_P_RECORD );
        assertNotNull( error );
        assertContains( error, directive );
    }
        
    /**
     * Join the given objects
     * into a string of fields separated by commas.
     * 
     * @param objs  the given objects
     * 
     * @return the string of joined objects
     */
    private static String listToBasicString( Object... objs )
    {
        String  str =
            Arrays.stream( objs )
                .map( String::valueOf )
                .collect( Collectors.joining( "," ) );
        return str;
    }
    
    /**
     * Join the given objects
     * into a string of fields separated by commas.
     * Extra whitespace is added at the beginning and end of the string,
     * and around each comma in the list.
     * 
     * @param objs  the given objects
     * 
     * @return the string of joined objects
     */
    private static String listToSpaceyString( Object... objs )
    {
        String  str =
            Arrays.stream( objs )
                .map( String::valueOf )
                .collect( Collectors.joining( "  ,  ", "  ", "  " ) );
        return str;
    }
    
    /**
     * Parse a string containing an integer &ge; 1.
     * If successful, the parsed integer is returned,
     * otherwise -1 is returned.
     *  
     * @param strInt    the string to parse
     * 
     * @return  the parsed integer, or -1 if parsing failed
     */
    private int getPositiveInt( String strInt )
    {
        int     result  = -1;
        try
        {
            result = Integer.parseInt( strInt );
            if ( result < 1 )
                throw new NumberFormatException( "not a positive integer" );
        }
        catch ( NumberFormatException exc )
        {
            result = -1;
        }
        return result;
    }
    
    /**
     * Given two strings,
     * perform a case-insensitive test for containment.
     * 
     * @param container the string container
     * @param contained the string expected to be contained
     */
    private static void assertContains( String container, String contained )
    {
        String  upperContainer  = container.toUpperCase();
        String  upperContained  = contained.toUpperCase();
        assertTrue( upperContainer.contains( upperContained ) );
    }
    
    /**
     * Given list of strings and a target string,
     * return the first string from the list
     * that contains the target string.
     * Returns null if the target is not found.
     * 
     * @param strings       the given list of strings
     * @param targetString  the given target string
     * 
     * @return  the containing string or null if none
     */
    private static String getContainingString( 
        List<String> strings, 
        String targetString
    )
    {
        String  str     = 
            strings.stream()
                .filter( s -> s.contains( targetString ) )
                .findFirst().orElse( null );
        return str;
    }
    
    /**
     * Encapsulate a list of commands,
     * and associated tests for the expected result
     * of issuing the command.
     */
    private class TestList
    {
        private final List<String>                      commands    = 
            new ArrayList<>();
        private final List<Consumer<TextProvisioner>>   testers     = 
            new ArrayList<>();
        private TextProvisioner provisioner;
        
        /**
         * Default constructor, not used.
         */
        private TestList()
        {
            // not used
        }
        
        /**
         * Add a command to the list of commands to be issued,
         * and a tester to validate the result.
         * 
         * @param command   the given command
         * @param tester    the given tester
         */
        public void add( String command, Consumer<TextProvisioner> tester )
        {
            commands.add( command );
            testers.add( tester );
        }
        
        /**
         * Execute the list of commands,
         * obtaining TextProvisioner instance in the process.
         */
        public void execute()
        {
            provisioner = feedReader( commands );
        }
        
        /**
         * Execute the list of tests,
         * applying each to the TextProvisioner
         * obtained in {@link #execute()}.
         */
        public void test()
        {
            testers.stream().forEach( c -> c.accept( provisioner ) );
        }
    }

    /**
     * Encapsulate the list of expected results
     * from issuing commands against a TextProvisioner.
     * The source of the events is not specified.
     * 
     * @param toRegister    
     *      the list of ship types expected to be found in the
     *      provisioner's list of types to register
     *      after execution of the operation
     * @param toDeploy    
     *      the list of ship types expected to be found in the
     *      provisioner's list of types to deploy
     *      after execution of the operation
     * @param rows 
     *      the expected number of grid rows
     *      expected to be encapsulated in the provisioner
     *      after execution of the operation
     * @param cols 
     *      the expected number of grid columns
     *      expected to be encapsulated in the provisioner
     *      after execution of the operation
     * 
     * @see TextProvisionerTest#testOfFile()
     * @see TextProvisionerTest#testOfReaderGoRight()
     */
    private record TestData( 
        Collection<ShipType2D> toRegister,
        Collection<ShipType2D> toDeploy,
        Integer    rows,
        Integer    cols
    )
    {
        public void compare( TextProvisioner actData )
        {
            Set<ShipType2D> expToRegister   = 
                new HashSet<>( toRegister() );
            Set<ShipType2D> actToRegister   = 
                new HashSet<>( actData.getToRegister() );
            
            Set<ShipType2D> expToDeploy     = 
                new HashSet<>( toDeploy() );
            Set<ShipType2D> actToDeploy     = 
                new HashSet<>( actData.getToDeploy() );
            
            assertEquals( expToRegister, actToRegister, "to register" );
            assertEquals( expToDeploy, actToDeploy, "to deploy" );
            assertEquals( rows, actData.getRows() );
            assertEquals( cols, actData.getCols() );
        }
    }
}
