package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.math.Polar;

class CommandProcessorTest
{
    private CommandProcessor    proc;

    @BeforeEach
    public void beforeEach()
    {
        proc = new CommandProcessor();
    }

    @Test
    public void testCommandProcessor()
    {
        assertNotNull( proc.getEquation() );
    }

    @Test
    public void testCommandProcessorEquation()
    {
        Equation            equation    = new Exp4jEquation();
        CommandProcessor    processor   = new CommandProcessor( equation );
        assertSame( equation, processor.getEquation() );

        processor = new CommandProcessor( null );
        assertNotNull( processor.getEquation() );
    }

    @Test
    public void testNewEquationWithoutName()
    {
        Equation            oldEquation = proc.getEquation();
        ParsedCommand       equationCmd =
            getParsedCommand( Command.EQUATION );
        CommandProcessor    newProc     = proc.newEquation( equationCmd );
        Equation            newEquation = newProc.getEquation();
        assertNotNull( oldEquation );
        assertNotNull( newEquation );
        assertNotSame( oldEquation, newEquation );
        assertEquals( oldEquation.getClass(), newEquation.getClass() );
        assertTrue( newEquation.getName().isEmpty() );
    }

    @Test
    public void testNewEquationWithName()
    {
        Equation            oldEquation = proc.getEquation();
        String              newName     = "New Equation";
        ParsedCommand       equationCmd =
            getParsedCommand( Command.EQUATION, newName );
        CommandProcessor    newProc     = proc.newEquation( equationCmd );
        Equation            newEquation = newProc.getEquation();
        assertNotNull( oldEquation );
        assertNotNull( newEquation );
        assertNotSame( oldEquation, newEquation );
        assertEquals( oldEquation.getClass(), newEquation.getClass() );
        assertEquals( newName, newEquation.getName() );
    }

    @Test
    public void testNewEquationExceptions()
    {
        ParsedCommand   command = getParsedCommand( Command.NONE );
        assertThrows( IllegalArgumentException.class, () ->
            proc.newEquation( command )
        );
        assertThrows( NullPointerException.class, () ->
            proc.newEquation( null )
        );
    }

    @Test
    public void testNewEquationPropagatesPlotter()
    {
        Equation            equation    = new Exp4jEquation();
        Plotter             plotter     = s -> {};
        CommandProcessor    processor   =
            new CommandProcessor( equation, plotter );
        assertSame( equation, processor.getEquation() );
        assertSame( plotter, processor.getPlotter() );

        ParsedCommand       equationCmd =
            getParsedCommand( Command.EQUATION );
        CommandProcessor    newProc     =
            processor.newEquation( equationCmd );
        Equation            newEquation = newProc.getEquation();
        assertNotSame( equation, newEquation );
        assertSame( plotter, newProc.getPlotter() );
    }

    @Test
    public void testParseExceptions()
    {
        assertThrows( NullPointerException.class, () ->
            proc.processCommand( null )
        );
    }

    @ParameterizedTest
    @ValueSource( strings= { "EXIT","NONE","SELECT","OPEN","SAVE" } )
    public void testProcessCommandNOOP( String strCommand )
    {
        // Note: future changes to CommandProcessor may add support
        // for these commands, which will break this test.
        Command         command = Command.toCommand( strCommand );
        ParsedCommand   parsed  = getParsedCommand( command );
        Result          result  = proc.processCommand( parsed );
        assertTrue( result.success() );
    }

    @ParameterizedTest
    @ValueSource( strings= {"YPLOT","XYPLOT","RPLOT","TPLOT"} )
    public void testProcessPlotCommandsNoPlotter( String strCommand )
    {
        // Plot commands with no configured Plotter should all fail
        Command         command     = Command.toCommand( strCommand );
        ParsedCommand   parsed      = getParsedCommand( command );
        Result          result      = proc.processCommand( parsed );
        assertFalse( result.success() );
        List<String>    messages    = result.messages();
        assertFalse( messages.isEmpty() );
        String          message     = messages.get( 0 ).toUpperCase();
        assertTrue( message.contains( "PLOTTER" ) );
    }

