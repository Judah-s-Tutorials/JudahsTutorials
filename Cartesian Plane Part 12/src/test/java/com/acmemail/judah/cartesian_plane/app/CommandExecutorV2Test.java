package com.acmemail.judah.cartesian_plane.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.EquationFileChooser;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.input.IEquationFileChooser;
import com.acmemail.judah.cartesian_plane.input.MessageConsumer;
import com.acmemail.judah.cartesian_plane.input.Plotter;

class CommandExecutorV2Test
{
    @TempDir
    private static Path                 tempRoot;

    private static final String         readName        = "readFile.txt";
    private static       Path           readPath;
    private static final Double         readStart       = -2.;
    private static final Double         readEnd         = 2.;
    private static final Double         readStep        = .5;
    private static final Map<Command,Double>  readMap =
        Map.of(
            Command.START, readStart,
            Command.END, readEnd,
            Command.STEP, readStep
        );
    private static final List<String>   readCommands    =
        toCommandList( readMap );

    private static final String         saveName        = "saveFile.txt";
    private static       Path           savePath;
    private static final Double         saveStart       = readStart + 1;
    private static final Double         saveEnd         = readEnd + 1;
    private static final Double         saveStep        = readStep + .5;
    private static final Map<Command,Double>    saveMap =
        Map.of(
            Command.START, saveStart,
            Command.END, saveEnd,
            Command.STEP, saveStep
        );
    private static final List<String>   saveCommands    =
        toCommandList( saveMap );

    private static final String         adHocName       = "adHoc.txt";
    private static       Path           adHocPath;

    private static Path                 invalidPath;

    private static final String         noSuchFileName  = "NoSuchFile.txt";
    private static Path                 noSuchFilePath;

    private static final CartesianPlane plane       = new CartesianPlane();
    private static final Root           root        = new Root( plane );
    private static final List<String>   messages    = new ArrayList<>();

    @BeforeAll
    public static void beforeAll() throws IOException
    {
        root.start();
        readPath = tempRoot.resolve( readName );
        savePath = tempRoot.resolve( saveName );
        adHocPath = tempRoot.resolve( adHocName );
        invalidPath = tempRoot.resolve( "/a/b/c.txt" );
        noSuchFilePath = tempRoot.resolve( noSuchFileName );

        write( readPath, readCommands );
    }

    @BeforeEach
    public void beforeEach() throws IOException
    {
        Files.deleteIfExists( noSuchFilePath );
        Files.deleteIfExists( adHocPath );
        messages.clear();
    }

    @Test
    public void testCommandExecutorV2CartesianPlane()
    {
        CommandExecutorV2 exec    = new CommandExecutorV2( plane );
        assertNotNull( exec );

        assertThrows( NullPointerException.class,
            () -> new CommandExecutorV2( (CartesianPlane)null )
        );
    }

    @Test
    public void testCommandExecutorPlotter()
    {
        Plotter             plotter = s -> noop();
        CommandExecutorV2 exec    = new CommandExecutorV2( plotter );
        assertNotNull( exec );

        assertThrows( NullPointerException.class,
            () -> new CommandExecutorV2( (Plotter)null )
        );
    }

    @Test
    public void testGetSetMessageConsumer()
    {
        CommandExecutorV2   exec    = new CommandExecutorV2( plane );
        MessageConsumer     defMC   = exec.getMessageConsumer();
        MessageConsumer     testMC  = (a,b,c,d) -> noop();
        exec.setMessageConsumer( testMC );
        assertEquals( testMC, exec.getMessageConsumer() );
        exec.setMessageConsumer( null );
        assertEquals( defMC, exec.getMessageConsumer() );

        // Verify MessageConsumer propagates to EquationFileChooser
        TFileChooser    fileChooser = new TFileChooser();
        exec.setEquationFileChooser( fileChooser );
        exec.setMessageConsumer( testMC );
        assertEquals( testMC, fileChooser.getMessageConsumer() );
    }

