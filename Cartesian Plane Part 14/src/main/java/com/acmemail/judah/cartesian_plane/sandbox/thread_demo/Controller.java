package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Program to demonstrate how the same method
 * can be executed in different threads.
 * Three lists are displayed,
 * one for classes,
 * one for parameter types
 * and one for threads.
 * Each class has a class method
 * which requires an argument
 * of the parameter type.
 * Pushing the execute button
 * will cause the selected thread
 * to call the method in the selected class
 * passing a parameter of the selected type.
 * All activity will be logged
 * in a separate window.
 * <p>
 * When the operator pushes the exit button
 * the demo will terminate.
 * The application 
 * will continue to display the execution log
 * until the operator dismisses
 * one of the application windows.
 * 
 * @author Jack Straub
 */
public class Controller
{
    /**
     * List of classes that will used
     * to construct the GUI.
     * Each class in the list
     * will be used to create a toggle button
     * in both the <em>class</em> and <em>class</em> 
     * displayed lists.
     */
    private final List<Class<?>>        classes         = new ArrayList<>();
    /** Maps a class name to an instance of that class. */
    private final Map<String,Element>   instanceMap     = new HashMap<>();
    /** Maps a thread name to a Thread object. */
    private final Map<String,Thread>    threadMap       = new HashMap<>();
    /** Radio button group for the displayed class list. */
    private final ButtonGroup           classGroup      = new ButtonGroup();
    /** Radio button group for the displayed instance list. */
    private final ButtonGroup           instanceGroup   = new ButtonGroup();
    /** Radio button group for the displayed thread list. */
    private final ButtonGroup           threadGroup     = new ButtonGroup();
    
    /** 
     * Represents the class selected from the displayed class list.
     * This is the class that encapsulates the method
     * that will be called when the execute button is pushed.
     */
    private Element     selectedClass;
    /** 
     * The parameter selected from the displayed parameter list.
     * This is the object that will be passed to the chosen method
     * when the execute button is pushed.
     */
    private Element     selectedParam;
    /** Reference to the object that encapsulates the log display. */
    private LogDisplay  logDisplay      = new LogDisplay();
    
