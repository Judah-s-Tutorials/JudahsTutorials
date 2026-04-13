package com.acmemail.judah.cartesian_plane.sandbox.stream_supplier;

public interface Command
{
    default void execute()
    {
        System.out.println( this );
    }
}
