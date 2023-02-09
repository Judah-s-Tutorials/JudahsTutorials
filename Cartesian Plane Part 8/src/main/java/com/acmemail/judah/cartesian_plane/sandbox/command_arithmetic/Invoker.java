package com.acmemail.judah.cartesian_plane.sandbox.command_arithmetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Maintains a list of commands.
 * On request each command in the list
 * is executed.
 * <p>
 * This is part of a demonstration
 * of the <em>command pattern;</em>
 * it represents the "command invoker class."
 * </p>
 * 
 * @author Jack Straub
 *
 */
public class Invoker
{
    private final List<OpCommand>   commands    = new ArrayList<>();
    
    public Invoker()
    {
        this( null );
    }
    
    public Invoker( Collection<OpCommand> commands )
    {
        if ( commands != null )
            this.commands.addAll( commands );
    }
    
    public void addCommand( OpCommand command )
    {
        commands.add( command );
    }
    
    public void performOperations()
    {
        for ( OpCommand command : commands )
            command.execute();
    }
}