    @ParameterizedTest
    @ValueSource( strings= {"YPLOT","XYPLOT","RPLOT","TPLOT"} )
    public void testProcessPlotCommands( String strCommand )
    {
        Equation        equation    = new Exp4jEquation();
        Command         command     = Command.toCommand( strCommand );
        List<Point2D>   expResult   = switch ( command )
        {
            case YPLOT  -> getYPLOTResult( equation );
            case XYPLOT -> getXYPLOTResult( equation );
            case RPLOT  -> getRPLOTResult( equation );
            case TPLOT  -> getTPLOTResult( equation );
            default     -> {
                fail( "Not a plot command: " + command );
                yield null;
            }
        };
        List<Point2D>   actResult   = getActResult( command, equation );
        assertListsEqual( expResult, actResult );
    }

    @Test
    public void testProcessCommandINVALID()
    {
        Command         command         = Command.INVALID;
        ParsedCommand   parsedCommand   = getParsedCommand( command );
        String          commandStr      = command.toString();
        Result          result          = proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        List<String>    errors          = result.messages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
        assertTrue( errors.get( 0 ).startsWith( commandStr ) );
    }

    @Test
    public void testProcessCommandEQUATION()
    {
        Equation    equation    = proc.getEquation();
        String      newName     = "newName";
        testSetString( Command.EQUATION, newName, equation::getName );
    }


    @Test
    public void testProcessCommandSTART()
    {
        Equation    equation    = proc.getEquation();
        testSetDouble( Command.START, equation::getRangeStart );
    }

    @Test
    public void testProcessCommandEND()
    {
        Equation    equation    = proc.getEquation();
        testSetDouble( Command.END, equation::getRangeEnd );
    }

    @Test
    public void testProcessCommandSTEP()
    {
        Equation    equation    = proc.getEquation();
        testSetDouble( Command.STEP, equation::getRangeStep );
    }

    @Test
    public void testProcessCommandPARAM()
    {
        Equation        equation    = proc.getEquation();
        String          newVal      = "newParamName";
        testSetString( Command.PARAM, newVal, equation::getParamName );
        testSetBadString( Command.PARAM, "%", equation::getParamName );
    }

    @Test
    public void testProcessCommandRADIUS()
    {
        Equation        equation    = proc.getEquation();
        String          newVal      = "newRadiusName";
        testSetString( Command.RADIUS, newVal, equation::getRadiusName );
        testSetBadString( Command.RADIUS, "%", equation::getRadiusName );
    }

    @Test
    public void testProcessCommandTHETA()
    {
        Equation        equation    = proc.getEquation();
        String          newVal      = "newThetaName";
        testSetString( Command.THETA, newVal, equation::getThetaName );
        testSetBadString( Command.THETA, "%", equation::getThetaName );
    }

    @Test
    public void testProcessCommandXEQUALS()
    {
        Equation    equation    = proc.getEquation();
        String      newVal      = "2t + 4";
        testSetString( Command.XEQUALS, newVal, equation::getXExpression );
    }

    @Test
    public void testProcessCommandYEQUALS()
    {
        Equation    equation    = proc.getEquation();
        String      newVal      = "2x + 4";
        testSetString( Command.YEQUALS, newVal, equation::getYExpression );
    }

    @Test
    public void testProcessCommandREQUALS()
    {
        Equation        equation    = proc.getEquation();
        String          newVal      = "a + a + a";
        testSetString( Command.REQUALS, newVal, equation::getRExpression );
    }

    @Test
    public void testProcessCommandTEQUALS()
    {
        Equation        equation    = proc.getEquation();
        String          newVal      = "a + a + a";
        testSetString( Command.TEQUALS, newVal, equation::getTExpression );
    }

    @Test
    public void testNoCommandString()
    {
        // If we pass an invalid command in a ParsedCommand object
        // with no command string, does the error message correctly
        // reflect the stringified ParsedCommand.Command field?
        Command         invCommand      = Command.INVALID;
        String          invCommandStr   = invCommand.toString();
        ParsedCommand   parsedCommand   =
            new ParsedCommand( invCommand, "", "" );
        Result          result          =
            proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        List<String>    errors          = result.messages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
        boolean         diagPresent     =
            errors.stream()
            .anyMatch( s -> s.startsWith( invCommandStr ) );
        assertTrue( diagPresent );
    }

