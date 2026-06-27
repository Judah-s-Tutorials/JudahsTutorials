package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultPlotterTest
{
    private CapturingPlane              plane;
    private DefaultPlotter              plotter;
    private final AtomicInteger         redrawCount = new AtomicInteger();
    private final NotificationListener  listener    =
        e -> redrawCount.incrementAndGet();

    @BeforeEach
    void setUp()
    {
        plane   = new CapturingPlane();
        plotter = new DefaultPlotter( plane );
        redrawCount.set( 0 );
        NotificationManager.INSTANCE
            .addNotificationListener( CPConstants.REDRAW_NP, listener );
    }

    @AfterEach
    void tearDown()
    {
        NotificationManager.INSTANCE.removeNotificationListener( listener );
    }

    @Test
    void testConstructor_NullPlane()
    {
        assertThrows( NullPointerException.class,
            () -> new DefaultPlotter( null ) );
    }

    @Test
    void testConstructor_ValidPlane()
    {
        // setUp constructed plotter without throwing
        assertNotNull( plotter );
    }

    @Test
    void testPlot_NullSupplier()
    {
        assertThrows( NullPointerException.class,
            () -> plotter.plot( null ) );
    }

    @Test
    void testPlot_InstallsSupplier()
    {
        plotter.plot( Stream::empty );
        assertNotNull( plane.captured );
    }

    @Test
    void testPlot_PropagatesPointsAsCommands()
    {
        List<Point2D>   points  = List.of(
            new Point2D.Double( 1.0, 2.0 ),
            new Point2D.Double( -3.5, 4.25 )
        );

        plotter.plot( points::stream );

        // Drain the installed stream and execute each command;
        // execute() calls plane.plotPoint(x,y), which the spy records.
        List<PlotCommand>   cmds    = plane.captured.get().toList();
        assertEquals( 2, cmds.size() );
        cmds.forEach( PlotCommand::execute );

        List<Point2D>   expected    = List.of(
            new Point2D.Float( 1.0f, 2.0f ),
            new Point2D.Float( -3.5f, 4.25f )
        );
        assertEquals( expected, plane.plottedPoints );
    }

    @Test
    void testPlot_FiresRedrawNotification()
    {
        plotter.plot( Stream::empty );
        assertEquals( 1, redrawCount.get() );
    }

    @Test
    void testPlot_SupplierMayBeInvokedMultipleTimes()
    {
        AtomicInteger   calls   = new AtomicInteger();
        plotter.plot( () -> {
            calls.incrementAndGet();
            return Stream.of( new Point2D.Double( 0, 0 ) );
        });

        // Simulate two repaints by re-pulling from the installed supplier.
        plane.captured.get().count();
        plane.captured.get().count();

        assertEquals( 2, calls.get() );
    }

    @Test
    void testPlot_EmptyPointStream()
    {
        plotter.plot( Stream::empty );
        assertEquals( 0, plane.captured.get().count() );
        assertEquals( 1, redrawCount.get() );
    }

    /**
     * CartesianPlane spy that intercepts setStreamSupplier (to capture
     * what DefaultPlotter installs) and plotPoint (so executed
     * PlotPointCommands record their coordinates here for assertion).
     * Both overrides intentionally skip super to avoid Swing side effects.
     */
    private static class CapturingPlane extends CartesianPlane
    {
        Supplier<Stream<PlotCommand>>   captured;
        final List<Point2D>             plottedPoints = new ArrayList<>();

        @Override
        public void setStreamSupplier( Supplier<Stream<PlotCommand>> supplier )
        {
            captured = supplier;
        }

        @Override
        public void plotPoint( float xco, float yco )
        {
            plottedPoints.add( new Point2D.Float( xco, yco ) );
        }
    }
}
