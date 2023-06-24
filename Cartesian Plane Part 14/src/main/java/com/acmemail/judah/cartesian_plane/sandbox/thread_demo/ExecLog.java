package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ExecLog
{
    private static final List<ExecLog>  execList    = new ArrayList<>();
    
    private final String    className;
    private final String    methodName;
    private final String    paramName;
    private final String    threadName;
    private final LocalTime timestamp;
    
    public ExecLog( String className, String methodName, String objectName )
    {
        super();
        this.className = className;
        this.methodName = methodName;
        this.paramName = objectName;
        this.threadName = Thread.currentThread().getName();
        this.timestamp = LocalTime.now();
    }
    
    public static void add( ExecLog obj )
    {
        execList.add( obj );
    }
    
    public static List<ExecLog> getLog()
    {
        return execList;
    }

    /**
     * @return the className
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * @return the methodName
     */
    public String getMethodName()
    {
        return methodName;
    }

    /**
     * @return the objectName
     */
    public String getParamName()
    {
        return paramName;
    }

    /**
     * @return the threadName
     */
    public String getThreadName()
    {
        return threadName;
    }
    
    public LocalTime getTime()
    {
        return timestamp;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder()
            .append( timestamp ).append( " " )
            .append( "class=").append( className ).append( " " )
            .append( "method=").append( methodName ).append( " " )
            .append( "param=").append( paramName ).append( " " )
            .append( "thread=" ).append( threadName );
        return bldr.toString();
    }
}
