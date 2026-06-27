package com.acmemail.judah.cartesian_plane.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.MessageConsumer;
import com.acmemail.judah.cartesian_plane.input.Plotter;

class CommandExecutorV1bTest
{
    private static final CartesianPlane plane       = new CartesianPlane();
    private static final Root           root        = new Root( plane );
    private static final List<String>   messages    = new ArrayList<>();

    @BeforeAll
    public static void beforeAll() throws IOException
    {
        root.start();
    }

    @BeforeEach
    public void beforeEach() throws IOException
    {
        messages.clear();
    }

    @Test
    void testCommandExecutorV1bPlotter()
    {
        Plotter             plotter = s -> noop();
        CommandExecutorV1b  exec    = new CommandExecutorV1b( plotter );
        assertEquals( plotter, exec.getPlotter() );

        assertThrows( NullPointerException.class,
            () -> new CommandExecutorV1b( (Plotter)null )
        );
    }

    @Test
    void testCommandExecutorV1bCartesianPlane()
    {
        CommandExecutorV1b  exec    = new CommandExecutorV1b( plane );
        assertNotNull( exec );

        assertThrows( NullPointerException.class,
            () -> new CommandExecutorV1b( (CartesianPlane)null )
        );
    }

    @Test
    public void testGetSetMessageConsumer()
    {
        CommandExecutorV1b  exec    = new CommandExecutorV1b( plane );
        MessageConsumer     defMC   = exec.getMessageConsumer();
        MessageConsumer     testMC  = (a,b,c,d) -> noop();
        exec.setMessageConsumer( testMC );
        assertEquals( testMC, exec.getMessageConsumer() );
        exec.setMessageConsumer( null );
        assertEquals( defMC, exec.getMessageConsumer() );
    }

    @Test
    public void testOpenNoop()
    {
        // Open command should be ignored except for a diagnostic message
        exec( List.of( Command.OPEN.toString() ) );
        assertFalse( messages.isEmpty() );
        String  message = messages.get( 0 ).toUpperCase();
        assertTrue( message.contains( message ) );
    }

    @Test
    public void testSaveNoop()
    {
        // Save command should be ignored except for a diagnostic message
        exec( List.of( Command.SAVE.toString() ) );
        assertFalse( messages.isEmpty() );
        String  message = messages.get( 0 ).toUpperCase();
        assertTrue( message.contains( message ) );
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
    public void testInvalidCommand()
    {
        // Test the default case at the end of the switch statement
        // in CommandExecutorV1b.exec.
        List<String>    commands    = List.of( Command.INVALID.toString() );
        // sanity check
        assertTrue( messages.isEmpty() );
        exec( commands );
        assertFalse( messages.isEmpty() );
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
        CommandExecutorV1b      executor    = plotter != null ?
            new CommandExecutorV1b( plotter ) :
            new CommandExecutorV1b( plane ) ;
        executor.setMessageConsumer( (a,b,c,d) -> messages.add( b ) );

        ListIterator<String>    iter        = cmds.listIterator();
        Equation                equation    =
            executor.exec( () ->
                iter.hasNext() ?
                CommandReader.parseCommand( iter.next() ) :
                null
            );
        return equation;
    }

    /**
     * A method that can be invoked,
     * but doesn't do anything.
     * Useful in tests for functional interfaces.
     */
    private static void noop()
    {

    }
}
