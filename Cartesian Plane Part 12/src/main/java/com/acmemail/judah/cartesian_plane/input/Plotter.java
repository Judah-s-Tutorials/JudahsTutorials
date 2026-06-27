package com.acmemail.judah.cartesian_plane.input;

import java.awt.geom.Point2D;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Consumer for a stream of {@link Point2D points} produced
 * by an {@link Equation}'s plot methods (YPLOT, XYPLOT, RPLOT, TPLOT).
 * <p>
 * The point stream is provided as a {@link Supplier},
 * not a {@link Stream},
 * so that an implementation may obtain
 * a fresh, unconsumed stream
 * each time one is needed
 * (for example, to repaint after a resize event).
 * Callers must supply a {@code Supplier}
 * that yields a new stream on every invocation.
 * The implementation may invoke the supplier after {@code plot} has
 * returned (e.g. on repaint), so the supplier must remain valid
 * for as long as the plotter is in use.
 *
 * @author Jack Straub
 *
 * @see com.acmemail.judah.cartesian_plane.DefaultPlotter
 * @see CommandProcessor
 */
@FunctionalInterface
public interface Plotter
{
    /**
     * Renders the points obtained from the given supplier.
     * The supplier may be invoked more than once;
     * each invocation must yield a fresh, unconsumed stream.
     * <p>
     * Exceptions thrown by {@code supplier} or by the stream pipeline
     * propagate to the invoker of this method or, for implementations
     * that defer evaluation, to the deferred caller.
     *
     * @param supplier  source of the point stream; must not be null
     *
     * @throws NullPointerException if {@code supplier} is null
     */
    void plot( Supplier<Stream<Point2D>> supplier );
}
