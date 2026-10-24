package com.acmemail.judah.battleship.util;

/**
 * Describes a command 
 * that can be added to an undo/redo stack.
 * 
 * @author Jack
 * 
 * @see UndoRedoManager
 */
public interface Command
{
    /**
     * Execute a command.
     */
    void execute();
    /**
     * Undo a previously executed command.
     */
    void undo();
}