    /**
     * When true, demonstration is in progress.
     * Once false, all threads should exit.
     */
    private boolean     inProgress      = true;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        Controller  controller  = new Controller();
        SwingUtilities.invokeLater( () -> controller.build() );
    }
    
    /**
     * Constructor.
     */
    public Controller()
    {
        // Establish the list of classes that will drive this demonstration.
        // An instance of each class will be added to the instanceMap, using
        // the class name as a key.
        classes.add( Hydrogen.class );
        classes.add( Helium.class );
        classes.add( Lithium.class );
        classes.add( Berylium.class );
        
        // Variable convenient for use in invoking the 
        // Class.getConstructor(Class[]) method.
        Class<?>[]  dummyParam  = new Class<?>[0];  
        try
        {
            // For each class in the list of classes:
            // 1. Get a constructor.
            // 2. Get an instance of the class.
            // 3. Add the instance to the instanceMap.
            for ( Class<?> clazz : classes )
            {
                Constructor<?>  ctor        = 
                    clazz.getConstructor( dummyParam );
                Element         element     = (Element)ctor.newInstance();
                String          className   = element.getName();
                instanceMap.put( className, element );
            }
        }
        catch ( 
            InvocationTargetException 
            | NoSuchMethodException
            | SecurityException
            | IllegalAccessException
            | IllegalArgumentException
            | InstantiationException exc 
        )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        // Display the execution log.
        SwingUtilities.invokeLater( () -> logDisplay.build() );
    }
    
    /**
     * Constructs the Controller GUI.
     */
    public void build()
    {
        JFrame  frame   = new JFrame( "Thread Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( new ContentPane() );
        
        // Position the upper-left corner of this frame to the
        // right and below the upper-left corner of the log display.
        Point   startPos    = logDisplay.getPos();
        if ( startPos != null ) 
        {
            startPos.x += 200;
            startPos.y += 200;
            frame.setLocation( startPos );
        }
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Runnable that drives each of the threads in the demo.
     * Each thread sleeps until it is deliberately woken.
     * Then it executes the method from the selected class,
     * passing an argument of the selected parameter type.
     */
    public void doRun()
    {
        // The object that encapsulates this thread. Used as a
        // mutex for controlling thread execution.
        Thread  thread  = Thread.currentThread();
        while ( inProgress )
        {
            // "Seize" the mutex before waiting on it. This is
            // a requirement of the Thread API.
            synchronized( thread )
            {
                ExecLog.add( new ExecLog( "WAITING" ) );
                try
                {
                    thread.wait();
                }
                catch ( InterruptedException exc )
                {
                }
            }
            if ( inProgress )
            {
                if ( selectedClass == null || selectedParam == null )
                    throw new RuntimeException( "invalid state" );
                ExecLog.add( new ExecLog( "WAKING" ) );
                selectedClass.getFunk().accept( selectedParam );
            }
        }
        ExecLog.add( new ExecLog( "TERMINATING" ) );
    }
    
    /**
     * Processes an action event
     * raised by the application's
     * exit button.
     * Marks the application as shutting down;
     * wakes all threads;
     * waits for all threads to terminate.
     * 
     * @param event encapsulates the ActionEvent being processed; not used
     */
    private void exit( ActionEvent event )
    {
        // Indicate shutdown in progress.
        inProgress = false;
        
        Collection<Thread> threads  = threadMap.values();
        // Wake all threads so they an see the new state of inProgress.
        System.out.println( "Waking" );
        threads.stream().forEach( this::notify );
        
        // Wait for all threads to complete.
        System.out.println( "Waiting" );
        threads.stream().forEach( this::join );
        
        System.out.println( "Done" );

    }
    
    /**
     * Synchronizes on the given Thread object,
     * then signals it, 
     * all threads waiting on it.
     * 
     * @param thread    the given Thread object
     */
    private void notify( Thread thread )
    {
        synchronized ( thread )
        {
            thread.notify();
        }
    }
    
    /**
     * Joins the given thread,
     * catching InterruptedException, if necessary.
     * If InterrupteException is raised,
     * prints a diagnostic
     * and terminates the application.
     * 
     * @param thread    the given thread
     */
    private void join( Thread thread )
    {
        try
        {
            thread.join();
        }
        catch ( InterruptedException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    /**
     * Action method to listen for 
     * events propagated by
     * pushing  the execute button.
     * 
     * @param evt   object associated with the Action event.
     */
    private void exec( ActionEvent evt )
    {
        Object   source = evt.getSource();
        if ( !(source instanceof JButton ) )
        {
            String  err = "Invalid object: " + source.getClass().getName();
            throw new RuntimeException( err  );
        }
        String  classText   = getSelection( classGroup ).getText();
        String  paramText   = getSelection( instanceGroup ).getText();
        String  threadText  = getSelection( threadGroup ).getText();
        
        selectedClass = instanceMap.get( classText );
        selectedParam = instanceMap.get( paramText );
        
        // This is the mutex that the selected thread is waiting on.
        // It has to be notified to wake the thread, but first it
        // has to be seized; this is a requirement of the Thread API.
        Thread  thread      = threadMap.get( threadText );
        synchronized ( thread )
        {
            thread.notify();
        }
    }
    
    /**
     * Finds the currently selected radio button
     * in the given button group.
     * 
     * @param group the given button group
     * 
     * @return the currently selected button in the given button group
     * 
     * @throws RuntimeException
     *      if the given group has no selected button
     */
    private static AbstractButton getSelection( ButtonGroup group )
    {
        AbstractButton  selected    =
            Collections.list( group.getElements() )
            .stream()
            .filter( e -> e.isSelected() )
            .findFirst()
            .orElse( null );
        if ( selected == null )
            throw new RuntimeException( "no button selectd in group" );
        return selected;
    }
    
    /**
     * Encapsulates the content pane
     * to be set in the frame
     * that encapsulates this GUI.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private class ContentPane extends JPanel
    {
        /**
         * Constructor.
         * Establishes a BorderLayout
         * as the layout manager 
         * for the JPanel.
         */
        public ContentPane()
        {
            super( new BorderLayout() );
            
            // Add the execute and exit buttons to the 
            // southern region of the JPanel.
            add( new ButtonPanel(), BorderLayout.SOUTH );
            
            // Add the class, parameter and thread lists
            // to the center region of this JPanel.
            JPanel  center  = new JPanel( new GridLayout( 1, 3 ) );
            Border  border  = 
                BorderFactory.createEmptyBorder( 10, 15, 10, 15 );
            center.setBorder( border );
            center.add( new ClassGroup() );
            center.add( new InstanceGroup() );
            center.add( new ThreadGroup() );
            add( center, BorderLayout.CENTER );
        }
    }
    
    /**
     * Encapsulates the list of classes
     * to be displayed in this GUI.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private class ClassGroup extends JPanel
    {
        /**
         * Constructor.
         */
        public ClassGroup()
        {
            // Establishes a GridLayout with n rows and 1 column
            // for this JPanel, where n is the length of the class list.
            super( new GridLayout( classes.size(), 1 ) );
            for ( String className : instanceMap.keySet() )
            {
                JRadioButton    button      = new JRadioButton( className );
                classGroup.add( button );
                add( button );
            }
            
            // Select the radio first button of the group.
            Enumeration<AbstractButton> buttons = classGroup.getElements();
            buttons.nextElement().setSelected( true );
            
            Border  border  = BorderFactory.createLineBorder( Color.BLACK );
            Border  titled  = 
                BorderFactory.createTitledBorder( border, "Class" );
            setBorder( titled );
        }
    }
    
    /**
     * Encapsulates the list of parameter types
     * to be displayed in this GUI.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private class InstanceGroup extends JPanel
    {
        /**
         * Constructor.
         */
        public InstanceGroup()
        {
            super( new GridLayout( instanceMap.size(), 1 ) );
            for ( Element ele : instanceMap.values() )
            {
                JRadioButton    button  = new JRadioButton( ele.getName() );
                instanceGroup.add( button );
                add( button );
            }
            
            // Select the radio first button of the group.
            Enumeration<AbstractButton> buttons = instanceGroup.getElements();
            buttons.nextElement().setSelected( true );
            
            Border  border  = BorderFactory.createLineBorder( Color.BLACK );
            Border  titled  = 
                BorderFactory.createTitledBorder( border, "Object" );
            setBorder( titled );
        }
    }
    
    /**
     * Encapsulates the list of threads
     * to be displayed in this GUI.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private class ThreadGroup extends JPanel
    {
        /**
         *  Controls the number of threads created,
         *  and the name of each.
         */
        private static final String[]   names   =
        {
            "Thread 1",
            "Thread 2",
            "Thread 3",
            "Thread 4",
        };
        
        /**
         * Constructor.
         */
        public ThreadGroup()
        {
            super( new GridLayout( names.length, 1 ) );
            for ( String name : names )
            {
                JRadioButton    button  = new JRadioButton( name );
                threadGroup.add( button );
                add( button );
                
                Thread  thread  = new Thread( () -> doRun(), name );
                threadMap.put( name, thread );
                thread.start();
            }
            
            // Select the first button in this list.
            Enumeration<AbstractButton> buttons = threadGroup.getElements();
            buttons.nextElement().setSelected( true );
            
            Border  border  = BorderFactory.createLineBorder( Color.BLACK );
            Border  titled  = 
                BorderFactory.createTitledBorder( border, "Thread" );
            setBorder( titled );
        }
    }
    
    /**
     * Encapsulates the buttons
     * that are used to drive
     * this GUI.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private class ButtonPanel extends JPanel
    {
        /**
         * Constructor.
         */
        public ButtonPanel()
        {
            JButton exec    = new JButton( "Execute" );
            JButton exit    = new JButton( "Exit" );
            
            add( exec );
            add( exit );
            
            // Note: "this" refers to this instance of the ButtonPanel,
            // which is an inner class nested inside of the Controller class...
            // ... "Controller.this" refers to the instance of the parent
            // this ButtonPanel is linked to.
            exit.addActionListener( Controller.this::exit );
            exec.addActionListener( Controller.this::exec );
        }
    }
}