    @Test
    public void testSETNoArg()
    {
        String          var1    = "var1";
        String          val1    = "5";
        String          var2    = "var2";
        String          val2    = "10";
        String          allVars =
            var1 + "=" + val1 + "," + var2 + "=" + val2;
        ParsedCommand   setAll  = getParsedCommand( Command.SET, allVars );
        affirm( proc.processCommand( setAll ) );

        String          output  = getStdout( Command.SET, "" );
        String[]        expStrs = { var1, val1, var2, val2 };
        for ( String str : expStrs )
            assertTrue( output.contains( str ) );
    }

    @ParameterizedTest
    @ValueSource( strings=
        { "p", "p,q", " a, b , c  ,  d  ", "abc", "def" })
    public void testParseVarsWithoutValues( String argStr )
    {
        ParsedCommand   command = getParsedCommand( Command.SET, argStr );
        Result          result  = proc.processCommand( command );
        assertTrue( result.success() );

        String[]        names   = argStr.split( "," );
        for ( String name : names )
        {
            // If its input is the empty string, split( "," )
            // returns a 1-element array consisting of the empty string.
            // Don't try to test that.
            String  vName   = name.trim();
            Equation            equation    = proc.getEquation();
            Optional<Double>    val         = equation.getVar( vName );
            assertTrue( val.isPresent() );
            assertEquals( 0, val.get() );
        }
    }

    @ParameterizedTest
    @ValueSource( strings=
        { "p=5", "p=5,q=6", " a = 5 , b = 6 , c  =  7  ,  d = 8  ",
          "abc = 5 , def = 6 "
        })
    public void testParseVarsWithValues( String str )
    {
        // All test values are assumed to be of the form:
        //     var-name = literal-value
        ParsedCommand   command = getParsedCommand( Command.SET, str );
        Result          result  = proc.processCommand( command );
        assertTrue( result.success() );

        String[]    specs   = str.split( "," );
        for ( String spec : specs )
        {
            Equation    equation    = proc.getEquation();
            String[]    pair        = spec.trim().split( "=", -1 );
            // sanity check
            assertEquals( 2, pair.length );
            String              name    = pair[0].trim();
            String              value   = pair[1].trim();
            double              dValue  = Double.parseDouble( value );
            Optional<Double>    actVal  = equation.getVar( name );
            assertTrue( actVal.isPresent() );
            assertEquals( dValue, actVal.get() );
        }
    }

    @ParameterizedTest
    @ValueSource( strings=
        { "%=5", "5=5,6=6", " 5a = 5 , 6b = 6 , ^c  =  7  ,  ^d = 8  ",
          "abc% = 5 , de%f = 6 "
        })
    public void testParseVarsWithBadNames( String argStr )
    {
        ParsedCommand   parsedCommand   =
            getParsedCommand( Command.SET, argStr );
        Result          result          =
            proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        List<String>        errors      = result.messages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
    }

    @ParameterizedTest
    @ValueSource( strings=
        { "p=.", "p=.,q=%", " a = 5..0 , b = ..6 , c  =  %7  ,  d = 8$  ",
          "abc = 55.x , def = x 6, ghi = 5 5, jkl = 5 6", "M=5 =6", "="
        })
    public void testParseVarsWithBadValues( String argStr )
    {
        ParsedCommand   parsedCommand   =
            getParsedCommand( Command.SET, argStr );
        Result          result          =
            proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        List<String>    errors          = result.messages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
    }

    @Test
    public void testParseVarsGoodAndBadSpecs()
    {
        // p and q are assigned from valid expressions;
        // s is not
        String          strVals         = "p=10,q=10,%,s=5 5";
        ParsedCommand   parsedCommand   =
            getParsedCommand( Command.SET, strVals );
        Result          result          =
            proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        List<String>        errors      = result.messages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );

        Equation            equation    = proc.getEquation();
        Optional<Double>    pVal        = equation.getVar( "p" );
        Optional<Double>    qVal        = equation.getVar( "q" );
        Optional<Double>    sVal        = equation.getVar( "s" );

