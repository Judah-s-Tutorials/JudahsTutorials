package com.acmemail.judah.graphics.sandbox;

import javax.swing.JComponent;

/**
 * This is a simple example of running a timer that fires every
 * <em>n</em> milliseconds.
 * Each time it fires it calls the <em>repaint</em> method
 * of a given JComponent (usually a JPanel).
 * 
 * @author Jack Straub
 */
public class AnimationTimer implements Runnable
{
    /** 
     * The JComponent who's repaint method is to be invoked.
     * Provided by the user in the constructor.
     */
    private final JComponent    component;
    /**
     * The length of time to wait between firings.
     * Provided by the user in the constructor.
     */
    private final long          delay;
    /**
     * Thread to execute the timer. Created <em>but not started</em>
     * in the constructor.
     */
    private final Thread		timerThread;
    
    /** 
     * Controls operation of the timer.
     * While true, the timer will continue to fire as scheduled.
     * To set to false, call the stop() method.
     * @see #stop()
     */
    private boolean             execute     = true;
    
    /**
     * Constructor. Instantiates the timer that will control execution,
     * <em>but does not start the timer.</em>
     * To start the timer, call the <em>start</em> method.
     * Also establishes a name for this timer,
     * which may be useful for debugging purposes.
     * 
     * @param component the component who's repaint method is
     *                  to be repeatedly invoked
     * @param name      name for this timer
     * @param delay     delay between firings, in milliseconds
     * 
     * @see #start()
     */
    public AnimationTimer( JComponent component, String name, long delay )
    {
        this.component = component;
        this.delay = delay;
        timerThread = new Thread( this, name );
    }

    /**
     * Method that is invoked when the timer is startede.
     */
    @Override
    public void run()
    {
        while ( execute )
        {
            component.repaint();
            pause();
        }
    }
    
    /**
     * Begins execution of this timer.
     * The first execution of the timer will be scheduled
     * to run immediately.
     * Note that a timer CANNOT be started more than once.
     */
    public void start()
    {
    	// If the thread state is NEW the thread has not been started.
    	// If it's anything else, the thread has been started, and may
    	// not be started again.
    	Thread.State	state	= timerThread.getState();
        if ( state == Thread.State.NEW )
        	timerThread.start();
    }
    
    /**
     * Halts execution of this timer.
     * After the timer is halted, it cannot be restarted.
     */
    public void stop()
    {
        execute = false;
    }
    
    /**
     * Convenience method to put this thread to sleep for the
     * given number of milliseconds.
     * The heart of the operation is "Thread.sleep(),"
     * which is actually pretty simple.
     * The minor complication is that the sleep method
     * might throw an InterruptedException which then
     * has to be caught.
     * If an InterruptedException is thrown
     * it can be safely ignored.
     */
    private void pause()
    {
        try
        {
            Thread.sleep( delay );
        }
        catch ( InterruptedException exc )
        {
            // Don't really care if an InterruptedExeption
            // is thrown, so do nothing.
        }
    }
}
