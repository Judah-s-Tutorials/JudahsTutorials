package com.acmemail.judah.battleship.util;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Implements the undo/redo stacks.
 * 
 * @author Jack
 */
public class UndoRedoManager
{
    /** The undo stack. */
    private final Deque<Command> undoStack = new ArrayDeque<>();
    /** The redo stack. */
    private final Deque<Command> redoStack = new ArrayDeque<>();
    
    /**
     * Default constructor.
     * Declared here for documentation purposes.
     */
    public UndoRedoManager()
    {
    }

    /**
     * Executes a new action.
     * 
     * @param command   encapsulation of the action to execute
     */
    public void executeCommand(Command command)
    {
        command.execute();
        undoStack.push( command );
        redoStack.clear(); // New actions clear redo history
    }

    /**
     * Undo the last executed action.
     */
    public void undo()
    {
        if ( !undoStack.isEmpty() )
        {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push( command ); // Save it to redo stack
        }
    }

    /**
     * Redo the last undone action.
     */
    public void redo()
    {
        if (!redoStack.isEmpty()) 
        {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push( command ); // Put it back on undo stack
        }
    }
    
    /**
     * Return to an initial state.
     */
    public void init()
    {
        undoStack.clear();
        redoStack.clear();
    }
}
