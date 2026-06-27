package com.acmemail.judah.cartesian_plane;

import java.awt.geom.Point2D;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.input.Plotter;

/**
 * Implementation of {@link Plotter}
 * that converts a stream of Point2Ds to PlotPointCommands
 * directed to an encapsulated CartesianPlane object.
 * Invoking this method
 * will update and redisplay the encapsulated CartesianPlane object.
 */
public class DefaultPlotter implements Plotter
{
    /** Encapsulated CartesianPlane object. */
    private final CartesianPlane    plane;

    /**
     * Constructor; establishes the encapsulated CartesianPlane object.
     *
     * @param plane
     *      The CartesianPlane object to encapsulate;
     *      must not be null.
     */
    public DefaultPlotter( CartesianPlane plane )
    {
        Objects.requireNonNull( plane, "plane" );
        this.plane = plane;
    }

    @Override
    public void plot( Supplier<Stream<Point2D>> supplier )
    {
        Objects.requireNonNull( supplier, "supplier" );
        plane.setStreamSupplier( () ->
            supplier.get().map( p -> PlotPointCommand.of( p, plane ) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
}
