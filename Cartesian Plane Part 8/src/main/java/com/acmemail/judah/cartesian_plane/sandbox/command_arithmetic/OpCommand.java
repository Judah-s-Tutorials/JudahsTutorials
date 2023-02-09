package com.acmemail.judah.cartesian_plane.sandbox.command_arithmetic;

/**
 * Identifies a command capable of executing 
 * an arithmetic operation.
 * <p>
 * This is part of a demonstration
 * of the <em>command pattern;</em>
 * it represents the "command interface."
 * </p>
 * 
 * @author Jack Straub
 */
public interface OpCommand
{
    /**
     * Executes the operation.
     */
    void execute();
}