    @Test
    public void testGetSetEquationFileChooser()
    {
        CommandExecutorV2       exec        = new CommandExecutorV2( plane );
        IEquationFileChooser    expChooser  = new TFileChooser();
        exec.setEquationFileChooser( expChooser );
        IEquationFileChooser    actChooser  = exec.getEquationFileChooser();
        assertEquals( expChooser, actChooser );
        exec.setEquationFileChooser( null );
        actChooser  = exec.getEquationFileChooser();
        assertNotNull( actChooser );
        assertTrue( actChooser instanceof EquationFileChooser );
    }

    @Test
    public void testInvalidCommand()
    {
        // Test the default case at the end of the switch statement
        // in CommandExecutorV2.exec.
        List<String>    commands    = List.of( Command.INVALID.toString() );
        // sanity check
        assertTrue( messages.isEmpty() );
        exec( commands );
        assertFalse( messages.isEmpty() );
    }

    @Test
    public void testExecMisc()
    {
        final Map<String,Double>    expVarMap       = getExpVarMap();
        final Map<Command,String>   expStringMap    = getExpStringMap();
        final Map<Command,Double>   expDoubleMap    = getExpDoubleMap();

        List<String>        cmds    = new ArrayList<>();
        setVars( expVarMap, cmds );
        setStrings( expStringMap, cmds );
        setDoubles( expDoubleMap, cmds );

        Equation    equation    = exec( cmds );
        validateVars( expVarMap, equation );
        validateStrings( expStringMap, equation );
        validateDoubles( expDoubleMap, equation );
    }

    @Test
    public void testExecYPLOT()
    {
        double  start   = -10;
        double  end     = 10;
        double  step    = 1;
        List<Point2D>   expPoints   =
            DoubleStream.iterate( start, x -> x <= end, x -> x + step )
                .mapToObj( x -> new Point2D.Double( x, x * x ) )
                .map( p -> (Point2D)p )
                .toList();
        List<String>    cmds    =
            List.of(
                Command.START + " " + start,
                Command.END + " " + end,
                Command.STEP + " " + step,
                Command.YEQUALS + " x^2",
                Command.YPLOT.toString()
            );

        List<Point2D>   actPoints   = new ArrayList<>();
        Plotter         plotter     = s -> s.get().forEach( actPoints::add );
        exec( cmds, plotter );
        assertEquals( expPoints, actPoints );
    }

    @Test
    public void testOpenGoRightWithoutArg()
    {
        Equation        expEquation = new Exp4jEquation();
        TFileChooser    chooser     =
            new TFileChooser( Optional.of( expEquation ), true );
        List<String>    cmds        =
            List.of( "OPEN", "EXIT" );
        Equation        actEquation = exec( cmds, null, chooser );
        assertSame( expEquation, actEquation );
    }

    @Test
    public void testOpenGoWrongWithoutArg()
    {
        TFileChooser    chooser     =
            new TFileChooser( Optional.empty(), true );
        List<String>    cmds        =
            List.of( "OPEN", "EXIT" );
        exec( cmds, null, chooser );
        assertTrue( chooser.getOpenResult().isEmpty() );
    }

    @Test
    public void testOpenGoWrongWithArg()
    {
        String  openCommand = Command.OPEN + " " + noSuchFilePath;
        List<String>    cmds        =
            List.of( openCommand, "EXIT" );
        // Sanity check
        assertTrue( messages.isEmpty() );
        exec( cmds, null );
        assertFalse( messages.isEmpty() );
    }

    @Test
    public void testOpenGoWrongWithArgLoadFailure()
    {
        try
        {
            write( adHocPath, List.of( "NotACommand" ) );
            String  openCommand = Command.OPEN + " " + adHocPath;
            List<String>    cmds        =
                List.of( openCommand, "EXIT" );
            // Sanity check
            assertTrue( messages.isEmpty() );
            exec( cmds, null );
            assertFalse( messages.isEmpty() );
        }
        catch ( IOException exc )
        {
            String  message = "Unexpect IOException: " + exc.getMessage();
            fail( message );
        }
    }

