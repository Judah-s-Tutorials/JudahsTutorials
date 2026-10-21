package com.acmemail.judah.battleship.ui.text;

/**
 * This interface defines the common functionality
 * required of both the {@link ConfigPhase}
 * and {@link SetupPhase} classes.
 */
public interface TextPhase
{
    /**
     * Indicates whether this phase was concluded
     * because the operator chose to quit the operation.
     * 
     * @return  true if the operator chose to quit the operation
     */
    boolean isQuit();
    
    /**
     * Start the execution loop,
     * which reads commands from a source
     * and writes feedback to a destination.
     */
    void exec();
}