        // p and q should be stored...
        assertTrue( pVal.isPresent() );
        assertEquals( 10, pVal.get() );
        assertTrue( qVal.isPresent() );
        assertEquals( 10, qVal.get() );

        // ... but s should not
        assertFalse( sVal.isPresent() );
        assertTrue( result.messages().size() > 1 );
    }

    @Test
    public void testSetGoWrong()
    {
        ParsedCommand   cmd     = getParsedCommand( Command.SET, "x=5 =6" );
        Result          result  = proc.processCommand( cmd );
        assertFalse( result.success() );
    }

    private void testSetDouble( Command cmd, DoubleSupplier getter  )
    {
        double          oldVal          = getter.getAsDouble();
        double          newVal          = oldVal + 1;
        String          newValStr       = String.valueOf( newVal );
        ParsedCommand   parsedCommand   = getParsedCommand( cmd, newValStr );
        Result          result          =
            proc.processCommand( parsedCommand );
        assertTrue( result.success() );
        assertEquals( newVal, getter.getAsDouble() );

        // test invalid value
        parsedCommand = getParsedCommand( cmd, "invalidvalue" );
        result = proc.processCommand( parsedCommand );
        assertFalse( result.success() );

        // test no-arg option
        testEmptyArg( cmd, newValStr );
    }

    private void testSetBadString(
        Command          cmd,
        String           badVal,
        Supplier<String> getter
    )
    {
        String          oldVal          = getter.get();
        assertNotEquals( oldVal, badVal );

        ParsedCommand   parsedCommand   = getParsedCommand( cmd, badVal );
        Result          result          =
            proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        assertEquals( oldVal, getter.get() );
    }

    private void testSetString(
        Command          cmd,
        String           newVal,
        Supplier<String> getter
    )
    {
        String          oldVal          = getter.get();
        assertNotEquals( oldVal, newVal );

        ParsedCommand   parsedCommand   = getParsedCommand( cmd, newVal );
        Result          result          =
            proc.processCommand( parsedCommand );
        assertTrue( result.success() );
        assertEquals( newVal, getter.get() );

        // test no-arg option
        testEmptyArg( cmd, newVal );
    }

    /**
     * Test a Command with an empty argument.
     * This is for Commands with two forms:
     * provided a non-empty argument
     * the command sets a field;
     * given an empty argument
     * it prints the value of the field.
     *
     * @param cmd       the command to execute without an argument
     * @param expOutput the expected output from processing the command
     */
    private void testEmptyArg( Command cmd, String expOutput )
    {
        String  actOutput   = getStdout( cmd, "" );
        assertEquals( expOutput, actOutput );
    }

    /**
     * Create a ParsedCommand
     * from a given Command.
     * The command string for the parsed command
     * will be the stringified Command,
     * and the argument string will be the empty string.
     *
     * @param command   the given Command
     *
     * @return  the generated ParsedCommand
     */
    private static ParsedCommand getParsedCommand( Command command )
    {
        ParsedCommand   parsedCommand   = getParsedCommand( command, "" );
        return parsedCommand;
    }

    /**
     * Create a ParsedCommand
     * from a given Command and argument string.
     * The command string for the parsed command
     * will be the stringified Command.
     *
     * @param command   the given Command
     * @param argStr    the given argument string
     *
     * @return  the generated ParsedCommand
     */
    private static ParsedCommand
    getParsedCommand( Command command, String argStr )
    {
        String          commandString   = command.toString();
        ParsedCommand   parsedCommand   =
            new ParsedCommand( command, commandString, argStr );
        return parsedCommand;
    }

    /**
     * This method executes a command
     * and recovers the output sent to stdout.
     * It redirects stdout to a memory buffer,
     * executes the command,
     * recovers the output from the memory buffer,
     * and restores stdout.
     * The recovered output is returned.
     *
     * @param cmd   the command to execute
     * @param arg   the argument to attach to the command
     *
     * @return  the output recovered during command processing
     */
    private String getStdout( Command cmd, String arg )
    {
        ByteArrayOutputStream   baoStream   = new ByteArrayOutputStream();
        PrintStream             printStream = new PrintStream( baoStream );
        PrintStream             stdOut      = System.out;
        try
        {
            System.setOut( printStream );
            ParsedCommand   parsedCommand   = getParsedCommand( cmd, arg );
            proc.processCommand( parsedCommand );
        }
        finally
        {
            System.setOut( stdOut );
        }

        String  str = baoStream.toString().trim();
        return str;
    }

    private static void affirm( Result result )
    {
        if ( !result.success() )
        {
            List<String>    messages    = result.messages();
            String          message     = !messages.isEmpty() ?
                String.join( "; ", messages ) : "no diagnostic reported";
            fail( message );
        }
    }

    private static List<Point2D> getYPLOTResult( Equation equation )
    {
        double          start   = -1;
        double          end     = 1;
        double          step    = .5;
        String          yEquals = "x * x";
        equation.setRange( start, end, step );
        affirm( equation.setYExpression( yEquals ) );

        List<Point2D>   result  =
            DoubleStream.iterate( start, x -> x <= end, x -> x + step )
                .mapToObj( x -> (Point2D)new Point2D.Double( x, x * x ) )
                .toList();
        return result;
    }

    private static List<Point2D> getXYPLOTResult( Equation equation )
    {
        double          start   = 0;
        double          end     = 2 * Math.PI;
        double          step    = .5;
        double          radius  = Math.PI / 4;
        String          xEquals = "r cos(t)";
        String          yEquals = "r sin(t)";
        equation.setRange( start, end, step );
        affirm( equation.setYExpression( yEquals ) );
        affirm( equation.setXExpression( xEquals ) );
        affirm( equation.setVar( "r", radius ) );

        List<Point2D>   result  =
            DoubleStream.iterate( start, t -> t <= end, t -> t + step )
                .mapToObj( t -> (Point2D)new Point2D.Double(
                    radius * Math.cos( t ),
                    radius * Math.sin( t )
                ))
                .toList();
        return result;
    }

    private static List<Point2D> getRPLOTResult( Equation equation )
    {
        double          start   = 0;
        double          end     = 2 * Math.PI;
        double          step    = .5;
        String          rEquals = "1 + cos(t)";
        equation.setRange( start, end, step );
        affirm( equation.setThetaName( "t" ) );
        affirm( equation.setRExpression( rEquals ) );

        List<Point2D>   result  =
            DoubleStream.iterate( start, t -> t <= end, t -> t + step )
                .mapToObj( t -> Polar.ofRTheta( 1 + Math.cos( t ), t ) )
                .map( Polar::toPoint )
                .toList();
        return result;
    }

    private static List<Point2D> getTPLOTResult( Equation equation )
    {
        double          start   = -1;
        double          end     = 1;
        double          step    = .5;
        String          tEquals = "acos(r)";
        equation.setRange( start, end, step );
        affirm( equation.setRadiusName( "r" ) );
        affirm( equation.setTExpression( tEquals ) );

        List<Point2D>   result  =
            DoubleStream.iterate( start, r -> r <= end, r -> r + step )
                .mapToObj( r -> Polar.ofRTheta( r, Math.acos( r ) ) )
                .map( Polar::toPoint )
                .toList();
        return result;
    }

    private List<Point2D> getActResult( Command plot, Equation equation )
    {
        List<Point2D>       actResult   = new ArrayList<>();
        Plotter             plotter     =
            s -> s.get().forEach( actResult::add );
        CommandProcessor    plotProc    =
            new CommandProcessor( equation, plotter );
        ParsedCommand       command     = getParsedCommand( plot );
        Result              result      = plotProc.processCommand( command );
        affirm( result );
        return actResult;
    }

    private static void assertListsEqual(
        List<Point2D> expList,
        List<Point2D> actList
    )
    {
        final double    epsilon = 1e-9;
        int expSize = expList.size();
        assertEquals( expSize, actList.size(), "list size" );
        for ( int inx = 0 ; inx < expSize ; ++inx )
        {
            Point2D expPoint    = expList.get( inx );
            Point2D actPoint    = actList.get( inx );
            assertEquals(
                expPoint.getX(),
                actPoint.getX(),
                epsilon,
                "x[" + inx + "]"
            );
            assertEquals(
                expPoint.getY(),
                actPoint.getY(),
                epsilon,
                "y[" + inx + "]"
            );
        }
    }
}
