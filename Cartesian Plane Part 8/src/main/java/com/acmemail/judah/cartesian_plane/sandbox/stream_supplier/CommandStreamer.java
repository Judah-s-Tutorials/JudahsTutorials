package com.acmemail.judah.cartesian_plane.sandbox.stream_supplier;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class CommandStreamer
{
    private Supplier<Stream<Command>> streamSupplier = () -> Stream.empty();
        
    public void 
    setStreamSupplier( Supplier<Stream<Command>> supplier )
    {
        if ( supplier == null )
            streamSupplier = () -> Stream.empty();
        else
            streamSupplier = supplier;
    }
    
    public void getAndTraverseStream()
    {
        System.out.println( "*** Begin command stream" );
        Stream<Command> stream  = streamSupplier.get();
        stream.forEach( c -> c.execute() );
        System.out.println( "*** End command stream" );
    }
}