    @Test
    public void testOpenGoRightWithArg()
    {
        List<String>    commands    = List.of( Command.OPEN + " " + readPath );
        Equation    equation    = exec( commands );
        validate( readMap, equation );
    }

    @Test
    public void testSaveGoRightWithArg()
    {
        List<String>    saveList    = new ArrayList<>( saveCommands );
        String          saveCommand = Command.SAVE + " " + savePath;
        saveList.add( saveCommand );
        exec( saveList );

        String          openCommand = Command.OPEN + " " + savePath;
        List<String>    openList    =
            List.of( openCommand, Command.EXIT.toString() );
        Equation        equation    = exec( openList );
        validate( saveMap, equation );
    }

    @Test
    public void testSaveGoRightWithoutArg()
    {
        TFileChooser    chooser     =
            new TFileChooser( null, true );
        List<String>    cmds        =
            List.of( "SAVE", "EXIT" );
        exec( cmds, null, chooser );
        assertTrue( messages.isEmpty() );
    }

    @Test
    public void testSaveGoWrongWithoutArg()
    {
        TFileChooser    chooser     = new TFileChooser( null, false );
        List<String>    cmds        = List.of( "SAVE", "EXIT" );
        exec( cmds, null, chooser );
        assertFalse( messages.isEmpty() );
    }

    @Test
    public void testSaveGoWrongWithArg()
    {
        String          saveCommand = Command.SAVE + " " + invalidPath;
        List<String>    saveList    =
            List.of( saveCommand, Command.EXIT.toString() );
        exec( saveList );
        assertFalse( messages.isEmpty() );
    }

    private static Map<String,Double> getExpVarMap()
    {
        final Map<String,Double>    expVarMap   =
            Map.of(
                "a", 10.,
                "b", 20.,
                "c", 30.,
                "d", -40.
            );
        return expVarMap;
    }

    private static void setVars( Map<String,Double> map, List<String> list )
    {
        map.entrySet().stream()
            .map( e -> "set " + e.getKey() + "=" + e.getValue() )
            .forEach(  list::add );
    }

    private static void
    validateVars( Map<String,Double> expVarMap, Equation equation )
    {
        Map<String,Double>  actVarMap   = equation.getVars();
        expVarMap.entrySet().forEach( e -> {
            String  key     = e.getKey();
            Double  expVal  = e.getValue();
            Double  actVal  = actVarMap.get( key );
            assertNotNull( actVal, key );
            assertEquals( expVal, actVal, key );
        });
    }

    private static Map<Command,String> getExpStringMap()
    {
        final Map<Command,String>    expStringMap   =
            Map.of(
                Command.PARAM, "param",
                Command.THETA, "theta",
                Command.RADIUS, "radius",
                Command.YEQUALS, "x^2",
                Command.XEQUALS, "3 * cos(a * t)",
                Command.REQUALS, "tan(17t) + cot(17t)"
            );
        return expStringMap;
    }

    private static void
    setStrings( Map<Command,String> map, List<String> list )
    {
        map.entrySet().stream()
        .map( e -> e.getKey() + " " + e.getValue() )
        .forEach(  list::add );
    }

    private static void
    validateStrings( Map<Command,String> expVarMap, Equation equation )
    {
        expVarMap.entrySet().forEach( e -> {
            Supplier<String>    supplier;
            Command             command     = e.getKey();
            switch ( command )
            {
            case PARAM -> supplier = equation::getParamName;
            case THETA -> supplier = equation::getThetaName;
            case RADIUS -> supplier = equation::getRadiusName;
            case YEQUALS -> supplier = equation::getYExpression;
            case XEQUALS -> supplier = equation::getXExpression;
            case TEQUALS -> supplier = equation::getTExpression;
            case REQUALS -> supplier = equation::getRExpression;
            default -> supplier = fail( command + " not configured as string" );
            }
            String  expStr  = supplier.get();
            String  actStr  = e.getValue();
            assertEquals( expStr, actStr );
        });
    }

    private static Map<Command,Double> getExpDoubleMap()
    {
        final Map<Command,Double>    expMap =
            Map.of(
                Command.START, 10.,
                Command.END, 20.,
                Command.STEP, 1.
            );
        return expMap;
    }

