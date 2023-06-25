package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the execution log
 * used for this demo.
 * An instance of this class
 * encapsulates the data
 * stored in log entry.
 * There is also a class variable
 * that contains the list of all generated log entries.
 * Each log entry represents
 * the execution of a method
 * under the auspices of a particular thread,
 * or a generic message.
 * <p>
 * The user can add an ActionListener to this class,
 * which will be notified each time
 * a new entry is added to the log.
 * 
 * @author Jack Straub
 * 
 * @see #addActionListener(ActionListener)
 */
public class ExecLog
{
    /** List of all log entries. */
    private static final List<ExecLog>  execList    = new ArrayList<>();
    
    /** List of all ActionListeners. */
    private static final List<ActionListener>   actionListeners = 
        new ArrayList<>();
    
    /**
     * The name of the class
     * containing the method to be executed
     * if this is a log of a method execution; or
     * the message if this is a log
     * of a generic message.
     */
    private final String    field1;
    /** 
     * Method name associated with an execution log; 
     * blank for generic message.
     */
    private final String    methodName;
    /** 
     * Parameter type associated with an execution log; 
     * blank for generic message.
     */
    private final String    paramName;
    /** 
     * The name of the thread
     * that was current
     * at the time the log entry 
     * was created.
     */
    private final String    threadName;
    /** The time of log entry creation. */
    private final LocalTime timestamp;
    
    /**
     * Constructor. 
     * Used to create log entries
     * associated with specific
     * method execution events.
     * 
     * @param className     the name of the class containing
     *                      the executed method
     * @param methodName    the name of the executed method
     * @param objectName    the type of the argument
     *                      passed to the executed method
     */
    public ExecLog( String className, String methodName, String objectName )
    {
        super();
        this.field1 = className;
        this.methodName = methodName;
        this.paramName = objectName;
        this.threadName = Thread.currentThread().getName();
        this.timestamp = LocalTime.now();
    }
    
    /**
     * Constructor.
     * Used to create log entries
     * associated with a given generic message.
     * Unused fields of this object
     * will be set to the empty string.
     * 
     * @param message   the given message
     */
    public ExecLog( String message )
    {
        super();
        this.field1 = message;
        this.methodName = "";
        this.paramName = "";
        this.threadName = Thread.currentThread().getName();
        this.timestamp = LocalTime.now();
    }
    
    /**
     * Adds a listener
     * to be notified
     * when a new entry
     * is added to the execution log.
     *  
     * @param listener  the listener to add
     */
    public static void addActionListener( ActionListener listener )
    {
        actionListeners.add( listener );
    }
    
    /**
     * Removes a given listener
     * from the list 
     * of ActionListeners.
     * If there is no such listener,
     * the operation is silently ignored.
     * 
     * @param listener  the listener to remove
     */
    public static void removeActionListener( ActionListener listener )
    {
        actionListeners.remove( listener );
    }
    
    /**
     * Adds a new entry
     * to the execution log,
     * and notifies all listeners.
     * 
     * @param obj   the entry to be added to the log
     */
    public static synchronized void add( ExecLog obj )
    {
        execList.add( obj );
        ActionEvent evt = new ActionEvent( obj, ActionEvent.ACTION_FIRST, "" );
        actionListeners.forEach( l -> l.actionPerformed( evt ) );
    }
    
    /**
     * Gets the list
     * of all execution log entries.
     * The caller must not 
     * modify the list.
     * 
     * @return  the list of all execution log entries
     */
    public static List<ExecLog> getLog()
    {
        return execList;
    }

    /**
     * Gets the class name encapsulated in this object.
     * Note that the "class name" and "message" properties
     * use the same instance variable.
     * 
     * @return the className
     */
    public String getClassName()
    {
        return field1;
    }

    /**
     * Gets the message encapsulated in this object.
     * Note that the "class name" and "message" properties
     * use the same instance variable.
     * 
     * @return the className
     */
    public String getMessaage()
    {
        return field1;
    }

    /**
     * Gets the name of the method
     * associated with an execution event.
     * 
     * @return the method name
     */
    public String getMethodName()
    {
        return methodName;
    }

    /**
     * Gets the parameter type
     * associated with an execution event.
     * 
     * @return the parameter type
     */
    public String getParamName()
    {
        return paramName;
    }

    /**
     * Gets the name of the thread
     * associated with an execution event.
     * 
     * @return the thread name
     */
    public String getThreadName()
    {
        return threadName;
    }
    
    /**
     * Gets the time stamp
     * associated with an execution event.
     * 
     * @return the time stamp
     */
    public LocalTime getTime()
    {
        return timestamp;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder()
            .append( timestamp ).append( " " )
            .append( "class=").append( field1 ).append( " " )
            .append( "method=").append( methodName ).append( " " )
            .append( "param=").append( paramName ).append( " " )
            .append( "thread=" ).append( threadName );
        return bldr.toString();
    }
}
