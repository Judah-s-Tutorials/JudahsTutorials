package com.acmemail.judah.cartesian_plane;

import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Plotter
{
    void setStreamSupplier( Supplier<Stream<PlotCommand>> supplier );
}