    private static void
    setDoubles( Map<Command,Double> map, List<String> list )
    {
        map.entrySet().stream()
        .map( e -> e.getKey() + " " + e.getValue() )
        .forEach(  list::add );
    }

    private static void
    validateDoubles( Map<Command,Double> expVarMap, Equation equation )
    {
        expVarMap.entrySet().forEach( e -> {
            Supplier<Double>    supplier;
            Command             command     = e.getKey();
            switch ( command )
            {
            case START -> supplier = equation::getRangeStart;
            case END -> supplier = equation::getRangeEnd;
            case STEP -> supplier = equation::getRangeStep;
            default -> supplier = fail( command + " not configured as double" );
            }
            Double  expVal  = supplier.get();
            Double  actVal  = e.getValue();
            assertEquals( expVal, actVal );
        });
    }

    private static Equation exec( List<String> cmds )
    {
        Equation    equation    = exec( cmds, null );
        return equation;
    }

    private static Equation exec( List<String> cmds, Plotter plotter )
    {
        Equation    equation    = exec( cmds, plotter, null );
        return equation;
    }

    private static Equation exec(
        List<String> cmds,
        Plotter plotter,
        IEquationFileChooser chooser
    )
    {
        CommandExecutorV2     executor    = plotter != null ?
            new CommandExecutorV2( plotter ) :
            new CommandExecutorV2( plane ) ;
        executor.setMessageConsumer( (a,b,c,d) -> messages.add( b ) );
        if ( chooser != null )
            executor.setEquationFileChooser( chooser );
        ListIterator<String>    iter        = cmds.listIterator();
        Equation                equation    =
            executor.exec( () ->
                iter.hasNext() ?
                CommandReader.parseCommand( iter.next() ) :
                null
            );
        return equation;
    }

    private static List<String>
    toCommandList( Map<Command, ? extends Object> commandMap )
    {
        List<String>    list    =
            commandMap.entrySet().stream()
                .map( e -> e.getKey() + " " + e.getValue() )
                .toList();
        return list;
    }

    private static void
    validate( Map<Command, ? extends Object> map, Equation equation )
    {
        map.entrySet().stream().forEach( e -> {
            Command command = e.getKey();
            Object  value   = e.getValue();
            switch ( command )
            {
            case START -> assertEquals( value, equation.getRangeStart() );
            case END -> assertEquals( value, equation.getRangeEnd() );
            case STEP -> assertEquals( value, equation.getRangeStep() );
            case SAVE, OPEN, EXIT -> {}
            default -> fail( command + " not configured for testing" );
            }
        });
    }

    /**
     * A method that can be invoked,
     * but doesn't do anything.
     * Useful in tests for functional interfaces.
     */
    private static void noop()
    {

    }

    private static void write( Path path, List<String> lines )
        throws IOException
    {
        try (
            BufferedWriter bWriter = Files.newBufferedWriter( path );
            PrintWriter pWriter = new PrintWriter( bWriter );
        )
        {
            lines.forEach( pWriter::println );
        }
    }

    private static class TFileChooser implements IEquationFileChooser
    {
        private Optional<Equation>  openResult;
        private boolean             saveResult;
        private MessageConsumer     messageConsumer;

        public TFileChooser()
        {
            this( Optional.empty(), false );
        }

        public TFileChooser( Optional<Equation> openResult, boolean saveResult )
        {
            this.openResult = openResult;
            this.saveResult = saveResult;
        }

        @Override
        public Optional<Equation> openDialog()
        {
            return openResult;
        }

        @Override
        public boolean saveDialog(Equation equation)
        {
            if ( !saveResult )
                messages.add( "save failure" );
            return saveResult;
        }

        @Override
        public void setMessageConsumer( MessageConsumer consumer )
        {
            messageConsumer = consumer;
        }

        /**
         * @return the openResult
         */
        public Optional<Equation> getOpenResult()
        {
            return openResult;
        }

        public MessageConsumer getMessageConsumer()
        {
            return messageConsumer;
        }
    }
}
